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

package com.perl5.lang.perl.lexer;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.parser.elementTypes.PerlElementTypeEx;
import com.perl5.lang.perl.parser.elementTypes.PerlTokenType;
import com.perl5.lang.perl.psi.impl.*;
import com.perl5.lang.pod.elementTypes.PodTemplatingElementType;
import com.perl5.lang.pod.lexer.PodElementTypes;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 30.03.2016.
 */
public interface PerlElementTypes extends PerlElementTypesGenerated, PodElementTypes
{
	IElementType POD_BLOCK = new PodTemplatingElementType("POD_BLOCK", PerlLanguage.INSTANCE);

	IElementType ANNOTATION_PREFIX = new PerlTokenType("ANNOTATION_PREFIX");
	IElementType ANNOTATION_DEPRECATED_KEY = new PerlTokenType("ANNOTATION_DEPRECATED_KEY");
	IElementType ANNOTATION_RETURNS_KEY = new PerlTokenType("ANNOTATION_RETURNS_KEY");
	IElementType ANNOTATION_OVERRIDE_KEY = new PerlTokenType("ANNOTATION_OVERRIDE_KEY");
	IElementType ANNOTATION_METHOD_KEY = new PerlTokenType("ANNOTATION_METHOD_KEY");
	IElementType ANNOTATION_ABSTRACT_KEY = new PerlTokenType("ANNOTATION_ABSTRACT_KEY");
	IElementType ANNOTATION_INJECT_KEY = new PerlTokenType("ANNOTATION_INJECT_KEY");
	IElementType ANNOTATION_NOINSPECTION_KEY = new PerlTokenType("ANNOTATION_NOINSPECTION_KEY");
	IElementType ANNOTATION_UNKNOWN_KEY = new PerlTokenType("ANNOTATION_UKNOWN_KEY");

	IElementType ANNOTATION_ABSTRACT = new PerlElementTypeEx("ANNOTATION_ABSTRACT")
	{
		@NotNull
		@Override
		public PsiElement getPsiElement(@NotNull ASTNode node)
		{
			return new PerlAnnotationAbstractImpl(node);
		}
	};

	IElementType ANNOTATION_DEPRECATED = new

			PerlElementTypeEx("ANNOTATION_DEPRECATED")
			{
				@NotNull
				@Override
				public PsiElement getPsiElement(@NotNull ASTNode node)
				{
					return new PerlAnnotationDeprecatedImpl(node);
				}
			};


	IElementType ANNOTATION_METHOD = new

			PerlElementTypeEx("ANNOTATION_METHOD")
			{
				@NotNull
				@Override
				public PsiElement getPsiElement(@NotNull ASTNode node)
				{
					return new PerlAnnotationMethodImpl(node);
				}
			};

	IElementType ANNOTATION_OVERRIDE = new

			PerlElementTypeEx("ANNOTATION_OVERRIDE")
			{
				@NotNull
				@Override
				public PsiElement getPsiElement(@NotNull ASTNode node)
				{
					return new PerlAnnotationOverrideImpl(node);
				}
			};

	IElementType ANNOTATION_RETURNS = new

			PerlElementTypeEx("ANNOTATION_RETURNS")
			{
				@NotNull
				@Override
				public PsiElement getPsiElement(@NotNull ASTNode node)
				{
					return new PerlAnnotationReturnsImpl(node);
				}
			};

	IElementType ANNOTATION_INJECT = new

			PerlElementTypeEx("ANNOTATION_INJECT")
			{
				@NotNull
				@Override
				public PsiElement getPsiElement(@NotNull ASTNode node)
				{
					return new PerlAnnotationInjectImpl(node);
				}
			};
	IElementType ANNOTATION_NOINSPECTION = new

			PerlElementTypeEx("ANNOTATION_NOINSPECTION")
			{
				@NotNull
				@Override
				public PsiElement getPsiElement(@NotNull ASTNode node)
				{
					return new PerlAnnotationNoInspectionImpl(node);
				}
			};

}
