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

package com.perl5.lang.perl.idea.annotators;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.CapturingProcessHandler;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.ExternalAnnotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationListener;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.perl5.lang.perl.idea.configuration.settings.PerlLocalSettings;
import com.perl5.lang.perl.idea.configuration.settings.PerlSettingsConfigurable;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.psi.PerlFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.event.HyperlinkEvent;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 16.04.2016.
 */
public class PerlCriticAnnotator extends ExternalAnnotator<PerlFile, List<PerlCriticErrorDescriptor>>
{
	public static final String PERL_CRITIC_LINUX_NAME = "perlcritic";
	public static final String PERL_CRITIC_WINDOWS_NAME = PERL_CRITIC_LINUX_NAME + ".bat";
	public static final String PERL_CRITIC_OS_DEPENDENT_NAME = SystemInfo.isWindows ? PERL_CRITIC_WINDOWS_NAME : PERL_CRITIC_LINUX_NAME;


	@Nullable
	@Override
	public PerlFile collectInformation(@NotNull PsiFile file)
	{
		return file instanceof PerlFile && file.isPhysical() && PerlSharedSettings.getInstance(file.getProject()).PERL_CRITIC_ENABLED ? (PerlFile) file : null;
	}

	protected GeneralCommandLine getPerlCriticExecutable(Project project) throws ExecutionException
	{
		PerlSharedSettings sharedSettings = PerlSharedSettings.getInstance(project);
		PerlLocalSettings localSettings = PerlLocalSettings.getInstance(project);
		String executable = localSettings.PERL_CRITIC_PATH;

		if (StringUtil.isEmpty(executable))
		{
			throw new ExecutionException("Path to Perl::Critic executable must be configured in perl settings");
		}
		GeneralCommandLine commandLine = new GeneralCommandLine(executable).withWorkDirectory(project.getBasePath());

		if (StringUtil.isNotEmpty(sharedSettings.PERL_CRITIC_ARGS))
		{
			commandLine.addParameters(StringUtil.split(sharedSettings.PERL_CRITIC_ARGS, " "));
		}

		return commandLine;
	}


	@Nullable
	@Override
	public List<PerlCriticErrorDescriptor> doAnnotate(final PerlFile sourcePsiFile)
	{
		if (sourcePsiFile == null)
		{
			return null;
		}

		byte[] sourceBytes = sourcePsiFile.getPerlContentInBytes();
		if (sourceBytes == null)
		{
			return null;
		}


		try
		{
			GeneralCommandLine perlcritic = getPerlCriticExecutable(sourcePsiFile.getProject());
			final Process process = perlcritic.createProcess();

			OutputStream outputStream = process.getOutputStream();
			outputStream.write(sourceBytes);
			outputStream.close();

			final CapturingProcessHandler processHandler = new CapturingProcessHandler(process);

			List<PerlCriticErrorDescriptor> errors = new ArrayList<PerlCriticErrorDescriptor>();
			PerlCriticErrorDescriptor lastDescriptor = null;
			for (String output : processHandler.runProcess().getStdoutLines())
			{
				PerlCriticErrorDescriptor fromString = PerlCriticErrorDescriptor.getFromString(output);
				if (fromString != null)
				{
					errors.add(lastDescriptor = fromString);
				}
				else if (lastDescriptor != null)
				{
					lastDescriptor.append(" " + output);
				}
				else if (!StringUtil.equals(output, "source OK"))
				{
					// fixme we could make some popup here
					System.err.println("Could not parse line: " + output);
				}
			}
			return errors;
		}
		catch (ExecutionException e)
		{
			Notifications.Bus.notify(new Notification(
					"PerlCritic error",
					"Perl::Critic failed to start and has been disabled",
					"<ul style=\"padding-left:10px;margin-left:0px;\">" +
							"<li>Make sure that Perl::Critic module is installed</li>" +
							"<li>Configure path to perlcritic executable in <a href=\"configure\">Perl5 settings</a> and re-enable it</li>" +
							"</ul>"
					,
					NotificationType.ERROR,
					new NotificationListener.Adapter()
					{
						@Override
						protected void hyperlinkActivated(@NotNull Notification notification, @NotNull HyperlinkEvent e)
						{
							Project project = sourcePsiFile.getProject();
							ShowSettingsUtil.getInstance().editConfigurable(project, new PerlSettingsConfigurable(project));
							notification.expire();
						}
					}
			));
			PerlSharedSettings.getInstance(sourcePsiFile.getProject()).PERL_CRITIC_ENABLED = false;

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void apply(@NotNull PsiFile file, List<PerlCriticErrorDescriptor> annotationResult, @NotNull AnnotationHolder holder)
	{
		if (annotationResult == null)
		{
			return;
		}

		Document document = file.getViewProvider().getDocument();
		if (document != null)
		{
			for (PerlCriticErrorDescriptor descriptor : annotationResult)
			{
				int line = descriptor.getLine();
				int col = descriptor.getCol();

				TextRange warningRange = null;
				if (col == 1) // suppose it's stupid output without column
				{
					warningRange = new TextRange(document.getLineStartOffset(line - 1), document.getLineEndOffset(line - 1));
				}
				else // trying to find real element
				{
					int offset = document.getLineStartOffset(line - 1) + col;
					PsiElement targetElement = file.findElementAt(offset);
					if (targetElement != null)
					{
						warningRange = targetElement.getTextRange();
					}
					else
					{
						System.err.println("Error creating annotation for " + descriptor);
					}
				}

				if (warningRange != null)
				{
					holder.createAnnotation(HighlightSeverity.WARNING, warningRange, descriptor.getMessage());
				}
			}
		}
	}
}
