/*
 * Copyright (c) 2023 Broadcom.
 * The term "Broadcom" refers to Broadcom Inc. and/or its subsidiaries.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Broadcom, Inc. - initial API and implementation
 *
 */
package org.eclipse.lsp.cobol.cli;

import com.google.common.collect.Multimap;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Guice;
import com.google.inject.Injector;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.GraphvizCmdLineEngine;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.eclipse.lsp.cobol.cli.di.CliModule;
import org.eclipse.lsp.cobol.cli.flowchart.ChartNode;
import org.eclipse.lsp.cobol.cli.flowchart.FlowchartBuilder;
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
import org.eclipse.lsp.cobol.common.model.tree.CodeBlockDefinitionNode;
import org.eclipse.lsp.cobol.common.model.tree.Node;
import org.eclipse.lsp.cobol.common.model.tree.variable.VariableNode;
import org.eclipse.lsp.cobol.common.symbols.CodeBlockReference;
import org.eclipse.lsp.cobol.common.symbols.SymbolTable;
import org.eclipse.lsp.cobol.core.PanelDefinitionParser;
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
import org.eclipse.lsp.cobol.core.semantics.CopybooksRepository;
import org.eclipse.lsp.cobol.service.settings.CachingConfigurationService;
import org.eclipse.lsp.cobol.service.settings.layout.CodeLayoutStore;
import org.eclipse.lsp.cobol.visualisation.CobolTreeVisualiser;
import org.eclipse.lsp4j.Location;
import picocli.CommandLine;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * The Cli class represents a Command Line Interface (CLI) for interacting with the application.
 */
@CommandLine.Command(description = "COBOL Analysis CLI tools.")
@Slf4j
public class Cli implements Callable<Integer> {
  private enum Action {
    list_copybooks,
    analysis,
    parsemap
  }

  @CommandLine.Parameters(description = "Values: ${COMPLETION-CANDIDATES}")
  Action action = null;

  @CommandLine.Option(
      names = {"-s", "--source"},
      required = true,
      description = "The COBOL program file.")
  private File src;

  @CommandLine.Option(
      names = {"-cf", "--copybook-folder"},
      description = "Path to the copybook folder.")
  private File[] cpyPaths;

  @CommandLine.Option(
          names = {"-ce", "--copybook-extension"},
          description = "List of copybook paths.")
    private String[] cpyExt = {"", ".cpy"};

  @CommandLine.Option(
      names = {"-oidms", "--idms-output"},
      description = "Output path for IDMS trees.")
  private String idmsParseTreeOutputPath;

  @CommandLine.Option(
      names = {"-ocobol", "--cobol-output"},
      description = "Output path for Cobol parse tree.")
  private String cobolParseTreeOutputPath;

  /**
   * Prints the file name to the console and returns result code.
   *
   * @return 0 indicating success.
   * @throws Exception if an error occurs during the method execution.
   */
  @Override
  public Integer call() throws Exception {
    Injector diCtx = Guice.createInjector(new CliModule());
    Pipeline pipeline = setupPipeline(diCtx, action);

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
      return 1;
    }

    if (cobolParseTreeOutputPath == null) {
        LOG.error("cobolParseTreeOutputPath must be provided");
        return 1;
    }

    if (idmsParseTreeOutputPath == null) {
        LOG.error("idmsParseTreeOutputPath must be provided");
        return 1;
    }

    String documentUri = src.toURI().toString();
    String text = new String(Files.readAllBytes(src.toPath()));

    if (action == Action.parsemap) {
        AnalysisContext ctx =
                new AnalysisContext(
                        new ExtendedDocument(text, ""),
                        createAnalysisConfiguration(),
                        benchmarkService.startSession(),
                        cobolParseTreeOutputPath, idmsParseTreeOutputPath);
        PipelineResult pipelineResult = pipeline.run(ctx);

        StageResult<IdmsPanelDefinitionParserStageResult> idmsAnalysisResult =
                (StageResult<IdmsPanelDefinitionParserStageResult>) pipelineResult.getLastStageResult();
        PanelDefinitionParser.StartRuleContext tree = idmsAnalysisResult.getData().getTree();
        new CobolTreeVisualiser().visualiseCobolAST(tree, "/Users/asgupta/Downloads/mbrdi-poc/test-map.json");
        return 0;
    }
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
    switch (action) {
        case analysis:
        StageResult<ProcessingResult> analysisResult =
            (StageResult<ProcessingResult>) pipelineResult.getLastStageResult();
        ParserRuleContext tree = analysisResult.getData().getTree();
//        new CobolTreeVisualiser().visualiseCobolAST(tree, cobolParseTreeOutputPath);
//        new DynamicFlowAnalyser(tree).run();
            Graphviz.useEngine(new GraphvizCmdLineEngine().timeout(5, TimeUnit.HOURS));
            ChartNode flowchart = new FlowchartBuilder(tree).run();

            JsonArray diagnostics = new JsonArray();
        ctx.getAccumulatedErrors()
            .forEach(
                err -> {
                  JsonObject diagnostic = toJson(err, gson);
                  diagnostics.add(diagnostic);
                });
        //        result.add("diagnostics", diagnostics);
        break;
      case list_copybooks:
        StageResult<CopybooksRepository> copybooksResult =
            (StageResult<CopybooksRepository>) pipelineResult.getLastStageResult();
        Multimap<String, String> definitions = copybooksResult.getData().getDefinitions();
        Multimap<String, Location> usages = copybooksResult.getData().getUsages();
        Set<String> missing = new HashSet<>(usages.keySet());
        missing.removeAll(definitions.keySet());

        JsonArray copybookUris = new JsonArray();
        JsonArray missingCopybooks = new JsonArray();
        missing.forEach(missingCopybooks::add);
        definitions.values().forEach(copybookUris::add);
        result.add("copybookUris", copybookUris);
        result.add("missingCopybooks", missingCopybooks);
        break;
      default:
        break;
    }
    System.out.println(gson.toJson(result));
      ProcessingResult data = (ProcessingResult) pipelineResult.getLastStageResult().getData();
      Node rootNode = data.getRootNode();
//      new CobolTreeVisualiser().visualiseCobolAST(rootNode);
      SymbolTable symbolTable = (SymbolTable) data.getSymbolTableMap().values().toArray()[0];
      Type listType = new TypeToken<List<PlainKeyValuePair>>(){}.getType();
      Collection<Map.Entry<String, VariableNode>> variableMapEntries = symbolTable.getVariables().entries();
      Set<Map.Entry<String, CodeBlockReference>> sectionMapEntries = symbolTable.getSectionMap().entrySet();
      Set<Map.Entry<String, CodeBlockReference>> paragraphMapEntries = symbolTable.getParagraphMap().entrySet();
      List<CodeBlockDefinitionNode> codeBlockDefinitions = symbolTable.getCodeBlocks();

      String variableMaoAsString = gson.toJson(variableMapEntries.stream().map(PlainKeyValuePair::new).collect(Collectors.toList()));
      String sectionMapAsString = gson.toJson(sectionMapEntries.stream().map(PlainKeyValuePair::new).collect(Collectors.toList()));
      String paragraphMapAsString = gson.toJson(paragraphMapEntries.stream().map(PlainKeyValuePair::new).collect(Collectors.toList()));
      String codeBlockDefinitionsAsString = gson.toJson(codeBlockDefinitions);
      PrintWriter out1 = new PrintWriter("/Users/asgupta/Downloads/mbrdi-poc/variable-table.json");
      out1.println(variableMaoAsString);
      out1.close();

      PrintWriter out2 = new PrintWriter("/Users/asgupta/Downloads/mbrdi-poc/section-table.json");
      out2.println(sectionMapAsString);
      out2.close();

      PrintWriter out3 = new PrintWriter("/Users/asgupta/Downloads/mbrdi-poc/paragraph-table.json");
      out3.println(paragraphMapAsString);
      out3.close();

      PrintWriter out4 = new PrintWriter("/Users/asgupta/Downloads/mbrdi-poc/codeblock-table.json");
      out4.println(codeBlockDefinitionsAsString);
      out4.close();

      return 0;
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

  private static Pipeline setupPipeline(Injector diCtx, Action action) {
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

      if (action == Action.parsemap) {
          Pipeline pipeline = new Pipeline();
          pipeline.add(new PanelDefinitionProcessingStage(messageService, parseTreeListener));
          return pipeline;
      }

    Pipeline pipeline = new Pipeline();
    pipeline.add(new DialectCompilerDirectiveStage(dialectService));
    pipeline.add(new CompilerDirectivesStage(messageService));
    pipeline.add(new DialectProcessingStage(dialectService));
    pipeline.add(new PreprocessorStage(grammarPreprocessor));
    if (action == Action.analysis) {
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
    }
    return pipeline;
  }
}
