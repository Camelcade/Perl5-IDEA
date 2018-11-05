/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

package com.perl5.lang.perl.util;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.util.io.FileUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Created by hurricup on 16.04.2016.
 */
public class PerlPluginUtil {
  public static final String PLUGIN_ID = "com.perl5";
  private static final String PERL_DIR = "perl5";
  private static final String REMOTES_DIR = "remote";

  @NotNull
  public static IdeaPluginDescriptor getPlugin() {
    return Objects.requireNonNull(PluginManager.getPlugin(PluginId.getId(PLUGIN_ID)));
  }

  @Nullable
  public static String getPluginVersion() {
    IdeaPluginDescriptor plugin = getPlugin();
    return plugin.getVersion();
  }

  @NotNull
  public static String getPluginRoot() {
    IdeaPluginDescriptor plugin = PerlPluginUtil.getPlugin();
    try {
      return FileUtil.toSystemIndependentName(plugin.getPath().getCanonicalPath());
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * @return path to IDE perl helper scripts
   */
  @NotNull
  public static String getPluginHelpersRoot() {
    return FileUtil.join(getPluginRoot(), "perl");
  }

  /**
   * @return path to the helpers libs dir
   */
  @NotNull
  public static String getHelpersLibPath() {
    return getHelperPath("lib");
  }

  /**
   * @return path to a helper with {@code relativePath}
   */
  public static String getHelperPath(@NotNull String relativePath) {
    return FileUtil.toSystemIndependentName(Paths.get(getPluginHelpersRoot(), relativePath).toString());
  }

  /**
   * @return perl5 dir in the ide's {@code system} dir
   */
  @NotNull
  public static String getPerlSystemPath() {
    String systemPath = PathManager.getSystemPath();
    String perlDirectory = FileUtil.join(systemPath, PERL_DIR);
    FileUtil.createDirectory(new File(perlDirectory));
    return perlDirectory;
  }

  /**
   * @return root for remote filesystems cache
   */
  @NotNull
  public static String getRemotesCachePath() {
    String remotesCachePath = FileUtil.join(getPerlSystemPath(), REMOTES_DIR);
    FileUtil.createDirectory(new File(remotesCachePath));
    return remotesCachePath;
  }
}
