/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.mojolicious.model;

import com.intellij.util.messages.Topic;
import org.jetbrains.annotations.NotNull;

public interface MojoProjectListener {
  Topic<MojoProjectListener> MOJO_PROJECT_TOPIC = Topic.create("Mojo project manager topic", MojoProjectListener.class);

  default void projectCreated(@NotNull MojoProject project) {
    MojoProjectManager.LOG.debug("project created event: " + project);
    if (project instanceof MojoApp) {
      applicationCreated((MojoApp)project);
    }
    else {
      pluginCreated((MojoPlugin)project);
    }
  }

  default void applicationCreated(@NotNull MojoApp mojoApp) {}

  default void pluginCreated(@NotNull MojoPlugin mojoPlugin) {}

  /**
   * @apiNote invoked before deleting from the model
   */
  default void projectDeleted(@NotNull MojoProject project) {
    MojoProjectManager.LOG.debug("project deleted event: " + project);
    if (project instanceof MojoApp) {
      applicationDeleted((MojoApp)project);
    }
    else {
      pluginDeleted((MojoPlugin)project);
    }
  }

  /**
   * @apiNote invoked before deleting from the model
   */
  default void applicationDeleted(@NotNull MojoApp mojoApp) {}

  /**
   * @apiNote invoked before deleting from the model
   */
  default void pluginDeleted(@NotNull MojoPlugin mojoPlugin) {}
}
