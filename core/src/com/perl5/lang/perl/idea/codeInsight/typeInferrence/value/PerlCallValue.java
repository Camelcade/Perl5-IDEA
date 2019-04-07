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

package com.perl5.lang.perl.idea.codeInsight.typeInferrence.value;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.ObjectUtils;
import com.intellij.util.PairProcessor;
import com.intellij.util.Processor;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.PerlSub;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import com.perl5.lang.perl.psi.references.PerlImplicitDeclarationsService;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;

import static com.perl5.lang.perl.util.PerlSubUtil.SUB_AUTOLOAD;

/**
 * Represents a method call value
 */
public abstract class PerlCallValue extends PerlValue {
  @NotNull
  protected final PerlValue myNamespaceNameValue;
  @NotNull
  protected final PerlValue mySubNameValue;
  @NotNull
  protected final List<PerlValue> myArguments;

  public PerlCallValue(@NotNull PerlValue namespaceNameValue,
                       @NotNull PerlValue subNameValue,
                       @NotNull List<PerlValue> arguments,
                       @Nullable PerlValue bless) {
    super(bless);
    myNamespaceNameValue = namespaceNameValue;
    mySubNameValue = subNameValue;
    myArguments = Collections.unmodifiableList(new ArrayList<>(arguments));
  }

  public PerlCallValue(@NotNull StubInputStream dataStream) throws IOException {
    super(dataStream);
    myNamespaceNameValue = PerlValuesManager.deserialize(dataStream);
    mySubNameValue = PerlValuesManager.deserialize(dataStream);
    int argumentsNumber = dataStream.readInt();
    List<PerlValue> arguments = new ArrayList<>(argumentsNumber);
    for (int i = 0; i < argumentsNumber; i++) {
      arguments.add(PerlValuesManager.deserialize(dataStream));
    }
    myArguments = Collections.unmodifiableList(arguments);
  }

  @Override
  protected void serializeData(@NotNull StubOutputStream dataStream) throws IOException {
    myNamespaceNameValue.serialize(dataStream);
    mySubNameValue.serialize(dataStream);
    dataStream.writeInt(myArguments.size());
    for (PerlValue argument : myArguments) {
      argument.serialize(dataStream);
    }
  }

  @NotNull
  public PerlValue getNamespaceNameValue() {
    return myNamespaceNameValue;
  }

  @NotNull
  public PerlValue getSubNameValue() {
    return mySubNameValue;
  }

  @NotNull
  @Override
  protected Set<String> getNamespaceNames(@NotNull Project project,
                                          @NotNull GlobalSearchScope searchScope,
                                          @Nullable Set<PerlValue> recursion) {
    if (recursion != null && !recursion.add(this)) {
      return Collections.emptySet();
    }
    return PerlValuesCacheService.getInstance(project).getNamespaceNames(this, () ->
      getReturnValue(project, searchScope).getNamespaceNames(project, searchScope, ObjectUtils.notNull(recursion, new HashSet<>())));
  }

  @NotNull
  @Override
  protected Set<String> getSubNames(@NotNull Project project, @NotNull GlobalSearchScope searchScope, @Nullable Set<PerlValue> recursion) {
    if (recursion != null && !recursion.add(this)) {
      return Collections.emptySet();
    }
    return PerlValuesCacheService.getInstance(project).getSubsNames(this, () ->
      getReturnValue(project, searchScope).getSubNames(project, searchScope, ObjectUtils.notNull(recursion, new HashSet<>()))
    );
  }

  /**
   * @return return values of this value targets
   */
  @NotNull
  private PerlValue getReturnValue(@NotNull Project project, @NotNull GlobalSearchScope searchScope) {
    return PerlValuesCacheService.getInstance(project).getReturnValue(this, () -> {
      PerlOneOfValue.Builder builder = new PerlOneOfValue.Builder();
      processCallTargets(project, searchScope, null, (namespace, it) -> {
        if (it instanceof PerlSub) {
          builder.addVariant(((PerlSub)it).getReturnValue(namespace, myArguments));
        }
        return true;
      });
      return builder.build();
    });
  }

  /**
   * Processes all possible call targets: subs declarations, definitions and typeglobs with {@code processor}
   * @param contextElement invocation point. Context element, necessary to compute additional imports
   */
  public final boolean processCallTargets(@NotNull Project project,
                                          @NotNull GlobalSearchScope searchScope,
                                          @Nullable PsiElement contextElement,
                                          @NotNull PairProcessor<String, ? super PsiNamedElement> processor) {
    Set<String> subNames = mySubNameValue.getSubNames(project, searchScope);
    Set<String> namespaceNames = myNamespaceNameValue.getNamespaceNames(project, searchScope);
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
                                                @NotNull PairProcessor<String, ? super PsiNamedElement> processor);

  /**
   * Processes all elements in all namespaces targeted by current call qualifying namespace. E.g. for {@code Foo::Bar->foo} processes
   * all elements from {@code Foo::Bar}
   * @param contextElement origin element of a call. Used to process implicit import into the file
   */
  public abstract boolean processTargetNamespaceElements(@NotNull Project project,
                                                         @NotNull GlobalSearchScope searchScope,
                                                         @Nullable PsiElement contextElement,
                                                         @NotNull PerlNamespaceItemProcessor<? super PsiNamedElement> processor);

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
    if (!processExportDescriptors(project, searchScope, processor, exportDescriptors)) {
      return false;
    }

    return true;
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

    PerlCallValue call = (PerlCallValue)o;

    if (!myNamespaceNameValue.equals(call.myNamespaceNameValue)) {
      return false;
    }
    if (!mySubNameValue.equals(call.mySubNameValue)) {
      return false;
    }
    return myArguments.equals(call.myArguments);
  }

  @Override
  protected int computeHashCode() {
    int result = super.computeHashCode();
    result = 31 * result + myNamespaceNameValue.hashCode();
    result = 31 * result + mySubNameValue.hashCode();
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
    if (processingContext.processAutoload &&
        !PerlPackageUtil.isUNIVERSAL(namespaceName) && !PerlPackageUtil.isCORE(namespaceName) &&
        !PerlSubUtil.processRelatedItems(project, searchScope, PerlPackageUtil.join(namespaceName, SUB_AUTOLOAD), processorWrapper)) {
      return false;
    }
    return true;
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

  @Nullable
  @Contract("null->null")
  public static PerlCallValue from(@Nullable PsiElement element) {
    return ObjectUtils.tryCast(PerlValue.from(element), PerlCallValue.class);
  }
}
