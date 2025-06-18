import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.intellij.platform.gradle.Constants.Tasks.INSTRUMENT_CODE
import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.intellij.platform.gradle.tasks.InstrumentCodeTask
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
    classpath("org.eclipse.jgit:org.eclipse.jgit:7.3.0.202506031305-r")
  }
}

plugins {
  id("com.hurricup.gradle.fixcompress")
  id("idea")
  id("jacoco")
  id("org.jetbrains.intellij.platform") version "2.6.0"
  id("org.jetbrains.grammarkit") version "2022.3.2.2"
  id("com.github.kt3k.coveralls") version "2.12.2"
  id("org.sonarqube") version "6.2.0.5505"
  id("org.jetbrains.qodana") version "0.1.13"
  id("org.jetbrains.kotlin.jvm") version "2.1.21"
}

repositories {
  mavenCentral()
}

val isCI = environment("CI").map { it.toBoolean() }.orElse(false)
val withCoverage = environment("COVERALLS_REPO_TOKEN").orElse(properties("with_coverage")).map { !it.isEmpty() }.orElse(false)
val platformVersionProvider by extra(project.provider {
  properties("platformVersion").get() + properties("platformBranch").get() + properties("platformBuild").get()
})

val pluginProjectsNames = setOf(
  "plugin",
  "lang.tt2",
  "lang.mojo",
  "lang.embedded",
  "lang.mason.framework",
  "lang.mason.htmlmason",
  "lang.mason.mason2"
)

allprojects {
  if (name == "test-utils") {
    return@allprojects
  }
  val isPlugin = project.name in pluginProjectsNames

  apply(plugin = "org.jetbrains.grammarkit")
  apply(plugin = if (isPlugin) "org.jetbrains.intellij.platform" else "org.jetbrains.intellij.platform.module")
  apply(plugin = "com.github.kt3k.coveralls")
  apply(plugin = "jacoco")
  apply(plugin = "java")
  apply(plugin = "org.jetbrains.kotlin.jvm")

  repositories {
    mavenCentral()
    intellijPlatform {
      defaultRepositories()
      jetbrainsRuntime()
      nightly()
    }
  }

  grammarKit {
    jflexRelease.set(properties("jflexVersion"))
  }

  version = properties("pluginVersion").get().ifEmpty { properties("platformVersion").get() } +
    properties("pluginBranch").get().ifEmpty { properties("platformBranch").get() } +
    properties("pluginBuild").get().ifEmpty { properties("platformBuild").get() }

  dependencies {
    intellijPlatform {
      val platformToolsVersion = properties("platformToolsVersion")
      if (platformToolsVersion.get().isEmpty()) {
        testFramework(TestFrameworkType.Platform)
      }
      else {
        javaCompiler(platformToolsVersion)
        testFramework(TestFrameworkType.Platform, version = platformToolsVersion)
      }
      testFramework(TestFrameworkType.Bundled)
      jetbrainsRuntime()
    }
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.opentest4j:opentest4j:1.3.0")
  }

  kotlin {
    compilerOptions {
      jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.fromTarget(properties("javaTargetVersion").get()))
    }
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

      ignoreFailures = false

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

        var excludeCategories = mutableListOf<String>()
        if (project.hasProperty("integrationTests")) {
          excludeCategories += listOf("categories.Light", "categories.Heavy")
        }
        else if (project.hasProperty("heavyTests")) {
          excludeCategories += listOf("categories.Light", "categories.Integration")
        }
        else if (project.hasProperty("lightTests")) {
          excludeCategories += listOf("categories.Heavy", "categories.Integration")
        }
        else if (!project.hasProperty("allTests")) {
          excludeCategories += listOf("categories.Integration")
        }

        if (excludeCategories.isNotEmpty()) {
          excludeCategories(*excludeCategories.toTypedArray())
          systemProperty("junit.exclude.categories", excludeCategories.joinToString(","))
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
          events.addAll(
            listOf(
              TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED, TestLogEvent.STANDARD_OUT, TestLogEvent.STANDARD_ERROR
            )
          )
          exceptionFormat = TestExceptionFormat.FULL
        }
      }
    }

    val isRoot = project == rootProject

    if (isRoot) {
      buildPlugin {
        enabled = false
      }
      prepareTestSandbox {
        enabled = false
      }
      patchPluginXml {
        enabled = false
      }
      buildSearchableOptions {
        enabled = false
      }
      prepareJarSearchableOptions {
        enabled = false
      }
      verifyPlugin {
        enabled = false
      }
      verifyPluginProjectConfiguration {
        enabled = false
      }
      publishPlugin {
        enabled = false
      }
    }
    if (isPlugin) {
      publishPlugin {
        if (project.hasProperty("eap")) {
          channels.set(listOf("EAP"))
        }
        token.set(properties("jbToken").orElse(""))
      }

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

    val files = File("coverage").walkTopDown().filter { it.extension == "exec" }.toList()

    println("\nFound following coverage data files:")
    files.sorted().forEach { println(it) }

    executionData(files)

    val sourcesDirs = allprojects.map {
      it.sourceSets.main.map { sourceSet -> sourceSet.allSource.srcDirs }
    }
    println("\nFound following source dirs:")
    sourcesDirs.flatMap { it.get().asSequence() }.sorted().forEach { println(it) }

    additionalSourceDirs.setFrom(sourcesDirs)
    sourceDirectories.setFrom(sourcesDirs)

    val classDirs = allprojects.map {
      it.tasks.named<InstrumentCodeTask>(INSTRUMENT_CODE).map { it -> it.outputDirectory }
    }
    println("\nFound following class dirs:")
    classDirs.map { it.get().get().toString() }.sorted().forEach { println(it) }

    classDirectories.from(classDirs)

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

    jvmArgs("-Xmx2048m")
  }
}


val coverageReportFile = project.buildDir.resolve("reports/jacoco/jacocoRootReport/jacocoRootReport.xml")
sonar {
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

intellijPlatform {
  val pluginList = mutableListOf<String>()
  val bundledPluginList = mutableListOf(properties("intelliLangPlugin").get(),)

  if (!isCI.get()) {
    pluginList.add("PsiViewer:${properties("psiViewerVersion").get()}")
  }

  val runWith = properties("runWith").orElse("")
  val (ideType, ideVersion) = when (runWith.get()) {
    "CL" -> {
      "CL" to properties("clionVersion").get()
    }
    "PC" -> {
      "PC" to properties("pycharmVersion").get()
    }
    "PY" -> {
      bundledPluginList.add("Docker")
      bundledPluginList.add(properties("remoteRunPlugin").get())
      "PY" to properties("pycharmVersion").get()
    }
    else -> {
      bundledPluginList.add("Docker")
      bundledPluginList.add(properties("coveragePlugin").get())
      bundledPluginList.add(properties("remoteRunPlugin").get())
      "IU" to platformVersionProvider.get()
    }
  }
  dependencies {
    intellijPlatform {
      create(ideType, ideVersion, useInstaller = properties("useInstaller").get().toBoolean())
      listOf(
        project(":plugin"),
        project(":lang.tt2"),
        project(":lang.mojo"),
        project(":lang.embedded"),
        project(":lang.mason.framework"),
        project(":lang.mason.htmlmason"),
        project(":lang.mason.mason2"),
      ).forEach { localPlugin(it) }
      plugins(pluginList)
      bundledPlugins(bundledPluginList)
    }
  }
}

configurations.all {
  resolutionStrategy.cacheDynamicVersionsFor(7, "days")
}