/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.psi.util.*;
import com.intellij.util.Processor;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager;
import com.perl5.lang.perl.parser.PerlElementTypesGenerated;
import com.perl5.lang.perl.psi.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;

public class PerlVariableAnnotations {
  private static final PerlVariableAnnotations EMPTY = new PerlVariableAnnotations() {
    @Override
    public void setIsDeprecated() {
      throw new UnsupportedOperationException();
    }

    @Override
    public void setValue(@NotNull PerlValue value) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
      return true;
    }
  };

  private static final byte IS_DEPRECATED = 0x01;

  private byte myFlags = 0;

  private @NotNull PerlValue myValue = UNKNOWN_VALUE;

  private PerlVariableAnnotations() {
  }

  private PerlVariableAnnotations(byte flags, @NotNull PerlValue value) {
    myFlags = flags;
    myValue = value;
  }

  public void serialize(@NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeByte(myFlags);
    myValue.serialize(dataStream);
  }

  public boolean isDeprecated() {
    return (myFlags & IS_DEPRECATED) == IS_DEPRECATED;
  }

  public void setIsDeprecated() {
    myFlags |= IS_DEPRECATED;
  }

  public @NotNull PerlValue getAnnotatedValue() {
    return myValue;
  }

  public void setValue(@NotNull PerlValue value) {
    myValue = value;
  }

  public boolean isEmpty() {
    return myFlags == 0 && myValue.isUnknown();
  }

  public static PerlVariableAnnotations empty() {
    return EMPTY;
  }

  public static PerlVariableAnnotations deserialize(@NotNull StubInputStream dataStream) throws IOException {
    return new PerlVariableAnnotations(
      dataStream.readByte(),
      PerlValuesManager.readValue(dataStream)
    );
  }

  public static @NotNull PerlVariableAnnotations from(@NotNull PerlVariableDeclarationElement variableDeclarationElement) {
    PerlVariableAnnotations annotations = new PerlVariableAnnotations();
    processAnnotations(variableDeclarationElement, new VariableAnnotationProcessor() {
      @Override
      public boolean processDeprecated() {
        annotations.setIsDeprecated();
        return true;
      }

      @Override
      public boolean processType(@NotNull PerlAnnotationType annotationType) {
        annotations.setValue(annotationType.getValue());
        return true;
      }
    });
    return annotations.isEmpty() ? EMPTY : annotations;
  }

  private static @NotNull List<PerlAnnotation> collectAnnotationsInScope(@NotNull PerlVariableDeclarationElement variableDeclarationElement) {
    return CachedValuesManager.getCachedValue(variableDeclarationElement, () -> {
      List<PerlAnnotation> perlAnnotations = PerlAnnotations.collectAnnotations(variableDeclarationElement);
      var declarationExpression = variableDeclarationElement.getDeclarationExpression();
      if (perlAnnotations.isEmpty() && declarationExpression != null) {
        perlAnnotations = PerlAnnotations.collectAnnotations(declarationExpression);
      }
      if (perlAnnotations.isEmpty() && declarationExpression != null &&
          PsiUtilCore.getElementType(declarationExpression.getParent()) == PerlElementTypesGenerated.FOREACH_ITERATOR) {
        perlAnnotations =
          PerlAnnotations.collectAnnotations(PsiTreeUtil.getParentOfType(variableDeclarationElement, PerlForeachCompound.class));
      }
      if (perlAnnotations.isEmpty() &&
          PsiUtilCore.getElementType(variableDeclarationElement.getParent()) == PerlElementTypesGenerated.SIGNATURE_ELEMENT) {
        perlAnnotations = ContainerUtil.filter(
          PerlAnnotations.collectAnnotations(PsiTreeUtil.getParentOfType(variableDeclarationElement, PerlSubElement.class)),
          it -> !(it instanceof PsiPerlAnnotationDeprecated));
      }
      return CachedValueProvider.Result.create(perlAnnotations, PsiModificationTracker.MODIFICATION_COUNT);
    });
  }

  public static boolean processAnnotations(@NotNull PerlVariableDeclarationElement variableDeclarationElement,
                                           @NotNull PerlVariableAnnotations.VariableAnnotationProcessor annotationsProcessor) {

    var annotationList = collectAnnotationsInScope(variableDeclarationElement);
    if (annotationList.isEmpty()) {
      return true;
    }
    boolean valueSet = false;
    boolean valueSetExplicitly = false;
    for (PerlAnnotation annotation : annotationList) {
      if (annotation instanceof PsiPerlAnnotationDeprecated) {
        if (!annotationsProcessor.processDeprecated()) {
          return false;
        }
      }
      else if (!valueSetExplicitly && annotation instanceof PerlAnnotationType typeAnnotation &&
               typeAnnotation.accept(variableDeclarationElement.getVariable())) {
        var isWildCard = typeAnnotation.isWildCard();
        if (!valueSet || !isWildCard) {
          valueSet = true;
          valueSetExplicitly = !isWildCard;
          if (!annotationsProcessor.processType(typeAnnotation)) {
            return false;
          }
        }
      }
    }
    return true;
  }

  @Override
  public String toString() {
    return isEmpty() ? "none" : "PerlVariableAnnotations{" +
                                "myFlags=" + myFlags +
                                ", myValue=" + myValue +
                                '}';
  }

  /**
   * Processes variable declarations to which {@code typeAnnotation} may belong
   */
  public static boolean processPotentialTargets(@NotNull PerlAnnotationType typeAnnotation,
                                                @NotNull Processor<? super PerlVariableDeclarationElement> declarationElementProcessor) {
    PsiElement run = typeAnnotation.getAnnotationContainer();
    while (run != null && PerlParserDefinition.WHITE_SPACE_AND_REAL_COMMENTS.contains(PsiUtilCore.getElementType(run))) {
      run = run.getNextSibling();
    }

    if (run instanceof PerlForeachCompound foreachCompound) {
      run = foreachCompound.getForeachIterator();
    }
    else if (run instanceof PerlSubElement subElement) {
      run = subElement.getSignatureContent();
    }
    else if (!(run instanceof PsiPerlExpr || run instanceof PsiPerlStatement || run instanceof PsiPerlVariableDeclarationElement)) {
      return true;
    }

    if (run == null) {
      return true;
    }

    var visitor = new PerlRecursiveVisitor() {
      @Override
      public void visitVariableDeclarationElement(@NotNull PsiPerlVariableDeclarationElement o) {
        if (!declarationElementProcessor.process(o)) {
          stop();
        }
      }

      @Override
      public void visitBlock(@NotNull PsiPerlBlock o) {
        // do not enter blocks
      }
    };
    run.accept(visitor);

    return !visitor.isStopped();
  }

  public interface VariableAnnotationProcessor {
    @SuppressWarnings("SameReturnValue")
    default boolean processDeprecated() {
      return true;
    }

    default boolean processType(@NotNull PerlAnnotationType annotationType) {
      return true;
    }
  }
}
