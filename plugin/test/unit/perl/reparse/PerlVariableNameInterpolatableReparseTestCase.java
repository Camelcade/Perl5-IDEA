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

package unit.perl.reparse;

import org.junit.Test;

public abstract class PerlVariableNameInterpolatableReparseTestCase extends PerlVariableNameReparseTestCase {
  @Test
  public void testNameInString() {doTest("qq/test<sigil>scalar_n<caret>ame something/");}

  @Test
  public void testBracedNameInString() {doTest("qq/test<sigil>{scalar_n<caret>ame}something/");}

  @Test
  public void testFqnNameInString() {doTest("qq/test<sigil>Foo::Bar::scalar_n<caret>ame test/");}

  @Test
  public void testBracedFqnNameInString() {doTest("qq/test<sigil>{Foo::Bar::scalar_n<caret>ame}test/");}

  @Test
  public void testNameInStringEnd() {
    doTest("qq/test<sigil>scalar_name<caret> something/");
  }

  @Test
  public void testBracedNameInStringEnd() {
    doTest("qq/test<sigil>{scalar_name<caret>}something/");
  }

  @Test
  public void testFqnNameInStringEnd() {
    doTest("qq/test<sigil>Foo::Bar::scalar_name<caret> test/");
  }

  @Test
  public void testBracedFqnNameInStringEnd() {
    doTest("qq/test<sigil>{Foo::Bar::scalar_name<caret>}test/");
  }

  public static class Scalar extends PerlVariableNameInterpolatableReparseTestCase {
    @Override
    protected String getSigil() {
      return "$";
    }
  }

  public static class Array extends PerlVariableNameInterpolatableReparseTestCase {
    @Override
    protected String getSigil() {
      return "@";
    }
  }
}
