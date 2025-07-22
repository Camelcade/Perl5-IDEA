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

package com.perl5.lang.htmlmason;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.util.Processor;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonSettings;
import com.perl5.lang.htmlmason.parser.psi.*;
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonFileImpl;
import com.perl5.lang.htmlmason.parser.stubs.HTMLMasonFlagsStubIndex;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import com.perl5.lang.perl.util.PerlSubUtilCore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.intellij.openapi.vfs.VfsUtilCore.VFS_SEPARATOR;
import static com.intellij.openapi.vfs.VfsUtilCore.VFS_SEPARATOR_CHAR;


public final class HTMLMasonUtil {
  private HTMLMasonUtil() {
  }

  public static @Nullable VirtualFile getComponentRoot(@NotNull Project project, @Nullable VirtualFile file) {
    return MasonCoreUtil.getComponentRoot(HTMLMasonSettings.getInstance(project), file);
  }

  public static List<PerlSubArgument> getArgumentsList(HTMLMasonParametrizedEntity entity) {
    List<PerlSubArgument> result = new ArrayList<>();

    for (HTMLMasonCompositeElement argsBlock : entity.getArgsBlocks()) {
      result.addAll(((HTMLMasonArgsBlock)argsBlock).getArgumentsList());
    }

    return result;
  }

  public static String getArgumentsListAsString(HTMLMasonParametrizedEntity entity) {
    return PerlSubUtilCore.getArgumentsListAsString(getArgumentsList(entity));
  }

  /**
   * @return absolute path relative to the components root
   */
  public static @NlsSafe @Nullable String getAbsoluteComponentPath(@NotNull HTMLMasonFileImpl masonFile) {

    VirtualFile componentFile = getComponentVirtualFile(masonFile);
    VirtualFile componentRoot = getComponentRoot(masonFile);

    if (componentFile != null && componentRoot != null) {
      return VFS_SEPARATOR + VfsUtilCore.getRelativePath(componentFile, componentRoot);
    }
    return null;
  }

  public static @Nullable HTMLMasonFileImpl getParentComponent(@NotNull HTMLMasonFileImpl masonFile) {
    String parentComponentPath = getParentComponentPath(masonFile);
    HTMLMasonSettings settings = HTMLMasonSettings.getInstance(masonFile.getProject());
    VirtualFile parentFile = null;

    if (parentComponentPath == null) {
      VirtualFile containingFile = getComponentVirtualFile(masonFile);
      if (containingFile != null) {
        VirtualFile startDir = containingFile.getParent();
        if (StringUtil.equals(containingFile.getName(), settings.autoHandlerName)) {
          startDir = startDir.getParent();
        }

        VirtualFile componentRoot = getComponentRoot(masonFile.getProject(), startDir);
        if (componentRoot != null) {
          while (VfsUtilCore.isAncestor(componentRoot, startDir, false)) {
            if ((parentFile = startDir.findFileByRelativePath(settings.autoHandlerName)) != null) {
              break;
            }
            startDir = startDir.getParent();
          }
        }
      }
    }
    else if (!StringUtil.equals(parentComponentPath, HTMLMasonFlagsStatement.UNDEF_RESULT)) {
      if (StringUtil.startsWith(parentComponentPath, "/")) {
        parentComponentPath = parentComponentPath.substring(1);
        for (VirtualFile root : PerlProjectManager.getInstance(settings.getProject()).getModulesRootsOfType(settings.getSourceRootType())) {
          if ((parentFile = root.findFileByRelativePath(parentComponentPath)) != null) {
            break;
          }
        }
      }
      else {
        VirtualFile containingVirtualFile = getComponentVirtualFile(masonFile);
        if (containingVirtualFile != null) {
          VirtualFile containingDir = containingVirtualFile.getParent();
          if (containingDir != null) {
            parentFile = containingDir.findFileByRelativePath(parentComponentPath);
          }
        }
      }
    }

    if (parentFile != null) {
      PsiFile file = PsiManager.getInstance(masonFile.getProject()).findFile(parentFile);
      if (file instanceof HTMLMasonFileImpl htmlMasonFile) {
        return htmlMasonFile;
      }
    }

    return null;
  }

  public static @Nullable String getParentComponentPath(@NotNull HTMLMasonFileImpl masonFile) {
    HTMLMasonFlagsStatement statement = masonFile.getFlagsStatement();
    return statement == null ? null : statement.getParentComponentPath();
  }

  public static @NotNull List<HTMLMasonFileImpl> getChildComponents(@NotNull HTMLMasonFileImpl masonFile) {
    VirtualFile containingFile = getComponentVirtualFile(masonFile);

    if (containingFile == null) {
      return Collections.emptyList();
    }

    VirtualFile componentRoot = getComponentRoot(masonFile);

    if (componentRoot == null) {
      return Collections.emptyList();
    }

    final List<HTMLMasonFileImpl> result = new ArrayList<>();
    final String relativePath = VFS_SEPARATOR + VfsUtilCore.getRelativePath(containingFile, componentRoot);
    final Project project = masonFile.getProject();
    final GlobalSearchScope scope = GlobalSearchScope.allScope(project);
    final HTMLMasonFileImpl currentFile = masonFile;
    HTMLMasonSettings settings = HTMLMasonSettings.getInstance(project);

    // indexed children
    for (String parentPath : StubIndex.getInstance().getAllKeys(HTMLMasonFlagsStubIndex.KEY, project)) {
      boolean isEquals = StringUtil.equals(relativePath, parentPath);
      boolean isRelative = parentPath.isEmpty() || parentPath.charAt(0) != VFS_SEPARATOR_CHAR;

      for (HTMLMasonFlagsStatement statement : StubIndex.getElements(
        HTMLMasonFlagsStubIndex.KEY, parentPath, project, scope, HTMLMasonFlagsStatement.class)) {
        PsiFile file = statement.getContainingFile();
        if (file instanceof HTMLMasonFileImpl htmlMasonFile &&
            (isEquals || isRelative && currentFile.equals(getParentComponent(htmlMasonFile)))) {
          result.add(htmlMasonFile);
        }
      }
    }

    if (StringUtil.equals(containingFile.getName(), settings.autoHandlerName)) {
      collectAutoHandledFiles(masonFile, PsiManager.getInstance(project), containingFile.getParent(), result, settings.autoHandlerName,
                              null);
    }

    return result;
  }

  public static void collectAutoHandledFiles(@NotNull HTMLMasonFileImpl masonFile,
                                             @NotNull PsiManager manager,
                                             @Nullable VirtualFile dir,
                                             @NotNull List<? super HTMLMasonFileImpl> result,
                                             @NotNull String autoHandlerName,
                                             @Nullable Set<? super VirtualFile> recursionMap) {
    if (dir == null) {
      return;
    }
    if (recursionMap == null) {
      recursionMap = new HashSet<>();
    }
    else {
      VirtualFile autoHandlerVirtualFile = dir.findChild(autoHandlerName);
      if (autoHandlerVirtualFile != null) {
        PsiFile autoHandlerPsiFile = manager.findFile(autoHandlerVirtualFile);
        if (autoHandlerPsiFile instanceof HTMLMasonFileImpl htmlMasonFile && masonFile.equals(getParentComponent(htmlMasonFile))) {
          result.add(htmlMasonFile);
        }
        return;
      }
    }

    recursionMap.add(dir);

    for (VirtualFile file : dir.getChildren()) {
      if (file.isDirectory() && !recursionMap.contains(file)) {
        collectAutoHandledFiles(masonFile, manager, file, result, autoHandlerName, recursionMap);
      }
      else if (!StringUtil.equals(file.getName(), autoHandlerName)) {
        PsiFile psiFile = manager.findFile(file);
        if (psiFile instanceof HTMLMasonFileImpl htmlMasonFile && masonFile.equals(getParentComponent(htmlMasonFile))) {
          result.add(htmlMasonFile);
        }
      }
    }
  }

  /**
   * @return absolute containing dir path relative to the components root
   */
  public static @NlsSafe @Nullable String getAbsoluteComponentContainerPath(@NotNull HTMLMasonFileImpl masonFile) {
    VirtualFile componentFile = getComponentVirtualFile(masonFile);
    VirtualFile componentRoot = getComponentRoot(masonFile);

    if (componentFile != null && componentRoot != null) {
      return VFS_SEPARATOR + VfsUtilCore.getRelativePath(componentFile.getParent(), componentRoot);
    }
    return null;
  }

  public static @Nullable VirtualFile getComponentRoot(@NotNull HTMLMasonFileImpl masonFile) {
    return getComponentRoot(masonFile.getProject(), getComponentVirtualFile(masonFile));
  }

  public static VirtualFile getComponentVirtualFile(@NotNull HTMLMasonFileImpl masonFile) {
    return MasonCoreUtil.getContainingVirtualFile(masonFile);
  }

  /**
   * Recursively looking for method in child components
   *
   * @param name method name
   * @return list of child components
   */
  public static @NotNull List<HTMLMasonMethodDefinition> findMethodDefinitionByNameInChildComponents(@NotNull HTMLMasonFileImpl masonFile,
                                                                                                     String name) {
    List<HTMLMasonMethodDefinition> result = new ArrayList<>();
    Set<HTMLMasonFileImpl> recursionSet = new HashSet<>();

    collectMethodDefinitionByNameInChildComponents(masonFile, name, result, recursionSet);

    return result;
  }

  public static void collectMethodDefinitionByNameInChildComponents(@NotNull HTMLMasonFileImpl masonFile,
                                                                    String name,
                                                                    List<HTMLMasonMethodDefinition> result,
                                                                    Set<HTMLMasonFileImpl> recursionSet) {
    for (HTMLMasonFileImpl childComponent : getChildComponents(masonFile)) {
      if (!recursionSet.contains(childComponent)) {
        recursionSet.add(childComponent);
        HTMLMasonMethodDefinition methodDefinition = childComponent.getMethodDefinitionByName(name);
        if (methodDefinition != null) {
          result.add(methodDefinition);
        }
        else {
          collectMethodDefinitionByNameInChildComponents(masonFile, name, result, recursionSet);
        }
      }
    }
  }

  /**
   * Recursively looking for method in parent components
   *
   * @param name method name
   * @return method definition or null
   */
  public static @Nullable HTMLMasonMethodDefinition findMethodDefinitionByNameInParents(@NotNull HTMLMasonFileImpl masonFile, String name) {
    HTMLMasonFileImpl parentComponent = getParentComponent(masonFile);
    return parentComponent == null ? null : findMethodDefinitionByNameInThisOrParents(parentComponent, name);
  }

  public static boolean processMethodDefinitionsInThisOrParents(@NotNull HTMLMasonFileImpl masonFile,
                                                                Processor<HTMLMasonMethodDefinition> processor,
                                                                Set<HTMLMasonFileImpl> recursionSet) {
    if (recursionSet.contains(masonFile)) {
      return false;
    }
    recursionSet.add(masonFile);

    if (!masonFile.processMethodDefinitions(processor)) {
      return false;
    }

    HTMLMasonFileImpl parentComponent = getParentComponent(masonFile);

    return parentComponent != null && processMethodDefinitionsInThisOrParents(parentComponent, processor, recursionSet);
  }

  @SuppressWarnings("UnusedReturnValue")
  public static boolean processMethodDefinitionsInThisOrParents(@NotNull HTMLMasonFileImpl masonFile,
                                                                Processor<HTMLMasonMethodDefinition> processor) {
    return processMethodDefinitionsInThisOrParents(masonFile, processor, new HashSet<HTMLMasonFileImpl>());
  }

  /**
   * Recursively looking for method in current or parent components
   *
   * @param name method name
   * @return method definition or null
   */
  public static @Nullable HTMLMasonMethodDefinition findMethodDefinitionByNameInThisOrParents(@NotNull HTMLMasonFileImpl masonFile,
                                                                                              String name) {
    HTMLMasonFileImpl.HTMLMasonMethodDefinitionSeeker seeker = new HTMLMasonFileImpl.HTMLMasonMethodDefinitionSeeker(name);
    processMethodDefinitionsInThisOrParents(masonFile, seeker);
    return seeker.getResult();
  }
}
