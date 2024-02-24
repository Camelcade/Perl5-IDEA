/*
 * Copyright 2015-2024 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi.stubs;

import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.util.Processor;
import com.perl5.lang.perl.idea.EP.PerlPackageProcessorService;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager;
import com.perl5.lang.perl.psi.PerlSubCallHandlerVersionService;
import com.perl5.lang.perl.util.PerlStubUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;


public abstract class PerlStubIndexBase<Psi extends PsiElement> extends StringStubIndexExtension<Psi> {
  private static final int VERSION = 9;

  @Override
  public int getVersion() {
    return super.getVersion() +
           VERSION +
           PerlValuesManager.getVersion() +
           PerlSubCallHandlerVersionService.getHandlersVersion() +
           PerlPackageProcessorService.getVersion();
  }

  public @NotNull Collection<String> getAllNames(@NotNull Project project) {
    return StubIndex.getInstance().getAllKeys(getKey(), project);
  }

  public @NotNull Collection<String> getAllNames(@NotNull GlobalSearchScope globalSearchScope) {
    return PerlStubUtil.getAllKeys(getKey(), globalSearchScope);
  }

  protected abstract @NotNull Class<Psi> getPsiClass();

  public boolean processElements(@NotNull Project project,
                                 @NotNull String keyText,
                                 @NotNull GlobalSearchScope scope,
                                 @NotNull Processor<? super Psi> processor) {
    return StubIndex.getInstance().processElements(getKey(), keyText, project, scope, getPsiClass(), element -> {
      ProgressManager.checkCanceled();
      return processor.process(element);
    });
  }
}
