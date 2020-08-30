/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package run;

import base.PerlLightTestCase;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.testFramework.UsefulTestCase;
import com.perl5.lang.perl.profiler.parser.PerlCallStackElement;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

/**
 * Tests additional stuff, like serializing, deserializing, navigation
 */
public class PerlProfilerLightTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/run/profilerLight";
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    addTestLibrary("profilerNavigation");
  }

  @Test
  public void testNavigateFqn() {
    doTestNavigation("MyTest::Some::Package::someconst1");
  }

  @Test
  public void testNavigateFqnMulti() {
    doTestNavigation("Foo::Bar::something");
  }

  @Test
  public void testNavigateTry() {
    doTestNavigation("Foo::Bar::try {...}");
  }

  @Test
  public void testNavigateTrySpace() {
    doTestNavigation("Foo::Bar::try {...} ");
  }

  protected void doTestNavigation(@NotNull String frameName) {
    var stackElement = new PerlCallStackElement(frameName);
    var navigatablePsiElements = stackElement.calcNavigatables(getProject());
    assertNotNull(navigatablePsiElements);
    StringBuilder sb = new StringBuilder(stackElement.fullName()).append(SEPARATOR_NEWLINES);
    for (NavigatablePsiElement navigatablePsiElement : navigatablePsiElements) {
      var elementPresentation = navigatablePsiElement.getPresentation();
      sb
        .append(serializePsiElement(navigatablePsiElement))
        .append("\n")
        .append(serializePresentation(elementPresentation))
        .append(SEPARATOR_NEWLINES);
    }
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
  }
}
