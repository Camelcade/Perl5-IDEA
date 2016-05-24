/*
 * Copyright 2016 Alexandr Evstigneev
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

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.execution.util.ExecUtil;
import com.intellij.ide.actions.OpenFileAction;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.LightVirtualFile;
import com.perl5.lang.mason2.filetypes.MasonPurePerlComponentFileType;
import com.perl5.lang.perl.fileTypes.PerlFileType;
import com.perl5.lang.perl.fileTypes.PerlFileTypePackage;
import com.perl5.lang.perl.fileTypes.PerlFileTypeTest;
import com.perl5.lang.perl.idea.configuration.settings.Perl5Settings;
import com.perl5.lang.perl.util.PerlActionUtil;
import com.perl5.lang.perl.util.PerlRunUtil;
import gnu.trove.THashSet;

import java.util.Set;

/**
 * Created by hurricup on 26.04.2016.
 */
public class PerlDeparseFileAction extends PerlActionBase
{
	public static final Set<FileType> DEPARSABLE_TYPES = new THashSet<FileType>();
	private static final String PERL_DEPARSE_GROUP = "PERL5_DEPARSE_FILE";

	static
	{
		DEPARSABLE_TYPES.add(PerlFileType.INSTANCE);
		DEPARSABLE_TYPES.add(PerlFileTypePackage.INSTANCE);
		DEPARSABLE_TYPES.add(PerlFileTypeTest.INSTANCE);
		DEPARSABLE_TYPES.add(MasonPurePerlComponentFileType.INSTANCE);
	}

	@Override
	public void actionPerformed(AnActionEvent event)
	{
		if (isEnabled(event))
		{
			PsiFile file = PerlActionUtil.getPsiFileFromEvent(event);

			if (file == null)
				return;

			final Document document = file.getViewProvider().getDocument();
			if (document == null)
				return;

			final Project project = file.getProject();

			String deparseArgument = "-MO=Deparse";
			Perl5Settings perl5Settings = Perl5Settings.getInstance(project);
			if (StringUtil.isNotEmpty(perl5Settings.PERL_DEPARSE_ARGUMENTS))
			{
				deparseArgument += "," + perl5Settings.PERL_DEPARSE_ARGUMENTS;
			}

			GeneralCommandLine commandLine = PerlRunUtil.getPerlCommandLine(project, file.getVirtualFile(), deparseArgument);

			if (commandLine != null)
			{
				commandLine.withWorkDirectory(project.getBasePath());
				FileDocumentManager.getInstance().saveDocument(document);
				try
				{
					ProcessOutput processOutput = ExecUtil.execAndGetOutput(commandLine);
					String deparsed = processOutput.getStdout();
					String error = processOutput.getStderr();

					if (StringUtil.isNotEmpty(error) && !StringUtil.contains(error, "syntax OK"))
					{
						if (StringUtil.isEmpty(deparsed))
						{
							Notifications.Bus.notify(new Notification(
									PERL_DEPARSE_GROUP,
									"Deparsing error",
									error.replaceAll("\\n", "<br/>"),
									NotificationType.ERROR
							));
						}
						else
						{
							Notifications.Bus.notify(new Notification(
									PERL_DEPARSE_GROUP,
									"Deparsing Completed",
									"XSubs deparsing completed, but some errors occurred during the process:<br/>" +
											error.replaceAll("\\n", "<br/>"),
									NotificationType.INFORMATION
							));
						}
					}

					if (StringUtil.isNotEmpty(deparsed))
					{
						VirtualFile newFile = new LightVirtualFile("Deparsed " + file.getName(), file.getFileType(), deparsed);
						OpenFileAction.openFile(newFile, project);
					}
				} catch (ExecutionException e)
				{
					Notifications.Bus.notify(new Notification(
							PERL_DEPARSE_GROUP,
							"Error starting perl process",
							e.getMessage(),
							NotificationType.ERROR
					));
				}

			}
		}
	}

	@Override
	protected boolean isEnabled(AnActionEvent event)
	{
		final PsiFile file = PerlActionUtil.getPsiFileFromEvent(event);
		return file != null && file.isPhysical() && DEPARSABLE_TYPES.contains(file.getFileType());
	}
}
