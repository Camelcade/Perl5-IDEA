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

package editor;


import base.PerlLightTestCase;
import com.perl5.lang.perl.idea.execution.filters.PerlAbsolutePathConsoleFilter;
import com.perl5.lang.perl.idea.execution.filters.PerlConsoleFileLinkFilter;
import org.junit.Test;
public class PerlConsoleFilterTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "consoleFilter/perl";
  }

  @Test
  public void testConfess() {doTestDie();}

  @Test
  public void testDie() {doTestDie();}

  @Test
  public void testPythonBug() {doTestDie();}

  @Test
  public void testInstallDtlFast() {
    doTestAbsolute();
  }

  @Test
  public void testInstallDtlFastWin() {
    doTestAbsolute();
  }

  @Test
  public void testDeparse() {doTestAbsolute();}

  @Test
  public void testLibraryPath() {
    doTestAbsolute();
  }

  @Test
  public void testSemicolons() {doTestAbsolute();}

  private void doTestAbsolute() {
    doTestConsoleFilter(new PerlAbsolutePathConsoleFilter(getProject(), () -> null));
  }

  private void doTestDie() {
    doTestConsoleFilter(new PerlConsoleFileLinkFilter(getProject(), () -> null));
  }
}
