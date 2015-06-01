package com.perl5.lang.perl.idea.completion.providers;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.util.PerlFunctionUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 01.06.2015.
 */
public class PerlBuiltInFunctionCompletionProvider extends CompletionProvider<CompletionParameters>
{
	public void addCompletions(@NotNull CompletionParameters parameters,
							   ProcessingContext context,
							   @NotNull CompletionResultSet resultSet)
	{

		for (String functionName : PerlFunctionUtil.BUILT_IN)
		{
			resultSet.addElement(LookupElementBuilder.create(functionName));
		}
	}

}
