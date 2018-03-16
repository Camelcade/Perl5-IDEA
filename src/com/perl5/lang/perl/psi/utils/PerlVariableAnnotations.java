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
import com.perl5.lang.perl.psi.PerlAnnotation;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PsiPerlAnnotationDeprecated;
import com.perl5.lang.perl.psi.PsiPerlAnnotationType;
import com.perl5.lang.perl.psi.stubs.PerlStubSerializationUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

/**
 * Created by hurricup on 08.08.2016.
 */
public class PerlVariableAnnotations {
  private static final byte IS_DEPRECATED = 0x01;

  private byte myFlags = 0;

  private PerlReturnType myRefType = PerlReturnType.VALUE;
  private String myType = null;


  public PerlVariableAnnotations() {

  }

  public PerlVariableAnnotations(byte flags, @Nullable String type, PerlReturnType refType) {
    myFlags = flags;
    myType = type;
    myRefType = refType;
  }

  public void serialize(@NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeByte(myFlags);
    dataStream.writeName(myType);
    myRefType.serialize(dataStream);
  }

  public boolean isDeprecated() {
    return (myFlags & IS_DEPRECATED) == IS_DEPRECATED;
  }

  public void setIsDeprecated() {
    myFlags |= IS_DEPRECATED;
  }

  public PerlReturnType getRefType() {
    return myRefType;
  }

  public void setRefType(PerlReturnType refType) {
    myRefType = refType;
  }

  public String getType() {
    return myType;
  }

  public void setType(String type) {
    myType = type;
  }

  public static PerlVariableAnnotations deserialize(@NotNull StubInputStream dataStream) throws IOException {
    return new PerlVariableAnnotations(
      dataStream.readByte(),
      PerlStubSerializationUtil.readString(dataStream),
      PerlReturnType.deserialize(dataStream)
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
      else if (annotation instanceof PsiPerlAnnotationType) // type
      {
        PerlNamespaceElement ns = ((PsiPerlAnnotationType)annotation).getType();
        if (ns != null) {
          myAnnotations.setType(ns.getCanonicalName());
          myAnnotations.setRefType(PerlReturnType.REF);
          // todo implement brackets and braces
        }
      }
    }

    return myAnnotations;
  }
}
