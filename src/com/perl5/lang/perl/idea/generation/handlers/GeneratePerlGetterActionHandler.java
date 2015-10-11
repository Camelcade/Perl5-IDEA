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

import com.intellij.openapi.editor.Document;

/**
 * Created by hurricup on 11.10.2015.
 */
public class GeneratePerlGetterActionHandler extends GeneratePerlGetterSetterActionHandlerBase
{
	public static String getGetterCode(String name)
	{
		return "\n\n" +
				"sub get_" + name + "\n" +
				"{\n" +
				"	my $self = shift;\n" +
				"	return $$self{" + name + "};\n" +
				"}\n";
	}

	@Override
	public void doGenerate(Document document, String name, int offset)
	{
		document.insertString(offset, getGetterCode(name));
	}

	@Override
	protected String getPromtText()
	{
		return "Type comma-separated getters names:";
	}

	@Override
	protected String getPromtTitle()
	{
		return "Generating Getters";
	}
}
