/*
 * Copyright 2016 Alexandr Evstigneev
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

package resolve.htmlmason;

import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.PerlVariableDeclarationWrapper;
import com.perl5.lang.perl.psi.PerlVariableNameElement;
import resolve.perl.PerlVariableResolveTestCase;

/**
 * Created by hurricup on 15.03.2016.
 */
public class HTMLMasonConcurrentBlocksResolveTest extends PerlVariableResolveTestCase {
  protected String getTestDataPath() {
    return "testData/resolve/htmlmason/concurrent_blocks";
  }

  @Override
  public String getFileExtension() {
    return "mas";
  }

  public void testArgs() throws Exception {
    doTest("args");
  }

  public void testInit() throws Exception {
    doTest("init");
  }

  public void testFromFilter() throws Exception {
    doTest("from_filter");
  }

  protected void doTest(String fileName) {
    initWithFileSmart(fileName);
    PsiElement fileLevelDeclaration = getElementAtCaret(0, PerlVariableDeclarationWrapper.class);
    assertNotNull(fileLevelDeclaration);
    PsiElement defLevelDeclaration = getElementAtCaret(1, PerlVariableDeclarationWrapper.class);
    assertNotNull(defLevelDeclaration);
    PsiElement defLevelUsage = getElementAtCaret(2, PerlVariableNameElement.class);
    assertNotNull(defLevelUsage);
    PsiElement fileLevelUsage = getElementAtCaret(3, PerlVariableNameElement.class);
    assertNotNull(fileLevelUsage);
  }
}
