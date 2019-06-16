/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package resolve;


import base.HTMLMasonLightTestCase;
import org.junit.Test;

/**
 * Proper file structure: https://github.com/hurricup/Perl5-IDEA/issues/905
 */
public abstract class HTMLMasonVariableResolveTestCase extends HTMLMasonLightTestCase {
  @Test
  public void testFromCleanup() {
    doTestResolve();
  }

  @Test
  public void testFromDef() {
    doTestResolve();
  }

  @Test
  public void testFromFilter() {
    doTestResolve();
  }

  @Test
  public void testFromFilteredBlockAhead() {
    doTestResolve();
  }

  @Test
  public void testFromFilteredBlockBehind() {
    doTestResolve();
  }

  @Test
  public void testFromInit() {
    doTestResolve();
  }

  @Test
  public void testFromLineAhead() {
    doTestResolve();
  }

  @Test
  public void testFromLineBehind() {
    doTestResolve();
  }

  @Test
  public void testFromMethod() {
    doTestResolve();
  }

  @Test
  public void testFromOnce() {
    doTestResolve();
  }

  @Test
  public void testFromPerlAhead() {
    doTestResolve();
  }

  @Test
  public void testFromPerlBehind() {
    doTestResolve();
  }

  @Test
  public void testFromShared() {
    doTestResolve();
  }

  @Test
  public void testFromFileArgs() {
    doTestResolve();
  }
}
