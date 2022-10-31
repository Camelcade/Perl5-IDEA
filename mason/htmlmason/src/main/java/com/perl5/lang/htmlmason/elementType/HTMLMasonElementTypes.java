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

package com.perl5.lang.htmlmason.elementType;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.templateLanguages.TemplateDataElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.perl5.lang.htmlmason.HTMLMasonLanguage;
import com.perl5.lang.htmlmason.parser.psi.impl.*;
import com.perl5.lang.perl.psi.stubs.PerlFileElementType;
import com.perl5.lang.pod.elementTypes.PodTemplatingElementType;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.htmlmason.HTMLMasonSyntaxElements.*;

public final class HTMLMasonElementTypes {
  private HTMLMasonElementTypes() {
  }

  public static final IElementType HTML_MASON_TEMPLATE_BLOCK_HTML = new HTMLMasonTemplateTokenType();
  public static final IElementType HTML_MASON_OUTER_ELEMENT_TYPE = new HTMLMasonTokenType("HTML_MASON_OUTER_ELEMENT_TYPE");
  public static final IElementType HTML_MASON_HTML_TEMPLATE_DATA = new TemplateDataElementType(
    "HTML_MASON_HTML_TEMPLATE_DATA",
    HTMLMasonLanguage.INSTANCE,
    HTML_MASON_TEMPLATE_BLOCK_HTML,
    HTML_MASON_OUTER_ELEMENT_TYPE
  );
  public static final IElementType HTML_MASON_POD_TEMPLATE_DATA =
    new PodTemplatingElementType("HTML_MASON_POD_TEMPLATE_DATA", HTMLMasonLanguage.INSTANCE);

  public static final IElementType HTML_MASON_BLOCK_OPENER = new HTMLMasonTokenType(KEYWORD_BLOCK_OPENER);
  public static final IElementType HTML_MASON_BLOCK_CLOSER = new HTMLMasonTokenType(KEYWORD_BLOCK_CLOSER);

  public static final IElementType HTML_MASON_LINE_OPENER = new HTMLMasonTokenType("%");
  public static final IElementType HTML_MASON_EXPR_FILTER_PIPE = new HTMLMasonTokenType("|");

  public static final IElementType HTML_MASON_TAG_CLOSER = new HTMLMasonTokenType(KEYWORD_TAG_CLOSER);

  public static final IElementType HTML_MASON_ESCAPER_NAME = new HTMLMasonTokenType("HTML_MASON_ESCAPER");
  public static final IElementType HTML_MASON_DEFAULT_ESCAPER_NAME = new HTMLMasonTokenType("HTML_MASON_ESCAPER");

  public static final IElementType HTML_MASON_CALL_OPENER = new HTMLMasonTokenType(KEYWORD_CALL_OPENER);
  public static final IElementType HTML_MASON_CALL_FILTERING_OPENER = new HTMLMasonTokenType(KEYWORD_CALL_OPENER_FILTER);
  public static final IElementType HTML_MASON_CALL_CLOSER = new HTMLMasonTokenType(KEYWORD_CALL_CLOSER);
  public static final IElementType HTML_MASON_CALL_CLOSER_UNMATCHED = new HTMLMasonTokenType(KEYWORD_CALL_CLOSER);
  public static final IElementType HTML_MASON_CALL_CLOSE_TAG_START = new HTMLMasonTokenType(KEYWORD_CALL_CLOSE_TAG_START);
  public static final IElementType HTML_MASON_CALL_CLOSE_TAG =
    new HTMLMasonElementType(KEYWORD_CALL_CLOSE_TAG_START + HTML_MASON_TAG_CLOSER);

  public static final IElementType HTML_MASON_METHOD_OPENER = new HTMLMasonTokenType(KEYWORD_METHOD_OPENER);
  public static final IElementType HTML_MASON_METHOD_CLOSER = new HTMLMasonTokenType(KEYWORD_METHOD_CLOSER);

  public static final IElementType HTML_MASON_DEF_OPENER = new HTMLMasonTokenType(KEYWORD_DEF_OPENER);
  public static final IElementType HTML_MASON_DEF_CLOSER = new HTMLMasonTokenType(KEYWORD_DEF_CLOSER);

  public static final IElementType HTML_MASON_DOC_OPENER = new HTMLMasonTokenType(KEYWORD_DOC_OPENER);
  public static final IElementType HTML_MASON_DOC_CLOSER = new HTMLMasonTokenType(KEYWORD_DOC_CLOSER);

  public static final IElementType HTML_MASON_FLAGS_OPENER = new HTMLMasonTokenType(KEYWORD_FLAGS_OPENER);
  public static final IElementType HTML_MASON_FLAGS_CLOSER = new HTMLMasonTokenType(KEYWORD_FLAGS_CLOSER);

  public static final IElementType HTML_MASON_ATTR_OPENER = new HTMLMasonTokenType(KEYWORD_ATTR_OPENER);
  public static final IElementType HTML_MASON_ATTR_CLOSER = new HTMLMasonTokenType(KEYWORD_ATTR_CLOSER);

  public static final IElementType HTML_MASON_ARGS_OPENER = new HTMLMasonTokenType(KEYWORD_ARGS_OPENER);
  public static final IElementType HTML_MASON_ARGS_CLOSER = new HTMLMasonTokenType(KEYWORD_ARGS_CLOSER);

  public static final IElementType HTML_MASON_INIT_OPENER = new HTMLMasonTokenType(KEYWORD_INIT_OPENER);
  public static final IElementType HTML_MASON_INIT_CLOSER = new HTMLMasonTokenType(KEYWORD_INIT_CLOSER);

  public static final IElementType HTML_MASON_ONCE_OPENER = new HTMLMasonTokenType(KEYWORD_ONCE_OPENER);
  public static final IElementType HTML_MASON_ONCE_CLOSER = new HTMLMasonTokenType(KEYWORD_ONCE_CLOSER);

  public static final IElementType HTML_MASON_SHARED_OPENER = new HTMLMasonTokenType(KEYWORD_SHARED_OPENER);
  public static final IElementType HTML_MASON_SHARED_CLOSER = new HTMLMasonTokenType(KEYWORD_SHARED_CLOSER);

  public static final IElementType HTML_MASON_CLEANUP_OPENER = new HTMLMasonTokenType(KEYWORD_CLEANUP_OPENER);
  public static final IElementType HTML_MASON_CLEANUP_CLOSER = new HTMLMasonTokenType(KEYWORD_CLEANUP_CLOSER);

  public static final IElementType HTML_MASON_PERL_OPENER = new HTMLMasonTokenType(KEYWORD_PERL_OPENER);
  public static final IElementType HTML_MASON_PERL_CLOSER = new HTMLMasonTokenType(KEYWORD_PERL_CLOSER);

  public static final IElementType HTML_MASON_TEXT_OPENER = new HTMLMasonTokenType(KEYWORD_TEXT_OPENER);
  public static final IElementType HTML_MASON_TEXT_CLOSER = new HTMLMasonTokenType(KEYWORD_TEXT_CLOSER);

  public static final IElementType HTML_MASON_FILTER_OPENER = new HTMLMasonTokenType(KEYWORD_FILTER_OPENER);
  public static final IElementType HTML_MASON_FILTER_CLOSER = new HTMLMasonTokenType(KEYWORD_FILTER_CLOSER);

  public static final IElementType HTML_MASON_CALL_STATEMENT = new HTMLMasonElementType("HTML_MASON_CALL_STATEMENT");
  public static final IElementType HTML_MASON_TEXT_BLOCK = new HTMLMasonElementType("HTML_MASON_TEXT_BLOCK");
  public static final IElementType HTML_MASON_ATTR_BLOCK = new HTMLMasonElementType("HTML_MASON_ATTR_BLOCK");
  public static final IElementType HTML_MASON_HARD_NEWLINE = new HTMLMasonElementType("HTML_MASON_HARD_NEWLINE");

  public static final HTMLMasonMethodElementType HTML_MASON_METHOD_DEFINITION =
    new HTMLMasonMethodElementType("HTML_MASON_METHOD_DEFINITION");
  public static final HTMLMasonSubcomponentElementType HTML_MASON_SUBCOMPONENT_DEFINITION =
    new HTMLMasonSubcomponentElementType("HTML_MASON_DEF_DEFINITION");

  public static final HTMLMasonFlagsStatementElementType HTML_MASON_FLAGS_STATEMENT =
    new HTMLMasonFlagsStatementElementType("HTML_MASON_FLAGS_STATEMENT");

  public static final HTMLMasonArgsBlockElementType HTML_MASON_ARGS_BLOCK = new HTMLMasonArgsBlockElementType("HTML_MASON_ARGS_BLOCK");

  public static final IElementType HTML_MASON_ONCE_BLOCK = new HTMLMasonElementType("HTML_MASON_ONCE_BLOCK") {
    @Override
    public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
      return new HTMLMasonOnceBlockImpl(node);
    }
  };
  public static final IElementType HTML_MASON_INIT_BLOCK = new HTMLMasonElementType("HTML_MASON_INIT_BLOCK") {
    @Override
    public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
      return new HTMLMasonInitBlockImpl(node);
    }
  };
  public static final IElementType HTML_MASON_CLEANUP_BLOCK = new HTMLMasonElementType("HTML_MASON_CLEANUP_BLOCK") {
    @Override
    public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
      return new HTMLMasonCleanupBlockImpl(node);
    }
  };
  public static final IElementType HTML_MASON_SHARED_BLOCK = new HTMLMasonElementType("HTML_MASON_SHARED_BLOCK") {
    @Override
    public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
      return new HTMLMasonSharedBlockImpl(node);
    }
  };
  public static final IElementType HTML_MASON_FILTERED_BLOCK = new HTMLMasonElementType("HTML_MASON_FILTERED_BLOCK") {
    @Override
    public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
      return new HTMLMasonCompositeElementImpl(node);
    }
  };

  public static final IElementType HTML_MASON_FILTER_BLOCK = new HTMLMasonElementType("HTML_MASON_FILTER_BLOCK") {
    @Override
    public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
      return new HTMLMasonFilterBlockImpl(node);
    }
  };

  public static final IElementType HTML_MASON_BLOCK = new HTMLMasonElementType("HTML_MASON_BLOCK") {
    @Override
    public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
      return new HTMLMasonBlockImpl(node);
    }
  };
  public static final IFileElementType FILE = new PerlFileElementType("HTML::Mason component", HTMLMasonLanguage.INSTANCE);
}
