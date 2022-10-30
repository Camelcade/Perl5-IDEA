/*
 * Copyright 2015-2020 Alexandr Evstigneev
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
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.extensions.packageprocessor.impl.ConstantProcessor;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.*;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.references.PerlSubReference;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter.*;

public class PerlAnnotator extends PerlBaseAnnotator {
  private static final Map<Class<? extends PerlVariable>, TextAttributesKey> VARIABLE_KEYS_MAP = new HashMap<>();

  static {
    VARIABLE_KEYS_MAP.put(PsiPerlScalarVariableImpl.class, PERL_SCALAR_BUILTIN);
    VARIABLE_KEYS_MAP.put(PsiPerlArrayIndexVariableImpl.class, PERL_SCALAR_BUILTIN);
    VARIABLE_KEYS_MAP.put(PsiPerlHashVariableImpl.class, PERL_HASH_BUILTIN);
    VARIABLE_KEYS_MAP.put(PsiPerlArrayVariableImpl.class, PERL_ARRAY_BUILTIN);
  }

  @Override
  public void annotate(final @NotNull PsiElement element, @NotNull AnnotationHolder holder) {
    IElementType elementType = PsiUtilCore.getElementType(element);
    BiConsumer<String, TextAttributesKey> defaultAnnotationProducer = (msg, key) -> createInfoAnnotation(holder, element, msg, key);
    Consumer<TextAttributesKey> defaultSilentProducer = (key) -> createInfoAnnotation(holder, element, null, key);

    if (elementType == NYI_STATEMENT) {
      defaultAnnotationProducer.accept("Unimplemented statement", CodeInsightColors.TODO_DEFAULT_ATTRIBUTES);
    }
    else if (element instanceof PerlGlobVariable && ((PerlGlobVariable)element).isBuiltIn()) {
      defaultSilentProducer.accept(PERL_GLOB_BUILTIN);
    }
    else if (element instanceof PerlVariable && ((PerlVariable)element).isBuiltIn()) {
      defaultSilentProducer.accept(VARIABLE_KEYS_MAP.get(element.getClass()));
    }
    else if (elementType == LABEL_DECLARATION || elementType == LABEL_EXPR) {
      createInfoAnnotation(holder, element.getFirstChild(), null, PerlSyntaxHighlighter.PERL_LABEL);
    }
    else if (elementType == PACKAGE) {
      assert element instanceof PerlNamespaceElement;
      PerlNamespaceElement namespaceElement = (PerlNamespaceElement)element;

      PsiElement parent = namespaceElement.getParent();

      if (parent instanceof PerlNamespaceDefinitionWithIdentifier) {
        createInfoAnnotation(holder, namespaceElement, null, PerlSyntaxHighlighter.PERL_PACKAGE_DEFINITION);
      }
      else {
        if (namespaceElement.isPragma()) {
          createInfoAnnotation(holder, namespaceElement, null, PerlSyntaxHighlighter.PERL_PACKAGE_PRAGMA);
        }
        else if (namespaceElement.isBuiltin()) {
          createInfoAnnotation(holder, namespaceElement, null, PerlSyntaxHighlighter.PERL_PACKAGE_CORE);
        }
      }
    }
    else if (element instanceof PerlCharSubstitution) {
      int codePoint = ((PerlCharSubstitution)element).getCodePoint();
      if (codePoint >= 0) {
        StringBuilder sb = new StringBuilder("<ul>");
        String chars = ((PerlCharSubstitution)element).getNonIgnorableChars();
        if (StringUtil.isEmptyOrSpaces(chars)) {
          chars = PerlBundle.message("perl.annotator.char.non.printable");
        }

        sb.append("<li>").append(PerlBundle.message("perl.annotator.char.character")).append(": ")
          .append(chars).append(" (").append(Character.getName(codePoint)).append(")").append("</li>");
        sb.append("<li>").append(PerlBundle.message("perl.annotator.char.codepoint")).append(": ")
          .append("0x").append(Integer.toHexString(codePoint).toUpperCase()).append(" (").append(codePoint).append(")").append("</li>");
        sb.append("</ul>");

        holder.newAnnotation(HighlightSeverity.INFORMATION, chars).range(element).tooltip(sb.toString()).create();
      }
    }
    else if (element instanceof PerlPolyNamedElement) {
      TextAttributesKey subAttribute = PerlSyntaxHighlighter.PERL_SUB_DEFINITION;
      if (element instanceof PerlUseStatementElement &&
          ((PerlUseStatementElement)element).getPackageProcessor() instanceof ConstantProcessor) {
        subAttribute = PerlSyntaxHighlighter.PERL_CONSTANT;
      }
      for (PerlDelegatingLightNamedElement<?> lightNamedElement : ((PerlPolyNamedElement<?>)element).getLightElements()) {
        if (lightNamedElement.isImplicit()) {
          continue;
        }
        TextAttributesKey currentKey =
          lightNamedElement instanceof PerlSubDefinition ? subAttribute : PerlSyntaxHighlighter.PERL_PACKAGE_DEFINITION;
        PsiElement navigationElement = lightNamedElement.getNavigationElement();
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
          .range(ElementManipulators.getValueTextRange(navigationElement).shiftRight(lightNamedElement.getTextOffset()))
          .enforcedTextAttributes(adjustTextAttributes(currentScheme.getAttributes(currentKey), false))
          .create();
      }
    }
    else if (elementType == SUB_NAME) {
      PsiElement parent = element.getParent();
      if (parent instanceof PsiPerlSubDeclaration) {
        defaultSilentProducer.accept(PerlSyntaxHighlighter.PERL_SUB_DECLARATION);
      }
      else if (parent instanceof PerlSubDefinitionElement) {
        if (PerlSubUtil.SUB_AUTOLOAD.equals(((PerlSubNameElement)element).getName())) {
          defaultSilentProducer.accept(PerlSyntaxHighlighter.PERL_AUTOLOAD);
        }
        else {
          defaultSilentProducer.accept(PerlSyntaxHighlighter.PERL_SUB_DEFINITION);
        }
      }
      else if (parent instanceof PerlMethod) {
        // fixme don't we need to take multiple references here?
        PsiElement grandParent = parent.getParent();
        PerlNamespaceElement methodNamespace = ((PerlMethod)parent).getNamespaceElement();

        if (
          !PerlSubCallElement.isNestedCall(grandParent) /// not ...->method fixme shouldn't we use isObjectMethod here?
          && (methodNamespace == null || methodNamespace.isCORE())// no explicit NS or it's core
          && ((PerlSubNameElement)element).isBuiltIn()
        ) {
          createInfoAnnotation(holder, element, null, PerlSyntaxHighlighter.PERL_SUB_BUILTIN);
        }
        else {

          PsiReference reference = element.getReference();

          if (reference instanceof PerlSubReference) {
            ((PerlSubReference)reference).multiResolve(false);

            if (((PerlSubReference)reference).isConstant()) {
              defaultAnnotationProducer.accept("Constant", PerlSyntaxHighlighter.PERL_CONSTANT);
            }
            else if (((PerlSubReference)reference).isAutoloaded()) {
              defaultAnnotationProducer.accept("Auto-loaded sub", PerlSyntaxHighlighter.PERL_AUTOLOAD);
            }
            else if (((PerlSubReference)reference).isXSub()) {
              defaultAnnotationProducer.accept("XSub", PerlSyntaxHighlighter.PERL_XSUB);
            }
          }
        }
      }
    }
  }
}