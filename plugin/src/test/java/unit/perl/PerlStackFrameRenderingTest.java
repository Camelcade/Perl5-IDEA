/*
 * Copyright 2015-2024 Alexandr Evstigneev
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

package unit.perl;

import base.PerlLightTestCase;
import com.intellij.testFramework.Parameterized;
import com.intellij.testFramework.UsefulTestCase;
import com.intellij.ui.SimpleTextAttributes;
import com.perl5.lang.perl.debugger.PerlStackFrame;
import com.perl5.lang.perl.debugger.protocol.PerlLoadedFileDescriptor;
import com.perl5.lang.perl.debugger.protocol.PerlStackFrameDescriptor;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;

@SuppressWarnings("Junit4RunWithInspection")
@RunWith(Parameterized.class)
public class PerlStackFrameRenderingTest extends PerlLightTestCase {
  private final @NotNull String myName;
  private final @NotNull PerlStackFrameDescriptor myFrameDescriptor;

  public PerlStackFrameRenderingTest(@NotNull String name, @NotNull PerlStackFrameDescriptor frameDescriptor) {
    myName = name;
    myFrameDescriptor = frameDescriptor;
  }

  @Override
  protected String getBaseDataPath() {
    return "unit/perl/stackframerendering";
  }

  @Test
  public void test() {
    var result = new StringBuilder();
    PerlStackFrame.doCustomizePresentation(myFrameDescriptor, (fragment, attributes) ->
      result.append(protectSpaces(fragment)).append(" - ").append(serialize(attributes)).append("\n"));
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), result.toString());
  }

  protected static @NotNull String serialize(@NotNull SimpleTextAttributes attributes) {
    if (attributes == SimpleTextAttributes.REGULAR_ATTRIBUTES) {
      return "REGULAR_ATTRIBUTES";
    }
    else if (attributes == SimpleTextAttributes.GRAYED_ATTRIBUTES) {
      return "GRAYED_ATTRIBUTES";
    }
    return attributes.toString();
  }

  @Override
  protected @NotNull String computeAnswerFileNameWithoutExtension(@NotNull String appendix) {
    return myName.replaceAll("\\W+", "_").toLowerCase() + appendix;
  }

  @org.junit.runners.Parameterized.Parameters(name = "{0}")
  public static Iterable<Object[]> fakeData() {
    return Collections.emptyList();
  }

  @Parameterized.Parameters(name = "{0}")
  public static Iterable<Object[]> realData(Class<?> clazz) {
    return Arrays.asList(new Object[][]{
      {"File, fqn and line", new PerlStackFrameDescriptor(new PerlLoadedFileDescriptor("/path/to/file", "Proper::Fqn"), 42)},
      {"File, main fqn and line", new PerlStackFrameDescriptor(new PerlLoadedFileDescriptor("/path/to/file", "::Fqn"), 42)},
      {"fqn and line", new PerlStackFrameDescriptor(new PerlLoadedFileDescriptor("", "Proper::Fqn"), 42)},
      {"File and line", new PerlStackFrameDescriptor(new PerlLoadedFileDescriptor("/path/to/file", null), 42)},
      {"Eval file, fqn and line",
        new PerlStackFrameDescriptor(new PerlLoadedFileDescriptor("(eval 26)[eval/path/to/file:7]", "Proper::Fqn"), 42)},
      {"Eval file and line", new PerlStackFrameDescriptor(new PerlLoadedFileDescriptor("(eval 26)[eval/path/to/file:7]", null), 42)},
    });
  }
}
