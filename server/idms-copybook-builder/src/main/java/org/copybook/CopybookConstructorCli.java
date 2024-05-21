package org.copybook;

import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.eclipse.lsp.cobol.common.error.SyntaxError;
import org.eclipse.lsp.cobol.dialects.idms.IdmsCopyLexer;
import org.eclipse.lsp.cobol.dialects.idms.IdmsCopyParser;
import org.eclipse.lsp.cobol.dialects.idms.visualisation.IdmsTreeVisualiser;
import picocli.CommandLine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@CommandLine.Command(description = "IDMS Copybook Parser target.")
@Slf4j
public class CopybookConstructorCli implements Callable<Integer> {
    @CommandLine.Parameters(description = "Values: ${COMPLETION-CANDIDATES}")
    CopybookAction action = null;

    @CommandLine.Option(
            names = {"-s", "--source"},
            required = true,
            description = "The copybook source path")
    private String src;

    @CommandLine.Option(
            names = {"-o", "--output"},
            required = true,
            description = "The copybook parse output path")
    private String outputPath;

    @Override
    public Integer call() throws Exception {
        if (src == null) {
            LOG.error("src must be provided");
            return 1;
        }
        if (outputPath == null) {
            LOG.error("outputPath must be provided");
            return 1;
        }

        if (action == CopybookAction.PARSE) {
            return doStuff(src, outputPath);
        }

        LOG.error("Unknown command: " + action);
        return 1;
    }

    private Integer doStuff(String sourcePath, String outputPath) throws IOException {
//        String programDocumentUri = "/Users/asgupta/Downloads/mbrdi-poc/V75CWEUR";
        String programDocumentUri = sourcePath;
        IdmsCopyLexer lexer = new IdmsCopyLexer(CharStreams.fromFileName(programDocumentUri));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        IdmsCopyParser parser = new IdmsCopyParser(tokens);
        ErrorListener listener = new ErrorListener(programDocumentUri);
        lexer.removeErrorListeners();
        lexer.addErrorListener(listener);
        parser.removeErrorListeners();
        parser.addErrorListener(listener);
//        parser.setErrorHandler(new CobolErrorStrategy(messageService));

        IdmsCopyParser.StartRuleContext result = parser.startRule();
        List<SyntaxError> errors = new ArrayList<>();
        errors.addAll(listener.getErrors());

        new IdmsCopyBookTreeVisualiser().visualiseIdmsAST(result, outputPath);
        if (!errors.isEmpty()) return 1;
        return 0;
    }
}
