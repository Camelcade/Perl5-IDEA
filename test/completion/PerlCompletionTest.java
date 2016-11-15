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
import com.perl5.lang.perl.extensions.packageprocessor.impl.POSIXExports;
import com.perl5.lang.perl.extensions.packageprocessor.impl.PerlDancer2DSL;
import com.perl5.lang.perl.extensions.packageprocessor.impl.PerlDancerDSL;
import com.perl5.lang.perl.fileTypes.PerlFileTypePackage;
import com.perl5.lang.perl.idea.project.PerlNamesCache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by hurricup on 04.03.2016.
 */
public class PerlCompletionTest extends PerlCompletionCodeInsightFixtureTestCase
{

	private List<String> SUPER_HERE_DOC_MARKER = Arrays.asList("MYSUPERMARKER");

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		getProject().getComponent(PerlNamesCache.class).forceCacheUpdate();
	}

	@Override
	protected String getTestDataPath()
	{
		return "testData/completion/perl";
	}

	public void testPackageToStringQ()
	{
		doTestStringCompletion();
	}

	public void testPackageToStringQPartial()
	{
		doTest(LIBRARY_PACKAGES);
	}

	public void testPackageToStringQQ()
	{
		doTestStringCompletion();
	}

	public void testPackageToStringQWFirst()
	{
		doTestStringCompletion();
	}

	public void testPackageToStringQWNonFirst()
	{
		doTestStringCompletion();
	}

	private void doTestStringCompletion()
	{
		doTest(REF_TYPES, LIBRARY_PACKAGES, Collections.singletonList("Foo::Bar::Bla"));
	}

	public void testMojoliciousHelper()
	{
		doTest("myhelper", "SUPER::");
	}

	public void testUnresolvedSubDeclaration()
	{
		doTest("somesubcustom");
	}

	public void testUnresolvedSubDefinition()
	{
		doTest("somesubcustom");
	}

	public void testConstants()
	{
		doTestSubs("ALONECONST1", "ALONECONST2", "ALONECONST3", "MULTI_CONST1", "MULTI_CONST2", "MULTI_CONST3");
	}

	public void testConstantsWithPackage()
	{
		doTest("ALONECONST1", "ALONECONST2", "ALONECONST3", "MULTI_CONST1", "MULTI_CONST2", "MULTI_CONST3");
	}

	public void testVariableInDeclaration()
	{
		doTest();
	}

	public void testImportSubs()
	{
		doTestSubs("somecode", "someothercode", "Foo::Bar::", "Foo::Bar->", "Foo::Baz::", "Foo::Baz->");
	}

	public void testImportHashes()
	{
		doTestHashVariables("somehash", "Foo::Bar::somehash");
	}

	public void testImportArrays()
	{
		doTestArrayVariables("somearray", "somehash", "Foo::Bar::somearray", "Foo::Bar::somehash", "Foo::Bar::EXPORT");
	}

	public void testImportScalars()
	{
		doTestScalarVariables(
				"somearray",
				"somehash",
				"somescalar",
				"Foo::Bar::EXPORT",
				"Foo::Bar::somearray",
				"Foo::Bar::somehash",
				"Foo::Bar::somescalar");
	}

	public void testImportDancer()
	{
		doTestSubs(PerlDancerDSL.DSL_KEYWORDS);
	}

	public void testImportDancer2()
	{
		ArrayList<String> dancerCopy = new ArrayList<>(PerlDancer2DSL.DSL_KEYWORDS);
		dancerCopy.remove("import");
		dancerCopy.remove("log"); // as far as we have no psi elements inside lookups, they are not duplicating
		doTestSubs(dancerCopy);
	}

	public void testImportPosixOk()
	{
		doTestSubs("isgreaterequal");
	}

	public void testImportPosix()
	{
		ArrayList<String> posixCopy = new ArrayList<>(POSIXExports.EXPORT);
		posixCopy.remove("%SIGRT");  // variable does not appear
		doTestSubs(posixCopy);
	}

	public void testImportPosixVar()
	{
		doTestHashVariables("SIGRT");
	}

	private void doTestSubs(String... additional)
	{
		doTestSubs(Arrays.asList(additional));
	}

	private void doTestSubs(List<String> additional)
	{
		doTest(BUILT_IN_SUBS, PACKAGES_LOOKUPS, additional);
	}

	public void testLexicalMy()
	{
		doTestScalarVariables("scalarname", "arrayname", "hashname");
	}

	public void testLexicalState()
	{
		doTestScalarVariables("scalarname", "arrayname", "hashname");
	}

	public void testLexicalOur()
	{
		doTestScalarVariables("scalarname", "arrayname", "hashname", "main::scalarname", "main::arrayname", "main::hashname");
	}

	private void doTestScalarVariables(String... additionalVars)
	{
		doTest(
				Arrays.asList(additionalVars),
				SCALAR_LOOKUPS
		);
	}

	private void doTestArrayVariables(String... additionalVars)
	{
		doTest(
				Arrays.asList(additionalVars),
				ARRAY_LOOKUPS
		);
	}

	private void doTestHashVariables(String... additionalVars)
	{
		doTest(
				Arrays.asList(additionalVars),
				HASH_LOOKUPS
		);
	}

	public void testSameStatementSimple()
	{
		doTestScalarVariables("normvar");
	}

	public void testSameStatementMap()
	{
		doTestScalarVariables("normvar");
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

	private void doTest()
	{
		doTest(Collections.emptyList());
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
		doTest(BUILT_IN_VERSIONS, LIBRARY_PACKAGES);
	}

	private void doTestAllPackages()
	{
		doTest(LIBRARY_PACKAGES);
	}

}
