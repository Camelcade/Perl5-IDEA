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
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.fileTypes.PerlFileTypePackage;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlScalarValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.idea.manipulators.PerlNamespaceElementManipulator;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.PerlSelfHinterElement;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
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
  public static final @NonNls String PACKAGE_MOO = "Moo";
  public static final String ADJUST_BLOCK = "ADJUST";
  public static final String CORE_NAMESPACE = "CORE";
  public static final String COVERAGE_MODULE = "Devel::Cover";
  public static final String DEBUGGER_MODULE = "Devel::Camelcadedb";
  public static final String DEFAULT_LIB_DIR = "lib";
  public static final String DEFAULT_TEST_DIR = "t";
  public static final String DEREFERENCE_OPERATOR = "->";
  public static final String FUNCTION_PARAMETERS = "Function::Parameters";
  public static final String JSON_MODULE = "JSON";
  public static final String MAIN_NAMESPACE_NAME = "main";
  public static final String NAMESPACE_ANY = "*";
  public static final String NAMESPACE_SEPARATOR = "::";
  public static final String PACKAGE_CARP = "Carp";
  public static final String PACKAGE_CLASS_MOP_MIXIN = "Class::MOP::Mixin";
  public static final String PACKAGE_MOOSE = "Moose";
  public static final String PACKAGE_MOOSE_X = PACKAGE_MOOSE + "X";
  public static final String PACKAGE_SCALAR_UTIL = "Scalar::Util";
  public static final String PACKAGE_VARS = "vars";
  public static final String PROFILER_MODULE = "Devel::NYTProf";
  public static final String SUPER_NAMESPACE = "SUPER";
  public static final String TAP_FORMATTER_MODULE = "TAP::Formatter::Camelcade";
  public static final String TEST_HARNESS_MODULE = "Test::Harness";
  public static final String UNIVERSAL_NAMESPACE = "UNIVERSAL";
  public static final String __PACKAGE__ = "__PACKAGE__";
  public static final char NAMESPACE_SEPARATOR_LEGACY = '\'';
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
  public static final AtomicNotNullLazyValue<PerlValue> NAMESPACE_ANY_VALUE =
    AtomicNotNullLazyValue.createValue(() -> PerlScalarValue.create(NAMESPACE_ANY));
  public static final Set<String> CORE_PACKAGES_ALL = new HashSet<>();

  static {
    PerlPackageUtilCore.CORE_PACKAGES_ALL.addAll(CORE_PACKAGES);
    PerlPackageUtilCore.CORE_PACKAGES_ALL.addAll(CORE_PACKAGES_PRAGMAS);
    PerlPackageUtilCore.CORE_PACKAGES_ALL.addAll(CORE_PACKAGES_DEPRECATED);
  }

  private static final Map<String, String> CANONICAL_NAMES_CACHE = new ConcurrentHashMap<>();
  private static final Map<String, String> PATH_TO_PACKAGE_NAME_MAP = new ConcurrentHashMap<>();

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
  public static @Nullable String getContextNamespaceName(@Nullable PsiElement element) {
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

  public static @NotNull PerlValue getContextType(@Nullable PsiElement element) {
    return PerlScalarValue.create(getContextNamespaceName(element));
  }

  public static @NotNull List<String> split(@Nullable String packageName) {
    return packageName == null ? Collections.emptyList() : StringUtil.split(getCanonicalNamespaceName(packageName), NAMESPACE_SEPARATOR);
  }

  @Contract("null -> null")
  public static @Nullable @NlsSafe Pair<@Nullable String, @Nullable String> splitNames(@Nullable @NlsSafe String fqn) {
    if (fqn == null || fqn.isEmpty()) {
      return null;
    }
    if (fqn.endsWith(NAMESPACE_SEPARATOR)) {
      return Pair.create(getCanonicalName(fqn), null);
    }
    var sepIndex = fqn.lastIndexOf(NAMESPACE_SEPARATOR);
    if (sepIndex < 0) {
      return Pair.create(null, fqn);
    }
    if (sepIndex == 0) {
      return Pair.create(MAIN_NAMESPACE_NAME, fqn.substring(NAMESPACE_SEPARATOR.length()));
    }
    return Pair.create(fqn.substring(0, sepIndex), fqn.substring(sepIndex + NAMESPACE_SEPARATOR.length()));
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
   * Translates package relative name to the package name Foo/Bar.pm => Foo::Bar
   *
   * @param packagePath package relative path
   * @return canonical package name
   */
  public static String getPackageNameByPath(final String packagePath) {
    String result = PATH_TO_PACKAGE_NAME_MAP.get(packagePath);

    if (result == null) {
      String path = packagePath.replace("\\", "/");
      result = getCanonicalNamespaceName(
        StringUtil.join(path.replaceFirst("\\.pm$", "").split("/"), NAMESPACE_SEPARATOR));
      PATH_TO_PACKAGE_NAME_MAP.put(packagePath, result);
    }
    return result;
  }
}
