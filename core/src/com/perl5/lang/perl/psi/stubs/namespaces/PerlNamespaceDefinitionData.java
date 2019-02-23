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

package com.perl5.lang.perl.psi.stubs.namespaces;

import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.psi.mro.PerlMroType;
import com.perl5.lang.perl.psi.stubs.PerlStubSerializationUtil;
import com.perl5.lang.perl.psi.utils.PerlNamespaceAnnotations;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Data for namespace definition stubs
 */
public class PerlNamespaceDefinitionData implements PerlNamespaceDefinition {
  @NotNull
  private final String myPackageName;
  @NotNull
  private final PerlMroType myMroType;
  @NotNull
  private final List<String> myParentNamespaces;
  @NotNull
  private final List<String> myEXPORT;
  @NotNull
  private final List<String> myEXPORT_OK;
  @NotNull
  private final Map<String, List<String>> myEXPORT_TAGS;
  @Nullable
  private final PerlNamespaceAnnotations myPerlNamespaceAnnotations;

  public PerlNamespaceDefinitionData(@NotNull PerlNamespaceDefinition namespaceDefinition) {
    this(Objects.requireNonNull(namespaceDefinition.getPackageName()), namespaceDefinition);
  }

  public PerlNamespaceDefinitionData(@NotNull String namespaceName, @NotNull PerlNamespaceDefinition namespaceDefinition) {
    this(namespaceName,
         namespaceDefinition.getMroType(),
         namespaceDefinition.getParentNamespacesNames(),
         namespaceDefinition.getEXPORT(),
         namespaceDefinition.getEXPORT_OK(),
         namespaceDefinition.getEXPORT_TAGS(),
         namespaceDefinition.getAnnotations()
    );
  }

  private PerlNamespaceDefinitionData(@NotNull String packageName,
                                      @NotNull PerlMroType mroType,
                                      @NotNull List<String> parentNamespaces,
                                      @NotNull List<String> EXPORT,
                                      @NotNull List<String> EXPORT_OK,
                                      @NotNull Map<String, List<String>> EXPORT_TAGS,
                                      @Nullable PerlNamespaceAnnotations perlNamespaceAnnotations) {
    myPackageName = packageName;
    myMroType = mroType;
    myParentNamespaces = parentNamespaces;
    myEXPORT = EXPORT;
    myEXPORT_OK = EXPORT_OK;
    myEXPORT_TAGS = EXPORT_TAGS;
    myPerlNamespaceAnnotations = perlNamespaceAnnotations;
  }

  @NotNull
  @Override
  public List<String> getParentNamespacesNames() {
    return myParentNamespaces;
  }

  @Nullable
  @Override
  public PerlNamespaceAnnotations getAnnotations() {
    return myPerlNamespaceAnnotations;
  }

  @NotNull
  @Override
  public String getPackageName() {
    return myPackageName;
  }

  @NotNull
  @Override
  public PerlMroType getMroType() {
    return myMroType;
  }

  @NotNull
  @Override
  public List<String> getEXPORT() {
    return myEXPORT;
  }

  @NotNull
  @Override
  public List<String> getEXPORT_OK() {
    return myEXPORT_OK;
  }

  @NotNull
  @Override
  public Map<String, List<String>> getEXPORT_TAGS() {
    return myEXPORT_TAGS;
  }

  public void serialize(@NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(getPackageName());
    dataStream.writeName(getMroType().toString());
    PerlStubSerializationUtil.writeStringsList(dataStream, getParentNamespacesNames());
    PerlStubSerializationUtil.writeStringsList(dataStream, getEXPORT());
    PerlStubSerializationUtil.writeStringsList(dataStream, getEXPORT_OK());
    PerlStubSerializationUtil.writeStringListMap(dataStream, getEXPORT_TAGS());

    PerlNamespaceAnnotations namespaceAnnotations = getAnnotations();
    if (namespaceAnnotations == null) {
      dataStream.writeBoolean(false);
    }
    else {
      dataStream.writeBoolean(true);
      namespaceAnnotations.serialize(dataStream);
    }
  }

  @NotNull
  public static PerlNamespaceDefinitionData deserialize(@NotNull StubInputStream dataStream) throws IOException {
    String packageName = PerlStubSerializationUtil.readString(dataStream);
    PerlMroType mroType = PerlMroType.valueOf(PerlStubSerializationUtil.readString(dataStream));
    List<String> parentNamespaces = PerlStubSerializationUtil.readStringsList(dataStream);
    List<String> EXPORT = PerlStubSerializationUtil.readStringsList(dataStream);
    List<String> EXPORT_OK = PerlStubSerializationUtil.readStringsList(dataStream);
    Map<String, List<String>> EXPORT_TAGS = PerlStubSerializationUtil.readStringListMap(dataStream);

    return new PerlNamespaceDefinitionData(
      Objects.requireNonNull(packageName),
      mroType,
      Objects.requireNonNull(parentNamespaces),
      Objects.requireNonNull(EXPORT),
      Objects.requireNonNull(EXPORT_OK),
      EXPORT_TAGS,
      dataStream.readBoolean() ? PerlNamespaceAnnotations.deserialize(dataStream) : null
    );
  }
}
