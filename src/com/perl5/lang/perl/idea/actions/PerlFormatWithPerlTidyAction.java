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
import com.intellij.execution.process.CapturingProcessHandler;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.perl5.lang.perl.psi.PerlFile;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by hurricup on 17.04.2016.
 * For improvements, see UpdateCopyrightAction
 */
public class PerlFormatWithPerlTidyAction extends AnAction
{
	public static final String PERL_TIDY_GROUP = "PERL5_PERL_TIDY";

	private static boolean isEnabled(AnActionEvent event)
	{
		final PsiFile file = getPsiFileFromEvent(event);
		return file instanceof PerlFile && ((PerlFile) file).isPerlTidyReformattable() && file.isWritable();
	}

	@Nullable
	private static PsiFile getPsiFileFromEvent(AnActionEvent event)
	{
		final DataContext context = event.getDataContext();
		final Project project = CommonDataKeys.PROJECT.getData(context);
		if (project == null)
		{
			return null;
		}

		final Editor editor = CommonDataKeys.EDITOR.getData(context);
		if (editor != null)
		{
			return PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());

		}
		else
		{
			VirtualFile virtualFile = CommonDataKeys.VIRTUAL_FILE.getData(context);
			if (virtualFile != null)
			{
				return PsiManager.getInstance(project).findFile(virtualFile);
			}
		}
		return null;
	}

	@Override
	public void actionPerformed(AnActionEvent event)
	{
		if (isEnabled(event))
		{
			PsiFile file = getPsiFileFromEvent(event);

			if (file == null)
				return;

			final Project project = file.getProject();

			final Document document = file.getViewProvider().getDocument();
			if (document == null)
				return;

			VirtualFile virtualFile = file.getVirtualFile();

			if (virtualFile == null)
				return;

			try
			{
				byte[] sourceBytes = virtualFile.contentsToByteArray();
				GeneralCommandLine perltidy = new GeneralCommandLine(SystemInfo.isWindows ? "perltidy.bat" : "perltidy", "-st", "-se").withWorkDirectory(file.getProject().getBasePath());

				try
				{
					final Process process = perltidy.createProcess();
					OutputStream outputStream = process.getOutputStream();
					outputStream.write(sourceBytes);
					outputStream.close();

					final CapturingProcessHandler processHandler = new CapturingProcessHandler(process, virtualFile.getCharset());
					ProcessOutput processOutput = processHandler.runProcess();

					final List<String> stdoutLines = processOutput.getStdoutLines(false);
					List<String> stderrLines = processOutput.getStderrLines();

					if (stderrLines.isEmpty())
					{
						ApplicationManager.getApplication().runWriteAction(new Runnable()
						{
							@Override
							public void run()
							{
								document.setText(StringUtil.join(stdoutLines, "\n"));
								PsiDocumentManager.getInstance(project).commitDocument(document);
							}
						});
					}
					else
					{
						for (String line : stderrLines)
						{
							System.err.println(line);
						}

					}
				} catch (ExecutionException e)
				{
					Notifications.Bus.notify(new Notification(
							PERL_TIDY_GROUP,
							"Error running Perl::Tidy",
							e.getMessage(),
							NotificationType.ERROR
					));
				}
			} catch (IOException e)
			{
				Notifications.Bus.notify(new Notification(
						PERL_TIDY_GROUP,
						"Re-formatting error",
						e.getMessage(),
						NotificationType.ERROR
				));
			}
		}
	}

	@Override
	public void update(AnActionEvent event)
	{
		final boolean enabled = isEnabled(event);
		event.getPresentation().setEnabled(enabled);
		if (ActionPlaces.isPopupPlace(event.getPlace()))
		{
			event.getPresentation().setVisible(enabled);
		}
	}

}
