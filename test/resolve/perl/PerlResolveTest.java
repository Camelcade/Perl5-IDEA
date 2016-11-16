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

package resolve.perl;

/**
 * Created by hurricup on 09.11.2016.
 */
public class PerlResolveTest extends PerlResolveTestCase
{
	@Override
	protected String getTestDataPath()
	{
		return "testData/resolve/perl";
	}

	public void testExportedSubs()
	{
		doTestWithFileCheck();
	}

	public void testDerefWithComments()
	{
		doTestWithFileCheck();
	}

	public void testStringToPackage()
	{
		doTestWithFileCheck();
	}

	public void testConstant()
	{
		doTestWithFileCheck();
	}

	public void testMojoliciousHelper()
	{
		doTestWithFileCheck();
	}

	public void testSequentionalHereDocs()
	{
		doTestWithFileCheck();
	}

	public void testVariablesAndElements()
	{
		doTestWithFileCheck();
	}

	public void testMainSubs()
	{
		doTestWithFileCheck();
	}

	public void testInterpolatedElements()
	{
		doTestWithFileCheck();
	}

	public void testSingleWordPackages()
	{
		doTestWithFileCheck();
	}

	public void testPackageRanges()
	{
		doTestWithFileCheck();
	}

}
