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

package com.perl5.lang.mojolicious;

import com.intellij.psi.templateLanguages.TemplateDataElementType;
import com.intellij.psi.tree.IElementType;

/**
 * Created by hurricup on 22.12.2015.
 */
public interface MojoliciousPerlElementTypes
{
	IElementType TEMPLATE_BLOCK_HTML = new MojoliciousPerlTokenType("TEMPLATE_BLOCK_HTML");
	IElementType EMBED_MARKER_OPEN = new MojoliciousPerlTokenType("EMBED_MARKER_OPEN");
	IElementType EMBED_MARKER_CLOSE = new MojoliciousPerlTokenType("EMBED_MARKER_CLOSE");
	IElementType EMBED_MARKER = new MojoliciousPerlTokenType("EMBED_MARKER_KEY");
	IElementType EMBED_MARKER_SEMICOLON = new MojoliciousPerlTokenType("EMBED_MARKER_SEMICOLON");

	IElementType OUTER_ELEMENT_TYPE = new MojoliciousPerlElementType("OUTER_ELEMENT_TYPE");
	IElementType HTML_TEMPLATE_DATA = new TemplateDataElementType("HTML_TEMPLATE_DATA", MojoliciousPerlLanguage.INSTANCE, TEMPLATE_BLOCK_HTML, OUTER_ELEMENT_TYPE);

}
