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

package com.perl5.lang.perl.psi;

import com.intellij.openapi.util.AtomicClearableLazyValue;
import com.intellij.openapi.util.ClearableLazyValue;
import com.intellij.openapi.util.KeyedExtensionCollector;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.KeyedLazyInstance;
import com.intellij.util.KeyedLazyInstanceEP;
import com.perl5.lang.perl.psi.impl.PerlSubCallElement;
import com.perl5.lang.perl.psi.stubs.calls.PerlSubCallElementData;
import com.perl5.lang.perl.psi.stubs.calls.PerlSubCallElementStub;
import com.perl5.lang.perl.util.PerlPluginUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;

public abstract class PerlSubCallHandler<CallData extends PerlSubCallElementData>
  implements PerlLightElementProvider<PerlSubCallElement, PerlSubCallElementStub>,
             PerlSelfHinter {
  public static final KeyedExtensionCollector<PerlSubCallHandler<?>, String> EP =
    new KeyedExtensionCollector<>("com.perl5.subCallHandler");
  private static final ClearableLazyValue<Integer> VERSION_PROVIDER = AtomicClearableLazyValue.createAtomic(() -> {
    int version = 0;
    //noinspection UnstableApiUsage
    for (KeyedLazyInstance<PerlSubCallHandler<?>> instance : Objects.requireNonNull(EP.getPoint()).getExtensionList()) {
      version += instance.getInstance().getVersion();
    }
    return version;
  });

  static {
    //noinspection UnstableApiUsage
    Objects.requireNonNull(EP.getPoint()).addChangeListener(VERSION_PROVIDER::drop, PerlPluginUtil.getUnloadAwareDisposable());
  }

  public static int getHandlersVersion() {
    return VERSION_PROVIDER.getValue();
  }

  public final @NotNull CallData getCallData(@NotNull PerlSubCallElement subCallElement) {
    PerlSubCallElementStub stub = subCallElement.getGreenStub();
    if (stub != null) {
      //noinspection unchecked
      return (CallData)stub.getCallData();
    }
    return computeCallData(subCallElement);
  }

  public abstract @NotNull CallData computeCallData(@NotNull PerlSubCallElement subCallElement);

  public void serialize(@NotNull PerlSubCallElementData callData, @NotNull StubOutputStream dataStream) throws IOException {
  }

  public abstract CallData deserialize(@NotNull StubInputStream dataStream) throws IOException;

  @Contract("null -> null")
  public static @Nullable PerlSubCallHandler<?> getHandler(@Nullable String subName) {
    if (subName == null) {
      return null;
    }
    return PerlSubCallHandler.EP.findSingle(subName);
  }

  static class Bean extends KeyedLazyInstanceEP<PerlSubCallHandler<?>> {
  }
}
