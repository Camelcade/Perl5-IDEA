/*
 * Copyright 2015-2022 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi.utils;

import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager;
import com.perl5.lang.perl.psi.PerlAnnotation;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PsiPerlAnnotationDeprecated;
import com.perl5.lang.perl.psi.PsiPerlAnnotationType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;

public class PerlVariableAnnotations {
  private static final PerlVariableAnnotations EMPTY = new PerlVariableAnnotations() {
    @Override
    public void setIsDeprecated() {
      throw new UnsupportedOperationException();
    }

    @Override
    public void setValue(@NotNull PerlValue value) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
      return true;
    }
  };

  private static final byte IS_DEPRECATED = 0x01;

  private byte myFlags = 0;

  private @NotNull PerlValue myValue = UNKNOWN_VALUE;

  private PerlVariableAnnotations() {
  }

  private PerlVariableAnnotations(byte flags, @NotNull PerlValue value) {
    myFlags = flags;
    myValue = value;
  }

  public void serialize(@NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeByte(myFlags);
    myValue.serialize(dataStream);
  }

  public boolean isDeprecated() {
    return (myFlags & IS_DEPRECATED) == IS_DEPRECATED;
  }

  public void setIsDeprecated() {
    myFlags |= IS_DEPRECATED;
  }

  public @NotNull PerlValue getAnnotatedValue() {
    return myValue;
  }

  public void setValue(@NotNull PerlValue value) {
    myValue = value;
  }

  public boolean isEmpty() {
    return myFlags == 0 && myValue.isUnknown();
  }

  public static PerlVariableAnnotations empty() {
    return EMPTY;
  }

  public static PerlVariableAnnotations deserialize(@NotNull StubInputStream dataStream) throws IOException {
    return new PerlVariableAnnotations(
      dataStream.readByte(),
      PerlValuesManager.readValue(dataStream)
    );
  }

  public static @Nullable PerlVariableAnnotations createFromAnnotationsList(@NotNull List<PerlAnnotation> annotations,
                                                                            @NotNull PerlVariable perlVariable) {
    if (annotations.isEmpty()) {
      return null;
    }

    PerlVariableAnnotations myAnnotations = new PerlVariableAnnotations();

    boolean valueSet = false;
    boolean valueSetExplicitly = false;
    for (PerlAnnotation annotation : annotations) {
      if (annotation instanceof PsiPerlAnnotationDeprecated) {
        myAnnotations.setIsDeprecated();
      }
      else if (!valueSetExplicitly && annotation instanceof PsiPerlAnnotationType typeAnnotation && typeAnnotation.accept(perlVariable)) {
        var isWildCard = typeAnnotation.isWildCard();
        if (!valueSet || !isWildCard) {
          valueSet = true;
          valueSetExplicitly = !isWildCard;
          myAnnotations.setValue(((PsiPerlAnnotationType)annotation).getValue());
        }
      }
    }

    return myAnnotations;
  }

  @Override
  public String toString() {
    return isDeprecated() ? "deprecated" : "none";
  }
}
