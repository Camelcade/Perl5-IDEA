/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlScalarValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.psi.properties.PerlValuableEntity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.perl5.lang.perl.parser.PerlParserUtil.AMBIGUOUS_PACKAGE_PATTERN;


public interface PerlString extends PerlQuoted, PerlValuableEntity  {
  String FILE_PATH_PATTERN_TEXT = "\\.?[\\p{L}\\d\\-_]+(?:\\.[\\p{L}\\d\\-_]*)*";
  String FILE_PATH_DELIMITER_PATTERN_TEXT = "(?:\\\\+|/+)";
  Pattern FILE_PATH_PATTERN = Pattern.compile(
    FILE_PATH_DELIMITER_PATTERN_TEXT + "*" +
    "(?:" + FILE_PATH_PATTERN_TEXT + FILE_PATH_DELIMITER_PATTERN_TEXT + ")+ ?" +
    "(" + FILE_PATH_PATTERN_TEXT + ")" + FILE_PATH_DELIMITER_PATTERN_TEXT + "?"
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
  @NotNull
  default List<PsiElement> getAllChildrenList() {
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

  @NotNull
  @Override
  default PerlValue computePerlValue() {
    return PerlScalarValue.create(ElementManipulators.getValueText(this));
  }

  @Contract("null->false")
  static boolean looksLikePackage(@Nullable String text) {
    return text != null && StringUtil.containsAnyChar(text, ":'") && AMBIGUOUS_PACKAGE_PATTERN.matcher(text).matches();
  }

  @Contract("null->false")
  static boolean looksLikePath(@Nullable String text) {
    return text != null && FILE_PATH_PATTERN.matcher(text).matches();
  }

  @Nullable
  @Contract("null->null")
  static String getContentFileName(@Nullable String text) {
    if (looksLikePath(text)) {
      Matcher m = FILE_PATH_PATTERN.matcher(text);
      if (m.matches()) {
        return m.group(1);
      }
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
