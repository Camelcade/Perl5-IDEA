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

package parser;

import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.html.HTMLLanguage;
import com.intellij.lang.html.HTMLParserDefinition;
import com.intellij.lexer.EmbeddedTokenTypesProvider;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.psi.templateLanguages.TemplateDataLanguageMappings;
import com.intellij.psi.templateLanguages.TemplateDataLanguagePatterns;
import com.intellij.testFramework.ParsingTestCase;
import com.intellij.testFramework.TestDataFile;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.extensions.packageprocessor.impl.ConstantProcessor;
import com.perl5.lang.perl.extensions.packageprocessor.impl.ExceptionClassProcessor;
import com.perl5.lang.perl.extensions.packageprocessor.impl.VarsProcessor;
import com.perl5.lang.perl.extensions.parser.PerlParserExtension;
import com.perl5.lang.perl.extensions.readonly.ReadonlyImplicitDeclarationsProvider;
import com.perl5.lang.perl.extensions.typesStandard.TypesStandardImplicitDeclarationsProvider;
import com.perl5.lang.perl.fileTypes.PerlFileTypeScript;
import com.perl5.lang.perl.idea.EP.PerlPackageProcessorEP;
import com.perl5.lang.perl.idea.application.PerlParserExtensions;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.idea.project.PerlNamesCache;
import com.perl5.lang.perl.parser.ClassAccessorParserExtension;
import com.perl5.lang.perl.parser.MooseParserExtension;
import com.perl5.lang.perl.parser.PerlSwitchParserExtensionImpl;
import com.perl5.lang.perl.psi.references.PerlCoreDeclarationsProvider;
import com.perl5.lang.perl.psi.references.PerlImplicitDeclarationsProvider;
import com.perl5.lang.perl.psi.references.PerlImplicitDeclarationsService;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.PodParserDefinition;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;


public abstract class PerlParserTestBase extends ParsingTestCase {
  public PerlParserTestBase() {
    this("", PerlFileTypeScript.EXTENSION_PL, new PerlParserDefinition());
  }

  public PerlParserTestBase(@NonNls @NotNull String dataPath, @NotNull String fileExt, @NotNull ParserDefinition... definitions) {
    super(dataPath, fileExt, true, definitions);
  }

  @Override
  protected void doTest(boolean checkErrors) {
    super.doTest(true);
    if (checkErrors) {
      doCheckErrors();
    }
  }

  protected void doCheckErrors() {
    assertFalse(
      "PsiFile contains error elements",
      toParseTreeText(myFile, skipSpaces(), includeRanges()).contains("PsiErrorElement")
    );
  }

  @Deprecated // this is legacy for heavy tests
  public void doTest(String name) {
    doTest(true);
  }

  @Deprecated // this is legacy for heavy tests
  public void doTest(String name, boolean check) {
    doTest(check);
  }

  public void doTest() {
    doTest(true);
  }

  @Override
  protected boolean skipSpaces() {
    return true;
  }

  protected String getPerlTidy() {
    try {
      return FileUtil.loadFile(new File("testData", "perlTidy.code"), CharsetToolkit.UTF8, true).trim();
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
    registerApplicationService(TemplateDataLanguageMappings.class, new TemplateDataLanguageMappings(getProject()));
    registerApplicationService(TemplateDataLanguagePatterns.class, new TemplateDataLanguagePatterns());

    addExplicitExtension(LanguageParserDefinitions.INSTANCE, PerlLanguage.INSTANCE, new PerlParserDefinition());
    addExplicitExtension(LanguageParserDefinitions.INSTANCE, PodLanguage.INSTANCE, new PodParserDefinition());

    registerExtensionPoint(EmbeddedTokenTypesProvider.EXTENSION_POINT_NAME, EmbeddedTokenTypesProvider.class);
    addExplicitExtension(LanguageParserDefinitions.INSTANCE, HTMLLanguage.INSTANCE, new HTMLParserDefinition());

    registerComponentInstance(myProject, PerlNamesCache.class, new PerlNamesCache(myProject));

    registerParserExtensions();

    PerlParserExtensions parserExtensions = new PerlParserExtensions();
    registerComponentInstance(ApplicationManager.getApplication(), PerlParserExtensions.class, parserExtensions);
    parserExtensions.initComponent();

    PerlPackageProcessorEP.EP.addExplicitExtension("constant", new ConstantProcessor());
    PerlPackageProcessorEP.EP.addExplicitExtension("vars", new VarsProcessor());
    PerlPackageProcessorEP.EP.addExplicitExtension("Exception::Class", new ExceptionClassProcessor());

    myProject.registerService(PerlSharedSettings.class, new PerlSharedSettings(getProject()));

    registerExtensionPoint(PerlImplicitDeclarationsProvider.EP_NAME, PerlImplicitDeclarationsProvider.class);
    registerExtension(PerlImplicitDeclarationsProvider.EP_NAME, new PerlCoreDeclarationsProvider());
    registerExtension(PerlImplicitDeclarationsProvider.EP_NAME, new TypesStandardImplicitDeclarationsProvider());
    registerExtension(PerlImplicitDeclarationsProvider.EP_NAME, new ReadonlyImplicitDeclarationsProvider());

    myProject.registerService(PerlImplicitDeclarationsService.class, new PerlImplicitDeclarationsService(getProject()));

  }

  protected void registerParserExtensions(){
    registerExtensionPoint(PerlParserExtension.EP_NAME, PerlParserExtension.class);
    registerExtension(PerlParserExtension.EP_NAME, new MooseParserExtension());
    registerExtension(PerlParserExtension.EP_NAME, new PerlSwitchParserExtensionImpl());
    registerExtension(PerlParserExtension.EP_NAME, new ClassAccessorParserExtension());

  }

  protected String loadFile(@NotNull @NonNls @TestDataFile String name) throws IOException {
    String adjustedName = myFileExt.isEmpty() ? name.replace(".", "") : name.replace("." + myFileExt, ".code");
    return FileUtil.loadFile(new File(myFullDataPath, adjustedName), CharsetToolkit.UTF8, true).trim();
  }

  @Override
  protected boolean checkAllPsiRoots() {
    return false;
  }
}
