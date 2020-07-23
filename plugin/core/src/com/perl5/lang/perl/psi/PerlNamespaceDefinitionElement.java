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

package com.perl5.lang.perl.psi;

import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiInvalidElementAccessException;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.Processor;
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import com.perl5.lang.perl.psi.stubs.imports.PerlUseStatementsIndex;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlTimeLogger;
import com.perl5.lang.perl.util.processors.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface PerlNamespaceDefinitionElement extends PerlNamespaceDefinition, PsiNamedElement, NavigationItem {
  Logger LOG_STUBS = Logger.getInstance("#perl5.namespce.stubs");
  Logger LOG_AST = Logger.getInstance("#perl5.namespce.ast");

  @Override
  @NotNull
  @Contract(pure = true)
  Project getProject() throws PsiInvalidElementAccessException;

  default boolean processExportDescriptorsWithAst(@NotNull PerlNamespaceEntityProcessor<PerlExportDescriptor> processor) {
    PsiFile containingFile = getContainingFile();
    if (!(containingFile instanceof PerlFile)) {
      return true;
    }

    String namespaceName = getNamespaceName();
    if (StringUtil.isEmpty(namespaceName)) {
      return true;
    }

    PerlTimeLogger logger = PerlTimeLogger.create(LOG_AST);
    PerlTimeLogger.Counter useStatementsCounter = logger.getCounter("use");
    PerlTimeLogger.Counter exportsCounter = logger.getCounter("export");

    Processor<PerlUseStatementElement> useStatementsProcessor =
      createUseStatementsProcessor(processor, useStatementsCounter, exportsCounter);

    boolean processingResult = true;
    PerlFileData perlFileData = ((PerlFile)containingFile).getPerlFileData();
    for (PerlUseStatementElement useStatement : perlFileData.getUseStatements()) {
      if (namespaceName.equals(PerlPackageUtil.getContextNamespaceName(useStatement)) && !useStatementsProcessor.process(useStatement)) {
        processingResult = false;
        break;
      }
    }

    logger.debug("AST processed: ",
                 useStatementsCounter.get(), " use statements; ",
                 exportsCounter.get(), " exports");

    return processingResult;
  }

  default boolean processExportDescriptors(@NotNull PerlNamespaceEntityProcessor<PerlExportDescriptor> processor) {
    String namespaceName = getNamespaceName();
    if (StringUtil.isEmpty(namespaceName)) {
      return true;
    }
    return processExportDescriptors(
      getProject(), GlobalSearchScope.fileScope(getContainingFile().getOriginalFile()), namespaceName, processor);
  }

  default @NotNull List<PerlExportDescriptor> collectImportDescriptors(@NotNull PerlImportsCollector collector) {
    processExportDescriptors(collector);
    return collector.getResult();
  }

  default @NotNull List<PerlExportDescriptor> getImportedSubsDescriptors() {
    return collectImportDescriptors(new PerlSubImportsCollector());
  }

  default @NotNull List<PerlExportDescriptor> getImportedScalarDescriptors() {
    return collectImportDescriptors(new PerlScalarImportsCollector());
  }

  default @NotNull List<PerlExportDescriptor> getImportedArrayDescriptors() {
    return collectImportDescriptors(new PerlArrayImportsCollector());
  }

  default @NotNull List<PerlExportDescriptor> getImportedHashDescriptors() {
    return collectImportDescriptors(new PerlHashImportsCollector());
  }

  default List<PerlNamespaceDefinitionElement> getParentNamespaceDefinitions() {
    return PerlPackageUtil.collectNamespaceDefinitions(getProject(), getParentNamespacesNames());
  }

  default @NotNull List<PerlNamespaceDefinitionElement> getChildNamespaceDefinitions() {
    return PerlPackageUtil.getChildNamespaces(getProject(), getNamespaceName());
  }

  default void getLinearISA(@NotNull Set<String> recursionMap, @NotNull List<String> result) {
    getMro().getLinearISA(getProject(), getParentNamespaceDefinitions(), recursionMap, result);
  }

  static @NotNull Set<PerlExportDescriptor> getExportDescriptors(@NotNull Project project,
                                                                 @NotNull GlobalSearchScope searchScope,
                                                                 @NotNull String namespaceName) {
    Set<PerlExportDescriptor> result = new HashSet<>();
    PerlNamespaceDefinitionElement.processExportDescriptors(
      project, searchScope, namespaceName, (__, it) -> {
        result.add(it);
        return true;
      });
    return result;
  }

  static boolean processExportDescriptors(@NotNull Project project,
                                          @NotNull GlobalSearchScope searchScope,
                                          @NotNull String namespaceName,
                                          @NotNull PerlNamespaceEntityProcessor<? super PerlExportDescriptor> processor) {
    PerlTimeLogger logger = PerlTimeLogger.create(LOG_STUBS);
    PerlTimeLogger.Counter useStatementsCounter = logger.getCounter("use");
    PerlTimeLogger.Counter exportsCounter = logger.getCounter("export");

    boolean result = PerlUseStatementsIndex.getInstance().processElements(
      project, namespaceName, searchScope, createUseStatementsProcessor(processor, useStatementsCounter, exportsCounter));

    logger.debug("Processed: ", useStatementsCounter.get(), " use statements; ",
                 exportsCounter.get(), " exports");

    return result;
  }

  private static @NotNull Processor<PerlUseStatementElement> createUseStatementsProcessor(@NotNull PerlNamespaceEntityProcessor<? super PerlExportDescriptor> processor,
                                                                                          @NotNull PerlTimeLogger.Counter useStatementsCounter,
                                                                                          @NotNull PerlTimeLogger.Counter exportsCounter) {
    return it -> {
      useStatementsCounter.inc();
      String packageName = it.getPackageName();

      if (packageName == null) {
        return true;
      }
      for (PerlExportDescriptor entry : it.getPackageProcessor().getImports(it)) {
        exportsCounter.inc();
        if (!processor.process(packageName, entry)) {
          return false;
        }
      }
      return true;
    };
  }
}
