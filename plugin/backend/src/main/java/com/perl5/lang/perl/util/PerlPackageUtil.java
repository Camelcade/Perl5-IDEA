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

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.PairProcessor;
import com.intellij.util.Processor;
import com.perl5.lang.perl.extensions.packageprocessor.PerlLibProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessor;
import com.perl5.lang.perl.fileTypes.PerlFileTypePackage;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValueResolver;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.idea.project.PerlDirectoryIndex;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.internals.PerlVersion;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import com.perl5.lang.perl.psi.stubs.globs.PerlGlobNamespaceStubIndex;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlLightNamespaceDescendantsIndex;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlLightNamespaceIndex;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDescendantsIndex;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceIndex;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlCallableNamesIndex;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlLightCallableNamesIndex;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

import java.io.File;
import java.util.*;
import java.util.function.Function;


public final class PerlPackageUtil implements PerlElementTypes {

  private PerlPackageUtil() {
  }

  public static @Nullable PerlNamespaceDefinitionElement getNamespaceContainerForElement(@Nullable PsiElement element) {
    if (element == null) {
      return null;
    }

    PerlNamespaceDefinitionElement namespaceContainer = PsiTreeUtil.getParentOfType(element, PerlNamespaceDefinitionElement.class);

    if (namespaceContainer instanceof PerlFileImpl) {
      PsiElement contextParent = namespaceContainer.getContext();
      if (contextParent != null && !contextParent.equals(namespaceContainer.getParent())) {
        return getNamespaceContainerForElement(contextParent);
      }
    }
    return namespaceContainer;
  }

  /**
   * Returns list of defined package names
   *
   * @param project project to search in
   * @return collection of package names
   */
  public static Collection<String> getKnownNamespaceNames(Project project) {
    Collection<String> keys = PerlNamespaceIndex.getInstance().getAllNames(project);
    keys.addAll(PerlLightNamespaceIndex.getInstance().getAllNames(project));
    return keys;
  }

  public static @NotNull Collection<String> getKnownNamespaceNames(@NotNull GlobalSearchScope scope) {
    Collection<String> keys = PerlNamespaceIndex.getInstance().getAllNames(scope);
    keys.addAll(PerlLightNamespaceIndex.getInstance().getAllNames(scope));
    return keys;
  }

  @SuppressWarnings("UnusedReturnValue")
  public static boolean processChildNamespaces(@NotNull String parentPackageName,
                                               @NotNull Project project,
                                               @NotNull GlobalSearchScope scope,
                                               @NotNull Processor<? super PerlNamespaceDefinitionElement> processor) {
    return PerlNamespaceDescendantsIndex.getInstance().processElements(project, parentPackageName, scope, processor) &&
           PerlLightNamespaceDescendantsIndex.getInstance().processLightElements(project, parentPackageName, scope, processor);
  }


  /**
   * Returns list of derived classes
   *
   * @param project project to search in
   * @return collection of definitions
   */
  public static @NotNull List<PerlNamespaceDefinitionElement> getChildNamespaces(@NotNull Project project,
                                                                                 @Nullable String packageName) {
    if (StringUtil.isEmpty(packageName)) {
      return Collections.emptyList();
    }
    List<PerlNamespaceDefinitionElement> list = getChildNamespaces(project, packageName, GlobalSearchScope.projectScope(project));
    if (list.isEmpty()) {
      list = getChildNamespaces(project, packageName, GlobalSearchScope.allScope(project));
    }
    return list;
  }

  public static @NotNull List<PerlNamespaceDefinitionElement> getChildNamespaces(@NotNull Project project,
                                                                                 @NotNull String packageName,
                                                                                 @NotNull GlobalSearchScope scope) {
    ArrayList<PerlNamespaceDefinitionElement> elements = new ArrayList<>();
    processChildNamespaces(packageName, project, scope, elements::add);
    return elements;
  }

  /**
   * Translates package relative name to the package name Foo/Bar.pm => Foo::Bar
   *
   * @param packagePath package relative path
   * @return canonical package name
   */
  public static String getPackageNameByPath(final String packagePath) {
    String result = PerlPackageUtilCore.PATH_TO_PACKAGE_NAME_MAP.get(packagePath);

    if (result == null) {
      String path = packagePath.replace("\\", "/");
      result = PerlPackageUtilCore.getCanonicalNamespaceName(
        StringUtil.join(path.replaceFirst("\\.pm$", "").split("/"), PerlPackageUtilCore.NAMESPACE_SEPARATOR));
      PerlPackageUtilCore.PATH_TO_PACKAGE_NAME_MAP.put(packagePath, result);
    }
    return result;
  }

  public static boolean processPackageFilesForPsiElement(@NotNull PsiElement element,
                                                         @NotNull PairProcessor<? super String, ? super VirtualFile> processor) {
    return processIncFilesForPsiElement(
      element,
      (file, classRoot) -> {
        String relativePath = VfsUtilCore.getRelativePath(file, classRoot);
        String packageName = getPackageNameByPath(relativePath);
        return processor.process(packageName, file);
      },
      PerlFileTypePackage.INSTANCE)
      ;
  }

  @SuppressWarnings("UnusedReturnValue")
  public static boolean processIncFilesForPsiElement(@NotNull PsiElement element,
                                                     @NotNull ClassRootVirtualFileProcessor processor,
                                                     @NotNull FileType fileType) {
    List<VirtualFile> incDirsForPsiElement = getIncDirsForPsiElement(element);
    Project project = element.getProject();
    Function<VirtualFile, VirtualFile> rootsComputator = PerlDirectoryIndex.getInstance(project).createRootComputator(incDirsForPsiElement);
    return FileTypeIndex.processFiles(
      fileType,
      virtualFile -> {
        ProgressManager.checkCanceled();
        VirtualFile incDir = rootsComputator.apply(virtualFile);
        return incDir == null || processor.process(virtualFile, incDir);
      },
      GlobalSearchScope.allScope(project));
  }

  public static void processNotOverridedMethods(final PerlNamespaceDefinitionElement namespaceDefinition,
                                                Processor<? super PerlSubElement> processor) {
    if (namespaceDefinition != null) {
      PsiFile containingFile = namespaceDefinition.getContainingFile();
      String packageName = namespaceDefinition.getNamespaceName();
      if (packageName == null) {
        return;
      }

      Set<String> namesSet = new HashSet<>();
      // collecting overrided
      for (PerlSubDefinitionElement subDefinitionBase : PsiTreeUtil.findChildrenOfType(containingFile, PerlSubDefinitionElement.class)) {
        if (subDefinitionBase.isValid() && StringUtil.equals(packageName, subDefinitionBase.getNamespaceName())) {
          namesSet.add(subDefinitionBase.getSubName());
        }
      }

      processParentClassesSubs(
        namespaceDefinition,
        namesSet,
        new HashSet<>(),
        processor
      );
    }
  }

  public static void processParentClassesSubs(PerlNamespaceDefinitionElement childClass,
                                              Set<? super String> processedSubsNames,
                                              Set<? super PerlNamespaceDefinitionElement> recursionMap,
                                              Processor<? super PerlSubElement> processor
  ) {
    if (childClass == null || recursionMap.contains(childClass)) {
      return;
    }
    recursionMap.add(childClass);

    for (PerlNamespaceDefinitionElement parentNamespace : PerlNamespaceDefinitionHandler.instance(childClass)
      .getParentNamespaceDefinitions(childClass)) {
      for (PsiElement subDefinitionBase : collectNamespaceSubs(parentNamespace)) {
        ProgressManager.checkCanceled();
        String subName = ((PerlSubElement)subDefinitionBase).getSubName();
        if (subDefinitionBase.isValid() &&
            ((PerlSubElement)subDefinitionBase).isMethod() &&
            !processedSubsNames.contains(subName)
          ) {
          processedSubsNames.add(subName);
          processor.process(((PerlSubElement)subDefinitionBase));
        }
      }
      processParentClassesSubs(
        parentNamespace,
        processedSubsNames,
        recursionMap,
        processor
      );
    }
  }

  public static List<PsiElement> collectNamespaceSubs(final @NotNull PsiElement namespace) {
    return CachedValuesManager.getCachedValue(
      namespace,
      () -> CachedValueProvider.Result
        .create(PerlPsiUtil.collectNamespaceMembers(namespace, PerlSubElement.class), namespace));
  }

  public static @Nullable PsiFile getPackagePsiFileByPackageName(Project project, String packageName) {
    VirtualFile packageVirtualFile = getPackageVirtualFileByPackageName(project, packageName);

    if (packageVirtualFile != null) {
      return PsiManager.getInstance(project).findFile(packageVirtualFile);
    }

    return null;
  }

  public static @Nullable VirtualFile getPackageVirtualFileByPackageName(Project project, String packageName) {
    if (StringUtil.isEmpty(packageName)) {
      return null;
    }

    String packagePath = PerlPackageUtilCore.getPackagePathByName(packageName);
    for (VirtualFile classRoot : PerlProjectManager.getInstance(project).getAllLibraryRoots()) {
      VirtualFile targetFile = classRoot.findFileByRelativePath(packagePath);
      if (targetFile != null) {
        return targetFile;
      }
    }
    return null;
  }

  /**
   * Resolving canonical package name to a psi file
   *
   * @param psiFile              base file
   * @param canonicalPackageName package name in canonical form
   * @return vartual file
   */
  public static @Nullable PsiFile resolvePackageNameToPsi(@NotNull PsiFile psiFile, String canonicalPackageName) {
    // resolves to a psi file
    return resolveRelativePathToPsi(psiFile, PerlPackageUtilCore.getPackagePathByName(canonicalPackageName));
  }

  /**
   * Resolving relative path to a psi file
   *
   * @param psiFile      base file
   * @param relativePath relative path
   * @return vartual file
   */
  public static @Nullable PsiFile resolveRelativePathToPsi(@NotNull PsiFile psiFile, String relativePath) {
    VirtualFile targetFile = resolveRelativePathToVirtualFile(psiFile, relativePath);

    if (targetFile != null && targetFile.exists()) {
      return PsiManager.getInstance(psiFile.getProject()).findFile(targetFile);
    }

    return null;
  }

  /**
   * Resolving relative path to a virtual file
   *
   * @param psiFile      base file
   * @param relativePath relative path
   * @return vartual file
   */
  public static @Nullable VirtualFile resolveRelativePathToVirtualFile(@NotNull PsiFile psiFile, String relativePath) {
    if (relativePath == null) {
      return null;
    }
    for (VirtualFile classRoot : getIncDirsForPsiElement(psiFile)) {
      if (classRoot == null) {
        continue;
      }
      VirtualFile targetFile = classRoot.findFileByRelativePath(relativePath);
      if (targetFile == null) {
        continue;
      }
      String foundRelativePath = VfsUtilCore.getRelativePath(targetFile, classRoot);

      if (StringUtil.isNotEmpty(foundRelativePath) && StringUtil.equals(foundRelativePath, relativePath)) {
        return targetFile;
      }
    }

    return null;
  }

  /**
   * Returns List of lib directories including class roots, current directory and use lib ones
   *
   * @param psiElement to resolve for
   * @return list of lib dirs
   */
  @VisibleForTesting
  public static @NotNull List<VirtualFile> getIncDirsForPsiElement(@NotNull PsiElement psiElement) {
    PsiFile psiFile = psiElement.getContainingFile().getOriginalFile();
    List<VirtualFile> result = new ArrayList<>();

    for (PerlUseStatementElement useStatement : PsiTreeUtil.findChildrenOfType(psiFile, PerlUseStatementElement.class)) {
      PerlPackageProcessor packageProcessor = useStatement.getPackageProcessor();
      if (packageProcessor instanceof PerlLibProvider perlLibProvider) {
        perlLibProvider.addLibDirs(useStatement, result);
      }
    }

    // classpath
    result.addAll(PerlProjectManager.getInstance(psiElement.getProject()).getAllLibraryRoots());

    // current dir
    if (PerlSharedSettings.getInstance(psiFile.getProject()).getTargetPerlVersion().lesserThan(PerlVersion.V5_26)) {
      VirtualFile virtualFile = psiFile.getVirtualFile();
      if (virtualFile != null && virtualFile.getParent() != null) {
        result.add(virtualFile.getParent());
      }
    }

    return result;
  }

  /**
   * Checks if sequence looks like a fqn
   *
   * @param text sequence to check
   * @return true if it's foo::bar
   */
  public static boolean isFullQualifiedName(String text) {
    return text.length() > 1 && StringUtil.containsAnyChar(text, ":'");
  }

  @Contract("null->null")
  public static @Nullable VirtualFile getClosestIncRoot(@Nullable PsiFile psiFile) {
    return psiFile == null ? null : getClosestIncRoot(psiFile.getProject(), PsiUtilCore.getVirtualFile(psiFile));
  }

  /**
   * @return innermost @INC root for a file
   * @apiNote this method may work wrong, because it is not accounts for dynamic lib paths, e.g. use lib
   */
  @Contract("_,null->null")
  public static @Nullable VirtualFile getClosestIncRoot(@NotNull Project project, @Nullable VirtualFile file) {
    return PerlDirectoryIndex.getInstance(project).getRoot(file);
  }

  /**
   * @return innermost @INC root for a file by path
   * @apiNote this method may work wrong, because it is not accounts for dynamic lib paths, e.g. use lib
   */
  public static @Nullable VirtualFile getClosestIncRoot(@NotNull Project project, @NotNull String filePath) {
    return getClosestIncRoot(project, VfsUtil.findFileByIoFile(new File(filePath), false));
  }

  public static boolean processCallables(@NotNull Project project,
                                         @NotNull GlobalSearchScope searchScope,
                                         @NotNull String canonicalName,
                                         @NotNull Processor<? super PerlCallableElement> processor) {
    if (!PerlSubUtil.processSubDefinitions(project, canonicalName, searchScope, processor)) {
      return false;
    }
    if (!PerlSubUtil.processSubDeclarations(project, canonicalName, searchScope, processor)) {
      return false;
    }
    for (PerlGlobVariableElement target : PerlGlobUtil.getGlobsDefinitions(project, canonicalName, searchScope)) {
      if (!processor.process(target)) {
        return false;
      }
    }
    return true;
  }

  public static boolean processCallablesInNamespace(@NotNull Project project,
                                                    @NotNull GlobalSearchScope searchScope,
                                                    @NotNull String packageName,
                                                    @NotNull Processor<? super PerlCallableElement> processor) {
    return PerlSubUtil.processRelatedSubsInPackage(project, searchScope, packageName, processor) &&
           PerlGlobNamespaceStubIndex.getInstance().processElements(project, packageName, searchScope, processor);
  }

  @SuppressWarnings("UnusedReturnValue")
  public static boolean processCallablesNamespaceNames(@NotNull PerlValueResolver resolver,
                                                       @NotNull String callableName,
                                                       @NotNull Processor<? super PerlCallableElement> processor) {
    var project = resolver.getProject();
    return
      PerlCallableNamesIndex.getInstance().processElements(project, callableName, resolver.getResolveScope(), processor) &&
      PerlLightCallableNamesIndex.getInstance().processLightElements(project, callableName, resolver.getResolveScope(), processor);
  }

  public interface ClassRootVirtualFileProcessor {
    boolean process(VirtualFile file, VirtualFile classRoot);
  }
}
