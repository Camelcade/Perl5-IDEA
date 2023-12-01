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
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkModificator;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.xmlb.annotations.Tag;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jdom.Element;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class PerlConfig {
  private static final Key<Boolean> INIT_KEY = Key.create("perl.config.read");
  private static final Logger LOG = Logger.getInstance(PerlConfig.class);
  private static final String TAG_NAME = "PerlConfig";
  private static final PerlConfig EMPTY_CONFIG = new PerlConfig(Collections.emptyMap());

  private static final String ARCHNAME = "archname";
  private static final String API_VERSIONSTRING = "api_versionstring";
  private static final String VERSION = "version";

  @Tag("configMap")
  private final @NotNull Map<String, String> myConfig;

  @SuppressWarnings("unused")
  private PerlConfig() {
    this(Collections.emptyMap());
  }

  private PerlConfig(@NotNull Map<String, String> config) {
    myConfig = Collections.unmodifiableMap(new HashMap<>(config));
  }

  @Contract("null->null")
  public @Nullable String get(@Nullable String key) {
    return myConfig.get(key);
  }

  public @Nullable String getArchname() {
    return get(ARCHNAME);
  }

  public @Nullable String getApiVersionString() {
    return get(API_VERSIONSTRING);
  }

  public @Nullable String getVersion() {
    return get(VERSION);
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

  @Contract("null->null")
  public static @Nullable PerlConfig from(@Nullable Sdk sdk) {
    var sdkAdditionalData = PerlSdkAdditionalData.from(sdk);
    return sdkAdditionalData == null ? null : sdkAdditionalData.getConfig();
  }

  public static void init(@Nullable Sdk sdk) {
    var currentConfig = from(sdk);
    if (currentConfig == null || !currentConfig.isEmpty() || INIT_KEY.get(sdk) != null) {
      return;
    }
    try {
      INIT_KEY.set(sdk, true);
      PerlConfig perlConfig = ProgressManager.getInstance().runProcessWithProgressSynchronously(
        () -> readConfig(sdk), PerlBundle.message("dialog.title.reading.perl.config"), false, null
      );
      if (perlConfig != null) {
        var sdkModificator = sdk.getSdkModificator();
        PerlSdkAdditionalData.notNullFrom(sdkModificator).setConfig(perlConfig);
        ApplicationManager.getApplication().invokeAndWait(()-> WriteAction.run(sdkModificator::commitChanges));
      }
    }
    finally {
      INIT_KEY.set(sdk, null);
    }
  }

  private static @Nullable PerlConfig readConfig(@NotNull Sdk sdk) {
    var configOutput =
      PerlRunUtil.getOutputFromPerl(sdk, "-MConfig", "-e", "print \"$_\\1$Config{$_}\\0\" for grep {$Config{$_}} sort keys %Config");
    if (configOutput.isEmpty()) {
      LOG.warn("Error reading config for " + sdk);
      return null;
    }
    var configData = new HashMap<String, String>();
    var configPairs = StringUtil.split(String.join("", configOutput), "\0");
    for (String configPair : configPairs) {
      var keyValPair = StringUtil.split(configPair, "\1");
      if (keyValPair.size() != 2) {
        LOG.warn("Unexpected parsing of key/value pair: " + keyValPair);
        continue;
      }
      configData.put(keyValPair.get(0), keyValPair.get(1));
    }
    return intern(new PerlConfig(configData));
  }

  @TestOnly
  public static PerlConfig create(@NotNull Map<String, String> dataMap) {
    return intern(new PerlConfig(dataMap));
  }
}
