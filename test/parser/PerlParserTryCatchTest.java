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

import com.intellij.psi.LanguageFileViewProviders;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.idea.configuration.settings.Perl5Settings;
import com.perl5.lang.perl.parser.PerlFileViewProviderFactory;

/**
 * Created by hurricup on 17.04.2016.
 */
public class PerlParserTryCatchTest extends PerlParserTestBase
{
	@Override
	protected String getTestDataPath()
	{
		return "testData/parser/perl/TryCatch";
	}

	public void testSyntax()
	{
		doTest("try_catch");
	}

	public void testErrorWrongParams()
	{
		doTest("catch_with_wrong_params", false);
	}

	public void testErrorWithoutTry()
	{
		doTest("catch_without_try", false);
	}

	public void testErrorWithoutVar()
	{
		doTest("catch_without_var", false);
	}

	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		LanguageFileViewProviders.INSTANCE.addExplicitExtension(PerlLanguage.INSTANCE, new PerlFileViewProviderFactory());
		Perl5Settings.getInstance(getProject()).PERL_TRY_CATCH_ENABLED = true;
	}
}
