/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

package com.perl5.lang.htmlmason.idea.livetemplates;

import com.perl5.lang.perl.idea.livetemplates.AbstractOutlineLiveTemplateProcessor;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractMasonLiveTemplateProcessor extends AbstractOutlineLiveTemplateProcessor {
  @Override
  protected boolean shouldAddMarkerAtLineStartingAtOffset(CharSequence buffer, int offset) {
    int bufferEnd = buffer.length();
    if (offset >= bufferEnd) {
      return false;
    }

    char currentChar = buffer.charAt(offset);
    if (currentChar == '%') {
      return false;
    }

    while (Character.isWhitespace(currentChar)) {
      if (currentChar == '\n' || ++offset >= bufferEnd) {
        return false;
      }
      currentChar = buffer.charAt(offset);
    }

    return true;
  }

  @Override
  @NotNull
  protected String getOutlineMarker() {
    return "% ";
  }
}
