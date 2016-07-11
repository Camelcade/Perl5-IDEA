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

package formatter;

import base.PerlLightCodeInsightFixtureTestCase;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.testFramework.UsefulTestCase;

/**
 * Created by hurricup on 11.07.2016.
 */
public abstract class PerlFormatterTest extends PerlLightCodeInsightFixtureTestCase
{
	protected void doFormatTest(String filename)
	{
		doFormatTest(filename, "");
	}

	protected void doFormatTest(String filename, String resultSuffix)
	{
		initWithFileSmart(filename);
		new WriteCommandAction.Simple(getProject())
		{
			@Override
			protected void run() throws Throwable
			{
				PsiFile file = myFixture.getFile();
				TextRange rangeToUse = file.getTextRange();
				CodeStyleManager.getInstance(getProject()).reformatText(file, rangeToUse.getStartOffset(), rangeToUse.getEndOffset());
				// 	CodeStyleManager.getInstance(getProject()).reformat(myFixture.getFile());
			}
		}.execute();

		String resultFileName = getTestDataPath() + "/" + filename + resultSuffix + ".txt";
		UsefulTestCase.assertSameLinesWithFile(resultFileName, myFixture.getFile().getText());
//		myFixture.checkResultByFile(resultFileName);
	}

}
