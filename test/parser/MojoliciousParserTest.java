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
import com.perl5.lang.mojolicious.MojoliciousFileViewProviderFactory;
import com.perl5.lang.mojolicious.MojoliciousLanguage;
import com.perl5.lang.mojolicious.MojoliciousParserDefinition;

/**
 * Created by hurricup on 04.03.2016.
 */
public class MojoliciousParserTest extends PerlParserTestBase
{
	public MojoliciousParserTest()
	{
		super("", "ep", new MojoliciousParserDefinition());
	}

	@Override
	protected String getTestDataPath()
	{
		return "testData/parser/mojolicious";
	}

	public void testSyntax()
	{
		doTest("mojo_parser_test");
	}

	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		LanguageFileViewProviders.INSTANCE.addExplicitExtension(MojoliciousLanguage.INSTANCE, new MojoliciousFileViewProviderFactory());
	}

}
