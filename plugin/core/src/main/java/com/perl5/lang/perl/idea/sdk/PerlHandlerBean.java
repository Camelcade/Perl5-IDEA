/*
 * Copyright 2015-2022 Alexandr Evstigneev
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

import com.intellij.ide.plugins.cl.PluginAwareClassLoader;
import com.intellij.openapi.components.ComponentManager;
import com.intellij.openapi.extensions.PluginDescriptor;
import com.intellij.serviceContainer.ComponentManagerImplKt;
import com.intellij.util.KeyedLazyInstanceEP;
import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public final class PerlHandlerBean extends KeyedLazyInstanceEP<AbstractPerlHandler<?, ?>> {
  @Override
  public @NotNull AbstractPerlHandler<?, ?> createInstance(@NotNull ComponentManager componentManager,
                                                           @NotNull PluginDescriptor pluginDescriptor) {
    try {
      Class<AbstractPerlHandler<?, ?>> extensionClass = getHandlerClass(componentManager, pluginDescriptor);
      Constructor<AbstractPerlHandler<?, ?>> constructor = extensionClass.getConstructor(PerlHandlerBean.class);
      constructor.setAccessible(true);
      return constructor.newInstance(PerlHandlerBean.this);
    }
    catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Copy-paste from {@link ComponentManagerImplKt#doLoadClass(String, PluginDescriptor)}
   */
  private @NotNull Class<AbstractPerlHandler<?, ?>> getHandlerClass(@NotNull ComponentManager componentManager,
                                                                    @NotNull PluginDescriptor pluginDescriptor)
    throws ClassNotFoundException {
    var classLoader = ObjectUtils.coalesce(pluginDescriptor.getClassLoader(), componentManager.getClass().getClassLoader());
    var implementationClassName =
      Objects.requireNonNull(getImplementationClassName(), () -> "Class name is not specified for extension: " + getKey());
    Class<AbstractPerlHandler<?, ?>> extensionClass;
    if (classLoader instanceof PluginAwareClassLoader pluginAwareClassLoader) {
      //noinspection unchecked
      extensionClass = (Class<AbstractPerlHandler<?, ?>>)pluginAwareClassLoader.tryLoadingClass(implementationClassName, true);
    }
    else {
      //noinspection unchecked
      extensionClass = (Class<AbstractPerlHandler<?, ?>>)classLoader.loadClass(implementationClassName);
    }
    if (extensionClass == null) {
      throw new ClassNotFoundException("Unable to load extension class: " + implementationClassName);
    }
    return extensionClass;
  }
}
