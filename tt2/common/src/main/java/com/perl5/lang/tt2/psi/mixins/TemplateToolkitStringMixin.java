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

package com.perl5.lang.tt2.psi.mixins;

import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.tt2.psi.TemplateToolkitString;
import com.perl5.lang.tt2.psi.impl.TemplateToolkitCompositeElementImpl;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.tt2.parser.TemplateToolkitElementTypesGenerated.*;


public class TemplateToolkitStringMixin extends TemplateToolkitCompositeElementImpl implements TemplateToolkitString {
  public static final TokenSet BLOCK_NAME_TARGETED_CONTAINERS = TokenSet.create(
    INCLUDE_DIRECTIVE,
    PROCESS_DIRECTIVE,
    WRAPPER_DIRECTIVE
  );

  public TemplateToolkitStringMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public boolean hasReferences() {
    return true;
  }
}
