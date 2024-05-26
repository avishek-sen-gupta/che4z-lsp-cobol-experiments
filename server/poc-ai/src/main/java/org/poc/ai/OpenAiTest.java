package org.poc.ai;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.cli.ParsePipeline;
import org.poc.analysis.visualisation.CobolTreeVisualiserImpl;
import org.poc.analysis.visualisation.PocOpsImpl;
import org.poc.common.navigation.CobolEntityNavigator;
import org.poc.flowchart.FlowchartBuilderImpl;
import poc.common.flowchart.CobolContextAugmentedTreeNode;
import poc.common.flowchart.FlowchartBuilder;
import vm.CobolEntityNavigatorBuilderImpl;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class OpenAiTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Working");
        Advisor advisor = new Advisor(System.getenv(Advisor.AZURE_OPENAI_API_KEY), System.getenv(Advisor.AZURE_OPENAI_ENDPOINT));

        String dotFilePath = "/Users/asgupta/Downloads/mbrdi-poc/flowchart.dot";
        String graphOutputPath = "/Users/asgupta/Downloads/mbrdi-poc/flowchart.png";
        String cobolParseTreeOutputPath = "/Users/asgupta/Downloads/mbrdi-poc/test-cobol.json";
        String idmsParseTreeOutputPath = "/Users/asgupta/Downloads/mbrdi-poc/test-idms.json";
        File[] copyBookPaths = {new File("/Users/asgupta/Downloads/mbrdi-poc")};
        File source = new File("/Users/asgupta/Downloads/mbrdi-poc/V75234");

        PocOpsImpl ops = new PocOpsImpl(new CobolTreeVisualiserImpl(),
                FlowchartBuilderImpl::build, new CobolEntityNavigatorBuilderImpl());
        ParsePipeline pipeline = new ParsePipeline(source,
                copyBookPaths,
                cobolParseTreeOutputPath,
                idmsParseTreeOutputPath,
                ops);

        CobolEntityNavigator navigator = pipeline.parse();
        FlowchartBuilder flowcharter = pipeline.flowcharter();
        ParseTree u204 = navigator.target("U204-CALL-COST-PRICE");
        String codeText = CobolContextAugmentedTreeNode.originalText(u204);
        System.out.println(codeText);

        PromptConstructor promptConstructor = new PromptConstructor();
        promptConstructor.addLine("Assume you are mainframe COBOL expert. The following lines contain a piece of code from a legacy codebase in COBOL. What do you think it does? Explain in as much detail as you can. Group your explanations by individual paragraphs, sections, and variables. When referring to a variable, section, or paragraph, enclose it in square brackets.");
//        promptConstructor.addLine(codeText);
        System.out.println("Processing your request...");
//        List<String> responses = advisor.advise(promptConstructor.getPrompt());

        List<String> responses = SampleResponses.ONE;

        AiInterpreter interpreter = new AiInterpreter(pipeline);
        interpreter.extractReferences(responses);
        interpreter.assemble();
        interpreter.write(dotFilePath, graphOutputPath);
        System.out.println("Complete");
    }
}
