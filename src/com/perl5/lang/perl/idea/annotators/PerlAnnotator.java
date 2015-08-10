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

package com.perl5.lang.perl.idea.annotators;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.perl.lexer.PerlElementTypes;

/**
 * Created by hurricup on 10.08.2015.
 */
public abstract class PerlAnnotator implements Annotator, PerlElementTypes
{
	EditorColorsScheme currentScheme = EditorColorsManager.getInstance().getGlobalScheme();

	public void decorateElement(Annotation annotation, TextAttributesKey key, boolean builtin, boolean deprecated)
	{
		TextAttributes attrs = currentScheme.getAttributes(key);

		if (builtin)
			attrs = TextAttributes.merge(attrs, PerlSyntaxHighlighter.BOLD);
		if (deprecated)
			attrs = TextAttributes.merge(attrs, PerlSyntaxHighlighter.STROKE);

		annotation.setEnforcedTextAttributes(attrs);
	}

}
