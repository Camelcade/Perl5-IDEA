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

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ObjectUtils;
import com.intellij.util.SmartList;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.psi.utils.PerlContextType;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.util.PerlArrayUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.perl5.lang.perl.psi.utils.PerlContextType.LIST;
import static com.perl5.lang.perl.psi.utils.PerlContextType.SCALAR;

/**
 * Created by hurricup on 30.04.2016.
 */
public interface PerlAssignExpression extends PsiPerlExpr {
  /**
   * Returns the leftmost side of assign expression
   *
   * @return left side
   */
  @NotNull
  default PsiElement getLeftSide() {
    return getFirstChild();
  }

  /**
   * Returns the rightmost side of assignment expression
   *
   * @return rightmost side or null if expression is incomplete
   */
  @Nullable
  default PsiElement getRightSide() {
    PsiElement lastChild = getLastChild();
    PsiElement firstChild = getFirstChild();

    if (lastChild == firstChild || lastChild == null || lastChild.getFirstChild() == null) {
      return null;
    }

    return lastChild;
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
  @Nullable
  default PerlAssignValueDescriptor getRightPartOfAssignment(@NotNull PsiElement leftPartElement) {
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
      if (PerlContextType.isList(leftElement)) {
        return null;
      }
      leftElementIndex++;
    }

    if (!found) {
      Logger.getInstance(PerlAssignExpression.class)
        .error("Unable to find a declaration: " + this.getText() + ": " + leftPartElement.getText());
      return null;
    }

    List<PsiElement> rightElements = flattenAssignmentPart(rightAssignPart);
    if (rightElements.isEmpty()) {
      return null;
    }
    if (PerlContextType.isScalar(leftAssignPart)) {
      PsiElement lastItem = ContainerUtil.getLastItem(rightElements);
      return new PerlAssignValueDescriptor(ObjectUtils.notNull(lastItem), PerlContextType.isList(lastItem) ? -1 : 0);
    }

    PerlContextType leftContextType = PerlContextType.from(leftPartElement);
    for (int i = 0; i < rightElements.size(); i++) {
      PsiElement rightElement = rightElements.get(i);
      if (leftElementIndex == 0) {
        if (leftContextType == SCALAR) {
          return new PerlAssignValueDescriptor(rightElement);
        }
        return new PerlAssignValueDescriptor(rightElements.subList(i, rightElements.size()));
      }
      if (PerlContextType.from(rightElement) == LIST) {
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
  @Nullable
  @Contract("null->null")
  static PerlAssignExpression getAssignmentExpression(@Nullable PsiElement element) {
    if (element == null) {
      return null;
    }
    PerlAssignExpression assignExpression = PsiTreeUtil.getParentOfType(element, PerlAssignExpression.class);
    if (assignExpression == null) {
      return null;
    }

    TextRange elementTextRange = element.getTextRange();
    PsiElement container = ContainerUtil.find(assignExpression.getChildren(), it -> it.getTextRange().contains(elementTextRange));
    if (container == null) {
      return null;
    }

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
  @NotNull
  static List<PsiElement> flattenAssignmentPart(@NotNull PsiElement element) {
    List<PsiElement> result = new SmartList<>();
    for (PsiElement listElement : PerlArrayUtil.collectListElements(element)) {
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

    @NotNull
    private final List<PsiElement> myElements;
    private final int myStartIndex;

    public PerlAssignValueDescriptor(@NotNull PsiElement element) {
      this(Collections.singletonList(element), 0);
    }

    public PerlAssignValueDescriptor(@NotNull List<PsiElement> elements) {
      this(elements, 0);
    }

    public PerlAssignValueDescriptor(@NotNull PsiElement element, int startIndex) {
      this(Collections.singletonList(element), startIndex);
    }

    public PerlAssignValueDescriptor(@NotNull List<PsiElement> elements, int startIndex) {
      myElements = unflattenElements(elements);
      myStartIndex = startIndex;
    }

    private List<PsiElement> unflattenElements(@NotNull List<PsiElement> sourceElements) {
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

    @NotNull
    public List<PsiElement> getElements() {
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
        return null;
      }
      return StringUtil.join(ContainerUtil.map(myElements, PsiElement::toString), ", ") +
             (myStartIndex == 0 ? "" : " [" + myStartIndex + "]");
    }

    @Nullable
    public String getText() {
      if (myElements.isEmpty()) {
        return null;
      }
      PsiElement firstElement = myElements.get(0);
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
