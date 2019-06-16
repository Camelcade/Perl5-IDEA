/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package rename;


import base.MojoLightTestCase;
import com.intellij.testFramework.UsefulTestCase;
import com.perl5.lang.perl.fileTypes.PerlFileTypeScript;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
public class MojoPerlRenameTest extends MojoLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/rename/perl";
  }


  @Override
  public String getFileExtension() {
    return PerlFileTypeScript.EXTENSION_PL;
  }


  @Test
  public void testMojoHelper() {
    doTest("newName");
  }

  protected void doRenameAtCaret(@NotNull String newName) {
    myFixture.renameElementAtCaret(newName);
  }

  protected void doTest() {
    doTest("NewName");
  }

  protected void doTest(@NotNull String newName) {
    initWithFileSmart();
    doRenameAtCaret(newName);
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), getFile().getText());
  }
}
