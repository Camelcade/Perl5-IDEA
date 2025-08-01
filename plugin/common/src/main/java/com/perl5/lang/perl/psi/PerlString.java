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

package com.perl5.lang.perl.psi;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.parser.PerlParserUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public interface PerlString extends PerlQuoted {
  String FILE_NAME_PATTERN = "[^<>:;,?\"*|/\\\\]+";
  String FILE_DELIMITER_PATTERN = "[\\\\/]+";
  String OPTIONAL_ROOT_PATTERN = "(?:/+|[a-zA-Z]:[/\\\\]+)?";
  Pattern FILE_PATH_PATTERN = Pattern.compile(
    OPTIONAL_ROOT_PATTERN +
    "(?:" + FILE_NAME_PATTERN + FILE_DELIMITER_PATTERN + ")*+" +
    "(?:" + FILE_NAME_PATTERN + ")?"
  );
  String OPEN_QUOTES = "<[{(";
  String CLOSE_QUOTES = ">]})";
  String ALTERNATIVE_QUOTES = "/|!~^._=?\\;";

  /**
   * @return first element of string content. Or null if string is empty or invalid
   */
  PsiElement getFirstContentToken();

  /**
   * @return all children, including leaf ones
   */
  default @NotNull List<PsiElement> getAllChildrenList() {
    PsiElement run = getFirstContentToken();

    if (run == null) {
      return Collections.emptyList();
    }

    PsiElement closeQuote = getCloseQuoteElement();
    List<PsiElement> result = new ArrayList<>();
    while (run != null && run != closeQuote) {
      result.add(run);
      run = run.getNextSibling();
    }
    return result;
  }

  @Contract(value = "null -> false", pure = true)
  static boolean looksLikePackage(@Nullable String text) {
    return StringUtil.isNotEmpty(text) && StringUtil.containsAnyChar(text, ":'") && PerlParserUtil.isAmbiguousPackage(text);
  }

  @Contract(value = "null -> false", pure = true)
  static boolean looksLikePath(@Nullable String text) {
    return StringUtil.isNotEmpty(text) && FILE_PATH_PATTERN.matcher(text).matches();
  }

  @Contract(value = "null -> null", pure = true)
  static @Nullable String getContentFileName(@Nullable String text) {
    if (looksLikePath(text)) {
      return new File(FileUtil.toSystemIndependentName(text)).getName();
    }
    return null;
  }

  /**
   * Suggests a quote to use for a {@code text} with a set of {@code defaultQuote} or non-default non-literal quotes
   *
   * @return a quote symbol or 0 if no quote been found
   */
  static char suggestOpenQuoteChar(@NotNull CharSequence text, char defaultQuote) {
    if (StringUtil.indexOf(text, defaultQuote) < 0) {
      return defaultQuote;
    }
    for (int i = 0; i < OPEN_QUOTES.length(); i++) {
      if (StringUtil.indexOf(text, OPEN_QUOTES.charAt(i)) < 0 && StringUtil.indexOf(text, CLOSE_QUOTES.charAt(i)) < 0) {
        return OPEN_QUOTES.charAt(i);
      }
    }
    for (int i = 0; i < ALTERNATIVE_QUOTES.length(); i++) {
      if (StringUtil.indexOf(text, ALTERNATIVE_QUOTES.charAt(i)) < 0) {
        return ALTERNATIVE_QUOTES.charAt(i);
      }
    }
    return 0;
  }

  /**
   * Choosing closing character by opening one
   *
   * @param charOpener - char with which sequence started
   * @return - ending char
   */
  static char getQuoteCloseChar(char charOpener) {
    int index = OPEN_QUOTES.indexOf(charOpener);
    return index < 0 ? charOpener : CLOSE_QUOTES.charAt(index);
  }
}
