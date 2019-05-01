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
import com.intellij.psi.PsiElement;
import com.perl5.lang.pod.parser.psi.PodRenderingContext;
import com.perl5.lang.pod.parser.psi.PodSectionFormatted;
import com.perl5.lang.pod.psi.PsiPodSectionFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.pod.parser.psi.PodSyntaxElements.FORMAT_HTML;

/**
 * Created by hurricup on 26.03.2016.
 */
public class PodSectionFormattedMixin extends PodSectionMixin implements PodSectionFormatted {

  public PodSectionFormattedMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Nullable
  protected PsiElement getFormatterElement() {
    return findChildByClass(PsiPodSectionFormat.class);
  }

  @Nullable
  protected PsiElement getFormatterNameElement() {
    PsiElement formatterElement = getFormatterElement();

    if (formatterElement != null) {
      ASTNode formatterNode = formatterElement.getNode();
      ASTNode formatterNameNode = formatterNode.findChildByType(POD_FORMAT_NAME);
      return formatterNameNode == null ? null : formatterNameNode.getPsi();
    }
    return null;
  }

  @Nullable
  public String getFormatterName() {
    PsiElement formatterNameElement = getFormatterNameElement();
    return formatterNameElement == null ? null : formatterNameElement.getText();
  }

  @Override
  public void renderElementContentAsHTML(StringBuilder builder, PodRenderingContext context) {
    String formatterName = getFormatterName();
    if (FORMAT_HTML.equals(formatterName)) {
      boolean isSafe = context.isHtmlSafe();
      context.setHtmlSafe(true);
      super.renderElementContentAsHTML(builder, context);
      context.setHtmlSafe(isSafe);
    }
  }
}
