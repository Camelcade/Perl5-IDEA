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

package com.perl5.lang.ea;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.WhitespacesBinders;
import com.intellij.psi.tree.TokenSet;
import com.perl5.PerlBundle;
import com.perl5.lang.ea.psi.PerlExternalAnnotationsElementTypes;
import com.perl5.lang.perl.parser.PerlParserImpl;
import com.perl5.lang.perl.parser.PerlParserUtil;

/**
 * Created by hurricup on 06.08.2016.
 */
public class PerlExternalAnnotationsParser extends PerlParserImpl implements PerlExternalAnnotationsElementTypes
{
	private static final TokenSet PACKAGE_RECOVERY_SET = TokenSet.create(
			RESERVED_PACKAGE
	);

	private static final TokenSet DECLARATION_RECOVERY_SET = TokenSet.orSet(
			PACKAGE_RECOVERY_SET,
			TokenSet.create(
					RESERVED_SUB
			));

	@Override
	public boolean parseFileContents(PsiBuilder b, int l)
	{
		while (!b.eof())
		{
			if (!parsePseudoNamespace(b, l))
			{
				recoverPseudoNamespace(b, l);
			}
		}
		return true;
	}

	private boolean parsePseudoNamespace(PsiBuilder b, int l)
	{
		if (b.getTokenType() == RESERVED_PACKAGE)
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();

			if (PerlParserUtil.mergePackageName(b, l))
			{
				PerlParserUtil.parsePerlVersion(b, l);
				parseOptionalSemicolon(b, l);

				// this is a hack for grammar kit. Otherwise first getTokenType advances lexer and it's not possible to make it less greedy
				if (b.lookAhead(0) != RESERVED_PACKAGE)
				{
					PsiBuilder.Marker contentMarker = b.mark();

					while (!b.eof())
					{
						if (b.getTokenType() == RESERVED_PACKAGE)
						{
							break;
						}
						else if (!parsePseudoDeclaration(b, l))
						{
							recoverPseudoDeclaration(b, l);
						}
					}

					contentMarker.done(NAMESPACE_CONTENT);
					contentMarker.setCustomEdgeTokenBinders(WhitespacesBinders.GREEDY_LEFT_BINDER, WhitespacesBinders.DEFAULT_RIGHT_BINDER);
				}

				m.done(PSEUDO_NAMESPACE);
				return true;
			}
			else
			{
				m.error(PerlBundle.message("parser.package.name.expected"));
			}
		}
		return false;
	}

	private void parseOptionalSemicolon(PsiBuilder b, int l)
	{
		if (!PerlParserUtil.parseSemicolon(b, l))
		{
			b.mark().error(PerlBundle.message("parser.semicolon.expected"));
		}
	}

	private boolean parsePseudoDeclaration(PsiBuilder b, int l)
	{
		if (b.getTokenType() == RESERVED_SUB)
		{
			PsiBuilder.Marker marker = b.mark();
			b.advanceLexer();

			if (PerlParserUtil.parseSubDefinitionName(b, l))
			{
				parseOptionalSemicolon(b, l);
				marker.done(PSEUDO_DECLARATION);
				return true;
			}
			else
			{
				marker.error(PerlBundle.message("parser.identifier.expected"));
				return false;
			}
		}
		return false;
	}

	private void recoverPseudoDeclaration(PsiBuilder b, int l)
	{
		PsiBuilder.Marker mark = null;
		while (!b.eof() && !DECLARATION_RECOVERY_SET.contains(b.getTokenType()))
		{
			if (mark == null)
			{
				mark = b.mark();
			}
			b.advanceLexer();
		}
		if (mark != null)
		{
			mark.error(PerlBundle.message("parser.sub.or.package.expected"));
		}
	}

	private void recoverPseudoNamespace(PsiBuilder b, int l)
	{
		PsiBuilder.Marker mark = null;
		while (!b.eof() && !PACKAGE_RECOVERY_SET.contains(b.getTokenType()))
		{
			if (mark == null)
			{
				mark = b.mark();
			}
			b.advanceLexer();
		}
		if (mark != null)
		{
			mark.error(PerlBundle.message("parser.package.expected"));
		}
	}
}
