/*
 * Copyright (c) 2021 Broadcom.
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
package org.eclipse.lsp.cobol.common.model.tree;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.eclipse.lsp.cobol.common.model.Locality;
import org.eclipse.lsp.cobol.common.model.NodeType;

/**
 * The class represents non-standard map statement in COBOL.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MapStatementNode extends Node {
    private final String label;

    public MapStatementNode(Locality location, String label) {
        super(location, NodeType.MAP_STATEMENT);
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
