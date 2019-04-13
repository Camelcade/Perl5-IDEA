/*
 * Copyright 2015-2018 Alexandr Evstigneev
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
import com.intellij.codeInsight.controlflow.ControlFlow;
import com.intellij.codeInsight.controlflow.Instruction;
import com.intellij.codeInsight.documentation.DocumentationManager;
import com.intellij.codeInsight.editorActions.SelectWordHandler;
import com.intellij.codeInsight.highlighting.HighlightManager;
import com.intellij.codeInsight.highlighting.actions.HighlightUsagesAction;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.template.impl.TemplateManagerImpl;
import com.intellij.codeInsight.template.impl.TemplateState;
import com.intellij.codeInsight.template.impl.editorActions.ExpandLiveTemplateByTabAction;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.PathEnvironmentVariableUtil;
import com.intellij.execution.impl.EditorHyperlinkSupport;
import com.intellij.execution.util.ExecUtil;
import com.intellij.ide.DataManager;
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
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lang.LanguageStructureViewBuilder;
import com.intellij.lang.documentation.DocumentationProvider;
import com.intellij.lang.documentation.DocumentationProviderEx;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.lexer.Lexer;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.projectRoots.impl.PerlSdkTable;
import com.intellij.openapi.projectRoots.impl.ProjectJdkImpl;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.*;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileFilter;
import com.intellij.openapi.vfs.newvfs.impl.VfsRootAccess;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.impl.DebugUtil;
import com.intellij.psi.impl.PsiManagerEx;
import com.intellij.psi.impl.source.tree.injected.InjectedFileViewProvider;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.rename.inplace.VariableInplaceRenameHandler;
import com.intellij.refactoring.util.NonCodeSearchDescriptionLocation;
import com.intellij.testFramework.MapDataContext;
import com.intellij.testFramework.UsefulTestCase;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl;
import com.intellij.ui.components.breadcrumbs.Crumb;
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
import com.perl5.lang.perl.idea.codeInsight.controlFlow.*;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.idea.completion.PerlStringCompletionCache;
import com.perl5.lang.perl.idea.configuration.settings.PerlLocalSettings;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.idea.intellilang.PerlInjectionMarkersService;
import com.perl5.lang.perl.idea.manipulators.PerlBareStringManipulator;
import com.perl5.lang.perl.idea.manipulators.PerlStringContentManipulator;
import com.perl5.lang.perl.idea.manipulators.PerlStringManipulator;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationBase;
import com.perl5.lang.perl.idea.project.PerlNamesCache;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.refactoring.introduce.PerlIntroduceTarget;
import com.perl5.lang.perl.idea.refactoring.introduce.PerlIntroduceVariableHandler;
import com.perl5.lang.perl.idea.refactoring.introduce.occurrence.PerlIntroduceTargetOccurrencesCollector;
import com.perl5.lang.perl.idea.refactoring.introduce.target.PerlIntroduceTargetsHandler;
import com.perl5.lang.perl.idea.sdk.PerlSdkAdditionalData;
import com.perl5.lang.perl.idea.sdk.PerlSdkType;
import com.perl5.lang.perl.idea.sdk.host.PerlHostHandler;
import com.perl5.lang.perl.idea.sdk.implementation.PerlImplementationHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerData;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerHandler;
import com.perl5.lang.perl.internals.PerlVersion;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.mixins.PerlStringBareMixin;
import com.perl5.lang.perl.psi.mixins.PerlStringMixin;
import com.perl5.lang.perl.psi.properties.PerlValuableEntity;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlRunUtil;
import com.perl5.lang.pod.PodLanguage;
import gnu.trove.THashSet;
import junit.framework.AssertionFailedError;
import org.intellij.plugins.intelliLang.inject.InjectLanguageAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public abstract class PerlLightTestCaseBase extends LightCodeInsightFixtureTestCase {
  private static final String START_FOLD = "<fold\\stext=\'[^\']*\'(\\sexpand=\'[^\']*\')*>";
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
  private final static boolean ENABLE_SVG_GENERATION = Boolean.parseBoolean(System.getenv("CAMELCADE_GENERATE_SVG"));
  private PerlSharedSettings mySharedSettings;
  private PerlLocalSettings myLocalSettings;
  private PerlInjectionMarkersService myInjectionMarkersService;
  private static final String SEPARATOR = "----------";
  private static final String SEPARATOR_NEW_LINE_BEFORE = "\n" + SEPARATOR;
  private static final String SEPARATOR_NEW_LINE_AFTER = SEPARATOR + "\n";
  private static final String SEPARATOR_NEWLINES = SEPARATOR_NEW_LINE_BEFORE + "\n";
  private final Disposable myPerlLightTestCaseDisposable = Disposer.newDisposable();

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    VfsRootAccess.allowRootAccess(getTestRootDisposable(), "/");
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
      ApplicationManager.getApplication()
        .invokeAndWait(() -> PerlProjectManager.getInstance(getProject()).setExternalLibraries(Collections.emptyList()));
    }
    finally {
      super.tearDown();
    }
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
      sdk -> {
        PerlSdkTable.getInstance().addJdk(sdk, getTestRootDisposable());
        PerlProjectManager.getInstance(getProject()).setProjectSdk(sdk);
        PerlRunUtil.refreshSdkDirs(sdk, getProject());
      });
  }

  protected void enableLiveTemplatesTesting() {
    TemplateManagerImpl.setTemplateTesting(getProject(), getTestRootDisposable());
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
      }));
  }

  /**
   * Adds a {@code contentRootFile} as a content entry to the current module
   *
   * @apiNote adding modules is not allowed in the light tests
   */
  protected void addContentEntry(@NotNull VirtualFile contentRootFile) {
    ModuleRootManager moduleRootManager = ModuleRootManager.getInstance(myModule);
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
        VirtualFile libdir = LocalFileSystem.getInstance().refreshAndFindFileByPath("../testData/testlib");
        assert libdir != null;

        PerlProjectManager perlProjectManager = PerlProjectManager.getInstance(getProject());
        ProjectJdkImpl testSdk = PerlSdkTable.getInstance().createSdk("test");
        testSdk.setSdkAdditionalData(new PerlSdkAdditionalData(
          PerlHostHandler.getDefaultHandler().createData(),
          PerlVersionManagerData.getDefault(),
          PerlImplementationHandler.getDefaultHandler().createData()));
        PerlSdkTable.getInstance().addJdk(testSdk, getTestRootDisposable());
        perlProjectManager.setProjectSdk(testSdk);
        perlProjectManager.addExternalLibrary(libdir);

        CodeInsightTestFixtureImpl.ensureIndexesUpToDate(getProject());
      }));
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
    myFixture.configureByText(filename + (extension.isEmpty() ? "" : "." + extension), content);
  }

  public void initWithFile(String filename, String extension) throws IOException {
    initWithFile(filename, extension, filename + (extension.isEmpty() ? "" : ".code"));
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

  public final void doTestCompletion() {
    doTestCompletion("", null);
  }

  public final void doTestCompletion(BiPredicate<? super LookupElement, ? super LookupElementPresentation> predicate) {
    doTestCompletion("", predicate);
  }

  public final void doTestCompletion(@NotNull String answerSuffix,
                                     BiPredicate<? super LookupElement, ? super LookupElementPresentation> predicate) {
    initWithFileSmart();
    doTestCompletionCheck(answerSuffix, predicate);
  }

  public final void doTestCompletionFromText(@NotNull String content) {
    initWithTextSmart(content);
    doTestCompletionCheck();
  }

  protected void doTestCompletionCheck() {
    doTestCompletionCheck("", null);
  }

  protected void doTestCompletionCheck(@NotNull String answerSuffix,
                                       @Nullable BiPredicate<? super LookupElement, ? super LookupElementPresentation> predicate) {
    CodeInsightTestFixtureImpl.ensureIndexesUpToDate(getProject());
    addVirtualFileFilter();
    myFixture.complete(CompletionType.BASIC, 1);
    List<String> result = new ArrayList<>();
    LookupElement[] elements = myFixture.getLookupElements();
    removeVirtualFileFilter();
    if (elements != null) {
      for (LookupElement lookupElement : elements) {
        LookupElementPresentation presentation = new LookupElementPresentation();
        lookupElement.renderElement(presentation);

        if (predicate != null && !predicate.test(lookupElement, presentation)) {
          continue;
        }
        StringBuilder sb = new StringBuilder();

        sb.append("Lookups: ")
          .append(StringUtil.join(lookupElement.getAllLookupStrings(), ", "))
          .append("; Text: ")
          .append(presentation.getItemText())
          .append("; Tail: ")
          .append(presentation.getTailText())
          .append("; Type: ")
          .append(presentation.getTypeText())
          .append("; Icon: ")
          .append(getIconText(presentation.getIcon()))
          .append("; Type Icon: ")
          .append(getIconText(presentation.getTypeIcon()))
        ;

        result.add(sb.toString());
      }
    }

    ContainerUtil.sort(result);

    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(answerSuffix), StringUtil.join(result, "\n"));
  }

  protected String getIconText(@Nullable Icon icon) {
    if (icon == null) {
      return "null";
    }
    String iconString = icon.toString();
    return iconString.substring(iconString.lastIndexOf('/'));
  }

  protected void addVirtualFileFilter() {
    VirtualFile openedVirtualFile = getFile().getVirtualFile();
    ((PsiManagerEx)myFixture.getPsiManager()).setAssertOnFileLoadingFilter(file -> {
      if (!(file.getFileType() instanceof PerlPluginBaseFileType)) {
        return false;
      }
      return !openedVirtualFile.equals(file);
    }, getTestRootDisposable());
  }

  protected void removeVirtualFileFilter() {
    ((PsiManagerEx)myFixture.getPsiManager()).setAssertOnFileLoadingFilter(VirtualFileFilter.NONE, getTestRootDisposable());
  }

  public String getTestResultsFilePath() {
    return getTestResultsFilePath("");
  }

  protected String getResultsTestDataPath() {
    return getTestDataPath();
  }

  public String getTestResultsFilePath(@NotNull String appendix) {
    return getResultsTestDataPath() + "/" + getTestName(true) + appendix + "." + getResultsFileExtension();
  }

  @NotNull
  protected String getResultsFileExtension() {
    return getFileExtension() + ".txt";
  }

  public void doTestResolve() {
    doTestResolve(false);
  }

  public void doTestResolve(boolean reparse) {
    initWithFileSmart();
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

  private String serializeReference(PsiReference reference) {
    StringBuilder sb = new StringBuilder();
    PsiElement sourceElement = reference.getElement();

    ResolveResult[] resolveResults;
    if (reference instanceof PsiPolyVariantReference) {
      resolveResults = ((PsiPolyVariantReference)reference).multiResolve(false);
    }
    else {
      PsiElement target = reference.resolve();
      resolveResults = target == null ? PsiElementResolveResult.EMPTY_ARRAY : PsiElementResolveResult.createResults(target);
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
      .append(resolveResults.length)
      .append(" results:")
      .append('\n');

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

    getFile().getViewProvider().getAllFiles().forEach(file -> {
      if (!file.getLanguage().isKindOf(PerlLanguage.INSTANCE) && !file.getLanguage().isKindOf(PodLanguage.INSTANCE)) {
        return;
      }
      file.accept(new PsiElementVisitor() {
        @Override
        public void visitElement(PsiElement element) {
          Collections.addAll(references, element.getReferences());
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
    Collection<UsageInfo> usages = myFixture.findUsages(targetElement);
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

  protected void doLiveTemplateTest(@NotNull String textToType) {
    doLiveTemplateTest("liveTemplatesTest", textToType);
  }

  protected void doLiveTemplateTest(@NotNull String fileName, @NotNull String textToType) {
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
        EditorModificationUtil.insertStringAtCaret(editor, textToType);
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
    CaretModel caretModel = getEditor().getCaretModel();
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
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), getEditorTextWithMacroses(macroses));
  }

  private DataContext getEditorDataContext() {
    return DataManager.getInstance().getDataContext(myFixture.getEditor().getComponent());
  }

  private String getEditorText() {
    return getEditor().getDocument().getText();
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
    return getEditorTextWithMacroses(addCaretsMacroses(new ArrayList<>()));
  }

  @NotNull
  private List<Pair<Integer, String>> addCaretsMacroses(@NotNull List<Pair<Integer, String>> macroses) {
    getEditor().getCaretModel().getAllCarets().forEach(caret -> addCaretInfo(caret, macroses));
    return macroses;
  }

  @NotNull
  private String getEditorTextWithMacroses(List<Pair<Integer, String>> macros) {
    ContainerUtil.sort(macros, Comparator.comparingInt(pair -> pair.first));
    StringBuilder sb = new StringBuilder(getEditorText());

    for (int i = macros.size() - 1; i > -1; i--) {
      Pair<Integer, String> macro = macros.get(i);
      sb.insert(macro.first, macro.second);
    }

    return sb.toString();
  }

  protected void doTestTyping(@NotNull String toType) {
    initWithFileSmart();
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

  protected void doTestEnter() {
    initWithFileSmart();
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
        .map(argument -> {
          if (argument.isOptional()) {
            return "[" + argument.toStringLong() + "]";
          }
          else {
            return argument.toStringLong();
          }
        })
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

  protected void doTestLightElements() {
    initWithFileSmartWithoutErrors();
    StringBuilder sb = new StringBuilder();
    getFile().accept(new PsiRecursiveElementVisitor() {
      @Override
      public void visitElement(PsiElement element) {
        if (element instanceof PerlPolyNamedElement) {
          sb.append("Poly-named provider: ").append(serializePsiElement(element)).append("\n");
          for (PerlDelegatingLightNamedElement namedElement : ((PerlPolyNamedElement)element).getLightElements()) {
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

  protected void doTestUsagesHighlighting() {
    initWithFileSmartWithoutErrors();

    List<Integer> caretsOffsets = getAndRemoveCarets();
    StringBuilder sb = new StringBuilder();
    HighlightManager highlightManager = HighlightManager.getInstance(getProject());
    Editor editor = getEditor();

    for (Integer caretOffset : caretsOffsets) {
      editor.getCaretModel().moveToOffset(caretOffset);
      myFixture.testAction(new HighlightUsagesAction());
      List<RangeHighlighter> highlighters = Arrays.asList(editor.getMarkupModel().getAllHighlighters());
      ContainerUtil.sort(highlighters, Comparator.comparingInt(RangeMarker::getStartOffset));

      List<Pair<Integer, String>> macroses = addCaretsMacroses(new ArrayList<>());

      for (RangeHighlighter highlighter : highlighters) {
        TextAttributes attributes = highlighter.getTextAttributes();
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
      if (sb.length() > 0) {
        sb.append("\n===============================================================\n");
      }
      sb.append(getEditorTextWithMacroses(macroses));
    }

    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
  }

  protected void testFoldingRegions(@NotNull String verificationFileName, LanguageFileType fileType) {
    testFoldingRegions(verificationFileName, false, fileType);
  }

  protected void testFoldingRegions(@NotNull String verificationFileName, boolean doCheckCollapseStatus, LanguageFileType fileType) {
    String expectedContent;
    try {
      expectedContent = FileUtil.loadFile(new File(getTestDataPath() + "/" + verificationFileName + ".code"));
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
    assertInstanceOf(getFile().getViewProvider(), InjectedFileViewProvider.class);
  }

  protected void assertNotInjected() {
    assertFalse("Editor is EditorWindow, looks like injected to me", getEditor() instanceof EditorWindow);
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


  @NotNull
  protected <T extends PsiElement> T getElementAtCaret(@NotNull Class<T> clazz) {
    int offset = myFixture.getEditor().getCaretModel().getOffset();
    PsiElement focused = myFixture.getFile().findElementAt(offset);
    return ObjectUtils.assertNotNull(PsiTreeUtil.getParentOfType(focused, clazz, false));
  }

  protected <T extends PsiElement> T getElementAtCaretWithoutInjection(@NotNull Class<T> clazz) {
    return ObjectUtils.assertNotNull(PsiTreeUtil.getParentOfType(getElementAtCaretWithoutInjection(), clazz, false));
  }

  @NotNull
  protected PsiElement getElementAtCaretWithoutInjection() {
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

  protected void doTestMassQuickFixes(@NotNull String fileName, @NotNull Class inspectionClass, @NotNull String quickfixNamePrefix) {
    initWithFileSmart(fileName);
    myFixture.enableInspections(inspectionClass);
    myFixture.getAllQuickFixes().stream()
      .filter((it) -> it.getText().startsWith(quickfixNamePrefix))
      .forEach(myFixture::launchAction);
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), getFile().getText());
  }

  protected void doTestAnnotationQuickFix(@NotNull String fileName, @NotNull Class inspectionClass, @NotNull String quickFixNamePrefix) {
    initWithFileSmartWithoutErrors(fileName);
    doTestAnnotationQuickFixWithoutInitialization(inspectionClass, quickFixNamePrefix);
  }

  protected void doTestAnnotationQuickFix(@NotNull Class inspectionClass, @NotNull String quickFixNamePrefix) {
    initWithFileSmartWithoutErrors();
    doTestAnnotationQuickFixWithoutInitialization(inspectionClass, quickFixNamePrefix);
  }

  private void doTestAnnotationQuickFixWithoutInitialization(@NotNull Class inspectionClass, @NotNull String quickFixNamePrefix) {
    myFixture.enableInspections(inspectionClass);
    //myFixture.checkHighlighting(true, false, false);
    doTestIntentionWithoutLoad(quickFixNamePrefix);
  }

  @Nullable
  protected IntentionAction getSingleIntentionSafe(@NotNull String prefixOrName) {
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

  @NotNull
  protected IntentionAction getSingleIntention(@NotNull String prefixOrName) {
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

  @NotNull
  protected String serializePresentation(@Nullable ItemPresentation presentation) {
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


  protected void doInspectionTest(Class clazz) {
    initWithFileSmart();
    addVirtualFileFilter();
    myFixture.enableInspections(clazz);
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
    initWithFileSmartWithoutErrors();
    ControlFlow controlFlow = PerlControlFlowBuilder.getFor(getFile());
    final String stringifiedControlFlow = Arrays.stream(controlFlow.getInstructions())
      .map(it -> it.toString() + " (" + it.getClass().getSimpleName() + ")")
      .collect(Collectors.joining("\n"));

    if (ENABLE_SVG_GENERATION) {
      try {
        String svgDataPath = getSvgDataPath();
        //if (!new File(svgDataPath).exists()) { // fixme this check may be configurable
        saveSvgFile(svgDataPath, controlFlow);
        //}
      }
      catch (Exception e) {
        LOG.error(e);
      }
    }

    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), stringifiedControlFlow);
  }

  @NotNull
  private String getSvgDataPath() {
    return FileUtil.join(getTestDataPath(), "svg", getTestName(true) + ".svg");
  }

  private void saveSvgFile(@NotNull final String outSvgFile, @NotNull final ControlFlow flow) throws IOException, ExecutionException {
    String dotUtilName = SystemInfoRt.isUnix ? "dot" : "dot.exe";
    File dotFullPath = PathEnvironmentVariableUtil.findInPath(dotUtilName);
    if (dotFullPath == null) {
      throw new FileNotFoundException("Cannot find dot utility in path");
    }
    File tmpFile = FileUtil.createTempFile("control-flow", ".dot", true);
    FileUtil.writeToFile(tmpFile, convertControlFlowToDot(flow));
    ExecUtil.execAndGetOutput(new GeneralCommandLine(dotFullPath.getAbsolutePath()).withInput(tmpFile.getAbsoluteFile())
                                .withParameters("-Tsvg", "-o" + outSvgFile, tmpFile.getAbsolutePath()).withRedirectErrorStream(true));
  }

  @NotNull
  private String convertControlFlowToDot(@NotNull final ControlFlow flow) {
    StringBuilder builder = new StringBuilder();
    builder.append("digraph {");
    for (Instruction instruction : flow.getInstructions()) {
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
      return escape(elementText);
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
    action.actionPerformedImpl(myModule.getProject(), myFixture.getEditor());
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
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), getEditorTextWithMacroses(macros));
  }

  private String serializeCrumb(@Nullable Crumb crumb) {
    if (crumb == null) {
      return "EMPTY";
    }
    String tooltip = crumb.getTooltip();
    return "[" + crumb.getText() + (tooltip == null ? "" : "(" + tooltip + ")") + ", " + getIconText(crumb.getIcon());
  }

  protected void doTestUsagesGrouping() {
    initWithFileSmartWithoutErrors();
    PsiElement targetElement = TargetElementUtil
      .findTargetElement(getEditor(), TargetElementUtil.ELEMENT_NAME_ACCEPTED | TargetElementUtil.REFERENCED_ELEMENT_ACCEPTED);
    assertNotNull(targetElement);
    PsiElement[] targetElements = {targetElement};
    Collection<UsageInfo> usages = myFixture.findUsages(targetElement);
    List<UsageGroupingRule> rules = getActiveGroupingRules(new UsageViewSettings(true, true, true, true, true));
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

  @NotNull
  private String getUsageGroups(@NotNull Usage usage, @NotNull UsageGroupingRule groupingRule) {
    StringBuilder sb = new StringBuilder();
    groupingRule.getParentGroupsFor(usage, UsageTarget.EMPTY_ARRAY).forEach(usageGroup -> {
      if (sb.length() > 0) {
        sb.append("\n");
      }
      sb.append("    ").append(serializeUsageGroup(usageGroup));
    });
    return sb.toString();
  }

  @NotNull
  private String serializeUsageGroup(@NotNull UsageGroup usageGroup) {
    if (usageGroup instanceof PsiElementUsageGroupBase) {
      return "PsiElement: " + serializePsiElement(((PsiElementUsageGroupBase)usageGroup).getElement());
    }
    return usageGroup.getClass().getSimpleName() + ": " + usageGroup.getText(null) + "; " + getIconText(usageGroup.getIcon(true));
  }

  /**
   * Copy-paste of {@link UsageViewImpl#getActiveGroupingRules(com.intellij.openapi.project.Project, com.intellij.usages.UsageViewSettings)}
   */
  @NotNull
  private List<UsageGroupingRule> getActiveGroupingRules(@NotNull UsageViewSettings usageViewSettings) {
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
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), getEditorTextWithMacroses(macros));
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

  @NotNull
  protected Language getDocumentationElementLanguage() {
    return PerlLanguage.INSTANCE;
  }

  protected void doTestQuickDoc() {
    initWithFileSmartWithoutErrors();
    List<Integer> caretsOffsets = getAndRemoveCarets();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < caretsOffsets.size(); i++) {
      Integer caretOffset = caretsOffsets.get(i);
      if (caretsOffsets.size() > 1) {
        sb.append(SEPARATOR).append("Caret #").append(i).append(" at: ").append(caretOffset)
          .append(SEPARATOR_NEW_LINE_AFTER);
      }
      getEditor().getCaretModel().moveToOffset(caretOffset);
      PsiElement elementAtCaret = getFile().getViewProvider().findElementAt(
        getEditor().getCaretModel().getOffset(), getDocumentationElementLanguage());
      assertNotNull(elementAtCaret);
      DocumentationProvider documentationProvider = DocumentationManager.getProviderFromElement(elementAtCaret);
      assertInstanceOf(documentationProvider, DocumentationProviderEx.class);
      PsiElement targetElement = DocumentationManager.getInstance(getProject()).findTargetElement(getEditor(), getFile(), elementAtCaret);
      assertNotNull(targetElement);
      String generatedDoc = documentationProvider.generateDoc(targetElement, elementAtCaret);
      assertNotNull(generatedDoc);
      sb.append(generatedDoc).append("\n");
    }

    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
  }

  protected void doTestPerlValue() {
    initWithFileSmart(getTestName(true));
    addVirtualFileFilter();
    PerlValuableEntity element = getElementAtCaret(PerlValuableEntity.class);
    assertNotNull(element);
    StringBuilder sb = new StringBuilder();
    sb.append(getEditorTextWithCaretsAndSelections().trim())
      .append(SEPARATOR_NEWLINES)
      .append(element.getText()).append("\n")
      .append(serializePsiElement(element)).append("\n")
      .append(PerlValue.from(element).getPresentableText());

    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
  }

  protected void doTestReturnValue() {
    initWithFileSmart(getTestName(true));
    addVirtualFileFilter();
    PerlSubElement element = getElementAtCaret(PerlSubElement.class);
    assertNotNull(element);
    StringBuilder sb = new StringBuilder();
    sb.append(getEditorTextWithCaretsAndSelections().trim())
      .append(SEPARATOR_NEWLINES)
      .append(element.getReturnValueFromCode(null, Collections.emptyList()));

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
    SyntaxHighlighter syntaxHighlighter = SyntaxHighlighterFactory.getSyntaxHighlighter(
      getFile().getFileType(), getProject(), getFile().getVirtualFile());
    assertNotNull(syntaxHighlighter);
    Lexer highlightingLexer = syntaxHighlighter.getHighlightingLexer();
    assertNotNull(highlightingLexer);

    String fileText = getFile().getText();
    highlightingLexer.start(fileText);
    IElementType tokenType;
    StringBuilder sb = new StringBuilder();
    while ((tokenType = highlightingLexer.getTokenType()) != null) {
      TextAttributesKey[] highlights = syntaxHighlighter.getTokenHighlights(tokenType);
      if (highlights.length > 0) {
        if (sb.length() > 0) {
          sb.append("\n");
        }
        sb.append(fileText, highlightingLexer.getTokenStart(), highlightingLexer.getTokenEnd())
          .append("\n");
        List<String> attrNames = new SmartList<>();
        for (TextAttributesKey attributesKey : highlights) {
          attrNames.add("    " + serializeTextAttributeKey(attributesKey));
        }
        sb.append(StringUtil.join(attrNames, "\n"));
      }
      highlightingLexer.advance();
    }
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
  }

  @NotNull
  private String serializeTextAttributeKey(@Nullable TextAttributesKey key) {
    if (key == null) {
      return "";
    }
    String keyName = key.getExternalName();
    TextAttributesKey fallbackKey = key.getFallbackAttributeKey();
    assertNotSame(fallbackKey, key);
    return fallbackKey == null ? keyName : (keyName + " => " + serializeTextAttributeKey(fallbackKey));
  }

}
