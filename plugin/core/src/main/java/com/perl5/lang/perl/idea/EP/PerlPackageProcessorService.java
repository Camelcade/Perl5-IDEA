/*
 * Copyright 2015-2024 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.EP;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.util.AtomicClearableLazyValue;
import com.intellij.openapi.util.ClearableLazyValue;
import com.intellij.openapi.util.KeyedExtensionCollector;
import com.intellij.util.KeyedLazyInstance;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessor;

import java.util.Objects;

@Service
public final class PerlPackageProcessorService implements Disposable {
  public static final KeyedExtensionCollector<PerlPackageProcessor, String> EP =
    new KeyedExtensionCollector<>("com.perl5.packageProcessor");

  private final ClearableLazyValue<Integer> myVersionProvider = AtomicClearableLazyValue.createAtomic(() -> {
    int version = 0;
    for (KeyedLazyInstance<PerlPackageProcessor> instance : Objects.requireNonNull(EP.getPoint()).getExtensionList()) {
      version += instance.getInstance().getVersion();
    }
    return version;
  });

  public PerlPackageProcessorService() {
    Objects.requireNonNull(EP.getPoint()).addChangeListener(myVersionProvider::drop, this);
  }

  public static int getVersion() {
    return ApplicationManager.getApplication().getService(PerlPackageProcessorService.class).myVersionProvider.getValue();
  }

  @Override
  public void dispose() {
  }
}
