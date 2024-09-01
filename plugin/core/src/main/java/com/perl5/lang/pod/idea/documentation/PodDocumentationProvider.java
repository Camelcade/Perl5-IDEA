/*
 * Copyright 2015-2024 Alexandr Evstigneev
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
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.documentation.PerlDocUtil;
import com.perl5.lang.perl.documentation.PerlDocumentationProviderBase;
import com.perl5.lang.perl.psi.PerlFile;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.lexer.PodElementTypes;
import com.perl5.lang.pod.lexer.PodTokenSets;
import com.perl5.lang.pod.parser.psi.*;
import com.perl5.lang.pod.parser.psi.impl.PodFileImpl;
import com.perl5.lang.pod.parser.psi.mixin.PodFormatterX;
import com.perl5.lang.pod.psi.PsiPodFormatIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.perl5.lang.perl.documentation.PerlDocumentationProvider.findPodElement;

public class PodDocumentationProvider extends PerlDocumentationProviderBase implements PodElementTypes {
  private static final Logger LOG = Logger.getInstance(PodDocumentationProvider.class);

  @Override
  public @Nullable String generateDoc(@Nullable PsiElement element, @Nullable PsiElement originalElement) {
    return doGenerateDoc(element);
  }

  @Override
  public PsiElement getDocumentationElementForLookupItem(PsiManager psiManager, Object object, PsiElement element) {
    if (object instanceof LiveTemplateLookupElementImpl) {
      String lookupString = ((LiveTemplateLookupElementImpl)object).getLookupString();
      if (lookupString.startsWith("=")) {
        return PerlDocUtil.resolveDoc("perlpod", lookupString, element, true);
      }
    }
    else if (object instanceof PodSection) {
      return (PodSection)object;
    }
    return super.getDocumentationElementForLookupItem(psiManager, object, element);
  }

  @Override
  public @Nullable PsiElement getCustomDocumentationElement(@NotNull Editor editor,
                                                            @NotNull PsiFile file,
                                                            @Nullable PsiElement contextElement,
                                                            int targetOffset) {
    if (contextElement == null || contextElement.getLanguage() != PodLanguage.INSTANCE) {
      return null;
    }
    IElementType elementType = PsiUtilCore.getElementType(contextElement);

    if (elementType == POD_ANGLE_LEFT) {
      return getCustomDocumentationElement(editor, file, contextElement.getPrevSibling(), targetOffset);
    }
    else if (PodTokenSets.POD_FORMATTERS_TOKENSET.contains(elementType)) {
      return PerlDocUtil.resolveDoc("perlpod", contextElement.getText(), contextElement, true);
    }
    else if (PodTokenSets.POD_COMMANDS_TOKENSET.contains(elementType)) {
      return PerlDocUtil.resolveDoc("perlpod", contextElement.getText(), contextElement, true);
    }
    return null;
  }

  @Override
  public @Nullable PsiElement getDocumentationElementForLink(PsiManager psiManager, String link, PsiElement context) {
    if (context.getLanguage().isKindOf(PerlLanguage.INSTANCE)) {
      context = findPodElement(context);
    }
    if (context instanceof PodCompositeElement || context instanceof PerlFile) {
      try {
        return PerlDocUtil.resolveDescriptor(PodLinkDescriptor.createFromUrl(URLDecoder.decode(link, StandardCharsets.UTF_8)), context,
                                             false);
      }
      catch (Exception e) {
        LOG.error(e);
      }
    }
    return super.getDocumentationElementForLink(psiManager, link, context);
  }

  public static @Nullable String doGenerateDoc(@Nullable PsiElement element) {
    return switch (element) {
      case null -> null;
      case PodFile ignored -> StringUtil.nullize(PerlDocUtil.renderPodFile((PodFileImpl)element));
      case PodFormatterX x -> generateDocByIndex(x);
      case PodSection section -> PerlDocUtil.renderElement(section);
      default -> null;
    };
  }

  private static @NotNull String generateDocByIndex(@NotNull PodFormatterX element) {
    String indexText = element.getPresentableText();
    List<PsiElement> targets = new ArrayList<>();
    element.getContainingFile().accept(new PodStubsAwareRecursiveVisitor() {
      @Override
      public void visitPodFormatIndex(@NotNull PsiPodFormatIndex o) {
        assert o instanceof PodFormatterX;
        if (StringUtil.equals(indexText, ((PodFormatterX)o).getPresentableText())) {
          ContainerUtil.addIfNotNull(targets, ((PodFormatterX)o).getIndexTarget());
        }
      }
    });
    return targets.stream().map(PodDocumentationProvider::doGenerateDoc).collect(Collectors.joining("<hr>"));
  }
}
