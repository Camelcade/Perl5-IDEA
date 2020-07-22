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

package com.perl5.lang.perl.extensions.packageprocessor;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.search.GlobalSearchScope;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlScopesUtil;
import com.perl5.lang.perl.util.PerlTimeLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;


public abstract class PerlPackageProcessorBase implements PerlPackageProcessor {
  protected static final Logger LOG = Logger.getInstance(PerlPackageProcessorBase.class);
  @Override
  public boolean isPragma() {
    return false;
  }

  @Override
  public void addExports(@NotNull PerlUseStatementElement useStatement, @NotNull Set<String> export, @NotNull Set<String> exportOk) {
    String packageName = useStatement.getPackageName();

    if (StringUtil.isEmpty(packageName)) {
      return;
    }

    // fixme handle tags
    GlobalSearchScope scope = PerlScopesUtil.allScopeWithoutCurrentWithAst(useStatement);
    for (PerlNamespaceDefinitionElement namespaceDefinition : PerlPackageUtil
      .getNamespaceDefinitions(useStatement.getProject(), scope, packageName)) {
      export.addAll(namespaceDefinition.getEXPORT());
      exportOk.addAll(namespaceDefinition.getEXPORT_OK());
    }
    exportOk.addAll(export);
  }


  @Override
  public @NotNull List<PerlExportDescriptor> getImports(@NotNull PerlUseStatementElement useStatement) {
    String packageName = useStatement.getPackageName();
    if (packageName == null) {
      return Collections.emptyList();
    }
    PerlTimeLogger logger = PerlTimeLogger.create(LOG);
    List<PerlExportDescriptor> result = new ArrayList<>();
    List<String> importParameters = getImportParameters(useStatement);
    Set<String> exportNames = new HashSet<>();
    Set<String> exportOkNames = new HashSet<>();

    addExports(useStatement, exportNames, exportOkNames);

    if (importParameters == null) {
      exportNames.forEach(name -> result.add(createDescriptor(packageName, name)));
    }
    else {
      importParameters.stream()
        .filter(exportOkNames::contains)
        .forEach(name -> result.add(createDescriptor(packageName, name)));
    }

    logger.debug("Collected imports for ", packageName);
    return result;
  }

  protected @NotNull PerlExportDescriptor createDescriptor(@NotNull String packageName, @NotNull String name) {
    return PerlExportDescriptor.create(packageName, name);
  }

  /**
   * Returns import parameters. Here you may filter some meaningless params
   *
   * @param useStatement use statement we are processing
   * @return list of imported parameters or null if use has no parameters
   */
  protected @Nullable List<String> getImportParameters(@NotNull PerlUseStatementElement useStatement) {
    return useStatement.getImportParameters();
  }
}
