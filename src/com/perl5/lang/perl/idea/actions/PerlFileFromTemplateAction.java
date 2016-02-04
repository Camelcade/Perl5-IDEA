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

import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.perl5.PerlIcons;

/**
 * Created by hurricup on 26.08.2015.
 */
public class PerlFileFromTemplateAction extends CreateFileFromTemplateAction implements DumbAware
{
	public static final String ACTION_TITLE = "New Perl5 file";

	public PerlFileFromTemplateAction()
	{
		super("Perl5 File", "Creates a Perl5 file from the specified template", PerlIcons.PERL_LANGUAGE_ICON);

	}

	@Override
	protected void buildDialog(Project project, PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder)
	{
		builder
				.setTitle(ACTION_TITLE)
				.addKind("Package", PerlIcons.PM_FILE, "Perl5 package")
				.addKind("Script", PerlIcons.PERL_SCRIPT_FILE_ICON, "Perl5 script")
				.addKind("Test", PerlIcons.TEST_FILE, "Perl5 test")
				.addKind("Mojolicious Template File", PerlIcons.MOJO_FILE, "Perl5 mojolicious")
				.addKind("Embedded Perl5 File", PerlIcons.EMBEDDED_PERL_FILE, "Perl5 embedded")
		;
	}

	@Override
	protected String getActionName(PsiDirectory directory, String newName, String templateName)
	{
		return "Create Perl5 file " + newName;
	}

}
