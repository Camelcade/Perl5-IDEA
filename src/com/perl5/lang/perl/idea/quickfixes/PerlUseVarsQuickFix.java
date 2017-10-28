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

package com.perl5.lang.perl.idea.quickfixes;

import com.intellij.codeInspection.LocalQuickFixOnPsiElement;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.psi.PerlUseStatement;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.PsiPerlStatement;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class PerlUseVarsQuickFix extends LocalQuickFixOnPsiElement {
  public PerlUseVarsQuickFix(@NotNull PerlUseStatement element) {
    super(element);
  }

  @NotNull
  @Override
  public String getText() {
    return PerlBundle.message("perl.quickfix.use.vars");
  }

  @Override
  public void invoke(@NotNull Project project, @NotNull PsiFile file, @NotNull PsiElement startElement, @NotNull PsiElement endElement) {
    Collection<PerlVariableDeclarationElement> declarations =
      PsiTreeUtil.findChildrenOfType(startElement, PerlVariableDeclarationElement.class);
    if (declarations.isEmpty()) {
      startElement.delete();
      return;
    }
    StringBuilder newCode = new StringBuilder("our");
    if (declarations.size() > 1) {
      newCode.append("(");
    }
    newCode.append(StringUtil.join(ContainerUtil.map(declarations, PsiElement::getText), ","));
    if (declarations.size() > 1) {
      newCode.append(")");
    }
    newCode.append(";");
    PerlFileImpl fakeFile = PerlElementFactory.createFile(myStartElement.getProject(), newCode.toString());
    PsiPerlStatement newElement = PsiTreeUtil.findChildOfType(fakeFile, PsiPerlStatement.class);
    if (newElement != null) {
      startElement.replace(newElement);
    }
  }

  @Nls
  @NotNull
  @Override
  public String getFamilyName() {
    return getName();
  }
}
