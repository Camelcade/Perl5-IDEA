import org.jetbrains.intellij.tasks.PrepareSandboxTask

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

plugins {
  id("java-test-fixtures")
  id("com.github.johnrengelman.shadow") version "8.1.0"
}

dependencies {
  listOf(
    ":plugin:asdf",
    ":plugin:berrybrew",
    ":plugin:copyright",
    ":plugin:core",
    ":plugin:coverage",
    ":plugin:debugger",
    ":plugin:docker",
    ":plugin:idea",
    ":plugin:intelliLang",
    ":plugin:perlInstall",
    ":plugin:perlbrew",
    ":plugin:plenv",
    ":plugin:profiler",
    ":plugin:terminal",
    ":plugin:wsl",
  ).forEach {
    compileOnly(project(it))
    testCompileOnly(project(it))
    runtimeOnly(project(it, "instrumentedJar"))
  }

  listOf(
    ":plugin:asdf",
    ":plugin:core",
    ":plugin:docker",
    ":plugin:perlbrew",
    ":plugin:plenv",
  ).forEach {
    testFixturesCompileOnly(project(it))
    testFixturesRuntimeOnly(project(it, "instrumentedJar"))
  }
}

intellij {
  type.set("IU")
  plugins.set(project.provider {
    listOf(
      properties("intelliLangPlugin").get(),
      properties("coveragePlugin").get(),
      properties("remoteRunPlugin").get(),
      "copyright",
      "terminal",
    )
  })
}

tasks {
  withType<PrepareSandboxTask> {
    inputs.dir("scripts")
    outputs.dir("$destinationDir/${intellij.pluginName.get()}/perl")
    pluginJar.set(shadowJar.flatMap {
      it.archiveFile
    })
    runtimeClasspathFiles.set(project.files())

    intoChild(pluginName.map { "$it/perl" })
      .from(file("scripts"))

    dependsOn(shadowJar)
  }
}
