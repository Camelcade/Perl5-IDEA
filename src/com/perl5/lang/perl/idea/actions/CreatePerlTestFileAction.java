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

package com.perl5.lang.perl.idea.actions;

import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.perl5.PerlIcons;

import javax.swing.*;

/**
 * Created by hurricup on 27.09.2015.
 */
public class CreatePerlTestFileAction extends PerlFileFromTemplateAction
{
	private static final String ACTION_TITLE = "New Perl5 Test";
	private static final Icon ACTION_ICON = PerlIcons.TEST_FILE;

	public CreatePerlTestFileAction()
	{
		super(ACTION_TITLE, "", ACTION_ICON);
	}

	@Override
	protected void buildDialog(Project project, PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder)
	{
		builder
				.setTitle(ACTION_TITLE)
				.addKind("Test", ACTION_ICON, "Perl5 test")
		;
	}

	@Override
	protected String getActionName(PsiDirectory directory, String newName, String templateName)
	{
		return ACTION_TITLE;
	}

	@Override
	public int hashCode()
	{
		return 0;
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof CreatePerlTestFileAction;
	}
}
