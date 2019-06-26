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

package liveTemplates;


import base.PerlCompletionPopupTestCase;
import com.perl5.lang.pod.filetypes.PodFileType;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
public class PodLiveTemplatesPopupTest extends PerlCompletionPopupTestCase {
  @Override
  protected String getBaseDataPath() {
    return "testData/completionPopup/pod";
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    withPerlPod();
    enableLiveTemplatesTesting();
    myFixture.copyFileToProject("test.pm");
  }

  @Override
  public String getFileExtension() {
    return PodFileType.EXTENSION;
  }

  @Test
  public void testH1() {doTest("=head1<caret>");}

  @Test
  public void testH2() {doTest("=head2<caret>");}

  @Test
  public void testAttr() {doTest("=attr<caret>");}

  @Test
  public void testMethod() {doTest("=method<caret>");}

  @Test
  public void testFunc() {doTest("=func<caret>");}

  @Test
  public void testH3() {doTest("=head3<caret>");}

  @Test
  public void testH4() {doTest("=head4<caret>");}

  @Test
  public void testBeginEnd() {doTest("=begin<caret>");}

  @Test
  public void testFor() {doTest("=for<caret>");}

  @Test
  public void testEncoding() {doTest("=encoding<caret>");}

  protected void doTest(@NotNull String textToType) {
    doLiveTemplateVariablePopupTest(textToType);
  }
}
