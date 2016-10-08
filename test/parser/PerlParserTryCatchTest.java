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
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.psi.PerlFileViewProviderFactory;

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

	public void testtry_catch()
	{
		doTest();
	}

	public void testcatch_with_wrong_params()
	{
		doTest(false);
	}

	public void testcatch_without_try()
	{
		doTest(false);
	}

	public void testcatch_without_var()
	{
		doTest(false);
	}

	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		LanguageFileViewProviders.INSTANCE.addExplicitExtension(PerlLanguage.INSTANCE, new PerlFileViewProviderFactory());
		PerlSharedSettings.getInstance(getProject()).PERL_TRY_CATCH_ENABLED = true;
	}
}
