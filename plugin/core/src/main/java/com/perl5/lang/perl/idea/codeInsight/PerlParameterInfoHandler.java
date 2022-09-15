/*
 * Copyright 2015-2021 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.codeInsight;

import com.intellij.lang.parameterInfo.*;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlTokenSets;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlCompositeElementImpl;
import com.perl5.lang.perl.psi.impl.PsiPerlCallArgumentsImpl;
import com.perl5.lang.perl.psi.impl.PsiPerlCommaSequenceExprImpl;
import com.perl5.lang.perl.psi.impl.PsiPerlParenthesisedExprImpl;
import com.perl5.lang.perl.psi.mixins.PerlMethodMixin;
import com.perl5.lang.perl.psi.references.PerlImplicitDeclarationsService;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.psi.utils.PerlResolveUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class PerlParameterInfoHandler implements ParameterInfoHandler<PsiPerlCallArgumentsImpl, PerlParameterInfo>, PerlElementTypes {
  @Override
  public @Nullable PsiPerlCallArgumentsImpl findElementForParameterInfo(@NotNull CreateParameterInfoContext context) {
    PsiPerlCallArgumentsImpl callArguments = findCallArguments(context);

    if (callArguments != null) {
      PerlParameterInfo[] methodParameterInfos = getMethodCallArguments(callArguments);
      if (methodParameterInfos == null || methodParameterInfos.length == 0) {
        return null;
      }
      markActiveParameters(callArguments, methodParameterInfos, context.getOffset());
      context.setItemsToShow(methodParameterInfos);
    }

    return callArguments;
  }

  @Override
  public void showParameterInfo(@NotNull PsiPerlCallArgumentsImpl container, @NotNull CreateParameterInfoContext context) {
    context.showHint(container, container.getTextOffset(), this);
  }

  @Override
  public @Nullable PsiPerlCallArgumentsImpl findElementForUpdatingParameterInfo(@NotNull UpdateParameterInfoContext context) {
    return findCallArguments(context);
  }

  @Override
  public void updateParameterInfo(@NotNull PsiPerlCallArgumentsImpl container, @NotNull UpdateParameterInfoContext context) {
    markActiveParameters(container, (PerlParameterInfo[])context.getObjectsToView(), context.getOffset());
  }

  @Override
  public void updateUI(PerlParameterInfo parameterInfo, @NotNull ParameterInfoUIContext context) {
    parameterInfo.setUpUIPresentation(context);
  }

  /**
   * Method marks parameters as active/inactive
   *
   * @param container      arguments container
   * @param parameterInfos array of parameter infos
   */
  private static void markActiveParameters(@NotNull PsiPerlCallArgumentsImpl container, PerlParameterInfo[] parameterInfos, int offset) {
    for (PerlParameterInfo parameterInfo : parameterInfos) {
      parameterInfo.setSelected(false);
    }

    markActiveparametersRecursively(container.getFirstChild(), parameterInfos, 0, offset);
  }

  private static int markActiveparametersRecursively(PsiElement element, PerlParameterInfo[] parameterInfos, int currentIndex, int offset) {
    while (element != null) {
      if (parameterInfos.length <= currentIndex) {
        return currentIndex;
      }

      IElementType elementType = PsiUtilCore.getElementType(element);
      if (element instanceof PsiPerlParenthesisedExprImpl || element instanceof PsiPerlCommaSequenceExprImpl) {
        currentIndex = markActiveparametersRecursively(element.getFirstChild(), parameterInfos, currentIndex, offset);
      }
      else {
        if (isComma(elementType)) {
          if (parameterInfos[currentIndex].getArgument().getContextType() == PerlContextType.SCALAR) {
            currentIndex++;
          }
        }
      }

      if (element.getTextRange().getEndOffset() >= offset) {
        if (element instanceof PerlCompositeElementImpl) {
          PerlContextType valueContextType = PerlContextType.from(element);

          if (valueContextType == PerlContextType.SCALAR) {
            if (currentIndex < parameterInfos.length) {
              parameterInfos[currentIndex].setSelected(true);
            }
            return parameterInfos.length;
          }
          else if (valueContextType == PerlContextType.LIST) // consumes all arguments to the end
          {
            while (currentIndex < parameterInfos.length) {
              parameterInfos[currentIndex++].setSelected(true);
            }
            return currentIndex;
          }
        }

        // space, comma or smth
        if (currentIndex < parameterInfos.length) {
          parameterInfos[currentIndex].setSelected(true);
        }
        return parameterInfos.length;
      }
      else if (elementType == SUB_EXPR && element.getFirstChild() instanceof PsiPerlBlock) {
        PsiElement nextSibling = PerlPsiUtil.getNextSignificantSibling(element);
        if (!isComma(PsiUtilCore.getElementType(nextSibling))) {
          currentIndex++;
        }
      }
      else if (isPrintHandleArgument(element)) {
        currentIndex++;
      }

      element = element.getNextSibling();
    }
    return currentIndex;
  }

  private static boolean isPrintHandleArgument(@NotNull PsiElement psiElement) {
    IElementType elementType = PsiUtilCore.getElementType(psiElement);
    if (elementType != BLOCK && elementType != PERL_HANDLE_EXPR && elementType != SCALAR_VARIABLE) {
      return false;
    }
    PsiElement parent = psiElement.getParent();
    if (!(parent instanceof PsiPerlCommaSequenceExpr)) {
      return false;
    }

    PsiElement grandParent = parent.getParent();
    if (!(grandParent instanceof PsiPerlCallArguments)) {
      return false;
    }

    if (!(grandParent.getParent() instanceof PsiPerlPrintExpr)) {
      return false;
    }

    PsiElement nextSibling = PerlPsiUtil.getNextSignificantSibling(psiElement);
    return !isComma(PsiUtilCore.getElementType(nextSibling));
  }

  private static boolean isComma(IElementType elementType) {
    return elementType == FAT_COMMA || elementType == COMMA;
  }

  private static @Nullable PsiPerlCallArgumentsImpl findCallArguments(ParameterInfoContext context) {
    PsiPerlCallArgumentsImpl callArguments =
      ParameterInfoUtils.findParentOfType(context.getFile(), context.getOffset(), PsiPerlCallArgumentsImpl.class);
    if (callArguments != null || context.getOffset() == 0) {
      return callArguments;
    }

    callArguments = ParameterInfoUtils.findParentOfType(context.getFile(), context.getOffset() - 1, PsiPerlCallArgumentsImpl.class);
    if (callArguments != null) {
      return callArguments;
    }

    return ParameterInfoUtils.findParentOfType(context.getFile(), context.getOffset() + 1, PsiPerlCallArgumentsImpl.class);
  }

  private static @Nullable PerlParameterInfo[] getTargetParameterInfo(@Nullable PsiElement target) {
    if (!(target instanceof PerlSubDefinitionElement)) {
      return null;
    }
    return PerlParameterInfo.wrapArguments(((PerlSubDefinitionElement)target).getSubArgumentsListWithoutSelf());
  }

  private static @Nullable PerlParameterInfo[] getMethodCallArguments(@NotNull PsiPerlCallArgumentsImpl arguments) {
    PsiElement run = arguments.getPrevSibling();
    while (run != null) {
      if (run instanceof PerlMethodMixin) {
        PerlSubNameElement subNameElement = ((PerlMethodMixin)run).getSubNameElement();
        if (subNameElement == null) {
          break;
        }

        Ref<PerlParameterInfo[]> parameterInfoRef = Ref.create();

        PerlResolveUtil.processResolveTargets((target, reference) -> {
          PerlParameterInfo[] parameterInfos = getTargetParameterInfo(target);
          if (parameterInfos != null) {
            parameterInfoRef.set(parameterInfos);
            return false;
          }
          return true;
        }, subNameElement);

        if (!parameterInfoRef.isNull()) {
          return parameterInfoRef.get();
        }
      }
      else if (PerlTokenSets.CUSTOM_EXPR_KEYWORDS.contains(PsiUtilCore.getElementType(run))) {
        return getTargetParameterInfo(PerlImplicitDeclarationsService.getInstance(run.getProject()).getCoreSub(run.getText()));
      }
      run = run.getPrevSibling();
    }
    return null;
  }
}
