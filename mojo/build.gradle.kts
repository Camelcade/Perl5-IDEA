/*
 * Copyright 2015-2025 Alexandr Evstigneev
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
import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType

dependencies {
  // dependencies
  listOf(
    ":plugin.core",
    ":lang.mojo.core",
  ).forEach {
    compileOnly(project(it))
    testCompileOnly(project(it))
  }

  intellijPlatform {
    val platformVersionProvider: Provider<String> by rootProject.extra

    create(
      type = provider { IntelliJPlatformType.IntellijIdeaUltimate },
      version = platformVersionProvider,
      useInstaller = providers.gradleProperty("useInstaller").map { it.toBoolean() },
    )

    localPlugin(project(":plugin"))
    bundledPlugin(providers.gradleProperty("remoteRunPlugin"))

    // packaging, which modules to include into this one
    listOf(
      ":lang.mojo.common",
      ":lang.mojo.core",
      ":lang.mojo.frontend",
      ":lang.mojo.frontend.split",
    ).forEach {
      pluginModule(project(it))
    }
  }

  testImplementation(testFixtures(project(":plugin.testFixtures")))
}
