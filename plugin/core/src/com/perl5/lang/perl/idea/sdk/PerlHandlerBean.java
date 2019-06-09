/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.sdk;

import com.intellij.openapi.extensions.AbstractExtensionPointBean;
import com.intellij.openapi.util.NotNullLazyValue;
import com.intellij.util.KeyedLazyInstance;
import com.intellij.util.KeyedLazyInstanceEP;
import com.intellij.util.xmlb.annotations.Attribute;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Copypaste from {@link KeyedLazyInstanceEP}
 */
public final class PerlHandlerBean extends AbstractExtensionPointBean
  implements KeyedLazyInstance<AbstractPerlHandler<?, ?>> {
  // these must be public for scrambling compatibility
  @Attribute("key")
  public String key;

  @Attribute("implementationClass")
  public String implementationClass;

  private final NotNullLazyValue<AbstractPerlHandler<?, ?>> myHandler = NotNullLazyValue.createValue(() -> {
    try {
      Class<AbstractPerlHandler<?, ?>> extensionClass = findClass(implementationClass);
      Constructor<AbstractPerlHandler<?, ?>> constructor = extensionClass.getConstructor(PerlHandlerBean.class);
      constructor.setAccessible(true);
      return constructor.newInstance(PerlHandlerBean.this);
    }
    catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  });

  @NotNull
  @Override
  public AbstractPerlHandler<?, ?> getInstance() {
    return myHandler.getValue();
  }

  @Override
  public String getKey() {
    return key;
  }
}
