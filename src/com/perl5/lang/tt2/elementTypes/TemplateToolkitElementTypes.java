/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

package com.perl5.lang.tt2.elementTypes;

import com.intellij.psi.templateLanguages.TemplateDataElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.perl5.lang.tt2.TemplateToolkitLanguage;
import com.perl5.lang.tt2.lexer.TemplateToolkitElementTypesGenerated;

/**
 * Created by hurricup on 05.06.2016.
 */
public interface TemplateToolkitElementTypes extends TemplateToolkitElementTypesGenerated {
  IFileElementType TT2_FILE = new TemplateToolkitFileElementType();

  IElementType TT2_TEMPLATE_DATA = new TemplateDataElementType(
    "TT2_TEMPLATE_DATA",
    TemplateToolkitLanguage.INSTANCE,
    TT2_HTML,
    TT2_OUTER
  );

  IElementType TT2_PERL_CODE = new TemplateToolkitPerlCodeElementType("PERL_CODE");
  IElementType TT2_RAWPERL_CODE = new TemplateToolkitRawPerlCodeElementType("RAW_PERL_CODE");
}
