/*
 * Copyright 2015-2019 Alexandr Evstigneev
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
import com.intellij.lexer.Lexer;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.StubBuilder;
import com.intellij.psi.stubs.*;
import com.intellij.psi.tree.IStubFileElementType;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValuesManager;
import com.perl5.lang.perl.psi.PerlFile;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDefinitionData;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class PerlFileElementType extends IStubFileElementType<PerlFileStub> {
  private static final int VERSION = 5;

  public PerlFileElementType(String debugName, Language language) {
    super(debugName, language);
  }

  @Override
  public int getStubVersion() {
    return super.getStubVersion() + VERSION + PerlValuesManager.VERSION;
  }

  @Override
  protected ASTNode doParseContents(@NotNull ASTNode chameleon, @NotNull PsiElement psi) {
    return getParser(psi).parse(this, getBuilder(psi, chameleon)).getFirstChildNode();
  }

  @Nullable
  protected Lexer getLexer(PsiElement psi) {
    return null;
  }

  @NotNull
  protected PsiParser getParser(PsiElement psi) {
    return LanguageParserDefinitions.INSTANCE.forLanguage(getLanguageForParser(psi)).createParser(psi.getProject());
  }

  @NotNull
  protected PsiBuilder getBuilder(PsiElement psi, ASTNode chameleon) {
    return PsiBuilderFactory.getInstance()
      .createBuilder(psi.getProject(), chameleon, getLexer(psi), getLanguageForParser(psi), chameleon.getChars());
  }

  @NotNull
  @Override
  public String getExternalId() {
    return "perl5.file." + toString();
  }

  @Override
  public StubBuilder getBuilder() {
    return new DefaultStubBuilder() {
      @NotNull
      @Override
      protected StubElement createStubForFile(@NotNull PsiFile file) {
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
    sink.occurrence(PerlNamespaceIndex.KEY, stub.getNamespaceName());
  }

  @Override
  public void serialize(@NotNull PerlFileStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    stub.getData().serialize(dataStream);
  }

  @NotNull
  @Override
  public PerlFileStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new PerlFileStub(PerlNamespaceDefinitionData.deserialize(dataStream), this);
  }
}
