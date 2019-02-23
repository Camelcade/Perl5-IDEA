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

package base;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.perl5.lang.perl.fileTypes.PerlFileTypeScript;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 * Created by hurricup on 04.03.2016.
 */
public abstract class PerlLightTestCase extends PerlLightTestCaseBase {

  public String getFileExtension() {
    return PerlFileTypeScript.EXTENSION_PL;
  }

  protected void withTestMore() {
    addTestLibrary("test_more");
  }

  public void initWithPerlTidy() {
    initWithPerlTidy("perlTidy");
  }

  protected void initWithCpanFile() {
    try {
      initWithFile("cpanfile", "");
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void initWithPerlTidy(@NotNull String targetName) {
    try {
      initWithFileContent(targetName, getFileExtension(),
                          FileUtil.loadFile(new File("testData", "perlTidy.code"), CharsetToolkit.UTF8, true).trim());
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected void withCpanfile() {addTestLibrary("cpanfile");}

  protected void withLog4perl() { addTestLibrary("log4perl"); }

  protected void withFileSpec() { addTestLibrary("fileSpec"); }

  protected void addCustomPackage() {
    myFixture.copyFileToProject("MyCustomPackage.pm");
  }
}
