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

package com.perl5.lang.perl.idea.generation.handlers;

import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 11.10.2015.
 */
public class GeneratePerlSetterActionHandler extends GeneratePerlGetterSetterActionHandlerBase
{
	public static String getSetterCode(String name)
	{
		return "sub set_" + name + "\n" +
				"{\n" +
				"	my ($self, $new_value) = @_;\n" +
				"	$$self{" + name + "} = $new_value;\n" +
				"	return $self;\n" +
				"}\n";
	}


	@NotNull
	@Override
	protected String getCode(String name)
	{
		return getSetterCode(name);
	}

	@Override
	protected String getPromtText()
	{
		return "Type comma-separated setters names:";
	}

	@Override
	protected String getPromtTitle()
	{
		return "Generating Setters";
	}

}
