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

package com.perl5.lang.perl.idea.codeInsight;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.lang.parameterInfo.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.util.ArrayUtil;
import com.perl5.lang.perl.psi.PerlSubDefinitionBase;
import com.perl5.lang.perl.psi.PerlSubNameElement;
import com.perl5.lang.perl.psi.impl.PsiPerlCallArgumentsImpl;
import com.perl5.lang.perl.psi.mixins.PerlMethodImplMixin;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by hurricup on 26.06.2016.
 */
public class PerlParameterInfoHandler implements ParameterInfoHandler<PsiPerlCallArgumentsImpl, PerlSubArgument>
{
	@Nullable
	private static PsiPerlCallArgumentsImpl findCallArguments(ParameterInfoContext context)
	{
		PsiPerlCallArgumentsImpl callArguments = ParameterInfoUtils.findParentOfType(context.getFile(), context.getOffset(), PsiPerlCallArgumentsImpl.class);
		if (callArguments != null || context.getOffset() == 0)
		{
			return callArguments;
		}
		return ParameterInfoUtils.findParentOfType(context.getFile(), context.getOffset() - 1, PsiPerlCallArgumentsImpl.class);
	}

	@Nullable
	private static PerlSubArgument[] getMethodCallArguments(@NotNull PsiPerlCallArgumentsImpl arguments)
	{
		PsiElement run = arguments.getPrevSibling();
		while (run != null)
		{
			if (run instanceof PerlMethodImplMixin)
			{
				PerlSubNameElement subNameElement = ((PerlMethodImplMixin) run).getSubNameElement();
				if (subNameElement == null)
				{
					break;
				}

				PerlSubArgument[] targetSubArguments;

				for (PsiReference reference : subNameElement.getReferences())
				{
					if (reference instanceof PsiPolyVariantReference)
					{
						for (ResolveResult resolveResult : ((PsiPolyVariantReference) reference).multiResolve(false))
						{
							targetSubArguments = getTargetSubArguments(resolveResult.getElement());
							if (targetSubArguments != null)
							{
								return targetSubArguments;
							}
						}
					}
					else
					{
						targetSubArguments = getTargetSubArguments(reference.resolve());
						if (targetSubArguments != null)
						{
							return targetSubArguments;
						}
					}
				}
			}
			run = run.getPrevSibling();
		}
		return null;
	}

	@Nullable
	private static PerlSubArgument[] getTargetSubArguments(@Nullable PsiElement target)
	{
		if (target == null || !(target instanceof PerlSubDefinitionBase))
		{
			return null;
		}

		@SuppressWarnings("unchecked") List<PerlSubArgument> subArgumentsList = ((PerlSubDefinitionBase) target).getSubArgumentsList();

		if (((PerlSubDefinitionBase) target).isMethod() && subArgumentsList.size() > 0)
		{
			subArgumentsList.remove(0);
		}

		// fixme we should wrap sub arguments to mark current element. One-liner is not really cool here
		return subArgumentsList.toArray(new PerlSubArgument[subArgumentsList.size()]);
	}

	@Override
	public boolean couldShowInLookup()
	{
		return true;
	}

	@Nullable
	@Override
	public Object[] getParametersForLookup(LookupElement item, ParameterInfoContext context)
	{
		return ArrayUtil.EMPTY_OBJECT_ARRAY;  // we don't
	}

	@Nullable
	@Override
	public Object[] getParametersForDocumentation(PerlSubArgument p, ParameterInfoContext context)
	{
		return ArrayUtil.EMPTY_OBJECT_ARRAY;  // we don't
	}

	@Nullable
	@Override
	public PsiPerlCallArgumentsImpl findElementForParameterInfo(@NotNull CreateParameterInfoContext context)
	{
//		System.err.println("Find for create");
		PsiPerlCallArgumentsImpl callArguments = findCallArguments(context);

		if (callArguments != null)
		{
			PerlSubArgument[] methodCallArguments = getMethodCallArguments(callArguments);
			if (methodCallArguments == null || methodCallArguments.length == 0)
			{
				return null;
			}
			context.setItemsToShow(methodCallArguments);
		}

		return callArguments;
	}

	@Override
	public void showParameterInfo(@NotNull PsiPerlCallArgumentsImpl element, @NotNull CreateParameterInfoContext context)
	{
//		System.err.println("Showing");
		context.showHint(element, element.getTextOffset(), this);
	}

	@Nullable
	@Override
	public PsiPerlCallArgumentsImpl findElementForUpdatingParameterInfo(@NotNull UpdateParameterInfoContext context)
	{
//		System.err.println("Find for update");
		return findCallArguments(context);
	}

	@Override
	public void updateParameterInfo(@NotNull PsiPerlCallArgumentsImpl element, @NotNull UpdateParameterInfoContext context)
	{
//		System.err.println("Updating");
/*
		System.err.println("Updating");
		Object[] objectsToView = context.getObjectsToView();
		if( objectsToView.length > 1)
			context.setHighlightedParameter(objectsToView[1]);
		context.setCurrentParameter(1);
*/
	}

	@Override
	public void updateUI(PerlSubArgument argument, @NotNull ParameterInfoUIContext context)
	{
//		System.err.println("Updating UI");
		String argumentString = argument.toStringLong();
		context.setupUIComponentPresentation(
				argumentString, -1, 0, false, false, false, context.getDefaultParameterColor()
		);
	}

	@Nullable
	@Override
	public String getParameterCloseChars()
	{
		return ",)";
	}

	@Override
	public boolean tracksParameterIndex()
	{
		return false;
	}
}
