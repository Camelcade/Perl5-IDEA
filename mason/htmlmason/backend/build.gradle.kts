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

fun properties(key: String) = providers.gradleProperty(key)

project.file("src/main/gen").let { genRoot ->
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
    ":plugin.backend",
    ":lang.mason.htmlmason.common",
    ":lang.mason.framework.common",
    ":lang.mason.framework.backend",
  ).forEach {
    compileOnly(project(it))
  }
  intellijPlatform {
    val platformVersionProvider: Provider<String> by rootProject.extra
    create("IC", platformVersionProvider.get(), useInstaller = properties("useInstaller").get().toBoolean())
  }
}

tasks {
  val generateLexerTask = register<GenerateLexerTask>("generateHTMLMasonLexer") {
    sourceFile.set(file("grammar/HTMLMason.flex"))
    targetOutputDir.set(file("src/main/gen/com/perl5/lang/htmlmason/lexer/"))
    skeleton.set(rootProject.file(properties("templating_lexer_skeleton").get()))
    purgeOldFiles.set(true)
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
