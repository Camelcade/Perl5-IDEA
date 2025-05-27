/*
 * Copyright 2015-2025 Alexandr Evstigneev
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


import com.intellij.lang.PsiBuilder;

/**
 * Proxy methods for package-private methods of Perl Parser
 */
public final class PerlParserProxy {
  private PerlParserProxy() {
  }

  public static boolean file_item(PsiBuilder b, int l) {
    return PerlParserGenerated.file_item(b, l);
  }

  @SuppressWarnings("UnusedReturnValue")
  public static boolean block_content(PsiBuilder b, int l) {
    return PerlParserGenerated.block_content(b, l);
  }

  @SuppressWarnings("UnusedReturnValue")
  public static boolean method_signature(PsiBuilder b, int l) {
    return PerlParserGenerated.method_signature(b, l);
  }

  public static boolean expr(PsiBuilder b, int l, int p) {
    return PerlParserGenerated.expr(b, l, p);
  }
}
