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
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.util.PerlPackageUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hurricup on 26.08.2015.
 */
public class PerlFileFromTemplateAction extends CreateFileFromTemplateAction implements DumbAware
{
	public static final String ACTION_TITLE = "New Perl5 file";
	public static final String PACKAGE_TEMPLATE_NAME = "Perl5 package";
	public static final String POD_TEMPLATE_NAME = "Perl5 pod";

	private static final List<String> TEMPLATES_WITH_PATH = new ArrayList<String>(
			Arrays.asList(
					PACKAGE_TEMPLATE_NAME,
					POD_TEMPLATE_NAME
			)
	);

	public PerlFileFromTemplateAction()
	{
		super("Perl5 File", "Creates a Perl5 file from the specified template", PerlIcons.PERL_LANGUAGE_ICON);

	}

	@Override
	protected void buildDialog(Project project, PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder)
	{
		builder
				.setTitle(ACTION_TITLE)
				.addKind("Package", PerlIcons.PM_FILE, PACKAGE_TEMPLATE_NAME)
				.addKind("Script", PerlIcons.PERL_SCRIPT_FILE_ICON, "Perl5 script")
				.addKind("Test", PerlIcons.TEST_FILE, "Perl5 test")
				.addKind("POD file", PerlIcons.POD_FILE, POD_TEMPLATE_NAME)
				.addKind("Mojolicious Template File", PerlIcons.MOJO_FILE, "Perl5 mojolicious")
				.addKind("Embedded Perl5 File", PerlIcons.EMBEDDED_PERL_FILE, "Perl5 embedded")
		;
	}

	@Override
	protected String getActionName(PsiDirectory directory, String newName, String templateName)
	{
		return "Create Perl5 file " + newName;
	}

	@Override
	protected PsiFile createFileFromTemplate(String name, FileTemplate template, PsiDirectory dir)
	{
		if (TEMPLATES_WITH_PATH.contains(template.getName()) && StringUtil.contains(name, PerlPackageUtil.PACKAGE_SEPARATOR))
		{
			final List<String> packageChunks = StringUtil.split(name, PerlPackageUtil.PACKAGE_SEPARATOR);
			name = packageChunks.remove(packageChunks.size() - 1);
			for (String packageChunk : packageChunks)
			{
				final PsiDirectory sub = dir.findSubdirectory(packageChunk);
				dir = sub == null ? dir.createSubdirectory(packageChunk) : sub;
			}
		}
		return super.createFileFromTemplate(name, template, dir);
	}
}
