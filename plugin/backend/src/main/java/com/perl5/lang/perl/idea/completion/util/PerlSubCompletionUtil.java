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

package com.perl5.lang.perl.idea.completion.util;

import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.*;
import com.perl5.lang.perl.idea.completion.inserthandlers.SubSelectionHandler;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlCompletionProcessor;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlSimpleCompletionProcessor;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlSimpleDelegatingCompletionProcessor;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlVariableCompletionProcessor;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlImplicitSubDefinition;
import com.perl5.lang.perl.psi.references.PerlImplicitDeclarationsService;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class PerlSubCompletionUtil {
  public static final SubSelectionHandler SUB_SELECTION_HANDLER = new SubSelectionHandler();


  public static boolean processSubDefinitionLookupElement(@NotNull PerlSubDefinitionElement subDefinition,
                                                          @NotNull PerlCompletionProcessor completionProcessor) {
    return processSubDefinitionLookupElement(subDefinition, null, completionProcessor);
  }

  public static boolean processSubDefinitionLookupElement(@NotNull PerlSubDefinitionElement subDefinition,
                                                          @Nullable PerlExportDescriptor exportDescriptor,
                                                          @NotNull PerlCompletionProcessor completionProcessor) {
    return processSubDefinitionLookupElement(
      exportDescriptor == null ? subDefinition.getSubName() : exportDescriptor.getImportedName(),
      subDefinition, completionProcessor);
  }

  public static boolean processSubDefinitionLookupElement(@NotNull String nameToUse,
                                                          @NotNull PerlSubDefinitionElement subDefinition,
                                                          @NotNull PerlCompletionProcessor completionProcessor) {
    if (!completionProcessor.matches(nameToUse)) {
      return completionProcessor.result();
    }
    LookupElementBuilder newElement = LookupElementBuilder
      .create(subDefinition, nameToUse)
      .withIcon(subDefinition.getIcon(0))
      .withStrikeoutness(subDefinition.isDeprecated())
      .withTypeText(subDefinition.getNamespaceName(), true);

    String argsString = subDefinition.getSubArgumentsListAsString();

    if (!argsString.isEmpty()) {
      newElement = newElement
        .withInsertHandler(SUB_SELECTION_HANDLER)
        .withTailText(argsString);
    }

    return completionProcessor.process(newElement);
  }

  public static boolean processImportedEntityLookupElement(@NotNull PsiElement element,
                                                           @NotNull PerlExportDescriptor exportDescriptor,
                                                           @NotNull PerlCompletionProcessor completionProcessor) {
    return switch (element) {
      case PerlSubDefinitionElement subDefinitionElement ->
        processSubDefinitionLookupElement(subDefinitionElement, exportDescriptor, completionProcessor);
      case PerlSubDeclarationElement subDeclarationElement ->
        processSubDeclarationLookupElement(subDeclarationElement, exportDescriptor, completionProcessor);
      case PerlGlobVariableElement globVariableElement ->
        processGlobLookupElement(globVariableElement, exportDescriptor, completionProcessor);
      default -> throw new RuntimeException("Don't know how to make lookup element for " + element.getClass());
    };
  }

  public static boolean processSubDeclarationLookupElement(@NotNull PerlSubDeclarationElement subDeclaration,
                                                           @NotNull PerlCompletionProcessor completionProcessor) {
    return processSubDeclarationLookupElement(subDeclaration, null, completionProcessor);
  }

  public static boolean processSubDeclarationLookupElement(@NotNull PerlSubDeclarationElement subDeclaration,
                                                           @Nullable PerlExportDescriptor exportDescriptor,
                                                           @NotNull PerlCompletionProcessor completionProcessor) {

    String lookupString = exportDescriptor == null ? subDeclaration.getSubName() : exportDescriptor.getImportedName();
    if (!completionProcessor.matches(lookupString)) {
      return completionProcessor.result();
    }

    return completionProcessor.process(LookupElementBuilder
                                         .create(subDeclaration, lookupString)
                                         .withIcon(subDeclaration.getIcon(0))
                                         .withStrikeoutness(subDeclaration.isDeprecated())
                                         .withInsertHandler(SUB_SELECTION_HANDLER)
                                         .withTypeText(subDeclaration.getNamespaceName(), true)
    );
  }

  public static boolean processGlobLookupElement(@NotNull PerlGlobVariableElement globVariable,
                                                 @NotNull PerlCompletionProcessor completionProcessor) {
    return processGlobLookupElement(globVariable, null, completionProcessor);
  }

  /**
   * Probably duplicate of {@link PerlVariableCompletionUtil#processVariableLookupElement(PerlGlobVariableElement, boolean, PerlVariableCompletionProcessor)}
   */
  public static boolean processGlobLookupElement(@NotNull PerlGlobVariableElement globVariable,
                                                 @Nullable PerlExportDescriptor exportDescriptor,
                                                 @NotNull PerlCompletionProcessor completionProcessor) {
    String lookupString = exportDescriptor == null ? globVariable.getName() : exportDescriptor.getImportedName();
    if (!completionProcessor.matches(lookupString)) {
      return completionProcessor.result();
    }

    return completionProcessor.process(LookupElementBuilder
                                         .create(globVariable, StringUtil.notNullize(lookupString))
                                         .withIcon(globVariable.getIcon(0))
                                         .withInsertHandler(SUB_SELECTION_HANDLER)
                                         .withTypeText(globVariable.getNamespaceName(), true)
    );
  }

  @SuppressWarnings("UnusedReturnValue")
  public static boolean processUnresolvedSubsLookups(@NotNull PerlSubElement subDefinition,
                                                     @NotNull PerlCompletionProcessor completionProcessor) {
    final String packageName = subDefinition.getNamespaceName();
    if (packageName == null) {
      return completionProcessor.result();
    }

    final Set<String> namesSet = new HashSet<>();
    PsiFile containingFile = subDefinition.getContainingFile();
    containingFile.accept(new PerlCompletionRecursiveVisitor(completionProcessor) {
      @Override
      protected boolean shouldVisitLightElements() {
        return true;
      }

      @Override
      public void visitMethod(@NotNull PsiPerlMethod method) {
        doVisitMethod(method);
        super.visitMethod(method);
      }

      private void doVisitMethod(@NotNull PsiPerlMethod method) {
        PerlCallValue methodValue = PerlCallValue.from(method);
        if (methodValue == null) {
          return;
        }

        PerlValue namespaceValue = methodValue.getNamespaceNameValue();

        if (!namespaceValue.canRepresentNamespace(packageName)) {
          return;
        }
        PerlSubNameElement subNameElement = method.getSubNameElement();

        if (subNameElement == null || !subNameElement.isValid()) {
          return;
        }

        String subName = subNameElement.getName();

        if (StringUtil.isEmpty(subName) || namesSet.contains(subName) || !completionProcessor.matches(subName)) {
          return;
        }
        PsiReference[] references = subNameElement.getReferences();
        if (references.length == 0) {
          return;
        }

        for (PsiReference reference : references) {
          if (reference.resolve() != null) {
            return;
          }
        }
        // unresolved
        namesSet.add(subName);
        completionProcessor.process(LookupElementBuilder.create(subName));
      }
    });
    return completionProcessor.result();
  }

  public static void processWithNotOverriddenSubs(@NotNull PerlSubElement subDefinition,
                                                  @NotNull PerlCompletionProcessor completionProcessor) {
    PerlPackageUtil.processNotOverridedMethods(
      PsiTreeUtil.getParentOfType(subDefinition, PerlNamespaceDefinitionElement.class),
      subDefinitionBase -> {
        String lookupString = subDefinitionBase.getSubName();
        if (completionProcessor.matches(lookupString)) {
          return completionProcessor.process(LookupElementBuilder.create(subDefinitionBase, lookupString));
        }
        return completionProcessor.result();
      }
    );
  }

  public static boolean processSubsCompletionsForCallValue(@NotNull PerlSimpleCompletionProcessor completionProcessor,
                                                           @NotNull PerlCallValue perlValue,
                                                           boolean isStatic) {
    return perlValue.processTargetNamespaceElements(
      completionProcessor.getLeafElement(), new PerlNamespaceItemProcessor<>() {
        @Override
        public boolean processItem(@NotNull PsiNamedElement element) {
          return switch (element) {
            case PerlImplicitSubDefinition implicitSubDefinition
              when implicitSubDefinition.isAnonymous() -> //noinspection DuplicateBranchesInSwitch
              completionProcessor.result();
            case PerlSubDefinitionElement subDefinitionElement
              when !subDefinitionElement.isAnonymous() &&
                   (isStatic && subDefinitionElement.isStatic() || subDefinitionElement.isMethod()) ->
              processSubDefinitionLookupElement(subDefinitionElement, completionProcessor);
            case PerlSubDeclarationElement subDeclarationElement
              when (isStatic && subDeclarationElement.isStatic() || subDeclarationElement.isMethod()) ->
              processSubDeclarationLookupElement(subDeclarationElement, completionProcessor);
            case PerlGlobVariableElement perlGlobVariableElement
              when perlGlobVariableElement.isLeftSideOfAssignment() && StringUtil.isNotEmpty(element.getName()) ->
              processGlobLookupElement(perlGlobVariableElement, completionProcessor);
            default -> completionProcessor.result();
          };
        }

        @Override
        public boolean processImportedItem(@NotNull PsiNamedElement element,
                                           @NotNull PerlExportDescriptor exportDescriptor) {
          return processImportedEntityLookupElement(element, exportDescriptor, completionProcessor);
        }

        @Override
        public boolean processOrphanDescriptor(@NotNull PerlExportDescriptor exportDescriptor) {
          if (exportDescriptor.isSub()) {
            return exportDescriptor.processLookupElement(completionProcessor);
          }
          return completionProcessor.result();
        }
      });
  }

  public static boolean processBuiltInSubsLookupElements(PerlSimpleCompletionProcessor completionProcessor) {
    PerlCompletionProcessor builtInCompletionProcessor = new PerlSimpleDelegatingCompletionProcessor(completionProcessor) {
      @Override
      public void addElement(@NotNull LookupElementBuilder lookupElement) {
        super.addElement(lookupElement.withBoldness(true));
      }
    };

    return PerlImplicitDeclarationsService.getInstance(completionProcessor.getProject()).processSubs(
      sub -> sub.isBuiltIn() ? processSubDefinitionLookupElement(sub, builtInCompletionProcessor)
                             : builtInCompletionProcessor.result());
  }

  /**
   * Processes all subs applicable at current context. Declarations, imports, built-ins.
   */
  @SuppressWarnings("UnusedReturnValue")
  public static boolean processContextSubsLookupElements(@NotNull PerlSimpleCompletionProcessor completionProcessor) {
    if (!PerlSubCompletionUtil.processBuiltInSubsLookupElements(completionProcessor)) {
      return false;
    }
    PerlCallStaticValue callValue = new PerlCallStaticValue(
      PerlPackageUtil.getContextType(completionProcessor.getLeafElement()), PerlScalarValue.create("dummy"), Collections.emptyList(),
      false);
    return processSubsCompletionsForCallValue(completionProcessor, callValue, true);
  }
}
