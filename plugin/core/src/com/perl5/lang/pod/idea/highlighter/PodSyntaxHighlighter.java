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

package com.perl5.lang.pod.idea.highlighter;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.pod.lexer.PodLexerAdapter;
import com.perl5.lang.pod.lexer.PodTokenSets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;
import static com.perl5.lang.pod.lexer.PodElementTypesGenerated.*;


public class PodSyntaxHighlighter extends SyntaxHighlighterBase {
  public static final TextAttributesKey POD_TAG_KEY = createTextAttributesKey("POD_TAG", DefaultLanguageHighlighterColors.DOC_COMMENT_TAG);
  public static final TextAttributesKey POD_FORMATTER_TAG_KEY =
    createTextAttributesKey("POD_FORMATTER_TAG", DefaultLanguageHighlighterColors.DOC_COMMENT_TAG_VALUE);
  public static final TextAttributesKey
    POD_FORMATTER_MARKUP_KEY = createTextAttributesKey("POD_FORMATTER_ANGLE", DefaultLanguageHighlighterColors.DOC_COMMENT_TAG_VALUE);
  public static final TextAttributesKey POD_VERBATIM_KEY = createTextAttributesKey("POD_CODE", HighlighterColors.TEXT);
  public static final TextAttributesKey POD_TEXT_KEY =
    createTextAttributesKey("POD_TEXT_KEY", DefaultLanguageHighlighterColors.DOC_COMMENT);

  private static final Map<IElementType, TextAttributesKey> ATTRIBUTES_MAP = new HashMap<>();

  static {
    safeMap(ATTRIBUTES_MAP, POD_CODE, POD_VERBATIM_KEY);
    safeMap(ATTRIBUTES_MAP, PodTokenSets.POD_COMMANDS_TOKENSET, POD_TAG_KEY);
    safeMap(ATTRIBUTES_MAP, TokenSet.create(POD_ANGLE_LEFT, POD_ANGLE_RIGHT, POD_PIPE), POD_FORMATTER_MARKUP_KEY);
    safeMap(ATTRIBUTES_MAP, PodTokenSets.POD_FORMATTERS_TOKENSET, POD_FORMATTER_TAG_KEY);
  }

  @Nullable
  private final Project myProject;

  public PodSyntaxHighlighter(@Nullable Project project) {
    myProject = project;
  }

  @NotNull
  @Override
  public Lexer getHighlightingLexer() {
    return new PodLexerAdapter(myProject);
  }

  @NotNull
  @Override
  public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
    return getPodHighlights(tokenType);
  }

  @NotNull
  public static TextAttributesKey[] getPodHighlights(IElementType tokenType) {
    return pack(POD_TEXT_KEY, ATTRIBUTES_MAP.get(tokenType));
  }
}
