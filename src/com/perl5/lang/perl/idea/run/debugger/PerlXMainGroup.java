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

import com.intellij.xdebugger.frame.XCompositeNode;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.util.PerlDebugUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 10.05.2016.
 */
public class PerlXMainGroup extends PerlXValueGroup
{
	private final int mySize;
	private int[] offset = new int[]{0};

	public PerlXMainGroup(PerlStackFrame stackFrame, int size)
	{
		super("%main::", "Symbol Table", PerlIcons.MAIN_GUTTER_ICON, null, stackFrame, false);
		mySize = size;
	}

	@Override
	public void computeChildren(@NotNull XCompositeNode node)
	{
		PerlDebugUtil.requestAndComputeChildren(node, getStackFrame(), offset, getSize(), "*main::{HASH}");
	}

	public int getOffset()
	{
		return offset[0];
	}

	@Override
	public int getSize()
	{
		return mySize;
	}
}
