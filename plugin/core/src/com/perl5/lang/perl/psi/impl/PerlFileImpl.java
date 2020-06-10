/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.lang.Language;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.impl.DirectoryInfo;
import com.intellij.openapi.roots.impl.ProjectFileIndexImpl;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.ObjectStubTree;
import com.intellij.psi.stubs.PsiFileStub;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubTreeLoader;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.extensions.PerlCodeGenerator;
import com.perl5.lang.perl.extensions.generation.PerlCodeGeneratorImpl;
import com.perl5.lang.perl.fileTypes.PerlFileTypePackage;
import com.perl5.lang.perl.fileTypes.PerlFileTypeScript;
import com.perl5.lang.perl.psi.PerlDoExpr;
import com.perl5.lang.perl.psi.PerlFile;
import com.perl5.lang.perl.psi.mro.PerlMroType;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.stubs.PerlFileStub;
import com.perl5.lang.perl.psi.stubs.imports.PerlUseStatementStub;
import com.perl5.lang.perl.psi.stubs.imports.runtime.PerlRuntimeImportStub;
import com.perl5.lang.perl.psi.utils.PerlNamespaceAnnotations;
import com.perl5.lang.perl.psi.utils.PerlResolveUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class PerlFileImpl extends PsiFileBase implements PerlFile {
  protected GlobalSearchScope myElementsResolveScope;
  protected PsiElement fileContext;

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


  @Override
  public PerlLexicalScope getLexicalScope() {
    return null;
  }

  /**
   * Returns package name for this psi file. Name built from filename and innermost root.
   *
   * @return canonical package name or null if it's not pm file or it's not in source root
   */
  public @Nullable String getFilePackageName() {
    VirtualFile containingFile = getVirtualFile();

    if (containingFile != null && containingFile.getFileType() == PerlFileTypePackage.INSTANCE) {
      VirtualFile innermostSourceRoot = PerlPackageUtil.getFileClassRoot(getProject(), containingFile);
      if (innermostSourceRoot != null) {
        String relativePath = VfsUtil.getRelativePath(containingFile, innermostSourceRoot);
        return PerlPackageUtil.getPackageNameByPath(relativePath);
      }
    }
    return null;
  }

  @Override
  public void subtreeChanged() {
    super.subtreeChanged();
    myElementsResolveScope = null;
  }

  @Override
  public @NotNull String getNamespaceName() {
    return PerlPackageUtil.MAIN_NAMESPACE_NAME;
  }

  @Override
  public @NotNull PerlMroType getMroType() {
    return PerlMroType.DFS;
  }

  //	@Override
  //	@NotNull
  //	public GlobalSearchScope getElementsResolveScope()
  //	{
  //		if (myElementsResolveScope != null)
  //			return myElementsResolveScope;
  //
  //		long t = System.currentTimeMillis();
  //		Set<VirtualFile> filesToSearch = new THashSet<VirtualFile>();
  //		collectIncludedFiles(filesToSearch);
  //		myElementsResolveScope = GlobalSearchScope.filesScope(getProject(), filesToSearch);
  //
  //		System.err.println("Collected in ms: " + (System.currentTimeMillis() - t) / 1000);
  //
  //		return myElementsResolveScope;
  //	}

  @Override
  public void collectIncludedFiles(Set<VirtualFile> includedVirtualFiles) {
    if (!includedVirtualFiles.contains(getVirtualFile())) {
      includedVirtualFiles.add(getVirtualFile());

      StubElement<?> fileStub = getStub();

      if (fileStub == null) {
        //				System.err.println("Collecting from psi for " + getVirtualFile());
        collectRequiresFromPsi(this, includedVirtualFiles);
      }
      else {
        //				System.err.println("Collecting from stubs for " + getVirtualFile());
        collectRequiresFromStub(fileStub, includedVirtualFiles);
      }
    }
  }

  protected void collectRequiresFromVirtualFile(VirtualFile virtualFile, Set<VirtualFile> includedVirtualFiles) {
    if (!includedVirtualFiles.contains(virtualFile)) {
      includedVirtualFiles.add(virtualFile);

      ObjectStubTree<?> objectStubTree = StubTreeLoader.getInstance().readOrBuild(getProject(), virtualFile, null);
      if (objectStubTree != null) {
        //				System.err.println("Collecting from stub for " + virtualFile);
        collectRequiresFromStub((PsiFileStub<?>)objectStubTree.getRoot(), includedVirtualFiles);
      }
      else {
        //				System.err.println("Collecting from psi for " + virtualFile);
        PsiFile targetPsiFile = PsiManager.getInstance(getProject()).findFile(virtualFile);
        if (targetPsiFile != null) {
          collectRequiresFromPsi(targetPsiFile, includedVirtualFiles);
        }
      }
    }
  }

  protected void collectRequiresFromStub(@NotNull StubElement<?> currentStub, Set<VirtualFile> includedVirtualFiles) {
    VirtualFile virtualFile = null;
    if (currentStub instanceof PerlUseStatementStub) {
      String packageName = ((PerlUseStatementStub)currentStub).getPackageName();
      virtualFile = PerlPackageUtil.resolvePackageNameToVirtualFile(this, packageName);
    }
    if (currentStub instanceof PerlRuntimeImportStub) {
      String importPath = ((PerlRuntimeImportStub)currentStub).getImportPath();
      if (importPath != null) {
        virtualFile = PerlPackageUtil.resolveRelativePathToVirtualFile(this, importPath);
      }
    }

    if (virtualFile != null) {
      collectRequiresFromVirtualFile(virtualFile, includedVirtualFiles);
    }

    for (Object childStub : currentStub.getChildrenStubs()) {
      assert childStub instanceof StubElement : childStub.getClass();
      collectRequiresFromStub((StubElement<?>)childStub, includedVirtualFiles);
    }
  }


  /**
   * Collects required files from psi structure, used in currently modified document
   *
   * @param includedVirtualFiles set of already collected virtual files
   */
  protected void collectRequiresFromPsi(PsiFile psiFile, Set<VirtualFile> includedVirtualFiles) {
    for (PsiElement importStatement : PsiTreeUtil.<PsiElement>findChildrenOfAnyType(psiFile, PerlUseStatementElement.class,
                                                                                    PerlDoExpr.class)) {
      VirtualFile virtualFile = null;
      if (importStatement instanceof PerlUseStatementElement) {
        String packageName = ((PerlUseStatementElement)importStatement).getPackageName();
        if (packageName != null) {
          virtualFile = PerlPackageUtil.resolvePackageNameToVirtualFile(this, packageName);
        }
      }
      else if (importStatement instanceof PerlDoExpr) {
        String importPath = ((PerlDoExpr)importStatement).getImportPath();

        if (importPath != null) {
          virtualFile = PerlPackageUtil.resolveRelativePathToVirtualFile(this, ((PerlDoExpr)importStatement).getImportPath());
        }
      }

      if (virtualFile != null) {
        collectRequiresFromVirtualFile(virtualFile, includedVirtualFiles);
      }
    }
  }

  @Override
  public boolean processDeclarations(@NotNull PsiScopeProcessor processor,
                                     @NotNull ResolveState state,
                                     PsiElement lastParent,
                                     @NotNull PsiElement place) {
    return PerlResolveUtil.processChildren(this, processor, state, lastParent, place) && processor.execute(this, state);
  }

  @Override
  public PerlCodeGenerator getCodeGenerator() {
    return PerlCodeGeneratorImpl.INSTANCE;
  }

/* This method is to get ElementTypes stats from PsiFile using PSIViewer
        public String getTokensStats()
	{
		final Map<IElementType, Integer> TOKENS_STATS = new THashMap<IElementType, Integer>();

		accept(new PerlRecursiveVisitor(){

			@Override
			public void visitElement(PsiElement element)
			{
				IElementType elementType = element.getNode().getElementType();
				if( elementType instanceof PerlElementType)
				{
					if (!TOKENS_STATS.containsKey(elementType))
					{
						TOKENS_STATS.put(elementType, 1);
					}
					else
					{
						TOKENS_STATS.put(elementType, TOKENS_STATS.get(elementType) + 1);
					}
				}
				super.visitElement(element);
			}
		});

		for (IElementType type: TOKENS_STATS.keySet())
		{
			System.err.println(type+ ";" + TOKENS_STATS.get(type));
		}

		return "";
	}
*/

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
    ProjectFileIndex index = ProjectFileIndex.SERVICE.getInstance(getProject());
    DirectoryInfo fileInfo = ((ProjectFileIndexImpl)index).getInfoForFileOrDirectory(virtualFile);
    VirtualFile contentRoot = ObjectUtils.coalesce(fileInfo.getContentRoot(), fileInfo.getLibraryClassRoot(), fileInfo.getSourceRoot());
    if (contentRoot == null) {
      return parentFile.getPresentableUrl();
    }
    return VfsUtil.getRelativePath(parentFile, contentRoot);
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
  public byte[] getPerlContentInBytes() {
    return getText().getBytes(getVirtualFile().getCharset());
  }

  @Override
  public PsiElement getContext() {
    return fileContext == null ? super.getContext() : fileContext;
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
    StubElement<?> stub = getGreenStub();
    if (stub instanceof PerlFileStub) {
      return ((PerlFileStub)stub).getParentNamespacesNames();
    }
    return PerlPackageUtil.collectParentNamespacesFromPsi(this);
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
}
