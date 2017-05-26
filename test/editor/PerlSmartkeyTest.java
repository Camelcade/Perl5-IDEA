package editor;

import base.PerlLightCodeInsightFixtureTestCase;
import com.intellij.testFramework.UsefulTestCase;

public class PerlSmartkeyTest extends PerlLightCodeInsightFixtureTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/smartkey/perl";
  }

  public void testCommentMiddle() {
    doTestEnter();
  }

  public void testCommentMiddleIndented() {
    doTestEnter();
  }

  public void testHeredocCloseBare() { doTestEnter(); }

  public void testHeredocClosed() { doTestEnter(); }

  public void testHeredocCloseDQ() { doTestEnter(); }

  public void testHeredocCloseEscaped() { doTestEnter(); }

  public void testHeredocCloseSQ() { doTestEnter(); }

  public void testHeredocCloseWithIndentedAhead() { doTestEnter(); }

  public void testHeredocCloseWithNormalAhead() { doTestEnter(); }

  public void testHeredocCloseXQ() { doTestEnter(); }

  public void testIndentedHeredocCloseBare() { doTestEnter(); }

  public void testIndentedHeredocClosed() { doTestEnter(); }

  public void testIndentedHeredocCloseDQ() { doTestEnter(); }

  public void testIndentedHeredocCloseEscaped() { doTestEnter(); }

  public void testIndentedHeredocCloseSQ() { doTestEnter(); }

  public void testIndentedHeredocCloseWithIndentedAhead() { doTestEnter(); }

  public void testIndentedHeredocCloseWithNormalAhead() { doTestEnter(); }

  public void testIndentedHeredocCloseXQ() { doTestEnter(); }

  protected void doTestEnter() {
    doTest('\n');
  }

  private String getResultAppendix(char typed) {
    return ".typed#" + (int)typed;
  }

  protected void doTest(char typed) {
    initWithFileSmart();
    myFixture.type(typed);
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(getResultAppendix(typed)), getFile().getText());
  }
}
