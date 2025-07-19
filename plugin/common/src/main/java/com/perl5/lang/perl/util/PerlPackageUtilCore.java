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

import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlScalarValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.PerlSelfHinterElement;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import static com.perl5.lang.perl.util.PerlCorePackages.*;

public final class PerlPackageUtilCore {
  public static final String MAIN_NAMESPACE_NAME = "main";
  public static final String NAMESPACE_SEPARATOR = "::";
  public static final String CORE_NAMESPACE_FULL = CORE_NAMESPACE + NAMESPACE_SEPARATOR;
  public static final String CORE_GLOBAL_NAMESPACE = CORE_NAMESPACE_FULL + "GLOBAL";
  public static final String MAIN_NAMESPACE_SHORT = NAMESPACE_SEPARATOR;
  public static final String MAIN_NAMESPACE_FULL = MAIN_NAMESPACE_NAME + NAMESPACE_SEPARATOR;
  public static final String SUPER_NAMESPACE_FULL = SUPER_NAMESPACE + NAMESPACE_SEPARATOR;
  public static final String PACKAGE_MOOSE_X_BASE = PACKAGE_MOOSE_X + NAMESPACE_SEPARATOR;
  public static final String PACKAGE_MOOSE_X_ROLE_PARAMETRIZIED = PACKAGE_MOOSE_X_BASE + "Role::Parameterized";
  public static final String PACKAGE_MOOSE_X_CLASSATTRIBUTE = PACKAGE_MOOSE_X_BASE + "ClassAttribute";
  public static final String PACKAGE_MOOSE_X_TYPES_CHECKEDUTILEXPORTS = PACKAGE_MOOSE_X_BASE + "Types::CheckedUtilExports";
  public static final String PACKAGE_MOOSE_BASE = "Moose" + NAMESPACE_SEPARATOR;
  public static final String PACKAGE_MOOSE_UTIL_TYPE_CONSTRAINTS = PACKAGE_MOOSE_BASE + "Util::TypeConstraints";
  public static final String PACKAGE_MOOSE_ROLE = PACKAGE_MOOSE_BASE + "Role";
  public static final String PACKAGE_MOOSE_OBJECT = PACKAGE_MOOSE_BASE + "Object";
  public static final @NonNls String MOO_ROLE = PACKAGE_MOO + NAMESPACE_SEPARATOR + "Role";
  public static final Pattern PACKAGE_SEPARATOR_TAIL_RE = Pattern.compile("(" + NAMESPACE_SEPARATOR + "|" +
                                                                          NAMESPACE_SEPARATOR_LEGACY + ")$");
  public static final Pattern PACKAGE_SEPARATOR_RE = Pattern.compile(
    NAMESPACE_SEPARATOR + "|" + NAMESPACE_SEPARATOR_LEGACY);
  public static final char NAMESPACE_SEPARATOR_LEGACY = '\'';
  public static final String PROFILER_MODULE = "Devel::NYTProf";
  public static final String DEBUGGER_MODULE = "Devel::Camelcadedb";
  public static final String COVERAGE_MODULE = "Devel::Cover";
  public static final String TEST_HARNESS_MODULE = "Test::Harness";
  public static final String TAP_FORMATTER_MODULE = "TAP::Formatter::Camelcade";
  public static final String JSON_MODULE = "JSON";
  public static final String ADJUST_BLOCK = "ADJUST";
  public static final String DEREFERENCE_OPERATOR = "->";
  public static final String NAMESPACE_ANY = "*";
  public static final AtomicNotNullLazyValue<PerlValue> NAMESPACE_ANY_VALUE =
    AtomicNotNullLazyValue.createValue(() -> PerlScalarValue.create(NAMESPACE_ANY));
  public static final String __PACKAGE__ = "__PACKAGE__";
  public static final String PACKAGE_CARP = "Carp";
  public static final String PACKAGE_SCALAR_UTIL = "Scalar::Util";
  public static final @NonNls String PACKAGE_MOO = "Moo";
  public static final String PACKAGE_CLASS_MOP_MIXIN = "Class::MOP::Mixin";
  public static final String PACKAGE_MOOSE = "Moose";
  public static final String PACKAGE_MOOSE_X = PACKAGE_MOOSE + "X";
  public static final String PACKAGE_VARS = "vars";
  public static final Set<String> CORE_PACKAGES_ALL = new HashSet<>();
  static {
    PerlPackageUtilCore.CORE_PACKAGES_ALL.addAll(CORE_PACKAGES);
    PerlPackageUtilCore.CORE_PACKAGES_ALL.addAll(CORE_PACKAGES_PRAGMAS);
    PerlPackageUtilCore.CORE_PACKAGES_ALL.addAll(CORE_PACKAGES_DEPRECATED);
  }

  public static final String SUPER_NAMESPACE = "SUPER";
  public static final String UNIVERSAL_NAMESPACE = "UNIVERSAL";
  public static final String CORE_NAMESPACE = "CORE";
  public static final String DEFAULT_LIB_DIR = "lib";
  public static final String DEFAULT_TEST_DIR = "t";
  public static final String FUNCTION_PARAMETERS = "Function::Parameters";
  private static final Map<String, String> CANONICAL_NAMES_CACHE = new ConcurrentHashMap<>();
  static final Map<String, String> PATH_TO_PACKAGE_NAME_MAP = new ConcurrentHashMap<>();

  private PerlPackageUtilCore() {
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
    return PerlScalarValue.create(PerlPackageUtilCore.getContextNamespaceName(psiElement));
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
    if (file instanceof PerlFileImpl perlFile) {
      PsiElement contextParent = file.getContext();
      PsiElement realParent = file.getParent();

      if (contextParent != null && !contextParent.equals(realParent)) {
        return getContextNamespaceName(contextParent);
      }

      return perlFile.getNamespaceName();
    }
    else {
      return MAIN_NAMESPACE_NAME;
    }
  }

  public static PerlNamespaceDefinitionElement getContainingNamespace(PsiElement element) {
    return PsiTreeUtil.getStubOrPsiParentOfType(element, PerlNamespaceDefinitionElement.class);
  }

  public static @NotNull String join(@NotNull String... chunks) {
    return StringUtil.join(chunks, NAMESPACE_SEPARATOR);
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

  /**
   * @return true if package name is in CoreList
   */
  @Contract("null -> false")
  public static boolean isBuiltIn(@Nullable String packageName) {
    return packageName != null && CORE_PACKAGES_ALL.contains(getCanonicalNamespaceName(packageName));
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
}
