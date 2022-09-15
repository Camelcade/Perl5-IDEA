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

package editor.intentions;


import com.intellij.openapi.util.io.FileUtil;
import com.perl5.PerlBundle;
import org.junit.Test;
public class PerlCompoundToStatementIntentionsTest extends PerlIntentionsTestCase {
  @Override
  protected String getBaseDataPath() {
    return FileUtil.join(super.getBaseDataPath(), "compoundToStatement");
  }

  @Test
  public void testForIterable() {doTestNegative();}

  @Test
  public void testForWithMultiStatements() {doTestNegative();}

  @Test
  public void testForWithoutStatement() {doTestNegative();}

  @Test
  public void testWithDeclarationIf() {doTest();}

  @Test
  public void testWithDeclarationUnless() {doTest();}

  @Test
  public void testWithDeclarationUntil() {doTest();}

  @Test
  public void testWithDeclarationWhile() {doTest();}

  @Test
  public void testWhileEach() {doTest();}

  @Test
  public void testIf() {doTest();}

  @Test
  public void testUnless() {doTest();}

  @Test
  public void testWhile() {doTest();}

  @Test
  public void testWhileWithPredeclared() {doTest();}

  @Test
  public void testUntil() {doTest();}

  @Test
  public void testFor() {doTest();}

  @Test
  public void testForWithIterator() {doTest();}

  @Test
  public void testForWithIteratorBracedRegexp() {doTest();}

  @Test
  public void testForWithIteratorDeclaration() {doTest();}

  @Test
  public void testForWithAmbiguousIterator() {doTest();}

  @Test
  public void testForeach() {doTest();}

  @Test
  public void testWhen() {doTest();}

  private void doTest() {
    doTestIntention(PerlBundle.message("perl.intention.convert.to.statement"));
  }

  private void doTestNegative() {
    doTestNoIntention(PerlBundle.message("perl.intention.convert.to.statement"));
  }
}
