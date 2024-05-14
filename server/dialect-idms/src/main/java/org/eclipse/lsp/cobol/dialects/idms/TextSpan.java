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
package org.eclipse.lsp.cobol.dialects.idms;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.MessageFormat;

/**
 * Represents a block of text
 */
public class TextSpan {
    @Expose
    @SerializedName("startLine")
    private int startLine;
    @Expose
    @SerializedName("stopLine")
    private int stopLine;
    @Expose
    @SerializedName("startColumn")
    private int startColumn;
    @Expose
    @SerializedName("stopColumn")
    private int stopColumn;

    public TextSpan(int startLine, int stopLine, int startColumn, int stopColumn) {
        this.startLine = startLine;
        this.stopLine = stopLine;
        this.startColumn = startColumn;
        this.stopColumn = stopColumn;
    }

    /**
     * String representation of text block
     * @return String
     */
    public String content() {
        return MessageFormat.format("[{0}, {1}] - [{2}, {3}]", startLine, startColumn, stopLine, stopColumn);
    }
}
