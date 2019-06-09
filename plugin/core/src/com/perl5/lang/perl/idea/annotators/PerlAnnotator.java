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

package com.perl5.lang.perl.idea.annotators;


import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.perl.parser.constant.psi.elementTypes.PerlConstantsWrapperElementType;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PsiPerlArrayIndexVariableImpl;
import com.perl5.lang.perl.psi.impl.PsiPerlArrayVariableImpl;
import com.perl5.lang.perl.psi.impl.PsiPerlHashVariableImpl;
import com.perl5.lang.perl.psi.impl.PsiPerlScalarVariableImpl;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.references.PerlSubReference;
import com.perl5.lang.perl.util.PerlSubUtil;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter.*;

public class PerlAnnotator extends PerlBaseAnnotator {
  private static final Map<Class<? extends PerlVariable>, TextAttributesKey> VARIABLE_KEYS_MAP = new THashMap<>();

  static {
    VARIABLE_KEYS_MAP.put(PsiPerlScalarVariableImpl.class, PERL_SCALAR_BUILTIN);
    VARIABLE_KEYS_MAP.put(PsiPerlArrayIndexVariableImpl.class, PERL_SCALAR_BUILTIN);
    VARIABLE_KEYS_MAP.put(PsiPerlHashVariableImpl.class, PERL_HASH_BUILTIN);
    VARIABLE_KEYS_MAP.put(PsiPerlArrayVariableImpl.class, PERL_ARRAY_BUILTIN);
  }

  @Override
  public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
    IElementType elementType = PsiUtilCore.getElementType(element);
    if (elementType == NYI_STATEMENT) {
      holder.createInfoAnnotation(element, "Unimplemented statement").setTextAttributes(CodeInsightColors.TODO_DEFAULT_ATTRIBUTES);
    }
    else if (element instanceof PerlGlobVariable && ((PerlGlobVariable)element).isBuiltIn()) {
      holder.createInfoAnnotation(element, null).setTextAttributes(PERL_GLOB_BUILTIN);
    }
    else if (element instanceof PerlVariable && ((PerlVariable)element).isBuiltIn()) {
      holder.createInfoAnnotation(element, null).setTextAttributes(VARIABLE_KEYS_MAP.get(element.getClass()));
    }
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
    else if (element instanceof PerlPolyNamedElement) {
      TextAttributesKey subAttribute = PerlSyntaxHighlighter.PERL_SUB_DEFINITION;
      if (elementType == PerlConstantsWrapperElementType.CONSTANT_WRAPPER) { // fixme some interface?
        subAttribute = PerlSyntaxHighlighter.PERL_CONSTANT;
      }
      for (PerlDelegatingLightNamedElement lightNamedElement : ((PerlPolyNamedElement)element).getLightElements()) {
        TextAttributesKey currentKey =
          lightNamedElement instanceof PerlSubDefinition ? subAttribute : PerlSyntaxHighlighter.PERL_PACKAGE_DEFINITION;
        PsiElement navigationElement = lightNamedElement.getNavigationElement();
        holder.createInfoAnnotation(ElementManipulators.getValueTextRange(navigationElement).shiftRight(lightNamedElement.getTextOffset()),
                                    null)
          .setEnforcedTextAttributes(adjustTextAttributes(currentScheme.getAttributes(currentKey), false));
      }
    }
    else if (elementType == SUB_NAME) //  instanceof PerlSubNameElement
    {
      PsiElement parent = element.getParent();
      if (parent instanceof PsiPerlSubDeclaration) {
        holder.createInfoAnnotation(element, null).setTextAttributes(PerlSyntaxHighlighter.PERL_SUB_DECLARATION);
      }
      else if (parent instanceof PerlSubDefinitionElement) {
        if (PerlSubUtil.SUB_AUTOLOAD.equals(((PerlSubNameElement)element).getName())) {
          holder.createInfoAnnotation(element, null).setTextAttributes(PerlSyntaxHighlighter.PERL_AUTOLOAD);
        }
        else {
          holder.createInfoAnnotation(element, null).setTextAttributes(PerlSyntaxHighlighter.PERL_SUB_DEFINITION);
        }
      }
      else if (parent instanceof PerlMethodCall) {
        // fixme don't we need to take multiple references here?
        PsiElement grandParent = parent.getParent();
        PerlNamespaceElement methodNamespace = ((PerlMethodCall)parent).getNamespaceElement();

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