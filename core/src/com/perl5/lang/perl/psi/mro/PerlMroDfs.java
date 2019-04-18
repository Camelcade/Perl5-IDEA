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

package com.perl5.lang.perl.psi.mro;

import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

/**
 * Created by hurricup on 15.06.2015.
 * Class represents default Perl's MRO.
 * In other words, it knows how to find sub definition and/or declaration
 */
public class PerlMroDfs extends PerlMro {
  public static final PerlMro INSTANCE = new PerlMroDfs();

  /**
   * Builds list of inheritance path for DFS mro (Perl5 default): http://perldoc.perl.org/mro.html
   *
   * @param project              project
   * @param namespaceDefinitions List of package names to add
   * @param recursionMap         recursion protection map
   * @param result               list to populate
   */
  @Override
  public void getLinearISA(@NotNull Project project,
                           @NotNull List<PerlNamespaceDefinitionElement> namespaceDefinitions,
                           @NotNull Set<String> recursionMap,
                           @NotNull List<String> result) {
    ProgressManager.checkCanceled();
    for (PerlNamespaceDefinitionElement namespaceDefinition : namespaceDefinitions) {
      String packageName = namespaceDefinition.getNamespaceName();
      if (!recursionMap.contains(packageName)) {
        recursionMap.add(packageName);
        result.add(packageName);
        namespaceDefinition.getLinearISA(recursionMap, result);
      }
    }
  }
}
