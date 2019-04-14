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

import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager;
import com.perl5.lang.perl.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;

/**
 * Created by hurricup on 03.06.2015.
 */
public class PerlSubAnnotations {
  private static final byte IS_METHOD = 0x01;
  private static final byte IS_DEPRECATED = 0x02;
  private static final byte IS_ABSTRACT = 0x04;
  private static final byte IS_OVERRIDE = 0x08;

  private byte myFlags = 0;

  @NotNull
  private PerlValue myReturnValue = UNKNOWN_VALUE;

  public PerlSubAnnotations() {
  }

  private PerlSubAnnotations(byte flags, @NotNull PerlValue returnValue) {
    myFlags = flags;
    myReturnValue = returnValue;
  }

  public void serialize(@NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeByte(myFlags);
    myReturnValue.serialize(dataStream);
  }

  public boolean isMethod() {
    return (myFlags & IS_METHOD) == IS_METHOD;
  }

  public void setIsMethod() {
    myFlags |= IS_METHOD;
  }

  public boolean isDeprecated() {
    return (myFlags & IS_DEPRECATED) == IS_DEPRECATED;
  }

  public void setIsDeprecated() {
    myFlags |= IS_DEPRECATED;
  }

  public boolean isAbstract() {
    return (myFlags & IS_ABSTRACT) == IS_ABSTRACT;
  }

  public void setIsAbstract() {
    myFlags |= IS_ABSTRACT;
  }

  public boolean isOverride() {
    return (myFlags & IS_OVERRIDE) == IS_OVERRIDE;
  }

  public void setIsOverride() {
    myFlags |= IS_OVERRIDE;
  }

  @NotNull
  public PerlValue getReturnValue() {
    return myReturnValue;
  }

  public void setReturnValue(@NotNull PerlValue returnValue) {
    myReturnValue = returnValue;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PerlSubAnnotations that = (PerlSubAnnotations)o;

    if (myFlags != that.myFlags) {
      return false;
    }
    return myReturnValue.equals(that.myReturnValue);
  }

  @Override
  public int hashCode() {
    int result = (int)myFlags;
    result = 31 * result + myReturnValue.hashCode();
    return result;
  }

  public static PerlSubAnnotations deserialize(@NotNull StubInputStream dataStream) throws IOException {
    return new PerlSubAnnotations(
      dataStream.readByte(),
      PerlValuesManager.readValue(dataStream)
    );
  }

  /**
   * Attempts to build sub annotations from one of the base elements. Fist wins
   *
   * @param baseElements elements to process, e.g. identifier or use constant
   * @return Sub annotations
   */
  @Nullable
  public static PerlSubAnnotations tryToFindAnnotations(@NotNull PsiElement... baseElements) {
    for (PsiElement element : baseElements) {
      List<PerlAnnotation> annotations = PerlPsiUtil.collectAnnotations(element);
      if (!annotations.isEmpty()) {
        return createFromAnnotationsList(annotations);
      }
    }

    return null;
  }

  @Nullable
  public static PerlSubAnnotations tryToFindAnnotations(@NotNull List<PsiElement> baseElements) {
    return tryToFindAnnotations(baseElements.toArray(PsiElement.EMPTY_ARRAY));
  }

  @Nullable
  public static PerlSubAnnotations createFromAnnotationsList(List<PerlAnnotation> annotations) {
    if (annotations.isEmpty()) {
      return null;
    }

    PerlSubAnnotations result = new PerlSubAnnotations();

    for (PerlAnnotation annotation : annotations) {
      if (annotation instanceof PsiPerlAnnotationAbstract) {
        result.setIsAbstract();
      }
      else if (annotation instanceof PsiPerlAnnotationDeprecated) {
        result.setIsDeprecated();
      }
      else if (annotation instanceof PsiPerlAnnotationMethod) {
        result.setIsMethod();
      }
      else if (annotation instanceof PsiPerlAnnotationOverride) {
        result.setIsOverride();
      }
      else if (annotation instanceof PsiPerlAnnotationReturns) {
        result.setReturnValue(((PsiPerlAnnotationReturns)annotation).getValue());
      }
    }

    return result;
  }

  @Nullable
  public static PerlSubAnnotations computeForLightElement(@NotNull PsiElement delegate, @NotNull PsiElement nameIdentifier) {
    List<PsiElement> baseElements = new ArrayList<>();
    PsiPerlStatement containingStatement = PsiTreeUtil.getParentOfType(delegate, PsiPerlStatement.class);
    if (containingStatement != null) {
      baseElements.add(containingStatement);
    }
    baseElements.add(delegate);

    if (nameIdentifier instanceof PerlStringContentElement) {
      PerlStringList perlStringList = PsiTreeUtil.getParentOfType(nameIdentifier, PerlStringList.class);
      if (perlStringList != null) {
        baseElements.add(perlStringList);
      }
    }
    else {
      baseElements.add(nameIdentifier);
    }

    return tryToFindAnnotations(baseElements);
  }
}
