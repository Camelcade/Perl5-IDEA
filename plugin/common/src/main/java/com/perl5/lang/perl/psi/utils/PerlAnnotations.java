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

import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.Processor;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlScalarValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.references.PerlImplicitDeclarationsProvider;
import com.perl5.lang.perl.util.PerlPackageUtilCore;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;


public final class PerlAnnotations implements PerlElementTypes {
  private PerlAnnotations() {
  }

  public static final Map<String, IElementType> TOKENS_MAP = Map.of(
    "deprecated", ANNOTATION_DEPRECATED_KEY,
    "returns", ANNOTATION_RETURNS_KEY,
    "type", ANNOTATION_TYPE_KEY,
    "method", ANNOTATION_METHOD_KEY,
    "inject", ANNOTATION_INJECT_KEY,
    "noinject", ANNOTATION_NO_INJECT_KEY,

    // these are parsed but not used
    "override", ANNOTATION_OVERRIDE_KEY,
    "abstract", ANNOTATION_ABSTRACT_KEY,
    "noinspection", ANNOTATION_NOINSPECTION_KEY
  );

  /**
   * @return true iff injection for {@code host} is suppressed with {@code #@noinject} annotations
   */
  public static boolean isInjectionSuppressed(@NotNull PsiElement host) {
    return getAnyAnnotationByClass(host, PsiPerlAnnotationNoInject.class) != null;
  }

  /**
   * Requires some generic approach with method in `see` section
   *
   * @see PerlImplicitDeclarationsProvider#readReturnValue(Element)
   */
  private static @Nullable String getReturnClass(@NotNull PerlAnnotationWithValue annotation) {
    PerlNamespaceElement namespaceElement = PsiTreeUtil.getChildOfType(annotation, PerlNamespaceElement.class);
    if (namespaceElement != null) {
      return namespaceElement.getCanonicalName();
    }

    PsiElement run = annotation.getFirstChild();
    while (run != null) {
      if (PsiUtilCore.getElementType(run) == OPERATOR_MUL) {
        return PerlPackageUtilCore.NAMESPACE_ANY;
      }
      run = run.getNextSibling();
    }

    return null;
  }

  /**
   * @return value desscribing annotation parameter: classname, wildcard, etc.
   */
  public static PerlValue getParameterValue(@NotNull PerlAnnotationWithValue annotation) {
    String returnClass = getReturnClass(annotation);
    return returnClass == null ? UNKNOWN_VALUE :
      returnClass.equals(PerlPackageUtilCore.NAMESPACE_ANY) ?
        PerlPackageUtilCore.NAMESPACE_ANY_VALUE.get() :
        PerlScalarValue.create(returnClass);
  }

  /**
   * Gets annotation before element, statement
   *
   * @param element         element to search for
   * @param annotationClass annotation class
   * @return annotation or null
   */
  public static @Nullable <T extends PerlAnnotation> T getAnyAnnotationByClass(@NotNull PsiElement element,
                                                                               final Class<T> annotationClass) {
    // before element
    T annotation = getAnnotationByClass(element, annotationClass);
    if (annotation != null) {
      return annotation;
    }

    // before statement
    PsiPerlStatement statement = PsiTreeUtil.getParentOfType(element, PsiPerlStatement.class);
    if (statement != null) {
      return getAnnotationByClass(statement, annotationClass);
    }
    return null;
  }

  public static @Nullable <T extends PerlAnnotation> T getAnnotationByClass(@NotNull PsiElement element, final Class<T> annotationClass) {
    final PerlAnnotation[] result = new PerlAnnotation[]{null};
    processElementAnnotations(element, perlAnnotation -> {
      if (annotationClass.isInstance(perlAnnotation)) {
        result[0] = perlAnnotation;
        return false;
      }
      return true;
    });
    //noinspection unchecked
    return (T)result[0];
  }

  /**
   * Checking the tree before the {@code element} and looking for annotations in code. Each annotation is processed by {@code annotationProcessor}
   */
  public static void processElementAnnotations(@NotNull PsiElement element,
                                               @NotNull Processor<? super PerlAnnotation> annotationProcessor) {
    if (element instanceof PsiFile) {
      return;
    }
    PsiElement run = element.getPrevSibling();

    PsiElement parentElement = element.getParent();
    IElementType parentElementType = PsiUtilCore.getElementType(parentElement);
    boolean isListItem = parentElementType == STRING_LIST;
    if (run == null || isListItem) {
      run = isListItem ? parentElement : element;
      int elementOffset = element.getNode().getStartOffset();
      while (true) {
        PsiElement parent = run.getParent();
        if (parent != null && !(parent instanceof PsiFile) && parent.getNode().getStartOffset() == elementOffset) {
          run = parent;
        }
        else {
          break;
        }
      }

      run = run.getPrevSibling();
    }

    while (run != null) {
      if (run instanceof PerlAnnotationContainer annotationContainer) {
        PerlAnnotation annotation = annotationContainer.getAnnotation();
        if (annotation != null) {
          if (!annotationProcessor.process(annotation)) {
            return;
          }
        }
      }
      else if (!(run instanceof PsiComment || run instanceof PsiWhiteSpace)) {
        return;
      }
      run = run.getPrevSibling();
    }
  }

  public static @NotNull List<PerlAnnotation> collectAnnotations(@Nullable PsiElement element) {
    if (element == null) {
      return Collections.emptyList();
    }

    final List<PerlAnnotation> result = new ArrayList<>();
    processElementAnnotations(element, perlAnnotation -> {
      result.add(perlAnnotation);
      return true;
    });
    ContainerUtil.sort(result, Comparator.comparingInt(PsiElement::getTextOffset));
    return result;
  }
}
