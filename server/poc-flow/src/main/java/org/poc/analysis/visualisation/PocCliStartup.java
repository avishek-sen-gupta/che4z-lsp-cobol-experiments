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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PocCliStartup {
    public static boolean cliMode = false;
    private static final Integer LSP_PORT = 1044;
    private static final String PIPE_ARG = "pipeEnabled";
    private final Logger logger = LoggerFactory.getLogger(PocCliStartup.class);

    public static void main(String[] args) throws IOException, InterruptedException {
//        FlowchartTasks.singleFlowchartDemo();
        FlowchartTasks.allSectionsSingleProgramFlowchartDemo();
    }
}
