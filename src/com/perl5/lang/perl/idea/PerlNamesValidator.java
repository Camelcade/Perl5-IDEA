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

package com.perl5.lang.perl.idea;

import com.intellij.lang.refactoring.NamesValidator;
import com.intellij.openapi.project.Project;
import com.perl5.lang.perl.lexer.PerlBaseLexer;
import com.perl5.lang.perl.lexer.PerlLexer;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 28.05.2015.
 */
public class PerlNamesValidator implements NamesValidator
{
	@Override
	public boolean isKeyword(@NotNull String name, Project project)
	{
		// todo what is this for?
		return false;
	}

	@Override
	public boolean isIdentifier(@NotNull String name, Project project)
	{
		return PerlLexer.IDENTIFIER_PATTERN.matcher(name).matches() || PerlBaseLexer.AMBIGUOUS_PACKAGE_PATTERN.matcher(name).matches();
	}
}
