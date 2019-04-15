/*
 * Copyright 2015-2018 Alexandr Evstigneev
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
import com.intellij.openapi.util.RecursionManager;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.ObjectUtils;
import com.intellij.util.Processor;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import com.perl5.lang.perl.psi.PerlSubElement;
import com.perl5.lang.perl.psi.references.PerlImplicitDeclarationsService;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.ARGUMENTS_VALUE;
import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;
import static com.perl5.lang.perl.util.PerlSubUtil.SUB_AUTOLOAD;

/**
 * Represents a method call value
 */
public abstract class PerlCallValue extends PerlParametrizedOperationValue {
  @NotNull
  protected final List<PerlValue> myArguments;

  protected PerlCallValue(@NotNull PerlValue namespaceNameValue,
                          @NotNull PerlValue subNameValue,
                          @NotNull List<PerlValue> arguments) {
    super(namespaceNameValue, subNameValue);
    myArguments = Collections.unmodifiableList(new ArrayList<>(arguments));
  }

  PerlCallValue(@NotNull StubInputStream dataStream) throws IOException {
    super(dataStream);
    myArguments = PerlValuesManager.readList(dataStream);
  }

  @NotNull
  @Override
  protected final PerlValue computeResolve(@NotNull PsiElement contextElement,
                                           @NotNull PerlValue resolvedNamespaceValue,
                                           @NotNull PerlValue resolvedSubNameValue,
                                           @NotNull Map<PerlValue, PerlValue> substitutions) {
    Set<String> subNames = resolvedSubNameValue.getSubNames();
    if (subNames.isEmpty()) {
      return UNKNOWN_VALUE;
    }
    if (subNames.size() == 1 && "new".equals(subNames.iterator().next())) {
      return resolvedNamespaceValue;
    }

    Set<String> namespaceNames = resolvedNamespaceValue.getNamespaceNames();
    if (namespaceNames.isEmpty()) {
      return UNKNOWN_VALUE;
    }

    List<PerlValue> resolvedArguments = computeResolvedArguments(resolvedNamespaceValue, contextElement, substitutions);
    PerlArrayValue argumentsValue =
      resolvedArguments.isEmpty() ? PerlArrayValue.EMPTY_ARRAY : PerlArrayValue.builder().addElements(resolvedArguments).build();
    Map<PerlValue, PerlValue> innerSubstitutions = Collections.singletonMap(ARGUMENTS_VALUE, argumentsValue);

    GlobalSearchScope resolveScope = contextElement.getResolveScope();
    PerlOneOfValue.Builder builder = PerlOneOfValue.builder();
    RecursionManager.doPreventingRecursion(
      new Object[]{resolveScope, resolvedNamespaceValue, resolvedSubNameValue, innerSubstitutions}, true, () -> {
        processCallTargets(
          contextElement.getProject(), resolveScope, contextElement, namespaceNames, subNames, it -> {
            if (it instanceof PerlSubElement) {
              builder.addVariant(((PerlSubElement)it).getReturnValue().resolve(it, innerSubstitutions));
            }
            return true;
          });
        return null;
      });
    return builder.build();
  }

  /**
   * @return a list of arguments that passed to the call, resolved in the context of {@code contextElement}
   */
  @NotNull
  protected List<PerlValue> computeResolvedArguments(@NotNull PerlValue resolvedNamespaceValue,
                                                     @NotNull PsiElement contextElement,
                                                     @NotNull Map<PerlValue, PerlValue> substitutions) {
    return ContainerUtil.map(myArguments, it -> it.resolve(contextElement, substitutions));
  }

  @Override
  protected void serializeData(@NotNull StubOutputStream dataStream) throws IOException {
    super.serializeData(dataStream);
    PerlValuesManager.writeCollection(dataStream, myArguments);
  }

  @NotNull
  @Override
  protected final PerlContextType getContextType() {
    return PerlContextType.LIST;
  }

  @NotNull
  public PerlValue getNamespaceNameValue() {
    return getBaseValue();
  }

  @NotNull
  public PerlValue getSubNameValue() {
    return getParameter();
  }

  /**
   * Processes all possible call targets: subs declarations, definitions and typeglobs with {@code processor}
   * @param contextElement invocation point. Context element, necessary to compute additional imports
   */
  public final boolean processCallTargets(@NotNull PsiElement contextElement,
                                          @NotNull Processor<? super PsiNamedElement> processor) {
    Project project = contextElement.getProject();
    GlobalSearchScope searchScope = contextElement.getResolveScope();
    Set<String> subNames = getSubNameValue().resolve(contextElement).getSubNames();
    Set<String> namespaceNames = getNamespaceNameValue().resolve(contextElement).getNamespaceNames();
    return !subNames.isEmpty() && !namespaceNames.isEmpty() &&
           processCallTargets(project, searchScope, contextElement, namespaceNames, subNames, processor);
  }

  /**
   * Processes all possible call targets: subs declarations, definitions and typeglobs with {@code processor} for
   * the {@code namespaceName}
   */
  protected abstract boolean processCallTargets(@NotNull Project project,
                                                @NotNull GlobalSearchScope searchScope,
                                                @NotNull PsiElement contextElement,
                                                @NotNull Set<String> namespaceNames,
                                                @NotNull Set<String> subNames,
                                                @NotNull Processor<? super PsiNamedElement> processor);

  /**
   * Processes all elements in all namespaces targeted by current call qualifying namespace. E.g. for {@code Foo::Bar->foo} processes
   * all elements from {@code Foo::Bar}
   * @param contextElement origin element of a call. Used to process implicit import into the file
   */
  public abstract boolean processTargetNamespaceElements(@NotNull Project project,
                                                         @NotNull GlobalSearchScope searchScope,
                                                         @NotNull PsiElement contextElement,
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
                                                    @NotNull Set<PerlExportDescriptor> exportDescriptors) {
    if (exportDescriptors.isEmpty()) {
      return true;
    }
    boolean[] foundOne = new boolean[]{false};
    for (PerlExportDescriptor exportDescriptor : exportDescriptors) {
      foundOne[0] = false;
      if (!PerlSubUtil.processRelatedItems(project, searchScope, exportDescriptor.getTargetCanonicalName(), it -> {
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
                                                          @NotNull String currentNamespaceName) {
    if (!PerlSubUtil.processRelatedItemsInPackage(project, searchScope, currentNamespaceName, it -> processor.processItem(it))) {
      return false;
    }

    // exports
    Set<PerlExportDescriptor> exportDescriptors = new HashSet<>();
    PerlNamespaceDefinitionElement.processExportDescriptors(project, searchScope, currentNamespaceName, (__, it) -> {
      exportDescriptors.add(it);
      return true;
    });
    return processExportDescriptors(project, searchScope, processor, exportDescriptors);
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
                                                   @NotNull ProcessingContext processingContext) {
    Processor<PsiNamedElement> processorWrapper = it -> {
      processingContext.processBuiltIns = false;
      processingContext.processAutoload = false;
      return processor.process(it);
    };

    // fixme simple main resolution
    // declared subs
    for (String subName : subNames) {
      if (!PerlSubUtil.processRelatedItems(project, searchScope, PerlPackageUtil.join(namespaceName, subName), processorWrapper)) {
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
           PerlSubUtil.processRelatedItems(project, searchScope, PerlPackageUtil.join(namespaceName, SUB_AUTOLOAD), processorWrapper);
  }

  protected static boolean processExportDescriptorsItems(@NotNull Project project,
                                                         @NotNull GlobalSearchScope searchScope,
                                                         @NotNull Set<String> subNames,
                                                         @NotNull Processor<PsiNamedElement> processorWrapper,
                                                         @NotNull Set<PerlExportDescriptor> exportDescriptors) {
    for (PerlExportDescriptor exportDescriptor : exportDescriptors) {
      if (subNames.contains(exportDescriptor.getImportedName()) &&
          !PerlSubUtil.processRelatedItems(project, searchScope, exportDescriptor.getTargetCanonicalName(), processorWrapper)) {
        return false;
      }
    }
    return true;
  }

  protected static class ProcessingContext {
    public boolean processAutoload = true;
    public boolean processBuiltIns = true;
  }

  @NotNull
  protected final String getPresentableArguments() {
    return StringUtil.join(ContainerUtil.map(myArguments, PerlValue::getPresentableText), ", ");
  }

  protected final String getArgumentsAsString() {
    return "(" + StringUtil.join(ContainerUtil.map(myArguments, PerlValue::toString), ", ") + ")";
  }

  @SuppressWarnings("NullableProblems")
  @Nullable
  @Contract("null->null")
  public static PerlCallValue from(@Nullable PsiElement element) {
    return ObjectUtils.tryCast(PerlValue.from(element), PerlCallValue.class);
  }
}
