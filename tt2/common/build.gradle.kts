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
  listOf(
    ":plugin.common",
  ).forEach {
    compileOnly(project(it))
  }
  intellijPlatform {
    val platformVersionProvider: Provider<String> by rootProject.extra
        create(IntelliJPlatformType.IntellijIdeaCommunity, platformVersionProvider.get()){
      useInstaller = providers.gradleProperty("useInstaller").get().toBoolean()
    }

  }
}

tasks {
  val generateParserTask = register<GenerateParserTask>("generateTT2Parser") {
    sourceFile.set(file("grammar/TemplateToolkit.bnf"))
    pathToParser.set("/com/perl5/lang/tt2/parser/TemplateToolkitParserGenerated.java")
    pathToPsiRoot.set("/com/perl5/lang/tt2/psi")
    targetRootOutputDir.set(genRoot)
    purgeOldFiles.set(true)
  }

  val generateLexerTask = register<GenerateLexerTask>("generateTT2Lexer") {
    sourceFile.set(file("grammar/TemplateToolkit.flex"))
    targetOutputDir.set(file("src/main/gen/com/perl5/lang/tt2/lexer/"))
    skeleton.set(rootProject.file(providers.gradleProperty("lexer_skeleton").get()))
    purgeOldFiles.set(true)

    dependsOn(generateParserTask)
  }

  rootProject.tasks.findByName("generateLexers")?.dependsOn(
    generateLexerTask
  )

  /*
    withType<JavaCompile> {
      dependsOn(generateLexerTask)
    }
    withType<KotlinCompile>{
      dependsOn(generateLexerTask)
    }
  */
}
