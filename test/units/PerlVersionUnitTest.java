/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

/**
 * Created by hurricup on 07.09.2015.
 */
public class PerlVersionUnitTest extends TestCase {
  public void testComparable() {
    PerlVersion version510 = new PerlVersion("v5.10");
    PerlVersion version510x = new PerlVersion("v5.10");
    PerlVersion version514 = new PerlVersion("v5.14");

    assertEquals(version514.compareTo(version510), 1);
    assertEquals(version510.compareTo(version510), 0);
    assertEquals(version510.compareTo(version514), -1);

    assertTrue(version514.greaterThan(version510));
    assertTrue(version510.equals(version510));
    assertTrue(version510.equals(version510x));
    assertTrue(version510.lesserThan(version514));

    assertFalse(version514.lesserThan(version510));
    assertFalse(version510.equals(version514));
    assertFalse(version510.greaterThan(version514));
  }


  public void testParsing() {
    // double constructor
    double doubleVersion = 5.008006;
    processTest(new PerlVersion(doubleVersion), true, true, false, 5, 8, 6, doubleVersion, "v5.8.6");
    doubleVersion = 5;
    processTest(new PerlVersion(doubleVersion), true, true, false, 5, 0, 0, doubleVersion, "v5");
    doubleVersion = 5.1;
    processTest(new PerlVersion(doubleVersion), true, true, false, 5, 100, 0, doubleVersion, "v5.100");
    doubleVersion = 5.0001;
    processTest(new PerlVersion(doubleVersion), true, true, false, 5, 0, 100, doubleVersion, "v5.0.100");
    doubleVersion = -1;
    processTest(new PerlVersion(doubleVersion), false, true, false, 0, 0, 0, 0, "v0");

    processTest(new PerlVersion("1.00"), true, true, false, 1, 0, 0, 1, "v1");
    processTest(new PerlVersion("1000000000000000000000000000000000000000000000000000000000.00"), false, false, false, 0, 0, 0, 0, "v0");
    processTest(new PerlVersion("1.00001"), true, true, false, 1, 0, 10, 1.00001, "v1.0.10");
    processTest(new PerlVersion("0.123"), true, true, false, 0, 123, 0, 0.123, "v0.123");

    processTest(new PerlVersion("12.345"), true, true, false, 12, 345, 0, 12.345, "v12.345");
    processTest(new PerlVersion("12.34_5"), true, false, true, 12, 345, 0, 12.345, "v12.345");
    processTest(new PerlVersion("42"), true, true, false, 42, 0, 0, 42, "v42");
    processTest(new PerlVersion("0"), true, true, false, 0, 0, 0, 0, "v0");
    processTest(new PerlVersion("0.0"), true, true, false, 0, 0, 0, 0, "v0");
    processTest(new PerlVersion("v1.2.3"), true, true, false, 1, 2, 3, 1.002003, "v1.2.3");
    processTest(new PerlVersion("v1.2.3.4"), true, true, false, 1, 2, 3, 1.002003004, "v1.2.3.4");
    processTest(new PerlVersion("v1.2.3.4.5"), true, true, false, 1, 2, 3, 1.002003004005, "v1.2.3.4.5");
    processTest(new PerlVersion("v0.1.2"), true, true, false, 0, 1, 2, 0.001002, "v0.1.2");
    processTest(new PerlVersion("v0.0.0"), true, true, false, 0, 0, 0, 0, "v0");

    processTest(new PerlVersion("01"), false, false, false, 0, 0, 0, 0, "v0");
    processTest(new PerlVersion("01.0203"), false, false, false, 0, 0, 0, 0, "v0");
    processTest(new PerlVersion("v01"), false, false, false, 0, 0, 0, 0, "v0");
    processTest(new PerlVersion("v01.02.03"), false, false, false, 0, 0, 0, 0, "v0");
    processTest(new PerlVersion(".1"), false, false, false, 0, 0, 0, 0, "v0");
    processTest(new PerlVersion(".1.2"), false, false, false, 0, 0, 0, 0, "v0");
    processTest(new PerlVersion("1."), false, false, false, 0, 0, 0, 0, "v0");
    processTest(new PerlVersion("1.a"), false, false, false, 0, 0, 0, 0, "v0");
    processTest(new PerlVersion("1._"), true, false, true, 1, 0, 0, 1, "v1");
    processTest(new PerlVersion("1.02_03"), true, false, true, 1, 20, 300, 1.020300, "v1.20.300");
    processTest(new PerlVersion("v1.2_3"), true, false, true, 1, 2, 3, 1.002003, "v1.2.3");
    processTest(new PerlVersion("v1.02_03"), true, false, true, 1, 2, 3, 1.002003, "v1.2.3");
    processTest(new PerlVersion("v1.2_3_4"), false, false, false, 0, 0, 0, 0, "v0");
    processTest(new PerlVersion("v1.2_3.4"), false, false, false, 0, 0, 0, 0, "v0");
    processTest(new PerlVersion("1.2_3.4"), false, false, false, 0, 0, 0, 0, "v0");
    processTest(new PerlVersion("0_"), false, false, false, 0, 0, 0, 0, "v0");
    processTest(new PerlVersion("1_"), false, false, false, 0, 0, 0, 0, "v0");
    processTest(new PerlVersion("1_."), false, false, false, 0, 0, 0, 0, "v0");
    processTest(new PerlVersion("1.1_"), true, false, true, 1, 100, 0, 1.1, "v1.100");
    processTest(new PerlVersion("1.02_03_04"), true, false, true, 1, 20, 304, 1.020304, "v1.20.304");
    processTest(new PerlVersion("1.2.3"), false, false, false, 0, 0, 0, 0, "v0");
    processTest(new PerlVersion("v1.2"), true, false, false, 1, 2, 0, 1.002, "v1.2");
    processTest(new PerlVersion("v0"), true, false, false, 0, 0, 0, 0, "v0");
    processTest(new PerlVersion("v1"), true, false, false, 1, 0, 0, 1, "v1");
    processTest(new PerlVersion("v.1.2.3"), false, false, false, 0, 0, 0, 0, "v0");
    processTest(new PerlVersion("v"), false, false, false, 0, 0, 0, 0, "v0");
    processTest(new PerlVersion("v1.2345.6"), false, false, false, 0, 0, 0, 0, "v0");
    processTest(new PerlVersion("undef"), false, false, false, 0, 0, 0, 0, "v0");
    processTest(new PerlVersion("1a"), false, false, false, 0, 0, 0, 0, "v0");
    processTest(new PerlVersion("1.2a3"), false, false, false, 0, 0, 0, 0, "v0");
    processTest(new PerlVersion("bar"), false, false, false, 0, 0, 0, 0, "v0");
    processTest(new PerlVersion("_"), false, false, false, 0, 0, 0, 0, "v0");
  }


  public void processTest(
    PerlVersion version,
    boolean isValid,
    boolean isStrict,
    boolean isAlpha,
    int revision,
    int major,
    int minor,
    double value,
    String dottedValue
  ) {
    assertEquals("Valid", isValid, version.isValid());
    assertEquals("Strict", isStrict, version.isStrict());
    assertEquals("Alpha", isAlpha, version.isAlpha());
    assertEquals("Revision number", revision, version.getRevision());
    assertEquals("Major part", major, version.getMajor());
    assertEquals("Minor part", minor, version.getMinor());
    assertEquals("Strict double version", value, version.getDoubleVersion());
    assertEquals("Strict dotted version", dottedValue, version.getStrictDottedVersion());
  }
}
