/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.perl.internals;

/**
 * Represents internal $^H
 * Respecitve data is in perl.h
 */
public class PerlStrictMask implements Cloneable {
  // fixme make this with Mask
  boolean hintInt = false;            //#define HINT_INTEGER        	0x00000001 /* integer pragma */
  boolean hintRefs = false;            //#define HINT_STRICT_REFS    	0x00000002 /* strict pragma */
  boolean hintLocale = false;            //#define HINT_LOCALE     		0x00000004 /* locale pragma */
  boolean hintBytes = false;            //#define HINT_BYTES      		0x00000008 /* bytes pragma */
  boolean hintLocalePartial = false;    //#define HINT_LOCALE_PARTIAL 	0x00000010 /* locale, but a subset of categories */

  boolean hintRefsExplicit = false;    //#define HINT_EXPLICIT_STRICT_REFS   0x00000020 /* strict.pm */
  boolean hintSubsExplicit = false;    //#define HINT_EXPLICIT_STRICT_SUBS   0x00000040 /* strict.pm */
  boolean hintVarsExplicit = false;    //#define HINT_EXPLICIT_STRICT_VARS   0x00000080 /* strict.pm */

  boolean hintBlockScope = false;        //#define HINT_BLOCK_SCOPE    0x00000100
  boolean hintSubs = false;            //#define HINT_STRICT_SUBS    0x00000200 /* strict pragma */
  boolean hintVars = false;            //#define HINT_STRICT_VARS    0x00000400 /* strict pragma */
  boolean hintUni8Bit = false;        //#define HINT_UNI_8_BIT      0x00000800 /* unicode_strings feature */

  /* The HINT_NEW_* constants are used by the overload pragma */
  boolean hintNewInteger = false;        //#define HINT_NEW_INTEGER    0x00001000
  boolean hintNewFloat = false;        //#define HINT_NEW_FLOAT      0x00002000
  boolean hintNewBinary = false;        //#define HINT_NEW_BINARY     0x00004000
  boolean hintNewString = false;        //#define HINT_NEW_STRING     0x00008000
  boolean hintNewRe = false;            //#define HINT_NEW_RE     	  0x00010000
  boolean hintLocalizeHH = false;        //#define HINT_LOCALIZE_HH    0x00020000 /* %^H needs to be copied */
  boolean hintLexicalIoIn = false;    //#define HINT_LEXICAL_IO_IN  0x00040000 /* ${^OPEN} is set for input */
  boolean hintLexicalIoOut = false;    //#define HINT_LEXICAL_IO_OUT 0x00080000 /* ${^OPEN} is set for output */

  boolean hintReTaint = false;        //#define HINT_RE_TAINT       0x00100000 /* re pragma */
  boolean hintReEval = false;            //#define HINT_RE_EVAL        0x00200000 /* re pragma */

  boolean hintFiletestAccess = false;    //#define HINT_FILETEST_ACCESS  0x00400000 /* filetest pragma */
  boolean hintUtf8 = false;            //#define HINT_UTF8       		0x00800000 /* utf8 pragma */

  boolean hintNoAmagic = false;        //#define HINT_NO_AMAGIC      	0x01000000 /* overloading pragma */

  boolean hintReFlags = false;        //#define HINT_RE_FLAGS       	0x02000000 /* re '/xism' pragma */

  public PerlStrictMask clone() {
    try {
      return (PerlStrictMask)super.clone();
    }
    catch (CloneNotSupportedException e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
