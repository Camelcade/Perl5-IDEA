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

import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.highlighting.actions.HighlightUsagesAction;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.ide.hierarchy.*;
import com.intellij.ide.hierarchy.actions.BrowseHierarchyActionBase;
import com.intellij.ide.structureView.StructureView;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.*;
import com.intellij.injected.editor.EditorWindow;
import com.intellij.lang.ASTNode;
import com.intellij.lang.LanguageStructureViewBuilder;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileFilter;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.impl.DebugUtil;
import com.intellij.psi.impl.PsiManagerEx;
import com.intellij.psi.impl.source.tree.injected.InjectedFileViewProvider;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.MapDataContext;
import com.intellij.testFramework.UsefulTestCase;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl;
import com.intellij.util.Function;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.containers.MultiMap;
import com.perl5.lang.perl.extensions.PerlImplicitVariablesProvider;
import com.perl5.lang.perl.fileTypes.PerlFileTypeScript;
import com.perl5.lang.perl.fileTypes.PerlPluginBaseFileType;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.idea.manipulators.PerlBareStringManipulator;
import com.perl5.lang.perl.idea.manipulators.PerlStringContentManipulator;
import com.perl5.lang.perl.idea.manipulators.PerlStringManipulator;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationBase;
import com.perl5.lang.perl.internals.PerlVersion;
import com.perl5.lang.perl.psi.PerlPolyNamedElement;
import com.perl5.lang.perl.psi.PerlStringContentElement;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import com.perl5.lang.perl.psi.mixins.PerlStringBareMixin;
import com.perl5.lang.perl.psi.mixins.PerlStringMixin;
import gnu.trove.THashSet;
import junit.framework.AssertionFailedError;
import org.intellij.plugins.intelliLang.inject.InjectLanguageAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by hurricup on 04.03.2016.
 */
public abstract class PerlLightCodeInsightFixtureTestCase extends LightCodeInsightFixtureTestCase {
  private static final String PERL_LIBRARY_NAME = "-perl-test-lib-";
  private static final String START_FOLD = "<fold\\stext=\'[^\']*\'(\\sexpand=\'[^\']*\')*>";
  private static final String END_FOLD = "</fold>";
  private static final VirtualFileFilter PERL_FILE_FLTER = file -> file.getFileType() instanceof PerlPluginBaseFileType;
  private TextAttributes myReadAttributes;
  private TextAttributes myWriteAttributes;
  private Disposable myDisposable;

  public String getFileExtension() {
    return PerlFileTypeScript.EXTENSION_PL;
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    myDisposable = Disposer.newDisposable();
    EditorColorsScheme scheme = EditorColorsManager.getInstance().getGlobalScheme();
    myReadAttributes = scheme.getAttributes(EditorColors.SEARCH_RESULT_ATTRIBUTES);
    myWriteAttributes = scheme.getAttributes(EditorColors.WRITE_SEARCH_RESULT_ATTRIBUTES);
    ElementManipulators.INSTANCE.addExplicitExtension(PerlStringMixin.class, new PerlStringManipulator());
    ElementManipulators.INSTANCE.addExplicitExtension(PerlStringBareMixin.class, new PerlBareStringManipulator());
    ElementManipulators.INSTANCE.addExplicitExtension(PerlStringContentElement.class, new PerlStringContentManipulator());
    setUpLibrary();
  }

  @Override
  protected void tearDown() throws Exception {
    myDisposable.dispose();
    super.tearDown();
  }

  public String getTestResultsFilePath() {
    return getTestResultsFilePath("");
  }

  public String getTestResultsFilePath(@NotNull String appendix) {
    return getTestDataPath() + "/" + getTestName(true) + appendix + "." + getFileExtension() + ".txt";
  }

  protected void setTargetPerlVersion(@NotNull PerlVersion perlVersion) {
    PerlSharedSettings.getInstance(getProject()).setTargetPerlVersion(perlVersion).settingsUpdated();
  }

  protected void setUpLibrary() {
    ApplicationManager.getApplication().runWriteAction(
      () ->
      {
        ModifiableRootModel modifiableModel =
          ModuleRootManager.getInstance(myModule).getModifiableModel();

        for (OrderEntry entry : modifiableModel.getOrderEntries()) {
          if (entry instanceof LibraryOrderEntry &&
              StringUtil
                .equals(((LibraryOrderEntry)entry).getLibraryName(), PERL_LIBRARY_NAME)) {
            modifiableModel.removeOrderEntry(entry);
          }
        }

        //			TempDirTestFixture tempDirFixture = myFixture.getTempDirFixture();
        //			tempDirFixture.copyAll("testData/testlib", "testlib");

        LibraryTable moduleLibraryTable = modifiableModel.getModuleLibraryTable();
        Library library = moduleLibraryTable.createLibrary(PERL_LIBRARY_NAME);
        Library.ModifiableModel libraryModifyableModel = library.getModifiableModel();
        VirtualFile libdir =
          LocalFileSystem.getInstance().refreshAndFindFileByPath("testData/testlib");
        assert libdir != null;
        //libdir.refresh(false, true); // this is necessary for manual update
        libraryModifyableModel.addRoot(libdir,
                                       OrderRootType.CLASSES); // myFixture.findFileInTempDir("testlib")
        libraryModifyableModel.commit();
        modifiableModel.commit();
        CodeInsightTestFixtureImpl.ensureIndexesUpToDate(getProject());
      });
  }

  public void initWithPerlTidy() {
    initWithPerlTidy("perlTidy");
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

  @NotNull
  protected PsiFile getTopFile() {
    PsiFile file = getFile();
    if (file.getViewProvider() instanceof InjectedFileViewProvider) {
      //noinspection ConstantConditions
      file = file.getContext().getContainingFile();
    }
    return file;
  }

  @NotNull
  protected Editor getTopEditor() {
    Editor editor = getEditor();
    while (editor instanceof EditorWindow) {
      editor = ((EditorWindow)editor).getDelegate();
    }
    return editor;
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

  public void initWithFileSmartWithoutErrors() {
    initWithFileSmartWithoutErrors(getTestName(true));
  }

  public void initWithFileSmartWithoutErrors(@NotNull String filename) {
    initWithFileSmart(filename);
    assertNoErrorElements();
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

  public void initWithTextSmart(String content) {
    try {
      initWithFileContent("test", getFileExtension(), content);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
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

  public void initWithFile(String filename, String extension) throws IOException {
    initWithFile(filename, extension, filename + ".code");
  }

  public void initWithFile(String targetFileName, String targetFileExtension, String sourceFileNameWithExtension) throws IOException {
    initWithFileContent(targetFileName, targetFileExtension,
                        FileUtil.loadFile(new File(getTestDataPath(), sourceFileNameWithExtension), CharsetToolkit.UTF8, true));
  }

  public void initWithFileContent(String filename, String extension, String content) throws IOException {
    myFixture.configureByText(filename + "." + extension, content);
  }

  @NotNull
  protected <T extends PsiElement> T getElementAtCaret(@NotNull Class<T> clazz) {
    int offset = myFixture.getEditor().getCaretModel().getOffset();
    PsiElement focused = myFixture.getFile().findElementAt(offset);
    return ObjectUtils.assertNotNull(PsiTreeUtil.getParentOfType(focused, clazz, false));
  }

  @NotNull
  protected <T extends PsiElement> T getElementAtCaret(int caretIndex, @NotNull Class<T> clazz) {
    CaretModel caretModel = myFixture.getEditor().getCaretModel();
    assertTrue(caretModel.getCaretCount() > caretIndex);
    Caret caret = caretModel.getAllCarets().get(caretIndex);
    int offset = caret.getOffset();
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

  protected void testSmartKeyFile(String filename, char typed) {
    initWithFileSmart(filename);
    myFixture.type(typed);
    checkResultByFile(filename);
  }

  protected void testSmartKey(String original, char typed, String expected) {
    initWithTextSmart(original);
    myFixture.type(typed);
    myFixture.checkResult(expected);
  }

  protected void testLiveTemplateFile(String filename) {
    initWithFileSmart(filename);
    myFixture.performEditorAction(IdeActions.ACTION_EXPAND_LIVE_TEMPLATE_BY_TAB);
    checkResultByFile(filename);
  }

  protected void checkResultByFile(String filenameWithoutExtension) {
    String checkFileName = filenameWithoutExtension + ".txt";


    myFixture.checkResultByFile(checkFileName);
  }

  public final void doTestCompletion() {
    doTestCompletion("");
  }

  public final void doTestCompletion(@NotNull String answerSuffix) {
    initWithFileSmart();
    doTestCompletionCheck(answerSuffix);
  }

  public final void doTestCompletionFromText(@NotNull String content) {
    initWithTextSmart(content);
    doTestCompletionCheck("");
  }

  private void addVirtualFileFilter() {
    ((PsiManagerEx)myFixture.getPsiManager()).setAssertOnFileLoadingFilter(PERL_FILE_FLTER, myDisposable);
  }

  private void removeVirtualFileFilter() {
    ((PsiManagerEx)myFixture.getPsiManager()).setAssertOnFileLoadingFilter(VirtualFileFilter.NONE, myDisposable);
  }

  private void doTestCompletionCheck(@NotNull String answerSuffix) {
    CodeInsightTestFixtureImpl.ensureIndexesUpToDate(getProject());
    addVirtualFileFilter();
    myFixture.complete(CompletionType.BASIC, 1);
    List<String> result = new ArrayList<>();
    LookupElement[] elements = myFixture.getLookupElements();
    removeVirtualFileFilter();
    if (elements != null) {
      for (LookupElement lookupElement : elements) {
        StringBuilder sb = new StringBuilder();
        LookupElementPresentation presentation = new LookupElementPresentation();
        lookupElement.renderElement(presentation);

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

  private String getIconText(@Nullable Icon icon) {
    if (icon == null) {
      return "null";
    }
    String iconString = icon.toString();
    return iconString.substring(iconString.lastIndexOf('/'));
  }

  @Deprecated
  public void assertLookupDoesntContains(List<String> expected) {
    List<String> lookups = myFixture.getLookupElementStrings();
    assertNotNull(lookups);
    UsefulTestCase.assertDoesntContain(lookups, expected);
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
    new WriteCommandAction.Simple(getProject()) {
      @Override
      protected void run() throws Throwable {
        PsiFile file = myFixture.getFile();
        if (file.getViewProvider() instanceof InjectedFileViewProvider) {
          //noinspection ConstantConditions
          file = file.getContext().getContainingFile();
        }
        TextRange rangeToUse = file.getTextRange();
        CodeStyleManager.getInstance(getProject()).reformatText(file, rangeToUse.getStartOffset(), rangeToUse.getEndOffset());
      }
    }.execute();

    String resultFilePath = getTestDataPath() + "/" + resultFileName + resultSuffix + ".txt";
    UsefulTestCase.assertSameLinesWithFile(resultFilePath, myFixture.getFile().getText());
    assertNoErrorElements();
  }

  protected void assertNoErrorElements() {
    assertFalse(
      "PsiFile contains error elements",
      DebugUtil.psiToString(getFile(), true, false).contains("PsiErrorElement")
    );
  }

  protected void addCustomPackage() {
    myFixture.copyFileToProject("MyCustomPackage.pm");
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

    StringBuilder result = new StringBuilder();
    myFixture.testAction(new HighlightUsagesAction());
    List<RangeHighlighter> highlighters = Arrays.asList(getEditor().getMarkupModel().getAllHighlighters());
    ContainerUtil.sort(highlighters, Comparator.comparingInt(RangeMarker::getStartOffset));

    for (RangeHighlighter highlighter : highlighters) {
      TextAttributes attributes = highlighter.getTextAttributes();

      TextRange range = TextRange.create(highlighter.getStartOffset(), highlighter.getEndOffset());
      CharSequence text = highlighter.getDocument().getCharsSequence();

      String type;
      if (attributes == myReadAttributes) {
        type = "READ";
      }
      else if (attributes == myWriteAttributes) {
        type = "WRITE";
      }
      else if (attributes == null) {
        type = "n/a";
      }
      else {
        type = attributes.toString();
      }

      result
        .append(range)
        .append(" - '")
        .append(range.subSequence(text))
        .append("': ")
        .append(type)
        .append("\n");
    }


    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), result.toString());
  }

  protected void doTestAnnotationQuickFix(@NotNull Class inspectionClass, @NotNull String quickFixNamePrefix) {
    initWithFileSmartWithoutErrors();
    myFixture.enableInspections(inspectionClass);
    //myFixture.checkHighlighting(true, false, false);
    doTestIntention(quickFixNamePrefix);
  }

  @NotNull
  protected IntentionAction getSingleIntention(@NotNull String prefixOrName) {
    IntentionAction intention = null;
    for (IntentionAction intentionAction : myFixture.getAvailableIntentions()) {
      if (intentionAction.getText().equals(prefixOrName)) {
        intention = intentionAction;
        break;
      }
      else if (intentionAction.getText().startsWith(prefixOrName)) {
        if (intention != null) {
          assertNull("Clarify prefix, too many with: " + prefixOrName + " " + intention.getText() + " and " + intentionAction.getText(),
                     intention);
        }
        intention = intentionAction;
      }
    }

    assertNotNull("Couldn't find intention: " + prefixOrName, intention);
    return intention;
  }

  protected void doTestIntention(@NotNull String intentionPrefix) {
    myFixture.launchAction(getSingleIntention(intentionPrefix));
    assertNoErrorElements();
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
    ItemPresentation presentation = currentElement.getPresentation();
    assertNotNull(presentation);
    String locationString = presentation.getLocationString();
    sb.append(prefix)
      .append(presentation.getPresentableText())
      .append(" in ")
      .append(locationString == null ? null : locationString.replaceAll("\\\\", "/"))
      .append("; ")
      .append(getIconText(presentation.getIcon(true)));

    if (presentation instanceof PerlItemPresentationBase) {
      sb.append("; ").append(((PerlItemPresentationBase)presentation).getTextAttributesKey());
    }

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
      Field myType2TreeMap = HierarchyBrowserBaseEx.class.getDeclaredField("myType2TreeMap");
      myType2TreeMap.setAccessible(true);
      Method createHierarchyTreeStructure =
        browser.getClass().getDeclaredMethod("createHierarchyTreeStructure", String.class, PsiElement.class);
      createHierarchyTreeStructure.setAccessible(true);
      Method getContentDisplayName = browser.getClass().getDeclaredMethod("getContentDisplayName", String.class, PsiElement.class);
      getContentDisplayName.setAccessible(true);
      Method getElementFromDescriptor = browser.getClass().getDeclaredMethod("getElementFromDescriptor", HierarchyNodeDescriptor.class);
      getElementFromDescriptor.setAccessible(true);

      Map<String, JTree> subTrees = (Map<String, JTree>)myType2TreeMap.get(browser);
      List<String> treesNames = new ArrayList<>(subTrees.keySet());
      ContainerUtil.sort(treesNames);
      for (String treeName : treesNames) {
        sb.append("----------------------------------------------------------------------------------\n")
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

  public void doTestResolve() {
    initWithFileSmart();
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
    final List<PsiReference> references = new ArrayList<PsiReference>();

    PsiFile file = getFile();

    file.accept(new PsiElementVisitor() {
      @Override
      public void visitElement(PsiElement element) {
        Collections.addAll(references, element.getReferences());
        element.acceptChildren(this);
      }
    });

    references.sort((o1, o2) -> o1.getElement().getTextRange().getStartOffset() + o1.getRangeInElement().getStartOffset() -
                                o2.getElement().getTextRange().getStartOffset() + o2.getRangeInElement().getStartOffset());

    return references;
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
}
