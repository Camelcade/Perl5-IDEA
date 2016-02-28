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

import com.intellij.openapi.util.text.StringUtil;
import parser.PerlParserSubtestBase;

/**
 * Created by hurricup on 28.02.2016.
 */
public abstract class PerlParserSubtestHeavyBase extends PerlParserSubtestBase
{
	@Override
	public void doTest(String filename, boolean checkErrors)
	{
		if (StringUtil.equals(System.getenv("CAMELCADE_HEAVY_TEST"), "1"))
		{
			super.doTest(filename, checkErrors);
		}
	}
}
