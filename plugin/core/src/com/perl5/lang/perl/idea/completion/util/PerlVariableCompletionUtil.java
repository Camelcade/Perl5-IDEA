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

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Processor;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.idea.PerlCompletionWeighter;
import com.perl5.lang.perl.idea.completion.PerlInsertHandlers;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlDelegatingVariableCompletionProcessor;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlVariableCompletionProcessor;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlVariableCompletionProcessorImpl;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.idea.ui.PerlIconProvider;
import com.perl5.lang.perl.internals.PerlVersion;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlBuiltInVariable;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.references.PerlBuiltInVariablesService;
import com.perl5.lang.perl.psi.utils.PerlResolveUtil;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.*;
import com.perl5.lang.perl.util.processors.PerlNamespaceEntityProcessor;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;
import java.util.Set;

import static com.perl5.PerlIcons.GLOB_GUTTER_ICON;

public class PerlVariableCompletionUtil {
  public static @NotNull LookupElementBuilder processVariableLookupElement(@NotNull String name, @NotNull PerlVariableType variableType) {
    return LookupElementBuilder.create(PerlVariable.braceName(name)).withIcon(PerlIconProvider.getIcon(variableType));
  }

  public static @NotNull LookupElementBuilder createArrayElementLookupElement(@NotNull String name,
                                                                              @NotNull PerlVariableType variableType) {
    return processVariableLookupElement(name, variableType)
      .withInsertHandler(PerlInsertHandlers.ARRAY_ELEMENT_INSERT_HANDLER).withTailText("[]");
  }

  public static @NotNull LookupElementBuilder processHashElementLookupElement(@NotNull String name,
                                                                              @NotNull PerlVariableType variableType) {
    return processVariableLookupElement(name, variableType)
      .withInsertHandler(PerlInsertHandlers.HASH_ELEMENT_INSERT_HANDLER).withTailText("{}");
  }

  /**
   * @param sigilToPrepend '_' means we don't need to prepend
   */
  public static boolean processVariableLookupElement(@NotNull PerlVariableDeclarationElement element,
                                                     char sigilToPrepend,
                                                     @NotNull PerlVariableCompletionProcessor completionProcessor) {
    String variableName = StringUtil.notNullize(element.getName());
    if (!completionProcessor.hasBraces()) {
      variableName = PerlVariable.braceName(variableName);
    }

    boolean isBuiltIn = element instanceof PerlBuiltInVariable;

    String explicitNamespaceName = completionProcessor.getExplicitNamespaceName();
    if (StringUtil.isNotEmpty(explicitNamespaceName) && !isBuiltIn && element.isGlobalDeclaration() &&
        !explicitNamespaceName.equals(element.getNamespaceName())) {
      return completionProcessor.result();
    }

    String lookupString = variableName;
    if (!isBuiltIn && element.isGlobalDeclaration() && !completionProcessor.isLexical()) {
      lookupString = element.getCanonicalName();
      if (StringUtil.isEmpty(lookupString)) {
        return completionProcessor.result();
      }
    }

    if (completionProcessor.isFullQualified()) {
      if (!completionProcessor.matches(lookupString)) {
        return completionProcessor.result();
      }
    }
    else {
      if (!completionProcessor.matches(variableName)) {
        return completionProcessor.result();
      }
    }

    Icon icon;
    if (sigilToPrepend != '_') {
      lookupString = sigilToPrepend + lookupString;
      icon = PerlVariableType.bySigil(sigilToPrepend).getIcon();
    }
    else {
      icon = PerlIconProvider.getIcon(element);
    }

    LookupElementBuilder elementBuilder = LookupElementBuilder.create(element, lookupString)
      .withIcon(icon)
      .withPresentableText(variableName);

    if (isBuiltIn) {
      elementBuilder = elementBuilder.withTypeText("Built-in");
    }
    else if (element.isGlobalDeclaration()) {
      elementBuilder = elementBuilder.withTypeText(element.getNamespaceName(), true);
    }

    return completionProcessor.process(
      sigilToPrepend == '_' ?
      elementBuilder :
      elementBuilder.withLookupString(
        completionProcessor.isFullQualified() ? StringUtil.notNullize(element.getCanonicalName()) : variableName));
  }

  public static @Nullable LookupElementBuilder processVariableLookupElement(@NotNull PerlGlobVariable typeGlob,
                                                                            boolean withSigil,
                                                                            @NotNull PerlVariableCompletionProcessor variableCompletionProcessor) {
    String variableName = StringUtil.notNullize(typeGlob.getCanonicalName());
    if (!variableCompletionProcessor.matches(variableName)) {
      return null;
    }
    String lookupString = withSigil ?
                          PerlVariableType.GLOB.getSigil() + variableName :
                          variableName;

    LookupElementBuilder elementBuilder = LookupElementBuilder
      .create(typeGlob, lookupString)
      .withPresentableText(Objects.requireNonNull(typeGlob.getName()))
      .withTypeText(typeGlob.getNamespaceName(), true)
      .withIcon(GLOB_GUTTER_ICON);
    return withSigil ? elementBuilder.withLookupString(variableName) : elementBuilder;
  }

  public static void fillWithUnresolvedVars(@NotNull PerlVariableCompletionProcessorImpl completionProcessor) {
    PsiElement variableNameElement = completionProcessor.getLeafElement();
    final PerlLexicalScope lexicalScope = PsiTreeUtil.getParentOfType(variableNameElement, PerlLexicalScope.class);
    PsiElement perlVariable = variableNameElement.getParent();
    final Set<String> collectedNames = new THashSet<>();

    if (lexicalScope != null && perlVariable instanceof PerlVariable) {
      final int minOffset = variableNameElement.getTextOffset();
      final PerlVariableType actualType = ((PerlVariable)perlVariable).getActualType();

      lexicalScope.accept(new PerlCompletionRecursiveVisitor(completionProcessor) {
        @Override
        public void visitPerlVariable(@NotNull PerlVariable perlVariable) {
          if (perlVariable.isValid() &&
              !(perlVariable.getParent() instanceof PerlVariableDeclarationElement) &&
              perlVariable.getTextOffset() > minOffset &&
              actualType == perlVariable.getActualType()
          ) {
            String variableName = perlVariable.getName();
            if (completionProcessor.matches(variableName) &&
                collectedNames.add(variableName) &&
                perlVariable.getLexicalDeclaration() == null) {
              completionProcessor.process(LookupElementBuilder.create(variableName));
            }
          }
          super.visitPerlVariable(perlVariable);
        }
      });
    }
  }

  public static @NotNull <T extends LookupElement> T setLexical(@NotNull T element) {
    element.putUserData(PerlCompletionWeighter.WEIGHT, 1);
    return element;
  }

  public static void fillWithBuiltInVariables(@NotNull PerlVariableCompletionProcessor completionProcessor) {
    PerlBuiltInVariablesService.getInstance(
      completionProcessor.getProject()).processVariables(createBuiltInVariablesLookupProcessor(completionProcessor));
  }

  public static void fillWithLexicalVariables(@NotNull PerlVariableCompletionProcessor variableCompletionProcessor) {
    PsiElement perlVariable = variableCompletionProcessor.getLeafParentElement();
    Processor<PerlVariableDeclarationElement> lookupProcessor = createLexicalLookupProcessor(
      new PerlDelegatingVariableCompletionProcessor(variableCompletionProcessor) {
        @Override
        public boolean isLexical() {
          return true;
        }
      });

    PsiScopeProcessor processor = (element, __) -> {
      if (!(element instanceof PerlVariableDeclarationElement)) {
        return true;
      }
      PerlVariableDeclarationElement variable = (PerlVariableDeclarationElement)element;
      PsiElement declarationStatement = PsiTreeUtil.getParentOfType(variable, PerlStatement.class);
      if (PsiTreeUtil.isAncestor(declarationStatement, perlVariable, false)) {
        return true;
      }

      if (StringUtil.isNotEmpty(variable.getName())) {
        boolean processResult = lookupProcessor.process(variable);
        if (processResult && variable.isGlobalDeclaration()) {
          variableCompletionProcessor.register(variable.getCanonicalNameWithSigil());
        }
        return processResult;
      }

      return true;
    };
    PerlResolveUtil.treeWalkUp(variableCompletionProcessor.getLeafElement(), processor);
  }


  /**
   * @return lookup generator for lexical variables
   * @see #createLookupGenerator(Processor, boolean, PerlVariableCompletionProcessor)
   */
  private static @NotNull Processor<PerlVariableDeclarationElement> createLexicalLookupProcessor(
    @NotNull PerlVariableCompletionProcessor variableCompletionProcessor) {
    return createVariableLookupProcessor(
      new PerlDelegatingVariableCompletionProcessor(variableCompletionProcessor) {
        @Override
        public void addElement(@NotNull LookupElementBuilder lookupElement) {
          super.addElement(setLexical(lookupElement));
        }
      });
  }

  /**
   * @return lookup generator for built-in variables
   * @see #createLookupGenerator(Processor, boolean, PerlVariableCompletionProcessor)
   */
  private static @NotNull Processor<PerlVariableDeclarationElement> createBuiltInVariablesLookupProcessor(
    @NotNull PerlVariableCompletionProcessor perlVariableCompletionProcessor) {
    return createVariableLookupProcessor(
      new PerlDelegatingVariableCompletionProcessor(perlVariableCompletionProcessor) {
        @Override
        public void addElement(@NotNull LookupElementBuilder lookupElement) {
          super.addElement(lookupElement.withBoldness(true));
        }
      });
  }

  /**
   * @return processor of variable declarations, generating lookup elements for them and feeding to the {@code lookupConsumer}
   */
  private static @NotNull Processor<PerlVariableDeclarationElement> createVariableLookupProcessor(
    @NotNull PerlVariableCompletionProcessor completionProcessor) {

    PsiElement perlVariable = completionProcessor.getLeafParentElement();
    boolean addHashSlices = hasHashSlices(perlVariable);

    PerlDelegatingVariableCompletionProcessor hashElementCompletionProcessor =
      new PerlDelegatingVariableCompletionProcessor(completionProcessor) {
        @Override
        public void addElement(@NotNull LookupElementBuilder lookupElement) {
          super.addElement(lookupElement.withInsertHandler(PerlInsertHandlers.HASH_ELEMENT_INSERT_HANDLER).withTailText("{}"));
        }
      };
    PerlDelegatingVariableCompletionProcessor arrayElementCompletionProcessor =
      new PerlDelegatingVariableCompletionProcessor(completionProcessor) {
        @Override
        public void addElement(@NotNull LookupElementBuilder lookupElement) {
          super.addElement(lookupElement.withInsertHandler(PerlInsertHandlers.ARRAY_ELEMENT_INSERT_HANDLER).withTailText("[]"));
        }
      };

    if (perlVariable instanceof PsiPerlScalarVariable) {
      return variable -> {
        if (variable.getActualType() == PerlVariableType.SCALAR) {
          return processVariableLookupElement(variable, '_', completionProcessor);
        }
        else if (variable.getActualType() == PerlVariableType.ARRAY) {
          return processVariableLookupElement(variable, '_', arrayElementCompletionProcessor);
        }
        else if (variable.getActualType() == PerlVariableType.HASH) {
          return processVariableLookupElement(variable, '_', hashElementCompletionProcessor);
        }
        return completionProcessor.result();
      };
    }
    else if (perlVariable instanceof PsiPerlArrayVariable) {
      return variable -> {
        if (variable.getActualType() == PerlVariableType.ARRAY) {
          return processVariableLookupElement(variable, '_', completionProcessor) &&
                 processVariableLookupElement(variable, '_', arrayElementCompletionProcessor);
        }
        else if (variable.getActualType() == PerlVariableType.HASH) {
          return processVariableLookupElement(variable, '_', hashElementCompletionProcessor);
        }
        return completionProcessor.result();
      };
    }
    else if (perlVariable instanceof PsiPerlArrayIndexVariable) {
      return variable -> {
        if (variable.getActualType() == PerlVariableType.ARRAY) {
          return processVariableLookupElement(variable, '_', completionProcessor);
        }
        return completionProcessor.result();
      };
    }
    else if (perlVariable instanceof PsiPerlHashVariable) {
      return variable -> {
        PerlVariableType variableType = variable.getActualType();
        if (variableType == PerlVariableType.HASH) {
          return processVariableLookupElement(variable, '_', completionProcessor) &&
                 (!addHashSlices || processVariableLookupElement(variable, '_', hashElementCompletionProcessor));
        }
        else if (addHashSlices && variableType == PerlVariableType.ARRAY) {
          return processVariableLookupElement(variable, '_', arrayElementCompletionProcessor);
        }
        return completionProcessor.result();
      };
    }
    else if (perlVariable instanceof PerlGlobVariable) {
      return variable -> processVariableLookupElement(variable, '_', completionProcessor);
    }
    return variable -> {
      PerlVariableType variableType = variable.getActualType();
      if (variableType == PerlVariableType.SCALAR) {
        return processVariableLookupElement(variable, PerlVariableType.SCALAR.getSigil(), completionProcessor);
      }
      else if (variableType == PerlVariableType.ARRAY) {
        return processVariableLookupElement(variable, PerlVariableType.ARRAY.getSigil(), completionProcessor) &&
               processVariableLookupElement(variable, PerlVariableType.SCALAR.getSigil(), arrayElementCompletionProcessor) &&
               processVariableLookupElement(variable, PerlVariableType.ARRAY.getSigil(), arrayElementCompletionProcessor);
      }
      else if (variable.getActualType() == PerlVariableType.HASH) {
        return processVariableLookupElement(variable, PerlVariableType.HASH.getSigil(), completionProcessor) &&
               processVariableLookupElement(variable, PerlVariableType.SCALAR.getSigil(), hashElementCompletionProcessor) &&
               processVariableLookupElement(variable, PerlVariableType.ARRAY.getSigil(), hashElementCompletionProcessor);
      }
      return completionProcessor.result();
    };
  }

  /**
   * @return true iff 5.20 hash/array slices are enabled
   */
  private static boolean hasHashSlices(@NotNull PsiElement psiElement) {
    return !PerlSharedSettings.getInstance(psiElement).getTargetPerlVersion().lesserThan(PerlVersion.V5_20);
  }

  private static final Set<String> VARIABLES_AVAILABLE_ONLY_WITH_FQN = ContainerUtil.newHashSet(
    "ISA", "EXPORT", "EXPORT_OK", "EXPORT_TAGS", "EXPORT_FAIL", "VERSION"
  );

  public static boolean processFullQualifiedVariables(@NotNull PerlVariableCompletionProcessor variableCompletionProcessor) {
    if (!variableCompletionProcessor.isFullQualified() && variableCompletionProcessor.getCompletionParameters().getInvocationCount() == 0) {
      return variableCompletionProcessor.result();
    }
    PsiElement variableNameElement = variableCompletionProcessor.getLeafElement();
    PsiElement perlVariable = variableCompletionProcessor.getLeafParentElement();
    Processor<PerlVariableDeclarationElement> variableProcessor = createVariableLookupProcessor(variableCompletionProcessor);
    Processor<PerlVariableDeclarationElement> lookupGenerator = it -> {
      if (!variableCompletionProcessor.isFullQualified() && VARIABLES_AVAILABLE_ONLY_WITH_FQN.contains(it.getName())) {
        return variableCompletionProcessor.result();
      }

      String idString = it.getCanonicalNameWithSigil();
      if (!variableCompletionProcessor.isRegistered(idString)) {
        return variableProcessor.process(it);
      }
      return variableCompletionProcessor.result();
    };

    Project project = variableNameElement.getProject();
    GlobalSearchScope resolveScope = variableNameElement.getResolveScope();
    String namespaceName = variableCompletionProcessor.getExplicitNamespaceName();

    if (perlVariable instanceof PsiPerlScalarVariable) {
      return PerlScalarUtil.processDefinedGlobalScalars(project, resolveScope, lookupGenerator, false, namespaceName) &&
             PerlArrayUtil.processDefinedGlobalArrays(project, resolveScope, lookupGenerator, false, namespaceName) &&
             PerlHashUtil.processDefinedGlobalHashes(project, resolveScope, lookupGenerator, false, namespaceName);
    }
    else if (perlVariable instanceof PsiPerlArrayVariable) {
      return PerlArrayUtil.processDefinedGlobalArrays(project, resolveScope, lookupGenerator, false, namespaceName) &&
             PerlHashUtil.processDefinedGlobalHashes(project, resolveScope, lookupGenerator, false, namespaceName);
    }
    else if (perlVariable instanceof PsiPerlArrayIndexVariable) {
      // global arrays
      return PerlArrayUtil.processDefinedGlobalArrays(project, resolveScope, lookupGenerator, false, namespaceName);
    }
    else if (perlVariable instanceof PsiPerlHashVariable) {
      // global hashes
      return PerlHashUtil.processDefinedGlobalHashes(project, resolveScope, lookupGenerator, false, namespaceName) &&
             (!hasHashSlices(perlVariable) ||
              PerlArrayUtil.processDefinedGlobalArrays(project, resolveScope, lookupGenerator, false, namespaceName));
    }
    else {
      return PerlScalarUtil.processDefinedGlobalScalars(project, resolveScope, lookupGenerator, false, namespaceName) &&
             PerlArrayUtil.processDefinedGlobalArrays(project, resolveScope, lookupGenerator, false, namespaceName) &&
             PerlHashUtil.processDefinedGlobalHashes(project, resolveScope, lookupGenerator, false, namespaceName) &&
             PerlGlobUtil.processDefinedGlobs(project, resolveScope, null, typeglob ->
               variableCompletionProcessor.process(processVariableLookupElement(
                 typeglob, perlVariable instanceof PsiPerlMethod, variableCompletionProcessor)), false, namespaceName);
    }
  }

  public static void processVariables(@NotNull PerlVariableCompletionProcessor variableCompletionProcessor) {
    if (!variableCompletionProcessor.isFullQualified()) {
      fillWithLexicalVariables(variableCompletionProcessor);
      fillWithBuiltInVariables(variableCompletionProcessor);
      fillWithImportedVariables(variableCompletionProcessor);
    }

    processFullQualifiedVariables(variableCompletionProcessor);
  }

  public static void fillWithImportedVariables(@NotNull PerlVariableCompletionProcessor variableCompletionProcessor) {
    PerlNamespaceDefinitionElement namespaceContainer =
      PerlPackageUtil.getNamespaceContainerForElement(variableCompletionProcessor.getLeafElement());

    if (namespaceContainer == null) {
      return;
    }

    String packageName = namespaceContainer.getNamespaceName();

    if (StringUtil.isEmpty(packageName)) {
      return;
    }

    PerlNamespaceEntityProcessor<PerlExportDescriptor> processor = null;
    PsiElement perlVariable = variableCompletionProcessor.getLeafParentElement();

    if (perlVariable instanceof PsiPerlScalarVariable) {
      processor = (namespaceName, descriptor) -> {
        LookupElementBuilder lookupElement = null;
        String entityName = descriptor.getImportedName();
        if (!variableCompletionProcessor.matches(entityName)) {
          return variableCompletionProcessor.result();
        }
        if (descriptor.isScalar()) {
          lookupElement = processVariableLookupElement(entityName, PerlVariableType.SCALAR);
        }
        else if (descriptor.isArray()) {
          lookupElement = processVariableLookupElement(entityName, PerlVariableType.ARRAY);
        }
        else if (descriptor.isHash()) {
          lookupElement = processVariableLookupElement(entityName, PerlVariableType.HASH);
        }

        if (lookupElement != null) {
          return variableCompletionProcessor.process(lookupElement.withTypeText(descriptor.getRealPackage(), true));
        }
        return variableCompletionProcessor.result();
      };
    }
    else if (perlVariable instanceof PsiPerlArrayVariable || perlVariable instanceof PsiPerlArrayIndexVariable) {
      processor = (namespaceName, descriptor) -> {
        LookupElementBuilder lookupElement = null;
        String entityName = descriptor.getImportedName();
        if (!variableCompletionProcessor.matches(entityName)) {
          return variableCompletionProcessor.result();
        }
        if (descriptor.isArray()) {
          lookupElement = createArrayElementLookupElement(entityName, PerlVariableType.ARRAY);
        }
        else if (descriptor.isHash()) {
          lookupElement = processHashElementLookupElement(entityName, PerlVariableType.HASH);
        }

        if (lookupElement != null) {
          return variableCompletionProcessor.process(lookupElement.withTypeText(descriptor.getRealPackage(), true));
        }
        return variableCompletionProcessor.result();
      };
    }
    else if (perlVariable instanceof PsiPerlHashVariable) {
      processor = (namespaceName, descriptor) -> {
        LookupElementBuilder lookupElement = null;
        if (descriptor.isHash()) {
          String entityName = descriptor.getImportedName();
          if (!variableCompletionProcessor.matches(entityName)) {
            return variableCompletionProcessor.result();
          }
          lookupElement = processVariableLookupElement(entityName, PerlVariableType.HASH);
        }

        if (lookupElement != null) {
          return variableCompletionProcessor.process(lookupElement.withTypeText(descriptor.getRealPackage(), true));
        }
        return variableCompletionProcessor.result();
      };
    }

    if (processor != null) {
      namespaceContainer.processExportDescriptorsWithAst(processor);
    }
  }
}
