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

package com.perl5.lang.perl.psi.mixins;

import com.intellij.codeInsight.controlflow.Instruction;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.ClearableLazyValue;
import com.perl5.lang.perl.idea.codeInsight.controlFlow.PerlControlFlowBuilder;
import com.perl5.lang.perl.psi.PerlSubExpr;
import com.perl5.lang.perl.psi.impl.PsiPerlExprImpl;
import org.jetbrains.annotations.NotNull;

public abstract class PerlSubExpression extends PsiPerlExprImpl implements PerlSubExpr {
  private final ClearableLazyValue<Instruction[]> myControlFlow = PerlControlFlowBuilder.createLazy(this);

  public PerlSubExpression(ASTNode node) {
    super(node);
  }

  @Override
  public @NotNull Instruction[] getControlFlow() {
    return myControlFlow.getValue();
  }

  @Override
  public void subtreeChanged() {
    myControlFlow.drop();
  }
}
