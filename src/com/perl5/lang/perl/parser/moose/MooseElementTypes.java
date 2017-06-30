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

package com.perl5.lang.perl.parser.moose;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.parser.elementTypes.PerlElementTypeEx;
import com.perl5.lang.perl.parser.elementTypes.PerlTokenTypeEx;
import com.perl5.lang.perl.parser.moose.psi.elementTypes.PerlMooseAttributeWrapperElementType;
import com.perl5.lang.perl.parser.moose.psi.impl.*;
import com.perl5.lang.perl.parser.moose.stubs.PerlMooseOverrideElementType;
import com.perl5.lang.perl.parser.moose.stubs.augment.PerlMooseAugmentStatementElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 27.12.2015.
 */
public interface MooseElementTypes {
  IElementType RESERVED_INNER = new PerlTokenTypeEx("inner") {
    @NotNull
    @Override
    public ASTNode createLeafNode(CharSequence leafText) {
      return new PerlMooseKeywordSubNameElementImpl(this, leafText);
    }
  };
  IElementType RESERVED_SUPER = new PerlTokenTypeEx("super") {
    @NotNull
    @Override
    public ASTNode createLeafNode(CharSequence leafText) {
      return new PerlMooseKeywordSubNameElementImpl(this, leafText);
    }
  };

  IElementType RESERVED_WITH = new PerlMooseTokenType("MOOSE_WITH");
  IElementType RESERVED_EXTENDS = new PerlMooseTokenType("MOOSE_EXTENDS");
  IElementType RESERVED_META = new PerlMooseTokenType("MOOSE_META");
  IElementType RESERVED_OVERRIDE = new PerlMooseTokenType("MOOSE_OVERRIDE");
  IElementType RESERVED_AROUND = new PerlMooseTokenType("MOOSE_AROUND");
  IElementType RESERVED_AUGMENT = new PerlMooseTokenType("MOOSE_AUGMENT");
  IElementType RESERVED_AFTER = new PerlMooseTokenType("MOOSE_AFTER");
  IElementType RESERVED_BEFORE = new PerlMooseTokenType("MOOSE_BEFORE");
  IElementType RESERVED_HAS = new PerlMooseTokenType("MOOSE_HAS");


  IElementType MOOSE_STATEMENT_WITH = new PerlElementTypeEx("MOOSE_STATEMENT_WITH") {
    @NotNull
    @Override
    public PsiElement getPsiElement(@NotNull ASTNode node) {
      return new PerlMooseWithStatementImpl(node);
    }
  };
  IElementType MOOSE_STATEMENT_EXTENDS = new PerlElementTypeEx("MOOSE_STATEMENT_EXTENDS") {
    @NotNull
    @Override
    public PsiElement getPsiElement(@NotNull ASTNode node) {
      return new PerlMooseExtendsStatementImpl(node);
    }
  };
  IElementType MOOSE_STATEMENT_META = new PerlElementTypeEx("MOOSE_STATEMENT_META") {
    @NotNull
    @Override
    public PsiElement getPsiElement(@NotNull ASTNode node) {
      return new PerlMooseMetaStatementImpl(node);
    }
  };
  IElementType MOOSE_STATEMENT_AROUND = new PerlElementTypeEx("MOOSE_STATEMENT_AROUND") {
    @NotNull
    @Override
    public PsiElement getPsiElement(@NotNull ASTNode node) {
      return new PerlMooseAroundStatementImpl(node);
    }
  };
  IElementType MOOSE_STATEMENT_AFTER = new PerlElementTypeEx("MOOSE_STATEMENT_AFTER") {
    @NotNull
    @Override
    public PsiElement getPsiElement(@NotNull ASTNode node) {
      return new PerlMooseAfterStatementImpl(node);
    }
  };
  IElementType MOOSE_STATEMENT_BEFORE = new PerlElementTypeEx("MOOSE_STATEMENT_BEFORE") {
    @NotNull
    @Override
    public PsiElement getPsiElement(@NotNull ASTNode node) {
      return new PerlMooseBeforeStatementImpl(node);
    }
  };

  IElementType MOOSE_STATEMENT_AUGMENT = new PerlMooseAugmentStatementElementType("MOOSE_STATEMENT_AUGMENT");
  IElementType MOOSE_STATEMENT_OVERRIDE = new PerlMooseOverrideElementType("MOOSE_STATEMENT_OVERRIDE");
  IElementType MOOSE_ATTRIBUTE_WRAPPER = new PerlMooseAttributeWrapperElementType("ATTRIBUTE_WRAPPER");
  IElementType MOOSE_HAS_EXPR = new PerlElementTypeEx("MOOSE_HAS_EXPR") {
    @NotNull
    @Override
    public PsiElement getPsiElement(@NotNull ASTNode node) {
      return new PerlHasExpression(node);
    }
  };

  class PerlMooseTokenType extends PerlTokenTypeEx {
    public PerlMooseTokenType(@NotNull @NonNls String debugName) {
      super(debugName);
    }

    @NotNull
    @Override
    public ASTNode createLeafNode(CharSequence leafText) {
      return new PerlMooseKeywordElementImpl(this, leafText);
    }
  }
}
