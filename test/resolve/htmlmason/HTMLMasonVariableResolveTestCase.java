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

import resolve.perl.PerlVariableResolveTestCase;

/**
 * Created by hurricup on 13.03.2016.
 * Proper file structure: https://github.com/hurricup/Perl5-IDEA/issues/905
 */
public abstract class HTMLMasonVariableResolveTestCase extends PerlVariableResolveTestCase
{
	protected abstract boolean resolveFromOnce();

	protected abstract boolean resolveFromShared();

	protected abstract boolean resolveFromFilter();

	protected abstract boolean resolveFromInit();

	protected abstract boolean resolveFromCleanup();

	protected abstract boolean resolveFromLineAhead();

	protected abstract boolean resolveFromLineBehind();

	protected abstract boolean resolveFromPerlBehind();

	protected abstract boolean resolveFromPerlAhead();

	protected abstract boolean resolveFromFilteredBlockAhead();

	protected abstract boolean resolveFromFilteredBlockBehind();

	protected abstract boolean resolveFromDef();

	protected abstract boolean resolveFromMethod();

	protected abstract boolean resolveFromFileArgs();

	public void testFromCleanup() throws Exception
	{
		doTest("from_cleanup", resolveFromCleanup());
	}

	public void testFromDef() throws Exception
	{
		doTest("from_def", resolveFromDef());
	}

	public void testFromFilter() throws Exception
	{
		doTest("from_filter", resolveFromFilter());
	}

	public void testFromFilteredBlockAhead() throws Exception
	{
		doTest("from_filtered_block_ahead", resolveFromFilteredBlockAhead());
	}

	public void testFromFilteredBlockBehind() throws Exception
	{
		doTest("from_filtered_block_behind", resolveFromFilteredBlockBehind());
	}

	public void testFromInit() throws Exception
	{
		doTest("from_init", resolveFromInit());
	}

	public void testFromLineAhead() throws Exception
	{
		doTest("from_line_ahead", resolveFromLineAhead());
	}

	public void testFromLineBehind() throws Exception
	{
		doTest("from_line_behind", resolveFromLineBehind());
	}

	public void testFromMethod() throws Exception
	{
		doTest("from_method", resolveFromMethod());
	}

	public void testFromOnce() throws Exception
	{
		doTest("from_once", resolveFromOnce());
	}

	public void testFromPerlAhead() throws Exception
	{
		doTest("from_perl_ahead", resolveFromPerlAhead());
	}

	public void testFromPerlBehind() throws Exception
	{
		doTest("from_perl_behind", resolveFromPerlBehind());
	}

	public void testFromShared() throws Exception
	{
		doTest("from_shared", resolveFromShared());
	}

	public void testFromFileArgs() throws Exception
	{
		doTest("from_file_args", resolveFromFileArgs());
	}

	@Override
	public void initWithFile(String filename)
	{
		initWithFileAsHTMLMason(filename);
	}

}
