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

/**
 * Created by hurricup on 19.07.2015.
 */
public class CreateEmbeddedPerlFileAction extends PerlFileFromTemplateAction
{
	private static final String NEW_PERL_FILE = "New Embedded Perl5 File";

	public CreateEmbeddedPerlFileAction()
	{
		super(NEW_PERL_FILE, "", PerlIcons.EMBEDDED_PERL_FILE);
	}

	@Override
	protected void buildDialog(Project project, PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder)
	{
		builder
				.setTitle(NEW_PERL_FILE)
				.addKind("Embedded Perl5 File", PerlIcons.EMBEDDED_PERL_FILE, "Perl5 embedded")
		;
	}

	@Override
	protected String getActionName(PsiDirectory directory, String newName, String templateName)
	{
		return NEW_PERL_FILE;
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof CreateEmbeddedPerlFileAction;
	}
}
