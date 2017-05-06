/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.pod.parser.psi;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hurricup on 27.03.2016.
 * Pattern for searching through the pod documentation
 */
public class PodDocumentPattern {
  public static final int DEFAULT_MAX_LIST_LEVEL = 2;
  private int myListLevel = DEFAULT_MAX_LIST_LEVEL;    // this is default value
  private Pattern myItemPattern;    // pattern to search items
  private Pattern myHeadingPattern; // pattern to search headers
  private String myIndexKey;    // index key to search. If both defined - first wins
  private boolean exactMatch = false;    // set to true if need exact match, instead of startsWith

  private PodDocumentPattern() {

  }

  public boolean accepts(PsiElement element) {
    return element != null && (acceptsItem(element) || acceptsIndex(element) || acceptsHeading(element));
  }

  protected boolean acceptsItem(PsiElement element) {
    if (getItemPattern() != null && element instanceof PodSectionItem) {
      if (((PodSectionItem)element).getListLevel() < getListLevel()) {
        String title = ((PodTitledSection)element).getTitleText();
        if (StringUtil.isNotEmpty(title)) {
          Matcher matcher = getItemPattern().matcher(title);
          return exactMatch && matcher.matches() || matcher.lookingAt();
        }
      }
    }
    return false;
  }

  protected boolean acceptsHeading(PsiElement element) {
    if (getHeadingPattern() != null && element instanceof PodTitledSection && ((PodTitledSection)element).isHeading()) {
      if (((PodTitledSection)element).getListLevel() < getListLevel()) {
        String title = ((PodTitledSection)element).getTitleText();
        if (StringUtil.isNotEmpty(title)) {
          Matcher matcher = getHeadingPattern().matcher(title);
          return exactMatch && matcher.matches() || matcher.lookingAt();
        }
      }
    }
    return false;
  }

  protected boolean acceptsIndex(PsiElement element) {
    if (getIndexKey() != null && element instanceof PodFormatterX) {
      if (((PodFormatterX)element).getListLevel() < getListLevel()) {
        String elementText = ((PodFormatterX)element).getTitleText();

        return StringUtil.isNotEmpty(elementText) && getIndexKey().equals(elementText);
      }
    }
    return false;
  }

  public int getListLevel() {
    return myListLevel;
  }

  public void setListLevel(int myListLevel) {
    this.myListLevel = myListLevel;
  }

  public Pattern getItemPattern() {
    return myItemPattern;
  }

  public void setItemPattern(Pattern myItemPattern) {
    this.myItemPattern = myItemPattern;
  }

  public void setItemPattern(String itemPattern) {
    setItemPattern(StringUtil.isEmpty(itemPattern) ? null : Pattern.compile(Pattern.quote(itemPattern) + "(\\s|\\b|$)"));
  }

  public Pattern getHeadingPattern() {
    return myHeadingPattern;
  }

  public void setHeadingPattern(String pattern) {
    setHeadingPattern(StringUtil.isEmpty(pattern) ? null : Pattern.compile(Pattern.quote(pattern) + "(\\s|\\b|$)"));
  }

  public void setHeadingPattern(Pattern myHeadingPattern) {
    this.myHeadingPattern = myHeadingPattern;
  }

  public String getIndexKey() {
    return myIndexKey;
  }

  public void setIndexKey(String myIndexkey) {
    this.myIndexKey = myIndexkey;
  }

  public PodDocumentPattern withExactMatch() {
    exactMatch = true;
    return this;
  }

  public static PodDocumentPattern itemPattern(@NotNull String itemText) {
    PodDocumentPattern pattern = new PodDocumentPattern();
    pattern.setItemPattern(itemText);
    return pattern;
  }

  public static PodDocumentPattern indexPattern(@NotNull String itemText) {
    PodDocumentPattern pattern = new PodDocumentPattern();
    pattern.setIndexKey(itemText);
    return pattern;
  }

  public static PodDocumentPattern headingAndItemPattern(@NotNull String itemText) {
    PodDocumentPattern pattern = new PodDocumentPattern();
    pattern.setItemPattern(itemText);
    pattern.setHeadingPattern(itemText);
    return pattern;
  }
}

