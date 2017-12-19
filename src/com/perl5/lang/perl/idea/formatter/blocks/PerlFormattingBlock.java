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
import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.TokenType;
import com.intellij.psi.formatter.FormatterUtil;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.containers.FactoryMap;
import com.perl5.lang.perl.idea.formatter.PerlFormattingContext;
import com.perl5.lang.perl.idea.formatter.PerlIndentProcessor;
import com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.parser.PerlParserUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
      POD,
      PerlParserUtil.DUMMY_BLOCK
    );

  protected final PerlFormattingContext myContext;
  private final Indent myIndent;
  private final boolean myIsFirst;
  private final boolean myIsLast;
  private final IElementType myElementType;
  private final AtomicNotNullLazyValue<Boolean> myIsIncomple;
  private final AtomicNotNullLazyValue<List<Block>> mySubBlocksProvider = AtomicNotNullLazyValue.createValue(
    () -> ContainerUtil.immutableList(buildSubBlocks())
  );

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
    myIsIncomple = AtomicNotNullLazyValue.createValue(() -> {
      if (myElementType == COMMA_SEQUENCE_EXPR) {
        IElementType lastNodeType = PsiUtilCore.getElementType(myNode.getLastChildNode());
        if (lastNodeType == COMMA || lastNodeType == FAT_COMMA) {
          return true;
        }
      }

      List<Block> blocks = getSubBlocks();
      if (!blocks.isEmpty()) {
        Block lastBlock = blocks.get(blocks.size() - 1);
        if (lastBlock.isIncomplete()) {
          return true;
        }
      }

      return PerlFormattingBlock.super.isIncomplete();
    });
  }

  @NotNull
  @Override
  protected final List<Block> buildChildren() {
    if (isLeaf()) {
      return Collections.emptyList();
    }
    return mySubBlocksProvider.getValue();
  }

  protected List<Block> buildSubBlocks() {
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
    else if (elementType == DEREF_EXPR && perlCodeStyleSettings.ALIGN_DEREFERENCE_IN_CHAIN) {
      alignmentFunction = childElementType -> childElementType == OPERATOR_DEREFERENCE ? alignment : null;
    }
    else if ((elementType == STRING_LIST || elementType == LP_STRING_QW) && perlCodeStyleSettings.ALIGN_QW_ELEMENTS) {
      @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
      Map<Integer, Alignment> alignmentMap = FactoryMap.create(key -> Alignment.createAlignment(true));
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


    Function<ASTNode, Wrap> wrapFunction;

    if (elementType == COMMA_SEQUENCE_EXPR) {
      Wrap wrap = Wrap.createWrap(WrapType.NORMAL, true);
      wrapFunction = childNode -> {
        if (PsiUtilCore.getElementType(childNode) == COMMA || myContext.isNewLineForbiddenAt(childNode)) {
          return null;
        }
        return wrap;
      };
    }
    else {
      wrapFunction = childNode -> null;
    }

    for (ASTNode child = myNode.getFirstChildNode(); child != null; child = child.getTreeNext()) {
      if (!shouldCreateBlockFor(child)) {
        if (StringUtil.containsLineBreak(child.getChars())) {
          relativeLineNumber[0]++;
        }
        continue;
      }
      IElementType childElementType = PsiUtilCore.getElementType(child);
      blocks.add(createBlock(child, wrapFunction.fun(child), alignmentFunction.fun(childElementType)));
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
  public Spacing getSpacing(@Nullable Block child1, @NotNull Block child2) {
    return myContext.getSpacing(this, child1, child2);
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

  @Override
  public boolean isIncomplete() {
    return myIsIncomple.getValue();
  }

  protected static boolean shouldCreateBlockFor(ASTNode node) {
    IElementType elementType = PsiUtilCore.getElementType(node);
    return elementType != TokenType.WHITE_SPACE && !node.getText().isEmpty() &&
           !(HEREDOC_BODIES_TOKENSET.contains(elementType) && node.getTextLength() == 1);
  }
}
