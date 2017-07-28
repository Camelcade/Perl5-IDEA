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

package com.perl5.lang.perl.psi.references;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.PerlHeredocOpener;
import com.perl5.lang.perl.psi.PerlHeredocTerminatorElement;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.Nullable;

public class PerlHeredocReference extends PerlCachingReference<PerlHeredocTerminatorElement> {

  public PerlHeredocReference(PerlHeredocTerminatorElement psiElement) {
    super(psiElement);
  }

  @Override
  protected ResolveResult[] resolveInner(boolean incompleteCode) {
    PsiElement run = getElement().getPrevSibling();

    // skip heading spaces of indented terminator and new-line for empty heredoc
    while (run instanceof PsiWhiteSpace) {
      run = run.getPrevSibling();
    }

    // got here-doc body before terminator
    if (run instanceof PerlHeredocElementImpl) {
      run = run.getPrevSibling();    // moving to newline before heredoc

      if (run instanceof PsiWhiteSpace) {
        run = run.getPrevSibling();
      }
    }

    if (run instanceof PerlHeredocTerminatorElement)    // sequential here-docs
    {
      PsiReference reference = run.getReference();
      if (reference != null) {
        PsiElement prevOpener = reference.resolve();
        if (prevOpener != null) {
          HeredocSeeker seeker = new HeredocSeeker(run.getNode().getStartOffset(), prevOpener);
          PerlPsiUtil.iteratePsiElementsRight(prevOpener, seeker);
          return PsiElementResolveResult.createResults(seeker.getResult());
        }
      }
    }
    else if (run != null)    // first here-doc in a row
    {
      final PsiDocumentManager manager = PsiDocumentManager.getInstance(run.getProject());
      final PsiFile file = run.getContainingFile();
      final Document document = manager.getDocument(file);

      if (document != null) {
        int seekEndOffset = run.getNextSibling().getNode().getStartOffset() - 1;
        int lineNumber = document.getLineNumber(seekEndOffset);
        PsiElement firstLineElement = file.findElementAt(document.getLineStartOffset(lineNumber));
        if (firstLineElement != null) {
          HeredocSeeker seeker = new HeredocSeeker(seekEndOffset, null);
          PerlPsiUtil.iteratePsiElementsRight(firstLineElement, seeker);
          if (seeker.getResult() != null) {
            return PsiElementResolveResult.createResults(seeker.getResult());
          }
        }
      }
    }

    return ResolveResult.EMPTY_ARRAY;
  }

  @Override
  public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
    // fixme this is not invoked!
    boolean appendNewLine = StringUtil.equals(myElement.getText(), "\n");
    PsiElement result = super.handleElementRename(newElementName);

    if (appendNewLine && result.getParent() != null) {
      PsiElement newLineElement = PerlElementFactory.createNewLine(myElement.getProject());
      result.getParent().addAfter(result, newLineElement);
    }

    return result;
  }

  static private class HeredocSeeker extends PerlPsiUtil.HeredocProcessor {
    protected PerlHeredocOpener myResult = null;
    protected PsiElement myAnchor = null;

    public HeredocSeeker(int lineEndOffset, @Nullable PsiElement anchor) {
      super(lineEndOffset);
      myAnchor = anchor;
    }

    @Override
    public boolean process(PsiElement element) {
      if (element == null || element.getNode().getStartOffset() >= lineEndOffset) {
        return false;
      }
      if (element != myAnchor && element instanceof PerlHeredocOpener) {
        myResult = (PerlHeredocOpener)element;
        return false;
      }
      return true;
    }

    public PerlHeredocOpener getResult() {
      return myResult;
    }
  }
}
