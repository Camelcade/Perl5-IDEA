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

public class PerlFormatterAlignTest extends PerlFormatterTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/formatter/perl/align";
  }


  public void testAlignListElementsTrue() {
    getCustomSettings().ALIGN_LIST_ELEMENTS = true;
    doTestAlignListElements();
  }

  public void testAlignListElementsFalse() {
    getCustomSettings().ALIGN_LIST_ELEMENTS = false;
    doTestAlignListElements();
  }

  private void doTestAlignListElements() {
    doWrappingTestSingleSource("alignListElements");
  }


  public void testAlignListCommentsTrue() {
    getCustomSettings().ALIGN_COMMENTS_IN_LIST = true;
    doTestAlignListComments();
  }

  public void testAlignListCommentsFalse() {
    getCustomSettings().ALIGN_COMMENTS_IN_LIST = false;
    doTestAlignListComments();
  }

  private void doTestAlignListComments() {
    doTestSingleSource("alignListComments");
  }

  public void testAlignDereferenceFalse() {
    getCustomSettings().ALIGN_DEREFERENCE_IN_CHAIN = false;
    doFormatTest();
  }

  public void testAlignDereferenceTrue() {
    getCustomSettings().ALIGN_DEREFERENCE_IN_CHAIN = true;
    doFormatTest();
  }

  public void testAlignFatCommaTrue() {
    getCustomSettings().ALIGN_FAT_COMMA = true;
    doFormatTest();
  }

  public void testAlignFatCommaFalse() {
    getCustomSettings().ALIGN_FAT_COMMA = false;
    doFormatTest();
  }

  public void testAlignQwTrue() {
    getCustomSettings().ALIGN_QW_ELEMENTS = true;
    doFormatTest();
  }

  public void testAlignQwFalse() {
    getCustomSettings().ALIGN_QW_ELEMENTS = false;
    doFormatTest();
  }

  public void testAlignTernaryTrue() {
    getCustomSettings().ALIGN_TERNARY = true;
    doFormatTest();
  }

  public void testAlignTernaryFalse() {
    getCustomSettings().ALIGN_TERNARY = false;
    doFormatTest();
  }
}
