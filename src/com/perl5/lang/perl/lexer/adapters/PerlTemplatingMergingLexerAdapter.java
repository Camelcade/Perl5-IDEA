package com.perl5.lang.perl.lexer.adapters;

import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.FlexLexer;
import com.intellij.lexer.MergingLexerAdapter;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PerlTemplatingMergingLexerAdapter extends MergingLexerAdapter {
  public PerlTemplatingMergingLexerAdapter(@Nullable Project project, @NotNull FlexLexer flexLexer, TokenSet tokensToMerge) {
    super(
      new PerlSublexingLexerAdapter(
        project,
        new FlexAdapter(flexLexer), false),
      tokensToMerge
    );
  }
}
