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

package com.perl5.lang.pod.parser.psi.mixin;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.pod.idea.completion.PodLinkCompletionProvider;
import com.perl5.lang.pod.parser.psi.PodRenderingContext;
import com.perl5.lang.pod.parser.psi.stubs.PodSectionStub;
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.pod.parser.psi.util.PodRenderUtil.PARAGRAPH_PREFIX;
import static com.perl5.lang.pod.parser.psi.util.PodRenderUtil.PARAGRAPH_SUFFIX;

public class PodSectionParagraph extends PodStubBasedSection {

  public PodSectionParagraph(@NotNull PodSectionStub stub,
                             @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public PodSectionParagraph(@NotNull ASTNode node) {
    super(node);
  }

  @NotNull
  @Override
  public String getPresentableText() {
    PodSectionStub stub = getGreenStub();
    if (stub != null) {
      return stub.getContent();
    }
    return StringUtil.notNullize(PodLinkCompletionProvider.trimItemText(PodRenderUtil.renderPsiElementAsText(this)));
  }

  @Override
  public void renderElementContentAsHTML(StringBuilder builder, PodRenderingContext context) {
    PsiElement firstChild = getFirstChild();

    if (firstChild != null) {
      builder.append(PARAGRAPH_PREFIX);
      PodRenderUtil.renderPsiRangeAsHTML(firstChild, null, builder, context);
      builder.append(PARAGRAPH_SUFFIX);
    }
  }

  @Override
  public void renderElementContentAsText(StringBuilder builder, PodRenderingContext context) {
    PsiElement firstChild = getFirstChild();

    if (firstChild != null) {
      PodRenderUtil.renderPsiRangeAsText(firstChild, null, builder, context);
    }
  }
}
