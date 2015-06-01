package com.perl5.lang.perl.idea.completion.providers;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
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

		if (perlVariable instanceof PerlPerlScalar)
			ApplicationManager.getApplication().runReadAction(new Runnable()
			{
				@Override
				public void run()
				{
					String currentText = variableName.getText();

					Collection<PerlVariable> declaredVariables = PerlUtil.findDeclaredLexicalVariables(perlVariable);

					// locals, todo we should limit with lexicaly visible
					for (PerlVariable variable : declaredVariables)
					{
						if (variable instanceof PerlPerlScalar)
						{
							PerlVariableName variableName = variable.getVariableName();
							if (variableName != null && variableName.getName() != null)
								resultSet.addElement(LookupElementBuilder.create(variableName.getName()));
						} else if (variable instanceof PerlPerlArray)
						{
							PerlVariableName variableName = variable.getVariableName();
							if (variableName != null && variableName.getName() != null)
								resultSet.addElement(LookupElementBuilder.create(variableName.getName() + "[]"));

						} else if (variable instanceof PerlPerlHash)
						{
							PerlVariableName variableName = variable.getVariableName();
							if (variableName != null && variableName.getName() != null)
								resultSet.addElement(LookupElementBuilder.create(variableName.getName() + "{}"));

						}
					}

					// global scalars
					for (String name : PerlScalarUtil.listDefinedGlobalScalars(variableName.getProject()))
					{
						resultSet.addElement(LookupElementBuilder.create(name));
					}
					// global arrays
					for (String name : PerlArrayUtil.listDefinedGlobalArrays(variableName.getProject()))
					{
						resultSet.addElement(LookupElementBuilder.create(name + "[]"));
					}
					// global hashes
					for (String name : PerlHashUtil.listDefinedGlobalHahses(variableName.getProject()))
					{
						resultSet.addElement(LookupElementBuilder.create(name + "{}"));
					}

				}
			});
		else if (perlVariable instanceof PerlPerlArray)
			ApplicationManager.getApplication().runReadAction(new Runnable()
			{
				@Override
				public void run()
				{
					String currentText = variableName.getText();

					Collection<PerlVariable> declaredVariables = PerlUtil.findDeclaredLexicalVariables(perlVariable);
					boolean useScalars = ((PerlPerlArray) perlVariable).getScalarSigils() != null;

					for (PerlVariable variable : declaredVariables)
					{
						if (variable instanceof PerlPerlScalar && useScalars)
						{
							PerlVariableName variableName = variable.getVariableName();
							if (variableName != null && variableName.getName() != null)
								resultSet.addElement(LookupElementBuilder.create(variableName.getName()));
						} else if (variable instanceof PerlPerlArray)
						{
							PerlVariableName variableName = variable.getVariableName();
							if (variableName != null && variableName.getName() != null)
								resultSet.addElement(LookupElementBuilder.create(variableName.getName()));

						} else if (variable instanceof PerlPerlHash)
						{
							PerlVariableName variableName = variable.getVariableName();
							if (variableName != null && variableName.getName() != null)
								resultSet.addElement(LookupElementBuilder.create(variableName.getName() + "{}"));
						}
					}
					// global scalars
					if (useScalars)
					{
						for (String name : PerlScalarUtil.listDefinedGlobalScalars(variableName.getProject()))
						{
							resultSet.addElement(LookupElementBuilder.create(name));
						}
					}
					// global arrays
					for (String name : PerlArrayUtil.listDefinedGlobalArrays(variableName.getProject()))
					{
						resultSet.addElement(LookupElementBuilder.create(name));
					}
					// global hashes
					for (String name : PerlHashUtil.listDefinedGlobalHahses(variableName.getProject()))
					{
						resultSet.addElement(LookupElementBuilder.create(name + "{}"));
					}
				}
			});
		else if (perlVariable instanceof PerlPerlHash)
			ApplicationManager.getApplication().runReadAction(new Runnable()
			{
				@Override
				public void run()
				{
					String currentText = variableName.getText();

					Collection<PerlVariable> declaredVariables = PerlUtil.findDeclaredLexicalVariables(perlVariable);
					boolean useScalars = ((PerlPerlHash) perlVariable).getScalarSigils() != null;

					for (PerlVariable variable : declaredVariables)
					{
						if (variable instanceof PerlPerlScalar && useScalars)
						{
							PerlVariableName variableName = variable.getVariableName();
							if (variableName != null && variableName.getName() != null)
								resultSet.addElement(LookupElementBuilder.create(variableName.getName()));
						} else if (variable instanceof PerlPerlHash)
						{
							PerlVariableName variableName = variable.getVariableName();
							if (variableName != null && variableName.getName() != null)
								resultSet.addElement(LookupElementBuilder.create(variableName.getName()));

						}
					}

					// global scalars
					if (useScalars)
					{
						for (String name : PerlScalarUtil.listDefinedGlobalScalars(variableName.getProject()))
						{
							resultSet.addElement(LookupElementBuilder.create(name));
						}
					}
					// global hashes
					for (String name : PerlHashUtil.listDefinedGlobalHahses(variableName.getProject()))
					{
						resultSet.addElement(LookupElementBuilder.create(name));
					}

				}
			});
	}
}