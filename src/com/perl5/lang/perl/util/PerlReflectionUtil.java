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

package com.perl5.lang.perl.util;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PerlReflectionUtil {
  @NotNull
  public static <R, P1> Function<P1, R> createInstanceFactory(Class<? extends R> clazz, Class<P1> parameterClazz) {
    Constructor<? extends R> constructor;
    try {
      constructor = clazz.getDeclaredConstructor(parameterClazz);
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
  public static <R, P1, P2> BiFunction<P1, P2, R> createInstanceFactory(Class<? extends R> clazz,
                                                                        Class<P1> parameterClazz1,
                                                                        Class<P2> parameterClazz2) {
    Constructor<? extends R> constructor;
    try {
      constructor = clazz.getDeclaredConstructor(parameterClazz1, parameterClazz2);
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
}
