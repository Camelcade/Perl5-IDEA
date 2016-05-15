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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.AtomicNullableLazyValue;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.ui.ColoredTextContainer;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.xdebugger.XSourcePosition;
import com.intellij.xdebugger.evaluation.XDebuggerEvaluator;
import com.intellij.xdebugger.frame.XCompositeNode;
import com.intellij.xdebugger.frame.XStackFrame;
import com.intellij.xdebugger.frame.XValueChildrenList;
import com.intellij.xdebugger.impl.XSourcePositionImpl;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.run.debugger.protocol.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * Created by hurricup on 04.05.2016.
 */
public class PerlStackFrame extends XStackFrame
{
	private final PerlStackFrameDescriptor myFrameDescriptor;
	private final PerlExecutionStack myPerlExecutionStack;
	private AtomicNullableLazyValue<VirtualFile> myVirtualFile = new AtomicNullableLazyValue<VirtualFile>()
	{
		@Nullable
		@Override
		protected VirtualFile compute()
		{
			String myFileName = myFrameDescriptor.getFileDescriptor().getPath();

			VirtualFile result = VfsUtil.findFileByIoFile(new File(myFileName), true);
			return result == null ? VirtualFileManager.getInstance().findFileByUrl(PerlRemoteFileSystem.PROTOCOL_PREFIX + myFileName) : result;
		}
	};

	public PerlStackFrame(PerlStackFrameDescriptor frameDescriptor, PerlExecutionStack stack)
	{
		myFrameDescriptor = frameDescriptor;
		myPerlExecutionStack = stack;
		String source = myFrameDescriptor.getSource();
		PerlDebugThread debugThread = myPerlExecutionStack.getSuspendContext().getDebugThread();
		PerlLoadedFileDescriptor fileDescriptor = myFrameDescriptor.getFileDescriptor();

		if (source != null)
		{
			PerlRemoteFileSystem.getInstance().registerRemoteFile(fileDescriptor.getPath(), source);
		}

		if (fileDescriptor.isEval())
		{
			debugThread.getEvalsListPanel().add(fileDescriptor);
		}
		else
		{
			debugThread.getScriptListPanel().add(fileDescriptor);
		}
	}

	@Override
	public void customizePresentation(@NotNull ColoredTextContainer component)
	{
		component.append(myFrameDescriptor.getFileDescriptor().getNameOrPath(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
		component.setIcon(AllIcons.Debugger.StackFrame);
	}

	@Nullable
	@Override
	public XSourcePosition getSourcePosition()
	{
		VirtualFile virtualFile = myVirtualFile.getValue();
		if (virtualFile != null)
		{
			return XSourcePositionImpl.create(virtualFile, myFrameDescriptor.getLine());
		}
		return super.getSourcePosition();
	}

	@Override
	public void computeChildren(@NotNull XCompositeNode node)
	{
		PerlValueDescriptor[] lexicals = myFrameDescriptor.getLexicals();
		PerlValueDescriptor[] globals = myFrameDescriptor.getGlobals();
		PerlValueDescriptor[] args = myFrameDescriptor.getArgs();
		int mainSize = myFrameDescriptor.getMainSize();

		boolean fallback = true;

		XValueChildrenList list = new XValueChildrenList();

		if (globals != null && globals.length > 0)
		{
			list.addTopGroup(new PerlXValueGroup("Global variables", "our", PerlIcons.OUR_GUTTER_ICON, globals, this, false));
			fallback = false;
		}
		if (mainSize > 0)
		{
			list.addTopGroup(new PerlXMainGroup(this, mainSize));
			fallback = false;
		}
		if (args != null && args.length > 0)
		{
			list.addTopGroup(new PerlXValueGroup("Arguments", null, PerlIcons.ARGS_GUTTER_ICON, args, this, true));
			fallback = false;
		}
		if (lexicals != null && lexicals.length > 0)
		{
			list.addTopGroup(new PerlXValueGroup("Lexical variables", "my/state", PerlIcons.MY_GUTTER_ICON, lexicals, this, true));
			fallback = false;
		}


		if (fallback)
		{
			super.computeChildren(node);
		}
		else
		{
			node.addChildren(list, true);
		}

	}

	public PerlExecutionStack getPerlExecutionStack()
	{
		return myPerlExecutionStack;
	}

	@Nullable
	@Override
	public XDebuggerEvaluator getEvaluator()
	{
		return new XDebuggerEvaluator()
		{
			@Override
			public void evaluate(@NotNull String expression, @NotNull final XEvaluationCallback callback, @Nullable XSourcePosition expressionPosition)
			{
				PerlDebugThread thread = myPerlExecutionStack.getSuspendContext().getDebugThread();

				thread.sendCommandAndGetResponse("e", new PerlEvalRequestDescriptor(expression), new PerlDebuggingTransactionHandler()
				{
					@Override
					public void run(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext)
					{
						PerlEvalResponseDescriptor descriptor = jsonDeserializationContext.deserialize(
								jsonObject.getAsJsonObject("data"), PerlEvalResponseDescriptor.class
						);

						if (descriptor == null)
						{
							callback.errorOccurred("Something bad happened on Perl side. Report to plugin devs.");
						}
						else if (descriptor.isError())
						{
							callback.errorOccurred(descriptor.getResult().getValue());
						}
						else
						{
							callback.evaluated(new PerlXNamedValue(descriptor.getResult(), PerlStackFrame.this));
						}
					}
				});
			}
		};
	}
}
