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

package com.perl5.lang.perl.idea.codeInsight.typeInferrence.value;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.PairProcessor;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.extensions.imports.PerlImportsProvider;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public final class PerlValueCallStatic extends PerlValueCall {
  private final boolean myHasExplicitNamespace;

  public PerlValueCallStatic(@NotNull PerlValue namespaceNameValue,
                             @NotNull PerlValue subNameValue,
                             @NotNull List<PerlValue> arguments,
                             boolean hasExplicitNamespace) {
    this(namespaceNameValue, subNameValue, arguments, hasExplicitNamespace, null);
  }

  public PerlValueCallStatic(@NotNull PerlValue namespaceNameValue,
                             @NotNull PerlValue subNameValue,
                             @NotNull List<PerlValue> arguments,
                             boolean hasExplicitNamespace,
                             @Nullable PerlValue bless) {
    super(namespaceNameValue, subNameValue, arguments, bless);
    myHasExplicitNamespace = hasExplicitNamespace;
  }

  public PerlValueCallStatic(@NotNull StubInputStream dataStream) throws IOException {
    super(dataStream);
    myHasExplicitNamespace = dataStream.readBoolean();
  }

  @Override
  protected void serializeData(@NotNull StubOutputStream dataStream) throws IOException {
    super.serializeData(dataStream);
    dataStream.writeBoolean(myHasExplicitNamespace);
  }

  @Override
  protected int getSerializationId() {
    return PerlValuesManager.CALL_STATIC_ID;
  }


  @NotNull
  @Override
  PerlValue createBlessedCopy(@NotNull PerlValue bless) {
    return new PerlValueCallStatic(myNamespaceNameValue, mySubNameValue, myArguments, myHasExplicitNamespace, bless);
  }

  @Override
  public boolean processTargetNamespaceElements(@NotNull Project project,
                                                @NotNull GlobalSearchScope searchScope,
                                                @Nullable PsiElement contextElement,
                                                @NotNull PerlNamespaceItemProcessor<? super PsiNamedElement> processor) {
    for (String currentNamespaceName : myNamespaceNameValue.getNamespaceNames(project, searchScope)) {
      if (!processTargetNamespaceElements(project, searchScope, processor, currentNamespaceName)) {
        return false;
      }
    }

    PerlNamespaceDefinitionElement containingNamespace = PerlPackageUtil.getContainingNamespace(contextElement.getOriginalElement());
    String namespaceName = containingNamespace == null ? null : containingNamespace.getPackageName();
    if (!StringUtil.isEmpty(namespaceName)) {
      processExportDescriptors(
        project, searchScope, processor, PerlImportsProvider.getAllExportDescriptors(containingNamespace));
    }


    return true;
  }

  @Override
  protected boolean processCallTargets(@NotNull Project project,
                                       @NotNull GlobalSearchScope searchScope,
                                       @Nullable PsiElement contextElement,
                                       @NotNull Set<String> namespaceNames,
                                       @NotNull Set<String> subNames,
                                       @NotNull PairProcessor<String, ? super PsiNamedElement> processor) {

    for (String contextNamespace : namespaceNames) {
      ProcessingContext processingContext = new ProcessingContext();
      processingContext.processBuiltIns = !myHasExplicitNamespace;

      if (!processItemsInNamespace(project, searchScope, subNames,
                                   it -> processor.process(contextNamespace, it),
                                   contextNamespace, processingContext)) {
        return false;
      }
    }

    if (!myHasExplicitNamespace && contextElement != null) {
      PerlNamespaceDefinitionElement containingNamespace = PerlPackageUtil.getContainingNamespace(contextElement.getOriginalElement());
      String namespaceName = containingNamespace == null ? null : containingNamespace.getPackageName();
      if (!StringUtil.isEmpty(namespaceName)) {
        processExportDescriptorsItems(
          project, searchScope, subNames, it -> processor.process(namespaceName, it),
          PerlImportsProvider.getAllExportDescriptors(containingNamespace));
      }
    }

    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    PerlValueCallStatic aStatic = (PerlValueCallStatic)o;

    return myHasExplicitNamespace == aStatic.myHasExplicitNamespace;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (myHasExplicitNamespace ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Static: " + myNamespaceNameValue + "::" + mySubNameValue;
  }

  @NotNull
  @Override
  protected String getPresentableValueText() {
    return PerlBundle
      .message("perl.value.call.static.presentable", myNamespaceNameValue.getPresentableText(), mySubNameValue.getPresentableText());
  }
}
