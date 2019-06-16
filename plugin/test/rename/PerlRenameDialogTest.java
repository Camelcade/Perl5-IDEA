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

package rename;


import base.PerlLightTestCase;
import org.junit.Test;

// following tests are not working in inline test
public class PerlRenameDialogTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/rename/perl";
  }

  @Test
  public void testClassAccessorFbp() {doTestRename();}

  @Test
  public void testExceptionClassFieldField() {doTestRename();}

  @Test
  public void testClassAccessorFbpRo() {doTestRename();}

  @Test
  public void testClassAccessorFbpWo() {doTestRename();}
}
