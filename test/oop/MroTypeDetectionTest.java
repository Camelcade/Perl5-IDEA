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
import com.perl5.lang.perl.psi.mro.PerlMroType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hurricup on 22.02.2016.
 */
public class MroTypeDetectionTest extends NamespaceTestCase
{
	public static final String DATA_PATH = "testData/oop/mroType";

	@Override
	protected String getTestDataPath()
	{
		return DATA_PATH;
	}

	public void testDefault()
	{
		doTest("mro_default.pl", "Foo", PerlMroType.DFS);
	}
	public void testDFS()
	{
		doTest("mro_dfs.pl", "Foo", PerlMroType.DFS);
	}
	public void testC3()
	{
		doTest("mro_c3.pl", "Foo", PerlMroType.C3);
	}



	public void doTest(String fileName, @NotNull String namespaceName, PerlMroType mroType)
	{
		assertEquals(mroType, getNamespaceInFile(fileName, namespaceName).getMroType());
	}

}
