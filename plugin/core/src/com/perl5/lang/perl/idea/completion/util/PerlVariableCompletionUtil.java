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

package com.perl5.lang.perl.idea.completion.util;

import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.idea.PerlCompletionWeighter;
import com.perl5.lang.perl.idea.completion.PerlInsertHandlers;
import com.perl5.lang.perl.idea.ui.PerlIconProvider;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlBuiltInVariable;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.references.PerlBuiltInVariablesService;
import com.perl5.lang.perl.psi.utils.PerlResolveUtil;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.Consumer;


public class PerlVariableCompletionUtil {

  @NotNull
  public static LookupElementBuilder createVariableLookupElement(@NotNull String name, @NotNull PerlVariableType variableType) {
    return LookupElementBuilder.create(PerlVariable.braceName(name)).withIcon(PerlIconProvider.getIcon(variableType));
  }

  @NotNull
  public static LookupElementBuilder createArrayElementLookupElement(@NotNull String name, @NotNull PerlVariableType variableType) {
    return createVariableLookupElement(name, variableType)
      .withInsertHandler(PerlInsertHandlers.ARRAY_ELEMENT_INSERT_HANDLER).withTailText("[]");
  }

  @NotNull
  public static LookupElementBuilder createHashElementLookupElement(@NotNull String name, @NotNull PerlVariableType variableType) {
    return createVariableLookupElement(name, variableType)
      .withInsertHandler(PerlInsertHandlers.HASH_ELEMENT_INSERT_HANDLER).withTailText("{}");
  }

  @NotNull
  private static String computeVariableName(@NotNull PerlVariableDeclarationElement element, boolean forceShortMain) {
    if (element.isGlobalDeclaration() && !(element instanceof PerlBuiltInVariable)) {
      return StringUtil.notNullize(PerlVariable.adjustName(element.getCanonicalName(), forceShortMain));
    }
    else {
      return PerlVariable.braceName(element.getVariableName());
    }
  }

  @NotNull
  public static LookupElementBuilder createVariableLookupElement(@NotNull PerlVariableDeclarationElement element, boolean forceShortMain) {
    return LookupElementBuilder.create(element, computeVariableName(element, forceShortMain))
      .withIcon(PerlIconProvider.getIcon(element));
  }

  @NotNull
  public static LookupElementBuilder createVariableLookupElement(@NotNull PerlGlobVariable typeGlob) {
    return LookupElementBuilder.create(typeGlob, StringUtil.notNullize(typeGlob.getCanonicalName()))
      .withIcon(PerlIconProvider.getIcon(typeGlob));
  }


  @NotNull
  public static LookupElementBuilder createHashElementLookupElement(@NotNull PerlVariableDeclarationElement element,
                                                                    boolean forceShortMain) {
    return createVariableLookupElement(element, forceShortMain)
      .withInsertHandler(PerlInsertHandlers.HASH_ELEMENT_INSERT_HANDLER).withTailText("{}");
  }

  @NotNull
  public static LookupElementBuilder createArrayElementLookupElement(@NotNull PerlVariableDeclarationElement element,
                                                                     boolean forceShortMain) {
    return createVariableLookupElement(element, forceShortMain)
      .withInsertHandler(PerlInsertHandlers.ARRAY_ELEMENT_INSERT_HANDLER).withTailText("[]");
  }

  public static void fillWithUnresolvedVars(@NotNull PerlVariableNameElement variableNameElement,
                                            @NotNull CompletionResultSet resultSet) {
    final PerlLexicalScope lexicalScope = PsiTreeUtil.getParentOfType(variableNameElement, PerlLexicalScope.class);
    PsiElement perlVariable = variableNameElement.getParent();
    final Set<String> collectedNames = new THashSet<>();

    if (lexicalScope != null && perlVariable instanceof PerlVariable) {
      final int minOffset = variableNameElement.getTextOffset();
      final PerlVariableType actualType = ((PerlVariable)perlVariable).getActualType();

      lexicalScope.accept(new PerlRecursiveVisitor() {
        @Override
        public void visitPerlVariable(@NotNull PerlVariable perlVariable) {
          if (perlVariable.isValid() &&
              !(perlVariable.getParent() instanceof PerlVariableDeclarationElement) &&
              perlVariable.getTextOffset() > minOffset &&
              actualType == perlVariable.getActualType()
            ) {
            String variableName = perlVariable.getName();
            if (StringUtil.isNotEmpty(variableName) &&
                !collectedNames.contains(variableName) &&
                perlVariable.getLexicalDeclaration() == null) {
              collectedNames.add(variableName);
              resultSet.addElement(LookupElementBuilder.create(variableName));
            }
          }
          super.visitPerlVariable(perlVariable);
        }
      });
    }
  }

  @NotNull
  public static <T extends LookupElement> T setLexical(@NotNull T element) {
    element.putUserData(PerlCompletionWeighter.WEIGHT, 1);
    return element;
  }

  public static void fillWithBuiltInVariables(@NotNull PsiElement variableNameElement,
                                              @NotNull CompletionResultSet resultSet) {
    PsiElement perlVariable = variableNameElement.getParent();
    Consumer<PerlVariableDeclarationElement> generator = createBuiltInLookupGenerator(perlVariable, resultSet::addElement);
    if (generator == null) {
      return;
    }

    PerlBuiltInVariablesService.getInstance(variableNameElement.getProject()).processVariables(it -> {
      generator.accept(it);
      return true;
    });
  }

  public static void fillWithLexicalVariables(@NotNull PsiElement variableNameElement,
                                              @NotNull final CompletionResultSet resultSet) {
    PsiElement perlVariable = variableNameElement.getParent();
    Consumer<PerlVariableDeclarationElement> lookupGenerator = createLexicalLookupGenerator(perlVariable, resultSet::addElement);

    if (lookupGenerator == null) {
      return;
    }

    PsiScopeProcessor processor = (element, __) -> {
      if (!(element instanceof PerlVariableDeclarationElement)) {
        return true;
      }
      PerlVariableDeclarationElement variable = (PerlVariableDeclarationElement)element;
      PsiElement declarationStatement = PsiTreeUtil.getParentOfType(variable, PerlStatement.class);
      if (PsiTreeUtil.isAncestor(declarationStatement, perlVariable, false)) {
        return true;
      }

      if (StringUtil.isNotEmpty(variable.getName())) {
        lookupGenerator.accept(variable);
      }

      return true;
    };
    PerlResolveUtil.treeWalkUp(variableNameElement, processor);
  }


  /**
   * @return lookup generator for lexical variables
   * @see #createLookupGenerator(PsiElement, Consumer)
   */
  @Nullable
  private static Consumer<PerlVariableDeclarationElement> createLexicalLookupGenerator(@Nullable PsiElement perlVariable,
                                                                                       @NotNull Consumer<LookupElementBuilder> lookupConsumer) {
    return createLookupGenerator(perlVariable, it -> lookupConsumer.accept(setLexical(it)));
  }

  /**
   * @return lookup generator for built-in variables
   * @see #createLookupGenerator(PsiElement, Consumer)
   */
  @Nullable
  private static Consumer<PerlVariableDeclarationElement> createBuiltInLookupGenerator(@Nullable PsiElement perlVariable,
                                                                                       @NotNull Consumer<LookupElementBuilder> lookupConsumer) {
    return createLookupGenerator(perlVariable, it -> lookupConsumer.accept(it.withBoldness(true)));
  }

  /**
   * @param perlVariable for which lookup is built (element under caret)
   * @return consumer of variable declarations, generating lookup elements for them and feeding to the {@code lookupConsumer}
   */
  @Nullable
  private static Consumer<PerlVariableDeclarationElement> createLookupGenerator(@Nullable PsiElement perlVariable,
                                                                                @NotNull Consumer<LookupElementBuilder> lookupConsumer) {
    if (perlVariable instanceof PsiPerlScalarVariable) {
      return variable -> {
        if (variable.getActualType() == PerlVariableType.SCALAR) {
          lookupConsumer.accept(createVariableLookupElement(variable, false));
        }
        else if (variable.getActualType() == PerlVariableType.ARRAY) {
          lookupConsumer.accept(createArrayElementLookupElement(variable, false));
        }
        else if (variable.getActualType() == PerlVariableType.HASH) {
          lookupConsumer.accept(createHashElementLookupElement(variable, false));
        }
      };
    }
    else if (perlVariable instanceof PsiPerlArrayVariable) {
      return variable -> {
        if (variable.getActualType() == PerlVariableType.ARRAY) {
          lookupConsumer.accept(createVariableLookupElement(variable, false));
          lookupConsumer.accept(createArrayElementLookupElement(variable, false));
        }
        else if (variable.getActualType() == PerlVariableType.HASH) {
          lookupConsumer.accept(createHashElementLookupElement(variable, false));
        }
      };
    }
    else if (perlVariable instanceof PsiPerlArrayIndexVariable) {
      return variable -> {
        if (variable.getActualType() == PerlVariableType.ARRAY) {
          lookupConsumer.accept(createVariableLookupElement(variable, false));
        }
      };
    }
    else if (perlVariable instanceof PsiPerlHashVariable) {
      return variable -> {
        if (variable.getActualType() == PerlVariableType.HASH) {
          lookupConsumer.accept(createVariableLookupElement(variable, false));
        }
      };
    }
    return null;
  }
}
