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

package com.perl5.lang.perl.psi.utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.perl5.lang.perl.idea.configuration.settings.Perl5Settings;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by hurricup on 01.06.2015.
 */
public class PerlSubArgument
{

	PerlVariableType argumentType;
	String argumentName;
	String variableClass;
	Boolean isOptional;

	public PerlSubArgument(PerlVariableType variableType, String variableName, String variableClass, boolean isOptional)
	{
		this.argumentType = variableType;
		this.argumentName = variableName;
		this.isOptional = isOptional;
		this.variableClass = variableClass;
	}

	public static PerlSubArgument deserialize(@NotNull StubInputStream dataStream) throws IOException
	{
		PerlVariableType argumentType = PerlVariableType.valueOf(dataStream.readName().toString());
		String argumentName = dataStream.readName().toString();
		String variableClass = dataStream.readName().toString();
		boolean isOptional = dataStream.readBoolean();
		return new PerlSubArgument(argumentType, argumentName, variableClass, isOptional);
	}

	public static PerlSubArgument getEmptyArgument()
	{
		return new PerlSubArgument(
				PerlVariableType.SCALAR,
				"",
				"",
				false
		);
	}

	public PerlVariableType getArgumentType()
	{
		return argumentType;
	}

	public String getArgumentName()
	{
		return argumentName;
	}

	public Boolean isOptional()
	{
		return isOptional;
	}

	public void setOptional(Boolean optional)
	{
		isOptional = optional;
	}

	public String getVariableClass()
	{
		return variableClass;
	}

	public String toStringShort()
	{
		return StringUtil.isNotEmpty(argumentName) ? argumentType.getSigil() + argumentName : "undef";
	}

	public void serialize(@NotNull StubOutputStream dataStream) throws IOException
	{
		dataStream.writeName(argumentType.toString());
		dataStream.writeName(argumentName);
		dataStream.writeName(variableClass);
		dataStream.writeBoolean(isOptional);
	}

	public boolean isSelf(Project project)
	{
		return getArgumentType() == PerlVariableType.SCALAR && Perl5Settings.getInstance(project).isSelfName(getArgumentName());
	}

	public boolean isEmpty()
	{
		return StringUtil.isEmpty(argumentName);
	}
}
