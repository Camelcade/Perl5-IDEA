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

package com.perl5.lang.perl.idea.findusages;

import com.intellij.codeInsight.TargetElementUtil;
import com.intellij.codeInsight.highlighting.HighlightUsagesHandlerBase;
import com.intellij.codeInsight.highlighting.HighlightUsagesHandlerFactory;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.*;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.Consumer;
import com.perl5.lang.perl.PerlLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hurricup on 04.11.2016.
 */
public class PerlHighlightUsagesHandlerFactory implements HighlightUsagesHandlerFactory {

  @Nullable
  @Override
  public HighlightUsagesHandlerBase createHighlightUsagesHandler(Editor editor, PsiFile file) {
    int offset = TargetElementUtil.adjustOffset(file, editor.getDocument(), editor.getCaretModel().getOffset());
    PsiElement target = file.getViewProvider().findElementAt(offset, PerlLanguage.INSTANCE);
    return target == null ? null : new PerlHighlightUsagesHandler(editor, file, target, offset);
  }

  private static class PerlHighlightUsagesHandler extends HighlightUsagesHandlerBase<PsiElement> {
    private final PsiElement myElement;
    private final int myAdjustedOffset;

    public PerlHighlightUsagesHandler(Editor editor, PsiFile file, @NotNull PsiElement element, int adjustedOffset) {
      super(editor, file);
      myElement = element;
      myAdjustedOffset = adjustedOffset;
    }

    @Override
    public List<PsiElement> getTargets() {
      PsiElement namedElement =
        TargetElementUtil.getInstance().getNamedElement(myElement, myAdjustedOffset - myElement.getNode().getStartOffset());
      if (namedElement != null) {
        PsiReference reference = ReferencesSearch.search(namedElement, new LocalSearchScope(myFile)).findFirst();
        if (reference instanceof PsiPolyVariantReference) {
          List<PsiElement> result = new ArrayList<>();

          for (ResolveResult resolveResult : ((PsiPolyVariantReference)reference).multiResolve(false)) {
            PsiElement targetElement = resolveResult.getElement();
            if (targetElement != null) {
              result.add(targetElement);
            }
          }


          return result;
        }
      }
      else {
        PsiReference reference = TargetElementUtil.findReference(myEditor, myAdjustedOffset);
        if (reference instanceof PsiPolyVariantReference) {
          List<PsiElement> result = new ArrayList<>();

          for (ResolveResult resolveResult : ((PsiPolyVariantReference)reference).multiResolve(false)) {
            PsiElement element = resolveResult.getElement();
            if (element != null) {
              result.add(element);
            }
          }

          return result;
        }
        else if (reference != null) {
          PsiElement target = reference.resolve();
          if (target != null) {
            return Collections.singletonList(target);
          }
        }
      }
      return null;
    }

    @Override
    protected void selectTargets(List<PsiElement> targets, Consumer<List<PsiElement>> selectionConsumer) {
    }

    @Override
    public void computeUsages(List<PsiElement> targets) {
      if (targets == null) {
        return;
      }
      for (PsiElement target : targets) {
        if (myFile.equals(target.getContainingFile()) && target instanceof PsiNameIdentifierOwner) {
          PsiElement nameIdentifier = ((PsiNameIdentifierOwner)target).getNameIdentifier();
          if (nameIdentifier != null) {
            myWriteUsages.add(ElementManipulators.getValueTextRange(nameIdentifier).shiftRight(nameIdentifier.getNode().getStartOffset()));
          }
        }

        for (PsiReference reference : ReferencesSearch.search(target, new LocalSearchScope(myFile)).findAll()) {
          myReadUsages.add(reference.getRangeInElement().shiftRight(reference.getElement().getNode().getStartOffset()));
        }
      }
    }
  }
}
