package com.perl5.lang.perl.idea.run.debugger;

import com.intellij.xdebugger.frame.XCompositeNode;
import com.intellij.xdebugger.frame.XValueChildrenList;
import com.intellij.xdebugger.frame.XValueGroup;
import com.perl5.lang.perl.idea.run.debugger.protocol.PerlValueDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by evstigneev on 10.05.2016.
 */
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
