/*
 * Copyright 2015-2025 Alexandr Evstigneev
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
import org.jetbrains.grammarkit.tasks.GenerateLexerTask
import org.jetbrains.grammarkit.tasks.GenerateParserTask
import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val genRoot = project.file("src/main/gen").also { genRoot ->
  sourceSets {
    main {
      java.srcDirs(genRoot)
    }
  }

  idea {
    module {
      generatedSourceDirs.add(genRoot)
    }
  }
}

dependencies {
  listOf(
    ":plugin.core",
    ":lang.tt2.common",
  ).forEach {
    compileOnly(project(it))
  }

  intellijPlatform {
    val platformVersionProvider: Provider<String> by rootProject.extra

    create(
      type = provider { IntelliJPlatformType.IntellijIdeaCommunity },
      version = platformVersionProvider,
      useInstaller = providers.gradleProperty("useInstaller").map { it.toBoolean() },
    )
  }
}

tasks {
  val generateTT2Parser by registering(GenerateParserTask::class) {
    sourceFile = file("grammar/TemplateToolkit.bnf")
    pathToParser = "/com/perl5/lang/tt2/parser/TemplateToolkitParserGenerated.java"
    pathToPsiRoot = "/com/perl5/lang/tt2/psi"
    targetRootOutputDir = genRoot
    purgeOldFiles = true
  }

  val generateTT2Lexer by registering(GenerateLexerTask::class) {
    sourceFile = file("grammar/TemplateToolkit.flex")
    targetOutputDir = file("src/main/gen/com/perl5/lang/tt2/lexer/")
    skeleton = providers.gradleProperty("lexer_skeleton").map { rootProject.file(it) }
    purgeOldFiles = true

    dependsOn(generateTT2Parser)
  }

  rootProject.tasks.named { it == "generateLexers" }.configureEach {
    dependsOn(generateTT2Lexer)
  }

  withType<JavaCompile> {
    dependsOn(generateTT2Lexer)
  }
  withType<KotlinCompile> {
    dependsOn(generateTT2Lexer)
  }
}
