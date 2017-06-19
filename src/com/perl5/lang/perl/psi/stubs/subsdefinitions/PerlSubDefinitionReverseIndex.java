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

package com.perl5.lang.perl.psi.stubs.subsdefinitions;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.intellij.util.Processor;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import com.perl5.lang.perl.psi.stubs.PerlStubIndexBase;
import org.jetbrains.annotations.NotNull;

/**
 * Index of package to sub definition
 */
public class PerlSubDefinitionReverseIndex extends PerlStubIndexBase<PerlSubDefinitionElement> {
  public static final int VERSION = 1;
  public static final StubIndexKey<String, PerlSubDefinitionElement> KEY = StubIndexKey.createIndexKey("perl.sub.definition.reverse");

  @Override
  public int getVersion() {
    return super.getVersion() + VERSION;
  }

  @NotNull
  @Override
  public StubIndexKey<String, PerlSubDefinitionElement> getKey() {
    return KEY;
  }

  public static boolean processSubDefinitionsInPackage(@NotNull Project project,
                                                       @NotNull String packageName,
                                                       @NotNull GlobalSearchScope scope,
                                                       @NotNull Processor<PerlSubDefinitionElement> processor) {
    return StubIndex.getInstance().processElements(KEY, packageName, project, scope, PerlSubDefinitionElement.class, processor);
  }
}
