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
import com.intellij.xdebugger.breakpoints.XLineBreakpoint;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 07.05.2016.
 */
public class PerlLineBreakPointDescriptor
{
	String path;
	int line;
	boolean enabled;
	String condition;

	@Nullable
	public static PerlLineBreakPointDescriptor createFromBreakpoint(XLineBreakpoint<PerlLineBreakpointProperties> breakpoint)
	{
		VirtualFile virtualFile = VirtualFileManager.getInstance().findFileByUrl(breakpoint.getFileUrl());

		PerlLineBreakPointDescriptor descriptor = null;
		if (virtualFile != null)
		{
			descriptor = new PerlLineBreakPointDescriptor();
			descriptor.path = virtualFile.getCanonicalPath();
			descriptor.line = breakpoint.getLine();
			descriptor.enabled = breakpoint.isEnabled();
			descriptor.condition = "";
		}
		return descriptor;
	}

}
