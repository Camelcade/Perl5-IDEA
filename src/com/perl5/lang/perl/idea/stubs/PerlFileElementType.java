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

package com.perl5.lang.perl.idea.stubs;

import com.intellij.lang.*;
import com.intellij.lexer.Lexer;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IStubFileElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 25.05.2015.
 */
public class PerlFileElementType extends IStubFileElementType
{
	private static final int VERSION = 4;

	public PerlFileElementType(String debugName, Language language)
	{
		super(debugName, language);
	}

	@Override
	public int getStubVersion()
	{
		return VERSION;
	}

	@Override
	protected ASTNode doParseContents(@NotNull ASTNode chameleon, @NotNull PsiElement psi)
	{
		return getParser(psi).parse(this, getBuilder(psi, chameleon)).getFirstChildNode();
	}

	@Nullable
	protected Lexer getLexer(PsiElement psi)
	{
		return null;
	}

	@NotNull
	protected PsiParser getParser(PsiElement psi)
	{
		return LanguageParserDefinitions.INSTANCE.forLanguage(getLanguageForParser(psi)).createParser(psi.getProject());
	}

	@NotNull
	protected PsiBuilder getBuilder(PsiElement psi, ASTNode chameleon)
	{
		return PsiBuilderFactory.getInstance().createBuilder(psi.getProject(), chameleon, getLexer(psi), getLanguageForParser(psi), chameleon.getChars());
	}

}
