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

package formatter;

/**
 * Created by hurricup on 13.03.2016.
 */
public class PerlFormatterTest extends PerlFormatterTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/formatter/perl";
  }


  public void testStatementModifierSpacing() throws Exception {
    myPerlSettings.SPACE_BEFORE_IF_PARENTHESES = false;
    doFormatTest();
  }

  public void testStatementModifierSpacingWithSpace() throws Exception {
    myPerlSettings.SPACE_BEFORE_IF_PARENTHESES = true;
    doFormatTest();
  }

  public void testExceptionClass() {doFormatTest();}

  public void testIndentedHeredoc() {
    doFormatTest();
  }

  public void testIndentedHeredocShiftLeft() {doFormatTest();}

  public void testIndentedHeredocShiftLeftEmpty() {doFormatTest();}

  public void testIndentedHeredocShiftLeftWithBadString() {doFormatTest();}

  public void testIndentedHeredocShiftLeftWithLeadingNewLine() {doFormatTest();}

  public void testIndentedHeredocShiftRight() {doFormatTest();}

  public void testIndentedHeredocShiftRightEmpty() {doFormatTest();}

  public void testIndentedHeredocShiftRightWithBadString() {doFormatTest();}

  public void testIndentedHeredocShiftRightWithLeadingNewLine() {doFormatTest();}
}
