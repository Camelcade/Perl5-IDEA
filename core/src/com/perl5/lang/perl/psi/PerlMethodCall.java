/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

import com.perl5.lang.perl.psi.properties.PerlNamespaceElementContainer;
import com.perl5.lang.perl.psi.properties.PerlSubNameElementContainer;
import com.perl5.lang.perl.psi.properties.PerlValuableEntity;
import org.jetbrains.annotations.Nullable;

/**
 * Invocable method class
 */
public interface PerlMethodCall extends PerlNamespaceElementContainer, PerlSubNameElementContainer, PerlValuableEntity {
  /**
   * @return explicit namespace name if any
   */
  @Nullable
  String getExplicitNamespaceName();

  /**
   * Checks if explicit namespace defined - got object or namespace element
   *
   * @return checking result
   */
  default boolean hasExplicitNamespace() {
    return getExplicitNamespaceName() != null;
  }

  /**
   * Check if this is an object method invocation
   *
   * @return result
   */
  boolean isObjectMethod();
}
