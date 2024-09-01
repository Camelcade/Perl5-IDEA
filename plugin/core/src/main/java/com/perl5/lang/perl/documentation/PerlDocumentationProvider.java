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

package com.perl5.lang.perl.documentation;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.extensions.mojo.MojoLightDelegatingSubDefinition;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlTokenSets;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlBuiltInSubDefinition;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.idea.documentation.PodDocumentationProvider;
import com.perl5.lang.pod.parser.psi.PodLinkDescriptor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.documentation.PerlDocUtil.SWITCH_DOC_LINK;
import static com.perl5.lang.perl.lexer.PerlTokenSets.*;
import static com.perl5.lang.perl.parser.MooseParserExtension.*;
import static com.perl5.lang.perl.util.PerlPackageUtil.FUNCTION_PARAMETERS;
import static com.perl5.lang.pod.lexer.PodElementTypes.POD_OUTER;

public class PerlDocumentationProvider extends PerlDocumentationProviderBase implements PerlElementTypes {
  private static final TokenSet FORCE_AS_OPERATORS_TOKENSET = TokenSet.orSet(
    HEREDOC_BODIES_TOKENSET,
    SPECIAL_STRING_TOKENS,
    TokenSet.create(
      STRING_CHAR_NAME,

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
      RESERVED_QR,

      LEFT_ANGLE,
      RIGHT_ANGLE
    )
  );
  private static final TokenSet FORCE_AS_FUNC_TOKENSET = TokenSet.orSet(TAGS_TOKEN_SET, TokenSet.create(BLOCK_NAME, OPERATOR_FILETEST));
  private static final String SECTION_DESCRIPTION = "DESCRIPTION";

  @Override
  public @Nullable String getQuickNavigateInfo(PsiElement element, PsiElement originalElement) {
    if (isWrongElement(originalElement)) {
      return null;
    }

    if (originalElement instanceof PerlVariableNameElement) {
      return getQuickNavigateInfo(element, originalElement.getParent());
    }
    var perlValue = PerlValuesManager.from(originalElement);
    return !perlValue.isUnknown() ? perlValue.getPresentableText() : null;
  }

  private static boolean isWrongElement(@Nullable PsiElement element) {
    return element == null || !element.isValid() || element.getLanguage() != PerlLanguage.INSTANCE;
  }

  @Override
  public PsiElement getDocumentationElementForLookupItem(PsiManager psiManager, Object object, PsiElement element) {
    if (object instanceof MojoLightDelegatingSubDefinition delegatingSubDefinition && delegatingSubDefinition.isValid()) {
      return findPodElement(ObjectUtils.notNull(delegatingSubDefinition.getTargetSubElement(), delegatingSubDefinition));
    }
    if (object instanceof PsiElement psiElement && psiElement.isValid()) {
      return findPodElement(psiElement);
    }
    return super.getDocumentationElementForLookupItem(psiManager, object, element);
  }

  @Override
  public @Nullable PsiElement getCustomDocumentationElement(@NotNull Editor editor,
                                                            @NotNull PsiFile file,
                                                            @Nullable PsiElement contextElement,
                                                            int targetOffset) {
    if (isWrongElement(contextElement)) {
      return null;
    }

    PsiElement functionParametersDoc = computeFunctionParametersDoc(contextElement);
    if (functionParametersDoc != null) {
      return functionParametersDoc;
    }

    IElementType elementType = PsiUtilCore.getElementType(contextElement);

    if (elementType == RESERVED_ASYNC) {
      return PerlDocUtil.resolveDescriptor(PodLinkDescriptor.create("Future::AsyncAwait", "async"), contextElement, false);
    }
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
    else if (elementType == STRING_SPECIAL_BACKREF) {
      return PerlDocUtil.resolveDoc(PerlDocUtil.PERL_OP, "the replacement of s///", contextElement, true);
    }
    else if (PerlDocUtil.isNumericArgumentToOperator(contextElement)) {
      return PerlDocUtil.getPerlOpDoc(contextElement.getParent().getFirstChild());
    }
    else if (isOp(contextElement)) {
      return PerlDocUtil.getPerlOpDoc(contextElement);
    }

    return null;
  }

  /**
   * @return some {@code Function::Parameters} specific documentation if available
   */
  private @Nullable PsiElement computeFunctionParametersDoc(@NotNull PsiElement contextElement) {
    IElementType elementType = PsiUtilCore.getElementType(contextElement);
    if (elementType == RESERVED_AFTER_FP) {
      return PerlDocUtil.resolveDescriptor(PodLinkDescriptor.create(FUNCTION_PARAMETERS, KEYWORD_AFTER), contextElement, false);
    }
    if (elementType == RESERVED_BEFORE_FP) {
      return PerlDocUtil.resolveDescriptor(PodLinkDescriptor.create(FUNCTION_PARAMETERS, KEYWORD_BEFORE), contextElement, false);
    }
    if (elementType == RESERVED_AROUND_FP) {
      return PerlDocUtil.resolveDescriptor(PodLinkDescriptor.create(FUNCTION_PARAMETERS, KEYWORD_AROUND), contextElement, false);
    }
    if (elementType == RESERVED_AUGMENT_FP) {
      return PerlDocUtil.resolveDescriptor(PodLinkDescriptor.create(FUNCTION_PARAMETERS, KEYWORD_AUGMENT), contextElement, false);
    }
    if (elementType == RESERVED_FUN || elementType == RESERVED_METHOD || elementType == RESERVED_METHOD_FP) {
      return PerlDocUtil.resolveDescriptor(PodLinkDescriptor.create(FUNCTION_PARAMETERS, SECTION_DESCRIPTION), contextElement, false);
    }
    if (elementType == RESERVED_OVERRIDE_FP) {
      return PerlDocUtil.resolveDescriptor(PodLinkDescriptor.create(FUNCTION_PARAMETERS, KEYWORD_OVERRIDE), contextElement, false);
    }
    IElementType parentElementType = PsiUtilCore.getElementType(contextElement.getParent());
    if (elementType == COLON) {
      if (parentElementType == METHOD_SIGNATURE_INVOCANT) {
        return PerlDocUtil
          .resolveDescriptor(PodLinkDescriptor.create(FUNCTION_PARAMETERS, "Simple parameter lists"), contextElement, false);
      }
      else if (parentElementType == AROUND_SIGNATURE_INVOCANTS) {
        return PerlDocUtil.resolveDescriptor(PodLinkDescriptor.create(FUNCTION_PARAMETERS, KEYWORD_AROUND), contextElement, false);
      }
      else if (parentElementType == SIGNATURE_ELEMENT) {
        return PerlDocUtil.resolveDescriptor(PodLinkDescriptor.create(FUNCTION_PARAMETERS, "Named parameters"), contextElement, false);
      }
    }
    // fixme these may occur in regular sub signature
    if (elementType == OPERATOR_ASSIGN && parentElementType == SIGNATURE_ELEMENT) {
      return PerlDocUtil.resolveDescriptor(PodLinkDescriptor.create(FUNCTION_PARAMETERS, "Default arguments"), contextElement, false);
    }
    if (PerlTokenSets.SIGILS.contains(elementType) && parentElementType == SUB_SIGNATURE_ELEMENT_IGNORE) {
      return PerlDocUtil.resolveDescriptor(PodLinkDescriptor.create(FUNCTION_PARAMETERS, "Unnamed parameters"), contextElement, false);
    }
    return null;
  }

  @Override
  public @Nullable @Nls String generateDoc(PsiElement element, @Nullable PsiElement originalElement) {
    if (element instanceof MojoLightDelegatingSubDefinition delegatingSubDefinition) {
      element = ObjectUtils.notNull(delegatingSubDefinition.getTargetSubElement(), element);
    }
    return PodDocumentationProvider.doGenerateDoc(findPodElement(element));
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
  public static @Nullable PsiElement findPodElement(@Nullable PsiElement element) {
    return switch (element) {
      case null -> null;
      case PerlBuiltInSubDefinition definition -> {
        String subName = StringUtil.notNullize(definition.getName());
        if ("default".equals(subName)) {
          yield PerlDocUtil.resolveDescriptor(SWITCH_DOC_LINK, element, false);
        }
        else {
          yield PerlDocUtil.getPerlFuncDocFromText(element, subName);
        }
      }
      case PerlSubElement subElement -> findPodElement(subElement);
      case PerlFileImpl file -> findPodElement(file);
      case PerlNamespaceDefinitionElement definitionElement -> findPodElement(definitionElement);
      case PerlVariable variable -> PerlDocUtil.getPerlVarDoc(variable);
      default -> null;
    };
  }

  /**
   * Finds documentation for sub declaration or definition
   */
  private static @Nullable PsiElement findPodElement(@NotNull PerlSubElement perlSub) {
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
  private static @Nullable PsiElement findPodElement(@NotNull PerlNamespaceDefinitionElement namespaceDefinition) {
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
  private static @Nullable PsiElement findPodElement(@NotNull PerlFileImpl perlFile) {
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
