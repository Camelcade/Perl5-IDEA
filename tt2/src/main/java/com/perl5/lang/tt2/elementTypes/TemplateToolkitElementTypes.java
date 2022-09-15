/*
 * Copyright 2015-2019 Alexandr Evstigneev
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface TemplateToolkitElementTypes extends TemplateToolkitElementTypesGenerated {
  IFileElementType TT2_FILE = new TemplateToolkitFileElementType();

  IElementType TT2_TEMPLATE_DATA = new TemplateDataElementType(
    "TT2_TEMPLATE_DATA",
    TemplateToolkitLanguage.INSTANCE,
    TT2_HTML,
    TT2_OUTER
  ) {
    @Override
    protected boolean isInsertionToken(@Nullable IElementType tokenType, @NotNull CharSequence tokenSequence) {
      return tokenType == TT2_OPEN_TAG || tokenType == TT2_OUTLINE_TAG;
    }
  };

  IElementType TT2_PERL_CODE = new TemplateToolkitPerlCodeElementType("PERL_CODE");
  IElementType TT2_RAWPERL_CODE = new TemplateToolkitRawPerlCodeElementType("RAW_PERL_CODE");

  // types from fake rules
  IElementType ANON_BLOCK = TemplateToolkitElementTypeFactory.getElementType("ANON_BLOCK");
  IElementType BLOCK_COMMENT = TemplateToolkitElementTypeFactory.getElementType("BLOCK_COMMENT");
  IElementType CASE_BLOCK = TemplateToolkitElementTypeFactory.getElementType("CASE_BLOCK");
  IElementType CATCH_BRANCH = TemplateToolkitElementTypeFactory.getElementType("CATCH_BRANCH");
  IElementType ELSE_BRANCH = TemplateToolkitElementTypeFactory.getElementType("ELSE_BRANCH");
  IElementType ELSIF_BRANCH = TemplateToolkitElementTypeFactory.getElementType("ELSIF_BRANCH");

  IElementType FILTER_BLOCK = TemplateToolkitElementTypeFactory.getElementType("FILTER_BLOCK");
  IElementType FINAL_BRANCH = TemplateToolkitElementTypeFactory.getElementType("FINAL_BRANCH");
  IElementType FOREACH_BLOCK = TemplateToolkitElementTypeFactory.getElementType("FOREACH_BLOCK");
  IElementType IF_BLOCK = TemplateToolkitElementTypeFactory.getElementType("IF_BLOCK");
  IElementType IF_BRANCH = TemplateToolkitElementTypeFactory.getElementType("IF_BRANCH");
  IElementType NAMED_BLOCK = TemplateToolkitElementTypeFactory.getElementType("NAMED_BLOCK");
  IElementType PERL_BLOCK = TemplateToolkitElementTypeFactory.getElementType("PERL_BLOCK");
  IElementType RAWPERL_BLOCK = TemplateToolkitElementTypeFactory.getElementType("RAWPERL_BLOCK");
  IElementType SWITCH_BLOCK = TemplateToolkitElementTypeFactory.getElementType("SWITCH_BLOCK");
  IElementType TRY_BRANCH = TemplateToolkitElementTypeFactory.getElementType("TRY_BRANCH");
  IElementType TRY_CATCH_BLOCK = TemplateToolkitElementTypeFactory.getElementType("TRY_CATCH_BLOCK");
  IElementType UNLESS_BLOCK = TemplateToolkitElementTypeFactory.getElementType("UNLESS_BLOCK");
  IElementType UNLESS_BRANCH = TemplateToolkitElementTypeFactory.getElementType("UNLESS_BRANCH");
  IElementType WHILE_BLOCK = TemplateToolkitElementTypeFactory.getElementType("WHILE_BLOCK");
  IElementType WRAPPER_BLOCK = TemplateToolkitElementTypeFactory.getElementType("WRAPPER_BLOCK");
}
