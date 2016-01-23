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

package com.perl5.lang.mason2;

import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.perl5.lang.mason2.elementType.MasonFileElementType;
import com.perl5.lang.mason2.lexer.MasonTemplatingLexerAdapter;
import com.perl5.lang.mason2.psi.impl.MasonTemplatingFileImpl;
import com.perl5.lang.perl.parser.MasonTemplatingParserImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 13.01.2016.
 */
public class MasonTemplatingParserDefinition extends MasonParserDefinition
{
	public static final IFileElementType FILE = new MasonFileElementType("Mason component", MasonTemplatingLanguage.INSTANCE);

	@Override
	public IFileElementType getFileNodeType()
	{
		return FILE;
	}

	@NotNull
	@Override
	public Lexer createLexer(Project project)
	{
		return new MasonTemplatingLexerAdapter(project);
	}

	@NotNull
	@Override
	public PsiParser createParser(Project project)
	{
		return new MasonTemplatingParserImpl();
	}

	@Override
	public PsiFile createFile(FileViewProvider viewProvider)
	{
		return new MasonTemplatingFileImpl(viewProvider);
	}

}
