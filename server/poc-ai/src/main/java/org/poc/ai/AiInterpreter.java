package org.poc.ai;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.cli.ParsePipeline;
import org.poc.common.navigation.CobolEntityNavigator;
import poc.common.flowchart.FlowchartBuilder;
import poc.common.flowchart.GraphGenerator;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AiInterpreter {
    private final CobolEntityNavigator navigator;
    private final ParsePipeline pipeline;
    private HashMap<String, SymbolReferences> responseToReferences = new HashMap<>();
    private List<SymbolReferences> references;
    private FlowchartBuilder flowcharter;

    public AiInterpreter(ParsePipeline pipeline) {
        this.pipeline = pipeline;
        this.navigator = pipeline.getNavigator();
        this.flowcharter = pipeline.flowcharter();
    }

    public SymbolReferences references(String responseLine) {
        SymbolReferences allSymbols = new SymbolReferences(navigator);
        Pattern pattern = Pattern.compile("([A-Z0-9\\-]+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(responseLine);
        while (matcher.find()) {
            allSymbols.add(matcher.group());
        }

        responseToReferences.put(responseLine, allSymbols);
        return allSymbols;
    }

    public void extractReferences(List<String> responses) {
        references = responses.stream().map(this::references).toList();
        references.forEach(r -> {
            System.out.println(r);
        });
    }

    public void assemble() {
        references.forEach(r -> {
            r.getSymbols().forEach(s -> flowcharter.draw(s));
        });
    }


    public void write(String dotFilePath, String graphOutputPath) throws IOException, InterruptedException {
        for (Map.Entry<String, SymbolReferences> responseSymbolSet : responseToReferences.entrySet()) {
            SymbolReferences references = responseSymbolSet.getValue();
            for (ParseTree symbol: references.getSymbols())
                flowcharter.connectToComment(responseSymbolSet.getKey(), symbol);
        }
        flowcharter.write(dotFilePath);
        new GraphGenerator().generateGraph(dotFilePath, graphOutputPath);
    }
}
