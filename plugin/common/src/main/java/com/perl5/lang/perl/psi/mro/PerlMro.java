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

package com.perl5.lang.perl.psi.mro;

import com.intellij.openapi.project.Project;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;


public abstract class PerlMro {
  protected PerlMro() {
  }

  /**
   * Method should return a sequence of packages. See http://perldoc.perl.org/mro.html#mro%3a%3aget_linear_isa(%24classname%5b%2c-%24type%5d)
   * Method should not add package itself or UNIVERSAL, only parents structure. Package itself and UNIVERSAL being added by calee
   *
   * @param project              current project
   * @param namespaceDefinitions list of namespaces to check
   * @param recursionMap         map for controlling recursive inheritance
   * @param result               list of package names to populate
   */
  public abstract void getLinearISA(@NotNull Project project,
                                    @NotNull List<? extends PerlNamespaceDefinitionElement> namespaceDefinitions,
                                    @NotNull Set<String> recursionMap,
                                    @NotNull List<String> result);
}
