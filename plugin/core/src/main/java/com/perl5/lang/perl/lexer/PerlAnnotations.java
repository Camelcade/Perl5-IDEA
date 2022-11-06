/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.perl.lexer;

import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlScalarValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.psi.PerlAnnotationWithValue;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PsiPerlAnnotationNoInject;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;
import static com.perl5.lang.perl.util.PerlPackageUtil.NAMESPACE_ANY;
import static com.perl5.lang.perl.util.PerlPackageUtil.NAMESPACE_ANY_VALUE;


public final class PerlAnnotations implements PerlElementTypes {
  private PerlAnnotations() {
  }

  public static final Map<String, IElementType> TOKENS_MAP = new HashMap<>();

  static {
    TOKENS_MAP.put("deprecated", ANNOTATION_DEPRECATED_KEY);
    TOKENS_MAP.put("returns", ANNOTATION_RETURNS_KEY);
    TOKENS_MAP.put("type", ANNOTATION_TYPE_KEY);
    TOKENS_MAP.put("method", ANNOTATION_METHOD_KEY);
    TOKENS_MAP.put("inject", ANNOTATION_INJECT_KEY);
    TOKENS_MAP.put("noinject", ANNOTATION_NO_INJECT_KEY);

    // these are parsed but not used
    TOKENS_MAP.put("override", ANNOTATION_OVERRIDE_KEY);
    TOKENS_MAP.put("abstract", ANNOTATION_ABSTRACT_KEY);
    TOKENS_MAP.put("noinspection", ANNOTATION_NOINSPECTION_KEY);
  }

  /**
   * @return true iff injection for {@code host} is suppressed with {@code #@noinject} annotations
   */
  public static boolean isInjectionSuppressed(@NotNull PsiElement host) {
    return PerlPsiUtil.getAnyAnnotationByClass(host, PsiPerlAnnotationNoInject.class) != null;
  }

  private static @Nullable String getReturnClass(@NotNull PerlAnnotationWithValue annotation) {
    PerlNamespaceElement namespaceElement = PsiTreeUtil.getChildOfType(annotation, PerlNamespaceElement.class);
    if (namespaceElement != null) {
      return namespaceElement.getCanonicalName();
    }

    PsiElement run = annotation.getFirstChild();
    while (run != null) {
      if (PsiUtilCore.getElementType(run) == OPERATOR_MUL) {
        return NAMESPACE_ANY;
      }
      run = run.getNextSibling();
    }

    return null;
  }

  /**
   * @return value desscribing annotation parameter: classname, wildcard, etc.
   */
  public static PerlValue getParameterValue(@NotNull PerlAnnotationWithValue annotation) {
    String returnClass = getReturnClass(annotation);
    return returnClass == null ? UNKNOWN_VALUE :
           returnClass.equals(NAMESPACE_ANY) ? NAMESPACE_ANY_VALUE : PerlScalarValue.create(returnClass);
  }
}
