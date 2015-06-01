package com.perl5.lang.perl.idea.completion.providers;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.psi.PerlPackageElement;
import com.perl5.lang.perl.util.PerlFunctionUtil;
import com.perl5.lang.perl.util.PerlGlobUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by hurricup on 01.06.2015.
 */
public class PerlFunctionCompletionProvider extends CompletionProvider<CompletionParameters>
{
	public void addCompletions(@NotNull final CompletionParameters parameters,
							   ProcessingContext context,
							   @NotNull final CompletionResultSet resultSet)
	{

		String packageName = null;
		PsiElement parent = parameters.getPosition().getParent();
		if( parent != null && parent instanceof PerlPackageElement)
			packageName = ((PerlPackageElement) parent).getPackageName();

		final String finalPackageName = packageName == null ? null: packageName + "::";

		final Project project = parameters.getPosition().getProject();

		ApplicationManager.getApplication().runReadAction(new Runnable()
		{
			@Override
			public void run()
			{
				Collection<String> definedSubs = PerlFunctionUtil.getDefinedSubsNames(project);
				definedSubs.addAll(PerlGlobUtil.getDefinedGlobsNames(project));

				for (String subname : definedSubs )
				{
					if( finalPackageName != null && subname.startsWith(finalPackageName) )
						subname = subname.substring(finalPackageName.length());
					LookupElementBuilder elementBuilder = LookupElementBuilder.create(subname);

					if( subname.contains("::"))
					{
						resultSet.addElement(elementBuilder.withLookupString(subname.substring(subname.lastIndexOf("::")+2)));
					}
					else
					{
						resultSet.addElement(elementBuilder);
					}

				}
			}
		});
	}

}
