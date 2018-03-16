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

package com.perl5.lang.perl.idea.formatter;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.FormattingModelBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.lang.LanguageFormatting;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.PsiLanguageInjectionHost.Shred;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Mapper for Formatting blocks range using ranges from shreds
 * N.B. This implementation supposes that suffix and prefix are meaningless
 */
public class PerlInjectedLanguageBlocksBuilder implements PsiLanguageInjectionHost.InjectedPsiVisitor {
  @NotNull
  private final CodeStyleSettings mySettings;

  @NotNull
  private final List<Entry> myEntries = new ArrayList<>();
  @NotNull
  private final ASTNode myHostNode;
  @NotNull
  private final TextRange myParentRange;

  /**
   * Used to align absolutely non-indented blocks in injected text
   */
  private final Alignment myAbsoluteIndentAlignment = Alignment.createAlignment();

  int injectedLength = 0;
  @Nullable
  private PsiFile myInjectedPsiFile;

  private PerlInjectedLanguageBlocksBuilder(@NotNull CodeStyleSettings settings,
                                            @NotNull ASTNode hostNode,
                                            @NotNull TextRange parentRange
  ) {
    mySettings = settings;
    myHostNode = hostNode;
    myParentRange = parentRange;
  }

  private void addShred(@NotNull Shred shred) {
    TextRange shredRange = shred.getRangeInsideHost().shiftRight(myHostNode.getStartOffset());
    if (shredRange.isEmpty()) {
      return;
    }
    Entry lastEntry = myEntries.isEmpty() ? null : myEntries.get(myEntries.size() - 1);
    if (lastEntry != null && lastEntry.myHostRange.getEndOffset() > shredRange.getStartOffset()) {
      throw new IllegalArgumentException(
        "Non-sequentional range added. Last range: " + lastEntry.myHostRange + " new entry " + shredRange);
    }
    // fixme take prefix and suffix lengths into account
    myEntries.add(new Entry(shredRange, TextRange.from(injectedLength, shredRange.getLength())));
    injectedLength += shredRange.getLength();
  }

  private boolean isEmpty() {
    return injectedLength == 0;
  }

  @Nullable
  private PsiElement getHostPsi() {
    return myHostNode.getPsi();
  }

  @NotNull
  private TextRange getInjectedTreeRange() {
    return TextRange.from(0, injectedLength);
  }

  private boolean enumerate() {
    PsiElement hostPsi = getHostPsi();
    if (hostPsi == null) {
      return false;
    }
    InjectedLanguageManager.getInstance(hostPsi.getProject()).enumerateEx(hostPsi, hostPsi.getContainingFile(), false, this);
    return getInjectedBuilder() != null;
  }

  @NotNull
  private List<Block> calcInjectedBlocks() {
    if (enumerate()) {
      Block wrapper = getInjectedLanguageRoot();
      return wrapper == null ? Collections.emptyList() : Collections.singletonList(wrapper);
    }
    return Collections.emptyList();
  }

  @Nullable
  private Block getInjectedLanguageRoot() {
    if (myInjectedPsiFile == null ||
        StringUtil.isEmptyOrSpaces(getInjectedTreeRange().subSequence(myInjectedPsiFile.getNode().getChars()))) {
      return null;
    }

    FormattingModelBuilder builder = getInjectedBuilder();
    if (builder == null) {
      return null;
    }

    final FormattingModel childModel = builder.createModel(myInjectedPsiFile, mySettings);
    return new PerlInjectedLanguageBlockWrapper(childModel.getRootBlock(), this);
  }

  @Nullable
  public TextRange getRangeInHostDocument(@NotNull TextRange rangeInInjected) {
    TextRange mappedRange = TextRange.create(
      getOffsetInHostDocument(rangeInInjected.getStartOffset()),
      getOffsetInHostDocument(rangeInInjected.getEndOffset())
    );
    return mappedRange.intersection(myParentRange);
  }

  private int getOffsetInHostDocument(int offsetInInjected) {
    if (offsetInInjected == injectedLength) {
      return myEntries.get(myEntries.size() - 1).myHostRange.getEndOffset();
    }
    for (Entry entry : myEntries) {
      if (entry.myInjectedRange.contains(offsetInInjected)) {
        return entry.myHostRange.getStartOffset() + offsetInInjected - entry.myInjectedRange.getStartOffset();
      }
    }

    throw new IllegalArgumentException("Unable to map offset:" + offsetInInjected);
  }

  @Nullable
  private FormattingModelBuilder getInjectedBuilder() {
    return myInjectedPsiFile == null ? null : LanguageFormatting.INSTANCE.forContext(myInjectedPsiFile);
  }

  @Override
  public void visit(@NotNull PsiFile injectedPsi, @NotNull List<Shred> places) {
    PsiElement hostPsi = getHostPsi();
    for (Shred shred : places) {
      if (hostPsi != shred.getHost()) {
        return;
      }
      addShred(shred);
    }
    if (isEmpty()) {
      return;
    }
    myInjectedPsiFile = injectedPsi;
  }

  public Alignment getAbsoluteIndentAlignment() {
    return myAbsoluteIndentAlignment;
  }

  public static List<Block> compute(@NotNull CodeStyleSettings settings,
                                    @NotNull ASTNode hostNode,
                                    @NotNull TextRange rangeInHost
  ) {
    return new PerlInjectedLanguageBlocksBuilder(settings, hostNode, rangeInHost).calcInjectedBlocks();
  }

  private static class Entry {
    private final TextRange myHostRange;
    private final TextRange myInjectedRange;

    public Entry(@NotNull TextRange hostRange, @NotNull TextRange injectedRange) {
      myHostRange = hostRange;
      myInjectedRange = injectedRange;
    }

    @Override
    public String toString() {
      return myInjectedRange + " => " + myHostRange;
    }
  }
}
