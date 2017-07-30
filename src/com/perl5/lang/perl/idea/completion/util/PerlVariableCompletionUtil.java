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
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.PerlCompletionWeighter;
import com.perl5.lang.perl.idea.completion.PerlInsertHandlers;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.references.scopes.PerlVariableScopeProcessor;
import com.perl5.lang.perl.psi.utils.PerlResolveUtil;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.PerlArrayUtil;
import com.perl5.lang.perl.util.PerlGlobUtil;
import com.perl5.lang.perl.util.PerlHashUtil;
import com.perl5.lang.perl.util.PerlScalarUtil;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by hurricup on 25.07.2015.
 */
public class PerlVariableCompletionUtil {
  public static final List<LookupElementBuilder> BUILT_IN_SCALARS = new ArrayList<>();
  public static final List<LookupElementBuilder> BUILT_IN_ARRAYS = new ArrayList<>();
  public static final List<LookupElementBuilder> BUILT_IN_ARRAYS_INDEXES = new ArrayList<>();
  public static final List<LookupElementBuilder> BUILT_IN_HASHES = new ArrayList<>();
  public static final List<LookupElementBuilder> BUILT_IN_GLOBS = new ArrayList<>();

  // fill scalars
  static {
    for (String name : PerlScalarUtil.BUILT_IN) {
      BUILT_IN_SCALARS.add(getScalarLookupElement(name).withBoldness(true));
    }

    for (String name : PerlArrayUtil.BUILT_IN) {
      BUILT_IN_SCALARS.add(getArrayElementLookupElement(name).withBoldness(true));
    }

    for (String name : PerlHashUtil.BUILT_IN) {
      BUILT_IN_SCALARS.add(getHashElementLookupElement(name).withBoldness(true));
    }
  }

  // fill arrays
  static {
    for (String name : PerlArrayUtil.BUILT_IN) {
      BUILT_IN_ARRAYS.add(getArrayLookupElement(name).withBoldness(true));
    }

    for (String name : PerlHashUtil.BUILT_IN) {
      BUILT_IN_ARRAYS.add(getHashSliceLookupElement(name).withBoldness(true));
    }
  }

  // fill arrays indexes
  static {
    for (String name : PerlArrayUtil.BUILT_IN) {
      BUILT_IN_ARRAYS.add(getArrayLookupElement(name).withBoldness(true));
    }
  }

  // fill hashes
  static {
    for (String name : PerlHashUtil.BUILT_IN) {
      BUILT_IN_HASHES.add(getHashLookupElement(name).withBoldness(true));
    }
  }

  // fill globs
  static {
    // built-in globs
    for (String name : PerlGlobUtil.BUILT_IN) {
      BUILT_IN_GLOBS.add(getGlobLookupElement(name).withBoldness(true));
    }
  }

  @NotNull
  public static LookupElementBuilder getScalarLookupElement(String name) {
    return LookupElementBuilder
      .create(name)
      .withIcon(PerlIcons.SCALAR_GUTTER_ICON);
  }

  @NotNull
  public static LookupElementBuilder getGlobLookupElement(String name) {
    return LookupElementBuilder
      .create(name)
      .withIcon(PerlIcons.GLOB_GUTTER_ICON);
  }

  @NotNull
  public static LookupElementBuilder getArrayLookupElement(String name) {
    return LookupElementBuilder
      .create(name)
      .withIcon(PerlIcons.ARRAY_GUTTER_ICON);
  }

  @NotNull
  public static LookupElementBuilder getArrayElementLookupElement(String name) {
    return getArrayLookupElement(name)
      .withInsertHandler(PerlInsertHandlers.ARRAY_ELEMENT_INSERT_HANDLER)
      .withTailText("[]");
  }

  @NotNull
  public static LookupElementBuilder getHashLookupElement(String name) {
    return LookupElementBuilder
      .create(name)
      .withIcon(PerlIcons.HASH_GUTTER_ICON);
  }

  @NotNull
  public static LookupElementBuilder getHashElementLookupElement(String name) {
    return getHashLookupElement(name)
      .withInsertHandler(PerlInsertHandlers.HASH_ELEMENT_INSERT_HANDLER)
      .withTailText("{}");
  }

  @NotNull
  public static LookupElementBuilder getHashSliceLookupElement(String name) {
    return getHashLookupElement(name)
      .withInsertHandler(PerlInsertHandlers.HASH_ELEMENT_INSERT_HANDLER) // slice here
      .withTailText("{}");
  }

  public static void fillWithUnresolvedVars(final PerlVariableNameElement variableNameElement, final CompletionResultSet resultSet) {
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

  public static void fillWithBuiltInVariables(PsiElement variableNameElement, @NotNull CompletionResultSet resultSet) {
    PsiElement perlVariable = variableNameElement.getParent();

    if (perlVariable instanceof PsiPerlScalarVariable) {
      resultSet.addAllElements(BUILT_IN_SCALARS);
    }
    else if (perlVariable instanceof PsiPerlArrayVariable) {
      resultSet.addAllElements(BUILT_IN_ARRAYS);
    }
    else if (perlVariable instanceof PsiPerlArrayIndexVariable) {
      resultSet.addAllElements(BUILT_IN_ARRAYS_INDEXES);
    }
    else if (perlVariable instanceof PsiPerlHashVariable) {
      resultSet.addAllElements(BUILT_IN_HASHES);
    }
    else if (perlVariable instanceof PsiPerlGlobVariable) {
      resultSet.addAllElements(BUILT_IN_GLOBS);
    }
  }

  public static void fillWithLExicalVariables(
    PsiElement variableNameElement,
    @NotNull final CompletionResultSet resultSet) {
    final PsiElement perlVariable = variableNameElement.getParent();

    PsiScopeProcessor processor = new PerlVariableScopeProcessor() {
      @Override
      public boolean execute(@NotNull PsiElement element, @NotNull ResolveState state) {
        if (element instanceof PerlVariableDeclarationElement) {
          PerlVariableDeclarationElement variable = (PerlVariableDeclarationElement)element;

          PsiElement declarationStatement = PsiTreeUtil.getParentOfType(variable, PsiPerlStatement.class);
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
  }
}
