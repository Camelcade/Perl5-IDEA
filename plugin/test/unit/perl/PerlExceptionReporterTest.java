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

package unit.perl;

import base.PerlLightTestCase;
import com.intellij.openapi.diagnostic.Attachment;
import com.intellij.openapi.util.text.StringUtil;
import com.perl5.errorHandler.YoutrackErrorHandler;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;

public class PerlExceptionReporterTest extends PerlLightTestCase {
  @Ignore("For manual testing only")
  @Test
  public void testReporting() {
    Throwable first = new Throwable();
    Throwable second = new Throwable(first);
    Throwable third = new Throwable(second);
    String result = new YoutrackErrorHandler().submit(
      "Test description и по-русски", "Test body и немного русского языка", "2019.2",
      Arrays.asList(new Attachment("fist", first),
                    new Attachment("fist", second),
                    new Attachment("second", third),
                    new Attachment("последний", "русский аттачмент")
      ));
    assertTrue(StringUtil.isNotEmpty(result));
  }
}
