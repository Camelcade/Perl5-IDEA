import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType



plugins {
  id("java-test-fixtures")
}

dependencies {
  listOf(
    ":plugin.asdf",
    ":plugin.berrybrew",
    ":plugin.common",
    ":plugin.backend",
    ":plugin.cpan",
    ":plugin.cpanminus.backend",
    ":plugin.cpanminus.common",
    ":plugin.docker",
    ":plugin.frontend",
    ":plugin.idea",
    ":plugin.perlInstall",
    ":plugin.perlbrew",
    ":plugin.plenv",
  ).forEach {
    testFixturesCompileOnly(project(it))
  }
  testFixturesCompileOnly("junit:junit:4.13.2")

  intellijPlatform {
    val platformVersionProvider: Provider<String> by rootProject.extra
        create(IntelliJPlatformType.IntellijIdeaCommunity, platformVersionProvider.get()){
      useInstaller = providers.gradleProperty("useInstaller").get().toBoolean()
    }

    bundledPlugins(
      providers.gradleProperty("intelliLangPlugin").get(),
      "org.jetbrains.plugins.terminal",
    )
  }
}

tasks {
  listOf(
    "composedJar",
    "instrumentedJar",
    "jar",
    "prepareSandbox",
    "prepareTestSandbox",
    "processResources",
  ).forEach {
    project.tasks.named(it).configure { enabled = false }
  }
  testFixturesJar {
    archiveVersion = ""
  }
}

