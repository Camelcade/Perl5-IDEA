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

package editor;


import base.PerlLightTestCase;
import com.perl5.lang.perl.idea.refactoring.rename.PerlMemberInplaceRenameHandler;
import org.junit.Test;
public class PerlNamesSuggesterTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "nameSuggester/perl";
  }

  @Test
  public void testListToScalarDeclaration() {doTest();}

  @Test
  public void testListToScalarDeclarationInParens() {doTest();}

  @Test
  public void testListToScalarInParens() {doTest();}

  @Test
  public void testStringListToScalarDeclaration() {doTest();}

  @Test
  public void testStringListToScalarDeclarationInParens() {doTest();}

  @Test
  public void testStringListToScalarInParens() {doTest();}

  @Test
  public void testAssignScalarEmpty() {doTest();}

  @Test
  public void testAssignScalarFirst() {doTest();}

  @Test
  public void testAssignScalarList() {doTest();}

  @Test
  public void testAssignScalarListFirst() {doTest();}

  @Test
  public void testAssignScalarListWithValues() {doTest();}

  @Test
  public void testAssignScalarNothing() {doTest();}

  @Test
  public void testAssignScalarParensInside() {doTest();}

  @Test
  public void testAssignScalarParensOutside() {doTest();}

  @Test
  public void testAssignScalarSecond() {doTest();}

  @Test
  public void testAssignScalarSecondParens() {doTest();}

  @Test
  public void testAssignScalarSimple() {doTest();}

  @Test
  public void testAssignScalarThird() {doTest();}

  @Test
  public void testAssignScalarThirdShift() {doTest();}

  @Test
  public void testHeredocOpener() {doTest();}

  @Test
  public void testHeredocTerminator() {doTest();}

  @Test
  public void testScalarSub() {doTest();}

  @Test
  public void testScalarSubArrayHash() {doTest();}

  @Test
  public void testScalarSubAll() {doTest();}

  @Test
  public void testScalarSubAllLocal() {doTest();}

  @Test
  public void testScalarSubAllOur() {doTest();}

  @Test
  public void testScalarSubAllState() {doTest();}

  @Test
  public void testScalarSubAllMixed() {doTest();}

  @Test
  public void testScalarSubParallel() {doTest();}

  @Test
  public void testScalarSubShadowed() {doTest();}

  @Test
  public void testScalarSubShadowedInner() {doTest();}

  @Test
  public void testScalarSubShadows() {doTest();}

  @Test
  public void testScalarSubShadowsInner() {doTest();}

  @Test
  public void testScalarSubArrayHashElements() {doTest();}

  @Test
  public void testArraySub() {doTest();}

  @Test
  public void testArraySubAll() {doTest();}

  @Test
  public void testArraySubAllLocal() {doTest();}

  @Test
  public void testArraySubAllMixed() {doTest();}

  @Test
  public void testArraySubAllOur() {doTest();}

  @Test
  public void testArraySubAllState() {doTest();}

  @Test
  public void testArraySubArrayElementSlice() {doTest();}

  @Test
  public void testArraySubHashSlices() {doTest();}

  @Test
  public void testArraySubParallel() {doTest();}

  @Test
  public void testArraySubScalarHash() {doTest();}

  @Test
  public void testArraySubShadowed() {doTest();}

  @Test
  public void testArraySubShadowedInner() {doTest();}

  @Test
  public void testArraySubShadows() {doTest();}

  @Test
  public void testArraySubShadowsInner() {doTest();}

  @Test
  public void testHashSub() {doTest();}

  @Test
  public void testHashSubAll() {doTest();}

  @Test
  public void testHashSubAllLocal() {doTest();}

  @Test
  public void testHashSubAllMixed() {doTest();}

  @Test
  public void testHashSubAllOur() {doTest();}

  @Test
  public void testHashSubAllState() {doTest();}

  @Test
  public void testHashSubArrayScalar() {doTest();}

  @Test
  public void testHashSubElementSlices() {doTest();}

  @Test
  public void testHashSubParallel() {doTest();}

  @Test
  public void testHashSubShadowed() {doTest();}

  @Test
  public void testHashSubShadowedInner() {doTest();}

  @Test
  public void testHashSubShadows() {doTest();}

  @Test
  public void testHashSubShadowsInner() {doTest();}

  private void doTest() {
    doTestSuggesterOnRename(new PerlMemberInplaceRenameHandler());
  }
}
