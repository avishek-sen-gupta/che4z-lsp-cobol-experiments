/*
 * Copyright (c) 2022 Broadcom.
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
package org.eclipse.lsp.cobol.common.processor;

import org.eclipse.lsp.cobol.common.model.tree.Node;

import java.util.function.BiConsumer;

/**
 * AST Processor marker
 *
 * @param <T> a node type to process
 */
public interface Processor<T extends Node> extends BiConsumer<T, ProcessingContext> {}
