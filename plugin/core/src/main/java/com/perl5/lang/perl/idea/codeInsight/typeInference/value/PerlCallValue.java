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

package com.perl5.lang.perl.idea.codeInsight.typeInference.value;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.RecursionManager;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.ObjectUtils;
import com.intellij.util.Processor;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import com.perl5.lang.perl.psi.PerlSubElement;
import com.perl5.lang.perl.psi.references.PerlImplicitDeclarationsService;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;
import static com.perl5.lang.perl.util.PerlSubUtil.SUB_AUTOLOAD;

/**
 * Represents a method call value
 */
public abstract class PerlCallValue extends PerlParametrizedOperationValue {
  protected final @NotNull List<PerlValue> myArguments;

  protected PerlCallValue(@NotNull PerlValue namespaceNameValue,
                          @NotNull PerlValue subNameValue,
                          @NotNull List<? extends PerlValue> arguments) {
    super(namespaceNameValue, subNameValue);
    myArguments = List.copyOf(arguments);
  }

  PerlCallValue(@NotNull PerlValueDeserializer deserializer) throws IOException {
    super(deserializer);
    myArguments = deserializer.readValuesList();
  }

  @Override
  protected @NotNull PerlValue computeResolve(@NotNull PerlValue resolvedNamespaceValue,
                                              @NotNull PerlValue resolvedSubNameValue,
                                              @NotNull PerlValueResolver resolver) {
    Set<String> subNames = resolvedSubNameValue.getSubNames();
    if (subNames.isEmpty()) {
      return UNKNOWN_VALUE;
    }

    Set<String> namespaceNames = computeNamespaceNames(resolvedNamespaceValue);
    if (namespaceNames.isEmpty()) {
      return UNKNOWN_VALUE;
    }

    List<PerlValue> resolvedArguments = computeResolvedArguments(resolvedNamespaceValue, resolver);
    PerlValue argumentsValue = PerlArrayValue.builder().addElements(resolvedArguments).build();

    PerlOneOfValue.Builder builder = PerlOneOfValue.builder();
    boolean[] hasTargets = new boolean[]{false};
    RecursionManager.doPreventingRecursion(
      Pair.create(resolver.getResolveScope(), this), true, () -> {
        processCallTargets(resolver.getProject(), resolver.getResolveScope(), resolver.getContextFile(), namespaceNames, subNames, it -> {
          hasTargets[0] = true;
          if (it instanceof PerlSubElement subElement) {
            builder.addVariant(new PerlSubValueResolver(it, argumentsValue).resolve(subElement.getReturnValue()));
            }
            return true;
          });
        return null;
      });

    addFallbackTargets(namespaceNames, subNames, resolvedArguments, hasTargets[0], builder, resolvedNamespaceValue, resolver);

    return builder.build();
  }

  /**
   * @return set of a namepsaces names that should be used for the {@code resolvedNamespaceValue}
   */
  protected @NotNull Set<String> computeNamespaceNames(@NotNull PerlValue resolvedNamespaceValue) {
    return resolvedNamespaceValue.getNamespaceNames();
  }

  /**
   * Computes a fallback value. This method should handle two cases:
   * - invisible/complex constructor, where we can't compute a proper return value
   * - incorrectly lexed namespace FQNs, where {@code Foo::Bar} was lexed and parsed as {@code Foo::Bar()}
   *
   * @param hasTarget true iff we has processed a real target of this call
   */
  protected abstract void addFallbackTargets(@NotNull Set<String> namespaceNames,
                                             @NotNull Set<String> subNames,
                                             @NotNull List<PerlValue> resolvedArguments,
                                             boolean hasTarget,
                                             @NotNull PerlOneOfValue.Builder builder,
                                             @NotNull PerlValue resolvedNamespaceValue,
                                             @NotNull PerlValueResolver resolver);

  /**
   * @return a list of arguments that passed to the call, resolved in the context of {@code contextElement}
   */
  protected @NotNull List<PerlValue> computeResolvedArguments(@NotNull PerlValue resolvedNamespaceValue,
                                                              @NotNull PerlValueResolver valueResolver) {
    return ContainerUtil.map(myArguments, valueResolver::resolve);
  }

  @Override
  protected void serializeData(@NotNull PerlValueSerializer serializer) throws IOException {
    super.serializeData(serializer);
    serializer.writeValuesList(myArguments);
  }

  @Override
  protected final @NotNull PerlContextType getContextType() {
    return PerlContextType.LIST;
  }

  public @NotNull PerlValue getNamespaceNameValue() {
    return getBaseValue();
  }

  public @NotNull PerlValue getSubNameValue() {
    return getParameter();
  }

  /**
   * Processes all possible call targets: subs declarations, definitions and typeglobs with {@code processor}
   * @param contextElement invocation point. Context element, necessary to compute additional imports
   */
  @SuppressWarnings("UnusedReturnValue")
  public final boolean processCallTargets(@NotNull PsiElement contextElement,
                                          @NotNull Processor<? super PsiNamedElement> processor) {
    Project project = contextElement.getProject();
    GlobalSearchScope searchScope = contextElement.getResolveScope();
    Set<String> subNames = getSubNameValue().resolve(contextElement).getSubNames();
    Set<String> namespaceNames = computeNamespaceNames(getNamespaceNameValue().resolve(contextElement));
    return !subNames.isEmpty() && !namespaceNames.isEmpty() &&
           processCallTargets(project, searchScope, contextElement, namespaceNames, subNames, processor);
  }

  /**
   * Processes all possible call targets: subs declarations, definitions and typeglobs with {@code processor} for
   * the {@code namespaceName}
   */
  protected abstract boolean processCallTargets(@NotNull Project project,
                                                @NotNull GlobalSearchScope searchScope,
                                                @Nullable PsiElement contextElement,
                                                @NotNull Set<String> namespaceNames,
                                                @NotNull Set<String> subNames,
                                                @NotNull Processor<? super PsiNamedElement> processor);

  /**
   * Processes all elements in all namespaces targeted by current call qualifying namespace. E.g. for {@code Foo::Bar->foo} processes
   * all elements from {@code Foo::Bar}
   * @param contextElement origin element of a call. Used to process implicit import into the file
   */
  public abstract boolean processTargetNamespaceElements(@NotNull PsiElement contextElement,
                                                         @NotNull PerlNamespaceItemProcessor<? super PsiNamedElement> processor);

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    PerlCallValue value = (PerlCallValue)o;

    return myArguments.equals(value.myArguments);
  }

  protected static boolean processExportDescriptors(@NotNull Project project,
                                                    @NotNull GlobalSearchScope searchScope,
                                                    @NotNull PerlNamespaceItemProcessor<? super PsiNamedElement> processor,
                                                    @NotNull Set<? extends PerlExportDescriptor> exportDescriptors) {
    if (exportDescriptors.isEmpty()) {
      return true;
    }
    boolean[] foundOne = new boolean[]{false};
    for (PerlExportDescriptor exportDescriptor : exportDescriptors) {
      foundOne[0] = false;
      if (!PerlPackageUtil.processCallables(project, searchScope, exportDescriptor.getTargetCanonicalName(), it -> {
        foundOne[0] = true;
        return processor.processImportedItem(it, exportDescriptor);
      })) {
        return false;
      }

      if (!foundOne[0]) {
        if (!processor.processOrphanDescriptor(exportDescriptor)) {
          return false;
        }
      }
    }
    return true;
  }

  protected static boolean processTargetNamespaceElements(@NotNull Project project,
                                                          @NotNull GlobalSearchScope searchScope,
                                                          @NotNull PerlNamespaceItemProcessor<? super PsiNamedElement> processor,
                                                          @NotNull String currentNamespaceName,
                                                          @Nullable PsiElement contextElement) {
    GlobalSearchScope effectiveScope = getEffectiveScope(project, searchScope, currentNamespaceName, contextElement);

    if (!PerlPackageUtil.processCallablesInNamespace(project, effectiveScope, currentNamespaceName, processor::processItem)) {
      return false;
    }

    // exports
    Set<PerlExportDescriptor> exportDescriptors =
      PerlNamespaceDefinitionElement.getExportDescriptors(project, effectiveScope, currentNamespaceName);
    return processExportDescriptors(project, effectiveScope, processor, exportDescriptors);
  }

  @Override
  protected int computeHashCode() {
    int result = super.computeHashCode();
    result = 31 * result + myArguments.hashCode();
    return result;
  }

  protected static boolean processItemsInNamespace(@NotNull Project project,
                                                   @NotNull GlobalSearchScope searchScope,
                                                   @NotNull Set<String> subNames,
                                                   @NotNull Processor<? super PsiNamedElement> processor,
                                                   @NotNull String namespaceName,
                                                   @NotNull ProcessingContext processingContext,
                                                   @Nullable PsiElement contextElement) {
    Processor<PsiNamedElement> processorWrapper = it -> {
      processingContext.processBuiltIns = false;
      processingContext.processAutoload = false;
      return processor.process(it);
    };

    GlobalSearchScope subsEffectiveScope = getEffectiveScope(project, searchScope, namespaceName, contextElement);
    for (String subName : subNames) {
      if (!PerlPackageUtil.processCallables(project, subsEffectiveScope, PerlPackageUtil.join(namespaceName, subName), processorWrapper)) {
        return false;
      }
    }

    // exports
    Set<PerlExportDescriptor> exportDescriptors = PerlNamespaceDefinitionElement.getExportDescriptors(project, searchScope, namespaceName);
    if (!processExportDescriptorsItems(project, searchScope, subNames, processorWrapper, exportDescriptors)) {
      return false;
    }

    // built-ins
    if (processingContext.processBuiltIns) {
      processingContext.processBuiltIns = false;
      for (String subName : subNames) {
        PerlSubDefinitionElement coreSub = PerlImplicitDeclarationsService.getInstance(project).getCoreSub(subName);
        if (coreSub != null && !processorWrapper.process(coreSub)) {
          return false;
        }
      }
    }

    // AUTOLOAD
    return !processingContext.processAutoload ||
           PerlPackageUtil.isUNIVERSAL(namespaceName) || PerlPackageUtil.isCORE(namespaceName) ||
           PerlPackageUtil.processCallables(project, searchScope, PerlPackageUtil.join(namespaceName, SUB_AUTOLOAD), processorWrapper);
  }

  /**
   * Adjust search scope if necessary. Used to handle simple main resolution
   *
   * @return original or adjusted scope to search
   */
  protected static @NotNull GlobalSearchScope getEffectiveScope(@NotNull Project project,
                                                                @NotNull GlobalSearchScope originalScope,
                                                                @NotNull String namespaceName,
                                                                @Nullable PsiElement contextElement) {
    PsiFile contextFile = contextElement == null ? null : contextElement.getContainingFile().getOriginalFile();
    if (PerlPackageUtil.MAIN_NAMESPACE_NAME.equals(namespaceName) &&
        PerlSharedSettings.getInstance(project).SIMPLE_MAIN_RESOLUTION && contextFile != null) {
      return GlobalSearchScope.fileScope(contextFile);
    }
    return originalScope;
  }

  protected static boolean processExportDescriptorsItems(@NotNull Project project,
                                                         @NotNull GlobalSearchScope searchScope,
                                                         @NotNull Set<String> subNames,
                                                         @NotNull Processor<? super PsiNamedElement> processorWrapper,
                                                         @NotNull Set<? extends PerlExportDescriptor> exportDescriptors) {
    for (PerlExportDescriptor exportDescriptor : exportDescriptors) {
      if (subNames.contains(exportDescriptor.getImportedName()) &&
          !PerlPackageUtil.processCallables(project, searchScope, exportDescriptor.getTargetCanonicalName(), processorWrapper)) {
        return false;
      }
    }
    return true;
  }

  protected static class ProcessingContext {
    public boolean processAutoload = true;
    public boolean processBuiltIns = true;
  }

  protected final @NotNull String getPresentableArguments() {
    return StringUtil.join(ContainerUtil.map(myArguments, PerlValue::getPresentableText), ", ");
  }

  protected final String getArgumentsAsString() {
    return "(" + StringUtil.join(ContainerUtil.map(myArguments, PerlValue::toString), ", ") + ")";
  }

  @Contract("null->null")
  public static @Nullable PerlCallValue from(@Nullable PsiElement element) {
    return ObjectUtils.tryCast(PerlValuesManager.from(element), PerlCallValue.class);
  }
}
