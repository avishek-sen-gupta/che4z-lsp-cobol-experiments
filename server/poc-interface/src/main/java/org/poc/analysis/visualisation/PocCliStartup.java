/*
 * Copyright (c) 2020 Broadcom.
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
package org.poc.analysis.visualisation;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.cli.ParsePipeline;
import poc.common.flowchart.ChartNodeTransformRules;
import poc.common.flowchart.FlowchartBuilder;
import org.poc.common.navigation.CobolEntityNavigator;
import org.poc.flowchart.FlowchartBuilderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poc.common.flowchart.GraphGenerator;
import vm.CobolEntityNavigatorBuilderImpl;

import java.io.File;
import java.io.IOException;

/**
 * This class is an entry point for the application. It initializes the DI context and runs the
 * server to accept the connections using either socket on LSP_PORT or pipes using STDIO. After the
 * establishing of the connection the main thread suspends until it is stopped forcibly.
 *
 * <p>To run the extension using path, you may specify "pipeEnabled" as a program argument. In other
 * case the server will start using socket.
 */
public class PocCliStartup {
    public static boolean cliMode = false;
    private static final Integer LSP_PORT = 1044;
    private static final String PIPE_ARG = "pipeEnabled";
    // It's need to be static, so it will be initialized after cliMode mode calculated (it is used in logger setup)
    private final Logger logger = LoggerFactory.getLogger(PocCliStartup.class);

    /**
     * The entry point for the application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException {
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

//        ParseTree u204 = navigator.target("U204-CALL-COST-PRICE");
        ParseTree u204 = navigator.target("R43-PUT");
        ParseTree k0A = navigator.target("K0A");
        ParseTree k1 = navigator.target("K1");
        ParseTree b2 = navigator.target("B2");
        ParseTree b2d = navigator.target("B2D");

//        flowcharter.draw(k0A).draw(k1).draw(b2).draw(u204);
//        flowcharter.outline(b2d, "SOME RANDOM STUFF");
        ChartNodeTransformRules rules = new ChartNodeTransformRules();

        flowcharter.buildAST(u204);
//        flowcharter.compress(u204, rules);
        flowcharter.draw(u204);
        flowcharter.write(dotFilePath);
        flowcharter.write(dotFilePath);
        new GraphGenerator().generateGraph(dotFilePath, graphOutputPath);
    }
}
