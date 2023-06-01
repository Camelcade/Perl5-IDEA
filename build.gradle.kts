import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.intellij.tasks.InstrumentCodeTask
import org.kt3k.gradle.plugin.coveralls.CoverallsTask

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
fun environment(key: String) = providers.environmentVariable(key)

buildscript {
  dependencies {
    classpath("org.codehaus.groovy.modules.http-builder:http-builder:0.7.1")
    classpath("org.apache.httpcomponents:httpmime:4.5.14")
    classpath("org.eclipse.jgit:org.eclipse.jgit:6.5.0.202303070854-r")
  }
}

plugins {
  id("idea")
  id("jacoco")
  id("org.jetbrains.intellij") version "1.13.3"
  id("org.jetbrains.grammarkit") version "2022.3.1"
  id("com.github.kt3k.coveralls") version "2.12.2"
  id("org.sonarqube") version "4.2.0.3129"
  id("org.jetbrains.qodana") version "0.1.13"
}

repositories {
  mavenCentral()
}

val isCI = environment("CI").map { it.toBoolean() }.orElse(false)
val withCoverage = environment("COVERALLS_REPO_TOKEN").orElse(properties("with_coverage")).map { !it.isEmpty() }.orElse(false)

val pluginProjectsNames = setOf(
  "plugin",
  "lang.tt2",
  "lang.mojo",
  "lang.embedded",
  "lang.mason.framework",
  "lang.mason.htmlmason",
  "lang.mason.mason2"
)

val pluginProjects = allprojects.filter {
  pluginProjectsNames.contains(it.name)
}

allprojects {
  if (name == "test-utils") {
    return@allprojects
  }

  apply(plugin = "org.jetbrains.grammarkit")
  apply(plugin = "org.jetbrains.intellij")
  apply(plugin = "com.github.kt3k.coveralls")
  apply(plugin = "jacoco")
  apply(plugin = "java")

  repositories {
    mavenCentral()
  }

  grammarKit {
    jflexRelease.set("1.9.1")
  }

  version = properties("pluginVersion").get().ifEmpty { properties("platformVersion").get() } +
            properties("pluginBranch").get().ifEmpty { properties("platformBranch").get() } +
            properties("pluginBuild").get().ifEmpty { properties("platformBuild").get() }

  intellij {
    version.set(project.provider {
      properties("platformVersion").get() + properties("platformBranch").get() + properties("platformBuild").get()
    })
    updateSinceUntilBuild.set(true)
  }

  tasks {
    withType<JavaCompile> {
      options.encoding = "UTF-8"
      sourceCompatibility = properties("javaVersion").get()
      targetCompatibility = properties("javaTargetVersion").get()
    }

    test {
      maxHeapSize = "2048m"

      outputs.upToDateWhen { false }

      if (project.hasProperty("overwrite")) {
        systemProperty("idea.tests.overwrite.data", "true")
      }

      if (project.hasProperty("youtrack.token")) {
        systemProperty("youtrack.token", properties("youtrack.token").get())
      }

      val envPerlVersion = environment("PERL_TEST_VERSION").orElse(properties("perl.test.version"))
      systemProperty("perl.test.version", envPerlVersion.get())

      if (project.hasProperty("idea.split.test.logs")) {
        systemProperty("idea.split.test.logs", "true")
        systemProperty("idea.single.test.log.max.length", "100_000_000")
      }

      useJUnit {
        if (project.hasProperty("runtest")) {
          include("**/" + properties("runtest").get() + ".class")
        }

        if (project.hasProperty("integrationTests")) {
          excludeCategories("categories.Light")
          excludeCategories("categories.Heavy")
        }
        else if (project.hasProperty("heavyTests")) {
          excludeCategories("categories.Light")
          excludeCategories("categories.Integration")
        }
        else if (project.hasProperty("lightTests")) {
          excludeCategories("categories.Heavy")
          excludeCategories("categories.Integration")
        }
        else if (!project.hasProperty("allTests")) {
          excludeCategories("categories.Integration")
        }
      }

      testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        showStandardStreams = true
      }

      configure<JacocoTaskExtension> {
        isEnabled = withCoverage.get()
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
      }

      if (isCI.get()) {
        testLogging {
          events.addAll(listOf(
            TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED, TestLogEvent.STANDARD_OUT, TestLogEvent.STANDARD_ERROR
          ))
          exceptionFormat = TestExceptionFormat.FULL
        }
      }
    }

    val isPlugin = pluginProjectsNames.contains(project.name)
    val isRoot = project == rootProject

    buildPlugin {
      onlyIf { isPlugin }
    }
    prepareSandbox {
      onlyIf { isPlugin || isRoot }
    }
    prepareTestingSandbox {
      onlyIf { isPlugin }
    }
    patchPluginXml {
      onlyIf { isPlugin }
    }
    buildSearchableOptions {
      onlyIf { isPlugin }
    }
    publishPlugin {
      if (project.hasProperty("eap")) {
        channels.set(listOf("EAP"))
      }
      token.set(properties("jbToken").orElse(""))
      onlyIf { isPlugin }
    }
    verifyPlugin {
      onlyIf { isPlugin }
    }
  }

  if (pluginProjectsNames.contains(name)) {
    tasks {
      patchPluginXml {
        pluginDescription.set(properties("descriptionFile").flatMap {
          providers.fileContents(layout.projectDirectory.file(it)).asText
        })

        changeNotes.set(properties("changesFile").flatMap {
          providers.fileContents(layout.projectDirectory.file(it)).asText
        })
      }
    }
  }
}

tasks {
  val jacocoRootReport = register<JacocoReport>("jacocoRootReport") {
    group = "verification"
    description = "Generates an aggregate report from all projects"

    dependsOn(allprojects.map {
      it.tasks.named("jacocoTestReport").map { task -> task.dependsOn }
    })
    mustRunAfter(allprojects.map {
      it.tasks.named("jacocoTestReport").map { task -> task.mustRunAfter }
    })
    executionData(allprojects.map {
      it.tasks.named<JacocoReport>("jacocoTestReport").map { task -> task.executionData }
    })

    executionData(File("coverage").walkTopDown().filter { it.extension == "exec" }.toList())

    additionalSourceDirs.setFrom(allprojects.map {
      it.sourceSets.main.map { sourceSet -> sourceSet.allSource.srcDirs }
    })
    sourceDirectories.setFrom(allprojects.map {
      it.sourceSets.main.map { sourceSet -> sourceSet.allSource.srcDirs }
    })
    classDirectories.setFrom(allprojects.map {
      it.tasks.named<InstrumentCodeTask>("instrumentCode").map { task -> task.outputDir }
    })

    reports {
      html.required.set(true) // human readable
      xml.required.set(true) // required by coveralls
      csv.required.set(false)
    }
  }

  withType<CoverallsTask> {
    group = "verification"
    description = "Uploads the aggregated coverage report to Coveralls"
    dependsOn(jacocoRootReport)
  }

  register("generateLexers") { }


  runIde {
    project.properties.forEach { (key, value) ->
      if (key.startsWith("pass.")) {
        val passedKey = key.substring(5)
        println("Passing $passedKey => $value")
        systemProperty(passedKey, value.toString())
      }
    }

    if (project.hasProperty("traverseUI")) {
      args("traverseUI", rootDir.resolve("resources/search/searchableOptions.xml"))
    }

    jvmArgs("-Xmx2048m")
  }
}


val coverageReportFile = project.buildDir.resolve("reports/jacoco/jacocoRootReport/jacocoRootReport.xml")
sonarqube {
  properties {
    property("sonar.projectKey", "Camelcade_Perl5-IDEA")
    property("sonar.organization", "camelcade")
    property("sonar.host.url", "https://sonarcloud.io")
    property("sonar.coverage.jacoco.xmlReportPaths", coverageReportFile)
  }
}

coveralls {
  jacocoReportPath = coverageReportFile
  allprojects.map {
    sourceDirs.addAll(it.sourceSets.main.get().allSource.sourceDirectories.map { it.absolutePath })
  }
}

intellij {
  val pluginList = mutableListOf(
    properties("intelliLangPlugin").get(),
    project(":plugin"),
    project(":lang.tt2"),
    project(":lang.mojo"),
    project(":lang.embedded"),
    project(":lang.mason.framework"),
    project(":lang.mason.htmlmason"),
    project(":lang.mason.mason2"),
  )

  if (!isCI.get()) {
    pluginList.add("PsiViewer:${properties("psiViewerVersion").get()}")
  }

  val runWith = properties("runWith").orElse("")
  when (runWith.get()) {
    "CL" -> {
      type.set("CL")
      version.set(properties("clionVersion"))
    }

    "PC" -> {
      type.set("PC")
      version.set(properties("pycharmVersion"))
    }

    "PY" -> {
      type.set("PY")
      version.set(properties("pycharmVersion"))
      pluginList.add("Docker")
      pluginList.add(properties("remoteRunPlugin").get())
    }

    else -> {
      type.set("IU")
      pluginList.add("Docker")
      pluginList.add(properties("coveragePlugin").get())
      pluginList.add(properties("remoteRunPlugin").get())
    }
  }

  plugins.set(pluginList)
}
