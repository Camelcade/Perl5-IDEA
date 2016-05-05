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

package com.perl5.lang.perl.idea.run.debugger;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.ColoredTextContainer;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.xdebugger.XSourcePosition;
import com.intellij.xdebugger.frame.XStackFrame;
import com.intellij.xdebugger.impl.XSourcePositionImpl;
import com.perl5.lang.perl.idea.run.debugger.protocol.PerlDebuggingEventStackFrame;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * Created by hurricup on 04.05.2016.
 */
public class PerlStackFrame extends XStackFrame
{
	private final PerlDebuggingEventStackFrame myEventStackFrame;
	private final VirtualFile myVirtualFile;

	public PerlStackFrame(PerlDebuggingEventStackFrame eventStackFrame)
	{
		myEventStackFrame = eventStackFrame;
		myVirtualFile = VfsUtil.findFileByIoFile(new File(eventStackFrame.getFile()), true);
	}

	@Override
	public void customizePresentation(@NotNull ColoredTextContainer component)
	{
		component.append(myEventStackFrame.getName(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
		component.setIcon(AllIcons.Debugger.StackFrame);
	}

	@Nullable
	@Override
	public XSourcePosition getSourcePosition()
	{
		if (myVirtualFile != null)
		{
			return XSourcePositionImpl.create(myVirtualFile, myEventStackFrame.getLine());
		}
		return super.getSourcePosition();
	}
}
