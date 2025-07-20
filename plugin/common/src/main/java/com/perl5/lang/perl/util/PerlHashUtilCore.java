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

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.PairProcessor;
import com.perl5.lang.perl.parser.PerlElementTypesGenerated;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.PerlStringContentElement;
import com.perl5.lang.perl.psi.PsiPerlAnonHash;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class PerlHashUtilCore {
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
  /**
   * Elements that should be iterated to collect hashes
   */
  private static final TokenSet HASH_ELEMENTS_CONTAINERS = TokenSet.create(
    PerlElementTypesGenerated.COMMA_SEQUENCE_EXPR,
    PerlElementTypesGenerated.PARENTHESISED_EXPR,
    PerlElementTypesGenerated.STRING_LIST
  );
  /**
   * Elements that may contain some number of key => val pairs, like arrays, arraycasts, hashes, etc
   * e.g $list_ref => { key => val, key1 => val, @key_vals, %key_vals2, key2 => val}
   */
  private static final TokenSet COLLAPSED_LISTS = TokenSet.create(
    PerlElementTypesGenerated.ARRAY_VARIABLE, PerlElementTypesGenerated.HASH_VARIABLE, PerlElementTypesGenerated.ARRAY_CAST_EXPR,
    PerlElementTypesGenerated.HASH_CAST_EXPR
  );

  private PerlHashUtilCore() {
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
    if (PsiUtilCore.getElementType(rootElement) == PerlElementTypesGenerated.ANON_HASH) {
      rootElement = ((PsiPerlAnonHash)rootElement).getExpr();
    }

    if (!HASH_ELEMENTS_CONTAINERS.contains(PsiUtilCore.getElementType(rootElement))) {
      return Collections.emptyList();
    }

    return PerlArrayUtilCore.collectListElements(rootElement);
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
