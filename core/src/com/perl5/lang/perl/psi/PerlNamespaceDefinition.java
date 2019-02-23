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

import com.perl5.lang.perl.psi.mro.PerlMro;
import com.perl5.lang.perl.psi.mro.PerlMroC3;
import com.perl5.lang.perl.psi.mro.PerlMroDfs;
import com.perl5.lang.perl.psi.mro.PerlMroType;
import com.perl5.lang.perl.psi.utils.PerlNamespaceAnnotations;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Describes api for perl namespace definition; Should be used only for inheritance
 */
public interface PerlNamespaceDefinition extends PerlDeprecatable {
  /**
   * Returns package name
   *
   * @return canonical package name
   */
  @Nullable
  String getPackageName();

  /**
   * Get mro type for current package
   *
   * @return mro type
   */
  @NotNull
  PerlMroType getMroType();

  /**
   * Returns MRO instance for current package
   *
   * @return mro class instance
   */
  @NotNull
  default PerlMro getMro() {
    return getMroType() == PerlMroType.C3 ? PerlMroC3.INSTANCE : PerlMroDfs.INSTANCE;
  }

  /**
   * Returns list of parent namespace names from stub or psi
   *
   * @return list of names
   */
  @NotNull
  List<String> getParentNamespacesNames();

  /**
   * Returns stubbed, local or external namespace annotations
   *
   * @return annotations or null
   */
  @Nullable
  PerlNamespaceAnnotations getAnnotations();

  /**
   * Retuns list of exports from this module
   *
   * @return list of @EXPORTs
   */
  @NotNull
  List<String> getEXPORT();

  /**
   * .
   * Returns list of optional exports from this module
   *
   * @return list of @EXPORT_OKs
   */
  @NotNull
  List<String> getEXPORT_OK();

  /**
   * Returns map of exported tags
   *
   * @return map of %EXPORT_TAGS
   */
  @NotNull
  Map<String, List<String>> getEXPORT_TAGS();

  @Override
  default boolean isDeprecated() {
    PerlNamespaceAnnotations namespaceAnnotations = getAnnotations();
    return namespaceAnnotations != null && namespaceAnnotations.isDeprecated();
  }
}
