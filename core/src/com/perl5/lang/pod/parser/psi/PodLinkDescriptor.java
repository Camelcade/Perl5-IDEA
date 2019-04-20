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

package com.perl5.lang.pod.parser.psi;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiFile;
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil;
import com.perl5.lang.pod.psi.PsiLinkName;
import com.perl5.lang.pod.psi.PsiLinkSection;
import com.perl5.lang.pod.psi.PsiLinkText;
import com.perl5.lang.pod.psi.PsiLinkUrl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 27.03.2016.
 * Builds pod link from the string
 */
public class PodLinkDescriptor {
  private static final Logger LOG = Logger.getInstance(PodLinkDescriptor.class);

  @Nullable
  private final String myName;
  @Nullable
  private final String mySection;
  @Nullable
  private final String myText;
  @Nullable
  private final String myHtmlText;

  private final boolean myIsUrl;

  private final boolean myIsSameFile;

  private PodLinkDescriptor(@Nullable String name,
                            @Nullable String section,
                            @Nullable String text,
                            @Nullable String htmlText,
                            boolean isUrl,
                            boolean isSameFile) {
    myText = text == null ? null : StringUtil.replace(text, "\n", " ");
    myHtmlText = htmlText;
    myName = name;
    mySection = section == null ? null : StringUtil.replace(section, "\n", " ");
    myIsUrl = isUrl;
    myIsSameFile = isSameFile;
  }

  @Override
  public String toString() {
    return super.toString() + getLink();
  }

  @Nullable
  public String getName() {
    return myName;
  }

  @Nullable
  public String getSection() {
    return mySection;
  }

  @NotNull
  public String getText() {
    return myText != null ? myText : buildTitle(myName, mySection, myIsSameFile);
  }

  @NotNull
  public String getHtmlText() {
    return myHtmlText != null ? myHtmlText : getText();
  }

  public boolean isUrl() {
    return myIsUrl;
  }

  public boolean isSameFile() {
    return myIsSameFile;
  }

  @NotNull
  public String getLink() {
    return buildLink(myName, mySection);
  }

  @NotNull
  public static PodLinkDescriptor create(@NotNull String file, @Nullable String section) {
    return new PodLinkDescriptor(file, section, null, null, false, false);
  }

  @NotNull
  private static String buildLink(@Nullable String file, @Nullable String section) {
    if (file == null) {
      return section == null ? "" : "/" + section;
    }
    return file + buildLink(null, section);
  }

  @NotNull
  private static String buildTitle(@Nullable String file, @Nullable String section, boolean isSameFile) {
    if (file == null) {
      return StringUtil.notNullize(section);
    }
    return section == null ? file : isSameFile ? section : (section + " in " + file);
  }

  @Nullable
  public static PodLinkDescriptor create(@NotNull PodFormatterL formatterL) {
    PsiLinkText textElement = formatterL.getLinkTextElement();
    PsiLinkName nameElement = formatterL.getLinkNameElement();
    PsiLinkSection sectionElement = formatterL.getLinkSectionElement();
    PsiLinkUrl linkUrlElement = formatterL.getLinkUrlElement();
    if (nameElement == null && sectionElement == null && linkUrlElement == null) {
      return null;
    }

    String linkTextText = null;
    String linkTextHtml = null;
    String linkName = null;
    String linkSection = null;
    boolean isUrl = false;
    boolean isSameFile = false;
    if (textElement != null) {
      linkTextHtml = PodRenderUtil.renderPsiElementAsHTML(textElement);
      linkTextText = PodRenderUtil.renderPsiElementAsText(textElement);
    }

    if (linkUrlElement != null) {
      isUrl = true;
      linkName = linkUrlElement.getText();
      if (linkTextText == null) {
        linkTextText = linkTextHtml = linkName;
      }
    }
    else if (sectionElement != null) {
      linkSection = sectionElement.getText();
      if (linkTextText == null) {
        linkTextHtml = PodRenderUtil.renderPsiElementAsHTML(sectionElement);
        linkTextText = PodRenderUtil.renderPsiElementAsText(sectionElement);
        if (nameElement != null) {
          linkName = nameElement.getText();
          linkTextHtml = buildTitle(linkName, linkTextHtml, false);
          linkTextText = buildTitle(linkName, linkTextText, false);
        }
      }
    }

    if (linkName == null) {
      if (nameElement != null) {
        linkName = nameElement.getText();
      }
      else {
        PsiFile containingFile = formatterL.getContainingFile();
        if (containingFile instanceof PodLinkTarget) {
          linkName = ((PodLinkTarget)containingFile).getPodLink();
          isSameFile = true;
        }
      }
    }

    return new PodLinkDescriptor(linkName, linkSection, linkTextText, linkTextHtml, isUrl, isSameFile);
  }

  @NotNull
  public static PodLinkDescriptor createFromUrl(@NotNull String link) {
    int fileIdEndOffset = link.indexOf('/');
    if (fileIdEndOffset == -1) {
      return create(link, null);
    }

    String file = link.substring(0, fileIdEndOffset);
    int sectionStartOffset = fileIdEndOffset + 1;
    String section = null;
    if (sectionStartOffset < link.length()) {
      section = link.substring(sectionStartOffset);
    }
    return create(file, section);
  }
}
