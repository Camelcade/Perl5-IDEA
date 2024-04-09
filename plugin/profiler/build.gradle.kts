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

dependencies {
  listOf(
    ":plugin:core",
  ).forEach {
    compileOnly(project(it))
    testCompileOnly(project(it))
    runtimeOnly(project(it, "instrumentedJar"))
  }

  val ideaJars = tasks.setupDependencies
    .flatMap { task -> task.idea.map { it.classes } }
    .map { idea ->
      listOf(
        "lib/modules/intellij.profiler.common.jar"
      ).map { idea.resolve(it) }
    }
    .let { project.files(it) }

  compileOnly(ideaJars)
  testCompileOnly(ideaJars)
}

intellij {
  type.set("IU")
}

