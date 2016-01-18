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

package com.perl5.lang.perl.idea.completion.providers;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.lexer.PerlLexer;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlStringContentElementImpl;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Created by hurricup on 18.01.2016.
 */
public class PerlHashIndexCompletionProvider extends CompletionProvider<CompletionParameters> implements PerlElementPatterns
{
	public static final Set<String> STRINGS_SET = new THashSet<String>();

	@Override
	protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull final CompletionResultSet result)
	{
		final PsiElement element = parameters.getPosition();
		PsiFile file = element.getContainingFile();

		for (String text : STRINGS_SET)
		{
			addElement(text, result);
		}

		file.accept(
				new PerlRecursiveVisitor()
				{
					@Override
					public void visitStringContentElement(@NotNull PerlStringContentElementImpl o)
					{
						if (o == element)
						{
							super.visitStringContentElement(o);
						}
						else if (SIMPLE_HASH_INDEX.accepts(o))
						{
							processStringElement(o);
						}
					}

					@Override
					public void visitCommaSequenceExpr(@NotNull PsiPerlCommaSequenceExpr o)
					{
						if (o.getParent() instanceof PsiPerlAnonHash)
						{
							PsiElement sequenceElement = o.getFirstChild();
							boolean isKey = true;

							while (sequenceElement != null)
							{
								IElementType elementType = sequenceElement.getNode().getElementType();
								if (isKey && sequenceElement instanceof PerlString)
								{
									for (PerlStringContentElement stringElement : PerlPsiUtil.collectStringElements(sequenceElement))
									{
										processStringElement(stringElement);
									}
								}
								else if (elementType == OPERATOR_COMMA || elementType == OPERATOR_COMMA_ARROW)
								{
									isKey = !isKey;
								}

								sequenceElement = PerlPsiUtil.getNextSignificantSibling(sequenceElement);
							}
						}
						super.visitCommaSequenceExpr(o);
					}

					protected void processStringElement(PerlStringContentElement stringContentElement)
					{
						String text = stringContentElement.getText();
						if (StringUtil.isNotEmpty(text) && !STRINGS_SET.contains(text) && PerlLexer.IDENTIFIER_PATTERN.matcher(text).matches())
						{
							STRINGS_SET.add(text);
							addElement(text, result);
						}
					}
				});
	}


	protected void addElement(String text, CompletionResultSet result)
	{
		result.addElement(LookupElementBuilder.create(text));
	}
}
