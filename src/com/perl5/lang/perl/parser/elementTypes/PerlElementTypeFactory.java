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

package com.perl5.lang.perl.parser.elementTypes;

import com.intellij.lang.ASTNode;
import com.intellij.psi.impl.source.tree.PsiCommentImpl;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.idea.stubs.PerlStubElementTypes;
import com.perl5.lang.perl.psi.impl.*;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Set;

/**
 * Created by hurricup on 25.05.2015.
 */
public class PerlElementTypeFactory
{
	protected final static Set<String> STRING_TOKENS = new THashSet<String>(Arrays.asList(
			"STRING_PLUS",
			"STRING_IDENTIFIER",
			"STRING_PACKAGE",
			"STRING_CONTENT"
	));

	public static IElementType getTokenType(@NotNull String name)
	{
		if (STRING_TOKENS.contains(name))
			return new PerlStringContentTokenType(name);
		if (name.equals("HEREDOC_END"))
			return new PerlTokenTypeEx(name)
			{
				@NotNull
				@Override
				public ASTNode createLeafNode(CharSequence leafText)
				{
					return new PerlHeredocTerminatorElementImpl(this, leafText);
				}
			};
		if (name.equals("VARIABLE_NAME"))
			return new PerlTokenTypeEx(name)
			{
				@NotNull
				@Override
				public ASTNode createLeafNode(CharSequence leafText)
				{
					return new PerlVariableNameElementImpl(this, leafText);
				}
			};
		if (name.equals("SUB"))
			return new PerlTokenTypeEx(name)
			{
				@NotNull
				@Override
				public ASTNode createLeafNode(CharSequence leafText)
				{
					return new PerlSubNameElementImpl(this, leafText);
				}
			};
		if (name.equals("PACKAGE"))
			return new PerlTokenTypeEx(name)
			{
				@NotNull
				@Override
				public ASTNode createLeafNode(CharSequence leafText)
				{
					return new PerlNamespaceElementImpl(this, leafText);
				}
			};
		if (name.equals("VERSION_ELEMENT"))
			return new PerlTokenTypeEx(name)
			{
				@NotNull
				@Override
				public ASTNode createLeafNode(CharSequence leafText)
				{
					return new PerlVersionElementImpl(this, leafText);
				}
			};
		if (name.equals("POD"))
			return new PerlTokenTypeEx(name)
			{
				@NotNull
				@Override
				public ASTNode createLeafNode(CharSequence leafText)
				{
					return new PsiCommentImpl(this, leafText);
				}
			};

		// fixme we should create self-convertable element types
		if (name.equals("HEREDOC_QQ") || name.equals("HEREDOC_QX") || name.equals("HEREDOC"))
			return new PerlHeredocElementType(name);
		if (name.equals("PARSABLE_STRING_USE_VARS"))
			return new PerlQQStringElementType(name);
		return new PerlTokenType(name);
	}

	public static IElementType getElementType(@NotNull String name)
	{
		// fixme we should create self-convertable element types
		if (name.equals("SUB_DEFINITION"))
			return PerlStubElementTypes.SUB_DEFINITION;
		else if (name.equals("METHOD_DEFINITION"))
			return PerlStubElementTypes.METHOD_DEFINITION;
		else if (name.equals("FUNC_DEFINITION"))
			return PerlStubElementTypes.FUNC_DEFINITION;
		else if (name.equals("SUB_DECLARATION"))
			return PerlStubElementTypes.SUB_DECLARATION;
		else if (name.equals("SUB_DECLARATION"))
			return PerlStubElementTypes.SUB_DECLARATION;
		else if (name.equals("GLOB_VARIABLE"))
			return PerlStubElementTypes.PERL_GLOB;
		else if (name.equals("NAMESPACE_DEFINITION"))
			return PerlStubElementTypes.PERL_NAMESPACE;
		else if (name.equals("VARIABLE_DECLARATION_WRAPPER"))
			return PerlStubElementTypes.PERL_VARIABLE_DECLARATION_WRAPPER;
		else if (name.equals("CONSTANT_NAME"))
			return PerlStubElementTypes.PERL_CONSTANT;

		else if (name.equals("USE_STATEMENT"))
			return PerlStubElementTypes.PERL_USE_STATEMENT;
		else if (name.equals("DO_EXPR"))
			return PerlStubElementTypes.PERL_DO_EXPR;
		else if (name.equals("REQUIRE_EXPR"))
			return PerlStubElementTypes.PERL_REQUIRE_EXPR;

		return new PerlElementType(name);
	}
}
