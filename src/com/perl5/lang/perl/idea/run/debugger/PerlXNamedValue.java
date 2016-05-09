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

import com.google.gson.Gson;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.xdebugger.frame.*;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.run.debugger.protocol.PerlValueDescriptor;
import com.perl5.lang.perl.idea.run.debugger.protocol.PerlValueRequestDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by hurricup on 08.05.2016.
 */
public class PerlXNamedValue extends XNamedValue
{
	private final PerlStackFrame myStackFrame;
	private final PerlValueDescriptor myPerlValueDescriptor;
	private int offset = 0;

	public PerlXNamedValue(@NotNull PerlValueDescriptor descriptor, PerlStackFrame stackFrame)
	{
		super(descriptor.getName());
		myPerlValueDescriptor = descriptor;
		myStackFrame = stackFrame;
	}

	@Override
	public void computeChildren(@NotNull XCompositeNode node)
	{
		if (!myPerlValueDescriptor.isExpandable() || StringUtil.isEmpty(myPerlValueDescriptor.getKey()))
		{
			super.computeChildren(node);
		}
		//
		PerlDebugThread thread = myStackFrame.getPerlExecutionStack().getSuspendContext().getDebugThread();


		final int frameSize = XCompositeNode.MAX_CHILDREN_TO_SHOW;
		String result = thread.sendCommandAndGetResponse("getchildren", new PerlValueRequestDescriptor(offset, frameSize, myPerlValueDescriptor.getKey()));
		PerlValueDescriptor[] descriptors = new Gson().fromJson(result, PerlValueDescriptor[].class);

		XValueChildrenList list = new XValueChildrenList();
		for (PerlValueDescriptor descriptor : descriptors)
		{
			list.add(new PerlXNamedValue(descriptor, myStackFrame));

			offset++;
		}
		boolean isLast = offset >= myPerlValueDescriptor.getSize();
		node.addChildren(list, isLast);
		if (!isLast)
		{
			node.tooManyChildren(myPerlValueDescriptor.getSize() - offset);
		}
	}

	@Override
	public void computePresentation(@NotNull XValueNode node, @NotNull XValuePlace place)
	{
		node.setPresentation(calculateIcon(), calculateType(), myPerlValueDescriptor.getValue(), myPerlValueDescriptor.isExpandable());
	}

	protected String calculateType()
	{
		String value = myPerlValueDescriptor.getType();

		int refDepth = myPerlValueDescriptor.getRefDepth();
		if (refDepth == 1)
		{
			value = "REF to " + value;
		}
		else if (refDepth > 0)
		{
			value = "REF(" + refDepth + ") to " + value;
		}


		return value;
	}

	@Nullable
	protected Icon calculateIcon()
	{
		String type = myPerlValueDescriptor.getType();
		if (StringUtil.isEmpty(type))
		{
			return null;
		}
		else if (StringUtil.equals(type, "SCALAR"))
		{
			return PerlIcons.SCALAR_GUTTER_ICON;
		}
		else if (StringUtil.equals(type, "ARRAY"))
		{
			return PerlIcons.ARRAY_GUTTER_ICON;
		}
		else if (StringUtil.equals(type, "HASH"))
		{
			return PerlIcons.HASH_GUTTER_ICON;
		}
		else if (StringUtil.equals(type, "CODE"))
		{
			return PerlIcons.SUB_GUTTER_ICON;
		}
		else if (StringUtil.equals(type, "GLOB"))
		{
			return PerlIcons.GLOB_GUTTER_ICON;
		}
		else if (StringUtil.equals(type, "FORMAT"))
		{
			return PerlIcons.FORMAT_GUTTER_ICON;
		}
		else if (StringUtil.equals(type, "IO::File"))
		{
			return PerlIcons.HANDLE_GUTTER_ICON;
		}
		else if (StringUtil.equals(type, "Regexp"))
		{
			return PerlIcons.REGEX_GUTTER_ICON;
		}
		else if (myPerlValueDescriptor.isBlessed())
		{
			return PerlIcons.PACKAGE_GUTTER_ICON;
		}
		return null;
	}
}
