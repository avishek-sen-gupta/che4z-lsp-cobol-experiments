package org.poc.analysis.visualisation;

import com.google.common.collect.ImmutableList;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.cli.ParsePipeline;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.poc.common.navigation.CobolEntityNavigator;
import org.poc.flowchart.FlowchartBuilderImpl;
import poc.common.flowchart.FlowchartBuilder;
import vm.CobolEntityNavigatorBuilderImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FlowchartTasks {
    private static final String AST_DIR = "ast";
    private static final String IMAGES_DIR = "images";
    private static final String DOTFILES_DIR = "dotfiles";

    public static void singleFlowchartDemo() throws IOException, InterruptedException {
        String dotFilePath = "/Users/asgupta/Downloads/mbrdi-poc/flowchart.dot";
        String imageOutputPath = "/Users/asgupta/Downloads/mbrdi-poc/flowchart.png";
        String cobolParseTreeOutputPath = "/Users/asgupta/Downloads/mbrdi-poc/test-cobol.json";
        String idmsParseTreeOutputPath = "/Users/asgupta/Downloads/mbrdi-poc/test-idms.json";
        File[] copyBookPaths = {new File("/Users/asgupta/Downloads/mbrdi-poc")};
        String dialectJarPath = "/Users/asgupta/code/mbrdi-proleap/che4z/che-che4z-lsp-for-cobol-2.1.2/server/dialect-idms/target/dialect-idms.jar";

//        File source = new File("/Users/asgupta/Downloads/mbrdi-poc/V75234");
//        File source = new File("/Users/asgupta/Downloads/mbrdi-poc/V7588049");
        File source = new File("/Users/asgupta/Downloads/mbrdi-poc/V751C931");
//        File source = new File("/Users/asgupta/Downloads/mbrdi-poc/test.cbl");

        PocOpsImpl ops = new PocOpsImpl(new CobolTreeVisualiserImpl(),
                FlowchartBuilderImpl::build, new CobolEntityNavigatorBuilderImpl());
        ParsePipeline pipeline = new ParsePipeline(source,
                copyBookPaths,
                dialectJarPath,
                cobolParseTreeOutputPath,
                idmsParseTreeOutputPath,
                ops);

        CobolEntityNavigator navigator = pipeline.parse();
        FlowchartBuilder flowcharter = pipeline.flowcharter();

        // This one demonstrates a moderately complex section
//        ParseTree procedure = navigator.target("U204-CALL-COST-PRICE");

        // This one demonstrates SEARCH...WHEN with NEXT SENTENCE
//        ParseTree procedure = navigator.target("B2");

        // This one demonstrates SEARCH statements with multiple SEARCH...WHEN clauses
//        ParseTree procedure = navigator.target("M2");

        // This one demonstrates SEARCH statements with multiple SEARCH...WHEN clauses (this is in V7588049)
//        ParseTree procedure = navigator.target("FORMAL-CHECK-CALC");

        // This one demonstrates ON clauses and SEARCH...WHEN with one Search...When condition
//        ParseTree procedure = navigator.target("A0");

        // This one demonstrates PERFORM X THRU Y
//        ParseTree procedure = navigator.target("S0");

        // This one demonstrates PERFORM VARYING for a procedure
//        ParseTree procedure = navigator.target("E0");

        // This one demonstrates PERFORM INLINE VARYING
//        ParseTree procedure = navigator.target("U2030-TASI-2603");

        // This one is root
        ParseTree procedure = navigator.root();

        flowcharter.generateFlowchart(procedure, dotFilePath, imageOutputPath, "ortho");
    }

    public static void allSectionsSingleProgramFlowchartDemo() throws IOException, InterruptedException {
        String sourceDir = "/Users/asgupta/Downloads/mbrdi-poc";
        File[] copyBookPaths = {new File("/Users/asgupta/Downloads/mbrdi-poc")};
        String dialectJarPath = "/Users/asgupta/code/mbrdi-proleap/che4z/che-che4z-lsp-for-cobol-2.1.2/server/dialect-idms/target/dialect-idms.jar";

        String dotFileRootDir = "/Users/asgupta/Downloads/mbrdi-poc/report/dotfiles";
        String imageOutputRootDir = "/Users/asgupta/Downloads/mbrdi-poc/report/images";
        String astOutputDir = "/Users/asgupta/Downloads/mbrdi-poc/report/ast";
        String reportRootDir = "/Users/asgupta/Downloads/mbrdi-poc/report";

//        File source = new File("/Users/asgupta/Downloads/mbrdi-poc/V75234");
//        File source = new File("/Users/asgupta/Downloads/mbrdi-poc/V7588049");
//        File source = new File("/Users/asgupta/Downloads/mbrdi-poc/V751C931");
//        File source = new File("/Users/asgupta/Downloads/mbrdi-poc/test.cbl");
        List<String> programNames = ImmutableList.of("V751C931", "V7588049", "V75234");
        for (String programName : programNames) {
            allSectionsGivenProgram(sourceDir, dotFileRootDir, imageOutputRootDir, copyBookPaths, dialectJarPath, programName, astOutputDir, reportRootDir);
        }
    }

    private static void allSectionsGivenProgram(String sourceDir, String dotFileRootDir, String imageOutputRootDir, File[] copyBookPaths, String dialectJarPath, String programName, String astOutputDirRoot, String reportRootDir) throws IOException, InterruptedException {
        File source = Paths.get(sourceDir, programName).toFile();
        Path astOutputDir = Paths.get(reportRootDir, programName, AST_DIR);
        Path imageOutputDir = Paths.get(reportRootDir, programName, IMAGES_DIR);
        Path dotFileOutputDir = Paths.get(reportRootDir, programName, DOTFILES_DIR);
        String cobolParseTreeOutputPath = astOutputDir.resolve(String.format("cobol-%s.json", programName)).toString();
        String idmsParseTreeOutputPath = astOutputDir.resolve(String.format("idms-%s.json", programName)).toString();

        Files.createDirectories(astOutputDir);
        Files.createDirectories(dotFileOutputDir);
        Files.createDirectories(imageOutputDir);

        PocOpsImpl ops = new PocOpsImpl(new CobolTreeVisualiserImpl(),
                FlowchartBuilderImpl::build, new CobolEntityNavigatorBuilderImpl());
        ParsePipeline pipeline = new ParsePipeline(source,
                copyBookPaths,
                dialectJarPath,
                cobolParseTreeOutputPath,
                idmsParseTreeOutputPath,
                ops);

        CobolEntityNavigator navigator = pipeline.parse();
        ParseTree root = navigator.root();
        List<ParseTree> allSections = navigator.findAllByCondition(n -> n.getClass() == CobolParser.ProcedureSectionContext.class, root);
        for (ParseTree section : allSections) {
            pipeline.flowcharter().generateFlowchart(section,
                    outputPath(section, dotFileOutputDir, "dot"),
                    outputPath(section, imageOutputDir, "png"), "ortho");
        }
    }

    private static String outputPath(ParseTree section, Path outputDir, String extension) {
        CobolParser.ProcedureSectionContext s = (CobolParser.ProcedureSectionContext) section;
        String sectionName = s.procedureSectionHeader().getText();
        return outputDir.resolve(String.format("%s.%s", sectionName, extension)).toString();
    }
}
