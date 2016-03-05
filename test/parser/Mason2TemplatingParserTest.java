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

package parser;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.testFramework.TestDataFile;
import com.perl5.lang.mason2.MasonTemplatingParserDefinition;
import org.jetbrains.annotations.NonNls;
import org.junit.Ignore;

import java.io.File;
import java.io.IOException;

/**
 * Created by hurricup on 04.03.2016.
 */
@Ignore
public class Mason2TemplatingParserTest extends PerlParserTestBase
{
	public Mason2TemplatingParserTest()
	{
		super("", "mc", new MasonTemplatingParserDefinition());
	}

	@Override
	protected String getTestDataPath()
	{
		return "testData/parser/mason2/template";
	}

	protected String loadFile(@NonNls @TestDataFile String name) throws IOException
	{
		return FileUtil.loadFile(new File(myFullDataPath, name.replace(".mc", ".code")), CharsetToolkit.UTF8, true).trim();
	}

	public void testComponent()
	{
		doTest("test_component", true);
	}
}
