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

package com.perl5.lang.perl.idea.codeInsight.typeInference.value;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.perl5.PerlBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public final class PerlReferenceValue extends PerlOperationValue {
  private final boolean myIsConstantReference;

  private PerlReferenceValue(@NotNull PerlValue referrent, boolean isConstantReference) {
    super(referrent);
    myIsConstantReference = isConstantReference;
  }

  PerlReferenceValue(@NotNull StubInputStream dataStream) throws IOException {
    super(dataStream);
    myIsConstantReference = dataStream.readBoolean();
  }

  @Override
  protected int getSerializationId() {
    return PerlValuesManager.REFERENCE_ID;
  }

  @Override
  protected void serializeData(@NotNull StubOutputStream dataStream) throws IOException {
    super.serializeData(dataStream);
    dataStream.writeBoolean(myIsConstantReference);
  }

  @NotNull
  @Override
  protected Set<String> getNamespaceNames(@NotNull Project project,
                                          @NotNull GlobalSearchScope searchScope,
                                          @Nullable Set<PerlValue> recursion) {
    return getBaseValue() instanceof PerlBlessedValue ? getBaseValue().getNamespaceNames(project, searchScope, recursion) :
           Collections.emptySet();
  }

  @Override
  public boolean canRepresentNamespace(@Nullable String namespaceName) {
    return getBaseValue() instanceof PerlBlessedValue && getBaseValue().canRepresentNamespace(namespaceName);
  }

  @NotNull
  public PerlValue getTarget() {
    return getBaseValue();
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

    PerlReferenceValue value = (PerlReferenceValue)o;

    return myIsConstantReference == value.myIsConstantReference;
  }

  @Override
  public int computeHashCode() {
    int result = super.computeHashCode();
    result = 31 * result + (myIsConstantReference ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Reference to: " + getBaseValue();
  }

  @NotNull
  @Override
  public String getPresentableText() {
    return PerlBundle.message("perl.value.reference.presentable", getBaseValue().getPresentableText());
  }
}
