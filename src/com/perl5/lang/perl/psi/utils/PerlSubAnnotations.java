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
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.stubs.PerlStubSerializationUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

/**
 * Created by hurricup on 03.06.2015.
 */
public class PerlSubAnnotations {
  private static final byte IS_METHOD = 0x01;
  private static final byte IS_DEPRECATED = 0x02;
  private static final byte IS_ABSTRACT = 0x04;
  private static final byte IS_OVERRIDE = 0x08;

  private byte myFlags = 0;
  private PerlReturnType myReturnType = PerlReturnType.VALUE;
  private String myReturns = null;

  public PerlSubAnnotations() {
  }

  public PerlSubAnnotations(byte flags, String returns, PerlReturnType returnType) {
    myFlags = flags;
    myReturnType = returnType;
    myReturns = returns;
  }

  public void serialize(@NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeByte(myFlags);
    dataStream.writeName(myReturns);
    myReturnType.serialize(dataStream);
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

  public String getReturns() {
    return myReturns;
  }

  public void setReturns(String returns) {
    this.myReturns = returns;
  }

  public PerlReturnType getReturnType() {
    return myReturnType;
  }

  public void setReturnType(PerlReturnType returnType) {
    this.myReturnType = returnType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PerlSubAnnotations)) return false;

    PerlSubAnnotations that = (PerlSubAnnotations)o;

    if (myFlags != that.myFlags) return false;
    if (getReturnType() != that.getReturnType()) return false;
    return getReturns() != null ? getReturns().equals(that.getReturns()) : that.getReturns() == null;
  }

  @Override
  public int hashCode() {
    int result = (int)myFlags;
    result = 31 * result + (getReturnType() != null ? getReturnType().hashCode() : 0);
    result = 31 * result + (getReturns() != null ? getReturns().hashCode() : 0);
    return result;
  }

  public static PerlSubAnnotations deserialize(@NotNull StubInputStream dataStream) throws IOException {
    return new PerlSubAnnotations(
      dataStream.readByte(),
      PerlStubSerializationUtil.readNullableString(dataStream),
      PerlReturnType.deserialize(dataStream)
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
    return tryToFindAnnotations(baseElements.toArray(new PsiElement[baseElements.size()]));
  }

  @Nullable
  public static PerlSubAnnotations createFromAnnotationsList(List<PerlAnnotation> annotations) {
    if (annotations.isEmpty()) {
      return null;
    }

    PerlSubAnnotations myAnnotations = new PerlSubAnnotations();

    for (PerlAnnotation annotation : annotations) {
      if (annotation instanceof PsiPerlAnnotationAbstract) {
        myAnnotations.setIsAbstract();
      }
      else if (annotation instanceof PsiPerlAnnotationDeprecated) {
        myAnnotations.setIsDeprecated();
      }
      else if (annotation instanceof PsiPerlAnnotationMethod) {
        myAnnotations.setIsMethod();
      }
      else if (annotation instanceof PsiPerlAnnotationOverride) {
        myAnnotations.setIsOverride();
      }
      else if (annotation instanceof PsiPerlAnnotationReturns) // returns
      {
        PsiElement possibleNamespace = annotation.getLastChild();
        if (possibleNamespace instanceof PerlNamespaceElement) {
          myAnnotations.setReturns(((PerlNamespaceElement)possibleNamespace).getCanonicalName());
          myAnnotations.setReturnType(PerlReturnType.REF);
          // todo implement brackets and braces
        }
      }
    }

    return myAnnotations;
  }
}
