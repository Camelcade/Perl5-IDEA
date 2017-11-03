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

package com.perl5.lang.perl.util;

import com.intellij.openapi.util.Comparing;
import com.perl5.lang.perl.psi.PerlAnnotation;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PsiPerlAnnotationDeprecated;
import com.perl5.lang.perl.psi.utils.PerlVariableAnnotations;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by hurricup on 08.08.2016.
 */
public class PerlVariableUtil {
  @Nullable
  public static PerlVariableAnnotations aggregateAnnotationsList(List<PerlAnnotation> annotations) {
    if (annotations.isEmpty()) {
      return null;
    }

    PerlVariableAnnotations myAnnotations = new PerlVariableAnnotations();

    for (PerlAnnotation annotation : annotations) {
      if (annotation instanceof PsiPerlAnnotationDeprecated) {
        myAnnotations.setIsDeprecated();
      }
    }

    return myAnnotations;
  }

  /**
   * Compares two variables by actual type, packageName and name. They can be from different declarations
   * builds AST
   */
  public static boolean equal(@Nullable PerlVariable v1, @Nullable PerlVariable v2) {
    if (Comparing.equal(v1, v2)) {
      return true;
    }
    assert v1 != null && v2 != null;
    return v1.getActualType().equals(v2.getActualType()) &&
           Comparing.equal(v1.getPackageName(), v2.getPackageName()) &&
           Comparing.equal(v1.getName(), v2.getName());
  }
}
