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

package smartKeys;

import com.perl5.lang.pod.filetypes.PodFileType;
import editor.PerlSmartKeysTestCase;
import org.jetbrains.annotations.NotNull;

// fixme probably we should run for all modifiers, just in case
public class PodSmartKeysTest extends PerlSmartKeysTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/smartKeys/pod";
  }

  @NotNull
  private String formatter() {return "I";}

  public void testOpenAngle() {
    doTestWithBS(formatter() + "<caret>", "<", formatter() + "<<caret>>");
  }

  public void testOpenAngleInside() {
    doTestWithBS("test" + formatter() + "<caret>" + "text", "<", "test" + formatter() + "<<caret>>text");
  }

  public void testOpenAngleGrowStart() {
    doTest(formatter() + "<caret><>", "<", formatter() + "<<caret><  >>");
  }

  public void testOpenAngleGrowMid() {
    doTest(formatter() + "<<caret><  >>", "<", formatter() + "<<<caret><  >>>");
  }

  public void testOpenAngleGrowEnd() {
    doTest(formatter() + "<<<caret>  >>", "<", formatter() + "<<<<caret>  >>>");
  }

  public void testOpenAngleShrinkStart() {
    doTestBS(formatter() + "<<caret><  >>", formatter() + "<caret><  >");
  }

  public void testOpenAngleShrinkMid() {
    doTestBS(formatter() + "<<<caret><  >>>", formatter() + "<<caret><  >>");
  }

  public void testOpenAngleShrinkEnd() {
    doTestBS(formatter() + "<<<<caret>  >>>", formatter() + "<<<caret>  >>");
  }

  public void testCloseAngleGrowStart() {
    doTest(formatter() + "<<caret>>", ">", formatter() + "<<  ><caret>>");
  }

  public void testCloseAngleGrowMid() {
    doTest(formatter() + "<<  ><caret>>", ">", formatter() + "<<<  >><caret>>");
  }

  public void testCloseAngleGrowEnd() {
    doTest(formatter() + "<<  >><caret>", ">", formatter() + "<<<  >>><caret>");
  }

  public void testCloseAngleShrinkStart() {
    doTestBS(formatter() + "<<  ><caret>>", formatter() + "<  <caret>>");
  }

  public void testCloseAngleShrinkMid() {
    doTestBS(formatter() + "<<<  >><caret>>", formatter() + "<<  ><caret>>");
  }

  public void testCloseAngleShrinkEnd() {
    doTestBS(formatter() + "<<<  >>><caret>", formatter() + "<<  >><caret>");
  }


  @Override
  public String getFileExtension() {
    return PodFileType.EXTENSION;
  }
}
