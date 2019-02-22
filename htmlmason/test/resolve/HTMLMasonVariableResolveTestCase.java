/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

/**
 * Created by hurricup on 13.03.2016.
 * Proper file structure: https://github.com/hurricup/Perl5-IDEA/issues/905
 */
public abstract class HTMLMasonVariableResolveTestCase extends HTMLMasonLightTestCase {
  public void testFromCleanup() {
    doTestResolve();
  }

  public void testFromDef() {
    doTestResolve();
  }

  public void testFromFilter() {
    doTestResolve();
  }

  public void testFromFilteredBlockAhead() {
    doTestResolve();
  }

  public void testFromFilteredBlockBehind() {
    doTestResolve();
  }

  public void testFromInit() {
    doTestResolve();
  }

  public void testFromLineAhead() {
    doTestResolve();
  }

  public void testFromLineBehind() {
    doTestResolve();
  }

  public void testFromMethod() {
    doTestResolve();
  }

  public void testFromOnce() {
    doTestResolve();
  }

  public void testFromPerlAhead() {
    doTestResolve();
  }

  public void testFromPerlBehind() {
    doTestResolve();
  }

  public void testFromShared() {
    doTestResolve();
  }

  public void testFromFileArgs() {
    doTestResolve();
  }
}
