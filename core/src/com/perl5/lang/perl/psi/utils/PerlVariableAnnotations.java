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

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.stubs.PerlStubSerializationUtil;
import com.perl5.lang.perl.types.PerlType;
import com.perl5.lang.perl.types.PerlTypeArrayRef;
import com.perl5.lang.perl.types.PerlTypeNamespace;
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
  private String myInnerType = null;


  public PerlVariableAnnotations() {

  }

  public PerlVariableAnnotations(byte flags, @Nullable String type, String innerType, PerlReturnType refType) {
    myFlags = flags;
    myType = type;
    myRefType = refType;
    myInnerType = innerType;
  }

  public void serialize(@NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeByte(myFlags);
    dataStream.writeName(myType);
    dataStream.writeName(myInnerType);
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

  // fixme rename to getType
  public PerlType getPerlType() {
    String name;
    switch (getRefType()) {
      case ARRAY_REF:
        name = getInnerType();
        return StringUtil.isEmpty(name) ? null : new PerlTypeArrayRef(new PerlTypeNamespace(name));
      default:
        name = getType();
        return StringUtil.isEmpty(name) ? null : new PerlTypeNamespace(name);
    }
  }

  public void setType(String type) {
    myType = type;
  }

  public void setInnerType(String type) {
    myInnerType = type;
  }

  public String getInnerType() {
    return myInnerType;
  }

  public static PerlVariableAnnotations deserialize(@NotNull StubInputStream dataStream) throws IOException {
    return new PerlVariableAnnotations(
      dataStream.readByte(),
      PerlStubSerializationUtil.readString(dataStream),
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
        }
        else {
          // inner type
          // fixme handle deep nested inner type
          PsiPerlArrayrefType type = ((PsiPerlAnnotationType)annotation).getArrayrefType();
          ns = PsiTreeUtil.getChildOfType(type, PerlNamespaceElement.class);
          if (ns != null) {
            myAnnotations.setInnerType(ns.getCanonicalName());
            myAnnotations.setRefType(PerlReturnType.ARRAY_REF);
          }
        }
      }
    }

    return myAnnotations;
  }
}
