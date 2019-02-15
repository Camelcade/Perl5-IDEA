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

package editor.liveTemplates;

import com.perl5.lang.pod.filetypes.PodFileType;

public class PodLiveTemplatesTest extends PerlLiveTemplatesTestCase {
  @Override
  public String getFileExtension() {
    return PodFileType.EXTENSION;
  }

  @Override
  protected String getTestDataPath() {
    return "testData/liveTemplates/pod";
  }

  public void testH1() {doTest("h1");}

  public void testH2() {doTest("h2");}

  public void testAttr() {doTest("at");}

  public void testMethod() {doTest("me");}

  public void testFunc() {doTest("fu");}

  public void testH3() {doTest("h3");}

  public void testH4() {doTest("h4");}

  public void testOverBack() {doTest("ov");}

  public void testBeginEnd() {doTest("be");}

  public void testFor() {doTest("fo");}

  public void testEncoding() {doTest("en");}

  public void testItem() {doTest("it");}

  public void testItemBulleted() {doTest("itb");}
}
