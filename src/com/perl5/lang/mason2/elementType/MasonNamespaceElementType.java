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

package com.perl5.lang.mason2.elementType;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.perl5.lang.mason2.Mason2Language;
import com.perl5.lang.mason2.Mason2Util;
import com.perl5.lang.mason2.psi.MasonNamespaceDefinition;
import com.perl5.lang.mason2.psi.impl.MasonNamespaceDefinitionImpl;
import com.perl5.lang.mason2.psi.stubs.MasonNamespaceDefitnitionsStubIndex;
import com.perl5.lang.mason2.psi.stubs.MasonParentNamespacesStubIndex;
import com.perl5.lang.perl.parser.elementTypes.PsiElementProvider;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDefinitionElementType;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDefinitionStub;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceIndex;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 05.01.2016.
 */
public class MasonNamespaceElementType extends PerlNamespaceDefinitionElementType implements PsiElementProvider {
  public MasonNamespaceElementType(String name) {
    super(name, Mason2Language.INSTANCE);
  }

  @Override
  public PerlNamespaceDefinitionElement createPsi(@NotNull PerlNamespaceDefinitionStub stub) {
    return new MasonNamespaceDefinitionImpl(stub, this);
  }

  @Override
  public PerlNamespaceDefinitionStub createStub(@NotNull PerlNamespaceDefinitionElement psi, StubElement parentStub) {
    assert psi instanceof MasonNamespaceDefinitionImpl;
    return new PerlNamespaceDefinitionStub(
      parentStub,
      this,
      ((MasonNamespaceDefinitionImpl)psi).getAbsoluteComponentPath(),
      psi.getMroType(),
      psi.getParentNamespacesNames(),
      psi.getEXPORT(),
      psi.getEXPORT_OK(),
      psi.getEXPORT_TAGS(),
      psi.getAnnotations()
    );
  }

  @Override
  public void indexStub(@NotNull PerlNamespaceDefinitionStub stub, @NotNull IndexSink sink) {
    String name = stub.getPackageName();
    assert name != null;
    sink.occurrence(MasonNamespaceDefitnitionsStubIndex.KEY, name);

    // fixme this is kinda hack to make MRO work. But, it should be smarter
    sink.occurrence(PerlNamespaceIndex.KEY, Mason2Util.getClassnameFromPath(name));

    for (String parent : stub.getParentNamespacesNames()) {
      if (parent != null && !parent.isEmpty()) {
        sink.occurrence(MasonParentNamespacesStubIndex.KEY, parent);
      }
    }
  }

  @Override
  public boolean shouldCreateStub(ASTNode node) {
    PsiElement psi = node.getPsi();
    return psi instanceof MasonNamespaceDefinition &&
           psi.isValid() &&
           StringUtil.isNotEmpty(((MasonNamespaceDefinition)psi).getAbsoluteComponentPath());
  }

  @NotNull
  @Override
  public PsiElement getPsiElement(@NotNull ASTNode node) {
    return new MasonNamespaceDefinitionImpl(node);
  }
}
