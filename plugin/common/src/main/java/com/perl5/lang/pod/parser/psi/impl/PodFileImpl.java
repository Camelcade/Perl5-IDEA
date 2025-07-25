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

package com.perl5.lang.pod.parser.psi.impl;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.util.PerlPackageService;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.filetypes.PodFileType;
import com.perl5.lang.pod.parser.psi.PodFile;
import com.perl5.lang.pod.parser.psi.PodRenderingContext;
import com.perl5.lang.pod.parser.psi.PodTitledSection;
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PodFileImpl extends PsiFileBase implements PodFile {
  public PodFileImpl(@NotNull FileViewProvider viewProvider) {
    super(viewProvider, PodLanguage.INSTANCE);
  }

  @Override
  public @NotNull FileType getFileType() {
    return PodFileType.INSTANCE;
  }

  @Override
  public String toString() {
    return "POD file";
  }

  @Override
  public Icon getIcon(int flags) {
    PsiFile baseFile = getViewProvider().getStubBindingRoot();
    return baseFile == this ? super.getIcon(flags) : baseFile.getIcon(flags);
  }

  @Override
  public void renderElementAsHTML(StringBuilder builder, PodRenderingContext context) {
    PodRenderUtil.renderPsiRangeAsHTML(getFirstNamedBlock(), null, builder, context);
  }

  public @Nullable PsiElement getFirstNamedBlock() {
    return findChildByClass(PodTitledSection.class);
  }

  @Override
  public void renderElementAsText(StringBuilder builder, PodRenderingContext context) {
    PodRenderUtil.renderPsiRangeAsText(getFirstNamedBlock(), null, builder, context);
  }

  @Override
  public boolean isIndexed() {
    return false;
  }

  @Override
  public int getListLevel() {
    return 0;
  }

  @Override
  public ItemPresentation getPresentation() {
    return this;
  }

  @Override
  public @Nullable String getPresentableText() {
    String packageName = getNormalizedPackageName();
    return packageName == null ? getName() : packageName;
  }

  /**
   * Returns POD file normalized package name (with optionally trimmed {@code pod::})
   *
   * @return POD package name or null if this is a script file file or smth
   */
  private @Nullable String getNormalizedPackageName() {
    String packageName = PerlPackageService.getInstance().getFilePackageName(this);
    if (StringUtil.isEmpty(packageName)) {
      return null;
    }
    return StringUtil.trimStart(packageName, "pod::");
  }

  @Override
  public @Nullable String getLocationString() {
    final PsiDirectory psiDirectory = getParent();
    if (psiDirectory != null) {
      return psiDirectory.getVirtualFile().getPresentableUrl();
    }
    return null;
  }

  @Override
  public @Nullable Icon getIcon(boolean unused) {
    return getFileType().getIcon();
  }

  @Override
  public @Nullable String getPodLink() {
    String normalizedPackageName = getNormalizedPackageName();
    if (StringUtil.isNotEmpty(normalizedPackageName)) {
      return normalizedPackageName;
    }
    return null;
  }

  @Override
  public @Nullable String getPodLinkText() {
    String normalizedPackageName = getNormalizedPackageName();
    if (StringUtil.isNotEmpty(normalizedPackageName)) {
      return normalizedPackageName;
    }
    return ObjectUtils.doIfNotNull(getVirtualFile(), VirtualFile::getName);
  }
}