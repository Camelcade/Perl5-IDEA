import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType

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
  // packaging, which modules to include into this one
  listOf(
    ":lang.mason.htmlmason.common",
    ":lang.mason.htmlmason.backend",
    ":lang.mason.htmlmason.frontend",
    ":lang.mason.htmlmason.frontend.split",
  ).forEach {
    runtimeOnly(project(it))
  }

  // compilation dependencies
  listOf(
    ":plugin.common", ":plugin.backend", ":plugin.frontend",
    ":lang.mason.htmlmason.backend", ":lang.mason.htmlmason.common", ":lang.mason.htmlmason.frontend",
    ":lang.mason.framework.backend", ":lang.mason.framework.common", ":lang.mason.framework.frontend",
  ).forEach {
    testCompileOnly(project(it))
  }
  testImplementation(testFixtures(project(":plugin.testFixtures")))

  intellijPlatform {
    val platformVersionProvider: Provider<String> by rootProject.extra
        create(IntelliJPlatformType.IntellijIdeaUltimate, platformVersionProvider.get()){
      useInstaller = providers.gradleProperty("useInstaller").get().toBoolean()
    }

    listOf(
      ":plugin",
      ":lang.mason.framework"
    ).forEach { localPlugin(project(it)) }
  }
}
