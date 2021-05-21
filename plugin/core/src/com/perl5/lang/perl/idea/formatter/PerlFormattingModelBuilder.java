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

package com.perl5.lang.perl.idea.formatter;

import com.intellij.formatting.Block;
import com.intellij.formatting.FormattingContext;
import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.FormattingModelBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.FileElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.idea.formatter.blocks.PerlFormattingBlock;
import com.perl5.lang.perl.util.PerlTimeLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.idea.formatter.PerlFormattingTokenSets.FORMATTING_RANGE_EDGE_ELEMENTS;
import static com.perl5.lang.perl.idea.formatter.PerlFormattingTokenSets.FORMATTING_SUFFICIENT_BLOCKS;


public class PerlFormattingModelBuilder implements FormattingModelBuilder {
  private static final Logger LOG = Logger.getInstance(PerlFormattingModelBuilder.class);
  private static final Key<TextRange> COMPUTED_RANGE_KEY = Key.create("perl5.formatting.computed.range");

  @Override
  public @NotNull FormattingModel createModel(@NotNull FormattingContext formattingContext) {
    var element = formattingContext.getPsiElement();
    var range = formattingContext.getFormattingRange();
    var settings = formattingContext.getCodeStyleSettings();
    var mode = formattingContext.getFormattingMode();
    PerlTimeLogger logger = PerlTimeLogger.create(LOG);
    TextRange computedRange = COMPUTED_RANGE_KEY.get(element);
    COMPUTED_RANGE_KEY.set(element, null);
    if (computedRange != null && range.contains(computedRange)) {
      LOG.debug("Got pre-computed range: ", computedRange);
      range = computedRange;
    }
    else {
      LOG.debug("No pre-computed range, trying to reduce provided:", range);
      range = computeFormattingRange(element.getContainingFile(), range);
    }
    PerlFormattingBlock rootBlock = new PerlFormattingBlock(element.getNode(), new PurePerlFormattingContext(formattingContext, range));
    PerlDocumentBasedFormattingModel model = new PerlDocumentBasedFormattingModel(rootBlock, element, settings);
    if (LOG.isDebugEnabled()) {
      logger.debug("Created model for: ", element.getNode(),
                   "; range: ", range,
                   "; settings: ", settings,
                   "; mode: ", mode);
      if (LOG.isTraceEnabled()) {
        LOG.trace("Tree size: " + computeTreeSize(rootBlock));
      }
    }
    return model;
  }

  private static @NotNull TextRange computeFormattingRange(@NotNull PsiFile psiFile, @NotNull TextRange originalRange) {
    if (psiFile.getTextRange().equals(originalRange)) {
      return originalRange;
    }

    PsiElement startLeaf = psiFile.findElementAt(originalRange.getStartOffset());
    PsiElement endLeaf = psiFile.findElementAt(originalRange.getEndOffset());
    if (startLeaf == null || endLeaf == null) {
      LOG.debug("Have no both leaves, full range: ", startLeaf, endLeaf);
      return psiFile.getTextRange();
    }
    PsiElement commonParent = PsiTreeUtil.findCommonParent(startLeaf, endLeaf);
    if (commonParent == null) {
      LOG.debug("No common parent for leaves, using full range: ", psiFile.getTextRange());
      return psiFile.getTextRange();
    }

    ASTNode nodeToFormat = commonParent.getNode();

    while (nodeToFormat != null) {
      if (nodeToFormat instanceof FileElement || FORMATTING_SUFFICIENT_BLOCKS.contains(nodeToFormat.getElementType())) {
        // found closest self sufficient block
        if (LOG.isDebugEnabled()) {
          LOG.debug("Closest self-sufficient block is: ", dumpNode(nodeToFormat));
        }

        while (nodeToFormat.getFirstChildNode() != null && nodeToFormat.getFirstChildNode() == nodeToFormat.getLastChildNode()) {
          nodeToFormat = nodeToFormat.getFirstChildNode();
          if (LOG.isDebugEnabled()) {
            LOG.debug("Descending to the child of the same range: ", dumpNode(nodeToFormat));
          }
        }

        ASTNode run = nodeToFormat.getFirstChildNode();
        while (run != null && !run.getTextRange().intersects(originalRange)) {
          run = run.getTreeNext();
        }

        if (run == null) {
          LOG.debug("Can't adjust more, using ", nodeToFormat.getTextRange());
          return nodeToFormat.getTextRange();
        }

        ASTNode firstIntersectingChild = run;
        while (run != null && run.getTextRange().intersects(originalRange)) {
          run = run.getTreeNext();
        }

        if (LOG.isDebugEnabled()) {
          LOG.debug("More precise direct kids: ", dumpNode(firstIntersectingChild), " - ", dumpNode(run));
        }

        // cut off compounds before and after
        ASTNode startNode = firstIntersectingChild.getTreePrev();
        ASTNode endNode = run;

        while (startNode != null && !FORMATTING_RANGE_EDGE_ELEMENTS.contains(startNode.getElementType())) {
          startNode = startNode.getTreePrev();
        }
        while (endNode != null && !FORMATTING_RANGE_EDGE_ELEMENTS.contains(endNode.getElementType())) {
          endNode = endNode.getTreeNext();
        }
        if (LOG.isDebugEnabled()) {
          LOG.debug("Adjusted childs range: ", dumpNode(startNode), " - ", dumpNode(endNode));
        }

        int startOffset = startNode == null ? nodeToFormat.getTextRange().getStartOffset() : startNode.getTextRange().getEndOffset();
        int endOffset = endNode == null ? nodeToFormat.getTextRange().getEndOffset() : endNode.getStartOffset();
        TextRange computedRange = TextRange.create(startOffset, endOffset);
        LOG.debug("Effective range: ", computedRange);
        return computedRange;
      }
      nodeToFormat = nodeToFormat.getTreeParent();
    }
    LOG.debug("Could not find self-sufficient block, full range");
    return psiFile.getTextRange();
  }

  @Override
  public @Nullable TextRange getRangeAffectingIndent(PsiFile file, int offset, ASTNode elementAtOffset) {
    TextRange computedRange = null;
    if (file != null) {
      LOG.debug("Computing range affecting indent at ", offset);
      computedRange = computeFormattingRange(file, TextRange.create(offset, offset));
    }
    COMPUTED_RANGE_KEY.set(file, computedRange);
    return computedRange;
  }

  private static @NotNull String dumpNode(@Nullable ASTNode node) {
    return "" + node + (node == null ? "" : node.getTextRange());
  }

  public static int computeTreeSize(@NotNull Block rootBlock) {
    int size = 1;
    for (Block subBlock : rootBlock.getSubBlocks()) {
      size += computeTreeSize(subBlock);
    }
    return size;
  }
}
