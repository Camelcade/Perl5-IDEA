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

package com.perl5.lang.perl.lexer;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.HEREDOC_END;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.HEREDOC_END_INDENTABLE;


public final class PerlHeredocQueueElement {
  private final IElementType myTargetElement;
  private final CharSequence myMarker;
  private final boolean myIsIndentable;

  public PerlHeredocQueueElement(@NotNull IElementType targetElement, @NotNull CharSequence marker, boolean isIndentable) {
    myTargetElement = targetElement;
    myMarker = marker;
    myIsIndentable = isIndentable;
  }

  public @NotNull IElementType getTargetElement() {
    return myTargetElement;
  }

  public @NotNull CharSequence getMarker() {
    return myMarker;
  }

  public boolean isIndentable() {
    return myIsIndentable;
  }

  public @NotNull IElementType getTerminatorElementType() {
    return myIsIndentable ? HEREDOC_END_INDENTABLE: HEREDOC_END;
  }
}
