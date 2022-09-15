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

package com.perl5.lang.perl.profiler.parser;

import com.intellij.openapi.project.Project;
import com.intellij.profiler.api.ProfilerDumpFileParser;
import com.intellij.profiler.api.ProfilerDumpParserProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlProfilerDumpFileParserProvider implements ProfilerDumpParserProvider {
  @Override
  public @NotNull String getId() {
    return "perl5.dump.parser";
  }

  @Override
  public @NotNull String getName() {
    return "NYTProf Dump";
  }

  @Override
  public @Nullable String getRequiredFileExtension() {
    return null;
  }

  @Override
  public @NotNull ProfilerDumpFileParser createParser(@NotNull Project project) {
    return new PerlProfilerDumpFileParser(project);
  }
}
