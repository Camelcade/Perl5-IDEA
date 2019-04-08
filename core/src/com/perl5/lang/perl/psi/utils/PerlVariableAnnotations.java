/*
 * Copyright 2015-2017 Alexandr Evstigneev
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
import com.perl5.lang.perl.psi.PsiPerlAnnotationDeprecated;
import com.perl5.lang.perl.psi.PsiPerlAnnotationType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlUnknownValue.UNKNOWN_VALUE;

/**
 * Created by hurricup on 08.08.2016.
 */
public class PerlVariableAnnotations {
  private static final byte IS_DEPRECATED = 0x01;

  private byte myFlags = 0;

  @NotNull
  private PerlValue myValue = UNKNOWN_VALUE;

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

  @NotNull
  public PerlValue getAnnotatedValue() {
    return myValue;
  }

  public void setValue(@NotNull PerlValue value) {
    myValue = value;
  }

  public static PerlVariableAnnotations deserialize(@NotNull StubInputStream dataStream) throws IOException {
    return new PerlVariableAnnotations(
      dataStream.readByte(),
      PerlValuesManager.readValue(dataStream)
    );
  }

  @Nullable
  public static PerlVariableAnnotations createFromAnnotationsList(List<PerlAnnotation> annotations) {
    if (annotations.isEmpty()) {
      return null;
    }

    PerlVariableAnnotations myAnnotations = new PerlVariableAnnotations();

    for (PerlAnnotation annotation : annotations) {
      if (annotation instanceof PsiPerlAnnotationDeprecated) {
        myAnnotations.setIsDeprecated();
      }
      else if (annotation instanceof PsiPerlAnnotationType) {
        myAnnotations.setValue(((PsiPerlAnnotationType)annotation).getValue());
      }
    }

    return myAnnotations;
  }
}
