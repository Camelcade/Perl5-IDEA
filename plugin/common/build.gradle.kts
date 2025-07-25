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
import org.jetbrains.grammarkit.tasks.GenerateLexerTask
import org.jetbrains.grammarkit.tasks.GenerateParserTask
import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType



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
  intellijPlatform {
    val platformVersionProvider: Provider<String> by rootProject.extra
        create(IntelliJPlatformType.IntellijIdeaCommunity, platformVersionProvider.get()){
      useInstaller = providers.gradleProperty("useInstaller").get().toBoolean()
    }

    bundledModules(
      "intellij.spellchecker"
    )
  }
}

tasks {
  val generatePerlParserTask = register<GenerateParserTask>("generatePerl5Parser") {
    sourceFile.set(file("grammar/Perl5.bnf"))
    pathToParser.set("/com/perl5/lang/perl/parser/PerlParserGenerated.java")
    pathToPsiRoot.set("/com/perl5/lang/perl/psi")
  }

  val generatePodParserTask = register<GenerateParserTask>("generatePodParser") {
    sourceFile.set(file("grammar/Pod.bnf"))
    pathToParser.set("/com/perl5/lang/pod/parser/PodParserGenerated.java")
    pathToPsiRoot.set("/com/perl5/lang/pod/psi")
  }

  val generatePerlLexerTask = register<GenerateLexerTask>("generatePerlLexer") {
    sourceFile.set(file("grammar/Perl.flex"))
    targetOutputDir.set(file("src/main/gen/com/perl5/lang/perl/lexer/"))

    dependsOn(generatePerlParserTask)
  }
  val generatePodLexerTask = register<GenerateLexerTask>("generatePodLexer") {
    sourceFile.set(file("grammar/Pod.flex"))
    targetOutputDir.set(file("src/main/gen/com/perl5/lang/pod/lexer/"))

    dependsOn(generatePodParserTask)
  }
  rootProject.tasks.findByName("generateLexers")?.dependsOn(
    generatePerlLexerTask,
    generatePodLexerTask
  )

  withType<GenerateLexerTask> {
    skeleton.set(rootProject.file(providers.gradleProperty("lexer_skeleton").get()))
    purgeOldFiles.set(true)
  }

  withType<GenerateParserTask> {
    targetRootOutputDir.set(genRoot)
    purgeOldFiles.set(true)
  }

  /*
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
  */
}
