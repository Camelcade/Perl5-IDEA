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

package editor.liveTemplates;

import com.intellij.openapi.util.io.FileUtil;
import com.perl5.lang.perl.fileTypes.PerlFileTypeTest;

public class PerlTestMoreLiveTemplatesTest extends PerlLiveTemplatesTestCase {
  @Override
  protected String getTestDataPath() {
    return FileUtil.join(super.getTestDataPath(), "test_more");
  }

  @Override
  public String getFileExtension() {
    return PerlFileTypeTest.EXTENSION;
  }

  public void testSubtest() {doTest("st");}

  public void testOk() {doTest("ok");}

  public void testIs() {doTest("is");}

  public void testIsnt() {doTest("it");}

  public void testLike() {doTest("li");}

  public void testUnlike() {doTest("ul");}

  public void testCmpOk() {doTest("cmo");}

  public void testCanOk() {doTest("cno");}

  public void testIsaOk() {doTest("iso");}

  public void testNewOk() {doTest("neo");}

  public void testPass() {doTest("pa");}

  public void testFail() {doTest("fa");}

  public void testRequireOk() {doTest("ro");}

  public void testUseOk() {doTest("uo");}

  public void testIsDeeply() {doTest("id");}

  public void testDiag() {doTest("di");}

  public void testNote() {doTest("nt");}

  public void testExplain() {doTest("exp");}

  public void testSkip() {doTest("sk");}

  public void testTodo() {doTest("td");}

  public void testTodoSkip() {doTest("tds");}

  public void testBailOut() {doTest("bo");}

  public void testPlanTests() {doTest("pl");}

  public void testPlanSkipAll() {doTest("psa");}

  public void testPlanNoPlan() {doTest("pnp");}
}
