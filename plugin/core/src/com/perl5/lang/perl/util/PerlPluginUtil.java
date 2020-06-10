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

package com.perl5.lang.perl.util;

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.perl5.lang.perl.fileTypes.PerlFileTypeService;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


public class PerlPluginUtil {
  public static final String PLUGIN_ID = "com.perl5";
  private static final String PERL_DIR = "perl5";
  private static final String REMOTES_DIR = "remote";

  public static @NotNull IdeaPluginDescriptor getPlugin() {
    return Objects.requireNonNull(PluginManagerCore.getPlugin(PluginId.getId(PLUGIN_ID)));
  }

  /**
   * @return disposable for the plugin. Need to be replaced with proper plugin disposable later
   */
  public static @NotNull Disposable getPluginDisposable() {
    return PerlFileTypeService.getInstance();
  }

  /**
   * @return project-level disposable for a plugin. It's eol happens on project service unloading what may
   * happen when project is closed or plugin unloaded.
   */
  public static @NotNull Disposable getProjectPluginDisposable(@NotNull Project project) {
    return PerlProjectManager.getInstance(project);
  }

  public static @NotNull String getPluginVersion() {
    return Objects.requireNonNull(getPlugin().getVersion());
  }

  public static @NotNull String getPluginRoot() {
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
  public static @NotNull String getPluginHelpersRoot() {
    return FileUtil.toSystemIndependentName(new File(getPluginRoot(), "perl").getPath());
  }

  /**
   * @return path to the helpers libs dir
   */
  public static @NotNull String getHelpersLibPath() {
    return getHelperPath("lib");
  }

  /**
   * @return path to a helper with {@code relativePath}
   */
  public static String getHelperPath(@NotNull String relativePath) {
    return FileUtil.toSystemIndependentName(new File(getPluginHelpersRoot(), relativePath).getPath());
  }

  /**
   * @return perl5 dir in the ide's {@code system} dir
   */
  public static @NotNull String getPerlSystemPath() {
    String systemPath = PathManager.getSystemPath();
    String perlDirectory = FileUtil.join(systemPath, PERL_DIR);
    FileUtil.createDirectory(new File(perlDirectory));
    return perlDirectory;
  }

  /**
   * @return root for remote filesystems cache
   */
  public static @NotNull String getRemotesCachePath() {
    String remotesCachePath = FileUtil.join(getPerlSystemPath(), REMOTES_DIR);
    FileUtil.createDirectory(new File(remotesCachePath));
    return remotesCachePath;
  }
}
