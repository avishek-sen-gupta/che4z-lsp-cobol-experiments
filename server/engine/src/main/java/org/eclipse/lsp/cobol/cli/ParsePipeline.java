package org.eclipse.lsp.cobol.cli;

import com.google.common.collect.ImmutableList;
import com.google.gson.*;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eclipse.lsp.cobol.cli.di.CliModule;
import org.eclipse.lsp.cobol.cli.modules.CliClientProvider;
import org.eclipse.lsp.cobol.common.AnalysisConfig;
import org.eclipse.lsp.cobol.common.ResultWithErrors;
import org.eclipse.lsp.cobol.common.SubroutineService;
import org.eclipse.lsp.cobol.common.benchmark.BenchmarkService;
import org.eclipse.lsp.cobol.common.benchmark.BenchmarkSession;
import org.eclipse.lsp.cobol.common.benchmark.Measurement;
import org.eclipse.lsp.cobol.common.copybook.CopybookProcessingMode;
import org.eclipse.lsp.cobol.common.error.SyntaxError;
import org.eclipse.lsp.cobol.common.mapping.ExtendedDocument;
import org.eclipse.lsp.cobol.common.mapping.ExtendedText;
import org.eclipse.lsp.cobol.common.message.MessageService;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.eclipse.lsp.cobol.core.engine.analysis.AnalysisContext;
import org.eclipse.lsp.cobol.core.engine.dialects.DialectService;
import org.eclipse.lsp.cobol.core.engine.pipeline.Pipeline;
import org.eclipse.lsp.cobol.core.engine.pipeline.PipelineResult;
import org.eclipse.lsp.cobol.core.engine.pipeline.StageResult;
import org.eclipse.lsp.cobol.core.engine.pipeline.stages.*;
import org.eclipse.lsp.cobol.core.engine.processor.AstProcessor;
import org.eclipse.lsp.cobol.core.engine.symbols.SymbolsRepository;
import org.eclipse.lsp.cobol.core.preprocessor.TextPreprocessor;
import org.eclipse.lsp.cobol.core.preprocessor.delegates.GrammarPreprocessor;
import org.eclipse.lsp.cobol.service.settings.CachingConfigurationService;
import org.eclipse.lsp.cobol.service.settings.layout.CodeLayoutStore;
import poc.common.flowchart.FlowchartBuilder;
import poc.common.flowchart.PocOps;
import org.poc.common.navigation.CobolEntityNavigator;
import org.poc.common.navigation.EntityNavigatorBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ParsePipeline {
    private File src;
    private File[] cpyPaths;
    private String[] cpyExt;
    private String idmsParseTreeOutputPath;
    private String cobolParseTreeOutputPath;
    private final PocOps ops;
    @Getter
    private CobolEntityNavigator navigator;


    public ParsePipeline(File src, File[] cpyPaths, String cobolParseTreeOutputPath, String idmsParseTreeOutputPath, PocOps ops) {
        this.src = src;
        this.cpyPaths = cpyPaths;
        this.idmsParseTreeOutputPath = idmsParseTreeOutputPath;
        this.cobolParseTreeOutputPath = cobolParseTreeOutputPath;
        this.ops = ops;
        cpyExt = new String[]{"", ".cpy"};
    }

    public CobolEntityNavigator parse() throws IOException {
        Injector diCtx = Guice.createInjector(new CliModule());
        Pipeline pipeline = setupPipeline(diCtx);

        CliClientProvider cliClientProvider = diCtx.getInstance(CliClientProvider.class);
        if (cpyPaths != null) {
            cliClientProvider.setCpyPaths(Arrays.asList(cpyPaths));
        }
        cliClientProvider.setCpyExt(Arrays.asList(cpyExt));

        // Cleaning up
        TextPreprocessor preprocessor = diCtx.getInstance(TextPreprocessor.class);
        BenchmarkService benchmarkService = diCtx.getInstance(BenchmarkService.class);

        if (src == null) {
            LOG.error("src must be provided");
            throw new RuntimeException("src must be provided");
        }

        if (cobolParseTreeOutputPath == null) {
            LOG.error("cobolParseTreeOutputPath must be provided");
            throw new RuntimeException("cobolParseTreeOutputPath must be provided");
        }

        if (idmsParseTreeOutputPath == null) {
            LOG.error("idmsParseTreeOutputPath must be provided");
            throw new RuntimeException("idmsParseTreeOutputPath must be provided");
        }

        String documentUri = src.toURI().toString();
        String text = new String(Files.readAllBytes(src.toPath()));

        ResultWithErrors<ExtendedText> resultWithErrors = preprocessor.cleanUpCode(documentUri, text);
        AnalysisContext ctx =
                new AnalysisContext(
                        new ExtendedDocument(resultWithErrors.getResult(), text),
                        createAnalysisConfiguration(),
                        benchmarkService.startSession(),
                        cobolParseTreeOutputPath, idmsParseTreeOutputPath);
        ctx.getAccumulatedErrors().addAll(resultWithErrors.getErrors());
        PipelineResult pipelineResult = pipeline.run(ctx);
        Gson gson = new GsonBuilder().setPrettyPrinting().addSerializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                return "recognizer".equals(fieldAttributes.getName()) || "children".equals(fieldAttributes.getName())
                        || "exception".equals(fieldAttributes.getName())
                        || "ctx".equals(fieldAttributes.getName())
                        || "contextUsages".equals(fieldAttributes.getName());
            }

            @Override
            public boolean shouldSkipClass(Class<?> aClass) {
                return RecognitionException.class.equals(aClass);
            }
        }).create();
        JsonObject result = new JsonObject();
        addTiming(result, ctx.getBenchmarkSession());
        StageResult<ProcessingResult> analysisResult =
                (StageResult<ProcessingResult>) pipelineResult.getLastStageResult();
        ParserRuleContext tree = analysisResult.getData().getTree();
        ParseTreeWalker walker = new ParseTreeWalker();
        DialectIntegratorListener dialectIntegrationListener = new DialectIntegratorListener();
        walker.walk(dialectIntegrationListener, tree);
        System.out.println("[INFO] Restored " + dialectIntegrationListener.getRestores() + " nodes.");
        System.out.println("Building tree...");

        ops.getVisualiser().visualiseCobolAST(tree, cobolParseTreeOutputPath, false, navigator);
        System.out.println("Built tree");
        EntityNavigatorBuilder navigatorBuilder = ops.getCobolEntityNavigatorBuilder();
        CobolParser.ProcedureDivisionBodyContext procedureDivisionBody = navigatorBuilder.procedureDivisionBody(tree);
        navigator = navigatorBuilder.procedureDivisionEntityNavigator(procedureDivisionBody);
        //        new DynamicFlowAnalyser(tree).run();

        JsonArray diagnostics = new JsonArray();
        ctx.getAccumulatedErrors()
                .forEach(
                        err -> {
                            JsonObject diagnostic = toJson(err, gson);
                            diagnostics.add(diagnostic);
                        });
        //        result.add("diagnostics", diagnostics);
        System.out.println(gson.toJson(result));
        ProcessingResult data = (ProcessingResult) pipelineResult.getLastStageResult().getData();
        return navigator;
    }

    public FlowchartBuilder buildFlowchartSpec(ParseTree root, int maxLevel, String dotFilePath) {
        FlowchartBuilder flowchartBuilder = ops.getFlowchartBuilderFactory().apply(navigator);
        return flowchartBuilder.draw(ImmutableList.of(root), maxLevel);
    }

    public FlowchartBuilder buildFlowchartSpec(List<ParseTree> roots, int maxLevel, String dotFilePath) {
        FlowchartBuilder flowchartBuilder = ops.getFlowchartBuilderFactory().apply(navigator);
        return flowchartBuilder.draw(roots, maxLevel);
    }

    public FlowchartBuilder flowcharter() {
        return ops.getFlowchartBuilderFactory().apply(navigator);
    }

    private static Pipeline setupPipeline(Injector diCtx) {
        DialectService dialectService = diCtx.getInstance(DialectService.class);
        MessageService messageService = diCtx.getInstance(MessageService.class);
        GrammarPreprocessor grammarPreprocessor = diCtx.getInstance(GrammarPreprocessor.class);
        ParseTreeListener parseTreeListener = diCtx.getInstance(ParseTreeListener.class);
        SymbolsRepository symbolsRepository = diCtx.getInstance(SymbolsRepository.class);
        SubroutineService subroutineService = diCtx.getInstance(SubroutineService.class);
        CachingConfigurationService cachingConfigurationService =
                diCtx.getInstance(CachingConfigurationService.class);
        AstProcessor astProcessor = diCtx.getInstance(AstProcessor.class);
        CodeLayoutStore layoutStore = diCtx.getInstance(CodeLayoutStore.class);

        Pipeline pipeline = new Pipeline();
        pipeline.add(new DialectCompilerDirectiveStage(dialectService));
        pipeline.add(new CompilerDirectivesStage(messageService));
        pipeline.add(new DialectProcessingStage(dialectService));
        pipeline.add(new PreprocessorStage(grammarPreprocessor));
        pipeline.add(new ImplicitDialectProcessingStage(dialectService));
        pipeline.add(new ParserStage(messageService, parseTreeListener));
        pipeline.add(
                new TransformTreeStage(
                        symbolsRepository,
                        messageService,
                        subroutineService,
                        cachingConfigurationService,
                        dialectService,
                        astProcessor,
                        layoutStore));
        return pipeline;
    }

    private void addTiming(JsonObject result, BenchmarkSession benchmarkSession) {
        JsonObject tObj = new JsonObject();
        benchmarkSession.getMeasurements()
                .forEach(m -> tObj.add(m.getId(), new JsonPrimitive(m.getTime() / 1_000_000_000.0)));
        result.add("timings", tObj);
        benchmarkSession.getMeasurements().stream()
                .map(Measurement::getTime)
                .reduce(Long::sum)
                .ifPresent(totalTime -> tObj.add("total", new JsonPrimitive(totalTime / 1_000_000_000.0)));
    }

    private static AnalysisConfig createAnalysisConfiguration() {
        return AnalysisConfig.defaultConfig(CopybookProcessingMode.ENABLED);
    }

    private JsonObject toJson(SyntaxError syntaxError, Gson gson) {
        JsonObject diagnostic = new JsonObject();
        Optional.ofNullable(syntaxError.getErrorCode())
                .ifPresent(code -> diagnostic.add("code", new JsonPrimitive(code.getLabel())));
        Optional.ofNullable(syntaxError.getErrorSource())
                .ifPresent(es -> diagnostic.add("source", new JsonPrimitive(es.getText())));
        Optional.ofNullable(syntaxError.getLocation())
                .ifPresent(l -> diagnostic.add("location", gson.toJsonTree(l)));
        Optional.ofNullable(syntaxError.getSeverity())
                .ifPresent(s -> diagnostic.add("severity", new JsonPrimitive(s.name())));
        Optional.ofNullable(syntaxError.getSuggestion())
                .ifPresent(s -> diagnostic.add("suggestion", new JsonPrimitive(s)));
        Optional.ofNullable(syntaxError.getRelatedInformation())
                .ifPresent(ri -> diagnostic.add("related", gson.toJsonTree(ri)));
        return diagnostic;
    }
}
