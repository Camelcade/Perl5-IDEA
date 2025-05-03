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

package intellilang

import base.PerlLightTestCase
import com.intellij.codeInsight.intention.impl.QuickEditAction
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import com.intellij.testFramework.fixtures.InjectionTestFixture
import org.jetbrains.annotations.NonNls
import org.junit.Test

class PerlQuickEditTest : PerlLightTestCase() {
  override fun getBaseDataPath(): @NonNls String? = "intellilang/perl/quickedit"
  private val injectionTestFixture: InjectionTestFixture get() = InjectionTestFixture(myFixture)

  @Test
  fun testEditHeredocMiddle() {
    val (originalEditor, fragmentFile) = initFileWithTestSample()

    myFixture.editor.caretModel.moveToOffset(fragmentFile.text.indexAfter("<html>"))
    myFixture.type("\nhello\n there")
    assertFalse(myFixture.editor.isDisposed)
    assertEquals(
      """
      <div>
          <html>
          hello
           there
          </html>
      </div>""".trimIndent(), myFixture.editor.document.text.trim().trimLines()
    )

    assertEquals(
      """
      use v5.36;
      
      sub foo{
          say <<~HTML;
          <div>
              <html>
              hello
               there
              </html>
          </div>
          HTML
      }""".trimIndent(), originalEditor.document.text
    )
  }

  @Test
  fun testEditHeredocEnd() {
    val (originalEditor, fragmentFile) = initFileWithTestSample()

    myFixture.editor.caretModel.moveToOffset(fragmentFile.text.indexAfter("</div>"))
    myFixture.type("\n\n\nhello\n there\n")
    assertFalse(myFixture.editor.isDisposed)
    assertEquals(
      """
      <div>
          <html>
          </html>
      </div>
      
      
      hello
       there
      """.trimIndent(), myFixture.editor.document.text.trim().trimLines()
    )

    assertEquals(
      """
      use v5.36;
      
      sub foo{
          say <<~HTML;
          <div>
              <html>
              </html>
          </div>
          
          
          hello
           there


          HTML
      }""".trimIndent(), originalEditor.document.text
    )
  }

  @Test
  fun testEditHeredocStart() {
    val (originalEditor, fragmentFile) = initFileWithTestSample()

    myFixture.editor.caretModel.moveToOffset(fragmentFile.text.indexOf("<div>"))
    myFixture.type("\n\n\nhello\n there\n\n")
    assertFalse(myFixture.editor.isDisposed)
    assertEquals(
      """
      hello
       there
      
      <div>
          <html>
          </html>
      </div>""".trimIndent(), myFixture.editor.document.text.trim().trimLines()
    )

    assertEquals(
      """
      use v5.36;
      
      sub foo{
          say <<~HTML;
          
          
          
          hello
           there
          
          <div>
              <html>
              </html>
          </div>
          HTML
      }""".trimIndent(), originalEditor.document.text
    )
  }

  @Test
  fun testEmptyHeredoc() {
    val (originalEditor, fragmentFile) = initFileWithTestSample(
      """
      use v5.36;
      
      sub foo{
          say <<~HTML;
          <caret>
          HTML
      }""".trimIndent(),
      ""
    )

    myFixture.type("\nhello\n  there\n")
    assertFalse(myFixture.editor.isDisposed)
    assertEquals(
      """
      hello
        there
      """.trimIndent(), myFixture.editor.document.text.trim().trimLines()
    )

    assertEquals(
      """
      use v5.36;
      
      sub foo{
          say <<~HTML;
          
          hello
            there
            
          HTML
      }""".trimIndent().trimLines(), originalEditor.document.text.trimLines()
    )
  }

  @Test
  fun testReplaceHeredoc() {
    val (originalEditor, fragmentFile) = initFileWithTestSample()

    myFixture.editor.selectionModel.setSelection(0, myFixture.editor.document.text.length)
    myFixture.type("hello\n there")
    assertFalse(myFixture.editor.isDisposed)
    assertEquals(
      """
      hello
       there
      """.trimIndent(), myFixture.editor.document.text.trim().trimLines()
    )

    assertEquals(
      """
      use v5.36;
      
      sub foo{
          say <<~HTML;
          hello
           there
          HTML
      }""".trimIndent(), originalEditor.document.text
    )
  }


  private fun initFileWithTestSample(
    testSample: String =
      """
        use v5.36;
        
        sub foo{
            say <<~HTML;
            <div>
                <html><caret>
                </html>
            </div>
            HTML
        }""".trimIndent(),
    injectedSample: String = """
        <div>
            <html>
            </html>
        </div>
        """.trimIndent()
  ): Pair<Editor, PsiFile> {
    initWithTextSmart(testSample)
    val originalEditor = injectionTestFixture.topLevelEditor
    val quickEditHandler = QuickEditAction().invokeImpl(project, injectionTestFixture.topLevelEditor, injectionTestFixture.topLevelFile)
    val fragmentFile = quickEditHandler.newFile
    assertEquals(injectedSample, fragmentFile.text.trim())

    myFixture.openFileInEditor(fragmentFile.virtualFile)
    return Pair(originalEditor, fragmentFile)
  }

  private fun String.indexAfter(string: String): Int {
    val r = indexOf(string)
    return if (r == -1) -1 else r + string.length
  }

  private fun String.trimLines(): String {
    return replace(Regex("[ \t]+\n"), "\n")
  }

}