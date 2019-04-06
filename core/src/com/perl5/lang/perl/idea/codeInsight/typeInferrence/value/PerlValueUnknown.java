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
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class PerlValueUnknown extends PerlValue {
  public static final PerlValueUnknown UNKNOWN_VALUE = new PerlValueUnknown();

  private PerlValueUnknown() {
  }

  @Override
  protected void serializeData(@NotNull StubOutputStream dataStream) throws IOException {
  }

  @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
  @Override
  public boolean equals(Object o) {
    return o == UNKNOWN_VALUE;
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

  @Override
  protected int getSerializationId() {
    return PerlValuesManager.UNKNOWN_ID;
  }

  @NotNull
  @Override
  PerlValueUnknown createBlessedCopy(@NotNull PerlValue bless) {
    return this;
  }

  @Override
  public String toString() {
    return "UNKNOWN_VALUE";
  }
}
