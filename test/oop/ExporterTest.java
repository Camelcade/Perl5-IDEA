/*
 * Copyright 2016 Alexandr Evstigneev
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

package oop;

import com.perl5.lang.perl.psi.mixins.PerlNamespaceDefinitionImplMixin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by hurricup on 22.02.2016.
 */
public class ExporterTest extends NamespaceTestCase
{
	public static final String DATA_PATH = "testData/oop/exporter";

	@Override
	protected String getTestDataPath()
	{
		return DATA_PATH;
	}

	public void testExport()
	{
		doTest("export.pl", "Foo", new String[]{"this", "is", "the", "end"}, new String[]{});
	}

	public void testExportOk()
	{
		doTest("export_ok.pl", "Foo", new String[]{}, new String[]{"this", "is", "the", "end"});
	}

	public void testExportExportOk()
	{
		doTest("export_export_ok.pl", "Foo", new String[]{"this", "is", "the", "export"}, new String[]{"this", "is", "the", "export_ok"});
	}


	public void doTest(String fileName, @NotNull String namespaceName, String[] exportArray, String[] exportOkArray)
	{
		PerlNamespaceDefinitionImplMixin.ExporterInfo exporterInfo = getNamespaceInFile(fileName, namespaceName).getExporterInfo();
		assertEquals(new ArrayList<String>(Arrays.asList(exportArray)), exporterInfo.getEXPORT());
		assertEquals(new ArrayList<String>(Arrays.asList(exportOkArray)), exporterInfo.getEXPORT_OK());
	}


}
