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

package com.perl5.lang.perl.psi.references;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.extensions.PerlRenameUsagesHelper;
import com.perl5.lang.perl.parser.constant.psi.light.PerlLightConstantDefinitionElement;
import com.perl5.lang.perl.psi.PerlGlobVariable;
import com.perl5.lang.perl.psi.PerlSubDeclarationElement;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import com.perl5.lang.perl.psi.PerlSubElement;
import com.perl5.lang.perl.psi.mro.PerlMroDfs;
import com.perl5.lang.perl.psi.properties.PerlIdentifierOwner;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 26.11.2015.
 * Basic class for sub reference. Uses context package to resolve. Used in string contents, moose, etc.
 */
public class PerlSubReferenceSimple extends PerlCachingReference<PsiElement> {
  protected static final int FLAG_AUTOLOADED = 1;
  protected static final int FLAG_CONSTANT = 2;
  protected static final int FLAG_DECLARED = 4;
  protected static final int FLAG_DEFINED = 8;
  protected static final int FLAG_ALIASED = 16;
  protected static final int FLAG_IMPORTED = 32;    // fixme this is not set anyway
  protected static final int FLAG_XSUB = 64;

  protected int FLAGS = 0;

  public PerlSubReferenceSimple(PsiElement psiElement) {
    super(psiElement);
  }

  public PerlSubReferenceSimple(@NotNull PsiElement element, TextRange textRange) {
    super(element, textRange);
  }

  @NotNull
  @Override
  protected ResolveResult[] resolveInner(boolean incompleteCode) {
    // fixme not dry with super resolver, need some generics fix
    PsiElement myElement = getElement();
    List<PsiElement> relatedItems = new ArrayList<>();

    String packageName = PerlPackageUtil.getContextPackageName(myElement);
    String subName = myElement.getNode().getText();
    Project project = myElement.getProject();

    relatedItems.addAll(PerlMroDfs.resolveSub(
      project,
      packageName,
      subName,
      false
    ));

    List<ResolveResult> result = getResolveResults(relatedItems);

    return result.toArray(new ResolveResult[result.size()]);
  }

  public boolean isAutoloaded() {
    return (FLAGS & FLAG_AUTOLOADED) > 0;
  }

  public boolean isDefined() {
    return (FLAGS & FLAG_DEFINED) > 0;
  }

  public boolean isDeclared() {
    return (FLAGS & FLAG_DECLARED) > 0;
  }

  public boolean isAliased() {
    return (FLAGS & FLAG_ALIASED) > 0;
  }

  public boolean isConstant() {
    return (FLAGS & FLAG_CONSTANT) > 0;
  }

  public boolean isImported() {
    return (FLAGS & FLAG_IMPORTED) > 0;
  }

  public boolean isXSub() {
    return (FLAGS & FLAG_XSUB) > 0;
  }

  public void resetFlags() {
    FLAGS = 0;
  }

  public void setAutoloaded() {
    FLAGS |= FLAG_AUTOLOADED;
  }

  public void setDefined() {

    FLAGS |= FLAG_DEFINED;
  }

  public void setXSub() {

    FLAGS |= FLAG_XSUB;
  }

  public void setDeclared() {

    FLAGS |= FLAG_DECLARED;
  }

  public void setAliased() {
    FLAGS |= FLAG_ALIASED;
  }

  public void setConstant() {

    FLAGS |= FLAG_CONSTANT;
  }

  public void setImported() {
    FLAGS |= FLAG_IMPORTED;
  }

  @NotNull
  public List<ResolveResult> getResolveResults(List<PsiElement> relatedItems) {
    List<ResolveResult> result = new ArrayList<>();

    resetFlags();

    for (PsiElement element : relatedItems) {
      if (!isAutoloaded() &&
          element instanceof PerlIdentifierOwner &&
          PerlSubUtil.SUB_AUTOLOAD.equals(((PerlIdentifierOwner)element).getName())) {
        setAutoloaded();
      }

      if (!isConstant() && element instanceof PerlLightConstantDefinitionElement) {
        setConstant();
      }

      if (!isDeclared() && element instanceof PerlSubDeclarationElement) {
        setDeclared();
      }

      if (!isDefined() && element instanceof PerlSubDefinitionElement) {
        setDefined();
      }

      if (!isXSub() && element instanceof PerlSubElement && ((PerlSubElement)element).isXSub()) {
        setXSub();
      }

      if (!isAliased() && element instanceof PerlGlobVariable) {
        setAliased();
      }

      result.add(new PsiElementResolveResult(element));
    }
    return result;
  }


  @Override
  public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
    PsiElement target = resolve();
    if (target instanceof PerlRenameUsagesHelper) {
      newElementName = ((PerlRenameUsagesHelper)target).getSubstitutedUsageName(newElementName, myElement);
    }
    return super.handleElementRename(newElementName);
  }
}
