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

package com.perl5.lang.perl.documentation;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Predicates;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.TokenType;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiElementProcessor;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.parser.psi.*;
import com.perl5.lang.pod.parser.psi.impl.PodFileImpl;
import com.perl5.lang.pod.parser.psi.mixin.PodFormatterX;
import com.perl5.lang.pod.parser.psi.mixin.PodSectionItem;
import com.perl5.lang.pod.parser.psi.util.PodFileUtil;
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.perl5.lang.perl.lexer.PerlTokenSets.*;
import static com.perl5.lang.perl.util.PerlCorePackages.PACKAGE_EXPORTER;
import static com.perl5.lang.perl.util.PerlPackageUtil.ADJUST_BLOCK;
import static com.perl5.lang.perl.util.PerlSubUtil.SUB_AUTOLOAD;
import static com.perl5.lang.perl.util.PerlSubUtil.SUB_DESTROY;


public final class PerlDocUtil implements PerlElementTypes {
  public static final String PERL_VAR_FILE_NAME = "perlvar.pod";
  public static final String PERL_FUNC_FILE_NAME = "perlfunc.pod";
  static final String PERL_OP = "perlop";
  public static final String PERL_OP_FILE_NAME = PERL_OP + ".pod";
  static final PodLinkDescriptor SWITCH_DOC_LINK = PodLinkDescriptor.create("perlsyn", "Switch Statements");
  static final PodLinkDescriptor SPECIAL_LITERALS_LINK = PodLinkDescriptor.create("perldata", "Special Literals");
  private static final PodLinkDescriptor MODIFIERS_DOC_LINK = PodLinkDescriptor.create("perlsyn", "Statement Modifiers");
  private static final PodLinkDescriptor COMPOUND_DOC_LINK = PodLinkDescriptor.create("perlsyn", "Compound Statements");
  private static final PodLinkDescriptor BLOCK_NAMES_LINK = PodLinkDescriptor.create("perlmod", "BEGIN, UNITCHECK, CHECK, INIT and END");
  private static final PodLinkDescriptor AUTOLOAD_LINK = PodLinkDescriptor.create("perlsub", "Autoloading");
  private static final String PERLDOC_PERLOBJ = "perlobj";
  private static final PodLinkDescriptor DESTROY_LINK = PodLinkDescriptor.create(PERLDOC_PERLOBJ, "Destructors");

  private static final Map<String, PodLinkDescriptor> OPERATORS_LINKS = Map.of(
    "~~", PodLinkDescriptor.create(PERL_OP, "Smartmatch Operator"),
    "qr", PodLinkDescriptor.create(PERL_OP, "qr/STRING/"),
    "s", PodLinkDescriptor.create(PERL_OP, "s/PATTERN/"),
    "m", PodLinkDescriptor.create(PERL_OP, "m/PATTERN/"),
    "=>", PodLinkDescriptor.create(PERL_OP, "Comma Operator"),
    "isa", PodLinkDescriptor.create(PERL_OP, "isa operator")
  );
  private static final Map<String, PodLinkDescriptor> VARIABLES_LINKS = Map.of(
    "@ISA", PodLinkDescriptor.create(PERLDOC_PERLOBJ, "A Class is Simply a Package"),
    "@EXPORT", PodLinkDescriptor.create(PACKAGE_EXPORTER, "How to Export"),
    "@EXPORT_OK", PodLinkDescriptor.create(PACKAGE_EXPORTER, "How to Export"),
    "%EXPORT_TAGS", PodLinkDescriptor.create(PACKAGE_EXPORTER, "Specialised Import Lists"),
    "$VERSION", PodLinkDescriptor.create(PERLDOC_PERLOBJ, "VERSION")
  );


  private PerlDocUtil() {
  }

  public static @Nullable PsiElement resolveDoc(@NotNull String file,
                                                @Nullable String section,
                                                @NotNull PsiElement origin,
                                                boolean exactMatch) {
    return resolveDescriptor(PodLinkDescriptor.create(file, section), origin, exactMatch);
  }

  public static @Nullable PsiElement resolveDescriptor(@Nullable PodLinkDescriptor descriptor,
                                                       @NotNull PsiElement origin,
                                                       boolean exactMatch) {
    final Project project = origin.getProject();

    if (descriptor == null) {
      return null;
    }
    PsiFile targetFile = PodFileUtil.getPodOrPackagePsiByDescriptor(project, descriptor);

    if (targetFile == null) {
      PsiFile containingFile = origin.getContainingFile();
      if (containingFile == null) {
        return null;
      }
      targetFile = containingFile.getViewProvider().getPsi(PodLanguage.INSTANCE);
    }

    if (targetFile == null) {
      return null;
    }
    if (descriptor.getSection() == null) {
      return targetFile;
    }

    PodDocumentPattern pattern = PodDocumentPattern.headingAndItemPattern(descriptor.getSection())
      .withIndexPattern(descriptor.getSection());
    if (exactMatch) {
      pattern.withExactMatch();
    }
    return searchPodElement(targetFile, pattern);
  }

  public static PsiElement getRegexModifierDoc(PsiElement element) {
    String text = element.getText();
    assert text != null;
    String anchor = "Modifiers";

    if (text.length() > 0) {
      char firstChar = text.charAt(0);
      if (StringUtil.containsChar("msixpn", firstChar)) {
        anchor = String.valueOf(firstChar);
      }
      else if (StringUtil.containsChar("adlux", firstChar)) {
        anchor = "/" + firstChar;
      }
      else if (StringUtil.containsChar("cgeor", firstChar)) {
        anchor = "Other Modifiers";
      }
    }

    return resolveDescriptor(PodLinkDescriptor.create("perlre", anchor), element, false);
  }

  public static @Nullable PsiElement getPerlVarDoc(PerlVariable variable) {
    final Project project = variable.getProject();

    PerlVariableType actualType = variable.getActualType();
    String variableName = variable.getName();

    if (actualType != null && StringUtil.isNotEmpty(variableName)) {
      String text = actualType.getSigil() + PerlVariable.braceName(variableName);

      if (VARIABLES_LINKS.containsKey(text)) {
        return resolveDescriptor(VARIABLES_LINKS.get(text), variable, false);
      }

      if (variable.isBuiltIn()) {
        PodDocumentPattern pattern;
        if (text.matches("\\$[1-9]\\d*")) {
          pattern = PodDocumentPattern.indexPattern("$1");
        }
        else {
          pattern = PodDocumentPattern.itemPattern(text).withExactMatch();
        }

        return searchPodElementInFile(project, PERL_VAR_FILE_NAME, pattern);
      }
    }

    return null;
  }

  public static @Nullable PsiElement getPerlFuncDoc(PsiElement element) {
    IElementType elementType = PsiUtilCore.getElementType(element);
    CharSequence tokenChars = element.getNode().getChars();

    PodLinkDescriptor redirect = null;
    if (MODIFIERS_KEYWORDS_TOKENSET.contains(elementType) && element.getParent() instanceof PsiPerlStatementModifier) {
      redirect = MODIFIERS_DOC_LINK;
    }
    else if (SWITCH_KEYWORDS_TOKENSET.contains(elementType)) {
      redirect = SWITCH_DOC_LINK;
    }
    else if(elementType == RESERVED_CLASS){
      redirect = PodLinkDescriptor.create("perlclass", "class");
    }
    else if(elementType == RESERVED_FIELD){
      redirect = PodLinkDescriptor.create("perlclass", "field");
    }
    else if (COMPOUND_KEYWORDS_TOKENSET.contains(elementType)) {
      redirect = COMPOUND_DOC_LINK;
    }
    else if (TAGS_TOKEN_SET.contains(elementType)) {
      redirect = SPECIAL_LITERALS_LINK;
    }
    else if (elementType == BLOCK_NAME) {
      if (StringUtil.equals(tokenChars, SUB_AUTOLOAD)) {
        redirect = AUTOLOAD_LINK;
      }
      else if( StringUtil.equals(tokenChars, ADJUST_BLOCK)){
        redirect = PodLinkDescriptor.create("perlclass", "Adjustment");
      }
      else if (StringUtil.equals(tokenChars, SUB_DESTROY)) {
        redirect = DESTROY_LINK;
      }
      else {
        redirect = BLOCK_NAMES_LINK;
      }
    }

    return redirect != null ?
           resolveDescriptor(redirect, element, false) :
           ObjectUtils.coalesce(getPerlFuncDocFromText(element, tokenChars.toString()), getPerlOpDoc(element));
  }

  static PsiElement getPerlFuncDocFromText(@NotNull PsiElement element, @NotNull String text) {
    if (text.matches("-[rwxoRWXOeszfdlpSbctugkTBMAC]")) {
      text = "-X";
    }

    return searchPodElementInFile(element.getProject(), PERL_FUNC_FILE_NAME, PodDocumentPattern.itemPattern(text));
  }

  public static @Nullable PsiElement getPerlOpDoc(@NotNull PsiElement element) {
    IElementType elementType = PsiUtilCore.getElementType(element);
    if (elementType == LEFT_ANGLE || elementType == RIGHT_ANGLE) {
      return resolveDoc(PERL_OP, "operator, i/o", element, true);
    }
    else if (elementType == STRING_SPECIAL_SUBST) {
      return resolveDoc(PERL_OP, "\\c", element, true);
    }
    else if (elementType == STRING_SPECIAL_OCT || elementType == STRING_SPECIAL_OCT_AMBIGUOUS) {
      return resolveDoc(PERL_OP, "\\o{}", element, true);
    }
    else if (elementType == STRING_SPECIAL_ESCAPE_CHAR) {
      IElementType parentElementType = PsiUtilCore.getElementType(element.getParent());
      if (parentElementType == STRING_SQ) {
        return resolveDoc(PERL_OP, "'STRING'", element, true);
      }
      return resolveDoc(PERL_OP, "escape", element, true);
    }
    if (elementType == STRING_SPECIAL_LEFT_BRACE ||
        elementType == STRING_SPECIAL_RIGHT_BRACE ||
        elementType == STRING_CHAR_NAME) {
      element = element.getParent().getFirstChild();
    }

    final Project project = element.getProject();
    String text = element.getText();

    PodLinkDescriptor redirect = OPERATORS_LINKS.get(text);
    if (redirect != null) {
      return resolveDescriptor(redirect, element, false);
    }

    // fixme use map?
    PodDocumentPattern pattern = PodDocumentPattern.indexPattern(text);
    if (element instanceof PerlHeredocOpener ||
        element instanceof PerlHeredocTerminatorElement ||
        element instanceof PerlHeredocElementImpl) {
      pattern.withIndexPattern("heredoc");    // searches with X<>
    }
    else if (text.matches("-[rwxoRWXOeszfdlpSbctugkTBMAC]")) {
      pattern.withIndexPattern("-X");
    }
    else if ("?".equals(text) || ":".equals(text)) {
      pattern.withIndexPattern("?:");
    }

    return searchPodElementInFile(project, PERL_OP_FILE_NAME, pattern);
  }

  private static PodCompositeElement searchPodElementInFile(@NotNull Project project,
                                                            @NotNull String fileName,
                                                            @NotNull PodDocumentPattern pattern) {
    var psiManager = PsiManager.getInstance(project);
    return FilenameIndex.getVirtualFilesByName(fileName, GlobalSearchScope.allScope(project)).stream()
      .map(psiManager::findFile)
      .map(it -> searchPodElement(it, pattern))
      .filter(Predicates.nonNull())
      .findFirst().orElse(null);
  }

  @Contract("null, _ -> null")
  public static @Nullable PodCompositeElement searchPodElement(@Nullable PsiFile psiFile, @NotNull PodDocumentPattern pattern) {
    if (psiFile == null) {
      return null;
    }

    if (psiFile.getLanguage() != PodLanguage.INSTANCE) {
      psiFile = psiFile.getViewProvider().getPsi(PodLanguage.INSTANCE);
      if (psiFile == null) {
        return null;
      }
    }

    final List<PodCompositeElement> result = new ArrayList<>();

    psiFile.accept(new PsiStubsAwareRecursiveVisitor() {
      @Override
      public void visitElement(@NotNull PsiElement element) {
        if (pattern.accepts(element)) {
          result.add((PodCompositeElement)element);
          stop();
        }
        else {
          super.visitElement(element);
        }
      }
    });

    return result.isEmpty() ? null : result.get(0);
  }

  public static String renderPodFile(PodFileImpl file) {
    StringBuilder builder = new StringBuilder();

    renderFileToc(file, builder);

    PodRenderUtil.renderPsiRangeAsHTML(file.getFirstNamedBlock(), null, builder, new PodRenderingContext());
    return builder.toString();
  }

  protected static void renderFileToc(PsiFile file, final StringBuilder builder) {
    PodTocBuilder tocBuilder = new PodTocBuilder(builder);
    PsiTreeUtil.processElements(file, tocBuilder);
    tocBuilder.adjustLevelTo(0);
  }


  /**
   * Traversing up from {@code element perl element}, skipping comments and whitespaces, looking for a POD block
   *
   * @return POD block or null if not found
   */
  @Contract("null -> null")
  public static @Nullable PsiElement findPrependingPodBlock(@Nullable PsiElement element) {
    if (element == null || element instanceof PsiFile || !element.getLanguage().isKindOf(PerlLanguage.INSTANCE)) {
      return null;
    }

    while (true) {
      PsiElement prevElement = element.getPrevSibling();

      if (prevElement == null) {
        return findPrependingPodBlock(element.getParent());
      }
      element = prevElement;

      IElementType elementType = PsiUtilCore.getElementType(element);
      if (elementType == POD) {
        return element;
      }
      else if (elementType != TokenType.WHITE_SPACE && elementType != COMMENT_LINE) {
        return null;
      }
    }
  }

  @Contract("null->null")
  public static @Nullable String renderElement(@Nullable PodSection podSection) {
    if (podSection == null) {
      return null;
    }
    PsiElement run = podSection;

    // detecting first section
    while (true) {
      PsiElement prevSibling = run.getPrevSibling();
      if (prevSibling == null) {
        break;
      }
      if (prevSibling instanceof PodSection && ((PodSection)prevSibling).hasContent()) {
        break;
      }
      if (prevSibling instanceof PodTitledSection) {
        podSection = (PodTitledSection)prevSibling;
      }
      run = prevSibling;
    }

    boolean isTitledSection = podSection instanceof PodTitledSection;
    boolean hasContent = podSection.hasContent();
    PsiElement lastSection = podSection;

    // detecting last section
    while (true) {
      PsiElement nextSibling = lastSection.getNextSibling();

      boolean isNextTitledSection = nextSibling instanceof PodTitledSection && !(nextSibling instanceof PodFormatterX);

      if (nextSibling == null || (isTitledSection && isNextTitledSection && hasContent)) {
        break;
      }
      hasContent = hasContent || nextSibling instanceof PodSection && ((PodSection)nextSibling).hasContent();

      lastSection = nextSibling;
    }

    StringBuilder builder = new StringBuilder();

    // appending breadcrumbs
    List<String> breadCrumbs = new ArrayList<>();
    run = podSection.getParent();
    while (true) {
      if (run instanceof PodLinkTarget) {
        String bcLink = ((PodLinkTarget)run).getPodLink();
        if (StringUtil.isNotEmpty(bcLink)) {
          breadCrumbs.add(0, PodRenderUtil.getHTMLPsiLink(bcLink, ((PodLinkTarget)run).getPodLinkText()));
        }
      }
      if (run instanceof PsiFile) {
        break;
      }
      run = run.getParent();
    }

    if (!breadCrumbs.isEmpty()) {
      builder.append("<p>");
      builder.append(StringUtil.join(breadCrumbs, ": "));
      builder.append("</p>");
    }

    String closeTag = "";

    if (podSection instanceof PodSectionItem) {
      if (((PodSectionItem)podSection).isBulleted()) {
        builder.append("<ul style=\"fon-size:200%;\">");
        closeTag = "</ul>";
      }
      else {
        builder.append("<dl>");
        closeTag = "</dl>";
      }
    }

    builder.append(PodRenderUtil.renderPsiRangeAsHTML(podSection, lastSection));
    builder.append(closeTag);
    return builder.toString();
  }

  /**
   * @return true iff {@code numberElement} is a hex or oct number parametrizing DQ string substitutions
   */
  static boolean isNumericArgumentToOperator(@NotNull PsiElement numberElement) {
    IElementType elementType = PsiUtilCore.getElementType(numberElement);
    if (elementType != NUMBER_HEX && elementType != NUMBER_OCT) {
      return false;
    }
    return PERL_PARAMETRIZED_STRING_SUBSTITUTIONS.contains(PsiUtilCore.getElementType(numberElement.getParent()));
  }

  private static class PodTocBuilder implements PsiElementProcessor<PsiElement> {
    private final StringBuilder myBuilder;
    private int myHeaderLevel = 0;

    public PodTocBuilder(StringBuilder myBuilder) {
      this.myBuilder = myBuilder;
    }

    @Override
    public boolean execute(@NotNull PsiElement element) {
      if (element instanceof PodTitledSection && ((PodTitledSection)element).isHeading()) {
        adjustLevelTo(((PodTitledSection)element).getHeadingLevel());
        myBuilder.append("<li style=\"margin-top:3px;margin-bottom:2px;\">");
        myBuilder.append(PodRenderUtil.getHTMLPsiLink(((PodTitledSection)element)));
        myBuilder.append("</li>");
      }

      return true;
    }

    public void adjustLevelTo(int level) {
      while (myHeaderLevel < level) {
        if (myHeaderLevel == 0) {
          myBuilder.append("<ul style=\"margin-left: 7px;margin-top:0px;margin-bottom:0px;\">");
        }
        else {
          myBuilder.append("<ul style=\"margin-left: 20px;margin-top:0px;margin-bottom:0px;\">");
        }
        myHeaderLevel++;
      }
      while (myHeaderLevel > level) {
        myBuilder.append("</ul>");
        myHeaderLevel--;
      }
    }
  }
}
