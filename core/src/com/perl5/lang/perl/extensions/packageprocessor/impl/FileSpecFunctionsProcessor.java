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

package com.perl5.lang.perl.extensions.packageprocessor.impl;

import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessorBase;
import org.jetbrains.annotations.NotNull;

/**
 * Package works like a switcher and exports different implementation depending on target os.
 * We could handle this, but not sure we should at. Hardcoding to the unix one should be enough.
 */
public class FileSpecFunctionsProcessor extends PerlPackageProcessorBase {
  private static final String UNIX_IMPLEMENTATION_PACKAGE = "File::Spec::Unix";

  @NotNull
  @Override
  protected PerlExportDescriptor createDescriptor(@NotNull String packageName, @NotNull String name) {
    return super.createDescriptor(UNIX_IMPLEMENTATION_PACKAGE, name);
  }
}
