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

package com.perl5.lang.perl.psi;

import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlStaticValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlUnknownValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.psi.properties.PerlPackageMember;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlUnknownValue.UNKNOWN_VALUE;


public interface PerlSub extends PerlDeprecatable, PerlPackageMember {

  /**
   * Returns function name for current function definition
   *
   * @return function name or null
   */
  String getSubName();

  /**
   * Checks PSI tree before a sub definition for annotations and builds annotations object
   *
   * @return PerlSubAnnotation object
   */
  @Nullable
  PerlSubAnnotations getAnnotations();

  /**
   * Returns canonical name PackageName::SubName
   *
   * @return name
   */
  default String getCanonicalName() {
    String packageName = getNamespaceName();
    if (packageName == null) {
      return null;
    }

    return packageName + PerlPackageUtil.PACKAGE_SEPARATOR + getSubName();
  }

  /**
   * Checks if sub defined as a method
   *
   * @return result
   */
  default boolean isMethod() {
    PerlSubAnnotations subAnnotations = getAnnotations();
    return subAnnotations != null && subAnnotations.isMethod();
  }

  /**
   * Checks if sub defined as static, default implementation returns !isMethod(), but may be different for constants for example
   *
   * @return true if sub is static
   */
  default boolean isStatic() {
    return !isMethod();
  }

  /**
   * Checks if current declaration/definition is XSub
   *
   * @return true if sub located in deparsed file
   */
  default boolean isXSub() {
    return false;
  }

  /**
   * Calculates return value. By default - checks annotations
   *
   * @param contextPackage package this sub been invoked from, useful to return $self
   * @param arguments      invocation arguments
   * @return type of return value if can be calculated, or {@link PerlUnknownValue#UNKNOWN_VALUE}
   */
  @NotNull
  default PerlValue getReturnValue(@Nullable String contextPackage, @NotNull List<PerlValue> arguments) {
    PerlValue valueFromCode = getReturnValueFromCode(contextPackage, arguments);
    if (!valueFromCode.isEmpty()) {
      return valueFromCode;
    }
    return getReturnValueFromAnnotations(contextPackage);
  }

  @NotNull
  default PerlValue getReturnValueFromCode(@Nullable String contextPackage, @NotNull List<PerlValue> arguments) {
    if (contextPackage != null && "new".equals(getSubName())) {
      return PerlStaticValue.create(contextPackage);
    }
    return UNKNOWN_VALUE;
  }

  @NotNull
  default PerlValue getReturnValueFromAnnotations(@Nullable String contextPackage) {
    PerlSubAnnotations subAnnotations = getAnnotations();
    if (subAnnotations == null) {
      return UNKNOWN_VALUE;
    }

    PerlValue returnValue = subAnnotations.getReturnValue();
    return PerlPackageUtil.PACKAGE_ANY_VALUE.equals(returnValue) ? PerlStaticValue.create(contextPackage) : returnValue;
  }

  default boolean isDeprecated() {
    PerlSubAnnotations subAnnotations = getAnnotations();
    return subAnnotations != null && subAnnotations.isDeprecated();
  }
}

