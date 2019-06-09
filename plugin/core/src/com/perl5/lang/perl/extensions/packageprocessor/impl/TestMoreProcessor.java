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

package com.perl5.lang.perl.extensions.packageprocessor.impl;

import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageOptionsProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessorBase;
import com.perl5.lang.perl.psi.PerlUseStatement;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TestMoreProcessor extends PerlPackageProcessorBase implements PerlPackageOptionsProvider {
  private static final Map<String, String> OPTIONS = new THashMap<>();

  static {
    OPTIONS.put("no_plan", "");
    OPTIONS.put("skip_all", "Skip all tests");
    OPTIONS.put("tests", "Number of tests expected");
  }

  @Nullable
  @Override
  protected List<String> getImportParameters(@NotNull PerlUseStatement useStatement) {
    return null; // this will cause descriptors to ignore parameters above
  }

  @NotNull
  @Override
  public Map<String, String> getOptions() {
    return OPTIONS;
  }

  @NotNull
  @Override
  public Map<String, String> getOptionsBundles() {
    return Collections.emptyMap();
  }
}
