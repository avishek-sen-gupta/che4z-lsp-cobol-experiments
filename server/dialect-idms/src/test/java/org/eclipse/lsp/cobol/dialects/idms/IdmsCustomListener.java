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

package org.eclipse.lsp.cobol.dialects.idms;

/** This test checks that Outline tree fot the program is constructed correctly */
public class IdmsCustomListener extends IdmsParserBaseListener {
    @Override
    public void enterTransferStatement(IdmsParser.TransferStatementContext ctx) {
//        super.enterTransferStatement(ctx);
//        System.out.println("Entered a transfer statement");
//        System.out.println(ctx.RETURN().getSymbol().getText());
//        System.out.println(ctx.USING().getSymbol().getText());
//        System.out.println(ctx.CONTROL().getSymbol().getText());
//        System.out.println(ctx.children);
    }

    @Override
    public void enterObtainStatement(IdmsParser.ObtainStatementContext ctx) {
        super.enterObtainStatement(ctx);
//        System.out.println("Entered Obtain statement");
//        System.out.println(ctx.findObtainClause().ownerClause().idms_db_entity_name());
    }

    @Override
    public void enterIfStatement(IdmsParser.IfStatementContext ctx) {
        super.enterIfStatement(ctx);
//        ctx.accept(new TestVisitor());
//        System.out.println(ctx.idmsIfCondition());
    }

    @Override
    public void enterIdmsIfCondition(IdmsParser.IdmsIfConditionContext ctx) {
        super.enterIdmsIfCondition(ctx);
    }

    @Override
    public void enterIdmsIfStatement(IdmsParser.IdmsIfStatementContext ctx) {
        super.enterIdmsIfStatement(ctx);
    }

    @Override
    public void exitIfStatement(IdmsParser.IfStatementContext ctx) {
        super.exitIfStatement(ctx);
    }
}
