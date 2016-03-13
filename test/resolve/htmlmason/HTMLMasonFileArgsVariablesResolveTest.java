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

package resolve.htmlmason;

/**
 * Created by hurricup on 13.03.2016.
 */
public class HTMLMasonFileArgsVariablesResolveTest extends HTMLMasonBlockVariablesResolveTestCase
{
	protected String getTestDataPath()
	{
		return "testData/resolve/htmlmason/file_args";
	}

	@Override
	protected boolean resolveFromOnce()
	{
		return false;
	}

	@Override
	protected boolean resolveFromShared()
	{
		return false;
	}

	@Override
	protected boolean resolveFromFilter()
	{
		return true;
	}

	@Override
	protected boolean resolveFromInit()
	{
		return true;
	}

	@Override
	protected boolean resolveFromCleanup()
	{
		return true;
	}

	@Override
	protected boolean resolveFromLineAhead()
	{
		return true;
	}

	@Override
	protected boolean resolveFromLineBehind()
	{
		return true;
	}

	@Override
	protected boolean resolveFromPerlBehind()
	{
		return true;
	}

	@Override
	protected boolean resolveFromPerlAhead()
	{
		return true;
	}

	@Override
	protected boolean resolveFromFilteredBlockAhead()
	{
		return true;
	}

	@Override
	protected boolean resolveFromFilteredBlockBehind()
	{
		return true;
	}

	@Override
	protected boolean resolveFromDef()
	{
		return false;
	}

	@Override
	protected boolean resolveFromMethod()
	{
		return false;
	}

	@Override
	protected boolean resolveSecondEntry()
	{
		return true;
	}

	@Override
	protected boolean resolveFromFileArgs()
	{
		return true;
	}

	@Override
	protected boolean resolveFromSecondEntryBackwards()
	{
		return false;
	}

}
