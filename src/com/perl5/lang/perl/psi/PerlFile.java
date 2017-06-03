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

package com.perl5.lang.perl.psi;

import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.perl5.lang.perl.extensions.PerlCodeGenerator;
import com.perl5.lang.perl.psi.properties.PerlLabelScope;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.pod.parser.psi.PodLinkTarget;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * Created by hurricup on 09.08.2015.
 */
public interface PerlFile extends PsiFile, PerlLexicalScope, PerlNamespaceContainer, PerlLabelScope, ItemPresentation, PodLinkTarget {
  /**
   * Recursively collects virtual files included in current file
   *
   * @param includedVirtualFiles set of already gathered files
   */
  void collectIncludedFiles(Set<VirtualFile> includedVirtualFiles);


  /**
   * Returns generator for overriding elements
   *
   * @return override generator
   */
  PerlCodeGenerator getCodeGenerator();

  /**
   * Returns perl content with templating injections replaced with spaces
   *
   * @return bytes for external analysis/formatting
   */
  @Nullable
  byte[] getPerlContentInBytes();

  /**
   * Overrides file context; if null - using default context resoving implementation
   *
   * @param fileContext new file context
   */
  void setFileContext(@Nullable PsiElement fileContext);
}
