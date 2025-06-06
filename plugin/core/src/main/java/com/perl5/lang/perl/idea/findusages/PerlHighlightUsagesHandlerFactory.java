/*
 * Copyright 2015-2025 Alexandr Evstigneev
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
import com.intellij.codeInsight.highlighting.ReadWriteAccessDetector;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageEditorUtil;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.Consumer;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.parser.PerlIdentifierRangeProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PerlHighlightUsagesHandlerFactory implements HighlightUsagesHandlerFactory {

  @Override
  public @Nullable HighlightUsagesHandlerBase<?> createHighlightUsagesHandler(@NotNull Editor editor, @NotNull PsiFile file) {
    if (!file.isValid()) {
      return null;
    }
    Project project = file.getProject();
    int offset = TargetElementUtil.adjustOffset(file, editor.getDocument(), editor.getCaretModel().getOffset());
    InjectedLanguageManager injectedLanguageManager = InjectedLanguageManager.getInstance(project);
    PsiElement target = injectedLanguageManager.findInjectedElementAt(file, offset);
    if (target != null && target.getLanguage().isKindOf(PerlLanguage.INSTANCE)) {
      //noinspection deprecation
      editor = com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil.getEditorForInjectedLanguageNoCommit(editor, file, offset);
      file = target.getContainingFile();
      offset = TargetElementUtil.adjustOffset(file, editor.getDocument(), editor.getCaretModel().getOffset());
    }
    else {
      target = file.getViewProvider().findElementAt(offset, PerlLanguage.INSTANCE);
    }

    return target == null ? null : new PerlHighlightUsagesHandler(editor, file, target, offset);
  }

  private static class PerlHighlightUsagesHandler extends HighlightUsagesHandlerBase<PsiElement> {
    private final @NotNull PsiElement myElement;
    private final int myOriginalEditorOffset;
    private final @NotNull Editor myOriginalEditor;

    public PerlHighlightUsagesHandler(@NotNull Editor editor, @NotNull PsiFile file, @NotNull PsiElement element, int offsetInTopEditor) {
      super(InjectedLanguageEditorUtil.getTopLevelEditor(editor),
            InjectedLanguageManager.getInstance(file.getProject()).getTopLevelFile(file));
      myElement = element;
      myOriginalEditorOffset = offsetInTopEditor;
      myOriginalEditor = editor;
    }

    @Override
    public @NotNull List<PsiElement> getTargets() {
      PsiElement namedElement = myElement.isValid() ?
        TargetElementUtil.getInstance().getNamedElement(myElement, myOriginalEditorOffset - myElement.getNode().getStartOffset()) : null;
      if (namedElement != null) {
        return collectReferenceTargets(ReferencesSearch.search(namedElement, new LocalSearchScope(myFile)).findFirst());
      }
      else {
        return collectReferenceTargets(TargetElementUtil.findReference(myOriginalEditor, myOriginalEditorOffset));
      }
    }

    protected @NotNull List<PsiElement> collectReferenceTargets(@Nullable PsiReference reference) {
      if (reference instanceof PsiPolyVariantReference polyVariantReference) {
        List<PsiElement> result = new ArrayList<>();

        for (ResolveResult resolveResult : polyVariantReference.multiResolve(false)) {
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
      return Collections.emptyList();
    }

    @Override
    protected void selectTargets(@NotNull List<? extends PsiElement> targets,
                                 @NotNull Consumer<? super List<? extends PsiElement>> selectionConsumer) {
      selectionConsumer.consume(targets);
    }

    @Override
    public void computeUsages(@NotNull List<? extends PsiElement> targets) {
      for (PsiElement target : targets) {
        if (myFile.equals(target.getContainingFile()) && target instanceof PsiNameIdentifierOwner nameIdentifierOwner) {
          PsiElement nameIdentifier = nameIdentifierOwner.getNameIdentifier();
          if (nameIdentifier != null) {
            TextRange rangeInIdentifier = target instanceof PerlIdentifierRangeProvider identifierRangeProvider
              ? identifierRangeProvider.getRangeInIdentifier()
                                          : ElementManipulators.getValueTextRange(nameIdentifier);
            myWriteUsages.add(computeHighlightingRange(nameIdentifier, rangeInIdentifier));
          }
        }

        for (PsiReference reference : ReferencesSearch.search(target, new LocalSearchScope(myFile)).findAll()) {
          TextRange rangeToHighlight = computeHighlightingRange(reference.getElement(), reference.getRangeInElement());
          ReadWriteAccessDetector detector = ReadWriteAccessDetector.findDetector(target);
          if (detector == null || detector.getReferenceAccess(target, reference) == ReadWriteAccessDetector.Access.Read) {
            myReadUsages.add(rangeToHighlight);
          }
          else {
            myWriteUsages.add(rangeToHighlight);
          }
        }
      }
    }

    protected @NotNull TextRange computeHighlightingRange(PsiElement element, TextRange rangeInElement) {
      TextRange rangeInFile = rangeInElement.shiftRight(element.getTextRange().getStartOffset());
      return InjectedLanguageManager.getInstance(element.getProject()).injectedToHost(element, rangeInFile);
    }
  }
}
