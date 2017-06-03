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

package resolve.perl;

import com.perl5.lang.perl.psi.PsiPerlLabelExpr;

/**
 * Created by hurricup on 04.03.2016.
 */
public class PerlLabelResolveTest extends PerlResolveTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/resolve/perl/labels";
  }

  public void testNextOutAnonSub() {
    doTest("next_label_out_anon_sub", false);
  }

  public void testNextOutDo() {
    doTest("next_label_out_do", false);
  }

  public void testNextOutEval() {
    doTest("next_label_out_eval", false);
  }

  public void testNextOutGrep() {
    doTest("next_label_out_grep", false);
  }

  public void testNextOutMap() {
    doTest("next_label_out_map", false);
  }

  public void testNextOutSort() {
    doTest("next_label_out_sort", false);
  }

  public void testNextOutSub() {
    doTest("next_label_out_sub", false);
  }

  public void testNextInAnonSub() {
    doTest("next_label_in_anon_sub", true);
  }

  public void testNextInGrep() {
    doTest("next_label_in_grep", true);
  }

  public void testNextInMap() {
    doTest("next_label_in_map", true);
  }

  public void testNextInSort() {
    doTest("next_label_in_sort", true);
  }

  public void testNextInSub() {
    doTest("next_label_in_sub", true);
  }

  public void testNextLabeledBlock() {
    doTest("next_labeled_block", true);
  }

  public void testRedoLabeledBlock() {
    doTest("redo_labeled_block", true);
  }

  public void testLastLabeledBlock() {
    doTest("last_labeled_block", true);
  }

  public void testNextLabelBeforePod() {
    doTest("next_label_before_pod", true);
  }

  public void testNextLabelBeforeComment() {
    doTest("next_label_before_comment", true);
  }

  public void testNextLabelOtherStatement() {
    doTest("next_label_other_statement", false);
  }

  public void testNextToFor() {
    doTest("next_labeled_for", true);
  }

  public void testNextToForeach() {
    doTest("next_labeled_foreach", true);
  }

  public void testNextToWhile() {
    doTest("next_labeled_while", true);
  }

  public void testNextToUntil() {
    doTest("next_labeled_until", true);
  }

  public void testNextToGiven() {
    doTest("next_labeled_given", false);
  }

  public void testNextToIf() {
    doTest("next_labeled_if", false);
  }

  public void testNextToUnless() {
    doTest("next_labeled_unless", false);
  }

  public void testGotoAfter() {
    doTest("goto_after", true);
  }

  public void testGotoBefore() {
    doTest("goto_before", true);
  }

  public void testGotoFromDeclaration() {
    doTest("goto_from_declaration", false);
  }

  public void testGotoInAfter() {
    doTest("goto_in_after", false);
  }

  public void testGotoInBefore() {
    doTest("goto_in_before", false);
  }

  public void testGotoOutAfter() {
    doTest("goto_out_after", true);
  }

  public void testGotoOutBefore() {
    doTest("goto_out_before", true);
  }

  public void doTest(String filename, boolean success) {
    doTest(filename, success, PsiPerlLabelExpr.class);
  }
}
