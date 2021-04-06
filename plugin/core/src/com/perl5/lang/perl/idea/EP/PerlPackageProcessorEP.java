/*
 * Copyright 2015-2021 Alexandr Evstigneev
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

import com.intellij.openapi.util.AtomicClearableLazyValue;
import com.intellij.openapi.util.ClearableLazyValue;
import com.intellij.openapi.util.KeyedExtensionCollector;
import com.intellij.util.KeyedLazyInstance;
import com.intellij.util.KeyedLazyInstanceEP;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessor;
import com.perl5.lang.perl.util.PerlPluginUtil;

import java.util.Objects;


public class PerlPackageProcessorEP extends KeyedLazyInstanceEP<PerlPackageProcessor> {
  public static final KeyedExtensionCollector<PerlPackageProcessor, String> EP =
    new KeyedExtensionCollector<>("com.perl5.packageProcessor");

  private static final ClearableLazyValue<Integer> VERSION_PROVIDER = AtomicClearableLazyValue.createAtomic(() -> {
    int version = 0;
    //noinspection UnstableApiUsage
    for (KeyedLazyInstance<PerlPackageProcessor> instance : Objects.requireNonNull(EP.getPoint()).getExtensionList()) {
      version += instance.getInstance().getVersion();
    }
    return version;
  });

  static {
    //noinspection UnstableApiUsage
    Objects.requireNonNull(EP.getPoint()).addChangeListener(VERSION_PROVIDER::drop, PerlPluginUtil.getUnloadAwareDisposable());
  }

  public static int getVersion() {
    return VERSION_PROVIDER.getValue();
  }
}
