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

package com.perl5.lang.mason2.idea.configuration;

/**
 * Created by hurricup on 16.01.2016.
 */
public class VariableDescription
{
	public String variableName;
	public String variableType;

	public VariableDescription()
	{
	}

	public VariableDescription(String variableName, String variableType)
	{
		this.variableName = variableName;
		this.variableType = variableType;
	}

	@Override
	protected VariableDescription clone()
	{
		return new VariableDescription(variableName, variableType);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		VariableDescription that = (VariableDescription) o;

		if (variableName != null ? !variableName.equals(that.variableName) : that.variableName != null) return false;
		return variableType != null ? variableType.equals(that.variableType) : that.variableType == null;

	}

	@Override
	public int hashCode()
	{
		int result = variableName != null ? variableName.hashCode() : 0;
		result = 31 * result + (variableType != null ? variableType.hashCode() : 0);
		return result;
	}
}
