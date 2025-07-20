/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.perl.parser.elementTypes;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.ILeafElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.BiFunction;


public class PerlTokenTypeEx extends PerlTokenType implements ILeafElementType {
  private final BiFunction<IElementType, CharSequence, ASTNode> myInstanceFactory;

  public PerlTokenTypeEx(@NotNull @NonNls String debugName, Class<? extends ASTNode> clazz) {
    super(debugName);
    myInstanceFactory = createInstanceFactory(clazz);
  }

  public PerlTokenTypeEx(@NotNull String debugName,
                         @Nullable Language language,
                         @NotNull Class<? extends ASTNode> clazz) {
    super(debugName, language);
    myInstanceFactory = createInstanceFactory(clazz);
  }

  private static @NotNull BiFunction<IElementType, CharSequence, ASTNode> createInstanceFactory(Class<? extends ASTNode> clazz) {
    Constructor<? extends ASTNode> constructor;
    try {
      constructor = clazz.getDeclaredConstructor(IElementType.class, CharSequence.class);
      constructor.setAccessible(true);
    }
    catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }

    return (p1, p2) -> {
      try {
        return constructor.newInstance(p1, p2);
      }
      catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException(e);
      }
    };
  }

  @Override
  public @NotNull ASTNode createLeafNode(@NotNull CharSequence leafText) {
    return myInstanceFactory.apply(this, leafText);
  }
}
