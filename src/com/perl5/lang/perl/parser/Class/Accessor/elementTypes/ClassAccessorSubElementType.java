/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.perl.parser.Class.Accessor.elementTypes;

import com.intellij.lang.ASTNode;
import com.perl5.lang.perl.parser.Class.Accessor.psi.impl.PerlClassAccessorSubImpl;
import com.perl5.lang.perl.parser.elementTypes.PerlTokenTypeEx;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 21.01.2016.
 */
public class ClassAccessorSubElementType extends PerlTokenTypeEx {
  public ClassAccessorSubElementType(@NotNull @NonNls String debugName) {
    super(debugName);
  }

  @NotNull
  @Override
  public ASTNode createLeafNode(CharSequence leafText) {
    return new PerlClassAccessorSubImpl(this, leafText);
  }
}
