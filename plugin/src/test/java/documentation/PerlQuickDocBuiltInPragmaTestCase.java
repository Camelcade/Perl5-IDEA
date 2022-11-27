/*
 * Copyright 2015-2022 Alexandr Evstigneev
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

package documentation;


import base.PerlLightTestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

abstract class PerlQuickDocBuiltInPragmaTestCase extends PerlLightTestCase {
  static final String ANSWERS_PATH = "documentation/perl/quickdoc/builtin_pragma";

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    withPerl536();
  }

  @Override
  protected String getBaseDataPath() {
    return ANSWERS_PATH;
  }

  @Test
  public void testTrue() { doTest(); }

  @Test
  public void testFalse() { doTest(); }

  @Test
  public void testIs_bool() { doTest(); }

  @Test
  public void testWeaken() { doTest(); }

  @Test
  public void testUnweaken() { doTest(); }

  @Test
  public void testIs_weak() { doTest(); }

  @Test
  public void testBlessed() { doTest(); }

  @Test
  public void testRefaddr() { doTest(); }

  @Test
  public void testReftype() { doTest(); }

  @Test
  public void testCreated_as_string() { doTest(); }

  @Test
  public void testCreated_as_number() { doTest(); }

  @Test
  public void testCeil() { doTest(); }

  @Test
  public void testFloor() { doTest(); }

  @Test
  public void testIndexed() { doTest(); }

  @Test
  public void testTrim() { doTest(); }

  @Override
  protected @NotNull String getResultsFileExtension() {
    return "txt";
  }

  protected abstract void doTest();
}
