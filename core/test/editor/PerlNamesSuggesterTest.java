/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

public class PerlNamesSuggesterTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/nameSuggester/perl";
  }

  public void testHeredocOpener() {doTest();}

  public void testHeredocTerminator() {doTest();}

  public void testScalarSub() {doTest();}

  public void testScalarSubArrayHash() {doTest();}

  public void testScalarSubAll() {doTest();}

  public void testScalarSubAllLocal() {doTest();}

  public void testScalarSubAllOur() {doTest();}

  public void testScalarSubAllState() {doTest();}

  public void testScalarSubAllMixed() {doTest();}

  public void testScalarSubParallel() {doTest();}

  public void testScalarSubShadowed() {doTest();}

  public void testScalarSubShadowedInner() {doTest();}

  public void testScalarSubShadows() {doTest();}

  public void testScalarSubShadowsInner() {doTest();}

  public void testScalarSubArrayHashElements() {doTest();}

  public void testArraySub() {doTest();}

  public void testArraySubAll() {doTest();}

  public void testArraySubAllLocal() {doTest();}

  public void testArraySubAllMixed() {doTest();}

  public void testArraySubAllOur() {doTest();}

  public void testArraySubAllState() {doTest();}

  public void testArraySubArrayElementSlice() {doTest();}

  public void testArraySubHashSlices() {doTest();}

  public void testArraySubParallel() {doTest();}

  public void testArraySubScalarHash() {doTest();}

  public void testArraySubShadowed() {doTest();}

  public void testArraySubShadowedInner() {doTest();}

  public void testArraySubShadows() {doTest();}

  public void testArraySubShadowsInner() {doTest();}

  public void testHashSub() {doTest();}

  public void testHashSubAll() {doTest();}

  public void testHashSubAllLocal() {doTest();}

  public void testHashSubAllMixed() {doTest();}

  public void testHashSubAllOur() {doTest();}

  public void testHashSubAllState() {doTest();}

  public void testHashSubArrayScalar() {doTest();}

  public void testHashSubElementSlices() {doTest();}

  public void testHashSubParallel() {doTest();}

  public void testHashSubShadowed() {doTest();}

  public void testHashSubShadowedInner() {doTest();}

  public void testHashSubShadows() {doTest();}

  public void testHashSubShadowsInner() {doTest();}

  private void doTest() {
    doTestSuggesterOnRename(new PerlMemberInplaceRenameHandler());
  }
}
