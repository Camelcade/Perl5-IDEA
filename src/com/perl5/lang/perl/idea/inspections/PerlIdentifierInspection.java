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

package com.perl5.lang.perl.idea.inspections;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.psi.*;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 23.07.2016.
 */
public class PerlIdentifierInspection extends PerlInspection {
  @NotNull
  @Override
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
    return new PerlVisitor() {
      private boolean isUtf = false;
      private boolean isUtfComputed = false;

      private synchronized boolean isUtf(PsiElement element) {
        if (isUtfComputed) {
          return isUtf;
        }

        isUtfComputed = true;
        return isUtf = element.getContainingFile().getVirtualFile().getCharset() == CharsetToolkit.UTF8_CHARSET;
      }

      @Override
      public void visitSubNameElement(@NotNull PerlSubNameElement o) {
        if (!(o.getParent() instanceof PerlSubDefinitionElement)) {
          checkPerlIdentifier(o);
        }
        super.visitSubNameElement(o);
      }

      @Override
      public void visitNamespaceElement(@NotNull PerlNamespaceElement o) {
        if (!(o.getParent() instanceof PerlNamespaceDefinitionWithIdentifier)) {
          checkPerlIdentifier(o);
        }
        super.visitNamespaceElement(o);
      }

      @Override
      public void visitPerlNamespaceDefinitionWithIdentifier(@NotNull PerlNamespaceDefinitionWithIdentifier o) {
        checkPerlNamedElementIdentifier(o);
        super.visitPerlNamespaceDefinitionWithIdentifier(o);
      }

      @Override
      public void visitPerlSubDefinitionElement(@NotNull PerlSubDefinitionElement o) {
        checkPerlNamedElementIdentifier(o);
        super.visitPerlSubDefinitionElement(o);
      }

      @Override
      public void visitVariableNameElement(@NotNull PerlVariableNameElement o) {
        checkPerlIdentifier(o);
        super.visitVariableNameElement(o);
      }

      protected void checkPerlNamedElementIdentifier(@NotNull PsiNameIdentifierOwner namedElement) {
        PsiElement nameIdentifier = namedElement.getNameIdentifier();
        if (nameIdentifier != null) {
          checkPerlIdentifier(namedElement.getName(), nameIdentifier);
        }
      }


      protected void checkPerlIdentifier(@NotNull PsiElement element) {
        checkPerlIdentifier(element.getNode().getChars(), element);
      }

      protected void checkPerlIdentifier(CharSequence text, @NotNull PsiElement element) {
        if (text == null) {
          return;
        }

        boolean hasError = false;
        StringBuilder formattedIdentifier = new StringBuilder();
        if (!isUtf(element)) {
          int length = text.length();
          if (length == 0) {
            return;
          }
          for (int i = 0; i < length; i++) {
            char currentChar = text.charAt(i);
            String escapedChar;
            if (currentChar == '<') {
              escapedChar = "&lt;";
            }
            else if (currentChar == '>') {
              escapedChar = "&gt;";
            }
            else {
              escapedChar = "" + currentChar;
            }

            if (currentChar > 127) {
              hasError = true;
              formattedIdentifier.append("<b>");
              formattedIdentifier.append(escapedChar);
              formattedIdentifier.append("</b>");
            }
            else {
              formattedIdentifier.append(escapedChar);
            }
          }

          if (hasError) {
            registerError(holder, element, PerlBundle.message("perl.incorrect.ascii.identifier", formattedIdentifier.toString()));
          }
        }
      }
    };
  }
}
