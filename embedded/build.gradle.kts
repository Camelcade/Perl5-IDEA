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
  // packaging, which modules to include into this one
  listOf(
    ":lang.embedded.core",
  ).forEach {
    intellijPlatform{
      pluginModule(implementation(project(it)))
    }
  }

  // additional compilation dependencies
  listOf(
    ":plugin.core",
  ).forEach {
    compileOnly(project(it))
    testCompileOnly(project(it))
  }

  // Test dependencies
  testImplementation(testFixtures(project(":plugin.testFixtures")))

  // Plugin dependencies
  intellijPlatform {
    localPlugin(project(":plugin"))
  }

  // Useinstaller handling
  intellijPlatform{
    val platformVersionProvider: Provider<String> by rootProject.extra
    create("IC", platformVersionProvider.get(), useInstaller = properties("useInstaller").get().toBoolean())
  }
}