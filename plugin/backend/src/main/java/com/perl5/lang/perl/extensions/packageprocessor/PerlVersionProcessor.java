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

package com.perl5.lang.perl.extensions.packageprocessor;

import com.intellij.openapi.diagnostic.Logger;
import com.perl5.lang.perl.internals.PerlVersion;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Set;


public final class PerlVersionProcessor implements PerlPackageProcessor, PerlFeaturesProvider {
  private static final Logger LOG = Logger.getInstance(PerlVersionProcessor.class);
  private static final PerlVersionProcessor INSTANCE = new PerlVersionProcessor();

  private PerlVersionProcessor() {
  }

  @Override
  public boolean isPragma() {
    return false;
  }

  @Override
  public void addExports(@NotNull PerlUseStatementElement useStatement,
                         @NotNull Set<? super String> export,
                         @NotNull Set<? super String> exportOk) {

  }

  @Override
  public @NotNull List<PerlExportDescriptor> getImports(@NotNull PerlUseStatementElement useStatement) {
    return Collections.emptyList();
  }

  public static PerlVersionProcessor getInstance() {
    return INSTANCE;
  }

  @Override
  public boolean isWarningsEnabled(@NotNull PerlUseStatementElement useStatement) {
    return !useStatement.getVersionElement().getPerlVersion().lesserThan(PerlVersion.V5_36);
  }

  @Override
  public boolean isStrictEnabled(@NotNull PerlUseStatementElement useStatement) {
    return !useStatement.getVersionElement().getPerlVersion().lesserThan(PerlVersion.V5_12);
  }
}
