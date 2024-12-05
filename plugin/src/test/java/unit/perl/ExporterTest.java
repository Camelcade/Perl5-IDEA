/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

package unit.perl;


import com.perl5.lang.perl.psi.mixins.PerlNamespaceDefinitionMixin;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;

public class ExporterTest extends NamespaceTestCase {

  @Override
  protected String getBaseDataPath() {
    return "unit/perl/exporter";
  }

  @Test
  public void testExport() {
    doTest("export.pl", "Foo", new String[]{"this", "is", "the", "end"}, new String[]{});
    doTest("boolean.pl", "boolean", new String[]{"true", "false", "boolean"}, new String[]{"isTrue", "isFalse", "isBoolean"});
    doTest("Opcode.pm", "Opcode", new String[]{}, new String[]{"opset", "opset_to_hex", "opdump"});
  }

  @Test
  public void testExportVariables() {
    doTest("variables.pl", "Foo", new String[]{"$MYVAR", "@MYARR", "%MYHASH", "&MYCODE", "SIMPLECODE"},
           new String[]{"$myvar", "@myarr", "%myhash", "&mycode", "simplecode"});
  }

  @Test
  public void testExportInBegin() {
    doTest("export_in_begin.pl", "Foo", new String[]{"this", "is", "the", "end"}, new String[]{});
  }

  @Test
  public void testExportInDefault() {
    doTest("export_in_default.pl", "Foo", new String[]{"this", "is", "the", "end"}, new String[]{});
  }

  @Test
  public void testExportInFor() {
    doTest("export_in_for.pl", "Foo", new String[]{"this", "is", "the", "end"}, new String[]{});
  }

  @Test
  public void testExportInForeach() {
    doTest("export_in_foreach.pl", "Foo", new String[]{"this", "is", "the", "end"}, new String[]{});
  }

  @Test
  public void testExportInFunc() {
    doTest("export_in_func.pl", "Foo", new String[]{"this", "is", "the", "end"}, new String[]{});
  }

  @Test
  public void testExportInGiven() {
    doTest("export_in_given.pl", "Foo", new String[]{"this", "is", "the", "end"}, new String[]{});
  }

  @Test
  public void testExportInIf() {
    doTest("export_in_if.pl", "Foo", new String[]{"this", "is", "the", "end"}, new String[]{});
  }

  @Test
  public void testExportInMethod() {
    doTest("export_in_method.pl", "Foo", new String[]{"this", "is", "the", "end"}, new String[]{});
  }

  @Test
  public void testExportInSub() {
    doTest("export_in_sub.pl", "Foo", new String[]{"this", "is", "the", "end"}, new String[]{});
  }

  @Test
  public void testExportInUnless() {
    doTest("export_in_unless.pl", "Foo", new String[]{"this", "is", "the", "end"}, new String[]{});
  }

  @Test
  public void testExportInUntil() {
    doTest("export_in_until.pl", "Foo", new String[]{"this", "is", "the", "end"}, new String[]{});
  }

  @Test
  public void testExportInWhen() {
    doTest("export_in_when.pl", "Foo", new String[]{"this", "is", "the", "end"}, new String[]{});
  }

  @Test
  public void testExportInWhile() {
    doTest("export_in_while.pl", "Foo", new String[]{"this", "is", "the", "end"}, new String[]{});
  }

  @Test
  public void testExportOk() {
    doTest("export_ok.pl", "Foo", new String[]{}, new String[]{"this", "is", "the", "end"});
  }

  @Test
  public void testExportExportOk() {
    doTest("export_export_ok.pl", "Foo", new String[]{"this", "is", "the", "export"}, new String[]{"this", "is", "the", "export_ok"});
  }

  @Test
  public void testTypingEXPORT() {
    doTest("typing_export.pl", "Foo", new String[]{}, new String[]{});
  }


  public void doTest(String fileName, @NotNull String namespaceName, String[] exportArray, String[] exportOkArray) {
    PerlNamespaceDefinitionMixin.ExporterInfo exporterInfo = getNamespaceInFile(fileName, namespaceName).getExporterInfo();
    assertEquals(List.of(exportArray), exporterInfo.getEXPORT());
    assertEquals(List.of(exportOkArray), exporterInfo.getEXPORT_OK());
  }
}
