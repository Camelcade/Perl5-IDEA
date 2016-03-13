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
public abstract class HTMLMasonBlockVariablesResolveTestCase extends HTMLMasonVariableResolveTestCase
{

	protected abstract boolean resolveSecondEntry();

	protected abstract boolean resolveFromSecondEntry();

	public void testSecondEntry() throws Exception
	{
		doTest("second_entry", resolveSecondEntry());
	}

	public void testFromSecondEntry() throws Exception
	{
		doTest("from_second_entry", resolveFromSecondEntry());
	}

}
