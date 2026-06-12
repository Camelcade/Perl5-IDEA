import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType

dependencies {
  listOf(
    ":plugin.common",
    ":plugin.backend",
  ).forEach {
    compileOnly(project(it))
    testCompileOnly(project(it))
  }

  intellijPlatform {
    val platformVersionProvider: Provider<String> by rootProject.extra
    create(IntelliJPlatformType.IntellijIdeaUltimate, platformVersionProvider.get()){
      useInstaller = providers.gradleProperty("useInstaller").get().toBoolean()
    }
    bundledPlugins(
      "intellij.structureView.plugin",
    )
  }
}

