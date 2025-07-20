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

package com.perl5.lang.perl.psi;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.util.ClearableLazyValue;
import com.intellij.util.KeyedLazyInstance;
import org.jetbrains.annotations.NotNull;

@Service
public final class PerlSubCallHandlerVersionService implements Disposable {
  private final ClearableLazyValue<Integer> myVersionProvider = ClearableLazyValue.createAtomic(() -> {
    int version = 0;
    for (KeyedLazyInstance<PerlSubCallHandler<?>> instance : PerlSubCallHandler.getExtensionPoint().getExtensions()) {
      version += instance.getInstance().getVersion();
    }
    return version;
  });

  public PerlSubCallHandlerVersionService() {
    PerlSubCallHandler.getExtensionPoint().addChangeListener(myVersionProvider::drop, this);
  }

  public static int getHandlersVersion() {
    return getInstance().myVersionProvider.getValue();
  }

  @Override
  public void dispose() {
    myVersionProvider.drop();
  }

  private static @NotNull PerlSubCallHandlerVersionService getInstance() {
    return ApplicationManager.getApplication().getService(PerlSubCallHandlerVersionService.class);
  }
}
