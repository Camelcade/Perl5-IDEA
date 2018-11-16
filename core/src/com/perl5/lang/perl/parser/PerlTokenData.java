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

package com.perl5.lang.perl.parser;

import com.intellij.psi.tree.IElementType;

/**
 * Created by hurricup on 07.05.2015.
 * represents token data - type and text
 */
public class PerlTokenData {
  protected IElementType tokenType;
  protected String tokenText;

  public PerlTokenData(IElementType type, String text) {
    tokenType = type;
    tokenText = text;
  }

  public IElementType getTokenType() {
    return tokenType;
  }

  public String getTokenText() {
    return tokenText;
  }
}
