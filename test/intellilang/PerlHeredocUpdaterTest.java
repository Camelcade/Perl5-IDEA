package intellilang;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.testFramework.UsefulTestCase;
import org.jetbrains.annotations.NotNull;

public class PerlHeredocUpdaterTest extends PerlHeredocInjectionTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/intellilang/perl/heredoc_updater";
  }

  public void testUnindentedUpdate() {
    doTestUnindentedUpdate("<html>\n" +
                           "<div>\n" +
                           "    <div>\n" +
                           "    </div>\n" +
                           "</div>\n" +
                           "</html>");
  }

  public void testUnindentedUpdateWithEmpty() {
    doTestUnindentedUpdate("");
  }

  public void testUnindentedUpdateWithNewLine() {
    doTestUnindentedUpdate("some text\n");
  }

  public void testUnindentedUpdateWithoutNewLine() {
    doTestUnindentedUpdate("some text");
  }

  public void testIndentedUpdate() {
    doTestIndentedUpdate("<html>\n" +
                         "<div>\n" +
                         "    <div>\n" +
                         "    </div>\n" +
                         "</div>\n" +
                         "</html>");
  }

  public void testIndentedUpdateWithEmpty() {
    doTestIndentedUpdate("");
  }

  public void testIndentedUpdateWithNewLine() {
    doTestIndentedUpdate("some text\n");
  }

  public void testIndentedUpdateWithoutNewLine() {
    doTestIndentedUpdate("some text");
  }

  private void doTestUnindentedUpdate(@NotNull String textToUpdate) {
    initWithTextSmart("{\n" +
                      "    <<EOM;\nbla<caret>bla\n" +
                      "EOM\n" +
                      "}\n");
    doTest(textToUpdate);
  }

  private void doTestIndentedUpdate(@NotNull String textToUpdate) {
    initWithTextSmart("{\n" +
                      "    <<~EOM;\nbla<caret>bla\n" +
                      "    EOM\n" +
                      "}\n");
    doTest(textToUpdate);
  }

  private void doTest(@NotNull String textToUpdate) {
    new WriteCommandAction.Simple(getProject()) {
      @Override
      protected void run() throws Throwable {
        getHeredocUnderCursor().updateText(textToUpdate);
      }
    }.execute().throwException();
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), getFile().getText());
  }
}
