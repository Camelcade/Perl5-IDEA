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

import com.intellij.codeInsight.TargetElementUtil;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.idea.codeInsight.typeInferrence.value.PerlValue;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlTokenSets;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.properties.PerlPodAwareElement;
import com.perl5.lang.perl.psi.properties.PerlValuableEntity;
import com.perl5.lang.perl.psi.references.PerlTargetElementEvaluatorEx2;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.parser.psi.PodDocumentPattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.codeInsight.TargetElementUtil.ELEMENT_NAME_ACCEPTED;
import static com.intellij.codeInsight.TargetElementUtil.REFERENCED_ELEMENT_ACCEPTED;
import static com.perl5.lang.perl.lexer.PerlTokenSets.HEREDOC_BODIES_TOKENSET;

/**
 * Created by hurricup on 26.03.2016.
 */
public class PerlDocumentationProvider extends PerlDocumentationProviderBase implements PerlElementTypes, PerlElementPatterns {
  private static final TokenSet myForceAsOp = TokenSet.orSet(
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
  private static final TokenSet myForceAsFunc = TokenSet.create(
    BLOCK_NAME,
    TAG,
    TAG_END,
    TAG_DATA,
    OPERATOR_FILETEST
  );

  @Nullable
  @Override
  public String getQuickNavigateInfo(PsiElement element, PsiElement originalElement) {
    if (originalElement instanceof PerlVariableNameElement) {
      return getQuickNavigateInfo(element, originalElement.getParent());
    }
    if (originalElement instanceof PerlValuableEntity) {
      return PerlValue.from((PerlValuableEntity)originalElement).getPresentableText();
    }
    return super.getQuickNavigateInfo(element, originalElement);
  }

  @Nullable
  @Override
  public PsiElement getCustomDocumentationElement(@NotNull Editor editor, @NotNull PsiFile file, @Nullable PsiElement contextElement) {
    if (contextElement == null || contextElement.getLanguage() != PerlLanguage.INSTANCE) {
      return null;
    }

    IElementType elementType = contextElement.getNode().getElementType();

    if (contextElement instanceof PerlVariable) {
      return PerlDocUtil.getPerlVarDoc((PerlVariable)contextElement); // fixme try to search doc in package or declaration
    }
    else if (elementType == REGEX_MODIFIER) {
      return PerlDocUtil.getRegexModifierDoc(contextElement);
    }
    else if (elementType == REGEX_TOKEN) {
      return PerlDocUtil.resolveDocLink("perlretut", contextElement);
    }
    else if (elementType == VERSION_ELEMENT) {
      return PerlDocUtil.resolveDocLink("perldata/\"Version Strings\"", contextElement);
    }
    else if (isFunc(contextElement)) {
      return PerlDocUtil.getPerlFuncDoc(contextElement);
    }
    else if (isOp(contextElement)) {
      return PerlDocUtil.getPerlOpDoc(contextElement);
    }
    else if (contextElement instanceof PerlSubNameElement) {
      PsiElement targetElement = findInlinePodElement(editor);
      if (targetElement != null) {
        return targetElement;
      }

      String packageName = ((PerlSubNameElement)contextElement).getPackageName();
      String subName = ((PerlSubNameElement)contextElement).getName();
      if (StringUtil.isNotEmpty(subName)) {
        PsiElement result = null;

        // search by link
        if (StringUtil.isNotEmpty(packageName) && !StringUtil.equals(PerlPackageUtil.MAIN_PACKAGE, packageName)) {
          result = PerlDocUtil.resolveDocLink(packageName + "/" + ((PerlSubNameElement)contextElement).getName(), contextElement);
        }

        // not found or main::
        if (result == null) {
          PsiElement target = null;

          if (contextElement.getParent() instanceof PerlSubElement) {
            target = contextElement.getParent();
          }
          else {
            PsiReference reference = contextElement.getReference();
            if (reference != null) {
              target = reference.resolve();
            }
          }

          if (target != null) {
            PsiFile targetFile = target.getContainingFile();
            if (targetFile != null) {
              PsiFile podFile = targetFile.getViewProvider().getPsi(PodLanguage.INSTANCE);
              if (podFile != null) {
                result = PerlDocUtil.searchPodElement(targetFile, PodDocumentPattern.headingAndItemPattern(subName));
              }
            }
          }
        }

        if (result != null) {
          return result;
        }
      }
    }
    else if (contextElement instanceof PerlNamespaceElement) {
      PsiElement targetElement = findInlinePodElement(editor);
      if (targetElement != null) {
        return targetElement;
      }


      String packageName = ((PerlNamespaceElement)contextElement).getCanonicalName();

      if (StringUtil.equals(PerlPackageUtil.SUPER_PACKAGE, packageName)) {
        return PerlDocUtil.resolveDocLink("perlobj/Inheritance", contextElement);
      }
      else if (StringUtil.isNotEmpty(packageName)) {
        return PerlDocUtil.resolveDocLink(packageName, contextElement);
      }
    }

    if (PerlTargetElementEvaluatorEx2.getLightNameIdentifierOwner(contextElement) != null) {
      PsiElement targetElement = findInlinePodElement(editor);
      if (targetElement != null) {
        return targetElement;
      }
    }

    return super.getCustomDocumentationElement(editor, file, contextElement);
  }


  @Nullable
  protected PsiElement findInlinePodElement(@NotNull Editor editor) {
    PsiElement targetElement = findTargetElement(editor, REFERENCED_ELEMENT_ACCEPTED | ELEMENT_NAME_ACCEPTED);
    if (targetElement instanceof PerlPodAwareElement) {
      targetElement = ((PerlPodAwareElement)targetElement).getPodAnchor();
    }
    PsiElement podBlock = PerlDocUtil.findPrependingPodBlock(targetElement);
    if (podBlock != null) {
      return podBlock;
    }
    return null;
  }

  /**
   * This is a copy of {@link TargetElementUtil#findTargetElement(com.intellij.openapi.editor.Editor, int)} without obsolete EDT assertion
   * fixme remove after assertion removal
   */
  @Nullable
  private static PsiElement findTargetElement(Editor editor, int flags) {

    int offset = editor.getCaretModel().getOffset();
    final PsiElement result = TargetElementUtil.getInstance().findTargetElement(editor, flags, offset);
    if (result != null) {
      return result;
    }

    int expectedCaretOffset = editor instanceof EditorEx ? ((EditorEx)editor).getExpectedCaretOffset() : offset;
    if (expectedCaretOffset != offset) {
      return TargetElementUtil.getInstance().findTargetElement(editor, flags, expectedCaretOffset);
    }
    return null;
  }

  protected static boolean isFunc(PsiElement element) {
    IElementType elementType = element.getNode().getElementType();
    return myForceAsFunc.contains(elementType) ||
           !myForceAsOp.contains(elementType) && (
             PerlTokenSets.DEFAULT_KEYWORDS_TOKENSET.contains(elementType) ||
             element instanceof PerlSubNameElement && ((PerlSubNameElement)element).isBuiltIn()
           );
  }

  protected static boolean isOp(PsiElement element) {
    IElementType elementType = element.getNode().getElementType();
    return myForceAsOp.contains(elementType) ||
           !myForceAsFunc.contains(elementType) && (
             PerlTokenSets.OPERATORS_TOKENSET.contains(elementType)
           );
  }
}
