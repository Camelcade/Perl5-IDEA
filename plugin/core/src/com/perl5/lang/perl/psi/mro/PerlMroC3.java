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

package com.perl5.lang.perl.psi.mro;

import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


public class PerlMroC3 extends PerlMro {
  public static final PerlMro INSTANCE = new PerlMroC3();

  /**
   * Builds list of inheritance path for C3 mro (Dylan, Python, Perl6): http://perldoc.perl.org/mro.html#The-C3-MRO
   *
   * @param project              project
   * @param namespaceDefinitions list of package names to add
   * @param recursionMap         recursion protection map
   * @param result               list to populate
   */
  @Override
  public void getLinearISA(@NotNull Project project,
                           @NotNull List<PerlNamespaceDefinitionElement> namespaceDefinitions,
                           @NotNull Set<String> recursionMap,
                           @NotNull List<String> result) {
    ProgressManager.checkCanceled();
    Collection<PerlNamespaceDefinitionElement> nextIterationDefinitions = new ArrayList<>();
    for (PerlNamespaceDefinitionElement namespaceDefinition : namespaceDefinitions) {
      String packageName = namespaceDefinition.getNamespaceName();
      if (!recursionMap.contains(packageName)) {
        recursionMap.add(packageName);
        result.add(packageName);
        nextIterationDefinitions.add(namespaceDefinition);
      }
    }

    for (PerlNamespaceDefinitionElement namespaceDefinition : nextIterationDefinitions) {
      namespaceDefinition.getLinearISA(recursionMap, result);
    }
  }
}
