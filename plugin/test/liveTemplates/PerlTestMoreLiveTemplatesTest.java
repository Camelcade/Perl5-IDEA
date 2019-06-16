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

package liveTemplates;


import com.intellij.openapi.util.io.FileUtil;
import com.perl5.lang.perl.fileTypes.PerlFileTypeTest;
import org.junit.Test;
public class PerlTestMoreLiveTemplatesTest extends PerlLiveTemplatesTestCase {
  @Override
  protected String getTestDataPath() {
    return FileUtil.join(super.getTestDataPath(), "test_more");
  }

  @Override
  public String getFileExtension() {
    return PerlFileTypeTest.EXTENSION;
  }

  @Test
  public void testSubtest() {doTest("st");}

  @Test
  public void testOk() {doTest("ok");}

  @Test
  public void testIs() {doTest("is");}

  @Test
  public void testIsnt() {doTest("it");}

  @Test
  public void testLike() {doTest("li");}

  @Test
  public void testUnlike() {doTest("ul");}

  @Test
  public void testCmpOk() {doTest("cmo");}

  @Test
  public void testCanOk() {doTest("cno");}

  @Test
  public void testIsaOk() {doTest("iso");}

  @Test
  public void testNewOk() {doTest("neo");}

  @Test
  public void testPass() {doTest("pa");}

  @Test
  public void testFail() {doTest("fa");}

  @Test
  public void testRequireOk() {doTest("ro");}

  @Test
  public void testUseOk() {doTest("uo");}

  @Test
  public void testIsDeeply() {doTest("id");}

  @Test
  public void testDiag() {doTest("di");}

  @Test
  public void testNote() {doTest("nt");}

  @Test
  public void testExplain() {doTest("exp");}

  @Test
  public void testSkip() {doTest("sk");}

  @Test
  public void testTodo() {doTest("td");}

  @Test
  public void testTodoSkip() {doTest("tds");}

  @Test
  public void testBailOut() {doTest("bo");}

  @Test
  public void testPlanTests() {doTest("pl");}

  @Test
  public void testPlanSkipAll() {doTest("psa");}

  @Test
  public void testPlanNoPlan() {doTest("pnp");}
}
