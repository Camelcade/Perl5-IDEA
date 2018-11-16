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

package com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers;

import com.intellij.ui.ColoredListCellRenderer;
import org.jetbrains.annotations.NotNull;

public class Perl5TextSdkWrapper implements Perl5SdkWrapper {
  @NotNull
  private final String myText;

  public Perl5TextSdkWrapper(@NotNull String text) {
    myText = text;
  }

  @Override
  public void customizeRenderer(@NotNull ColoredListCellRenderer<Perl5SdkWrapper> renderer) {
    renderer.append(myText);
  }

  @Override
  public String toString() {
    return "Text: " + myText;
  }
}
