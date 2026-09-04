/*
 * Copyright 2015-2026 Alexandr Evstigneev
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
package rename

import base.MojoLightTestCase
import org.junit.Test


open class MojoTemplatesRenameTest : MojoLightTestCase() {
  override fun getBaseDataPath(): String = "rename/templates"

  @Test
  fun testCorrectHtml() = doTestRenameVar()

  @Test
  fun testIncompleteHtml() = doTestRenameVar()

  @Test
  fun testCorrectHtmlBlock() = doTestRenameVar()

  @Test
  fun testIncompleteHtmlBlock() = doTestRenameVar()
}
