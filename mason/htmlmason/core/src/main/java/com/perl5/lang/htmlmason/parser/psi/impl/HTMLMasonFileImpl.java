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

package com.perl5.lang.htmlmason.parser.psi.impl;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Processor;
import com.perl5.lang.htmlmason.HTMLMasonLanguage;
import com.perl5.lang.htmlmason.HTMLMasonUtil;
import com.perl5.lang.htmlmason.MasonCoreUtil;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonSettings;
import com.perl5.lang.htmlmason.parser.psi.*;
import com.perl5.lang.htmlmason.parser.stubs.HTMLMasonFlagsStubIndex;
import com.perl5.lang.perl.psi.PerlCompositeElement;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.intellij.openapi.vfs.VfsUtilCore.VFS_SEPARATOR;
import static com.intellij.openapi.vfs.VfsUtilCore.VFS_SEPARATOR_CHAR;

public class HTMLMasonFileImpl extends PerlFileImpl implements HTMLMasonFile {
  protected List<PerlVariableDeclarationElement> myImplicitVariables = null;
  protected int myMasonChangeCounter;

  public HTMLMasonFileImpl(@NotNull FileViewProvider viewProvider) {
    super(viewProvider, HTMLMasonLanguage.INSTANCE);
  }

  public @Nullable VirtualFile getComponentRoot() {
    return HTMLMasonUtil.getComponentRoot(getProject(), getComponentVirtualFile());
  }

  public VirtualFile getComponentVirtualFile() {
    return MasonCoreUtil.getContainingVirtualFile(this);
  }

  /**
   * @return absolute path relative to the components root
   */
  public @NlsSafe @Nullable String getAbsoluteComponentPath() {
    VirtualFile componentFile = getComponentVirtualFile();
    VirtualFile componentRoot = getComponentRoot();

    if (componentFile != null && componentRoot != null) {
      return VFS_SEPARATOR + VfsUtilCore.getRelativePath(componentFile, componentRoot);
    }
    return null;
  }

  /**
   * @return absolute containing dir path relative to the components root
   */
  public @NlsSafe @Nullable String getAbsoluteComponentContainerPath() {
    VirtualFile componentFile = getComponentVirtualFile();
    VirtualFile componentRoot = getComponentRoot();

    if (componentFile != null && componentRoot != null) {
      return VFS_SEPARATOR + VfsUtilCore.getRelativePath(componentFile.getParent(), componentRoot);
    }
    return null;
  }

  @Override
  public @NotNull List<PerlVariableDeclarationElement> getImplicitVariables() {
    HTMLMasonSettings settings = HTMLMasonSettings.getInstance(getProject());
    if (myImplicitVariables == null || myMasonChangeCounter != settings.getChangeCounter()) {
      myImplicitVariables = buildImplicitVariables(settings);
      myMasonChangeCounter = settings.getChangeCounter();
    }
    return myImplicitVariables;
  }

  protected List<PerlVariableDeclarationElement> buildImplicitVariables(HTMLMasonSettings settings) {
    List<PerlVariableDeclarationElement> newImplicitVariables = new ArrayList<>();

    if (isValid()) {
      MasonCoreUtil.fillVariablesList(this, newImplicitVariables, settings.globalVariables);
    }
    return newImplicitVariables;
  }

  public @Nullable HTMLMasonFileImpl getParentComponent() {
    String parentComponentPath = getParentComponentPath();
    HTMLMasonSettings settings = HTMLMasonSettings.getInstance(getProject());
    VirtualFile parentFile = null;

    if (parentComponentPath == null) {
      VirtualFile containingFile = getComponentVirtualFile();
      if (containingFile != null) {
        VirtualFile startDir = containingFile.getParent();
        if (StringUtil.equals(containingFile.getName(), settings.autoHandlerName)) {
          startDir = startDir.getParent();
        }

        VirtualFile componentRoot = HTMLMasonUtil.getComponentRoot(getProject(), startDir);
        if (componentRoot != null) {
          while (VfsUtilCore.isAncestor(componentRoot, startDir, false)) {
            if ((parentFile = startDir.findFileByRelativePath(settings.autoHandlerName)) != null) {
              break;
            }
            startDir = startDir.getParent();
          }
        }
      }
    }
    else if (!StringUtil.equals(parentComponentPath, HTMLMasonFlagsStatement.UNDEF_RESULT)) {
      if (StringUtil.startsWith(parentComponentPath, "/")) {
        parentComponentPath = parentComponentPath.substring(1);
        for (VirtualFile root : settings.getComponentsRoots()) {
          if ((parentFile = root.findFileByRelativePath(parentComponentPath)) != null) {
            break;
          }
        }
      }
      else {
        VirtualFile containingVirtualFile = getComponentVirtualFile();
        if (containingVirtualFile != null) {
          VirtualFile containingDir = containingVirtualFile.getParent();
          if (containingDir != null) {
            parentFile = containingDir.findFileByRelativePath(parentComponentPath);
          }
        }
      }
    }

    if (parentFile != null) {
      PsiFile file = PsiManager.getInstance(getProject()).findFile(parentFile);
      if (file instanceof HTMLMasonFileImpl htmlMasonFile) {
        return htmlMasonFile;
      }
    }

    return null;
  }

  public @NotNull List<HTMLMasonFileImpl> getChildComponents() {
    VirtualFile containingFile = getComponentVirtualFile();

    if (containingFile == null) {
      return Collections.emptyList();
    }

    VirtualFile componentRoot = getComponentRoot();

    if (componentRoot == null) {
      return Collections.emptyList();
    }

    final List<HTMLMasonFileImpl> result = new ArrayList<>();
    final String relativePath = VFS_SEPARATOR + VfsUtilCore.getRelativePath(containingFile, componentRoot);
    final Project project = getProject();
    final GlobalSearchScope scope = GlobalSearchScope.allScope(project);
    final HTMLMasonFileImpl currentFile = this;
    HTMLMasonSettings settings = HTMLMasonSettings.getInstance(project);

    // indexed children
    for (String parentPath : StubIndex.getInstance().getAllKeys(HTMLMasonFlagsStubIndex.KEY, project)) {
      boolean isEquals = StringUtil.equals(relativePath, parentPath);
      boolean isRelative = parentPath.length() == 0 || parentPath.charAt(0) != VFS_SEPARATOR_CHAR;

      for (HTMLMasonFlagsStatement statement : StubIndex.getElements(
        HTMLMasonFlagsStubIndex.KEY, parentPath, project, scope, HTMLMasonFlagsStatement.class)) {
        PsiFile file = statement.getContainingFile();
        if (file instanceof HTMLMasonFileImpl htmlMasonFile &&
            (isEquals || isRelative && currentFile.equals(htmlMasonFile.getParentComponent()))) {
          result.add(htmlMasonFile);
        }
      }
    }

    if (StringUtil.equals(containingFile.getName(), settings.autoHandlerName)) {
      collectAutoHandledFiles(PsiManager.getInstance(project), containingFile.getParent(), result, settings.autoHandlerName, null);
    }

    return result;
  }

  protected void collectAutoHandledFiles(@NotNull PsiManager manager,
                                         @Nullable VirtualFile dir,
                                         @NotNull List<HTMLMasonFileImpl> result,
                                         @NotNull String autoHandlerName,
                                         @Nullable Set<VirtualFile> recursionMap) {
    if (dir == null) {
      return;
    }
    if (recursionMap == null) {
      recursionMap = new HashSet<>();
    }
    else {
      VirtualFile autoHandlerVirtualFile = dir.findChild(autoHandlerName);
      if (autoHandlerVirtualFile != null) {
        PsiFile autoHandlerPsiFile = manager.findFile(autoHandlerVirtualFile);
        if (autoHandlerPsiFile instanceof HTMLMasonFileImpl htmlMasonFile &&
            this.equals(htmlMasonFile.getParentComponent())) {
          result.add(htmlMasonFile);
        }
        return;
      }
    }

    recursionMap.add(dir);

    for (VirtualFile file : dir.getChildren()) {
      if (file.isDirectory() && !recursionMap.contains(file)) {
        collectAutoHandledFiles(manager, file, result, autoHandlerName, recursionMap);
      }
      else if (!StringUtil.equals(file.getName(), autoHandlerName)) {
        PsiFile psiFile = manager.findFile(file);
        if (psiFile instanceof HTMLMasonFileImpl htmlMasonFile && this.equals(htmlMasonFile.getParentComponent())) {
          result.add(htmlMasonFile);
        }
      }
    }
  }

  protected @Nullable String getParentComponentPath() {
    HTMLMasonFlagsStatement statement = getFlagsStatement();
    return statement == null ? null : statement.getParentComponentPath();
  }

  public @Nullable HTMLMasonFlagsStatement getFlagsStatement() {
    StubElement<?> stub = getStub();
    FlagsStatementSeeker<PsiElement> seeker;

    if (stub != null) {
      seeker = new FlagsStatementStubSeeker();
      PerlPsiUtil.processElementsFromStubs(stub, seeker, null);
    }
    else {
      seeker = new FlagsStatementPsiSeeker();
      PerlPsiUtil.processElementsFromPsi(this, seeker, null);
    }
    return seeker.getResult();
  }

  @Override
  public boolean processDeclarations(@NotNull PsiScopeProcessor processor,
                                     @NotNull ResolveState state,
                                     PsiElement lastParent,
                                     @NotNull PsiElement place) {
    boolean checkShared = false;
    boolean checkArgs = false;
    boolean checkInit = false;
    boolean checkCode = false;
    boolean checkCleanup = false;

    PsiElement onceAnchor = null;
    PsiElement sharedAnchor = null;
    PsiElement argsAnchor = null;
    PsiElement initAnchor = null;
    PsiElement cleanupAnchor = null;

    if (lastParent instanceof HTMLMasonSharedBlockImpl) {
      checkShared = true;
      sharedAnchor = lastParent;
    }
    else if (lastParent instanceof HTMLMasonArgsBlockImpl) {
      checkShared = true;
      checkArgs = true;
      argsAnchor = lastParent;
    }
    else if (lastParent instanceof HTMLMasonInitBlockImpl) {
      checkArgs = true;
      checkShared = true;
      checkInit = true;
      initAnchor = lastParent;
    }
    else if (lastParent instanceof HTMLMasonFilterBlockImpl) {
      checkArgs = true;
      checkShared = true;
    }
    else if (lastParent instanceof HTMLMasonOnceBlock) {
      onceAnchor = lastParent;
    }
    else if (!(lastParent instanceof HTMLMasonSubcomponentDefitnitionImpl || lastParent instanceof HTMLMasonMethodDefinitionImpl)) {
      checkArgs = true;
      checkShared = true;
      checkInit = true;
      checkCode = true;

      if (lastParent instanceof HTMLMasonCleanupBlockImpl) {
        checkCleanup = true;
        cleanupAnchor = lastParent;
        lastParent = null;
      }
    }

    if (checkCode) {
      if (!processChildren(this, processor, state, lastParent, place)) {
        return false;
      }
    }
    if (checkCleanup) {
      if (!checkSubblocks(processor, state, place, HTMLMasonCleanupBlock.class, cleanupAnchor)) {
        return false;
      }
    }
    if (checkInit) {
      if (!checkSubblocks(processor, state, place, HTMLMasonInitBlock.class, initAnchor)) {
        return false;
      }
    }
    if (checkArgs) {
      if (!checkSubblocks(processor, state, place, HTMLMasonArgsBlock.class, argsAnchor)) {
        return false;
      }
    }
    if (checkShared) {
      if (!checkSubblocks(processor, state, place, HTMLMasonSharedBlock.class, sharedAnchor)) {
        return false;
      }
    }

    if (!checkSubblocks(processor, state, place, HTMLMasonOnceBlock.class, onceAnchor)) {
      return false;
    }

    // implicit variables
    for (PerlVariableDeclarationElement wrapper : getImplicitVariables()) {
      if (!processor.execute(wrapper, state)) {
        return false;
      }
    }

    return false;
  }

  @SuppressWarnings("Duplicates")
  protected boolean checkSubblocks(
    @NotNull PsiScopeProcessor processor,
    @NotNull ResolveState state,
    @NotNull PsiElement place,
    @NotNull Class<? extends HTMLMasonCompositeElement> clazz,
    @Nullable PsiElement anchor
  ) {
    List<HTMLMasonCompositeElement> elements = getBlocksMap().get(clazz);

    for (int i = elements.size() - 1; i >= 0; i--) {
      HTMLMasonCompositeElement element = elements.get(i);
      if (anchor == null && !element.processDeclarationsForReal(processor, state, null, place)) {
        return false;
      }
      else if (anchor != null && anchor.equals(element)) {
        anchor = null;
      }
    }

    return true;
  }

  public List<HTMLMasonCompositeElement> getSubComponentsDefinitions() {
    return getBlocksMap().get(HTMLMasonSubcomponentDefitnition.class);
  }

  /**
   * Recursively looking for method in child components
   *
   * @param name method name
   * @return list of child components
   */
  public @NotNull List<HTMLMasonMethodDefinition> findMethodDefinitionByNameInChildComponents(String name) {
    List<HTMLMasonMethodDefinition> result = new ArrayList<>();
    Set<HTMLMasonFileImpl> recursionSet = new HashSet<>();

    collectMethodDefinitionByNameInChildComponents(name, result, recursionSet);

    return result;
  }

  protected void collectMethodDefinitionByNameInChildComponents(String name,
                                                                List<HTMLMasonMethodDefinition> result,
                                                                Set<HTMLMasonFileImpl> recursionSet) {
    for (HTMLMasonFileImpl childComponent : getChildComponents()) {
      if (!recursionSet.contains(childComponent)) {
        recursionSet.add(childComponent);
        HTMLMasonMethodDefinition methodDefinition = childComponent.getMethodDefinitionByName(name);
        if (methodDefinition != null) {
          result.add(methodDefinition);
        }
        else {
          childComponent.collectMethodDefinitionByNameInChildComponents(name, result, recursionSet);
        }
      }
    }
  }

  /**
   * Recursively looking for method in parent components
   *
   * @param name method name
   * @return method definition or null
   */
  public @Nullable HTMLMasonMethodDefinition findMethodDefinitionByNameInParents(String name) {
    HTMLMasonFileImpl parentComponent = getParentComponent();
    return parentComponent == null ? null : parentComponent.findMethodDefinitionByNameInThisOrParents(name);
  }

  /**
   * Recursively looking for method in current or parent components
   *
   * @param name method name
   * @return method definition or null
   */
  public @Nullable HTMLMasonMethodDefinition findMethodDefinitionByNameInThisOrParents(String name) {
    HTMLMasonMethodDefinitionSeeker seeker = new HTMLMasonMethodDefinitionSeeker(name);
    processMethodDefinitionsInThisOrParents(seeker);
    return seeker.getResult();
  }

  public boolean processMethodDefinitionsInThisOrParents(Processor<HTMLMasonMethodDefinition> processor) {
    return processMethodDefinitionsInThisOrParents(processor, new HashSet<>());
  }

  protected boolean processMethodDefinitionsInThisOrParents(Processor<HTMLMasonMethodDefinition> processor,
                                                            Set<HTMLMasonFileImpl> recursionSet) {
    if (recursionSet.contains(this)) {
      return false;
    }
    recursionSet.add(this);

    if (!processMethodDefinitions(processor)) {
      return false;
    }

    HTMLMasonFileImpl parentComponent = getParentComponent();

    return parentComponent != null && parentComponent.processMethodDefinitionsInThisOrParents(processor, recursionSet);
  }

  public @Nullable HTMLMasonMethodDefinition getMethodDefinitionByName(String name) {
    HTMLMasonMethodDefinitionSeeker seeker = new HTMLMasonMethodDefinitionSeeker(name);
    processMethodDefinitions(seeker);
    return seeker.getResult();
  }

  protected boolean processMethodDefinitions(Processor<HTMLMasonMethodDefinition> processor) {
    for (HTMLMasonCompositeElement methodDefinition : getMethodsDefinitions()) {
      assert methodDefinition instanceof HTMLMasonMethodDefinition : "got " + methodDefinition + " instead of method definition";
      if (!processor.process((HTMLMasonMethodDefinition)methodDefinition)) {
        return false;
      }
    }
    return true;
  }

  public List<HTMLMasonCompositeElement> getMethodsDefinitions() {
    StubElement<?> parentStub = getStub();
    if (parentStub != null) {
      final List<HTMLMasonCompositeElement> result = new ArrayList<>();
      PerlPsiUtil.processElementsFromStubs(parentStub, psi ->
      {
        if (psi instanceof HTMLMasonMethodDefinition methodDefinition) {
          result.add(methodDefinition);
        }
        return true;
      }, null);

      return result;
    }
    return getBlocksMap().get(HTMLMasonMethodDefinition.class);
  }

  @Override
  public @NotNull List<HTMLMasonCompositeElement> getArgsBlocks() {
    StubElement<?> rootStub = getStub();

    //noinspection Duplicates in HTMLMasonStubBasedNamedElementImpl
    if (rootStub != null) {
      final List<HTMLMasonCompositeElement> result = new ArrayList<>();

      PerlPsiUtil.processElementsFromStubs(
        rootStub,
        psi ->
        {
          if (psi instanceof HTMLMasonArgsBlock argsBlock) {
            result.add(argsBlock);
          }
          return true;
        },
        HTMLMasonNamedElement.class
      );
      return result;
    }

    return getBlocksMap().get(HTMLMasonArgsBlock.class);
  }

  @Override
  public byte @Nullable [] getPerlContentInBytes() {
    return null;
  }

  private Map<Class<? extends HTMLMasonCompositeElement>, List<HTMLMasonCompositeElement>> getBlocksMap() {
    return CachedValuesManager.getCachedValue(this, () ->
    {
      Map<Class<? extends HTMLMasonCompositeElement>, List<HTMLMasonCompositeElement>> result = new HashMap<>();

      final List<HTMLMasonCompositeElement> cleanupResult = new ArrayList<>();
      final List<HTMLMasonCompositeElement> initResult = new ArrayList<>();
      final List<HTMLMasonCompositeElement> argsResult = new ArrayList<>();
      final List<HTMLMasonCompositeElement> sharedResult = new ArrayList<>();
      final List<HTMLMasonCompositeElement> onceResult = new ArrayList<>();
      final List<HTMLMasonCompositeElement> methodsResult = new ArrayList<>();
      final List<HTMLMasonCompositeElement> subComponentsResult = new ArrayList<>();

      result.put(HTMLMasonOnceBlock.class, onceResult);
      result.put(HTMLMasonSharedBlock.class, sharedResult);
      result.put(HTMLMasonInitBlock.class, initResult);
      result.put(HTMLMasonArgsBlock.class, argsResult);
      result.put(HTMLMasonCleanupBlock.class, cleanupResult);
      result.put(HTMLMasonMethodDefinition.class, methodsResult);
      result.put(HTMLMasonSubcomponentDefitnition.class, subComponentsResult);

      PsiTreeUtil.processElements(HTMLMasonFileImpl.this, element ->
      {
        switch (element) {
          case HTMLMasonOnceBlock block -> onceResult.add(block);
          case HTMLMasonSharedBlock block -> sharedResult.add(block);
          case HTMLMasonCleanupBlock block -> cleanupResult.add(block);
          case HTMLMasonInitBlock block
            when HTMLMasonFileImpl.this.equals(PsiTreeUtil.getParentOfType(element, HTMLMasonArgsContainer.class)) -> initResult.add(block);
          case HTMLMasonArgsBlock block
            when HTMLMasonFileImpl.this.equals(PsiTreeUtil.getParentOfType(element, HTMLMasonArgsContainer.class)) -> argsResult.add(block);
          case HTMLMasonMethodDefinition definition -> methodsResult.add(definition);
          case HTMLMasonSubcomponentDefitnition defitnition -> subComponentsResult.add(defitnition);
          default -> {
          }
        }

        return true;
      });

      return CachedValueProvider.Result.create(result, HTMLMasonFileImpl.this);
    });
  }

  public static boolean processChildren(@NotNull PsiElement element,
                                        @NotNull PsiScopeProcessor processor,
                                        @NotNull ResolveState resolveState,
                                        @Nullable PsiElement lastParent,
                                        @NotNull PsiElement place) {
    PsiElement run = lastParent == null ? element.getLastChild() : lastParent.getPrevSibling();
    while (run != null) {
      if (run instanceof PerlCompositeElement &&
          !(run instanceof PerlLexicalScope) &&
          !run.processDeclarations(processor, resolveState, null, place)
      ) {
        return false;
      }
      run = run.getPrevSibling();
    }

    return true;
  }

  protected abstract static class FlagsStatementSeeker<T> implements Processor<T> {
    protected HTMLMasonFlagsStatement myResult = null;

    public HTMLMasonFlagsStatement getResult() {
      return myResult;
    }
  }

  protected static class FlagsStatementStubSeeker extends FlagsStatementSeeker<PsiElement> {
    @Override
    public boolean process(PsiElement psi) {
      if (psi instanceof HTMLMasonFlagsStatement flagsStatement) {
        myResult = flagsStatement;
        return false;
      }
      return true;
    }
  }

  protected static class FlagsStatementPsiSeeker extends FlagsStatementSeeker<PsiElement> {
    @Override
    public boolean process(PsiElement element) {
      if (element instanceof HTMLMasonFlagsStatement flagsStatement) {
        myResult = flagsStatement;
        return false;
      }
      return true;
    }
  }

  protected static class HTMLMasonMethodDefinitionSeeker implements Processor<HTMLMasonMethodDefinition> {
    private final String myName;
    private HTMLMasonMethodDefinition myResult;

    public HTMLMasonMethodDefinitionSeeker(String myName) {
      this.myName = myName;
    }

    @Override
    public boolean process(HTMLMasonMethodDefinition htmlMasonMethodDefinition) {
      if (StringUtil.equals(myName, htmlMasonMethodDefinition.getName())) {
        myResult = htmlMasonMethodDefinition;
        return false;
      }
      return true;
    }

    public HTMLMasonMethodDefinition getResult() {
      return myResult;
    }
  }
}
