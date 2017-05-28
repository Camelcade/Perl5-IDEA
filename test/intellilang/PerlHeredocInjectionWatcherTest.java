package intellilang;

import base.PerlLightCodeInsightFixtureTestCase;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.LiteralTextEscaper;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.DebugUtil;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.EditorTestUtil;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Ignore;

import java.util.List;

/**
 * Created by hurricup on 28.05.2017.
 */
@Ignore
public class PerlHeredocInjectionWatcherTest extends PerlLightCodeInsightFixtureTestCase {

  private boolean myInterpolation;

  @Override
  protected String getTestDataPath() {
    return "testData/intellilang/perl/heredoc_watcher";
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    myInterpolation = PerlSharedSettings.getInstance(getProject()).ALLOW_INJECTIONS_WITH_INTERPOLATION;
    PerlSharedSettings.getInstance(getProject()).ALLOW_INJECTIONS_WITH_INTERPOLATION = true;
  }

  @Override
  protected void tearDown() throws Exception {
    PerlSharedSettings.getInstance(getProject()).ALLOW_INJECTIONS_WITH_INTERPOLATION = myInterpolation;
    super.tearDown();
  }

  public void testRemoveBodyDQBlockShift() {
    doTestDelete(false);
  }

  public void testRemoveBodyDQLineShift() {
    doTestDelete(false);
  }

  public void testShrinkBodyDQBlockShift() {
    doTestDelete(false);
  }

  public void testShrinkBodyDQLineShift() {
    doTestDelete(false);
  }


  public void testRemoveBodySQBlockShift() {
    doTestDelete(false);
  }

  public void testRemoveBodySQLineShift() {
    doTestDelete(false);
  }

  public void testShrinkBodySQBlockShift() {
    doTestDelete(false);
  }

  public void testShrinkBodySQLineShift() {
    doTestDelete(false);
  }

  public void testAddBodySQLineShift() {
    doTestTypeSpace(false);
  }

  public void testAddBodySQBlockShift() {
    doTestTypeSpace(false);
  }

  public void testAddBodyDQLineShift() {
    doTestTypeSpace(false);
  }

  public void testAddBodyDQBlockShift() {
    doTestTypeSpace(false);
  }

  public void testExtendBodySQLineShift() {
    doTestTypeSpace(false);
  }

  public void testExtendBodySQBlockShift() {
    doTestTypeSpace(false);
  }

  public void testExtendBodyDQLineShift() {
    doTestTypeSpace(false);
  }

  public void testExtendBodyDQBlockShift() {
    doTestTypeSpace(false);
  }

  public void testAddSpaceSQ() {
    doTestTypeSpace();
  }

  public void testAddSpaceSQWithoutEffect() {
    doTestTypeSpace();
  }

  public void testExtendSpaceSQ() {
    doTestTypeSpace();
  }

  public void testExtendSpaceSQWithoutEffect() {
    doTestTypeSpace();
  }

  public void testRemoveSpaceSQ() {
    doTestDelete();
  }

  public void testRemoveSpaceSQWithoutEffect() {
    doTestDelete();
  }

  public void testRemoveSpaceDQ() {
    doTestDelete();
  }

  public void testRemoveSpaceDQWithoutEffect() {
    doTestDelete();
  }

  public void testShrinkSpaceSQ() {
    doTestDelete();
  }

  public void testShrinkSpaceSQWithoutEffect() {
    doTestDelete();
  }

  public void testShrinkSpaceDQ() {
    doTestDelete();
  }

  public void testShrinkSpaceDQWithoutEffect() {
    doTestDelete();
  }

  private void doTestDelete() {doTestDelete(true);}

  private void doTestDelete(boolean toTheEnd) {
    doTest(toTheEnd, () -> EditorTestUtil.executeAction(getEditor(), IdeActions.ACTION_EDITOR_DELETE));
  }

  private void doTestTypeSpace() {doTestTypeSpace(true);}

  private void doTestTypeSpace(boolean toTheEnd) {
    doTest(toTheEnd, () -> myFixture.type(" "));
  }

  private void doTest(boolean toTheEnd, @NotNull Runnable mutator) {
    initWithFileSmart();
    PerlHeredocElementImpl heredocElement = assertHeredocIsConsistent();
    TextRange heredocRange = heredocElement.getTextRange();
    getEditor().getCaretModel().moveToOffset(toTheEnd ? heredocRange.getEndOffset() : heredocRange.getStartOffset());
    mutator.run();
    PsiDocumentManager.getInstance(getProject()).commitAllDocuments();
    assertFalse(
      "PsiFile contains error elements",
      DebugUtil.psiToString(getFile(), true, false).contains("PsiErrorElement")
    );
    assertHeredocIsConsistent();
  }

  private PerlHeredocElementImpl assertHeredocIsConsistent() {
    PerlHeredocElementImpl heredocElement = PsiTreeUtil.findChildOfType(getFile(), PerlHeredocElementImpl.class);
    assertNotNull(heredocElement);
    InjectedLanguageManager injectedLanguageManager = InjectedLanguageManager.getInstance(getProject());
    List<Pair<PsiElement, TextRange>> files = injectedLanguageManager.getInjectedPsiFiles(heredocElement);
    assertNotNull(files);
    assertTrue(files.size() == 1);
    Pair<PsiElement, TextRange> injection = files.get(0);
    String actual = injectedLanguageManager.getUnescapedText(injection.first);
    LiteralTextEscaper<PerlHeredocElementImpl> escaper = heredocElement.createLiteralTextEscaper();
    StringBuilder expected = new StringBuilder();
    escaper.decode(injection.second, expected);
    Assert.assertEquals(expected.toString(), actual);
    return heredocElement;
  }
}
