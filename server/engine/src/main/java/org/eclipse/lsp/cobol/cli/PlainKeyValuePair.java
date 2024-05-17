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

import java.util.Map;

/**
 * TestKv
 */
public class PlainKeyValuePair<T> {
    private String key;
    private T value;

    public PlainKeyValuePair(String key, T value) {
        this.key = key;
        this.value = value;
    }

    public PlainKeyValuePair(Map.Entry<String, T> entry) {
        this(entry.getKey(), entry.getValue());
    }
}
