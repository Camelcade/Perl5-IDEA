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

package completion;

import base.PerlLightCodeInsightFixtureTestCase;
import com.intellij.codeInsight.completion.CompletionType;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by hurricup on 04.03.2016.
 */
public abstract class PerlCompletionCodeInsightFixtureTestCase extends PerlLightCodeInsightFixtureTestCase
{
	@Override
	public void initWithFileContent(String filename, String extension, String content) throws IOException
	{
		super.initWithFileContent(filename, extension, content);
		myFixture.complete(CompletionType.BASIC, 1);
	}

	public void checkPackageFileCompletionWithArray(String fileName, String... result)
	{
		initWithFileAsPackage(fileName);
		assertContainsLookupElements(result);
	}

	public void assertContainsLookupElements(String... pattern)
	{
		List<String> strings = myFixture.getLookupElementStrings();
		assertNotNull(strings);
		assertContainsElements(new HashSet<String>(strings), Arrays.asList(pattern));
	}

	public void assertNotContainsLookupElements(String... pattern)
	{
		List<String> strings = myFixture.getLookupElementStrings();
		assertNotNull(strings);
		assertDoesntContain(new HashSet<Object>(strings), Arrays.asList(pattern));
	}
}
