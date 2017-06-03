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

package oop;

import base.PerlLightCodeInsightFixtureTestCase;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.mixins.PerlNamespaceDefinitionImplMixin;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 22.02.2016.
 */
public abstract class NamespaceTestCase extends PerlLightCodeInsightFixtureTestCase {
  protected PerlNamespaceDefinitionImplMixin getNamespaceInFile(@NotNull String fileName, @NotNull String namespaceName) {
    myFixture.configureByFile(fileName);
    PsiFile file = myFixture.getFile();

    PerlNamespaceDefinitionImplMixin namespaceDefinition = null;
    for (PerlNamespaceDefinitionImplMixin element : PsiTreeUtil.findChildrenOfType(file, PerlNamespaceDefinitionImplMixin.class)) {
      if (StringUtil.equals(element.getName(), namespaceName)) {
        namespaceDefinition = element;
        break;
      }
    }
    assertNotNull(namespaceDefinition);
    namespaceDefinition.subtreeChanged();
    return namespaceDefinition;
  }
}
