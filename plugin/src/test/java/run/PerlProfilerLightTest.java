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
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.testFramework.PlatformTestUtil;
import com.intellij.testFramework.UsefulTestCase;
import com.perl5.lang.perl.profiler.parser.frames.PerlCallStackElement;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Tests additional stuff, like serializing, deserializing, navigation
 */
public class PerlProfilerLightTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "run/profilerLight";
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

  @Test
  public void testUseStrcit() {
    doTestNavigation("Foo::Baz::BeginNavigation::BEGIN@3");
  }

  @Test
  public void testUseStrcitN() {
    doTestNavigation("Foo::Baz::BeginNavigation::BEGIN@3.3");
  }

  @Test
  public void testBegin() {
    doTestNavigation("Foo::Baz::BeginNavigation::BEGIN@9");
  }

  @Test
  public void testUseInBegin() {
    doTestNavigation("Foo::Baz::BeginNavigation::BEGIN@14");
  }

  @Test
  public void testSubExprShort() {
    VirtualFile targetFile = getSubExprFile();
    doTestNavigation("Foo::Baz::SubExpr::__ANON__[" + targetFile.getPath() + ":6]");
  }

  @Test
  public void testSubExprLong() {
    VirtualFile targetFile = getSubExprFile();
    doTestNavigation("Foo::Baz::SubExpr::__ANON__[" + targetFile.getPath() + ":15]");
  }

  @Test
  public void testSubExprEval() {
    VirtualFile targetFile = getSubExprFile();
    doTestNavigation("Foo::Baz::SubExpr::__ANON__[(eval 0)[" + targetFile.getPath() + ":8]:6]");
  }

  private @NotNull VirtualFile getSubExprFile() {
    var virtualFiles =
      new ArrayList<>(FilenameIndex.getVirtualFilesByName("SubExpr.pm", GlobalSearchScope.allScope(getProject())));
    assertSize(1, virtualFiles);
    return virtualFiles.get(0);
  }

  protected void doTestNavigation(@NotNull String frameName) {
    var stackElement = PerlCallStackElement.create(frameName);
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
      navigatablePsiElement.navigate(true);
      PlatformTestUtil.dispatchAllEventsInIdeEventQueue();
      FileEditorManager fileEditorManager = FileEditorManager.getInstance(getProject());
      var selectedFileEditor = fileEditorManager.getSelectedEditor();
      assertInstanceOf(selectedFileEditor, TextEditor.class);
      var selectedEditor = ((TextEditor)selectedFileEditor).getEditor();
      assertInstanceOf(selectedEditor, EditorImpl.class);
      sb.append(getEditorTextWithCaretsAndSelections(selectedEditor)).append(SEPARATOR_NEWLINES);
    }
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
  }
}
