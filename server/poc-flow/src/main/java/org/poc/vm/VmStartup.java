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
package org.poc.vm;

import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.lsp.cobol.cli.ParsePipeline;
import org.poc.analysis.visualisation.CobolTreeVisualiserImpl;
import org.poc.analysis.visualisation.PocOpsImpl;
import org.poc.common.navigation.CobolEntityNavigator;
import org.poc.flowchart.FlowchartBuilderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import poc.common.flowchart.*;

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
        DataStructure dataStructures = pipeline.getDataStructures();

        // This one is root
        ParseTree procedure = navigator.procedureBodyRoot();

        flowcharter.buildChartAST(procedure).buildControlFlow();
        ChartNode root = flowcharter.getRoot();
        ChartNodeService nodeService = flowcharter.getChartNodeService();

        dataStructures.report();
        System.out.println("INTERPRETING\n--------------------------------\n");
        root.acceptInterpreter(CobolInterpreterFactory.interpreter(ConditionResolver.ALWAYS_TRUE), nodeService, FlowControl::CONTINUE);
    }
}
