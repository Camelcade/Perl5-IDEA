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

package com.perl5.lang.pod.idea.documentation;

import com.intellij.codeInsight.template.impl.LiveTemplateLookupElementImpl;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.perl5.lang.perl.documentation.PerlDocUtil;
import com.perl5.lang.perl.documentation.PerlDocumentationProviderBase;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.lexer.PodElementTypes;
import com.perl5.lang.pod.parser.psi.PodFormatter;
import com.perl5.lang.pod.parser.psi.PodSection;
import com.perl5.lang.pod.parser.psi.PodSectionParagraph;
import com.perl5.lang.pod.parser.psi.PodSectionVerbatimParagraph;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.codeInsight.TargetElementUtil.REFERENCED_ELEMENT_ACCEPTED;

public class PodDocumentationProvider extends PerlDocumentationProviderBase implements PodElementTypes {

  @Override
  public PsiElement getDocumentationElementForLookupItem(PsiManager psiManager, Object object, PsiElement element) {
    if (object instanceof LiveTemplateLookupElementImpl) {
      String lookupString = ((LiveTemplateLookupElementImpl)object).getLookupString();
      if (lookupString.startsWith("=")) {
        return PerlDocUtil.resolveDoc("perlpod", lookupString, element, true);
      }
    }
    return super.getDocumentationElementForLookupItem(psiManager, object, element);
  }

  @Nullable
  @Override
  public PsiElement getCustomDocumentationElement(@NotNull Editor editor, @NotNull PsiFile file, @Nullable PsiElement contextElement) {
    if (contextElement == null || contextElement.getLanguage() != PodLanguage.INSTANCE) {
      return null;
    }

    if (contextElement instanceof PodFormatter) {
      PsiElement tagElement = contextElement.getFirstChild();
      if (tagElement != null) {
        String tagText = tagElement.getText();
        if (StringUtil.isNotEmpty(tagText)) {
          return PerlDocUtil.resolveDoc("perlpod", tagText, contextElement, true);
        }
      }
    }
    else if (contextElement instanceof PodSectionParagraph) {
      return PerlDocUtil.resolveDoc("perlpod", "Ordinary Paragraph", contextElement, true);
    }
    else if (contextElement instanceof PodSectionVerbatimParagraph) {
      return PerlDocUtil.resolveDoc("perlpod", "Verbatim Paragraph", contextElement, true);
    }
    else if (contextElement instanceof PodSection) {
      PsiElement tagElement = contextElement.getFirstChild();
      if (tagElement != null && tagElement.getNode().getElementType() != POD_UNKNOWN) {
        String tagText = tagElement.getText();
        if (StringUtil.isNotEmpty(tagText)) {
          return PerlDocUtil.resolveDoc("perlpod", tagText, contextElement, true);
        }
      }
    }

    PsiElement targetElement = findTargetElement(editor, REFERENCED_ELEMENT_ACCEPTED);
    if (targetElement != null) {
      return targetElement;
    }

    return super.getCustomDocumentationElement(editor, file, contextElement);
  }
}
