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

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.*;

public class PerlQQStringElementType extends PerlStringElementType {
  public PerlQQStringElementType(@NotNull String debugName) {
    super(debugName);
  }

  @Override
  protected boolean isOperatorToken(@Nullable IElementType tokenType) {
    return tokenType == RESERVED_QQ;
  }

  @Override
  protected boolean isOperatorMandatory() {
    return false;
  }

  @Override
  protected boolean isOpenQuoteToken(@Nullable IElementType tokenType) {
    return tokenType == QUOTE_DOUBLE_OPEN;
  }

  @Override
  protected boolean isCloseQuoteToken(@Nullable IElementType tokenType) {
    return tokenType == QUOTE_DOUBLE_CLOSE;
  }
}
