/*
 * Copyright 2015-2023 Alexandr Evstigneev
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
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;


public class MasonParserUtil {

  public static boolean parsePerlBlock(@NotNull PsiBuilder b, int l, @NotNull IElementType closeToken, @NotNull IElementType blockToken) {
    PsiBuilder.Marker abstractBlockMarker = b.mark();
    b.advanceLexer();

    while (!b.eof() && b.getTokenType() != closeToken) {
      if (!PerlParserProxy.file_item(b, l)) {
        break;
      }
    }
    boolean r = endOrRecover(b, closeToken);
    abstractBlockMarker.done(blockToken);
    return r;
  }

  public static boolean endOrRecover(@NotNull PsiBuilder b, @NotNull IElementType toElement) {
    return endOrRecover(b, toElement, "Error");
  }

  public static boolean endOrRecover(@NotNull PsiBuilder b, @NotNull IElementType toElement, @NotNull String errorMessage) {
    return PerlParserUtil.consumeToken(b, toElement) || recoverToGreedy(b, toElement, errorMessage);
  }

  public static boolean recoverToGreedy(@NotNull PsiBuilder b, @NotNull IElementType toElement, @NotNull String errorMessage) {
    boolean r = recoverTo(b, toElement, errorMessage);
    r = r || PerlParserUtil.consumeToken(b, toElement);
    return r;
  }

  public static boolean recoverTo(@NotNull PsiBuilder b, @NotNull IElementType toElement, @NotNull String errorMessage) {
    // recover bad code
    PsiBuilder.Marker errorMarker = b.mark();
    while (!b.eof() && b.getTokenType() != toElement) {
      b.advanceLexer();
    }
    errorMarker.error(errorMessage);
    return b.eof();
  }
}
