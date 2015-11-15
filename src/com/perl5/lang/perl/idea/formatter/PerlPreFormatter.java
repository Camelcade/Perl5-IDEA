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
import com.intellij.psi.impl.source.resolve.ResolveCache;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.idea.formatter.operation.*;
import com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlLexer;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
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

	private final List<PerlFormattingOperation> myFormattingOperations = new ArrayList<PerlFormattingOperation>();
	protected TextRange myRange;

	public PerlPreFormatter(Project project)
	{
		myProject = project;
		mySettings = CodeStyleSettingsManager.getSettings(project);
		myPerlSettings = mySettings.getCustomSettings(PerlCodeStyleSettings.class);
	}

	protected static boolean isStringSimple(PerlString o)
	{
		return o.getFirstChild().getNextSibling() == o.getLastChild().getPrevSibling() &&
				// we need this because lexer unable to properly parse utf
				PerlLexer.BARE_STRING_PATTERN.matcher(o.getStringContent()).matches();
	}

	protected static boolean isStringSimpleIdentifier(PerlString o)
	{
		return o.getFirstChild().getNextSibling() == o.getLastChild().getPrevSibling() &&
				PerlLexer.IDENTIFIER_PATTERN.matcher(o.getStringContent()).matches();
	}

	protected static boolean isCommaArrowAhead(PsiElement o)
	{
		PsiElement nextElement = PerlPsiUtil.getNextSignificantSibling(o);
		return nextElement != null && nextElement.getNode().getElementType() == OPERATOR_COMMA_ARROW;
	}

	protected static boolean isInHeredocOpener(PerlString o)
	{
		return o.getParent() instanceof PerlHeredocOpener;
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

				for (int i = myFormattingOperations.size() - 1; i >= 0; i--)
				{
					myDelta += myFormattingOperations.get(i).apply();
				}

			} finally
			{
				manager.commitDocument(document);
			}
		}

		return TextRange.create(range.getStartOffset(), range.getEndOffset() + myDelta);
	}

	protected boolean isStringQuotable(PsiPerlStringBare o)
	{
		return myPerlSettings.OPTIONAL_QUOTES == FORCE && isCommaArrowAhead(o) ||
				myPerlSettings.OPTIONAL_QUOTES_HASH_INDEX == FORCE && isHashIndexKey(o);
	}

	protected boolean isSimpleScalarCast(PsiPerlScalarCastExpr o)
	{
		return o.getLastChild() instanceof PsiPerlScalarVariable;
	}

	protected boolean isStringInHeredocQuotable(PsiPerlStringBare o)
	{
		return myPerlSettings.OPTIONAL_QUOTES_HEREDOC_OPENER == FORCE && isInHeredocOpener(o);
	}

	protected boolean isStringUnqutable(PerlString o)
	{
		return isStringSimple(o) && (
				isHashIndexKey(o) && myPerlSettings.OPTIONAL_QUOTES_HASH_INDEX == SUPPRESS ||
						isCommaArrowAhead(o) && myPerlSettings.OPTIONAL_QUOTES == SUPPRESS)
				;
	}

	protected boolean isStringInHeredocUnquotable(PerlString o)
	{
		return isStringSimpleIdentifier(o) && isInHeredocOpener(o) && myPerlSettings.OPTIONAL_QUOTES_HEREDOC_OPENER == SUPPRESS;
	}


	@Override
	public void visitStringDq(@NotNull PsiPerlStringDq o)
	{
		if (!myRange.contains(o.getTextRange()))
		{
			return;
		}
		if (isStringUnqutable(o) || isStringInHeredocUnquotable(o))
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
		if (isStringUnqutable(o))
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
		if (isStringQuotable(o))
		{
			myFormattingOperations.add(new PerlFormattingReplace(o, PerlElementFactory.createString(myProject, "'" + o.getStringContent() + "'")));
		}
		else if (isStringInHeredocQuotable(o))
		{
			myFormattingOperations.add(new PerlFormattingReplace(o, PerlElementFactory.createString(myProject, "\"" + o.getStringContent() + "\"")));
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

/*
	@Override
	public void visitScalarCastExpr(@NotNull PsiPerlScalarCastExpr o)
	{
		PsiElement parent = o.getParent();
		if (myPerlSettings.OPTIONAL_DEREFERENCE_HASHREF_ELEMENT == SUPPRESS &&
				(parent instanceof PsiPerlScalarArrayElement || parent instanceof PsiPerlScalarHashElement) &&
				isSimpleScalarCast(o)
				)
		{
			myRemovals.add(o.getFirstChild());
			myInsertionsAfter.add(Couple.of(o.getLastChild(), PerlElementFactory.createDereference(myProject)));
		}
		super.visitScalarCastExpr(o);
	}

	@Override
	public void visitDerefExpr(@NotNull PsiPerlDerefExpr o)
	{
		if (myPerlSettings.OPTIONAL_DEREFERENCE_HASHREF_ELEMENT == FORCE)
		{
			PsiElement scalarVariableElement = o.getFirstChild();
			if (scalarVariableElement instanceof PsiPerlScalarVariable)
			{
				PsiElement derefElement = PerlPsiUtil.getNextSignificantSibling(scalarVariableElement);
				if (derefElement != null && derefElement.getNode().getElementType() == OPERATOR_DEREFERENCE)
				{
					PsiElement probableIndexElement = PerlPsiUtil.getNextSignificantSibling(derefElement);

					if (probableIndexElement instanceof PsiPerlHashIndex || probableIndexElement instanceof PsiPerlArrayIndex)
					{
						PsiElement sigilElement = scalarVariableElement.getFirstChild();
						if (sigilElement != null && sigilElement.getNode().getElementType() == SIGIL_SCALAR)
						{
							myRemovals.add(derefElement);
							myInsertionsAfter.add(Couple.of(sigilElement, sigilElement.copy()));
						}
					}
				}
			}
		}
		super.visitDerefExpr(o);
	}
*/

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
					myFormattingOperations.add(new PerlFormattingInsertAfter(PerlElementFactory.createDereference(myProject), o));
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
						myFormattingOperations.add(new PerlFormattingRemove(potentialDereference));
					}
				}
			}
		}
	}

	@Override
	public void visitScalarHashElement(@NotNull PsiPerlScalarHashElement o)
	{
		processDerefExpressionIndex(o);
		super.visitScalarHashElement(o);
	}

	@Override
	public void visitScalarArrayElement(@NotNull PsiPerlScalarArrayElement o)
	{
		processDerefExpressionIndex(o);
		super.visitScalarArrayElement(o);
	}

	@Override
	public void visitStatementModifier(@NotNull PsiPerlStatementModifier o)
	{
		PsiPerlExpr expression = PsiTreeUtil.getChildOfType(o, PsiPerlExpr.class);
		if (expression != null)
		{
			if (myPerlSettings.OPTIONAL_PARENTHESES == FORCE && !(expression instanceof PsiPerlParenthesisedExpr))
			{
				myFormattingOperations.add(new PerlFormattingStatementModifierWrap(o));
			}
			else if (myPerlSettings.OPTIONAL_PARENTHESES == SUPPRESS && expression instanceof PsiPerlParenthesisedExpr)
			{
				myFormattingOperations.add(new PerlFormattingStatementModifierUnwrap(o));
			}
		}
		super.visitStatementModifier(o);
	}

	protected void unquoteString(PerlString o)
	{
		myFormattingOperations.add(new PerlFormattingReplace(o, PerlElementFactory.createBareString(myProject, o.getStringContent())));
	}

}
