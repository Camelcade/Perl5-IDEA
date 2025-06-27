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

plugins {
  id("java-test-fixtures")
}

dependencies {
  listOf(
    ":plugin.asdf",
    ":plugin.berrybrew",
    ":plugin.common",
    ":plugin.core",
    ":plugin.cpan",
    ":plugin.cpanminus",
    ":plugin.docker",
    ":plugin.frontend",
    ":plugin.idea",
    ":plugin.perlInstall",
    ":plugin.perlbrew",
    ":plugin.plenv",
  ).forEach {
    testFixturesCompileOnly(project(it))
  }
  testFixturesCompileOnly("junit:junit:4.13.2")

  intellijPlatform {
    val platformVersionProvider: Provider<String> by rootProject.extra

    create(
      type = provider { IntelliJPlatformType.IntellijIdeaCommunity },
      version = platformVersionProvider,
      useInstaller = providers.gradleProperty("useInstaller").map { it.toBoolean() },
    )

    bundledPlugin("org.jetbrains.plugins.terminal")
    bundledPlugin(providers.gradleProperty("intelliLangPlugin"))
  }
}
