/*
 * Copyright 2015 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.findusages;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.lexer.adapters.PerlMergingLexerAdapter;

/**
 * Created by hurricup on 06.01.2016.
 */
public class PerlWordsScanner extends DefaultWordsScanner implements PerlElementTypes {
  public PerlWordsScanner() {
    super(new PerlMergingLexerAdapter((Project)null),
          PerlParserDefinition.IDENTIFIERS,
          TokenSet.orSet(PerlParserDefinition.COMMENTS, TokenSet.create(POD)),
          PerlParserDefinition.LITERALS
    );
    setMayHaveFileRefsInLiterals(true);
  }
}
