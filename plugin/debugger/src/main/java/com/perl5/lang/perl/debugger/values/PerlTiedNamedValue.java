/*
 * Copyright 2015-2021 Alexandr Evstigneev
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

package com.perl5.lang.perl.debugger.values;

import com.intellij.icons.AllIcons;
import com.intellij.xdebugger.frame.XCompositeNode;
import com.intellij.xdebugger.frame.XValueChildrenList;
import com.intellij.xdebugger.frame.XValueGroup;
import com.perl5.lang.perl.debugger.PerlStackFrame;
import com.perl5.lang.perl.debugger.protocol.PerlValueDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class PerlTiedNamedValue extends XValueGroup {
  private final PerlXNamedValue myPerlXNamedValue;

  public PerlTiedNamedValue(@NotNull PerlValueDescriptor tiedValue,
                            @NotNull PerlStackFrame stackFrame) {
    super("Tied with");
    myPerlXNamedValue = new PerlXNamedValue(tiedValue, stackFrame);
  }

  @Override
  public @Nullable Icon getIcon() {
    return AllIcons.Toolwindows.ToolWindowModuleDependencies;
  }

  @Override
  public @NotNull String getSeparator() {
    return ": ";
  }

  @Override
  public @Nullable String getComment() {
    return myPerlXNamedValue.computeType();
  }

  @Override
  public void computeChildren(@NotNull XCompositeNode node) {
    node.addChildren(XValueChildrenList.singleton(myPerlXNamedValue), true);
  }
}
