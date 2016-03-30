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

import com.intellij.core.CoreApplicationEnvironment;
import com.intellij.lang.ParserDefinition;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.testFramework.ParsingTestCase;
import com.intellij.testFramework.TestDataFile;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.idea.application.PerlParserExtensions;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 * Created by hurricup on 28.02.2016.
 */
public abstract class PerlParserTestBase extends ParsingTestCase
{
	protected String myFileName;

	public PerlParserTestBase()
	{
		this("", "pl", new PerlParserDefinition());
	}

	public PerlParserTestBase(@NonNls @NotNull String dataPath, @NotNull String fileExt, @NotNull ParserDefinition... definitions)
	{
		super(dataPath, fileExt, definitions);
	}

	public void doTest(String filename)
	{
		doTest(filename, true);
	}

	public void doTest(String filename, boolean checkErrors)
	{
		myFileName = filename;
		doTest(true);

		if (checkErrors)
			assertFalse(
					"PsiFile contains error elements",
					toParseTreeText(myFile, skipSpaces(), includeRanges()).contains("PsiErrorElement")
			);
	}


	@Override
	protected String getTestName(boolean lowercaseFirstLetter)
	{
		return myFileName;
	}


	@Override
	protected boolean skipSpaces()
	{
		return true;
	}

	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		CoreApplicationEnvironment.registerExtensionPointAndExtensions(new File("resources"), "plugin.xml", Extensions.getRootArea());
		new PerlParserExtensions().initComponent();
	}

	protected String loadFile(@NonNls @TestDataFile String name) throws IOException
	{
		return FileUtil.loadFile(new File(myFullDataPath, name.replace("." + myFileExt, ".code")), CharsetToolkit.UTF8, true).trim();
	}

	@Override
	protected boolean checkAllPsiRoots()
	{
		return false;
	}

}
