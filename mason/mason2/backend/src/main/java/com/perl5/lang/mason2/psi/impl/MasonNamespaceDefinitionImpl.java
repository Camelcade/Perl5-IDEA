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

package com.perl5.lang.mason2.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.htmlmason.MasonCoreUtil;
import com.perl5.lang.mason2.Mason2Util;
import com.perl5.lang.mason2.idea.configuration.MasonSettings;
import com.perl5.lang.mason2.psi.MasonNamespaceDefinition;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.impl.PsiPerlNamespaceDefinitionImpl;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDefinitionStub;
import com.perl5.lang.perl.util.PerlFileUtil;
import com.perl5.lang.perl.util.PerlPackageUtilCore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class MasonNamespaceDefinitionImpl extends PsiPerlNamespaceDefinitionImpl implements MasonNamespaceDefinition {
  protected List<PerlVariableDeclarationElement> myImplicitVariables = null;
  protected int mySettingsChangeCounter;

  public MasonNamespaceDefinitionImpl(ASTNode node) {
    super(node);
  }

  public MasonNamespaceDefinitionImpl(PerlNamespaceDefinitionStub stub, IElementType nodeType) {
    super(stub, nodeType);
  }

  protected @NotNull List<PerlVariableDeclarationElement> buildImplicitVariables(MasonSettings masonSettings) {
    List<PerlVariableDeclarationElement> newImplicitVariables = new ArrayList<>();

    if (isValid()) {
      MasonCoreUtil.fillVariablesList(this, newImplicitVariables, masonSettings.globalVariables);
    }
    return newImplicitVariables;
  }

  @Override
  public PerlNamespaceElement getNamespaceElement() {
    return null;
  }

  @Override
  public @Nullable String getNamespaceName() {
    String absoluteComponentPath = getAbsoluteComponentPath();
    if (absoluteComponentPath != null) {
      return Mason2Util.getClassnameFromPath(absoluteComponentPath);
    }
    return null;
  }

  @Override
  protected @Nullable String computeNamespaceName() {
    String packageName = Mason2Util.getVirtualFileClassName(getProject(), MasonCoreUtil.getContainingVirtualFile(getContainingFile()));
    return packageName == null ? PerlPackageUtilCore.MAIN_NAMESPACE_NAME : packageName;
  }

  @Override
  public @Nullable String getAbsoluteComponentPath() {
    VirtualFile containingFile = MasonCoreUtil.getContainingVirtualFile(getContainingFile());
    if (containingFile != null) {
      return PerlFileUtil.getPathRelativeToContentRoot(containingFile, getProject());
    }

    return null;
  }

  @Override
  public @Nullable String getComponentPath() {
    VirtualFile containingFile = MasonCoreUtil.getContainingVirtualFile(getContainingFile());
    if (containingFile != null) {
      VirtualFile containingRoot = Mason2Util.getComponentRoot(getProject(), containingFile);
      if (containingRoot != null) {
        return VfsUtilCore.getRelativePath(containingFile, containingRoot);
      }
    }
    return null;
  }

  protected @Nullable String getParentNamespaceFromAutobase() {
    // autobase
    VirtualFile componentRoot = getContainingFile().getComponentRoot();
    VirtualFile containingFile = MasonCoreUtil.getContainingVirtualFile(getContainingFile());

    if (componentRoot != null && containingFile != null) {
      VirtualFile parentComponentFile = getParentComponentFile(componentRoot, containingFile.getParent(), containingFile);
      if (parentComponentFile != null) // found autobase class
      {
        String componentPath = PerlFileUtil.getPathRelativeToContentRoot(parentComponentFile, getProject());

        return componentPath;
      }
    }
    return null;
  }

  /**
   * Recursively traversing paths and looking for autobase
   *
   * @param componentRoot    component root we are search in
   * @param currentDirectory directory we are currently in	 *
   * @param childFile        current file (just to speed things up)
   * @return parent component virtual file or null if not found
   */
  private @Nullable VirtualFile getParentComponentFile(VirtualFile componentRoot, VirtualFile currentDirectory, VirtualFile childFile) {
    // check in current dir
    List<String> autobaseNames = new ArrayList<>(MasonSettings.getInstance(getProject()).autobaseNames);

    if (childFile.getParent().equals(currentDirectory) && autobaseNames.contains(childFile.getName())) // avoid cyclic inheritance
    {
      autobaseNames = autobaseNames.subList(0, autobaseNames.lastIndexOf(childFile.getName()));
    }

    for (int i = autobaseNames.size() - 1; i >= 0; i--) {
      VirtualFile potentialParent = VfsUtil.findRelativeFile(currentDirectory, autobaseNames.get(i));
      if (potentialParent != null && potentialParent.exists() && !potentialParent.equals(childFile)) {
        return potentialParent;
      }
    }

    // move up or exit
    if (!componentRoot.equals(currentDirectory)) {
      return getParentComponentFile(componentRoot, currentDirectory.getParent(), childFile);
    }
    return null;
  }

  @Override
  public String getPresentableName() {
    VirtualFile componentRoot = getContainingFile().getComponentRoot();
    VirtualFile containingFile = MasonCoreUtil.getContainingVirtualFile(getContainingFile());

    if (componentRoot != null && containingFile != null) {
      String componentPath = VfsUtilCore.getRelativePath(containingFile, componentRoot);

      if (componentPath != null) {
        return VfsUtil.VFS_SEPARATOR_CHAR + componentPath;
      }
    }

    return super.getPresentableName();
  }

  @Override
  public @NotNull MasonFileImpl getContainingFile() {
    return (MasonFileImpl)super.getContainingFile();
  }

  @Override
  public @NotNull List<PerlVariableDeclarationElement> getImplicitVariables() {
    MasonSettings settings = MasonSettings.getInstance(getProject());
    if (myImplicitVariables == null || mySettingsChangeCounter != settings.getChangeCounter()) {
      myImplicitVariables = buildImplicitVariables(settings);
      mySettingsChangeCounter = settings.getChangeCounter();
    }
    return myImplicitVariables;
  }
}
