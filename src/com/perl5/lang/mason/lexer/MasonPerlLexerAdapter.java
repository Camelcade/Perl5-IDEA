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

package com.perl5.lang.mason.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.lexer.LexerBase;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.mojolicious.lexer.MojoliciousPerlLexer;
import com.perl5.lang.perl.lexer.PerlLexer;
import com.perl5.lang.perl.lexer.PerlLexerGenerated;
import com.perl5.lang.perl.lexer.PerlLexerWithCustomStatesAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by hurricup on 20.12.2015.
 */
public class MasonPerlLexerAdapter extends PerlLexerWithCustomStatesAdapter
{
	public MasonPerlLexerAdapter(Project project)
	{
		super(new MasonPerlLexer(project));
	}
}
