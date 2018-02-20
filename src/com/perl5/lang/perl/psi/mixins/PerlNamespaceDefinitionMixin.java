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

package com.perl5.lang.perl.psi.mixins;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.Processor;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.extensions.packageprocessor.PerlMroProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageParentsProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessor;
import com.perl5.lang.perl.extensions.parser.PerlRuntimeParentsProvider;
import com.perl5.lang.perl.extensions.parser.PerlRuntimeParentsProviderFromArray;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationSimple;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.mro.PerlMroType;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDefinitionStub;
import com.perl5.lang.perl.psi.utils.PerlNamespaceAnnotations;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by hurricup on 28.05.2015.
 */
public abstract class PerlNamespaceDefinitionMixin extends PerlStubBasedPsiElementBase<PerlNamespaceDefinitionStub>
  implements StubBasedPsiElement<PerlNamespaceDefinitionStub>,
             PerlNamespaceDefinitionWithIdentifier,
             PerlElementPatterns,
             PerlCompositeElement {
  private PerlMroType mroTypeCache = null;
  private List<String> parentsNamesCache = null;
  private ExporterInfo exporterInfoCache = null;

  public PerlNamespaceDefinitionMixin(@NotNull ASTNode node) {
    super(node);
  }

  public PerlNamespaceDefinitionMixin(@NotNull PerlNamespaceDefinitionStub stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Nullable
  @Override
  public PsiPerlNamespaceContent getNamespaceContent() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlNamespaceContent.class);
  }

  @Nullable
  @Override
  public String getName() {
    return getPackageName();
  }

  @Nullable
  @Override
  public PerlNamespaceElement getNamespaceElement() {
    return findChildByClass(PerlNamespaceElement.class);
  }

  @Nullable
  @Override
  public PsiElement getNameIdentifier() {
    return getNamespaceElement();
  }

  @Override
  public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
    PerlNamespaceElement namespaceElement = getNamespaceElement();
    if (namespaceElement != null) {
      PerlPsiUtil.renameElement(namespaceElement, name);
    }
    return this;
  }


  @Nullable
  @Override
  public String getPackageName() {
    PerlNamespaceDefinitionStub stub = getStub();
    if (stub != null) {
      return stub.getPackageName();
    }

    return getPackageNameHeavy();
  }

  protected String getPackageNameHeavy() {
    PerlNamespaceElement namespaceElement = getNamespaceElement();
    if (namespaceElement != null) {
      return namespaceElement.getCanonicalName();
    }

    return null;
  }

  @NotNull
  @Override
  public List<String> getParentNamespacesNames() {
    PerlNamespaceDefinitionStub stub = getStub();
    if (stub != null) {
      return stub.getParentNamespacesNames();
    }

    return getParentNamespacesNamesFromPsi();
  }

  @NotNull
  public List<String> getParentNamespacesNamesFromPsi() {
    if (parentsNamesCache == null) {
      collectParentNamespacesFromPsi();
    }
    return parentsNamesCache;
  }

  protected synchronized void collectParentNamespacesFromPsi() {
    if (parentsNamesCache == null) {
      //			System.err.println("Scanning");
      ParentNamespacesNamesCollector collector = getCollector();
      PerlPsiUtil.processNamespaceStatements(this, collector);
      collector.applyRunTimeModifiers();
      parentsNamesCache = collector.getParentNamespaces();
    }
  }

  protected ParentNamespacesNamesCollector getCollector() {
    return new ParentNamespacesNamesCollector(new ArrayList<>(), getPackageName());
  }

  @Nullable
  @Override
  public Icon getIcon(int flags) {
    return PerlIcons.PACKAGE_GUTTER_ICON;
  }

  @Override
  public ItemPresentation getPresentation() {
    return new PerlItemPresentationSimple(this, getPresentableName());
  }

  @Override
  public PerlMroType getMroType() {
    PerlNamespaceDefinitionStub stub = getStub();
    if (stub != null) {
      return stub.getMroType();
    }

    if (mroTypeCache == null) {
      MroSearcher searcher = new MroSearcher();
      PerlPsiUtil.processNamespaceStatements(this, searcher);
      mroTypeCache = searcher.getResult();
    }
    return mroTypeCache;
  }



  @NotNull
  @Override
  public List<String> getEXPORT() {
    PerlNamespaceDefinitionStub stub = getStub();
    if (stub != null) {
      return stub.getEXPORT();
    }

    return getExporterInfo().getEXPORT();
  }

  @NotNull
  @Override
  public List<String> getEXPORT_OK() {
    PerlNamespaceDefinitionStub stub = getStub();
    if (stub != null) {
      return stub.getEXPORT_OK();
    }

    return getExporterInfo().getEXPORT_OK();
  }

  @NotNull
  @Override
  public Map<String, List<String>> getEXPORT_TAGS() {
    PerlNamespaceDefinitionStub stub = getStub();
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

  @Override
  public void subtreeChanged() {
    super.subtreeChanged();
    mroTypeCache = null;
    parentsNamesCache = null;
    exporterInfoCache = null;
  }

  @NotNull
  public ExporterInfo getExporterInfo() {
    if (exporterInfoCache == null) {
      collectExporterInfo();
    }
    return exporterInfoCache;
  }

  protected synchronized void collectExporterInfo() {
    if (exporterInfoCache == null) {
      exporterInfoCache = new ExporterInfo();
      PerlPsiUtil.processNamespaceStatements(this, exporterInfoCache);
    }
  }

  @Nullable
  @Override
  public PerlNamespaceAnnotations getAnnotations() {
    PerlNamespaceDefinitionStub stub = getStub();
    if (stub != null) {
      return stub.getAnnotations();
    }
    else {
      // re-parsing
      return getLocalAnnotations();
    }
  }

  @Nullable
  public PerlNamespaceAnnotations getLocalAnnotations() {
    return PerlNamespaceAnnotations.tryToFindAnnotations(this);
  }

  @Override
  public String toString() {
    return super.toString() + "@" + getPackageName();
  }

  public static class MroSearcher implements Processor<PsiElement> {
    public int counter = 0;
    private PerlMroType myResult = PerlMroType.DFS;

    @Override
    public boolean process(PsiElement element) {
      //			counter++;
      //			System.err.println("Processing" + element);
      if (element instanceof PerlUseStatement) {
        PerlPackageProcessor packageProcessor = ((PerlUseStatement)element).getPackageProcessor();
        if (packageProcessor instanceof PerlMroProvider) {
          myResult = ((PerlMroProvider)packageProcessor).getMroType((PerlUseStatement)element);
          //					System.err.println("Got it");
          return false;
        }
      }
      return true;
    }

    public PerlMroType getResult() {
      return myResult;
    }
  }

  public static class ExporterInfo implements Processor<PsiElement> {
    private List<String> EXPORT = new ArrayList<>();
    private List<String> EXPORT_OK = new ArrayList<>();
    private Map<String, List<String>> EXPORT_TAGS = Collections.emptyMap(); //new THashMap<String, List<String>>(); fixme nyi

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

    protected List<String> getRightSideStrings(@NotNull PsiElement rigthSide) {
      List<String> result = new ArrayList<>();
      if (rigthSide.getFirstChild() != null) {
        int lastEnd = -1;
        for (PsiElement psiElement : PerlPsiUtil.collectStringElements(rigthSide.getFirstChild())) {
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

    public List<String> getEXPORT() {
      return EXPORT;
    }

    public List<String> getEXPORT_OK() {
      return EXPORT_OK;
    }

    public Map<String, List<String>> getEXPORT_TAGS() {
      return EXPORT_TAGS;
    }
  }

  public static class ParentNamespacesNamesCollector implements Processor<PsiElement> {
    private final List<String> parentNamespaces;
    private final List<PerlRuntimeParentsProvider> runtimeModifiers = new ArrayList<>();
    private final String myPackageName;

    public ParentNamespacesNamesCollector(List<String> parentNamespaces, String packageName) {
      this.parentNamespaces = parentNamespaces;
      myPackageName = packageName;
    }

    @Override
    public boolean process(PsiElement element) {
      //			System.err.println("Processing " + element);

      if (element instanceof PerlUseStatement) {
        PerlPackageProcessor processor = ((PerlUseStatement)element).getPackageProcessor();
        if (processor instanceof PerlPackageParentsProvider) {
          ((PerlPackageParentsProvider)processor).changeParentsList((PerlUseStatement)element, parentNamespaces);
        }
      }
      else if (element instanceof PerlRuntimeParentsProvider) {
        runtimeModifiers.add((PerlRuntimeParentsProvider)element);
      }
      else if (element.getFirstChild() instanceof PerlRuntimeParentsProvider) {
        runtimeModifiers.add((PerlRuntimeParentsProvider)element.getFirstChild());
      }
      else if (ISA_ASSIGN_STATEMENT.accepts(element)) {
        PsiElement assignExpr = element.getFirstChild();
        if (assignExpr instanceof PsiPerlAssignExpr) {
          PsiPerlArrayVariable variable = PsiTreeUtil.findChildOfType(element, PsiPerlArrayVariable.class);

          if (variable != null && StringUtil.equals("ISA", variable.getName())) {
            PsiElement rightSide = assignExpr.getLastChild();
            if (rightSide != null) {
              String explicitPackageName = variable.getExplicitPackageName();
              if (explicitPackageName == null || StringUtil.equals(explicitPackageName, myPackageName)) {
                runtimeModifiers.add(new PerlRuntimeParentsProviderFromArray(assignExpr.getLastChild()));
              }
            }
          }
        }
      }

      return true;
    }

    public void applyRunTimeModifiers() {
      for (PerlRuntimeParentsProvider provider : runtimeModifiers) {
        provider.changeParentsList(parentNamespaces);
      }
    }

    public List<String> getParentNamespaces() {
      return parentNamespaces;
    }
  }
}
