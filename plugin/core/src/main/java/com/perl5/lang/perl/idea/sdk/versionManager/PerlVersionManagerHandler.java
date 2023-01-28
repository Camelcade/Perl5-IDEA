/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.sdk.versionManager;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.NlsActions.ActionText;
import com.perl5.lang.perl.idea.sdk.AbstractPerlHandler;
import com.perl5.lang.perl.idea.sdk.PerlHandlerBean;
import com.perl5.lang.perl.idea.sdk.PerlHandlerCollector;
import com.perl5.lang.perl.idea.sdk.host.PerlHostHandler;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandler;
import org.jdom.Element;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

public abstract class PerlVersionManagerHandler<Data extends PerlVersionManagerData<Data, Handler>, Handler extends PerlVersionManagerHandler<Data, Handler>>
  extends AbstractPerlHandler<Data, Handler> {
  private static final String TAG_NAME = "versionManager";

  private static final PerlHandlerCollector<PerlVersionManagerHandler<?, ?>> EP =
    new PerlHandlerCollector<>("com.perl5.versionManagerHandler");

  public PerlVersionManagerHandler(@NotNull PerlHandlerBean bean) {
    super(bean);
  }

  /**
   * Shows a UI to create an sdk for this version manager on the host described by the {@code hostHandler}
   *
   * @param disposable any temporary things may be bound to the disposable which is going to be disposed by invoker
   */
  public abstract void createSdkInteractively(@Nullable Project project,
                                              @NotNull PerlHostHandler<?, ?> hostHandler,
                                              @Nullable Consumer<Sdk> sdkConsumer,
                                              @NotNull Disposable disposable);

  /**
   * @return title for menu item in add interpreter dialog
   */
  public abstract @NotNull @ActionText String getMenuItemTitle();

  /**
   * @return short lowercased name, for interpreters list
   */
  public @NotNull String getShortName() {
    return getPresentableName();
  }

  /**
   * @return Version Manager presentable name
   */
  public abstract @NotNull String getPresentableName();

  /**
   * @return true iff version manager is supported by OS described with {@code osHandler}
   * E.g. perlbrew is not available on windows systems
   * @apiNote if {@code osHandler} is null, we suppose that it's applicable
   */
  public abstract boolean isApplicable(@Nullable PerlOsHandler osHandler);

  @Override
  protected final @NotNull String getTagName() {
    return TAG_NAME;
  }

  public static @NotNull List<PerlVersionManagerHandler<?, ?>> all() {
    return EP.getExtensionsList();
  }

  public static @NotNull Stream<? extends PerlVersionManagerHandler<?, ?>> stream() {
    return all().stream();
  }

  @Contract("null->null")
  static @Nullable PerlVersionManagerHandler<?, ?> from(@Nullable Sdk sdk) {
    PerlVersionManagerData<?, ?> versionManagerData = PerlVersionManagerData.from(sdk);
    return versionManagerData == null ? null : versionManagerData.getHandler();
  }

  /**
   * @return true iff {@code sdk} has the same version handler as this one
   */
  @Contract("null -> false")
  public boolean isSameHandler(@Nullable Sdk sdk) {
    PerlVersionManagerHandler<?, ?> handler = PerlVersionManagerHandler.from(sdk);
    return handler != null && handler.getId().equals(getId());
  }


  /**
   * Attempts to load {@link PerlVersionManagerData} from the {@code parentElement}
   *
   * @return data read or null if data can't be read
   */
  public static @Nullable PerlVersionManagerData<?, ?> load(@NotNull Element parentElement) {
    Element element = parentElement.getChild(TAG_NAME);
    if (element == null) {
      return null;
    }
    PerlVersionManagerHandler<?, ?> handler = EP.findSingle(element.getAttributeValue(ID_ATTRIBUTE));
    return handler != null ? handler.loadData(element) : null;
  }

  public static @NotNull PerlVersionManagerHandler<?, ?> getDefaultHandler() {
    return Objects.requireNonNull(EP.findSingle("system"), "System perl handler must always present");
  }
}
