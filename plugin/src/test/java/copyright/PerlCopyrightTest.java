/*
 * Copyright 2015-2025 Alexandr Evstigneev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package copyright;

import base.PerlLightTestCase;
import com.intellij.copyright.CopyrightManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.UsefulTestCase;
import com.maddyhome.idea.copyright.CopyrightProfile;
import com.maddyhome.idea.copyright.options.LanguageOptions;
import com.maddyhome.idea.copyright.psi.UpdateCopyright;
import com.maddyhome.idea.copyright.psi.UpdateCopyrightFactory;
import com.perl5.lang.perl.fileTypes.PerlFileTypePackage;
import com.sun.istack.NotNull;
import org.junit.Test;

import java.util.function.Consumer;

public class PerlCopyrightTest extends PerlLightTestCase {
  private LanguageOptions myLanguageOptions;

  @Test
  public void testAddBlockToPod() {
    blockWithBlankLine();
    doTest();
  }

  @Test
  public void testAddBlockToPodWithCut() {
    blockWithBlankLine();
    doTest();
  }

  @Test
  public void testReplaceBlockInPodWithBlock() {
    blockWithBlankLine();
    doTest();
  }

  @Test
  public void testReplaceBlockInPodWithBlockOnTop() {
    blockWithBlankLine();
    doTest();
  }

  @Test
  public void testReplaceBlockInPodWithLine() {
    lineWithoutBlankLine();
    doTest();
  }

  @Test
  public void testReplaceBlockInPodWithLineOnTop() {
    lineWithoutBlankLine();
    doTest();
  }

  @Test
  public void testReplaceLineWithBlockInPod() {
    blockWithoutBlankLine();
    doTest();
  }

  @Test
  public void testPackageNameVariable() {
    VirtualFile packageFile = myFixture.copyFileToProject("Bar.pm", "lib/Some/Foo/Bar.pm");
    markAsLibRoot(packageFile.getParent().getParent().getParent(), true);
    myFixture.configureFromExistingVirtualFile(packageFile);
    doTestWithoutInit("Copyright of the package ${perlfile.packageName}\n" +
                      "or ${file.className}");
  }

  @Test
  public void testAddLine() {
    lineWithBlankLine();
    doTest();
  }

  private void lineWithBlankLine() {
    modifyLanguageOptions(it -> {
      it.block = false;
      it.addBlankAfter = true;
    });
  }

  @Test
  public void testAddBlock() {
    blockWithBlankLine();
    doTest();
  }

  @Test
  public void testReplaceLineWithLine() {
    lineWithBlankLine();
    doTest();
  }

  @Test
  public void testReplaceLineWithBlock() {
    blockWithBlankLine();
    doTest();
  }

  @Test
  public void testReplaceBlockWithLine() {
    lineWithBlankLine();
    doTest();
  }

  @Test
  public void testReplaceBlockWithBlock() {
    blockWithBlankLine();
    doTest();
  }

  @Test
  public void testReplaceLineWithLineEmpty() {
    lineWithBlankLine();
    doTest();
  }

  @Test
  public void testReplaceLineWithBlockEmpty() {
    blockWithBlankLine();
    doTest();
  }

  @Test
  public void testReplaceBlockWithLineEmpty() {
    lineWithBlankLine();
    doTest();
  }

  @Test
  public void testReplaceBlockWithBlockEmpty() {
    blockWithBlankLine();
    doTest();
  }

  private void blockWithBlankLine() {
    modifyLanguageOptions(it -> {
      it.block = true;
      it.addBlankAfter = true;
    });
  }

  @Test
  public void testAddLineWithoutEmptyLine() {
    lineWithoutBlankLine();
    doTest();
  }

  private void lineWithoutBlankLine() {
    modifyLanguageOptions(it -> {
      it.block = false;
      it.addBlankAfter = false;
    });
  }

  @Test
  public void testAddBlockWithoutEmptyLine() {
    blockWithoutBlankLine();
    doTest();
  }

  private void blockWithoutBlankLine() {
    modifyLanguageOptions(it -> {
      it.block = true;
      it.addBlankAfter = false;
    });
  }

  @Test
  public void testAddLineEmpty() {
    modifyLanguageOptions(it -> it.block = false);
    doTest();
  }

  @Test
  public void testAddBlockEmpty() {
    modifyLanguageOptions(it -> it.block = true);
    doTest();
  }

  @Override
  protected String getBaseDataPath() {
    return "copyright/perl";
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    myLanguageOptions = getLanguageOptions();
  }

  private LanguageOptions getLanguageOptions() {
    return CopyrightManager.getInstance(getProject()).getOptions().getOptions(PerlFileTypePackage.INSTANCE.getName());
  }

  @Override
  protected void tearDown() throws Exception {
    try {
      setLanguageOptions(myLanguageOptions);
    }
    finally {
      super.tearDown();
    }
  }

  private void setLanguageOptions(LanguageOptions options) {
    CopyrightManager.getInstance(getProject()).getOptions().setOptions(PerlFileTypePackage.INSTANCE.getName(), options);
  }

  private void modifyLanguageOptions(@NotNull Consumer<? super LanguageOptions> optionsConfigurator) {
    LanguageOptions options = getLanguageOptions();
    optionsConfigurator.accept(options);
    setLanguageOptions(options);
  }

  @Override
  public String getFileExtension() {
    return PerlFileTypePackage.EXTENSION;
  }

  private void doTest() {
    initWithFileSmart();
    String notice = "Copyright test\nMulti-lined with ${file.fileName} ";
    doTestWithoutInit(notice);
  }

  private void doTestWithoutInit(String notice) {
    final CopyrightProfile profile = new CopyrightProfile();
    profile.setNotice(notice);

    final UpdateCopyright updateCopyright =
      UpdateCopyrightFactory.createUpdateCopyright(getProject(), getModule(), myFixture.getFile(), profile);

    assertNotNull(updateCopyright);
    updateCopyright.prepare();
    try {
      updateCopyright.complete();
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }

    String documentText = getEditor().getDocument().getText();
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), documentText.replaceAll("\n +\n", "\n\n"));
  }
}
