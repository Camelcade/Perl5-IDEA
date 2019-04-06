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
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.perl5.PerlBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Set;

public final class PerlValueReference extends PerlValue {
  @NotNull
  private final PerlValue myReferrent;

  public PerlValueReference(@NotNull PerlValue referrent) {
    this(referrent, null);
  }

  PerlValueReference(@NotNull PerlValue referrent, @Nullable PerlValue bless) {
    super(bless);
    myReferrent = referrent;
  }

  public PerlValueReference(@NotNull StubInputStream dataStream) throws IOException {
    super(dataStream);
    myReferrent = PerlValuesManager.deserialize(dataStream);
  }

  @Override
  protected int getSerializationId() {
    return PerlValuesManager.REFERENCE_ID;
  }

  @Override
  protected void serializeData(@NotNull StubOutputStream dataStream) throws IOException {
    myReferrent.serialize(dataStream);
  }

  @NotNull
  @Override
  public PerlValue getBless() {
    return myReferrent.getBlessedInner();
  }

  /**
   * @return Returns a new reference object with blessed entity
   */
  @NotNull
  public PerlValueReference setBlessed(@NotNull PerlValue bless) {
    return new PerlValueReference(myReferrent.createBlessedCopy(bless), getBlessedInner());
  }

  @NotNull
  @Override
  PerlValue createBlessedCopy(@NotNull PerlValue bless) {
    return new PerlValueReference(this.myReferrent, bless);
  }

  @NotNull
  @Override
  protected Set<String> getNamespaceNames(@NotNull Project project,
                                          @NotNull GlobalSearchScope searchScope,
                                          @Nullable Set<PerlValue> recursion) {
    return getBless().getNamespaceNames(project, searchScope, recursion);
  }

  @Override
  public boolean canRepresentNamespace(@Nullable String namespaceName) {
    return getBless().canRepresentNamespace(namespaceName);
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

    PerlValueReference reference = (PerlValueReference)o;

    return myReferrent.equals(reference.myReferrent);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + myReferrent.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "Reference to: " + myReferrent;
  }

  @NotNull
  @Override
  public String getPresentableValueText() {
    return PerlBundle.message("perl.value.reference.presentable", myReferrent.getPresentableText());
  }
}
