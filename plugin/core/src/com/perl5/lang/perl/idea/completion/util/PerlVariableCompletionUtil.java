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
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Processor;
import com.perl5.lang.perl.idea.PerlCompletionWeighter;
import com.perl5.lang.perl.idea.completion.PerlInsertHandlers;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.idea.ui.PerlIconProvider;
import com.perl5.lang.perl.internals.PerlVersion;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlBuiltInVariable;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.references.PerlBuiltInVariablesService;
import com.perl5.lang.perl.psi.utils.PerlResolveUtil;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.*;
import gnu.trove.THashSet;
import org.jetbrains.annotations.Contract;
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
    return PerlVariable.braceName(element.getVariableName());
  }

  /**
   * @param sigilToPrepend '_' means we don't need to prepend
   */
  @NotNull
  public static LookupElementBuilder createVariableLookupElement(@NotNull PerlVariableDeclarationElement element,
                                                                 boolean forceShortMain,
                                                                 char sigilToPrepend) {
    String variableName = computeVariableName(element, forceShortMain);
    String lookupString = sigilToPrepend == '_' ? variableName :
                          sigilToPrepend + variableName;
    LookupElementBuilder elementBuilder = LookupElementBuilder.create(element, lookupString)
      .withIcon(PerlIconProvider.getIcon(element));
    return sigilToPrepend == '_' ? elementBuilder : elementBuilder.withLookupString(variableName);
  }

  @NotNull
  public static LookupElementBuilder createVariableLookupElement(@NotNull PerlGlobVariable typeGlob, boolean withSigil) {
    String variableName = StringUtil.notNullize(typeGlob.getCanonicalName());
    String lookupString = withSigil ?
                          PerlVariableType.GLOB.getSigil() + variableName :
                          variableName;

    LookupElementBuilder elementBuilder = LookupElementBuilder.create(typeGlob, lookupString).withIcon(PerlIconProvider.getIcon(typeGlob));
    return withSigil ? elementBuilder.withLookupString(variableName) : elementBuilder;
  }


  @NotNull
  public static LookupElementBuilder createHashElementLookupElement(@NotNull PerlVariableDeclarationElement element,
                                                                    boolean forceShortMain,
                                                                    char sigilToPrepend) {
    return createVariableLookupElement(element, forceShortMain, sigilToPrepend)
      .withInsertHandler(PerlInsertHandlers.HASH_ELEMENT_INSERT_HANDLER).withTailText("{}");
  }

  @NotNull
  public static LookupElementBuilder createArrayElementLookupElement(@NotNull PerlVariableDeclarationElement element,
                                                                     boolean forceShortMain,
                                                                     char sigilToPrepend) {
    return createVariableLookupElement(element, forceShortMain, sigilToPrepend)
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
   * @see #createLookupGenerator(PsiElement, Consumer, boolean)
   */
  @Nullable
  private static Consumer<PerlVariableDeclarationElement> createLexicalLookupGenerator(@Nullable PsiElement perlVariable,
                                                                                       @NotNull Consumer<LookupElementBuilder> lookupConsumer) {
    return createLookupGenerator(perlVariable, it -> lookupConsumer.accept(setLexical(it)), false);
  }

  /**
   * @return lookup generator for built-in variables
   * @see #createLookupGenerator(PsiElement, Consumer, boolean)
   */
  @Nullable
  private static Consumer<PerlVariableDeclarationElement> createBuiltInLookupGenerator(@Nullable PsiElement perlVariable,
                                                                                       @NotNull Consumer<LookupElementBuilder> lookupConsumer) {
    return createLookupGenerator(perlVariable, it -> lookupConsumer.accept(it.withBoldness(true)), false);
  }

  /**
   * @param perlVariable for which lookup is built (element under caret)
   * @param forceShortMain if true and if fqn is in the {@code main} namespace, use a short form {@code ::}
   * @return consumer of variable declarations, generating lookup elements for them and feeding to the {@code lookupConsumer}
   */
  @Contract("null,_,_->null")
  @Nullable
  private static Consumer<PerlVariableDeclarationElement> createLookupGenerator(@Nullable PsiElement perlVariable,
                                                                                @NotNull Consumer<LookupElementBuilder> lookupConsumer,
                                                                                boolean forceShortMain) {
    if (perlVariable == null) {
      return null;
    }
    boolean hashHashSlices = hasHashSlices(perlVariable);
    if (perlVariable instanceof PsiPerlMethod || perlVariable instanceof PsiPerlPerlHandleExpr) {
      return variable -> {
        PerlVariableType variableType = variable.getActualType();
        if (variableType == PerlVariableType.SCALAR) {
          lookupConsumer.accept(createVariableLookupElement(variable, forceShortMain, PerlVariableType.SCALAR.getSigil()));
        }
        else if (variableType == PerlVariableType.ARRAY) {
          lookupConsumer.accept(createVariableLookupElement(variable, forceShortMain, PerlVariableType.ARRAY.getSigil()));
          lookupConsumer.accept(createArrayElementLookupElement(variable, forceShortMain, PerlVariableType.SCALAR.getSigil()));
          lookupConsumer.accept(createArrayElementLookupElement(variable, forceShortMain, PerlVariableType.ARRAY.getSigil()));
        }
        else if (variable.getActualType() == PerlVariableType.HASH) {
          lookupConsumer.accept(createVariableLookupElement(variable, forceShortMain, PerlVariableType.HASH.getSigil()));
          lookupConsumer.accept(createHashElementLookupElement(variable, forceShortMain, PerlVariableType.SCALAR.getSigil()));
          lookupConsumer.accept(createHashElementLookupElement(variable, forceShortMain, PerlVariableType.ARRAY.getSigil()));
        }
      };
    }
    if (perlVariable instanceof PsiPerlScalarVariable) {
      return variable -> {
        if (variable.getActualType() == PerlVariableType.SCALAR) {
          lookupConsumer.accept(createVariableLookupElement(variable, forceShortMain, '_'));
        }
        else if (variable.getActualType() == PerlVariableType.ARRAY) {
          lookupConsumer.accept(createArrayElementLookupElement(variable, forceShortMain, '_'));
        }
        else if (variable.getActualType() == PerlVariableType.HASH) {
          lookupConsumer.accept(createHashElementLookupElement(variable, forceShortMain, '_'));
        }
      };
    }
    else if (perlVariable instanceof PsiPerlArrayVariable) {
      return variable -> {
        if (variable.getActualType() == PerlVariableType.ARRAY) {
          lookupConsumer.accept(createVariableLookupElement(variable, forceShortMain, '_'));
          lookupConsumer.accept(createArrayElementLookupElement(variable, forceShortMain, '_'));
        }
        else if (variable.getActualType() == PerlVariableType.HASH) {
          lookupConsumer.accept(createHashElementLookupElement(variable, forceShortMain, '_'));
        }
      };
    }
    else if (perlVariable instanceof PsiPerlArrayIndexVariable) {
      return variable -> {
        if (variable.getActualType() == PerlVariableType.ARRAY) {
          lookupConsumer.accept(createVariableLookupElement(variable, forceShortMain, '_'));
        }
      };
    }
    else if (perlVariable instanceof PsiPerlHashVariable) {
      return variable -> {
        PerlVariableType variableType = variable.getActualType();
        if (variableType == PerlVariableType.HASH) {
          lookupConsumer.accept(createVariableLookupElement(variable, forceShortMain, '_'));
          if (hashHashSlices) {
            lookupConsumer.accept(createHashElementLookupElement(variable, forceShortMain, '_'));
          }
        }
        else if (hashHashSlices && variableType == PerlVariableType.ARRAY) {
          lookupConsumer.accept(createArrayElementLookupElement(variable, forceShortMain, '_'));
        }
      };
    }
    else if (perlVariable instanceof PerlGlobVariable) {
      return variable -> lookupConsumer.accept(createVariableLookupElement(variable, forceShortMain, '_'));
    }
    return null;
  }

  /**
   * @return true iff 5.20 hash/array slices are enabled
   */
  private static boolean hasHashSlices(@NotNull PsiElement psiElement) {
    return !PerlSharedSettings.getInstance(psiElement).getTargetPerlVersion().lesserThan(PerlVersion.V5_20);
  }

  public static void fillWithFullQualifiedVariables(@NotNull PsiElement variableNameElement, @NotNull CompletionResultSet resultSet) {
    PsiElement perlVariable = variableNameElement.getParent();
    Consumer<PerlVariableDeclarationElement> lookupGenerator = createLookupGenerator(
      perlVariable, resultSet::addElement,
      StringUtil.startsWith(variableNameElement.getNode().getChars(), PerlPackageUtil.NAMESPACE_SEPARATOR));
    if (lookupGenerator == null) {
      return;
    }

    Processor<PerlVariableDeclarationElement> lookupGeneratorProcessor = it -> {
      lookupGenerator.accept(it);
      return true;
    };

    Project project = variableNameElement.getProject();
    GlobalSearchScope resolveScope = variableNameElement.getResolveScope();

    final CompletionResultSet finalResultSet = resultSet;

    if (perlVariable instanceof PsiPerlScalarVariable) {
      PerlScalarUtil.processDefinedGlobalScalars(project, resolveScope, lookupGeneratorProcessor);
      PerlArrayUtil.processDefinedGlobalArrays(project, resolveScope, lookupGeneratorProcessor);
      PerlHashUtil.processDefinedGlobalHashes(project, resolveScope, lookupGeneratorProcessor);
    }
    else if (perlVariable instanceof PerlGlobVariable || perlVariable instanceof PsiPerlMethod) {
      PerlScalarUtil.processDefinedGlobalScalars(project, resolveScope, lookupGeneratorProcessor);
      PerlArrayUtil.processDefinedGlobalArrays(project, resolveScope, lookupGeneratorProcessor);
      PerlHashUtil.processDefinedGlobalHashes(project, resolveScope, lookupGeneratorProcessor);

      // globs
      PerlGlobUtil.processDefinedGlobsNames(project, resolveScope, typeglob -> {
        finalResultSet.addElement(createVariableLookupElement(typeglob, perlVariable instanceof PsiPerlMethod));
        return true;
      });
    }
    else if (perlVariable instanceof PsiPerlArrayVariable) {
      PerlArrayUtil.processDefinedGlobalArrays(project, resolveScope, lookupGeneratorProcessor);
      PerlHashUtil.processDefinedGlobalHashes(project, resolveScope, lookupGeneratorProcessor);
    }
    else if (perlVariable instanceof PsiPerlArrayIndexVariable) {
      // global arrays
      PerlArrayUtil.processDefinedGlobalArrays(project, resolveScope, lookupGeneratorProcessor);
    }
    else if (perlVariable instanceof PsiPerlHashVariable) {
      // global hashes
      PerlHashUtil.processDefinedGlobalHashes(project, resolveScope, lookupGeneratorProcessor);
      if (hasHashSlices(perlVariable)) {
        PerlArrayUtil.processDefinedGlobalArrays(project, resolveScope, lookupGeneratorProcessor);
      }
    }
  }
}
