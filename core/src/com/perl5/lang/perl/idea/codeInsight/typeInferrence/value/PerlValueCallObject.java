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
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.PairProcessor;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.psi.mro.PerlMro;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public final class PerlValueCallObject extends PerlValueCall {
  private final boolean myIsSuper;

  public PerlValueCallObject(@NotNull PerlValue namespaceNameValue,
                             @NotNull PerlValue subNameValue,
                             @NotNull List<PerlValue> arguments,
                             boolean isSuper) {
    this(namespaceNameValue, subNameValue, arguments, isSuper, null);
  }

  public PerlValueCallObject(@NotNull PerlValue namespaceNameValue,
                             @NotNull PerlValue subNameValue,
                             @NotNull List<PerlValue> arguments,
                             boolean isSuper,
                             @Nullable PerlValue bless) {
    super(namespaceNameValue, subNameValue, arguments, bless);
    myIsSuper = isSuper;
  }

  public PerlValueCallObject(@NotNull StubInputStream dataStream) throws IOException {
    super(dataStream);
    myIsSuper = dataStream.readBoolean();
  }

  @Override
  protected void serializeData(@NotNull StubOutputStream dataStream) throws IOException {
    super.serializeData(dataStream);
    dataStream.writeBoolean(myIsSuper);
  }

  @Override
  protected int getSerializationId() {
    return PerlValuesManager.CALL_OBJECT_ID;
  }

  @NotNull
  @Override
  PerlValue createBlessedCopy(@NotNull PerlValue bless) {
    return new PerlValueCallObject(myNamespaceNameValue, mySubNameValue, myArguments, myIsSuper, bless);
  }

  public boolean isSuper() {
    return myIsSuper;
  }

  @Override
  public boolean processTargetNamespaceElements(@NotNull Project project,
                                                @NotNull GlobalSearchScope searchScope,
                                                @Nullable PsiElement contextElement,
                                                @NotNull PerlNamespaceItemProcessor<? super PsiNamedElement> processor) {
    for (String contextNamespace : myNamespaceNameValue.getNamespaceNames(project, searchScope)) {
      for (String currentNamespaceName : PerlMro.getLinearISA(project, searchScope, contextNamespace, myIsSuper)) {
        if (!processTargetNamespaceElements(project, searchScope, processor, currentNamespaceName)) {
          return false;
        }
      }
    }
    return true;
  }


  @Override
  protected boolean processCallTargets(@NotNull Project project,
                                       @NotNull GlobalSearchScope searchScope,
                                       @Nullable PsiElement contextElement, @NotNull Set<String> namespaceNames,
                                       @NotNull Set<String> subNames,
                                       @NotNull PairProcessor<String, ? super PsiNamedElement> processor) {
    for (String contextNamespace : namespaceNames) {
      for (String currentNamespaceName : PerlMro.getLinearISA(project, searchScope, contextNamespace, myIsSuper)) {
        ProcessingContext processingContext = new ProcessingContext();
        processingContext.processBuiltIns = false;
        if (!processItemsInNamespace(project, searchScope, subNames,
                                     it -> processor.process(contextNamespace, it),
                                     currentNamespaceName, processingContext)) {
          return false;
        }
        if (!processingContext.processAutoload) { // marker that we've got at least one result
          break;
        }
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

    PerlValueCallObject object = (PerlValueCallObject)o;

    return myIsSuper == object.myIsSuper;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (myIsSuper ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Object: " + myNamespaceNameValue + "->" + (myIsSuper ? "SUPER::" : "") + mySubNameValue;
  }

  @NotNull
  @Override
  protected String getPresentableValueText() {
    return PerlBundle
      .message("perl.value.call.object.presentable", myNamespaceNameValue.getPresentableText(), mySubNameValue.getPresentableText());
  }
}
