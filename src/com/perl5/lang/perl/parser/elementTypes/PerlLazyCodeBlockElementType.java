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

package com.perl5.lang.perl.parser.elementTypes;

import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.perl5.lang.perl.lexer.adapters.PerlSublexingLexerAdapter;
import com.perl5.lang.perl.parser.PerlLazyBlockParser;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 16.10.2016.
 */
public class PerlLazyCodeBlockElementType extends PerlLazyBlockElementType {
  public PerlLazyCodeBlockElementType(@NotNull @NonNls String debugName) {
    super(debugName);
  }

  @NotNull
  @Override
  protected Lexer getInnerLexer(@NotNull Project project) {
    return new PerlSublexingLexerAdapter(project, false, false);
  }

  @NotNull
  @Override
  protected PsiParser getParser() {
    return PerlLazyBlockParser.INSTANCE;
  }
}
