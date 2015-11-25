/*
 * Copyright 2015 Alexandr Evstigneev
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

package com.perl5.lang.perl.parser;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.parser.builder.PerlBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Created by hurricup on 22.11.2015.
 */
public abstract class PerlParserExtension
{
	public static final ExtensionPointName<PerlParserExtension> PERL_PARSER_EXTENSION_EP = ExtensionPointName.create("com.perl5.parserExtension");

	/**
	 * Returns a set of keywords and element types to lex.
	 *
	 * @return set of custom keywords
	 */
	@NotNull
	public abstract Map<String, IElementType> getReservedTokens();

	/**
	 * Parse method.
	 * You may re-use PerlParser static methods to implement native perl expressions
	 *
	 * @param b PerlBuilder
	 * @param l parsing level
	 * @return parsing result
	 */
	public abstract boolean parse(PerlBuilder b, int l);

	/**
	 * Creating PsiElement by node type. If not handled by current extension, returns null
	 *
	 * @param node node to handle
	 * @return class instance or null if undhandled
	 */
	@Nullable
	public PsiElement createElement(@NotNull ASTNode node)
	{
		return null;
	}

}
