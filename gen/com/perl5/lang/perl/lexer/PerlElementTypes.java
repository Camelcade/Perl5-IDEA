// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.lexer;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.parser.elementTypes.PerlElementTypeFactory;
import com.perl5.lang.perl.psi.impl.*;

public interface PerlElementTypes
{

	IElementType ADD_EXPR = PerlElementTypeFactory.getElementType("ADD_EXPR");
	IElementType AND_EXPR = PerlElementTypeFactory.getElementType("AND_EXPR");
	IElementType ANNOTATION = PerlElementTypeFactory.getElementType("ANNOTATION");
	IElementType ANNOTATION_ABSTRACT = PerlElementTypeFactory.getElementType("ANNOTATION_ABSTRACT");
	IElementType ANNOTATION_DEPRECATED = PerlElementTypeFactory.getElementType("ANNOTATION_DEPRECATED");
	IElementType ANNOTATION_INCOMPLETE = PerlElementTypeFactory.getElementType("ANNOTATION_INCOMPLETE");
	IElementType ANNOTATION_METHOD = PerlElementTypeFactory.getElementType("ANNOTATION_METHOD");
	IElementType ANNOTATION_OVERRIDE = PerlElementTypeFactory.getElementType("ANNOTATION_OVERRIDE");
	IElementType ANNOTATION_RETURNS_ARRAYREF = PerlElementTypeFactory.getElementType("ANNOTATION_RETURNS_ARRAYREF");
	IElementType ANNOTATION_RETURNS_HASHREF = PerlElementTypeFactory.getElementType("ANNOTATION_RETURNS_HASHREF");
	IElementType ANNOTATION_RETURNS_REF = PerlElementTypeFactory.getElementType("ANNOTATION_RETURNS_REF");
	IElementType ANNOTATION_UNKNOWN = PerlElementTypeFactory.getElementType("ANNOTATION_UNKNOWN");
	IElementType ANON_ARRAY = PerlElementTypeFactory.getElementType("ANON_ARRAY");
	IElementType ANON_ARRAY_ELEMENT = PerlElementTypeFactory.getElementType("ANON_ARRAY_ELEMENT");
	IElementType ANON_HASH = PerlElementTypeFactory.getElementType("ANON_HASH");
	IElementType ANON_SUB = PerlElementTypeFactory.getElementType("ANON_SUB");
	IElementType ARRAY_ARRAY_SLICE = PerlElementTypeFactory.getElementType("ARRAY_ARRAY_SLICE");
	IElementType ARRAY_CAST_EXPR = PerlElementTypeFactory.getElementType("ARRAY_CAST_EXPR");
	IElementType ARRAY_HASH_SLICE = PerlElementTypeFactory.getElementType("ARRAY_HASH_SLICE");
	IElementType ARRAY_INDEX = PerlElementTypeFactory.getElementType("ARRAY_INDEX");
	IElementType ARRAY_INDEX_VARIABLE = PerlElementTypeFactory.getElementType("ARRAY_INDEX_VARIABLE");
	IElementType ARRAY_VARIABLE = PerlElementTypeFactory.getElementType("ARRAY_VARIABLE");
	IElementType ASSIGN_EXPR = PerlElementTypeFactory.getElementType("ASSIGN_EXPR");
	IElementType ATTRIBUTE = PerlElementTypeFactory.getElementType("ATTRIBUTE");
	IElementType BITWISE_AND_EXPR = PerlElementTypeFactory.getElementType("BITWISE_AND_EXPR");
	IElementType BITWISE_OR_XOR_EXPR = PerlElementTypeFactory.getElementType("BITWISE_OR_XOR_EXPR");
	IElementType BLOCK = PerlElementTypeFactory.getElementType("BLOCK");
	IElementType CALL_ARGUMENTS = PerlElementTypeFactory.getElementType("CALL_ARGUMENTS");
	IElementType CODE_CAST_EXPR = PerlElementTypeFactory.getElementType("CODE_CAST_EXPR");
	IElementType CODE_VARIABLE = PerlElementTypeFactory.getElementType("CODE_VARIABLE");
	IElementType COMMA_SEQUENCE_EXPR = PerlElementTypeFactory.getElementType("COMMA_SEQUENCE_EXPR");
	IElementType COMPARE_EXPR = PerlElementTypeFactory.getElementType("COMPARE_EXPR");
	IElementType COMPILE_REGEX = PerlElementTypeFactory.getElementType("COMPILE_REGEX");
	IElementType CONDITIONAL_BLOCK = PerlElementTypeFactory.getElementType("CONDITIONAL_BLOCK");
	IElementType CONDITIONAL_BLOCK_WHILE = PerlElementTypeFactory.getElementType("CONDITIONAL_BLOCK_WHILE");
	IElementType CONDITION_STATEMENT = PerlElementTypeFactory.getElementType("CONDITION_STATEMENT");
	IElementType CONDITION_STATEMENT_WHILE = PerlElementTypeFactory.getElementType("CONDITION_STATEMENT_WHILE");
	IElementType CONSTANTS_BLOCK = PerlElementTypeFactory.getElementType("CONSTANTS_BLOCK");
	IElementType CONSTANT_DEFINITION = PerlElementTypeFactory.getElementType("CONSTANT_DEFINITION");
	IElementType CONSTANT_NAME = PerlElementTypeFactory.getElementType("CONSTANT_NAME");
	IElementType CONTINUE_BLOCK = PerlElementTypeFactory.getElementType("CONTINUE_BLOCK");
	IElementType DEFAULT_COMPOUND = PerlElementTypeFactory.getElementType("DEFAULT_COMPOUND");
	IElementType DEREF_EXPR = PerlElementTypeFactory.getElementType("DEREF_EXPR");
	IElementType DO_EXPR = PerlElementTypeFactory.getElementType("DO_EXPR");
	IElementType EQUAL_EXPR = PerlElementTypeFactory.getElementType("EQUAL_EXPR");
	IElementType EVAL_EXPR = PerlElementTypeFactory.getElementType("EVAL_EXPR");
	IElementType EXPR = PerlElementTypeFactory.getElementType("EXPR");
	IElementType FILE_READ_EXPR = PerlElementTypeFactory.getElementType("FILE_READ_EXPR");
	IElementType FILE_READ_FORCED_EXPR = PerlElementTypeFactory.getElementType("FILE_READ_FORCED_EXPR");
	IElementType FLIPFLOP_EXPR = PerlElementTypeFactory.getElementType("FLIPFLOP_EXPR");
	IElementType FOREACH_COMPOUND = PerlElementTypeFactory.getElementType("FOREACH_COMPOUND");
	IElementType FOREACH_STATEMENT_MODIFIER = PerlElementTypeFactory.getElementType("FOREACH_STATEMENT_MODIFIER");
	IElementType FORMAT_DEFINITION = PerlElementTypeFactory.getElementType("FORMAT_DEFINITION");
	IElementType FOR_COMPOUND = PerlElementTypeFactory.getElementType("FOR_COMPOUND");
	IElementType FOR_ITERATOR = PerlElementTypeFactory.getElementType("FOR_ITERATOR");
	IElementType FOR_ITERATOR_STATEMENT = PerlElementTypeFactory.getElementType("FOR_ITERATOR_STATEMENT");
	IElementType FOR_LIST_EPXR = PerlElementTypeFactory.getElementType("FOR_LIST_EPXR");
	IElementType FOR_LIST_STATEMENT = PerlElementTypeFactory.getElementType("FOR_LIST_STATEMENT");
	IElementType FOR_STATEMENT_MODIFIER = PerlElementTypeFactory.getElementType("FOR_STATEMENT_MODIFIER");
	IElementType FUNC_DEFINITION = PerlElementTypeFactory.getElementType("FUNC_DEFINITION");
	IElementType FUNC_SIGNATURE_CONTENT = PerlElementTypeFactory.getElementType("FUNC_SIGNATURE_CONTENT");
	IElementType GIVEN_COMPOUND = PerlElementTypeFactory.getElementType("GIVEN_COMPOUND");
	IElementType GLOB_CAST_EXPR = PerlElementTypeFactory.getElementType("GLOB_CAST_EXPR");
	IElementType GLOB_SLOT = PerlElementTypeFactory.getElementType("GLOB_SLOT");
	IElementType GLOB_VARIABLE = PerlElementTypeFactory.getElementType("GLOB_VARIABLE");
	IElementType GOTO_EXPR = PerlElementTypeFactory.getElementType("GOTO_EXPR");
	IElementType GREP_EXPR = PerlElementTypeFactory.getElementType("GREP_EXPR");
	IElementType HASH_CAST_EXPR = PerlElementTypeFactory.getElementType("HASH_CAST_EXPR");
	IElementType HASH_INDEX = PerlElementTypeFactory.getElementType("HASH_INDEX");
	IElementType HASH_VARIABLE = PerlElementTypeFactory.getElementType("HASH_VARIABLE");
	IElementType HEREDOC_OPENER = PerlElementTypeFactory.getElementType("HEREDOC_OPENER");
	IElementType IF_COMPOUND = PerlElementTypeFactory.getElementType("IF_COMPOUND");
	IElementType IF_STATEMENT_MODIFIER = PerlElementTypeFactory.getElementType("IF_STATEMENT_MODIFIER");
	IElementType LABEL_DECLARATION = PerlElementTypeFactory.getElementType("LABEL_DECLARATION");
	IElementType LAST_EXPR = PerlElementTypeFactory.getElementType("LAST_EXPR");
	IElementType LP_AND_EXPR = PerlElementTypeFactory.getElementType("LP_AND_EXPR");
	IElementType LP_NOT_EXPR = PerlElementTypeFactory.getElementType("LP_NOT_EXPR");
	IElementType LP_OR_XOR_EXPR = PerlElementTypeFactory.getElementType("LP_OR_XOR_EXPR");
	IElementType MAP_EXPR = PerlElementTypeFactory.getElementType("MAP_EXPR");
	IElementType MATCH_REGEX = PerlElementTypeFactory.getElementType("MATCH_REGEX");
	IElementType METHOD = PerlElementTypeFactory.getElementType("METHOD");
	IElementType METHOD_DEFINITION = PerlElementTypeFactory.getElementType("METHOD_DEFINITION");
	IElementType METHOD_SIGNATURE_CONTENT = PerlElementTypeFactory.getElementType("METHOD_SIGNATURE_CONTENT");
	IElementType METHOD_SIGNATURE_INVOCANT = PerlElementTypeFactory.getElementType("METHOD_SIGNATURE_INVOCANT");
	IElementType MUL_EXPR = PerlElementTypeFactory.getElementType("MUL_EXPR");
	IElementType NAMED_BLOCK = PerlElementTypeFactory.getElementType("NAMED_BLOCK");
	IElementType NAMED_LIST_EXPR = PerlElementTypeFactory.getElementType("NAMED_LIST_EXPR");
	IElementType NAMED_UNARY_EXPR = PerlElementTypeFactory.getElementType("NAMED_UNARY_EXPR");
	IElementType NAMESPACE_CONTENT = PerlElementTypeFactory.getElementType("NAMESPACE_CONTENT");
	IElementType NAMESPACE_DEFINITION = PerlElementTypeFactory.getElementType("NAMESPACE_DEFINITION");
	IElementType NAMESPACE_EXPR = PerlElementTypeFactory.getElementType("NAMESPACE_EXPR");
	IElementType NESTED_CALL = PerlElementTypeFactory.getElementType("NESTED_CALL");
	IElementType NESTED_CALL_ARGUMENTS = PerlElementTypeFactory.getElementType("NESTED_CALL_ARGUMENTS");
	IElementType NEXT_EXPR = PerlElementTypeFactory.getElementType("NEXT_EXPR");
	IElementType NO_STATEMENT = PerlElementTypeFactory.getElementType("NO_STATEMENT");
	IElementType NUMBER_CONSTANT = PerlElementTypeFactory.getElementType("NUMBER_CONSTANT");
	IElementType NYI_STATEMENT = PerlElementTypeFactory.getElementType("NYI_STATEMENT");
	IElementType OR_EXPR = PerlElementTypeFactory.getElementType("OR_EXPR");
	IElementType PARENTHESISED_EXPR = PerlElementTypeFactory.getElementType("PARENTHESISED_EXPR");
	IElementType PERL_HANDLE_EXPR = PerlElementTypeFactory.getElementType("PERL_HANDLE_EXPR");
	IElementType PERL_REGEX = PerlElementTypeFactory.getElementType("PERL_REGEX");
	IElementType PERL_REGEX_EX = PerlElementTypeFactory.getElementType("PERL_REGEX_EX");
	IElementType PERL_REGEX_MODIFIERS = PerlElementTypeFactory.getElementType("PERL_REGEX_MODIFIERS");
	IElementType POW_EXPR = PerlElementTypeFactory.getElementType("POW_EXPR");
	IElementType PREFIX_MINUS_AS_STRING_EXPR = PerlElementTypeFactory.getElementType("PREFIX_MINUS_AS_STRING_EXPR");
	IElementType PREFIX_UNARY_EXPR = PerlElementTypeFactory.getElementType("PREFIX_UNARY_EXPR");
	IElementType PREF_MM_EXPR = PerlElementTypeFactory.getElementType("PREF_MM_EXPR");
	IElementType PREF_PP_EXPR = PerlElementTypeFactory.getElementType("PREF_PP_EXPR");
	IElementType PRINT_EXPR = PerlElementTypeFactory.getElementType("PRINT_EXPR");
	IElementType PRINT_HANDLE = PerlElementTypeFactory.getElementType("PRINT_HANDLE");
	IElementType READ_HANDLE = PerlElementTypeFactory.getElementType("READ_HANDLE");
	IElementType REDO_EXPR = PerlElementTypeFactory.getElementType("REDO_EXPR");
	IElementType REF_EXPR = PerlElementTypeFactory.getElementType("REF_EXPR");
	IElementType REGEX_EXPR = PerlElementTypeFactory.getElementType("REGEX_EXPR");
	IElementType REPLACEMENT_REGEX = PerlElementTypeFactory.getElementType("REPLACEMENT_REGEX");
	IElementType REQUIRE_EXPR = PerlElementTypeFactory.getElementType("REQUIRE_EXPR");
	IElementType RETURN_EXPR = PerlElementTypeFactory.getElementType("RETURN_EXPR");
	IElementType SCALAR_ARRAY_ELEMENT = PerlElementTypeFactory.getElementType("SCALAR_ARRAY_ELEMENT");
	IElementType SCALAR_CALL = PerlElementTypeFactory.getElementType("SCALAR_CALL");
	IElementType SCALAR_CAST_EXPR = PerlElementTypeFactory.getElementType("SCALAR_CAST_EXPR");
	IElementType SCALAR_HASH_ELEMENT = PerlElementTypeFactory.getElementType("SCALAR_HASH_ELEMENT");
	IElementType SCALAR_INDEX_CAST_EXPR = PerlElementTypeFactory.getElementType("SCALAR_INDEX_CAST_EXPR");
	IElementType SCALAR_VARIABLE = PerlElementTypeFactory.getElementType("SCALAR_VARIABLE");
	IElementType SHIFT_EXPR = PerlElementTypeFactory.getElementType("SHIFT_EXPR");
	IElementType SORT_EXPR = PerlElementTypeFactory.getElementType("SORT_EXPR");
	IElementType STATEMENT = PerlElementTypeFactory.getElementType("STATEMENT");
	IElementType STATEMENT_MODIFIER = PerlElementTypeFactory.getElementType("STATEMENT_MODIFIER");
	IElementType STRING_BARE = PerlElementTypeFactory.getElementType("STRING_BARE");
	IElementType STRING_DQ = PerlElementTypeFactory.getElementType("STRING_DQ");
	IElementType STRING_LIST = PerlElementTypeFactory.getElementType("STRING_LIST");
	IElementType STRING_SQ = PerlElementTypeFactory.getElementType("STRING_SQ");
	IElementType STRING_XQ = PerlElementTypeFactory.getElementType("STRING_XQ");
	IElementType SUB_CALL_EXPR = PerlElementTypeFactory.getElementType("SUB_CALL_EXPR");
	IElementType SUB_DECLARATION = PerlElementTypeFactory.getElementType("SUB_DECLARATION");
	IElementType SUB_DEFINITION = PerlElementTypeFactory.getElementType("SUB_DEFINITION");
	IElementType SUB_EXPR = PerlElementTypeFactory.getElementType("SUB_EXPR");
	IElementType SUB_SIGNATURE_CONTENT = PerlElementTypeFactory.getElementType("SUB_SIGNATURE_CONTENT");
	IElementType SUB_SIGNATURE_ELEMENT_IGNORE = PerlElementTypeFactory.getElementType("SUB_SIGNATURE_ELEMENT_IGNORE");
	IElementType SUFF_PP_EXPR = PerlElementTypeFactory.getElementType("SUFF_PP_EXPR");
	IElementType TAG_SCALAR = PerlElementTypeFactory.getElementType("TAG_SCALAR");
	IElementType TERM_EXPR = PerlElementTypeFactory.getElementType("TERM_EXPR");
	IElementType TRENAR_EXPR = PerlElementTypeFactory.getElementType("TRENAR_EXPR");
	IElementType TR_MODIFIERS = PerlElementTypeFactory.getElementType("TR_MODIFIERS");
	IElementType TR_REGEX = PerlElementTypeFactory.getElementType("TR_REGEX");
	IElementType TR_REPLACEMENTLIST = PerlElementTypeFactory.getElementType("TR_REPLACEMENTLIST");
	IElementType TR_SEARCHLIST = PerlElementTypeFactory.getElementType("TR_SEARCHLIST");
	IElementType UNCONDITIONAL_BLOCK = PerlElementTypeFactory.getElementType("UNCONDITIONAL_BLOCK");
	IElementType UNDEF_EXPR = PerlElementTypeFactory.getElementType("UNDEF_EXPR");
	IElementType UNLESS_COMPOUND = PerlElementTypeFactory.getElementType("UNLESS_COMPOUND");
	IElementType UNLESS_STATEMENT_MODIFIER = PerlElementTypeFactory.getElementType("UNLESS_STATEMENT_MODIFIER");
	IElementType UNTIL_COMPOUND = PerlElementTypeFactory.getElementType("UNTIL_COMPOUND");
	IElementType UNTIL_STATEMENT_MODIFIER = PerlElementTypeFactory.getElementType("UNTIL_STATEMENT_MODIFIER");
	IElementType USE_STATEMENT = PerlElementTypeFactory.getElementType("USE_STATEMENT");
	IElementType USE_STATEMENT_CONSTANT = PerlElementTypeFactory.getElementType("USE_STATEMENT_CONSTANT");
	IElementType USE_VARS_STATEMENT = PerlElementTypeFactory.getElementType("USE_VARS_STATEMENT");
	IElementType VARIABLE_DECLARATION_GLOBAL = PerlElementTypeFactory.getElementType("VARIABLE_DECLARATION_GLOBAL");
	IElementType VARIABLE_DECLARATION_LEXICAL = PerlElementTypeFactory.getElementType("VARIABLE_DECLARATION_LEXICAL");
	IElementType VARIABLE_DECLARATION_LOCAL = PerlElementTypeFactory.getElementType("VARIABLE_DECLARATION_LOCAL");
	IElementType VARIABLE_DECLARATION_WRAPPER = PerlElementTypeFactory.getElementType("VARIABLE_DECLARATION_WRAPPER");
	IElementType WHEN_COMPOUND = PerlElementTypeFactory.getElementType("WHEN_COMPOUND");
	IElementType WHEN_STATEMENT_MODIFIER = PerlElementTypeFactory.getElementType("WHEN_STATEMENT_MODIFIER");
	IElementType WHILE_COMPOUND = PerlElementTypeFactory.getElementType("WHILE_COMPOUND");
	IElementType WHILE_STATEMENT_MODIFIER = PerlElementTypeFactory.getElementType("WHILE_STATEMENT_MODIFIER");

	IElementType ANNOTATION_ABSTRACT_KEY = PerlElementTypeFactory.getTokenType("ANNOTATION_ABSTRACT");
	IElementType ANNOTATION_DEPRECATED_KEY = PerlElementTypeFactory.getTokenType("ANNOTATION_DEPRECATED");
	IElementType ANNOTATION_METHOD_KEY = PerlElementTypeFactory.getTokenType("ANNOTATION_METHOD");
	IElementType ANNOTATION_OVERRIDE_KEY = PerlElementTypeFactory.getTokenType("ANNOTATION_OVERRIDE");
	IElementType ANNOTATION_PREFIX = PerlElementTypeFactory.getTokenType("ANNOTATION_PREFIX");
	IElementType ANNOTATION_RETURNS_KEY = PerlElementTypeFactory.getTokenType("ANNOTATION_RETURNS");
	IElementType ANNOTATION_UNKNOWN_KEY = PerlElementTypeFactory.getTokenType("ANNOTATION_UNKNOWN");
	IElementType BLOCK_NAME = PerlElementTypeFactory.getTokenType("BLOCK_NAME");
	IElementType COLON = PerlElementTypeFactory.getTokenType(":");
	IElementType COMMENT_BLOCK = PerlElementTypeFactory.getTokenType("COMMENT_BLOCK");
	IElementType COMMENT_LINE = PerlElementTypeFactory.getTokenType("COMMENT_LINE");
	IElementType FORMAT = PerlElementTypeFactory.getTokenType("FORMAT");
	IElementType FORMAT_TERMINATOR = PerlElementTypeFactory.getTokenType("FORMAT_TERMINATOR");
	IElementType HANDLE = PerlElementTypeFactory.getTokenType("HANDLE");
	IElementType HEREDOC = PerlElementTypeFactory.getTokenType("HEREDOC");
	IElementType HEREDOC_END = PerlElementTypeFactory.getTokenType("HEREDOC_END");
	IElementType HEREDOC_PSEUDO_QUOTE = PerlElementTypeFactory.getTokenType("HEREDOC_PSEUDO_QUOTE");
	IElementType HEREDOC_QQ = PerlElementTypeFactory.getTokenType("HEREDOC_QQ");
	IElementType HEREDOC_QX = PerlElementTypeFactory.getTokenType("HEREDOC_QX");
	IElementType IDENTIFIER = PerlElementTypeFactory.getTokenType("IDENTIFIER");
	IElementType LABEL = PerlElementTypeFactory.getTokenType("LABEL");
	IElementType LEFT_ANGLE = PerlElementTypeFactory.getTokenType("LEFT_ANGLE");
	IElementType LEFT_BRACE = PerlElementTypeFactory.getTokenType("LEFT_BRACE");
	IElementType LEFT_BRACKET = PerlElementTypeFactory.getTokenType("LEFT_BRACKET");
	IElementType LEFT_PAREN = PerlElementTypeFactory.getTokenType("LEFT_PAREN");
	IElementType NUMBER = PerlElementTypeFactory.getTokenType("NUMBER");
	IElementType NUMBER_SIMPLE = PerlElementTypeFactory.getTokenType("NUMBER_SIMPLE");
	IElementType NUMBER_VERSION = PerlElementTypeFactory.getTokenType("NUMBER_VERSION");
	IElementType OPERATOR_AND = PerlElementTypeFactory.getTokenType("&&");
	IElementType OPERATOR_AND_ASSIGN = PerlElementTypeFactory.getTokenType("&&=");
	IElementType OPERATOR_AND_LP = PerlElementTypeFactory.getTokenType("and");
	IElementType OPERATOR_ASSIGN = PerlElementTypeFactory.getTokenType("=");
	IElementType OPERATOR_BITWISE_AND = PerlElementTypeFactory.getTokenType("&");
	IElementType OPERATOR_BITWISE_AND_ASSIGN = PerlElementTypeFactory.getTokenType("&=");
	IElementType OPERATOR_BITWISE_NOT = PerlElementTypeFactory.getTokenType("~");
	IElementType OPERATOR_BITWISE_OR = PerlElementTypeFactory.getTokenType("|");
	IElementType OPERATOR_BITWISE_OR_ASSIGN = PerlElementTypeFactory.getTokenType("|=");
	IElementType OPERATOR_BITWISE_XOR = PerlElementTypeFactory.getTokenType("^");
	IElementType OPERATOR_BITWISE_XOR_ASSIGN = PerlElementTypeFactory.getTokenType("^=");
	IElementType OPERATOR_CMP_NUMERIC = PerlElementTypeFactory.getTokenType("<=>");
	IElementType OPERATOR_CMP_STR = PerlElementTypeFactory.getTokenType("cmp");
	IElementType OPERATOR_COMMA = PerlElementTypeFactory.getTokenType(",");
	IElementType OPERATOR_COMMA_ARROW = PerlElementTypeFactory.getTokenType("=>");
	IElementType OPERATOR_CONCAT = PerlElementTypeFactory.getTokenType(".");
	IElementType OPERATOR_CONCAT_ASSIGN = PerlElementTypeFactory.getTokenType(".=");
	IElementType OPERATOR_DEREFERENCE = PerlElementTypeFactory.getTokenType("->");
	IElementType OPERATOR_DIV = PerlElementTypeFactory.getTokenType("/");
	IElementType OPERATOR_DIV_ASSIGN = PerlElementTypeFactory.getTokenType("/=");
	IElementType OPERATOR_EQ_NUMERIC = PerlElementTypeFactory.getTokenType("==");
	IElementType OPERATOR_EQ_STR = PerlElementTypeFactory.getTokenType("eq");
	IElementType OPERATOR_FILETEST = PerlElementTypeFactory.getTokenType("OPERATOR_FILETEST");
	IElementType OPERATOR_FLIP_FLOP = PerlElementTypeFactory.getTokenType("..");
	IElementType OPERATOR_GE_NUMERIC = PerlElementTypeFactory.getTokenType(">=");
	IElementType OPERATOR_GE_STR = PerlElementTypeFactory.getTokenType("ge");
	IElementType OPERATOR_GT_NUMERIC = PerlElementTypeFactory.getTokenType(">");
	IElementType OPERATOR_GT_STR = PerlElementTypeFactory.getTokenType("gt");
	IElementType OPERATOR_HELLIP = PerlElementTypeFactory.getTokenType("...");
	IElementType OPERATOR_HEREDOC = PerlElementTypeFactory.getTokenType("OPERATOR_HEREDOC");
	IElementType OPERATOR_LE_NUMERIC = PerlElementTypeFactory.getTokenType("<=");
	IElementType OPERATOR_LE_STR = PerlElementTypeFactory.getTokenType("le");
	IElementType OPERATOR_LT_NUMERIC = PerlElementTypeFactory.getTokenType("<");
	IElementType OPERATOR_LT_STR = PerlElementTypeFactory.getTokenType("lt");
	IElementType OPERATOR_MINUS = PerlElementTypeFactory.getTokenType("-");
	IElementType OPERATOR_MINUS_ASSIGN = PerlElementTypeFactory.getTokenType("-=");
	IElementType OPERATOR_MINUS_MINUS = PerlElementTypeFactory.getTokenType("--");
	IElementType OPERATOR_MINUS_UNARY = PerlElementTypeFactory.getTokenType("MINUS_UNARY");
	IElementType OPERATOR_MOD = PerlElementTypeFactory.getTokenType("%");
	IElementType OPERATOR_MOD_ASSIGN = PerlElementTypeFactory.getTokenType("%=");
	IElementType OPERATOR_MUL = PerlElementTypeFactory.getTokenType("*");
	IElementType OPERATOR_MUL_ASSIGN = PerlElementTypeFactory.getTokenType("*=");
	IElementType OPERATOR_NE_NUMERIC = PerlElementTypeFactory.getTokenType("!=");
	IElementType OPERATOR_NE_STR = PerlElementTypeFactory.getTokenType("ne");
	IElementType OPERATOR_NOT = PerlElementTypeFactory.getTokenType("!");
	IElementType OPERATOR_NOT_LP = PerlElementTypeFactory.getTokenType("not");
	IElementType OPERATOR_NOT_RE = PerlElementTypeFactory.getTokenType("!~");
	IElementType OPERATOR_OR = PerlElementTypeFactory.getTokenType("||");
	IElementType OPERATOR_OR_ASSIGN = PerlElementTypeFactory.getTokenType("||=");
	IElementType OPERATOR_OR_DEFINED = PerlElementTypeFactory.getTokenType("//");
	IElementType OPERATOR_OR_DEFINED_ASSIGN = PerlElementTypeFactory.getTokenType("//=");
	IElementType OPERATOR_OR_LP = PerlElementTypeFactory.getTokenType("or");
	IElementType OPERATOR_PLUS = PerlElementTypeFactory.getTokenType("+");
	IElementType OPERATOR_PLUS_ASSIGN = PerlElementTypeFactory.getTokenType("+=");
	IElementType OPERATOR_PLUS_PLUS = PerlElementTypeFactory.getTokenType("++");
	IElementType OPERATOR_PLUS_UNARY = PerlElementTypeFactory.getTokenType("PLUS_UNARY");
	IElementType OPERATOR_POW = PerlElementTypeFactory.getTokenType("**");
	IElementType OPERATOR_POW_ASSIGN = PerlElementTypeFactory.getTokenType("**=");
	IElementType OPERATOR_RE = PerlElementTypeFactory.getTokenType("=~");
	IElementType OPERATOR_REFERENCE = PerlElementTypeFactory.getTokenType("\\\\");
	IElementType OPERATOR_SHIFT_LEFT = PerlElementTypeFactory.getTokenType("<<");
	IElementType OPERATOR_SHIFT_LEFT_ASSIGN = PerlElementTypeFactory.getTokenType("<<=");
	IElementType OPERATOR_SHIFT_RIGHT = PerlElementTypeFactory.getTokenType(">>");
	IElementType OPERATOR_SHIFT_RIGHT_ASSIGN = PerlElementTypeFactory.getTokenType(">>=");
	IElementType OPERATOR_SMARTMATCH = PerlElementTypeFactory.getTokenType("~~");
	IElementType OPERATOR_X = PerlElementTypeFactory.getTokenType("x");
	IElementType OPERATOR_XOR_LP = PerlElementTypeFactory.getTokenType("xor");
	IElementType OPERATOR_X_ASSIGN = PerlElementTypeFactory.getTokenType("x=");
	IElementType PACKAGE = PerlElementTypeFactory.getTokenType("PACKAGE");
	IElementType PACKAGE_CORE_IDENTIFIER = PerlElementTypeFactory.getTokenType("PACKAGE_CORE_IDENTIFIER");
	IElementType PACKAGE_IDENTIFIER = PerlElementTypeFactory.getTokenType("PACKAGE_IDENTIFIER");
	IElementType PACKAGE_PRAGMA_CONSTANT = PerlElementTypeFactory.getTokenType("PACKAGE_PRAGMA_CONSTANT");
	IElementType PACKAGE_PRAGMA_VARS = PerlElementTypeFactory.getTokenType("PACKAGE_PRAGMA_VARS");
	IElementType PARSABLE_STRING_USE_VARS = PerlElementTypeFactory.getTokenType("PARSABLE_STRING_USE_VARS");
	IElementType POD = PerlElementTypeFactory.getTokenType("POD");
	IElementType QUESTION = PerlElementTypeFactory.getTokenType("?");
	IElementType QUOTE_DOUBLE = PerlElementTypeFactory.getTokenType("QUOTE_DOUBLE");
	IElementType QUOTE_DOUBLE_CLOSE = PerlElementTypeFactory.getTokenType("QUOTE_DOUBLE_CLOSE");
	IElementType QUOTE_DOUBLE_OPEN = PerlElementTypeFactory.getTokenType("QUOTE_DOUBLE_OPEN");
	IElementType QUOTE_SINGLE = PerlElementTypeFactory.getTokenType("QUOTE_SINGLE");
	IElementType QUOTE_SINGLE_CLOSE = PerlElementTypeFactory.getTokenType("QUOTE_SINGLE_CLOSE");
	IElementType QUOTE_SINGLE_OPEN = PerlElementTypeFactory.getTokenType("QUOTE_SINGLE_OPEN");
	IElementType QUOTE_TICK = PerlElementTypeFactory.getTokenType("QUOTE_TICK");
	IElementType QUOTE_TICK_CLOSE = PerlElementTypeFactory.getTokenType("QUOTE_TICK_CLOSE");
	IElementType QUOTE_TICK_OPEN = PerlElementTypeFactory.getTokenType("QUOTE_TICK_OPEN");
	IElementType REGEX_MODIFIER = PerlElementTypeFactory.getTokenType("REGEX_MODIFIER");
	IElementType REGEX_QUOTE = PerlElementTypeFactory.getTokenType("REGEX_QUOTE");
	IElementType REGEX_QUOTE_CLOSE = PerlElementTypeFactory.getTokenType("REGEX_QUOTE_CLOSE");
	IElementType REGEX_QUOTE_E = PerlElementTypeFactory.getTokenType("REGEX_QUOTE_E");
	IElementType REGEX_QUOTE_OPEN = PerlElementTypeFactory.getTokenType("REGEX_QUOTE_OPEN");
	IElementType REGEX_QUOTE_OPEN_E = PerlElementTypeFactory.getTokenType("REGEX_QUOTE_OPEN_E");
	IElementType REGEX_QUOTE_OPEN_X = PerlElementTypeFactory.getTokenType("REGEX_QUOTE_OPEN_X");
	IElementType REGEX_TOKEN = PerlElementTypeFactory.getTokenType("REGEX_TOKEN");
	IElementType RESERVED_CONTINUE = PerlElementTypeFactory.getTokenType("continue");
	IElementType RESERVED_DEFAULT = PerlElementTypeFactory.getTokenType("default");
	IElementType RESERVED_DO = PerlElementTypeFactory.getTokenType("do");
	IElementType RESERVED_ELSE = PerlElementTypeFactory.getTokenType("else");
	IElementType RESERVED_ELSIF = PerlElementTypeFactory.getTokenType("elsif");
	IElementType RESERVED_EVAL = PerlElementTypeFactory.getTokenType("eval");
	IElementType RESERVED_FOR = PerlElementTypeFactory.getTokenType("for");
	IElementType RESERVED_FOREACH = PerlElementTypeFactory.getTokenType("foreach");
	IElementType RESERVED_FORMAT = PerlElementTypeFactory.getTokenType("format");
	IElementType RESERVED_FUNC = PerlElementTypeFactory.getTokenType("func");
	IElementType RESERVED_GIVEN = PerlElementTypeFactory.getTokenType("given");
	IElementType RESERVED_GOTO = PerlElementTypeFactory.getTokenType("goto");
	IElementType RESERVED_GREP = PerlElementTypeFactory.getTokenType("grep");
	IElementType RESERVED_IF = PerlElementTypeFactory.getTokenType("if");
	IElementType RESERVED_LAST = PerlElementTypeFactory.getTokenType("last");
	IElementType RESERVED_LOCAL = PerlElementTypeFactory.getTokenType("local");
	IElementType RESERVED_M = PerlElementTypeFactory.getTokenType("m");
	IElementType RESERVED_MAP = PerlElementTypeFactory.getTokenType("map");
	IElementType RESERVED_METHOD = PerlElementTypeFactory.getTokenType("method");
	IElementType RESERVED_MY = PerlElementTypeFactory.getTokenType("my");
	IElementType RESERVED_NEXT = PerlElementTypeFactory.getTokenType("next");
	IElementType RESERVED_NO = PerlElementTypeFactory.getTokenType("no");
	IElementType RESERVED_OUR = PerlElementTypeFactory.getTokenType("our");
	IElementType RESERVED_PACKAGE = PerlElementTypeFactory.getTokenType("package");
	IElementType RESERVED_PRINT = PerlElementTypeFactory.getTokenType("print");
	IElementType RESERVED_PRINTF = PerlElementTypeFactory.getTokenType("printf");
	IElementType RESERVED_Q = PerlElementTypeFactory.getTokenType("q");
	IElementType RESERVED_QQ = PerlElementTypeFactory.getTokenType("qq");
	IElementType RESERVED_QR = PerlElementTypeFactory.getTokenType("qr");
	IElementType RESERVED_QW = PerlElementTypeFactory.getTokenType("qw");
	IElementType RESERVED_QX = PerlElementTypeFactory.getTokenType("qx");
	IElementType RESERVED_REDO = PerlElementTypeFactory.getTokenType("redo");
	IElementType RESERVED_REQUIRE = PerlElementTypeFactory.getTokenType("require");
	IElementType RESERVED_RETURN = PerlElementTypeFactory.getTokenType("return");
	IElementType RESERVED_S = PerlElementTypeFactory.getTokenType("s");
	IElementType RESERVED_SAY = PerlElementTypeFactory.getTokenType("say");
	IElementType RESERVED_SORT = PerlElementTypeFactory.getTokenType("sort");
	IElementType RESERVED_STATE = PerlElementTypeFactory.getTokenType("state");
	IElementType RESERVED_SUB = PerlElementTypeFactory.getTokenType("sub");
	IElementType RESERVED_TR = PerlElementTypeFactory.getTokenType("tr");
	IElementType RESERVED_UNDEF = PerlElementTypeFactory.getTokenType("undef");
	IElementType RESERVED_UNLESS = PerlElementTypeFactory.getTokenType("unless");
	IElementType RESERVED_UNTIL = PerlElementTypeFactory.getTokenType("until");
	IElementType RESERVED_USE = PerlElementTypeFactory.getTokenType("use");
	IElementType RESERVED_WHEN = PerlElementTypeFactory.getTokenType("when");
	IElementType RESERVED_WHILE = PerlElementTypeFactory.getTokenType("while");
	IElementType RESERVED_Y = PerlElementTypeFactory.getTokenType("y");
	IElementType RIGHT_ANGLE = PerlElementTypeFactory.getTokenType("RIGHT_ANGLE");
	IElementType RIGHT_BRACE = PerlElementTypeFactory.getTokenType("RIGHT_BRACE");
	IElementType RIGHT_BRACKET = PerlElementTypeFactory.getTokenType("RIGHT_BRACKET");
	IElementType RIGHT_PAREN = PerlElementTypeFactory.getTokenType("RIGHT_PAREN");
	IElementType SEMICOLON = PerlElementTypeFactory.getTokenType(";");
	IElementType SIGIL_ARRAY = PerlElementTypeFactory.getTokenType("SIGIL_ARRAY");
	IElementType SIGIL_CODE = PerlElementTypeFactory.getTokenType("SIGIL_CODE");
	IElementType SIGIL_GLOB = PerlElementTypeFactory.getTokenType("SIGIL_GLOB");
	IElementType SIGIL_HASH = PerlElementTypeFactory.getTokenType("SIGIL_HASH");
	IElementType SIGIL_SCALAR = PerlElementTypeFactory.getTokenType("SIGIL_SCALAR");
	IElementType SIGIL_SCALAR_INDEX = PerlElementTypeFactory.getTokenType("SIGIL_SCALAR_INDEX");
	IElementType STRING_CONTENT = PerlElementTypeFactory.getTokenType("STRING_CONTENT");
	IElementType STRING_IDENTIFIER = PerlElementTypeFactory.getTokenType("STRING_IDENTIFIER");
	IElementType STRING_PACKAGE = PerlElementTypeFactory.getTokenType("STRING_PACKAGE");
	IElementType STRING_PLUS = PerlElementTypeFactory.getTokenType("STRING_PLUS");
	IElementType STRING_WHITESPACE = PerlElementTypeFactory.getTokenType("STRING_WHITESPACE");
	IElementType SUB = PerlElementTypeFactory.getTokenType("SUB");
	IElementType SUB_PROTOTYPE_TOKEN = PerlElementTypeFactory.getTokenType("SUB_PROTOTYPE_TOKEN");
	IElementType TAG = PerlElementTypeFactory.getTokenType("TAG");
	IElementType VARIABLE_NAME = PerlElementTypeFactory.getTokenType("VARIABLE_NAME");
	IElementType VERSION_ELEMENT = PerlElementTypeFactory.getTokenType("VERSION_ELEMENT");

	class Factory
	{
		public static PsiElement createElement(ASTNode node)
		{
			IElementType type = node.getElementType();
			if (type == ADD_EXPR)
			{
				return new PsiPerlAddExprImpl(node);
			}
			else if (type == AND_EXPR)
			{
				return new PsiPerlAndExprImpl(node);
			}
			else if (type == ANNOTATION)
			{
				return new PsiPerlAnnotationImpl(node);
			}
			else if (type == ANNOTATION_ABSTRACT)
			{
				return new PsiPerlAnnotationAbstractImpl(node);
			}
			else if (type == ANNOTATION_DEPRECATED)
			{
				return new PsiPerlAnnotationDeprecatedImpl(node);
			}
			else if (type == ANNOTATION_INCOMPLETE)
			{
				return new PsiPerlAnnotationIncompleteImpl(node);
			}
			else if (type == ANNOTATION_METHOD)
			{
				return new PsiPerlAnnotationMethodImpl(node);
			}
			else if (type == ANNOTATION_OVERRIDE)
			{
				return new PsiPerlAnnotationOverrideImpl(node);
			}
			else if (type == ANNOTATION_RETURNS_ARRAYREF)
			{
				return new PsiPerlAnnotationReturnsArrayrefImpl(node);
			}
			else if (type == ANNOTATION_RETURNS_HASHREF)
			{
				return new PsiPerlAnnotationReturnsHashrefImpl(node);
			}
			else if (type == ANNOTATION_RETURNS_REF)
			{
				return new PsiPerlAnnotationReturnsRefImpl(node);
			}
			else if (type == ANNOTATION_UNKNOWN)
			{
				return new PsiPerlAnnotationUnknownImpl(node);
			}
			else if (type == ANON_ARRAY)
			{
				return new PsiPerlAnonArrayImpl(node);
			}
			else if (type == ANON_ARRAY_ELEMENT)
			{
				return new PsiPerlAnonArrayElementImpl(node);
			}
			else if (type == ANON_HASH)
			{
				return new PsiPerlAnonHashImpl(node);
			}
			else if (type == ANON_SUB)
			{
				return new PsiPerlAnonSubImpl(node);
			}
			else if (type == ARRAY_ARRAY_SLICE)
			{
				return new PsiPerlArrayArraySliceImpl(node);
			}
			else if (type == ARRAY_CAST_EXPR)
			{
				return new PsiPerlArrayCastExprImpl(node);
			}
			else if (type == ARRAY_HASH_SLICE)
			{
				return new PsiPerlArrayHashSliceImpl(node);
			}
			else if (type == ARRAY_INDEX)
			{
				return new PsiPerlArrayIndexImpl(node);
			}
			else if (type == ARRAY_INDEX_VARIABLE)
			{
				return new PsiPerlArrayIndexVariableImpl(node);
			}
			else if (type == ARRAY_VARIABLE)
			{
				return new PsiPerlArrayVariableImpl(node);
			}
			else if (type == ASSIGN_EXPR)
			{
				return new PsiPerlAssignExprImpl(node);
			}
			else if (type == ATTRIBUTE)
			{
				return new PsiPerlAttributeImpl(node);
			}
			else if (type == BITWISE_AND_EXPR)
			{
				return new PsiPerlBitwiseAndExprImpl(node);
			}
			else if (type == BITWISE_OR_XOR_EXPR)
			{
				return new PsiPerlBitwiseOrXorExprImpl(node);
			}
			else if (type == BLOCK)
			{
				return new PsiPerlBlockImpl(node);
			}
			else if (type == CALL_ARGUMENTS)
			{
				return new PsiPerlCallArgumentsImpl(node);
			}
			else if (type == CODE_CAST_EXPR)
			{
				return new PsiPerlCodeCastExprImpl(node);
			}
			else if (type == CODE_VARIABLE)
			{
				return new PsiPerlCodeVariableImpl(node);
			}
			else if (type == COMMA_SEQUENCE_EXPR)
			{
				return new PsiPerlCommaSequenceExprImpl(node);
			}
			else if (type == COMPARE_EXPR)
			{
				return new PsiPerlCompareExprImpl(node);
			}
			else if (type == COMPILE_REGEX)
			{
				return new PsiPerlCompileRegexImpl(node);
			}
			else if (type == CONDITIONAL_BLOCK)
			{
				return new PsiPerlConditionalBlockImpl(node);
			}
			else if (type == CONDITIONAL_BLOCK_WHILE)
			{
				return new PsiPerlConditionalBlockWhileImpl(node);
			}
			else if (type == CONDITION_STATEMENT)
			{
				return new PsiPerlConditionStatementImpl(node);
			}
			else if (type == CONDITION_STATEMENT_WHILE)
			{
				return new PsiPerlConditionStatementWhileImpl(node);
			}
			else if (type == CONSTANTS_BLOCK)
			{
				return new PsiPerlConstantsBlockImpl(node);
			}
			else if (type == CONSTANT_DEFINITION)
			{
				return new PsiPerlConstantDefinitionImpl(node);
			}
			else if (type == CONSTANT_NAME)
			{
				return new PsiPerlConstantNameImpl(node);
			}
			else if (type == CONTINUE_BLOCK)
			{
				return new PsiPerlContinueBlockImpl(node);
			}
			else if (type == DEFAULT_COMPOUND)
			{
				return new PsiPerlDefaultCompoundImpl(node);
			}
			else if (type == DEREF_EXPR)
			{
				return new PsiPerlDerefExprImpl(node);
			}
			else if (type == DO_EXPR)
			{
				return new PsiPerlDoExprImpl(node);
			}
			else if (type == EQUAL_EXPR)
			{
				return new PsiPerlEqualExprImpl(node);
			}
			else if (type == EVAL_EXPR)
			{
				return new PsiPerlEvalExprImpl(node);
			}
			else if (type == EXPR)
			{
				return new PsiPerlExprImpl(node);
			}
			else if (type == FILE_READ_EXPR)
			{
				return new PsiPerlFileReadExprImpl(node);
			}
			else if (type == FILE_READ_FORCED_EXPR)
			{
				return new PsiPerlFileReadForcedExprImpl(node);
			}
			else if (type == FLIPFLOP_EXPR)
			{
				return new PsiPerlFlipflopExprImpl(node);
			}
			else if (type == FOREACH_COMPOUND)
			{
				return new PsiPerlForeachCompoundImpl(node);
			}
			else if (type == FOREACH_STATEMENT_MODIFIER)
			{
				return new PsiPerlForeachStatementModifierImpl(node);
			}
			else if (type == FORMAT_DEFINITION)
			{
				return new PsiPerlFormatDefinitionImpl(node);
			}
			else if (type == FOR_COMPOUND)
			{
				return new PsiPerlForCompoundImpl(node);
			}
			else if (type == FOR_ITERATOR)
			{
				return new PsiPerlForIteratorImpl(node);
			}
			else if (type == FOR_ITERATOR_STATEMENT)
			{
				return new PsiPerlForIteratorStatementImpl(node);
			}
			else if (type == FOR_LIST_EPXR)
			{
				return new PsiPerlForListEpxrImpl(node);
			}
			else if (type == FOR_LIST_STATEMENT)
			{
				return new PsiPerlForListStatementImpl(node);
			}
			else if (type == FOR_STATEMENT_MODIFIER)
			{
				return new PsiPerlForStatementModifierImpl(node);
			}
			else if (type == FUNC_DEFINITION)
			{
				return new PsiPerlFuncDefinitionImpl(node);
			}
			else if (type == FUNC_SIGNATURE_CONTENT)
			{
				return new PsiPerlFuncSignatureContentImpl(node);
			}
			else if (type == GIVEN_COMPOUND)
			{
				return new PsiPerlGivenCompoundImpl(node);
			}
			else if (type == GLOB_CAST_EXPR)
			{
				return new PsiPerlGlobCastExprImpl(node);
			}
			else if (type == GLOB_SLOT)
			{
				return new PsiPerlGlobSlotImpl(node);
			}
			else if (type == GLOB_VARIABLE)
			{
				return new PsiPerlGlobVariableImpl(node);
			}
			else if (type == GOTO_EXPR)
			{
				return new PsiPerlGotoExprImpl(node);
			}
			else if (type == GREP_EXPR)
			{
				return new PsiPerlGrepExprImpl(node);
			}
			else if (type == HASH_CAST_EXPR)
			{
				return new PsiPerlHashCastExprImpl(node);
			}
			else if (type == HASH_INDEX)
			{
				return new PsiPerlHashIndexImpl(node);
			}
			else if (type == HASH_VARIABLE)
			{
				return new PsiPerlHashVariableImpl(node);
			}
			else if (type == HEREDOC_OPENER)
			{
				return new PsiPerlHeredocOpenerImpl(node);
			}
			else if (type == IF_COMPOUND)
			{
				return new PsiPerlIfCompoundImpl(node);
			}
			else if (type == IF_STATEMENT_MODIFIER)
			{
				return new PsiPerlIfStatementModifierImpl(node);
			}
			else if (type == LABEL_DECLARATION)
			{
				return new PsiPerlLabelDeclarationImpl(node);
			}
			else if (type == LAST_EXPR)
			{
				return new PsiPerlLastExprImpl(node);
			}
			else if (type == LP_AND_EXPR)
			{
				return new PsiPerlLpAndExprImpl(node);
			}
			else if (type == LP_NOT_EXPR)
			{
				return new PsiPerlLpNotExprImpl(node);
			}
			else if (type == LP_OR_XOR_EXPR)
			{
				return new PsiPerlLpOrXorExprImpl(node);
			}
			else if (type == MAP_EXPR)
			{
				return new PsiPerlMapExprImpl(node);
			}
			else if (type == MATCH_REGEX)
			{
				return new PsiPerlMatchRegexImpl(node);
			}
			else if (type == METHOD)
			{
				return new PsiPerlMethodImpl(node);
			}
			else if (type == METHOD_DEFINITION)
			{
				return new PsiPerlMethodDefinitionImpl(node);
			}
			else if (type == METHOD_SIGNATURE_CONTENT)
			{
				return new PsiPerlMethodSignatureContentImpl(node);
			}
			else if (type == METHOD_SIGNATURE_INVOCANT)
			{
				return new PsiPerlMethodSignatureInvocantImpl(node);
			}
			else if (type == MUL_EXPR)
			{
				return new PsiPerlMulExprImpl(node);
			}
			else if (type == NAMED_BLOCK)
			{
				return new PsiPerlNamedBlockImpl(node);
			}
			else if (type == NAMED_LIST_EXPR)
			{
				return new PsiPerlNamedListExprImpl(node);
			}
			else if (type == NAMED_UNARY_EXPR)
			{
				return new PsiPerlNamedUnaryExprImpl(node);
			}
			else if (type == NAMESPACE_CONTENT)
			{
				return new PsiPerlNamespaceContentImpl(node);
			}
			else if (type == NAMESPACE_DEFINITION)
			{
				return new PsiPerlNamespaceDefinitionImpl(node);
			}
			else if (type == NAMESPACE_EXPR)
			{
				return new PsiPerlNamespaceExprImpl(node);
			}
			else if (type == NESTED_CALL)
			{
				return new PsiPerlNestedCallImpl(node);
			}
			else if (type == NESTED_CALL_ARGUMENTS)
			{
				return new PsiPerlNestedCallArgumentsImpl(node);
			}
			else if (type == NEXT_EXPR)
			{
				return new PsiPerlNextExprImpl(node);
			}
			else if (type == NO_STATEMENT)
			{
				return new PsiPerlNoStatementImpl(node);
			}
			else if (type == NUMBER_CONSTANT)
			{
				return new PsiPerlNumberConstantImpl(node);
			}
			else if (type == NYI_STATEMENT)
			{
				return new PsiPerlNyiStatementImpl(node);
			}
			else if (type == OR_EXPR)
			{
				return new PsiPerlOrExprImpl(node);
			}
			else if (type == PARENTHESISED_EXPR)
			{
				return new PsiPerlParenthesisedExprImpl(node);
			}
			else if (type == PERL_HANDLE_EXPR)
			{
				return new PsiPerlPerlHandleExprImpl(node);
			}
			else if (type == PERL_REGEX)
			{
				return new PsiPerlPerlRegexImpl(node);
			}
			else if (type == PERL_REGEX_EX)
			{
				return new PsiPerlPerlRegexExImpl(node);
			}
			else if (type == PERL_REGEX_MODIFIERS)
			{
				return new PsiPerlPerlRegexModifiersImpl(node);
			}
			else if (type == POW_EXPR)
			{
				return new PsiPerlPowExprImpl(node);
			}
			else if (type == PREFIX_MINUS_AS_STRING_EXPR)
			{
				return new PsiPerlPrefixMinusAsStringExprImpl(node);
			}
			else if (type == PREFIX_UNARY_EXPR)
			{
				return new PsiPerlPrefixUnaryExprImpl(node);
			}
			else if (type == PREF_MM_EXPR)
			{
				return new PsiPerlPrefMmExprImpl(node);
			}
			else if (type == PREF_PP_EXPR)
			{
				return new PsiPerlPrefPpExprImpl(node);
			}
			else if (type == PRINT_EXPR)
			{
				return new PsiPerlPrintExprImpl(node);
			}
			else if (type == PRINT_HANDLE)
			{
				return new PsiPerlPrintHandleImpl(node);
			}
			else if (type == READ_HANDLE)
			{
				return new PsiPerlReadHandleImpl(node);
			}
			else if (type == REDO_EXPR)
			{
				return new PsiPerlRedoExprImpl(node);
			}
			else if (type == REF_EXPR)
			{
				return new PsiPerlRefExprImpl(node);
			}
			else if (type == REGEX_EXPR)
			{
				return new PsiPerlRegexExprImpl(node);
			}
			else if (type == REPLACEMENT_REGEX)
			{
				return new PsiPerlReplacementRegexImpl(node);
			}
			else if (type == REQUIRE_EXPR)
			{
				return new PsiPerlRequireExprImpl(node);
			}
			else if (type == RETURN_EXPR)
			{
				return new PsiPerlReturnExprImpl(node);
			}
			else if (type == SCALAR_ARRAY_ELEMENT)
			{
				return new PsiPerlScalarArrayElementImpl(node);
			}
			else if (type == SCALAR_CALL)
			{
				return new PsiPerlScalarCallImpl(node);
			}
			else if (type == SCALAR_CAST_EXPR)
			{
				return new PsiPerlScalarCastExprImpl(node);
			}
			else if (type == SCALAR_HASH_ELEMENT)
			{
				return new PsiPerlScalarHashElementImpl(node);
			}
			else if (type == SCALAR_INDEX_CAST_EXPR)
			{
				return new PsiPerlScalarIndexCastExprImpl(node);
			}
			else if (type == SCALAR_VARIABLE)
			{
				return new PsiPerlScalarVariableImpl(node);
			}
			else if (type == SHIFT_EXPR)
			{
				return new PsiPerlShiftExprImpl(node);
			}
			else if (type == SORT_EXPR)
			{
				return new PsiPerlSortExprImpl(node);
			}
			else if (type == STATEMENT)
			{
				return new PsiPerlStatementImpl(node);
			}
			else if (type == STATEMENT_MODIFIER)
			{
				return new PsiPerlStatementModifierImpl(node);
			}
			else if (type == STRING_BARE)
			{
				return new PsiPerlStringBareImpl(node);
			}
			else if (type == STRING_DQ)
			{
				return new PsiPerlStringDqImpl(node);
			}
			else if (type == STRING_LIST)
			{
				return new PsiPerlStringListImpl(node);
			}
			else if (type == STRING_SQ)
			{
				return new PsiPerlStringSqImpl(node);
			}
			else if (type == STRING_XQ)
			{
				return new PsiPerlStringXqImpl(node);
			}
			else if (type == SUB_CALL_EXPR)
			{
				return new PsiPerlSubCallExprImpl(node);
			}
			else if (type == SUB_DECLARATION)
			{
				return new PsiPerlSubDeclarationImpl(node);
			}
			else if (type == SUB_DEFINITION)
			{
				return new PsiPerlSubDefinitionImpl(node);
			}
			else if (type == SUB_EXPR)
			{
				return new PsiPerlSubExprImpl(node);
			}
			else if (type == SUB_SIGNATURE_CONTENT)
			{
				return new PsiPerlSubSignatureContentImpl(node);
			}
			else if (type == SUB_SIGNATURE_ELEMENT_IGNORE)
			{
				return new PsiPerlSubSignatureElementIgnoreImpl(node);
			}
			else if (type == SUFF_PP_EXPR)
			{
				return new PsiPerlSuffPpExprImpl(node);
			}
			else if (type == TAG_SCALAR)
			{
				return new PsiPerlTagScalarImpl(node);
			}
			else if (type == TERM_EXPR)
			{
				return new PsiPerlTermExprImpl(node);
			}
			else if (type == TRENAR_EXPR)
			{
				return new PsiPerlTrenarExprImpl(node);
			}
			else if (type == TR_MODIFIERS)
			{
				return new PsiPerlTrModifiersImpl(node);
			}
			else if (type == TR_REGEX)
			{
				return new PsiPerlTrRegexImpl(node);
			}
			else if (type == TR_REPLACEMENTLIST)
			{
				return new PsiPerlTrReplacementlistImpl(node);
			}
			else if (type == TR_SEARCHLIST)
			{
				return new PsiPerlTrSearchlistImpl(node);
			}
			else if (type == UNCONDITIONAL_BLOCK)
			{
				return new PsiPerlUnconditionalBlockImpl(node);
			}
			else if (type == UNDEF_EXPR)
			{
				return new PsiPerlUndefExprImpl(node);
			}
			else if (type == UNLESS_COMPOUND)
			{
				return new PsiPerlUnlessCompoundImpl(node);
			}
			else if (type == UNLESS_STATEMENT_MODIFIER)
			{
				return new PsiPerlUnlessStatementModifierImpl(node);
			}
			else if (type == UNTIL_COMPOUND)
			{
				return new PsiPerlUntilCompoundImpl(node);
			}
			else if (type == UNTIL_STATEMENT_MODIFIER)
			{
				return new PsiPerlUntilStatementModifierImpl(node);
			}
			else if (type == USE_STATEMENT)
			{
				return new PsiPerlUseStatementImpl(node);
			}
			else if (type == USE_STATEMENT_CONSTANT)
			{
				return new PsiPerlUseStatementConstantImpl(node);
			}
			else if (type == USE_VARS_STATEMENT)
			{
				return new PsiPerlUseVarsStatementImpl(node);
			}
			else if (type == VARIABLE_DECLARATION_GLOBAL)
			{
				return new PsiPerlVariableDeclarationGlobalImpl(node);
			}
			else if (type == VARIABLE_DECLARATION_LEXICAL)
			{
				return new PsiPerlVariableDeclarationLexicalImpl(node);
			}
			else if (type == VARIABLE_DECLARATION_LOCAL)
			{
				return new PsiPerlVariableDeclarationLocalImpl(node);
			}
			else if (type == VARIABLE_DECLARATION_WRAPPER)
			{
				return new PsiPerlVariableDeclarationWrapperImpl(node);
			}
			else if (type == WHEN_COMPOUND)
			{
				return new PsiPerlWhenCompoundImpl(node);
			}
			else if (type == WHEN_STATEMENT_MODIFIER)
			{
				return new PsiPerlWhenStatementModifierImpl(node);
			}
			else if (type == WHILE_COMPOUND)
			{
				return new PsiPerlWhileCompoundImpl(node);
			}
			else if (type == WHILE_STATEMENT_MODIFIER)
			{
				return new PsiPerlWhileStatementModifierImpl(node);
			}
			throw new AssertionError("Unknown element type: " + type);
		}
	}
}
