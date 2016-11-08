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

import com.intellij.testFramework.UsefulTestCase;
import com.perl5.lang.perl.fileTypes.PerlFileTypePackage;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hurricup on 04.03.2016.
 */
public class PerlCompletionTest extends PerlCompletionCodeInsightFixtureTestCase
{

	private List<String> SUPER_HERE_DOC_MARKER = Arrays.asList("MYSUPERMARKER");

	@Override
	protected String getTestDataPath()
	{
		return "testData/completion/perl";
	}

	public void testLexicalMy()
	{
		doTestLexicalVars("scalarname", "arrayname", "hashname");
	}

	public void testLexicalState()
	{
		doTestLexicalVars("scalarname", "arrayname", "hashname");
	}

	public void testLexicalOur()
	{
		doTestLexicalVars("scalarname", "arrayname", "hashname");
	}

	private void doTestLexicalVars(String... additionalVars)
	{
		doTest(
				Arrays.asList(additionalVars),
				SCALAR_LOOKUPS
		);
	}

	public void testSameStatementSimple()
	{
		doTestLexicalVars("normvar");
	}

	public void testSameStatementMap()
	{
		doTestLexicalVars("normvar");
	}

	public void testHeredocOpenerBare()
	{
		doTestHeredoc();
	}

	public void testHeredocOpenerBackref()
	{
		doTestHeredoc();
	}

	public void testHeredocOpenerQQ()
	{
		doTestHeredoc();
	}

	public void testHeredocOpenerSQ()
	{
		doTestHeredoc();
	}

	public void testHeredocOpenerXQ()
	{
		doTestHeredoc();
	}

	private void doTestHeredoc()
	{
		doTest(SUPER_HERE_DOC_MARKER, getLanguageMarkers());
	}

	public void testRefTypes()
	{
		initWithTextSmart("my $var = '<caret>'");
		List<String> strings = myFixture.getLookupElementStrings();
		assertNotNull(strings);

		UsefulTestCase.assertSameElements(strings, mergeLists(REF_TYPES, LIBRARY_PACKAGES));
	}

	public void testHashIndexBare()
	{
		initWithTextSmart("$$a{testindex}; $b->{<caret>}");
		List<String> strings = myFixture.getLookupElementStrings();
		assertNotNull(strings);
		UsefulTestCase.assertSameElements(strings, Arrays.asList("testindex"));
	}


	public void testAnnotation()
	{
		doTest("returns", "inject", "method", "override", "abstract", "deprecated", "noinspection");
	}

	public void testInjectMarkers()
	{
		doTest(getLanguageMarkers());
	}


	public void testNextLabels()
	{
		doTest("LABEL1", "LABEL2", "LABEL3");
	}

	public void testGotoLabels()
	{
		doTest("LABEL1", "LABEL2", "LABEL3", "LABEL4", "LABEL5", "LABEL6", "LABEL8");
	}

	@Override
	public String getFileExtension()
	{
		return PerlFileTypePackage.EXTENSION;
	}

	public void testPackageDefinition()
	{
		doTest("packageDefinition");
	}

	public void testPackageUse()
	{
		doTestPackageAndVersions();
	}

	public void testPackageNo()
	{
		doTestPackageAndVersions();
	}

	public void testPackageRequire()
	{
		doTestPackageAndVersions();
	}

	public void testPackageMy()
	{
		doTestAllPackages();
	}

	public void testPackageOur()
	{
		doTestAllPackages();
	}

	public void testPackageState()
	{
		doTestAllPackages();
	}

	public void testTryCatch()
	{
		doTestAllPackages();
	}

	@SafeVarargs
	private final void doTest(List<String>... result)
	{
		assertCompletionIs(result);
	}

	private void doTest(String... result)
	{
		doTest(Arrays.asList(result));
	}

	private void doTestPackageAndVersions()
	{
		doTest(BUILT_IN_PACKAGES, BUILT_IN_VERSIONS, LIBRARY_PACKAGES);
	}

	private void doTestAllPackages()
	{
		doTest(BUILT_IN_PACKAGES, LIBRARY_PACKAGES);
	}

}
