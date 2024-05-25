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

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.cli.ParsePipeline;
import org.eclipse.lsp.cobol.core.CobolParser;
import org.flowchart.FlowchartBuilder;
import org.flowchart.GraphGenerator;
import org.poc.common.navigation.CobolEntityNavigator;
import org.poc.flowchart.FlowchartBuilderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        CobolEntityNavigatorBuilderImpl navigatorBuilder = new CobolEntityNavigatorBuilderImpl();
        PocOpsImpl pocOps = new PocOpsImpl(new CobolTreeVisualiserImpl(), FlowchartBuilderImpl::build, navigatorBuilder);
        ParsePipeline pipeline = new ParsePipeline(new File("/Users/asgupta/Downloads/mbrdi-poc/V75234"),
                new File[]{new File("/Users/asgupta/Downloads/mbrdi-poc")},
                "/Users/asgupta/Downloads/mbrdi-poc/test-cobol.json",
                "/Users/asgupta/Downloads/mbrdi-poc/test-idms.json",
                pocOps);

        ParserRuleContext tree = pipeline.parse();
        CobolEntityNavigator navigator = pipeline.getNavigator();
//        ParseTree e0 = navigator.findTarget("U204-CALL-COST-PRICE");
        CobolParser.ProcedureDivisionBodyContext procedureDivisionBody = navigatorBuilder.procedureDivisionBody(tree);
        ParseTree k0A = navigator.findTarget("K0A");
        ParseTree k1 = navigator.findTarget("K1");
        ParseTree b2 = navigator.findTarget("B2");
        String dotFilePath = "/Users/asgupta/Downloads/mbrdi-poc/flowchart.dot";
        String graphOutputPath = "/Users/asgupta/Downloads/mbrdi-poc/flowchart.png";
        FlowchartBuilder flowcharter = pipeline.flowcharter();
        flowcharter.draw(k0A).draw(k1).draw(b2).write(dotFilePath);
//        pipeline.buildFlowchartSpec(ImmutableList.of(k0A, k1), -1, dotFilePath);
        new GraphGenerator().generateGraph(dotFilePath, graphOutputPath);
    }
}
