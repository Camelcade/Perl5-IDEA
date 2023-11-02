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
plugins {
  id("com.github.johnrengelman.shadow") version "8.1.1"
}

dependencies {
  listOf(
    ":plugin:core",
    ":lang.mason.mason2:core",
    ":lang.mason.framework",
  ).forEach {
    compileOnly(project(it))
    testCompileOnly(project(it))
    testRuntimeOnly(project(it, "instrumentedJar"))
  }
  listOf(
    ":lang.mason.mason2:core",
  ).forEach {
    runtimeOnly(project(it, "instrumentedJar"))
  }
  testImplementation(testFixtures(project(":plugin")))
}

intellij {
  plugins.set(listOf(project(":plugin"), project(":lang.mason.framework")))
}

tasks {
  buildPlugin {
    archiveBaseName.set("lang.mason2")
  }

  withType<PrepareSandboxTask> {
    pluginJar.set(shadowJar.flatMap {
      it.archiveFile
    })
    runtimeClasspathFiles.set(project.files())

    dependsOn(shadowJar)
  }
}