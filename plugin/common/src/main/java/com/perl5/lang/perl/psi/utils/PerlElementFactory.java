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
import com.perl5.lang.perl.psi.impl.*;
import com.perl5.lang.perl.psi.mixins.PerlNamespaceDefinitionMixin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.HEREDOC;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.HEREDOC_QQ;

public final class PerlElementFactory {
  public static PsiElement createNewLine(Project project) {
    PerlFileImpl file = createFile(project, "\n");
    return file.getFirstChild();
  }


  @Contract(pure = true)
  public static @NotNull PsiPerlDerefExpr createMethodCall(@NotNull Project project,
                                                           @NotNull String packageName,
                                                           @NotNull String subName,
                                                           @NotNull String arguments) {
    PerlFileImpl file = createFile(project, String.format("%s->%s%s;", packageName, subName, arguments));
    return Objects.requireNonNull(PsiTreeUtil.findChildOfType(file, PsiPerlDerefExpr.class));
  }

  public static PerlUseStatementElement createUseStatement(Project project, String packageName) {
    assert packageName != null;

    PerlFileImpl file = createFile(project, String.format("use %s;", packageName));
    PerlUseStatementElement def = PsiTreeUtil.findChildOfType(file, PerlUseStatementElement.class);
    assert def != null;
    return def;
  }

  /**
   * @return a statement psi element created from {@code statementText} or null if something went wrong, statement could not be created
   */
  public static @Nullable PsiElement createStatement(@NotNull Project project, @NotNull String statementText) {
    PerlFileImpl perlFile = createFile(project, statementText + ";");
    PsiElement[] children = perlFile.getChildren();
    return children.length == 0 ? null : children[0];
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
  public static @NotNull PerlHeredocElementImpl createHeredocBodyReplacement(@NotNull PerlHeredocElementImpl originalElement,
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
    assert newElement != null : "Can't find element " + originalElement.getClass() + " generated with: \n" + sb;
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

    return List.of(heredocOpener, headingNewLine, tailingNewLine);
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
    assert string != null : "While creating string from: " + code;
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

  public static @NotNull PsiElement createSpace(@NotNull Project project) {
    return Objects.requireNonNull(createFile(project, " ").getFirstChild());
  }

  public static @NotNull PsiElement createComma(@NotNull Project project) {
    return Objects.requireNonNull(createFile(project, ",").getFirstChild());
  }

  public static PerlFileImpl createFile(@NotNull Project project, @NotNull String text) {
    return createFile(project, text, PerlFileTypePackage.INSTANCE);
  }

  public static PerlFileImpl createFile(@NotNull Project project, @NotNull String text, @NotNull FileType fileType) {
    return (PerlFileImpl)PsiFileFactory.getInstance(project).
      createFileFromText("file.dummy", fileType, text);
  }
}
