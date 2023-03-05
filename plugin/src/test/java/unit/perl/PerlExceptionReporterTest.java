/*
 * Copyright 2015-2023 Alexandr Evstigneev
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
import categories.Heavy;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Attachment;
import com.intellij.openapi.diagnostic.IdeaLoggingEvent;
import com.intellij.openapi.diagnostic.RuntimeExceptionWithAttachments;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.errorHandler.YoutrackErrorHandler;
import org.jetbrains.annotations.NotNull;
import org.junit.Assume;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;

import static com.perl5.errorHandler.YoutrackErrorHandler.YOUTRACK_PROPERTY_KEY;
import static com.perl5.errorHandler.YoutrackErrorHandler.YOUTRACK_PROPERTY_VALUE;

@Category(Heavy.class)
public class PerlExceptionReporterTest extends PerlLightTestCase {
  @Test
  public void testReporting() {
    assumeNotSkipped();
    Throwable first = new Throwable();
    Throwable second = new Throwable(first);
    Throwable third = new Throwable(second);

    var mainException = new RuntimeExceptionWithAttachments("Test body и немного русского языка", new Attachment("fist", first),
                                                            new Attachment("fist", second), new Attachment("second", third),
                                                            new Attachment("последний", "русский аттачмент"));

    var loggingEvents = new IdeaLoggingEvent[]{new IdeaLoggingEvent("Test description и по-русски", mainException)};

    doTest(loggingEvents, "nothing", 4);
  }

  private static void assumeNotSkipped() {
    Assume.assumeFalse("Skipped for pull requests", StringUtil.equals("skip", YOUTRACK_PROPERTY_VALUE));
  }

  @Test
  public void testReportingInvalidPsiElement() {
    assumeNotSkipped();
    initWithTextSmart("say 'hi'");
    PsiFile file = getFile();
    assertTrue(file.isValid());
    PsiElement statement = file.getFirstChild();
    WriteCommandAction.runWriteCommandAction(getProject(), statement::delete);
    assertFalse(statement.isValid());
    try {
      PsiUtilCore.ensureValid(statement);
    }
    catch (Exception e) {
      var loggingEvents = new IdeaLoggingEvent[]{new IdeaLoggingEvent("Test message", e)};
      var additionalInfo = "No info";
      doTest(loggingEvents, additionalInfo, 1);
    }
  }

  private void doTest(@NotNull IdeaLoggingEvent[] loggingEvents, @NotNull String additionalInfo, int expectedAttachments) {
    var errorHandler = new YoutrackErrorHandler();
    var submittingData = errorHandler.doSubmit(loggingEvents, additionalInfo, getProject());

    var youtrackResponse = submittingData.second;
    assertNotNull(youtrackResponse);
    LOG.warn("Posted issue: " + youtrackResponse.idReadable);

    assertEquals("Wrong attachments number: ", expectedAttachments, youtrackResponse.attachmentsAdded);

    if (!YoutrackErrorHandler.hasAdminToken()) {
      LOG.warn("Pass token via property to remove test issues automatically: " + YOUTRACK_PROPERTY_KEY);
      return;
    }

    try {
      var deleteResponse = errorHandler.deleteIssue(youtrackResponse);
      assertEquals("Error removing issue: " + youtrackResponse + "; " + deleteResponse.getStatusLine(), 200,
                   deleteResponse.getStatusLine().getStatusCode());
    }
    catch (IOException ex) {
      fail("Error removing issue: " + ex.getMessage());
    }
  }
}
