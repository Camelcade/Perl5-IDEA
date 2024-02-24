/*
 * Copyright 2015-2024 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi.stubs;

import com.intellij.lang.*;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.StubBuilder;
import com.intellij.psi.stubs.*;
import com.intellij.psi.tree.IStubFileElementType;
import com.perl5.lang.perl.idea.EP.PerlPackageProcessorService;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager;
import com.perl5.lang.perl.parser.builder.PerlPsiBuilderFactory;
import com.perl5.lang.perl.psi.PerlFile;
import com.perl5.lang.perl.psi.PerlSubCallHandlerVersionService;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDefinitionData;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceIndex.NAMESPACE_KEY;

public final class PerlFileElementType extends IStubFileElementType<PerlFileStub> {
  private static final int VERSION = 6;

  public PerlFileElementType(String debugName, Language language) {
    super(debugName, language);
  }

  @Override
  public int getStubVersion() {
    return super.getStubVersion() +
           VERSION +
           PerlValuesManager.getVersion() +
           PerlSubCallHandlerVersionService.getHandlersVersion() +
           PerlPackageProcessorService.getVersion();
  }

  @Override
  protected ASTNode doParseContents(@NotNull ASTNode chameleon, @NotNull PsiElement psi) {
    return getParser(psi).parse(this, getBuilder(psi, chameleon)).getFirstChildNode();
  }

  private @NotNull PsiParser getParser(PsiElement psi) {
    return LanguageParserDefinitions.INSTANCE.forLanguage(getLanguageForParser(psi)).createParser(psi.getProject());
  }

  private @NotNull PsiBuilder getBuilder(PsiElement psi, ASTNode chameleon) {
    return PerlPsiBuilderFactory.Companion.createBuilder(
      psi.getProject(), chameleon, null, getLanguageForParser(psi), chameleon.getChars());
  }

  @Override
  public @NotNull String getExternalId() {
    return "perl5.file." + this;
  }

  @Override
  public StubBuilder getBuilder() {
    return new DefaultStubBuilder() {
      @Override
      protected @NotNull StubElement<?> createStubForFile(@NotNull PsiFile file) {
        return new PerlFileStub((PerlFile)file, PerlFileElementType.this);
      }
    };
  }

  @Override
  public boolean shouldBuildStubFor(VirtualFile file) {
    return super.shouldBuildStubFor(file);
  }

  @Override
  public void indexStub(@NotNull PerlFileStub stub, @NotNull IndexSink sink) {
    if (stub.isEmpty() && stub.getNamespaceName().equals(PerlPackageUtil.MAIN_NAMESPACE_NAME)) {
      return;
    }
    sink.occurrence(NAMESPACE_KEY, stub.getNamespaceName());
  }

  @Override
  public void serialize(@NotNull PerlFileStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    stub.getData().serialize(dataStream);
  }

  @Override
  public @NotNull PerlFileStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new PerlFileStub(PerlNamespaceDefinitionData.deserialize(dataStream), this);
  }
}
