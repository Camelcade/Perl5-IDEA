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

package unit.perl

import base.PerlLightTestCase
import com.intellij.openapi.util.JDOMUtil
import com.intellij.openapi.util.io.FileUtil
import com.perl5.lang.perl.idea.run.run.PerlRunConfiguration
import com.perl5.lang.perl.idea.run.run.PerlRunConfigurationType
import org.jdom.Element
import org.junit.Test

class PerlRunConfigurationSerializationTest : PerlLightTestCase() {
  @Test
  fun testPerlRunConfigurationSerialization() {
    val configFile = """<configuration nameIsGenerated="true" show_console_on_std_err="false" show_console_on_std_out="false">
  <option name="allowRunningInParallel" value="false" />
  <option name="alternativeSdkName" />
  <option name="compileTimeBreakpointsEnabled" value="false" />
  <option name="consoleCharset" value="UTF-8" />
  <option name="envs">
    <map>
      <entry key="ENV_PATH" value="${'$'}PROJECT_DIR$/blib/envpath" />
    </map>
  </option>
  <option name="initCode" value="" />
  <option name="nonInteractiveModeEnabled" value="false" />
  <option name="passParentEnvs" value="true" />
  <option name="perlArguments" value="wrong=${'$'}PROJECT_DIR$/wrong" />
  <option name="programParameters" value="mypath=${'$'}PROJECT_DIR$/blib1" />
  <option name="projectPathOnTarget" />
  <option name="requiredModules" value="" />
  <option name="scriptCharset" value="utf8" />
  <option name="scriptPath" value="${'$'}PROJECT_DIR$/test.pl" />
  <option name="selectedOptions">
    <list />
  </option>
  <option name="startMode" value="RUN" />
  <option name="useAlternativeSdk" value="false" />
  <option name="workingDirectory" value="${'$'}PROJECT_DIR$/blib" />
</configuration>""".trimIndent()
    val configuration = PerlRunConfiguration(project, PerlRunConfigurationType.getInstance().scriptConfigurationFactory, "test")
    configuration.readExternal(JDOMUtil.load(configFile))

    assertNoMacroValue(configuration.envs["ENV_PATH"]!!, "/blib/envpath")
    assertNoMacroValue(configuration.perlArguments!!, "/wrong")
    assertNoMacroValue(configuration.programParameters!!, "/blib1")
    assertNoMacroValue(configuration.workingDirectory!!, "/blib")
    assertNoMacroValue(configuration.scriptPath!!, "/test.pl")

    val rootElement = Element("configuration")
    configuration.writeExternal(rootElement)
    assertEquals(configFile, JDOMUtil.write(rootElement))
  }

  private fun assertNoMacroValue(stringValue: String, expectedSuffix: String) = {
    assertFalse("Looks like $stringValue contains the macro", stringValue.contains('$'))
    assertTrue("Value $stringValue should end with $expectedSuffix", stringValue.endsWith(expectedSuffix))
    assertEquals(FileUtil.toSystemIndependentName("${project.basePath!!}expectedSuffix"), FileUtil.toSystemIndependentName(stringValue))
  }
}