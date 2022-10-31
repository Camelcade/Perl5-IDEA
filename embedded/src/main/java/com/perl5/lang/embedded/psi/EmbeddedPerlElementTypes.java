/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.embedded.psi;

import com.intellij.psi.templateLanguages.TemplateDataElementType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.embedded.EmbeddedPerlLanguage;
import com.perl5.lang.pod.elementTypes.PodTemplatingElementType;


public final class EmbeddedPerlElementTypes {
  private EmbeddedPerlElementTypes() {
  }

  public static final IElementType EMBED_TEMPLATE_BLOCK_HTML = new EmbeddedPerlTemplateTokenType();
  public static final IElementType EMBED_OUTER_ELEMENT_TYPE = new EmbeddedPerlTokenType("EMBED_OUTER_ELEMENT_TYPE");
  public static final IElementType EMBED_HTML_TEMPLATE_DATA =
    new TemplateDataElementType("EMBED_HTML_TEMPLATE_DATA", EmbeddedPerlLanguage.INSTANCE, EMBED_TEMPLATE_BLOCK_HTML,
                                EMBED_OUTER_ELEMENT_TYPE);
  public static final IElementType EMBED_POD_TEMPLATE_DATA =
    new PodTemplatingElementType("EMBED_POD_TEMPLATE_DATA", EmbeddedPerlLanguage.INSTANCE);

  public static final IElementType EMBED_MARKER_OPEN = new EmbeddedPerlTokenType("EMBED_MARKER_OPEN");
  public static final IElementType EMBED_MARKER_CLOSE = new EmbeddedPerlTokenType("EMBED_MARKER_CLOSE");
}
