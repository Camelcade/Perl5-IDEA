package editor;

import base.PerlLightTestCase;
import org.junit.Test;

public class PerlGotoShadowedVariableHandlerTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/gotoDeclaration/perl/shadowed";
  }

  @Test
  public void testShadowedScalar() {
    doTest();
  }

  @Test
  public void testShadowedScalarWrong() {
    doTest();
  }

  @Test
  public void testShadowedScalarMyLocal() {
    doTest();
  }

  @Test
  public void testShadowedScalarMyOur() {
    doTest();
  }

  @Test
  public void testShadowedScalarMyState() {
    doTest();
  }

  @Test
  public void testShadowedScalarOurMy() {
    doTest();
  }

  @Test
  public void testShadowedScalarOurOur() {
    doTest();
  }

  @Test
  public void testShadowedArray() {
    doTest();
  }

  @Test
  public void testShadowedHash() {
    doTest();
  }

  @Test
  public void testShadowedScalarReverse() {
    doTest();
  }

  @Test
  public void testShadowedArrayReverse() {
    doTest();
  }

  @Test
  public void testShadowedHashReverse() {
    doTest();
  }

  private void doTest() {
    doTestGoToDeclarationTargets();
  }
}
