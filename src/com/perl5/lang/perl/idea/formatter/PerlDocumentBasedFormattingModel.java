package com.perl5.lang.perl.idea.formatter;

import com.intellij.formatting.Block;
import com.intellij.formatting.FormattingModelWithShiftIndentInsideDocumentRange;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.formatter.DocumentBasedFormattingModel;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.lexer.PerlTokenSets.HEREDOC_BODIES_TOKENSET;

/**
 * Created by hurricup on 22.05.2017.
 */
public class PerlDocumentBasedFormattingModel extends DocumentBasedFormattingModel implements
                                                                                   FormattingModelWithShiftIndentInsideDocumentRange {
  private final CommonCodeStyleSettings.IndentOptions myIndentOptions;

  public PerlDocumentBasedFormattingModel(@NotNull Block rootBlock,
                                          @NotNull PsiElement element,
                                          @NotNull CodeStyleSettings settings
  ) {
    super(rootBlock, element.getProject(), settings, element.getContainingFile().getFileType(), element.getContainingFile());
    myIndentOptions = settings.getCommonSettings(PerlLanguage.INSTANCE).getIndentOptions();
  }

  @Override
  public String adjustWhiteSpaceInsideDocument(ASTNode node, String whiteSpace) {
    return whiteSpace;
  }

  @Override
  public TextRange shiftIndentInsideDocumentRange(Document document, ASTNode node, TextRange range, int indent) {
    if (!HEREDOC_BODIES_TOKENSET.contains(PsiUtilCore.getElementType(node))) {
      return null;
    }
    PsiElement psi = node.getPsi();
    assert psi instanceof PerlHeredocElementImpl : "Got " + psi + " instead of heredoc from " + PsiUtilCore.getElementType(node);
    if (!((PerlHeredocElementImpl)psi).isIndentable()) {
      return null;
    }

    final int newLength = shiftIndentInside(range, indent);
    return new TextRange(range.getStartOffset(), range.getStartOffset() + newLength);
  }

  // code below is a copypaste from badly extendable parent class
  private int shiftIndentInside(final TextRange elementRange, final int shift) {
    final StringBuilder buffer = new StringBuilder();
    StringBuilder afterWhiteSpace = new StringBuilder();
    int whiteSpaceLength = 0;
    boolean insideWhiteSpace = true;
    int line = 0;
    for (int i = elementRange.getStartOffset(); i < elementRange.getEndOffset(); i++) {
      final char c = getDocument().getCharsSequence().charAt(i);
      switch (c) {
        case '\n':
          if (line > 0) {
            createWhiteSpace(whiteSpaceLength + shift, buffer);
          }
          buffer.append(afterWhiteSpace.toString());
          insideWhiteSpace = true;
          whiteSpaceLength = 0;
          afterWhiteSpace = new StringBuilder();
          buffer.append(c);
          line++;
          break;
        case ' ':
          if (insideWhiteSpace) {
            whiteSpaceLength += 1;
          }
          else {
            afterWhiteSpace.append(c);
          }
          break;
        case '\t':
          if (insideWhiteSpace) {
            whiteSpaceLength += getIndentOptions().TAB_SIZE;
          }
          else {
            afterWhiteSpace.append(c);
          }

          break;
        default:
          insideWhiteSpace = false;
          afterWhiteSpace.append(c);
      }
    }
    if (line > 0 && afterWhiteSpace.length() > 0) {
      createWhiteSpace(whiteSpaceLength + shift, buffer);
      buffer.append(afterWhiteSpace.toString());
    }
    getDocument().replaceString(elementRange.getStartOffset(), elementRange.getEndOffset(), buffer.toString());
    return buffer.length();
  }

  private void createWhiteSpace(final int whiteSpaceLength, StringBuilder buffer) {
    if (whiteSpaceLength < 0) return;
    final CommonCodeStyleSettings.IndentOptions indentOptions = getIndentOptions();
    if (indentOptions.USE_TAB_CHARACTER) {
      int tabs = whiteSpaceLength / indentOptions.TAB_SIZE;
      int spaces = whiteSpaceLength - tabs * indentOptions.TAB_SIZE;
      StringUtil.repeatSymbol(buffer, '\t', tabs);
      StringUtil.repeatSymbol(buffer, ' ', spaces);
    }
    else {
      StringUtil.repeatSymbol(buffer, ' ', whiteSpaceLength);
    }
  }

  private CommonCodeStyleSettings.IndentOptions getIndentOptions() {
    return myIndentOptions;
  }
}
