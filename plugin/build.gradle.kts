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
import org.jetbrains.intellij.platform.gradle.tasks.PrepareSandboxTask

dependencies {
  listOf(
    ":plugin.asdf",
    ":plugin.berrybrew",
    ":plugin.carton",
    ":plugin.common",
    ":plugin.copyright",
    ":plugin.core",
    ":plugin.coverage",
    ":plugin.cpan",
    ":plugin.cpanminus",
    ":plugin.debugger",
    ":plugin.docker",
    ":plugin.frontend",
    ":plugin.frontend.split",
    ":plugin.idea",
    ":plugin.intelliLang",
    ":plugin.makeMaker",
    ":plugin.moduleBuild",
    ":plugin.perlInstall",
    ":plugin.perlbrew",
    ":plugin.plenv",
    ":plugin.profiler",
    ":plugin.terminal",
    ":plugin.wsl",
  ).forEach {
    runtimeOnly(project(it))
    testCompileOnly(project(it))
  }
  testImplementation(testFixtures(project(":plugin.testFixtures")))

  intellijPlatform {
    val platformVersionProvider: Provider<String> by rootProject.extra

    create(
      type = provider { IntelliJPlatformType.IntellijIdeaUltimate },
      version = platformVersionProvider,
      useInstaller = providers.gradleProperty("useInstaller").map { it.toBoolean() },
    )

    bundledPlugins(
      "com.intellij.copyright",
      "XPathView",
      "org.jetbrains.plugins.terminal",
      "com.intellij.css",
      "com.intellij.database",
      "com.intellij.java",
      "org.intellij.plugins.markdown",
    )
    bundledPlugin(providers.gradleProperty("intelliLangPlugin"))
    bundledPlugin(providers.gradleProperty("remoteRunPlugin"))
    bundledPlugin(providers.gradleProperty("coveragePlugin"))

    bundledModules(
      "intellij.platform.coverage.agent",
      "intellij.profiler.common",
    )
  }
}

tasks {
  withType<PrepareSandboxTask> {
    inputs.dir("scripts")

    intoChild(intellijPlatform.projectName.map { projectName -> "$projectName/perl" })
      .from(file("scripts"))
  }
}
