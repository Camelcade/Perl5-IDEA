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

package com.perl5.lang.perl.parser.moose.elementTypes;

import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.ILeafElementType;
import com.perl5.lang.perl.parser.elementTypes.PerlTokenType;
import com.perl5.lang.perl.parser.moose.psi.impl.PerlMooseInnerKeywordImpl;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 19.01.2016.
 */
public class PerlTokenTypeInner extends PerlTokenType implements ILeafElementType
{
	public PerlTokenTypeInner(@NotNull @NonNls String debugName)
	{
		super(debugName);
	}

	@NotNull
	@Override
	public ASTNode createLeafNode(CharSequence leafText)
	{
		return new PerlMooseInnerKeywordImpl(this, leafText);
	}
}
