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

package com.perl5.lang.perl.extensions.packageprocessor;

import com.perl5.lang.perl.psi.PerlUseStatement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by hurricup on 18.08.2015.
 * Implement this interface if your package modifies @ISA and provides package information
 */
public interface PerlPackageParentsProvider {
  /**
   * Modify list of parents provided by this package. Informaion being collected from all processors sequentially
   *
   * @param useStatement use statement to fetch parameters from
   * @param currentList  current parents list
   */
  void changeParentsList(@NotNull PerlUseStatement useStatement, @NotNull List<String> currentList);

  /**
   * Returns true if we should show available package files in autocompletion
   *
   * @return result
   */
  boolean hasPackageFilesOptions();
}
