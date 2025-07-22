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

package com.perl5.lang.htmlmason.parser.psi.impl;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Processor;
import com.perl5.lang.htmlmason.HTMLMasonLanguage;
import com.perl5.lang.htmlmason.MasonCoreUtilCore;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonSettings;
import com.perl5.lang.htmlmason.parser.psi.*;
import com.perl5.lang.perl.psi.PerlCompositeElement;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HTMLMasonFileImpl extends PerlFileImpl implements HTMLMasonFile {
  protected List<PerlVariableDeclarationElement> myImplicitVariables = null;
  protected int myMasonChangeCounter;

  public HTMLMasonFileImpl(@NotNull FileViewProvider viewProvider) {
    super(viewProvider, HTMLMasonLanguage.INSTANCE);
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
      MasonCoreUtilCore.fillVariablesList(this, newImplicitVariables, settings.globalVariables);
    }
    return newImplicitVariables;
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

  public @Nullable HTMLMasonMethodDefinition getMethodDefinitionByName(String name) {
    HTMLMasonMethodDefinitionSeeker seeker = new HTMLMasonMethodDefinitionSeeker(name);
    processMethodDefinitions(seeker);
    return seeker.getResult();
  }

  public boolean processMethodDefinitions(Processor<? super HTMLMasonMethodDefinition> processor) {
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

  public static class HTMLMasonMethodDefinitionSeeker implements Processor<HTMLMasonMethodDefinition> {
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
