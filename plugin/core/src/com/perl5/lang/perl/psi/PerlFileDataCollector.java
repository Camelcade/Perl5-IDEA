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

package com.perl5.lang.perl.psi;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.ClearableLazyValue;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.SmartList;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import com.perl5.lang.perl.util.PerlTimeLogger;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PerlFileDataCollector extends PerlRecursiveVisitor {
  private static final Logger LOG = Logger.getInstance(PerlFileDataCollector.class);
  private final List<PerlNamespaceDefinitionElement> myNamespaces = new SmartList<>();
  private final List<PerlUseStatementElement> myUseStatements = new SmartList<>();
  private final @NotNull PsiElement myRoot;

  public PerlFileDataCollector(@NotNull PsiElement root) {
    myRoot = root;
  }

  @Override
  public void visitElement(@NotNull PsiElement element) {
    if (element instanceof PerlFileDataOwner && !element.equals(myRoot)) {
      processFileDataOwner((PerlFileDataOwner)element);
    }
    else {
      super.visitElement(element);
    }
  }

  private void processFileDataOwner(PerlFileDataOwner element) {
    PerlFileData subtreeData = element.getPerlFileData();
    myNamespaces.addAll(subtreeData.getNamespaces());
    myUseStatements.addAll(subtreeData.getUseStatements());
  }

  @Override
  protected boolean shouldVisitLightElements() {
    return true;
  }

  @Override
  public void visitPerlNamespaceDefinitionWithIdentifier(@NotNull PerlNamespaceDefinitionWithIdentifier o) {
    myNamespaces.add(o);
    super.visitPerlNamespaceDefinitionWithIdentifier(o);
  }

  @Override
  public void visitUseStatement(@NotNull PerlUseStatementElement o) {
    myUseStatements.add(o);
    super.visitUseStatement(o);
  }

  public @NotNull PerlFileData build() {
    myRoot.accept(this);
    return new PerlFileData(myNamespaces, myUseStatements);
  }

  public static @NotNull ClearableLazyValue<PerlFileData> createLazyBuilder(@NotNull PsiElement psiElement) {
    return ClearableLazyValue.create(() -> build(psiElement));
  }

  public static @NotNull PerlFileData build(@NotNull PsiElement psiElement) {
    PerlTimeLogger logger = PerlTimeLogger.create(LOG);
    PerlFileDataCollector collector = new PerlFileDataCollector(psiElement);
    PerlFileData perlFileData = collector.build();
    if (LOG.isDebugEnabled()) {
      String suffix = psiElement instanceof PsiFile ? " " + ((PsiFile)psiElement).getVirtualFile() : "";
      logger.debug("Collected data from AST for ", psiElement, suffix,
                   "; size: ", PerlTimeLogger.kb(psiElement.getTextLength()), " kb ",
                   "; namespaces: ", perlFileData.getNamespaces().size(),
                   "; use statements: ", perlFileData.getUseStatements().size());
    }
    return perlFileData;
  }
}
