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

import com.intellij.codeInsight.TargetElementUtil;
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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.documentation.PerlDocUtil.SWITCH_DOC_LINK;
import static com.perl5.lang.perl.lexer.PerlTokenSets.HEREDOC_BODIES_TOKENSET;
import static com.perl5.lang.perl.lexer.PerlTokenSets.TAGS_TOKEN_SET;
import static com.perl5.lang.pod.lexer.PodElementTypes.POD_OUTER;

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
    if (object instanceof PsiElement) {
      return findPodElement((PsiElement)object);
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

    return findDocumentation(editor);
  }

  /**
   * @return a documenation element related to the perl element pointed by the current editor with caret on declaration or reference
   */
  @Nullable
  public static PsiElement findDocumentation(@NotNull Editor editor) {
    return findPodElement(
      TargetElementUtil.findTargetElement(editor, TargetElementUtil.ELEMENT_NAME_ACCEPTED | TargetElementUtil.REFERENCED_ELEMENT_ACCEPTED));
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
   * @return corresponding pod element for the {@code element} if any
   */
  @Contract("null->null")
  @Nullable
  private static PsiElement findPodElement(@Nullable PsiElement element) {
    if (element == null) {
      return null;
    }
    else if (element instanceof PerlBuiltInSubDefinition) {
      String subName = StringUtil.notNullize(((PerlBuiltInSubDefinition)element).getName());
      if ("default".equals(subName)) {
        return PerlDocUtil.resolveDescriptor(SWITCH_DOC_LINK, element, false);
      }
      else {
        return PerlDocUtil.getPerlFuncDocFromText(element, subName);
      }
    }
    else if (element instanceof PerlSubElement) {
      return findPodElement((PerlSubElement)element);
    }
    else if (element instanceof PerlFileImpl) {
      return findPodElement((PerlFileImpl)element);
    }
    else if (element instanceof PerlNamespaceDefinitionElement) {
      return findPodElement((PerlNamespaceDefinitionElement)element);
    }
    else if (element instanceof PerlVariable) {
      return PerlDocUtil.getPerlVarDoc((PerlVariable)element);
    }
    return null;
  }

  /**
   * Finds documentation for sub declaration or definition
   */
  @Nullable
  private static PsiElement findPodElement(@NotNull PerlSubElement perlSub) {
    String namespaceName = perlSub.getNamespaceName();
    String subName = perlSub.getSubName();
    if (StringUtil.isNotEmpty(namespaceName) && StringUtil.isNotEmpty(subName)) {
      PsiElement docElement = PerlDocUtil.resolveDoc(namespaceName, subName, perlSub, false);
      if (docElement != null) {
        return docElement;
      }
    }
    return PerlDocUtil.findPrependingPodBlock(perlSub);
  }

  /**
   * Finds  documentation for namespace definition
   */
  @Nullable
  private static PsiElement findPodElement(@NotNull PerlNamespaceDefinitionElement namespaceDefinition) {
    String namespaceName = namespaceDefinition.getNamespaceName();
    if (StringUtil.isNotEmpty(namespaceName)) {
      PsiElement docElement = PerlDocUtil.resolveDoc(namespaceName, null, namespaceDefinition, false);
      if (docElement != null) {
        return docElement;
      }
    }
    return PerlDocUtil.findPrependingPodBlock(namespaceDefinition);
  }

  /**
   * Finds  documentation for a file
   */
  @Nullable
  private static PsiElement findPodElement(@NotNull PerlFileImpl perlFile) {
    PsiFile nestedPodFile = perlFile.getViewProvider().getPsi(PodLanguage.INSTANCE);

    if (nestedPodFile != null) {
      PsiElement firstChild = nestedPodFile.getFirstChild();
      if (firstChild != null && (firstChild.getNextSibling() != null || PsiUtilCore.getElementType(firstChild) != POD_OUTER)) {
        return nestedPodFile;
      }
    }

    String filePackageName = perlFile.getFilePackageName();
    return StringUtil.isNotEmpty(filePackageName) ? PerlDocUtil.resolveDoc(filePackageName, null, perlFile, true) : null;
  }
}
