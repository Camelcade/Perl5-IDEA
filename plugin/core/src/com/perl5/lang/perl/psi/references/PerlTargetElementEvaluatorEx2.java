/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi.references;

import com.intellij.codeInsight.TargetElementEvaluatorEx;
import com.intellij.codeInsight.TargetElementEvaluatorEx2;
import com.intellij.codeInsight.TargetElementUtil;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.PerlVariableNameElement;
import com.perl5.lang.perl.psi.impl.PerlPolyNamedElement;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class PerlTargetElementEvaluatorEx2 extends TargetElementEvaluatorEx2 implements TargetElementEvaluatorEx {
  @Override
  public @Nullable PsiElement adjustReferenceOrReferencedElement(@NotNull PsiFile file,
                                                                 @NotNull Editor editor,
                                                                 int offset,
                                                                 int flags,
                                                                 @Nullable PsiElement refElement) {
    if (refElement == null) {
      PsiReference ref = TargetElementUtil.findReference(editor, offset);
      if (ref instanceof PerlCachingReference) {
        ResolveResult[] resolveResults = ((PsiPolyVariantReference)ref).multiResolve(false);
        if (resolveResults.length > 0) {
          return resolveResults[0].getElement();
        }
      }
    }
    return refElement;
  }


  @Override
  public @Nullable PsiElement getNamedElement(@NotNull PsiElement element) {
    return getLightNameIdentifierOwner(element);
  }

  public static @Nullable PsiElement getLightNameIdentifierOwner(@NotNull PsiElement element) {
    PerlPolyNamedElement<?> polyNamedElement = PsiTreeUtil.getParentOfType(element, PerlPolyNamedElement.class);
    if (polyNamedElement == null) {
      return null;
    }

    for (PerlDelegatingLightNamedElement<?> lightNamedElement : polyNamedElement.getLightElements()) {
      PsiElement identifier = lightNamedElement.getNameIdentifier();
      if (identifier != null && identifier.getTextRange().contains(element.getTextRange())) {
        return lightNamedElement;
      }
    }
    return null;
  }


  // fixme WTF?
  @Override
  public @Nullable PsiElement getElementByReference(@NotNull PsiReference ref, int flags) {
    return null;
  }

  @Override
  public boolean isIdentifierPart(PsiFile file, @NotNull CharSequence text, int offset) {
    return file.findElementAt(offset) instanceof PerlVariableNameElement;
  }
}
