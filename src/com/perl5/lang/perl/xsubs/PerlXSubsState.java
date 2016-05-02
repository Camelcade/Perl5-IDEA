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

package com.perl5.lang.perl.xsubs;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.CapturingProcessHandler;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationListener;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.components.*;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.util.FileContentUtil;
import com.intellij.util.Processor;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import com.perl5.lang.perl.util.PerlPluginUtil;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.event.HyperlinkEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by hurricup on 01.05.2016.
 */

@State(
		name = "Perl5XSubsState",
		storages = {
				@Storage(id = "workspace", file = StoragePathMacros.WORKSPACE_FILE)
		}
)

public class PerlXSubsState implements PersistentStateComponent<PerlXSubsState>
{
	@Transient
	public static final String DEPARSED_FILE_NAME = "_Deparsed_XSubs.pm";
	@Transient
	public static final String PERL_XSUBS_NOTIFICATION_GROUP = "PERL5_XSUBS";
	public boolean isActual = true;
	public Map<String, Long> filesMap = new THashMap<String, Long>();
	@Transient
	private Task.Backgroundable parserTask = null;
	@Transient
	private Project myProject;

	public static PerlXSubsState getInstance(@NotNull Project project)
	{
		PerlXSubsState persisted = ServiceManager.getService(project, PerlXSubsState.class);
		if (persisted == null)
		{
			persisted = new PerlXSubsState();
		}

		persisted.setProject(project);
		return persisted;
	}

	private static boolean isXSFile(@NotNull VirtualFile file)
	{
		if (!file.isValid() || file.isDirectory() || file instanceof LightVirtualFile)
		{
			return false;
		}

		String name = file.getName();


		if (SystemInfo.isWindows && StringUtil.endsWith(name, ".xs.dll") || !SystemInfo.isWindows && StringUtil.endsWith(name, ".so"))
		{
			String path = file.getCanonicalPath();
			return path != null && StringUtil.contains(path, "/auto/");
		}
		return false;
	}

	public void setProject(Project myProject)
	{
		this.myProject = myProject;
	}

	@Nullable
	@Override
	public PerlXSubsState getState()
	{
		return this;
	}

	@Override
	public void loadState(PerlXSubsState state)
	{
		XmlSerializerUtil.copyBean(state, this);
	}

	public void rescanFiles()
	{
		final int[] actualFiles = new int[]{0};
		for (VirtualFile classRoot : ProjectRootManager.getInstance(myProject).orderEntries().getClassesRoots())
		{
			VfsUtil.processFilesRecursively(classRoot, new Processor<VirtualFile>()
			{
				@Override
				public boolean process(VirtualFile virtualFile)
				{
					if (isXSFile(virtualFile))
					{
						if (!isFileUpToDate(virtualFile))
						{
							return isActual = false;
						}
						else
						{
							actualFiles[0]++;
						}
					}
					return true;
				}
			});
		}

		if (actualFiles[0] > 0)
		{
			VirtualFile projectRoot = myProject.getBaseDir();
			VirtualFile deparsedFile = projectRoot.findFileByRelativePath(DEPARSED_FILE_NAME);
			if (deparsedFile == null)
			{
				isActual = false;
			}
		}
	}

	private boolean isFileUpToDate(VirtualFile virtualFile)
	{
		String path = virtualFile.getCanonicalPath();

		if (path != null)
		{
			Long modificationStamp = filesMap.get(path);
			return modificationStamp != null && modificationStamp == VfsUtilCore.virtualToIoFile(virtualFile).lastModified();
		}
		return false;
	}

	public void notifyUser()
	{
		Notification notification = new Notification(
				PERL_XSUBS_NOTIFICATION_GROUP,
				"XSubs change detected",
				"<p>It seems that yor XSubs declarations file is absent or outdated.</p><br/>" +
						"<p>We recommend you to <a href=\"http://regenerate\">regenerate</a> it.</p><br/>" +
						"<p>You may do it any time in Perl5 settings.</p><br/>",
				NotificationType.INFORMATION,
				new NotificationListener.UrlOpeningListener(false)
				{
					@Override
					protected void hyperlinkActivated(@NotNull Notification notification, @NotNull HyperlinkEvent event)
					{
						reparseXSubs();
						notification.expire();
					}
				}
		);
		Notifications.Bus.notify(notification);

	}

	public void reparseXSubs()
	{
		if (parserTask != null)
		{
			Messages.showErrorDialog(myProject, "Another process currently deparsing XSubs, please wait for further notifications", "XSubs Deparsing In Process");
			return;
		}

		final Map<String, Long> newFilesMap = new THashMap<String, Long>();

		for (VirtualFile classRoot : ProjectRootManager.getInstance(myProject).orderEntries().getClassesRoots())
		{
			VfsUtil.processFilesRecursively(classRoot, new Processor<VirtualFile>()
			{
				@Override
				public boolean process(VirtualFile virtualFile)
				{
					if (isXSFile(virtualFile))
					{
						String filePath = virtualFile.getCanonicalPath();
						if (filePath != null)
						{
//							System.err.println("Found " + filePath);
							newFilesMap.put(filePath, VfsUtilCore.virtualToIoFile(virtualFile).lastModified());
						}
					}
					return true;
				}
			});
		}

		GeneralCommandLine commandLine = PerlPluginUtil.getPluginScriptCommandLine(myProject, "xs_parser_simple.pl");

		if (commandLine != null)
		{
			try
			{
				final CapturingProcessHandler processHandler = new CapturingProcessHandler(commandLine.createProcess());

				parserTask = new Task.Backgroundable(myProject, "XSubs Scanning", false)
				{
					@Override
					public void run(@NotNull ProgressIndicator indicator)
					{
						ProcessOutput processOutput = processHandler.runProcess();
						final String stdout = processOutput.getStdout();
						List<String> stderr = processOutput.getStderrLines(false);

						final StringBuilder messageBuilder = new StringBuilder();
						if (!stderr.isEmpty())
						{
							for (String errorMessage : stderr)
							{
								if (errorMessage.equals("\n"))
								{
									messageBuilder.append("<br/>");
								}
								else
								{
									messageBuilder.append("<p>");
									messageBuilder.append(errorMessage);
									messageBuilder.append("</p>");
								}
							}
						}

						if (!stdout.isEmpty())
						{
							new WriteAction<Object>()
							{
								@Override
								protected void run(@NotNull Result<Object> result) throws Throwable
								{
									try
									{
										VirtualFile newFile = myProject.getBaseDir().findOrCreateChildData(this, DEPARSED_FILE_NAME);
										newFile.setWritable(true);
										OutputStream outputStream = newFile.getOutputStream(null);
										outputStream.write(stdout.getBytes());
										outputStream.close();
										newFile.setWritable(false);
										FileContentUtil.reparseFiles(newFile);

										isActual = true;

										messageBuilder.append("<p>");
										messageBuilder.append("Deparsing completed successfully!");
										messageBuilder.append("</p><br/>");

										if (messageBuilder.length() > 0)
										{
											Notifications.Bus.notify(new Notification(
													"PERL5_DEPARSING_REPORT",
													"XSubs deparsing finished",
													messageBuilder.toString(),
													NotificationType.INFORMATION
											));
										}

									} catch (IOException e)
									{
										Notifications.Bus.notify(new Notification(
												"PERL5_DEPARSING_ERROR",
												"Error creating XSubs deparsed file",
												e.getMessage(),
												NotificationType.ERROR
										));
									} finally
									{
										PerlXSubsState.this.parserTask = null;
									}
								}
							}.execute();
						}
					}
				};
				parserTask.queue();


			} catch (ExecutionException e)
			{
				Notifications.Bus.notify(new Notification(
						"PERL5_START_ERROR",
						"XSubs deparser report",
						e.getMessage(),
						NotificationType.ERROR
				));
			}
		}

		filesMap = newFilesMap;
	}

}
