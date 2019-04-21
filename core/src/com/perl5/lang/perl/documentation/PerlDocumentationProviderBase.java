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

package com.perl5.lang.perl.documentation;

import com.intellij.lang.documentation.AbstractDocumentationProvider;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.psi.PerlFile;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.lexer.PodElementTypes;
import com.perl5.lang.pod.parser.psi.PodCompositeElement;
import com.perl5.lang.pod.parser.psi.PodFile;
import com.perl5.lang.pod.parser.psi.PodLinkDescriptor;
import com.perl5.lang.pod.parser.psi.impl.PodFileImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URLDecoder;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.POD;

/**
 * Created by hurricup on 03.04.2016.
 */
public abstract class PerlDocumentationProviderBase extends AbstractDocumentationProvider {
  @Nullable
  @Override
  public String generateDoc(PsiElement element, @Nullable PsiElement originalElement) {
    String result = null;
    if (element instanceof PodFile) {
      result = PerlDocUtil.renderPodFile((PodFileImpl)element);
      if (StringUtil.isEmpty(result)) {
        result = "Empty documenation section...";
      }
    }
    else if (element instanceof PodCompositeElement) {
      result = PerlDocUtil.renderElement((PodCompositeElement)element);
    }
    else if (PsiUtilCore.getElementType(element) == POD) {
      result = PerlDocUtil.renderPodElement(element);
    }
    return StringUtil.isEmpty(result) ? super.generateDoc(element, originalElement) : result;
  }


  @Nullable
  @Override
  public PsiElement getDocumentationElementForLink(PsiManager psiManager, String link, PsiElement context) {
    if (context instanceof PodCompositeElement || context instanceof PerlFile) {
      try {
        return PerlDocUtil.resolveDescriptor(PodLinkDescriptor.createFromUrl(URLDecoder.decode(link, "UTF-8")), context, false);
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    return super.getDocumentationElementForLink(psiManager, link, context);
  }

  @Nullable
  @Override
  public PsiElement getCustomDocumentationElement(@NotNull Editor editor, @NotNull PsiFile file, @Nullable PsiElement contextElement) {

    if (contextElement instanceof PsiFile) {
      PsiFile podFile = ((PsiFile)contextElement).getViewProvider().getPsi(PodLanguage.INSTANCE);

      if (podFile != null &&
          podFile.getChildren().length == 1 &&
          podFile.getFirstChild().getNode().getElementType() == PodElementTypes.POD_OUTER) {
        return null;
      }

      return podFile;
    }
    else if (contextElement != null) {
      return getCustomDocumentationElement(editor, file, contextElement.getParent());
    }

    return null;
  }
}
