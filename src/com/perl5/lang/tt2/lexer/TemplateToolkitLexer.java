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

package com.perl5.lang.tt2.lexer;

import com.intellij.openapi.project.Project;
import com.perl5.lang.perl.lexer.PerlLexerWithCustomStates;

import java.io.Reader;

/**
 * Created by hurricup on 05.06.2016.
 */
public class TemplateToolkitLexer extends TemplateToolkitLexerGenerated implements PerlLexerWithCustomStates
{
	private final Project myProject;
	private int customState = 0;

	public TemplateToolkitLexer(Project project)
	{
		super((Reader) null);
		myProject = project;
	}

	@Override
	protected int getPreparsedLexicalState()
	{
		return LEX_PREPARSED_ITEMS;
	}

	public Project getProject()
	{
		return myProject;
	}

	public int getCustomState()
	{
		return customState;
	}

	public void setCustomState(int newState)
	{
		customState = newState;
	}

	@Override
	public int getInitialCustomState()
	{
		return LEX_HTML;
	}
}
