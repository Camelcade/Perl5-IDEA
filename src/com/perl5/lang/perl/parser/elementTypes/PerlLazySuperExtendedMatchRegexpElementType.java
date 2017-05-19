package com.perl5.lang.perl.parser.elementTypes;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.perl5.lang.perl.lexer.adapters.PerlSubLexerAdapter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class PerlLazySuperExtendedMatchRegexpElementType extends PerlLazyRegexpElementType {
  public PerlLazySuperExtendedMatchRegexpElementType(@NotNull @NonNls String debugName) {
    super(debugName);
  }

  @NotNull
  @Override
  protected Lexer getInnerLexer(@NotNull Project project) {
    return PerlSubLexerAdapter.forExtendedMatchRegex(project);
  }
}
