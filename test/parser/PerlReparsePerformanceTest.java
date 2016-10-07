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

import base.PerlLightCodeInsightFixtureTestCase;
import com.intellij.openapi.editor.LogicalPosition;

/**
 * Created by hurricup on 07.10.2016.
 */
public class PerlReparsePerformanceTest extends PerlLightCodeInsightFixtureTestCase
{
	public void testPerlTidy()
	{
/*
		initWithPerlTidy();
		myFixture.getEditor().getCaretModel().moveToLogicalPosition(new LogicalPosition(65,0));
		System.err.println("Warming up...");
		for (int i = 0; i < 5; i++)
		{
			typeEnter();
		}

		int iterations = 50;
		long totalTime = 0;
		for (int i = 0; i < iterations; i++)
		{
			totalTime += typeEnter();
		}

		System.err.println(iterations + " iterations; " + totalTime / iterations + " per one");
*/
	}

	private long typeEnter()
	{
		long startTime = System.currentTimeMillis();
		myFixture.type("\n");
		long endTime = System.currentTimeMillis();
		long length = endTime - startTime;
		return length;
	}
}
