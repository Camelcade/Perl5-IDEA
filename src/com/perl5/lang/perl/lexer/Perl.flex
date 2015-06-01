package com.perl5.lang.perl.lexer;
import com.intellij.lexer.*;
import com.intellij.psi.tree.IElementType;
import static com.perl5.lang.perl.lexer.PerlElementTypes.*;

%%

%{
  public _PerlLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _PerlLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL="\r"|"\n"|"\r\n"
LINE_WS=[\ \t\f]
WHITE_SPACE=({LINE_WS}|{EOL})+


%%
<YYINITIAL> {
  {WHITE_SPACE}                   { return com.intellij.psi.TokenType.WHITE_SPACE; }

  "PERL_COMMENT"                  { return PERL_COMMENT; }
  "PERL_COMMENT_BLOCK"            { return PERL_COMMENT_BLOCK; }
  "PERL_POD"                      { return PERL_POD; }
  "PERL_SUB_PROTOTYPE_TOKEN"      { return PERL_SUB_PROTOTYPE_TOKEN; }
  "@"                             { return PERL_SIGIL_ARRAY; }
  "%"                             { return PERL_SIGIL_HASH; }
  "$"                             { return PERL_SIGIL_SCALAR; }
  "$#"                            { return PERL_SIGIL_SCALAR_INDEX; }
  "PERL_VARIABLE_NAME"            { return PERL_VARIABLE_NAME; }
  "ARRAY_BUILT_IN"                { return PERL_ARRAY_BUILT_IN; }
  "HASH_BUILT_IN"                 { return PERL_HASH_BUILT_IN; }
  "SCALAR_BUILT_IN"               { return PERL_SCALAR_BUILT_IN; }
  "GLOB_BUILT_IN"                 { return PERL_GLOB_BUILT_IN; }
  "PERL_HEREDOC"                  { return PERL_HEREDOC; }
  "PERL_HEREDOC_END"              { return PERL_HEREDOC_END; }
  "PERL_VERSION"                  { return PERL_VERSION; }
  "PERL_NUMBER_VERSION"           { return PERL_NUMBER_VERSION; }
  "PERL_NUMBER"                   { return PERL_NUMBER; }
  "PERL_KEYWORD"                  { return PERL_KEYWORD; }
  "PERL_RESERVED"                 { return PERL_RESERVED; }
  "PERL_OPERATOR"                 { return PERL_OPERATOR; }
  "not"                           { return PERL_OPERATOR_NOT; }
  "x"                             { return PERL_OPERATOR_X; }
  "/"                             { return PERL_OPERATOR_DIV; }
  "PERL_OPERATOR_UNARY"           { return PERL_OPERATOR_UNARY; }
  "PERL_OPERATOR_FILETEST"        { return PERL_OPERATOR_FILETEST; }
  "PERL_REGEX_QUOTE_OPEN"         { return PERL_REGEX_QUOTE_OPEN; }
  "PERL_REGEX_QUOTE_CLOSE"        { return PERL_REGEX_QUOTE_CLOSE; }
  "PERL_REGEX_MODIFIER"           { return PERL_REGEX_MODIFIER; }
  "PERL_REGEX_TOKEN"              { return PERL_REGEX_TOKEN; }
  "PERL_STRING_CONTENT"           { return PERL_STRING_CONTENT; }
  "PERL_TAG"                      { return PERL_TAG; }
  ","                             { return PERL_COMMA; }
  "=>"                            { return PERL_ARROW_COMMA; }
  "->"                            { return PERL_DEREFERENCE; }
  "::"                            { return PERL_DEPACKAGE; }
  ":"                             { return PERL_COLON; }
  "["                             { return PERL_LBRACK; }
  "]"                             { return PERL_RBRACK; }
  "("                             { return PERL_LPAREN; }
  ")"                             { return PERL_RPAREN; }
  "{"                             { return PERL_LBRACE; }
  "}"                             { return PERL_RBRACE; }
  "<"                             { return PERL_LANGLE; }
  ">"                             { return PERL_RANGLE; }
  ";"                             { return PERL_SEMI; }
  "\""                            { return PERL_QUOTE; }
  "PERL_FUNCTION_ATTRIBUTE"       { return PERL_FUNCTION_ATTRIBUTE; }
  "PERL_PACKAGE"                  { return PERL_PACKAGE; }
  "PERL_PACKAGE_BUILT_IN"         { return PERL_PACKAGE_BUILT_IN; }
  "PERL_PACKAGE_DEPRECATED"       { return PERL_PACKAGE_DEPRECATED; }
  "PERL_PACKAGE_PRAGMA"           { return PERL_PACKAGE_PRAGMA; }
  "PERL_FUNCTION"                 { return PERL_FUNCTION; }
  "PERL_FUNCTION_BUILT_IN"        { return PERL_FUNCTION_BUILT_IN; }
  "PERL_HANDLE"                   { return PERL_HANDLE; }
  "PERL_HANDLE_BUILT_IN"          { return PERL_HANDLE_BUILT_IN; }
  "PERL_BLOCK_NAME"               { return PERL_BLOCK_NAME; }
  "PERL_LABEL"                    { return PERL_LABEL; }
  "TEMPLATE_BLOCK_HTML"           { return TEMPLATE_BLOCK_HTML; }
  "EMBED_MARKER"                  { return EMBED_MARKER; }
  "HEREDOC_SQL"                   { return HEREDOC_SQL; }


  [^] { return com.intellij.psi.TokenType.BAD_CHARACTER; }
}
