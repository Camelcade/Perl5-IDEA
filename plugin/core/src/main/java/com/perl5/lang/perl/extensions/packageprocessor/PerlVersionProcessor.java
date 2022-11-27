/*
 * Copyright 2015-2022 Alexandr Evstigneev
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
import com.perl5.lang.perl.internals.PerlFeaturesTable;
import com.perl5.lang.perl.internals.PerlVersion;
import com.perl5.lang.perl.psi.PerlVersionElement;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElementBase;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Set;


public class PerlVersionProcessor implements PerlPackageProcessor, PerlFeaturesProvider, PerlWarningsProvider {
  private static final Logger LOG = Logger.getInstance(PerlVersionProcessor.class);
  protected static final PerlVersionProcessor INSTANCE = new PerlVersionProcessor();

  protected PerlVersionProcessor() {
  }

  @Override
  public boolean isPragma() {
    return false;
  }

  @Override
  public void addExports(@NotNull PerlUseStatementElement useStatement, @NotNull Set<String> export, @NotNull Set<String> exportOk) {

  }

  @Override
  public @NotNull List<PerlExportDescriptor> getImports(@NotNull PerlUseStatementElement useStatement) {
    return Collections.emptyList();
  }

  @Override
  public PerlFeaturesTable getFeaturesTable(PerlUseStatementElement useStatement, PerlFeaturesTable currentFeaturesTable) {
    // fixme implement modification
    return currentFeaturesTable == null ? new PerlFeaturesTable() : currentFeaturesTable.clone();
  }

  public static PerlVersionProcessor getProcessor(@NotNull PerlUseStatementElementBase useStatement) {
    PerlVersionElement versionElement = getVersionElement(useStatement);

    if (versionElement.getPerlVersion().lesserThan(PerlVersion.V5_12)) {
      return INSTANCE;
    }

    return PerlVersionProcessor512.INSTANCE;
  }

  private static @NotNull PerlVersionElement getVersionElement(@NotNull PerlUseStatementElementBase useStatement) {
    PerlVersionElement versionElement = useStatement.getVersionElement();
    LOG.assertTrue(versionElement != null);
    return versionElement;
  }

  @Override
  public boolean isWarningsEnabled(@NotNull PerlUseStatementElement useStatement) {
    return !useStatement.getVersionElement().getPerlVersion().lesserThan(PerlVersion.V5_36);
  }
}
