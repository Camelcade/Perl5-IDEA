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
  id("com.github.johnrengelman.shadow") version "7.1.2"
}

dependencies {
  implementation(project(":plugin:core", "instrumentedJar"))
  implementation(project(":plugin:idea", "instrumentedJar"))
  implementation(project(":plugin:profiler", "instrumentedJar"))
  implementation(project(":plugin:coverage", "instrumentedJar"))
  implementation(project(":plugin:debugger", "instrumentedJar"))
  implementation(project(":plugin:copyright", "instrumentedJar"))
  implementation(project(":plugin:intelliLang", "instrumentedJar"))
  implementation(project(":plugin:docker", "instrumentedJar"))
  implementation(project(":plugin:wsl", "instrumentedJar"))
  implementation(project(":plugin:perlbrew", "instrumentedJar"))
  implementation(project(":plugin:berrybrew", "instrumentedJar"))
  implementation(project(":plugin:plenv", "instrumentedJar"))

  testFixturesApi(project(":plugin:core", "instrumentedJar"))
  testFixturesApi(project(":plugin:docker", "instrumentedJar"))
  testFixturesApi(project(":plugin:perlbrew", "instrumentedJar"))
  testFixturesApi(project(":plugin:plenv", "instrumentedJar"))
}

intellij {
  type.set("IU")
  plugins.set(project.provider {
    listOf(
      properties("intelliLangPlugin").get(),
      properties("coveragePlugin").get(),
      properties("remoteRunPlugin").get(),
      "copyright",
      "java",
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
