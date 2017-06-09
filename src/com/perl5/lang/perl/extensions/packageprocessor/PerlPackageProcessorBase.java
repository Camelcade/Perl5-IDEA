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

import com.intellij.openapi.util.text.StringUtil;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.PerlUseStatement;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by hurricup on 18.08.2015.
 */
public abstract class PerlPackageProcessorBase implements PerlPackageProcessor {
  @Override
  public boolean isPragma() {
    return false;
  }

  public void addExports(@NotNull PerlUseStatement useStatement, @NotNull Set<String> export, @NotNull Set<String> exportOk) {
    String packageName = useStatement.getPackageName();

    if (StringUtil.isEmpty(packageName)) {
      return;
    }

    // fixme handle tags
    for (PerlNamespaceDefinitionElement namespaceDefinition : PerlPackageUtil
      .getNamespaceDefinitions(useStatement.getProject(), packageName)) {
      export.addAll(namespaceDefinition.getEXPORT());
      exportOk.addAll(namespaceDefinition.getEXPORT_OK());
    }
    exportOk.addAll(export);
  }


  @Override
  @NotNull
  public List<PerlExportDescriptor> getImports(@NotNull PerlUseStatement useStatement) {
    List<PerlExportDescriptor> result = new ArrayList<PerlExportDescriptor>();
    String packageName = useStatement.getPackageName();
    if (packageName != null) {
      List<String> parameters = useStatement.getImportParameters();
      //			System.err.println("Import parameters for " + packageName + " are " + parameters);
      Set<String> exportNames = new HashSet<String>();
      Set<String> exportOkNames = new HashSet<String>();

      addExports(useStatement, exportNames, exportOkNames);

      if (parameters == null)    // default import
      {
        for (String item : exportNames) {
          result.add(new PerlExportDescriptor(item, packageName));
        }
      }
      else {
        for (String parameter : parameters) {
          if (exportOkNames.contains(parameter)) {
            result.add(new PerlExportDescriptor(parameter, packageName));
          }
        }
      }
    }

    //		System.err.println("Imported from " + packageName + ": " + result);

    return result;
  }
}
