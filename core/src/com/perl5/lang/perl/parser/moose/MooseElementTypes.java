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
  IElementType RESERVED_INNER = new PerlTokenTypeEx("inner", PerlMooseKeywordSubNameElementImpl.class);
  IElementType RESERVED_SUPER = new PerlTokenTypeEx("super", PerlMooseKeywordSubNameElementImpl.class);

  IElementType RESERVED_WITH = new PerlMooseTokenType("MOOSE_WITH");
  IElementType RESERVED_EXTENDS = new PerlMooseTokenType("MOOSE_EXTENDS");
  IElementType RESERVED_META = new PerlMooseTokenType("MOOSE_META");
  IElementType RESERVED_OVERRIDE = new PerlMooseTokenType("MOOSE_OVERRIDE");
  IElementType RESERVED_AROUND = new PerlMooseTokenType("MOOSE_AROUND");
  IElementType RESERVED_AUGMENT = new PerlMooseTokenType("MOOSE_AUGMENT");
  IElementType RESERVED_AFTER = new PerlMooseTokenType("MOOSE_AFTER");
  IElementType RESERVED_BEFORE = new PerlMooseTokenType("MOOSE_BEFORE");
  IElementType RESERVED_HAS = new PerlMooseTokenType("MOOSE_HAS");

  IElementType MOOSE_STATEMENT_WITH = new PerlElementTypeEx("MOOSE_STATEMENT_WITH", PerlMooseWithStatementImpl.class);
  IElementType MOOSE_STATEMENT_EXTENDS = new PerlElementTypeEx("MOOSE_STATEMENT_EXTENDS", PerlMooseExtendsStatementImpl.class);
  IElementType MOOSE_STATEMENT_META = new PerlElementTypeEx("MOOSE_STATEMENT_META", PerlMooseMetaStatementImpl.class);
  IElementType MOOSE_STATEMENT_AROUND = new PerlElementTypeEx("MOOSE_STATEMENT_AROUND", PerlMooseAroundStatementImpl.class);
  IElementType MOOSE_STATEMENT_AFTER = new PerlElementTypeEx("MOOSE_STATEMENT_AFTER", PerlMooseAfterStatementImpl.class);
  IElementType MOOSE_STATEMENT_BEFORE = new PerlElementTypeEx("MOOSE_STATEMENT_BEFORE", PerlMooseBeforeStatementImpl.class);

  IElementType MOOSE_STATEMENT_AUGMENT = new PerlMooseAugmentStatementElementType("MOOSE_STATEMENT_AUGMENT");
  IElementType MOOSE_STATEMENT_OVERRIDE = new PerlMooseOverrideElementType("MOOSE_STATEMENT_OVERRIDE");
  IElementType MOOSE_ATTRIBUTE_WRAPPER = new PerlMooseAttributeWrapperElementType("ATTRIBUTE_WRAPPER");
  IElementType MOOSE_HAS_EXPR = new PerlElementTypeEx("MOOSE_HAS_EXPR", PerlHasExpression.class);

  class PerlMooseTokenType extends PerlTokenTypeEx {
    public PerlMooseTokenType(@NotNull @NonNls String debugName) {
      super(debugName, PerlMooseKeywordElementImpl.class);
    }
  }
}
