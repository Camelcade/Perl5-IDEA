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

package com.perl5.lang.perl.idea.refactoring.rename;

import com.intellij.ide.IdeBundle;
import com.intellij.ide.util.MemberChooser;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.rename.RenamePsiElementProcessor;
import com.intellij.ui.SpeedSearchComparator;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.codeInsight.PerlMethodMember;
import com.perl5.lang.perl.psi.PerlGlobVariable;
import com.perl5.lang.perl.psi.PerlSubBase;
import com.perl5.lang.perl.util.PerlSubUtil;
import com.perl5.lang.pod.PodLanguage;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hurricup on 03.10.2015.
 */
public abstract class PerlRenamePolyReferencedElementProcessor extends RenamePsiElementProcessor
{
	@Override
	public void prepareRenaming(PsiElement element, String newName, Map<PsiElement, String> allRenames, SearchScope scope)
	{
		final String currentBaseName = ((PsiNameIdentifierOwner) element).getName();

		if (currentBaseName != null && StringUtil.isNotEmpty(newName))
		{
			boolean globScanned = element instanceof PerlGlobVariable;

			for (PsiReference reference : ReferencesSearch.search(element, element.getUseScope()).findAll())
			{
				if (reference instanceof PsiPolyVariantReference)
				{
					for (ResolveResult resolveResult : ((PsiPolyVariantReference) reference).multiResolve(false))
					{
						PsiElement resolveResultElement = resolveResult.getElement();
						if (!allRenames.containsKey(resolveResultElement))
						{
							allRenames.put(resolveResultElement, newName);
							if (!globScanned && resolveResultElement instanceof PerlGlobVariable)
							{
								globScanned = true;
								prepareRenaming(resolveResultElement, newName, allRenames, scope);
							}
						}
					}
				}
				processDocReference(currentBaseName, newName, reference, allRenames);
			}

			if (element instanceof PerlSubBase && ((PerlSubBase) element).isMethod())
			{
				// collect submethods
			}
		}
	}

	private void processDocReference(String currentBaseName, String newName, PsiReference reference, Map<PsiElement, String> allRenames)
	{
		PsiElement sourceElement = reference.getElement();
		if (sourceElement.getLanguage().isKindOf(PodLanguage.INSTANCE))
		{
			PsiNameIdentifierOwner identifierOwner = PsiTreeUtil.getParentOfType(sourceElement, PsiNameIdentifierOwner.class);
			if (identifierOwner != null)
			{
				PsiElement nameIdentifier = identifierOwner.getNameIdentifier();
				if (nameIdentifier != null && nameIdentifier.getTextRange().contains(sourceElement.getTextRange()))
				{
					String currentName = identifierOwner.getName();
					if (currentName != null)
					{
						String newSectionName = currentName.replace(currentBaseName, newName);
						allRenames.put(identifierOwner, newSectionName);
					}
				}
			}
		}
	}

	@Nullable
	@Override
	public PsiElement substituteElementToRename(PsiElement element, @Nullable Editor editor)
	{
//		if (element instanceof PerlSubBase && ((PerlSubBase) element).isMethod())
//		{
//			return suggestSuperMethod((PerlSubBase) element);
//		}
		return super.substituteElementToRename(element, editor);
	}

	private PsiElement suggestSuperMethod(PerlSubBase subBase)
	{
		List<PerlSubBase> topLevelSuperMethods = PerlSubUtil.getTopLevelSuperMethods(subBase);

		if (topLevelSuperMethods.size() == 1 && topLevelSuperMethods.get(0).equals(subBase))
			return subBase;


		List<PerlMethodMember> fullList = new ArrayList<PerlMethodMember>();
		fullList.add(new PerlMethodMember(subBase));
		for (PerlSubBase parentMethod : topLevelSuperMethods)
		{
			fullList.add(new PerlMethodMember(parentMethod));
		}

		// fixme not dry with PerlCodeGenerator
		final MemberChooser<PerlMethodMember> chooser =
				new MemberChooser<PerlMethodMember>(fullList.toArray(new PerlMethodMember[fullList.size()]), false, false, subBase.getProject())
				{
					@Override
					protected SpeedSearchComparator getSpeedSearchComparator()
					{
						return new SpeedSearchComparator(false)
						{
							@Nullable
							@Override
							public Iterable<TextRange> matchingFragments(String pattern, String text)
							{
								return super.matchingFragments(PerlMethodMember.trimUnderscores(pattern), text);
							}
						};
					}

					@Override
					protected ShowContainersAction getShowContainersAction()
					{
						return new ShowContainersAction(IdeBundle.message("action.show.classes"), PerlIcons.PACKAGE_GUTTER_ICON);
					}
				};

		chooser.setTitle("Choose Method To Rename");
		chooser.setCopyJavadocVisible(false);

		chooser.show();
		if (chooser.getExitCode() != DialogWrapper.OK_EXIT_CODE)
		{
			return subBase;
		}

		List<PerlMethodMember> selectedElements = chooser.getSelectedElements();

		if (selectedElements != null && selectedElements.size() > 0)
		{
			return selectedElements.get(0).getPsiElement();
		}

		return subBase;
	}
}
