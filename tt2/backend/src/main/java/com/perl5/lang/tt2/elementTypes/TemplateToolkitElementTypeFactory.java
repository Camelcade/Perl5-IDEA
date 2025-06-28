/*
 * Copyright 2015-2024 Alexandr Evstigneev
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

package com.perl5.lang.tt2.elementTypes;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.pod.elementTypes.PodElementTypeFactory;
import com.perl5.lang.tt2.psi.impl.*;
import org.jetbrains.annotations.NotNull;


public class TemplateToolkitElementTypeFactory extends PodElementTypeFactory {
  public static IElementType getTokenType(String debugName) {
    if (debugName.equals("TT2_HTML")) {
      return new TemplateToolkitTemplateTokenType();
    }
    return new TemplateToolkitTokenType(debugName);
  }

  public static IElementType getElementType(String name) {
    return switch (name) {
      case "ADD_EXPR" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiAddExprImpl(node);
        }
      };
      case "AND_EXPR" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiAndExprImpl(node);
        }
      };
      case "ANON_BLOCK" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiAnonBlockImpl(node);
        }
      };
      case "ANON_BLOCK_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiAnonBlockDirectiveImpl(node);
        }
      };
      case "ARRAY_EXPR" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiArrayExprImpl(node);
        }
      };
      case "ASSIGN_EXPR" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiAssignExprImpl(node);
        }
      };
      case "BLOCK_COMMENT" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiBlockCommentImpl(node);
        }
      };
      case "BLOCK_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiBlockDirectiveImpl(node);
        }
      };
      case "BLOCK_NAME" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiBlockNameImpl(node);
        }
      };
      case "CALL_ARGUMENTS" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiCallArgumentsImpl(node);
        }
      };
      case "CALL_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiCallDirectiveImpl(node);
        }
      };
      case "CALL_EXPR" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiCallExprImpl(node);
        }
      };
      case "CASE_BLOCK" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiCaseBlockImpl(node);
        }
      };
      case "CASE_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiCaseDirectiveImpl(node);
        }
      };
      case "CATCH_BRANCH" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiCatchBranchImpl(node);
        }
      };
      case "CATCH_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiCatchDirectiveImpl(node);
        }
      };
      case "CLEAR_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiClearDirectiveImpl(node);
        }
      };
      case "COMPARE_EXPR" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiCompareExprImpl(node);
        }
      };
      case "DEBUG_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiDebugDirectiveImpl(node);
        }
      };
      case "DEBUG_FORMAT" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiDebugFormatImpl(node);
        }
      };
      case "DEFAULT_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiDefaultDirectiveImpl(node);
        }
      };
      case "DIRECTIVE_POSTFIX" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiDirectivePostfixImpl(node);
        }
      };
      case "DQ_STRING_EXPR" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiDqStringExprImpl(node);
        }
      };
      case "ELSE_BRANCH" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiElseBranchImpl(node);
        }
      };
      case "ELSE_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiElseDirectiveImpl(node);
        }
      };
      case "ELSIF_BRANCH" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiElsifBranchImpl(node);
        }
      };
      case "ELSIF_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiElsifDirectiveImpl(node);
        }
      };
      case "END_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiEndDirectiveImpl(node);
        }
      };
      case "EQUAL_EXPR" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiEqualExprImpl(node);
        }
      };
      case "EXCEPTION_ARGS" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiExceptionArgsImpl(node);
        }
      };
      case "EXCEPTION_MESSAGE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiExceptionMessageImpl(node);
        }
      };
      case "EXCEPTION_TYPE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiExceptionTypeImpl(node);
        }
      };
      case "EXPR" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          throw new RuntimeException("Instantiating " + node);
        }
      };
      case "FILTER_BLOCK" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiFilterBlockImpl(node);
        }
      };
      case "FILTER_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiFilterDirectiveImpl(node);
        }
      };
      case "FILTER_ELEMENT_EXPR" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiFilterElementExprImpl(node);
        }
      };
      case "FINAL_BRANCH" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiFinalBranchImpl(node);
        }
      };
      case "FINAL_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiFinalDirectiveImpl(node);
        }
      };
      case "FOREACH_BLOCK" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiForeachBlockImpl(node);
        }
      };
      case "FOREACH_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiForeachDirectiveImpl(node);
        }
      };
      case "FOREACH_ITERABLE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiForeachIterableImpl(node);
        }
      };
      case "FOREACH_ITERATOR" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiForeachIteratorImpl(node);
        }
      };
      case "GET_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiGetDirectiveImpl(node);
        }
      };
      case "HASH_EXPR" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiHashExprImpl(node);
        }
      };
      case "IDENTIFIER_EXPR" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiIdentifierExprImpl(node);
        }
      };
      case "IF_BLOCK" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiIfBlockImpl(node);
        }
      };
      case "IF_BRANCH" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiIfBranchImpl(node);
        }
      };
      case "IF_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiIfDirectiveImpl(node);
        }
      };
      case "INCLUDE_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiIncludeDirectiveImpl(node);
        }
      };
      case "INSERT_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiInsertDirectiveImpl(node);
        }
      };
      case "LAST_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiLastDirectiveImpl(node);
        }
      };
      case "MACRO_CONTENT" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiMacroContentImpl(node);
        }
      };
      case "MACRO_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiMacroDirectiveImpl(node);
        }
      };
      case "MACRO_NAME" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiMacroNameImpl(node);
        }
      };
      case "META_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiMetaDirectiveImpl(node);
        }
      };
      case "MODULE_NAME" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiModuleNameImpl(node);
        }
      };
      case "MODULE_PARAMS" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiModuleParamsImpl(node);
        }
      };
      case "MUL_EXPR" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiMulExprImpl(node);
        }
      };
      case "NAMED_BLOCK" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiNamedBlockImpl(node);
        }
      };
      case "NEXT_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiNextDirectiveImpl(node);
        }
      };
      case "OR_EXPR" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiOrExprImpl(node);
        }
      };
      case "PAIR_EXPR" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiPairExprImpl(node);
        }
      };
      case "PARENTHESISED_EXPR" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiParenthesisedExprImpl(node);
        }
      };
      case "PERL_BLOCK" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiPerlBlockImpl(node);
        }
      };
      case "PERL_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiPerlDirectiveImpl(node);
        }
      };
      case "PROCESS_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiProcessDirectiveImpl(node);
        }
      };
      case "RANGE_EXPR" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiRangeExprImpl(node);
        }
      };
      case "RAWPERL_BLOCK" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiRawperlBlockImpl(node);
        }
      };
      case "RAWPERL_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiRawperlDirectiveImpl(node);
        }
      };
      case "RETURN_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiReturnDirectiveImpl(node);
        }
      };
      case "SET_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiSetDirectiveImpl(node);
        }
      };
      case "SQ_STRING_EXPR" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiSqStringExprImpl(node);
        }
      };
      case "STOP_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiStopDirectiveImpl(node);
        }
      };
      case "SWITCH_BLOCK" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiSwitchBlockImpl(node);
        }
      };
      case "SWITCH_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiSwitchDirectiveImpl(node);
        }
      };
      case "TAGS_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiTagsDirectiveImpl(node);
        }
      };
      case "TERM_EXPR" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiTermExprImpl(node);
        }
      };
      case "TERNAR_EXPR" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiTernarExprImpl(node);
        }
      };
      case "THROW_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiThrowDirectiveImpl(node);
        }
      };
      case "TRY_BRANCH" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiTryBranchImpl(node);
        }
      };
      case "TRY_CATCH_BLOCK" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiTryCatchBlockImpl(node);
        }
      };
      case "TRY_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiTryDirectiveImpl(node);
        }
      };
      case "UNARY_EXPR" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiUnaryExprImpl(node);
        }
      };
      case "UNLESS_BLOCK" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiUnlessBlockImpl(node);
        }
      };
      case "UNLESS_BRANCH" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiUnlessBranchImpl(node);
        }
      };
      case "UNLESS_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiUnlessDirectiveImpl(node);
        }
      };
      case "USE_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiUseDirectiveImpl(node);
        }
      };
      case "USE_INSTANCE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiUseInstanceImpl(node);
        }
      };
      case "VARIABLE_EXPR" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiVariableExprImpl(node);
        }
      };
      case "WHILE_BLOCK" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiWhileBlockImpl(node);
        }
      };
      case "WHILE_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiWhileDirectiveImpl(node);
        }
      };
      case "WRAPPER_BLOCK" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiWrapperBlockImpl(node);
        }
      };
      case "WRAPPER_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiWrapperDirectiveImpl(node);
        }
      };
      case "EMPTY_DIRECTIVE" -> new TemplateToolkitElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new TemplateToolkitCompositeElementImpl(node);
        }
      };
      default -> throw new RuntimeException("Missing element: " + name);
    };
  }
}
