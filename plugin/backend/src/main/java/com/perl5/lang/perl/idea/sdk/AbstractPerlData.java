/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

import com.esotericsoftware.kryo.kryo5.util.Null;
import com.intellij.openapi.diagnostic.Logger;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Base class for all sub-elements for {@link PerlSdkAdditionalData}
 *
 * @param <Handler> for this data
 */
public abstract class AbstractPerlData<Data extends AbstractPerlData<Data, Handler>, Handler extends AbstractPerlHandler<Data, Handler>> {
  private static final Logger LOG = Logger.getInstance(AbstractPerlData.class);
  private Throwable myCommitStackTrace = null;

  public final void markAsCommited(@NotNull Throwable commitStackTrace) {
    if (myCommitStackTrace == null) {
      myCommitStackTrace = commitStackTrace;
    }
  }

  protected final boolean isWritable() {
    return myCommitStackTrace == null;
  }

  protected final void assertWritable() {
    if (!isWritable()) {
      LOG.error(new Throwable("Additional sdk data can't be directly modified, see javadoc for the assertion.", myCommitStackTrace));
    }
  }

  private final @NotNull Handler myHandler;

  protected AbstractPerlData(@NotNull Handler handler) {
    assertWritable();
    myHandler = handler;
  }

  public final void save(@NotNull Element target) {
    getHandler().saveData(target, self());
  }

  protected abstract @NotNull Data self();

  public final @NotNull Handler getHandler() {
    return myHandler;
  }
}
