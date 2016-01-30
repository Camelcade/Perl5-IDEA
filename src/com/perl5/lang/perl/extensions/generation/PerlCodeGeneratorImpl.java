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

package com.perl5.lang.perl.extensions.generation;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.extensions.PerlCodeGenerator;
import com.perl5.lang.perl.psi.PerlMethodDefinition;
import com.perl5.lang.perl.psi.PerlSubBase;
import com.perl5.lang.perl.psi.PerlSubDefinitionBase;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by hurricup on 30.01.2016.
 */
public class PerlCodeGeneratorImpl implements PerlCodeGenerator
{
	public static final PerlCodeGenerator INSTANCE = new PerlCodeGeneratorImpl();

	@Nullable
	@Override
	public String getOverrideCodeText(PsiElement subBase)
	{
		if (subBase instanceof PerlSubBase)
		{
			PerlSubBase perlSubBase = (PerlSubBase) subBase;
			StringBuilder code = new StringBuilder();
			code.append("#@override\n");

			PerlSubAnnotations annotations = perlSubBase.getSubAnnotations();
			if (annotations.isDeprecated())
			{
				code.append("#@deprecated\n");
			}
			if (annotations.isAbstract())
			{
				code.append("#@abstract\n");
			}
			if (annotations.isMethod() || subBase instanceof PerlMethodDefinition)
			{
				code.append("#@method\n");
			}
			if (StringUtil.isNotEmpty(annotations.getReturns()))
			{
				code.append("#@returns ");
				code.append(annotations.getReturns());
				code.append("\n");
			}

			code.append("sub ");
			code.append(perlSubBase.getSubName());
			code.append("{\n");

			if (perlSubBase instanceof PerlSubDefinitionBase)
			{
				List<PerlSubArgument> arguments = ((PerlSubDefinitionBase) perlSubBase).getSubArgumentsList();

				if (arguments.size() > 0)
				{
					boolean useShift = false;

					for (PerlSubArgument argument : arguments)
					{
						if (StringUtil.isNotEmpty(argument.getVariableClass()))
						{
							useShift = true;
							break;
						}
					}

					if (useShift)
					{
						for (PerlSubArgument argument : arguments)
						{
							code.append("my ");
							code.append(argument.getVariableClass());
							code.append(" ");
							code.append(argument.getArgumentType().getSigil());
							code.append(argument.getArgumentName());
							code.append(" = shift;\n");
						}
					}
					else
					{
						code.append("my ");
						code.append('(');
						boolean insertComma = false;
						for (PerlSubArgument argument : arguments)
						{
							if (insertComma)
							{
								code.append(", ");
							}
							else
							{
								insertComma = true;
							}

							code.append(argument.getArgumentType().getSigil());
							code.append(argument.getArgumentName());
						}
						code.append(") = @_;\n");
					}
				}
				else
				{
					code.append("my ($self) = @_;\n");
				}
			}

			code.append("}");
			return code.toString();
		}
		return null;
	}

	@Nullable
	@Override
	public String getMethodModifierCodeText(PsiElement subBase, String modifierType)
	{
		return null;
	}
}
