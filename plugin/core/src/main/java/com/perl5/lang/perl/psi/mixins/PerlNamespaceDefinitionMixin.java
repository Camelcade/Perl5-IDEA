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

package com.perl5.lang.perl.psi.mixins;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.ClearableLazyValue;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.Processor;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.extensions.packageprocessor.PerlMroProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessor;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationSimple;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import com.perl5.lang.perl.psi.mro.PerlMroType;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDefinitionStub;
import com.perl5.lang.perl.psi.utils.PerlNamespaceAnnotations;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.perl5.lang.perl.idea.PerlElementPatterns.*;


public abstract class PerlNamespaceDefinitionMixin extends PerlStubBasedPsiElementBase<PerlNamespaceDefinitionStub>
  implements StubBasedPsiElement<PerlNamespaceDefinitionStub>,
             PerlNamespaceDefinitionWithIdentifier,
             PerlCompositeElement {
  private final ClearableLazyValue<ExporterInfo> myExporterInfo = ClearableLazyValue.create(this::computeExporterInfo);
  private final ClearableLazyValue<PerlMroType> myMroType = ClearableLazyValue.create(this::computeMroType);
  private final ClearableLazyValue<List<String>> myParentNamespaces = ClearableLazyValue.create(
    () -> PerlPackageUtil.collectParentNamespaceNamesFromPsi(this));

  public PerlNamespaceDefinitionMixin(@NotNull ASTNode node) {
    super(node);
  }

  public PerlNamespaceDefinitionMixin(@NotNull PerlNamespaceDefinitionStub stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public @Nullable PsiPerlNamespaceContent getNamespaceContent() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlNamespaceContent.class);
  }

  @Override
  public @Nullable String getName() {
    return getNamespaceName();
  }

  @Override
  public @Nullable PerlNamespaceElement getNamespaceElement() {
    return findChildByClass(PerlNamespaceElement.class);
  }

  @Override
  public @Nullable PsiElement getNameIdentifier() {
    return getNamespaceElement();
  }

  @Override
  public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
    return PerlPsiUtil.renameNamedElement(this, name);
  }


  @Override
  public @Nullable String getNamespaceName() {
    PerlNamespaceDefinitionStub stub = getGreenStub();
    if (stub != null) {
      return stub.getNamespaceName();
    }

    return computeNamespaceName();
  }

  protected String computeNamespaceName() {
    PerlNamespaceElement namespaceElement = getNamespaceElement();
    if (namespaceElement != null) {
      return namespaceElement.getCanonicalName();
    }

    return null;
  }

  @Override
  public @NotNull List<String> getParentNamespacesNames() {
    PerlNamespaceDefinitionStub stub = getGreenStub();
    if (stub != null) {
      return stub.getParentNamespacesNames();
    }
    return myParentNamespaces.getValue();
  }

  @Override
  public @Nullable Icon getIcon(int flags) {
    return PerlIcons.PACKAGE_GUTTER_ICON;
  }

  @Override
  public ItemPresentation getPresentation() {
    return new PerlItemPresentationSimple(this, getPresentableName());
  }

  @Override
  public @NotNull PerlMroType getMroType() {
    PerlNamespaceDefinitionStub stub = getGreenStub();
    if (stub != null) {
      return stub.getMroType();
    }

    return myMroType.getValue();
  }

  private @NotNull PerlMroType computeMroType() {
    MroSearcher searcher = new MroSearcher();
    PerlPsiUtil.processNamespaceStatements(this, searcher);
    return searcher.getResult();
  }


  @Override
  public @NotNull List<String> getEXPORT() {
    PerlNamespaceDefinitionStub stub = getGreenStub();
    if (stub != null) {
      return stub.getEXPORT();
    }

    return getExporterInfo().getEXPORT();
  }

  @Override
  public @NotNull List<String> getEXPORT_OK() {
    PerlNamespaceDefinitionStub stub = getGreenStub();
    if (stub != null) {
      return stub.getEXPORT_OK();
    }

    return getExporterInfo().getEXPORT_OK();
  }

  @Override
  public @NotNull Map<String, List<String>> getEXPORT_TAGS() {
    PerlNamespaceDefinitionStub stub = getGreenStub();
    if (stub != null) {
      return stub.getEXPORT_TAGS();
    }

    return getExporterInfo().getEXPORT_TAGS();
  }

  @Override
  public int getTextOffset() {
    PsiElement nameIdentifier = getNameIdentifier();

    return nameIdentifier == null
           ? super.getTextOffset()
           : nameIdentifier.getTextOffset();
  }

  public @NotNull ExporterInfo getExporterInfo() {
    return myExporterInfo.getValue();
  }

  private @NotNull ExporterInfo computeExporterInfo() {
    ExporterInfo result = new ExporterInfo();
    PerlPsiUtil.processNamespaceStatements(this, result);
    return result;
  }

  @Override
  public @Nullable PerlNamespaceAnnotations getAnnotations() {
    PerlNamespaceDefinitionStub stub = getGreenStub();
    if (stub != null) {
      return stub.getAnnotations();
    }
    else {
      // re-parsing
      return getLocalAnnotations();
    }
  }

  public @Nullable PerlNamespaceAnnotations getLocalAnnotations() {
    return PerlNamespaceAnnotations.tryToFindAnnotations(this);
  }

  @Override
  public String toString() {
    return super.toString() + "@" + getNamespaceName();
  }

  public static class MroSearcher implements Processor<PsiElement> {
    public int counter = 0;
    private @NotNull PerlMroType myResult = PerlMroType.DFS;

    @Override
    public boolean process(PsiElement element) {
      if (element instanceof PerlUseStatementElement) {
        PerlPackageProcessor packageProcessor = ((PerlUseStatementElement)element).getPackageProcessor();
        if (packageProcessor instanceof PerlMroProvider) {
          myResult = ((PerlMroProvider)packageProcessor).getMroType((PerlUseStatementElement)element);
          return false;
        }
      }
      return true;
    }

    public @NotNull PerlMroType getResult() {
      return myResult;
    }
  }

  @Override
  public void subtreeChanged() {
    super.subtreeChanged();
    myExporterInfo.drop();
    myMroType.drop();
    myParentNamespaces.drop();
  }

  public static class ExporterInfo implements Processor<PsiElement> {
    private final @NotNull List<String> EXPORT = new ArrayList<>();
    private final @NotNull List<String> EXPORT_OK = new ArrayList<>();
    private final @NotNull Map<String, List<String>> EXPORT_TAGS = Collections.emptyMap();

    @Override
    public boolean process(PsiElement element) {
      if (ASSIGN_STATEMENT.accepts(element)) {
        if (EXPORT_ASSIGN_STATEMENT.accepts(element)) {
          PsiElement rightSide = element.getFirstChild().getLastChild();
          if (rightSide != null) {
            EXPORT.clear();
            EXPORT.addAll(getRightSideStrings(rightSide));
          }
        }
        else if (EXPORT_OK_ASSIGN_STATEMENT.accepts(element)) {
          PsiElement rightSide = element.getFirstChild().getLastChild();
          if (rightSide != null) {
            EXPORT_OK.clear();
            EXPORT_OK.addAll(getRightSideStrings(rightSide));
          }
        }
      }

      return true;
    }

    protected List<String> getRightSideStrings(@NotNull PsiElement rightSide) {
      List<String> result = new ArrayList<>();
      if (rightSide.getFirstChild() != null) {
        int lastEnd = -1;
        for (PsiElement psiElement : PerlPsiUtil.collectStringElements(rightSide.getFirstChild())) {
          String text = psiElement.getText();
          if (StringUtil.isNotEmpty(text)) {
            int newStart = psiElement.getNode().getStartOffset();
            if (newStart == lastEnd) // appending
            {
              int lastIndex = result.size() - 1;
              result.add(result.remove(lastIndex) + text);
            }
            else {
              result.add(text);
            }
            lastEnd = newStart + text.length();
          }
        }
      }
      return result;
    }

    public @NotNull List<String> getEXPORT() {
      return EXPORT;
    }

    public @NotNull List<String> getEXPORT_OK() {
      return EXPORT_OK;
    }

    public @NotNull Map<String, List<String>> getEXPORT_TAGS() {
      return EXPORT_TAGS;
    }
  }
}
