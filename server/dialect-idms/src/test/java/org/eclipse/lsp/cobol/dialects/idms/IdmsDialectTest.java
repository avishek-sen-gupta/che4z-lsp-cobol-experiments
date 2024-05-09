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
package org.eclipse.lsp.cobol.dialects.idms;

import org.eclipse.lsp.cobol.common.copybook.CopybookService;
import org.eclipse.lsp.cobol.common.message.MessageService;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Test for IdmsDialect class
 */
class IdmsDialectTest {
  @Test
  void testKeywords() {
    IdmsDialect dialect = new IdmsDialect(mock(CopybookService.class), mock(MessageService.class));
    Map<String, String> result = dialect.getKeywords();
    assertEquals(46, result.size());
  }

//  @Test
//    void testStuff() throws IOException {
//      CharStream cs = fromFileName("/Users/asgupta/Downloads/S24 Manual Price Corrections If defined tolerance limit exceeds among Vendor and SPICS DB prices in the invoice 2/V7523438.txt");
//      IdmsLexer idmsLexer = new IdmsLexer(cs);
//      CommonTokenStream tokens = new CommonTokenStream(idmsLexer);
//      IdmsParser idmsParser = new IdmsParser(tokens);
//      IdmsParser.StartRuleContext startRuleContext = idmsParser.startRule();
      final IdmsParserListener listener = new CustomIdmsParseTreeListener();
//      ParseTreeWalker walker = new ParseTreeWalker();
//      walker.walk(listener, startRuleContext);

//      CharStream cs = fromFileName("/Users/asgupta/Downloads/S24 Manual Price Corrections If defined tolerance limit exceeds among Vendor and SPICS DB prices in the invoice 2/V7523438.txt");
//      IdmsLexer cobolLexer = new CobolLexer(cs);
//      CommonTokenStream tokens = new CommonTokenStream(cobolLexer);
//      IdmsParser cobolParser = new IdmsParser(tokens);
//      IdmsParser.StartRuleContext startRuleContext = idmsParser.startRule();
//      final IdmsParserListener listener = new IdmsCustomListener();
//      ParseTreeWalker walker = new ParseTreeWalker();
//      walker.walk(listener, startRuleContext);
//
//  }
}
