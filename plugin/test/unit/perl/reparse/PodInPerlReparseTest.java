/*
 * Copyright 2015-2021 Alexandr Evstigneev
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

package unit.perl.reparse;

import base.PerlLightTestCase;
import com.perl5.lang.pod.PodLanguage;
import org.junit.Test;

public class PodInPerlReparseTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/unit/perl/reparse/pod_in_perl";
  }

  @Test
  public void testAfterPod() {doTest();}

  @Test
  public void testBeforePod() {doTest();}

  @Test
  public void testBetweenPod() {doTest();}

  @Test
  public void testEmptyPod() {doTest();}

  @Test
  public void testOnlyPod() {doTest();}

  @Test
  public void testBreakByRemoval() {doTestBs();}

  @Test
  public void testBreakByTyping() {doTestBreak();}

  @Test
  public void testBreakByTypingAfter() {doTestBreak();}

  @Test
  public void testBreakByTypingBefore() {doTestBreak();}

  @Test
  public void testBreakByTypingBetween() {doTestBreak();}

  private void doTest() {
    doTestReparse("1", PodLanguage.INSTANCE);
  }

  private void doTestBreak() {
    doTestReparse("=h", PodLanguage.INSTANCE);
  }

  private void doTestBs() {
    doTestReparseBs(PodLanguage.INSTANCE);
  }
}
