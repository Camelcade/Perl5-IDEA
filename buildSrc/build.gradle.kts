/**
 * Hack plugin to put a proper version of commons-compress into the classpath of the Gradle process.
 */
plugins {
  `kotlin-dsl`
  `java-gradle-plugin`
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.apache.commons:commons-compress:1.27.1")
}

gradlePlugin {
  plugins {
    create("fixCompressPlugin") {
      id = "com.hurricup.gradle.fixcompress"
      implementationClass = "com.hurricup.gradle.FixCompressPlugin"
    }
  }
}
