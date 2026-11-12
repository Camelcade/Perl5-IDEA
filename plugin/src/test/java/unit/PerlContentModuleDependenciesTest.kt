/*
 * Copyright 2015-2026 Alexandr Evstigneev
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

package unit

import categories.CategoriesFilter
import categories.Smoke
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.util.JDOMUtil
import com.intellij.testFramework.UsefulTestCase
import org.jdom.Element
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import unit.PerlContentModuleDependenciesTest.Companion.DEPENDENCIES_FILE_PROPERTY
import java.nio.file.Path
import java.util.*
import java.util.zip.ZipFile
import kotlin.io.path.bufferedReader
import kotlin.io.path.exists
import kotlin.io.path.readText

/**
 * Checks that the `<dependencies>` of each content module descriptor list every module the content module compiles against.
 *
 * The declared compile dependencies come from the `exportContentModuleDependencies` task of the root `build.gradle.kts`,
 * which passes its output file to the `:plugin` test task via the [DEPENDENCIES_FILE_PROPERTY] system property.
 * The expected descriptor is the actual one with the missing dependencies appended; run with `-Poverwrite` to write it back.
 *
 * @param moduleName the name of the checked content module
 */
@Category(Smoke::class)
@RunWith(Parameterized::class)
class PerlContentModuleDependenciesTest(private val moduleName: String) {
  @Test
  fun testDescriptorDependencies() {
    val descriptor = exportedModules.descriptors.getValue(moduleName)
    assertTrue("Descriptor of $moduleName does not exist: $descriptor", descriptor.exists())

    val requiredModules = exportedModules.dependencies[moduleName].orEmpty()
    val unknownModules = requiredModules.filterNot(::isKnownModule)
    assertTrue(
      "$moduleName compiles against modules unknown to the IDE module registry and to our content modules: $unknownModules",
      unknownModules.isEmpty()
    )

    val descriptorText = descriptor.readText()
    val missingModules = requiredModules - declaredModuleDependencies(descriptorText)
    UsefulTestCase.assertSameLinesWithFile(
      descriptor.toString(),
      appendDependencies(descriptorText, missingModules.map(::moduleDependencyTag)),
      false
    ) { "$moduleName compiles against $missingModules, but its descriptor does not depend on them; run with -Poverwrite to add them" }
  }

  /**
   * Returns the `<module>` dependency tag on [dependencyName], with the namespace of the plugin owning it when this plugin
   * does not own it.
   */
  private fun moduleDependencyTag(dependencyName: String): String {
    val dependencyOwner = exportedModules.owners[dependencyName]
    val namespace = dependencyOwner?.takeIf { it.pluginName != exportedModules.owners[moduleName]?.pluginName }?.namespace
    return if (namespace == null) """<module name="$dependencyName"/>""" else """<module name="$dependencyName" namespace="$namespace"/>"""
  }

  /**
   * Holds the plugin declaring a content module in its `<content>`.
   *
   * @property pluginName the name of the plugin project
   * @property namespace the namespace of the `<content>` declaring the module, if any
   */
  private data class ContentModuleOwner(val pluginName: String, val namespace: String?)

  /**
   * Holds the content modules exported by the `exportContentModuleDependencies` task.
   *
   * @property descriptors the descriptor paths by content module name
   * @property dependencies the names of modules each content module compiles against, by content module name
   * @property owners the plugins declaring content modules, by content module name
   */
  private class ExportedContentModules(
    val descriptors: Map<String, Path>,
    val dependencies: Map<String, List<String>>,
    val owners: Map<String, ContentModuleOwner>,
  )

  companion object {
    /** Names the system property set for the `:plugin` test task next to `exportContentModuleDependencies` in the root build. */
    private const val DEPENDENCIES_FILE_PROPERTY = "perl5.content.module.dependencies.file"
    private const val DESCRIPTOR_KEY_PREFIX = "descriptor."
    private const val DEPENDENCIES_KEY_PREFIX = "dependencies."
    private const val PLUGIN_KEY_PREFIX = "plugin."
    private const val INDENT = "  "

    private val DEPENDENCIES_END = Regex("""^([ \t]*)</dependencies>""", RegexOption.MULTILINE)
    private val RESOURCE_BUNDLE_LINE = Regex("""^([ \t]*)<resource-bundle>.*</resource-bundle>[ \t]*\r?\n""", RegexOption.MULTILINE)
    private val IDEA_PLUGIN_START_LINE = Regex("""<idea-plugin\b[^>]*>[ \t]*\r?\n""")

    private val exportedModules: ExportedContentModules by lazy { loadExportedModules() }

    private val platformModuleNames: Set<String> by lazy { loadPlatformModuleNames() }

    @JvmStatic
    @Parameterized.Parameters(name = "{0}")
    fun contentModules(): Collection<String> =
      if (CategoriesFilter.shouldRun(PerlContentModuleDependenciesTest::class.java)) exportedModules.descriptors.keys.sorted() else emptyList()

    private fun isKnownModule(moduleName: String): Boolean =
      moduleName in exportedModules.descriptors || moduleName in platformModuleNames

    /**
     * Returns the names of modules listed in the `<dependencies>` of the descriptor; plugin dependencies are ignored.
     */
    private fun declaredModuleDependencies(descriptorText: String): Set<String> =
      JDOMUtil.load(descriptorText).getChild("dependencies")?.getChildren("module")
        ?.mapNotNullTo(mutableSetOf()) { it.getAttributeValue("name") }
        .orEmpty()

    /**
     * Returns the descriptor text with [dependencyTags] appended to the end of its `<dependencies>`, keeping the formatting.
     *
     * A descriptor without `<dependencies>` gets one after its `<resource-bundle>`, or at the top of `<idea-plugin>`.
     */
    private fun appendDependencies(descriptorText: String, dependencyTags: List<String>): String {
      if (dependencyTags.isEmpty()) {
        return descriptorText
      }
      DEPENDENCIES_END.find(descriptorText)?.let { dependenciesEnd ->
        val entriesIndent = dependenciesEnd.groupValues[1] + INDENT
        return descriptorText.insertAt(dependenciesEnd.range.first, dependencyTags.joinToString("") { "$entriesIndent$it\n" })
      }
      val resourceBundleLine = RESOURCE_BUNDLE_LINE.find(descriptorText)
      val anchor = resourceBundleLine ?: IDEA_PLUGIN_START_LINE.find(descriptorText)
                   ?: error("No <idea-plugin> start tag on its own line found in the descriptor:\n$descriptorText")
      val blockIndent = resourceBundleLine?.groupValues?.get(1) ?: INDENT
      val dependenciesBlock = buildString {
        append("$blockIndent<dependencies>\n")
        dependencyTags.forEach { append("$blockIndent$INDENT$it\n") }
        append("$blockIndent</dependencies>\n")
      }
      return descriptorText.insertAt(anchor.range.last + 1, dependenciesBlock)
    }

    private fun String.insertAt(offset: Int, text: String): String = replaceRange(offset, offset, text)

    private fun loadExportedModules(): ExportedContentModules {
      val exportFile = checkNotNull(System.getProperty(DEPENDENCIES_FILE_PROPERTY)) {
        "System property $DEPENDENCIES_FILE_PROPERTY is not set; it is set by the :plugin test task from the output of " +
        "the exportContentModuleDependencies task, run the test with Gradle"
      }.let(Path::of)
      check(exportFile.exists()) { "Content module dependencies file does not exist: $exportFile" }
      val properties = Properties().apply { exportFile.bufferedReader().use(::load) }.let { loaded ->
        loaded.stringPropertyNames().associateWith(loaded::getProperty)
      }

      fun valuesWithPrefix(prefix: String): Map<String, String> =
        properties.filterKeys { it.startsWith(prefix) }.mapKeys { it.key.removePrefix(prefix) }

      return ExportedContentModules(
        descriptors = valuesWithPrefix(DESCRIPTOR_KEY_PREFIX).mapValues { Path.of(it.value) },
        dependencies = valuesWithPrefix(DEPENDENCIES_KEY_PREFIX).mapValues { (_, names) -> names.split(',').filter(String::isNotEmpty) },
        owners = valuesWithPrefix(PLUGIN_KEY_PREFIX)
          .flatMap { (pluginName, pluginDescriptor) -> contentModuleOwners(pluginName, Path.of(pluginDescriptor)) }
          .toMap(),
      )
    }

    /**
     * Returns the content modules declared by the plugin descriptor, paired with their owner.
     */
    private fun contentModuleOwners(pluginName: String, pluginDescriptor: Path): List<Pair<String, ContentModuleOwner>> =
      JDOMUtil.load(pluginDescriptor).getChildren("content").flatMap { content: Element ->
        val owner = ContentModuleOwner(pluginName, content.getAttributeValue("namespace"))
        content.getChildren("module").map { it.getAttributeValue("name") to owner }
      }

    /**
     * Returns the names of modules in the module registry of the IDE under test, `modules/module-descriptors.jar`.
     */
    private fun loadPlatformModuleNames(): Set<String> {
      val registry = Path.of(PathManager.getHomePath(), "modules", "module-descriptors.jar")
      check(registry.exists()) { "IDE module registry does not exist: $registry" }
      return ZipFile(registry.toFile()).use { zip ->
        zip.entries().asSequence()
          .map { it.name }
          .filter { '/' !in it && it.endsWith(".xml") }
          .mapTo(mutableSetOf()) { it.removeSuffix(".xml") }
      }
    }
  }
}
