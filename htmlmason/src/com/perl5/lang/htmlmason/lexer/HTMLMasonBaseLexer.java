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

package com.perl5.lang.htmlmason.lexer;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonCustomTag;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonCustomTagRole;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonSettings;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.PerlTemplatingLexer;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static com.perl5.lang.htmlmason.lexer.HTMLMasonLexer.*;


public abstract class HTMLMasonBaseLexer extends PerlTemplatingLexer implements HTMLMasonElementTypes, PerlElementTypes {
  private final CommentEndCalculator COMMENT_END_CALCULATOR = commentText ->
  {
    int realLexicalState = getRealLexicalState();
    if (realLexicalState == PERL_EXPR || realLexicalState == PERL_EXPR_FILTER) {
      return StringUtil.indexOf(commentText, KEYWORD_BLOCK_CLOSER);
    }
    return -1;
  };
  @Nullable
  protected Map<String, HTMLMasonCustomTag> myCustomTagsMap;

  @Nullable
  @Override
  protected CommentEndCalculator getCommentEndCalculator() {
    return COMMENT_END_CALCULATOR;
  }

  public HTMLMasonBaseLexer withProject(@Nullable Project project) {
    myCustomTagsMap = project == null ? null : HTMLMasonSettings.getInstance(project).getCustomTagsMap();
    myPerlLexer.withProject(project);
    return this;
  }

  protected IElementType processCloseTagFallback() {
    yybegin(NON_CLEAR_LINE);
    return HTML_MASON_TEMPLATE_BLOCK_HTML;
  }

  protected IElementType processMethodCloseTag() {
    yybegin(AFTER_PERL_BLOCK);
    setPerlToInitial();
    return HTML_MASON_METHOD_CLOSER;
  }

  protected IElementType processDefCloseTag() {
    yybegin(AFTER_PERL_BLOCK);
    setPerlToInitial();
    return HTML_MASON_DEF_CLOSER;
  }

  protected IElementType processCustomCloseTag() {
    CharSequence tokenText = yytext();
    CharSequence tokenKey = tokenText.subSequence(0, tokenText.length() - 1);
    assert myCustomTagsMap != null;
    HTMLMasonCustomTag customTag = myCustomTagsMap.get(tokenKey.toString());
    if (customTag != null) {
      HTMLMasonCustomTagRole tagRole = customTag.getRole();
      if (tagRole == HTMLMasonCustomTagRole.METHOD) {
        return processMethodCloseTag();
      }
      else if (tagRole == HTMLMasonCustomTagRole.DEF) {
        return processDefCloseTag();
      }
    }
    return processCloseTagFallback();
  }

  protected IElementType processArgsOpenTag(int state) {
    yybegin(state);
    startPerlExpression();
    return HTML_MASON_ARGS_OPENER;
  }

  protected IElementType processPerlOpenTag(int state) {
    yybegin(state);
    return HTML_MASON_PERL_OPENER;
  }

  protected IElementType processArgsCloser() {
    endPerlExpression();
    yybegin(AFTER_PERL_BLOCK);
    return HTML_MASON_ARGS_CLOSER;
  }

  protected IElementType processCustomArgsCloser() {
    CharSequence tokenText = yytext();
    CharSequence tokenKey = tokenText.subSequence(3, tokenText.length() - 1);
    assert myCustomTagsMap != null;
    HTMLMasonCustomTag customTag = myCustomTagsMap.get(tokenKey.toString());
    if (customTag != null && customTag.getRole() == HTMLMasonCustomTagRole.ARGS) {
      return processArgsCloser();
    }
    return delegateLexing();
  }

  protected IElementType processPerlCloser() {
    yybegin(AFTER_PERL_BLOCK);
    return HTML_MASON_PERL_CLOSER;
  }

  protected IElementType processCustomPerlCloser() {
    CharSequence tokenText = yytext();
    CharSequence tokenKey = tokenText.subSequence(3, tokenText.length() - 1);
    assert myCustomTagsMap != null;
    HTMLMasonCustomTag customTag = myCustomTagsMap.get(tokenKey.toString());
    if (customTag != null && customTag.getRole() == HTMLMasonCustomTagRole.PERL) {
      return processPerlCloser();
    }
    return delegateLexing();
  }

  protected IElementType processMethodOpenTag() {
    yybegin(PARAMETRIZED_OPENER);
    return HTML_MASON_METHOD_OPENER;
  }

  protected IElementType processDefOpenTag() {
    yybegin(PARAMETRIZED_OPENER);
    return HTML_MASON_DEF_OPENER;
  }

  protected IElementType processCustomSimpleOpenTag() {
    CharSequence tokenText = yytext();
    CharSequence tokenKey = tokenText.subSequence(0, tokenText.length() - 1);
    assert myCustomTagsMap != null;
    HTMLMasonCustomTag customTag = myCustomTagsMap.get(tokenKey.toString());
    if (customTag != null) {
      HTMLMasonCustomTagRole tagRole = customTag.getRole();
      if (tagRole == HTMLMasonCustomTagRole.ARGS) {
        return processArgsOpenTag(ARGS_WITH_CUSTOM_CLOSER);
      }
      else if (tagRole == HTMLMasonCustomTagRole.PERL) {
        return processPerlOpenTag(PERL_WITH_CUSTOM_CLOSER);
      }
    }
    return processOpenTagFallback();
  }

  protected IElementType processCustomComplexOpenTag() {
    assert myCustomTagsMap != null;
    HTMLMasonCustomTag customTag = myCustomTagsMap.get(yytext().toString());
    if (customTag != null) {
      HTMLMasonCustomTagRole tagRole = customTag.getRole();
      if (tagRole == HTMLMasonCustomTagRole.METHOD) {
        return processMethodOpenTag();
      }
      else if (tagRole == HTMLMasonCustomTagRole.DEF) {
        return processDefOpenTag();
      }
    }

    return processOpenTagFallback();
  }

  protected IElementType processOpenTagFallback() {
    pushback();
    startPerlExpression();
    yybegin(PERL_EXPR);
    return HTML_MASON_BLOCK_OPENER;
  }
}
