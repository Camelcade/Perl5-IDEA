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

package editor.intentions;


import com.perl5.PerlBundle;
import org.junit.Test;
public class PerlIntentionsTest extends PerlIntentionsTestCase {
  @Test
  public void testForeachToFor() {doTestForeachToForIntention();}

  @Test
  public void testForeachToForFor() {doTestForeachToForIntention();}

  private void doTestForeachToForIntention() {doTestIntention(PerlBundle.message("perl.intention.foreach.to.for"));}

  @Test
  public void testStringToHeredocQQ() {doTestConvertToHeredoc();}

  @Test
  public void testStringToHeredocQQMnemonic() {doTestConvertToHeredoc();}

  @Test
  public void testStringToHeredocSQ() {doTestConvertToHeredoc();}

  @Test
  public void testStringToHeredocSQMnemonic() {doTestConvertToHeredoc();}

  @Test
  public void testStringToHeredocXQ() {doTestConvertToHeredoc();}

  @Test
  public void testStringToHeredocXQMnemonic() {doTestConvertToHeredoc();}

  private void doTestConvertToHeredoc() {
    doTestIntention(PerlBundle.message("perl.intention.heredoc.last.prefix"));
  }
}
