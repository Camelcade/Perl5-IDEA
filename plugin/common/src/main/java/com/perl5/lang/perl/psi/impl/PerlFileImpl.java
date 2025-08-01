/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi.impl;

import com.intellij.codeInsight.controlflow.Instruction;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.lang.Language;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.util.ClearableLazyValue;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.fileTypes.PerlFileTypeScript;
import com.perl5.lang.perl.idea.codeInsight.controlFlow.PerlControlFlowBuilder;
import com.perl5.lang.perl.psi.PerlFile;
import com.perl5.lang.perl.psi.mro.PerlMroType;
import com.perl5.lang.perl.psi.references.PerlFileContextSubstitutor;
import com.perl5.lang.perl.psi.stubs.PerlFileStub;
import com.perl5.lang.perl.psi.utils.PerlNamespaceAnnotations;
import com.perl5.lang.perl.psi.utils.PerlResolveUtilCore;
import com.perl5.lang.perl.util.PerlPackageService;
import com.perl5.lang.perl.util.PerlPackageUtilCore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class PerlFileImpl extends PsiFileBase implements PerlFile {
  protected PsiElement fileContext;

  private final ClearableLazyValue<List<String>> myParentNamespaces = ClearableLazyValue.create(
    () -> PerlPackageUtilCore.collectParentNamespaceNamesFromPsi(this));
  private final ClearableLazyValue<Instruction[]> myControlFlow = PerlControlFlowBuilder.createLazy(this);

  public PerlFileImpl(@NotNull FileViewProvider viewProvider, Language language) {
    super(viewProvider, language);
  }

  public PerlFileImpl(@NotNull FileViewProvider viewProvider) {
    super(viewProvider, PerlLanguage.INSTANCE);
  }

  @Override
  public @NotNull FileType getFileType() {
    VirtualFile virtualFile = getVirtualFile();

    if (virtualFile != null) {
      return getVirtualFile().getFileType();
    }
    return getDefaultFileType();
  }

  protected FileType getDefaultFileType() {
    // fixme getViewProvider().getVirtualFile() should be here, but incompatible with IDEA14
    return PerlFileTypeScript.INSTANCE;
  }

  /**
   * Returns package name for this psi file. Name built from filename and innermost root.
   *
   * @return canonical package name or null if it's not pm file or it's not in source root
   */
  public @Nullable String getFilePackageName() {
    return PerlPackageService.getInstance().getFilePackageName(this);
  }

  @Override
  public void subtreeChanged() {
    super.subtreeChanged();
    myParentNamespaces.drop();
    myControlFlow.drop();
  }

  @Override
  public @NotNull String getNamespaceName() {
    return PerlPackageUtilCore.MAIN_NAMESPACE_NAME;
  }

  @Override
  public @NotNull PerlMroType getMroType() {
    return PerlMroType.DFS;
  }

  @Override
  public boolean processDeclarations(@NotNull PsiScopeProcessor processor,
                                     @NotNull ResolveState state,
                                     PsiElement lastParent,
                                     @NotNull PsiElement place) {
    return PerlResolveUtilCore.processChildren(this, processor, state, lastParent, place) && processor.execute(this, state);
  }

  @Override
  public ItemPresentation getPresentation() {
    return this;
  }

  @Override
  public @Nullable String getPresentableText() {
    String result = getFilePackageName();
    return result == null ? getName() : result;
  }

  @Override
  public @Nullable String getLocationString() {
    VirtualFile virtualFile = PsiUtilCore.getVirtualFile(this);
    if (virtualFile == null) {
      return null;
    }
    VirtualFile parentFile = virtualFile.getParent();
    if (parentFile == null) {
      return null;
    }
    var fileIndex = ProjectFileIndex.getInstance(getProject());
    VirtualFile fileRoot = fileIndex.getContentRootForFile(virtualFile);
    if (fileRoot == null) {
      fileRoot = fileIndex.getClassRootForFile(virtualFile);
    }
    if (fileRoot == null) {
      fileRoot = fileIndex.getSourceRootForFile(virtualFile);
    }

    return fileRoot == null ? parentFile.getPresentableUrl() : VfsUtilCore.getRelativePath(parentFile, fileRoot);
  }

  @Override
  public @Nullable Icon getIcon(boolean unused) {
    return getFileType().getIcon();
  }

  @Override
  public @Nullable String getPodLink() {
    return getFilePackageName();
  }

  @Override
  public @Nullable String getPodLinkText() {
    return getPodLink();
  }


  @Override
  public byte @Nullable [] getPerlContentInBytes() {
    return getText().getBytes(getVirtualFile().getCharset());
  }

  @Override
  public PsiElement getContext() {
    return PerlFileContextSubstitutor.getContext(fileContext == null ? super.getContext() : fileContext);
  }

  @Override
  public void setFileContext(PsiElement fileContext) {
    this.fileContext = fileContext;
  }

  // fixme we could use some SmartPsiElementPointer and UserData to hold the context
  @Override
  protected PsiFileImpl clone() {
    PerlFileImpl clone = (PerlFileImpl)super.clone();
    clone.setFileContext(fileContext);
    return clone;
  }

  @Override
  public @NotNull List<String> getParentNamespacesNames() {
    return withGreenStubOrAst(stub -> {
      if (stub instanceof PerlFileStub perlFileStub) {
        return perlFileStub.getParentNamespacesNames();
      }
      return Collections.emptyList();
    }, ast -> myParentNamespaces.getValue());
  }

  @Override
  public @Nullable PerlNamespaceAnnotations getAnnotations() {
    return null;
  }

  @Override
  public @NotNull List<String> getEXPORT() {
    return Collections.emptyList();
  }

  @Override
  public @NotNull List<String> getEXPORT_OK() {
    return Collections.emptyList();
  }

  @Override
  public @NotNull Map<String, List<String>> getEXPORT_TAGS() {
    return Collections.emptyMap();
  }

  @Override
  public @NotNull Instruction[] getControlFlow() {
    return myControlFlow.getValue();
  }
}
