package intellilang;

import com.intellij.psi.LiteralTextEscaper;
import com.intellij.testFramework.UsefulTestCase;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;

public class PerlHeredocEscaperTest extends PerlHeredocInjectionTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/intellilang/perl/escaper";
  }

  public void testUnindentableEmpty() { doTest();}

  public void testUnindentableOneliner() { doTest();}

  public void testUnindentableMultiliner() { doTest();}

  public void testUnindentableWithInterpolation() { doTest();}

  public void testUnindentedEmpty() { doTest();}

  public void testUnindentedOneliner() { doTest();}

  public void testUnindentedMultiliner() { doTest();}

  public void testUnindentedWithInterpolation() { doTest();}

  public void testProperlyIndentedEmpty() { doTest();}

  public void testProperlyIndentedOneliner() { doTest();}

  public void testProperlyIndentedMultiliner() { doTest();}

  public void testProperlyIndentedMultilinerWithNewLines() { doTest();}

  public void testProperlyIndentedWithInterpolation() { doTest();}

  public void testImproperlyIndentedOneliner() { doTest();}

  public void testImproperlyIndentedMultiliner() { doTest();}

  public void testImproperlyIndentedMultilinerWithNewLines() { doTest();}

  public void testImproperlyIndentedWithInterpolation() { doTest();}

  private void doTest() {
    initWithFileSmart();
    PerlHeredocElementImpl heredocElement = getHeredocUnderCursor();
    LiteralTextEscaper<PerlHeredocElementImpl> escaper = heredocElement.createLiteralTextEscaper();
    StringBuilder sb = new StringBuilder();
    boolean r = escaper.decode(escaper.getRelevantTextRange(), sb);
    assert r;
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
  }
}
