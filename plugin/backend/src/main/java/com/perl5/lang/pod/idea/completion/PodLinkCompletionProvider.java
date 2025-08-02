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

package com.perl5.lang.pod.idea.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.usageView.UsageViewUtil;
import com.intellij.util.ProcessingContext;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlCompletionProcessor;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlSimpleCompletionProcessor;
import com.perl5.lang.perl.idea.completion.util.PerlPackageCompletionUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.pod.filetypes.PodFileType;
import com.perl5.lang.pod.parser.psi.PodCompositeElement;
import com.perl5.lang.pod.parser.psi.PodFile;
import com.perl5.lang.pod.parser.psi.PodStubsAwareRecursiveVisitor;
import com.perl5.lang.pod.parser.psi.PodTitledSection;
import com.perl5.lang.pod.parser.psi.mixin.PodFormatterL;
import com.perl5.lang.pod.parser.psi.mixin.PodFormatterX;
import com.perl5.lang.pod.parser.psi.mixin.PodSectionItem;
import com.perl5.lang.pod.parser.psi.util.PodFileUtil;
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil;
import com.perl5.lang.pod.psi.PsiItemSection;
import com.perl5.lang.pod.psi.PsiPodFormatIndex;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

import static com.perl5.lang.pod.parser.PodElementTypesGenerated.*;

public class PodLinkCompletionProvider extends CompletionProvider<CompletionParameters> {
  private static final Logger LOG = Logger.getInstance(PodLinkCompletionProvider.class);

  @Override
  protected void addCompletions(@NotNull CompletionParameters parameters,
                                @NotNull ProcessingContext context,
                                @NotNull CompletionResultSet result) {
    PsiElement element = parameters.getPosition();
    PsiElement linkPart = element.getParent();
    PodFormatterL linkElement = PsiTreeUtil.getParentOfType(element, PodFormatterL.class);
    if (linkElement == null) {
      return;
    }
    IElementType parentType = PsiUtilCore.getElementType(linkPart);
    PerlSimpleCompletionProcessor completionProcessor = new PerlSimpleCompletionProcessor(parameters, result, linkElement);

    if (parentType == LINK_TEXT && element.getPrevSibling() == null) {
      processFilesCompletions(completionProcessor);
    }
    if (parentType == LINK_NAME) {
      processFilesCompletions(completionProcessor);
    }
    if (parentType == LINK_SECTION) {
      addSectionsCompletions(PodFileUtil.getTargetFile(linkElement), completionProcessor);
    }
    completionProcessor.logStatus(getClass());
  }

  @SuppressWarnings("UnusedReturnValue")
  protected boolean processFilesCompletions(@NotNull PerlCompletionProcessor completionProcessor) {
    PodFormatterL linkElement = (PodFormatterL)completionProcessor.getLeafElement();
    final Set<String> foundPods = new HashSet<>();
    PerlPackageUtil.processIncFilesForPsiElement(linkElement, (file, classRoot) -> {
      String className = PodFileUtil.getPackageNameFromVirtualFile(file, classRoot);
      if (StringUtil.isNotEmpty(className)) {
        boolean isBuiltIn = false;
        if (StringUtil.startsWith(className, "pod::")) {
          isBuiltIn = true;
          className = className.substring(5);
        }
        if (completionProcessor.matches(className) && foundPods.add(className)) {
          if (!completionProcessor
            .process(LookupElementBuilder.create(file, className).withIcon(PerlIcons.POD_FILE).withBoldness(isBuiltIn))) {
            return false;
          }
        }
      }
      return completionProcessor.result();
    }, PodFileType.INSTANCE);

    return PerlPackageUtil.processPackageFilesForPsiElement(linkElement, (packageName, file) -> {
      if (StringUtil.isNotEmpty(packageName) && completionProcessor.matches(packageName) && foundPods.add(packageName)) {
        return PerlPackageCompletionUtil.processPackageLookupElement(file, packageName, null, completionProcessor, false);
      }
      return completionProcessor.result();
    });
  }

  protected static void addSectionsCompletions(@Nullable PsiFile targetFile,
                                               @NotNull PerlCompletionProcessor completionProcessor) {
    if (targetFile == null) {
      return;
    }
    Set<String> distinctString = new HashSet<>();

    targetFile.accept(new PodStubsAwareRecursiveVisitor() {
      @Override
      public void visitElement(@NotNull PsiElement element) {
        if (completionProcessor.result()) {
          super.visitElement(element);
        }
      }

      @Override
      public void visitTargetableSection(PodTitledSection o) {
        String title = cleanItemText(o.getTitleText());
        if (completionProcessor.matches(title) && distinctString.add(title)) {
          completionProcessor.process(LookupElementBuilder.create(o, PodRenderUtil.escapeTitle(title))
                                        .withLookupString(title)
                                        .withPresentableText(title)
                                        .withIcon(PerlIcons.POD_FILE)
                                        .withTypeText(UsageViewUtil.getType(o)));
        }
        super.visitTargetableSection(o);
      }

      @Override
      public void visitItemSection(@NotNull PsiItemSection o) {
        if (((PodSectionItem)o).isTargetable()) {
          super.visitItemSection(o);
        }
        else {
          visitElement(o);
        }
      }

      @Contract("null->null")
      private @Nullable String cleanItemText(@Nullable String itemText) {
        if (itemText == null) {
          return null;
        }
        String trimmed = StringUtil.trimLeading(itemText.trim(), '*');
        if (isNumber(trimmed)) {
          return null;
        }
        return PodRenderUtil.trimItemText(trimmed);
      }

      private static boolean isNumber(@NotNull String text){
        if( text.isEmpty()){
          return false;
        }

        for( int i = 0; i < text.length(); i++){
          if( !Character.isDigit(text.charAt(i))){
            return false;
          }
        }

        return true;
      }

      @Override
      public void visitPodFormatIndex(@NotNull PsiPodFormatIndex o) {
        assert o instanceof PodFormatterX;
        if (!((PodFormatterX)o).isMeaningful()) {
          return;
        }
        String indexTitle = ((PodFormatterX)o).getTitleText();
        if (StringUtil.isEmpty(indexTitle) || !distinctString.add(indexTitle)) {
          return;
        }

        String escapedIndexTitle = PodRenderUtil.escapeTitle(indexTitle);
        if (!completionProcessor.matches(indexTitle) && !completionProcessor.matches(escapedIndexTitle)) {
          return;
        }

        PsiElement indexTarget = ((PodFormatterX)o).getIndexTarget();
        String targetPresentableText;
        if (indexTarget instanceof PodFile podFile) {
          targetPresentableText = cleanItemText(podFile.getPodLinkText());
        }
        else if (indexTarget instanceof PodCompositeElement podCompositeElement) {
          targetPresentableText = cleanItemText(podCompositeElement.getPresentableText());
        }
        else {
          LOG.warn("Unhandled index target: " + indexTarget);
          return;
        }
        String tailText = null;
        if (targetPresentableText != null) {
          tailText = "(" + targetPresentableText + ")";
        }
        completionProcessor.process(
          LookupElementBuilder.create(o, escapedIndexTitle)
            .withLookupString(indexTitle)
            .withPresentableText(indexTitle)
            .withTailText(tailText)
            .withTypeText(UsageViewUtil.getType(o))
            .withIcon(PerlIcons.POD_FILE)
        );
      }
    });
  }
}
