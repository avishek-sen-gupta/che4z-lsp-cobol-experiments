/*
 * Copyright (c) 2022 DAF Trucks NV.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * DAF Trucks NV – implementation of DaCo COBOL statements
 * and DAF development standards
 */
package org.eclipse.lsp.cobol.dialects.daco.usecases;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.eclipse.lsp.cobol.common.error.ErrorSource;
import org.eclipse.lsp.cobol.dialects.daco.utils.DialectConfigs;
import org.eclipse.lsp.cobol.test.engine.UseCaseEngine;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.DiagnosticSeverity;
import org.eclipse.lsp4j.Range;
import org.junit.jupiter.api.Test;

/**
 * Tests the DaCo ROW Add statement
 */
class TestDaCoTableGetStatement {

    private static final String TEXT =
            "        IDENTIFICATION DIVISION. \r\n"
                    + "        PROGRAM-ID. test1. \r\n"
                    + "        ENVIRONMENT DIVISION.\n"
                    + "        IDMS-CONTROL SECTION.\n"
                    + "            PROTOCOL. MODE ABC.\n"
                    + "            IDMS-RECORDS MANUAL\n"
                    + "        DATA DIVISION. \r\n"
                    + "        WORKING-STORAGE SECTION. \r\n"
                    + "        PROCEDURE DIVISION. \r\n"
                    + "            GET TABLE ANY GACP. \r\n"
                    + "            GET TABLE SEQ GACP. \r\n"
                    // Negative tests
                    + "            GET TABLE ANY {GA|1}. \r\n"
                    + "            GET TABLE SEQ {GA|1}. \r\n"
                    + "            GET TABLE ANY {GAFSD|1}. \r\n"
                    + "            GET TABLE SEQ {GAFSD|1}. \r\n";

    @Test
    void test() {

        UseCaseEngine.runTest(
                TEXT,
                ImmutableList.of(),
                ImmutableMap.of(
                        "1",
                        new Diagnostic(
                                new Range(),
                                "Exact length of table reference must be 4 bytes",
                                DiagnosticSeverity.Error,
                                ErrorSource.DIALECT.getText())),
                ImmutableList.of(),
                DialectConfigs.getDaCoAnalysisConfig());
    }
}
