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

package com.perl5.lang.perl.idea.refactoring;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.refactoring.RefactoringFactory;
import com.intellij.refactoring.RenameRefactoring;

/**
 * Created by hurricup on 29.05.2015.
 */
public class RenameRefactoringQueue	implements Runnable
{
	private Project myProject;
	private RenameRefactoring myRefactoring;

	public RenameRefactoringQueue(Project project)
	{
		myProject = project;
	}

	public void addElement(PsiElement element, String newName)
	{


		if( element instanceof PsiNamedElement )
		{
			if (myRefactoring == null)
				myRefactoring = RefactoringFactory.getInstance(myProject).createRename(element, newName);
			else
				myRefactoring.addElement(element,newName);
		}
	}

	void setSearchInComments(boolean value)
	{
		if(myRefactoring != null)
			myRefactoring.setSearchInComments(value);
	};


	void setSearchInNonJavaFiles(boolean value)
	{
		if(myRefactoring != null)
			myRefactoring.setSearchInNonJavaFiles(value);
	}

	public void run()
	{
		if(myRefactoring != null)
			myRefactoring.run();
	}

	public Project getProject()
	{
		return myProject;
	}
}
