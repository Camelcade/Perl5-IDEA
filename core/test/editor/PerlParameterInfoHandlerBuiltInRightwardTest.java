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

package editor;

import org.jetbrains.annotations.NotNull;

public class PerlParameterInfoHandlerBuiltInRightwardTest extends PerlParameterInfoHandlerBuiltInTest {
  @Override
  protected String getTestDataPath() {
    return "testData/parameterInfo/builtInRightward";
  }

  public void testFiletestLowerr() {doTest();}

  public void testFiletestLowerw() {doTest();}

  public void testFiletestLowerx() {doTest();}

  public void testFiletestLowero() {doTest();}

  public void testFiletestLowere() {doTest();}

  public void testFiletestLowerz() {doTest();}

  public void testFiletestLowers() {doTest();}

  public void testFiletestLowerf() {doTest();}

  public void testFiletestLowerd() {doTest();}

  public void testFiletestLowerl() {doTest();}

  public void testFiletestLowerp() {doTest();}

  public void testFiletestLowerb() {doTest();}

  public void testFiletestLowerc() {doTest();}

  public void testFiletestLowert() {doTest();}

  public void testFiletestLoweru() {doTest();}

  public void testFiletestLowerg() {doTest();}

  public void testFiletestLowerk() {doTest();}

  public void testFiletestUpperR() {doTest();}

  public void testFiletestUpperW() {doTest();}

  public void testFiletestUpperX() {doTest();}

  public void testFiletestUpperO() {doTest();}

  public void testFiletestUpperS() {doTest();}

  public void testFiletestUpperT() {doTest();}

  public void testFiletestUpperB() {doTest();}

  public void testFiletestUpperM() {doTest();}

  public void testFiletestUpperA() {doTest();}

  public void testFiletestUpperC() {doTest();}

  @NotNull
  protected String getCodeFromName(@NotNull String name) {
    return name + " <caret> ";
  }
}
