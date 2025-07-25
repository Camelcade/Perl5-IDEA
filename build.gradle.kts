import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.jvm.tasks.Jar
import org.jetbrains.intellij.platform.gradle.Constants.Sandbox.Plugin.LIB
import org.jetbrains.intellij.platform.gradle.Constants.Sandbox.Plugin.LIB_MODULES
import org.jetbrains.intellij.platform.gradle.Constants.Tasks.INSTRUMENT_CODE
import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.jetbrains.intellij.platform.gradle.ProductMode
import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.intellij.platform.gradle.tasks.InstrumentCodeTask
import org.jetbrains.intellij.platform.gradle.tasks.PrepareSandboxTask
import org.jetbrains.intellij.platform.gradle.tasks.aware.IntelliJPlatformVersionAware
import org.jetbrains.intellij.platform.gradle.tasks.aware.SplitModeAware.SplitModeTarget
import org.kt3k.gradle.plugin.coveralls.CoverallsTask
import java.nio.file.Files
import kotlin.io.path.exists
import kotlin.io.path.moveTo
import kotlin.io.path.readText
import kotlin.io.path.writeText

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
  id("org.jetbrains.intellij.platform") version "2.6.1-SNAPSHOT"
  id("org.jetbrains.grammarkit") version "2022.3.2.2"
  id("com.github.kt3k.coveralls") version "2.12.2"
  id("org.sonarqube") version "6.2.0.5505"
  id("org.jetbrains.qodana") version "0.1.13"
  id("org.jetbrains.kotlin.jvm") version "2.2.0"
}

repositories {
  mavenCentral()
}

val isCI = environment("CI").map { it.toBoolean() }.orElse(false)
val withCoverage = environment("COVERALLS_REPO_TOKEN").orElse(providers.gradleProperty("with_coverage")).map { !it.isEmpty() }.orElse(false)
val platformVersionProvider by extra(project.provider {
  providers.gradleProperty("platformVersion").get() + providers.gradleProperty("platformBranch").get() + providers.gradleProperty("platformBuild").get()
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
    jflexRelease.set(providers.gradleProperty("jflexVersion"))
  }

  version = providers.gradleProperty("pluginVersion").get().ifEmpty { providers.gradleProperty("platformVersion").get() } +
    providers.gradleProperty("pluginBranch").get().ifEmpty { providers.gradleProperty("platformBranch").get() } +
    providers.gradleProperty("pluginBuild").get().ifEmpty { providers.gradleProperty("platformBuild").get() }

  dependencies {
    intellijPlatform {
      val platformToolsVersion = providers.gradleProperty("platformToolsVersion")
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
      jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.fromTarget(providers.gradleProperty("javaTargetVersion").get()))
    }
  }

  tasks {
    withType<JavaCompile> {
      options.encoding = "UTF-8"
      sourceCompatibility = providers.gradleProperty("javaVersion").get()
      targetCompatibility = providers.gradleProperty("javaTargetVersion").get()
    }

    test {
      maxHeapSize = "2048m"
      outputs.upToDateWhen { false }

      ignoreFailures = false

      if (project.hasProperty("overwrite")) {
        systemProperty("idea.tests.overwrite.data", "true")
      }

      if (project.hasProperty("youtrack.token")) {
        systemProperty("youtrack.token", providers.gradleProperty("youtrack.token").get())
      }

      val envPerlVersion = environment("PERL_TEST_VERSION").orElse(providers.gradleProperty("perl.test.version"))
      systemProperty("perl.test.version", envPerlVersion.get())

      if (project.hasProperty("idea.split.test.logs")) {
        systemProperty("idea.split.test.logs", "true")
        systemProperty("idea.single.test.log.max.length", "100_000_000")
      }

      useJUnit {
        if (project.hasProperty("runtest")) {
          include("**/" + providers.gradleProperty("runtest").get() + ".class")
        }

        val excludeCategories = mutableListOf<String>()
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

    listOf(
      "composedJar",
      "instrumentedJar",
      "jar",
    ).forEach {
      project.tasks.named<Jar>(it).configure {
        archiveVersion = ""
        archiveBaseName = archiveBaseName(project.name)
      }
    }

    if (isRoot || isPlugin) {
      project.tasks.register<Task>("normalizeSandbox") {
        dependsOn(project.tasks.named("prepareSandbox"))
        doLast {
          normalizeSandbox(project, "prepareSandbox")
        }
      }
      listOf(
        "buildSearchableOptions",
        "runIde",
        "buildPlugin",
      ).forEach {
        project.tasks.named(it).configure { dependsOn(project.tasks.named("normalizeSandbox")) }
      }
    }

    if (isRoot) {
      listOf(
        "buildPlugin",
        "buildSearchableOptions",
        "classes",
        "compileJava",
        "compileKotlin",
//        "composedJar", // disabling these tasks fails the build, because preparesandbox wants jar anyways
//        "generateManifest",
        "instrumentCode",
//        "instrumentedJar",
//        "jar",
        "patchPluginXml",
        "prepareJarSearchableOptions",
        "processResources",
        "publishPlugin",
        "verifyPlugin",
        "verifyPluginProjectConfiguration",
      ).forEach {
        project.tasks.named(it).configure { enabled = false }
      }
    }

    if (isPlugin) {
      jarSearchableOptions {
        archiveVersion = ""
      }

      project.tasks.register<Task>("normalizeTestSandbox") {
        dependsOn(project.tasks.named("prepareTestSandbox"))
        doLast {
          normalizeSandbox(project, "prepareTestSandbox")
        }
      }

      listOf(
        "test",
      ).forEach {
        project.tasks.named(it).configure { dependsOn(project.tasks.named("normalizeTestSandbox")) }
      }
      publishPlugin {
        if (project.hasProperty("eap")) {
          channels.set(listOf("EAP"))
        }
        token.set(providers.gradleProperty("jbToken").orElse(""))
      }

      patchPluginXml {
        pluginDescription.set(providers.gradleProperty("descriptionFile").flatMap {
          providers.fileContents(layout.projectDirectory.file(it)).asText
        })

        changeNotes.set(providers.gradleProperty("changesFile").flatMap {
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

    project.logger.info("\tFound following coverage data files:")
    files.sorted().forEach { project.logger.info("\t- $it") }

    executionData(files)

    val sourcesDirs = allprojects.map {
      it.sourceSets.main.map { sourceSet -> sourceSet.allSource.srcDirs }
    }
    project.logger.info("\tFound following source dirs:")
    sourcesDirs.flatMap { it.get().asSequence() }.sorted().forEach { project.logger.info("\t- $it") }

    additionalSourceDirs.setFrom(sourcesDirs)
    sourceDirectories.setFrom(sourcesDirs)

    val classDirs = allprojects.map { project ->
      project.tasks.named<InstrumentCodeTask>(INSTRUMENT_CODE).map { it.outputDirectory }
    }
    project.logger.info("\tFound following class dirs:")
    classDirs.map { it.get().get().toString() }.sorted().forEach { project.logger.info("\t- $it") }

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

// hack for IJPL-198341
abstract class FixClientExecutable : DefaultTask(), IntelliJPlatformVersionAware {
  @TaskAction
  fun fix() {
    val client = platformPath.resolve("bin/linux/amd64/jetbrains_client.sh").takeIf { it.exists() } ?: return
    val content = client.readText()

    client.writeText(content.replace("IDE_HOME=\$(dirname \"\${IDE_BIN_HOME}\")",
                                     "IDE_HOME=\$(dirname \$(dirname \$(dirname \"\${IDE_BIN_HOME}\")))"))
  }
}

val fixClientExecutable by tasks.registering(FixClientExecutable::class)

val runInSplitMode by intellijPlatformTesting.runIde.registering {
  splitMode = true
  splitModeTarget = SplitModeTarget.BOTH
  task {
    dependsOn(fixClientExecutable)
  }
}

val coverageReportFile = project.layout.buildDirectory.file("reports/jacoco/jacocoRootReport/jacocoRootReport.xml")
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
  allprojects.map { project ->
    sourceDirs.addAll(project.sourceSets.main.get().allSource.sourceDirectories.map { it.absolutePath })
  }
}

intellijPlatform {
  val pluginList = mutableListOf<String>()
  val bundledPluginList = mutableListOf(providers.gradleProperty("intelliLangPlugin").get())

  if (!isCI.get()) {
    pluginList.add("PsiViewer:${providers.gradleProperty("psiViewerVersion").get()}")
  }

  val runWith = providers.gradleProperty("runWith").orElse("")
  val (ideType, ideVersion) = when (runWith.get()) {
    "CL" -> {
      IntelliJPlatformType.CLion to providers.gradleProperty("clionVersion").get()
    }
    "PC" -> {
      IntelliJPlatformType.PyCharmCommunity to providers.gradleProperty("pycharmVersion").get()
    }
    "PY" -> {
      bundledPluginList.add("Docker")
      bundledPluginList.add(providers.gradleProperty("remoteRunPlugin").get())
      IntelliJPlatformType.PyCharmProfessional to providers.gradleProperty("pycharmVersion").get()
    }
    else -> {
      bundledPluginList.add("Docker")
      bundledPluginList.add(providers.gradleProperty("coveragePlugin").get())
      bundledPluginList.add(providers.gradleProperty("remoteRunPlugin").get())
      IntelliJPlatformType.IntellijIdeaUltimate to platformVersionProvider.get()
    }
  }
  dependencies {
    intellijPlatform {
      create(ideType, ideVersion){
        useInstaller = providers.gradleProperty("useInstaller").get().toBoolean()
        productMode = ProductMode.MONOLITH
      }
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

fun normalizeSandbox(project: Project, taskName: String) {
  project.logger.info("Normalizing $taskName results for ${project.name}")
  val pluginsRootPath = project.tasks.named<PrepareSandboxTask>(taskName).get().defaultDestinationDirectory.get().asFile.toPath()
  project.logger.info("\tPlugins root $pluginsRootPath")
  for (pluginName in pluginProjectsNames) {
    val pluginRootPath = pluginsRootPath.resolve(pluginName)
    if (!Files.exists(pluginRootPath)) {
      continue
    }
    project.logger.info("\tProcessing $pluginRootPath")
    val mainJarName = "${archiveBaseName(pluginName)}.jar"
    val pluginLibPath = pluginRootPath.resolve(LIB)
    val pluginLibModulesPath = pluginRootPath.resolve(LIB_MODULES)
    Files.createDirectories(pluginLibModulesPath)
    Files.list(pluginLibPath).use {
      it.filter { path ->
        val fileName = path.fileName.toString()
        fileName.endsWith(".jar") &&
          fileName.startsWith(archiveBasePrefix(pluginName)) &&
          fileName != mainJarName &&
          !fileName.contains("searchableOptions")
      }.forEach { path ->
        project.logger.info("\t\tMoving $path to $pluginLibModulesPath")
        path.moveTo(pluginLibModulesPath.resolve(path.fileName))
      }
    }
  }
}

fun archiveBasePrefix(projectName: String) = "${rootProject.name}.${projectName}"
fun archiveBaseName(projectName: String) = "${archiveBasePrefix(projectName)}.main"
