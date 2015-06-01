package com.perl5.lang.pod.lexer;
import com.intellij.lexer.*;
import com.intellij.psi.tree.IElementType;
import static com.perl5.lang.pod.lexer.PodElementTypes.*;

%%

%{
  public _PodLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _PodLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL="\r"|"\n"|"\r\n"
LINE_WS=[\ \t\f]
WHITE_SPACE=({LINE_WS}|{EOL})+


%%
<YYINITIAL> {
  {WHITE_SPACE}      { return com.intellij.psi.TokenType.WHITE_SPACE; }

  "POD_TAG"          { return POD_TAG; }
  "POD_CODE"         { return POD_CODE; }
  "POD_TEXT"         { return POD_TEXT; }
  "POD_NEWLINE"      { return POD_NEWLINE; }


  [^] { return com.intellij.psi.TokenType.BAD_CHARACTER; }
}
