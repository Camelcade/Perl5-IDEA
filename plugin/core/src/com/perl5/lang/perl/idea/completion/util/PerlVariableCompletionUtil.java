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

import java.util.Set;


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

  public static LookupElement setLexical(@NotNull LookupElement element) {
    element.putUserData(PerlCompletionWeighter.WEIGHT, 1);
    return element;
  }

  public static void fillWithBuiltInVariables(@NotNull PsiElement variableNameElement,
                                              @NotNull CompletionResultSet resultSet) {
    PsiElement perlVariable = variableNameElement.getParent();
    PerlBuiltInVariablesService perlBuiltInVariablesService = PerlBuiltInVariablesService.getInstance(variableNameElement.getProject());

    PsiScopeProcessor variableProcessor = (element, __) -> {
      resultSet.addElement(createVariableLookupElement((PerlVariableDeclarationElement)element, false).withBoldness(true));
      return true;
    };
    if (perlVariable instanceof PsiPerlScalarVariable) {
      perlBuiltInVariablesService.processScalars(variableProcessor);

      perlBuiltInVariablesService.processArrays((element, __) -> {
        resultSet
          .addElement(
            createArrayElementLookupElement((PerlVariableDeclarationElement)element, false).withBoldness(true).withPsiElement(element));
        return true;
      });

      perlBuiltInVariablesService.processHashes((element, __) -> {
        resultSet
          .addElement(
            createHashElementLookupElement((PerlVariableDeclarationElement)element, false).withBoldness(true).withPsiElement(element));
        return true;
      });
    }
    else if (perlVariable instanceof PsiPerlArrayVariable) {
      perlBuiltInVariablesService.processArrays((element, state) -> {
        resultSet.addElement(createVariableLookupElement((PerlVariableDeclarationElement)element, false).withBoldness(true));
        resultSet.addElement(createArrayElementLookupElement((PerlVariableDeclarationElement)element, false).withBoldness(true));
        return true;
      });
      perlBuiltInVariablesService.processHashes((element, state) -> {
        resultSet.addElement(createHashElementLookupElement((PerlVariableDeclarationElement)element, false).withBoldness(true));
        return true;
      });
    }
    else if (perlVariable instanceof PsiPerlArrayIndexVariable) {
      perlBuiltInVariablesService.processArrays(variableProcessor);
    }
    else if (perlVariable instanceof PsiPerlHashVariable) {
      perlBuiltInVariablesService.processHashes(variableProcessor);
    }
    else if (perlVariable instanceof PsiPerlGlobVariable) {
      perlBuiltInVariablesService.processGlobs(variableProcessor);
    }
  }

  public static void fillWithLexicalVariables(@NotNull PsiElement variableNameElement,
                                              @NotNull final CompletionResultSet resultSet) {
    final PsiElement perlVariable = variableNameElement.getParent();

    PsiScopeProcessor processor = (element, state) -> {
      if (element instanceof PerlVariableDeclarationElement) {
        PerlVariableDeclarationElement variable = (PerlVariableDeclarationElement)element;
        PsiElement declarationStatement = PsiTreeUtil.getParentOfType(variable, PerlStatement.class);
        if (PsiTreeUtil.isAncestor(declarationStatement, perlVariable, false)) {
          return true;
        }

        if (perlVariable instanceof PsiPerlScalarVariable) {
          if (variable.getName() != null) {
            if (variable.getActualType() == PerlVariableType.SCALAR) {
              resultSet.addElement(setLexical(createVariableLookupElement(variable, false)));
            }
            else if (variable.getActualType() == PerlVariableType.ARRAY) {
              resultSet.addElement(setLexical(createArrayElementLookupElement(variable, false)));
            }
            else if (variable.getActualType() == PerlVariableType.HASH) {
              resultSet.addElement(setLexical(createHashElementLookupElement(variable, false)));
            }
          }
        }
        else if (perlVariable instanceof PsiPerlArrayVariable) {
          if (variable.getName() != null) {
            if (variable.getActualType() == PerlVariableType.ARRAY) {
              resultSet.addElement(setLexical(createVariableLookupElement(variable, false)));
              resultSet.addElement(setLexical(createArrayElementLookupElement(variable, false)));
            }
            else if (variable.getActualType() == PerlVariableType.HASH) {
              resultSet.addElement(setLexical(createHashElementLookupElement(variable, false)));
            }
          }
        }
        else if (perlVariable instanceof PsiPerlArrayIndexVariable) {
          if (variable.getName() != null && variable.getActualType() == PerlVariableType.ARRAY) {
            resultSet.addElement(setLexical(createVariableLookupElement(variable, false)));
          }
        }
        else if (perlVariable instanceof PsiPerlHashVariable) {
          if (variable.getName() != null) {
            if (variable.getActualType() == PerlVariableType.HASH) {
              resultSet.addElement(setLexical(createVariableLookupElement(variable, false)));
            }
          }
        }
      }

      return true;
    };
    PerlResolveUtil.treeWalkUp(variableNameElement, processor);
    PerlBuiltInVariablesService.getInstance(variableNameElement.getProject()).processVariables(processor);
  }
}
