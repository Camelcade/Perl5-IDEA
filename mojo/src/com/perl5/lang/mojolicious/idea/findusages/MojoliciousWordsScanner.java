/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

package com.perl5.lang.mojolicious.idea.findusages;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.perl5.lang.mojolicious.lexer.MojoliciousLexerAdapter;
import com.perl5.lang.perl.PerlParserDefinition;

/**
 * Created by hurricup on 06.01.2016.
 */
public class MojoliciousWordsScanner extends DefaultWordsScanner {
  public MojoliciousWordsScanner() {
    super(new MojoliciousLexerAdapter(null),
          PerlParserDefinition.IDENTIFIERS,
          PerlParserDefinition.COMMENTS,
          PerlParserDefinition.LITERALS);
    setMayHaveFileRefsInLiterals(true);
  }
}
