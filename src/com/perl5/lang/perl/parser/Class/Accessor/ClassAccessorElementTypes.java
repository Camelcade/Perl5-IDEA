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

package com.perl5.lang.perl.parser.Class.Accessor;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.parser.Class.Accessor.elementTypes.ClassAccessorWrapperElementType;
import com.perl5.lang.perl.parser.elementTypes.PerlNestedCallElementType;
import com.perl5.lang.perl.parser.elementTypes.PerlTokenType;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlLightSubDefinitionElementType;

/**
 * Created by hurricup on 21.01.2016.
 */
public interface ClassAccessorElementTypes {
  IElementType CLASS_ACCESSOR_FBP = new PerlNestedCallElementType("CLASS_ACCESSOR_FBP");
  IStubElementType CLASS_ACCESSOR_METHOD = new PerlLightSubDefinitionElementType("CLASS_ACCESSOR_METHOD");
  // fixme must be AFTER METHOD
  IElementType CLASS_ACCESSOR_WRAPPER = new ClassAccessorWrapperElementType("CLASS_ACCESSOR_WRAPPER");
  IElementType CLASS_ACCESSOR_WRAPPER_RO = new ClassAccessorWrapperElementType("CLASS_ACCESSOR_WRAPPER_RO");
  IElementType CLASS_ACCESSOR_WRAPPER_WO = new ClassAccessorWrapperElementType("CLASS_ACCESSOR_WRAPPER_WO");

  IElementType RESERVED_MK_RO_ACCESSORS = new PerlTokenType("MK_RO_ACCESSORS");
  IElementType RESERVED_MK_WO_ACCESSORS = new PerlTokenType("MK_WO_ACCESSORS");
  IElementType RESERVED_MK_ACCESSORS = new PerlTokenType("MK_ACCESSORS");
  IElementType RESERVED_FOLLOW_BEST_PRACTICE = new PerlTokenType("FOLLOW_BEST_PRACTICE");
}
