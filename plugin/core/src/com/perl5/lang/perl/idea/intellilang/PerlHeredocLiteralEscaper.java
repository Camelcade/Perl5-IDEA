/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.intellilang;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.LiteralTextEscaper;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import org.jetbrains.annotations.NotNull;


public class PerlHeredocLiteralEscaper extends LiteralTextEscaper<PerlHeredocElementImpl> {
  public PerlHeredocLiteralEscaper(PerlHeredocElementImpl host) {
    super(host);
  }

  @Override
  public boolean isOneLine() {
    return false;
  }

  @Override
  public boolean decode(@NotNull TextRange rangeInsideHost, @NotNull StringBuilder outChars) {
    outChars.append(rangeInsideHost.subSequence(myHost.getNode().getChars()));
    return true;
  }

  @Override
  public int getOffsetInHost(int offsetInDecoded, @NotNull final TextRange rangeInsideHost) {
    return offsetInDecoded + rangeInsideHost.getStartOffset();
  }

  @NotNull
  @Override
  public TextRange getRelevantTextRange() {
    return ElementManipulators.getManipulator(myHost).getRangeInElement(myHost);
  }
}
