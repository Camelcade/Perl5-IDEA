/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.pod.lexer;

import com.intellij.psi.tree.IElementType;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by hurricup on 25.03.2016.
 */
public class PodDebuggingLexer extends PodLexer {
  public PodDebuggingLexer(Reader in) {
    super(in);
  }

  @Override
  public IElementType advance() throws IOException {
    return super.advance();
/*
                IElementType result = super.advance();
		System.err.println(String.format("Type: %s Value: %s Range: %d - %d State: %d", result, yytext(), getTokenStart(), getTokenEnd(), yystate()));
		return result;
*/

  }
}
