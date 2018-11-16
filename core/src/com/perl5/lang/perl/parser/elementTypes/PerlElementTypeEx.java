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

package com.perl5.lang.perl.parser.elementTypes;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.impl.PerlCompositeElementImpl;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

/**
 * Created by hurricup on 19.01.2016.
 */
public class PerlElementTypeEx extends PerlElementType implements PsiElementProvider {
  private final Function<ASTNode, PsiElement> myInstanceFactory;

  public PerlElementTypeEx(@NotNull @NonNls String debugName) {
    this(debugName, PerlCompositeElementImpl.class);
  }

  public PerlElementTypeEx(@NotNull @NonNls String debugName, Class<? extends PsiElement> clazz) {
    super(debugName);
    myInstanceFactory = createInstanceFactory(clazz);
  }

  @NotNull
  static Function<ASTNode, PsiElement> createInstanceFactory(Class<? extends PsiElement> clazz) {
    Constructor<? extends PsiElement> constructor;
    try {
      constructor = clazz.getDeclaredConstructor(ASTNode.class);
      constructor.setAccessible(true);
    }
    catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }

    return p1 -> {
      try {
        return constructor.newInstance(p1);
      }
      catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
        throw new RuntimeException(e);
      }
    };
  }

  @NotNull
  @Override
  public PsiElement getPsiElement(@NotNull ASTNode node) {
    return myInstanceFactory.apply(node);
  }
}