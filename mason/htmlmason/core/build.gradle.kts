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

val genRoot = project.file("src/main/gen")

sourceSets {
  main {
    java.srcDirs(genRoot)
  }
}

dependencies {
  listOf(
    ":plugin:core",
    ":lang.mason.framework",
  ).forEach {
    compileOnly(project(it))
  }
}

tasks {
  val generateLexerTask = register<GenerateLexerTask>("generateHTMLMasonLexer") {
    sourceFile.set(file("grammar/HTMLMason.flex"))
    targetDir.set("src/main/gen/com/perl5/lang/htmlmason/lexer/")
    targetClass.set("HTMLMasonLexer")
    skeleton.set(rootProject.file(properties("templating_lexer_skeleton").get()))
    purgeOldFiles.set(true)
  }
  rootProject.tasks.findByName("generateLexers")?.dependsOn(
    generateLexerTask
  )

  withType<JavaCompile> {
    dependsOn(generateLexerTask)
  }

}
