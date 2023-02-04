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

package com.perl5.lang.perl.extensions.log4perl;

import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageOptionsProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessorBase;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Log4PerlPackageProcessor extends PerlPackageProcessorBase implements PerlPackageOptionsProvider {
  private static final String GET_LOGGER = "get_logger";
  private static final String LEVELS = ":levels";
  private static final String EASY = ":easy";
  private static final String LOG4PERL = "Log::Log4perl";
  private static final String LOG4PERL_LOGGER = "Log::Log4perl::Logger";
  private static final String LOG4PERL_LEVELS = "Log::Log4perl::Levels";

  private static final Map<String, String> OPTIONS = Map.of(
    LEVELS, "Exports log levels from Log4perl::Level",
    ":nowarn", "No warnings about non-initialized usage",
    ":nostrict", "Put Log4perl in a more permissive mode",
    ":resurrect", "Resurrects buried statements before running",
    ":no_extra_logdie_message", "Suppresses extra LOGDIE message"
  );

  private static final List<String> LOG_METHODS = List.of(
    "TRACE", "DEBUG", "INFO", "WARN", "ERROR", "FATAL", "ALWAYS", "LOGDIE", "LOGEXIT", "LOGWARN");

  private static final List<String> LOG_LOGGER_METHODS = List.of("LOGCROAK", "LOGCLUCK", "LOGCARP", "LOGCONFESS");

  private static final List<String> LEVELS_VARS = List.of("OFF", "FATAL", "ERROR", "WARN", "INFO", "DEBUG", "TRACE", "ALL");

  private static final Map<String, String> BUNDLE = Collections.singletonMap(
    EASY, "get_logger, logging methods, :levels and :nowarn"
  );

  @Override
  public @NotNull Map<String, String> getOptions() {
    return OPTIONS;
  }

  @Override
  public @NotNull Map<String, String> getOptionsBundles() {
    return BUNDLE;
  }

  @Override
  public @NotNull List<PerlExportDescriptor> getImports(@NotNull PerlUseStatementElement useStatement) {
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
        case GET_LOGGER -> importGetLogger = true;
        case LEVELS -> importLevels = true;
        case EASY -> {
          importGetLogger = true;
          importLevels = true;
          importSubs = true;
        }
      }
    }

    if (!importGetLogger && !importLevels) {
      return Collections.emptyList();
    }

    List<PerlExportDescriptor> result = new ArrayList<>();
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
  public void addExports(@NotNull PerlUseStatementElement useStatement, @NotNull Set<String> export, @NotNull Set<String> exportOk) {
    export.add(GET_LOGGER);
    exportOk.add(GET_LOGGER);
  }
}
