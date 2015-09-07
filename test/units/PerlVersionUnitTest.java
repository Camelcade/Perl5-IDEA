/*
 * Copyright 2015 Alexandr Evstigneev
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

package units;

import com.perl5.lang.perl.internals.PerlVersion;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hurricup on 07.09.2015.
 */
public class PerlVersionUnitTest extends TestCase
{
	// fixme implement test for different formats
	public static final List<String> TEST_SET = Arrays.asList(
			"v5.8.8",
			"5.008008",
			"5.008_008"
	);

	public void testDouble()
	{
		double doubleVersion = 5.008006;
		PerlVersion version = new PerlVersion(doubleVersion);
		assertTrue("Strict", version.isStrict());
		assertFalse("Alpha", version.isAlpha());
		assertTrue("Valid", version.isValid());
		assertEquals("Revision number", 5, version.getRevision());
		assertEquals("Major part", 8, version.getMajor());
		assertEquals("Minor part", 6, version.getMinor());
		assertEquals("Re-stored version", doubleVersion, version.getVersion());
	}

}
