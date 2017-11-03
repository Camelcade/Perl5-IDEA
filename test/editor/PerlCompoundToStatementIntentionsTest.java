/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

package editor;

import com.intellij.openapi.util.io.FileUtil;
import com.perl5.PerlBundle;

public class PerlCompoundToStatementIntentionsTest extends PerlIntentionsTestCase {
  @Override
  protected String getTestDataPath() {
    return FileUtil.join(super.getTestDataPath(), "compoundToStatement");
  }

  public void testWithDeclarationIf() {doTest();}

  public void testWithDeclarationUnless() {doTest();}

  public void testWithDeclarationUntil() {doTest();}

  public void testWithDeclarationWhile() {doTest();}

  public void testWhileEach() {doTest();}

  public void testIf() {doTest();}

  public void testUnless() {doTest();}

  public void testWhile() {doTest();}

  public void testWhileWithPredeclared() {doTest();}

  public void testUntil() {doTest();}

  public void testFor() {doTest();}

  public void testForWithIterator() {doTest();}

  public void testForWithIteratorBracedRegexp() {doTest();}

  public void testForWithIteratorDeclaration() {doTest();}

  public void testForWithAmbiguousIterator() {doTest();}

  public void testForeach() {doTest();}

  public void testWhen() {doTest();}

  private void doTest() {
    doTestIntention(PerlBundle.message("perl.intention.convert.to.statement"));
  }
}
