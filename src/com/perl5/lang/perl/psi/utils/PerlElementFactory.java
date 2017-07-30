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

package com.perl5.lang.perl.psi.utils;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.fileTypes.PerlFileTypePackage;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import com.perl5.lang.perl.psi.impl.PerlNamespaceElementImpl;
import com.perl5.lang.perl.psi.impl.PerlStringContentElementImpl;
import com.perl5.lang.perl.psi.mixins.PerlNamespaceDefinitionMixin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.HEREDOC;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.HEREDOC_QQ;

public class PerlElementFactory {
  public static PsiElement createNewLine(Project project) {
    PerlFileImpl file = createFile(project, "\n");
    return file.getFirstChild();
  }


  public static PsiPerlDerefExpr createMethodCall(Project project, String packageName, String subName) {
    assert packageName != null;
    assert subName != null;

    PerlFileImpl file = createFile(project, String.format("%s->%s;", packageName, subName));
    PsiPerlDerefExpr def = PsiTreeUtil.findChildOfType(file, PsiPerlDerefExpr.class);
    assert def != null;
    return def;
  }

  public static PerlUseStatement createUseStatement(Project project, String packageName) {
    assert packageName != null;

    PerlFileImpl file = createFile(project, String.format("use %s;", packageName));
    PerlUseStatement def = PsiTreeUtil.findChildOfType(file, PerlUseStatement.class);
    assert def != null;
    return def;
  }

  // fixme probably we don't need package name and sub. just identifier
  public static PerlNamespaceElementImpl createPackageName(Project project, String name) {
    PerlFileImpl file = createFile(project, "package " + name + ";");
    PerlNamespaceDefinitionMixin def = PsiTreeUtil.findChildOfType(file, PerlNamespaceDefinitionMixin.class);
    assert def != null;
    return (PerlNamespaceElementImpl)def.getNamespaceElement();
  }

  /**
   * Create a replacement heredoc element with new text
   *
   * @param originalElement element to replace
   * @param newText         new element text
   * @return new generated element
   */
  @NotNull
  public static PerlHeredocElementImpl createHeredocBodyReplacement(@NotNull PerlHeredocElementImpl originalElement,
                                                                    @NotNull String newText) {
    StringBuilder sb = new StringBuilder("<<");
    String marker = getSafeHeredocMarker(newText);
    IElementType originalElementType = PsiUtilCore.getElementType(originalElement);
    char quote;
    if (originalElementType == HEREDOC) {
      quote = '\'';
    }
    else if (originalElementType == HEREDOC_QQ) {
      quote = '"';
    }
    else {
      quote = '`';
    }
    sb.append(quote).append(marker).append(quote).append(";\n").append(newText);
    if (!newText.endsWith("\n")) {
      sb.append("\n");
    }

    PerlFileImpl newFile = createFile(originalElement.getProject(), sb.append(marker).toString());
    PerlHeredocElementImpl newElement = newFile.findChildByClass(originalElement.getClass());
    assert newElement != null : "Can't find element " + originalElement.getClass() + " generated with: \n" + sb.toString();
    return newElement;
  }

  /**
   * @param heredocBody text of here-doc body
   * @return safe marker to use with this body
   */
  private static String getSafeHeredocMarker(@NotNull String heredocBody) {
    String marker = "EOM";
    while (marker.equals(heredocBody) ||
           heredocBody.startsWith(marker + "\n") ||
           heredocBody.endsWith(marker + "\n") ||
           heredocBody.contains("\n" + marker + "\n")
      ) {
      //noinspection StringConcatenationInLoop
      marker = marker + "M";
    }
    return marker;
  }

  public static List<PsiElement> createHereDocElements(Project project, char quoteSymbol, String markerText, String contentText) {
    PerlFileImpl file = createFile(project,
                                   String.format("<<%c%s%c\n%s\n%s\n;", quoteSymbol, markerText, quoteSymbol, contentText, markerText)
    );

    PsiElement heredocOpener = PsiTreeUtil.findChildOfType(file, PsiPerlHeredocOpener.class);
    @SuppressWarnings("ConstantConditions") PsiElement headingNewLine = heredocOpener.getNextSibling();
    PsiElement tailingNewLine = headingNewLine.getNextSibling().getNextSibling().getNextSibling();

    return new ArrayList<>(Arrays.asList(
      heredocOpener,
      headingNewLine,
      tailingNewLine
    ));
  }

  public static PerlStringContentElementImpl createStringContent(Project project, String name) {
    PerlFileImpl file = createFile(project, "'" + name + "';");
    PsiPerlStringSq string = PsiTreeUtil.findChildOfType(file, PsiPerlStringSq.class);
    assert string != null;
    return (PerlStringContentElementImpl)string.getFirstChild().getNextSibling();
  }

  public static PerlString createBareString(Project project, String content) {
    PerlFileImpl file = createFile(project, content + " => 42;");
    PerlString string = PsiTreeUtil.findChildOfType(file, PerlString.class);
    assert string != null : "While creating bare string from: " + content;
    return string;
  }

  public static PerlString createString(Project project, String code) {
    PerlFileImpl file = createFile(project, code + ";");
    PerlString string = PsiTreeUtil.findChildOfType(file, PerlString.class);
    assert string != null : "While creating bare string from: " + code;
    return string;
  }

  public static PsiElement createDereference(Project project) {
    PerlFileImpl file = createFile(project, "$a->{bla};");
    PerlVariable variable = PsiTreeUtil.findChildOfType(file, PerlVariable.class);
    assert variable != null : "While creating dereference";
    return variable.getNextSibling();
  }

  public static PsiPerlParenthesisedExpr createParenthesisedExpression(Project project) {
    PerlFileImpl file = createFile(project, "();");
    PsiPerlParenthesisedExpr result = PsiTreeUtil.findChildOfType(file, PsiPerlParenthesisedExpr.class);
    assert result != null : "While creating PsiPerlParenthesisedExpr";
    return result;
  }

  public static PerlFileImpl createFile(Project project, String text) {
    return createFile(project, text, PerlFileTypePackage.INSTANCE);
  }

  public static PerlFileImpl createFile(Project project, String text, FileType fileType) {
    return (PerlFileImpl)PsiFileFactory.getInstance(project).
      createFileFromText("file.dummy", fileType, text);
  }
}
