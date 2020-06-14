/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package smartKeys;

import editor.PerlSmartKeysTestCase;
import org.junit.Test;

public class PerlAutoColonTest extends PerlSmartKeysTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/smartKeys/perl/autoColon";
  }

  @Test
  public void testDoubleColonInUseEnabledAtEnd() {
    enableAutoColon();
    doTest("use Mojolicious<caret>", ":", "use Mojolicious::<caret>");
  }

  @Test
  public void testDoubleColonInUseEnabledAtMid() {
    enableAutoColon();
    doTest("use Mojo<caret>licious", ":", "use Mojo::<caret>licious");
  }

  @Test
  public void testExtraColonInUseEnabledAtMid() {
    enableAutoColon();
    doTest("use Mojo::<caret>licious", ":", "use Mojo::<caret>licious");
  }

  @Test
  public void testDoubleColonInUseDisabledAtMid() {
    disableAutoColon();
    doTest("use Mojo<caret>licious", ":", "use Mojo:<caret>licious");
  }

  @Test
  public void testExtraColonInUseDisabledAtMid() {
    disableAutoColon();
    doTest("use Mojo::<caret>licious", ":", "use Mojo:::<caret>licious");
  }

  @Test
  public void testDoubleColonInUseDisabledAtEnd() {
    disableAutoColon();
    doTest("use Mojolicious<caret>", ":", "use Mojolicious:<caret>");
  }

  @Test
  public void testExtraDoubleColonInUseEnabledAtEnd() {
    enableAutoColon();
    doTest("use Mojolicious::<caret>", ":", "use Mojolicious::<caret>");
  }

  @Test
  public void testExtraDoubleColonInUseDisabledAtEnd() {
    disableAutoColon();
    doTest("use Mojolicious::<caret>", ":", "use Mojolicious:::<caret>");
  }

  @Test
  public void testDoubleColonInUseEnabled() {
    enableAutoColon();
    doTest("use Mojolicious<caret>;", ":", "use Mojolicious::<caret>;");
  }

  @Test
  public void testDoubleColonInUseDisabled() {
    disableAutoColon();
    doTest("use Mojolicious<caret>;", ":", "use Mojolicious:<caret>;");
  }

  @Test
  public void testExtraDoubleColonInUseEnabled() {
    enableAutoColon();
    doTest("use Mojolicious::<caret>;", ":", "use Mojolicious::<caret>;");
  }

  @Test
  public void testExtraDoubleColonInUseDisabled() {
    disableAutoColon();
    doTest("use Mojolicious::<caret>;", ":", "use Mojolicious:::<caret>;");
  }

  @Test
  public void testRemoveLastColonOptionEnabled() {
    enableAutoColon();
    doTestBS("use Foo::<caret>Bar;", "use Foo<caret>Bar;");
  }

  @Test
  public void testRemoveFirstColonOptionEnabled() {
    enableAutoColon();
    doTestBS("use Foo:<caret>:Bar;", "use Foo<caret>Bar;");
  }

  @Test
  public void testRemoveLastColonOptionDisabled() {
    disableAutoColon();
    doTestBS("use Foo::<caret>Bar;", "use Foo:<caret>Bar;");
  }

  @Test
  public void testRemoveFirstColonOptionDisabled() {
    disableAutoColon();
    doTestBS("use Foo:<caret>:Bar;", "use Foo<caret>:Bar;");
  }

  @Test
  public void testAddColonAfterSubOptionEnabled() {
    enableAutoColon();
    doTest("Foo::Bar::nonexistent<caret>;", ":", "Foo::Bar::nonexistent::<caret>;");
  }

  @Test
  public void testAddExtraColonAfterSubOptionEnabled() {
    enableAutoColon();
    doTest("Foo::Bar::nonexistent::<caret>;", ":", "Foo::Bar::nonexistent::<caret>;");
  }

  @Test
  public void testAddColonAfterSubOptionDisabled() {
    disableAutoColon();
    doTest("Foo::Bar::nonexistent<caret>;", ":", "Foo::Bar::nonexistent:<caret>;");
  }

  @Test
  public void testRemovalLastInQualifierEnabled() {
    enableAutoColon();
    doTestBS("Foo::<caret>Bar::nonexistent;", "Foo<caret>Bar::nonexistent;");
  }

  @Test
  public void testRemovalFirstInQualifierEnabled() {
    enableAutoColon();
    doTestBS("Foo:<caret>:Bar::nonexistent;", "Foo<caret>Bar::nonexistent;");
  }

  @Test
  public void testRemovalLastInQualifierDisabled() {
    disableAutoColon();
    doTestBS("Foo::<caret>Bar::nonexistent;", "Foo:<caret>Bar::nonexistent;");
  }

  @Test
  public void testRemovalFirstInQualifierDisabled() {
    disableAutoColon();
    doTestBS("Foo:<caret>:Bar::nonexistent;", "Foo<caret>:Bar::nonexistent;");
  }

  @Test
  public void testAddExtraColonAfterSubOptionDisabled() {
    disableAutoColon();
    doTest("Foo::Bar::nonexistent::<caret>;", ":", "Foo::Bar::nonexistent:::<caret>;");
  }

  @Test
  public void testAddColonInSubOptionEnabled() {
    enableAutoColon();
    doTest("Foo::Bar::nonex<caret>istent;", ":", "Foo::Bar::nonex::<caret>istent;");
  }

  @Test
  public void testAddColonInQualifyingPackageOptionEnabled() {
    enableAutoColon();
    doTest("Foo::B<caret>ar::nonexistent;", ":", "Foo::B::<caret>ar::nonexistent;");
  }

  @Test
  public void testAddColonInSubOptionDisabled() {
    disableAutoColon();
    doTest("Foo::Bar::nonex<caret>istent;", ":", "Foo::Bar::nonex:<caret>istent;");
  }

  @Test
  public void testAddColonInQualifyingPackageOptionDisabled() {
    disableAutoColon();
    doTest("Foo::B<caret>ar::nonexistent;", ":", "Foo::B:<caret>ar::nonexistent;");
  }
}
