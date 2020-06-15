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

package com.perl5.lang.perl.psi.stubs.namespaces;

import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.intellij.util.Processor;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.impl.PerlPolyNamedElement;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.stubs.PerlStubIndexBase;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class PerlLightNamespaceIndex extends PerlStubIndexBase<PerlPolyNamedElement> {
  public static final int VERSION = 1;
  public static final StubIndexKey<String, PerlPolyNamedElement> KEY = StubIndexKey.createIndexKey("perl.package.light.direct");

  @Override
  public int getVersion() {
    return super.getVersion() + VERSION;
  }

  @Override
  public @NotNull StubIndexKey<String, PerlPolyNamedElement> getKey() {
    return KEY;
  }

  public static boolean processNamespaces(@NotNull Project project,
                                          @NotNull String packageName,
                                          @NotNull GlobalSearchScope scope,
                                          @NotNull Processor<? super PerlNamespaceDefinitionElement> processor) {

    return StubIndex.getInstance().processElements(KEY, packageName, project, scope, PerlPolyNamedElement.class, polyNamedElement -> {
      ProgressManager.checkCanceled();
      for (PerlDelegatingLightNamedElement<?> lightNamedElement : ((PerlPolyNamedElement<?>)polyNamedElement).getLightElements()) {
        if (lightNamedElement instanceof PerlNamespaceDefinitionElement &&
            packageName.equals(((PerlNamespaceDefinitionElement)lightNamedElement).getNamespaceName())) {
          if (!processor.process((PerlNamespaceDefinitionElement)lightNamedElement)) {
            return false;
          }
        }
      }

      return true;
    });
  }

  public static @NotNull Collection<String> getAllNames(Project project) {
    return StubIndex.getInstance().getAllKeys(KEY, project);
  }
}
