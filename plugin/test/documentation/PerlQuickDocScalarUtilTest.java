/*
 * Copyright 2015-2021 Alexandr Evstigneev
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

package documentation;

import base.PerlLightTestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class PerlQuickDocScalarUtilTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/documentation/perl/quickdoc/scalarUtil";
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    withPerl532();
  }

  @Test
  public void testBlessedFqn() { doTestFqn("blessed"); }

  @Test
  public void testRefaddrFqn() { doTestFqn("refaddr"); }

  @Test
  public void testReftypeFqn() { doTestFqn("reftype"); }

  @Test
  public void testWeakenFqn() { doTestFqn("weaken"); }

  @Test
  public void testUnweakenFqn() { doTestFqn("unweaken"); }

  @Test
  public void testIsweakFqn() { doTestFqn("isweak"); }

  @Test
  public void testDualvarFqn() { doTestFqn("dualvar"); }

  @Test
  public void testIsdualFqn() { doTestFqn("isdual"); }

  @Test
  public void testIsvstringFqn() { doTestFqn("isvstring"); }

  @Test
  public void testLooks_like_numberFqn() { doTestFqn("looks_like_number"); }

  @Test
  public void testOpenhandleFqn() { doTestFqn("openhandle"); }

  @Test
  public void testReadonlyFqn() { doTestFqn("readonly"); }

  @Test
  public void testSet_prototypeFqn() { doTestFqn("set_prototype"); }

  @Test
  public void testTaintedFqn() { doTestFqn("tainted"); }

  @Test
  public void testBlessedImported() { doTestImported("blessed"); }

  @Test
  public void testRefaddrImported() { doTestImported("refaddr"); }

  @Test
  public void testReftypeImported() { doTestImported("reftype"); }

  @Test
  public void testWeakenImported() { doTestImported("weaken"); }

  @Test
  public void testUnweakenImported() { doTestImported("unweaken"); }

  @Test
  public void testIsweakImported() { doTestImported("isweak"); }

  @Test
  public void testDualvarImported() { doTestImported("dualvar"); }

  @Test
  public void testIsdualImported() { doTestImported("isdual"); }

  @Test
  public void testIsvstringImported() { doTestImported("isvstring"); }

  @Test
  public void testLooks_like_numberImported() { doTestImported("looks_like_number"); }

  @Test
  public void testOpenhandleImported() { doTestImported("openhandle"); }

  @Test
  public void testReadonlyImported() { doTestImported("readonly"); }

  @Test
  public void testSet_prototypeImported() { doTestImported("set_prototype"); }

  @Test
  public void testTaintedImported() { doTestImported("tainted"); }

  private void doTestFqn(@NotNull String keyword) {
    initWithTextSmartWithoutErrors("Scalar::Util::<caret>" + keyword);
    doTestQuickDocWithoutInit();
  }

  private void doTestImported(@NotNull String keyword) {
    initWithTextSmartWithoutErrors("use Scalar::Util qw/" + keyword + "/;\n" +
                                   "<caret>" + keyword);
    doTestQuickDocWithoutInit();
  }
}
