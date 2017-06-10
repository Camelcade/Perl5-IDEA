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

package com.perl5.lang.perl.idea.structureView.elements;

import com.perl5.lang.perl.psi.PerlSubDeclarationElement;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import com.perl5.lang.perl.psi.PerlSubElement;
import com.perl5.lang.perl.psi.mixins.PerlSubBase;

/**
 * Created by hurricup on 15.08.2015.
 */
public class PerlSubStructureViewElement extends PerlStructureViewElement {
  public PerlSubStructureViewElement(PerlSubDefinitionElement element) {
    super(element);
  }

  public PerlSubStructureViewElement(PerlSubDeclarationElement element) {
    super(element);
  }

  public boolean isDeclaration() {
    return myElement instanceof PerlSubDeclarationElement;
  }

  public boolean isMethod() {
    return myElement instanceof PerlSubElement && ((PerlSubBase)myElement).isMethod();
  }
}
