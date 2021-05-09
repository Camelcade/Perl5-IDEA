/*
 * Copyright 2015-2021 Alexandr Evstigneev
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

import com.intellij.codeInsight.TargetElementUtil;
import com.intellij.codeInsight.actions.MultiCaretCodeInsightAction;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.controlflow.ConditionalInstruction;
import com.intellij.codeInsight.controlflow.Instruction;
import com.intellij.codeInsight.documentation.DocumentationManager;
import com.intellij.codeInsight.editorActions.SelectWordHandler;
import com.intellij.codeInsight.generation.surroundWith.SurroundWithHandler;
import com.intellij.codeInsight.highlighting.BraceMatchingUtil;
import com.intellij.codeInsight.highlighting.HighlightManager;
import com.intellij.codeInsight.highlighting.actions.HighlightUsagesAction;
import com.intellij.codeInsight.hint.ShowParameterInfoHandler;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInsight.intention.impl.QuickEditAction;
import com.intellij.codeInsight.lookup.*;
import com.intellij.codeInsight.navigation.actions.GotoDeclarationAction;
import com.intellij.codeInsight.template.impl.LiveTemplateCompletionContributor;
import com.intellij.codeInsight.template.impl.LiveTemplateLookupElementImpl;
import com.intellij.codeInsight.template.impl.TemplateManagerImpl;
import com.intellij.codeInsight.template.impl.TemplateState;
import com.intellij.codeInsight.template.impl.editorActions.ExpandLiveTemplateByTabAction;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.PathEnvironmentVariableUtil;
import com.intellij.execution.impl.EditorHyperlinkSupport;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.execution.util.ExecUtil;
import com.intellij.ide.DataManager;
import com.intellij.ide.actions.runAnything.activity.RunAnythingProvider;
import com.intellij.ide.actions.runAnything.groups.RunAnythingHelpGroup;
import com.intellij.ide.actions.runAnything.items.RunAnythingHelpItem;
import com.intellij.ide.actions.runAnything.items.RunAnythingItem;
import com.intellij.ide.hierarchy.*;
import com.intellij.ide.hierarchy.actions.BrowseHierarchyActionBase;
import com.intellij.ide.structureView.StructureView;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.DeleteNameDescriptionLocation;
import com.intellij.ide.util.DeleteTypeDescriptionLocation;
import com.intellij.ide.util.gotoByName.FilteringGotoByModel;
import com.intellij.ide.util.gotoByName.GotoClassModel2;
import com.intellij.ide.util.gotoByName.GotoFileModel;
import com.intellij.ide.util.gotoByName.GotoSymbolModel2;
import com.intellij.ide.util.treeView.smartTree.*;
import com.intellij.injected.editor.EditorWindow;
import com.intellij.injected.editor.VirtualFileWindow;
import com.intellij.lang.*;
import com.intellij.lang.documentation.DocumentationProvider;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.lang.parameterInfo.ParameterInfoHandler;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.impl.SimpleDataContext;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.ex.util.LayeredHighlighterIterator;
import com.intellij.openapi.editor.ex.util.LexerEditorHighlighter;
import com.intellij.openapi.editor.ex.util.SegmentArrayWithData;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.impl.PerlModuleExtension;
import com.intellij.openapi.projectRoots.impl.PerlSdkTable;
import com.intellij.openapi.projectRoots.impl.ProjectJdkImpl;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.util.*;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.registry.Registry;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileFilter;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.impl.BlockSupportImpl;
import com.intellij.psi.impl.ChangedPsiRangeUtil;
import com.intellij.psi.impl.DebugUtil;
import com.intellij.psi.impl.PsiManagerEx;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.psi.impl.source.tree.FileElement;
import com.intellij.psi.impl.source.tree.injected.InjectedFileViewProvider;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageEditorUtil;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.refactoring.rename.inplace.VariableInplaceRenameHandler;
import com.intellij.refactoring.util.NonCodeSearchDescriptionLocation;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.testFramework.MapDataContext;
import com.intellij.testFramework.ParsingTestCase;
import com.intellij.testFramework.UsefulTestCase;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import com.intellij.testFramework.fixtures.CodeInsightTestUtil;
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl;
import com.intellij.testFramework.utils.parameterInfo.MockCreateParameterInfoContext;
import com.intellij.ui.DeferredIcon;
import com.intellij.ui.components.breadcrumbs.Crumb;
import com.intellij.ui.icons.RowIcon;
import com.intellij.usageView.*;
import com.intellij.usages.*;
import com.intellij.usages.impl.UsageViewImpl;
import com.intellij.usages.rules.UsageGroupingRule;
import com.intellij.usages.rules.UsageGroupingRuleProvider;
import com.intellij.util.*;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.containers.MultiMap;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.extensions.PerlImplicitVariablesProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.fileTypes.PerlFileTypeScript;
import com.perl5.lang.perl.fileTypes.PerlPluginBaseFileType;
import com.perl5.lang.perl.idea.codeInsight.Perl5CodeInsightSettings;
import com.perl5.lang.perl.idea.codeInsight.PerlParameterInfo;
import com.perl5.lang.perl.idea.codeInsight.controlFlow.*;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager;
import com.perl5.lang.perl.idea.completion.PerlStringCompletionCache;
import com.perl5.lang.perl.idea.configuration.settings.PerlLocalSettings;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.idea.intellilang.PerlInjectionMarkersService;
import com.perl5.lang.perl.idea.manipulators.PerlBareStringManipulator;
import com.perl5.lang.perl.idea.manipulators.PerlStringContentManipulator;
import com.perl5.lang.perl.idea.manipulators.PerlStringManipulator;
import com.perl5.lang.perl.idea.modules.PerlLibrarySourceRootType;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationBase;
import com.perl5.lang.perl.idea.project.PerlNamesCache;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.refactoring.introduce.PerlIntroduceTarget;
import com.perl5.lang.perl.idea.refactoring.introduce.PerlIntroduceVariableHandler;
import com.perl5.lang.perl.idea.refactoring.introduce.occurrence.PerlIntroduceTargetOccurrencesCollector;
import com.perl5.lang.perl.idea.refactoring.introduce.target.PerlIntroduceTargetsHandler;
import com.perl5.lang.perl.idea.refactoring.rename.PerlMemberInplaceRenameHandler;
import com.perl5.lang.perl.idea.sdk.PerlSdkAdditionalData;
import com.perl5.lang.perl.idea.sdk.PerlSdkType;
import com.perl5.lang.perl.idea.sdk.host.PerlHostHandler;
import com.perl5.lang.perl.idea.sdk.implementation.PerlImplementationHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerData;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerHandler;
import com.perl5.lang.perl.internals.PerlVersion;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlPolyNamedElement;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.mixins.PerlStringBareMixin;
import com.perl5.lang.perl.psi.mixins.PerlStringMixin;
import com.perl5.lang.perl.psi.properties.PerlValuableEntity;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlRunUtil;
import com.perl5.lang.pod.PodLanguage;
import gnu.trove.THashSet;
import gnu.trove.TIntHashSet;
import junit.framework.AssertionFailedError;
import kotlin.collections.CollectionsKt;
import org.intellij.lang.annotations.MagicConstant;
import org.intellij.plugins.intelliLang.inject.InjectLanguageAction;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RunWith(JUnit4.class)
public abstract class PerlLightTestCaseBase extends BasePlatformTestCase {
  protected static final String PER_TEST_CODE = "<per test code>";
  private static final String START_FOLD = "<fold\\stext='[^']*'(\\sexpand='[^']*')*>";
  private static final String END_FOLD = "</fold>";
  private static final List<ElementDescriptionLocation> LOCATIONS = Arrays.asList(
    UsageViewShortNameLocation.INSTANCE,
    UsageViewLongNameLocation.INSTANCE,
    UsageViewTypeLocation.INSTANCE,
    UsageViewNodeTextLocation.INSTANCE,
    NonCodeSearchDescriptionLocation.STRINGS_AND_COMMENTS,
    DeleteNameDescriptionLocation.INSTANCE,
    DeleteTypeDescriptionLocation.SINGULAR,
    DeleteTypeDescriptionLocation.PLURAL
  );
  private TextAttributes myReadAttributes;
  private TextAttributes myWriteAttributes;
  private Perl5CodeInsightSettings myCodeInsightSettings;
  private static final boolean ENABLE_SVG_GENERATION = Boolean.parseBoolean(System.getenv("CAMELCADE_GENERATE_SVG"));
  private PerlSharedSettings mySharedSettings;
  private PerlLocalSettings myLocalSettings;
  private PerlInjectionMarkersService myInjectionMarkersService;
  public static final String SEPARATOR = "----------";
  public static final String SEPARATOR_NEW_LINE_BEFORE = "\n" + SEPARATOR;
  public static final String SEPARATOR_NEW_LINE_AFTER = SEPARATOR + "\n";
  public static final String SEPARATOR_NEWLINES = SEPARATOR_NEW_LINE_BEFORE + "\n";
  private final Disposable myPerlLightTestCaseDisposable = Disposer.newDisposable();

  private static final String COMPLETE_PREFIX = "<complete ";
  private String myCompletionResultLookupString = null;
  private int myCompletionResultEditorOffset = -1;
  private int myCompletionInvocationCount = 0;

  /**
   * @return test data path relative to the module root
   */
  @NonNls
  protected String getBaseDataPath(){
    return "";
  }

  @Override
  protected final String getTestDataPath() {
    File file = new File(getBaseDataPath());
    return file.exists() ? file.getAbsolutePath(): "";
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    LiveTemplateCompletionContributor.setShowTemplatesInTests(true, getTestRootDisposable());
    //VfsRootAccess.allowRootAccess(getTestRootDisposable(), "/");
    ElementManipulators.INSTANCE.addExplicitExtension(PerlStringMixin.class, new PerlStringManipulator());
    ElementManipulators.INSTANCE.addExplicitExtension(PerlStringBareMixin.class, new PerlBareStringManipulator());
    ElementManipulators.INSTANCE.addExplicitExtension(PerlStringContentElement.class, new PerlStringContentManipulator());
    if (shouldSetUpLibrary()) {
      setUpLibrary();
    }
    myCodeInsightSettings = Perl5CodeInsightSettings.getInstance().copy();
    mySharedSettings = XmlSerializerUtil.createCopy(PerlSharedSettings.getInstance(getProject()));
    myLocalSettings = XmlSerializerUtil.createCopy(PerlLocalSettings.getInstance(getProject()));
    myInjectionMarkersService = XmlSerializerUtil.createCopy(PerlInjectionMarkersService.getInstance(getProject()));
    EditorColorsScheme scheme = EditorColorsManager.getInstance().getGlobalScheme();
    myReadAttributes = scheme.getAttributes(EditorColors.SEARCH_RESULT_ATTRIBUTES);
    myWriteAttributes = scheme.getAttributes(EditorColors.WRITE_SEARCH_RESULT_ATTRIBUTES);
  }

  @Override
  protected void tearDown() throws Exception {
    try {
      Disposer.dispose(myPerlLightTestCaseDisposable);
      PerlStringCompletionCache.getInstance(getProject()).clear();
      PerlInjectionMarkersService.getInstance(getProject()).loadState(myInjectionMarkersService);
      Perl5CodeInsightSettings.getInstance().loadState(myCodeInsightSettings);
      PerlSharedSettings.getInstance(getProject()).loadState(mySharedSettings);
      PerlLocalSettings.getInstance(getProject()).loadState(myLocalSettings);
      ApplicationManager.getApplication().invokeAndWait(() -> {
        PerlProjectManager projectManager = PerlProjectManager.getInstance(getProject());
        projectManager.setProjectSdk(null);
        projectManager.setExternalLibraries(Collections.emptyList());
      });
    }
    finally {
      super.tearDown();
    }
  }

  protected PsiFile getFile() {
    return myFixture.getFile();
  }

  protected Editor getEditor() {
    return myFixture.getEditor();
  }

  /**
   * Registers disposable to be disposed first on tear down of the light test case
   */
  protected final void addPerlTearDownListener(@NotNull Disposable disposable) {
    Disposer.register(myPerlLightTestCaseDisposable, disposable);
  }

  /**
   * Registers disposable to be disposed after teaar down of the fixture and nullizing the module
   */
  protected final void addTearDownListener(@NotNull Disposable disposable) {
    Disposer.register(getTestRootDisposable(), disposable);
  }

  protected void addSdk() {
    PerlSdkType.createSdk(
      "/usr/bin/perl",
      PerlHostHandler.getDefaultHandler().createData(),
      PerlVersionManagerHandler.getDefaultHandler().createData(),
      this::onSdkCreation);
  }

  private void onSdkCreation(@NotNull Sdk sdk) {
    PerlSdkTable.getInstance().addJdk(sdk, myPerlLightTestCaseDisposable);
    PerlProjectManager.getInstance(getProject()).setProjectSdk(sdk);
    PerlRunUtil.refreshSdkDirs(sdk, getProject());
  }

  /**
   * Unmarks a {@code libDir} as perl source root of any type
   */
  protected void removePerlSourceRoot(@NotNull VirtualFile libDir) {
    assertTrue(libDir.isValid() && libDir.isDirectory());
    PerlModuleExtension.modify(getModule(), it -> it.removeRoot(libDir));
  }

  /**
   * Marks a {@code libDir} as perl library directory
   *
   * @param removeAutomatically if true, this source root going to be unmarked automatically in the perl test tearDown
   */
  protected void markAsLibRoot(@NotNull VirtualFile libDir, boolean removeAutomatically) {
    assertTrue(libDir.isValid() && libDir.isDirectory());
    PerlModuleExtension.modify(getModule(), it -> it.setRoot(libDir, PerlLibrarySourceRootType.INSTANCE));
    if (removeAutomatically) {
      addPerlTearDownListener(() -> removePerlSourceRoot(libDir));
    }
  }

  protected void enableLiveTemplatesTesting() {
    TemplateManagerImpl.setTemplateTesting(getTestRootDisposable());
  }

  protected void setTargetPerlVersion(@NotNull PerlVersion perlVersion) {
    PerlSharedSettings.getInstance(getProject()).setTargetPerlVersion(perlVersion).settingsUpdated();
  }

  /**
   * Adds a library from testData/testLibSets as external dependency
   *
   * @param testLibraryName library name
   */
  protected void addTestLibrary(@NotNull String testLibraryName) {
    Application application = ApplicationManager.getApplication();
    application.invokeAndWait(() -> application.runWriteAction(
      () -> {
        VirtualFile libdir = LocalFileSystem.getInstance().refreshAndFindFileByPath(
          FileUtil.join("testData", "testLibSets", testLibraryName)
        );
        assert libdir != null;

        PerlProjectManager perlProjectManager = PerlProjectManager.getInstance(getProject());
        perlProjectManager.addExternalLibrary(libdir);

        CodeInsightTestFixtureImpl.ensureIndexesUpToDate(getProject());
        PerlNamesCache.getInstance(getProject()).forceCacheUpdate();
      }));
  }

  /**
   * Adds a {@code contentRootFile} as a content entry to the current module
   *
   * @apiNote adding modules is not allowed in the light tests
   */
  protected void addContentEntry(@NotNull VirtualFile contentRootFile) {
    ModuleRootManager moduleRootManager = ModuleRootManager.getInstance(getModule());
    assertNotNull(moduleRootManager);
    ModifiableRootModel modifiableModel = moduleRootManager.getModifiableModel();
    modifiableModel.addContentEntry(contentRootFile);
    WriteAction.run(modifiableModel::commit);
    addPerlTearDownListener(() -> {
      ModifiableRootModel modifiableModel1 = moduleRootManager.getModifiableModel();
      for (ContentEntry modelContentEntry : modifiableModel1.getContentEntries()) {
        if (contentRootFile.equals(modelContentEntry.getFile())) {
          modifiableModel1.removeContentEntry(modelContentEntry);
        }
      }
      WriteAction.run(modifiableModel1::commit);
    });
  }

  /**
   * @return true iff libraries should be added in the set up test method
   */
  protected boolean shouldSetUpLibrary() {
    return true;
  }

  protected void setUpLibrary() {
    Application application = ApplicationManager.getApplication();
    application.invokeAndWait(() -> application.runWriteAction(
      () -> {
        var testLibPathName = getTestLibPath();
        VirtualFile libdir = LocalFileSystem.getInstance().refreshAndFindFileByPath(testLibPathName);
        assertNotNull("Directory does not exists: " + new File(testLibPathName).getAbsolutePath(), libdir);

        PerlProjectManager perlProjectManager = PerlProjectManager.getInstance(getProject());
        ProjectJdkImpl testSdk = PerlSdkTable.getInstance().createSdk("test");
        testSdk.setSdkAdditionalData(new PerlSdkAdditionalData(
          PerlHostHandler.getDefaultHandler().createData(),
          PerlVersionManagerData.getDefault(),
          PerlImplementationHandler.getDefaultHandler().createData()));
        PerlSdkTable.getInstance().addJdk(testSdk, myPerlLightTestCaseDisposable);
        perlProjectManager.setProjectSdk(testSdk);
        perlProjectManager.addExternalLibrary(libdir);
        CodeInsightTestFixtureImpl.ensureIndexesUpToDate(getProject());
        PerlNamesCache.getInstance(getProject()).forceCacheUpdate();
      }));
  }

  protected @NotNull String getTestLibPath() {
    return "../testData/testlib";
  }

  protected @NotNull String getTestLibPathFromNested() {
    return "../../testData/testlib";
  }

  public abstract String getFileExtension();

  protected void testSmartKey(String original, char typed, String expected) {
    initWithTextSmart(original);
    myFixture.type(typed);
    myFixture.checkResult(expected);
  }

  public void initWithFileSmart() {
    initWithFileSmart(getTestName(true));
  }

  public void initWithFileSmart(String filename) {
    try {
      initWithFile(filename, getFileExtension());
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void initWithTextSmart(@NotNull String content) {
    initWithFileContent("test", getFileExtension(), content);
  }

  public void initWithTextSmartWithoutErrors(@NotNull String content) {
    initWithTextSmart(content);
  }

  public void initWithFileContent(String filename, String extension, String content) {
    myFixture.configureByText(filename + (extension.isEmpty() ? "" : "." + extension), getPatchedContent(content));
  }

  protected String getPatchedContent(@NotNull String content) {
    return StringUtil.replace(content, PER_TEST_CODE, getPerTestCode());
  }

  protected @NotNull String getPerTestCode() {
    return "Implement getPerTestCode() method in test";
  }

  public void initWithFile(String filename, String extension) throws IOException {
    initWithFile(filename, extension, filename + (extension.isEmpty() ? "" : getRealDataFileExtension()));
  }

  protected String getRealDataFileExtension() {
    return ".code";
  }

  public void initWithFile(String targetFileName, String targetFileExtension, String sourceFileNameWithExtension) throws IOException {
    initWithFileContent(targetFileName, targetFileExtension,
                        FileUtil.loadFile(new File(getTestDataPath(), sourceFileNameWithExtension), CharsetToolkit.UTF8, true));
  }

  public void initWithFileSmartWithoutErrors() {
    initWithFileSmartWithoutErrors(getTestName(true));
  }

  public void initWithFileSmartWithoutErrors(@NotNull String filename) {
    initWithFileSmart(filename);
    assertNoErrorElements();
  }

  protected void assertNoErrorElements() {
    assertFalse(
      "PsiFile contains error elements:\n" + getFile().getText(),
      DebugUtil.psiToString(getFile(), true, false).contains("PsiErrorElement")
    );
  }

  protected void doFormatTest() {
    doFormatTest("");
  }

  protected void doFormatTest(@NotNull String answerSuffix) {
    doFormatTest(getTestName(true), answerSuffix);
  }

  protected void doFormatTest(@NotNull String filename, @NotNull String resultSuffix) {
    doFormatTest(filename, filename, resultSuffix);
  }

  protected void doFormatTest(@NotNull String sourceFileName, @NotNull String resultFileName, @NotNull String resultSuffix) {
    initWithFileSmartWithoutErrors(sourceFileName);
    doFormatTestWithoutInitialization(resultFileName, resultSuffix);
  }

  protected void doFormatTestWithoutInitialization(@NotNull String resultFileName, @NotNull String resultSuffix) {
    WriteCommandAction.writeCommandAction(getProject()).run(() -> {
      PsiFile file = myFixture.getFile();
      //noinspection UnstableApiUsage
      if (file.getViewProvider() instanceof InjectedFileViewProvider) {
        //noinspection ConstantConditions
        file = file.getContext().getContainingFile();
      }
      TextRange rangeToUse = file.getTextRange();
      CodeStyleManager.getInstance(getProject()).reformatText(file, rangeToUse.getStartOffset(), rangeToUse.getEndOffset());
    });

    String resultFilePath = getTestDataPath() + "/" + resultFileName + resultSuffix + ".txt";
    UsefulTestCase.assertSameLinesWithFile(resultFilePath, myFixture.getFile().getText());
    assertNoErrorElements();
  }

  @MagicConstant(valuesFromClass = Lookup.class)
  protected char getCompletionCompleteChar() {
    return Lookup.NORMAL_SELECT_CHAR;
  }

  protected final int getCompletionInvocationCount() {
    return myCompletionInvocationCount;
  }

  protected void setCompletionInvocationCount(int completionInvocationCount) {
    myCompletionInvocationCount = completionInvocationCount;
  }

  protected @NotNull CompletionType getCompletionType() {
    return CompletionType.BASIC;
  }

  protected String processCompletionResultContent(@NotNull String content) {
    myCompletionResultEditorOffset = StringUtil.indexOf(content, COMPLETE_PREFIX);
    if (myCompletionResultEditorOffset < 0) {
      fail("Please, add <complete lookup_string> to your test data");
    }
    int endOffset = StringUtil.indexOf(content, '>', myCompletionResultEditorOffset);
    if (endOffset < 0) {
      fail("Unclosed `complete` tag");
    }
    myCompletionResultLookupString = content.substring(myCompletionResultEditorOffset + COMPLETE_PREFIX.length(), endOffset);
    return content.substring(0, myCompletionResultEditorOffset) + content.substring(endOffset + 1);
  }

  protected void doTestCompletionResult() {
    doTestCompletionResult(getCompletionCompleteChar());
  }

  protected void doTestCompletionResult(@MagicConstant(valuesFromClass = Lookup.class) char completeChar) {
    initWithFileSmart();
    assertNotNull("Please, add <complete lookup_string> to your test data", myCompletionResultLookupString);
    getEditor().getCaretModel().moveToOffset(myCompletionResultEditorOffset);
    doTestCompletionResult(myCompletionResultLookupString, getCompletionType(), getCompletionInvocationCount(), completeChar);
  }

  protected void doTestCompletionResult(@NotNull String lookupString,
                                        @NotNull CompletionType completionType,
                                        int invocationCount,
                                        @MagicConstant(valuesFromClass = Lookup.class) char completeChar) {
    doCompleteLookupString(lookupString, completionType, invocationCount, completeChar);
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), getEditorTextWithCaretsAndSelections());
  }

  protected void doCompleteLookupString(@NotNull String lookupString,
                                        @NotNull CompletionType completionType,
                                        int invocationCount,
                                        @MagicConstant(valuesFromClass = Lookup.class) char completeChar) {
    LookupElement[] lookupElements = myFixture.complete(completionType, invocationCount);
    for (LookupElement lookupElement : lookupElements) {
      LookupElementPresentation presentation = new LookupElementPresentation();
      lookupElement.renderElement(presentation);
      String tailText = StringUtil.notNullize(presentation.getTailText());
      for (String elementLookupString : lookupElement.getAllLookupStrings()) {
        if (StringUtil.equals(lookupString, elementLookupString + tailText) || StringUtil.equals(lookupString, elementLookupString)) {
          LookupEx activeLookup = LookupManager.getActiveLookup(getEditor());
          assertNotNull(activeLookup);
          activeLookup.setCurrentItem(lookupElement);
          ApplicationManager.getApplication().invokeAndWait(() -> myFixture.finishLookup(completeChar));
          return;
        }
      }
    }
    fail("Unable to find lookup string: " +
         lookupString +
         " in " +
         ReadAction.compute(() -> renderLookupElementsToString(lookupElements, null)));
  }

  public final void doTestCompletion() {
    doTestCompletion("", null);
  }

  public final void doTestCompletion(@Nullable BiPredicate<? super LookupElement, ? super LookupElementPresentation> predicate) {
    doTestCompletion("", predicate);
  }

  public final void doTestCompletion(@NotNull String answerSuffix,
                                     @Nullable BiPredicate<? super LookupElement, ? super LookupElementPresentation> predicate) {
    initWithFileSmart();
    doTestCompletionCheck(answerSuffix, predicate);
  }

  public final void doTestCompletionFromText(@NotNull String content) {
    doTestCompletionFromText(content, null);
  }

  public final void doTestCompletionFromText(@NotNull String content,
                                             @Nullable BiPredicate<? super LookupElement, ? super LookupElementPresentation> predicate) {
    initWithTextSmartWithoutErrors(content);
    doTestCompletionCheck("", predicate);
  }

  protected void doTestCompletionCheck() {
    doTestCompletionCheck("", null);
  }

  protected void doTestCompletionCheck(@NotNull String answerSuffix,
                                       @Nullable BiPredicate<? super LookupElement, ? super LookupElementPresentation> predicate) {
    CodeInsightTestFixtureImpl.ensureIndexesUpToDate(getProject());
    addVirtualFileFilter();
    myFixture.complete(getCompletionType(), getCompletionInvocationCount());
    LookupElement[] elements = myFixture.getLookupElements();
    removeVirtualFileFilter();
    UsefulTestCase.assertSameLinesWithFile(
      getTestResultsFilePath(answerSuffix),
      renderLookupElementsToString(elements, predicate));
  }

  protected @NotNull String renderLookupElementsToString(@Nullable LookupElement[] elements,
                                                         @Nullable BiPredicate<? super LookupElement, ? super LookupElementPresentation> predicate) {
    return String.join("\n", renderLookupElementsToList(elements, predicate));
  }

  protected @NotNull List<String> renderLookupElementsToList(@Nullable LookupElement[] elements,
                                                             @Nullable BiPredicate<? super LookupElement, ? super LookupElementPresentation> predicate) {
    if (elements == null) {
      return Collections.emptyList();
    }
    List<String> result = new ArrayList<>();
    for (LookupElement lookupElement : elements) {
      LookupElementPresentation presentation = new LookupElementPresentation();
      lookupElement.renderElement(presentation);

      if (predicate != null && !predicate.test(lookupElement, presentation)) {
        continue;
      }
      StringBuilder sb = new StringBuilder();

      Set<String> lookupStrings = lookupElement.getAllLookupStrings();
      if (lookupStrings.size() > 1) {
        Set<String> orderedSet = new LinkedHashSet<>();
        orderedSet.add(lookupElement.getLookupString());
        orderedSet.addAll(ContainerUtil.sorted(lookupStrings));
        lookupStrings = orderedSet;
      }

      sb.append("Text: ")
        .append(presentation.getItemText())
        .append("; Tail: ")
        .append(presentation.getTailText())
        .append("; Type: ")
        .append(presentation.getTypeText())
        .append("; Icon: ")
        .append(getIconText(presentation.getIcon()))
        .append("; Type Icon: ")
        .append(getIconText(presentation.getTypeIcon()))
        .append("\n\tLookups: ")
        .append(String.join(", ", lookupStrings))
      ;

      Object lookupObject = lookupElement.getObject();
      if (lookupObject instanceof PsiElement) {
        sb.append("\n\tPsiElement: ").append(serializePsiElement((PsiElement)lookupObject));
      }
      else if (lookupObject instanceof VirtualFile) {
        sb.append("\n\tVirtualFile: ").append(((VirtualFile)lookupObject).getName());
      }
      else if (lookupObject instanceof PerlExportDescriptor) {
        sb.append("\n\tExport: ").append(lookupObject);
      }
      else if (lookupObject instanceof IElementType) {
        sb.append("\n\tExport: ").append(lookupObject);
      }
      else if (lookupObject instanceof String) {
        sb.append("\n\tString: ").append(lookupObject);
      }
      else if (lookupElement instanceof LiveTemplateLookupElementImpl) {
        sb.append("\n\tLive template: ").append(((LiveTemplateLookupElementImpl)lookupElement).getTemplate());
      }
      else {
        sb.append("\n\t").append(lookupObject.getClass().getSimpleName());
      }

      result.add(sb.toString());
    }

    ContainerUtil.sort(result);
    return result;
  }

  protected String getIconText(@Nullable Icon icon) {
    if (icon == null) {
      return "null";
    }
    String iconString;
    if (icon instanceof IconLoader.CachedImageIcon) {
      iconString = ((IconLoader.CachedImageIcon)icon).getOriginalPath();
    }
    else if (icon instanceof DeferredIcon) {
      return getIconText(((DeferredIcon)icon).getBaseIcon());
    }
    else if (icon instanceof RowIcon) {
      List<String> iconStrings = new ArrayList<>();
      for (Icon subIcon : ((RowIcon)icon).getAllIcons()) {
        if (subIcon != null) {
          iconStrings.add(getIconText(subIcon));
        }
      }
      return String.join("; ", iconStrings);
    }
    else {
      iconString = icon.toString();
    }
    return iconString.substring(iconString.lastIndexOf('/'));
  }

  protected @Nullable PsiFile getTopLevelFile() {
    PsiFile psiFile = getFile();
    return psiFile == null ? null : InjectedLanguageManager.getInstance(getProject()).getTopLevelFile(psiFile);
  }

  protected void addVirtualFileFilter() {
    VirtualFile openedVirtualFile = ObjectUtils.doIfNotNull(getTopLevelFile(), PsiFile::getVirtualFile);
    ((PsiManagerEx)myFixture.getPsiManager()).setAssertOnFileLoadingFilter(file -> {
      if (!(file.getFileType() instanceof PerlPluginBaseFileType)) {
        return false;
      }
      if (file instanceof VirtualFileWindow) {
        file = ((VirtualFileWindow)file).getDelegate();
      }
      if (file instanceof LightVirtualFile) {
        file = ((LightVirtualFile)file).getOriginalFile();
      }
      return !Objects.equals(openedVirtualFile, file);
    }, getTestRootDisposable());
  }

  protected void removeVirtualFileFilter() {
    ((PsiManagerEx)myFixture.getPsiManager()).setAssertOnFileLoadingFilter(VirtualFileFilter.NONE, getTestRootDisposable());
  }


  public String getTestResultsFilePath() {
    return getTestResultsFilePath(getTestResultSuffix());
  }

  protected @NotNull String getTestResultSuffix() {
    return "";
  }

  protected String getResultsTestDataPath() {
    return getTestDataPath();
  }

  public String getTestResultsFilePath(@NotNull String appendix) {
    return getResultsTestDataPath() + "/" + computeAnswerFileName(appendix);
  }

  protected @NotNull String computeAnswerFileName(@NotNull String appendix) {
    return computeAnswerFileNameWithoutExtension(appendix) + "." + getResultsFileExtension();
  }

  protected @NotNull String computeAnswerFileNameWithoutExtension(@NotNull String appendix) {
    return getTestName(true) + appendix;
  }

  protected @NotNull String getResultsFileExtension() {
    return getFileExtension() + ".txt";
  }

  public void doTestResolve() {
    doTestResolve(false);
  }

  public void doTestResolve(boolean reparse) {
    initWithFileSmartWithoutErrors();
    doTestResolveWithoutInit(reparse);
  }

  public void doTestResolveWithoutInit(boolean reparse) {
    if (reparse) {
      PerlNamesCache.getInstance(getProject()).forceCacheUpdate();
      FileContentUtil.reparseFiles(getProject(), Collections.emptyList(), true);
    }
    checkSerializedReferencesWithFile();
  }

  public void checkSerializedReferencesWithFile() {
    checkSerializedReferencesWithFile("");
  }

  public void checkSerializedReferencesWithFile(@NotNull String appendix) {
    CodeInsightTestFixtureImpl.ensureIndexesUpToDate(getProject());
    addVirtualFileFilter();
    StringBuilder sb = new StringBuilder();

    List<PsiReference> references = collectFileReferences();
    for (PsiReference psiReference : references) {
      psiReference.resolve();
    }
    removeVirtualFileFilter();

    for (PsiReference psiReference : references) {
      sb.append(serializeReference(psiReference)).append("\n");
    }
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(appendix), sb.toString());
  }

  protected boolean withInjections() {
    return false;
  }

  private String serializeReference(PsiReference reference) {
    StringBuilder sb = new StringBuilder();
    PsiElement sourceElement = reference.getElement();

    List<ResolveResult> resolveResults;
    if (reference instanceof PsiPolyVariantReference) {
      resolveResults = Arrays.asList(((PsiPolyVariantReference)reference).multiResolve(false));
    }
    else {
      PsiElement target = reference.resolve();
      resolveResults = target == null ? Collections.emptyList() : Collections.singletonList(new PsiElementResolveResult(target));
    }

    TextRange referenceRange = reference.getRangeInElement();
    String sourceElementText = sourceElement.getText();
    int sourceElementOffset = sourceElement.getNode().getStartOffset();

    sb
      .append(reference.getClass().getSimpleName())
      .append(" at ")
      .append(referenceRange.shiftRight(sourceElementOffset))
      .append("; text in range: '")
      .append(referenceRange.subSequence(sourceElementText))
      .append("'")
      .append(" => ")
      .append(resolveResults.size())
      .append(" results:")
      .append('\n');

    //noinspection ConstantConditions
    resolveResults.sort(Comparator.comparing(it -> {
      var virtualFile = PsiUtilCore.getVirtualFile(it.getElement());
      return virtualFile == null ? "" : virtualFile.getPath();
    }));

    for (ResolveResult result : resolveResults) {
      if (!result.isValidResult()) {
        throw new AssertionFailedError("Invalid resolve result");
      }

      PsiElement targetElement = result.getElement();
      assertNotNull(targetElement);

      sb.append('\t').append(serializePsiElement(targetElement)).append("\n");
    }
    return sb.toString();
  }

  private List<PsiReference> collectFileReferences() {
    final List<PsiReference> references = new ArrayList<>();
    InjectedLanguageManager injectedLanguageManager = InjectedLanguageManager.getInstance(getProject());

    getFile().getViewProvider().getAllFiles().forEach(file -> {
      if (!file.getLanguage().isKindOf(PerlLanguage.INSTANCE) && !file.getLanguage().isKindOf(PodLanguage.INSTANCE)) {
        return;
      }
      file.accept(new PsiElementVisitor() {
        @Override
        public void visitElement(@NotNull PsiElement element) {
          Collections.addAll(references, element.getReferences());
          if (withInjections()) {
            if (element instanceof PsiLanguageInjectionHost) {
              List<Pair<PsiElement, TextRange>> files = injectedLanguageManager.getInjectedPsiFiles(element);
              if (files != null) {
                for (Pair<PsiElement, TextRange> pair : files) {
                  pair.first.accept(this);
                }
              }
            }
          }
          element.acceptChildren(this);
        }
      });
    });

    references.sort((o1, o2) -> o1.getElement().getTextRange().getStartOffset() + o1.getRangeInElement().getStartOffset() -
                                o2.getElement().getTextRange().getStartOffset() + o2.getRangeInElement().getStartOffset());

    return references;
  }

  protected void doTestFindUsages() {
    initWithFileSmart();
    int flags = TargetElementUtil.ELEMENT_NAME_ACCEPTED | TargetElementUtil.REFERENCED_ELEMENT_ACCEPTED;
    PsiElement targetElement = TargetElementUtil.findTargetElement(getEditor(), flags);
    assertNotNull("Cannot find referenced element", targetElement);
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), checkAndSerializeFindUsages(targetElement));
  }

  private String checkAndSerializeFindUsages(@NotNull PsiElement targetElement) {
    StringBuilder sb = new StringBuilder();
    sb.append("Target: ").append(serializePsiElement(targetElement)).append("\n");
    List<UsageInfo> usages = new ArrayList<>(myFixture.findUsages(targetElement));
    usages.sort(Comparator.comparing(it -> {
      PsiElement element = it.getElement();
      return element == null ? null : it.getElement().getContainingFile().getName();
    }));
    sb.append("Total usages: ").append(usages.size()).append("\n");
    for (UsageInfo usage : usages) {
      sb.append("\t").append(serializePsiElement(usage.getElement()));
      PsiReference reference = usage.getReference();
      if (reference != null) {
        sb.append(", via: ").append(serializeReference(reference));
      }
      sb.append("\n");
    }

    return sb.toString();
  }

  protected void doLiveTemplateBulkTest(@NotNull String textToType) {
    doLiveTemplateBulkTest("liveTemplatesTest", textToType);
  }

  protected void doLiveTemplateTest() {
    doLiveTemplateBulkTest(getTestName(true), "");
  }

  protected void doLiveTemplateBulkTest(@NotNull String fileName, @NotNull String textToType) {
    enableLiveTemplatesTesting();
    initWithFileSmart(fileName);
    Editor editor = getEditor();
    Document document = editor.getDocument();
    CaretModel caretModel = editor.getCaretModel();
    List<Integer> caretsOffsets = getAndRemoveCarets();
    String originalText = document.getText();

    StringBuilder sb = new StringBuilder();

    for (Integer offset : caretsOffsets) {
      WriteCommandAction.runWriteCommandAction(getProject(), () -> {
        caretModel.moveToOffset(offset);
        if (StringUtil.isNotEmpty(textToType)) {
          EditorModificationUtil.insertStringAtCaret(editor, textToType);
        }
        String textBeforeAction = document.getText();
        myFixture.testAction(new ExpandLiveTemplateByTabAction());
        if (!StringUtil.equals(document.getText(), textBeforeAction)) {
          sb.append(StringUtil.repeat("-", 80)).append("\n")
            .append("Caret offset: ")
            .append(offset)
            .append("\n")
            .append(StringUtil.repeat("-", 80)).append("\n")
            .append(getEditorTextWithCaretsAndSelections());
        }
        TemplateState templateState = TemplateManagerImpl.getTemplateState(editor);
        if (templateState != null) {
          templateState.gotoEnd(true);
        }
        document.setText(originalText);
      });
    }
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
  }

  /**
   * @return all carets offsets. Secondary carets removed
   */
  protected List<Integer> getAndRemoveCarets() {
    CaretModel caretModel = getTopLevelEditor().getCaretModel();
    List<Integer> caretsOffsets = ContainerUtil.map(caretModel.getAllCarets(), Caret::getOffset);
    caretModel.removeSecondaryCarets();
    return caretsOffsets;
  }

  protected void doTestWorldSelector() {
    initWithFileSmartWithoutErrors();
    List<Integer> offsets = getAndRemoveCarets();
    List<Pair<Integer, String>> macroses = new ArrayList<>();
    Caret currentCaret = getEditor().getCaretModel().getCurrentCaret();
    for (Integer offset : offsets) {
      currentCaret.moveToOffset(offset);
      new SelectWordHandler(null).execute(getEditor(), currentCaret, getEditorDataContext());
      addCaretInfo(currentCaret, macroses);
      currentCaret.removeSelection();
    }
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), getEditorTextWithMacroses(getTopLevelEditor(), macroses));
  }

  private DataContext getEditorDataContext() {
    return DataManager.getInstance().getDataContext(myFixture.getEditor().getComponent());
  }


  private String getEditorText() {
    return getTopLevelEditor().getDocument().getText();
  }

  protected @NotNull Editor getTopLevelEditor() {
    return InjectedLanguageEditorUtil.getTopLevelEditor(getEditor());
  }

  /**
   * Add markers with offsets for caret and selection
   */
  private void addCaretInfo(@NotNull Caret caret,
                            @NotNull List<Pair<Integer, String>> macroses) {
    macroses.add(Pair.create(caret.getOffset(), "<caret>"));
    if (caret.hasSelection()) {
      macroses.add(Pair.create(caret.getSelectionStart(), "<selection>"));
      macroses.add(Pair.create(caret.getSelectionEnd(), "</selection>"));
    }
  }

  protected String getEditorTextWithCaretsAndSelections() {
    return getEditorTextWithCaretsAndSelections(getTopLevelEditor());
  }

  protected String getEditorTextWithCaretsAndSelections(@NotNull Editor editor) {
    return getEditorTextWithMacroses(editor, addCaretsMacroses(editor, new ArrayList<>()));
  }

  private @NotNull List<Pair<Integer, String>> addCaretsMacroses(@NotNull Editor editor,
                                                                 @NotNull List<Pair<Integer, String>> macroses) {
    editor.getCaretModel().getAllCarets().forEach(caret -> addCaretInfo(caret, macroses));
    return macroses;
  }

  private @NotNull String getEditorTextWithMacroses(@NotNull Editor editor, @NotNull List<Pair<Integer, String>> macros) {
    ContainerUtil.sort(macros, Comparator.comparingInt(pair -> pair.first));
    StringBuilder sb = new StringBuilder(editor.getDocument().getText());

    for (int i = macros.size() - 1; i > -1; i--) {
      Pair<Integer, String> macro = macros.get(i);
      sb.insert(macro.first, macro.second);
    }

    return sb.toString();
  }

  protected void doTestTyping(@NotNull String toType) {
    initWithFileSmart();
    doTestTypingWithoutInit(toType);
  }

  protected void doTestTypingWithoutInit(@NotNull String toType) {
    myFixture.type(toType);
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), getEditorTextWithCaretsAndSelections());
  }

  protected void doTestTypingWithoutFiles(@NotNull String initialText, @NotNull String toType, @NotNull String expected) {
    initWithTextSmart(initialText);
    myFixture.type(toType);
    myFixture.checkResult(expected);
  }

  protected void doTestBackspaceWithoutFiles(@NotNull String initialText, @NotNull String expected) {
    initWithTextSmart(initialText);
    myFixture.performEditorAction(IdeActions.ACTION_EDITOR_BACKSPACE);
    myFixture.checkResult(expected);
  }


  protected void doTestEnter(@NotNull String fileName) {
    initWithFileSmart(fileName);
    doTestEnterNoInit();
  }

  protected void doTestEnter() {
    initWithFileSmart();
    doTestEnterNoInit();
  }

  protected void doTestEnterNoInit() {
    myFixture.type('\n');
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), getEditorTextWithCaretsAndSelections());
  }

  protected String serializePsiElement(@Nullable PsiElement element) {
    if (element == null) {
      return "null";
    }
    StringBuilder sb = new StringBuilder();
    sb.append(element);
    if (element instanceof PerlSubDefinitionElement) {
      String argumentsList = ((PerlSubDefinitionElement)element).getSubArgumentsList().stream()
        .map(this::serializeSubArgument)
        .collect(Collectors.joining(", "));
      if (StringUtil.isNotEmpty(argumentsList)) {
        sb.append("(").append(argumentsList).append(")");
      }
    }
    ASTNode node = element.getNode();
    if (node != null) {
      sb.append(" at ").append(node.getStartOffset()).append(" in ").append(element.getContainingFile().getName());
    }
    PsiElement navigationElement = element.getNavigationElement();
    if (navigationElement == null) {
      sb.append("; no navigation element;");
    }
    else if (navigationElement != element) {
      sb.append("; navigated to: ").append(serializePsiElement(navigationElement));
    }
    return sb.toString();
  }

  protected String serializeSubArgument(@NotNull PerlSubArgument argument) {
    if (argument.isOptional()) {
      return "[" + argument.toStringLong() + "]";
    }
    else {
      return argument.toStringLong();
    }
  }

  protected void doTestLightElements() {
    initWithFileSmartWithoutErrors();
    StringBuilder sb = new StringBuilder();
    getFile().accept(new PsiRecursiveElementVisitor() {
      @Override
      public void visitElement(@NotNull PsiElement element) {
        if (element instanceof PerlPolyNamedElement) {
          sb.append("Poly-named provider: ").append(serializePsiElement(element)).append("\n");
          for (PerlDelegatingLightNamedElement<?> namedElement : ((PerlPolyNamedElement<?>)element).getLightElements()) {
            sb.append("\t").append(serializePsiElement(namedElement)).append("\n");
          }
          sb.append("\n");
        }

        if (element instanceof PerlImplicitVariablesProvider) {
          sb.append("Implicit variables provider: ").append(serializePsiElement(element)).append("\n");
          for (PerlVariableDeclarationElement declarationElement : ((PerlImplicitVariablesProvider)element).getImplicitVariables()) {
            sb.append("\t").append(serializePsiElement(declarationElement)).append("\n");
          }

          sb.append("\n");
        }
        super.visitElement(element);
      }
    });

    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
  }

  protected void doTestBraceMatcher() {
    initWithFileSmartWithoutErrors();

    Editor editor = getEditor();
    EditorHighlighter highlighter = ((EditorEx)editor).getHighlighter();
    CharSequence charsSequence = editor.getDocument().getCharsSequence();
    HighlighterIterator highlighterIterator = highlighter.createIterator(0);
    int counter = 0;
    TIntHashSet matchedRights = new TIntHashSet();
    List<Pair<Integer, String>> markers = new ArrayList<>();

    while (!highlighterIterator.atEnd()) {
      FileType fileType = PsiUtilBase.getPsiFileAtOffset(getFile(), highlighterIterator.getStart()).getFileType();
      if (BraceMatchingUtil.isLBraceToken(highlighterIterator, charsSequence, fileType)) {
        markers.add(Pair.create(highlighterIterator.getStart(), "<open" + counter + ">"));
        markers.add(Pair.create(highlighterIterator.getEnd(), "</open" + counter + ">"));
        HighlighterIterator matchIterator = highlighter.createIterator(highlighterIterator.getStart());
        if (BraceMatchingUtil.matchBrace(charsSequence, fileType, matchIterator, true)) {
          markers.add(Pair.create(matchIterator.getStart(), "<close" + counter + ">"));
          markers.add(Pair.create(matchIterator.getEnd(), "</close" + counter + ">"));
          matchedRights.add(matchIterator.getStart());
        }
        counter++;
      }
      else if (!matchedRights.contains(highlighterIterator.getStart()) &&
               BraceMatchingUtil.isRBraceToken(highlighterIterator, charsSequence, fileType)) {
        markers.add(Pair.create(highlighterIterator.getStart(), "<close" + counter + ">"));
        markers.add(Pair.create(highlighterIterator.getEnd(), "</close" + counter + ">"));
      }
      highlighterIterator.advance();
    }
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), getEditorTextWithMacroses(editor, markers));
  }

  protected void doTestUsagesHighlighting() {
    initWithFileSmartWithoutErrors();

    List<Integer> caretsOffsets = getAndRemoveCarets();
    HighlightManager highlightManager = HighlightManager.getInstance(getProject());
    Editor editor = getTopLevelEditor();
    String result = "";

    for (Integer caretOffset : caretsOffsets) {
      editor.getCaretModel().moveToOffset(caretOffset);
      myFixture.testAction(new HighlightUsagesAction());
      List<RangeHighlighter> highlighters = Arrays.asList(editor.getMarkupModel().getAllHighlighters());
      ContainerUtil.sort(highlighters, Comparator.comparingInt(RangeMarker::getStartOffset));

      List<Pair<Integer, String>> macroses = new ArrayList<>();

      for (RangeHighlighter highlighter : highlighters) {
        TextAttributes attributes = highlighter.getTextAttributes(null);
        String type;
        if (attributes == myReadAttributes) {
          type = "READ";
        }
        else if (attributes == myWriteAttributes) {
          type = "WRITE";
        }
        else if (attributes == null) {
          type = "UNKNOWN";
        }
        else {
          type = attributes.toString();
        }

        macroses.add(Pair.create(highlighter.getStartOffset(), "<" + type + ">"));
        macroses.add(Pair.create(highlighter.getEndOffset(), "</" + type + ">"));
        highlightManager.removeSegmentHighlighter(editor, highlighter);
      }
      String textWithMacroses = getEditorTextWithMacroses(editor, macroses);
      if (result.isEmpty()) {
        result = textWithMacroses;
      }
      else {
        assertEquals("Caret offset: " + caretOffset, result, textWithMacroses);
      }
    }

    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), result);
  }

  protected void testFoldingRegions(@NotNull String verificationFileName, LanguageFileType fileType) {
    testFoldingRegions(verificationFileName, false, fileType);
  }

  protected void testFoldingRegions(@NotNull String verificationFileName, boolean doCheckCollapseStatus, LanguageFileType fileType) {
    String expectedContent;
    try {
      expectedContent = FileUtil.loadFile(new File(getTestDataPath() + "/" + verificationFileName + getRealDataFileExtension()));
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
    Assert.assertNotNull(expectedContent);

    expectedContent = StringUtil.replace(expectedContent, "\r", "");
    final String cleanContent = expectedContent.replaceAll(START_FOLD, "").replaceAll(END_FOLD, "");

    myFixture.configureByText(fileType, cleanContent);
    final String actual = ((CodeInsightTestFixtureImpl)myFixture).getFoldingDescription(doCheckCollapseStatus);

    Assert.assertEquals(expectedContent, actual);
  }

  protected void assertInjectable() {
    assertTrue(
      "InjectLanguageAction should be available at cursor",
      new InjectLanguageAction().isAvailable(getProject(), getEditor(), getFile())
    );
  }

  protected void assertNotInjectable() {
    assertFalse(
      "InjectLanguageAction should not be available at cursor",
      new InjectLanguageAction().isAvailable(getProject(), getEditor(), getFile())
    );
  }

  protected void assertInjected() {
    assertInstanceOf(getEditor(), EditorWindow.class);
    //noinspection UnstableApiUsage
    assertInstanceOf(getFile().getViewProvider(), InjectedFileViewProvider.class);
  }

  protected void assertNotInjected() {
    assertFalse("Editor is EditorWindow, looks like injected to me", getEditor() instanceof EditorWindow);
    //noinspection UnstableApiUsage
    assertFalse("File is injected", getFile() instanceof InjectedFileViewProvider);
  }

  @Deprecated // use initWithFileSmart
  public void initWithFileAsScript(String filename) {
    try {
      initWithFile(filename, PerlFileTypeScript.EXTENSION_PL);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


  protected @NotNull <T extends PsiElement> T getElementAtCaret(@NotNull Class<T> clazz) {
    int offset = myFixture.getEditor().getCaretModel().getOffset();
    PsiElement focused = myFixture.getFile().findElementAt(offset);
    return Objects.requireNonNull(PsiTreeUtil.getParentOfType(focused, clazz, false));
  }

  protected @NotNull <T extends PsiElement> T getTopLevelFileElementAtCaret(@NotNull Class<T> clazz) {
    int offset = getTopLevelEditor().getCaretModel().getOffset();
    var topLevelFile = getTopLevelFile();
    assertNotNull(topLevelFile);
    PsiElement focused = topLevelFile.findElementAt(offset);
    return Objects.requireNonNull(PsiTreeUtil.getParentOfType(focused, clazz, false));
  }

  protected <T extends PsiElement> T getElementAtCaretWithoutInjection(@NotNull Class<T> clazz) {
    return Objects.requireNonNull(PsiTreeUtil.getParentOfType(getElementAtCaretWithoutInjection(), clazz, false));
  }

  protected @NotNull PsiElement getElementAtCaretWithoutInjection() {
    PsiElement result = getFile().getViewProvider().findElementAt(getEditor().getCaretModel().getOffset());
    assertNotNull(result);
    PsiFile leafFile = result.getContainingFile();
    if (InjectedLanguageManager.getInstance(getProject()).isInjectedFragment(leafFile)) {
      result = leafFile.getContext();
    }
    assertNotNull(result);
    return result;
  }


  @Deprecated
  public void assertLookupDoesntContains(List<String> expected) {
    List<String> lookups = myFixture.getLookupElementStrings();
    assertNotNull(lookups);
    UsefulTestCase.assertDoesntContain(lookups, expected);
  }

  protected void doTestMassQuickFixes(@NotNull String fileName,
                                      @NotNull Class<? extends LocalInspectionTool> inspectionClass,
                                      @NotNull String quickfixNamePrefix) {
    initWithFileSmart(fileName);
    //noinspection unchecked
    myFixture.enableInspections(inspectionClass);
    myFixture.getAllQuickFixes().stream()
      .filter((it) -> it.getText().startsWith(quickfixNamePrefix))
      .forEach(myFixture::launchAction);
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), getFile().getText());
  }

  protected void doTestAnnotationQuickFix(@NotNull String fileName,
                                          @NotNull Class<? extends LocalInspectionTool> inspectionClass,
                                          @NotNull String quickFixNamePrefix) {
    initWithFileSmartWithoutErrors(fileName);
    doTestAnnotationQuickFixWithoutInitialization(inspectionClass, quickFixNamePrefix);
  }

  protected void doTestAnnotationQuickFix(@NotNull Class<? extends LocalInspectionTool> inspectionClass,
                                          @NotNull String quickFixNamePrefix) {
    initWithFileSmartWithoutErrors();
    doTestAnnotationQuickFixWithoutInitialization(inspectionClass, quickFixNamePrefix);
  }

  private void doTestAnnotationQuickFixWithoutInitialization(@NotNull Class<? extends LocalInspectionTool> inspectionClass,
                                                             @NotNull String quickFixNamePrefix) {
    //noinspection unchecked
    myFixture.enableInspections(inspectionClass);
    //myFixture.checkHighlighting(true, false, false);
    doTestIntentionWithoutLoad(quickFixNamePrefix);
  }

  protected @Nullable IntentionAction getSingleIntentionSafe(@NotNull String prefixOrName) {
    IntentionAction intention = null;
    for (IntentionAction intentionAction : myFixture.getAvailableIntentions()) {
      if (intentionAction.getText().equals(prefixOrName)) {
        intention = intentionAction;
        break;
      }
      else if (intentionAction.getText().startsWith(prefixOrName)) {
        if (intention != null) {
          fail("Clarify prefix, too many with: " + prefixOrName + " " + intention.getText() + " and " + intentionAction.getText());
        }
        intention = intentionAction;
      }
    }
    return intention;
  }

  protected @NotNull IntentionAction getSingleIntention(@NotNull String prefixOrName) {
    IntentionAction intention = getSingleIntentionSafe(prefixOrName);
    assertNotNull("Couldn't find intention: " + prefixOrName, intention);
    return intention;
  }

  protected void doTestIntention(@NotNull String intentionPrefix) {
    doTestIntention(intentionPrefix, true);
  }

  protected void doTestIntention(@NotNull String intentionPrefix, boolean checkErrors) {
    initWithFileSmartWithoutErrors();
    doTestIntentionWithoutLoad(intentionPrefix, checkErrors);
  }

  protected void doTestNoIntention(@NotNull String intentionPrefix) {
    initWithFileSmart();
    assertNull(getSingleIntentionSafe(intentionPrefix));
  }

  private void doTestIntentionWithoutLoad(@NotNull String intentionPrefix) {
    doTestIntentionWithoutLoad(intentionPrefix, true);
  }

  private void doTestIntentionWithoutLoad(@NotNull String intentionPrefix, boolean checkErrors) {
    myFixture.launchAction(getSingleIntention(intentionPrefix));
    if (checkErrors) {
      assertNoErrorElements();
    }
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), getFile().getText());
  }

  protected void doTestStructureView() {
    initWithFileSmartWithoutErrors();
    PsiFile psiFile = getFile();
    final VirtualFile virtualFile = psiFile.getVirtualFile();
    final FileEditor fileEditor = FileEditorManager.getInstance(getProject()).getSelectedEditor(virtualFile);
    final StructureViewBuilder builder = LanguageStructureViewBuilder.INSTANCE.getStructureViewBuilder(psiFile);
    assertNotNull(builder);

    StructureView structureView = builder.createStructureView(fileEditor, getProject());
    assertNotNull(structureView);
    addTearDownListener(structureView);

    StructureViewModel structureViewModel = structureView.getTreeModel();
    StringBuilder sb = new StringBuilder();
    serializeTree(structureViewModel.getRoot(), structureViewModel, sb, null, "", new THashSet<>());
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
  }

  private void serializeTree(@NotNull StructureViewTreeElement currentElement,
                             @NotNull StructureViewModel structureViewModel,
                             @NotNull StringBuilder sb,
                             @Nullable Collection<Pair<Grouper, Group>> groups,
                             @NotNull String prefix,
                             @NotNull Set<Object> recursionSet
  ) {
    Object value = currentElement.getValue();
    sb.append(prefix);
    ItemPresentation presentation = currentElement.getPresentation();
    sb.append(serializePresentation(presentation));

    if (value instanceof PsiElement) {
      sb.append(" -> ").append(serializePsiElement((PsiElement)value));
    }
    sb.append("\n");

    // filters
    if (structureViewModel.getFilters().length > 0) {
      List<String> passedFilters = new ArrayList<>();
      for (Filter filter : structureViewModel.getFilters()) {
        if (filter.isVisible(currentElement)) {
          passedFilters.add(filter.getName());
        }
      }

      if (!passedFilters.isEmpty()) {
        sb.append(prefix).append("Passed by filters: ").append(StringUtil.join(passedFilters, ", ")).append("\n");
      }
    }


    // groups
    if (groups != null) {
      for (Pair<Grouper, Group> groupPair : groups) {
        Grouper grouper = groupPair.first;
        Group group = groupPair.second;
        ItemPresentation groupPresentation = group.getPresentation();
        sb.append(prefix)
          .append("Grouped by: '").append(grouper.getPresentation().getText()).append("' (").append(grouper.getName()).append(") to ")
          .append(groupPresentation.getPresentableText()).append(" in ").append(groupPresentation.getLocationString())
          .append("\n");
      }
    }


    if (recursionSet.add(value)) {
      sb.append("\n");
      TreeElement[] children = currentElement.getChildren();

      MultiMap<TreeElement, Pair<Grouper, Group>> groupingResults = new MultiMap<>();
      for (Grouper grouper : structureViewModel.getGroupers()) {
        for (Group group : grouper
          .group(new TreeElementWrapper(getProject(), currentElement, structureViewModel), Arrays.asList(children))) {
          for (TreeElement element : group.getChildren()) {
            groupingResults.putValue(element, Pair.create(grouper, group));
          }
        }
      }

      for (TreeElement childElement : children) {
        assertInstanceOf(childElement, StructureViewTreeElement.class);
        serializeTree(
          (StructureViewTreeElement)childElement,
          structureViewModel,
          sb,
          groupingResults.get(childElement),
          prefix + "  ",
          new THashSet<>(recursionSet)
        );
      }
    }
    else {
      sb.append("(recursion)").append("\n\n");
    }
  }

  protected @NotNull String serializePresentation(@Nullable ItemPresentation presentation) {
    if (presentation == null) {
      return "null";
    }
    String locationString = presentation.getLocationString();
    StringBuilder sb = new StringBuilder();
    sb.append(presentation.getPresentableText())
      .append(" in ")
      .append(locationString == null ? null : locationString.replaceAll("\\\\", "/"))
      .append("; ")
      .append(getIconText(presentation.getIcon(true)));

    if (presentation instanceof PerlItemPresentationBase) {
      sb.append("; ").append(((PerlItemPresentationBase)presentation).getTextAttributesKey());
    }
    return sb.toString();
  }

  protected void doTestTypeHierarchy() {
    initWithFileSmartWithoutErrors();
    Editor editor = getEditor();
    Object psiElement = FileEditorManager.getInstance(getProject())
      .getData(CommonDataKeys.PSI_ELEMENT.getName(), editor, editor.getCaretModel().getCurrentCaret());
    MapDataContext dataContext = new MapDataContext();
    dataContext.put(CommonDataKeys.PROJECT, getProject());
    dataContext.put(CommonDataKeys.EDITOR, getEditor());
    dataContext.put(CommonDataKeys.PSI_ELEMENT, (PsiElement)psiElement);
    dataContext.put(CommonDataKeys.PSI_FILE, getFile());

    HierarchyProvider hierarchyProvider =
      BrowseHierarchyActionBase.findProvider(LanguageTypeHierarchy.INSTANCE, (PsiElement)psiElement, getFile(), dataContext);
    assertNotNull(hierarchyProvider);
    StringBuilder sb = new StringBuilder();
    sb.append("Provider: ").append(hierarchyProvider.getClass().getSimpleName()).append("\n");
    PsiElement target = hierarchyProvider.getTarget(dataContext);
    assertNotNull(target);
    sb.append("Target: ").append(target).append("\n");
    HierarchyBrowser browser = hierarchyProvider.createHierarchyBrowser(target);
    sb.append("Browser: ").append(browser.getClass().getSimpleName()).append("\n");
    assertInstanceOf(browser, TypeHierarchyBrowserBase.class);

    try {
      Field myType2Sheet = HierarchyBrowserBaseEx.class.getDeclaredField("myType2Sheet");
      myType2Sheet.setAccessible(true);
      Method createHierarchyTreeStructure =
        browser.getClass().getDeclaredMethod("createHierarchyTreeStructure", String.class, PsiElement.class);
      createHierarchyTreeStructure.setAccessible(true);
      Method getContentDisplayName = browser.getClass().getDeclaredMethod("getContentDisplayName", String.class, PsiElement.class);
      getContentDisplayName.setAccessible(true);
      Method getElementFromDescriptor = browser.getClass().getDeclaredMethod("getElementFromDescriptor", HierarchyNodeDescriptor.class);
      getElementFromDescriptor.setAccessible(true);

      Map<String, ?> subTrees = (Map<String, ?>)myType2Sheet.get(browser);
      List<String> treesNames = new ArrayList<>(subTrees.keySet());
      ContainerUtil.sort(treesNames);
      for (String treeName : treesNames) {
        sb.append(SEPARATOR_NEW_LINE_AFTER)
          .append("Tree: ").append(getContentDisplayName.invoke(browser, treeName, target)).append("\n");
        HierarchyTreeStructure structure = (HierarchyTreeStructure)createHierarchyTreeStructure.invoke(browser, treeName, target);
        if (structure == null) {
          sb.append("none\n");
          continue;
        }
        serializeTreeStructure(structure,
                               (HierarchyNodeDescriptor)structure.getRootElement(),
                               node -> {
                                 try {
                                   return (PsiElement)getElementFromDescriptor.invoke(browser, node);
                                 }
                                 catch (Exception e) {
                                   throw new RuntimeException(e);
                                 }
                               },
                               sb,
                               "",
                               new THashSet<>());
      }
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
  }

  private void serializeTreeStructure(@NotNull HierarchyTreeStructure treeStructure,
                                      @Nullable HierarchyNodeDescriptor currentElement,
                                      @NotNull Function<HierarchyNodeDescriptor, PsiElement> elementProvider,
                                      @NotNull StringBuilder sb,
                                      @NotNull String prefix,
                                      @NotNull Set<HierarchyNodeDescriptor> recursionSet
  ) {
    if (currentElement == null) {
      return;
    }
    PsiElement psiElement = elementProvider.fun(currentElement);
    if (!recursionSet.add(currentElement)) {
      sb.append(prefix).append("Recursion to: ").append(serializePsiElement(psiElement)).append("\n");
      return;
    }
    sb.append(prefix).append(serializePsiElement(psiElement)).append("\n");
    for (Object object : treeStructure.getChildElements(currentElement)) {
      assertInstanceOf(object, HierarchyNodeDescriptor.class);
      serializeTreeStructure(treeStructure, (HierarchyNodeDescriptor)object, elementProvider, sb, prefix + "    ",
                             new THashSet<>(recursionSet));
    }
  }


  protected void doAnnotatorTest() {
    initWithFileSmart();
    addVirtualFileFilter();
    myFixture.checkHighlighting(true, true, true);
    removeVirtualFileFilter();
  }


  protected void doInspectionTest(@NotNull Class<? extends LocalInspectionTool>... clazz) {
    initWithFileSmart();
    doInspectionTestWithoutInit(clazz);
  }

  protected void doInspectionTestWithoutInit(@NotNull Class<? extends LocalInspectionTool>... clazz) {
    addVirtualFileFilter();
    for (Class<? extends LocalInspectionTool> aClass : clazz) {
      //noinspection unchecked
      myFixture.enableInspections(aClass);
    }
    myFixture.checkHighlighting(true, false, false);
    removeVirtualFileFilter();
  }


  protected void doTestPackageProcessorDescriptors() {
    initWithFileSmartWithoutErrors();
    PsiElement elementAtCaret = getElementAtCaretWithoutInjection();
    assertNotNull(elementAtCaret);
    PerlNamespaceDefinitionElement namespace = PerlPackageUtil.getNamespaceContainerForElement(elementAtCaret);
    assertNotNull(namespace);
    StringBuilder sb = new StringBuilder();

    appendDescriptors("Scalars", sb, namespace.getImportedScalarDescriptors());
    appendDescriptors("Arrays", sb, namespace.getImportedArrayDescriptors());
    appendDescriptors("Hashes", sb, namespace.getImportedHashDescriptors());
    appendDescriptors("Subs", sb, namespace.getImportedSubsDescriptors());

    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
  }

  private void appendDescriptors(@NotNull String sectionLabel,
                                 @NotNull StringBuilder sb,
                                 @NotNull List<PerlExportDescriptor> exportDescriptors) {
    if (exportDescriptors.isEmpty()) {
      return;
    }
    sb.append(sectionLabel).append(":\n");
    for (PerlExportDescriptor descriptor : exportDescriptors) {
      sb.append("    ")
        .append(descriptor.getImportedName())
        .append(" => ")
        .append(descriptor.getTargetCanonicalName())
        .append("\n");
    }
    sb.append("\n");
  }

  protected void doElementDescriptionTest() {
    initWithFileSmartWithoutErrors();
    doElementDescriptionCheck();
  }

  protected void doElementDescriptionTest(@NotNull String content) {
    initWithTextSmart(content);
    assertNoErrorElements();
    doElementDescriptionCheck();
  }

  private void doElementDescriptionCheck() {
    PsiElement elementAtCaret = myFixture.getElementAtCaret();
    assertNotNull(elementAtCaret);
    StringBuilder actualDump = new StringBuilder();
    LOCATIONS.forEach(
      location -> {
        String actual = ElementDescriptionUtil.getElementDescription(elementAtCaret, location);
        String locationName = location.getClass().getSimpleName();
        actualDump.append(locationName)
          .append(": ")
          .append(actual)
          .append("\n");
      }
    );
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), actualDump.toString());
  }

  protected final void doTestControlFlow() {
    doTestControlFlow(true);
  }

  protected final void doTestControlFlow(boolean checkErrors) {
    if (checkErrors) {
      initWithFileSmartWithoutErrors();
    }
    else {
      initWithFileSmart();
    }
    PsiElement psiElement = getFile().findElementAt(getEditor().getCaretModel().getOffset());
    assertNotNull(psiElement);
    PsiElement controlFlowScope = PerlControlFlowBuilder.getControlFlowScope(psiElement);
    assertNotNull(controlFlowScope);
    Instruction[] controlFlow = PerlControlFlowBuilder.getFor(controlFlowScope);
    final String stringifiedControlFlow = Arrays.stream(controlFlow)
      .map(it -> it.toString() + " (" + it.getClass().getSimpleName() + ")")
      .collect(Collectors.joining("\n"));

    if (ENABLE_SVG_GENERATION) {
      try {
        String svgDataPath = getSvgDataPath();
        FileUtil.createParentDirs(new File(svgDataPath));
        saveSvgFile(svgDataPath, controlFlow);
      }
      catch (Exception e) {
        LOG.error(e);
      }
    }

    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), stringifiedControlFlow);
  }

  private @NotNull String getSvgDataPath() {
    return FileUtil.join(getTestDataPath(), "svg", getTestName(true) + ".svg");
  }

  private void saveSvgFile(final @NotNull String outSvgFile, final @NotNull Instruction[] controlFlow)
    throws IOException, ExecutionException {
    String dotUtilName = SystemInfoRt.isUnix ? "dot" : "dot.exe";
    File dotFullPath = PathEnvironmentVariableUtil.findInPath(dotUtilName);
    if (dotFullPath == null) {
      throw new FileNotFoundException("Cannot find dot utility in path");
    }
    File tmpFile = FileUtil.createTempFile("control-flow", ".dot", true);
    String controlFlowGraph = convertControlFlowToDot(controlFlow);
    FileUtil.writeToFile(tmpFile, controlFlowGraph);
    GeneralCommandLine commandLine = new GeneralCommandLine(dotFullPath.getAbsolutePath())
      .withInput(tmpFile.getAbsoluteFile())
      .withParameters("-Tsvg", "-o" + outSvgFile, tmpFile.getAbsolutePath())
      .withRedirectErrorStream(true);
    ProcessOutput output = ExecUtil.execAndGetOutput(commandLine);
    assertNotNull(output);
    if (output.getExitCode() != 0) {
      fail("Error: " + output.getStderr() + "\n" +
           "Output: " + output.getStdout() + "\n" +
           "Exit code: " + output.getExitCode() + "\n" +
           "Command line: " + commandLine);
    }
  }

  private @NotNull String convertControlFlowToDot(final @NotNull Instruction[] controlFlow) {
    StringBuilder builder = new StringBuilder();
    builder.append("digraph {");
    for (Instruction instruction : controlFlow) {
      printInstruction(builder, instruction);

      if (instruction instanceof PerlIteratorConditionInstruction) {
      }
      else if (instruction instanceof PartialConditionalInstructionImpl) {
        builder.append("\n").append("Its ").append(((PartialConditionalInstructionImpl)instruction).getResult()).
          append(" branch, condition: ").append(((PartialConditionalInstructionImpl)instruction).getConditionText());
      }
      else if (instruction instanceof ConditionalInstruction) {
        ConditionalInstruction conditionalInstruction = (ConditionalInstruction)instruction;
        builder.append("\n").append("Its ").append(conditionalInstruction.getResult()).
          append(" branch, condition: ").append(getTextSafe(conditionalInstruction.getCondition()));
      }
      builder.append(PerlPsiUtil.DOUBLE_QUOTE).append("]");

      builder.append(System.lineSeparator());
      if (instruction.allPred().isEmpty()) {
        builder.append("Entry -> Instruction").append(instruction.num()).append(System.lineSeparator());
      }
      if (instruction.allSucc().isEmpty()) {
        builder.append("Instruction").append(instruction.num()).append(" -> Exit").append(System.lineSeparator());
      }
      for (Instruction succ : instruction.allSucc()) {
        builder.append("Instruction").append(instruction.num()).append(" -> ")
          .append("Instruction").append(succ.num()).append(System.lineSeparator());
      }
    }
    builder.append("}");
    return builder.toString();
  }

  private void printInstruction(StringBuilder builder, Instruction instruction) {
    PsiElement element = instruction.getElement();
    Class<? extends Instruction> instructionClass = instruction.getClass();

    builder.append("Instruction").append(instruction.num()).append("[font=\"Courier\", label=\"")
      .append(getInstructionText(instruction))
      .append(" \\n(").append(instruction.num()).append(")[")
      .append(element != null ? element.getClass().getSimpleName() : "null").append("]")
      .append(System.lineSeparator())
      .append("{")
      .append(instructionClass.getSimpleName().isEmpty()
              ? instructionClass.getSuperclass().getSimpleName()
              : instructionClass.getSimpleName())
      .append("}");
  }

  private String getInstructionText(@NotNull Instruction instruction) {
    return escape(doGetInstructionText(instruction));
  }

  private String doGetInstructionText(@NotNull Instruction instruction) {
    if (instruction instanceof PerlAssignInstruction) {
      PerlAssignInstruction assignInstruction = (PerlAssignInstruction)instruction;
      PerlAssignExpression.PerlAssignValueDescriptor rightSide = assignInstruction.getRightSide();
      return assignInstruction.getLeftSide().getText() + " "
             + assignInstruction.getOperation().getText() + " "
             + rightSide.getText() + (rightSide.getStartIndex() == 0 ? "" : " [" + rightSide.getStartIndex() + "]");
    }
    else if (instruction instanceof PerlIterateInstruction) {
      return "iterate " +
             getTextSafe(((PerlIterateInstruction)instruction).getSourceElement()) +
             " using " +
             getTextSafe(((PerlIterateInstruction)instruction).getTargetElement());
    }
    else {
      PsiElement element = instruction.getElement();
      if (element == null) {
        return "";
      }
      String elementText = element.getText();
      if (StringUtil.isEmpty(elementText)) {
        return element instanceof PsiNamedElement ? ((PsiNamedElement)element).getName() : "empty text, unnamed";
      }
      return elementText;
    }
  }

  private String getTextSafe(@Nullable PsiElement element) {
    return element == null ? "null" : element.getText();
  }

  private String escape(String text) {
    return StringUtil.replace(StringUtil.escapeChars(text, '"'), "\n", "\\n");
  }

  protected void doLineCommenterTest() {
    initWithFileSmart();
    MultiCaretCodeInsightAction action = (MultiCaretCodeInsightAction)ActionManager.getInstance().getAction(IdeActions.ACTION_COMMENT_LINE);
    action.actionPerformedImpl(getProject(), myFixture.getEditor());
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), getEditorTextWithCaretsAndSelections());
  }

  protected void doTestConsoleFilter(@NotNull com.intellij.execution.filters.Filter filter) {
    initWithFileSmart();
    Document document = getEditor().getDocument();
    int lines = document.getLineCount();

    String documentText = document.getText();

    StringBuilder sb = new StringBuilder();
    for (int lineNumber = 0; lineNumber < lines; lineNumber++) {
      int lineStart = document.getLineStartOffset(lineNumber);

      String lineText = EditorHyperlinkSupport.getLineText(document, lineNumber, true);
      int endOffset = lineStart + lineText.length();
      com.intellij.execution.filters.Filter.Result result = filter.applyFilter(lineText, endOffset);
      if (result == null) {
        continue;
      }
      for (com.intellij.execution.filters.Filter.ResultItem resultItem : result.getResultItems()) {
        int linkStartOffset = resultItem.getHighlightStartOffset();
        int linkEndOffset = resultItem.getHighlightEndOffset();
        sb.append(linkStartOffset)
          .append(" - ")
          .append(linkEndOffset)
          .append("; ")
          .append('[')
          .append(documentText, linkStartOffset, linkEndOffset)
          .append(']')
          .append(" => ")
          .append(resultItem.getHyperlinkInfo())
          .append("\n");
      }
    }

    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
  }

  protected void doBreadCrumbsTest() {
    initWithFileSmartWithoutErrors();
    CaretModel caretModel = getEditor().getCaretModel();
    List<Pair<Integer, String>> macros = new ArrayList<>();
    List<Integer> carets = getAndRemoveCarets();
    carets.forEach(caretOffset -> {
      caretModel.moveToOffset(caretOffset);
      macros.add(Pair.create(caretOffset,
                             "<" +
                             StringUtil.join(ContainerUtil.map(myFixture.getBreadcrumbsAtCaret(), this::serializeCrumb), ": ") +
                             ">"));
    });
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), getEditorTextWithMacroses(getTopLevelEditor(), macros));
  }

  private String serializeCrumb(@Nullable Crumb crumb) {
    if (crumb == null) {
      return "EMPTY";
    }
    String tooltip = crumb.getTooltip();
    return "[" +
           crumb.getText() +
           (tooltip == null ? "" : "(" + tooltip + ")") +
           ", " + getIconText(crumb.getIcon()) + "]";
  }

  protected void doTestUsagesGrouping() {
    initWithFileSmartWithoutErrors();
    PsiElement targetElement = TargetElementUtil
      .findTargetElement(getEditor(), TargetElementUtil.ELEMENT_NAME_ACCEPTED | TargetElementUtil.REFERENCED_ELEMENT_ACCEPTED);
    assertNotNull(targetElement);
    PsiElement[] targetElements = {targetElement};
    Collection<UsageInfo> usages = myFixture.findUsages(targetElement);
    List<UsageGroupingRule> rules = getActiveGroupingRules(new UsageViewSettings(true, true, true, true, true, false));
    StringBuilder sb = new StringBuilder();
    usages.forEach(usageInfo -> {
      PsiElement element = Objects.requireNonNull(usageInfo.getElement());
      Usage usage = UsageInfoToUsageConverter.convert(targetElements, usageInfo);
      sb.append("Usage: ")
        .append('"').append(element.getText()).append("\"; ")
        .append(element.getTextRange()).append("; ");

      if (usage instanceof ReadWriteAccessUsageInfo2UsageAdapter) {
        if (((ReadWriteAccessUsageInfo2UsageAdapter)usage).isAccessedForReading()) {
          sb.append("reading; ");
        }
        if (((ReadWriteAccessUsageInfo2UsageAdapter)usage).isAccessedForWriting()) {
          sb.append("writing; ");
        }
      }
      sb.append(serializePsiElement(element)).append("\n");

      rules.forEach(rule -> {
        String groups = getUsageGroups(usage, rule);
        if (!groups.isEmpty()) {
          sb.append(" - Rule: ").append(rule.getClass().getSimpleName()).append("\n").append(groups).append("\n\n");
        }
      });

      sb.append("\n");
    });
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
  }

  private @NotNull String getUsageGroups(@NotNull Usage usage, @NotNull UsageGroupingRule groupingRule) {
    StringBuilder sb = new StringBuilder();
    groupingRule.getParentGroupsFor(usage, UsageTarget.EMPTY_ARRAY).forEach(usageGroup -> {
      if (sb.length() > 0) {
        sb.append("\n");
      }
      sb.append("    ").append(serializeUsageGroup(usageGroup));
    });
    return sb.toString();
  }

  private @NotNull String serializeUsageGroup(@NotNull UsageGroup usageGroup) {
    if (usageGroup instanceof PsiElementUsageGroupBase) {
      return "PsiElement: " + serializePsiElement(((PsiElementUsageGroupBase<?>)usageGroup).getElement());
    }
    return usageGroup.getClass().getSimpleName() + ": " + usageGroup.getText(null) + "; " + getIconText(usageGroup.getIcon(true));
  }

  /**
   * Copy-paste of {@link UsageViewImpl#getActiveGroupingRules(com.intellij.openapi.project.Project, com.intellij.usages.UsageViewSettings)}
   */
  private @NotNull List<UsageGroupingRule> getActiveGroupingRules(@NotNull UsageViewSettings usageViewSettings) {
    final List<UsageGroupingRuleProvider> providers = UsageGroupingRuleProvider.EP_NAME.getExtensionList();
    List<UsageGroupingRule> list = new ArrayList<>(providers.size());
    for (UsageGroupingRuleProvider provider : providers) {
      ContainerUtil.addAll(list, provider.getActiveRules(getProject(), usageViewSettings));
    }

    list.sort(Comparator.comparingInt(UsageGroupingRule::getRank));
    return list;
  }

  protected void doTestIntroduceVariable() {
    initWithFileSmartWithoutErrors();
    Editor editor = getEditor();

    TemplateManagerImpl.setTemplateTesting(getTestRootDisposable());
    if (editor instanceof EditorWindow) {
      editor = ((EditorWindow)editor).getDelegate();
    }
    new PerlIntroduceVariableHandler().invoke(getProject(), editor, getFile(), null);
    assertNoErrorElements();

    String beforeRename = getEditorTextWithCaretsAndSelections();

    TemplateState state = TemplateManagerImpl.getTemplateState(editor);
    if (state != null) {
      final TextRange range = state.getCurrentVariableRange();
      assert range != null;
      final Editor finalEditor = editor;
      WriteCommandAction.writeCommandAction(getProject())
        .run(() -> finalEditor.getDocument().replaceString(range.getStartOffset(), range.getEndOffset(), "test_name"));

      state = TemplateManagerImpl.getTemplateState(editor);
      assert state != null;
      state.gotoEnd(false);

      assertNoErrorElements();
    }
    String afterRename = getEditorTextWithCaretsAndSelections();
    UsefulTestCase.assertSameLinesWithFile(
      getTestResultsFilePath(), beforeRename + "\n================ AFTER RENAME =================\n" + afterRename);
  }

  protected void doTestIntroduceVariableTargets(boolean checkErrors) {
    if (checkErrors) {
      initWithFileSmartWithoutErrors();
    }
    else {
      initWithFileSmart();
    }
    List<PerlIntroduceTarget> introduceTargets = PerlIntroduceTargetsHandler.getIntroduceTargets(getEditor(), getFile());
    StringBuilder sb = new StringBuilder();

    introduceTargets.forEach(it -> sb.append(serializePsiElement(it.getPlace()))
      .append("\n")
      .append("    ").append(it.getTextRangeInElement())
      .append("\n")
      .append("    '").append(it.render()).append("'")
      .append("\n"));

    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
  }

  protected void doTestIntroduceVariableOccurances() {
    initWithFileSmartWithoutErrors();
    List<PerlIntroduceTarget> introduceTargets = PerlIntroduceTargetsHandler.getIntroduceTargets(getEditor(), getFile());
    assertTrue(introduceTargets.size() > 0);
    List<Pair<Integer, String>> macros = new ArrayList<>();
    PerlIntroduceTargetOccurrencesCollector.collect(introduceTargets.get(introduceTargets.size() - 1)).forEach(it -> {
      TextRange occurenceRange = it.getTextRange();
      macros.add(Pair.create(occurenceRange.getStartOffset(), "<occurrence>"));
      macros.add(Pair.create(occurenceRange.getEndOffset(), "</occurrence>"));
    });
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), getEditorTextWithMacroses(getTopLevelEditor(), macros));
  }

  protected void doTestSuggesterOnRename(@NotNull VariableInplaceRenameHandler handler) {
    initWithFileSmartWithoutErrors();
    TemplateManagerImpl.setTemplateTesting(getTestRootDisposable());
    Editor editor = getEditor();
    if (editor instanceof EditorWindow) {
      editor = ((EditorWindow)editor).getDelegate();
    }
    handler.doRename(myFixture.getElementAtCaret(), editor, getEditorDataContext());
    List<String> names = myFixture.getLookupElementStrings();
    assertNotNull(names);
    TemplateState state = TemplateManagerImpl.getTemplateState(editor);
    assertNotNull(state);
    state.gotoEnd(false);
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), StringUtil.join(names, "\n"));
  }

  protected void doTestIntroduceVariableNamesSuggester() {
    initWithFileSmartWithoutErrors();
    Editor editor = getEditor();

    TemplateManagerImpl.setTemplateTesting(getTestRootDisposable());
    if (editor instanceof EditorWindow) {
      editor = ((EditorWindow)editor).getDelegate();
    }
    new PerlIntroduceVariableHandler().invoke(getProject(), editor, getFile(), null);
    assertNoErrorElements();
    List<String> names = myFixture.getLookupElementStrings();
    assertNotNull(names);

    TemplateState state = TemplateManagerImpl.getTemplateState(editor);
    assertNotNull(state);
    TextRange currentVariableRange = state.getCurrentVariableRange();
    assertNotNull(currentVariableRange);
    String selectedItem = currentVariableRange.substring(getEditorText());

    state.gotoEnd(false);
    UsefulTestCase.assertSameLinesWithFile(
      getTestResultsFilePath(),
      StringUtil.join(ContainerUtil.map(names, it -> Objects.equals(it, selectedItem) ? "> " + it : it), "\n"));
  }

  protected void doTestQuickDoc() {
    initWithFileSmartWithoutErrors();
    doTestQuickDocWithoutInit();
  }

  /**
   * @return a builtin keyword for from the test name. E.g: {@code testScalar} => {@code scalar}, {@code testFiletestx} => {@code -x}
   */
  protected @NotNull String getBuiltInFromTestName() {
    String name = getTestName(true);
    return isFileTestTest() ? "-" + name.substring(name.length() - 1) : name;
  }

  protected boolean isFileTestTest() {
    return getTestName(true).startsWith("filetest");
  }

  protected void doTestCompletionQuickDoc(@NotNull String elementPattern) {
    myFixture.complete(getCompletionType(), getCompletionInvocationCount());
    LookupElement[] elements = myFixture.getLookupElements();
    assertNotNull("No lookup elements", elements);
    LookupElement targetElement = getMostRelevantLookupElement(elementPattern, elements);
    assertNotNull("Unable to find lookup " + elementPattern, targetElement);
    Object elementObject = targetElement.getObject();
    int offset = getEditor().getCaretModel().getOffset();
    PsiElement elementAtCaret = getFile().findElementAt(offset);
    PsiElement providerSource = ObjectUtils.notNull(elementAtCaret, getFile());
    DocumentationProvider documentationProvider = DocumentationManager.getProviderFromElement(providerSource);
    assertNotNull("Unable to find documentation provider for " + providerSource, documentationProvider);
    PsiElement documentationElement =
      documentationProvider.getDocumentationElementForLookupItem(PsiManager.getInstance(getProject()), elementObject, elementAtCaret);
    if (documentationElement == null) {
      LOG.warn("No documentation element found for " + elementObject);
    }
    String generatedDoc = documentationElement == null ? "": StringUtil.notNullize(documentationProvider.generateDoc(documentationElement, elementAtCaret));
    assertNotNull("No document generated for " + documentationElement, generatedDoc);
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), generatedDoc);
  }

  private @Nullable LookupElement getMostRelevantLookupElement(@NotNull String pattern, @NotNull LookupElement[] lookupElements) {
    LookupElement mostRelevantElement = null;
    int extraSymbols = -1;

    for (LookupElement lookupElement : lookupElements) {
      String lookupString = lookupElement.getLookupString();
      if (lookupString.equals(pattern)) {
        return lookupElement;
      }
      else if (lookupString.contains(pattern) && (extraSymbols < 0 || extraSymbols > lookupString.length() - pattern.length())) {
        mostRelevantElement = lookupElement;
        extraSymbols = lookupString.length() - pattern.length();
      }
    }
    return mostRelevantElement;
  }

  private static final Pattern LINK_PATTERN = Pattern.compile("<a href=\"psi_element://([^\"]+?)\"[^>]*>(.+?)</a>");

  protected void doTestQuickDocWithoutInit() {
    List<Integer> caretsOffsets = getAndRemoveCarets();
    CaretModel caretModel = getTopLevelEditor().getCaretModel();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < caretsOffsets.size(); i++) {
      Integer caretOffset = caretsOffsets.get(i);
      if (caretsOffsets.size() > 1) {
        sb.append(SEPARATOR).append("Caret #").append(i).append(" at: ").append(caretOffset)
          .append(SEPARATOR_NEW_LINE_AFTER);
      }
      caretModel.moveToOffset(caretOffset);
      Pair<PsiElement, String> targetAndDocumentation = getContextAndDocumentationAtCaret();
      sb.append(targetAndDocumentation.second).append("\n");
    }
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
  }

  protected void doTestPerlValue() {
    initWithFileSmart(getTestName(true));
    doTestPerlValueWithoutInit();
  }

  protected void doTestPerlValueWithoutInit(){
    addVirtualFileFilter();
    doTestPerlValueWithoutInit(getElementAtCaret(PerlValuableEntity.class));
  }

  protected void doTestPerlValueWithoutInit(@NotNull PerlValuableEntity element) {
    StringBuilder sb = new StringBuilder();
    PerlValue elementValue = PerlValuesManager.from(element);
    sb.append(getEditorTextWithCaretsAndSelections().trim())
      .append(SEPARATOR_NEWLINES)
      .append(element.getText()).append("\n")
      .append(serializePsiElement(element)).append("\n")
      .append(elementValue.getPresentableText());

    sb.append(SEPARATOR_NEWLINES).append("Resolved").append(SEPARATOR_NEWLINES).append(elementValue.resolve(element).getPresentableText());

    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
  }

  protected void doTestReturnValue() {
    initWithFileSmart(getTestName(true));
    addVirtualFileFilter();
    PsiElement element = TargetElementUtil.getInstance().getNamedElement(getFile().findElementAt(getEditor().getCaretModel().getOffset()), 0);
    if( element == null){
      element = getElementAtCaret(PerlSubElement.class);
    }
    assertNotNull(element);
    assertInstanceOf(element, PerlSubElement.class);
    StringBuilder sb = new StringBuilder();
    PerlValue returnValue = ((PerlSubElement)element).getReturnValue();
    sb.append(getEditorTextWithCaretsAndSelections().trim())
      .append(SEPARATOR_NEWLINES)
      .append(returnValue);

    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
  }

  protected void doTestGoToByModel(@NotNull FilteringGotoByModel<?> model) {
    doTestGoToByModel(model, it -> true);
  }

  protected void doTestGoToByModel(@NotNull FilteringGotoByModel<?> model, @NotNull String name) {
    doTestGoToByModel(model, name::equals);
  }

  protected void doTestGoToByModel(@NotNull FilteringGotoByModel<?> model, @NotNull String... names) {
    doTestGoToByModel(model, it -> ArrayUtil.contains(names, it));
  }

  protected void doTestGoToByModel(@NotNull FilteringGotoByModel<?> model, @NotNull Condition<String> nameFilter) {
    doTestGoToByModel(model, false, nameFilter);
  }

  protected FilteringGotoByModel<?> getGoToClassModel() {
    return new GotoClassModel2(getProject());
  }

  protected FilteringGotoByModel<?> getGoToSymbolModel() {
    return new GotoSymbolModel2(getProject());
  }

  protected FilteringGotoByModel<?> getGoToFileModel() {
    return new GotoFileModel(getProject());
  }

  protected void doTestGoToByModel(@NotNull FilteringGotoByModel<?> model,
                                   boolean includeNonProjectFiles,
                                   @NotNull Condition<String> nameFilter) {
    CodeInsightTestFixtureImpl.ensureIndexesUpToDate(getProject());
    List<String> acceptableNames = ContainerUtil.filter(model.getNames(includeNonProjectFiles), nameFilter);
    ContainerUtil.sort(acceptableNames);
    StringBuilder sb = new StringBuilder();
    for (String acceptableName : acceptableNames) {
      Object[] elements = model.getElementsByName(acceptableName, includeNonProjectFiles, acceptableName);
      if (elements.length == 0) {
        continue;
      }
      if (sb.length() > 0) {
        sb.append(SEPARATOR_NEWLINES);
      }
      sb.append(acceptableName).append("\n");
      List<String> itemsResult = new ArrayList<>();
      for (Object element : elements) {
        if (element instanceof PsiDirectory) {
          itemsResult.add("PsiDirectory");
        }
        else if (element instanceof NavigationItem) {
          itemsResult.add(serializePresentation(((NavigationItem)element).getPresentation()));
        }
        else {
          itemsResult.add(element.toString());
        }
      }
      ContainerUtil.sort(itemsResult);
      sb.append(StringUtil.join(itemsResult, "\n"));
    }

    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
  }

  protected void doTestHighlighter(boolean checkErrors) {
    if (checkErrors) {
      initWithFileSmartWithoutErrors();
    }
    else {
      initWithFileSmart();
    }
    doTestHighlighterWithoutInit();
  }

  protected void doTestHighlighterWithoutInit() {
    EditorHighlighter editorHighlighter = ((EditorEx)getEditor()).getHighlighter();
    assertInstanceOf(editorHighlighter, LexerEditorHighlighter.class);
    HighlighterIterator highlighterIterator = editorHighlighter.createIterator(0);

    String documentText = getEditor().getDocument().getText();
    StringBuilder sb = new StringBuilder();
    while (!highlighterIterator.atEnd()) {
      SyntaxHighlighter activeSyntaxHighlighter =
        highlighterIterator instanceof LayeredHighlighterIterator ?
        ((LayeredHighlighterIterator)highlighterIterator).getActiveSyntaxHighlighter() :
        ((LexerEditorHighlighter)editorHighlighter).getSyntaxHighlighter();
      TextAttributesKey[] highlights = activeSyntaxHighlighter.getTokenHighlights(highlighterIterator.getTokenType());
      if (highlights.length > 0) {
        if (sb.length() > 0) {
          sb.append("\n");
        }
        sb.append(documentText, highlighterIterator.getStart(), highlighterIterator.getEnd())
          .append("\n");
        List<String> attrNames = new SmartList<>();
        for (TextAttributesKey attributesKey : highlights) {
          attrNames.add("    " + serializeTextAttributeKey(attributesKey));
        }
        sb.append(StringUtil.join(attrNames, "\n"));
      }
      highlighterIterator.advance();
    }
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
  }

  protected void doTestHighlighterRestart() {
    initWithFileSmart();
    doTestHighlighterRestartWithoutInit();
  }

  protected void doTestHighlighterRestartWithoutInit() {
    EditorHighlighter editorHighlighter = ((EditorEx)getEditor()).getHighlighter();
    assertInstanceOf(editorHighlighter, LexerEditorHighlighter.class);
    SegmentArrayWithData segments = ((LexerEditorHighlighter)editorHighlighter).getSegments();
    assertNotNull(segments);
    CharSequence documentChars = getEditor().getDocument().getCharsSequence();
    int currentOffset = 0;
    List<Integer> rangeSizes = new ArrayList<>();
    List<TextRange> ranges = new ArrayList<>();

    int documentLength = documentChars.length();
    for (int i = 0; i < documentLength; i++) {
      int previousSegmentIndex = segments.findSegmentIndex(i) - 2;
      int startSegmentIndex = Math.max(0, previousSegmentIndex);
      int data;
      do {
        data = segments.getSegmentData(startSegmentIndex);
        if (data >= 0 || startSegmentIndex == 0) {
          break;
        }
        startSegmentIndex--;
      }
      while (true);

      int startOffset = segments.getSegmentStart(startSegmentIndex);
      if (startOffset > currentOffset) {
        int rangeSize = startOffset - currentOffset;
        ranges.add(TextRange.create(currentOffset, startOffset));
        rangeSizes.add(rangeSize);
        currentOffset = startOffset;
      }
    }

    Collections.sort(rangeSizes);
    int rangesSize = rangeSizes.size();

    int maxRangeSize = rangeSizes.isEmpty() ? 0 : Collections.max(rangeSizes);
    StringBuilder result = new StringBuilder("File size: " + documentLength + "\n" +
                                             "Re-highlighted ranges: " + rangesSize + "\n" +
                                             "Avg range size: " + CollectionsKt.averageOfInt(rangeSizes) + "\n" +
                                             "Min range size: " + Collections.min(rangeSizes) + "\n" +
                                             "Max range size: " + maxRangeSize + "\n");

    int percent = 10;
    int passed = 0;
    int size90 = -1;

    for (Integer size : rangeSizes) {
      passed += size;
      if (passed >= documentLength * percent / 100) {
        if (percent == 90) {
          size90 = size;
        }
        result.append(percent).append("% of text has range <= ").append(size).append("\n");
        percent += 10;
      }
    }

    result.append(SEPARATOR_NEW_LINE_AFTER);

    Consumer<TextRange> rangeProcessor = it -> {
      result.append("Range: ").append(it).append(" (").append(it.getLength()).append(")\n");
      result.append(protectSpaces(it.subSequence(documentChars))).append(SEPARATOR_NEWLINES);
    };

    if (size90 > 0) {
      int threshold = size90;
      ranges.forEach(it -> {
        if (it.getLength() >= threshold) {
          rangeProcessor.consume(it);
        }
      });
    }
    else {
      for (TextRange range : ranges) {
        if (range.getLength() == maxRangeSize) {
          rangeProcessor.consume(range);
        }
      }
    }

    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), result.toString());
  }

  protected @NotNull String protectSpaces(@NotNull CharSequence charSequence) {
    return charSequence.toString().replace(' ', '␣');
  }

  private @NotNull String serializeTextAttributeKey(@Nullable TextAttributesKey key) {
    if (key == null) {
      return "";
    }
    String keyName = key.getExternalName();
    TextAttributesKey fallbackKey = key.getFallbackAttributeKey();
    assertNotSame(fallbackKey, key);
    return fallbackKey == null ? keyName : (keyName + " => " + serializeTextAttributeKey(fallbackKey));
  }

  /**
   * Logic taken from {@link ShowParameterInfoHandler#invoke(com.intellij.openapi.project.Project, com.intellij.openapi.editor.Editor, com.intellij.psi.PsiFile, int, com.intellij.psi.PsiElement, boolean, boolean)}
   */
  protected void doTestParameterInfo() {
    initWithFileSmartWithoutErrors();
    doTestParameterInfoWithoutInit();
  }

  protected void doTestParameterInfoWithoutInit() {
    addVirtualFileFilter();
    List<Integer> offsets = getAndRemoveCarets();
    Editor editor = getEditor();
    PsiFile file = getFile();
    int fileLength = file.getTextLength();
    CaretModel caretModel = editor.getCaretModel();
    StringBuilder sb = new StringBuilder();
    for (Integer offset : offsets) {
      if (sb.length() > 0) {
        sb.append(SEPARATOR_NEWLINES);
      }
      caretModel.moveToOffset(offset);
      sb.append(getEditorTextWithCaretsAndSelections()).append("\n");
      final int offsetForLangDetection = offset > 0 && offset == fileLength ? offset - 1 : offset;
      final Language language = PsiUtilCore.getLanguageAtOffset(file, offsetForLangDetection);
      ParameterInfoHandler<?, ?>[] handlers =
        ShowParameterInfoHandler.getHandlers(getProject(), language, file.getViewProvider().getBaseLanguage());
      assertNotNull(handlers);
      if (handlers.length == 0) {
        sb.append("No handlers");
        continue;
      }


      for (ParameterInfoHandler<?, ?> handler : handlers) {
        MockCreateParameterInfoContext context = new MockCreateParameterInfoContext(editor, file);
        Object element = handler.findElementForParameterInfo(context);
        if (element == null) {
          sb.append("No element");
          continue;
        }
        Object[] itemsToShow = context.getItemsToShow();
        assertNotNull(itemsToShow);
        List<String> hintElements = new ArrayList<>();
        for (Object itemToShow : itemsToShow) {
          if (itemToShow instanceof PerlParameterInfo) {
            String itemString = serializeSubArgument(((PerlParameterInfo)itemToShow).getArgument());
            if (((PerlParameterInfo)itemToShow).isSelected()) {
              itemString = "<" + itemString + ">";
            }
            hintElements.add(itemString);
          }
          else {
            hintElements.add(itemToShow.toString());
          }
        }
        sb.append(StringUtil.join(hintElements, ", "));
      }
    }
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
  }

  protected void withTestMore() {
    addTestLibrary("test_more");
  }

  public void initWithPerlTidy() {
    initWithTestDataFile("perlTidy");
  }

  public void initWithPinxi() {
    initWithTestDataFile("pinxi");
  }

  protected void initWithCpanFile() {
    try {
      initWithFile("cpanfile", "");
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void initWithTestDataFile(@NotNull String sourceName) {
    initWithTestDataFile(sourceName, sourceName);
  }

  public void initWithTestDataFile(@NotNull String sourceName, @NotNull String targetName) {
    try {
      initWithFileContent(targetName, getFileExtension(),
                          FileUtil.loadFile(new File("testData", sourceName + getRealDataFileExtension()), CharsetToolkit.UTF8, true)
                            .trim());
      assertNoErrorElements();
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected void withFunctionParameters() {addTestLibrary("functionParameters");}

  protected void withFuture() { addTestLibrary("future"); }

  protected void withPerlPod528() { addTestLibrary("perldoc528"); }

  protected void withPerlPod532() { addTestLibrary("perldoc532"); }

  protected void withCpanfile() { addTestLibrary("cpanfile"); }

  protected void withLog4perl() { addTestLibrary("log4perl"); }

  protected void withMoose() { addTestLibrary("moose"); }

  protected void withMooseX() {
    withMoose();
    addTestLibrary("mooseX");
  }

  protected void withRoleTiny() { addTestLibrary("roleTiny"); }

  protected void withMoo() { addTestLibrary("moo"); }

  protected void withFileSpec() { addTestLibrary("fileSpec"); }

  protected void withMojo() { addTestLibrary("mojo"); }

  protected void doTestStubs() {
    String testName = getTestName(true);
    String fileName = StringUtil.replace(testName, "_", ".");
    VirtualFile virtualFile = myFixture.copyFileToProject(fileName);
    CodeInsightTestFixtureImpl.ensureIndexesUpToDate(getProject());
    addVirtualFileFilter();
    PsiFile psiFile = PsiManager.getInstance(getProject()).findFile(virtualFile);
    assertNotNull(psiFile);
    FileViewProvider fileViewProvider = psiFile.getViewProvider();
    assertNotNull(fileViewProvider);

    StringBuilder sb = new StringBuilder();
    for (PsiFile file : fileViewProvider.getAllFiles()) {
      assertInstanceOf(file, PsiFileImpl.class);
      StubElement<?> stub = ((PsiFileImpl)file).getStub();
      if (sb.length() > 0) {
        sb.append(SEPARATOR_NEWLINES);
      }
      sb.append(file.getLanguage()).append("\n");
      if (stub != null) {
        sb.append(DebugUtil.stubTreeToString(stub));
      }
    }
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
  }

  protected void doTestGoToDeclarationTargets() {
    initWithFileSmartWithoutErrors();
    addVirtualFileFilter();
    PsiElement[] targetElements =
      GotoDeclarationAction.findAllTargetElements(getProject(), getEditor(), getEditor().getCaretModel().getOffset());
    StringBuilder sb = new StringBuilder();
    sb.append("Total elements: ").append(targetElements.length).append("\n");
    removeVirtualFileFilter();
    for (PsiElement element : targetElements) {
      sb.append("\t").append(serializePsiElement(element)).append("\n");
      if (element instanceof NavigationItem) {
        sb.append("\t\t").append(serializePresentation(((NavigationItem)element).getPresentation())).append("\n");
      }
    }
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
  }

  /**
   * Computes target element for the quickdoc and generates it
   *
   * @return pair of target element and generated documentation
   */
  protected @NotNull Pair<PsiElement, String> getContextAndDocumentationAtCaret() {
    Editor editor = getEditor();
    PsiFile file = getFile();
    PsiElement elementAtCaret = file != null ? file.findElementAt(editor.getCaretModel().getOffset()) : null;
    assertNotNull(elementAtCaret);
    PsiElement targetElement = DocumentationManager.getInstance(getProject()).findTargetElement(editor, file);
    assertNotNull(targetElement);
    DocumentationProvider documentationProvider = DocumentationManager.getProviderFromElement(targetElement, elementAtCaret);
    assertNotNull(documentationProvider);
    String generatedDoc = StringUtil.notNullize(documentationProvider.generateDoc(targetElement, elementAtCaret));
    return Pair.create(targetElement, generatedDoc);
  }

  protected void doTestDocumentationLinksWithoutInit() {
    Pair<PsiElement, String> targetAndDocumentation = getContextAndDocumentationAtCaret();
    PsiElement targetElement = targetAndDocumentation.first;
    assertNotNull(targetElement);
    String documentation = targetAndDocumentation.second;
    assertNotNull(documentation);

    addVirtualFileFilter();
    Matcher matcher = LINK_PATTERN.matcher(documentation);
    List<Trinity<String, String, PsiElement>> links = new ArrayList<>();
    while (matcher.find()) {
      links.add(Trinity.create(matcher.group(1), matcher.group(2), getLinkTarget(targetElement, matcher.group(1))));
    }
    removeVirtualFileFilter();

    StringBuilder sb = new StringBuilder(serializePsiElement(targetElement)).append("\n\n");
    for (Trinity<String, String, PsiElement> link : links) {
      sb.append(StringUtil.stripHtml(link.second, false)).append(": ").append(link.first).append("\n");
      sb.append("\t").append(serializePsiElement(link.third)).append("\n");
    }
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
  }


  /**
   * Part of {@link DocumentationManager#navigateByLink(com.intellij.codeInsight.documentation.DocumentationComponent, java.lang.String)}
   */
  private @Nullable PsiElement getLinkTarget(@NotNull PsiElement psiElement, @NotNull String refText) {
    PsiManager manager = psiElement.getManager();
    DocumentationProvider provider = DocumentationManager.getProviderFromElement(psiElement);
    PsiElement targetElement = provider.getDocumentationElementForLink(manager, refText, psiElement);
    if (targetElement == null) {
      for (DocumentationProvider documentationProvider : DocumentationProvider.EP_NAME.getExtensionList()) {
        targetElement = documentationProvider.getDocumentationElementForLink(manager, refText, psiElement);
        if (targetElement != null) {
          break;
        }
      }
    }
    if (targetElement == null) {
      for (Language language : Language.getRegisteredLanguages()) {
        DocumentationProvider documentationProvider = LanguageDocumentation.INSTANCE.forLanguage(language);
        if (documentationProvider != null) {
          targetElement = documentationProvider.getDocumentationElementForLink(manager, refText, psiElement);
          if (targetElement != null) {
            break;
          }
        }
      }
    }
    return targetElement;
  }

  protected void doTestRename() {
    doTestRename("NewName");
  }

  protected void doTestRename(@NotNull String newName) {
    initWithFileSmart();
    doRenameAtCaret(newName);
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), getFile().getText());
  }

  protected void doTestRenameInplace(@NotNull String newName) {
    initWithFileSmartWithoutErrors();
    doInplaceRenameAtCaret(newName);
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), getFile().getText());
  }

  protected void doRenameAtCaret(@NotNull String newName) {
    myFixture.renameElementAtCaret(newName);
  }

  protected void doInplaceRenameAtCaret(@NotNull String newName) {
    CodeInsightTestUtil.doInlineRename(new PerlMemberInplaceRenameHandler(), newName, myFixture);
  }

  protected void doTestSurrounders(Predicate<String> namePredicate, boolean shouldHaveSurrounders) {
    initWithFileSmartWithoutErrors();
    List<AnAction> actions = ReadAction.compute(() -> SurroundWithHandler.buildSurroundActions(getProject(), getEditor(), getFile(), null));
    if (actions == null) {
      if (shouldHaveSurrounders) {
        fail("No surounders found");
      }
      return;
    }

    List<String> actionNames = actions.stream().map(AnAction::toString).filter(namePredicate).collect(Collectors.toList());
    if (actionNames.isEmpty()) {
      if (shouldHaveSurrounders) {
        fail("No suitable surrounders found: " + actions);
      }
      return;
    }

    assertTrue("Should not have surrounders, but got: " + actionNames, shouldHaveSurrounders);

    StringBuilder sb = new StringBuilder();
    for (String actionName : actionNames) {
      if (sb.length() > 0) {
        initWithFileSmartWithoutErrors();
        sb.append(SEPARATOR_NEWLINES);
      }
      sb.append(actionName).append(SEPARATOR_NEWLINES);
      List<AnAction> actionsList = ReadAction.compute(
        () -> SurroundWithHandler.buildSurroundActions(getProject(), getEditor(), getFile(), null));
      assertNotNull(actionsList);
      AnAction anAction = ContainerUtil.find(actionsList, it -> it.toString().equals(actionName));
      assertNotNull("No action: " + actionName, anAction);
      ApplicationManager.getApplication().invokeAndWait(() -> myFixture.testAction(anAction));
      sb.append(ReadAction.compute((@NotNull ThrowableComputable<String, RuntimeException>)this::getEditorTextWithCaretsAndSelections));
    }

    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
  }

  protected <V, T extends RunAnythingProvider<V>> void doTestRunAnythingHelpGroup(@NotNull RunAnythingHelpGroup<T> helpGroup) {
    DataContext dataContext = SimpleDataContext.getProjectContext(getProject());
    List<RunAnythingItem> helpItems = new ArrayList<>(helpGroup.getGroupItems(dataContext, ""));
    StringBuilder sb = new StringBuilder();
    sb.append("Help items:\n");
    helpItems.sort(Comparator.comparing(RunAnythingItem::getCommand));
    helpItems.forEach(it -> sb.append("\t").append(serializeRunAnythingItem(it)).append("\n"));
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
  }

  protected <V, T extends RunAnythingProvider<V>> void doTestRunAnythingProvider(@NotNull String pattern,
                                                                                 @NotNull T provider) {
    DataContext dataContext = SimpleDataContext.getProjectContext(getProject());
    List<RunAnythingItem> completionItems = ContainerUtil.map(provider.getValues(dataContext, pattern),
                                                              it -> provider.getMainListItem(dataContext, it));
    V matchingValue = provider.findMatchingValue(dataContext, pattern);
    StringBuilder sb = new StringBuilder();
    sb.append("Matching value:\n\t").append(matchingValue).append("\n");

    sb.append("Completion items:\n");
    completionItems.sort(Comparator.comparing(RunAnythingItem::getCommand));
    completionItems.forEach(it -> sb.append("\t").append(serializeRunAnythingItem(it)).append("\n"));
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
  }

  protected @NotNull String serializeRunAnythingItem(@Nullable RunAnythingItem runAnythingItem) {
    if (runAnythingItem == null) {
      return "null";
    }

    String result = "[" + runAnythingItem.getCommand() + "]";

    if (runAnythingItem instanceof RunAnythingHelpItem) {
      String placeHolder = ReflectionUtil.getField(RunAnythingHelpItem.class, runAnythingItem, String.class, "myPlaceholder");
      String description = ReflectionUtil.getField(RunAnythingHelpItem.class, runAnythingItem, String.class, "myDescription");
      Icon icon = ReflectionUtil.getField(RunAnythingHelpItem.class, runAnythingItem, Icon.class, "myIcon");
      result = "[" + placeHolder + "]; " +
               "[" + description + "]; " +
               getIconText(icon) + "; " +
               result;
    }
    return result;
  }

  protected void assertNameValid(@NotNull String name) {
    doTestNameValidator(name, true);
  }

  protected void assertNameInvalid(@NotNull String name) {
    doTestNameValidator(name, false);
  }

  protected @NotNull InputValidator getValidator() {
    throw new RuntimeException("Test should implement this method");
  }

  protected void doTestNameValidator(@NotNull String name, boolean result) {
    boolean actual = getValidator().checkInput(name);
    if (result) {
      assertTrue("Name expected to be valid: " + name, actual);
    }
    else {
      assertFalse("Name expected to be invalid: " + name, actual);
    }
  }

  protected void assumeNotWindows() {
    Assume.assumeFalse("Not running on windows", SystemInfo.isWindows);
  }

  protected void setCompletionLimit(int value) {
    Registry.get("ide.completion.variant.limit").setValue(value, getTestRootDisposable());
  }

  protected void disableAutoColon() {
    Perl5CodeInsightSettings.getInstance().AUTO_INSERT_COLON = false;
  }

  protected void enableAutoColon() {
    Perl5CodeInsightSettings.getInstance().AUTO_INSERT_COLON = true;
  }

  protected void doTestReparse(@NotNull String textToInsert) {
    doTestReparse(textToInsert, PerlLanguage.INSTANCE);
  }

  protected void doTestReparse(@NotNull String textToInsert, @NotNull Language language) {
    initWithFileSmart();
    doTestReparseWithoutInit(textToInsert, language);
  }

  protected void doTestReparseBs() {
    doTestReparseBs(PerlLanguage.INSTANCE);
  }

  protected void doTestReparseBs(@NotNull Language language) {
    initWithFileSmart();
    doTestReparseWithoutInit(() -> {
      Editor editor = getEditor();
      CaretModel caretModel = editor.getCaretModel();
      Document document = editor.getDocument();
      int offset = caretModel.getOffset();
      if (offset > 0) {
        document.deleteString(offset - 1, offset);
        caretModel.moveToOffset(offset - 1);
      }
    }, language);
  }

  protected void doTestReparseWithoutInit(@NotNull String textToInsert) {
    doTestReparseWithoutInit(textToInsert, PerlLanguage.INSTANCE);
  }

  protected void doTestReparseWithoutInit(@NotNull String textToInsert, @NotNull Language language) {
    doTestReparseWithoutInit(() -> EditorModificationUtil.insertStringAtCaret(getEditor(), textToInsert), language);
  }

  protected void doTestReparseWithoutInit(@NotNull Runnable documentModifier,
                                          @NotNull Language language) {
    PsiFile psiFile = null;
    var mainPsiFile = getFile();
    for (PsiFile subTree : mainPsiFile.getViewProvider().getAllFiles()) {
      if (subTree.getLanguage().isKindOf(language)) {
        psiFile = subTree;
        break;
      }
    }

    assertNotNull("No psi file found for a language: " + language +
                  "; got " + mainPsiFile +
                  " with subtrees " + mainPsiFile.getViewProvider().getAllFiles(), psiFile);

    FileASTNode fileNode = psiFile.getNode();
    WriteCommandAction.runWriteCommandAction(getProject(), documentModifier);
    String newText = getEditorText();
    TextRange changedRange = ChangedPsiRangeUtil.getChangedPsiRange(psiFile, (FileElement)fileNode, newText);
    Assert.assertNotNull("No changes", changedRange);
    Couple<ASTNode> roots = BlockSupportImpl.findReparseableRoots((PsiFileImpl)psiFile, fileNode, changedRange, newText);

    StringBuilder result = new StringBuilder("Reparsing block").append(SEPARATOR_NEWLINES);

    if (roots == null) {
      result.append("Full reparse");
    }
    else {
      result.append(roots.first).append(SEPARATOR_NEWLINES);
      result.append(roots.first.getChars());
    }
    result.append(SEPARATOR_NEWLINES).append("After typing").append(SEPARATOR_NEWLINES);
    result.append(getEditorTextWithCaretsAndSelections());
    result.append(SEPARATOR_NEWLINES).append("Psi structure").append(SEPARATOR_NEWLINES);
    PsiDocumentManager.getInstance(getProject()).commitAllDocuments();
    String psiString = DebugUtil.psiToString(psiFile, false, false);
    result.append(psiString);

    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), result.toString());
    if (!psiString.contains("PsiErrorElement")) {
      var finalPsiFile = psiFile;
      WriteAction.run(() -> ParsingTestCase.ensureCorrectReparse(finalPsiFile));
    }
  }

  protected void doTestInjection() {
    doTestInjection(true);
  }

  protected void doTestInjection(boolean checkErrors) {
    if (checkErrors) {
      initWithFileSmartWithoutErrors();
    }
    else {
      initWithFileSmart();
    }
    doTestInjectionWithoutInit();
  }

  protected void doTestInjectionWithoutInit() {
    PsiFile file = getTopLevelFile();
    assertNotNull(file);
    doTestInjectionWithoutInit(file);
  }

  protected void doTestInjectionWithoutInit(@NotNull PsiFile file) {
    InjectedLanguageManager injectedLanguageManager = InjectedLanguageManager.getInstance(getProject());
    StringBuilder result = new StringBuilder();
    file.accept(new PsiRecursiveElementVisitor() {
      @Override
      public void visitElement(@NotNull PsiElement element) {
        if (element instanceof PsiLanguageInjectionHost) {
          List<Pair<PsiElement, TextRange>> files = injectedLanguageManager.getInjectedPsiFiles(element);
          if (files != null && !files.isEmpty()) {
            CharSequence hostChars = element.getNode().getChars();
            TextRange hostRange = element.getTextRange();
            result.append("Host: ").append(element).append("\n");
            result.append("Host range: ").append(hostRange).append("\n");
            result.append("Host chars: ").append(protectSpaces(hostChars));
            PsiElement injectedFile = null;
            for (Pair<PsiElement, TextRange> pair : files) {
              if (injectedFile == null) {
                injectedFile = pair.first;
                result.append("\nInjected file language: ").append(injectedFile.getLanguage()).append("\n");
                VirtualFile virtualFile = PsiUtilCore.getVirtualFile(injectedFile);
                assertInstanceOf(virtualFile, LightVirtualFile.class);
                result.append("Decoded psi file content:").append(SEPARATOR_NEWLINES);
                result.append(protectSpaces(injectedFile.getText())).append(SEPARATOR_NEWLINES);
                result.append("Decoded virtual file content:").append(SEPARATOR_NEWLINES);
                result.append(protectSpaces(((LightVirtualFile)virtualFile).getContent())).append(SEPARATOR_NEWLINES);
              }
              else {
                assertEquals(pair.first, injectedFile);
              }
              TextRange range = pair.second;
              result.append("Host range: ").append(range).append(" '").append(protectSpaces(range.subSequence(hostChars))).append("'\n");
            }
            result.append(SEPARATOR_NEWLINES);
          }
        }
        super.visitElement(element);
      }
    });
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), result.toString());
  }

  protected void doTestInjectedTypingWithoutInit(@NotNull String textToType) {
    var topLevelFile = getTopLevelFile();
    assertNotNull(topLevelFile);
    var topLevelEditor = getTopLevelEditor();
    var targetOffsetInDecoded = computeOffsetInDecodedFile();
    var quickEditHandler = new QuickEditAction().invokeImpl(getProject(), topLevelEditor, topLevelFile);
    var injectedVirtualFile = quickEditHandler.getNewFile().getVirtualFile();
    assertNotNull(injectedVirtualFile);
    myFixture.configureFromExistingVirtualFile(injectedVirtualFile);
    getTopLevelEditor().getCaretModel().moveToOffset(targetOffsetInDecoded);
    myFixture.type(textToType);
    PsiDocumentManager.getInstance(getProject()).commitAllDocuments();

    assertTrue(topLevelFile.isValid());
    doTestInjectionWithoutInit(topLevelFile);
  }

  /**
   * @return editor offset in decoded injected file for the caret position in injected element
   */
  private int computeOffsetInDecodedFile() {
    var injectionHost = getTopLevelFileElementAtCaret(PsiLanguageInjectionHost.class);
    assertNotNull(injectionHost);
    var offsetInHost = getTopLevelEditor().getCaretModel().getOffset() - injectionHost.getTextRange().getStartOffset();
    var injector = getInjector(injectionHost);
    StringBuilder decodedContent = new StringBuilder();
    int[] offsetInDecoded = {-1};
    injector.getLanguagesToInject(new MultiHostRegistrar() {
      @Override
      public @NotNull MultiHostRegistrar startInjecting(@NotNull Language language) {
        return this;
      }

      @Override
      public @NotNull MultiHostRegistrar addPlace(@Nullable String prefix,
                                                  @Nullable String suffix,
                                                  @NotNull PsiLanguageInjectionHost host,
                                                  @NotNull TextRange rangeInsideHost) {
        if (prefix != null) {
          decodedContent.append(prefix);
        }
        var chunkStartOffset = decodedContent.length();
        LiteralTextEscaper<? extends PsiLanguageInjectionHost> literalTextEscaper = injectionHost.createLiteralTextEscaper();
        literalTextEscaper.decode(rangeInsideHost, decodedContent);
        if (rangeInsideHost.contains(offsetInHost)) {
          for (int i = 0; i < decodedContent.length() - chunkStartOffset; i++) {
            if (literalTextEscaper.getOffsetInHost(i, rangeInsideHost) == offsetInHost) {
              offsetInDecoded[0] = chunkStartOffset + i;
              break;
            }
          }
        }
        else if (offsetInHost == rangeInsideHost.getEndOffset()) {
          offsetInDecoded[0] = decodedContent.length();
        }
        if (suffix != null) {
          decodedContent.append(suffix);
        }
        return this;
      }

      @Override
      public void doneInjecting() {

      }
    }, injectionHost);
    if (offsetInDecoded[0] < 0) {
      fail("Unable to find offset in decoded text");
    }
    return offsetInDecoded[0];
  }

  protected @NotNull MultiHostInjector getInjector(@NotNull PsiLanguageInjectionHost host) {
    var result = new ArrayList<MultiHostInjector>();
    for (MultiHostInjector injector : MultiHostInjector.MULTIHOST_INJECTOR_EP_NAME.getExtensions(getProject())) {
      for (Class<? extends PsiElement> aClass : injector.elementsToInjectIn()) {
        if (!aClass.equals(PsiLanguageInjectionHost.class) && !aClass.equals(PsiElement.class) && aClass.isInstance(host)) {
          result.add(injector);
          break;
        }
      }
    }
    if (result.size() != 1) {
      fail("Expected a single injector for " + host.getClass() + " got: " + result);
    }
    return result.get(0);
  }
}
