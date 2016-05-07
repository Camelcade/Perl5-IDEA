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

package com.perl5.lang.perl.util;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.xdebugger.XDebuggerManager;
import com.intellij.xdebugger.breakpoints.XLineBreakpoint;
import com.perl5.lang.perl.idea.run.debugger.breakpoints.PerlLineBreakpointProperties;
import com.perl5.lang.perl.idea.run.debugger.breakpoints.PerlLineBreakpointType;
import com.perl5.lang.perl.idea.run.debugger.protocol.PerlDebuggingEventBreakpointBase;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collection;

/**
 * Created by hurricup on 07.05.2016.
 */
public class PerlDebugUtils
{
	@Nullable
	public static XLineBreakpoint findBreakpoint(final Project project, final PerlDebuggingEventBreakpointBase breakpointBase)
	{
		final XLineBreakpoint[] result = new XLineBreakpoint[]{null};

		ApplicationManager.getApplication().runReadAction(new Runnable()
		{
			@Override
			public void run()
			{
				VirtualFile virtualFile = VfsUtil.findFileByIoFile(new File(breakpointBase.getPath()), true);
				if (virtualFile != null)
				{
					String virtualFileUrl = virtualFile.getUrl();

					Collection<? extends XLineBreakpoint<PerlLineBreakpointProperties>> breakpoints = XDebuggerManager.getInstance(project).getBreakpointManager().getBreakpoints(PerlLineBreakpointType.class);
					for (XLineBreakpoint<PerlLineBreakpointProperties> breakpoint : breakpoints)
					{
						if (StringUtil.equals(breakpoint.getFileUrl(), virtualFileUrl) && breakpoint.getLine() == breakpointBase.getLine())
						{
							result[0] = breakpoint;
							return;
						}
					}
				}

			}
		});
		return result[0];
	}

}
