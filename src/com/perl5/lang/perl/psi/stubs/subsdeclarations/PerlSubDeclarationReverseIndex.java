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

package com.perl5.lang.perl.psi.stubs.subsdeclarations;

import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.intellij.util.Processor;
import com.perl5.lang.perl.psi.PerlSubDeclarationElement;
import com.perl5.lang.perl.psi.stubs.PerlStubIndexBase;
import org.jetbrains.annotations.NotNull;

public class PerlSubDeclarationReverseIndex extends PerlStubIndexBase<PerlSubDeclarationElement> {
  public static final int VERSION = 1;
  public static final StubIndexKey<String, PerlSubDeclarationElement> KEY = StubIndexKey.createIndexKey("perl.sub.declaration.reverse");

  @Override
  public int getVersion() {
    return super.getVersion() + VERSION;
  }

  @NotNull
  @Override
  public StubIndexKey<String, PerlSubDeclarationElement> getKey() {
    return KEY;
  }

  public static boolean processSubDeclarationsInPackage(@NotNull Project project,
                                                        @NotNull String packageName,
                                                        @NotNull GlobalSearchScope scope,
                                                        @NotNull Processor<PerlSubDeclarationElement> processor) {
    return StubIndex.getInstance().processElements(KEY, packageName, project, scope, PerlSubDeclarationElement.class, element -> {
      ProgressManager.checkCanceled();
      return processor.process(element);
    });
  }
}
