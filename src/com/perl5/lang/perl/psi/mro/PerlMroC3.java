/*
 * Copyright 2015 Alexandr Evstigneev
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
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by hurricup on 08.08.2015.
 */
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
  public void getLinearISA(Project project,
                           List<PerlNamespaceDefinition> namespaceDefinitions,
                           HashSet<String> recursionMap,
                           ArrayList<String> result) {
    //		System.err.println("Resolving C3 for " + packageNames);
    Collection<PerlNamespaceDefinition> nextIterationDefinitions = new ArrayList<PerlNamespaceDefinition>();
    for (PerlNamespaceDefinition namespaceDefinition : namespaceDefinitions) {
      String packageName = namespaceDefinition.getPackageName();
      if (!recursionMap.contains(packageName)) {
        recursionMap.add(packageName);
        result.add(packageName);
        nextIterationDefinitions.add(namespaceDefinition);
      }
    }

    for (PerlNamespaceDefinition namespaceDefinition : nextIterationDefinitions) {
      namespaceDefinition.getLinearISA(recursionMap, result);
    }
  }
}
