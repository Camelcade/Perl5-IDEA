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

package com.perl5.lang.mason2.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubIndex;
import com.perl5.lang.htmlmason.MasonCoreUtil;
import com.perl5.lang.mason2.Mason2Util;
import com.perl5.lang.mason2.idea.configuration.MasonSettings;
import com.perl5.lang.mason2.psi.MasonNamespaceDefinition;
import com.perl5.lang.mason2.psi.stubs.MasonNamespaceDefitnitionsStubIndex;
import com.perl5.lang.mason2.psi.stubs.MasonParentNamespacesStubIndex;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.impl.PsiPerlNamespaceDefinitionImpl;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDefinitionStub;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 05.01.2016.
 */
public class MasonNamespaceDefinitionImpl extends PsiPerlNamespaceDefinitionImpl implements MasonNamespaceDefinition {
  protected List<PerlVariableDeclarationElement> myImplicitVariables = null;
  protected int mySettingsChangeCounter;

  public MasonNamespaceDefinitionImpl(ASTNode node) {
    super(node);
  }

  public MasonNamespaceDefinitionImpl(PerlNamespaceDefinitionStub stub, IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @NotNull
  protected List<PerlVariableDeclarationElement> buildImplicitVariables(MasonSettings masonSettings) {
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

  @Nullable
  @Override
  public String getPackageName() {
    String absoluteComponentPath = getAbsoluteComponentPath();
    if (absoluteComponentPath != null) {
      return Mason2Util.getClassnameFromPath(absoluteComponentPath);
    }
    return null;
  }

  @Override
  protected String getPackageNameHeavy() {
    String packageName = Mason2Util.getVirtualFileClassName(getProject(), MasonCoreUtil.getContainingVirtualFile(getContainingFile()));
    return packageName == null ? PerlPackageUtil.MAIN_PACKAGE : packageName;
  }

  @Override
  public List<PerlNamespaceDefinitionElement> getParentNamespaceDefinitions() {
    List<String> parentsPaths;
    PerlNamespaceDefinitionStub stub = getStub();
    if (stub != null) {
      parentsPaths = stub.getParentNamespacesNames();
    }
    else {
      parentsPaths = getParentNamespacesNamesFromPsi();
    }

    VirtualFile containingFile = MasonCoreUtil.getContainingVirtualFile(getContainingFile());
    List<PerlNamespaceDefinitionElement> parentsNamespaces;

    if (!parentsPaths.isEmpty() && containingFile != null) {
      parentsNamespaces = Mason2Util.collectComponentNamespacesByPaths(getProject(), parentsPaths, containingFile.getParent());
    }
    else {
      String autobaseParent = getParentNamespaceFromAutobase();
      if (autobaseParent != null) {
        parentsNamespaces = Mason2Util.getMasonNamespacesByAbsolutePath(getProject(), autobaseParent);
      }
      else {
        parentsNamespaces = new ArrayList<>();
      }
    }

    if (parentsNamespaces.isEmpty()) {
      parentsNamespaces.addAll(PerlPackageUtil.getNamespaceDefinitions(getProject(), MASON_DEFAULT_COMPONENT_PARENT));
    }

    return parentsNamespaces;
  }

  @Nullable
  @Override
  public String getAbsoluteComponentPath() {
    VirtualFile containingFile = MasonCoreUtil.getContainingVirtualFile(getContainingFile());
    if (containingFile != null) {
      return VfsUtil.getRelativePath(containingFile, getProject().getBaseDir());
    }

    return null;
  }

  @Nullable
  @Override
  public String getComponentPath() {
    VirtualFile containingFile = MasonCoreUtil.getContainingVirtualFile(getContainingFile());
    if (containingFile != null) {
      VirtualFile containingRoot = Mason2Util.getComponentRoot(getProject(), containingFile);
      if (containingRoot != null) {
        return VfsUtil.getRelativePath(containingFile, containingRoot);
      }
    }
    return null;
  }

  @Nullable
  protected String getParentNamespaceFromAutobase() {
    // autobase
    VirtualFile componentRoot = getContainingFile().getComponentRoot();
    VirtualFile containingFile = MasonCoreUtil.getContainingVirtualFile(getContainingFile());

    if (componentRoot != null && containingFile != null) {
      VirtualFile parentComponentFile = getParentComponentFile(componentRoot, containingFile.getParent(), containingFile);
      if (parentComponentFile != null) // found autobase class
      {
        String componentPath = VfsUtil.getRelativePath(parentComponentFile, getProject().getBaseDir());

        if (componentPath != null) {
          return componentPath;
        }
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
  @Nullable
  private VirtualFile getParentComponentFile(VirtualFile componentRoot, VirtualFile currentDirectory, VirtualFile childFile) {
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

  @NotNull
  @Override
  public List<PerlNamespaceDefinitionElement> getChildNamespaceDefinitions() {
    MasonSettings masonSettings = MasonSettings.getInstance(getProject());
    final List<PerlNamespaceDefinitionElement> childNamespaces = new ArrayList<>();

    // collect psi children
    final Project project = getProject();
    final GlobalSearchScope projectScope = GlobalSearchScope.projectScope(project);
    final String componentPath = getComponentPath();
    if (componentPath != null) {
      final List<String> relativePaths = new ArrayList<>();
      final List<String> exactPaths = new ArrayList<>();

      StubIndex.getInstance().processAllKeys(MasonParentNamespacesStubIndex.KEY, project, parentPath -> {
        if (parentPath.charAt(0) == VfsUtil.VFS_SEPARATOR_CHAR) // absolute path, should be equal
        {
          if (componentPath.equals(parentPath.substring(1))) {
            exactPaths.add(parentPath);
          }
        }
        else if (componentPath.endsWith(parentPath))    // relative path
        {
          relativePaths.add(parentPath);
        }

        return true;
      });

      for (String parentPath : exactPaths) {
        childNamespaces.addAll(StubIndex.getElements(
          MasonParentNamespacesStubIndex.KEY,
          parentPath,
          project,
          projectScope,
          MasonNamespaceDefinition.class
        ));
      }

      for (String parentPath : relativePaths) {
        for (MasonNamespaceDefinition masonNamespaceDefinition : StubIndex.getElements(
          MasonParentNamespacesStubIndex.KEY,
          parentPath,
          project,
          projectScope,
          MasonNamespaceDefinition.class
        )) {
          if (masonNamespaceDefinition.getParentNamespaceDefinitions().contains(MasonNamespaceDefinitionImpl.this)) {
            childNamespaces.add(masonNamespaceDefinition);
          }
        }
      }
    }

    // collect autobased children
    if (masonSettings.autobaseNames.contains(getContainingFile().getName())) {
      VirtualFile containingFile = MasonCoreUtil.getContainingVirtualFile(getContainingFile());
      if (containingFile != null) {
        final String basePath = VfsUtil.getRelativePath(containingFile.getParent(), getProject().getBaseDir());

        if (basePath != null) {
          final List<String> componentPaths = new ArrayList<>();
          StubIndex.getInstance().processAllKeys(
            MasonNamespaceDefitnitionsStubIndex.KEY, getProject(), componentPath1 -> {
              if (componentPath1.startsWith(basePath)) {
                componentPaths.add(componentPath1);
              }
              return true;
            }
          );

          for (String compoPath : componentPaths) {
            for (MasonNamespaceDefinition namespaceDefinition : StubIndex.getElements(
              MasonNamespaceDefitnitionsStubIndex.KEY,
              compoPath,
              project,
              projectScope,
              MasonNamespaceDefinition.class
            )) {
              if (namespaceDefinition.getParentNamespaceDefinitions().contains(MasonNamespaceDefinitionImpl.this)
                  && !childNamespaces.contains(namespaceDefinition)
                ) {
                childNamespaces.add(namespaceDefinition);
              }
            }
          }
        }
      }
    }
    return childNamespaces;
  }

  @Override
  public String getPresentableName() {
    VirtualFile componentRoot = getContainingFile().getComponentRoot();
    VirtualFile containingFile = MasonCoreUtil.getContainingVirtualFile(getContainingFile());

    if (componentRoot != null && containingFile != null) {
      String componentPath = VfsUtil.getRelativePath(containingFile, componentRoot);

      if (componentPath != null) {
        return VfsUtil.VFS_SEPARATOR_CHAR + componentPath;
      }
    }

    return super.getPresentableName();
  }

  @NotNull
  @Override
  public MasonFileImpl getContainingFile() {
    return (MasonFileImpl)super.getContainingFile();
  }

  @NotNull
  @Override
  public List<PerlVariableDeclarationElement> getImplicitVariables() {
    MasonSettings settings = MasonSettings.getInstance(getProject());
    if (myImplicitVariables == null || mySettingsChangeCounter != settings.getChangeCounter()) {
      myImplicitVariables = buildImplicitVariables(settings);
      mySettingsChangeCounter = settings.getChangeCounter();
    }
    return myImplicitVariables;
  }
}
