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

package com.perl5.lang.perl.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.PairProcessor;
import com.intellij.util.Processor;
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.stubs.variables.PerlVariablesStubIndex;
import com.perl5.lang.perl.util.processors.PerlHashImportsCollector;
import com.perl5.lang.perl.util.processors.PerlImportsCollector;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Created by hurricup on 19.04.2015.
 */
public class PerlHashUtil implements PerlElementTypes {
  public static final HashSet<String> BUILT_IN = new HashSet<>(Arrays.asList(
    "!",
    "+",
    "-",
    "^H",
    "ENV",
    "INC",
    "OVERLOAD",
    "SIG",
    "LAST_PAREN_MATCH",
    "LAST_MATCH_START",
    "^CAPTURE",
    "^CAPTURE_ALL",
    "OS_ERROR",
    "ERRNO"
  ));
  /**
   * Elements that should be iterated to collect hashes
   */
  private static final TokenSet HASH_ELEMENTS_CONTAINERS = TokenSet.create(
    COMMA_SEQUENCE_EXPR,
    PARENTHESISED_EXPR,
    STRING_LIST
  );
  /**
   * Elements that may contain some number of key => val pairs, like arrays, arraycasts, hashes, etc
   * e.g $list_ref => { key => val, key1 => val, @key_vals, %key_vals2, key2 => val}
   */
  private static final TokenSet COLLAPSED_LISTS = TokenSet.create(
    ARRAY_VARIABLE, HASH_VARIABLE, ARRAY_CAST_EXPR, HASH_CAST_EXPR
  );

  public static boolean isBuiltIn(String variable) {
    return BUILT_IN.contains(variable);
  }

  /**
   * Searching project files for global hash definitions by specific package and variable name
   *
   * @param project       project to search in
   * @param canonicalName canonical variable name package::name
   * @return Collection of found definitions
   */
  public static Collection<PerlVariableDeclarationElement> getGlobalHashDefinitions(Project project, String canonicalName) {
    return getGlobalHashDefinitions(project, canonicalName, GlobalSearchScope.allScope(project));
  }

  public static Collection<PerlVariableDeclarationElement> getGlobalHashDefinitions(Project project,
                                                                                    String canonicalName,
                                                                                    GlobalSearchScope scope) {
    if (canonicalName == null) {
      return Collections.emptyList();
    }
    return StubIndex.getElements(
      PerlVariablesStubIndex.KEY_HASH,
      canonicalName,
      project,
      scope,
      PerlVariableDeclarationElement.class
    );
  }


  /**
   * Returns list of defined global hashes
   *
   * @param project project to search in
   * @return collection of variable canonical names
   */
  public static Collection<String> getDefinedGlobalHashNames(Project project) {
    return PerlUtil.getIndexKeysWithoutInternals(PerlVariablesStubIndex.KEY_HASH, project);
  }

  /**
   * Processes all global hashes names with specific processor
   *
   * @param project   project to search in
   * @param processor string processor for suitable strings
   * @return collection of constants names
   */
  public static boolean processDefinedGlobalHashes(@NotNull Project project,
                                                   @NotNull GlobalSearchScope scope,
                                                   @NotNull Processor<PerlVariableDeclarationElement> processor) {
    return PerlScalarUtil.processDefinedGlobalVariables(PerlVariablesStubIndex.KEY_HASH, project, scope, processor);
  }

  /**
   * Returns a map of imported hashes names
   *
   * @param namespaceDefinitionElement element to start looking from
   * @return result map
   */
  @NotNull
  public static List<PerlExportDescriptor> getImportedHashesDescriptors(@NotNull PerlNamespaceDefinitionElement namespaceDefinitionElement) {
    PerlImportsCollector collector = new PerlHashImportsCollector();
    PerlUtil.processImportedEntities(namespaceDefinitionElement, collector);
    return collector.getResult();
  }

  /**
   * Attempts to traverse psi element (anon hash for example) and aggregates it as a map with key => val
   *
   * @param rootElement elemtn to iterate
   * @return map of key_value => Pair(keyElement,valElement)
   */
  @NotNull
  public static Map<String, PerlHashEntry> collectHashMap(@NotNull PsiElement rootElement) {
    return packToHash(collectHashElements(rootElement));
  }

  public static Map<String, PerlHashEntry> packToHash(@NotNull List<PsiElement> elements) {
    Map<String, PerlHashEntry> result = new THashMap<>();

    processAsHash(elements, (keyElement, valElement) -> {
      String keyText = ElementManipulators.getValueText(keyElement);
      if (StringUtil.isNotEmpty(keyText)) {
        result.put(keyText, PerlHashEntry.create(keyElement, valElement));
      }
      return true;
    });

    return result;
  }

  public static boolean processHashElements(@NotNull PsiElement rootElement, @NotNull PairProcessor<PsiElement, PsiElement> processor) {
    return processAsHash(collectHashElements(rootElement), processor);
  }

  /**
   * @return hashmap as list of key, val
   */
  public static List<PsiElement> collectHashElements(@NotNull PsiElement rootElement) {
    if (PsiUtilCore.getElementType(rootElement) == ANON_HASH) {
      rootElement = ((PsiPerlAnonHash)rootElement).getExpr();
    }

    if (!HASH_ELEMENTS_CONTAINERS.contains(PsiUtilCore.getElementType(rootElement))) {
      return Collections.emptyList();
    }

    return PerlArrayUtil.collectListElements(rootElement);
  }

  public static boolean processAsHash(@NotNull List<PsiElement> elements, @NotNull PairProcessor<PsiElement, PsiElement> processor) {
    boolean isKey = true;
    for (int i = 0; i < elements.size(); i++) {
      PsiElement listElement = elements.get(i);
      if (isKey && (listElement instanceof PerlString || listElement instanceof PerlStringContentElement)) {
        i++;
        if (!processor.process(listElement, i < elements.size() ? elements.get(i) : null)) {
          return false;
        }
      }
      else if (!COLLAPSED_LISTS.contains(PsiUtilCore.getElementType(listElement))) {
        isKey = !isKey;
      }
    }

    return true;
  }
}
