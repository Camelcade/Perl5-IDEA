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

package com.perl5.lang.perl.idea.sdk;

import com.intellij.configurationStore.XmlSerializer;
import com.intellij.util.xmlb.annotations.Tag;
import org.jdom.Element;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class PerlConfig {
  private static final String TAG_NAME = "PerlConfig";
  private static final PerlConfig EMPTY_CONFIG = new PerlConfig(Collections.emptyMap());
  @Tag("configMap")
  private final @NotNull Map<String, String> myConfig;

  @SuppressWarnings("unused")
  private PerlConfig() {
    this(new HashMap<>());
  }

  private PerlConfig(@NotNull Map<String, String> config) {
    myConfig = config;
  }

  @Contract("null->null")
  public @Nullable String get(@Nullable String key) {
    return myConfig.get(key);
  }

  public boolean isEmpty() {
    return this == EMPTY_CONFIG;
  }

  /**
   * Loading config from xml
   */
  public static @NotNull PerlConfig load(@NotNull Element parentElement) {
    Element container = parentElement.getChild(TAG_NAME);
    if (container == null) {
      return EMPTY_CONFIG;
    }
    return intern(XmlSerializer.deserialize(container, PerlConfig.class));
  }

  private static @NotNull PerlConfig intern(@Nullable PerlConfig configMap) {
    return configMap == null || configMap.myConfig.isEmpty() ? EMPTY_CONFIG : configMap;
  }

  public static @NotNull PerlConfig empty() {
    return EMPTY_CONFIG;
  }

  public void save(@NotNull Element parentElement) {
    var container = new Element(TAG_NAME);
    XmlSerializer.serializeObjectInto(this, container);
    parentElement.addContent(container);
  }

  @TestOnly
  public static PerlConfig create(@NotNull Map<String, String> dataMap) {
    return intern(new PerlConfig(dataMap));
  }
}
