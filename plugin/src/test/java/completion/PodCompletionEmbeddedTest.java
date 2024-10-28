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

package completion;


import base.PerlLightTestCase;
import com.perl5.lang.perl.idea.project.PerlNamesCache;
import org.junit.Test;
public class PodCompletionEmbeddedTest extends PerlLightTestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    updateNamesCacheSynchronously();
  }

  @Override
  protected String getBaseDataPath() {
    return "completion/pod/embedded";
  }

  @Test
  public void testAttr() {doTest();}

  @Test
  public void testAttrSecond() {doTest();}

  @Test
  public void testFunc() {doTest();}

  @Test
  public void testHeader1() {doTest();}

  @Test
  public void testHeader2() {doTest();}

  @Test
  public void testHeader2ExcludeDocumented() {doTest();}

  @Test
  public void testHeader3() {doTest();}

  @Test
  public void testHeader4() {doTest();}

  @Test
  public void testMethod() {doTest();}

  @Test
  public void testItem() {doTest();}

  private void doTest() {doTestCompletion();}
}
