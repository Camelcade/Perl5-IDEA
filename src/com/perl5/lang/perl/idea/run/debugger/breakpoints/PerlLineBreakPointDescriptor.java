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

package com.perl5.lang.perl.idea.run.debugger.breakpoints;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.xdebugger.XExpression;
import com.intellij.xdebugger.breakpoints.XLineBreakpoint;
import com.perl5.lang.perl.idea.run.debugger.PerlDebugThread;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 07.05.2016.
 */
public class PerlLineBreakPointDescriptor
{
	private String path;
	private int line;
	private boolean enabled;
	private String condition;
	private boolean remove;

	@Nullable
	public static PerlLineBreakPointDescriptor createFromBreakpoint(XLineBreakpoint<PerlLineBreakpointProperties> breakpoint, PerlDebugThread debugThread)
	{
		VirtualFile virtualFile;
		String fileUrl = breakpoint.getFileUrl();
		String filePath = null;

		if (fileUrl.startsWith("mock://"))
		{
			virtualFile = debugThread.getForeignVirtualFileByName(fileUrl);
			if (virtualFile != null)
			{
				filePath = PerlDebugThread.FOREIGN_FILE_NAME.get(virtualFile);
			}
		}
		else
		{
			virtualFile = VirtualFileManager.getInstance().findFileByUrl(fileUrl);
			if (virtualFile != null)
			{
				filePath = virtualFile.getCanonicalPath();
			}
		}


		PerlLineBreakPointDescriptor descriptor = null;
		if (virtualFile != null)
		{
			descriptor = new PerlLineBreakPointDescriptor();
			descriptor.path = filePath;
			descriptor.line = breakpoint.getLine();
			descriptor.enabled = breakpoint.isEnabled();
			descriptor.remove = false;

			XExpression conditionExpression = breakpoint.getConditionExpression();
			descriptor.condition = conditionExpression != null ? conditionExpression.getExpression() : "";
		}
		return descriptor;
	}

	@Nullable
	public static PerlLineBreakPointDescriptor createRemoveFromBreakpoint(XLineBreakpoint<PerlLineBreakpointProperties> breakpoint, PerlDebugThread debugThread)
	{
		PerlLineBreakPointDescriptor descriptor = createFromBreakpoint(breakpoint, debugThread);

		if (descriptor != null)
		{
			descriptor.remove = true;
		}

		return descriptor;
	}

	public String getPath()
	{
		return path;
	}

	public int getLine()
	{
		return line;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public String getCondition()
	{
		return condition;
	}

}
