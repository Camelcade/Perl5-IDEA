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

package com.perl5.lang.perl.documentation;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlTokenSets;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlBuiltInSubDefinition;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.properties.PerlValuableEntity;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.idea.documentation.PodDocumentationProvider;
import com.perl5.lang.pod.parser.psi.impl.PodFileImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.documentation.PerlDocUtil.SWITCH_DOC_LINK;
import static com.perl5.lang.perl.lexer.PerlTokenSets.HEREDOC_BODIES_TOKENSET;
import static com.perl5.lang.perl.lexer.PerlTokenSets.TAGS_TOKEN_SET;

/**
 * Created by hurricup on 26.03.2016.
 */
public class PerlDocumentationProvider extends PerlDocumentationProviderBase implements PerlElementTypes, PerlElementPatterns {
  private static final TokenSet FORCE_AS_OPERATORS_TOKENSET = TokenSet.orSet(
    HEREDOC_BODIES_TOKENSET,
    TokenSet.create(
      RESERVED_Q,
      RESERVED_QQ,
      RESERVED_QX,
      RESERVED_QW,
      RESERVED_TR,
      RESERVED_Y,
      HEREDOC_OPENER,
      HEREDOC_END,
      HEREDOC_END_INDENTABLE,

      RESERVED_S,
      RESERVED_M,
      RESERVED_QR
    )
  );
  private static final TokenSet FORCE_AS_FUNC_TOKENSET = TokenSet.orSet(TAGS_TOKEN_SET, TokenSet.create(BLOCK_NAME, OPERATOR_FILETEST));

  @Override
  public String generateDoc(PsiElement element, @Nullable PsiElement originalElement) {
    if (element instanceof PerlSubElement) {
      return generateDoc((PerlSubElement)element);
    }
    else if (element instanceof PerlFileImpl) {
      return generateDoc((PerlFileImpl)element);
    }
    else if (element instanceof PerlNamespaceDefinitionElement) {
      return generateDoc((PerlNamespaceDefinitionElement)element);
    }
    else if (element instanceof PerlVariable) {
      PsiElement docElement = PerlDocUtil.getPerlVarDoc((PerlVariable)element);
      if (docElement != null) {
        return PodDocumentationProvider.doGenerateDoc(docElement);
      }
    }
    return super.generateDoc(element, originalElement);
  }

  @Nullable
  @Override
  public String getQuickNavigateInfo(PsiElement element, PsiElement originalElement) {
    if (originalElement instanceof PerlVariableNameElement) {
      return getQuickNavigateInfo(element, originalElement.getParent());
    }
    if (originalElement instanceof PerlValuableEntity) {
      return PerlValue.from(originalElement.getOriginalElement()).getPresentableText();
    }
    return super.getQuickNavigateInfo(element, originalElement);
  }

  @Override
  public PsiElement getDocumentationElementForLookupItem(PsiManager psiManager, Object object, PsiElement element) {
    if (object instanceof PerlVariable) {
      return PerlDocUtil.getPerlVarDoc((PerlVariable)object);
    }
    else if (object instanceof PerlBuiltInSubDefinition) {
      String subName = StringUtil.notNullize(((PerlBuiltInSubDefinition)object).getName());
      if ("default".equals(subName)) {
        return PerlDocUtil.resolveDescriptor(SWITCH_DOC_LINK, (PsiElement)object, false);
      }
      else {
        return PerlDocUtil.getPerlFuncDocFromText((PsiElement)object, subName);
      }
    }
    return super.getDocumentationElementForLookupItem(psiManager, object, element);
  }

  @Nullable
  @Override
  public PsiElement getCustomDocumentationElement(@NotNull Editor editor, @NotNull PsiFile file, @Nullable PsiElement contextElement) {
    if (contextElement == null || contextElement.getLanguage() != PerlLanguage.INSTANCE) {
      return null;
    }

    IElementType elementType = contextElement.getNode().getElementType();

    if (elementType == REGEX_MODIFIER) {
      return PerlDocUtil.getRegexModifierDoc(contextElement);
    }
    else if (elementType == REGEX_TOKEN) {
      return PerlDocUtil.resolveDoc("perlretut", null, contextElement, false);
    }
    else if (elementType == VERSION_ELEMENT) {
      return PerlDocUtil.resolveDoc("perldata", "Version Strings", contextElement, false);
    }
    else if (isFunc(contextElement)) {
      return PerlDocUtil.getPerlFuncDoc(contextElement);
    }
    else if (isOp(contextElement)) {
      return PerlDocUtil.getPerlOpDoc(contextElement);
    }
    return super.getCustomDocumentationElement(editor, file, contextElement);
  }

  protected static boolean isFunc(PsiElement element) {
    IElementType elementType = element.getNode().getElementType();
    return FORCE_AS_FUNC_TOKENSET.contains(elementType) ||
           !FORCE_AS_OPERATORS_TOKENSET.contains(elementType) && (
             PerlTokenSets.DEFAULT_KEYWORDS_TOKENSET.contains(elementType) ||
             element instanceof PerlSubNameElement && ((PerlSubNameElement)element).isBuiltIn()
           );
  }

  protected static boolean isOp(PsiElement element) {
    IElementType elementType = element.getNode().getElementType();
    return FORCE_AS_OPERATORS_TOKENSET.contains(elementType) ||
           !FORCE_AS_FUNC_TOKENSET.contains(elementType) && (
             PerlTokenSets.OPERATORS_TOKENSET.contains(elementType)
           );
  }

  /**
   * Generates documentation for sub declaration or definition
   */
  @Nullable
  private String generateDoc(@NotNull PerlSubElement perlSub) {
    String namespaceName = perlSub.getNamespaceName();
    String subName = perlSub.getSubName();
    if (StringUtil.isNotEmpty(namespaceName) && StringUtil.isNotEmpty(subName)) {
      PsiElement docElement = PerlDocUtil.resolveDoc(namespaceName, subName, perlSub, false);
      if (docElement != null) {
        return PodDocumentationProvider.doGenerateDoc(docElement);
      }
    }
    PsiElement podBlock = PerlDocUtil.findPrependingPodBlock(perlSub);
    return podBlock == null ? null : PerlDocUtil.renderPodElement(podBlock);
  }

  /**
   * Generates documentation for namespace definition
   */
  @Nullable
  private String generateDoc(@NotNull PerlNamespaceDefinitionElement namespaceDefinition) {
    String namespaceName = namespaceDefinition.getNamespaceName();
    if (StringUtil.isNotEmpty(namespaceName)) {
      PsiElement docElement = PerlDocUtil.resolveDoc(namespaceName, null, namespaceDefinition, false);
      if (docElement != null) {
        return PodDocumentationProvider.doGenerateDoc(docElement);
      }
    }
    PsiElement podBlock = PerlDocUtil.findPrependingPodBlock(namespaceDefinition);
    return podBlock == null ? null : PerlDocUtil.renderPodElement(podBlock);
  }

  /**
   * Generates documentation for a file
   */
  @Nullable
  private String generateDoc(@NotNull PerlFileImpl perlFile) {
    PsiFile nestedPodFile = perlFile.getViewProvider().getPsi(PodLanguage.INSTANCE);

    if (nestedPodFile != null) {
      PsiElement firstChild = nestedPodFile.getFirstChild();
      if (firstChild != null && (firstChild.getNextSibling() != null || PsiUtilCore.getElementType(firstChild) != POD_OUTER)) {
        return PerlDocUtil.renderPodFile((PodFileImpl)nestedPodFile);
      }
    }

    String filePackageName = perlFile.getFilePackageName();
    if (StringUtil.isNotEmpty(filePackageName)) {
      PsiElement docElement = PerlDocUtil.resolveDoc(filePackageName, null, perlFile, true);
      if (docElement != null) {
        return PodDocumentationProvider.doGenerateDoc(docElement);
      }
    }

    return null;
  }
}
