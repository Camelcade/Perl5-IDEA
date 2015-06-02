package com.perl5.lang.perl.idea.completion.providers;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.completion.PerlInsertHandlers;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.util.PerlArrayUtil;
import com.perl5.lang.perl.util.PerlHashUtil;
import com.perl5.lang.perl.util.PerlScalarUtil;
import com.perl5.lang.perl.util.PerlUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by hurricup on 01.06.2015.
 */
public class PerlVariableCompletionProvider extends CompletionProvider<CompletionParameters>
{
	public void addCompletions(@NotNull final CompletionParameters parameters,
							   ProcessingContext context,
							   @NotNull final CompletionResultSet resultSet)
	{

		final PsiElement variableName = parameters.getPosition();

		final PsiElement perlVariable;
		perlVariable = variableName.getParent();

		if (perlVariable instanceof PsiPerlPerlScalar)
			ApplicationManager.getApplication().runReadAction(new Runnable()
			{
				@Override
				public void run()
				{
					String currentText = variableName.getText();

					Collection<PerlVariable> declaredVariables = PerlUtil.findDeclaredLexicalVariables(perlVariable);

					for (PerlVariable variable : declaredVariables)
					{
						if (variable instanceof PsiPerlPerlScalar)
						{
							PerlVariableName variableName = variable.getVariableName();
							if (variableName != null && variableName.getName() != null)
								resultSet.addElement(LookupElementBuilder
										.create(variableName.getName())
										.withIcon(PerlIcons.SCALAR_GUTTER_ICON));
						} else if (variable instanceof PsiPerlPerlArray)
						{
							PerlVariableName variableName = variable.getVariableName();
							if (variableName != null && variableName.getName() != null)
							{
								String varName = variableName.getName();
								resultSet.addElement(LookupElementBuilder
												.create(varName)
												.withIcon(PerlIcons.ARRAY_GUTTER_ICON)
												.withInsertHandler(PerlInsertHandlers.ARRAY_ELEMENT_INSERT_HANDLER)
												.withPresentableText(varName + "[]")
								);
							}
						} else if (variable instanceof PsiPerlPerlHash)
						{
							PerlVariableName variableName = variable.getVariableName();
							if (variableName != null && variableName.getName() != null)
							{
								String varName = variableName.getName();
								resultSet.addElement(LookupElementBuilder
												.create(varName)
												.withIcon(PerlIcons.HASH_GUTTER_ICON)
												.withInsertHandler(PerlInsertHandlers.HASH_ELEMENT_INSERT_HANDLER)
												.withPresentableText(varName + "{}")
								);
							}
						}
					}

					// global scalars
					for (String name : PerlScalarUtil.listDefinedGlobalScalars(variableName.getProject()))
					{
						resultSet.addElement(LookupElementBuilder.create(name).withIcon(PerlIcons.SCALAR_GUTTER_ICON));
					}
					// global arrays
					for (String name : PerlArrayUtil.listDefinedGlobalArrays(variableName.getProject()))
					{
						resultSet.addElement(LookupElementBuilder
										.create(name)
										.withIcon(PerlIcons.ARRAY_GUTTER_ICON)
										.withInsertHandler(PerlInsertHandlers.ARRAY_ELEMENT_INSERT_HANDLER)
										.withPresentableText(name + "[]")
						);
					}
					// global hashes
					for (String name : PerlHashUtil.listDefinedGlobalHahses(variableName.getProject()))
					{
						resultSet.addElement(LookupElementBuilder
										.create(name)
										.withIcon(PerlIcons.HASH_GUTTER_ICON)
										.withInsertHandler(PerlInsertHandlers.HASH_ELEMENT_INSERT_HANDLER)
										.withPresentableText(name + "{}")
						);
					}

				}
			});
		else if (perlVariable instanceof PsiPerlPerlArray)
			ApplicationManager.getApplication().runReadAction(new Runnable()
			{
				@Override
				public void run()
				{
					String currentText = variableName.getText();

					Collection<PerlVariable> declaredVariables = PerlUtil.findDeclaredLexicalVariables(perlVariable);
					boolean useScalars = ((PsiPerlPerlArray) perlVariable).getScalarSigils() != null;

					for (PerlVariable variable : declaredVariables)
					{
						if (variable instanceof PsiPerlPerlScalar && useScalars)
						{
							PerlVariableName variableName = variable.getVariableName();
							if (variableName != null && variableName.getName() != null)
								resultSet.addElement(LookupElementBuilder.create(variableName.getName()).withIcon(PerlIcons.SCALAR_GUTTER_ICON));
						} else if (variable instanceof PsiPerlPerlArray)
						{
							PerlVariableName variableName = variable.getVariableName();
							if (variableName != null && variableName.getName() != null)
								resultSet.addElement(LookupElementBuilder.create(variableName.getName()).withIcon(PerlIcons.ARRAY_GUTTER_ICON));

						} else if (variable instanceof PsiPerlPerlHash)
						{
							PerlVariableName variableName = variable.getVariableName();
							if (variableName != null && variableName.getName() != null)
							{
								String varName = variableName.getName();
								resultSet.addElement(LookupElementBuilder
												.create(varName)
												.withIcon(PerlIcons.HASH_GUTTER_ICON)
												.withInsertHandler(PerlInsertHandlers.HASH_ELEMENT_INSERT_HANDLER)
												.withPresentableText(varName + "{}")
								);
							}
						}
					}
					// global scalars
					if (useScalars)
					{
						for (String name : PerlScalarUtil.listDefinedGlobalScalars(variableName.getProject()))
						{
							resultSet.addElement(LookupElementBuilder.create(name).withIcon(PerlIcons.SCALAR_GUTTER_ICON));
						}
					}
					// global arrays
					for (String name : PerlArrayUtil.listDefinedGlobalArrays(variableName.getProject()))
					{
						resultSet.addElement(LookupElementBuilder.create(name).withIcon(PerlIcons.ARRAY_GUTTER_ICON));
					}
					// global hashes
					for (String name : PerlHashUtil.listDefinedGlobalHahses(variableName.getProject()))
					{
						resultSet.addElement(LookupElementBuilder
										.create(name)
										.withIcon(PerlIcons.HASH_GUTTER_ICON)
										.withInsertHandler(PerlInsertHandlers.HASH_ELEMENT_INSERT_HANDLER)
										.withPresentableText(name + "{}")
						);
					}
				}
			});
		else if (perlVariable instanceof PsiPerlPerlHash)
			ApplicationManager.getApplication().runReadAction(new Runnable()
			{
				@Override
				public void run()
				{
					String currentText = variableName.getText();

					Collection<PerlVariable> declaredVariables = PerlUtil.findDeclaredLexicalVariables(perlVariable);
					boolean useScalars = ((PsiPerlPerlHash) perlVariable).getScalarSigils() != null;

					for (PerlVariable variable : declaredVariables)
					{
						if (variable instanceof PsiPerlPerlScalar && useScalars)
						{
							PerlVariableName variableName = variable.getVariableName();
							if (variableName != null && variableName.getName() != null)
								resultSet.addElement(LookupElementBuilder.create(variableName.getName()).withIcon(PerlIcons.SCALAR_GUTTER_ICON));
						} else if (variable instanceof PsiPerlPerlHash)
						{
							PerlVariableName variableName = variable.getVariableName();
							if (variableName != null && variableName.getName() != null)
								resultSet.addElement(LookupElementBuilder.create(variableName.getName()).withIcon(PerlIcons.HASH_GUTTER_ICON));

						}
					}

					// global scalars
					if (useScalars)
					{
						for (String name : PerlScalarUtil.listDefinedGlobalScalars(variableName.getProject()))
						{
							resultSet.addElement(LookupElementBuilder.create(name).withIcon(PerlIcons.SCALAR_GUTTER_ICON));
						}
					}
					// global hashes
					for (String name : PerlHashUtil.listDefinedGlobalHahses(variableName.getProject()))
					{
						resultSet.addElement(LookupElementBuilder.create(name).withIcon(PerlIcons.HASH_GUTTER_ICON));
					}

				}
			});
	}
}