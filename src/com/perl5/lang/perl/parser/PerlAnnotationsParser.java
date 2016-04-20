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

package com.perl5.lang.perl.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LightPsiParser;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 20.04.2016.
 */
public class PerlAnnotationsParser implements PsiParser, LightPsiParser, PerlElementTypes
{

	private static boolean consumeIdentifierAsString(PsiBuilder b)
	{
		if (b.getTokenType() == IDENTIFIER)
		{
			PsiBuilder.Marker m = b.mark();
			b.advanceLexer();
			m.collapse(STRING_CONTENT);
			return true;
		}
		return false;
	}

	@NotNull
	public ASTNode parse(IElementType root_, PsiBuilder builder_)
	{
		parseLight(root_, builder_);
		return builder_.getTreeBuilt();
	}

	public void parseLight(IElementType root_, PsiBuilder b)
	{
		IElementType tokenType = b.getTokenType();
		PsiBuilder.Marker marker_ = b.mark();
		if (tokenType == ANNOTATION_PREFIX)
		{
			PsiBuilder.Marker annotationMarker = b.mark();
			b.advanceLexer();
			tokenType = b.getTokenType();
			if (tokenType == ANNOTATION_ABSTRACT_KEY)
			{
				b.advanceLexer();
				annotationMarker.done(ANNOTATION_ABSTRACT);
			}
			else if (tokenType == ANNOTATION_DEPRECATED_KEY)
			{
				b.advanceLexer();
				annotationMarker.done(ANNOTATION_DEPRECATED);

			}
			else if (tokenType == ANNOTATION_METHOD_KEY)
			{
				b.advanceLexer();
				annotationMarker.done(ANNOTATION_METHOD);

			}
			else if (tokenType == ANNOTATION_OVERRIDE_KEY)
			{
				b.advanceLexer();
				annotationMarker.done(ANNOTATION_OVERRIDE);

			}
			else if (tokenType == ANNOTATION_RETURNS_KEY)
			{
				b.advanceLexer();
				tokenType = b.getTokenType();

				if (tokenType == PACKAGE)
				{
					b.advanceLexer();
				}
				else if (tokenType == IDENTIFIER)
				{
					PsiBuilder.Marker packageMarker = b.mark();
					b.advanceLexer();
					packageMarker.collapse(PACKAGE);
				}
				else
				{
					b.mark().error("Package expected");
				}

				annotationMarker.done(ANNOTATION_RETURNS);

			}
			else if (tokenType == ANNOTATION_INJECT_KEY)
			{
				b.advanceLexer();

				if (!consumeIdentifierAsString(b))
				{
					b.mark().error("Language marker expected");
				}

				annotationMarker.done(ANNOTATION_INJECT);

			}
			else if (tokenType == ANNOTATION_NOINSPECTION_KEY)
			{
				b.advanceLexer();

				if (!consumeIdentifierAsString(b))
				{
					b.mark().error("Inspection name expected");
				}
				annotationMarker.done(ANNOTATION_NOINSPECTION);
			}
			else
			{
				annotationMarker.drop();
			}
		}

		while (!b.eof())
			b.advanceLexer();

		marker_.done(root_);
	}

}
