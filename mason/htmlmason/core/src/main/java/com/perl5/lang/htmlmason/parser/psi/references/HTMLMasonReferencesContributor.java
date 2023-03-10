/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

package com.perl5.lang.htmlmason.parser.psi.references;

import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceRegistrar;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.htmlmason.HTMLMasonElementPatterns.HTML_MASON_COMPONENT_CALLEE;
import static com.perl5.lang.htmlmason.HTMLMasonElementPatterns.HTML_MASON_FLAGS_PARENT;


public class HTMLMasonReferencesContributor extends PsiReferenceContributor {
  @Override
  public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
    registrar.registerReferenceProvider(
      HTML_MASON_COMPONENT_CALLEE,
      new HTMLMasonComponentReferencesProvider()
    );
    registrar.registerReferenceProvider(
      HTML_MASON_FLAGS_PARENT,
      new HTMLMasonFlagsReferencesProvider()
    );
  }
}
