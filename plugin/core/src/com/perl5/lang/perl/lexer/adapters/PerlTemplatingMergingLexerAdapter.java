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

package com.perl5.lang.perl.lexer.adapters;

import com.intellij.lexer.MergingLexerAdapter;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.lexer.PerlLexingContext;
import com.perl5.lang.perl.lexer.PerlProtoLexer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PerlTemplatingMergingLexerAdapter extends MergingLexerAdapter {
  public PerlTemplatingMergingLexerAdapter(@Nullable Project project,
                                           @NotNull PerlProtoLexer flexLexer,
                                           TokenSet tokensToMerge,
                                           boolean enforceSublexing) {
    super(new PerlSublexingLexerAdapter(flexLexer, createLexingContext(project, enforceSublexing)), tokensToMerge);
  }

  private static @NotNull PerlLexingContext createLexingContext(@Nullable Project project, boolean enforceSublexing) {
    return PerlLexingContext.create(project).withEnforcedSublexing(enforceSublexing);
  }

  @Override
  public void advance() {
    ProgressManager.checkCanceled();
    super.advance();
  }
}
