/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.embedded.lexer;

import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.lexer.adapters.PerlMergingLexerAdapter;
import com.perl5.lang.perl.lexer.adapters.PerlTemplatingMergingLexerAdapter;

import static com.perl5.lang.embedded.psi.EmbeddedPerlElementTypes.EMBED_TEMPLATE_BLOCK_HTML;


public class EmbeddedPerlLexerAdapter extends PerlTemplatingMergingLexerAdapter {
  private static final TokenSet TOKENS_TO_MERGE = TokenSet.orSet(
    PerlMergingLexerAdapter.TOKENS_TO_MERGE,
    TokenSet.create(EMBED_TEMPLATE_BLOCK_HTML)
  );

  public EmbeddedPerlLexerAdapter(Project project, boolean enforceSublexing) {
    super(project, new EmbeddedPerlLexer(null).withProject(project), TOKENS_TO_MERGE, enforceSublexing);
  }
}
