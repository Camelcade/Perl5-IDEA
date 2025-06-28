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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.PairProcessor;
import com.intellij.util.Processor;
import com.intellij.util.SmartList;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.PerlStringContentElement;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.PsiPerlAnonHash;
import com.perl5.lang.perl.psi.references.PerlImplicitDeclarationsService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.perl5.lang.perl.psi.stubs.variables.PerlHashNamespaceStubIndex.KEY_HASH_IN_NAMESPACE;
import static com.perl5.lang.perl.psi.stubs.variables.PerlHashStubIndex.KEY_HASH;


public final class PerlHashUtil implements PerlElementTypes {
  public static final Set<String> BUILT_IN = Set.of(
    "!",
    "+",
    "-",
    "^H",
    "ENV",
    "INC",
    "SIG",
    "LAST_PAREN_MATCH",
    "^CAPTURE",
    "^CAPTURE_ALL",
    "OS_ERROR",
    "ERRNO",
    "^HOOK"
  );

  private PerlHashUtil() {
  }

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

  public static Collection<PerlVariableDeclarationElement> getGlobalHashDefinitions(@NotNull Project project,
                                                                                    @NotNull String canonicalName,
                                                                                    @NotNull GlobalSearchScope scope) {
    List<PerlVariableDeclarationElement> result = new SmartList<>();
    processGlobalHashesByName(project, scope, canonicalName, true, it -> {
      if (canonicalName.equals(it.getCanonicalName())) {
        result.add(it);
      }
      return true;
    });
    return result;
  }


  /**
   * Returns list of defined global hashes
   *
   * @param project project to search in
   * @return collection of variable canonical names
   */
  @SuppressWarnings("StaticMethodOnlyUsedInOneClass")
  public static Collection<String> getDefinedGlobalHashNames(@NotNull Project project) {
    return PerlStubUtil.getAllKeys(KEY_HASH, GlobalSearchScope.allScope(project));
  }

  @SuppressWarnings("UnusedReturnValue")
  public static boolean processGlobalHashesByName(@NotNull Project project,
                                                  @NotNull GlobalSearchScope scope,
                                                  @NotNull String canonicalName,
                                                  boolean processAll,
                                                  @NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    return PerlImplicitDeclarationsService.getInstance(project).processHashes(canonicalName, processor) &&
           PerlVariableUtil.processGlobalVariables(KEY_HASH, project, scope, processor, canonicalName, processAll);
  }

  @SuppressWarnings("StaticMethodOnlyUsedInOneClass")
  public static boolean processGlobalHashes(@NotNull Project project,
                                            @NotNull GlobalSearchScope scope,
                                            @Nullable String namespaceName,
                                            boolean processAll,
                                            @NotNull Processor<? super PerlVariableDeclarationElement> processor) {
    if( namespaceName == null){
      return PerlImplicitDeclarationsService.getInstance(project).processHashes(processor) &&
             PerlVariableUtil.processGlobalVariables(KEY_HASH, project, scope, processor, processAll);
    }
    return PerlImplicitDeclarationsService.getInstance(project).processHashesInNamespace(namespaceName, processor) &&
           PerlVariableUtil.processGlobalVariables(KEY_HASH_IN_NAMESPACE, project, scope, processor, namespaceName, !processAll);
  }

  /**
   * Attempts to traverse psi element (anon hash for example) and aggregates it as a map with key => val
   *
   * @param rootElement elemtn to iterate
   * @return map of key_value => Pair(keyElement,valElement)
   */
  public static @NotNull Map<String, PerlHashEntry> collectHashMap(@NotNull PsiElement rootElement) {
    return packToHash(collectHashElements(rootElement));
  }

  public static Map<String, PerlHashEntry> packToHash(@NotNull List<? extends PsiElement> elements) {
    if (elements.isEmpty()) {
      return Collections.emptyMap();
    }
    Map<String, PerlHashEntry> result = new HashMap<>();

    processAsHash(elements, (keyElement, valElement) -> {
      String keyText = ElementManipulators.getValueText(keyElement);
      if (StringUtil.isNotEmpty(keyText)) {
        result.put(keyText, PerlHashEntry.create(keyElement, valElement));
      }
      return true;
    });

    return result;
  }

  @SuppressWarnings({"UnusedReturnValue", "StaticMethodOnlyUsedInOneClass"})
  public static boolean processHashElements(@NotNull PsiElement rootElement,
                                            @NotNull PairProcessor<? super PsiElement, ? super PsiElement> processor) {
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

  public static boolean processAsHash(@NotNull List<? extends PsiElement> elements,
                                      @NotNull PairProcessor<? super PsiElement, ? super PsiElement> processor) {
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
