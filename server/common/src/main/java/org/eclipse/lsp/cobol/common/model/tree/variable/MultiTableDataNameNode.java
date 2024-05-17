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
package org.eclipse.lsp.cobol.common.model.tree.variable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.lsp.cobol.common.model.Locality;

/**
 * This value class represents the multi-dimensional Table variable that may have nested variables,
 * and an optional index
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MultiTableDataNameNode extends VariableWithLevelNode implements UsageClause {
  private final OccursClause occursClause;
  private final UsageFormat usageFormat;

  public MultiTableDataNameNode(
          Locality location,
          int level,
          String name,
          boolean redefines,
          OccursClause occursClause,
          UsageFormat usageFormat,
          boolean global, ParserRuleContext ctx) {
    super(location, level, name, redefines, VariableType.MULTI_TABLE_DATA_NAME, global, ctx);
    this.occursClause = occursClause;
    this.usageFormat = usageFormat;
  }

  @Override
  protected String getVariableDisplayString() {
    StringBuilder stringBuilder = new StringBuilder(getFormattedSuffix());
    stringBuilder.append(String.format(" OCCURS %1$d TIMES", occursClause.getFrom()));
    if (usageFormat != UsageFormat.UNDEFINED) stringBuilder.append(" USAGE ").append(usageFormat.toDisplayString());
    return stringBuilder.append(".").toString();
  }
}
