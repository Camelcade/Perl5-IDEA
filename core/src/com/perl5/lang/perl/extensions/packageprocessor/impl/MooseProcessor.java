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

package com.perl5.lang.perl.extensions.packageprocessor.impl;

import com.perl5.lang.perl.extensions.packageprocessor.*;
import com.perl5.lang.perl.psi.PerlUseStatement;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.perl5.lang.perl.util.PerlPackageUtil.*;


public class MooseProcessor extends PerlPackageProcessorBase implements
                                                             PerlStrictProvider,
                                                             PerlWarningsProvider,
                                                             PerlPackageParentsProvider,
                                                             PerlPackageLoader {
  protected static final List<String> LOADED_CLASSES = Arrays.asList(
    PACKAGE_MOOSE_OBJECT, PACKAGE_CARP, PACKAGE_SCALAR_UTIL
  );
  protected static final List<String> PARENT_CLASSES = Collections.singletonList(PACKAGE_MOOSE_OBJECT);
  static final List<PerlExportDescriptor> EXPORTS = Arrays.asList(
    PerlExportDescriptor.create(PACKAGE_CARP, "confess"),
    PerlExportDescriptor.create(PACKAGE_SCALAR_UTIL, "blessed")
  );

  @NotNull
  @Override
  public List<String> getLoadedPackageNames(PerlUseStatement useStatement) {
    return getLoadedClasses();
  }

  @Override
  public void changeParentsList(@NotNull PerlUseStatement useStatement, @NotNull List<String> currentList) {
    currentList.clear();
    currentList.addAll(getParentClasses());
  }

  @Override
  public boolean hasPackageFilesOptions() {
    return false;
  }

  public List<String> getLoadedClasses() {
    return LOADED_CLASSES;
  }

  public List<String> getParentClasses() {
    return PARENT_CLASSES;
  }

  @NotNull
  @Override
  public List<PerlExportDescriptor> getImports(@NotNull PerlUseStatement useStatement) {
    return EXPORTS;
  }
}
