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

import org.eclipse.lsp.cobol.common.model.tree.variable.VariableNode;

/**
 * TestKv
 */
public class TestKv {
    private String key;
    private VariableNode value;

    public TestKv(String key, VariableNode value) {
        this.key = key;
        this.value = value;
    }
}
