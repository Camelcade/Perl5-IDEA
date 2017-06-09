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

package com.perl5.lang.perl.idea.annotators;

/**
 * Created by hurricup on 25.04.2015.
 */

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.references.PerlSubReference;
import org.jetbrains.annotations.NotNull;

public class PerlAnnotator extends PerlBaseAnnotator {
  @Override
  public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
    IElementType elementType = PsiUtilCore.getElementType(element);
    if (elementType == NYI_STATEMENT) {
      holder.createInfoAnnotation(element, "Unimplemented statement").setTextAttributes(CodeInsightColors.TODO_DEFAULT_ATTRIBUTES);
    }
/*
                else if (elementType == REGEX_CHAR_CLASS && element.getTextLength() == 1 && ((LeafPsiElement) element).getChars().charAt(0) == '-')
		{
			holder.createInfoAnnotation(element, null).setTextAttributes(PerlSyntaxHighlighter.PERL_REGEX_CHAR_CLASS);
		}
*/
    else if (elementType == LABEL_DECLARATION || elementType == LABEL_EXPR) {
      holder.createInfoAnnotation(element.getFirstChild(), null).setTextAttributes(PerlSyntaxHighlighter.PERL_LABEL);
    }
    else if (elementType == PACKAGE) {
      assert element instanceof PerlNamespaceElement;
      PerlNamespaceElement namespaceElement = (PerlNamespaceElement)element;

      PsiElement parent = namespaceElement.getParent();

      if (parent instanceof PerlNamespaceDefinitionWithIdentifier) {
        decorateElement(namespaceElement, holder, PerlSyntaxHighlighter.PERL_PACKAGE_DEFINITION, false);
      }
      else {
        if (namespaceElement.isPragma()) {
          decorateElement(namespaceElement, holder, PerlSyntaxHighlighter.PERL_PACKAGE_PRAGMA, false);
        }
        else if (namespaceElement.isBuiltin()) {
          decorateElement(namespaceElement, holder, PerlSyntaxHighlighter.PERL_PACKAGE_CORE, false);
        }
      }
    }
    else if (elementType == CONSTANT_DEFINITION) {
      assert element instanceof PerlConstantDefinition;
      PsiElement nameIdentifier = ((PerlConstantDefinition)element).getNameIdentifier();
      if (nameIdentifier != null) {
        decorateElement(nameIdentifier, holder, PerlSyntaxHighlighter.PERL_CONSTANT, false);
      }
    }
    else if (elementType == SUB_NAME) //  instanceof PerlSubNameElement
    {
      PsiElement parent = element.getParent();
      if (parent instanceof PsiPerlSubDeclaration) {
        holder.createInfoAnnotation(element, null).setTextAttributes(PerlSyntaxHighlighter.PERL_SUB_DECLARATION);
      }
      else if (parent instanceof PerlSubDefinition) {
        if ("AUTOLOAD".equals(((PerlSubNameElement)element).getName())) {
          holder.createInfoAnnotation(element, null).setTextAttributes(PerlSyntaxHighlighter.PERL_AUTOLOAD);
        }
        else {
          holder.createInfoAnnotation(element, null).setTextAttributes(PerlSyntaxHighlighter.PERL_SUB_DEFINITION);
        }
      }
      else if (parent instanceof PerlMethod) {
        // fixme don't we need to take multiple references here?
        PsiElement grandParent = parent.getParent();
        PerlNamespaceElement methodNamespace = ((PerlMethod)parent).getNamespaceElement();

        if (
          !(grandParent instanceof PsiPerlNestedCall)    /// not ...->method fixme shouldn't we use isObjectMethod here?
          && (methodNamespace == null || methodNamespace.isCORE())    // no explicit NS or it's core
          && ((PerlSubNameElement)element).isBuiltIn()
          ) {
          decorateElement(element, holder, PerlSyntaxHighlighter.PERL_SUB_BUILTIN);
        }
        else {

          PsiReference reference = element.getReference();

          if (reference instanceof PerlSubReference) {
            ((PerlSubReference)reference).multiResolve(false);

            if (((PerlSubReference)reference).isConstant()) {
              holder.createInfoAnnotation(element, "Constant").setTextAttributes(PerlSyntaxHighlighter.PERL_CONSTANT);
            }
            else if (((PerlSubReference)reference).isAutoloaded()) {
              holder.createInfoAnnotation(element, "Auto-loaded sub").setTextAttributes(PerlSyntaxHighlighter.PERL_AUTOLOAD);
            }
            else if (((PerlSubReference)reference).isXSub()) {
              holder.createInfoAnnotation(element, "XSub").setTextAttributes(PerlSyntaxHighlighter.PERL_XSUB);
            }
          }
        }
      }
    }
  }
}