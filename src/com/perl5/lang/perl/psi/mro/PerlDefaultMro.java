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

package com.perl5.lang.perl.psi.mro;

import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import com.perl5.lang.perl.util.PerlUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by hurricup on 15.06.2015.
 * Class represents default Perl's MRO.
 * In other words, it knows how to find sub definition and/or declaration
 */
public class PerlDefaultMro
{
	public static Collection<PsiPerlSubDefinition> getSubDefinitions(PerlMethod perlMethod)
	{
		Collection<PsiPerlSubDefinition> result = new ArrayList<>();

		PerlSubNameElement subNameElement = perlMethod.getSubNameElement();
		if( subNameElement != null )
		{

			PerlNamespaceElement namespaceElement = perlMethod.getNamespaceElement();
			PsiElement callElement = perlMethod.getParent();
			PsiElement callContainer = callElement.getParent();

//			assert callElement instanceof PsiPerlRightwardCallExpr || callElement instanceof PsiPerlFunctionCallExpr;

			PsiElement baseElement = null;
			if (callContainer instanceof PerlDerefExpression)
				baseElement = PerlUtil.getPrevDereferenceElement(callElement);

			if (baseElement != null)
			{
				if( namespaceElement != null && "SUPER".equals(namespaceElement.getCanonicalName()))
				{
					// expr->SUPER::sub

				}
				else
				{
					if( baseElement instanceof PsiPerlNamespaceExpr)
					{
						// package->sub

					}
//					else if( baseElement instanceof PsiPerlString)
//					{
//						// string->sub
//					}
					else if (baseElement instanceof PsiPerlVariableExpr)
					{
						// $scalar->sub
					}
					// expr->sub
				}
			} else
			{
				// sub
				String packageName;
				if( namespaceElement != null )
					packageName = namespaceElement.getCanonicalName();
				else
					packageName = PerlPackageUtil.getContextPackageName(callElement);

				String canonicalName = packageName + "::" + subNameElement.getName();
				result = PerlSubUtil.findSubDefinitions(callElement.getProject(), canonicalName);
			}
		}
		return result;
	}
}
