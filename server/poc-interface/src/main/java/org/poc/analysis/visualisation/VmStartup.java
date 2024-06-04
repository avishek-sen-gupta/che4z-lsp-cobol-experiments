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
import org.poc.common.navigation.CobolEntityNavigator;
import org.poc.flowchart.FlowchartBuilderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poc.common.flowchart.ChartNode;
import poc.common.flowchart.ChartNodeService;
import org.poc.flowchart.SmolCobolInterpreter;
import poc.common.flowchart.FlowControl;
import poc.common.flowchart.FlowchartBuilder;
import vm.CobolEntityNavigatorBuilderImpl;

import java.io.File;
import java.io.IOException;

public class VmStartup {
    public static boolean cliMode = false;
    private static final Integer LSP_PORT = 1044;
    private static final String PIPE_ARG = "pipeEnabled";
    private final Logger logger = LoggerFactory.getLogger(VmStartup.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        String dotFilePath = "/Users/asgupta/Downloads/mbrdi-poc/flowchart.dot";
        String imageOutputPath = "/Users/asgupta/Downloads/mbrdi-poc/flowchart.png";
        String cobolParseTreeOutputPath = "/Users/asgupta/Downloads/mbrdi-poc/test-cobol.json";
        String idmsParseTreeOutputPath = "/Users/asgupta/Downloads/mbrdi-poc/test-idms.json";
        File[] copyBookPaths = {new File("/Users/asgupta/Downloads/mbrdi-poc")};
        String dialectJarPath = "/Users/asgupta/code/mbrdi-proleap/che4z/che-che4z-lsp-for-cobol-2.1.2/server/dialect-idms/target/dialect-idms.jar";

//        File source = new File("/Users/asgupta/Downloads/mbrdi-poc/V75234");
//        File source = new File("/Users/asgupta/Downloads/mbrdi-poc/V7588049");
//        File source = new File("/Users/asgupta/Downloads/mbrdi-poc/V751C931");
        File source = new File("/Users/asgupta/Downloads/mbrdi-poc/test.cbl");

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

        flowcharter.buildChartAST(procedure).buildControlFlow();
        ChartNode root = flowcharter.getRoot();
        ChartNodeService nodeService = flowcharter.getChartNodeService();

        System.out.println("INTERPRETING\n--------------------------------\n");
        root.acceptInterpreter(new SmolCobolInterpreter(), nodeService, FlowControl::CONTINUE);
    }
}
