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

package com.perl5.lang.perl.idea.completion.util;

import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.BaseScopeProcessor;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.PerlCompletionWeighter;
import com.perl5.lang.perl.idea.completion.PerlInsertHandlers;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.references.PerlBuiltInVariablesService;
import com.perl5.lang.perl.psi.utils.PerlResolveUtil;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Created by hurricup on 25.07.2015.
 */
public class PerlVariableCompletionUtil {
  @NotNull
  private static String braceName(@NotNull String name) {
    return name; // StringUtil.startsWithChar(name, '^') ? "{" + name + "}": name; fixme this should be smarter
  }

  @NotNull
  public static LookupElementBuilder getScalarLookupElement(@NotNull String name) {
    return LookupElementBuilder
      .create(braceName(name))
      .withIcon(PerlIcons.SCALAR_GUTTER_ICON);
  }

  @NotNull
  public static LookupElementBuilder getGlobLookupElement(@NotNull String name) {
    return LookupElementBuilder
      .create(braceName(name))
      .withIcon(PerlIcons.GLOB_GUTTER_ICON);
  }

  @NotNull
  public static LookupElementBuilder getArrayLookupElement(@NotNull String name) {
    return LookupElementBuilder
      .create(braceName(name))
      .withIcon(PerlIcons.ARRAY_GUTTER_ICON);
  }

  @NotNull
  public static LookupElementBuilder getHashLookupElement(@NotNull String name) {
    return LookupElementBuilder
      .create(braceName(name))
      .withIcon(PerlIcons.HASH_GUTTER_ICON);
  }

  @NotNull
  public static LookupElementBuilder getArrayElementLookupElement(@NotNull String name) {
    return getArrayLookupElement(name)
      .withInsertHandler(PerlInsertHandlers.ARRAY_ELEMENT_INSERT_HANDLER)
      .withTailText("[]");
  }

  @NotNull
  public static LookupElementBuilder getArraySliceLookupElement(@NotNull String name) {
    return getArrayLookupElement(name)
      .withInsertHandler(PerlInsertHandlers.ARRAY_ELEMENT_INSERT_HANDLER)
      .withTailText("[]");
  }

  @NotNull
  public static LookupElementBuilder getHashElementLookupElement(@NotNull String name) {
    return getHashLookupElement(name)
      .withInsertHandler(PerlInsertHandlers.HASH_ELEMENT_INSERT_HANDLER)
      .withTailText("{}");
  }

  @NotNull
  public static LookupElementBuilder getHashSliceLookupElement(@NotNull String name) {
    return getHashLookupElement(name)
      .withInsertHandler(PerlInsertHandlers.HASH_ELEMENT_INSERT_HANDLER) // slice here
      .withTailText("{}");
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

    if (perlVariable instanceof PsiPerlScalarVariable) {
      perlBuiltInVariablesService.processScalars(new BaseScopeProcessor() {
        @Override
        public boolean execute(@NotNull PsiElement element, @NotNull ResolveState state) {
          //noinspection ConstantConditions
          resultSet.addElement(getScalarLookupElement(((PerlVariableDeclarationElement)element).getName()).withBoldness(true));
          return true;
        }
      });

      perlBuiltInVariablesService.processArrays(new BaseScopeProcessor() {
        @Override
        public boolean execute(@NotNull PsiElement element, @NotNull ResolveState state) {
          //noinspection ConstantConditions
          resultSet.addElement(getArrayElementLookupElement(((PerlVariableDeclarationElement)element).getName()).withBoldness(true));
          return true;
        }
      });

      perlBuiltInVariablesService.processHashes(new BaseScopeProcessor() {
        @Override
        public boolean execute(@NotNull PsiElement element, @NotNull ResolveState state) {
          //noinspection ConstantConditions
          resultSet.addElement(getHashElementLookupElement(((PerlVariableDeclarationElement)element).getName()).withBoldness(true));
          return true;
        }
      });
    }
    else if (perlVariable instanceof PsiPerlArrayVariable) {
      perlBuiltInVariablesService.processArrays(new BaseScopeProcessor() {
        @Override
        public boolean execute(@NotNull PsiElement element, @NotNull ResolveState state) {
          //noinspection ConstantConditions
          resultSet.addElement(getArrayLookupElement(((PerlVariableDeclarationElement)element).getName()).withBoldness(true));
          //noinspection ConstantConditions
          resultSet.addElement(getArraySliceLookupElement(((PerlVariableDeclarationElement)element).getName()).withBoldness(true));
          return true;
        }
      });
      perlBuiltInVariablesService.processHashes(new BaseScopeProcessor() {
        @Override
        public boolean execute(@NotNull PsiElement element, @NotNull ResolveState state) {
          //noinspection ConstantConditions
          resultSet.addElement(getHashSliceLookupElement(((PerlVariableDeclarationElement)element).getName()).withBoldness(true));
          return true;
        }
      });
    }
    else if (perlVariable instanceof PsiPerlArrayIndexVariable) {
      perlBuiltInVariablesService.processArrays(new BaseScopeProcessor() {
        @Override
        public boolean execute(@NotNull PsiElement element, @NotNull ResolveState state) {
          //noinspection ConstantConditions
          resultSet.addElement(getArrayLookupElement(((PerlVariableDeclarationElement)element).getName()).withBoldness(true));
          return true;
        }
      });
    }
    else if (perlVariable instanceof PsiPerlHashVariable) {
      perlBuiltInVariablesService.processHashes(new BaseScopeProcessor() {
        @Override
        public boolean execute(@NotNull PsiElement element, @NotNull ResolveState state) {
          //noinspection ConstantConditions
          resultSet.addElement(getHashLookupElement(((PerlVariableDeclarationElement)element).getName()).withBoldness(true));
          return true;
        }
      });
    }
    else if (perlVariable instanceof PsiPerlGlobVariable) {
      perlBuiltInVariablesService.processGlobs(new BaseScopeProcessor() {
        @Override
        public boolean execute(@NotNull PsiElement element, @NotNull ResolveState state) {
          //noinspection ConstantConditions
          resultSet.addElement(getGlobLookupElement(((PerlVariableDeclarationElement)element).getName()).withBoldness(true));
          return true;
        }
      });
    }
  }

  public static void fillWithLExicalVariables(@NotNull PsiElement variableNameElement,
                                              @NotNull final CompletionResultSet resultSet) {
    final PsiElement perlVariable = variableNameElement.getParent();

    PsiScopeProcessor processor = new BaseScopeProcessor() {
      @Override
      public boolean execute(@NotNull PsiElement element, @NotNull ResolveState state) {
        if (element instanceof PerlVariableDeclarationElement) {
          PerlVariableDeclarationElement variable = (PerlVariableDeclarationElement)element;

          PsiElement declarationStatement = PsiTreeUtil.getParentOfType(variable, PerlStatement.class);
          if (declarationStatement != null && PsiTreeUtil.isAncestor(declarationStatement, perlVariable, false)) {
            return true;
          }

          if (perlVariable instanceof PsiPerlScalarVariable) {
            String variableName = variable.getName();
            if (variableName != null) {
              if (variable.getActualType() == PerlVariableType.SCALAR) {
                resultSet.addElement(setLexical(getScalarLookupElement(variableName)));
              }
              else if (variable.getActualType() == PerlVariableType.ARRAY) {
                resultSet.addElement(setLexical(getArrayElementLookupElement(variableName)));
              }
              else if (variable.getActualType() == PerlVariableType.HASH) {
                resultSet.addElement(setLexical(getHashElementLookupElement(variableName)));
              }
            }
          }
          else if (perlVariable instanceof PsiPerlArrayVariable) {
            String variableName = variable.getName();
            if (variableName != null) {
              if (variable.getActualType() == PerlVariableType.ARRAY) {
                resultSet.addElement(setLexical(getArrayLookupElement(variableName)));
                resultSet.addElement(setLexical(getArraySliceLookupElement(variableName)));
              }
              else if (variable.getActualType() == PerlVariableType.HASH) {
                resultSet.addElement(setLexical(getHashSliceLookupElement(variableName)));
              }
            }
          }
          else if (perlVariable instanceof PsiPerlArrayIndexVariable) {
            String variableName = variable.getName();
            if (variableName != null) {
              if (variable.getActualType() == PerlVariableType.ARRAY) {
                resultSet.addElement(setLexical(getArrayLookupElement(variableName)));
              }
            }
          }
          else if (perlVariable instanceof PsiPerlHashVariable) {
            String variableName = variable.getName();
            if (variableName != null) {
              if (variable.getActualType() == PerlVariableType.HASH) {
                resultSet.addElement(setLexical(getHashLookupElement(variableName)));
              }
            }
          }
        }

        return true;
      }
    };
    PerlResolveUtil.treeWalkUp(variableNameElement, processor);
    PerlBuiltInVariablesService.getInstance(variableNameElement.getProject()).processVariables(processor);
  }
}
