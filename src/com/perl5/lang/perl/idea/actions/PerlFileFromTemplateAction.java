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
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.fileTypes.ex.FileTypeManagerEx;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.perl5.PerlBundle;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.filetemplates.PerlCreateFileFromTemplateHandler;
import com.perl5.lang.perl.util.PerlAnnotationsUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;

import java.util.ArrayList;
import java.util.List;

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
				.addKind(PerlBundle.message("perl.file.kind.package"), PerlIcons.PM_FILE, "Perl5 package")
				.addKind(PerlBundle.message("perl.file.kind.script"), PerlIcons.PERL_SCRIPT_FILE_ICON, "Perl5 script")
				.addKind(PerlBundle.message("perl.file.kind.test"), PerlIcons.TEST_FILE, "Perl5 test")
				.addKind(PerlBundle.message("perl.file.kind.pod"), PerlIcons.POD_FILE, "Perl5 pod")
				.addKind(PerlBundle.message("perl.file.kind.mojo"), PerlIcons.MOJO_FILE, "Perl5 mojolicious")
				.addKind(PerlBundle.message("perl.file.kind.embedded"), PerlIcons.EMBEDDED_PERL_FILE, "Perl5 embedded")
		;

		addAnnotationsPathIfPossible(project, directory, builder);

		FileTypeManagerEx fileTypeManager = FileTypeManagerEx.getInstanceEx();
		for (FileTemplate fileTemplate : FileTemplateManager.getInstance(project).getAllTemplates())
		{
			if (PerlCreateFileFromTemplateHandler.INSTANCE.handlesTemplate(fileTemplate))
			{
				builder.addKind(fileTemplate.getName(), fileTypeManager.getFileTypeByExtension(fileTemplate.getExtension()).getIcon(), fileTemplate.getName());
			}
		}
	}

	private void addAnnotationsPathIfPossible(Project project, PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder)
	{
		VirtualFile virtualFile = directory.getVirtualFile();
		VirtualFile applicationAnnotationsRoot = PerlAnnotationsUtil.getApplicationAnnotationsRoot();
		boolean addFile = false;
		if (applicationAnnotationsRoot != null && VfsUtilCore.isAncestor(applicationAnnotationsRoot, virtualFile, false))
		{
			addFile = true;
		}
		else
		{
			VirtualFile projectAnnotationsRoot = PerlAnnotationsUtil.getProjectAnnotationsRoot(project);
			if (projectAnnotationsRoot != null && VfsUtilCore.isAncestor(projectAnnotationsRoot, virtualFile, false))
			{
				addFile = true;
			}
		}
		if (addFile)
		{
			builder.addKind(PerlBundle.message("perl.external.annotations"), PerlIcons.PERL_EXTERNAL_ANNOTATIONS_LANGUAGE_ICON, "Perl5 external annotations");
		}
	}


	@Override
	protected String getActionName(PsiDirectory directory, String newName, String templateName)
	{
		return "Create Perl5 file " + newName;
	}

	@Override
	protected PsiFile createFileFromTemplate(String name, FileTemplate template, PsiDirectory dir)
	{
		final List<String> pathChunks = new ArrayList<String>();

		if (StringUtil.contains(name, PerlPackageUtil.PACKAGE_SEPARATOR))
		{
			pathChunks.addAll(StringUtil.split(name, PerlPackageUtil.PACKAGE_SEPARATOR));
		}
		else if (StringUtil.contains(name, "/"))
		{
			pathChunks.addAll(StringUtil.split(name, "/"));
		}
		else
		{
			pathChunks.add(name);
		}
		name = pathChunks.remove(pathChunks.size() - 1);

		for (String pathChunk : pathChunks)
		{
			if (StringUtil.isNotEmpty(pathChunk))
			{
				final PsiDirectory sub = dir.findSubdirectory(pathChunk);
				dir = sub == null ? dir.createSubdirectory(pathChunk) : sub;
			}
		}
		return super.createFileFromTemplate(name, template, dir);
	}
}
