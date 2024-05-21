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
package org.copybook;

import picocli.CommandLine;

import java.io.IOException;

public class CopybookConstructor {
    public static void main(String[] args) throws IOException {
        int exitCode = new CommandLine(new CopybookConstructorCli()).execute(args);
        System.exit(exitCode);
    }
}
