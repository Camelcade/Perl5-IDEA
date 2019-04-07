/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.codeInsight.typeInference.value;

import com.intellij.psi.PsiNamedElement;
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import org.jetbrains.annotations.NotNull;

/**
 * @see com.intellij.util.Processor
 */
public interface PerlNamespaceItemProcessor<T extends PsiNamedElement> {
  /**
   * invoked for every defined or declared item
   */
  boolean processItem(@NotNull T t);

  /**
   * invoked for every imported item with definition or declaration
   */
  boolean processImportedItem(@NotNull T t, @NotNull PerlExportDescriptor exportDescriptor);

  /**
   * invoked for every descriptor without deifitions
   */
  boolean processOrphanDescriptor(@NotNull PerlExportDescriptor exportDescriptor);
}
