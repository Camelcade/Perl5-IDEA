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


import com.perl5.lang.pod.filetypes.PodFileType;
import editor.PerlSmartKeysTestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public abstract class PodSmartKeysFormattersTestCase extends PerlSmartKeysTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/smartKeys/pod";
  }

  protected abstract @NotNull String formatter();

  @Test
  public void testOpenAngle() {
    doTestWithBS(formatter() + "<caret>", "<", formatter() + "<<caret>>");
  }

  @Test
  public void testOpenAngleInside() {
    doTestWithBS("test" + formatter() + "<caret>" + "text", "<", "test" + formatter() + "<<caret>>text");
  }

  @Test
  public void testOpenAngleGrowStart() {
    doTest(formatter() + "<caret><>", "<", formatter() + "<<caret><  >>");
  }

  @Test
  public void testOpenAngleGrowMid() {
    doTest(formatter() + "<<caret><  >>", "<", formatter() + "<<<caret><  >>>");
  }

  @Test
  public void testOpenAngleGrowEnd() {
    doTest(formatter() + "<<<caret>  >>", "<", formatter() + "<<<<caret>  >>>");
  }

  @Test
  public void testOpenAngleShrinkStart() {
    doTestBS(formatter() + "<<caret><  >>", formatter() + "<caret><  >");
  }

  @Test
  public void testOpenAngleShrinkMid() {
    doTestBS(formatter() + "<<<caret><  >>>", formatter() + "<<caret><  >>");
  }

  @Test
  public void testOpenAngleShrinkEnd() {
    doTestBS(formatter() + "<<<<caret>  >>>", formatter() + "<<<caret>  >>");
  }

  @Test
  public void testCloseAngleGrowStart() {
    doTest(formatter() + "<<caret>>", ">", formatter() + "<<  ><caret>>");
  }

  @Test
  public void testCloseAngleGrowMid() {
    doTest(formatter() + "<<  ><caret>>", ">", formatter() + "<<<  >><caret>>");
  }

  @Test
  public void testCloseAngleGrowEnd() {
    doTest(formatter() + "<<  >><caret>", ">", formatter() + "<<<  >>><caret>");
  }

  @Test
  public void testCloseAngleShrinkStart() {
    doTestBS(formatter() + "<<  ><caret>>", formatter() + "<  <caret>>");
  }

  @Test
  public void testCloseAngleShrinkMid() {
    doTestBS(formatter() + "<<<  >><caret>>", formatter() + "<<  ><caret>>");
  }

  @Test
  public void testCloseAngleShrinkEnd() {
    doTestBS(formatter() + "<<<  >>><caret>", formatter() + "<<  >><caret>");
  }

  @Override
  public String getFileExtension() {
    return PodFileType.EXTENSION;
  }
}
