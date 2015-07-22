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

package com.perl5.lang.perl.idea.quickfixes;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.PerlUseStatement;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 19.07.2015.
 */
public class PerlUsePackageQuickFix implements LocalQuickFix
{
	String myPackageName;

	public PerlUsePackageQuickFix(String packageName)
	{
		myPackageName = packageName;
	}

	@Nls
	@NotNull
	@Override
	public String getName()
	{
		return String.format("Add use %s;", myPackageName);
	}

	@NotNull
	@Override
	public String getFamilyName()
	{
		return "Add use statement";
	}

	@Override
	public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor)
	{
		PerlUseStatement newStatement = PerlElementFactory.createUseStatement(project, myPackageName);
		PsiElement statementContainer = descriptor.getPsiElement();

		// fixme the best way here is to add next usage between use pragma and use package; Will help to keep consistency
		PsiElement baseElement = PsiTreeUtil.findChildOfType(statementContainer, PerlUseStatement.class);
		if (baseElement != null)
		{
			if (((PerlUseStatement) baseElement).isPragmaOrVersion() ) // pragma or version
			{
				while (true	)
				{
					// trying to find next use statement
					PsiElement nextStatement = baseElement;

					while ((nextStatement = nextStatement.getNextSibling()) != null
							&& (nextStatement instanceof PsiWhiteSpace || nextStatement instanceof PsiComment)
							){}

					if (nextStatement instanceof PerlUseStatement && ((PerlUseStatement) nextStatement).isPragmaOrVersion())	// found more use pragma/version
						baseElement = nextStatement;
					else
						break;
				}
			}
			else    // not a pragma
			{
				baseElement = baseElement.getPrevSibling();
				if (baseElement instanceof PsiWhiteSpace) // newline
					baseElement = baseElement.getPrevSibling();
			}
		}
		else
		{
			for (PsiElement checkElement : currentElementChildren)
				if (checkElement instanceof PsiComment)
					baseElement = checkElement;
				else if (!(checkElement instanceof PsiWhiteSpace))
					break;
		}

		PsiElement newLineElement = PerlElementFactory.createNewLine(project);
		if (baseElement != null) // add after element
		{
			PsiElement containerElement = baseElement.getParent();
			containerElement.addAfter(newStatement, baseElement);
			containerElement.addAfter(newLineElement, baseElement);
		} else if (currentElementChildren.length > 0)    // add before the first valid element
		{
			currentElement.addBefore(newStatement, currentElementChildren[0]);
			currentElement.addBefore(newLineElement, currentElementChildren[0]);
		} else    // add as first element of the file
		{
			currentElement.add(newStatement);
			currentElement.add(newLineElement);
		}
	}
}
