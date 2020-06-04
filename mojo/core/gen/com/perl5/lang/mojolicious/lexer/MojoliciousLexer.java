/* The following code was generated by JFlex 1.7.0-1 tweaked for IntelliJ platform */

package com.perl5.lang.mojolicious.lexer;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.mojolicious.lexer.MojoliciousBaseLexer;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.7.0-1
 * from the specification file <tt>/home/hurricup/Projects/IDEA-Perl5/mojo/core/grammar/Mojolicious.flex</tt>
 */
public class MojoliciousLexer extends MojoliciousBaseLexer {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;
  public static final int PERL_BLOCK = 2;
  public static final int PERL_EXPR_BLOCK = 4;
  public static final int PERL_LINE = 6;
  public static final int PERL_EXPR_LINE = 8;
  public static final int NON_CLEAR_LINE = 10;
  public static final int BLOCK_COMMENT = 12;
  public static final int CHECK_SPACE_CLEAR_LINE = 14;
  public static final int CHECK_SPACE = 16;
  public static final int AFTER_PERL_BLOCK = 18;
  public static final int AFTER_PERL_LINE = 20;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  1,  1,  2,  2,  3,  3,  4,  4,  5,  5,  6,  6,  7,  7, 
     8,  8,  9,  9, 10, 10
  };

  /** 
   * Translates characters to character classes
   * Chosen bits are [9, 6, 6]
   * Total runtime size is 1568 bytes
   */
  public static int ZZ_CMAP(int ch) {
    return ZZ_CMAP_A[(ZZ_CMAP_Y[ZZ_CMAP_Z[ch>>12]|((ch>>6)&0x3f)]<<6)|(ch&0x3f)];
  }

  /* The ZZ_CMAP_Z table has 272 entries */
  static final char ZZ_CMAP_Z[] = zzUnpackCMap(
    "\1\0\1\100\1\200\u010d\100");

  /* The ZZ_CMAP_Y table has 192 entries */
  static final char ZZ_CMAP_Y[] = zzUnpackCMap(
    "\1\0\1\1\1\2\175\3\1\4\77\3");

  /* The ZZ_CMAP_A table has 320 entries */
  static final char ZZ_CMAP_A[] = zzUnpackCMap(
    "\11\0\1\1\1\4\1\2\1\3\1\5\22\0\1\1\2\0\1\12\1\0\1\6\26\0\1\10\1\7\1\11\43"+
    "\0\1\13\1\0\1\20\1\14\1\0\1\15\1\0\1\16\4\0\1\17\26\0\1\2\242\0\2\2\26\0");

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\13\0\1\1\2\2\1\3\1\1\7\4\2\5\2\4"+
    "\2\6\1\7\1\1\3\10\1\11\1\12\1\11\1\13"+
    "\1\14\1\13\1\15\1\16\1\15\1\17\1\20\1\21"+
    "\1\22\1\23\3\0\1\24\3\0\1\25\1\0\3\12"+
    "\1\20\1\26\1\27\1\30\4\0\1\12\1\27\4\0"+
    "\3\31\4\0\3\32";

  private static int [] zzUnpackAction() {
    int [] result = new int[84];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\21\0\42\0\63\0\104\0\125\0\146\0\167"+
    "\0\210\0\231\0\252\0\273\0\314\0\335\0\356\0\377"+
    "\0\u0110\0\u0121\0\u0132\0\u0143\0\u0154\0\u0165\0\u0176\0\u0110"+
    "\0\u0187\0\u0198\0\u01a9\0\u0110\0\u01ba\0\u01cb\0\u01dc\0\u01ed"+
    "\0\u01fe\0\u020f\0\u0110\0\u0220\0\u0231\0\u0110\0\u0242\0\u0253"+
    "\0\u0110\0\u0253\0\u0253\0\u0110\0\u0264\0\u0275\0\u0286\0\u0110"+
    "\0\u0121\0\u0297\0\u02a8\0\u0110\0\u0165\0\u02b9\0\u02ca\0\u0110"+
    "\0\u01fe\0\u02db\0\u02ec\0\u02fd\0\u0110\0\u0110\0\u030e\0\u0110"+
    "\0\u031f\0\u0330\0\u0341\0\u0352\0\u0110\0\u0110\0\u0363\0\u0374"+
    "\0\u0385\0\u0396\0\u0110\0\u0352\0\u03a7\0\u03b8\0\u03c9\0\u03da"+
    "\0\u03eb\0\u0110\0\u03c9\0\u03fc";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[84];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\14\1\15\1\16\3\15\1\17\1\14\1\20\10\14"+
    "\6\21\1\22\1\23\3\21\1\24\1\25\12\21\1\26"+
    "\1\27\13\21\3\30\1\31\5\21\1\32\1\33\6\21"+
    "\3\34\1\35\13\21\1\14\1\36\1\16\1\36\2\15"+
    "\1\37\1\14\1\20\10\14\6\40\1\41\1\42\11\40"+
    "\6\43\1\44\1\43\1\45\20\43\1\45\10\43\1\46"+
    "\1\47\1\50\1\47\2\50\13\46\1\51\1\52\1\53"+
    "\1\52\2\53\13\51\1\14\1\0\1\14\4\0\1\14"+
    "\1\0\10\14\1\0\5\15\13\0\1\14\1\15\1\16"+
    "\3\15\1\0\1\14\1\0\10\14\6\0\1\54\1\55"+
    "\2\0\1\56\14\0\1\57\44\0\1\60\15\0\1\61"+
    "\26\0\1\62\23\0\1\63\12\0\1\64\15\0\1\65"+
    "\16\0\1\30\30\0\1\66\23\0\1\67\5\0\1\34"+
    "\15\0\1\36\1\15\1\36\2\15\21\0\1\54\12\0"+
    "\6\40\2\0\11\40\11\0\1\70\15\0\1\71\21\0"+
    "\1\72\2\0\1\73\14\0\1\74\13\0\1\47\1\52"+
    "\1\47\2\52\14\0\5\52\22\0\1\75\11\0\2\56"+
    "\4\0\13\56\6\0\1\76\1\77\2\0\1\100\23\0"+
    "\1\101\23\0\1\102\15\0\1\103\23\0\1\104\7\0"+
    "\1\105\11\0\2\73\4\0\13\73\7\0\1\72\2\0"+
    "\1\105\15\0\1\106\27\0\1\107\3\0\5\102\1\110"+
    "\1\111\27\0\1\112\3\0\1\104\1\113\1\114\1\113"+
    "\1\115\32\0\1\116\12\0\1\113\15\0\1\110\31\0"+
    "\1\117\5\0\1\113\15\0\5\116\1\120\1\121\12\0"+
    "\1\117\1\122\1\123\1\122\1\124\24\0\1\122\15\0"+
    "\1\120\16\0\1\122\14\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[1037];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String[] ZZ_ERROR_MSG = {
    "Unknown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\13\0\5\1\1\11\6\1\1\11\3\1\1\11\6\1"+
    "\1\11\2\1\1\11\2\1\1\11\2\1\1\11\3\1"+
    "\1\11\3\0\1\11\3\0\1\11\1\0\3\1\2\11"+
    "\1\1\1\11\4\0\2\11\4\0\1\11\2\1\4\0"+
    "\1\11\2\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[84];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private CharSequence zzBuffer = "";

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /**
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;


  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public MojoliciousLexer(java.io.Reader in) {
    this.zzReader = in;
  }


  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    int size = 0;
    for (int i = 0, length = packed.length(); i < length; i += 2) {
      size += packed.charAt(i);
    }
    char[] map = new char[size];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < packed.length()) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }
	protected int bufferStart;

    public void setTokenStart(int position){zzCurrentPos = zzStartRead = position;}
    public void setTokenEnd(int position){zzMarkedPos = position;}
    public CharSequence getBuffer(){ return zzBuffer;}
    public int getBufferEnd() {return zzEndRead;}
    public int getNextTokenStart(){ return zzMarkedPos;}
    public boolean isLastToken(){ return zzMarkedPos == zzEndRead; }
	public int getBufferStart(){ return bufferStart;	}
 	public int getRealLexicalState() {return zzLexicalState;  }

 	public void pullback(int i)
 	{
 		int length = yylength();
 		if( i == length)
 		{
 			return;
 		}
 		assert i < length: "Pulling back for " + i + " of " + length + " for: " + yytext();
		yypushback(length - i);
 	}

  public final int getTokenStart() {
    return zzStartRead;
  }

  public final int getTokenEnd() {
    return zzMarkedPos;
  }

  public void reset(CharSequence buffer, int start, int end, int initialState) {
    zzBuffer = buffer;
    bufferStart = zzCurrentPos = zzMarkedPos = zzStartRead = start;
    zzAtEOF  = false;
    zzAtBOL = true;
    zzEndRead = end;
    yybegin(initialState);
    resetInternals();
  }

  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   *
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {
    return true;
  }


  /**
   * Returns the current lexical state.
   */
  public int yystate() {
    return isInitialState() ? zzLexicalState: -666;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final CharSequence yytext() {
    return zzBuffer.subSequence(zzStartRead, zzMarkedPos);
  }


  /**
   * Returns the character at position <tt>pos</tt> from the
   * matched text.
   *
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch.
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer.charAt(zzStartRead+pos);
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occurred while scanning.
   *
   * In a wellformed scanner (no or only correct usage of
   * yypushback(int) and a match-all fallback rule) this method
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }
    message += "; Real state is: " + zzLexicalState + "; yystate(): " + yystate();

    throw new Error(message);
  }


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public IElementType perlAdvance() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    CharSequence zzBufferL = zzBuffer;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;

      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {

          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL/*, zzEndReadL*/);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL/*, zzEndReadL*/);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + ZZ_CMAP(zzInput) ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
        zzAtEOF = true;
        switch (zzLexicalState) {
            case BLOCK_COMMENT: {
              yybegin(YYINITIAL);return COMMENT_BLOCK;
            }  // fall though
            case 85: break;
            case CHECK_SPACE_CLEAR_LINE: {
              yybegin(YYINITIAL);return MOJO_TEMPLATE_BLOCK_HTML;
            }  // fall though
            case 86: break;
            case CHECK_SPACE: {
              yybegin(YYINITIAL);return MOJO_TEMPLATE_BLOCK_HTML;
            }  // fall though
            case 87: break;
            default:
        return null;
        }
      }
      else {
        switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
          case 1: 
            { yybegin(NON_CLEAR_LINE);return MOJO_TEMPLATE_BLOCK_HTML;
            } 
            // fall through
          case 27: break;
          case 2: 
            { yybegin(CHECK_SPACE_CLEAR_LINE);
            } 
            // fall through
          case 28: break;
          case 3: 
            { yybegin(PERL_LINE);return MOJO_LINE_OPENER;
            } 
            // fall through
          case 29: break;
          case 4: 
            { return delegateLexing();
            } 
            // fall through
          case 30: break;
          case 5: 
            { yybegin(AFTER_PERL_LINE);return TokenType.WHITE_SPACE;
            } 
            // fall through
          case 31: break;
          case 6: 
            { yybegin(AFTER_PERL_LINE);endPerlExpression();return SEMICOLON;
            } 
            // fall through
          case 32: break;
          case 7: 
            { yybegin(CHECK_SPACE);
            } 
            // fall through
          case 33: break;
          case 8: 
            { 
            } 
            // fall through
          case 34: break;
          case 9: 
            { yypushback(1);yybegin(NON_CLEAR_LINE);return MOJO_TEMPLATE_BLOCK_HTML;
            } 
            // fall through
          case 35: break;
          case 10: 
            { pullback(0);yybegin(YYINITIAL);return TokenType.WHITE_SPACE;
            } 
            // fall through
          case 36: break;
          case 11: 
            { yypushback(1);yybegin(NON_CLEAR_LINE);
            } 
            // fall through
          case 37: break;
          case 12: 
            { yybegin(NON_CLEAR_LINE);return TokenType.WHITE_SPACE;
            } 
            // fall through
          case 38: break;
          case 13: 
            { yypushback(1);yybegin(YYINITIAL);
            } 
            // fall through
          case 39: break;
          case 14: 
            { yybegin(YYINITIAL);return TokenType.WHITE_SPACE;
            } 
            // fall through
          case 40: break;
          case 15: 
            { yybegin(NON_CLEAR_LINE);return MOJO_LINE_OPENER_TAG;
            } 
            // fall through
          case 41: break;
          case 16: 
            { startPerlExpression();yybegin(PERL_EXPR_LINE);return MOJO_LINE_OPENER;
            } 
            // fall through
          case 42: break;
          case 17: 
            { yybegin(NON_CLEAR_LINE);return COMMENT_LINE;
            } 
            // fall through
          case 43: break;
          case 18: 
            { yybegin(PERL_BLOCK);return MOJO_BLOCK_OPENER;
            } 
            // fall through
          case 44: break;
          case 19: 
            { yybegin(AFTER_PERL_BLOCK);return MOJO_BLOCK_CLOSER;
            } 
            // fall through
          case 45: break;
          case 20: 
            { yybegin(AFTER_PERL_BLOCK);endPerlExpression();return MOJO_BLOCK_EXPR_CLOSER;
            } 
            // fall through
          case 46: break;
          case 21: 
            { yybegin(AFTER_PERL_BLOCK);return COMMENT_BLOCK;
            } 
            // fall through
          case 47: break;
          case 22: 
            { yybegin(NON_CLEAR_LINE);return MOJO_BLOCK_OPENER_TAG;
            } 
            // fall through
          case 48: break;
          case 23: 
            { startPerlExpression();yybegin(PERL_EXPR_BLOCK);return MOJO_BLOCK_EXPR_OPENER;
            } 
            // fall through
          case 49: break;
          case 24: 
            { yybegin(BLOCK_COMMENT);
            } 
            // fall through
          case 50: break;
          case 25: 
            // lookahead expression with fixed base length
            zzMarkedPos = Character.offsetByCodePoints
                (zzBufferL/*, zzStartRead, zzEndRead - zzStartRead*/, zzStartRead, 3);
            { return MOJO_END;
            } 
            // fall through
          case 51: break;
          case 26: 
            // lookahead expression with fixed base length
            zzMarkedPos = Character.offsetByCodePoints
                (zzBufferL/*, zzStartRead, zzEndRead - zzStartRead*/, zzStartRead, 5);
            { return MOJO_BEGIN;
            } 
            // fall through
          case 52: break;
          default:
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}