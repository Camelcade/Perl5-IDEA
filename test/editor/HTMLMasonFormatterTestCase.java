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

package editor;

import base.PerlLightCodeInsightFixtureTestCase;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.codeStyle.CodeStyleManager;

/**
 * Created by hurricup on 15.03.2016.
 */
public class HTMLMasonFormatterTestCase extends PerlLightCodeInsightFixtureTestCase
{
	@Override
	protected String getTestDataPath()
	{
		return "testData/formatting/htmlmason";
	}

	public void testStatementModifiersSpacing() throws Exception
	{
		doFormatTest("spaceless", "");
	}


	protected void doFormatTest(String filename, String resultSuffix)
	{
		initWithFileAsHTMLMason(filename);
		new WriteCommandAction.Simple(getProject())
		{
			@Override
			protected void run() throws Throwable
			{
				CodeStyleManager.getInstance(getProject()).reformat(myFixture.getFile());
			}
		}.execute();
		myFixture.checkResultByFile(filename + resultSuffix + ".txt");
	}

}
