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

package com.perl5.lang.perl.psi.utils;

import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.PerlAnnotation;
import com.perl5.lang.perl.psi.PsiPerlAnnotationDeprecated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class PerlNamespaceAnnotations {
  private static final byte IS_DEPRECATED = 0x01;

  private byte myFlags = 0;

  public PerlNamespaceAnnotations() {

  }

  public PerlNamespaceAnnotations(byte flags) {
    myFlags = flags;
  }

  public boolean isDeprecated() {
    return (myFlags & IS_DEPRECATED) == IS_DEPRECATED;
  }

  public void setIsDeprecated() {
    myFlags |= IS_DEPRECATED;
  }

  public byte getFlags() {
    return myFlags;
  }

  private static @Nullable PerlNamespaceAnnotations createFromAnnotationsList(List<? extends PerlAnnotation> annotations) {
    if (annotations.isEmpty()) {
      return null;
    }

    PerlNamespaceAnnotations myAnnotations = new PerlNamespaceAnnotations();

    for (PerlAnnotation annotation : annotations) {
      if (annotation instanceof PsiPerlAnnotationDeprecated) {
        myAnnotations.setIsDeprecated();
      }
    }

    return myAnnotations;
  }

  /**
   * Attempts to build namespace annotations from one of the base elements. Fist wins
   *
   * @param baseElements elements to process, e.g. identifier or use constant
   * @return Sub annotations
   */
  public static @Nullable PerlNamespaceAnnotations tryToFindAnnotations(@NotNull PsiElement... baseElements) {
    for (PsiElement element : baseElements) {
      List<PerlAnnotation> annotations = PerlAnnotations.collectAnnotations(element);
      if (!annotations.isEmpty()) {
        return createFromAnnotationsList(annotations);
      }
    }

    return null;
  }

  @Override
  public String toString() {
    return isDeprecated() ? "deprecated" : "none";
  }
}
