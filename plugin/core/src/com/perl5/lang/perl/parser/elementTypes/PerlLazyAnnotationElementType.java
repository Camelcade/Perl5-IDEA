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

package com.perl5.lang.perl.parser.elementTypes;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.perl5.lang.perl.lexer.PerlLexer;
import com.perl5.lang.perl.lexer.PerlLexingContext;
import com.perl5.lang.perl.psi.impl.PerlAnnotationContainerImpl;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;


public class PerlLazyAnnotationElementType extends PerlLazyBlockElementType {
  public PerlLazyAnnotationElementType(@NotNull @NonNls String debugName) {
    super(debugName, PerlAnnotationContainerImpl.class);
  }

  @Override
  protected @NotNull PerlLexingContext getLexingContext(@NotNull Project project, @NotNull ASTNode chameleon) {
    return super.getLexingContext(project, chameleon).withEnforcedInitialState(PerlLexer.ANNOTATION);
  }
}
