/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.perl.util;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.Processor;
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import com.perl5.lang.perl.psi.references.PerlNamespaceFileReference;
import com.perl5.lang.perl.psi.references.PerlNamespaceReference;
import com.perl5.lang.perl.psi.stubs.imports.PerlUseStatementsIndex;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlLightNamespaceIndex;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceIndex;
import com.perl5.lang.perl.util.processors.PerlNamespaceEntityProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class PerlNamespaceUtil {
  public static final Logger LOG_STUBS = Logger.getInstance("#perl5.namespce.stubs");

  private PerlNamespaceUtil() {
  }

  public static void getLinearISA(@NotNull PerlNamespaceDefinitionElement namespaceDefinitionElement,
                                  @NotNull Set<String> recursionMap,
                                  @NotNull List<String> result) {
  }

  public static @NotNull Set<PerlExportDescriptor> getExportDescriptors(@NotNull Project project,
                                                                        @NotNull GlobalSearchScope searchScope,
                                                                        @NotNull String namespaceName) {
    Set<PerlExportDescriptor> result = new HashSet<>();
    processExportDescriptors(
      project, searchScope, namespaceName, (__, it) -> {
        result.add(it);
        return true;
      });
    return result;
  }

  public static boolean processExportDescriptors(@NotNull Project project,
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

  public static @NotNull List<PerlNamespaceDefinitionElement> collectNamespaceDefinitions(@NotNull Project project,
                                                                                          @NotNull List<String> packageNames) {
    ArrayList<PerlNamespaceDefinitionElement> namespaceDefinitions = new ArrayList<>();
    for (String packageName : packageNames) {
      Collection<PerlNamespaceDefinitionElement> list =
        getNamespaceDefinitions(project, GlobalSearchScope.projectScope(project), packageName);

      if (list.isEmpty()) {
        list = getNamespaceDefinitions(project, GlobalSearchScope.allScope(project), packageName);
      }

      namespaceDefinitions.addAll(list);
    }
    return namespaceDefinitions;
  }

  /**
   * Searching project files for namespace definitions by specific package name
   *
   * @see #processNamespaces(String, Project, GlobalSearchScope, Processor)
   */
  public static Collection<PerlNamespaceDefinitionElement> getNamespaceDefinitions(@NotNull Project project,
                                                                                   @NotNull GlobalSearchScope scope,
                                                                                   @NotNull String canonicalPackageName) {
    List<PerlNamespaceDefinitionElement> result = new ArrayList<>();
    processNamespaces(canonicalPackageName, project, scope, result::add);
    return result;
  }

  /**
   * Processes all global packages names with specific processor
   *
   * @param scope     search scope
   * @param processor string processor for suitable strings
   * @return collection of constants names
   */
  @SuppressWarnings("UnusedReturnValue")
  public static boolean processNamespaces(@NotNull String packageName,
                                          @NotNull Project project,
                                          @NotNull GlobalSearchScope scope,
                                          @NotNull Processor<? super PerlNamespaceDefinitionElement> processor) {
    return PerlNamespaceIndex.getInstance().processElements(project, packageName, scope, processor) &&
           PerlLightNamespaceIndex.getInstance().processLightElements(project, packageName, scope, processor);
  }

  /**
   * Returns list of files suitable for this namespace, works only if namespace is in use or require statement
   *
   * @return list of PerlNameSpaceDefitions
   */
  public static @NotNull List<PerlFileImpl> getNamespaceFiles(@NotNull PerlNamespaceElement namespaceElement) {
    List<PerlFileImpl> namespaceFiles = new ArrayList<>();

    PsiReference[] references = namespaceElement.getReferences();

    for (PsiReference reference : references) {
      if (reference instanceof PerlNamespaceFileReference namespaceFileReference) {
        ResolveResult[] results = namespaceFileReference.multiResolve(false);

        for (ResolveResult result : results) {
          PsiElement targetElement = result.getElement();
          assert targetElement != null;

          if (targetElement instanceof PerlFileImpl perlFile) {
            namespaceFiles.add(perlFile);
          }
        }
      }
    }
    return namespaceFiles;
  }

  /**
   * Returns list of definitions of current namespace
   *
   * @return list of PerlNameSpaceDefitions
   */
  public static @NotNull List<PerlNamespaceDefinitionElement> getNamespaceDefinitions(@NotNull PerlNamespaceElement namespaceElement) {
    List<PerlNamespaceDefinitionElement> namespaceDefinitions = new ArrayList<>();

    PsiReference[] references = namespaceElement.getReferences();

    for (PsiReference reference : references) {
      if (reference instanceof PerlNamespaceReference namespaceReference) {
        ResolveResult[] results = namespaceReference.multiResolve(false);

        for (ResolveResult result : results) {
          PsiElement targetElement = result.getElement();
          assert targetElement != null;
          assert targetElement instanceof PerlNamespaceDefinitionElement;

          namespaceDefinitions.add((PerlNamespaceDefinitionElement)targetElement);
        }
      }
    }
    return namespaceDefinitions;
  }
}
