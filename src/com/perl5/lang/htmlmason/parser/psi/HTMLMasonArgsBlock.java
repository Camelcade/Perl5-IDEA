/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.htmlmason.parser.psi;

import com.intellij.psi.StubBasedPsiElement;
import com.perl5.lang.htmlmason.parser.stubs.HTMLMasonArgsBlockStub;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by hurricup on 08.03.2016.
 */
public interface HTMLMasonArgsBlock extends HTMLMasonCompositeElement, StubBasedPsiElement<HTMLMasonArgsBlockStub>, PerlElementTypes {
  /**
   * Returns list of accepted arguments
   *
   * @return list of accepted arguments
   */
  @NotNull
  List<PerlSubArgument> getArgumentsList();
}
