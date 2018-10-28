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

package com.perl5.lang.perl.idea.sdk.versionManager;

import com.intellij.openapi.projectRoots.Sdk;
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
   */
  public abstract void createSdkInteractively(@NotNull PerlHostHandler<?, ?> hostHandler,
                                              @Nullable Consumer<Sdk> sdkConsumer);

  /**
   * @return title for menu item in add interpreter dialog
   */
  @NotNull
  public abstract String getMenuItemTitle();

  /**
   * @return short lowercased name, for interpreters list
   */
  @NotNull
  public String getShortName() {
    return getPresentableName();
  }

  /**
   * @return Version Manager presentable name
   */
  @NotNull
  public abstract String getPresentableName();

  /**
   * @return true iff version manager is supported by OS described with {@code osHandler}
   * E.g. perlbrew is not available on windows systems
   * @apiNote if {@code osHandler} is null, we suppose that it's applicable
   */
  public abstract boolean isApplicable(@Nullable PerlOsHandler osHandler);

  @NotNull
  @Override
  protected final String getTagName() {
    return TAG_NAME;
  }

  @NotNull
  public static List<PerlVersionManagerHandler<?, ?>> all() {
    return EP.getExtensions();
  }

  public static void forEach(@NotNull Consumer<? super PerlVersionManagerHandler> action) {
    all().forEach(action);
  }

  @NotNull
  public static Stream<? extends PerlVersionManagerHandler<?, ?>> stream() {
    return all().stream();
  }

  @Contract("null->null")
  @Nullable
  static PerlVersionManagerHandler from(@Nullable Sdk sdk) {
    PerlVersionManagerData<?, ?> versionManagerData = PerlVersionManagerData.from(sdk);
    return versionManagerData == null ? null : versionManagerData.getHandler();
  }

  /**
   * @return true iff {@code sdk} has the same version handler as this one
   */
  @Contract("null -> false")
  public boolean isSameHandler(@Nullable Sdk sdk) {
    PerlVersionManagerHandler handler = PerlVersionManagerHandler.from(sdk);
    return handler != null && handler.getId().equals(getId());
  }


  /**
   * Attempts to load {@link PerlVersionManagerData} from the {@code parentElement}
   *
   * @return data read or new empty data created by defaultHandler
   */
  @NotNull
  public static PerlVersionManagerData<?, ?> load(@NotNull Element parentElement) {
    Element element = parentElement.getChild(TAG_NAME);
    if (element != null) {
      PerlVersionManagerHandler<?, ?> handler = EP.findSingle(element.getAttributeValue(ID_ATTRIBUTE));
      if (handler != null) {
        PerlVersionManagerData<?, ?> data = handler.loadData(element);
        if (data != null) {
          return data;
        }
      }
    }
    return getDefaultHandler().createData();
  }

  @NotNull
  public static PerlVersionManagerHandler<?, ?> getDefaultHandler() {
    return Objects.requireNonNull(EP.findSingle("system"), "System perl handler must always present");
  }
}
