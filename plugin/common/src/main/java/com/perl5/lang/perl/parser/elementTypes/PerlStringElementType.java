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

package com.perl5.lang.perl.parser.elementTypes;

import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiUtilCore;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.HEREDOC_OPENER;

public abstract class PerlStringElementType extends PerlTwoQuotesQuoteLikeElementType {
  public PerlStringElementType(@NotNull String debugName) {
    super(debugName);
  }

  @Override
  protected boolean isParentOk(@NotNull ASTNode parent) {
    return PsiUtilCore.getElementType(parent) != HEREDOC_OPENER && super.isParentOk(parent);
  }
}
