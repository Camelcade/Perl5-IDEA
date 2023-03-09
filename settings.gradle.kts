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

pluginManagement {
  repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    gradlePluginPortal()
  }
}
rootProject.name = "perl5"
include("plugin")
project(":plugin").projectDir = file("plugin")
include(":plugin:core")
project(":plugin:core").projectDir = file("plugin/core")
include(":plugin:idea")
project(":plugin:idea").projectDir = file("plugin/idea")
include(":plugin:copyright")
project(":plugin:copyright").projectDir = file("plugin/copyright")
include(":plugin:profiler")
project(":plugin:profiler").projectDir = file("plugin/profiler")
include(":plugin:debugger")
project(":plugin:debugger").projectDir = file("plugin/debugger")
include(":plugin:coverage")
project(":plugin:coverage").projectDir = file("plugin/coverage")
include(":plugin:intelliLang")
project(":plugin:intelliLang").projectDir = file("plugin/intelliLang")
include(":plugin:docker")
project(":plugin:docker").projectDir = file("plugin/docker")
include(":plugin:wsl")
project(":plugin:wsl").projectDir = file("plugin/wsl")
include(":plugin:perlbrew")
project(":plugin:perlbrew").projectDir = file("plugin/perlbrew")
include(":plugin:asdf")
project(":plugin:asdf").projectDir = file("plugin/asdf")
include(":plugin:plenv")
project(":plugin:plenv").projectDir = file("plugin/plenv")
include(":plugin:perlInstall")
project(":plugin:perlInstall").projectDir = file("plugin/perlInstall")
include(":plugin:berrybrew")
project(":plugin:berrybrew").projectDir = file("plugin/berrybrew")
include(":plugin:terminal")
project(":plugin:terminal").projectDir = file("plugin/terminal")
include("lang.tt2")
project(":lang.tt2").projectDir = file("tt2")
include("lang.mojo")
project(":lang.mojo").projectDir = file("mojo")
include(":lang.mojo:core")
project(":lang.mojo:core").projectDir = file("mojo/core")
include("lang.embedded")
project(":lang.embedded").projectDir = file("embedded")
include(":lang.embedded:core")
project(":lang.embedded:core").projectDir = file("embedded/core")
include("lang.mason.framework")
project(":lang.mason.framework").projectDir = file("mason/framework")
include("lang.mason.mason2")
project(":lang.mason.mason2").projectDir = file("mason/mason2")
include("lang.mason.mason2:core")
project(":lang.mason.mason2:core").projectDir = file("mason/mason2/core")
include("lang.mason.htmlmason")
project(":lang.mason.htmlmason").projectDir = file("mason/htmlmason")
include("lang.mason.htmlmason:core")
project(":lang.mason.htmlmason:core").projectDir = file("mason/htmlmason/core")
