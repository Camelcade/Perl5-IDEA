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

package base

import com.intellij.ide.plugins.PluginMainDescriptor
import com.intellij.ide.plugins.PluginManager
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.util.io.FileUtil
import java.nio.file.Path

open class PerlDistributionTestCase : PerlLightTestCaseBase() {
  override fun getFileExtension(): String? = "none"

  override fun getBaseDataPath(): String = "unit/distribution"

  fun testDistribution(pluginId: String) {
    val pluginDescriptor = PluginManager.getInstance().findEnabledPlugin(PluginId.getId(pluginId)) as PluginMainDescriptor
    val result = StringBuilder("Plugin: $pluginId\n")

    val pluginPath = pluginDescriptor.pluginPath

    val relativizer = fun(path: Path): String = FileUtil.toSystemIndependentName(pluginPath.relativize(path).toString())

    result.append("Jar files:\n")
    pluginDescriptor.jarFiles!!.forEach { result.append("- ${relativizer(it)}\n") }

    result.append("Content modules:\n")
    pluginDescriptor.contentModules.forEach { moduleDescriptor ->
      result.append(
        " - ${moduleDescriptor.moduleName} (${moduleDescriptor.moduleLoadingRule}); ${
          moduleDescriptor.jarFiles!!.joinToString { file -> relativizer(file) }
        }\n"
      )
      val dependencies = moduleDescriptor.moduleDependencies.plugins.map { "    - $it\n" } +
        moduleDescriptor.moduleDependencies.modules.map { "    - $it\n" }
      if (dependencies.isNotEmpty()) {
        result.append("  - Dependencies:\n").append(dependencies.joinToString(""))
      }
    }

    assertSameLinesWithFile(testResultsFilePath, result.toString())
  }

}