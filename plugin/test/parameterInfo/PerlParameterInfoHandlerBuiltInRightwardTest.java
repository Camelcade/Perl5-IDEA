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

package parameterInfo;


import org.jetbrains.annotations.NotNull;
import org.junit.Test;
public class PerlParameterInfoHandlerBuiltInRightwardTest extends PerlParameterInfoHandlerBuiltInTest {
  @Override
  protected String getBaseDataPath() {
    return "testData/parameterInfo/builtInRightward";
  }

  @Test
  public void testFiletestLowerr() {doTest();}

  @Test
  public void testFiletestLowerw() {doTest();}

  @Test
  public void testFiletestLowerx() {doTest();}

  @Test
  public void testFiletestLowero() {doTest();}

  @Test
  public void testFiletestLowere() {doTest();}

  @Test
  public void testFiletestLowerz() {doTest();}

  @Test
  public void testFiletestLowers() {doTest();}

  @Test
  public void testFiletestLowerf() {doTest();}

  @Test
  public void testFiletestLowerd() {doTest();}

  @Test
  public void testFiletestLowerl() {doTest();}

  @Test
  public void testFiletestLowerp() {doTest();}

  @Test
  public void testFiletestLowerb() {doTest();}

  @Test
  public void testFiletestLowerc() {doTest();}

  @Test
  public void testFiletestLowert() {doTest();}

  @Test
  public void testFiletestLoweru() {doTest();}

  @Test
  public void testFiletestLowerg() {doTest();}

  @Test
  public void testFiletestLowerk() {doTest();}

  @Test
  public void testFiletestUpperR() {doTest();}

  @Test
  public void testFiletestUpperW() {doTest();}

  @Test
  public void testFiletestUpperX() {doTest();}

  @Test
  public void testFiletestUpperO() {doTest();}

  @Test
  public void testFiletestUpperS() {doTest();}

  @Test
  public void testFiletestUpperT() {doTest();}

  @Test
  public void testFiletestUpperB() {doTest();}

  @Test
  public void testFiletestUpperM() {doTest();}

  @Test
  public void testFiletestUpperA() {doTest();}

  @Test
  public void testFiletestUpperC() {doTest();}

  @NotNull
  protected String getCodeFromName(@NotNull String name) {
    return name + " <caret> ";
  }
}
