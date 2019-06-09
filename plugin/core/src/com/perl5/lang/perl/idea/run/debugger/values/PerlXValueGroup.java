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

package com.perl5.lang.perl.idea.run.debugger.values;

import com.intellij.xdebugger.frame.XCompositeNode;
import com.intellij.xdebugger.frame.XValueChildrenList;
import com.intellij.xdebugger.frame.XValueGroup;
import com.perl5.lang.perl.idea.run.debugger.PerlStackFrame;
import com.perl5.lang.perl.idea.run.debugger.protocol.PerlValueDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public class PerlXValueGroup extends XValueGroup {
  private final PerlStackFrame myStackFrame;
  private final String myComment;
  private final Icon myIcon;
  private final PerlValueDescriptor[] myVariables;
  private final boolean myIsAutoExpand;

  public PerlXValueGroup(@NotNull String name,
                         String comment,
                         Icon icon,
                         PerlValueDescriptor[] variables,
                         PerlStackFrame stackFrame,
                         boolean isAutoExpand) {
    super(name);
    myComment = comment;
    myIcon = icon;
    myVariables = variables;
    myStackFrame = stackFrame;
    myIsAutoExpand = isAutoExpand;
  }

  @Override
  public boolean isAutoExpand() {
    return myIsAutoExpand;
  }

  @Override
  public void computeChildren(@NotNull XCompositeNode node) {
    if (myVariables.length == 0) {
      super.computeChildren(node);
    }
    else {
      XValueChildrenList list = new XValueChildrenList();
      for (PerlValueDescriptor descriptor : myVariables) {
        list.add(new PerlXNamedValue(descriptor, myStackFrame));
      }
      node.setAlreadySorted(true);
      node.addChildren(list, true);
    }
  }

  @Nullable
  @Override
  public Icon getIcon() {
    return myIcon;
  }

  @Nullable
  @Override
  public String getComment() {
    return myComment;
  }

  public PerlStackFrame getStackFrame() {
    return myStackFrame;
  }

  public PerlValueDescriptor[] getVariables() {
    return myVariables;
  }

  protected int getSize() {
    return myVariables.length;
  }

  @NotNull
  @Override
  public String getName() {
    return super.getName() + "(" + getSize() + ")";
  }
}
