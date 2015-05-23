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

package com.perl5.lang.perl.idea.references;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.psi.PerlNamedElement;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 26.04.2015.
 */
//public class PerlReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference
//{
////	private String key;
////
////	public PerlReference(@NotNull PsiElement element, TextRange textRange) {
////		super(element, textRange);
////		key = element.getText().substring(textRange.getStartOffset(), textRange.getEndOffset());
////	}
////
////	@NotNull
////	@Override
////	public Object[] getVariants()
////	{
////		Project project = myElement.getProject();
////		List<PsiElement> perlBarePackages = PerlPackageUtil.findPackageDefinitions(project);
////		List<LookupElement> variants = new ArrayList<LookupElement>();
////		for (final PsiElement perlBarePackage : perlBarePackages) {
////			variants.add(LookupElementBuilder.create((PerlNamedElement)perlBarePackage).
////							withIcon(PerlIcons.PM_FILE).
////							withTypeText(perlBarePackage.getContainingFile().getName())
////			);
////		}
////		return variants.toArray();
////	}
////
////	@NotNull
////	@Override
////	public ResolveResult[] multiResolve(boolean incompleteCode)
////	{
////		Project project = myElement.getProject();
////		// @todo not sure we should find only definitions here
////		final List<PsiElement> perlBarePackages = PerlPackageUtil.findPackageDefinitions(project, key);
////		List<ResolveResult> results = new ArrayList<ResolveResult>();
////		for (PsiElement perlBarePackage: perlBarePackages) {
////			results.add(new PsiElementResolveResult(perlBarePackage));
////		}
////		return results.toArray(new ResolveResult[results.size()]);
////	}
////
////	@Nullable
////	@Override
////	public PsiElement resolve()
////	{
////		ResolveResult[] resolveResults = multiResolve(false);
////		return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
////	}
//}
