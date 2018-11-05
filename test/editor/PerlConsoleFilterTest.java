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

package editor;

import base.PerlLightTestCase;
import com.perl5.lang.perl.idea.execution.PerlRunConsole;
import com.perl5.lang.perl.idea.execution.filters.PerlAbsolutePathConsoleFilter;
import com.perl5.lang.perl.idea.execution.filters.PerlConsoleFileLinkFilter;

public class PerlConsoleFilterTest extends PerlLightTestCase {
  @Override
  protected String getTestDataPath() {
    return "testData/consoleFilter/perl";
  }

  public void testConfess() {doTestDie();}

  public void testDie() {doTestDie();}

  public void testPythonBug() {doTestDie();}

  public void testInstallDtlFast() {
    doTestAbsolute();
  }

  public void testInstallDtlFastWin() {
    doTestAbsolute();
  }

  public void testDeparse() {doTestAbsolute();}

  public void testLibraryPath() {
    doTestAbsolute();
  }

  public void testSemicolons() {doTestAbsolute();}

  private void doTestAbsolute() {
    doTestConsoleFilter(new PerlAbsolutePathConsoleFilter(getProject(), new PerlRunConsole(getProject(), null)));
  }

  private void doTestDie() {
    doTestConsoleFilter(new PerlConsoleFileLinkFilter(getProject()));
  }

}
