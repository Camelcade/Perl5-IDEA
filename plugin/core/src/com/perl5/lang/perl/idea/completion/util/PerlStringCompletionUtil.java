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

package com.perl5.lang.perl.idea.completion.util;

import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.lang.Language;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageOptionsProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageParentsProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessor;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.idea.completion.PerlStringCompletionCache;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlCompletionProcessor;
import com.perl5.lang.perl.idea.intellilang.PerlInjectionMarkersService;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlStringContentElementImpl;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.perl5.lang.perl.parser.PerlParserUtil.IDENTIFIER_PATTERN;


public class PerlStringCompletionUtil implements PerlElementPatterns {
  public static final String[] REF_TYPES = new String[]{
    "SCALAR",
    "ARRAY",
    "HASH",
    "CODE",
    "REF",
    "GLOB",
    "LVALUE",
    "FORMAT",
    "IO",
    "VSTRING",
    "Regexp"
  };

  public static void fillWithHashIndexes(@NotNull PerlCompletionProcessor completionProcessor) {
    Set<String> hashIndexesCache = PerlStringCompletionCache.getInstance(completionProcessor.getProject()).getHashIndexesCache();

    for (String text : hashIndexesCache) {
      if (completionProcessor.matches(text) && !completionProcessor.process(LookupElementBuilder.create(text))) {
        return;
      }
    }

    PsiElement element = completionProcessor.getLeafElement();
    PsiFile file = completionProcessor.getContainingFile();

    file.accept(
      new PerlCompletionRecursiveVisitor(completionProcessor) {
        @Override
        public void visitStringContentElement(@NotNull PerlStringContentElementImpl o) {
          if (o != element && SIMPLE_HASH_INDEX.accepts(o)) {
            processStringElement(o);
          }
          super.visitStringContentElement(o);
        }

        @Override
        public void visitCommaSequenceExpr(@NotNull PsiPerlCommaSequenceExpr o) {
          if (o.getParent() instanceof PsiPerlAnonHash) {
            PsiElement sequenceElement = o.getFirstChild();
            boolean isKey = true;

            while (sequenceElement != null) {
              ProgressManager.checkCanceled();
              IElementType elementType = sequenceElement.getNode().getElementType();
              if (isKey && sequenceElement instanceof PerlString) {
                for (PerlStringContentElement stringElement : PerlPsiUtil.collectStringElements(sequenceElement)) {
                  processStringElement(stringElement);
                }
              }
              else if (elementType == COMMA || elementType == FAT_COMMA) {
                isKey = !isKey;
              }

              sequenceElement = PerlPsiUtil.getNextSignificantSibling(sequenceElement);
            }
          }
          super.visitCommaSequenceExpr(o);
        }

        protected void processStringElement(PerlStringContentElement stringContentElement) {
          String text = stringContentElement.getText();
          if (StringUtil.isNotEmpty(text) && hashIndexesCache.add(text) &&
              completionProcessor.matches(text) && IDENTIFIER_PATTERN.matcher(text).matches()) {
            completionProcessor.process(LookupElementBuilder.create(stringContentElement, text));
          }
        }
      });
  }

  public static void fillWithExportableEntities(@NotNull PerlCompletionProcessor completionProcessor) {
    PsiElement element = completionProcessor.getLeafElement();
    final String contextPackageName = PerlPackageUtil.getContextNamespaceName(element);

    element.getContainingFile().accept(
      new PerlCompletionRecursiveVisitor(completionProcessor) {
        @Override
        public void visitSubDeclarationElement(@NotNull PerlSubDeclarationElement o) {
          if (contextPackageName.equals(o.getNamespaceName())) {
            String subName = o.getSubName();
            if (completionProcessor.matches(subName)) {
              completionProcessor.process(LookupElementBuilder.create(o, subName));
            }
          }
          super.visitSubDeclarationElement(o);
        }

        @Override
        protected boolean shouldVisitLightElements() {
          return true;
        }

        @Override
        public void visitPerlSubDefinitionElement(@NotNull PerlSubDefinitionElement o) {
          if (contextPackageName.equals(o.getNamespaceName())) {
            String subName = o.getSubName();
            if (completionProcessor.matches(subName)) {
              completionProcessor.process(LookupElementBuilder.create(o, subName));
            }
          }
          super.visitPerlSubDefinitionElement(o);
        }
      }
    );
  }

  public static void fillWithUseParameters(@NotNull PerlCompletionProcessor completionProcessor) {
    PsiElement baseElement = completionProcessor.getLeafElement();
    PerlUseStatementElement useStatement =
      PsiTreeUtil.getParentOfType(baseElement, PerlUseStatementElement.class, true, PsiPerlStatement.class);

    if (useStatement == null) {
      return;
    }

    List<String> typedParameters = useStatement.getImportParameters();
    Set<String> typedStringsSet = typedParameters == null ? Collections.emptySet() : new THashSet<>(typedParameters);

    PerlPackageProcessor packageProcessor = useStatement.getPackageProcessor();
    // fixme we should allow lookup elements customization by package processor
    if (packageProcessor instanceof PerlPackageOptionsProvider) {
      Map<String, String> options = ((PerlPackageOptionsProvider)packageProcessor).getOptions();

      for (Map.Entry<String, String> option : options.entrySet()) {
        String lookupString = option.getKey();
        if (!typedStringsSet.contains(lookupString) && completionProcessor.matches(lookupString) && !completionProcessor.process(
          LookupElementBuilder.create(lookupString).withTypeText(option.getValue(), true).withIcon(PerlIcons.PERL_OPTION))) {
          return;
        }
      }

      options = ((PerlPackageOptionsProvider)packageProcessor).getOptionsBundles();

      for (Map.Entry<String, String> option : options.entrySet()) {
        String lookupString = option.getKey();
        if (!typedStringsSet.contains(lookupString) && completionProcessor.matches(lookupString) && !completionProcessor.process(
          LookupElementBuilder.create(lookupString).withTypeText(option.getValue(), true).withIcon(PerlIcons.PERL_OPTIONS))) {
          return;
        }
      }
    }

    if (packageProcessor instanceof PerlPackageParentsProvider && ((PerlPackageParentsProvider)packageProcessor).hasPackageFilesOptions() &&
        !PerlPackageUtil.processPackageFilesForPsiElement(baseElement, (packageName, file) ->
          typedStringsSet.contains(packageName) ||
          PerlPackageCompletionUtil.processPackageLookupElement(file, packageName, null, completionProcessor))) {
      return;
    }

    Set<String> export = new HashSet<>();
    Set<String> exportOk = new HashSet<>();
    packageProcessor.addExports(useStatement, export, exportOk);
    exportOk.removeAll(export);

    for (String subName : export) {
      if (!typedStringsSet.contains(subName) && completionProcessor.matches(subName) && !completionProcessor.process(
        LookupElementBuilder.create(subName).withIcon(PerlIcons.SUB_GUTTER_ICON).withTypeText("default", true))) {
        return;
      }
    }
    for (String subName : exportOk) {
      if (!typedStringsSet.contains(subName) && completionProcessor.matches(subName) && !completionProcessor.process(
        LookupElementBuilder.create(subName).withIcon(PerlIcons.SUB_GUTTER_ICON).withTypeText("optional", true))) {
        return;
      }
    }
  }

  public static void fillWithRefTypes(@NotNull PerlCompletionProcessor completionProcessor) {
    for (String refType : REF_TYPES) {
      if (completionProcessor.matches(refType) && !completionProcessor.process(LookupElementBuilder.create(refType))) {
        return;
      }
    }
  }

  public static void fillWithInjectableMarkers(@NotNull PerlCompletionProcessor completionProcessor) {
    // injectable markers
    PerlInjectionMarkersService injectionService = PerlInjectionMarkersService.getInstance(completionProcessor.getProject());
    for (String marker : injectionService.getSupportedMarkers()) {
      if (!completionProcessor.matches(marker)) {
        continue;
      }
      Language language = injectionService.getLanguageByMarker(marker);
      if (language == null) {
        continue;
      }

      LookupElementBuilder newItem = LookupElementBuilder
        .create(marker)
        .withTypeText("inject with " + language.getDisplayName(), true);

      if (language.getAssociatedFileType() != null) {
        newItem = newItem.withIcon(language.getAssociatedFileType().getIcon());
      }

      if (!completionProcessor.process(newItem)) {
        return;
      }
    }
  }

  public static void fillWithHeredocOpeners(@NotNull PerlCompletionProcessor completionProcessor) {
    Project project = completionProcessor.getProject();
    Set<String> heredocOpenersCache = PerlStringCompletionCache.getInstance(project).getHeredocOpenersCache();
    // cached values
    for (String marker : heredocOpenersCache) {
      if (completionProcessor.matches(marker) && !completionProcessor.process(LookupElementBuilder.create(marker))) {
        return;
      }
    }

    PerlInjectionMarkersService injectionService = PerlInjectionMarkersService.getInstance(project);

    // collect new values
    PsiFile file = completionProcessor.getContainingFile();
    file.accept(new PerlCompletionRecursiveVisitor(completionProcessor) {
      @Override
      public void visitHeredocOpener(@NotNull PsiPerlHeredocOpener o) {
        String openerName = o.getName();
        if (StringUtil.isNotEmpty(openerName) && heredocOpenersCache.add(openerName) &&
            injectionService.getLanguageByMarker(openerName) == null &&
            !completionProcessor.process(LookupElementBuilder.create(o, openerName))) {
          return;
        }
        super.visitHeredocOpener(o);
      }
    });
  }
}
