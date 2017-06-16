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

package com.perl5.lang.perl.idea.formatter.blocks;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.formatter.FormatterUtil;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.Function;
import com.intellij.util.containers.FactoryMap;
import com.perl5.lang.perl.idea.formatter.PerlFormattingContext;
import com.perl5.lang.perl.idea.formatter.PerlIndentProcessor;
import com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.parser.PerlParserUtil;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.perl5.lang.perl.lexer.PerlTokenSets.HEREDOC_BODIES_TOKENSET;

/**
 * Created by hurricup on 03.09.2015.
 */
public class PerlFormattingBlock extends AbstractBlock implements PerlElementTypes {
  /**
   * Composite elements that should be treated as leaf elements, no children
   */
  public final static TokenSet LEAF_ELEMENTS =
    TokenSet.create(
      STRING_SQ,
      STRING_DQ,
      STRING_XQ,
      POD,
      PerlParserUtil.DUMMY_BLOCK
    );

  /**
   * Elements that must have LF between them
   */
  public final static TokenSet LF_ELEMENTS = TokenSet.create(
    LABEL_DECLARATION,
    STATEMENT,
    FOR_COMPOUND,
    FOREACH_COMPOUND,
    WHILE_COMPOUND,
    WHEN_COMPOUND,
    UNTIL_COMPOUND,
    IF_COMPOUND,
    USE_STATEMENT
  );

  public final static TokenSet BLOCK_OPENERS = TokenSet.create(
    LEFT_BRACE,
    LEFT_BRACKET,
    LEFT_PAREN
  );

  public final static TokenSet BLOCK_CLOSERS = TokenSet.create(
    RIGHT_BRACE,
    RIGHT_BRACKET,
    RIGHT_PAREN,

    SEMICOLON
  );
  protected final PerlFormattingContext myContext;
  private final Indent myIndent;
  private final boolean myIsFirst;
  private final boolean myIsLast;
  private final IElementType myElementType;
  private List<Block> mySubBlocks;

  public PerlFormattingBlock(
    @NotNull ASTNode node,
    @Nullable Wrap wrap,
    @Nullable Alignment alignment,
    @NotNull PerlFormattingContext context
  ) {
    super(node, wrap, alignment);
    myContext = context;
    myIndent = context.getIndentProcessor().getNodeIndent(node);
    myIsFirst = FormatterUtil.getPreviousNonWhitespaceSibling(node) == null;
    myIsLast = FormatterUtil.getNextNonWhitespaceSibling(node) == null;
    myElementType = node.getElementType();
  }

  @NotNull
  @Override
  protected List<Block> buildChildren() {
    if (mySubBlocks == null) {
      mySubBlocks = buildSubBlocks();
    }

    // fixme what is re-creation for?
    return new ArrayList<>(mySubBlocks);
  }

  protected List<Block> buildSubBlocks() {
    if (isLeaf()) {
      return Collections.emptyList();
    }
    final List<Block> blocks = new ArrayList<>();

    int[] relativeLineNumber = new int[]{0};

    IElementType elementType = getElementType();
    Alignment alignment = Alignment.createAlignment(true);
    Function<IElementType, Alignment> alignmentFunction;
    PerlCodeStyleSettings perlCodeStyleSettings = myContext.getPerlSettings();
    if (elementType == COMMA_SEQUENCE_EXPR && perlCodeStyleSettings.ALIGN_FAT_COMMA) {
      alignmentFunction = childElementType -> childElementType == FAT_COMMA ? alignment : null;
    }
    else if (elementType == TRENAR_EXPR && perlCodeStyleSettings.ALIGN_TERNARY) {
      alignmentFunction = childElementType -> childElementType == QUESTION || childElementType == COLON ? alignment : null;
    }
    else if ((elementType == STRING_LIST || elementType == LP_STRING_QW) && perlCodeStyleSettings.ALIGN_QW_ELEMENTS) {
      @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
      FactoryMap<Integer, Alignment> alignmentMap = new FactoryMap<Integer, Alignment>() {
        @Nullable
        @Override
        protected Alignment create(Integer key) {
          return Alignment.createAlignment(true);
        }
      };
      int[] elementIndex = new int[]{0};
      int[] lastRelativeLineNumber = new int[]{0};

      alignmentFunction = childElementType -> {
        if (childElementType != STRING_CONTENT) {
          return null;
        }

        if (lastRelativeLineNumber[0] != relativeLineNumber[0]) {
          lastRelativeLineNumber[0] = relativeLineNumber[0];
          elementIndex[0] = 0;
        }

        return alignmentMap.get(elementIndex[0]++);
      };
    }
    else {
      alignmentFunction = childElementType -> null;
    }

    Wrap wrap = null;

    if (elementType == COMMA_SEQUENCE_EXPR && !isNewLineForbidden(this)) {
      wrap = Wrap.createWrap(WrapType.NORMAL, true);
    }

    for (ASTNode child = myNode.getFirstChildNode(); child != null; child = child.getTreeNext()) {
      if (!shouldCreateBlockFor(child)) {
        if (StringUtil.containsLineBreak(child.getChars())) {
          relativeLineNumber[0]++;
        }
        continue;
      }
      blocks.add(createBlock(child, wrap, alignmentFunction.fun(PsiUtilCore.getElementType(child))));
    }
    return blocks;
  }

  protected PerlFormattingBlock createBlock(@NotNull ASTNode node,
                                            @Nullable Wrap wrap,
                                            @Nullable Alignment alignment) {
    if (HEREDOC_BODIES_TOKENSET.contains(PsiUtilCore.getElementType(node))) {
      return new PerlHeredocFormattingBlock(node, wrap, alignment, myContext);
    }
    return new PerlFormattingBlock(node, wrap, alignment, myContext);
  }

  @Nullable
  @Override
  public Spacing getSpacing(Block child1, @NotNull Block child2) {
    if (child1 instanceof PerlFormattingBlock && child2 instanceof PerlFormattingBlock) {
      ASTNode child1Node = ((PerlFormattingBlock)child1).getNode();
      IElementType child1Type = child1Node.getElementType();
      ASTNode child2Node = ((PerlFormattingBlock)child2).getNode();
      IElementType child2Type = child2Node.getElementType();

      // fix for number/concat
      if (child2Type == OPERATOR_CONCAT) {
        ASTNode run = child1Node;
        while (run instanceof CompositeElement) {
          run = run.getLastChildNode();
        }

        if (run != null) {
          IElementType runType = run.getElementType();
          if (runType == NUMBER_SIMPLE || runType == NUMBER && StringUtil.endsWith(run.getText(), ".")) {
            return Spacing.createSpacing(1, 1, 0, true, 1);
          }
        }
      }

      // LF after opening brace and before closing need to check if here-doc opener is in the line
      if (LF_ELEMENTS.contains(child1Type) && LF_ELEMENTS.contains(child2Type)) {
        if (!isNewLineForbidden((PerlFormattingBlock)child1)) {
          return Spacing.createSpacing(0, 0, 1, true, 1);
        }
        else {
          return Spacing.createSpacing(1, Integer.MAX_VALUE, 0, true, 1);
        }
      }
      if (isCodeBlock() && !inGrepMapSort() && !blockHasLessChildrenThan(2) &&
          (BLOCK_OPENERS.contains(child1Type) && ((PerlFormattingBlock)child1).isFirst()
           || BLOCK_CLOSERS.contains(child2Type) && ((PerlFormattingBlock)child2).isLast()
          )
          && !isNewLineForbidden((PerlFormattingBlock)child1)
        ) {
        return Spacing.createSpacing(0, 0, 1, true, 1);
      }
    }
    return myContext.getSpacingBuilder().getSpacing(this, child1, child2);
  }

  /**
   * Checks if Heredoc is ahead of current block and it's not possible to insert newline
   * fixme we should collect all forbidden ranges, cache them and use here
   *
   * @param block block in question
   * @return check result
   */
  protected boolean isNewLineForbidden(PerlFormattingBlock block) {
    PsiElement element = block.getNode().getPsi();
    PsiFile file = element.getContainingFile();
    assert file instanceof PerlFileImpl;
    return ((PerlFileImpl)file).isNewLineForbiddenAt(element);
  }

  @Override
  public boolean isLeaf() {
    return myNode.getFirstChildNode() == null || LEAF_ELEMENTS.contains(myNode.getElementType());
  }

  @Override
  public Indent getIndent() {
    return myIndent;
  }

  @Nullable
  @Override
  protected Indent getChildIndent() {
    IElementType elementType = getElementType();

    if (myContext.getIndentProcessor().getUnindentableContainers().contains(elementType)) {
      return Indent.getNoneIndent();
    }

    if (myContext.getIndentProcessor().getBlockLikeContainers().contains(elementType)) {
      return Indent.getNormalIndent();
    }

    return super.getChildIndent();
  }

  @NotNull
  @Override
  public ChildAttributes getChildAttributes(int newChildIndex) {
    return new ChildAttributes(getChildIndent(), getChildAlignment());
  }

  @Nullable
  private Alignment getChildAlignment() {

    IElementType elementType = getElementType();
    if (PerlIndentProcessor.COMMA_LIKE_SEQUENCES.contains(elementType)) {
      return null;
    }

    // this is default algorythm from AbstractBlock#getFirstChildAlignment()
    List<Block> subBlocks = getSubBlocks();
    for (final Block subBlock : subBlocks) {
      Alignment alignment = subBlock.getAlignment();
      if (alignment != null) {
        return alignment;
      }
    }
    return null;
  }

  public boolean isLast() {
    return myIsLast;
  }

  public boolean isFirst() {
    return myIsFirst;
  }

  public IElementType getElementType() {
    return myElementType;
  }

  public boolean isCodeBlock() {
    return getElementType() == BLOCK;
  }

  /**
   * Check if we are in grep map or sort
   *
   * @return check result
   */
  public boolean inGrepMapSort() {
    ASTNode parent = myNode.getTreeParent();
    IElementType parentElementType;
    return parent != null &&
           ((parentElementType = parent.getElementType()) == GREP_EXPR || parentElementType == SORT_EXPR || parentElementType == MAP_EXPR);
  }

  /**
   * Checks if block contains more than specified number of meaningful children. Spaces and line comments are being ignored
   *
   * @return check result
   */
  public boolean blockHasLessChildrenThan(int maxChildren) {
    int counter = -2; // for braces
    ASTNode childNode = myNode.getFirstChildNode();
    while (childNode != null) {
      IElementType nodeType = childNode.getElementType();
      if (nodeType != TokenType.WHITE_SPACE && nodeType != COMMENT_LINE && nodeType != SEMICOLON) {
        if (++counter >= maxChildren) {
          return false;
        }
      }
      childNode = childNode.getTreeNext();
    }
    return true;
  }

  protected static boolean shouldCreateBlockFor(ASTNode node) {
    IElementType elementType = PsiUtilCore.getElementType(node);
    return elementType != TokenType.WHITE_SPACE && !node.getText().isEmpty() &&
           !(HEREDOC_BODIES_TOKENSET.contains(elementType) && node.getTextLength() == 1);
  }
}
