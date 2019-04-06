/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.codeInsight.typeInferrence.value;

import com.intellij.psi.stubs.StubOutputStream;
import com.perl5.lang.perl.lexer.PerlBaseLexer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class PerlValueUndef extends PerlValue {
  public static final PerlValueUndef UNDEF_VALUE = new PerlValueUndef();

  private PerlValueUndef() {
  }

  @Override
  protected void serializeData(@NotNull StubOutputStream dataStream) throws IOException {
  }

  @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
  @Override
  public boolean equals(Object o) {
    return o == UNDEF_VALUE;
  }

  @Override
  protected int computeHashCode() {
    return getClass().hashCode();
  }

  @Override
  protected int getSerializationId() {
    return PerlValuesManager.UNDEF_ID;
  }

  @NotNull
  @Override
  PerlValueUndef createBlessedCopy(@NotNull PerlValue bless) {
    return this;
  }

  @NotNull
  @Override
  protected String getPresentableValueText() {
    return PerlBaseLexer.STRING_UNDEF;
  }

  @Override
  public String toString() {
    return "UNDEF_VALUE";
  }
}
