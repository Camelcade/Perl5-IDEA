/*
 * Copyright 2015 Alexandr Evstigneev
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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlUseStatement;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.util.processors.PerlInternalIndexKeysProcessor;
import com.perl5.lang.perl.util.processors.PerlNamespaceEntityProcessor;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collection;
import java.util.Set;

/**
 * Created by hurricup on 27.05.2015.
 * Misc helper methods
 */
public class PerlUtil implements PerlElementTypes {
  private static final TokenSet LIST_CONTEXT_ELEMENTS = TokenSet.create(
    ARRAY_SLICE,
    ARRAY_CAST_EXPR,
    HASH_SLICE,
    ARRAY_VARIABLE,
    HASH_VARIABLE,
    HASH_CAST_EXPR,
    GREP_EXPR,
    MAP_EXPR,
    SORT_EXPR
  );

  @Nullable
  public static VirtualFile getFileClassRoot(PsiFile psiFile) {
    return getFileClassRoot(psiFile.getProject(), psiFile.getVirtualFile());
  }

  /**
   * Searches for innermost source root for a file
   *
   * @param project project to search in
   * @param file    containing file
   * @return innermost root
   */
  @Nullable
  public static VirtualFile getFileClassRoot(Project project, VirtualFile file) {
    VirtualFile result = null;
    for (VirtualFile classRoot : ProjectRootManager.getInstance(project).orderEntries().getClassesRoots()) {
      if (VfsUtil.isAncestor(classRoot, file, false) && (result == null || VfsUtil.isAncestor(result, classRoot, true))) {
        result = classRoot;
      }
    }

    return result;
  }

  /**
   * Searches for innermost source root for a file by it's absolute path
   *
   * @param project  module to search in
   * @param filePath containing filename
   * @return innermost root
   */
  @Nullable
  public static VirtualFile getFileClassRoot(Project project, String filePath) {
    File file = new File(filePath);
    VirtualFile result = null;

    for (VirtualFile classRoot : ProjectRootManager.getInstance(project).orderEntries().getClassesRoots()) {
      File sourceRootFile = new File(classRoot.getPath());
      if (VfsUtil.isAncestor(sourceRootFile, file, false) && (result == null || VfsUtil.isAncestor(result, classRoot, true))) {
        result = classRoot;
      }
    }

    return result;
  }

  public static Collection<String> getIndexKeysWithoutInternals(StubIndexKey<String, ?> key, Project project) {
    final Set<String> result = new THashSet<String>();

    // safe for getElements
    StubIndex.getInstance().processAllKeys(key, project, new
      PerlInternalIndexKeysProcessor() {
        @Override
        public boolean process(String name) {
          if (super.process(name)) {
            result.add(name);
          }
          return true;
        }
      });

    return result;
  }

  /**
   * Processing use statements in the namespace or file and processing all imports found
   *
   * @param rootElement Root element to start searching from
   */
  public static void processImportedEntities(
    @NotNull PsiElement rootElement,
    @NotNull PerlNamespaceEntityProcessor<PerlExportDescriptor> processor
  ) {
    for (PsiElement element : PerlPsiUtil.collectUseStatements(rootElement)) {
      PerlUseStatement useStatement = (PerlUseStatement)element;
      String packageName = useStatement.getPackageName();

      if (packageName != null) {
        for (PerlExportDescriptor entry : useStatement.getPackageProcessor().getImports(useStatement)) {
          processor.process(packageName, entry);
        }
      }
    }
  }

  /**
   * Returns context type for psi element
   *
   * @return like scalar for scalars, strings and so on, lists for arrays, hashes, can be null if we not sure
   */
  @Nullable
  public static PerlContextType getElementContextType(PsiElement element) {
    if (LIST_CONTEXT_ELEMENTS.contains(PsiUtilCore.getElementType(element))) {
      return PerlContextType.LIST;
    }
    return PerlContextType.SCALAR;
  }
}
