package com.perl5.lang.lexer.ported;

/**
 * Created by hurricup on 18.04.2015.
 */
public class yy_stack_frame
{
	YYSTYPE val;    /* semantic value */
	short   state;
	int     savestack_ix;   /* size of savestack at this state */
	int     compcv; /* value of PL_compcv when this value was created */ // WAS CV

}
