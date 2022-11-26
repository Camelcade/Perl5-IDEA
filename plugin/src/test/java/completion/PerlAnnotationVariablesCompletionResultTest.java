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

package completion;

import org.junit.Test;

public class PerlAnnotationVariablesCompletionResultTest extends PerlCompletionResultTestCase {
  @Override
  protected String getBaseDataPath() {
    return "completionResult/perl/annotated_variables";
  }

  @Test
  public void testArrayNoSigil() { doTestInsert(); }

  @Test
  public void testArraySigil() { doTestInsert(); }

  @Test
  public void testScalarNoSigil() { doTestInsert(); }

  @Test
  public void testScalarNoSigilWithType() { doTestInsert(); }

  @Test
  public void testScalarSigil() { doTestInsert(); }

  @Test
  public void testScalarSigilWithType() { doTestInsert(); }

  @Test
  public void testScalarSigilWithTypeReplace() { doTestInsert(); }
}
