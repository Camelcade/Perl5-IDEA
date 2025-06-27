fun properties(key: String) = providers.gradleProperty(key)

plugins {
  id("java-test-fixtures")
}

dependencies {
  listOf(
    ":plugin.asdf",
    ":plugin.berrybrew",
    ":plugin.common",
    ":plugin.core",
    ":plugin.cpan",
    ":plugin.cpanminus",
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
    create("IC", platformVersionProvider.get(), useInstaller = providers.gradleProperty("useInstaller").get().toBoolean())
    bundledPlugins(
      properties("intelliLangPlugin").get(),
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

