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

package com.perl5.lang.pod.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.pod.PodParserDefinition;
import com.perl5.lang.pod.lexer.PodElementTypes;
import org.jetbrains.annotations.NotNull;


public class PodParserUtil extends GeneratedParserUtilBase implements PodElementTypes {
  public static boolean completeOrReport(PsiBuilder b, int l, @NotNull IElementType targetElement, @NotNull String message) {
    if (b.getTokenType() == targetElement) {
      b.advanceLexer();
      return true;
    }
    PsiBuilder.Marker marker = b.mark();
    while (!b.eof() && b.getTokenType() != targetElement && b.getTokenType() != POD_NEWLINE) {
      b.advanceLexer();
    }
    marker.error(message);
    b.advanceLexer();
    return true;
  }

  public static boolean checkAndConvert(PsiBuilder b, int l, IElementType sourceType, IElementType targetType) {
    if (b.getTokenType() == sourceType) {
      PsiBuilder.Marker m = b.mark();
      b.advanceLexer();
      m.collapse(POD_INDENT_LEVEL);
      return true;
    }
    return false;
  }


  public static boolean collapseNonSpaceTo(PsiBuilder b, int l, IElementType targetElement) {
    IElementType tokenType = b.getTokenType();

    if (tokenType == POD_IDENTIFIER) {
      PsiBuilder.Marker m = b.mark();
      while (!b.eof() && !PodParserDefinition.ALL_WHITE_SPACES.contains(b.rawLookup(1))) {
        b.advanceLexer();
      }
      b.advanceLexer();
      m.collapse(targetElement);
      return true;
    }
    return false;
  }

  public static boolean parsePodSectionContent(PsiBuilder b, int l, IElementType stopToken, IElementType targetToken, String errorMessage) {
    PsiBuilder.Marker m = b.mark();
    while (!b.eof() && b.getTokenType() != stopToken) {
      //			PsiBuilder.Marker runMarker = b.mark();
      b.advanceLexer();
      //			runMarker.collapse(POD_IDENTIFIER);
    }

    m.done(targetToken);

    if (b.eof()) {
      b.mark().error(errorMessage);
    }

    return true;
  }
}
