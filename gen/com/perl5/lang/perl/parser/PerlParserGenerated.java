// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LightPsiParser;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

import static com.perl5.lang.perl.lexer.PerlElementTypes.ADD_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.AND_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.ANNOTATION;
import static com.perl5.lang.perl.lexer.PerlElementTypes.ANNOTATION_ABSTRACT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.ANNOTATION_ABSTRACT_KEY;
import static com.perl5.lang.perl.lexer.PerlElementTypes.ANNOTATION_DEPRECATED;
import static com.perl5.lang.perl.lexer.PerlElementTypes.ANNOTATION_DEPRECATED_KEY;
import static com.perl5.lang.perl.lexer.PerlElementTypes.ANNOTATION_INCOMPLETE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.ANNOTATION_METHOD;
import static com.perl5.lang.perl.lexer.PerlElementTypes.ANNOTATION_METHOD_KEY;
import static com.perl5.lang.perl.lexer.PerlElementTypes.ANNOTATION_OVERRIDE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.ANNOTATION_OVERRIDE_KEY;
import static com.perl5.lang.perl.lexer.PerlElementTypes.ANNOTATION_PREFIX;
import static com.perl5.lang.perl.lexer.PerlElementTypes.ANNOTATION_RETURNS_ARRAYREF;
import static com.perl5.lang.perl.lexer.PerlElementTypes.ANNOTATION_RETURNS_HASHREF;
import static com.perl5.lang.perl.lexer.PerlElementTypes.ANNOTATION_RETURNS_KEY;
import static com.perl5.lang.perl.lexer.PerlElementTypes.ANNOTATION_RETURNS_REF;
import static com.perl5.lang.perl.lexer.PerlElementTypes.ANNOTATION_UNKNOWN;
import static com.perl5.lang.perl.lexer.PerlElementTypes.ANNOTATION_UNKNOWN_KEY;
import static com.perl5.lang.perl.lexer.PerlElementTypes.ANON_ARRAY;
import static com.perl5.lang.perl.lexer.PerlElementTypes.ANON_ARRAY_ELEMENT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.ANON_HASH;
import static com.perl5.lang.perl.lexer.PerlElementTypes.ANON_SUB;
import static com.perl5.lang.perl.lexer.PerlElementTypes.ARRAY_ARRAY_SLICE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.ARRAY_CAST_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.ARRAY_HASH_SLICE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.ARRAY_INDEX;
import static com.perl5.lang.perl.lexer.PerlElementTypes.ARRAY_INDEX_VARIABLE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.ARRAY_VARIABLE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.ASSIGN_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.ATTRIBUTE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.BITWISE_AND_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.BITWISE_OR_XOR_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.BLOCK;
import static com.perl5.lang.perl.lexer.PerlElementTypes.BLOCK_NAME;
import static com.perl5.lang.perl.lexer.PerlElementTypes.CALL_ARGUMENTS;
import static com.perl5.lang.perl.lexer.PerlElementTypes.CODE_CAST_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.CODE_VARIABLE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.COLON;
import static com.perl5.lang.perl.lexer.PerlElementTypes.COMMA_SEQUENCE_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.COMPARE_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.COMPILE_REGEX;
import static com.perl5.lang.perl.lexer.PerlElementTypes.CONDITIONAL_BLOCK;
import static com.perl5.lang.perl.lexer.PerlElementTypes.CONDITIONAL_BLOCK_WHILE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.CONDITION_STATEMENT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.CONDITION_STATEMENT_WHILE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.CONSTANTS_BLOCK;
import static com.perl5.lang.perl.lexer.PerlElementTypes.CONSTANT_DEFINITION;
import static com.perl5.lang.perl.lexer.PerlElementTypes.CONSTANT_NAME;
import static com.perl5.lang.perl.lexer.PerlElementTypes.CONTINUE_BLOCK;
import static com.perl5.lang.perl.lexer.PerlElementTypes.DEFAULT_COMPOUND;
import static com.perl5.lang.perl.lexer.PerlElementTypes.DEREF_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.DO_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.EQUAL_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.EVAL_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.FILE_READ_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.FILE_READ_FORCED_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.FLIPFLOP_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.FOREACH_COMPOUND;
import static com.perl5.lang.perl.lexer.PerlElementTypes.FOREACH_STATEMENT_MODIFIER;
import static com.perl5.lang.perl.lexer.PerlElementTypes.FORMAT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.FORMAT_DEFINITION;
import static com.perl5.lang.perl.lexer.PerlElementTypes.FORMAT_TERMINATOR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.FOR_COMPOUND;
import static com.perl5.lang.perl.lexer.PerlElementTypes.FOR_ITERATOR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.FOR_ITERATOR_STATEMENT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.FOR_LIST_EPXR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.FOR_LIST_STATEMENT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.FOR_STATEMENT_MODIFIER;
import static com.perl5.lang.perl.lexer.PerlElementTypes.FUNC_DEFINITION;
import static com.perl5.lang.perl.lexer.PerlElementTypes.FUNC_SIGNATURE_CONTENT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.GIVEN_COMPOUND;
import static com.perl5.lang.perl.lexer.PerlElementTypes.GLOB_CAST_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.GLOB_SLOT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.GLOB_VARIABLE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.GOTO_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.GREP_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.HANDLE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.HASH_CAST_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.HASH_INDEX;
import static com.perl5.lang.perl.lexer.PerlElementTypes.HASH_VARIABLE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.HEREDOC_OPENER;
import static com.perl5.lang.perl.lexer.PerlElementTypes.IDENTIFIER;
import static com.perl5.lang.perl.lexer.PerlElementTypes.IF_COMPOUND;
import static com.perl5.lang.perl.lexer.PerlElementTypes.IF_STATEMENT_MODIFIER;
import static com.perl5.lang.perl.lexer.PerlElementTypes.LABEL;
import static com.perl5.lang.perl.lexer.PerlElementTypes.LABEL_DECLARATION;
import static com.perl5.lang.perl.lexer.PerlElementTypes.LAST_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.LEFT_ANGLE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.LEFT_BRACE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.LEFT_BRACKET;
import static com.perl5.lang.perl.lexer.PerlElementTypes.LEFT_PAREN;
import static com.perl5.lang.perl.lexer.PerlElementTypes.LP_AND_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.LP_NOT_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.LP_OR_XOR_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.MAP_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.MATCH_REGEX;
import static com.perl5.lang.perl.lexer.PerlElementTypes.METHOD;
import static com.perl5.lang.perl.lexer.PerlElementTypes.METHOD_DEFINITION;
import static com.perl5.lang.perl.lexer.PerlElementTypes.METHOD_SIGNATURE_CONTENT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.METHOD_SIGNATURE_INVOCANT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.MUL_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.NAMED_BLOCK;
import static com.perl5.lang.perl.lexer.PerlElementTypes.NAMED_LIST_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.NAMED_UNARY_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.NAMESPACE_CONTENT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.NAMESPACE_DEFINITION;
import static com.perl5.lang.perl.lexer.PerlElementTypes.NAMESPACE_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.NESTED_CALL;
import static com.perl5.lang.perl.lexer.PerlElementTypes.NESTED_CALL_ARGUMENTS;
import static com.perl5.lang.perl.lexer.PerlElementTypes.NEXT_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.NO_STATEMENT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.NUMBER;
import static com.perl5.lang.perl.lexer.PerlElementTypes.NUMBER_CONSTANT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.NUMBER_SIMPLE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.NUMBER_VERSION;
import static com.perl5.lang.perl.lexer.PerlElementTypes.NYI_STATEMENT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_AND;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_AND_ASSIGN;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_AND_LP;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_ASSIGN;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_BITWISE_AND;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_BITWISE_AND_ASSIGN;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_BITWISE_NOT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_BITWISE_OR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_BITWISE_OR_ASSIGN;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_BITWISE_XOR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_BITWISE_XOR_ASSIGN;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_CMP_NUMERIC;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_CMP_STR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_COMMA;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_COMMA_ARROW;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_CONCAT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_CONCAT_ASSIGN;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_DEREFERENCE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_DIV;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_DIV_ASSIGN;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_EQ_NUMERIC;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_EQ_STR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_FILETEST;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_FLIP_FLOP;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_GE_NUMERIC;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_GE_STR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_GT_NUMERIC;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_GT_STR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_HELLIP;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_HEREDOC;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_LE_NUMERIC;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_LE_STR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_LT_NUMERIC;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_LT_STR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_MINUS;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_MINUS_ASSIGN;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_MINUS_MINUS;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_MINUS_UNARY;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_MOD;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_MOD_ASSIGN;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_MUL;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_MUL_ASSIGN;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_NE_NUMERIC;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_NE_STR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_NOT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_NOT_LP;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_NOT_RE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_OR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_OR_ASSIGN;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_OR_DEFINED;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_OR_DEFINED_ASSIGN;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_OR_LP;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_PLUS;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_PLUS_ASSIGN;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_PLUS_PLUS;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_PLUS_UNARY;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_POW;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_POW_ASSIGN;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_RE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_REFERENCE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_SHIFT_LEFT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_SHIFT_LEFT_ASSIGN;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_SHIFT_RIGHT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_SHIFT_RIGHT_ASSIGN;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_SMARTMATCH;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_X;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_XOR_LP;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OPERATOR_X_ASSIGN;
import static com.perl5.lang.perl.lexer.PerlElementTypes.OR_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.PACKAGE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.PACKAGE_CORE_IDENTIFIER;
import static com.perl5.lang.perl.lexer.PerlElementTypes.PACKAGE_PRAGMA_CONSTANT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.PACKAGE_PRAGMA_VARS;
import static com.perl5.lang.perl.lexer.PerlElementTypes.PARENTHESISED_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.PERL_HANDLE_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.PERL_REGEX;
import static com.perl5.lang.perl.lexer.PerlElementTypes.PERL_REGEX_EX;
import static com.perl5.lang.perl.lexer.PerlElementTypes.PERL_REGEX_MODIFIERS;
import static com.perl5.lang.perl.lexer.PerlElementTypes.POD;
import static com.perl5.lang.perl.lexer.PerlElementTypes.POW_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.PREFIX_MINUS_AS_STRING_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.PREFIX_UNARY_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.PREF_MM_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.PREF_PP_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.PRINT_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.PRINT_HANDLE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.QUESTION;
import static com.perl5.lang.perl.lexer.PerlElementTypes.QUOTE_DOUBLE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.QUOTE_DOUBLE_CLOSE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.QUOTE_DOUBLE_OPEN;
import static com.perl5.lang.perl.lexer.PerlElementTypes.QUOTE_SINGLE_CLOSE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.QUOTE_SINGLE_OPEN;
import static com.perl5.lang.perl.lexer.PerlElementTypes.QUOTE_TICK;
import static com.perl5.lang.perl.lexer.PerlElementTypes.QUOTE_TICK_CLOSE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.QUOTE_TICK_OPEN;
import static com.perl5.lang.perl.lexer.PerlElementTypes.READ_HANDLE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.REDO_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.REF_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.REGEX_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.REGEX_MODIFIER;
import static com.perl5.lang.perl.lexer.PerlElementTypes.REGEX_QUOTE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.REGEX_QUOTE_CLOSE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.REGEX_QUOTE_E;
import static com.perl5.lang.perl.lexer.PerlElementTypes.REGEX_QUOTE_OPEN;
import static com.perl5.lang.perl.lexer.PerlElementTypes.REGEX_QUOTE_OPEN_E;
import static com.perl5.lang.perl.lexer.PerlElementTypes.REGEX_QUOTE_OPEN_X;
import static com.perl5.lang.perl.lexer.PerlElementTypes.REPLACEMENT_REGEX;
import static com.perl5.lang.perl.lexer.PerlElementTypes.REQUIRE_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_CONTINUE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_DEFAULT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_DO;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_ELSE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_ELSIF;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_EVAL;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_FOR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_FOREACH;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_FORMAT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_FUNC;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_GIVEN;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_GOTO;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_GREP;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_IF;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_LAST;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_LOCAL;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_M;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_MAP;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_METHOD;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_MY;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_NEXT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_NO;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_OUR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_PACKAGE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_PRINT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_PRINTF;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_Q;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_QQ;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_QR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_QW;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_QX;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_REDO;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_REQUIRE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_RETURN;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_S;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_SAY;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_SORT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_STATE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_SUB;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_TR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_UNDEF;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_UNLESS;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_UNTIL;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_USE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_WHEN;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_WHILE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RESERVED_Y;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RETURN_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RIGHT_ANGLE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RIGHT_BRACE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RIGHT_BRACKET;
import static com.perl5.lang.perl.lexer.PerlElementTypes.RIGHT_PAREN;
import static com.perl5.lang.perl.lexer.PerlElementTypes.SCALAR_ARRAY_ELEMENT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.SCALAR_CALL;
import static com.perl5.lang.perl.lexer.PerlElementTypes.SCALAR_CAST_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.SCALAR_HASH_ELEMENT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.SCALAR_INDEX_CAST_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.SCALAR_VARIABLE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.SEMICOLON;
import static com.perl5.lang.perl.lexer.PerlElementTypes.SHIFT_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.SIGIL_ARRAY;
import static com.perl5.lang.perl.lexer.PerlElementTypes.SIGIL_CODE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.SIGIL_GLOB;
import static com.perl5.lang.perl.lexer.PerlElementTypes.SIGIL_HASH;
import static com.perl5.lang.perl.lexer.PerlElementTypes.SIGIL_SCALAR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.SIGIL_SCALAR_INDEX;
import static com.perl5.lang.perl.lexer.PerlElementTypes.SORT_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.STATEMENT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.STATEMENT_MODIFIER;
import static com.perl5.lang.perl.lexer.PerlElementTypes.STRING_BARE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.STRING_CONTENT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.STRING_DQ;
import static com.perl5.lang.perl.lexer.PerlElementTypes.STRING_LIST;
import static com.perl5.lang.perl.lexer.PerlElementTypes.STRING_SQ;
import static com.perl5.lang.perl.lexer.PerlElementTypes.STRING_XQ;
import static com.perl5.lang.perl.lexer.PerlElementTypes.SUB;
import static com.perl5.lang.perl.lexer.PerlElementTypes.SUB_CALL_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.SUB_DECLARATION;
import static com.perl5.lang.perl.lexer.PerlElementTypes.SUB_DEFINITION;
import static com.perl5.lang.perl.lexer.PerlElementTypes.SUB_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.SUB_SIGNATURE_CONTENT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.SUB_SIGNATURE_ELEMENT_IGNORE;
import static com.perl5.lang.perl.lexer.PerlElementTypes.SUFF_PP_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.TAG;
import static com.perl5.lang.perl.lexer.PerlElementTypes.TAG_SCALAR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.TERM_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.TRENAR_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.TR_MODIFIERS;
import static com.perl5.lang.perl.lexer.PerlElementTypes.TR_REGEX;
import static com.perl5.lang.perl.lexer.PerlElementTypes.TR_REPLACEMENTLIST;
import static com.perl5.lang.perl.lexer.PerlElementTypes.TR_SEARCHLIST;
import static com.perl5.lang.perl.lexer.PerlElementTypes.UNCONDITIONAL_BLOCK;
import static com.perl5.lang.perl.lexer.PerlElementTypes.UNDEF_EXPR;
import static com.perl5.lang.perl.lexer.PerlElementTypes.UNLESS_COMPOUND;
import static com.perl5.lang.perl.lexer.PerlElementTypes.UNLESS_STATEMENT_MODIFIER;
import static com.perl5.lang.perl.lexer.PerlElementTypes.UNTIL_COMPOUND;
import static com.perl5.lang.perl.lexer.PerlElementTypes.UNTIL_STATEMENT_MODIFIER;
import static com.perl5.lang.perl.lexer.PerlElementTypes.USE_STATEMENT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.USE_STATEMENT_CONSTANT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.USE_VARS_STATEMENT;
import static com.perl5.lang.perl.lexer.PerlElementTypes.VARIABLE_DECLARATION_GLOBAL;
import static com.perl5.lang.perl.lexer.PerlElementTypes.VARIABLE_DECLARATION_LEXICAL;
import static com.perl5.lang.perl.lexer.PerlElementTypes.VARIABLE_DECLARATION_LOCAL;
import static com.perl5.lang.perl.lexer.PerlElementTypes.VARIABLE_DECLARATION_WRAPPER;
import static com.perl5.lang.perl.lexer.PerlElementTypes.WHEN_COMPOUND;
import static com.perl5.lang.perl.lexer.PerlElementTypes.WHEN_STATEMENT_MODIFIER;
import static com.perl5.lang.perl.lexer.PerlElementTypes.WHILE_COMPOUND;
import static com.perl5.lang.perl.lexer.PerlElementTypes.WHILE_STATEMENT_MODIFIER;
import static com.perl5.lang.perl.parser.PerlParserUtil.*;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class PerlParserGenerated implements PsiParser, LightPsiParser
{

	public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[]{
			create_token_set_(IF_COMPOUND, UNLESS_COMPOUND),
			create_token_set_(PERL_REGEX, PERL_REGEX_EX),
			create_token_set_(NESTED_CALL, SUB_CALL_EXPR),
			create_token_set_(FUNC_SIGNATURE_CONTENT, METHOD_SIGNATURE_CONTENT, SUB_SIGNATURE_CONTENT),
			create_token_set_(FOREACH_STATEMENT_MODIFIER, FOR_STATEMENT_MODIFIER, IF_STATEMENT_MODIFIER, STATEMENT_MODIFIER,
					UNLESS_STATEMENT_MODIFIER, UNTIL_STATEMENT_MODIFIER, WHEN_STATEMENT_MODIFIER, WHILE_STATEMENT_MODIFIER),
			create_token_set_(CONDITION_STATEMENT, CONDITION_STATEMENT_WHILE, FOR_ITERATOR_STATEMENT, FOR_LIST_STATEMENT,
					NO_STATEMENT, STATEMENT, USE_STATEMENT, USE_STATEMENT_CONSTANT,
					USE_VARS_STATEMENT),
			create_token_set_(ANNOTATION, ANNOTATION_ABSTRACT, ANNOTATION_DEPRECATED, ANNOTATION_INCOMPLETE,
					ANNOTATION_METHOD, ANNOTATION_OVERRIDE, ANNOTATION_RETURNS_ARRAYREF, ANNOTATION_RETURNS_HASHREF,
					ANNOTATION_RETURNS_REF, ANNOTATION_UNKNOWN),
			create_token_set_(ADD_EXPR, AND_EXPR, ANON_ARRAY, ANON_ARRAY_ELEMENT,
					ANON_HASH, ANON_SUB, ARRAY_ARRAY_SLICE, ARRAY_CAST_EXPR,
					ARRAY_HASH_SLICE, ARRAY_INDEX_VARIABLE, ARRAY_VARIABLE, ASSIGN_EXPR,
					BITWISE_AND_EXPR, BITWISE_OR_XOR_EXPR, CODE_CAST_EXPR, CODE_VARIABLE,
					COMMA_SEQUENCE_EXPR, COMPARE_EXPR, COMPILE_REGEX, DEREF_EXPR,
					DO_EXPR, EQUAL_EXPR, EVAL_EXPR, EXPR,
					FILE_READ_EXPR, FILE_READ_FORCED_EXPR, FLIPFLOP_EXPR, GLOB_CAST_EXPR,
					GLOB_VARIABLE, GOTO_EXPR, GREP_EXPR, HASH_CAST_EXPR,
					HASH_VARIABLE, HEREDOC_OPENER, LAST_EXPR, LP_AND_EXPR,
					LP_NOT_EXPR, LP_OR_XOR_EXPR, MAP_EXPR, MATCH_REGEX,
					MUL_EXPR, NAMED_LIST_EXPR, NAMED_UNARY_EXPR, NAMESPACE_EXPR,
					NESTED_CALL, NEXT_EXPR, NUMBER_CONSTANT, OR_EXPR,
					PARENTHESISED_EXPR, PERL_HANDLE_EXPR, POW_EXPR, PREFIX_MINUS_AS_STRING_EXPR,
					PREFIX_UNARY_EXPR, PREF_MM_EXPR, PREF_PP_EXPR, PRINT_EXPR,
					REDO_EXPR, REF_EXPR, REGEX_EXPR, REPLACEMENT_REGEX,
					REQUIRE_EXPR, RETURN_EXPR, SCALAR_ARRAY_ELEMENT, SCALAR_CAST_EXPR,
					SCALAR_HASH_ELEMENT, SCALAR_INDEX_CAST_EXPR, SCALAR_VARIABLE, SHIFT_EXPR,
					SORT_EXPR, STRING_BARE, STRING_DQ, STRING_LIST,
					STRING_SQ, STRING_XQ, SUB_CALL_EXPR, SUB_EXPR,
					SUFF_PP_EXPR, TAG_SCALAR, TERM_EXPR, TRENAR_EXPR,
					TR_REGEX, UNDEF_EXPR, VARIABLE_DECLARATION_GLOBAL, VARIABLE_DECLARATION_LEXICAL,
					VARIABLE_DECLARATION_LOCAL),
	};
	final static Parser recover_block_parser_ = new Parser()
	{
		public boolean parse(PsiBuilder b, int l)
		{
			return recover_block(b, l + 1);
		}
	};
	final static Parser recover_condition_statement_expr_parser_ = new Parser()
	{
		public boolean parse(PsiBuilder b, int l)
		{
			return recover_condition_statement_expr(b, l + 1);
		}
	};
	final static Parser recover_namespace_content_parser_ = new Parser()
	{
		public boolean parse(PsiBuilder b, int l)
		{
			return recover_namespace_content(b, l + 1);
		}
	};
	final static Parser recover_namespace_definition_parser_ = new Parser()
	{
		public boolean parse(PsiBuilder b, int l)
		{
			return recover_namespace_definition(b, l + 1);
		}
	};
	final static Parser recover_parenthesised_parser_ = new Parser()
	{
		public boolean parse(PsiBuilder b, int l)
		{
			return recover_parenthesised(b, l + 1);
		}
	};
	final static Parser recover_regex_code_parser_ = new Parser()
	{
		public boolean parse(PsiBuilder b, int l)
		{
			return recover_regex_code(b, l + 1);
		}
	};
	final static Parser recover_signature_content_parser_ = new Parser()
	{
		public boolean parse(PsiBuilder b, int l)
		{
			return recover_signature_content(b, l + 1);
		}
	};
	final static Parser recover_statement_parser_ = new Parser()
	{
		public boolean parse(PsiBuilder b, int l)
		{
			return recover_statement(b, l + 1);
		}
	};

	/* ********************************************************** */
	// annotation_unknown
	//     | annotation_abstract
	//     | annotation_deprecated
	//     | annotation_method
	//     | annotation_returns_ref
	//     | annotation_returns_arrayref
	//     | annotation_returns_hashref
	//     | annotation_override
	//     | annotation_incomplete
	public static boolean annotation(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "annotation")) return false;
		if (!nextTokenIs(b, ANNOTATION_PREFIX)) return false;
		boolean r;
		Marker m = enter_section_(b, l, _COLLAPSE_, null);
		r = annotation_unknown(b, l + 1);
		if (!r) r = annotation_abstract(b, l + 1);
		if (!r) r = annotation_deprecated(b, l + 1);
		if (!r) r = annotation_method(b, l + 1);
		if (!r) r = annotation_returns_ref(b, l + 1);
		if (!r) r = annotation_returns_arrayref(b, l + 1);
		if (!r) r = annotation_returns_hashref(b, l + 1);
		if (!r) r = annotation_override(b, l + 1);
		if (!r) r = annotation_incomplete(b, l + 1);
		exit_section_(b, l, m, ANNOTATION, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// ANNOTATION_PREFIX ANNOTATION_ABSTRACT_KEY
	public static boolean annotation_abstract(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "annotation_abstract")) return false;
		if (!nextTokenIs(b, ANNOTATION_PREFIX)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeTokens(b, 0, ANNOTATION_PREFIX, ANNOTATION_ABSTRACT_KEY);
		exit_section_(b, m, ANNOTATION_ABSTRACT, r);
		return r;
	}

	/* ********************************************************** */
	// ANNOTATION_PREFIX ANNOTATION_DEPRECATED_KEY
	public static boolean annotation_deprecated(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "annotation_deprecated")) return false;
		if (!nextTokenIs(b, ANNOTATION_PREFIX)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeTokens(b, 0, ANNOTATION_PREFIX, ANNOTATION_DEPRECATED_KEY);
		exit_section_(b, m, ANNOTATION_DEPRECATED, r);
		return r;
	}

	/* ********************************************************** */
	// ANNOTATION_PREFIX <<parseIncompleteAnnotation>>
	public static boolean annotation_incomplete(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "annotation_incomplete")) return false;
		if (!nextTokenIs(b, ANNOTATION_PREFIX)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = consumeToken(b, ANNOTATION_PREFIX);
		p = r; // pin = 1
		r = r && parseIncompleteAnnotation(b, l + 1);
		exit_section_(b, l, m, ANNOTATION_INCOMPLETE, r, p, null);
		return r || p;
	}

	/* ********************************************************** */
	// ANNOTATION_PREFIX ANNOTATION_METHOD_KEY
	public static boolean annotation_method(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "annotation_method")) return false;
		if (!nextTokenIs(b, ANNOTATION_PREFIX)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeTokens(b, 0, ANNOTATION_PREFIX, ANNOTATION_METHOD_KEY);
		exit_section_(b, m, ANNOTATION_METHOD, r);
		return r;
	}

	/* ********************************************************** */
	// ANNOTATION_PREFIX ANNOTATION_OVERRIDE_KEY
	public static boolean annotation_override(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "annotation_override")) return false;
		if (!nextTokenIs(b, ANNOTATION_PREFIX)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeTokens(b, 0, ANNOTATION_PREFIX, ANNOTATION_OVERRIDE_KEY);
		exit_section_(b, m, ANNOTATION_OVERRIDE, r);
		return r;
	}

	/* ********************************************************** */
	// ANNOTATION_PREFIX ANNOTATION_RETURNS_KEY namespace_element_bracketed
	public static boolean annotation_returns_arrayref(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "annotation_returns_arrayref")) return false;
		if (!nextTokenIs(b, ANNOTATION_PREFIX)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeTokens(b, 0, ANNOTATION_PREFIX, ANNOTATION_RETURNS_KEY);
		r = r && namespace_element_bracketed(b, l + 1);
		exit_section_(b, m, ANNOTATION_RETURNS_ARRAYREF, r);
		return r;
	}

	/* ********************************************************** */
	// ANNOTATION_PREFIX ANNOTATION_RETURNS_KEY namespace_element_braced
	public static boolean annotation_returns_hashref(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "annotation_returns_hashref")) return false;
		if (!nextTokenIs(b, ANNOTATION_PREFIX)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeTokens(b, 0, ANNOTATION_PREFIX, ANNOTATION_RETURNS_KEY);
		r = r && namespace_element_braced(b, l + 1);
		exit_section_(b, m, ANNOTATION_RETURNS_HASHREF, r);
		return r;
	}

	/* ********************************************************** */
	// ANNOTATION_PREFIX ANNOTATION_RETURNS_KEY <<mergePackageName>>
	public static boolean annotation_returns_ref(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "annotation_returns_ref")) return false;
		if (!nextTokenIs(b, ANNOTATION_PREFIX)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeTokens(b, 0, ANNOTATION_PREFIX, ANNOTATION_RETURNS_KEY);
		r = r && mergePackageName(b, l + 1);
		exit_section_(b, m, ANNOTATION_RETURNS_REF, r);
		return r;
	}

	/* ********************************************************** */
	// ANNOTATION_PREFIX ANNOTATION_UNKNOWN_KEY
	public static boolean annotation_unknown(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "annotation_unknown")) return false;
		if (!nextTokenIs(b, ANNOTATION_PREFIX)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeTokens(b, 0, ANNOTATION_PREFIX, ANNOTATION_UNKNOWN_KEY);
		exit_section_(b, m, ANNOTATION_UNKNOWN, r);
		return r;
	}

	/* ********************************************************** */
	// annotation+
	static boolean annotations(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "annotations")) return false;
		if (!nextTokenIs(b, ANNOTATION_PREFIX)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = annotation(b, l + 1);
		int c = current_position_(b);
		while (r)
		{
			if (!annotation(b, l + 1)) break;
			if (!empty_element_parsed_guard_(b, "annotations", c)) break;
			c = current_position_(b);
		}
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// LEFT_BRACKET [expr] RIGHT_BRACKET
	public static boolean anon_array(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "anon_array")) return false;
		if (!nextTokenIs(b, LEFT_BRACKET)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, LEFT_BRACKET);
		r = r && anon_array_1(b, l + 1);
		r = r && consumeToken(b, RIGHT_BRACKET);
		exit_section_(b, m, ANON_ARRAY, r);
		return r;
	}

	// [expr]
	private static boolean anon_array_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "anon_array_1")) return false;
		expr(b, l + 1, -1);
		return true;
	}

	/* ********************************************************** */
	// parenthesised_expr array_index
	public static boolean anon_array_element(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "anon_array_element")) return false;
		if (!nextTokenIs(b, LEFT_PAREN)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = parenthesised_expr(b, l + 1);
		r = r && array_index(b, l + 1);
		exit_section_(b, m, ANON_ARRAY_ELEMENT, r);
		return r;
	}

	/* ********************************************************** */
	// LEFT_BRACE [expr] RIGHT_BRACE <<validateAnonHashSuffix>>
	public static boolean anon_hash(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "anon_hash")) return false;
		if (!nextTokenIs(b, LEFT_BRACE)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, LEFT_BRACE);
		r = r && anon_hash_1(b, l + 1);
		r = r && consumeToken(b, RIGHT_BRACE);
		r = r && validateAnonHashSuffix(b, l + 1);
		exit_section_(b, m, ANON_HASH, r);
		return r;
	}

	// [expr]
	private static boolean anon_hash_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "anon_hash_1")) return false;
		expr(b, l + 1, -1);
		return true;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER] RESERVED_SUB block
	public static boolean anon_sub(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "anon_sub")) return false;
		if (!nextTokenIs(b, "<anon sub>", PACKAGE_CORE_IDENTIFIER, RESERVED_SUB)) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<anon sub>");
		r = anon_sub_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_SUB);
		r = r && block(b, l + 1);
		exit_section_(b, l, m, ANON_SUB, r, false, null);
		return r;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean anon_sub_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "anon_sub_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// <<parseArrayOrSlice>>
	static boolean array(PsiBuilder b, int l)
	{
		return parseArrayOrSlice(b, l + 1);
	}

	/* ********************************************************** */
	// array_primitive array_index
	public static boolean array_array_slice(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "array_array_slice")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _COLLAPSE_, "<array array slice>");
		r = array_primitive(b, l + 1);
		r = r && array_index(b, l + 1);
		exit_section_(b, l, m, ARRAY_ARRAY_SLICE, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// SIGIL_ARRAY cast_target
	public static boolean array_cast_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "array_cast_expr")) return false;
		if (!nextTokenIs(b, SIGIL_ARRAY)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, SIGIL_ARRAY);
		r = r && cast_target(b, l + 1);
		exit_section_(b, m, ARRAY_CAST_EXPR, r);
		return r;
	}

	/* ********************************************************** */
	// array_primitive hash_index
	public static boolean array_hash_slice(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "array_hash_slice")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _COLLAPSE_, "<array hash slice>");
		r = array_primitive(b, l + 1);
		r = r && hash_index(b, l + 1);
		exit_section_(b, l, m, ARRAY_HASH_SLICE, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// <<parseArrayIndex>>
	public static boolean array_index(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "array_index")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<array index>");
		r = parseArrayIndex(b, l + 1);
		exit_section_(b, l, m, ARRAY_INDEX, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// SIGIL_SCALAR_INDEX variable_body
	public static boolean array_index_variable(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "array_index_variable")) return false;
		if (!nextTokenIs(b, SIGIL_SCALAR_INDEX)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, SIGIL_SCALAR_INDEX);
		r = r && variable_body(b, l + 1);
		exit_section_(b, m, ARRAY_INDEX_VARIABLE, r);
		return r;
	}

	/* ********************************************************** */
	// array_variable
	//     | array_cast_expr
	//     | string_list
	static boolean array_primitive(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "array_primitive")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = array_variable(b, l + 1);
		if (!r) r = array_cast_expr(b, l + 1);
		if (!r) r = string_list(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// SIGIL_ARRAY variable_body
	public static boolean array_variable(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "array_variable")) return false;
		if (!nextTokenIs(b, SIGIL_ARRAY)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, SIGIL_ARRAY);
		r = r && variable_body(b, l + 1);
		exit_section_(b, m, ARRAY_VARIABLE, r);
		return r;
	}

	/* ********************************************************** */
	// IDENTIFIER [string_sq_parsed]
	public static boolean attribute(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "attribute")) return false;
		if (!nextTokenIs(b, IDENTIFIER)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, IDENTIFIER);
		r = r && attribute_1(b, l + 1);
		exit_section_(b, m, ATTRIBUTE, r);
		return r;
	}

	// [string_sq_parsed]
	private static boolean attribute_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "attribute_1")) return false;
		string_sq_parsed(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// LEFT_BRACE block_content RIGHT_BRACE
	public static boolean block(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "block")) return false;
		if (!nextTokenIs(b, LEFT_BRACE)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = consumeToken(b, LEFT_BRACE);
		p = r; // pin = 1
		r = r && report_error_(b, block_content(b, l + 1));
		r = p && consumeToken(b, RIGHT_BRACE) && r;
		exit_section_(b, l, m, BLOCK, r, p, null);
		return r || p;
	}

	/* ********************************************************** */
	// block [continue_block]
	static boolean block_compound(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "block_compound")) return false;
		if (!nextTokenIs(b, LEFT_BRACE)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = block(b, l + 1);
		r = r && block_compound_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// [continue_block]
	private static boolean block_compound_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "block_compound_1")) return false;
		continue_block(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// file_item *
	static boolean block_content(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "block_content")) return false;
		Marker m = enter_section_(b, l, _NONE_, null);
		int c = current_position_(b);
		while (true)
		{
			if (!file_item(b, l + 1)) break;
			if (!empty_element_parsed_guard_(b, "block_content", c)) break;
			c = current_position_(b);
		}
		exit_section_(b, l, m, null, true, false, recover_block_parser_);
		return true;
	}

	/* ********************************************************** */
	// LEFT_BRACE <<parseBracedCastContent>> RIGHT_BRACE
	static boolean braced_cast(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "braced_cast")) return false;
		if (!nextTokenIs(b, LEFT_BRACE)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = consumeToken(b, LEFT_BRACE);
		p = r; // pin = 1
		r = r && report_error_(b, parseBracedCastContent(b, l + 1));
		r = p && consumeToken(b, RIGHT_BRACE) && r;
		exit_section_(b, l, m, null, r, p, null);
		return r || p;
	}

	/* ********************************************************** */
	// anon_hash comma list_expr
	//     | block [[comma] list_expr]
	//     | list_expr
	public static boolean call_arguments(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "call_arguments")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<call arguments>");
		r = call_arguments_0(b, l + 1);
		if (!r) r = call_arguments_1(b, l + 1);
		if (!r) r = list_expr(b, l + 1);
		exit_section_(b, l, m, CALL_ARGUMENTS, r, false, null);
		return r;
	}

	// anon_hash comma list_expr
	private static boolean call_arguments_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "call_arguments_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = anon_hash(b, l + 1);
		r = r && comma(b, l + 1);
		r = r && list_expr(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// block [[comma] list_expr]
	private static boolean call_arguments_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "call_arguments_1")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = block(b, l + 1);
		r = r && call_arguments_1_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// [[comma] list_expr]
	private static boolean call_arguments_1_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "call_arguments_1_1")) return false;
		call_arguments_1_1_0(b, l + 1);
		return true;
	}

	// [comma] list_expr
	private static boolean call_arguments_1_1_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "call_arguments_1_1_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = call_arguments_1_1_0_0(b, l + 1);
		r = r && list_expr(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// [comma]
	private static boolean call_arguments_1_1_0_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "call_arguments_1_1_0_0")) return false;
		comma(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// method | code
	static boolean callable(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "callable")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = method(b, l + 1);
		if (!r) r = code(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// [namespace_canonical] sub_definition_name
	static boolean canonical_sub_name(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "canonical_sub_name")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = canonical_sub_name_0(b, l + 1);
		r = r && sub_definition_name(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// [namespace_canonical]
	private static boolean canonical_sub_name_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "canonical_sub_name_0")) return false;
		namespace_canonical(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// braced_cast | scalar_primitive
	static boolean cast_target(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "cast_target")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = braced_cast(b, l + 1);
		if (!r) r = scalar_primitive(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// <<parseAmbiguousSigil OPERATOR_BITWISE_AND SIGIL_CODE>> method
	//     | code_variable
	//     | code_cast_expr
	static boolean code(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "code")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = code_0(b, l + 1);
		if (!r) r = code_variable(b, l + 1);
		if (!r) r = code_cast_expr(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// <<parseAmbiguousSigil OPERATOR_BITWISE_AND SIGIL_CODE>> method
	private static boolean code_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "code_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = parseAmbiguousSigil(b, l + 1, OPERATOR_BITWISE_AND, SIGIL_CODE);
		r = r && method(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// <<parseAmbiguousSigil OPERATOR_BITWISE_AND SIGIL_CODE>> cast_target
	public static boolean code_cast_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "code_cast_expr")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _COLLAPSE_, "<expression>");
		r = parseAmbiguousSigil(b, l + 1, OPERATOR_BITWISE_AND, SIGIL_CODE);
		r = r && cast_target(b, l + 1);
		exit_section_(b, l, m, CODE_CAST_EXPR, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// <<parseAmbiguousSigil OPERATOR_BITWISE_AND SIGIL_CODE>> variable_body
	public static boolean code_variable(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "code_variable")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<code variable>");
		r = parseAmbiguousSigil(b, l + 1, OPERATOR_BITWISE_AND, SIGIL_CODE);
		r = r && variable_body(b, l + 1);
		exit_section_(b, l, m, CODE_VARIABLE, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// OPERATOR_COMMA | OPERATOR_COMMA_ARROW
	static boolean comma(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "comma")) return false;
		if (!nextTokenIs(b, "", OPERATOR_COMMA, OPERATOR_COMMA_ARROW)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, OPERATOR_COMMA);
		if (!r) r = consumeToken(b, OPERATOR_COMMA_ARROW);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER]  RESERVED_QR match_regex_body
	public static boolean compile_regex(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "compile_regex")) return false;
		if (!nextTokenIs(b, "<compile regex>", PACKAGE_CORE_IDENTIFIER, RESERVED_QR)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<compile regex>");
		r = compile_regex_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_QR);
		p = r; // pin = 2
		r = r && match_regex_body(b, l + 1);
		exit_section_(b, l, m, COMPILE_REGEX, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean compile_regex_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "compile_regex_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// sub_definition
	//     | named_block
	//     | if_compound
	//     | unless_compound
	//     | given_compound
	//     | while_compound
	//     | until_compound
	//     | for_compound
	//     | foreach_compound
	//     | when_compound
	//     | default_compound
	static boolean compound_statement(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "compound_statement")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = sub_definition(b, l + 1);
		if (!r) r = named_block(b, l + 1);
		if (!r) r = if_compound(b, l + 1);
		if (!r) r = unless_compound(b, l + 1);
		if (!r) r = given_compound(b, l + 1);
		if (!r) r = while_compound(b, l + 1);
		if (!r) r = until_compound(b, l + 1);
		if (!r) r = for_compound(b, l + 1);
		if (!r) r = foreach_compound(b, l + 1);
		if (!r) r = when_compound(b, l + 1);
		if (!r) r = default_compound(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// LEFT_PAREN condition_statement_expr RIGHT_PAREN
	public static boolean condition_statement(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "condition_statement")) return false;
		if (!nextTokenIs(b, LEFT_PAREN)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = consumeToken(b, LEFT_PAREN);
		p = r; // pin = 1
		r = r && report_error_(b, condition_statement_expr(b, l + 1));
		r = p && consumeToken(b, RIGHT_PAREN) && r;
		exit_section_(b, l, m, CONDITION_STATEMENT, r, p, null);
		return r || p;
	}

	/* ********************************************************** */
	// expr
	static boolean condition_statement_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "condition_statement_expr")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<expression>");
		r = expr(b, l + 1, -1);
		exit_section_(b, l, m, null, r, false, recover_condition_statement_expr_parser_);
		return r;
	}

	/* ********************************************************** */
	// LEFT_PAREN [condition_statement_expr] RIGHT_PAREN
	public static boolean condition_statement_while(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "condition_statement_while")) return false;
		if (!nextTokenIs(b, LEFT_PAREN)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = consumeToken(b, LEFT_PAREN);
		p = r; // pin = 1
		r = r && report_error_(b, condition_statement_while_1(b, l + 1));
		r = p && consumeToken(b, RIGHT_PAREN) && r;
		exit_section_(b, l, m, CONDITION_STATEMENT_WHILE, r, p, null);
		return r || p;
	}

	// [condition_statement_expr]
	private static boolean condition_statement_while_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "condition_statement_while_1")) return false;
		condition_statement_expr(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// condition_statement block
	public static boolean conditional_block(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "conditional_block")) return false;
		if (!nextTokenIs(b, LEFT_PAREN)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = condition_statement(b, l + 1);
		p = r; // pin = 1
		r = r && block(b, l + 1);
		exit_section_(b, l, m, CONDITIONAL_BLOCK, r, p, null);
		return r || p;
	}

	/* ********************************************************** */
	// condition_statement_while block
	public static boolean conditional_block_while(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "conditional_block_while")) return false;
		if (!nextTokenIs(b, LEFT_PAREN)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = condition_statement_while(b, l + 1);
		p = r; // pin = 1
		r = r && block(b, l + 1);
		exit_section_(b, l, m, CONDITIONAL_BLOCK_WHILE, r, p, null);
		return r || p;
	}

	/* ********************************************************** */
	// constant_definition | scalar_expr
	static boolean constant_block_content_element(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "constant_block_content_element")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = constant_definition(b, l + 1);
		if (!r) r = scalar_expr(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// <<parseConstantDefinition>> comma scalar_expr
	public static boolean constant_definition(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "constant_definition")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<constant definition>");
		r = parseConstantDefinition(b, l + 1);
		r = r && comma(b, l + 1);
		r = r && scalar_expr(b, l + 1);
		exit_section_(b, l, m, CONSTANT_DEFINITION, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// STRING_CONTENT
	public static boolean constant_name(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "constant_name")) return false;
		if (!nextTokenIs(b, STRING_CONTENT)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, STRING_CONTENT);
		exit_section_(b, m, CONSTANT_NAME, r);
		return r;
	}

	/* ********************************************************** */
	// LEFT_BRACE [constants_block_content] RIGHT_BRACE
	public static boolean constants_block(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "constants_block")) return false;
		if (!nextTokenIs(b, LEFT_BRACE)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = consumeToken(b, LEFT_BRACE);
		p = r; // pin = 1
		r = r && report_error_(b, constants_block_1(b, l + 1));
		r = p && consumeToken(b, RIGHT_BRACE) && r;
		exit_section_(b, l, m, CONSTANTS_BLOCK, r, p, null);
		return r || p;
	}

	// [constants_block_content]
	private static boolean constants_block_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "constants_block_1")) return false;
		constants_block_content(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// constant_block_content_element (comma constant_block_content_element) * [comma+]
	static boolean constants_block_content(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "constants_block_content")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = constant_block_content_element(b, l + 1);
		r = r && constants_block_content_1(b, l + 1);
		r = r && constants_block_content_2(b, l + 1);
		exit_section_(b, l, m, null, r, false, recover_block_parser_);
		return r;
	}

	// (comma constant_block_content_element) *
	private static boolean constants_block_content_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "constants_block_content_1")) return false;
		int c = current_position_(b);
		while (true)
		{
			if (!constants_block_content_1_0(b, l + 1)) break;
			if (!empty_element_parsed_guard_(b, "constants_block_content_1", c)) break;
			c = current_position_(b);
		}
		return true;
	}

	// comma constant_block_content_element
	private static boolean constants_block_content_1_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "constants_block_content_1_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = comma(b, l + 1);
		r = r && constant_block_content_element(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// [comma+]
	private static boolean constants_block_content_2(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "constants_block_content_2")) return false;
		constants_block_content_2_0(b, l + 1);
		return true;
	}

	// comma+
	private static boolean constants_block_content_2_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "constants_block_content_2_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = comma(b, l + 1);
		int c = current_position_(b);
		while (r)
		{
			if (!comma(b, l + 1)) break;
			if (!empty_element_parsed_guard_(b, "constants_block_content_2_0", c)) break;
			c = current_position_(b);
		}
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// [POD] [PACKAGE_CORE_IDENTIFIER] RESERVED_CONTINUE block
	public static boolean continue_block(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "continue_block")) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<continue block>");
		r = continue_block_0(b, l + 1);
		r = r && continue_block_1(b, l + 1);
		r = r && consumeToken(b, RESERVED_CONTINUE);
		p = r; // pin = 3
		r = r && block(b, l + 1);
		exit_section_(b, l, m, CONTINUE_BLOCK, r, p, null);
		return r || p;
	}

	// [POD]
	private static boolean continue_block_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "continue_block_0")) return false;
		consumeToken(b, POD);
		return true;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean continue_block_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "continue_block_1")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER] RESERVED_DEFAULT block
	public static boolean default_compound(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "default_compound")) return false;
		if (!nextTokenIs(b, "<default compound>", PACKAGE_CORE_IDENTIFIER, RESERVED_DEFAULT)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<default compound>");
		r = default_compound_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_DEFAULT);
		p = r; // pin = 2
		r = r && block(b, l + 1);
		exit_section_(b, l, m, DEFAULT_COMPOUND, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean default_compound_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "default_compound_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER] RESERVED_DO eval_argument
	public static boolean do_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "do_expr")) return false;
		if (!nextTokenIs(b, "<expression>", PACKAGE_CORE_IDENTIFIER, RESERVED_DO)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<expression>");
		r = do_expr_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_DO);
		p = r; // pin = 2
		r = r && eval_argument(b, l + 1);
		exit_section_(b, l, m, DO_EXPR, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean do_expr_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "do_expr_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// parenthesised_expr | block | expr
	static boolean eval_argument(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "eval_argument")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = parenthesised_expr(b, l + 1);
		if (!r) r = block(b, l + 1);
		if (!r) r = expr(b, l + 1, -1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER] RESERVED_EVAL [eval_argument]
	public static boolean eval_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "eval_expr")) return false;
		if (!nextTokenIs(b, "<expression>", PACKAGE_CORE_IDENTIFIER, RESERVED_EVAL)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<expression>");
		r = eval_expr_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_EVAL);
		p = r; // pin = 2
		r = r && eval_expr_2(b, l + 1);
		exit_section_(b, l, m, EVAL_EXPR, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean eval_expr_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "eval_expr_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	// [eval_argument]
	private static boolean eval_expr_2(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "eval_expr_2")) return false;
		eval_argument(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// !<<eof>>
	// {
	// 	<<parseHeredocContent>> |
	// 	namespace_definition |
	//     [label_declaration] (
	//         <<parseSemicolon>> +
	//         | nyi_statement
	//         | <<parseParserExtensionStatement>>
	//         | compound_statement
	//         | format_definition
	//         | statement
	//         | block_compound  // put it after statement to handle anon hashes before it.
	//         | annotation
	// 		| POD
	// 		| <<parseBadCharacters>> // Fallback for bad characters
	//      )
	// }
	static boolean file_item(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "file_item")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = file_item_0(b, l + 1);
		r = r && file_item_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// !<<eof>>
	private static boolean file_item_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "file_item_0")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NOT_, null);
		r = !eof(b, l + 1);
		exit_section_(b, l, m, null, r, false, null);
		return r;
	}

	// <<parseHeredocContent>> |
	// 	namespace_definition |
	//     [label_declaration] (
	//         <<parseSemicolon>> +
	//         | nyi_statement
	//         | <<parseParserExtensionStatement>>
	//         | compound_statement
	//         | format_definition
	//         | statement
	//         | block_compound  // put it after statement to handle anon hashes before it.
	//         | annotation
	// 		| POD
	// 		| <<parseBadCharacters>> // Fallback for bad characters
	//      )
	private static boolean file_item_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "file_item_1")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = parseHeredocContent(b, l + 1);
		if (!r) r = namespace_definition(b, l + 1);
		if (!r) r = file_item_1_2(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// [label_declaration] (
	//         <<parseSemicolon>> +
	//         | nyi_statement
	//         | <<parseParserExtensionStatement>>
	//         | compound_statement
	//         | format_definition
	//         | statement
	//         | block_compound  // put it after statement to handle anon hashes before it.
	//         | annotation
	// 		| POD
	// 		| <<parseBadCharacters>> // Fallback for bad characters
	//      )
	private static boolean file_item_1_2(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "file_item_1_2")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = file_item_1_2_0(b, l + 1);
		r = r && file_item_1_2_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// [label_declaration]
	private static boolean file_item_1_2_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "file_item_1_2_0")) return false;
		label_declaration(b, l + 1);
		return true;
	}

	// <<parseSemicolon>> +
	//         | nyi_statement
	//         | <<parseParserExtensionStatement>>
	//         | compound_statement
	//         | format_definition
	//         | statement
	//         | block_compound  // put it after statement to handle anon hashes before it.
	//         | annotation
	// 		| POD
	// 		| <<parseBadCharacters>>
	private static boolean file_item_1_2_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "file_item_1_2_1")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = file_item_1_2_1_0(b, l + 1);
		if (!r) r = nyi_statement(b, l + 1);
		if (!r) r = parseParserExtensionStatement(b, l + 1);
		if (!r) r = compound_statement(b, l + 1);
		if (!r) r = format_definition(b, l + 1);
		if (!r) r = statement(b, l + 1);
		if (!r) r = block_compound(b, l + 1);
		if (!r) r = annotation(b, l + 1);
		if (!r) r = consumeToken(b, POD);
		if (!r) r = parseBadCharacters(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// <<parseSemicolon>> +
	private static boolean file_item_1_2_1_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "file_item_1_2_1_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = parseSemicolon(b, l + 1);
		int c = current_position_(b);
		while (r)
		{
			if (!parseSemicolon(b, l + 1)) break;
			if (!empty_element_parsed_guard_(b, "file_item_1_2_1_0", c)) break;
			c = current_position_(b);
		}
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// file_item*
	static boolean file_items(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "file_items")) return false;
		int c = current_position_(b);
		while (true)
		{
			if (!file_item(b, l + 1)) break;
			if (!empty_element_parsed_guard_(b, "file_items", c)) break;
			c = current_position_(b);
		}
		return true;
	}

	/* ********************************************************** */
	// <<checkFileReadToken>> [read_handle | <<parseFileHandleAsString>> ] <<checkAndConvertToken RIGHT_ANGLE OPERATOR_GT_NUMERIC>>
	public static boolean file_read_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "file_read_expr")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<expression>");
		r = checkFileReadToken(b, l + 1);
		r = r && file_read_expr_1(b, l + 1);
		r = r && checkAndConvertToken(b, l + 1, RIGHT_ANGLE, OPERATOR_GT_NUMERIC);
		exit_section_(b, l, m, FILE_READ_EXPR, r, false, null);
		return r;
	}

	// [read_handle | <<parseFileHandleAsString>> ]
	private static boolean file_read_expr_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "file_read_expr_1")) return false;
		file_read_expr_1_0(b, l + 1);
		return true;
	}

	// read_handle | <<parseFileHandleAsString>>
	private static boolean file_read_expr_1_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "file_read_expr_1_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = read_handle(b, l + 1);
		if (!r) r = parseFileHandleAsString(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// <<checkAndConvertToken LEFT_ANGLE OPERATOR_LT_NUMERIC>> [read_handle | <<parseFileHandleAsString>> ] <<checkAndConvertToken RIGHT_ANGLE OPERATOR_GT_NUMERIC>>
	public static boolean file_read_forced_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "file_read_forced_expr")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _COLLAPSE_, "<expression>");
		r = checkAndConvertToken(b, l + 1, LEFT_ANGLE, OPERATOR_LT_NUMERIC);
		r = r && file_read_forced_expr_1(b, l + 1);
		r = r && checkAndConvertToken(b, l + 1, RIGHT_ANGLE, OPERATOR_GT_NUMERIC);
		exit_section_(b, l, m, FILE_READ_FORCED_EXPR, r, false, null);
		return r;
	}

	// [read_handle | <<parseFileHandleAsString>> ]
	private static boolean file_read_forced_expr_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "file_read_forced_expr_1")) return false;
		file_read_forced_expr_1_0(b, l + 1);
		return true;
	}

	// read_handle | <<parseFileHandleAsString>>
	private static boolean file_read_forced_expr_1_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "file_read_forced_expr_1_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = read_handle(b, l + 1);
		if (!r) r = parseFileHandleAsString(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// for_iterator block
	//     | for_list_statement block_compound
	static boolean for_arguments(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "for_arguments")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = for_arguments_0(b, l + 1);
		if (!r) r = for_arguments_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// for_iterator block
	private static boolean for_arguments_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "for_arguments_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = for_iterator(b, l + 1);
		r = r && block(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// for_list_statement block_compound
	private static boolean for_arguments_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "for_arguments_1")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = for_list_statement(b, l + 1);
		r = r && block_compound(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER] RESERVED_FOR for_arguments
	public static boolean for_compound(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "for_compound")) return false;
		if (!nextTokenIs(b, "<for compound>", PACKAGE_CORE_IDENTIFIER, RESERVED_FOR)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<for compound>");
		r = for_compound_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_FOR);
		p = r; // pin = 2
		r = r && for_arguments(b, l + 1);
		exit_section_(b, l, m, FOR_COMPOUND, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean for_compound_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "for_compound_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// LEFT_PAREN [for_iterator_statement]  SEMICOLON [for_iterator_statement] SEMICOLON [for_iterator_statement] RIGHT_PAREN
	public static boolean for_iterator(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "for_iterator")) return false;
		if (!nextTokenIs(b, LEFT_PAREN)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = consumeToken(b, LEFT_PAREN);
		r = r && for_iterator_1(b, l + 1);
		r = r && consumeToken(b, SEMICOLON);
		p = r; // pin = 3
		r = r && report_error_(b, for_iterator_3(b, l + 1));
		r = p && report_error_(b, consumeToken(b, SEMICOLON)) && r;
		r = p && report_error_(b, for_iterator_5(b, l + 1)) && r;
		r = p && consumeToken(b, RIGHT_PAREN) && r;
		exit_section_(b, l, m, FOR_ITERATOR, r, p, null);
		return r || p;
	}

	// [for_iterator_statement]
	private static boolean for_iterator_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "for_iterator_1")) return false;
		for_iterator_statement(b, l + 1);
		return true;
	}

	// [for_iterator_statement]
	private static boolean for_iterator_3(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "for_iterator_3")) return false;
		for_iterator_statement(b, l + 1);
		return true;
	}

	// [for_iterator_statement]
	private static boolean for_iterator_5(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "for_iterator_5")) return false;
		for_iterator_statement(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// expr
	public static boolean for_iterator_statement(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "for_iterator_statement")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<for iterator statement>");
		r = expr(b, l + 1, -1);
		exit_section_(b, l, m, FOR_ITERATOR_STATEMENT, r, false, recover_parenthesised_parser_);
		return r;
	}

	/* ********************************************************** */
	// LEFT_PAREN for_list_expr_content RIGHT_PAREN
	public static boolean for_list_epxr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "for_list_epxr")) return false;
		if (!nextTokenIs(b, LEFT_PAREN)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = consumeToken(b, LEFT_PAREN);
		p = r; // pin = 1
		r = r && report_error_(b, for_list_expr_content(b, l + 1));
		r = p && consumeToken(b, RIGHT_PAREN) && r;
		exit_section_(b, l, m, FOR_LIST_EPXR, r, p, null);
		return r || p;
	}

	/* ********************************************************** */
	// list_expr
	static boolean for_list_expr_content(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "for_list_expr_content")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = list_expr(b, l + 1);
		exit_section_(b, l, m, null, r, false, recover_parenthesised_parser_);
		return r;
	}

	/* ********************************************************** */
	// [ variable_declaration | variable ] for_list_epxr
	public static boolean for_list_statement(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "for_list_statement")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<for list statement>");
		r = for_list_statement_0(b, l + 1);
		r = r && for_list_epxr(b, l + 1);
		exit_section_(b, l, m, FOR_LIST_STATEMENT, r, false, null);
		return r;
	}

	// [ variable_declaration | variable ]
	private static boolean for_list_statement_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "for_list_statement_0")) return false;
		for_list_statement_0_0(b, l + 1);
		return true;
	}

	// variable_declaration | variable
	private static boolean for_list_statement_0_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "for_list_statement_0_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = variable_declaration(b, l + 1);
		if (!r) r = variable(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER]  RESERVED_FOR expr
	public static boolean for_statement_modifier(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "for_statement_modifier")) return false;
		if (!nextTokenIs(b, "<Postfix for>", PACKAGE_CORE_IDENTIFIER, RESERVED_FOR)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<Postfix for>");
		r = for_statement_modifier_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_FOR);
		p = r; // pin = 2
		r = r && expr(b, l + 1, -1);
		exit_section_(b, l, m, FOR_STATEMENT_MODIFIER, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean for_statement_modifier_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "for_statement_modifier_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER] RESERVED_FOREACH for_arguments
	public static boolean foreach_compound(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "foreach_compound")) return false;
		if (!nextTokenIs(b, "<foreach compound>", PACKAGE_CORE_IDENTIFIER, RESERVED_FOREACH)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<foreach compound>");
		r = foreach_compound_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_FOREACH);
		p = r; // pin = 2
		r = r && for_arguments(b, l + 1);
		exit_section_(b, l, m, FOREACH_COMPOUND, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean foreach_compound_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "foreach_compound_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER]  RESERVED_FOREACH expr
	public static boolean foreach_statement_modifier(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "foreach_statement_modifier")) return false;
		if (!nextTokenIs(b, "<Postfix foreach>", PACKAGE_CORE_IDENTIFIER, RESERVED_FOREACH)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<Postfix foreach>");
		r = foreach_statement_modifier_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_FOREACH);
		p = r; // pin = 2
		r = r && expr(b, l + 1, -1);
		exit_section_(b, l, m, FOREACH_STATEMENT_MODIFIER, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean foreach_statement_modifier_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "foreach_statement_modifier_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER] RESERVED_FORMAT [IDENTIFIER] OPERATOR_ASSIGN [FORMAT] FORMAT_TERMINATOR
	public static boolean format_definition(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "format_definition")) return false;
		if (!nextTokenIs(b, "<format definition>", PACKAGE_CORE_IDENTIFIER, RESERVED_FORMAT)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<format definition>");
		r = format_definition_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_FORMAT);
		p = r; // pin = 2
		r = r && report_error_(b, format_definition_2(b, l + 1));
		r = p && report_error_(b, consumeToken(b, OPERATOR_ASSIGN)) && r;
		r = p && report_error_(b, format_definition_4(b, l + 1)) && r;
		r = p && consumeToken(b, FORMAT_TERMINATOR) && r;
		exit_section_(b, l, m, FORMAT_DEFINITION, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean format_definition_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "format_definition_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	// [IDENTIFIER]
	private static boolean format_definition_2(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "format_definition_2")) return false;
		consumeToken(b, IDENTIFIER);
		return true;
	}

	// [FORMAT]
	private static boolean format_definition_4(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "format_definition_4")) return false;
		consumeToken(b, FORMAT);
		return true;
	}

	/* ********************************************************** */
	// [annotations] <<checkAndConvertToken RESERVED_SUB RESERVED_FUNC>> sub_definition_name [func_signature] [sub_attributes] block
	public static boolean func_definition(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "func_definition")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<func definition>");
		r = func_definition_0(b, l + 1);
		r = r && checkAndConvertToken(b, l + 1, RESERVED_SUB, RESERVED_FUNC);
		r = r && sub_definition_name(b, l + 1);
		r = r && func_definition_3(b, l + 1);
		r = r && func_definition_4(b, l + 1);
		r = r && block(b, l + 1);
		exit_section_(b, l, m, FUNC_DEFINITION, r, false, null);
		return r;
	}

	// [annotations]
	private static boolean func_definition_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "func_definition_0")) return false;
		annotations(b, l + 1);
		return true;
	}

	// [func_signature]
	private static boolean func_definition_3(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "func_definition_3")) return false;
		func_signature(b, l + 1);
		return true;
	}

	// [sub_attributes]
	private static boolean func_definition_4(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "func_definition_4")) return false;
		sub_attributes(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// LEFT_PAREN func_signature_content RIGHT_PAREN
	static boolean func_signature(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "func_signature")) return false;
		if (!nextTokenIs(b, LEFT_PAREN)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = consumeToken(b, LEFT_PAREN);
		p = r; // pin = 1
		r = r && report_error_(b, func_signature_content(b, l + 1));
		r = p && consumeToken(b, RIGHT_PAREN) && r;
		exit_section_(b, l, m, null, r, p, null);
		return r || p;
	}

	/* ********************************************************** */
	// [variable_parenthesised_declaration_contents]
	public static boolean func_signature_content(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "func_signature_content")) return false;
		Marker m = enter_section_(b, l, _NONE_, "<func signature content>");
		variable_parenthesised_declaration_contents(b, l + 1);
		exit_section_(b, l, m, FUNC_SIGNATURE_CONTENT, true, false, recover_signature_content_parser_);
		return true;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER] RESERVED_GIVEN conditional_block
	public static boolean given_compound(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "given_compound")) return false;
		if (!nextTokenIs(b, "<given compound>", PACKAGE_CORE_IDENTIFIER, RESERVED_GIVEN)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<given compound>");
		r = given_compound_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_GIVEN);
		p = r; // pin = 2
		r = r && conditional_block(b, l + 1);
		exit_section_(b, l, m, GIVEN_COMPOUND, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean given_compound_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "given_compound_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// <<parseGlobOrElement>>
	static boolean glob(PsiBuilder b, int l)
	{
		return parseGlobOrElement(b, l + 1);
	}

	/* ********************************************************** */
	// <<parseAmbiguousSigil OPERATOR_MUL SIGIL_GLOB>> cast_target
	public static boolean glob_cast_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "glob_cast_expr")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _COLLAPSE_, "<expression>");
		r = parseAmbiguousSigil(b, l + 1, OPERATOR_MUL, SIGIL_GLOB);
		r = r && cast_target(b, l + 1);
		exit_section_(b, l, m, GLOB_CAST_EXPR, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// glob_variable
	//     | glob_cast_expr
	static boolean glob_primitive(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "glob_primitive")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = glob_variable(b, l + 1);
		if (!r) r = glob_cast_expr(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// glob_primitive hash_index
	public static boolean glob_slot(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "glob_slot")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<glob slot>");
		r = glob_primitive(b, l + 1);
		r = r && hash_index(b, l + 1);
		exit_section_(b, l, m, GLOB_SLOT, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// <<parseAmbiguousSigil OPERATOR_MUL SIGIL_GLOB>> variable_body
	public static boolean glob_variable(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "glob_variable")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<glob variable>");
		r = parseAmbiguousSigil(b, l + 1, OPERATOR_MUL, SIGIL_GLOB);
		r = r && variable_body(b, l + 1);
		exit_section_(b, l, m, GLOB_VARIABLE, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// <<convertIdentifier LABEL>> | OPERATOR_BITWISE_AND <<convertIdentifier SUB>> | expr
	static boolean goto_param(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "goto_param")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = convertIdentifier(b, l + 1, LABEL);
		if (!r) r = goto_param_1(b, l + 1);
		if (!r) r = expr(b, l + 1, -1);
		exit_section_(b, m, null, r);
		return r;
	}

	// OPERATOR_BITWISE_AND <<convertIdentifier SUB>>
	private static boolean goto_param_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "goto_param_1")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, OPERATOR_BITWISE_AND);
		r = r && convertIdentifier(b, l + 1, SUB);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER] RESERVED_GREP grep_map_arguments
	public static boolean grep_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "grep_expr")) return false;
		if (!nextTokenIs(b, "<expression>", PACKAGE_CORE_IDENTIFIER, RESERVED_GREP)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<expression>");
		r = grep_expr_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_GREP);
		p = r; // pin = 2
		r = r && grep_map_arguments(b, l + 1);
		exit_section_(b, l, m, GREP_EXPR, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean grep_expr_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "grep_expr_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// LEFT_PAREN grep_map_arguments_ RIGHT_PAREN !LEFT_BRACKET
	//     | grep_map_arguments_
	static boolean grep_map_arguments(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "grep_map_arguments")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = grep_map_arguments_0(b, l + 1);
		if (!r) r = grep_map_arguments_(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// LEFT_PAREN grep_map_arguments_ RIGHT_PAREN !LEFT_BRACKET
	private static boolean grep_map_arguments_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "grep_map_arguments_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, LEFT_PAREN);
		r = r && grep_map_arguments_(b, l + 1);
		r = r && consumeToken(b, RIGHT_PAREN);
		r = r && grep_map_arguments_0_3(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// !LEFT_BRACKET
	private static boolean grep_map_arguments_0_3(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "grep_map_arguments_0_3")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NOT_, null);
		r = !consumeToken(b, LEFT_BRACKET);
		exit_section_(b, l, m, null, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// block [comma] grep_map_sort_tail
	//     | scalar_expr comma grep_map_sort_tail
	//     | expr
	static boolean grep_map_arguments_(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "grep_map_arguments_")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = grep_map_arguments__0(b, l + 1);
		if (!r) r = grep_map_arguments__1(b, l + 1);
		if (!r) r = expr(b, l + 1, -1);
		exit_section_(b, m, null, r);
		return r;
	}

	// block [comma] grep_map_sort_tail
	private static boolean grep_map_arguments__0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "grep_map_arguments__0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = block(b, l + 1);
		r = r && grep_map_arguments__0_1(b, l + 1);
		r = r && grep_map_sort_tail(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// [comma]
	private static boolean grep_map_arguments__0_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "grep_map_arguments__0_1")) return false;
		comma(b, l + 1);
		return true;
	}

	// scalar_expr comma grep_map_sort_tail
	private static boolean grep_map_arguments__1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "grep_map_arguments__1")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = scalar_expr(b, l + 1);
		r = r && comma(b, l + 1);
		r = r && grep_map_sort_tail(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// file_read_forced_expr | list_expr
	static boolean grep_map_sort_tail(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "grep_map_sort_tail")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = file_read_forced_expr(b, l + 1);
		if (!r) r = list_expr(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// hash_variable
	//     | hash_cast_expr
	static boolean hash(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "hash")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = hash_variable(b, l + 1);
		if (!r) r = hash_cast_expr(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// <<parseAmbiguousSigil OPERATOR_MOD SIGIL_HASH>> cast_target
	public static boolean hash_cast_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "hash_cast_expr")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _COLLAPSE_, "<expression>");
		r = parseAmbiguousSigil(b, l + 1, OPERATOR_MOD, SIGIL_HASH);
		r = r && cast_target(b, l + 1);
		exit_section_(b, l, m, HASH_CAST_EXPR, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// <<parseHashIndex>>
	public static boolean hash_index(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "hash_index")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<hash index>");
		r = parseHashIndex(b, l + 1);
		exit_section_(b, l, m, HASH_INDEX, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// <<parseAmbiguousSigil OPERATOR_MOD SIGIL_HASH>> variable_body
	public static boolean hash_variable(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "hash_variable")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<hash variable>");
		r = parseAmbiguousSigil(b, l + 1, OPERATOR_MOD, SIGIL_HASH);
		r = r && variable_body(b, l + 1);
		exit_section_(b, l, m, HASH_VARIABLE, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// OPERATOR_HEREDOC string
	public static boolean heredoc_opener(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "heredoc_opener")) return false;
		if (!nextTokenIs(b, OPERATOR_HEREDOC)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = consumeToken(b, OPERATOR_HEREDOC);
		p = r; // pin = 1
		r = r && string(b, l + 1);
		exit_section_(b, l, m, HEREDOC_OPENER, r, p, null);
		return r || p;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER] RESERVED_IF conditional_block if_compound_elsif * [if_compound_else]
	public static boolean if_compound(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "if_compound")) return false;
		if (!nextTokenIs(b, "<if compound>", PACKAGE_CORE_IDENTIFIER, RESERVED_IF)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<if compound>");
		r = if_compound_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_IF);
		p = r; // pin = 2
		r = r && report_error_(b, conditional_block(b, l + 1));
		r = p && report_error_(b, if_compound_3(b, l + 1)) && r;
		r = p && if_compound_4(b, l + 1) && r;
		exit_section_(b, l, m, IF_COMPOUND, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean if_compound_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "if_compound_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	// if_compound_elsif *
	private static boolean if_compound_3(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "if_compound_3")) return false;
		int c = current_position_(b);
		while (true)
		{
			if (!if_compound_elsif(b, l + 1)) break;
			if (!empty_element_parsed_guard_(b, "if_compound_3", c)) break;
			c = current_position_(b);
		}
		return true;
	}

	// [if_compound_else]
	private static boolean if_compound_4(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "if_compound_4")) return false;
		if_compound_else(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// [POD] [PACKAGE_CORE_IDENTIFIER] RESERVED_ELSE unconditional_block
	static boolean if_compound_else(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "if_compound_else")) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = if_compound_else_0(b, l + 1);
		r = r && if_compound_else_1(b, l + 1);
		r = r && consumeToken(b, RESERVED_ELSE);
		p = r; // pin = 3
		r = r && unconditional_block(b, l + 1);
		exit_section_(b, l, m, null, r, p, null);
		return r || p;
	}

	// [POD]
	private static boolean if_compound_else_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "if_compound_else_0")) return false;
		consumeToken(b, POD);
		return true;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean if_compound_else_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "if_compound_else_1")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// [POD] [PACKAGE_CORE_IDENTIFIER] RESERVED_ELSIF conditional_block
	static boolean if_compound_elsif(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "if_compound_elsif")) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = if_compound_elsif_0(b, l + 1);
		r = r && if_compound_elsif_1(b, l + 1);
		r = r && consumeToken(b, RESERVED_ELSIF);
		p = r; // pin = 3
		r = r && conditional_block(b, l + 1);
		exit_section_(b, l, m, null, r, p, null);
		return r || p;
	}

	// [POD]
	private static boolean if_compound_elsif_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "if_compound_elsif_0")) return false;
		consumeToken(b, POD);
		return true;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean if_compound_elsif_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "if_compound_elsif_1")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER]  RESERVED_IF expr
	public static boolean if_statement_modifier(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "if_statement_modifier")) return false;
		if (!nextTokenIs(b, "<Postfix if>", PACKAGE_CORE_IDENTIFIER, RESERVED_IF)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<Postfix if>");
		r = if_statement_modifier_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_IF);
		p = r; // pin = 2
		r = r && expr(b, l + 1, -1);
		exit_section_(b, l, m, IF_STATEMENT_MODIFIER, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean if_statement_modifier_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "if_statement_modifier_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// <<parseExpressionLevel 21>>
	static boolean immutable_expr(PsiBuilder b, int l)
	{
		return parseExpressionLevel(b, l + 1, 21);
	}

	/* ********************************************************** */
	// scalar
	//   | array
	//   | array_index_variable
	static boolean interpolated_constructs(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "interpolated_constructs")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = scalar(b, l + 1);
		if (!r) r = array(b, l + 1);
		if (!r) r = array_index_variable(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// <<parseLabelDeclaration>>
	public static boolean label_declaration(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "label_declaration")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<label declaration>");
		r = parseLabelDeclaration(b, l + 1);
		exit_section_(b, l, m, LABEL_DECLARATION, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// callable LEFT_PAREN [call_arguments] RIGHT_PAREN !LEFT_BRACKET
	static boolean leftward_call_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "leftward_call_expr")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<expression>");
		r = callable(b, l + 1);
		r = r && consumeToken(b, LEFT_PAREN);
		r = r && leftward_call_expr_2(b, l + 1);
		r = r && consumeToken(b, RIGHT_PAREN);
		r = r && leftward_call_expr_4(b, l + 1);
		exit_section_(b, l, m, null, r, false, null);
		return r;
	}

	// [call_arguments]
	private static boolean leftward_call_expr_2(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "leftward_call_expr_2")) return false;
		call_arguments(b, l + 1);
		return true;
	}

	// !LEFT_BRACKET
	private static boolean leftward_call_expr_4(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "leftward_call_expr_4")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NOT_, null);
		r = !consumeToken(b, LEFT_BRACKET);
		exit_section_(b, l, m, null, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// scalar_variable | array_variable | hash_variable
	static boolean lexical_variable(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "lexical_variable")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = scalar_variable(b, l + 1);
		if (!r) r = array_variable(b, l + 1);
		if (!r) r = hash_variable(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// <<parseExpressionLevel 3>>
	static boolean list_expr(PsiBuilder b, int l)
	{
		return parseExpressionLevel(b, l + 1, 3);
	}

	/* ********************************************************** */
	// <<convertIdentifier LABEL>> | expr
	static boolean lnr_param(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "lnr_param")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = convertIdentifier(b, l + 1, LABEL);
		if (!r) r = expr(b, l + 1, -1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// LEFT_PAREN local_variable_declaration_argument (comma + local_variable_declaration_argument ) * comma * RIGHT_PAREN
	static boolean local_parenthesised_declaration(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "local_parenthesised_declaration")) return false;
		if (!nextTokenIs(b, LEFT_PAREN)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = consumeToken(b, LEFT_PAREN);
		p = r; // pin = 1
		r = r && report_error_(b, local_variable_declaration_argument(b, l + 1));
		r = p && report_error_(b, local_parenthesised_declaration_2(b, l + 1)) && r;
		r = p && report_error_(b, local_parenthesised_declaration_3(b, l + 1)) && r;
		r = p && consumeToken(b, RIGHT_PAREN) && r;
		exit_section_(b, l, m, null, r, p, null);
		return r || p;
	}

	// (comma + local_variable_declaration_argument ) *
	private static boolean local_parenthesised_declaration_2(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "local_parenthesised_declaration_2")) return false;
		int c = current_position_(b);
		while (true)
		{
			if (!local_parenthesised_declaration_2_0(b, l + 1)) break;
			if (!empty_element_parsed_guard_(b, "local_parenthesised_declaration_2", c)) break;
			c = current_position_(b);
		}
		return true;
	}

	// comma + local_variable_declaration_argument
	private static boolean local_parenthesised_declaration_2_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "local_parenthesised_declaration_2_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = local_parenthesised_declaration_2_0_0(b, l + 1);
		r = r && local_variable_declaration_argument(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// comma +
	private static boolean local_parenthesised_declaration_2_0_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "local_parenthesised_declaration_2_0_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = comma(b, l + 1);
		int c = current_position_(b);
		while (r)
		{
			if (!comma(b, l + 1)) break;
			if (!empty_element_parsed_guard_(b, "local_parenthesised_declaration_2_0_0", c)) break;
			c = current_position_(b);
		}
		exit_section_(b, m, null, r);
		return r;
	}

	// comma *
	private static boolean local_parenthesised_declaration_3(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "local_parenthesised_declaration_3")) return false;
		int c = current_position_(b);
		while (true)
		{
			if (!comma(b, l + 1)) break;
			if (!empty_element_parsed_guard_(b, "local_parenthesised_declaration_3", c)) break;
			c = current_position_(b);
		}
		return true;
	}

	/* ********************************************************** */
	// strict_variable_declaration_argument | scalar_expr
	static boolean local_variable_declaration_argument(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "local_variable_declaration_argument")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = strict_variable_declaration_argument(b, l + 1);
		if (!r) r = scalar_expr(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// local_parenthesised_declaration | local_variable_declaration_argument
	static boolean local_variable_declaration_variation(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "local_variable_declaration_variation")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = local_parenthesised_declaration(b, l + 1);
		if (!r) r = local_variable_declaration_argument(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER] RESERVED_MAP grep_map_arguments
	public static boolean map_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "map_expr")) return false;
		if (!nextTokenIs(b, "<expression>", PACKAGE_CORE_IDENTIFIER, RESERVED_MAP)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<expression>");
		r = map_expr_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_MAP);
		p = r; // pin = 2
		r = r && grep_map_arguments(b, l + 1);
		exit_section_(b, l, m, MAP_EXPR, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean map_expr_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "map_expr_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER]  [RESERVED_M] match_regex_body
	public static boolean match_regex(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "match_regex")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<match regex>");
		r = match_regex_0(b, l + 1);
		r = r && match_regex_1(b, l + 1);
		r = r && match_regex_body(b, l + 1);
		exit_section_(b, l, m, MATCH_REGEX, r, false, null);
		return r;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean match_regex_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "match_regex_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	// [RESERVED_M]
	private static boolean match_regex_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "match_regex_1")) return false;
		consumeToken(b, RESERVED_M);
		return true;
	}

	/* ********************************************************** */
	// regex_match REGEX_QUOTE_CLOSE [perl_regex_modifiers]
	static boolean match_regex_body(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "match_regex_body")) return false;
		if (!nextTokenIs(b, "", REGEX_QUOTE_OPEN, REGEX_QUOTE_OPEN_X)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = regex_match(b, l + 1);
		r = r && consumeToken(b, REGEX_QUOTE_CLOSE);
		r = r && match_regex_body_2(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// [perl_regex_modifiers]
	private static boolean match_regex_body_2(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "match_regex_body_2")) return false;
		perl_regex_modifiers(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// <<parseMethod>>
	public static boolean method(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "method")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<method>");
		r = parseMethod(b, l + 1);
		exit_section_(b, l, m, METHOD, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// LEFT_PAREN [<<parseSimpleCallArguments>>] RIGHT_PAREN
	static boolean method_call_arguments(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "method_call_arguments")) return false;
		if (!nextTokenIs(b, LEFT_PAREN)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, LEFT_PAREN);
		r = r && method_call_arguments_1(b, l + 1);
		r = r && consumeToken(b, RIGHT_PAREN);
		exit_section_(b, m, null, r);
		return r;
	}

	// [<<parseSimpleCallArguments>>]
	private static boolean method_call_arguments_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "method_call_arguments_1")) return false;
		parseSimpleCallArguments(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// [annotations] <<checkAndConvertToken RESERVED_SUB RESERVED_METHOD>> sub_definition_name [method_signature] [sub_attributes] block
	public static boolean method_definition(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "method_definition")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<method definition>");
		r = method_definition_0(b, l + 1);
		r = r && checkAndConvertToken(b, l + 1, RESERVED_SUB, RESERVED_METHOD);
		r = r && sub_definition_name(b, l + 1);
		r = r && method_definition_3(b, l + 1);
		r = r && method_definition_4(b, l + 1);
		r = r && block(b, l + 1);
		exit_section_(b, l, m, METHOD_DEFINITION, r, false, null);
		return r;
	}

	// [annotations]
	private static boolean method_definition_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "method_definition_0")) return false;
		annotations(b, l + 1);
		return true;
	}

	// [method_signature]
	private static boolean method_definition_3(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "method_definition_3")) return false;
		method_signature(b, l + 1);
		return true;
	}

	// [sub_attributes]
	private static boolean method_definition_4(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "method_definition_4")) return false;
		sub_attributes(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// LEFT_PAREN method_signature_content RIGHT_PAREN
	static boolean method_signature(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "method_signature")) return false;
		if (!nextTokenIs(b, LEFT_PAREN)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = consumeToken(b, LEFT_PAREN);
		p = r; // pin = 1
		r = r && report_error_(b, method_signature_content(b, l + 1));
		r = p && consumeToken(b, RIGHT_PAREN) && r;
		exit_section_(b, l, m, null, r, p, null);
		return r || p;
	}

	/* ********************************************************** */
	// [method_signature_invocant] [variable_parenthesised_declaration_contents]
	public static boolean method_signature_content(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "method_signature_content")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<method signature content>");
		r = method_signature_content_0(b, l + 1);
		r = r && method_signature_content_1(b, l + 1);
		exit_section_(b, l, m, METHOD_SIGNATURE_CONTENT, r, false, recover_signature_content_parser_);
		return r;
	}

	// [method_signature_invocant]
	private static boolean method_signature_content_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "method_signature_content_0")) return false;
		method_signature_invocant(b, l + 1);
		return true;
	}

	// [variable_parenthesised_declaration_contents]
	private static boolean method_signature_content_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "method_signature_content_1")) return false;
		variable_parenthesised_declaration_contents(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// <<scalarDeclarationWrapper>> COLON
	public static boolean method_signature_invocant(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "method_signature_invocant")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<method signature invocant>");
		r = scalarDeclarationWrapper(b, l + 1);
		r = r && consumeToken(b, COLON);
		exit_section_(b, l, m, METHOD_SIGNATURE_INVOCANT, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// BLOCK_NAME block
	public static boolean named_block(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "named_block")) return false;
		if (!nextTokenIs(b, BLOCK_NAME)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, BLOCK_NAME);
		r = r && block(b, l + 1);
		exit_section_(b, m, NAMED_BLOCK, r);
		return r;
	}

	/* ********************************************************** */
	// annotation_deprecated
	static boolean namespace_annotations(PsiBuilder b, int l)
	{
		return annotation_deprecated(b, l + 1);
	}

	/* ********************************************************** */
	// <<convertPackageIdentifier>>
	static boolean namespace_canonical(PsiBuilder b, int l)
	{
		return convertPackageIdentifier(b, l + 1);
	}

	/* ********************************************************** */
	// real_namespace_content
	public static boolean namespace_content(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "namespace_content")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<namespace content>");
		r = real_namespace_content(b, l + 1);
		exit_section_(b, l, m, NAMESPACE_CONTENT, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// namespace_definition_name (block | <<parseSemicolon>> <<parseNamespaceContent>>)
	public static boolean namespace_definition(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "namespace_definition")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<namespace definition>");
		r = namespace_definition_name(b, l + 1);
		r = r && namespace_definition_1(b, l + 1);
		exit_section_(b, l, m, NAMESPACE_DEFINITION, r, false, null);
		return r;
	}

	// block | <<parseSemicolon>> <<parseNamespaceContent>>
	private static boolean namespace_definition_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "namespace_definition_1")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = block(b, l + 1);
		if (!r) r = namespace_definition_1_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// <<parseSemicolon>> <<parseNamespaceContent>>
	private static boolean namespace_definition_1_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "namespace_definition_1_1")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = parseSemicolon(b, l + 1);
		r = r && parseNamespaceContent(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// [namespace_annotations] [PACKAGE_CORE_IDENTIFIER] RESERVED_PACKAGE <<mergePackageName>> [perl_version]
	static boolean namespace_definition_name(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "namespace_definition_name")) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = namespace_definition_name_0(b, l + 1);
		r = r && namespace_definition_name_1(b, l + 1);
		r = r && consumeToken(b, RESERVED_PACKAGE);
		p = r; // pin = 3
		r = r && report_error_(b, mergePackageName(b, l + 1));
		r = p && namespace_definition_name_4(b, l + 1) && r;
		exit_section_(b, l, m, null, r, p, recover_namespace_definition_parser_);
		return r || p;
	}

	// [namespace_annotations]
	private static boolean namespace_definition_name_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "namespace_definition_name_0")) return false;
		namespace_annotations(b, l + 1);
		return true;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean namespace_definition_name_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "namespace_definition_name_1")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	// [perl_version]
	private static boolean namespace_definition_name_4(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "namespace_definition_name_4")) return false;
		perl_version(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// LEFT_BRACE <<mergePackageName>> RIGHT_BRACE
	static boolean namespace_element_braced(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "namespace_element_braced")) return false;
		if (!nextTokenIs(b, LEFT_BRACE)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, LEFT_BRACE);
		r = r && mergePackageName(b, l + 1);
		r = r && consumeToken(b, RIGHT_BRACE);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// LEFT_BRACKET <<mergePackageName>> RIGHT_BRACKET
	static boolean namespace_element_bracketed(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "namespace_element_bracketed")) return false;
		if (!nextTokenIs(b, LEFT_BRACKET)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, LEFT_BRACKET);
		r = r && mergePackageName(b, l + 1);
		r = r && consumeToken(b, RIGHT_BRACKET);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// <<mergePackageName>>
	public static boolean namespace_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "namespace_expr")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _COLLAPSE_, "<expression>");
		r = mergePackageName(b, l + 1);
		exit_section_(b, l, m, NAMESPACE_EXPR, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// method [method_call_arguments]
	public static boolean nested_call(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "nested_call")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<nested call>");
		r = method(b, l + 1);
		r = r && nested_call_1(b, l + 1);
		exit_section_(b, l, m, NESTED_CALL, r, false, null);
		return r;
	}

	// [method_call_arguments]
	private static boolean nested_call_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "nested_call_1")) return false;
		method_call_arguments(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// method_call_arguments
	public static boolean nested_call_arguments(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "nested_call_arguments")) return false;
		if (!nextTokenIs(b, LEFT_PAREN)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = method_call_arguments(b, l + 1);
		exit_section_(b, m, NESTED_CALL_ARGUMENTS, r);
		return r;
	}

	/* ********************************************************** */
	// <<parseNestedElementVariation>>
	//     | hash_index
	//     | array_index
	//     | nested_call
	//     | nested_call_arguments    // function call like $var->()
	//     | scalar_call
	static boolean nested_element_variation(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "nested_element_variation")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = parseNestedElementVariation(b, l + 1);
		if (!r) r = hash_index(b, l + 1);
		if (!r) r = array_index(b, l + 1);
		if (!r) r = nested_call(b, l + 1);
		if (!r) r = nested_call_arguments(b, l + 1);
		if (!r) r = scalar_call(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER] RESERVED_NO use_no_parameters
	public static boolean no_statement(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "no_statement")) return false;
		if (!nextTokenIs(b, "<no statement>", PACKAGE_CORE_IDENTIFIER, RESERVED_NO)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<no statement>");
		r = no_statement_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_NO);
		p = r; // pin = 2
		r = r && use_no_parameters(b, l + 1);
		exit_section_(b, l, m, NO_STATEMENT, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean no_statement_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "no_statement_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// no_statement
	static boolean no_statements(PsiBuilder b, int l)
	{
		return no_statement(b, l + 1);
	}

	/* ********************************************************** */
	// expr [<<parseStatementModifier>>]
	static boolean normal_statement(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "normal_statement")) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = expr(b, l + 1, -1);
		p = r; // pin = 1
		r = r && normal_statement_1(b, l + 1);
		exit_section_(b, l, m, null, r, p, null);
		return r || p;
	}

	// [<<parseStatementModifier>>]
	private static boolean normal_statement_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "normal_statement_1")) return false;
		parseStatementModifier(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// NUMBER
	//     | NUMBER_VERSION
	//     | NUMBER_SIMPLE
	public static boolean number_constant(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "number_constant")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<number constant>");
		r = consumeToken(b, NUMBER);
		if (!r) r = consumeToken(b, NUMBER_VERSION);
		if (!r) r = consumeToken(b, NUMBER_SIMPLE);
		exit_section_(b, l, m, NUMBER_CONSTANT, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// OPERATOR_HELLIP
	public static boolean nyi_statement(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "nyi_statement")) return false;
		if (!nextTokenIs(b, OPERATOR_HELLIP)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, OPERATOR_HELLIP);
		exit_section_(b, m, NYI_STATEMENT, r);
		return r;
	}

	/* ********************************************************** */
	// (OPERATOR_PLUS|OPERATOR_MINUS|OPERATOR_CONCAT) !OPERATOR_ASSIGN
	static boolean operator_add(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "operator_add")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = operator_add_0(b, l + 1);
		r = r && operator_add_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// OPERATOR_PLUS|OPERATOR_MINUS|OPERATOR_CONCAT
	private static boolean operator_add_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "operator_add_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, OPERATOR_PLUS);
		if (!r) r = consumeToken(b, OPERATOR_MINUS);
		if (!r) r = consumeToken(b, OPERATOR_CONCAT);
		exit_section_(b, m, null, r);
		return r;
	}

	// !OPERATOR_ASSIGN
	private static boolean operator_add_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "operator_add_1")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NOT_, null);
		r = !consumeToken(b, OPERATOR_ASSIGN);
		exit_section_(b, l, m, null, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// OPERATOR_AND !OPERATOR_ASSIGN
	static boolean operator_and(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "operator_and")) return false;
		if (!nextTokenIs(b, OPERATOR_AND)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, OPERATOR_AND);
		r = r && operator_and_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// !OPERATOR_ASSIGN
	private static boolean operator_and_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "operator_and_1")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NOT_, null);
		r = !consumeToken(b, OPERATOR_ASSIGN);
		exit_section_(b, l, m, null, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// <<checkAndCollapseToken OPERATOR_POW_ASSIGN OPERATOR_MUL OPERATOR_MUL OPERATOR_ASSIGN>>  // **=
	//     |<<checkAndCollapseToken OPERATOR_PLUS_ASSIGN OPERATOR_PLUS OPERATOR_ASSIGN>>      // +=
	//     |<<checkAndCollapseToken OPERATOR_MINUS_ASSIGN OPERATOR_MINUS OPERATOR_ASSIGN>>     // -=
	//     |<<checkAndCollapseToken OPERATOR_MUL_ASSIGN OPERATOR_MUL OPERATOR_ASSIGN>>       // *=
	//     |<<checkAndCollapseToken OPERATOR_DIV_ASSIGN OPERATOR_DIV OPERATOR_ASSIGN>>       // /=
	//     |<<checkAndCollapseToken OPERATOR_MOD_ASSIGN OPERATOR_MOD OPERATOR_ASSIGN>>       // %=
	//     |<<checkAndCollapseToken OPERATOR_CONCAT_ASSIGN OPERATOR_CONCAT OPERATOR_ASSIGN>>    // .=
	//     |<<checkAndCollapseToken OPERATOR_X_ASSIGN OPERATOR_X OPERATOR_ASSIGN>>         // x=
	//     |<<checkAndCollapseToken OPERATOR_BITWISE_AND_ASSIGN OPERATOR_BITWISE_AND OPERATOR_ASSIGN>>   // &=
	//     |<<checkAndCollapseToken OPERATOR_BITWISE_OR_ASSIGN OPERATOR_BITWISE_OR OPERATOR_ASSIGN>>    // |=
	//     |<<checkAndCollapseToken OPERATOR_BITWISE_XOR_ASSIGN OPERATOR_BITWISE_XOR OPERATOR_ASSIGN>>   // ^=
	//     |<<checkAndCollapseToken OPERATOR_SHIFT_LEFT_ASSIGN OPERATOR_SHIFT_LEFT OPERATOR_ASSIGN>>    //  <<=
	//     |<<checkAndCollapseToken OPERATOR_SHIFT_RIGHT_ASSIGN OPERATOR_SHIFT_RIGHT OPERATOR_ASSIGN>>   //  >>=
	//     |<<checkAndCollapseToken OPERATOR_AND_ASSIGN OPERATOR_AND OPERATOR_ASSIGN>>           //  &&=
	//     |<<checkAndCollapseToken OPERATOR_OR_ASSIGN OPERATOR_OR OPERATOR_ASSIGN>>            //  ||=
	//     |<<checkAndCollapseToken OPERATOR_OR_DEFINED_ASSIGN OPERATOR_OR_DEFINED OPERATOR_ASSIGN>>    //  //=
	//     |OPERATOR_ASSIGN !OPERATOR_ASSIGN
	static boolean operator_assign(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "operator_assign")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = checkAndCollapseToken(b, l + 1, OPERATOR_POW_ASSIGN, OPERATOR_MUL, OPERATOR_MUL, OPERATOR_ASSIGN);
		if (!r) r = checkAndCollapseToken(b, l + 1, OPERATOR_PLUS_ASSIGN, OPERATOR_PLUS, OPERATOR_ASSIGN);
		if (!r) r = checkAndCollapseToken(b, l + 1, OPERATOR_MINUS_ASSIGN, OPERATOR_MINUS, OPERATOR_ASSIGN);
		if (!r) r = checkAndCollapseToken(b, l + 1, OPERATOR_MUL_ASSIGN, OPERATOR_MUL, OPERATOR_ASSIGN);
		if (!r) r = checkAndCollapseToken(b, l + 1, OPERATOR_DIV_ASSIGN, OPERATOR_DIV, OPERATOR_ASSIGN);
		if (!r) r = checkAndCollapseToken(b, l + 1, OPERATOR_MOD_ASSIGN, OPERATOR_MOD, OPERATOR_ASSIGN);
		if (!r) r = checkAndCollapseToken(b, l + 1, OPERATOR_CONCAT_ASSIGN, OPERATOR_CONCAT, OPERATOR_ASSIGN);
		if (!r) r = checkAndCollapseToken(b, l + 1, OPERATOR_X_ASSIGN, OPERATOR_X, OPERATOR_ASSIGN);
		if (!r) r = checkAndCollapseToken(b, l + 1, OPERATOR_BITWISE_AND_ASSIGN, OPERATOR_BITWISE_AND, OPERATOR_ASSIGN);
		if (!r) r = checkAndCollapseToken(b, l + 1, OPERATOR_BITWISE_OR_ASSIGN, OPERATOR_BITWISE_OR, OPERATOR_ASSIGN);
		if (!r) r = checkAndCollapseToken(b, l + 1, OPERATOR_BITWISE_XOR_ASSIGN, OPERATOR_BITWISE_XOR, OPERATOR_ASSIGN);
		if (!r) r = checkAndCollapseToken(b, l + 1, OPERATOR_SHIFT_LEFT_ASSIGN, OPERATOR_SHIFT_LEFT, OPERATOR_ASSIGN);
		if (!r) r = checkAndCollapseToken(b, l + 1, OPERATOR_SHIFT_RIGHT_ASSIGN, OPERATOR_SHIFT_RIGHT, OPERATOR_ASSIGN);
		if (!r) r = checkAndCollapseToken(b, l + 1, OPERATOR_AND_ASSIGN, OPERATOR_AND, OPERATOR_ASSIGN);
		if (!r) r = checkAndCollapseToken(b, l + 1, OPERATOR_OR_ASSIGN, OPERATOR_OR, OPERATOR_ASSIGN);
		if (!r) r = checkAndCollapseToken(b, l + 1, OPERATOR_OR_DEFINED_ASSIGN, OPERATOR_OR_DEFINED, OPERATOR_ASSIGN);
		if (!r) r = operator_assign_16(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// OPERATOR_ASSIGN !OPERATOR_ASSIGN
	private static boolean operator_assign_16(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "operator_assign_16")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, OPERATOR_ASSIGN);
		r = r && operator_assign_16_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// !OPERATOR_ASSIGN
	private static boolean operator_assign_16_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "operator_assign_16_1")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NOT_, null);
		r = !consumeToken(b, OPERATOR_ASSIGN);
		exit_section_(b, l, m, null, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// (OPERATOR_BITWISE_OR|OPERATOR_BITWISE_XOR) !OPERATOR_ASSIGN
	static boolean operator_bitwise_or_xor(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "operator_bitwise_or_xor")) return false;
		if (!nextTokenIs(b, "", OPERATOR_BITWISE_OR, OPERATOR_BITWISE_XOR)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = operator_bitwise_or_xor_0(b, l + 1);
		r = r && operator_bitwise_or_xor_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// OPERATOR_BITWISE_OR|OPERATOR_BITWISE_XOR
	private static boolean operator_bitwise_or_xor_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "operator_bitwise_or_xor_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, OPERATOR_BITWISE_OR);
		if (!r) r = consumeToken(b, OPERATOR_BITWISE_XOR);
		exit_section_(b, m, null, r);
		return r;
	}

	// !OPERATOR_ASSIGN
	private static boolean operator_bitwise_or_xor_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "operator_bitwise_or_xor_1")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NOT_, null);
		r = !consumeToken(b, OPERATOR_ASSIGN);
		exit_section_(b, l, m, null, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// <<checkAndCollapseToken OPERATOR_GE_NUMERIC OPERATOR_GT_NUMERIC OPERATOR_ASSIGN>>   // >=
	//     |<<checkAndCollapseToken OPERATOR_LE_NUMERIC OPERATOR_LT_NUMERIC OPERATOR_ASSIGN>>  // <=
	//     |OPERATOR_GT_NUMERIC
	//     |OPERATOR_LT_NUMERIC
	//     |OPERATOR_GE_STR
	//     |OPERATOR_LE_STR
	//     |OPERATOR_LT_STR
	//     |OPERATOR_GT_STR
	static boolean operator_compare(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "operator_compare")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = checkAndCollapseToken(b, l + 1, OPERATOR_GE_NUMERIC, OPERATOR_GT_NUMERIC, OPERATOR_ASSIGN);
		if (!r) r = checkAndCollapseToken(b, l + 1, OPERATOR_LE_NUMERIC, OPERATOR_LT_NUMERIC, OPERATOR_ASSIGN);
		if (!r) r = consumeToken(b, OPERATOR_GT_NUMERIC);
		if (!r) r = consumeToken(b, OPERATOR_LT_NUMERIC);
		if (!r) r = consumeToken(b, OPERATOR_GE_STR);
		if (!r) r = consumeToken(b, OPERATOR_LE_STR);
		if (!r) r = consumeToken(b, OPERATOR_LT_STR);
		if (!r) r = consumeToken(b, OPERATOR_GT_STR);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// <<checkAndCollapseToken OPERATOR_EQ_NUMERIC OPERATOR_ASSIGN OPERATOR_ASSIGN>> // ==
	//     |<<checkAndCollapseToken OPERATOR_NE_NUMERIC OPERATOR_NOT OPERATOR_ASSIGN>>  // !=
	//     |OPERATOR_CMP_NUMERIC
	//     |OPERATOR_EQ_STR
	//     |OPERATOR_NE_STR
	//     |OPERATOR_CMP_STR
	//     |<<checkAndCollapseToken OPERATOR_SMARTMATCH OPERATOR_BITWISE_NOT OPERATOR_BITWISE_NOT>>
	static boolean operator_equal(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "operator_equal")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = checkAndCollapseToken(b, l + 1, OPERATOR_EQ_NUMERIC, OPERATOR_ASSIGN, OPERATOR_ASSIGN);
		if (!r) r = checkAndCollapseToken(b, l + 1, OPERATOR_NE_NUMERIC, OPERATOR_NOT, OPERATOR_ASSIGN);
		if (!r) r = consumeToken(b, OPERATOR_CMP_NUMERIC);
		if (!r) r = consumeToken(b, OPERATOR_EQ_STR);
		if (!r) r = consumeToken(b, OPERATOR_NE_STR);
		if (!r) r = consumeToken(b, OPERATOR_CMP_STR);
		if (!r) r = checkAndCollapseToken(b, l + 1, OPERATOR_SMARTMATCH, OPERATOR_BITWISE_NOT, OPERATOR_BITWISE_NOT);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// (OPERATOR_MUL !OPERATOR_MUL |OPERATOR_DIV|OPERATOR_MOD|OPERATOR_X) !OPERATOR_ASSIGN
	static boolean operator_mul(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "operator_mul")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = operator_mul_0(b, l + 1);
		r = r && operator_mul_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// OPERATOR_MUL !OPERATOR_MUL |OPERATOR_DIV|OPERATOR_MOD|OPERATOR_X
	private static boolean operator_mul_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "operator_mul_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = operator_mul_0_0(b, l + 1);
		if (!r) r = consumeToken(b, OPERATOR_DIV);
		if (!r) r = consumeToken(b, OPERATOR_MOD);
		if (!r) r = consumeToken(b, OPERATOR_X);
		exit_section_(b, m, null, r);
		return r;
	}

	// OPERATOR_MUL !OPERATOR_MUL
	private static boolean operator_mul_0_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "operator_mul_0_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, OPERATOR_MUL);
		r = r && operator_mul_0_0_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// !OPERATOR_MUL
	private static boolean operator_mul_0_0_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "operator_mul_0_0_1")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NOT_, null);
		r = !consumeToken(b, OPERATOR_MUL);
		exit_section_(b, l, m, null, r, false, null);
		return r;
	}

	// !OPERATOR_ASSIGN
	private static boolean operator_mul_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "operator_mul_1")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NOT_, null);
		r = !consumeToken(b, OPERATOR_ASSIGN);
		exit_section_(b, l, m, null, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// (OPERATOR_OR|OPERATOR_OR_DEFINED) !OPERATOR_ASSIGN
	static boolean operator_or_or_defined(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "operator_or_or_defined")) return false;
		if (!nextTokenIs(b, "", OPERATOR_OR, OPERATOR_OR_DEFINED)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = operator_or_or_defined_0(b, l + 1);
		r = r && operator_or_or_defined_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// OPERATOR_OR|OPERATOR_OR_DEFINED
	private static boolean operator_or_or_defined_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "operator_or_or_defined_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, OPERATOR_OR);
		if (!r) r = consumeToken(b, OPERATOR_OR_DEFINED);
		exit_section_(b, m, null, r);
		return r;
	}

	// !OPERATOR_ASSIGN
	private static boolean operator_or_or_defined_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "operator_or_or_defined_1")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NOT_, null);
		r = !consumeToken(b, OPERATOR_ASSIGN);
		exit_section_(b, l, m, null, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// (
	// 	OPERATOR_BITWISE_NOT
	// 	| OPERATOR_NOT
	// 	| <<checkAndConvertToken OPERATOR_PLUS_UNARY OPERATOR_PLUS>>
	// 	| <<checkAndConvertToken OPERATOR_MINUS_UNARY OPERATOR_MINUS>>
	// 	) !OPERATOR_ASSIGN
	static boolean operator_prefix_unary(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "operator_prefix_unary")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = operator_prefix_unary_0(b, l + 1);
		r = r && operator_prefix_unary_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// OPERATOR_BITWISE_NOT
	// 	| OPERATOR_NOT
	// 	| <<checkAndConvertToken OPERATOR_PLUS_UNARY OPERATOR_PLUS>>
	// 	| <<checkAndConvertToken OPERATOR_MINUS_UNARY OPERATOR_MINUS>>
	private static boolean operator_prefix_unary_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "operator_prefix_unary_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, OPERATOR_BITWISE_NOT);
		if (!r) r = consumeToken(b, OPERATOR_NOT);
		if (!r) r = checkAndConvertToken(b, l + 1, OPERATOR_PLUS_UNARY, OPERATOR_PLUS);
		if (!r) r = checkAndConvertToken(b, l + 1, OPERATOR_MINUS_UNARY, OPERATOR_MINUS);
		exit_section_(b, m, null, r);
		return r;
	}

	// !OPERATOR_ASSIGN
	private static boolean operator_prefix_unary_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "operator_prefix_unary_1")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NOT_, null);
		r = !consumeToken(b, OPERATOR_ASSIGN);
		exit_section_(b, l, m, null, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// (OPERATOR_SHIFT_LEFT|OPERATOR_SHIFT_RIGHT) !OPERATOR_ASSIGN
	static boolean operator_shift(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "operator_shift")) return false;
		if (!nextTokenIs(b, "", OPERATOR_SHIFT_LEFT, OPERATOR_SHIFT_RIGHT)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = operator_shift_0(b, l + 1);
		r = r && operator_shift_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// OPERATOR_SHIFT_LEFT|OPERATOR_SHIFT_RIGHT
	private static boolean operator_shift_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "operator_shift_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, OPERATOR_SHIFT_LEFT);
		if (!r) r = consumeToken(b, OPERATOR_SHIFT_RIGHT);
		exit_section_(b, m, null, r);
		return r;
	}

	// !OPERATOR_ASSIGN
	private static boolean operator_shift_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "operator_shift_1")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NOT_, null);
		r = !consumeToken(b, OPERATOR_ASSIGN);
		exit_section_(b, l, m, null, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// LEFT_PAREN parenthesised_expr_content RIGHT_PAREN
	public static boolean parenthesised_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "parenthesised_expr")) return false;
		if (!nextTokenIs(b, LEFT_PAREN)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = consumeToken(b, LEFT_PAREN);
		p = r; // pin = 1
		r = r && report_error_(b, parenthesised_expr_content(b, l + 1));
		r = p && consumeToken(b, RIGHT_PAREN) && r;
		exit_section_(b, l, m, PARENTHESISED_EXPR, r, p, null);
		return r || p;
	}

	/* ********************************************************** */
	// [expr]
	static boolean parenthesised_expr_content(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "parenthesised_expr_content")) return false;
		Marker m = enter_section_(b, l, _NONE_, null);
		expr(b, l + 1, -1);
		exit_section_(b, l, m, null, true, false, recover_parenthesised_parser_);
		return true;
	}

	/* ********************************************************** */
	// block | scalar_variable !(LEFT_BRACE|LEFT_BRACKET|<<isOperatorToken>>)
	static boolean perl_handle(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "perl_handle")) return false;
		if (!nextTokenIs(b, "", LEFT_BRACE, SIGIL_SCALAR)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = block(b, l + 1);
		if (!r) r = perl_handle_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// scalar_variable !(LEFT_BRACE|LEFT_BRACKET|<<isOperatorToken>>)
	private static boolean perl_handle_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "perl_handle_1")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = scalar_variable(b, l + 1);
		r = r && perl_handle_1_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// !(LEFT_BRACE|LEFT_BRACKET|<<isOperatorToken>>)
	private static boolean perl_handle_1_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "perl_handle_1_1")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NOT_, null);
		r = !perl_handle_1_1_0(b, l + 1);
		exit_section_(b, l, m, null, r, false, null);
		return r;
	}

	// LEFT_BRACE|LEFT_BRACKET|<<isOperatorToken>>
	private static boolean perl_handle_1_1_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "perl_handle_1_1_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, LEFT_BRACE);
		if (!r) r = consumeToken(b, LEFT_BRACKET);
		if (!r) r = isOperatorToken(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// HANDLE
	public static boolean perl_handle_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "perl_handle_expr")) return false;
		if (!nextTokenIs(b, HANDLE)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, HANDLE);
		exit_section_(b, m, PERL_HANDLE_EXPR, r);
		return r;
	}

	/* ********************************************************** */
	// <<parseRegexContent false>>
	public static boolean perl_regex(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "perl_regex")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _COLLAPSE_, "<perl regex>");
		r = parseRegexContent(b, l + 1, false);
		exit_section_(b, l, m, PERL_REGEX, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// <<parseRegexContent true>>
	public static boolean perl_regex_ex(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "perl_regex_ex")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _COLLAPSE_, "<perl regex ex>");
		r = parseRegexContent(b, l + 1, true);
		exit_section_(b, l, m, PERL_REGEX_EX, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// perl_regex_item_ex +
	static boolean perl_regex_ex_items(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "perl_regex_ex_items")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = perl_regex_item_ex(b, l + 1);
		int c = current_position_(b);
		while (r)
		{
			if (!perl_regex_item_ex(b, l + 1)) break;
			if (!empty_element_parsed_guard_(b, "perl_regex_ex_items", c)) break;
			c = current_position_(b);
		}
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// interpolated_constructs
	//   | <<convertRegexToken>>
	static boolean perl_regex_item(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "perl_regex_item")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = interpolated_constructs(b, l + 1);
		if (!r) r = convertRegexToken(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// interpolated_constructs
	//   | <<convertRegexTokenEx>>
	static boolean perl_regex_item_ex(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "perl_regex_item_ex")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = interpolated_constructs(b, l + 1);
		if (!r) r = convertRegexTokenEx(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// perl_regex_item +
	static boolean perl_regex_items(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "perl_regex_items")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = perl_regex_item(b, l + 1);
		int c = current_position_(b);
		while (r)
		{
			if (!perl_regex_item(b, l + 1)) break;
			if (!empty_element_parsed_guard_(b, "perl_regex_items", c)) break;
			c = current_position_(b);
		}
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// REGEX_MODIFIER +
	public static boolean perl_regex_modifiers(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "perl_regex_modifiers")) return false;
		if (!nextTokenIs(b, REGEX_MODIFIER)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, REGEX_MODIFIER);
		int c = current_position_(b);
		while (r)
		{
			if (!consumeToken(b, REGEX_MODIFIER)) break;
			if (!empty_element_parsed_guard_(b, "perl_regex_modifiers", c)) break;
			c = current_position_(b);
		}
		exit_section_(b, m, PERL_REGEX_MODIFIERS, r);
		return r;
	}

	/* ********************************************************** */
	// <<parsePerlVersion>>
	static boolean perl_version(PsiBuilder b, int l)
	{
		return parsePerlVersion(b, l + 1);
	}

	/* ********************************************************** */
	// <<checkAndCollapseToken OPERATOR_POW OPERATOR_MUL OPERATOR_MUL>> !OPERATOR_ASSIGN
	static boolean pow_operator(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "pow_operator")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = checkAndCollapseToken(b, l + 1, OPERATOR_POW, OPERATOR_MUL, OPERATOR_MUL);
		r = r && pow_operator_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// !OPERATOR_ASSIGN
	private static boolean pow_operator_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "pow_operator_1")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NOT_, null);
		r = !consumeToken(b, OPERATOR_ASSIGN);
		exit_section_(b, l, m, null, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// <<parsePrintArguments>>
	static boolean print_arguments(PsiBuilder b, int l)
	{
		return parsePrintArguments(b, l + 1);
	}

	/* ********************************************************** */
	// print_handle expr | expr
	static boolean print_arguments_content(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "print_arguments_content")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = print_arguments_content_0(b, l + 1);
		if (!r) r = expr(b, l + 1, -1);
		exit_section_(b, m, null, r);
		return r;
	}

	// print_handle expr
	private static boolean print_arguments_content_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "print_arguments_content_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = print_handle(b, l + 1);
		r = r && expr(b, l + 1, -1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER] (RESERVED_PRINT|RESERVED_PRINTF|RESERVED_SAY) ( LEFT_PAREN [print_arguments] RIGHT_PAREN | [print_arguments] )
	public static boolean print_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "print_expr")) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _COLLAPSE_, "<expression>");
		r = print_expr_0(b, l + 1);
		r = r && print_expr_1(b, l + 1);
		p = r; // pin = 2
		r = r && print_expr_2(b, l + 1);
		exit_section_(b, l, m, PRINT_EXPR, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean print_expr_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "print_expr_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	// RESERVED_PRINT|RESERVED_PRINTF|RESERVED_SAY
	private static boolean print_expr_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "print_expr_1")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, RESERVED_PRINT);
		if (!r) r = consumeToken(b, RESERVED_PRINTF);
		if (!r) r = consumeToken(b, RESERVED_SAY);
		exit_section_(b, m, null, r);
		return r;
	}

	// LEFT_PAREN [print_arguments] RIGHT_PAREN | [print_arguments]
	private static boolean print_expr_2(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "print_expr_2")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = print_expr_2_0(b, l + 1);
		if (!r) r = print_expr_2_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// LEFT_PAREN [print_arguments] RIGHT_PAREN
	private static boolean print_expr_2_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "print_expr_2_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, LEFT_PAREN);
		r = r && print_expr_2_0_1(b, l + 1);
		r = r && consumeToken(b, RIGHT_PAREN);
		exit_section_(b, m, null, r);
		return r;
	}

	// [print_arguments]
	private static boolean print_expr_2_0_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "print_expr_2_0_1")) return false;
		print_arguments(b, l + 1);
		return true;
	}

	// [print_arguments]
	private static boolean print_expr_2_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "print_expr_2_1")) return false;
		print_arguments(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// <<parsePrintHandle>> | perl_handle
	public static boolean print_handle(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "print_handle")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<print handle>");
		r = parsePrintHandle(b, l + 1);
		if (!r) r = perl_handle(b, l + 1);
		exit_section_(b, l, m, PRINT_HANDLE, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// <<parseReadHandle>>
	public static boolean read_handle(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "read_handle")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<read handle>");
		r = parseReadHandle(b, l + 1);
		exit_section_(b, l, m, READ_HANDLE, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// file_item *
	static boolean real_namespace_content(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "real_namespace_content")) return false;
		Marker m = enter_section_(b, l, _NONE_, null);
		int c = current_position_(b);
		while (true)
		{
			if (!file_item(b, l + 1)) break;
			if (!empty_element_parsed_guard_(b, "real_namespace_content", c)) break;
			c = current_position_(b);
		}
		exit_section_(b, l, m, null, true, false, recover_namespace_content_parser_);
		return true;
	}

	/* ********************************************************** */
	// <<recoverBlock>>
	static boolean recover_block(PsiBuilder b, int l)
	{
		return recoverBlock(b, l + 1);
	}

	/* ********************************************************** */
	// !(LEFT_BRACE|RIGHT_PAREN)
	static boolean recover_condition_statement_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "recover_condition_statement_expr")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NOT_, "<expression>");
		r = !recover_condition_statement_expr_0(b, l + 1);
		exit_section_(b, l, m, null, r, false, null);
		return r;
	}

	// LEFT_BRACE|RIGHT_PAREN
	private static boolean recover_condition_statement_expr_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "recover_condition_statement_expr_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, LEFT_BRACE);
		if (!r) r = consumeToken(b, RIGHT_PAREN);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// <<recoverNamespaceContent>>
	static boolean recover_namespace_content(PsiBuilder b, int l)
	{
		return recoverNamespaceContent(b, l + 1);
	}

	/* ********************************************************** */
	// !(<<checkSemicolon>> | LEFT_BRACE)
	static boolean recover_namespace_definition(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "recover_namespace_definition")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NOT_, null);
		r = !recover_namespace_definition_0(b, l + 1);
		exit_section_(b, l, m, null, r, false, null);
		return r;
	}

	// <<checkSemicolon>> | LEFT_BRACE
	private static boolean recover_namespace_definition_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "recover_namespace_definition_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = checkSemicolon(b, l + 1);
		if (!r) r = consumeToken(b, LEFT_BRACE);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// !(RIGHT_PAREN | LEFT_BRACE | RIGHT_BRACE | <<checkSemicolon>> )
	static boolean recover_parenthesised(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "recover_parenthesised")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NOT_, null);
		r = !recover_parenthesised_0(b, l + 1);
		exit_section_(b, l, m, null, r, false, null);
		return r;
	}

	// RIGHT_PAREN | LEFT_BRACE | RIGHT_BRACE | <<checkSemicolon>>
	private static boolean recover_parenthesised_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "recover_parenthesised_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, RIGHT_PAREN);
		if (!r) r = consumeToken(b, LEFT_BRACE);
		if (!r) r = consumeToken(b, RIGHT_BRACE);
		if (!r) r = checkSemicolon(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// !(REGEX_QUOTE_CLOSE)
	static boolean recover_regex_code(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "recover_regex_code")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NOT_, null);
		r = !consumeToken(b, REGEX_QUOTE_CLOSE);
		exit_section_(b, l, m, null, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// !(RIGHT_PAREN|LEFT_BRACE)
	static boolean recover_signature_content(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "recover_signature_content")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NOT_, null);
		r = !recover_signature_content_0(b, l + 1);
		exit_section_(b, l, m, null, r, false, null);
		return r;
	}

	// RIGHT_PAREN|LEFT_BRACE
	private static boolean recover_signature_content_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "recover_signature_content_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, RIGHT_PAREN);
		if (!r) r = consumeToken(b, LEFT_BRACE);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// <<recoverStatement>>
	static boolean recover_statement(PsiBuilder b, int l)
	{
		return recoverStatement(b, l + 1);
	}

	/* ********************************************************** */
	// code | scalar_expr
	static boolean referencable_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "referencable_expr")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<expression>");
		r = code(b, l + 1);
		if (!r) r = scalar_expr(b, l + 1);
		exit_section_(b, l, m, null, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// anon_array
	//     | anon_hash
	//     | anon_sub
	static boolean reference_value_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "reference_value_expr")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<expression>");
		r = anon_array(b, l + 1);
		if (!r) r = anon_hash(b, l + 1);
		if (!r) r = anon_sub(b, l + 1);
		exit_section_(b, l, m, null, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// file_item+
	static boolean regex_code(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "regex_code")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = file_item(b, l + 1);
		int c = current_position_(b);
		while (r)
		{
			if (!file_item(b, l + 1)) break;
			if (!empty_element_parsed_guard_(b, "regex_code", c)) break;
			c = current_position_(b);
		}
		exit_section_(b, l, m, null, r, false, recover_regex_code_parser_);
		return r;
	}

	/* ********************************************************** */
	// REGEX_QUOTE_OPEN [perl_regex]
	//   | REGEX_QUOTE_OPEN_X [perl_regex_ex]
	static boolean regex_match(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "regex_match")) return false;
		if (!nextTokenIs(b, "", REGEX_QUOTE_OPEN, REGEX_QUOTE_OPEN_X)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = regex_match_0(b, l + 1);
		if (!r) r = regex_match_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// REGEX_QUOTE_OPEN [perl_regex]
	private static boolean regex_match_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "regex_match_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, REGEX_QUOTE_OPEN);
		r = r && regex_match_0_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// [perl_regex]
	private static boolean regex_match_0_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "regex_match_0_1")) return false;
		perl_regex(b, l + 1);
		return true;
	}

	// REGEX_QUOTE_OPEN_X [perl_regex_ex]
	private static boolean regex_match_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "regex_match_1")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, REGEX_QUOTE_OPEN_X);
		r = r && regex_match_1_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// [perl_regex_ex]
	private static boolean regex_match_1_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "regex_match_1_1")) return false;
		perl_regex_ex(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// compile_regex
	//     | replacement_regex
	//     | tr_regex
	//     | match_regex
	static boolean regex_term(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "regex_term")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = compile_regex(b, l + 1);
		if (!r) r = replacement_regex(b, l + 1);
		if (!r) r = tr_regex(b, l + 1);
		if (!r) r = match_regex(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER] RESERVED_S
	//     regex_match
	//     replacement_replace
	//     REGEX_QUOTE_CLOSE
	//     [perl_regex_modifiers]
	public static boolean replacement_regex(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "replacement_regex")) return false;
		if (!nextTokenIs(b, "<replacement regex>", PACKAGE_CORE_IDENTIFIER, RESERVED_S)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<replacement regex>");
		r = replacement_regex_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_S);
		p = r; // pin = 2
		r = r && report_error_(b, regex_match(b, l + 1));
		r = p && report_error_(b, replacement_replace(b, l + 1)) && r;
		r = p && report_error_(b, consumeToken(b, REGEX_QUOTE_CLOSE)) && r;
		r = p && replacement_regex_5(b, l + 1) && r;
		exit_section_(b, l, m, REPLACEMENT_REGEX, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean replacement_regex_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "replacement_regex_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	// [perl_regex_modifiers]
	private static boolean replacement_regex_5(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "replacement_regex_5")) return false;
		perl_regex_modifiers(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// replacement_replace_regex | replacement_replace_code
	static boolean replacement_replace(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "replacement_replace")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = replacement_replace_regex(b, l + 1);
		if (!r) r = replacement_replace_code(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// {REGEX_QUOTE_E | REGEX_QUOTE_CLOSE REGEX_QUOTE_OPEN_E} [regex_code]
	static boolean replacement_replace_code(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "replacement_replace_code")) return false;
		if (!nextTokenIs(b, "", REGEX_QUOTE_CLOSE, REGEX_QUOTE_E)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = replacement_replace_code_0(b, l + 1);
		r = r && replacement_replace_code_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// REGEX_QUOTE_E | REGEX_QUOTE_CLOSE REGEX_QUOTE_OPEN_E
	private static boolean replacement_replace_code_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "replacement_replace_code_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, REGEX_QUOTE_E);
		if (!r) r = parseTokens(b, 0, REGEX_QUOTE_CLOSE, REGEX_QUOTE_OPEN_E);
		exit_section_(b, m, null, r);
		return r;
	}

	// [regex_code]
	private static boolean replacement_replace_code_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "replacement_replace_code_1")) return false;
		regex_code(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// {REGEX_QUOTE | REGEX_QUOTE_CLOSE REGEX_QUOTE_OPEN} [perl_regex]
	static boolean replacement_replace_regex(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "replacement_replace_regex")) return false;
		if (!nextTokenIs(b, "", REGEX_QUOTE, REGEX_QUOTE_CLOSE)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = replacement_replace_regex_0(b, l + 1);
		r = r && replacement_replace_regex_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// REGEX_QUOTE | REGEX_QUOTE_CLOSE REGEX_QUOTE_OPEN
	private static boolean replacement_replace_regex_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "replacement_replace_regex_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, REGEX_QUOTE);
		if (!r) r = parseTokens(b, 0, REGEX_QUOTE_CLOSE, REGEX_QUOTE_OPEN);
		exit_section_(b, m, null, r);
		return r;
	}

	// [perl_regex]
	private static boolean replacement_replace_regex_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "replacement_replace_regex_1")) return false;
		perl_regex(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER] RESERVED_REQUIRE (<<mergeRequirePackageName>> | perl_version | scalar_expr)
	public static boolean require_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "require_expr")) return false;
		if (!nextTokenIs(b, "<expression>", PACKAGE_CORE_IDENTIFIER, RESERVED_REQUIRE)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<expression>");
		r = require_expr_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_REQUIRE);
		p = r; // pin = 2
		r = r && require_expr_2(b, l + 1);
		exit_section_(b, l, m, REQUIRE_EXPR, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean require_expr_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "require_expr_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	// <<mergeRequirePackageName>> | perl_version | scalar_expr
	private static boolean require_expr_2(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "require_expr_2")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = mergeRequirePackageName(b, l + 1);
		if (!r) r = perl_version(b, l + 1);
		if (!r) r = scalar_expr(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER] RESERVED_RETURN [list_expr]
	public static boolean return_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "return_expr")) return false;
		if (!nextTokenIs(b, "<expression>", PACKAGE_CORE_IDENTIFIER, RESERVED_RETURN)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<expression>");
		r = return_expr_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_RETURN);
		p = r; // pin = 2
		r = r && return_expr_2(b, l + 1);
		exit_section_(b, l, m, RETURN_EXPR, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean return_expr_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "return_expr_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	// [list_expr]
	private static boolean return_expr_2(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "return_expr_2")) return false;
		list_expr(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// callable [call_arguments]
	static boolean rightward_call_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "rightward_call_expr")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<expression>");
		r = callable(b, l + 1);
		r = r && rightward_call_expr_1(b, l + 1);
		exit_section_(b, l, m, null, r, false, null);
		return r;
	}

	// [call_arguments]
	private static boolean rightward_call_expr_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "rightward_call_expr_1")) return false;
		call_arguments(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// <<parseFileContent>>
	static boolean root(PsiBuilder b, int l)
	{
		return parseFileContent(b, l + 1);
	}

	/* ********************************************************** */
	// <<parseScalarOrElement>>
	static boolean scalar(PsiBuilder b, int l)
	{
		return parseScalarOrElement(b, l + 1);
	}

	/* ********************************************************** */
	// scalar_primitive array_index
	public static boolean scalar_array_element(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "scalar_array_element")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _COLLAPSE_, "<scalar array element>");
		r = scalar_primitive(b, l + 1);
		r = r && array_index(b, l + 1);
		exit_section_(b, l, m, SCALAR_ARRAY_ELEMENT, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// scalar [method_call_arguments]
	public static boolean scalar_call(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "scalar_call")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<scalar call>");
		r = scalar(b, l + 1);
		r = r && scalar_call_1(b, l + 1);
		exit_section_(b, l, m, SCALAR_CALL, r, false, null);
		return r;
	}

	// [method_call_arguments]
	private static boolean scalar_call_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "scalar_call_1")) return false;
		method_call_arguments(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// SIGIL_SCALAR cast_target
	public static boolean scalar_cast_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "scalar_cast_expr")) return false;
		if (!nextTokenIs(b, SIGIL_SCALAR)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, SIGIL_SCALAR);
		r = r && cast_target(b, l + 1);
		exit_section_(b, m, SCALAR_CAST_EXPR, r);
		return r;
	}

	/* ********************************************************** */
	// number_constant
	//     | tag_scalar
	//     | string
	static boolean scalar_constant(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "scalar_constant")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = number_constant(b, l + 1);
		if (!r) r = tag_scalar(b, l + 1);
		if (!r) r = string(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// <<parseExpressionLevel 4>>
	static boolean scalar_expr(PsiBuilder b, int l)
	{
		return parseExpressionLevel(b, l + 1, 4);
	}

	/* ********************************************************** */
	// scalar_primitive hash_index
	public static boolean scalar_hash_element(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "scalar_hash_element")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _COLLAPSE_, "<scalar hash element>");
		r = scalar_primitive(b, l + 1);
		r = r && hash_index(b, l + 1);
		exit_section_(b, l, m, SCALAR_HASH_ELEMENT, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// SIGIL_SCALAR_INDEX cast_target
	public static boolean scalar_index_cast_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "scalar_index_cast_expr")) return false;
		if (!nextTokenIs(b, SIGIL_SCALAR_INDEX)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, SIGIL_SCALAR_INDEX);
		r = r && cast_target(b, l + 1);
		exit_section_(b, m, SCALAR_INDEX_CAST_EXPR, r);
		return r;
	}

	/* ********************************************************** */
	// scalar_variable
	//     | scalar_cast_expr
	//     | undef_expr  // shouldn't it be in term ? (check declarations)
	//     | scalar_index_cast_expr
	static boolean scalar_primitive(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "scalar_primitive")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = scalar_variable(b, l + 1);
		if (!r) r = scalar_cast_expr(b, l + 1);
		if (!r) r = undef_expr(b, l + 1);
		if (!r) r = scalar_index_cast_expr(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// SIGIL_SCALAR variable_body
	public static boolean scalar_variable(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "scalar_variable")) return false;
		if (!nextTokenIs(b, SIGIL_SCALAR)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, SIGIL_SCALAR);
		r = r && variable_body(b, l + 1);
		exit_section_(b, m, SCALAR_VARIABLE, r);
		return r;
	}

	/* ********************************************************** */
	// LEFT_PAREN sort_arguments_ RIGHT_PAREN !LEFT_BRACKET
	//     | sort_arguments_
	static boolean sort_arguments(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sort_arguments")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = sort_arguments_0(b, l + 1);
		if (!r) r = sort_arguments_(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// LEFT_PAREN sort_arguments_ RIGHT_PAREN !LEFT_BRACKET
	private static boolean sort_arguments_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sort_arguments_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, LEFT_PAREN);
		r = r && sort_arguments_(b, l + 1);
		r = r && consumeToken(b, RIGHT_PAREN);
		r = r && sort_arguments_0_3(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// !LEFT_BRACKET
	private static boolean sort_arguments_0_3(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sort_arguments_0_3")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NOT_, null);
		r = !consumeToken(b, LEFT_BRACKET);
		exit_section_(b, l, m, null, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// scalar_variable grep_map_sort_tail
	//     | block [comma] grep_map_sort_tail
	//     | callable grep_map_sort_tail
	//     | grep_map_sort_tail
	static boolean sort_arguments_(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sort_arguments_")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = sort_arguments__0(b, l + 1);
		if (!r) r = sort_arguments__1(b, l + 1);
		if (!r) r = sort_arguments__2(b, l + 1);
		if (!r) r = grep_map_sort_tail(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// scalar_variable grep_map_sort_tail
	private static boolean sort_arguments__0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sort_arguments__0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = scalar_variable(b, l + 1);
		r = r && grep_map_sort_tail(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// block [comma] grep_map_sort_tail
	private static boolean sort_arguments__1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sort_arguments__1")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = block(b, l + 1);
		r = r && sort_arguments__1_1(b, l + 1);
		r = r && grep_map_sort_tail(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// [comma]
	private static boolean sort_arguments__1_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sort_arguments__1_1")) return false;
		comma(b, l + 1);
		return true;
	}

	// callable grep_map_sort_tail
	private static boolean sort_arguments__2(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sort_arguments__2")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = callable(b, l + 1);
		r = r && grep_map_sort_tail(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER] RESERVED_SORT sort_arguments
	public static boolean sort_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sort_expr")) return false;
		if (!nextTokenIs(b, "<expression>", PACKAGE_CORE_IDENTIFIER, RESERVED_SORT)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<expression>");
		r = sort_expr_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_SORT);
		p = r; // pin = 2
		r = r && sort_arguments(b, l + 1);
		exit_section_(b, l, m, SORT_EXPR, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean sort_expr_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sort_expr_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// statement_body <<statementSemi>>
	public static boolean statement(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "statement")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _COLLAPSE_, "<statement>");
		r = statement_body(b, l + 1);
		r = r && statementSemi(b, l + 1);
		exit_section_(b, l, m, STATEMENT, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// use_statements
	//     | no_statements
	//     | sub_declaration
	//     | normal_statement
	static boolean statement_body(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "statement_body")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = use_statements(b, l + 1);
		if (!r) r = no_statements(b, l + 1);
		if (!r) r = sub_declaration(b, l + 1);
		if (!r) r = normal_statement(b, l + 1);
		exit_section_(b, l, m, null, r, false, recover_statement_parser_);
		return r;
	}

	/* ********************************************************** */
	// if_statement_modifier
	//     | unless_statement_modifier
	//     | while_statement_modifier
	//     | until_statement_modifier
	//     | for_statement_modifier
	//     | foreach_statement_modifier
	//     | when_statement_modifier
	public static boolean statement_modifier(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "statement_modifier")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _COLLAPSE_, "<statement modifier>");
		r = if_statement_modifier(b, l + 1);
		if (!r) r = unless_statement_modifier(b, l + 1);
		if (!r) r = while_statement_modifier(b, l + 1);
		if (!r) r = until_statement_modifier(b, l + 1);
		if (!r) r = for_statement_modifier(b, l + 1);
		if (!r) r = foreach_statement_modifier(b, l + 1);
		if (!r) r = when_statement_modifier(b, l + 1);
		exit_section_(b, l, m, STATEMENT_MODIFIER, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// strict_variable_declaration_wrapper | [PACKAGE_CORE_IDENTIFIER] RESERVED_UNDEF
	static boolean strict_variable_declaration_argument(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "strict_variable_declaration_argument")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = strict_variable_declaration_wrapper(b, l + 1);
		if (!r) r = strict_variable_declaration_argument_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// [PACKAGE_CORE_IDENTIFIER] RESERVED_UNDEF
	private static boolean strict_variable_declaration_argument_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "strict_variable_declaration_argument_1")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = strict_variable_declaration_argument_1_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_UNDEF);
		exit_section_(b, m, null, r);
		return r;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean strict_variable_declaration_argument_1_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "strict_variable_declaration_argument_1_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// variable_declaration_wrapper !(LEFT_BRACE | LEFT_BRACKET | OPERATOR_DEREFERENCE )
	static boolean strict_variable_declaration_wrapper(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "strict_variable_declaration_wrapper")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = variable_declaration_wrapper(b, l + 1);
		r = r && strict_variable_declaration_wrapper_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// !(LEFT_BRACE | LEFT_BRACKET | OPERATOR_DEREFERENCE )
	private static boolean strict_variable_declaration_wrapper_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "strict_variable_declaration_wrapper_1")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NOT_, null);
		r = !strict_variable_declaration_wrapper_1_0(b, l + 1);
		exit_section_(b, l, m, null, r, false, null);
		return r;
	}

	// LEFT_BRACE | LEFT_BRACKET | OPERATOR_DEREFERENCE
	private static boolean strict_variable_declaration_wrapper_1_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "strict_variable_declaration_wrapper_1_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, LEFT_BRACE);
		if (!r) r = consumeToken(b, LEFT_BRACKET);
		if (!r) r = consumeToken(b, OPERATOR_DEREFERENCE);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// string_bare | string_sq | string_dq | string_xq | heredoc_opener
	static boolean string(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "string")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = string_bare(b, l + 1);
		if (!r) r = string_sq(b, l + 1);
		if (!r) r = string_dq(b, l + 1);
		if (!r) r = string_xq(b, l + 1);
		if (!r) r = heredoc_opener(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// <<parseAndWrapStringContent>>
	public static boolean string_bare(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "string_bare")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _COLLAPSE_, "<string bare>");
		r = parseAndWrapStringContent(b, l + 1);
		exit_section_(b, l, m, STRING_BARE, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// <<parseInterpolatedConstructs>>
	//   | <<convertToStringContent>>
	static boolean string_content_element(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "string_content_element")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = parseInterpolatedConstructs(b, l + 1);
		if (!r) r = convertToStringContent(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// string_content_element +
	static boolean string_content_qq(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "string_content_qq")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = string_content_element(b, l + 1);
		int c = current_position_(b);
		while (r)
		{
			if (!string_content_element(b, l + 1)) break;
			if (!empty_element_parsed_guard_(b, "string_content_qq", c)) break;
			c = current_position_(b);
		}
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// [[PACKAGE_CORE_IDENTIFIER] RESERVED_QQ] string_dq_parsed
	//   | <<parseNetstedInterpolatedString QUOTE_DOUBLE>>
	public static boolean string_dq(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "string_dq")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _COLLAPSE_, "<string dq>");
		r = string_dq_0(b, l + 1);
		if (!r) r = parseNetstedInterpolatedString(b, l + 1, QUOTE_DOUBLE);
		exit_section_(b, l, m, STRING_DQ, r, false, null);
		return r;
	}

	// [[PACKAGE_CORE_IDENTIFIER] RESERVED_QQ] string_dq_parsed
	private static boolean string_dq_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "string_dq_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = string_dq_0_0(b, l + 1);
		r = r && string_dq_parsed(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// [[PACKAGE_CORE_IDENTIFIER] RESERVED_QQ]
	private static boolean string_dq_0_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "string_dq_0_0")) return false;
		string_dq_0_0_0(b, l + 1);
		return true;
	}

	// [PACKAGE_CORE_IDENTIFIER] RESERVED_QQ
	private static boolean string_dq_0_0_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "string_dq_0_0_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = string_dq_0_0_0_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_QQ);
		exit_section_(b, m, null, r);
		return r;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean string_dq_0_0_0_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "string_dq_0_0_0_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// QUOTE_DOUBLE_OPEN [<<parseInterpolatedStringContent>>] QUOTE_DOUBLE_CLOSE
	static boolean string_dq_parsed(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "string_dq_parsed")) return false;
		if (!nextTokenIs(b, QUOTE_DOUBLE_OPEN)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = consumeToken(b, QUOTE_DOUBLE_OPEN);
		p = r; // pin = 1
		r = r && report_error_(b, string_dq_parsed_1(b, l + 1));
		r = p && consumeToken(b, QUOTE_DOUBLE_CLOSE) && r;
		exit_section_(b, l, m, null, r, p, null);
		return r || p;
	}

	// [<<parseInterpolatedStringContent>>]
	private static boolean string_dq_parsed_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "string_dq_parsed_1")) return false;
		parseInterpolatedStringContent(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// [[PACKAGE_CORE_IDENTIFIER] RESERVED_QW] <<parseSQString>>
	public static boolean string_list(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "string_list")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _COLLAPSE_, "<string list>");
		r = string_list_0(b, l + 1);
		r = r && parseSQString(b, l + 1);
		exit_section_(b, l, m, STRING_LIST, r, false, null);
		return r;
	}

	// [[PACKAGE_CORE_IDENTIFIER] RESERVED_QW]
	private static boolean string_list_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "string_list_0")) return false;
		string_list_0_0(b, l + 1);
		return true;
	}

	// [PACKAGE_CORE_IDENTIFIER] RESERVED_QW
	private static boolean string_list_0_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "string_list_0_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = string_list_0_0_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_QW);
		exit_section_(b, m, null, r);
		return r;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean string_list_0_0_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "string_list_0_0_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// [[PACKAGE_CORE_IDENTIFIER] RESERVED_Q] <<parseSQString>>
	//   | <<parseNestedSQString>>
	public static boolean string_sq(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "string_sq")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _COLLAPSE_, "<string sq>");
		r = string_sq_0(b, l + 1);
		if (!r) r = parseNestedSQString(b, l + 1);
		exit_section_(b, l, m, STRING_SQ, r, false, null);
		return r;
	}

	// [[PACKAGE_CORE_IDENTIFIER] RESERVED_Q] <<parseSQString>>
	private static boolean string_sq_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "string_sq_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = string_sq_0_0(b, l + 1);
		r = r && parseSQString(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// [[PACKAGE_CORE_IDENTIFIER] RESERVED_Q]
	private static boolean string_sq_0_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "string_sq_0_0")) return false;
		string_sq_0_0_0(b, l + 1);
		return true;
	}

	// [PACKAGE_CORE_IDENTIFIER] RESERVED_Q
	private static boolean string_sq_0_0_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "string_sq_0_0_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = string_sq_0_0_0_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_Q);
		exit_section_(b, m, null, r);
		return r;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean string_sq_0_0_0_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "string_sq_0_0_0_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// QUOTE_SINGLE_OPEN <<parseAndWrapStringContent>>* QUOTE_SINGLE_CLOSE
	static boolean string_sq_parsed(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "string_sq_parsed")) return false;
		if (!nextTokenIs(b, QUOTE_SINGLE_OPEN)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = consumeToken(b, QUOTE_SINGLE_OPEN);
		p = r; // pin = 1
		r = r && report_error_(b, string_sq_parsed_1(b, l + 1));
		r = p && consumeToken(b, QUOTE_SINGLE_CLOSE) && r;
		exit_section_(b, l, m, null, r, p, null);
		return r || p;
	}

	// <<parseAndWrapStringContent>>*
	private static boolean string_sq_parsed_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "string_sq_parsed_1")) return false;
		int c = current_position_(b);
		while (true)
		{
			if (!parseAndWrapStringContent(b, l + 1)) break;
			if (!empty_element_parsed_guard_(b, "string_sq_parsed_1", c)) break;
			c = current_position_(b);
		}
		return true;
	}

	/* ********************************************************** */
	// [[PACKAGE_CORE_IDENTIFIER] RESERVED_QX] string_xq_parsed
	//   | <<parseNetstedInterpolatedString QUOTE_TICK>>
	public static boolean string_xq(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "string_xq")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _COLLAPSE_, "<string xq>");
		r = string_xq_0(b, l + 1);
		if (!r) r = parseNetstedInterpolatedString(b, l + 1, QUOTE_TICK);
		exit_section_(b, l, m, STRING_XQ, r, false, null);
		return r;
	}

	// [[PACKAGE_CORE_IDENTIFIER] RESERVED_QX] string_xq_parsed
	private static boolean string_xq_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "string_xq_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = string_xq_0_0(b, l + 1);
		r = r && string_xq_parsed(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// [[PACKAGE_CORE_IDENTIFIER] RESERVED_QX]
	private static boolean string_xq_0_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "string_xq_0_0")) return false;
		string_xq_0_0_0(b, l + 1);
		return true;
	}

	// [PACKAGE_CORE_IDENTIFIER] RESERVED_QX
	private static boolean string_xq_0_0_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "string_xq_0_0_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = string_xq_0_0_0_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_QX);
		exit_section_(b, m, null, r);
		return r;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean string_xq_0_0_0_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "string_xq_0_0_0_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// QUOTE_TICK_OPEN [<<parseInterpolatedStringContent>>] QUOTE_TICK_CLOSE
	static boolean string_xq_parsed(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "string_xq_parsed")) return false;
		if (!nextTokenIs(b, QUOTE_TICK_OPEN)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = consumeToken(b, QUOTE_TICK_OPEN);
		p = r; // pin = 1
		r = r && report_error_(b, string_xq_parsed_1(b, l + 1));
		r = p && consumeToken(b, QUOTE_TICK_CLOSE) && r;
		exit_section_(b, l, m, null, r, p, null);
		return r || p;
	}

	// [<<parseInterpolatedStringContent>>]
	private static boolean string_xq_parsed_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "string_xq_parsed_1")) return false;
		parseInterpolatedStringContent(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// COLON attribute ([COLON] attribute)*
	static boolean sub_attributes(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_attributes")) return false;
		if (!nextTokenIs(b, COLON)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = consumeToken(b, COLON);
		p = r; // pin = 1
		r = r && report_error_(b, attribute(b, l + 1));
		r = p && sub_attributes_2(b, l + 1) && r;
		exit_section_(b, l, m, null, r, p, null);
		return r || p;
	}

	// ([COLON] attribute)*
	private static boolean sub_attributes_2(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_attributes_2")) return false;
		int c = current_position_(b);
		while (true)
		{
			if (!sub_attributes_2_0(b, l + 1)) break;
			if (!empty_element_parsed_guard_(b, "sub_attributes_2", c)) break;
			c = current_position_(b);
		}
		return true;
	}

	// [COLON] attribute
	private static boolean sub_attributes_2_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_attributes_2_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = sub_attributes_2_0_0(b, l + 1);
		r = r && attribute(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// [COLON]
	private static boolean sub_attributes_2_0_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_attributes_2_0_0")) return false;
		consumeToken(b, COLON);
		return true;
	}

	/* ********************************************************** */
	// leftward_call_expr | rightward_call_expr
	public static boolean sub_call_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_call_expr")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _COLLAPSE_, "<expression>");
		r = leftward_call_expr(b, l + 1);
		if (!r) r = rightward_call_expr(b, l + 1);
		exit_section_(b, l, m, SUB_CALL_EXPR, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// [annotations] [PACKAGE_CORE_IDENTIFIER] RESERVED_SUB canonical_sub_name sub_declaration_parameters
	public static boolean sub_declaration(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_declaration")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<sub declaration>");
		r = sub_declaration_0(b, l + 1);
		r = r && sub_declaration_1(b, l + 1);
		r = r && consumeToken(b, RESERVED_SUB);
		r = r && canonical_sub_name(b, l + 1);
		r = r && sub_declaration_parameters(b, l + 1);
		exit_section_(b, l, m, SUB_DECLARATION, r, false, null);
		return r;
	}

	// [annotations]
	private static boolean sub_declaration_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_declaration_0")) return false;
		annotations(b, l + 1);
		return true;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean sub_declaration_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_declaration_1")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// [sub_prototype] [sub_attributes]
	static boolean sub_declaration_parameters(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_declaration_parameters")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = sub_declaration_parameters_0(b, l + 1);
		r = r && sub_declaration_parameters_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// [sub_prototype]
	private static boolean sub_declaration_parameters_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_declaration_parameters_0")) return false;
		sub_prototype(b, l + 1);
		return true;
	}

	// [sub_attributes]
	private static boolean sub_declaration_parameters_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_declaration_parameters_1")) return false;
		sub_attributes(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// [annotations] [PACKAGE_CORE_IDENTIFIER] RESERVED_SUB canonical_sub_name sub_definition_parameters block
	public static boolean sub_definition(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_definition")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<sub definition>");
		r = sub_definition_0(b, l + 1);
		r = r && sub_definition_1(b, l + 1);
		r = r && consumeToken(b, RESERVED_SUB);
		r = r && canonical_sub_name(b, l + 1);
		r = r && sub_definition_parameters(b, l + 1);
		r = r && block(b, l + 1);
		exit_section_(b, l, m, SUB_DEFINITION, r, false, null);
		return r;
	}

	// [annotations]
	private static boolean sub_definition_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_definition_0")) return false;
		annotations(b, l + 1);
		return true;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean sub_definition_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_definition_1")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// <<parseSubDefinitionName>>
	static boolean sub_definition_name(PsiBuilder b, int l)
	{
		return parseSubDefinitionName(b, l + 1);
	}

	/* ********************************************************** */
	// [sub_prototype_or_signature] [sub_attributes]
	static boolean sub_definition_parameters(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_definition_parameters")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = sub_definition_parameters_0(b, l + 1);
		r = r && sub_definition_parameters_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// [sub_prototype_or_signature]
	private static boolean sub_definition_parameters_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_definition_parameters_0")) return false;
		sub_prototype_or_signature(b, l + 1);
		return true;
	}

	// [sub_attributes]
	private static boolean sub_definition_parameters_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_definition_parameters_1")) return false;
		sub_attributes(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER] RESERVED_SUB sub_definition_parameters block
	public static boolean sub_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_expr")) return false;
		if (!nextTokenIs(b, "<expression>", PACKAGE_CORE_IDENTIFIER, RESERVED_SUB)) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<expression>");
		r = sub_expr_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_SUB);
		r = r && sub_definition_parameters(b, l + 1);
		r = r && block(b, l + 1);
		exit_section_(b, l, m, SUB_EXPR, r, false, null);
		return r;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean sub_expr_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_expr_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// LEFT_PAREN [sub_prototype_content] RIGHT_PAREN
	static boolean sub_prototype(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_prototype")) return false;
		if (!nextTokenIs(b, LEFT_PAREN)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = consumeToken(b, LEFT_PAREN);
		p = r; // pin = 1
		r = r && report_error_(b, sub_prototype_1(b, l + 1));
		r = p && consumeToken(b, RIGHT_PAREN) && r;
		exit_section_(b, l, m, null, r, p, null);
		return r || p;
	}

	// [sub_prototype_content]
	private static boolean sub_prototype_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_prototype_1")) return false;
		sub_prototype_content(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// <<parseSubPrototype>>
	static boolean sub_prototype_content(PsiBuilder b, int l)
	{
		return parseSubPrototype(b, l + 1);
	}

	/* ********************************************************** */
	// sub_signature |sub_prototype
	static boolean sub_prototype_or_signature(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_prototype_or_signature")) return false;
		if (!nextTokenIs(b, LEFT_PAREN)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = sub_signature(b, l + 1);
		if (!r) r = sub_prototype(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// LEFT_PAREN sub_signature_content RIGHT_PAREN
	static boolean sub_signature(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_signature")) return false;
		if (!nextTokenIs(b, LEFT_PAREN)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, LEFT_PAREN);
		r = r && sub_signature_content(b, l + 1);
		r = r && consumeToken(b, RIGHT_PAREN);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// <<cancelProtoLikeSignature>>
	// 	{
	// 		sub_signature_element (OPERATOR_COMMA sub_signature_element)* [OPERATOR_COMMA sub_signature_element_slurpy]
	// 		| [sub_signature_element_slurpy]
	// 	}
	public static boolean sub_signature_content(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_signature_content")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _COLLAPSE_, "<sub signature content>");
		r = cancelProtoLikeSignature(b, l + 1);
		r = r && sub_signature_content_1(b, l + 1);
		exit_section_(b, l, m, SUB_SIGNATURE_CONTENT, r, false, null);
		return r;
	}

	// sub_signature_element (OPERATOR_COMMA sub_signature_element)* [OPERATOR_COMMA sub_signature_element_slurpy]
	// 		| [sub_signature_element_slurpy]
	private static boolean sub_signature_content_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_signature_content_1")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = sub_signature_content_1_0(b, l + 1);
		if (!r) r = sub_signature_content_1_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// sub_signature_element (OPERATOR_COMMA sub_signature_element)* [OPERATOR_COMMA sub_signature_element_slurpy]
	private static boolean sub_signature_content_1_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_signature_content_1_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = sub_signature_element(b, l + 1);
		r = r && sub_signature_content_1_0_1(b, l + 1);
		r = r && sub_signature_content_1_0_2(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// (OPERATOR_COMMA sub_signature_element)*
	private static boolean sub_signature_content_1_0_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_signature_content_1_0_1")) return false;
		int c = current_position_(b);
		while (true)
		{
			if (!sub_signature_content_1_0_1_0(b, l + 1)) break;
			if (!empty_element_parsed_guard_(b, "sub_signature_content_1_0_1", c)) break;
			c = current_position_(b);
		}
		return true;
	}

	// OPERATOR_COMMA sub_signature_element
	private static boolean sub_signature_content_1_0_1_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_signature_content_1_0_1_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, OPERATOR_COMMA);
		r = r && sub_signature_element(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// [OPERATOR_COMMA sub_signature_element_slurpy]
	private static boolean sub_signature_content_1_0_2(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_signature_content_1_0_2")) return false;
		sub_signature_content_1_0_2_0(b, l + 1);
		return true;
	}

	// OPERATOR_COMMA sub_signature_element_slurpy
	private static boolean sub_signature_content_1_0_2_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_signature_content_1_0_2_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, OPERATOR_COMMA);
		r = r && sub_signature_element_slurpy(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// [sub_signature_element_slurpy]
	private static boolean sub_signature_content_1_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_signature_content_1_1")) return false;
		sub_signature_element_slurpy(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// sub_signature_element_ignore	| sub_signature_scalar
	static boolean sub_signature_element(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_signature_element")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = sub_signature_element_ignore(b, l + 1);
		if (!r) r = sub_signature_scalar(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// SIGIL_SCALAR [<<checkAssignIdentifier>> [scalar_expr]] &(OPERATOR_COMMA|RIGHT_PAREN)
	public static boolean sub_signature_element_ignore(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_signature_element_ignore")) return false;
		if (!nextTokenIs(b, SIGIL_SCALAR)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, SIGIL_SCALAR);
		r = r && sub_signature_element_ignore_1(b, l + 1);
		r = r && sub_signature_element_ignore_2(b, l + 1);
		exit_section_(b, m, SUB_SIGNATURE_ELEMENT_IGNORE, r);
		return r;
	}

	// [<<checkAssignIdentifier>> [scalar_expr]]
	private static boolean sub_signature_element_ignore_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_signature_element_ignore_1")) return false;
		sub_signature_element_ignore_1_0(b, l + 1);
		return true;
	}

	// <<checkAssignIdentifier>> [scalar_expr]
	private static boolean sub_signature_element_ignore_1_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_signature_element_ignore_1_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = checkAssignIdentifier(b, l + 1);
		r = r && sub_signature_element_ignore_1_0_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// [scalar_expr]
	private static boolean sub_signature_element_ignore_1_0_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_signature_element_ignore_1_0_1")) return false;
		scalar_expr(b, l + 1);
		return true;
	}

	// &(OPERATOR_COMMA|RIGHT_PAREN)
	private static boolean sub_signature_element_ignore_2(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_signature_element_ignore_2")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _AND_, null);
		r = sub_signature_element_ignore_2_0(b, l + 1);
		exit_section_(b, l, m, null, r, false, null);
		return r;
	}

	// OPERATOR_COMMA|RIGHT_PAREN
	private static boolean sub_signature_element_ignore_2_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_signature_element_ignore_2_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, OPERATOR_COMMA);
		if (!r) r = consumeToken(b, RIGHT_PAREN);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// <<arrayDeclarationWrapper>> | <<hashDeclarationWrapper>>
	static boolean sub_signature_element_slurpy(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_signature_element_slurpy")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = arrayDeclarationWrapper(b, l + 1);
		if (!r) r = hashDeclarationWrapper(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// <<scalarDeclarationWrapper>> [OPERATOR_ASSIGN scalar_expr]
	static boolean sub_signature_scalar(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_signature_scalar")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = scalarDeclarationWrapper(b, l + 1);
		r = r && sub_signature_scalar_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// [OPERATOR_ASSIGN scalar_expr]
	private static boolean sub_signature_scalar_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_signature_scalar_1")) return false;
		sub_signature_scalar_1_0(b, l + 1);
		return true;
	}

	// OPERATOR_ASSIGN scalar_expr
	private static boolean sub_signature_scalar_1_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "sub_signature_scalar_1_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, OPERATOR_ASSIGN);
		r = r && scalar_expr(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// TAG
	public static boolean tag_scalar(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "tag_scalar")) return false;
		if (!nextTokenIs(b, TAG)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, TAG);
		exit_section_(b, m, TAG_SCALAR, r);
		return r;
	}

	/* ********************************************************** */
	// REGEX_MODIFIER +
	public static boolean tr_modifiers(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "tr_modifiers")) return false;
		if (!nextTokenIs(b, REGEX_MODIFIER)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, REGEX_MODIFIER);
		int c = current_position_(b);
		while (r)
		{
			if (!consumeToken(b, REGEX_MODIFIER)) break;
			if (!empty_element_parsed_guard_(b, "tr_modifiers", c)) break;
			c = current_position_(b);
		}
		exit_section_(b, m, TR_MODIFIERS, r);
		return r;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER] (RESERVED_TR|RESERVED_Y) tr_search tr_replacement [tr_modifiers]
	public static boolean tr_regex(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "tr_regex")) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<tr regex>");
		r = tr_regex_0(b, l + 1);
		r = r && tr_regex_1(b, l + 1);
		p = r; // pin = 2
		r = r && report_error_(b, tr_search(b, l + 1));
		r = p && report_error_(b, tr_replacement(b, l + 1)) && r;
		r = p && tr_regex_4(b, l + 1) && r;
		exit_section_(b, l, m, TR_REGEX, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean tr_regex_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "tr_regex_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	// RESERVED_TR|RESERVED_Y
	private static boolean tr_regex_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "tr_regex_1")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, RESERVED_TR);
		if (!r) r = consumeToken(b, RESERVED_Y);
		exit_section_(b, m, null, r);
		return r;
	}

	// [tr_modifiers]
	private static boolean tr_regex_4(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "tr_regex_4")) return false;
		tr_modifiers(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// {REGEX_QUOTE | REGEX_QUOTE_CLOSE REGEX_QUOTE_OPEN} [tr_replacementlist] REGEX_QUOTE_CLOSE
	static boolean tr_replacement(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "tr_replacement")) return false;
		if (!nextTokenIs(b, "", REGEX_QUOTE, REGEX_QUOTE_CLOSE)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = tr_replacement_0(b, l + 1);
		p = r; // pin = 1
		r = r && report_error_(b, tr_replacement_1(b, l + 1));
		r = p && consumeToken(b, REGEX_QUOTE_CLOSE) && r;
		exit_section_(b, l, m, null, r, p, null);
		return r || p;
	}

	// REGEX_QUOTE | REGEX_QUOTE_CLOSE REGEX_QUOTE_OPEN
	private static boolean tr_replacement_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "tr_replacement_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, REGEX_QUOTE);
		if (!r) r = parseTokens(b, 0, REGEX_QUOTE_CLOSE, REGEX_QUOTE_OPEN);
		exit_section_(b, m, null, r);
		return r;
	}

	// [tr_replacementlist]
	private static boolean tr_replacement_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "tr_replacement_1")) return false;
		tr_replacementlist(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// STRING_CONTENT
	public static boolean tr_replacementlist(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "tr_replacementlist")) return false;
		if (!nextTokenIs(b, STRING_CONTENT)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, STRING_CONTENT);
		exit_section_(b, m, TR_REPLACEMENTLIST, r);
		return r;
	}

	/* ********************************************************** */
	// REGEX_QUOTE_OPEN [tr_searchlist]
	static boolean tr_search(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "tr_search")) return false;
		if (!nextTokenIs(b, REGEX_QUOTE_OPEN)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = consumeToken(b, REGEX_QUOTE_OPEN);
		p = r; // pin = 1
		r = r && tr_search_1(b, l + 1);
		exit_section_(b, l, m, null, r, p, null);
		return r || p;
	}

	// [tr_searchlist]
	private static boolean tr_search_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "tr_search_1")) return false;
		tr_searchlist(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// STRING_CONTENT
	public static boolean tr_searchlist(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "tr_searchlist")) return false;
		if (!nextTokenIs(b, STRING_CONTENT)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, STRING_CONTENT);
		exit_section_(b, m, TR_SEARCHLIST, r);
		return r;
	}

	/* ********************************************************** */
	// <<parseExpressionLevel 14>>
	static boolean unary_expr(PsiBuilder b, int l)
	{
		return parseExpressionLevel(b, l + 1, 14);
	}

	/* ********************************************************** */
	// block
	public static boolean unconditional_block(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "unconditional_block")) return false;
		if (!nextTokenIs(b, LEFT_BRACE)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = block(b, l + 1);
		exit_section_(b, m, UNCONDITIONAL_BLOCK, r);
		return r;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER] RESERVED_UNDEF (undef_params | LEFT_PAREN undef_params RIGHT_PAREN) ?
	public static boolean undef_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "undef_expr")) return false;
		if (!nextTokenIs(b, "<expression>", PACKAGE_CORE_IDENTIFIER, RESERVED_UNDEF)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<expression>");
		r = undef_expr_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_UNDEF);
		p = r; // pin = 2
		r = r && undef_expr_2(b, l + 1);
		exit_section_(b, l, m, UNDEF_EXPR, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean undef_expr_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "undef_expr_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	// (undef_params | LEFT_PAREN undef_params RIGHT_PAREN) ?
	private static boolean undef_expr_2(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "undef_expr_2")) return false;
		undef_expr_2_0(b, l + 1);
		return true;
	}

	// undef_params | LEFT_PAREN undef_params RIGHT_PAREN
	private static boolean undef_expr_2_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "undef_expr_2_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = undef_params(b, l + 1);
		if (!r) r = undef_expr_2_0_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// LEFT_PAREN undef_params RIGHT_PAREN
	private static boolean undef_expr_2_0_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "undef_expr_2_0_1")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, LEFT_PAREN);
		r = r && undef_params(b, l + 1);
		r = r && consumeToken(b, RIGHT_PAREN);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// deref_expr
	//     | variable
	static boolean undef_params(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "undef_params")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = expr(b, l + 1, 21);
		if (!r) r = variable(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER] RESERVED_UNLESS conditional_block if_compound_elsif * [if_compound_else]
	public static boolean unless_compound(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "unless_compound")) return false;
		if (!nextTokenIs(b, "<unless compound>", PACKAGE_CORE_IDENTIFIER, RESERVED_UNLESS)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<unless compound>");
		r = unless_compound_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_UNLESS);
		p = r; // pin = 2
		r = r && report_error_(b, conditional_block(b, l + 1));
		r = p && report_error_(b, unless_compound_3(b, l + 1)) && r;
		r = p && unless_compound_4(b, l + 1) && r;
		exit_section_(b, l, m, UNLESS_COMPOUND, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean unless_compound_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "unless_compound_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	// if_compound_elsif *
	private static boolean unless_compound_3(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "unless_compound_3")) return false;
		int c = current_position_(b);
		while (true)
		{
			if (!if_compound_elsif(b, l + 1)) break;
			if (!empty_element_parsed_guard_(b, "unless_compound_3", c)) break;
			c = current_position_(b);
		}
		return true;
	}

	// [if_compound_else]
	private static boolean unless_compound_4(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "unless_compound_4")) return false;
		if_compound_else(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER]  RESERVED_UNLESS expr
	public static boolean unless_statement_modifier(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "unless_statement_modifier")) return false;
		if (!nextTokenIs(b, "<Postfix unless>", PACKAGE_CORE_IDENTIFIER, RESERVED_UNLESS)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<Postfix unless>");
		r = unless_statement_modifier_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_UNLESS);
		p = r; // pin = 2
		r = r && expr(b, l + 1, -1);
		exit_section_(b, l, m, UNLESS_STATEMENT_MODIFIER, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean unless_statement_modifier_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "unless_statement_modifier_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER] RESERVED_UNTIL conditional_block [continue_block]
	public static boolean until_compound(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "until_compound")) return false;
		if (!nextTokenIs(b, "<until compound>", PACKAGE_CORE_IDENTIFIER, RESERVED_UNTIL)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<until compound>");
		r = until_compound_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_UNTIL);
		p = r; // pin = 2
		r = r && report_error_(b, conditional_block(b, l + 1));
		r = p && until_compound_3(b, l + 1) && r;
		exit_section_(b, l, m, UNTIL_COMPOUND, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean until_compound_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "until_compound_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	// [continue_block]
	private static boolean until_compound_3(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "until_compound_3")) return false;
		continue_block(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER]  RESERVED_UNTIL expr
	public static boolean until_statement_modifier(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "until_statement_modifier")) return false;
		if (!nextTokenIs(b, "<Postfix until>", PACKAGE_CORE_IDENTIFIER, RESERVED_UNTIL)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<Postfix until>");
		r = until_statement_modifier_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_UNTIL);
		p = r; // pin = 2
		r = r && expr(b, l + 1, -1);
		exit_section_(b, l, m, UNTIL_STATEMENT_MODIFIER, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean until_statement_modifier_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "until_statement_modifier_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// constant_definition
	//   | constants_block
	//   | expr
	static boolean use_constant_parameters(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "use_constant_parameters")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = constant_definition(b, l + 1);
		if (!r) r = constants_block(b, l + 1);
		if (!r) r = expr(b, l + 1, -1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// <<mergePackageName>> [perl_version [comma]] [<<parseStringifiedExpression>>]
	static boolean use_module_parameters(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "use_module_parameters")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = mergePackageName(b, l + 1);
		r = r && use_module_parameters_1(b, l + 1);
		r = r && use_module_parameters_2(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// [perl_version [comma]]
	private static boolean use_module_parameters_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "use_module_parameters_1")) return false;
		use_module_parameters_1_0(b, l + 1);
		return true;
	}

	// perl_version [comma]
	private static boolean use_module_parameters_1_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "use_module_parameters_1_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = perl_version(b, l + 1);
		r = r && use_module_parameters_1_0_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// [comma]
	private static boolean use_module_parameters_1_0_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "use_module_parameters_1_0_1")) return false;
		comma(b, l + 1);
		return true;
	}

	// [<<parseStringifiedExpression>>]
	private static boolean use_module_parameters_2(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "use_module_parameters_2")) return false;
		parseStringifiedExpression(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// use_module_parameters | use_version_parameters
	static boolean use_no_parameters(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "use_no_parameters")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = use_module_parameters(b, l + 1);
		if (!r) r = use_version_parameters(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER] RESERVED_USE use_no_parameters
	public static boolean use_statement(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "use_statement")) return false;
		if (!nextTokenIs(b, "<use statement>", PACKAGE_CORE_IDENTIFIER, RESERVED_USE)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<use statement>");
		r = use_statement_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_USE);
		p = r; // pin = 2
		r = r && use_no_parameters(b, l + 1);
		exit_section_(b, l, m, USE_STATEMENT, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean use_statement_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "use_statement_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER] RESERVED_USE <<checkAndCollapseToken PACKAGE PACKAGE_PRAGMA_CONSTANT>> [perl_version] [use_constant_parameters]
	public static boolean use_statement_constant(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "use_statement_constant")) return false;
		if (!nextTokenIs(b, "<use statement constant>", PACKAGE_CORE_IDENTIFIER, RESERVED_USE)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<use statement constant>");
		r = use_statement_constant_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_USE);
		r = r && checkAndCollapseToken(b, l + 1, PACKAGE, PACKAGE_PRAGMA_CONSTANT);
		p = r; // pin = 3
		r = r && report_error_(b, use_statement_constant_3(b, l + 1));
		r = p && use_statement_constant_4(b, l + 1) && r;
		exit_section_(b, l, m, USE_STATEMENT_CONSTANT, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean use_statement_constant_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "use_statement_constant_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	// [perl_version]
	private static boolean use_statement_constant_3(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "use_statement_constant_3")) return false;
		perl_version(b, l + 1);
		return true;
	}

	// [use_constant_parameters]
	private static boolean use_statement_constant_4(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "use_statement_constant_4")) return false;
		use_constant_parameters(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// use_statement_constant
	//   | use_vars_statement
	//   | use_statement
	static boolean use_statements(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "use_statements")) return false;
		if (!nextTokenIs(b, "", PACKAGE_CORE_IDENTIFIER, RESERVED_USE)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = use_statement_constant(b, l + 1);
		if (!r) r = use_vars_statement(b, l + 1);
		if (!r) r = use_statement(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// scalar_variable
	//     | array_variable
	//     | hash_variable
	//     | glob_variable
	//     | code_variable
	static boolean use_vars_interpolated_constructs(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "use_vars_interpolated_constructs")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = scalar_variable(b, l + 1);
		if (!r) r = array_variable(b, l + 1);
		if (!r) r = hash_variable(b, l + 1);
		if (!r) r = glob_variable(b, l + 1);
		if (!r) r = code_variable(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER] RESERVED_USE <<checkAndCollapseToken PACKAGE PACKAGE_PRAGMA_VARS>> [perl_version] [<<parseUseVarsParameters>>]
	public static boolean use_vars_statement(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "use_vars_statement")) return false;
		if (!nextTokenIs(b, "<use vars statement>", PACKAGE_CORE_IDENTIFIER, RESERVED_USE)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<use vars statement>");
		r = use_vars_statement_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_USE);
		r = r && checkAndCollapseToken(b, l + 1, PACKAGE, PACKAGE_PRAGMA_VARS);
		p = r; // pin = 3
		r = r && report_error_(b, use_vars_statement_3(b, l + 1));
		r = p && use_vars_statement_4(b, l + 1) && r;
		exit_section_(b, l, m, USE_VARS_STATEMENT, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean use_vars_statement_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "use_vars_statement_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	// [perl_version]
	private static boolean use_vars_statement_3(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "use_vars_statement_3")) return false;
		perl_version(b, l + 1);
		return true;
	}

	// [<<parseUseVarsParameters>>]
	private static boolean use_vars_statement_4(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "use_vars_statement_4")) return false;
		parseUseVarsParameters(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// perl_version
	static boolean use_version_parameters(PsiBuilder b, int l)
	{
		return perl_version(b, l + 1);
	}

	/* ********************************************************** */
	// COLON attribute ([COLON] attribute)*
	static boolean var_attributes(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "var_attributes")) return false;
		if (!nextTokenIs(b, COLON)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, COLON);
		r = r && attribute(b, l + 1);
		r = r && var_attributes_2(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// ([COLON] attribute)*
	private static boolean var_attributes_2(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "var_attributes_2")) return false;
		int c = current_position_(b);
		while (true)
		{
			if (!var_attributes_2_0(b, l + 1)) break;
			if (!empty_element_parsed_guard_(b, "var_attributes_2", c)) break;
			c = current_position_(b);
		}
		return true;
	}

	// [COLON] attribute
	private static boolean var_attributes_2_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "var_attributes_2_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = var_attributes_2_0_0(b, l + 1);
		r = r && attribute(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// [COLON]
	private static boolean var_attributes_2_0_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "var_attributes_2_0_0")) return false;
		consumeToken(b, COLON);
		return true;
	}

	/* ********************************************************** */
	// scalar | array | hash | glob
	static boolean variable(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "variable")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = scalar(b, l + 1);
		if (!r) r = array(b, l + 1);
		if (!r) r = hash(b, l + 1);
		if (!r) r = glob(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// <<parseVariableName>>
	static boolean variable_body(PsiBuilder b, int l)
	{
		return parseVariableName(b, l + 1);
	}

	/* ********************************************************** */
	// variable_declaration_global
	//     | variable_declaration_lexical
	//     | variable_declaration_local
	static boolean variable_declaration(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "variable_declaration")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = variable_declaration_global(b, l + 1);
		if (!r) r = variable_declaration_lexical(b, l + 1);
		if (!r) r = variable_declaration_local(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// variable_declaration_wrapper | [PACKAGE_CORE_IDENTIFIER] RESERVED_UNDEF
	static boolean variable_declaration_argument(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "variable_declaration_argument")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = variable_declaration_wrapper(b, l + 1);
		if (!r) r = variable_declaration_argument_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// [PACKAGE_CORE_IDENTIFIER] RESERVED_UNDEF
	private static boolean variable_declaration_argument_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "variable_declaration_argument_1")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = variable_declaration_argument_1_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_UNDEF);
		exit_section_(b, m, null, r);
		return r;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean variable_declaration_argument_1_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "variable_declaration_argument_1_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER] RESERVED_OUR [<<mergePackageName>>] variable_declaration_variation [var_attributes]
	public static boolean variable_declaration_global(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "variable_declaration_global")) return false;
		if (!nextTokenIs(b, "<variable declaration global>", PACKAGE_CORE_IDENTIFIER, RESERVED_OUR)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<variable declaration global>");
		r = variable_declaration_global_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_OUR);
		p = r; // pin = 2
		r = r && report_error_(b, variable_declaration_global_2(b, l + 1));
		r = p && report_error_(b, variable_declaration_variation(b, l + 1)) && r;
		r = p && variable_declaration_global_4(b, l + 1) && r;
		exit_section_(b, l, m, VARIABLE_DECLARATION_GLOBAL, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean variable_declaration_global_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "variable_declaration_global_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	// [<<mergePackageName>>]
	private static boolean variable_declaration_global_2(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "variable_declaration_global_2")) return false;
		mergePackageName(b, l + 1);
		return true;
	}

	// [var_attributes]
	private static boolean variable_declaration_global_4(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "variable_declaration_global_4")) return false;
		var_attributes(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER] (RESERVED_MY | RESERVED_STATE) [<<mergePackageName>>] variable_declaration_variation [var_attributes]
	public static boolean variable_declaration_lexical(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "variable_declaration_lexical")) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _COLLAPSE_, "<variable declaration lexical>");
		r = variable_declaration_lexical_0(b, l + 1);
		r = r && variable_declaration_lexical_1(b, l + 1);
		p = r; // pin = 2
		r = r && report_error_(b, variable_declaration_lexical_2(b, l + 1));
		r = p && report_error_(b, variable_declaration_variation(b, l + 1)) && r;
		r = p && variable_declaration_lexical_4(b, l + 1) && r;
		exit_section_(b, l, m, VARIABLE_DECLARATION_LEXICAL, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean variable_declaration_lexical_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "variable_declaration_lexical_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	// RESERVED_MY | RESERVED_STATE
	private static boolean variable_declaration_lexical_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "variable_declaration_lexical_1")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, RESERVED_MY);
		if (!r) r = consumeToken(b, RESERVED_STATE);
		exit_section_(b, m, null, r);
		return r;
	}

	// [<<mergePackageName>>]
	private static boolean variable_declaration_lexical_2(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "variable_declaration_lexical_2")) return false;
		mergePackageName(b, l + 1);
		return true;
	}

	// [var_attributes]
	private static boolean variable_declaration_lexical_4(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "variable_declaration_lexical_4")) return false;
		var_attributes(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER] RESERVED_LOCAL [<<mergePackageName>>]  local_variable_declaration_variation
	public static boolean variable_declaration_local(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "variable_declaration_local")) return false;
		if (!nextTokenIs(b, "<variable declaration local>", PACKAGE_CORE_IDENTIFIER, RESERVED_LOCAL)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<variable declaration local>");
		r = variable_declaration_local_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_LOCAL);
		p = r; // pin = 2
		r = r && report_error_(b, variable_declaration_local_2(b, l + 1));
		r = p && local_variable_declaration_variation(b, l + 1) && r;
		exit_section_(b, l, m, VARIABLE_DECLARATION_LOCAL, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean variable_declaration_local_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "variable_declaration_local_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	// [<<mergePackageName>>]
	private static boolean variable_declaration_local_2(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "variable_declaration_local_2")) return false;
		mergePackageName(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// variable_parenthesised_declaration | variable_declaration_argument
	static boolean variable_declaration_variation(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "variable_declaration_variation")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = variable_parenthesised_declaration(b, l + 1);
		if (!r) r = variable_declaration_argument(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	/* ********************************************************** */
	// lexical_variable
	public static boolean variable_declaration_wrapper(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "variable_declaration_wrapper")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<variable declaration wrapper>");
		r = lexical_variable(b, l + 1);
		exit_section_(b, l, m, VARIABLE_DECLARATION_WRAPPER, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// variable | array_index_variable
	static boolean variable_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "variable_expr")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<expression>");
		r = variable(b, l + 1);
		if (!r) r = array_index_variable(b, l + 1);
		exit_section_(b, l, m, null, r, false, null);
		return r;
	}

	/* ********************************************************** */
	// LEFT_PAREN variable_parenthesised_declaration_contents RIGHT_PAREN
	static boolean variable_parenthesised_declaration(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "variable_parenthesised_declaration")) return false;
		if (!nextTokenIs(b, LEFT_PAREN)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = consumeToken(b, LEFT_PAREN);
		p = r; // pin = 1
		r = r && report_error_(b, variable_parenthesised_declaration_contents(b, l + 1));
		r = p && consumeToken(b, RIGHT_PAREN) && r;
		exit_section_(b, l, m, null, r, p, null);
		return r || p;
	}

	/* ********************************************************** */
	// strict_variable_declaration_argument (comma + strict_variable_declaration_argument ) * comma*
	static boolean variable_parenthesised_declaration_contents(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "variable_parenthesised_declaration_contents")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = strict_variable_declaration_argument(b, l + 1);
		r = r && variable_parenthesised_declaration_contents_1(b, l + 1);
		r = r && variable_parenthesised_declaration_contents_2(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// (comma + strict_variable_declaration_argument ) *
	private static boolean variable_parenthesised_declaration_contents_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "variable_parenthesised_declaration_contents_1")) return false;
		int c = current_position_(b);
		while (true)
		{
			if (!variable_parenthesised_declaration_contents_1_0(b, l + 1)) break;
			if (!empty_element_parsed_guard_(b, "variable_parenthesised_declaration_contents_1", c)) break;
			c = current_position_(b);
		}
		return true;
	}

	// comma + strict_variable_declaration_argument
	private static boolean variable_parenthesised_declaration_contents_1_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "variable_parenthesised_declaration_contents_1_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = variable_parenthesised_declaration_contents_1_0_0(b, l + 1);
		r = r && strict_variable_declaration_argument(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// comma +
	private static boolean variable_parenthesised_declaration_contents_1_0_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "variable_parenthesised_declaration_contents_1_0_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = comma(b, l + 1);
		int c = current_position_(b);
		while (r)
		{
			if (!comma(b, l + 1)) break;
			if (!empty_element_parsed_guard_(b, "variable_parenthesised_declaration_contents_1_0_0", c)) break;
			c = current_position_(b);
		}
		exit_section_(b, m, null, r);
		return r;
	}

	// comma*
	private static boolean variable_parenthesised_declaration_contents_2(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "variable_parenthesised_declaration_contents_2")) return false;
		int c = current_position_(b);
		while (true)
		{
			if (!comma(b, l + 1)) break;
			if (!empty_element_parsed_guard_(b, "variable_parenthesised_declaration_contents_2", c)) break;
			c = current_position_(b);
		}
		return true;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER] RESERVED_WHEN conditional_block
	public static boolean when_compound(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "when_compound")) return false;
		if (!nextTokenIs(b, "<when compound>", PACKAGE_CORE_IDENTIFIER, RESERVED_WHEN)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<when compound>");
		r = when_compound_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_WHEN);
		p = r; // pin = 2
		r = r && conditional_block(b, l + 1);
		exit_section_(b, l, m, WHEN_COMPOUND, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean when_compound_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "when_compound_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER]  RESERVED_WHEN expr
	public static boolean when_statement_modifier(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "when_statement_modifier")) return false;
		if (!nextTokenIs(b, "<Postfix when>", PACKAGE_CORE_IDENTIFIER, RESERVED_WHEN)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<Postfix when>");
		r = when_statement_modifier_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_WHEN);
		p = r; // pin = 2
		r = r && expr(b, l + 1, -1);
		exit_section_(b, l, m, WHEN_STATEMENT_MODIFIER, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean when_statement_modifier_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "when_statement_modifier_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER] RESERVED_WHILE conditional_block_while [continue_block]
	public static boolean while_compound(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "while_compound")) return false;
		if (!nextTokenIs(b, "<while compound>", PACKAGE_CORE_IDENTIFIER, RESERVED_WHILE)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<while compound>");
		r = while_compound_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_WHILE);
		p = r; // pin = 2
		r = r && report_error_(b, conditional_block_while(b, l + 1));
		r = p && while_compound_3(b, l + 1) && r;
		exit_section_(b, l, m, WHILE_COMPOUND, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean while_compound_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "while_compound_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	// [continue_block]
	private static boolean while_compound_3(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "while_compound_3")) return false;
		continue_block(b, l + 1);
		return true;
	}

	/* ********************************************************** */
	// [PACKAGE_CORE_IDENTIFIER]  RESERVED_WHILE expr
	public static boolean while_statement_modifier(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "while_statement_modifier")) return false;
		if (!nextTokenIs(b, "<Postfix while>", PACKAGE_CORE_IDENTIFIER, RESERVED_WHILE)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<Postfix while>");
		r = while_statement_modifier_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_WHILE);
		p = r; // pin = 2
		r = r && expr(b, l + 1, -1);
		exit_section_(b, l, m, WHILE_STATEMENT_MODIFIER, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean while_statement_modifier_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "while_statement_modifier_0")) return false;
		consumeToken(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	/* ********************************************************** */
	// Expression root: expr
	// Operator priority table:
	// 0: N_ARY(lp_or_xor_expr)
	// 1: N_ARY(lp_and_expr)
	// 2: PREFIX(lp_not_expr)
	// 3: ATOM(named_list_expr)
	// 4: POSTFIX(comma_sequence_expr)
	// 5: N_ARY(assign_expr) ATOM(last_expr) ATOM(next_expr) ATOM(goto_expr) ATOM(redo_expr)
	// 6: POSTFIX(trenar_expr)
	// 7: BINARY(flipflop_expr)
	// 8: N_ARY(or_expr)
	// 9: N_ARY(and_expr)
	// 10: N_ARY(bitwise_or_xor_expr)
	// 11: N_ARY(bitwise_and_expr)
	// 12: BINARY(equal_expr)
	// 13: BINARY(compare_expr)
	// 14: ATOM(named_unary_expr)
	// 15: N_ARY(shift_expr)
	// 16: N_ARY(add_expr)
	// 17: N_ARY(mul_expr)
	// 18: BINARY(regex_expr)
	// 19: ATOM(ref_expr) ATOM(prefix_minus_as_string_expr) PREFIX(prefix_unary_expr)
	// 20: N_ARY(pow_expr)
	// 21: PREFIX(pref_pp_expr) PREFIX(pref_mm_expr) POSTFIX(suff_pp_expr)
	// 22: POSTFIX(deref_expr)
	// 23: ATOM(term_expr)
	public static boolean expr(PsiBuilder b, int l, int g)
	{
		if (!recursion_guard_(b, l, "expr")) return false;
		addVariant(b, "<expression>");
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<expression>");
		r = lp_not_expr(b, l + 1);
		if (!r) r = named_list_expr(b, l + 1);
		if (!r) r = last_expr(b, l + 1);
		if (!r) r = next_expr(b, l + 1);
		if (!r) r = goto_expr(b, l + 1);
		if (!r) r = redo_expr(b, l + 1);
		if (!r) r = named_unary_expr(b, l + 1);
		if (!r) r = ref_expr(b, l + 1);
		if (!r) r = prefix_minus_as_string_expr(b, l + 1);
		if (!r) r = prefix_unary_expr(b, l + 1);
		if (!r) r = pref_pp_expr(b, l + 1);
		if (!r) r = pref_mm_expr(b, l + 1);
		if (!r) r = term_expr(b, l + 1);
		p = r;
		r = r && expr_0(b, l + 1, g);
		exit_section_(b, l, m, null, r, p, null);
		return r || p;
	}

	public static boolean expr_0(PsiBuilder b, int l, int g)
	{
		if (!recursion_guard_(b, l, "expr_0")) return false;
		boolean r = true;
		while (true)
		{
			Marker m = enter_section_(b, l, _LEFT_, null);
			if (g < 0 && lp_or_xor_expr_0(b, l + 1))
			{
				while (true)
				{
					r = report_error_(b, expr(b, l, 0));
					if (!lp_or_xor_expr_0(b, l + 1)) break;
				}
				exit_section_(b, l, m, LP_OR_XOR_EXPR, r, true, null);
			}
			else if (g < 1 && consumeTokenSmart(b, OPERATOR_AND_LP))
			{
				while (true)
				{
					r = report_error_(b, expr(b, l, 1));
					if (!consumeTokenSmart(b, OPERATOR_AND_LP)) break;
				}
				exit_section_(b, l, m, LP_AND_EXPR, r, true, null);
			}
			else if (g < 4 && parseCommaSequence(b, l + 1))
			{
				r = true;
				exit_section_(b, l, m, COMMA_SEQUENCE_EXPR, r, true, null);
			}
			else if (g < 5 && operator_assign(b, l + 1))
			{
				while (true)
				{
					r = report_error_(b, expr(b, l, 5));
					if (!operator_assign(b, l + 1)) break;
				}
				exit_section_(b, l, m, ASSIGN_EXPR, r, true, null);
			}
			else if (g < 6 && trenar_expr_0(b, l + 1))
			{
				r = true;
				exit_section_(b, l, m, TRENAR_EXPR, r, true, null);
			}
			else if (g < 7 && flipflop_expr_0(b, l + 1))
			{
				r = expr(b, l, 7);
				exit_section_(b, l, m, FLIPFLOP_EXPR, r, true, null);
			}
			else if (g < 8 && operator_or_or_defined(b, l + 1))
			{
				while (true)
				{
					r = report_error_(b, expr(b, l, 8));
					if (!operator_or_or_defined(b, l + 1)) break;
				}
				exit_section_(b, l, m, OR_EXPR, r, true, null);
			}
			else if (g < 9 && operator_and(b, l + 1))
			{
				while (true)
				{
					r = report_error_(b, expr(b, l, 9));
					if (!operator_and(b, l + 1)) break;
				}
				exit_section_(b, l, m, AND_EXPR, r, true, null);
			}
			else if (g < 10 && operator_bitwise_or_xor(b, l + 1))
			{
				while (true)
				{
					r = report_error_(b, expr(b, l, 10));
					if (!operator_bitwise_or_xor(b, l + 1)) break;
				}
				exit_section_(b, l, m, BITWISE_OR_XOR_EXPR, r, true, null);
			}
			else if (g < 11 && bitwise_and_expr_0(b, l + 1))
			{
				while (true)
				{
					r = report_error_(b, expr(b, l, 11));
					if (!bitwise_and_expr_0(b, l + 1)) break;
				}
				exit_section_(b, l, m, BITWISE_AND_EXPR, r, true, null);
			}
			else if (g < 12 && operator_equal(b, l + 1))
			{
				r = expr(b, l, 12);
				exit_section_(b, l, m, EQUAL_EXPR, r, true, null);
			}
			else if (g < 13 && compare_expr_0(b, l + 1))
			{
				r = expr(b, l, 13);
				exit_section_(b, l, m, COMPARE_EXPR, r, true, null);
			}
			else if (g < 15 && operator_shift(b, l + 1))
			{
				while (true)
				{
					r = report_error_(b, expr(b, l, 15));
					if (!operator_shift(b, l + 1)) break;
				}
				exit_section_(b, l, m, SHIFT_EXPR, r, true, null);
			}
			else if (g < 16 && operator_add(b, l + 1))
			{
				while (true)
				{
					r = report_error_(b, expr(b, l, 16));
					if (!operator_add(b, l + 1)) break;
				}
				exit_section_(b, l, m, ADD_EXPR, r, true, null);
			}
			else if (g < 17 && operator_mul(b, l + 1))
			{
				while (true)
				{
					r = report_error_(b, expr(b, l, 17));
					if (!operator_mul(b, l + 1)) break;
				}
				exit_section_(b, l, m, MUL_EXPR, r, true, null);
			}
			else if (g < 18 && regex_expr_0(b, l + 1))
			{
				r = expr(b, l, 18);
				exit_section_(b, l, m, REGEX_EXPR, r, true, null);
			}
			else if (g < 20 && pow_operator(b, l + 1))
			{
				while (true)
				{
					r = report_error_(b, expr(b, l, 20));
					if (!pow_operator(b, l + 1)) break;
				}
				exit_section_(b, l, m, POW_EXPR, r, true, null);
			}
			else if (g < 21 && suff_pp_expr_0(b, l + 1))
			{
				r = true;
				exit_section_(b, l, m, SUFF_PP_EXPR, r, true, null);
			}
			else if (g < 22 && deref_expr_0(b, l + 1))
			{
				r = true;
				exit_section_(b, l, m, DEREF_EXPR, r, true, null);
			}
			else
			{
				exit_section_(b, l, m, null, false, false, null);
				break;
			}
		}
		return r;
	}

	// OPERATOR_OR_LP|OPERATOR_XOR_LP
	private static boolean lp_or_xor_expr_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "lp_or_xor_expr_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeTokenSmart(b, OPERATOR_OR_LP);
		if (!r) r = consumeTokenSmart(b, OPERATOR_XOR_LP);
		exit_section_(b, m, null, r);
		return r;
	}

	public static boolean lp_not_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "lp_not_expr")) return false;
		if (!nextTokenIsFast(b, OPERATOR_NOT_LP)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = consumeTokenSmart(b, OPERATOR_NOT_LP);
		p = r;
		r = p && expr(b, l, 2);
		exit_section_(b, l, m, LP_NOT_EXPR, r, p, null);
		return r || p;
	}

	// <<parseListExpression>>
	public static boolean named_list_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "named_list_expr")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _COLLAPSE_, "<expression>");
		r = parseListExpression(b, l + 1);
		exit_section_(b, l, m, NAMED_LIST_EXPR, r, false, null);
		return r;
	}

	// [PACKAGE_CORE_IDENTIFIER] RESERVED_LAST [lnr_param]
	public static boolean last_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "last_expr")) return false;
		if (!nextTokenIsFast(b, PACKAGE_CORE_IDENTIFIER, RESERVED_LAST)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<expression>");
		r = last_expr_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_LAST);
		p = r; // pin = 2
		r = r && last_expr_2(b, l + 1);
		exit_section_(b, l, m, LAST_EXPR, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean last_expr_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "last_expr_0")) return false;
		consumeTokenSmart(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	// [lnr_param]
	private static boolean last_expr_2(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "last_expr_2")) return false;
		lnr_param(b, l + 1);
		return true;
	}

	// [PACKAGE_CORE_IDENTIFIER] RESERVED_NEXT [lnr_param]
	public static boolean next_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "next_expr")) return false;
		if (!nextTokenIsFast(b, PACKAGE_CORE_IDENTIFIER, RESERVED_NEXT)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<expression>");
		r = next_expr_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_NEXT);
		p = r; // pin = 2
		r = r && next_expr_2(b, l + 1);
		exit_section_(b, l, m, NEXT_EXPR, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean next_expr_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "next_expr_0")) return false;
		consumeTokenSmart(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	// [lnr_param]
	private static boolean next_expr_2(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "next_expr_2")) return false;
		lnr_param(b, l + 1);
		return true;
	}

	// [PACKAGE_CORE_IDENTIFIER] RESERVED_GOTO [goto_param]
	public static boolean goto_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "goto_expr")) return false;
		if (!nextTokenIsFast(b, PACKAGE_CORE_IDENTIFIER, RESERVED_GOTO)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<expression>");
		r = goto_expr_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_GOTO);
		p = r; // pin = 2
		r = r && goto_expr_2(b, l + 1);
		exit_section_(b, l, m, GOTO_EXPR, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean goto_expr_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "goto_expr_0")) return false;
		consumeTokenSmart(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	// [goto_param]
	private static boolean goto_expr_2(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "goto_expr_2")) return false;
		goto_param(b, l + 1);
		return true;
	}

	// [PACKAGE_CORE_IDENTIFIER] RESERVED_REDO [lnr_param]
	public static boolean redo_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "redo_expr")) return false;
		if (!nextTokenIsFast(b, PACKAGE_CORE_IDENTIFIER, RESERVED_REDO)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, "<expression>");
		r = redo_expr_0(b, l + 1);
		r = r && consumeToken(b, RESERVED_REDO);
		p = r; // pin = 2
		r = r && redo_expr_2(b, l + 1);
		exit_section_(b, l, m, REDO_EXPR, r, p, null);
		return r || p;
	}

	// [PACKAGE_CORE_IDENTIFIER]
	private static boolean redo_expr_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "redo_expr_0")) return false;
		consumeTokenSmart(b, PACKAGE_CORE_IDENTIFIER);
		return true;
	}

	// [lnr_param]
	private static boolean redo_expr_2(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "redo_expr_2")) return false;
		lnr_param(b, l + 1);
		return true;
	}

	// QUESTION scalar_expr COLON scalar_expr
	private static boolean trenar_expr_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "trenar_expr_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeTokenSmart(b, QUESTION);
		r = r && scalar_expr(b, l + 1);
		r = r && consumeToken(b, COLON);
		r = r && scalar_expr(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// OPERATOR_FLIP_FLOP|OPERATOR_HELLIP
	private static boolean flipflop_expr_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "flipflop_expr_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeTokenSmart(b, OPERATOR_FLIP_FLOP);
		if (!r) r = consumeTokenSmart(b, OPERATOR_HELLIP);
		exit_section_(b, m, null, r);
		return r;
	}

	// OPERATOR_BITWISE_AND !OPERATOR_ASSIGN
	private static boolean bitwise_and_expr_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "bitwise_and_expr_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeTokenSmart(b, OPERATOR_BITWISE_AND);
		r = r && bitwise_and_expr_0_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// !OPERATOR_ASSIGN
	private static boolean bitwise_and_expr_0_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "bitwise_and_expr_0_1")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NOT_, null);
		r = !consumeTokenSmart(b, OPERATOR_ASSIGN);
		exit_section_(b, l, m, null, r, false, null);
		return r;
	}

	// (operator_compare)
	private static boolean compare_expr_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "compare_expr_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = operator_compare(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// { OPERATOR_FILETEST | <<isUnaryOperator>> method } [unary_expr]
	public static boolean named_unary_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "named_unary_expr")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _COLLAPSE_, "<expression>");
		r = named_unary_expr_0(b, l + 1);
		r = r && named_unary_expr_1(b, l + 1);
		exit_section_(b, l, m, NAMED_UNARY_EXPR, r, false, null);
		return r;
	}

	// OPERATOR_FILETEST | <<isUnaryOperator>> method
	private static boolean named_unary_expr_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "named_unary_expr_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeTokenSmart(b, OPERATOR_FILETEST);
		if (!r) r = named_unary_expr_0_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// <<isUnaryOperator>> method
	private static boolean named_unary_expr_0_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "named_unary_expr_0_1")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = isUnaryOperator(b, l + 1);
		r = r && method(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// [unary_expr]
	private static boolean named_unary_expr_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "named_unary_expr_1")) return false;
		unary_expr(b, l + 1);
		return true;
	}

	// OPERATOR_RE|OPERATOR_NOT_RE
	private static boolean regex_expr_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "regex_expr_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeTokenSmart(b, OPERATOR_RE);
		if (!r) r = consumeTokenSmart(b, OPERATOR_NOT_RE);
		exit_section_(b, m, null, r);
		return r;
	}

	// OPERATOR_REFERENCE referencable_expr
	public static boolean ref_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "ref_expr")) return false;
		if (!nextTokenIsFast(b, OPERATOR_REFERENCE)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeTokenSmart(b, OPERATOR_REFERENCE);
		r = r && referencable_expr(b, l + 1);
		exit_section_(b, m, REF_EXPR, r);
		return r;
	}

	// <<parseMinusBareword>>
	public static boolean prefix_minus_as_string_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "prefix_minus_as_string_expr")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _COLLAPSE_, "<expression>");
		r = parseMinusBareword(b, l + 1);
		exit_section_(b, l, m, PREFIX_MINUS_AS_STRING_EXPR, r, false, null);
		return r;
	}

	public static boolean prefix_unary_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "prefix_unary_expr")) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = operator_prefix_unary(b, l + 1);
		p = r;
		r = p && expr(b, l, 19);
		exit_section_(b, l, m, PREFIX_UNARY_EXPR, r, p, null);
		return r || p;
	}

	public static boolean pref_pp_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "pref_pp_expr")) return false;
		if (!nextTokenIsFast(b, OPERATOR_PLUS_PLUS)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = consumeTokenSmart(b, OPERATOR_PLUS_PLUS);
		p = r;
		r = p && expr(b, l, 21);
		exit_section_(b, l, m, PREF_PP_EXPR, r, p, null);
		return r || p;
	}

	public static boolean pref_mm_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "pref_mm_expr")) return false;
		if (!nextTokenIsFast(b, OPERATOR_MINUS_MINUS)) return false;
		boolean r, p;
		Marker m = enter_section_(b, l, _NONE_, null);
		r = consumeTokenSmart(b, OPERATOR_MINUS_MINUS);
		p = r;
		r = p && expr(b, l, 21);
		exit_section_(b, l, m, PREF_MM_EXPR, r, p, null);
		return r || p;
	}

	// OPERATOR_PLUS_PLUS|OPERATOR_MINUS_MINUS
	private static boolean suff_pp_expr_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "suff_pp_expr_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeTokenSmart(b, OPERATOR_PLUS_PLUS);
		if (!r) r = consumeTokenSmart(b, OPERATOR_MINUS_MINUS);
		exit_section_(b, m, null, r);
		return r;
	}

	// (<<parseArrowSmart>> nested_element_variation) +
	private static boolean deref_expr_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "deref_expr_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = deref_expr_0_0(b, l + 1);
		int c = current_position_(b);
		while (r)
		{
			if (!deref_expr_0_0(b, l + 1)) break;
			if (!empty_element_parsed_guard_(b, "deref_expr_0", c)) break;
			c = current_position_(b);
		}
		exit_section_(b, m, null, r);
		return r;
	}

	// <<parseArrowSmart>> nested_element_variation
	private static boolean deref_expr_0_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "deref_expr_0_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = parseArrowSmart(b, l + 1);
		r = r && nested_element_variation(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// <<parseParserExtensionTerm>>
	//     | variable_declaration
	//     | <<parseListOrListElement>> // fixme this must be in array with qw
	//     | scalar_constant
	//     | do_expr
	//     | sub_expr
	//     | eval_expr
	//     | grep_expr
	//     | return_expr
	//     | map_expr
	//     | sort_expr
	//     | regex_term
	//     | file_read_expr
	//     | print_expr
	//     | require_expr
	//     | undef_expr
	//     | perl_handle_expr
	//     | sub_call_expr
	//     | reference_value_expr
	//     | variable_expr
	//     | namespace_expr
	public static boolean term_expr(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "term_expr")) return false;
		boolean r;
		Marker m = enter_section_(b, l, _COLLAPSE_, "<expression>");
		r = parseParserExtensionTerm(b, l + 1);
		if (!r) r = variable_declaration(b, l + 1);
		if (!r) r = parseListOrListElement(b, l + 1);
		if (!r) r = scalar_constant(b, l + 1);
		if (!r) r = do_expr(b, l + 1);
		if (!r) r = sub_expr(b, l + 1);
		if (!r) r = eval_expr(b, l + 1);
		if (!r) r = grep_expr(b, l + 1);
		if (!r) r = return_expr(b, l + 1);
		if (!r) r = map_expr(b, l + 1);
		if (!r) r = sort_expr(b, l + 1);
		if (!r) r = regex_term(b, l + 1);
		if (!r) r = file_read_expr(b, l + 1);
		if (!r) r = print_expr(b, l + 1);
		if (!r) r = require_expr(b, l + 1);
		if (!r) r = undef_expr(b, l + 1);
		if (!r) r = perl_handle_expr(b, l + 1);
		if (!r) r = sub_call_expr(b, l + 1);
		if (!r) r = reference_value_expr(b, l + 1);
		if (!r) r = variable_expr(b, l + 1);
		if (!r) r = namespace_expr(b, l + 1);
		exit_section_(b, l, m, TERM_EXPR, r, false, null);
		return r;
	}

	public ASTNode parse(IElementType t, PsiBuilder b)
	{
		parseLight(t, b);
		return b.getTreeBuilt();
	}

	public void parseLight(IElementType t, PsiBuilder b)
	{
		boolean r;
		b = adapt_builder_(t, b, this, EXTENDS_SETS_);
		Marker m = enter_section_(b, 0, _COLLAPSE_, null);
		if (t == ADD_EXPR)
		{
			r = expr(b, 0, 15);
		}
		else if (t == AND_EXPR)
		{
			r = expr(b, 0, 8);
		}
		else if (t == ANNOTATION)
		{
			r = annotation(b, 0);
		}
		else if (t == ANNOTATION_ABSTRACT)
		{
			r = annotation_abstract(b, 0);
		}
		else if (t == ANNOTATION_DEPRECATED)
		{
			r = annotation_deprecated(b, 0);
		}
		else if (t == ANNOTATION_INCOMPLETE)
		{
			r = annotation_incomplete(b, 0);
		}
		else if (t == ANNOTATION_METHOD)
		{
			r = annotation_method(b, 0);
		}
		else if (t == ANNOTATION_OVERRIDE)
		{
			r = annotation_override(b, 0);
		}
		else if (t == ANNOTATION_RETURNS_ARRAYREF)
		{
			r = annotation_returns_arrayref(b, 0);
		}
		else if (t == ANNOTATION_RETURNS_HASHREF)
		{
			r = annotation_returns_hashref(b, 0);
		}
		else if (t == ANNOTATION_RETURNS_REF)
		{
			r = annotation_returns_ref(b, 0);
		}
		else if (t == ANNOTATION_UNKNOWN)
		{
			r = annotation_unknown(b, 0);
		}
		else if (t == ANON_ARRAY)
		{
			r = anon_array(b, 0);
		}
		else if (t == ANON_ARRAY_ELEMENT)
		{
			r = anon_array_element(b, 0);
		}
		else if (t == ANON_HASH)
		{
			r = anon_hash(b, 0);
		}
		else if (t == ANON_SUB)
		{
			r = anon_sub(b, 0);
		}
		else if (t == ARRAY_ARRAY_SLICE)
		{
			r = array_array_slice(b, 0);
		}
		else if (t == ARRAY_CAST_EXPR)
		{
			r = array_cast_expr(b, 0);
		}
		else if (t == ARRAY_HASH_SLICE)
		{
			r = array_hash_slice(b, 0);
		}
		else if (t == ARRAY_INDEX)
		{
			r = array_index(b, 0);
		}
		else if (t == ARRAY_INDEX_VARIABLE)
		{
			r = array_index_variable(b, 0);
		}
		else if (t == ARRAY_VARIABLE)
		{
			r = array_variable(b, 0);
		}
		else if (t == ASSIGN_EXPR)
		{
			r = expr(b, 0, 4);
		}
		else if (t == ATTRIBUTE)
		{
			r = attribute(b, 0);
		}
		else if (t == BITWISE_AND_EXPR)
		{
			r = expr(b, 0, 10);
		}
		else if (t == BITWISE_OR_XOR_EXPR)
		{
			r = expr(b, 0, 9);
		}
		else if (t == BLOCK)
		{
			r = block(b, 0);
		}
		else if (t == CALL_ARGUMENTS)
		{
			r = call_arguments(b, 0);
		}
		else if (t == CODE_CAST_EXPR)
		{
			r = code_cast_expr(b, 0);
		}
		else if (t == CODE_VARIABLE)
		{
			r = code_variable(b, 0);
		}
		else if (t == COMMA_SEQUENCE_EXPR)
		{
			r = expr(b, 0, 3);
		}
		else if (t == COMPARE_EXPR)
		{
			r = expr(b, 0, 12);
		}
		else if (t == COMPILE_REGEX)
		{
			r = compile_regex(b, 0);
		}
		else if (t == CONDITION_STATEMENT)
		{
			r = condition_statement(b, 0);
		}
		else if (t == CONDITION_STATEMENT_WHILE)
		{
			r = condition_statement_while(b, 0);
		}
		else if (t == CONDITIONAL_BLOCK)
		{
			r = conditional_block(b, 0);
		}
		else if (t == CONDITIONAL_BLOCK_WHILE)
		{
			r = conditional_block_while(b, 0);
		}
		else if (t == CONSTANT_DEFINITION)
		{
			r = constant_definition(b, 0);
		}
		else if (t == CONSTANT_NAME)
		{
			r = constant_name(b, 0);
		}
		else if (t == CONSTANTS_BLOCK)
		{
			r = constants_block(b, 0);
		}
		else if (t == CONTINUE_BLOCK)
		{
			r = continue_block(b, 0);
		}
		else if (t == DEFAULT_COMPOUND)
		{
			r = default_compound(b, 0);
		}
		else if (t == DEREF_EXPR)
		{
			r = expr(b, 0, 21);
		}
		else if (t == DO_EXPR)
		{
			r = do_expr(b, 0);
		}
		else if (t == EQUAL_EXPR)
		{
			r = expr(b, 0, 11);
		}
		else if (t == EVAL_EXPR)
		{
			r = eval_expr(b, 0);
		}
		else if (t == EXPR)
		{
			r = expr(b, 0, -1);
		}
		else if (t == FILE_READ_EXPR)
		{
			r = file_read_expr(b, 0);
		}
		else if (t == FILE_READ_FORCED_EXPR)
		{
			r = file_read_forced_expr(b, 0);
		}
		else if (t == FLIPFLOP_EXPR)
		{
			r = expr(b, 0, 6);
		}
		else if (t == FOR_COMPOUND)
		{
			r = for_compound(b, 0);
		}
		else if (t == FOR_ITERATOR)
		{
			r = for_iterator(b, 0);
		}
		else if (t == FOR_ITERATOR_STATEMENT)
		{
			r = for_iterator_statement(b, 0);
		}
		else if (t == FOR_LIST_EPXR)
		{
			r = for_list_epxr(b, 0);
		}
		else if (t == FOR_LIST_STATEMENT)
		{
			r = for_list_statement(b, 0);
		}
		else if (t == FOR_STATEMENT_MODIFIER)
		{
			r = for_statement_modifier(b, 0);
		}
		else if (t == FOREACH_COMPOUND)
		{
			r = foreach_compound(b, 0);
		}
		else if (t == FOREACH_STATEMENT_MODIFIER)
		{
			r = foreach_statement_modifier(b, 0);
		}
		else if (t == FORMAT_DEFINITION)
		{
			r = format_definition(b, 0);
		}
		else if (t == FUNC_DEFINITION)
		{
			r = func_definition(b, 0);
		}
		else if (t == FUNC_SIGNATURE_CONTENT)
		{
			r = func_signature_content(b, 0);
		}
		else if (t == GIVEN_COMPOUND)
		{
			r = given_compound(b, 0);
		}
		else if (t == GLOB_CAST_EXPR)
		{
			r = glob_cast_expr(b, 0);
		}
		else if (t == GLOB_SLOT)
		{
			r = glob_slot(b, 0);
		}
		else if (t == GLOB_VARIABLE)
		{
			r = glob_variable(b, 0);
		}
		else if (t == GOTO_EXPR)
		{
			r = goto_expr(b, 0);
		}
		else if (t == GREP_EXPR)
		{
			r = grep_expr(b, 0);
		}
		else if (t == HASH_CAST_EXPR)
		{
			r = hash_cast_expr(b, 0);
		}
		else if (t == HASH_INDEX)
		{
			r = hash_index(b, 0);
		}
		else if (t == HASH_VARIABLE)
		{
			r = hash_variable(b, 0);
		}
		else if (t == HEREDOC_OPENER)
		{
			r = heredoc_opener(b, 0);
		}
		else if (t == IF_COMPOUND)
		{
			r = if_compound(b, 0);
		}
		else if (t == IF_STATEMENT_MODIFIER)
		{
			r = if_statement_modifier(b, 0);
		}
		else if (t == LABEL_DECLARATION)
		{
			r = label_declaration(b, 0);
		}
		else if (t == LAST_EXPR)
		{
			r = last_expr(b, 0);
		}
		else if (t == LP_AND_EXPR)
		{
			r = expr(b, 0, 0);
		}
		else if (t == LP_NOT_EXPR)
		{
			r = lp_not_expr(b, 0);
		}
		else if (t == LP_OR_XOR_EXPR)
		{
			r = expr(b, 0, -1);
		}
		else if (t == MAP_EXPR)
		{
			r = map_expr(b, 0);
		}
		else if (t == MATCH_REGEX)
		{
			r = match_regex(b, 0);
		}
		else if (t == METHOD)
		{
			r = method(b, 0);
		}
		else if (t == METHOD_DEFINITION)
		{
			r = method_definition(b, 0);
		}
		else if (t == METHOD_SIGNATURE_CONTENT)
		{
			r = method_signature_content(b, 0);
		}
		else if (t == METHOD_SIGNATURE_INVOCANT)
		{
			r = method_signature_invocant(b, 0);
		}
		else if (t == MUL_EXPR)
		{
			r = expr(b, 0, 16);
		}
		else if (t == NAMED_BLOCK)
		{
			r = named_block(b, 0);
		}
		else if (t == NAMED_LIST_EXPR)
		{
			r = named_list_expr(b, 0);
		}
		else if (t == NAMED_UNARY_EXPR)
		{
			r = named_unary_expr(b, 0);
		}
		else if (t == NAMESPACE_CONTENT)
		{
			r = namespace_content(b, 0);
		}
		else if (t == NAMESPACE_DEFINITION)
		{
			r = namespace_definition(b, 0);
		}
		else if (t == NAMESPACE_EXPR)
		{
			r = namespace_expr(b, 0);
		}
		else if (t == NESTED_CALL)
		{
			r = nested_call(b, 0);
		}
		else if (t == NESTED_CALL_ARGUMENTS)
		{
			r = nested_call_arguments(b, 0);
		}
		else if (t == NEXT_EXPR)
		{
			r = next_expr(b, 0);
		}
		else if (t == NO_STATEMENT)
		{
			r = no_statement(b, 0);
		}
		else if (t == NUMBER_CONSTANT)
		{
			r = number_constant(b, 0);
		}
		else if (t == NYI_STATEMENT)
		{
			r = nyi_statement(b, 0);
		}
		else if (t == OR_EXPR)
		{
			r = expr(b, 0, 7);
		}
		else if (t == PARENTHESISED_EXPR)
		{
			r = parenthesised_expr(b, 0);
		}
		else if (t == PERL_HANDLE_EXPR)
		{
			r = perl_handle_expr(b, 0);
		}
		else if (t == PERL_REGEX)
		{
			r = perl_regex(b, 0);
		}
		else if (t == PERL_REGEX_EX)
		{
			r = perl_regex_ex(b, 0);
		}
		else if (t == PERL_REGEX_MODIFIERS)
		{
			r = perl_regex_modifiers(b, 0);
		}
		else if (t == POW_EXPR)
		{
			r = expr(b, 0, 19);
		}
		else if (t == PREF_MM_EXPR)
		{
			r = pref_mm_expr(b, 0);
		}
		else if (t == PREF_PP_EXPR)
		{
			r = pref_pp_expr(b, 0);
		}
		else if (t == PREFIX_MINUS_AS_STRING_EXPR)
		{
			r = prefix_minus_as_string_expr(b, 0);
		}
		else if (t == PREFIX_UNARY_EXPR)
		{
			r = prefix_unary_expr(b, 0);
		}
		else if (t == PRINT_EXPR)
		{
			r = print_expr(b, 0);
		}
		else if (t == PRINT_HANDLE)
		{
			r = print_handle(b, 0);
		}
		else if (t == READ_HANDLE)
		{
			r = read_handle(b, 0);
		}
		else if (t == REDO_EXPR)
		{
			r = redo_expr(b, 0);
		}
		else if (t == REF_EXPR)
		{
			r = ref_expr(b, 0);
		}
		else if (t == REGEX_EXPR)
		{
			r = expr(b, 0, 17);
		}
		else if (t == REPLACEMENT_REGEX)
		{
			r = replacement_regex(b, 0);
		}
		else if (t == REQUIRE_EXPR)
		{
			r = require_expr(b, 0);
		}
		else if (t == RETURN_EXPR)
		{
			r = return_expr(b, 0);
		}
		else if (t == SCALAR_ARRAY_ELEMENT)
		{
			r = scalar_array_element(b, 0);
		}
		else if (t == SCALAR_CALL)
		{
			r = scalar_call(b, 0);
		}
		else if (t == SCALAR_CAST_EXPR)
		{
			r = scalar_cast_expr(b, 0);
		}
		else if (t == SCALAR_HASH_ELEMENT)
		{
			r = scalar_hash_element(b, 0);
		}
		else if (t == SCALAR_INDEX_CAST_EXPR)
		{
			r = scalar_index_cast_expr(b, 0);
		}
		else if (t == SCALAR_VARIABLE)
		{
			r = scalar_variable(b, 0);
		}
		else if (t == SHIFT_EXPR)
		{
			r = expr(b, 0, 14);
		}
		else if (t == SORT_EXPR)
		{
			r = sort_expr(b, 0);
		}
		else if (t == STATEMENT)
		{
			r = statement(b, 0);
		}
		else if (t == STATEMENT_MODIFIER)
		{
			r = statement_modifier(b, 0);
		}
		else if (t == STRING_BARE)
		{
			r = string_bare(b, 0);
		}
		else if (t == STRING_DQ)
		{
			r = string_dq(b, 0);
		}
		else if (t == STRING_LIST)
		{
			r = string_list(b, 0);
		}
		else if (t == STRING_SQ)
		{
			r = string_sq(b, 0);
		}
		else if (t == STRING_XQ)
		{
			r = string_xq(b, 0);
		}
		else if (t == SUB_CALL_EXPR)
		{
			r = sub_call_expr(b, 0);
		}
		else if (t == SUB_DECLARATION)
		{
			r = sub_declaration(b, 0);
		}
		else if (t == SUB_DEFINITION)
		{
			r = sub_definition(b, 0);
		}
		else if (t == SUB_EXPR)
		{
			r = sub_expr(b, 0);
		}
		else if (t == SUB_SIGNATURE_CONTENT)
		{
			r = sub_signature_content(b, 0);
		}
		else if (t == SUB_SIGNATURE_ELEMENT_IGNORE)
		{
			r = sub_signature_element_ignore(b, 0);
		}
		else if (t == SUFF_PP_EXPR)
		{
			r = expr(b, 0, 20);
		}
		else if (t == TAG_SCALAR)
		{
			r = tag_scalar(b, 0);
		}
		else if (t == TERM_EXPR)
		{
			r = term_expr(b, 0);
		}
		else if (t == TR_MODIFIERS)
		{
			r = tr_modifiers(b, 0);
		}
		else if (t == TR_REGEX)
		{
			r = tr_regex(b, 0);
		}
		else if (t == TR_REPLACEMENTLIST)
		{
			r = tr_replacementlist(b, 0);
		}
		else if (t == TR_SEARCHLIST)
		{
			r = tr_searchlist(b, 0);
		}
		else if (t == TRENAR_EXPR)
		{
			r = expr(b, 0, 5);
		}
		else if (t == UNCONDITIONAL_BLOCK)
		{
			r = unconditional_block(b, 0);
		}
		else if (t == UNDEF_EXPR)
		{
			r = undef_expr(b, 0);
		}
		else if (t == UNLESS_COMPOUND)
		{
			r = unless_compound(b, 0);
		}
		else if (t == UNLESS_STATEMENT_MODIFIER)
		{
			r = unless_statement_modifier(b, 0);
		}
		else if (t == UNTIL_COMPOUND)
		{
			r = until_compound(b, 0);
		}
		else if (t == UNTIL_STATEMENT_MODIFIER)
		{
			r = until_statement_modifier(b, 0);
		}
		else if (t == USE_STATEMENT)
		{
			r = use_statement(b, 0);
		}
		else if (t == USE_STATEMENT_CONSTANT)
		{
			r = use_statement_constant(b, 0);
		}
		else if (t == USE_VARS_STATEMENT)
		{
			r = use_vars_statement(b, 0);
		}
		else if (t == VARIABLE_DECLARATION_GLOBAL)
		{
			r = variable_declaration_global(b, 0);
		}
		else if (t == VARIABLE_DECLARATION_LEXICAL)
		{
			r = variable_declaration_lexical(b, 0);
		}
		else if (t == VARIABLE_DECLARATION_LOCAL)
		{
			r = variable_declaration_local(b, 0);
		}
		else if (t == VARIABLE_DECLARATION_WRAPPER)
		{
			r = variable_declaration_wrapper(b, 0);
		}
		else if (t == WHEN_COMPOUND)
		{
			r = when_compound(b, 0);
		}
		else if (t == WHEN_STATEMENT_MODIFIER)
		{
			r = when_statement_modifier(b, 0);
		}
		else if (t == WHILE_COMPOUND)
		{
			r = while_compound(b, 0);
		}
		else if (t == WHILE_STATEMENT_MODIFIER)
		{
			r = while_statement_modifier(b, 0);
		}
		else
		{
			r = parse_root_(t, b, 0);
		}
		exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
	}

	protected boolean parse_root_(IElementType t, PsiBuilder b, int l)
	{
		return root(b, l + 1);
	}
}
