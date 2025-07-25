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

package com.perl5.lang.perl.psi;

import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.SmartList;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.util.PerlArrayUtilCore;
import com.perl5.lang.perl.util.PerlContextUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.perl5.lang.perl.psi.utils.PerlContextType.LIST;
import static com.perl5.lang.perl.psi.utils.PerlContextType.SCALAR;


public interface PerlAssignExpression extends PsiPerlExpr {
  /**
   * Returns the leftmost side of assign expression
   *
   * @return left side
   */
  default @NotNull PsiElement getLeftSide() {
    return getFirstChild();
  }

  /**
   * Returns the rightmost side of assignment expression
   *
   * @return rightmost side or null if expression is incomplete
   */
  default @Nullable PsiElement getRightSide() {
    PsiElement lastChild = getLastChild();
    PsiElement firstChild = getFirstChild();

    if (lastChild == firstChild || lastChild == null || lastChild.getFirstChild() == null) {
      return null;
    }

    return lastChild;
  }

  /**
   * @return the operator element on the right from assignment element
   */
  @Contract("null->null")
  default @Nullable PsiElement getRightOperatorElement(@Nullable PsiElement assignmentElement) {
    var assignmentElementRange = assignmentElement == null ? null : assignmentElement.getTextRange();
    if (assignmentElementRange == null || !getTextRange().contains(assignmentElementRange)) {
      return null;
    }
    PsiElement anchor = null;
    for (PsiElement child : getChildren()) {
      if (child.getTextRange().contains(assignmentElementRange)) {
        anchor = child;
        break;
      }
    }
    if (anchor == null) {
      return null;
    }
    PsiElement result = anchor.getPrevSibling();
    while (result != null) {
      if (!PerlPsiUtil.isCommentOrSpace(result)) {
        return result;
      }
      result = result.getPrevSibling();
    }
    return null;
  }

  /**
   * @return a left counterpart of the assignment for the right side.
   * @implNote current implementation works properly only for simple a = b expressions.
   */
  default @Nullable PsiElement getLeftPartOfAssignment(@NotNull PsiElement value) {
    PsiElement rightSide = getRightSide();
    if (rightSide == null) {
      return null;
    }
    List<PsiElement> elements = flattenAssignmentPart(getRightSide());
    if (elements.size() != 1 || !elements.getFirst().equals(value)) {
      return null;
    }
    return getLeftSide();
  }

  /**
   * @return a descriptor of value assigned to the {@code leftPartElement} if any. Descriptor contains a list of {@code psiElement}s and index in
   * the element, if it's necessary. null means there is no element, or assignment, or it's the rightmost part of it.
   * <br/>
   * In case of {@code my ($var, $var2) = @_;} for {@code $var2} will return a descriptor with
   * {@code @_} as PsiElement and 1 as index.
   * <br/>
   * In case of transitive assignments, always returns rightmost value. E.g. {@code $var1 = $var2 = $var3;} will return a
   * {@code $var3} for {@code $var1} and {@code $var2}
   * <br/>
   * In case of {@code my @arr = ($var1, $var2)} will return a descriptor of {@code $var1} and {@code $var2} and zero index.
   * fixme use real right part. To handle my $var = undef = $var;
   */
  default @Nullable PerlAssignValueDescriptor getRightPartOfAssignment(@NotNull PsiElement leftPartElement) {
    List<PsiElement> children = PerlPsiUtil.cleanupChildren(getChildren());
    if (children.size() < 2) {
      return null;
    }
    TextRange leftPartTextRange = leftPartElement.getTextRange();
    PsiElement leftAssignPart = null;
    PsiElement rightAssignPart = null;
    for (int i = 0; i < children.size() - 1; i++) {
      PsiElement child = children.get(i);
      if (child.getTextRange().contains(leftPartTextRange)) {
        leftAssignPart = child;
        rightAssignPart = children.get(i + 1);
        break;
      }
    }
    if (leftAssignPart == null) {
      return null;
    }

    boolean found = false;
    int leftElementIndex = 0;
    for (PsiElement leftElement : flattenAssignmentPart(leftAssignPart)) {
      if (leftElement.getTextRange().equals(leftPartTextRange)) {
        found = true;
        break;
      }
      if (PerlContextUtil.isList(leftElement)) {
        return null;
      }
      leftElementIndex++;
    }

    if (!found) {
      return null;
    }

    List<PsiElement> rightElements = flattenAssignmentPart(rightAssignPart);
    if (rightElements.isEmpty()) {
      return null;
    }
    if (PerlContextUtil.isScalar(leftAssignPart)) {
      PsiElement lastItem = ContainerUtil.getLastItem(rightElements);
      return new PerlAssignValueDescriptor(Objects.requireNonNull(lastItem), PerlContextUtil.isList(lastItem) ? -1 : 0);
    }

    PerlContextType leftContextType = PerlContextUtil.contextFrom(leftPartElement);
    for (int i = 0; i < rightElements.size(); i++) {
      PsiElement rightElement = rightElements.get(i);
      if (leftElementIndex == 0) {
        if (leftContextType == SCALAR) {
          return new PerlAssignValueDescriptor(rightElement);
        }
        return new PerlAssignValueDescriptor(rightElements.subList(i, rightElements.size()));
      }
      if (PerlContextUtil.contextFrom(rightElement) == LIST) {
        return new PerlAssignValueDescriptor(rightElements.subList(i, rightElements.size()), leftElementIndex);
      }
      leftElementIndex--;
    }
    return null;
  }

  /**
   * @return an assignment expression if {@code element} is a part of one.
   * Unwraps multi-variable declarations and passing through any empty wrappers, e.g. variable declaration
   */
  @Contract("null->null")
  static @Nullable PerlAssignExpression getAssignmentExpression(@Nullable PsiElement element) {
    if (element == null) {
      return null;
    }
    PerlAssignExpression assignExpression = PsiTreeUtil.getParentOfType(element, PerlAssignExpression.class);
    if (assignExpression == null) {
      return null;
    }

    TextRange elementTextRange = element.getTextRange();
    PsiElement container = ContainerUtil.find(assignExpression.getChildren(), it -> it.getTextRange().contains(elementTextRange));

    if (container.equals(element)) {
      return assignExpression;
    }

    return ContainerUtil.find(flattenAssignmentPart(container), it -> it != null && it.getTextRange().equals(elementTextRange)) != null ?
           assignExpression : null;
  }

  /**
   * Flattens {@code element} by collecting list of elements and unpacking list of declarations.
   *
   * @return Flattered list of assignment participants
   */
  static @NotNull List<PsiElement> flattenAssignmentPart(@NotNull PsiElement element) {
    List<PsiElement> result = new SmartList<>();
    for (PsiElement listElement : PerlArrayUtilCore.collectListElements(element)) {
      if (listElement instanceof PerlVariableDeclarationExpr) {
        ContainerUtil.addAll(result, listElement.getChildren());
      }
      else {
        result.add(listElement);
      }
    }
    return ContainerUtil.map(
      result, it -> it instanceof PerlVariableDeclarationElement && it.getFirstChild() != null ? it.getFirstChild() : it);
  }

  class PerlAssignValueDescriptor {
    public static final PerlAssignValueDescriptor EMPTY = new PerlAssignValueDescriptor(Collections.emptyList(), 0);

    private final @NotNull List<PsiElement> myElements;
    private final int myStartIndex;

    public PerlAssignValueDescriptor(@NotNull PsiElement element) {
      this(Collections.singletonList(element), 0);
    }

    public PerlAssignValueDescriptor(@NotNull List<? extends PsiElement> elements) {
      this(elements, 0);
    }

    public PerlAssignValueDescriptor(@NotNull PsiElement element, int startIndex) {
      this(Collections.singletonList(element), startIndex);
    }

    public PerlAssignValueDescriptor(@NotNull List<? extends PsiElement> elements, int startIndex) {
      myElements = unflattenElements(elements);
      myStartIndex = startIndex;
    }

    private List<PsiElement> unflattenElements(@NotNull List<? extends PsiElement> sourceElements) {
      if (sourceElements.isEmpty()) {
        return Collections.emptyList();
      }
      List<PsiElement> result;
      while (true) {
        result = new ArrayList<>();
        boolean modified = false;
        for (int i = 0; i < sourceElements.size(); i++) {
          PsiElement element = sourceElements.get(i);
          PsiElement parent = element.getParent();
          PsiElement[] children = parent.getChildren();
          if (children.length == 1) {
            result.add(parent);
            modified = true;
            continue;
          }

          if (element.equals(children[0])) {
            int lastElementIndex = sourceElements.indexOf(children[children.length - 1]);
            if (lastElementIndex > 0) {
              result.add(parent);
              modified = true;
              i = lastElementIndex;
              continue;
            }
          }

          result.add(element);
        }
        if (!modified) {
          break;
        }
        sourceElements = result;
      }
      // falling through parens
      result = ContainerUtil.map(result, it -> {
        while (it instanceof PsiPerlParenthesisedExpr) {
          PsiElement[] children = it.getChildren();
          if (children.length == 1) {
            it = children[0];
          }
          else {
            break;
          }
        }
        return it;
      });

      return result;
    }

    public @NotNull List<PsiElement> getElements() {
      return myElements;
    }

    /**
     * @return start index in the target. E.g. for second element of {@code @list} it's going to be 1. For last one {@code -1} just like in
     * perl itself
     */
    public int getStartIndex() {
      return myStartIndex;
    }

    @Override
    public String toString() {
      if (myElements.isEmpty()) {
        return "Empty assign expression";
      }
      return StringUtil.join(ContainerUtil.map(myElements, PsiElement::toString), ", ") +
             (myStartIndex == 0 ? "" : " [" + myStartIndex + "]");
    }

    public @Nullable String getText() {
      if (myElements.isEmpty()) {
        return null;
      }
      PsiElement firstElement = myElements.getFirst();
      PsiElement lastElement = Objects.requireNonNull(ContainerUtil.getLastItem(myElements));
      return firstElement.getContainingFile().getText().substring(
        firstElement.getTextRange().getStartOffset(), lastElement.getTextRange().getEndOffset());
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      PerlAssignValueDescriptor that = (PerlAssignValueDescriptor)o;

      if (myStartIndex != that.myStartIndex) {
        return false;
      }
      return myElements.equals(that.myElements);
    }

    @Override
    public int hashCode() {
      int result = myElements.hashCode();
      result = 31 * result + myStartIndex;
      return result;
    }
  }
}
