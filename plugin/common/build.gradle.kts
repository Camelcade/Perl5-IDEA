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
import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.jetbrains.intellij.platform.gradle.tasks.GenerateLexerTask
import org.jetbrains.intellij.platform.gradle.tasks.GenerateParserTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


val genRoot: File = project.file("src/main/gen").also { genRoot ->
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
  intellijPlatform {
    val platformVersionProvider: Provider<String> by rootProject.extra
        create(IntelliJPlatformType.IntellijIdeaUltimate, platformVersionProvider.get()){
      useInstaller = providers.gradleProperty("useInstaller").get().toBoolean()
    }

    bundledModules(
      "intellij.spellchecker"
    )
    bundledLibrary("plugins/cwm-plugin/lib/split.jar")
  }
}

tasks {
  val generatePerlParserTask = register<GenerateParserTask>("generatePerl5Parser") {
    description = "Generates perl5 parser"
    sourceFile.set(file("grammar/Perl5.bnf"))
    targetRootOutputDir.set(genRoot)
    pathToParser.set("com/perl5/lang/perl/parser/PerlParserGenerated.java")
    pathToPsiRoot.set("com/perl5/lang/perl/psi")
  }

  val generatePodParserTask = register<GenerateParserTask>("generatePodParser") {
    description = "Generates perl pod parser"
    sourceFile.set(file("grammar/Pod.bnf"))
    targetRootOutputDir.set(genRoot)
    pathToParser.set("com/perl5/lang/pod/parser/PodParserGenerated.java")
    pathToPsiRoot.set("com/perl5/lang/pod/psi")
  }

  val generatePerlLexerTask = register<GenerateLexerTask>("generatePerlLexer") {
    description = "Generates perl5 lexer"
    sourceFile.set(file("grammar/Perl.flex"))
    targetRootOutputDir.set(genRoot)

    dependsOn(generatePerlParserTask)
  }
  val generatePodLexerTask = register<GenerateLexerTask>("generatePodLexer") {
    description = "Generates perl pod lexer"
    sourceFile.set(file("grammar/Pod.flex"))
    targetRootOutputDir.set(genRoot)

    dependsOn(generatePodParserTask)
  }
  rootProject.tasks.findByName("generateLexers")?.dependsOn(
    generatePerlLexerTask,
    generatePodLexerTask
  )

  withType<GenerateLexerTask> {
    skeleton.set(rootProject.file(providers.gradleProperty("lexer_skeleton").get()))
    purgeOldFiles.set(false)
  }

  withType<GenerateParserTask> {
    targetRootOutputDir.set(genRoot)
    purgeOldFiles.set(true)
  }

  withType<JavaCompile> {
    dependsOn(
      generatePerlLexerTask,
      generatePodLexerTask
    )
  }
  withType<KotlinCompile> {
    dependsOn(
      generatePerlLexerTask,
      generatePodLexerTask
    )
  }
}
