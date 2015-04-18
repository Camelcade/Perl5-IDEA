package com.perl5.lang.lexer.ported;

/**
 * Created by hurricup on 18.04.2015.
 */
public class LEXSHARED
{
	LEXSHARED 	ls_prev;
	SV          ls_linestr;    	/* mirrors PL_parser->linestr */
	char        ls_bufptr; 		/* mirrors PL_parser->bufptr */
	char        re_eval_start; 	/* start of "(?{..." text */
	SV          re_eval_str;   	/* "(?{...})" text */
}
