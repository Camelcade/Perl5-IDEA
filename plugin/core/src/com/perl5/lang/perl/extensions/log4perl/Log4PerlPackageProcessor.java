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

package com.perl5.lang.perl.extensions.log4perl;

import com.intellij.openapi.util.Pair;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageOptionsProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessorBase;
import com.perl5.lang.perl.psi.PerlUseStatement;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Log4PerlPackageProcessor extends PerlPackageProcessorBase implements PerlPackageOptionsProvider {
  private static final String GET_LOGGER = "get_logger";
  private static final String LEVELS = ":levels";
  private static final String EASY = ":easy";
  private static final String LOG4PERL = "Log::Log4perl";
  private static final String LOG4PERL_LOGGER = "Log::Log4perl::Logger";
  private static final String LOG4PERL_LEVELS = "Log::Log4perl::Levels";

  private static final Map<String, String> OPTIONS = ContainerUtil.newHashMap(
    Pair.create(LEVELS, "Exports log levels from Log4perl::Level"),
    Pair.create(":nowarn", "No warnings about non-initalized usage"),
    Pair.create(":nostrict", "Put Log4perl in a more permissive mode"),
    Pair.create(":resurrect", "Resurrects buried statements before running"),
    Pair.create(":no_extra_logdie_message", "Suppresses extra LOGDIE message")
  );

  private static final List<String> LOG_METHODS = ContainerUtil.newArrayList(
    "TRACE", "DEBUG", "INFO", "WARN", "ERROR", "FATAL", "ALWAYS",
    "LOGDIE", "LOGEXIT", "LOGWARN");

  private static final List<String> LOG_LOGGER_METHODS = ContainerUtil.newArrayList(
    "LOGCROAK", "LOGCLUCK", "LOGCARP", "LOGCONFESS");

  private static final List<String> LEVELS_VARS = ContainerUtil.newArrayList(
    "OFF", "FATAL", "ERROR", "WARN", "INFO", "DEBUG", "TRACE", "ALL"
  );

  private static final Map<String, String> BUNDLE = Collections.singletonMap(
    EASY, "get_logger, logging methods, :levels and :nowarn"
  );

  @NotNull
  @Override
  public Map<String, String> getOptions() {
    return OPTIONS;
  }

  @NotNull
  @Override
  public Map<String, String> getOptionsBundles() {
    return BUNDLE;
  }

  @NotNull
  @Override
  public List<PerlExportDescriptor> getImports(@NotNull PerlUseStatement useStatement) {
    String packageName = useStatement.getPackageName();
    List<String> importParameters = useStatement.getImportParameters();
    if (packageName == null || importParameters == null || importParameters.isEmpty()) {
      return Collections.emptyList();
    }

    boolean importGetLogger = false;
    boolean importLevels = false;
    boolean importSubs = false;
    for (String importParameter : importParameters) {
      switch (importParameter) {
        case GET_LOGGER:
          importGetLogger = true;
          break;
        case LEVELS:
          importLevels = true;
          break;
        case EASY:
          importGetLogger = true;
          importLevels = true;
          importSubs = true;
      }
    }

    if (!importGetLogger && !importLevels && !importSubs) {
      return Collections.emptyList();
    }

    List<PerlExportDescriptor> result = ContainerUtil.newArrayList();
    if (importGetLogger) {
      result.add(PerlExportDescriptor.create(packageName, GET_LOGGER));
    }
    if (importSubs) {
      LOG_METHODS.forEach(it -> result.add(PerlExportDescriptor.create(LOG4PERL, it)));
      LOG_LOGGER_METHODS.forEach(it -> result.add(PerlExportDescriptor.create(LOG4PERL_LOGGER, it, it.toLowerCase())));
    }
    if (importLevels) {
      LEVELS_VARS.forEach(it -> result.add(PerlExportDescriptor.create(LOG4PERL_LEVELS, "$" + it)));
    }

    return result;
  }

  @Override
  public void addExports(@NotNull PerlUseStatement useStatement, @NotNull Set<String> export, @NotNull Set<String> exportOk) {
    export.add(GET_LOGGER);
    exportOk.add(GET_LOGGER);
  }
}
