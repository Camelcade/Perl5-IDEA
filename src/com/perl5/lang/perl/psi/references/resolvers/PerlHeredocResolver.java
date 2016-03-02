/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi.references.resolvers;

import com.intellij.openapi.editor.Document;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.perl5.lang.perl.psi.PerlHeredocOpener;
import com.perl5.lang.perl.psi.PerlHeredocTerminatorElement;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import com.perl5.lang.perl.psi.references.PerlHeredocReference;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;


/**
 * Created by hurricup on 02.03.2016.
 */
public class PerlHeredocResolver implements ResolveCache.AbstractResolver<PerlHeredocReference, PerlHeredocOpener>
{
	@Override
	public PerlHeredocOpener resolve(@NotNull PerlHeredocReference perlHeredocReference, boolean incompleteCode)
	{
		PsiElement run = perlHeredocReference.getElement();
		if ((run = run.getPrevSibling()) != null) // new line or here-doc
		{
			if (run instanceof PerlHeredocElementImpl)
			{
				run = run.getPrevSibling();    // moving to newline before heredoc
				if (run == null)
				{
					return null;
				}
			}

			PsiElement predecessor = run;
			if (predecessor instanceof PsiWhiteSpace) // empty here-docs has no newline after them
			{
				predecessor = predecessor.getPrevSibling();
			}

			if (predecessor instanceof PerlHeredocTerminatorElement)    // sequential here-docs
			{
				PsiReference reference = predecessor.getReference();
				if (reference != null)
				{
					PsiElement prevOpener = reference.resolve();
					if (prevOpener != null)
					{
						HeredocSeeker seeker = new HeredocSeeker(run.getNode().getStartOffset());
						PerlPsiUtil.iteratePsiElementsRight(prevOpener, seeker);
						return seeker.getResult();
					}
				}
			}
			else    // first here-doc in a row
			{
				final PsiDocumentManager manager = PsiDocumentManager.getInstance(run.getProject());
				final PsiFile file = run.getContainingFile();
				final Document document = manager.getDocument(file);

				if (document != null)
				{
					int lineNumber = document.getLineNumber(run.getNode().getStartOffset());
					PsiElement firstLineElement = file.findElementAt(document.getLineStartOffset(lineNumber));
					if (firstLineElement != null)
					{
						HeredocSeeker seeker = new HeredocSeeker(run.getNode().getStartOffset());
						PerlPsiUtil.iteratePsiElementsRight(firstLineElement, seeker);
						return seeker.getResult();
					}
				}
			}

		}
		return null;

//		return PerlPsiUtil.findHeredocOpenerByOffset(element.getContainingFile(), element.getText(), element.getTextOffset());
	}

	static private class HeredocSeeker extends PerlPsiUtil.HeredocProcessor
	{
		protected PerlHeredocOpener myResult = null;

		public HeredocSeeker(int lineEndOffset)
		{
			super(lineEndOffset);
		}

		@Override
		public boolean process(PsiElement element)
		{
			if (element == null || element.getNode().getStartOffset() >= lineEndOffset)
			{
				return false;
			}
			if (element instanceof PerlHeredocOpener)
			{
				myResult = (PerlHeredocOpener) element;
				return false;
			}
			return true;
		}

		public PerlHeredocOpener getResult()
		{
			return myResult;
		}
	}

}
