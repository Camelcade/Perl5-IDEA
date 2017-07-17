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

package com.perl5.lang.perl.parser.perlswitch;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.parser.elementTypes.PerlElementTypeEx;
import com.perl5.lang.perl.parser.perlswitch.psi.*;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 15.12.2015.
 */
public interface PerlSwitchElementTypes {
  IElementType SWITCH_COMPOUND = new PerlElementTypeEx("SWITCH_COMPOUND") {
    @NotNull
    @Override
    public PsiElement getPsiElement(@NotNull ASTNode node) {
      return new PerlSwitchCompoundStatementImpl(node);
    }
  };
  IElementType SWITCH_CONDITION = new PerlElementTypeEx("SWITCH_CONDITION") {
    @NotNull
    @Override
    public PsiElement getPsiElement(@NotNull ASTNode node) {
      return new PerlSwitchConditionImpl(node);
    }
  };
  IElementType CASE_COMPOUND = new PerlElementTypeEx("CASE_COMPOUND") {
    @NotNull
    @Override
    public PsiElement getPsiElement(@NotNull ASTNode node) {
      return new PerlCaseCompoundStatementImpl(node);
    }
  };
  IElementType CASE_DEFAULT = new PerlElementTypeEx("CASE_DEFAULT") {
    @NotNull
    @Override
    public PsiElement getPsiElement(@NotNull ASTNode node) {
      return new PerlCaseDefaultCompoundImpl(node);
    }
  };
  IElementType CASE_CONDITION = new PerlElementTypeEx("CASE_CONDITION") {
    @NotNull
    @Override
    public PsiElement getPsiElement(@NotNull ASTNode node) {
      return new PerlCaseConditionImpl(node);
    }
  };
}
