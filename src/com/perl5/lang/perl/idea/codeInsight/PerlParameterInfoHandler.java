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
import com.perl5.lang.perl.psi.impl.PerlCompositeElementImpl;
import com.perl5.lang.perl.psi.impl.PsiPerlCallArgumentsImpl;
import com.perl5.lang.perl.psi.impl.PsiPerlCommaSequenceExprImpl;
import com.perl5.lang.perl.psi.impl.PsiPerlParenthesisedExprImpl;
import com.perl5.lang.perl.psi.mixins.PerlMethodImplMixin;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by hurricup on 26.06.2016.
 */
public class PerlParameterInfoHandler implements ParameterInfoHandler<PsiPerlCallArgumentsImpl, PerlParameterInfo>
{
	/**
	 * Method marks parameters as active/inactive
	 *
	 * @param container      arguments container
	 * @param parameterInfos array of parameter infos
	 */
	private static void markActiveParameters(@NotNull PsiPerlCallArgumentsImpl container, PerlParameterInfo[] parameterInfos, int offset)
	{
		for (PerlParameterInfo parameterInfo : parameterInfos)
		{
			parameterInfo.setSelected(false);
		}

		markActiveparametersRecursively(container.getFirstChild(), parameterInfos, 0, offset);
	}

	private static int markActiveparametersRecursively(PsiElement element, PerlParameterInfo[] parameterInfos, int currentIndex, int offset)
	{
		while (element != null)
		{
			if (parameterInfos.length <= currentIndex)
			{
				return currentIndex;
			}

			if (element instanceof PsiPerlParenthesisedExprImpl || element instanceof PsiPerlCommaSequenceExprImpl)
			{
				currentIndex = markActiveparametersRecursively(element.getFirstChild(), parameterInfos, currentIndex, offset);
			}
			else if (element instanceof PerlCompositeElementImpl)
			{
				PerlContextType valueContextType = ((PerlCompositeElementImpl) element).getValueContextType();
				PerlVariableType currentArgumentType = parameterInfos[currentIndex].getArgument().getArgumentType();


			}
			element = element.getNextSibling();
		}
		return currentIndex;
	}

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
	private static PerlParameterInfo[] getTargetParameterInfo(@Nullable PsiElement target)
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
		return PerlParameterInfo.wrapArguments(subArgumentsList);
	}

	@Nullable
	private static PerlParameterInfo[] getMethodCallArguments(@NotNull PsiPerlCallArgumentsImpl arguments)
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

				PerlParameterInfo[] parameterInfos;

				for (PsiReference reference : subNameElement.getReferences())
				{
					if (reference instanceof PsiPolyVariantReference)
					{
						for (ResolveResult resolveResult : ((PsiPolyVariantReference) reference).multiResolve(false))
						{
							parameterInfos = getTargetParameterInfo(resolveResult.getElement());
							if (parameterInfos != null)
							{
								return parameterInfos;
							}
						}
					}
					else
					{
						parameterInfos = getTargetParameterInfo(reference.resolve());
						if (parameterInfos != null)
						{
							return parameterInfos;
						}
					}
				}
			}
			run = run.getPrevSibling();
		}
		return null;
	}

	@Nullable
	@Override
	public PsiPerlCallArgumentsImpl findElementForParameterInfo(@NotNull CreateParameterInfoContext context)
	{
//		System.err.println("Find for create");
		PsiPerlCallArgumentsImpl callArguments = findCallArguments(context);

		if (callArguments != null)
		{
			PerlParameterInfo[] methodParameterInfos = getMethodCallArguments(callArguments);
			if (methodParameterInfos == null || methodParameterInfos.length == 0)
			{
				return null;
			}
			markActiveParameters(callArguments, methodParameterInfos, context.getOffset());
			context.setItemsToShow(methodParameterInfos);
		}

		return callArguments;
	}

	@Override
	public void showParameterInfo(@NotNull PsiPerlCallArgumentsImpl container, @NotNull CreateParameterInfoContext context)
	{
		context.showHint(container, container.getTextOffset(), this);
	}

	@Nullable
	@Override
	public PsiPerlCallArgumentsImpl findElementForUpdatingParameterInfo(@NotNull UpdateParameterInfoContext context)
	{
		return findCallArguments(context);
	}

	@Override
	public void updateParameterInfo(@NotNull PsiPerlCallArgumentsImpl container, @NotNull UpdateParameterInfoContext context)
	{
		markActiveParameters(container, (PerlParameterInfo[]) context.getObjectsToView(), context.getOffset());
	}

	@Override
	public void updateUI(PerlParameterInfo parameterInfo, @NotNull ParameterInfoUIContext context)
	{
		parameterInfo.setUpUIPresentation(context);
	}

	@Nullable
	@Override
	public String getParameterCloseChars()
	{
		return null;
	}

	@Override
	public boolean tracksParameterIndex()
	{
		return false;
	}

	@Override
	public boolean couldShowInLookup()
	{
		return false;
	}

	@Nullable
	@Override
	public Object[] getParametersForLookup(LookupElement item, ParameterInfoContext context)
	{
		return ArrayUtil.EMPTY_OBJECT_ARRAY;  // we don't
	}

	@Nullable
	@Override
	public Object[] getParametersForDocumentation(PerlParameterInfo p, ParameterInfoContext context)
	{
		return ArrayUtil.EMPTY_OBJECT_ARRAY;  // we don't
	}
}
