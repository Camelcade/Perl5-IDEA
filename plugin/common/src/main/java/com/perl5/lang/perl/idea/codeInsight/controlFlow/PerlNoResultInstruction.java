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

package com.perl5.lang.perl.idea.codeInsight.controlFlow;

import com.intellij.codeInsight.controlflow.ControlFlowBuilder;
import com.intellij.codeInsight.controlflow.impl.InstructionImpl;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents instructions, that may go out of scope without returning result:<ul>
 * <li>die & friends</li>
 * <li>exit</li>
 * <li>flow control instructions: goto, next, last, redo</li>
 * </ul>
 */
public class PerlNoResultInstruction extends InstructionImpl {
  public PerlNoResultInstruction(@NotNull ControlFlowBuilder builder,
                                 @Nullable PsiElement element) {
    super(builder, element);
  }
}

