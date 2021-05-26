/*
 * Copyright 2015-2021 Alexandr Evstigneev
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
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.PairProcessor;
import com.intellij.util.Processor;
import com.intellij.util.SmartList;
import com.perl5.lang.perl.extensions.packageprocessor.PerlLibProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageParentsProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessor;
import com.perl5.lang.perl.extensions.parser.PerlRuntimeParentsProvider;
import com.perl5.lang.perl.extensions.parser.PerlRuntimeParentsProviderFromArray;
import com.perl5.lang.perl.fileTypes.PerlFileTypePackage;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlScalarValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.idea.manipulators.PerlNamespaceElementManipulator;
import com.perl5.lang.perl.idea.project.PerlDirectoryIndex;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.refactoring.rename.RenameRefactoringQueue;
import com.perl5.lang.perl.internals.PerlVersion;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlLightNamespaceIndex;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlLightNamespaceReverseIndex;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceIndex;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceReverseIndex;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import gnu.trove.THashSet;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Pattern;


public class PerlPackageUtil implements PerlElementTypes, PerlCorePackages {
  public static final String NAMESPACE_SEPARATOR = "::";
  public static final String DEREFERENCE_OPERATOR = "->";
  public static final char NAMESPACE_SEPARATOR_LEGACY = '\'';

  public static final String NAMESPACE_ANY = "*";
  public static final PerlValue NAMESPACE_ANY_VALUE = PerlScalarValue.create(NAMESPACE_ANY);

  public static final String __PACKAGE__ = "__PACKAGE__";
  public static final String PACKAGE_CARP = "Carp";
  public static final String PACKAGE_SCALAR_UTIL = "Scalar::Util";
  @NonNls public static final String PACKAGE_MOO = "Moo";
  @NonNls public static final String MOO_ROLE = PACKAGE_MOO + NAMESPACE_SEPARATOR + "Role";
  public static final String PACKAGE_CLASS_MOP_MIXIN = "Class::MOP::Mixin";
  public static final String PACKAGE_MOOSE = "Moose";
  public static final String PACKAGE_MOOSE_BASE = "Moose" + NAMESPACE_SEPARATOR;
  public static final String PACKAGE_MOOSE_X = PACKAGE_MOOSE + "X";
  public static final String PACKAGE_MOOSE_X_BASE = PACKAGE_MOOSE_X + NAMESPACE_SEPARATOR;
  public static final String PACKAGE_MOOSE_OBJECT = PACKAGE_MOOSE_BASE + "Object";
  public static final String PACKAGE_MOOSE_ROLE = PACKAGE_MOOSE_BASE + "Role";
  public static final String PACKAGE_MOOSE_UTIL_TYPE_CONSTRAINTS = PACKAGE_MOOSE_BASE + "Util::TypeConstraints";
  public static final String PACKAGE_MOOSE_X_TYPES_CHECKEDUTILEXPORTS = PACKAGE_MOOSE_X_BASE + "Types::CheckedUtilExports";
  public static final String PACKAGE_MOOSE_X_CLASSATTRIBUTE = PACKAGE_MOOSE_X_BASE + "ClassAttribute";
  public static final String PACKAGE_MOOSE_X_METHODATTRIBUTES_ROLE = PACKAGE_MOOSE_X_BASE + "MethodAttributes::Role";
  public static final String PACKAGE_MOOSE_X_ROLE_PARAMETRIZIED = PACKAGE_MOOSE_X_BASE + "Role::Parameterized";
  public static final String PACKAGE_MOOSE_X_ROLE_WITHOVERLOADING = PACKAGE_MOOSE_X_BASE + "Role::WithOverloading";
  public static final String PACKAGE_MOOSE_X_METHODATTRIBUTES = PACKAGE_MOOSE_X_BASE + "MethodAttributes";
  public static final String PACKAGE_VARS = "vars";

  public static final Pattern PACKAGE_SEPARATOR_RE = Pattern.compile(NAMESPACE_SEPARATOR + "|" + NAMESPACE_SEPARATOR_LEGACY);
  public static final Pattern PACKAGE_SEPARATOR_TAIL_RE = Pattern.compile("(" + NAMESPACE_SEPARATOR + "|" +
                                                                          NAMESPACE_SEPARATOR_LEGACY + ")$");

  public static final Set<String> CORE_PACKAGES_ALL = new THashSet<>();

  public static final String SUPER_NAMESPACE = "SUPER";
  public static final String SUPER_NAMESPACE_FULL = SUPER_NAMESPACE + NAMESPACE_SEPARATOR;

  public static final String MAIN_NAMESPACE_NAME = "main";
  public static final String MAIN_NAMESPACE_FULL = MAIN_NAMESPACE_NAME + NAMESPACE_SEPARATOR;
  public static final String MAIN_NAMESPACE_SHORT = NAMESPACE_SEPARATOR;

  public static final String UNIVERSAL_NAMESPACE = "UNIVERSAL";

  public static final String CORE_NAMESPACE = "CORE";
  public static final String CORE_NAMESPACE_FULL = CORE_NAMESPACE + NAMESPACE_SEPARATOR;
  public static final String CORE_GLOBAL_NAMESPACE = CORE_NAMESPACE_FULL + "GLOBAL";
  public static final String DEFAULT_LIB_DIR = "lib";
  public static final String DEFAULT_TEST_DIR = "t";

  private static final Map<String, String> CANONICAL_NAMES_CACHE = new ConcurrentHashMap<>();
  private static final Map<String, String> PATH_TO_PACKAGE_NAME_MAP = new ConcurrentHashMap<>();
  public static final String FUNCTION_PARAMETERS = "Function::Parameters";

  static {
    CORE_PACKAGES_ALL.addAll(CORE_PACKAGES);
    CORE_PACKAGES_ALL.addAll(CORE_PACKAGES_PRAGMAS);
    CORE_PACKAGES_ALL.addAll(CORE_PACKAGES_DEPRECATED);
  }

  /**
   * @return true if package name is in CoreList
   */
  @Contract("null -> false")
  public static boolean isBuiltIn(@Nullable String packageName) {
    return packageName != null && CORE_PACKAGES_ALL.contains(getCanonicalNamespaceName(packageName));
  }

  /**
   * Checks if package is pragma
   *
   * @param pacakgeName package name
   * @return result
   */
  public static boolean isPragma(String pacakgeName) {
    return CORE_PACKAGES_PRAGMAS.contains(getCanonicalNamespaceName(pacakgeName));
  }

  /**
   * Checks if package is deprecated
   *
   * @param packageName package name
   * @return result
   */
  public static boolean isDeprecated(Project project, String packageName) {
    for (PerlNamespaceDefinitionElement definition : getNamespaceDefinitions(project, packageName)) {
      if (definition.isDeprecated()) {
        return true;
      }
    }

    return CORE_PACKAGES_DEPRECATED.contains(getCanonicalNamespaceName(packageName));
  }

  @Contract("null->false")
  public static boolean isSUPER(@Nullable String packageName) {
    return SUPER_NAMESPACE.equals(packageName);
  }

  public static boolean isMain(String packageName) {
    return MAIN_NAMESPACE_NAME.equals(packageName);
  }

  public static boolean isCORE(String packageName) {
    return CORE_NAMESPACE.equals(packageName);
  }

  public static boolean isUNIVERSAL(String packageName) {
    return UNIVERSAL_NAMESPACE.equals(packageName);
  }

  public static @NotNull String join(@NotNull String... chunks) {
    return StringUtil.join(chunks, NAMESPACE_SEPARATOR);
  }

  /**
   * Make canonical package name.
   *
   * @param name package name
   * @return canonical package name
   */
  public static String getCanonicalNamespaceName(@NotNull String name) {
    String canonicalName = getCanonicalName(name);
    return StringUtil.startsWith(canonicalName, MAIN_NAMESPACE_FULL) ?
           canonicalName.substring(MAIN_NAMESPACE_FULL.length()) : canonicalName;
  }

  public static @NotNull String getCanonicalName(@NotNull String name) {
    String newName;

    if ((newName = CANONICAL_NAMES_CACHE.get(name)) != null) {
      return newName;
    }

    String originalName = name;

    name = PACKAGE_SEPARATOR_TAIL_RE.matcher(name).replaceFirst("");

    String[] chunks = PACKAGE_SEPARATOR_RE.split(name, -1);

    if (chunks.length > 0 && chunks[0].isEmpty())    // implicit main
    {
      chunks[0] = MAIN_NAMESPACE_NAME;
    }

    newName = StringUtil.join(chunks, NAMESPACE_SEPARATOR);

    CANONICAL_NAMES_CACHE.put(originalName, newName);

    return newName;
  }

  public static @NotNull PerlValue getContextType(@Nullable PsiElement element) {
    return PerlScalarValue.create(getContextNamespaceName(element));
  }

  public static @NotNull List<String> split(@Nullable String packageName) {
    return packageName == null ? Collections.emptyList() : StringUtil.split(getCanonicalNamespaceName(packageName), NAMESPACE_SEPARATOR);
  }

  /**
   * Searching of namespace element is in. If no explicit namespaces defined, main is returned
   *
   * @param element psi element to find definition for
   * @return canonical package name
   */
  @Contract("null->null;!null->!null")
  public static String getContextNamespaceName(@Nullable PsiElement element) {
    if (element == null) {
      return null;
    }
    PerlNamespaceDefinitionElement namespaceDefinition = getContainingNamespace(element);

    if (namespaceDefinition != null &&
        namespaceDefinition.getNamespaceName() != null) // checking that definition is valid and got namespace
    {
      String name = namespaceDefinition.getNamespaceName();
      assert name != null;
      return name;
    }

    // default value
    PsiFile file = element.getContainingFile();
    if (file instanceof PerlFileImpl) {
      PsiElement contextParent = file.getContext();
      PsiElement realParent = file.getParent();

      if (contextParent != null && !contextParent.equals(realParent)) {
        return getContextNamespaceName(contextParent);
      }

      return ((PerlFileImpl)file).getNamespaceName();
    }
    else {
      return MAIN_NAMESPACE_NAME;
    }
  }

  // fixme shouldn't we have recursion protection here?
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

  // fixme take fileContext in account?
  public static PerlNamespaceDefinitionElement getContainingNamespace(PsiElement element) {
    return PsiTreeUtil.getStubOrPsiParentOfType(element, PerlNamespaceDefinitionElement.class);
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
   * @param project     project to search in
   * @param packageName canonical package name (without tailing ::)
   * @return collection of found definitions
   * @deprecated use {@link #getNamespaceDefinitions(Project, GlobalSearchScope, String)}
   */
  @Deprecated
  public static Collection<PerlNamespaceDefinitionElement> getNamespaceDefinitions(Project project, @NotNull String packageName) {
    return getNamespaceDefinitions(project, GlobalSearchScope.allScope(project), packageName);
  }

  public static Collection<PerlNamespaceDefinitionElement> getNamespaceDefinitions(Project project,
                                                                                   GlobalSearchScope scope,
                                                                                   @NotNull String packageName) {
    List<PerlNamespaceDefinitionElement> result = new ArrayList<>();
    processNamespaces(packageName, project, scope, result::add);
    return result;
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

  @SuppressWarnings("UnusedReturnValue")
  public static boolean processChildNamespaces(@NotNull String parentPackageName,
                                               @NotNull Project project,
                                               @NotNull GlobalSearchScope scope,
                                               @NotNull Processor<PerlNamespaceDefinitionElement> processor) {
    return PerlNamespaceReverseIndex.getInstance().processElements(project, parentPackageName, scope, processor) &&
           PerlLightNamespaceReverseIndex.getInstance().processLightElements(project, parentPackageName, scope, processor);
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
   * Builds package path from packageName Foo::Bar => Foo/Bar.pm
   *
   * @param packageName canonical package name
   * @return package path
   */
  public static String getPackagePathByName(String packageName) {
    return StringUtil.join(packageName.split(":+"), "/") + "." + PerlFileTypePackage.EXTENSION;
  }

  /**
   * Translates package relative name to the package name Foo/Bar.pm => Foo::Bar
   *
   * @param packagePath package relative path
   * @return canonical package name
   */
  public static String getPackageNameByPath(final String packagePath) {
    String result = PATH_TO_PACKAGE_NAME_MAP.get(packagePath);

    if (result == null) {
      String path = packagePath.replaceAll("\\\\", "/");
      result = getCanonicalNamespaceName(StringUtil.join(path.replaceFirst("\\.pm$", "").split("/"), NAMESPACE_SEPARATOR));
      PATH_TO_PACKAGE_NAME_MAP.put(packagePath, result);
    }
    return result;
  }

  /**
   * Adds to queue netsted namespaces, which names should be adjusted to the new package name/path
   *
   * @param queue   - RenameRefactoringQueue
   * @param file    - file has been moved
   * @param oldPath - previous filepath
   */
  public static void collectNestedPackageDefinitionsFromFile(@NotNull RenameRefactoringQueue queue, VirtualFile file, String oldPath) {
    Project project = queue.getProject();
    VirtualFile newInnermostRoot = getClosestIncRoot(project, file);

    if (newInnermostRoot != null) {
      String newRelativePath = VfsUtil.getRelativePath(file, newInnermostRoot);
      String newPackageName = getPackageNameByPath(newRelativePath);

      VirtualFile oldInnermostRoot = getClosestIncRoot(project, oldPath);

      if (oldInnermostRoot != null) {
        String oldRelativePath = oldPath.substring(oldInnermostRoot.getPath().length());
        String oldPackageName = getPackageNameByPath(oldRelativePath);

        if (!oldPackageName.equals(newPackageName)) {
          PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
          if (psiFile != null) {
            for (PerlNamespaceDefinitionElement namespaceDefinition : PsiTreeUtil
              .findChildrenOfType(psiFile, PerlNamespaceDefinitionElement.class)) {
              if (oldPackageName.equals(namespaceDefinition.getNamespaceName())) {
                queue.addElement(namespaceDefinition, newPackageName);
              }
            }
          }
        }
      }
    }
  }

  /**
   * Searches for all pm files and add renaming of nested package definitions to the queue. Invoked after renaming
   *
   * @param queue     RenameRefactoringQueue object
   * @param directory VirtualFile of renamed directory
   * @param oldPath   old directory path
   */
  public static void collectNestedPackageDefinitions(RenameRefactoringQueue queue, VirtualFile directory, String oldPath) {
    Project project = queue.getProject();
    VirtualFile directorySourceRoot = getClosestIncRoot(project, directory);

    if (directorySourceRoot != null) {
      for (VirtualFile file : VfsUtil.collectChildrenRecursively(directory)) {
        if (!file.isDirectory() &&
            file.getFileType() == PerlFileTypePackage.INSTANCE &&
            directorySourceRoot.equals(getClosestIncRoot(project, file))) {
          String relativePath = VfsUtil.getRelativePath(file, directory);
          String oldFilePath = oldPath + "/" + relativePath;
          collectNestedPackageDefinitionsFromFile(queue, file, oldFilePath);
        }
      }
    }
  }

  /**
   * Searches for all pm files in directory to be renamed/moved, searches for references to those packages and add them to the renaming queue
   *
   * @param project   Project to be renamed
   * @param directory VirtualFile of directory to be renamed
   * @param newPath   new directory path
   */
  public static void adjustNestedFiles(Project project, VirtualFile directory, String newPath) {
    VirtualFile oldDirectorySourceRoot = getClosestIncRoot(project, directory);
    PsiManager psiManager = PsiManager.getInstance(project);

    if (oldDirectorySourceRoot != null) {
      for (VirtualFile file : VfsUtil.collectChildrenRecursively(directory)) {
        if (!file.isDirectory() &&
            file.getFileType() == PerlFileTypePackage.INSTANCE &&
            oldDirectorySourceRoot.equals(getClosestIncRoot(project, file))) {
          PsiFile psiFile = psiManager.findFile(file);

          if (psiFile != null) {
            for (PsiReference inboundReference : ReferencesSearch.search(psiFile)) {
              String newPackagePath = newPath + "/" + VfsUtil.getRelativePath(file, directory);
              VirtualFile newInnermostRoot = getClosestIncRoot(project, newPackagePath);
              if (newInnermostRoot != null) {
                String newRelativePath = newPackagePath.substring(newInnermostRoot.getPath().length());
                String newPackageName = getPackageNameByPath(newRelativePath);

                PerlPsiUtil.renameElement(inboundReference.getElement(), newPackageName);
              }
            }
          }
        }
      }
    }
  }

  public static boolean processPackageFilesForPsiElement(@NotNull PsiElement element,
                                                         @NotNull PairProcessor<String, VirtualFile> processor) {
    return processIncFilesForPsiElement(
      element,
      (file, classRoot) -> {
        String relativePath = VfsUtil.getRelativePath(file, classRoot);
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
                                                Processor<PerlSubElement> processor) {
    if (namespaceDefinition != null) {
      PsiFile containingFile = namespaceDefinition.getContainingFile();
      String packageName = namespaceDefinition.getNamespaceName();
      if (packageName == null) {
        return;
      }

      Set<String> namesSet = new THashSet<>();
      // collecting overrided
      for (PerlSubDefinitionElement subDefinitionBase : PsiTreeUtil.findChildrenOfType(containingFile, PerlSubDefinitionElement.class)) {
        if (subDefinitionBase.isValid() && StringUtil.equals(packageName, subDefinitionBase.getNamespaceName())) {
          namesSet.add(subDefinitionBase.getSubName());
        }
      }

      processParentClassesSubs(
        namespaceDefinition,
        namesSet,
        new THashSet<>(),
        processor
      );
    }
  }

  public static void processParentClassesSubs(PerlNamespaceDefinitionElement childClass,
                                              Set<String> processedSubsNames,
                                              Set<PerlNamespaceDefinitionElement> recursionMap,
                                              Processor<PerlSubElement> processor
  ) {
    if (childClass == null || recursionMap.contains(childClass)) {
      return;
    }
    recursionMap.add(childClass);

    for (PerlNamespaceDefinitionElement parentNamespace : childClass.getParentNamespaceDefinitions()) {
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

    String packagePath = getPackagePathByName(packageName);
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
    return resolveRelativePathToPsi(psiFile, getPackagePathByName(canonicalPackageName));
  }

  /**
   * Resolving canonical package to a virtual file
   *
   * @param psiFile              base file
   * @param canonicalPackageName package name in canonical form
   * @return vartual file
   */
  public static @Nullable VirtualFile resolvePackageNameToVirtualFile(@NotNull PsiFile psiFile, String canonicalPackageName) {
    // resolves to a psi file
    return resolveRelativePathToVirtualFile(psiFile, getPackagePathByName(canonicalPackageName));
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
      String foundRelativePath = VfsUtil.getRelativePath(targetFile, classRoot);

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
  private static @NotNull List<VirtualFile> getIncDirsForPsiElement(@NotNull PsiElement psiElement) {
    PsiFile psiFile = psiElement.getContainingFile().getOriginalFile();
    List<VirtualFile> result = new ArrayList<>();

    // libdirs providers fixme we need to use stubs or psi here
    for (PerlUseStatementElement useStatement : PsiTreeUtil.findChildrenOfType(psiFile, PerlUseStatementElement.class)) {
      PerlPackageProcessor packageProcessor = useStatement.getPackageProcessor();
      if (packageProcessor instanceof PerlLibProvider) {
        ((PerlLibProvider)packageProcessor).addLibDirs(useStatement, result);
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

  /**
   * Returns qualified ranges for identifier, like variable name or sub_name_qualified
   *
   * @param text token text
   * @return pair of two ranges; first will be null if it's not qualified name
   */
  public static @NotNull Pair<TextRange, TextRange> getQualifiedRanges(@NotNull CharSequence text) {
    if (text.length() == 1) {
      return Pair.create(null, TextRange.create(0, 1));
    }

    int lastSeparatorOffset = StringUtil.lastIndexOfAny(text, ":'");

    if (lastSeparatorOffset < 0) {
      return Pair.create(null, TextRange.create(0, text.length()));
    }

    TextRange packageRange = PerlNamespaceElementManipulator.getRangeInString(text.subSequence(0, lastSeparatorOffset));

    TextRange nameRange;

    if (++lastSeparatorOffset < text.length()) {
      nameRange = TextRange.create(lastSeparatorOffset, text.length());
    }
    else {
      nameRange = TextRange.EMPTY_RANGE;
    }
    return Pair.create(packageRange, nameRange);
  }

  /**
   * @return the expected value of the {@code $self} passed to the method. This is either context value or value from the self hinter
   */
  public static @NotNull PerlValue getExpectedSelfValue(@NotNull PsiElement psiElement) {
    PsiElement run = psiElement;
    while (true) {
      PerlSelfHinterElement selfHinter = PsiTreeUtil.getParentOfType(run, PerlSelfHinterElement.class);
      if (selfHinter == null) {
        break;
      }
      PerlValue hintedType = selfHinter.getSelfType();
      if (!hintedType.isUnknown()) {
        return hintedType;
      }
      run = selfHinter;
    }
    return PerlScalarValue.create(getContextNamespaceName(psiElement));
  }

  /**
   * @return list of parent namespaces defined by different syntax constructions in the sub-tree of the {@code namespaceDefinition}
   */
  public static @NotNull List<String> collectParentNamespaceNamesFromPsi(@NotNull PerlNamespaceDefinitionElement namespaceDefinition) {
    String namespaceName = namespaceDefinition.getNamespaceName();
    if (StringUtil.isEmpty(namespaceName)) {
      return Collections.emptyList();
    }
    ParentNamespacesNamesCollector collector = new ParentNamespacesNamesCollector(namespaceName);
    PerlPsiUtil.processNamespaceStatements(namespaceDefinition, collector);
    collector.applyRunTimeModifiers();
    return collector.getParentNamespaces();
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

  public interface ClassRootVirtualFileProcessor {
    boolean process(VirtualFile file, VirtualFile classRoot);
  }

  public static class ParentNamespacesNamesCollector implements Processor<PsiElement> {
    private final @NotNull List<String> myParentNamespaces = new SmartList<>();
    private final @NotNull List<PerlRuntimeParentsProvider> runtimeModifiers = new SmartList<>();
    private final @NotNull String myNamespaceName;

    public ParentNamespacesNamesCollector(@NotNull String namespaceName) {
      myNamespaceName = namespaceName;
    }

    @Override
    public boolean process(PsiElement element) {
      if (element instanceof PerlUseStatementElement) {
        PerlPackageProcessor processor = ((PerlUseStatementElement)element).getPackageProcessor();
        if (processor instanceof PerlPackageParentsProvider) {
          ((PerlPackageParentsProvider)processor).changeParentsList((PerlUseStatementElement)element, myParentNamespaces);
        }
      }
      else if (element instanceof PerlRuntimeParentsProvider) {
        runtimeModifiers.add((PerlRuntimeParentsProvider)element);
      }
      else if (element.getFirstChild() instanceof PerlRuntimeParentsProvider) {
        runtimeModifiers.add((PerlRuntimeParentsProvider)element.getFirstChild());
      }
      else if (PerlElementPatterns.ISA_ASSIGN_STATEMENT.accepts(element)) {
        PsiElement assignExpr = element.getFirstChild();
        if (assignExpr instanceof PsiPerlAssignExpr) {
          PsiPerlArrayVariable variable = PsiTreeUtil.findChildOfType(element, PsiPerlArrayVariable.class);

          if (variable != null && StringUtil.equals("ISA", variable.getName())) {
            PsiElement rightSide = assignExpr.getLastChild();
            if (rightSide != null) {
              String explicitPackageName = variable.getExplicitNamespaceName();
              if (explicitPackageName == null || StringUtil.equals(explicitPackageName, myNamespaceName)) {
                runtimeModifiers.add(new PerlRuntimeParentsProviderFromArray(assignExpr.getLastChild()));
              }
            }
          }
        }
      }

      return true;
    }

    public void applyRunTimeModifiers() {
      for (PerlRuntimeParentsProvider provider : runtimeModifiers) {
        provider.changeParentsList(myParentNamespaces);
      }
    }

    public @NotNull List<String> getParentNamespaces() {
      return myParentNamespaces;
    }
  }
}
