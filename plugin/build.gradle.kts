import org.jetbrains.intellij.platform.gradle.tasks.PrepareSandboxTask

/*
 * Copyright 2015-2021 Alexandr Evstigneev
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

fun properties(key: String) = providers.gradleProperty(key)

dependencies {
  listOf(
    ":plugin:asdf",
    ":plugin:berrybrew",
    ":plugin:carton",
    ":plugin:copyright",
    ":plugin:core",
    ":plugin:coverage",
    ":plugin:cpan",
    ":plugin:cpanminus",
    ":plugin:debugger",
    ":plugin:docker",
    ":plugin:idea",
    ":plugin:intelliLang",
    ":plugin:makeMaker",
    ":plugin:moduleBuild",
    ":plugin:perlInstall",
    ":plugin:perlbrew",
    ":plugin:plenv",
    ":plugin:profiler",
    ":plugin:terminal",
    ":plugin:wsl",
  ).forEach {
    compileOnly(project(it))
    testCompileOnly(project(it))
    runtimeOnly(project(it))
    intellijPlatform {
      pluginModule(implementation(project(it)))
    }
  }

  intellijPlatform {
    val platformVersionProvider: Provider<String> by rootProject.extra
    create("IU", platformVersionProvider.get(), useInstaller = properties("useInstaller").get().toBoolean())
    bundledPlugins(
      "com.intellij.copyright",
      properties("intelliLangPlugin").get(),
      properties("remoteRunPlugin").get(),
      properties("coveragePlugin").get(),
      "XPathView",
      "org.jetbrains.plugins.terminal",
    )
  }
}



tasks {
  buildPlugin {
    archiveBaseName.set("lang.perl5")
  }

  withType<PrepareSandboxTask> {
    inputs.dir("scripts")

    intoChild(intellijPlatform.projectName.map { projectName -> "$projectName/perl" })
      .from(file("scripts"))
  }

  test {
    dependencies {
      intellijPlatform {
        listOf(
          "com.intellij.css",
          "com.intellij.database",
          "com.intellij.java",
          "org.intellij.plugins.markdown",
        ).forEach { bundledPlugin(it) }
        listOf(
          "intellij.platform.coverage.agent",
          "intellij.profiler.common",
        ).forEach { bundledModule(it) }
      }
    }
  }
}
