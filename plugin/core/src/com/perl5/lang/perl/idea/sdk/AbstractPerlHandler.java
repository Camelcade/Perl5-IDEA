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

import com.intellij.util.xmlb.XmlSerializer;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractPerlHandler<Data extends AbstractPerlData<Data, Handler>, Handler extends AbstractPerlHandler<Data, Handler>> {
  protected static final String ID_ATTRIBUTE = "id";

  @NotNull
  private final String myId;

  public AbstractPerlHandler(@NotNull PerlHandlerBean bean) {
    myId = bean.getKey();
  }

  /**
   * @return new instance of the data, associated with this handler
   */
  @NotNull
  public abstract Data createData();

  /**
   * @return unique ID of the handler. Id should be unique on the EP level. Meaning there can be HostHandler and ImplementationHandler with same id
   * @implSpec this id MUST be the same as {@code key} in plugin.xml
   */
  @NotNull
  public final String getId() {
    return myId;
  }

  /**
   * @return name of the tag for this handler to serialize data in xml
   */
  @NotNull
  protected abstract String getTagName();

  /**
   * @return {@link Data} loaded from the {@code element} or null if data is corrupted
   */
  @Nullable
  public final Data loadData(@NotNull Element element) {
    return doLoadData(element, createData());
  }

  /**
   * Loads additional data from {@code element} if necessary
   *
   * @return {@code} data passed
   */
  @Nullable
  protected Data doLoadData(@NotNull Element element, @NotNull Data data) {
    XmlSerializer.deserializeInto(data, element);
    return data;
  }

  /**
   * @param data serializes {@code data} to the {@code targetElement}
   */
  public final void saveData(@NotNull Element targetElement, @NotNull Data data) {
    Element tagElement = new Element(getTagName());
    tagElement.setAttribute(ID_ATTRIBUTE, getId());
    targetElement.addContent(tagElement);
    doSaveData(tagElement, data);
  }

  /**
   * Saves additional fields of the {@code data} to the {@code targetElement}
   */
  protected void doSaveData(@NotNull Element targetElement, Data data) {
    XmlSerializer.serializeInto(data, targetElement);
  }
}
