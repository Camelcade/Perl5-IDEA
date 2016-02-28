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

package parser.heavy;

/**
 * Created by hurricup on 28.02.2016.
 */
public class PerlSourcesParserTestBenchmark extends PerlSourcesParserTestAbstract
{
	private static final String GROUP = "benchmark";

	@Override
	protected String getTestsGroup()
	{
		return GROUP;
	}

	public void testrt26188_speed_up_keys_on_empty_hash()
	{
		doTest("rt26188-speed-up-keys-on-empty-hash");
	}
}
