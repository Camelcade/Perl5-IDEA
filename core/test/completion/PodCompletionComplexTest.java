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

package completion;

import base.PodLightTestCase;
import com.intellij.codeInsight.TargetElementUtil;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.Lookup;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl;
import com.intellij.usageView.UsageInfo;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Test opens completion, collects lookups, choosing lookups with psi element object. Then, for each lookup:<ul>
 * <li>Completing lookup</li>
 * <li>Checks if result refers to original target</li>
 * <li>Checks if target usages contains inserted element</li>
 * </ul>
 */
public class PodCompletionComplexTest extends PodLightTestCase {
  private static final Logger LOG = Logger.getInstance(PodCompletionComplexTest.class);

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    withPerlPod();
  }

  @Override
  protected boolean restrictFilesParsing() {
    return false;
  }

  public void testPerlPod() {
    doTest("L<perlpod/<caret>>");
  }

  private void doTest(@NotNull String text) {
    initWithTextSmart(text);
    Map<String, LookupElement> lookupElements = new HashMap<>();
    for (LookupElement lookupElement : myFixture.complete(CompletionType.BASIC, 1)) {
      if (lookupElement.getObject() instanceof PsiElement) {
        String lookupString = lookupElement.getLookupString();
        LookupElement existingElement = lookupElements.get(lookupString);
        if (existingElement != null) {
          if (((PsiElement)existingElement.getObject()).getTextRange().getStartOffset() <
              ((PsiElement)lookupElement.getObject()).getTextRange().getStartOffset()) {
            continue;
          }
        }
        lookupElements.put(lookupString, lookupElement);
      }
    }

    List<String> keys = new ArrayList<>(lookupElements.keySet());
    keys.sort(String::compareTo);
    for (String lookupKey : keys) {
      LOG.debug("Processing: " + lookupKey);
      initWithTextSmart(text);
      CaretModel caretModel = getEditor().getCaretModel();
      int offset = caretModel.getOffset();
      myFixture.complete(CompletionType.BASIC, 1);
      LookupElement targetLookup = lookupElements.get(lookupKey);
      myFixture.getLookup().setCurrentItem(targetLookup);
      myFixture.finishLookup(Lookup.NORMAL_SELECT_CHAR);
      caretModel.moveToOffset(offset);
      PsiElement targetPsiElement = (PsiElement)targetLookup.getObject();
      PsiElement referenceElement = TargetElementUtil.findTargetElement(getEditor(), TargetElementUtil.REFERENCED_ELEMENT_ACCEPTED);
      assertEquals("While processing: " + lookupKey + "; " + targetLookup + "; " + targetPsiElement, targetPsiElement, referenceElement);
      Collection<UsageInfo> targetUsages =
        ((CodeInsightTestFixtureImpl)myFixture).findUsages(targetPsiElement, GlobalSearchScope.fileScope(getFile()));
      if (targetUsages.size() != 1) {
        fail("While processing: " + lookupKey + "; " + targetLookup + "; " + targetPsiElement);
      }
    }
  }
}
