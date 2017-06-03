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

package com.perl5.lang.perl.psi.utils;

import com.perl5.lang.perl.psi.PerlVariableDeclarationWrapper;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;

/**
 * Created by hurricup on 06.06.2015.
 * Class represents lexical declaration in the PsiFile
 */
public class PerlLexicalDeclaration {
  PerlVariableDeclarationWrapper myDeclarationWrapper;
  int textOffset;
  PerlLexicalScope myScope;

  public PerlLexicalDeclaration(PerlVariableDeclarationWrapper declarationWrapper, PerlLexicalScope variableScope) {

    myDeclarationWrapper = declarationWrapper;
    textOffset = declarationWrapper.getTextOffset();
    myScope = variableScope;
  }

  public PerlVariableDeclarationWrapper getDeclarationWrapper() {
    return myDeclarationWrapper;
  }

  public int getTextOffset() {
    return textOffset;
  }

  public PerlLexicalScope getScope() {
    return myScope;
  }
}
