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

package com.perl5.lang.pod.elementTypes;

import com.intellij.psi.tree.IElementType;
import com.perl5.lang.pod.PodLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class PodTokenType extends IElementType {
  public PodTokenType(@NotNull @NonNls String debugName) {
    super(debugName, PodLanguage.INSTANCE);
  }

  @Override
  public String toString() {
    return "Perl5 POD: " + super.toString();
  }
}
