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

package resolve.htmlmason;

import base.PerlLightCodeInsightFixtureTestCase;

/**
 * Created by hurricup on 13.03.2016.
 * Proper file structure: https://github.com/hurricup/Perl5-IDEA/issues/905
 */
public abstract class HTMLMasonVariableResolveTestCase extends PerlLightCodeInsightFixtureTestCase {
  @Override
  public String getFileExtension() {
    return "mas";
  }

  public void testFromCleanup() throws Exception {
    doTestResolve();
  }

  public void testFromDef() throws Exception {
    doTestResolve();
  }

  public void testFromFilter() throws Exception {
    doTestResolve();
  }

  public void testFromFilteredBlockAhead() throws Exception {
    doTestResolve();
  }

  public void testFromFilteredBlockBehind() throws Exception {
    doTestResolve();
  }

  public void testFromInit() throws Exception {
    doTestResolve();
  }

  public void testFromLineAhead() throws Exception {
    doTestResolve();
  }

  public void testFromLineBehind() throws Exception {
    doTestResolve();
  }

  public void testFromMethod() throws Exception {
    doTestResolve();
  }

  public void testFromOnce() throws Exception {
    doTestResolve();
  }

  public void testFromPerlAhead() throws Exception {
    doTestResolve();
  }

  public void testFromPerlBehind() throws Exception {
    doTestResolve();
  }

  public void testFromShared() throws Exception {
    doTestResolve();
  }

  public void testFromFileArgs() throws Exception {
    doTestResolve();
  }
}
