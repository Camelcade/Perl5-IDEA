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

package com.perl5.lang.perl.documentation;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiRecursiveElementVisitor;
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
import com.perl5.lang.pod.parser.psi.mixin.PodSectionItem;
import com.perl5.lang.pod.parser.psi.util.PodFileUtil;
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.perl5.lang.perl.lexer.PerlTokenSets.*;
import static com.perl5.lang.perl.util.PerlSubUtil.SUB_AUTOLOAD;
import static com.perl5.lang.perl.util.PerlSubUtil.SUB_DESTROY;


public class PerlDocUtil implements PerlElementTypes {
  public static final String PERL_VAR_FILE_NAME = "perlvar.pod";
  public static final String PERL_FUNC_FILE_NAME = "perlfunc.pod";
  public static final String PERL_OP_FILE_NAME = "perlop.pod";
  static final PodLinkDescriptor SWITCH_DOC_LINK = PodLinkDescriptor.create("perlsyn", "Switch Statements");
  static final PodLinkDescriptor SPECIAL_LITERALS_LINK = PodLinkDescriptor.create("perldata", "Special Literals");
  private static final PodLinkDescriptor MODIFIERS_DOC_LINK = PodLinkDescriptor.create("perlsyn", "Statement Modifiers");
  private static final PodLinkDescriptor COMPOUND_DOC_LINK = PodLinkDescriptor.create("perlsyn", "Compound Statements");
  private static final PodLinkDescriptor BLOCK_NAMES_LINK = PodLinkDescriptor.create("perlmod", "BEGIN, UNITCHECK, CHECK, INIT and END");
  private static final PodLinkDescriptor AUTOLOAD_LINK = PodLinkDescriptor.create("perlsub", "Autoloading");
  private static final PodLinkDescriptor DESTROY_LINK = PodLinkDescriptor.create("perlobj", "Destructors");

  private static final Map<String, PodLinkDescriptor> OPERATORS_LINKS = new HashMap<>();
  private static final Map<String, PodLinkDescriptor> VARIABLES_LINKS = new HashMap<>();

  static {
    OPERATORS_LINKS.put("~~", PodLinkDescriptor.create("perlop", "Smartmatch Operator"));
    OPERATORS_LINKS.put("qr", PodLinkDescriptor.create("perlop", "qr/STRING/"));
    OPERATORS_LINKS.put("s", PodLinkDescriptor.create("perlop", "s/PATTERN/"));
    OPERATORS_LINKS.put("m", PodLinkDescriptor.create("perlop", "m/PATTERN/"));
    OPERATORS_LINKS.put("=>", PodLinkDescriptor.create("perlop", "Comma Operator"));

    VARIABLES_LINKS.put("@ISA", PodLinkDescriptor.create("perlobj", "A Class is Simply a Package"));
    VARIABLES_LINKS.put("@EXPORT", PodLinkDescriptor.create("Exporter", "How to Export"));
    VARIABLES_LINKS.put("@EXPORT_OK", PodLinkDescriptor.create("Exporter", "How to Export"));
    VARIABLES_LINKS.put("%EXPORT_TAGS", PodLinkDescriptor.create("Exporter", "Specialised Import Lists"));
    VARIABLES_LINKS.put("$VERSION", PodLinkDescriptor.create("perlobj", "VERSION"));
  }

  @Nullable
  public static PsiElement resolveDoc(@NotNull String file, @Nullable String section, @NotNull PsiElement origin, boolean exactMatch) {
    return resolveDescriptor(PodLinkDescriptor.create(file, section), origin, exactMatch);
  }

  @Nullable
  public static PsiElement resolveDescriptor(@Nullable PodLinkDescriptor descriptor, @NotNull PsiElement origin, boolean exactMatch) {
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
        anchor = "" + firstChar;
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

  @Nullable
  public static PsiElement getPerlVarDoc(PerlVariable variable) {
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

  @Nullable
  public static PsiElement getPerlFuncDoc(PsiElement element) {
    IElementType elementType = PsiUtilCore.getElementType(element);
    CharSequence tokenChars = element.getNode().getChars();

    PodLinkDescriptor redirect = null;
    if (MODIFIERS_KEYWORDS_TOKENSET.contains(elementType) && element.getParent() instanceof PsiPerlStatementModifier) {
      redirect = MODIFIERS_DOC_LINK;
    }
    else if (SWITCH_KEYWORDS_TOKENSET.contains(elementType)) {
      redirect = SWITCH_DOC_LINK;
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

  @Nullable
  public static PsiElement getPerlOpDoc(@NotNull PsiElement element) {
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

  protected static PodCompositeElement searchPodElementInFile(Project project, String fileName, PodDocumentPattern pattern) {
    PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, fileName, GlobalSearchScope.allScope(project));
    if (psiFiles.length > 0) {
      return searchPodElement(psiFiles[0], pattern);
    }
    return null;
  }

  @Nullable
  public static PodCompositeElement searchPodElement(@Nullable PsiFile psiFile, final PodDocumentPattern pattern) {
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
      public void visitElement(PsiElement element) {
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
  @Nullable
  @Contract("null -> null")
  public static PsiElement findPrependingPodBlock(@Nullable PsiElement element) {
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

  /**
   * Attempts to find a named section in the POD element and generates documentation from it.
   *
   * @param podElement POD element of the perl file
   * @return generated documentation or null
   */
  @Nullable
  public static String renderPodElement(@NotNull PsiElement podElement) {
    PsiFile podPsi = podElement.getContainingFile().getViewProvider().getPsi(PodLanguage.INSTANCE);
    if (podPsi == null) {
      return null;
    }
    TextRange podElementRange = podElement.getTextRange();
    Ref<String> resultRef = Ref.create();
    podPsi.accept(new PsiRecursiveElementVisitor() {
      private boolean stopProcessing = false;

      @Override
      public void visitElement(PsiElement element) {
        if (stopProcessing) {
          return;
        }
        if (!podElementRange.intersects(element.getTextRange())) {
          return;
        }
        else if (element instanceof PodTitledSection && podElementRange.contains(element.getTextRange())) {
          resultRef.set(renderElement((PodTitledSection)element));
          stopProcessing = true;
          return;
        }
        super.visitElement(element);
      }
    });
    return resultRef.get();
  }

  @Contract("null->null")
  @Nullable
  public static String renderElement(@Nullable PodSection podSection) {
    if (podSection == null) {
      return null;
    }
    boolean hasContent = podSection.hasContent();
    PsiElement run = podSection;
    PsiElement lastSection = podSection;

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

    // detecting last section
    while (!hasContent) {
      PsiElement nextSibling = lastSection.getNextSibling();

      if (nextSibling == null) {
        break;
      }
      hasContent = nextSibling instanceof PodSection && ((PodSection)nextSibling).hasContent();

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

  private static class PodTocBuilder implements PsiElementProcessor {
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
