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
  public void testDoubleColonAfterScalarSigil() {
    enableAutoColon();
    doTest("$<caret>", ":", "$::<caret>");
  }

  @Test
  public void testDoubleColonAfterArraySigil() {
    enableAutoColon();
    doTest("@<caret>", ":", "@::<caret>");
  }

  @Test
  public void testDoubleColonAfterHashSigil() {
    enableAutoColon();
    doTest("%<caret>", ":", "%::<caret>");
  }

  @Test
  public void testDoubleColonAfterCodeSigil() {
    enableAutoColon();
    doTest("&<caret>", ":", "&::<caret>");
  }

  @Test
  public void testDoubleColonAfterGlobSigil() {
    enableAutoColon();
    doTest("*<caret>", ":", "*::<caret>");
  }

  @Test
  public void testDoubleColonAfterArraySizeSigil() {
    enableAutoColon();
    doTest("$#<caret>", ":", "$#::<caret>");
  }

  @Test
  public void testDoubleColonAfterScalarSigilDisabled() {
    disableAutoColon();
    doTest("$<caret>", ":", "$:<caret>");
  }

  @Test
  public void testDoubleColonAfterArraySigilDisabled() {
    disableAutoColon();
    doTest("@<caret>", ":", "@:<caret>");
  }

  @Test
  public void testDoubleColonAfterHashSigilDisabled() {
    disableAutoColon();
    doTest("%<caret>", ":", "%:<caret>");
  }

  @Test
  public void testDoubleColonAfterCodeSigilDisabled() {
    disableAutoColon();
    doTest("&<caret>", ":", "&:<caret>");
  }

  @Test
  public void testDoubleColonAfterGlobSigilDisabled() {
    disableAutoColon();
    doTest("*<caret>", ":", "*:<caret>");
  }

  @Test
  public void testDoubleColonAfterArraySizeSigilDisabled() {
    disableAutoColon();
    doTest("$#<caret>", ":", "$#:<caret>");
  }

  @Test
  public void testRemoveDoubleColonAfterScalarSigil() {
    enableAutoColon();
    doTestBS("$::<caret>", "$<caret>");
  }

  @Test
  public void testRemoveDoubleColonAfterArraySigil() {
    enableAutoColon();
    doTestBS("@::<caret>", "@<caret>");
  }

  @Test
  public void testRemoveDoubleColonAfterHashSigil() {
    enableAutoColon();
    doTestBS("%::<caret>", "%<caret>");
  }

  @Test
  public void testRemoveDoubleColonAfterCodeSigil() {
    enableAutoColon();
    doTestBS("&::<caret>", "&<caret>");
  }

  @Test
  public void testRemoveDoubleColonAfterGlobSigil() {
    enableAutoColon();
    doTestBS("*::<caret>", "*<caret>");
  }

  @Test
  public void testRemoveDoubleColonAfterArraySizeSigil() {
    enableAutoColon();
    doTestBS("$#::<caret>", "$#<caret>");
  }

  @Test
  public void testRemoveDoubleColonAfterScalarSigilDisabled() {
    disableAutoColon();
    doTestBS("$::<caret>", "$:<caret>");
  }

  @Test
  public void testRemoveDoubleColonAfterArraySigilDisabled() {
    disableAutoColon();
    doTestBS("@::<caret>", "@:<caret>");
  }

  @Test
  public void testRemoveDoubleColonAfterHashSigilDisabled() {
    disableAutoColon();
    doTestBS("%::<caret>", "%:<caret>");
  }

  @Test
  public void testRemoveDoubleColonAfterCodeSigilDisabled() {
    disableAutoColon();
    doTestBS("&::<caret>", "&:<caret>");
  }

  @Test
  public void testRemoveDoubleColonAfterGlobSigilDisabled() {
    disableAutoColon();
    doTestBS("*::<caret>", "*:<caret>");
  }

  @Test
  public void testRemoveDoubleColonAfterArraySizeSigilDisabled() {
    disableAutoColon();
    doTestBS("$#::<caret>", "$#:<caret>");
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

  @Test
  public void testAddColonInGlobEndEnabled() {
    enableAutoColon();
    doTestWithBS("*Foo<caret>", ":", "*Foo::<caret>");
  }

  @Test
  public void testAddColonInScalarEndEnabled() {
    enableAutoColon();
    doTestWithBS("$Foo<caret>", ":", "$Foo::<caret>");
  }

  @Test
  public void testAddColonInArrayEndEnabled() {
    enableAutoColon();
    doTestWithBS("@Foo<caret>", ":", "@Foo::<caret>");
  }

  @Test
  public void testAddColonInHashEndEnabled() {
    enableAutoColon();
    doTestWithBS("%Foo<caret>", ":", "%Foo::<caret>");
  }

  @Test
  public void testAddColonInGlobMidEnabled() {
    enableAutoColon();
    doTestWithBS("*Foo<caret>Bar", ":", "*Foo::<caret>Bar");
  }

  @Test
  public void testAddColonInScalarMidEnabled() {
    enableAutoColon();
    doTestWithBS("$Foo<caret>Bar", ":", "$Foo::<caret>Bar");
  }

  @Test
  public void testAddColonInArrayMidEnabled() {
    enableAutoColon();
    doTestWithBS("@Foo<caret>Bar", ":", "@Foo::<caret>Bar");
  }

  @Test
  public void testAddColonInHashMidEnabled() {
    enableAutoColon();
    doTest("%Foo<caret>Bar", ":", "%Foo::<caret>Bar");
  }

  @Test
  public void testRemoveColonInGlobMidEnabled() {
    enableAutoColon();
    doTestBS("*Foo:<caret>:Bar", "*Foo<caret>Bar");
  }

  @Test
  public void testRemoveColonInScalarMidEnabled() {
    enableAutoColon();
    doTestBS("$Foo:<caret>:Bar", "$Foo<caret>Bar");
  }

  @Test
  public void testRemoveColonInArrayMidEnabled() {
    enableAutoColon();
    doTestBS("@Foo:<caret>:Bar", "@Foo<caret>Bar");
  }

  @Test
  public void testRemoveColonInHashMidEnabled() {
    enableAutoColon();
    doTestBS("%Foo:<caret>:Bar", "%Foo<caret>Bar");
  }
}
