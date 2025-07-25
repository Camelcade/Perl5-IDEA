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

package com.perl5.lang.tt2.elementTypes;

import com.intellij.psi.tree.IElementType;
import com.perl5.lang.tt2.TemplateToolkitLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;


public class TemplateToolkitTokenType extends IElementType {
  public TemplateToolkitTokenType(@NotNull @NonNls String debugName) {
    super(debugName, TemplateToolkitLanguage.INSTANCE);
  }

  @Override
  public String toString() {
    return "TT2: " + super.toString();
  }
}
