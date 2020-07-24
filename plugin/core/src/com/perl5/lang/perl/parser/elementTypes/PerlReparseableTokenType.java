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
import com.intellij.psi.tree.IReparseableLeafElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PerlReparseableTokenType extends PerlTokenTypeEx implements IReparseableLeafElementType<ASTNode> {
  public PerlReparseableTokenType(@NotNull String debugName,
                                  Class<? extends ASTNode> clazz) {
    super(debugName, clazz);
  }

  /**
   * @return true iff {@code newText} may replace the {@code leaf} text without breaking things
   */
  protected abstract boolean isReparseable(@NotNull ASTNode leaf, @NotNull CharSequence newText);

  @Override
  public final @Nullable ASTNode reparseLeaf(@NotNull ASTNode leaf, @NotNull CharSequence newText) {
    return isReparseable(leaf, newText) ? createLeafNode(newText) : null;
  }
}
