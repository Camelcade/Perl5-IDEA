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

package com.perl5.lang.perl.idea.formatter;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Couple;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CodeStyleSettingsManager;
import com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlLexer;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 12.11.2015.
 */
public class PerlPreFormatter extends PerlRecursiveVisitor implements PerlCodeStyleSettings.OptionalConstructions, PerlElementTypes
{
	protected final Project myProject;
	protected final CodeStyleSettings mySettings;
	protected final PerlCodeStyleSettings myPerlSettings;
	private final List<Couple<PsiElement>> myReplacements = new ArrayList<Couple<PsiElement>>();
	private final List<PsiElement> myRemovals = new ArrayList<PsiElement>();
	private final List<Couple<PsiElement>> myInsertionsBefore = new ArrayList<Couple<PsiElement>>();
	private final List<Couple<PsiElement>> myInsertionsAfter = new ArrayList<Couple<PsiElement>>();
	protected TextRange myRange;

	public PerlPreFormatter(Project project)
	{
		myProject = project;
		mySettings = CodeStyleSettingsManager.getSettings(project);
		myPerlSettings = mySettings.getCustomSettings(PerlCodeStyleSettings.class);
	}

	// this should be probably in PerlString itself
	protected static boolean isStringUnqutable(PerlString o)
	{
		return (isStringSimple(o)) && (isHashIndexKey(o) || isCommaArrowAhead(o));
	}

	protected static boolean isStringSimple(PerlString o)
	{
		return o.getFirstChild().getNextSibling() == o.getLastChild().getPrevSibling() &&
				// we need this because lexer unable to properly parse utf
				PerlLexer.BARE_STRING_PATTERN.matcher(o.getStringContent()).matches();
	}

	protected static boolean isCommaArrowAhead(PsiElement o)
	{
		PsiElement nextElement = PerlPsiUtil.getNextSignificantSibling(o);
		return nextElement != null && nextElement.getNode().getElementType() == OPERATOR_COMMA_ARROW;
	}

	protected static boolean isHashIndexKey(PsiElement o)
	{
		if (!(o.getParent() instanceof PsiPerlHashIndex))
			return false;
		PsiElement prevSibling = o.getPrevSibling();
		PsiElement nextSibling = o.getNextSibling();
		return prevSibling != null && prevSibling.getNode().getElementType() == LEFT_BRACE &&
				nextSibling != null && nextSibling.getNode().getElementType() == RIGHT_BRACE;
	}

	public TextRange process(PsiElement element, TextRange range)
	{
/*
		if (!myPyCodeStyleSettings.SPACE_AFTER_NUMBER_SIGN) {
			return range;
		}
*/
		myRange = range;
		final PsiDocumentManager manager = PsiDocumentManager.getInstance(myProject);
		final Document document = manager.getDocument(element.getContainingFile());
		int myDelta = 0;
		if (document != null)
		{
			manager.doPostponedOperationsAndUnblockDocument(document);

			try
			{
				// scan document
				element.accept(this);

				for (Couple<PsiElement> pair : myReplacements)
				{
					PsiElement oldElement = pair.getFirst();
					PsiElement newElement = pair.getSecond();
					myDelta += newElement.getNode().getTextLength() - oldElement.getNode().getTextLength();
					oldElement.replace(newElement);
				}
				for (Couple<PsiElement> pair : myInsertionsAfter)
				{
					PsiElement anchor = pair.getFirst();
					PsiElement insertion = pair.getSecond();
					myDelta += insertion.getNode().getTextLength();
					anchor.getParent().addAfter(pair.getSecond(), anchor);
				}
				for (Couple<PsiElement> pair : myInsertionsBefore)
				{
					PsiElement anchor = pair.getFirst();
					PsiElement insertion = pair.getSecond();
					myDelta += insertion.getNode().getTextLength();
					anchor.getParent().addBefore(pair.getSecond(), anchor);
				}
				for (PsiElement oldElement : myRemovals)
				{
					myDelta -= oldElement.getNode().getTextLength();
					oldElement.delete();
				}
			} finally
			{
				manager.commitDocument(document);
			}
		}
		return TextRange.create(range.getStartOffset(), range.getEndOffset() + myDelta);
	}

	@Override
	public void visitStringDq(@NotNull PsiPerlStringDq o)
	{
		if (!myRange.contains(o.getTextRange()))
		{
			return;
		}
		if (myPerlSettings.OPTIONAL_QUOTES == SUPPRESS && isStringUnqutable(o))
		{
			unquoteString(o);
		}
		else
		{
			super.visitStringDq(o);
		}
	}

	@Override
	public void visitStringSq(@NotNull PsiPerlStringSq o)
	{
		if (!myRange.contains(o.getTextRange()))
		{
			return;
		}
		if (myPerlSettings.OPTIONAL_QUOTES == SUPPRESS && isStringUnqutable(o))
		{
			unquoteString(o);
		}
		else
		{
			super.visitStringSq(o);
		}
	}

	@Override
	public void visitStringBare(@NotNull PsiPerlStringBare o)
	{
		if (!myRange.contains(o.getTextRange()))
		{
			return;
		}
		if (myPerlSettings.OPTIONAL_QUOTES == FORCE && isStringUnqutable(o))
		{
			myReplacements.add(Couple.of((PsiElement) o, PerlElementFactory.createString(myProject, "'" + o.getStringContent() + "'")));
		}
		else
		{
			super.visitStringBare(o);
		}
	}

	@Override
	public void visitHashIndex(@NotNull PsiPerlHashIndex o)
	{
		if (!myRange.contains(o.getTextRange()))
		{
			return;
		}

		processDerefExpressionIndex(o);
		super.visitHashIndex(o);
	}

	@Override
	public void visitArrayIndex(@NotNull PsiPerlArrayIndex o)
	{
		if (!myRange.contains(o.getTextRange()))
		{
			return;
		}
		processDerefExpressionIndex(o);
		super.visitArrayIndex(o);
	}

	protected void processDerefExpressionIndex(PsiElement o)
	{
		PsiElement parent = o.getParent();
		if (parent instanceof PsiPerlDerefExpr)
		{
			if (myPerlSettings.OPTIONAL_DEREFERENCE == FORCE)
			{
				PsiElement nextIndexElement = PerlPsiUtil.getNextSignificantSibling(o);
				if (nextIndexElement instanceof PsiPerlHashIndex || nextIndexElement instanceof PsiPerlArrayIndex)
				{
					myInsertionsAfter.add(Couple.of(o, PerlElementFactory.createDereference(myProject)));
				}
			}
			else if (myPerlSettings.OPTIONAL_DEREFERENCE == SUPPRESS)
			{
				PsiElement potentialDereference = PerlPsiUtil.getNextSignificantSibling(o);
				if (potentialDereference != null && potentialDereference.getNode().getElementType() == OPERATOR_DEREFERENCE)
				{
					PsiElement nextIndexElement = PerlPsiUtil.getNextSignificantSibling(potentialDereference);
					if (nextIndexElement instanceof PsiPerlHashIndex || nextIndexElement instanceof PsiPerlArrayIndex)
					{
						myRemovals.add(potentialDereference);
					}
				}
			}
		}
	}


	protected void unquoteString(PerlString o)
	{
		myReplacements.add(Couple.of((PsiElement) o, PerlElementFactory.createBareString(myProject, o.getStringContent())));
	}

}
