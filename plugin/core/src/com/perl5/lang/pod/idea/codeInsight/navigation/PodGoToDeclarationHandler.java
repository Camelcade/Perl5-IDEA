/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.pod.idea.codeInsight.navigation;

import com.intellij.codeInsight.TargetElementUtil;
import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.parser.psi.references.PodLinkToSectionReference;
import org.jetbrains.annotations.Nullable;

public class PodGoToDeclarationHandler implements GotoDeclarationHandler {
  @Nullable
  @Override
  public PsiElement[] getGotoDeclarationTargets(@Nullable PsiElement sourceElement, int offset, Editor editor) {
    if (sourceElement == null || !sourceElement.getLanguage().isKindOf(PodLanguage.INSTANCE)) {
      return null;
    }

    PsiReference reference = TargetElementUtil.findReference(editor, offset);
    if (!(reference instanceof PodLinkToSectionReference)) {
      return null;
    }

    return ContainerUtil.map(((PodLinkToSectionReference)reference).multiResolve(false), ResolveResult::getElement)
      .toArray(PsiElement.EMPTY_ARRAY);
  }
}
