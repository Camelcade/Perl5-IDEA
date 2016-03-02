/*
 * Copyright 2015 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.editor.smartkeys;

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.perl5.lang.perl.idea.codeInsight.Perl5CodeInsightSettings;
import com.perl5.lang.perl.psi.PerlHeredocOpener;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 06.09.2015.
 */
public class PerlEnterHandlerDelegate implements EnterHandlerDelegate
{
	@Override
	public Result preprocessEnter(@NotNull PsiFile file,
								  @NotNull Editor editor,
								  @NotNull Ref<Integer> caretOffset,
								  @NotNull Ref<Integer> caretAdvance,
								  @NotNull DataContext dataContext,
								  EditorActionHandler originalHandler)
	{
		return Result.Continue;
	}

	@Override
	public Result postProcessEnter(@NotNull PsiFile file, @NotNull Editor editor, @NotNull DataContext dataContext)
	{
		if (file instanceof PerlFileImpl && Perl5CodeInsightSettings.getInstance().HEREDOC_AUTO_INSERTION)
		{
			final CaretModel caretModel = editor.getCaretModel();
			int offset = caretModel.getOffset();
			PsiElement currentElement = file.findElementAt(offset);

			if (currentElement == null || currentElement.getParent() instanceof PerlHeredocElementImpl)
			{
				LogicalPosition currentPosition = caretModel.getLogicalPosition();
				final int enterLine = currentPosition.line - 1;
				final int currentOffset = caretModel.getOffset();
				if (enterLine > -1 && currentOffset > 0)
				{
					final Document document = editor.getDocument();
					final PsiDocumentManager manager = PsiDocumentManager.getInstance(file.getProject());

					final int lineStartOffset = document.getLineStartOffset(enterLine);
					final List<PerlHeredocOpener> openers = new ArrayList<PerlHeredocOpener>();

					PsiElement firstLineElement = file.findElementAt(lineStartOffset);
					if (firstLineElement != null)
					{
						HeredocCollector collector = new HeredocCollector(currentOffset - 1);
						PerlPsiUtil.iteratePsiElementsRight(firstLineElement, collector);

						PerlHeredocOpener lastOpener = null;
						for (PerlHeredocOpener currentOpener : collector.getResult())
						{
							PsiReference inboundReference = ReferencesSearch.search(currentOpener).findFirst();
							if (inboundReference == null) // disclosed marker
							{
								int addOffset = -1;
								String openerName = currentOpener.getName();
								boolean emptyOpener = StringUtil.isEmpty(openerName);
								String closeMarker = "\n" + openerName + "\n";

								if (lastOpener == null) // first one
								{
									addOffset = currentOffset;
								}
								else // sequentional
								{
									PsiReference lastOpenerReference = ReferencesSearch.search(lastOpener).findFirst();
									if (lastOpenerReference != null)
									{
										PsiElement element = lastOpenerReference.getElement();
										addOffset = element.getTextRange().getEndOffset();
										if (!emptyOpener)
										{
											closeMarker = "\n" + closeMarker;
										}
									}
									else
									{
//										System.err.println("Unable to find last opener reference");
										return Result.Continue;
									}
								}

								document.insertString(addOffset, closeMarker);
								manager.commitDocument(document);
								inboundReference = ReferencesSearch.search(currentOpener).findFirst();

								if (inboundReference != null)
								{
									lastOpener = currentOpener;
								}
								else
								{
//									System.err.println("Unable to find marker");
									return Result.Continue;
								}

							}
							else
							{
								lastOpener = currentOpener;
							}
						}
					}
				}
			}
		}
		return Result.Continue;
	}

	static private class HeredocCollector extends PerlPsiUtil.HeredocProcessor
	{
		protected List<PerlHeredocOpener> myResult = new ArrayList<PerlHeredocOpener>();

		public HeredocCollector(int lineEndOffset)
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
				myResult.add((PerlHeredocOpener) element);
			}
			return true;
		}

		public List<PerlHeredocOpener> getResult()
		{
			return myResult;
		}
	}

}
