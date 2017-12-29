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

package com.perl5.lang.perl.idea.run.debugger;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.ResolveState;
import com.intellij.util.ReflectionUtil;
import com.intellij.util.ThreeState;
import com.intellij.xdebugger.XSourcePosition;
import com.intellij.xdebugger.frame.*;
import com.intellij.xdebugger.impl.XSourcePositionImpl;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.run.debugger.protocol.PerlLayersDescriptor;
import com.perl5.lang.perl.idea.run.debugger.protocol.PerlValueDescriptor;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.references.scopes.PerlVariableDeclarationSearcher;
import com.perl5.lang.perl.psi.utils.PerlResolveUtil;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.PerlDebugUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by hurricup on 08.05.2016.
 */
public class PerlXNamedValue extends XNamedValue {
  private static Method mySourcePositionMethod;
  private static Method myLegacyMethod;

  static {
    mySourcePositionMethod = ReflectionUtil.getMethod(XInlineDebuggerDataCallback.class, "computed", XSourcePosition.class);
    if (mySourcePositionMethod == null) {
      myLegacyMethod =
        ReflectionUtil.getMethod(XInlineDebuggerDataCallback.class, "computed", VirtualFile.class, Document.class, int.class);
    }
  }

  private final PerlStackFrame myStackFrame;
  private final PerlValueDescriptor myPerlValueDescriptor;
  private int[] offset = new int[]{0};

  public PerlXNamedValue(@NotNull PerlValueDescriptor descriptor, PerlStackFrame stackFrame) {
    super(descriptor.getName());
    myPerlValueDescriptor = descriptor;
    myStackFrame = stackFrame;
  }

  @Override
  public void computeChildren(@NotNull XCompositeNode node) {
    boolean isExpandable = myPerlValueDescriptor.isExpandable();
    PerlLayersDescriptor layers = myPerlValueDescriptor.getLayers();
    PerlValueDescriptor tiedWith = myPerlValueDescriptor.getTiedWith();
    if (!isExpandable && layers == null && tiedWith == null || StringUtil.isEmpty(myPerlValueDescriptor.getKey())) {
      super.computeChildren(node);
    }

    XValueChildrenList childrenList = new XValueChildrenList();
    if (layers != null) {
      childrenList.add(new PerlXLayersNamedValue(layers));
    }

    //if(tiedWith != null ){
    //  childrenList.add(new PerlXNamedValue(tiedWith, myStackFrame));
    //}

    if (childrenList.size() > 0) {
      node.addChildren(childrenList, false);
    }

    if (isExpandable) {
      PerlDebugUtil.requestAndComputeChildren(node, myStackFrame, offset, myPerlValueDescriptor.getSize(), myPerlValueDescriptor.getKey());
    }
  }


  @Override
  public void computePresentation(@NotNull XValueNode node, @NotNull XValuePlace place) {
    node.setPresentation(calculateIcon(), calculateType(), myPerlValueDescriptor.getValue(), myPerlValueDescriptor.isExpandable());
  }

  protected String calculateType() {
    String value = myPerlValueDescriptor.getType();

    int refDepth = myPerlValueDescriptor.getRefDepth();
    if (refDepth == 1) {
      value = "REF to " + value;
    }
    else if (refDepth > 0) {
      value = "REF(" + refDepth + ") to " + value;
    }

    if (myPerlValueDescriptor.getLayers() != null) {
      value += ", IOLayers";
    }

    return value;
  }

  @Nullable
  protected Icon calculateIcon() {
    String type = myPerlValueDescriptor.getType();
    if (StringUtil.isEmpty(type)) {
      return null;
    }
    else if (StringUtil.startsWith(type, "SCALAR")) {
      return myPerlValueDescriptor.isUtf() ? PerlIcons.UTF_SCALAR_GUTTER_ICON : PerlIcons.SCALAR_GUTTER_ICON;
    }
    else if (StringUtil.startsWith(type, "ARRAY")) {
      return PerlIcons.ARRAY_GUTTER_ICON;
    }
    else if (StringUtil.startsWith(type, "HASH")) {
      return PerlIcons.HASH_GUTTER_ICON;
    }
    else if (StringUtil.startsWith(type, "CODE")) {
      return PerlIcons.SUB_GUTTER_ICON;
    }
    else if (StringUtil.startsWith(type, "GLOB")) {
      return PerlIcons.GLOB_GUTTER_ICON;
    }
    else if (StringUtil.startsWith(type, "FORMAT")) {
      return PerlIcons.FORMAT_GUTTER_ICON;
    }
    else if (StringUtil.startsWith(type, "IO::File")) {
      return PerlIcons.HANDLE_GUTTER_ICON;
    }
    else if (StringUtil.startsWith(type, "Regexp")) {
      return PerlIcons.REGEX_GUTTER_ICON;
    }
    else if (myPerlValueDescriptor.isBlessed()) {
      return PerlIcons.PACKAGE_GUTTER_ICON;
    }
    return null;
  }


  @Override
  public void computeSourcePosition(@NotNull XNavigatable navigatable) {
    if (!computeMySourcePosition(navigatable, null)) {
      super.computeSourcePosition(navigatable);
    }
  }

  protected boolean computeMySourcePosition(@Nullable XNavigatable navigatable, @Nullable final XInlineDebuggerDataCallback callback) {
    String name = myPerlValueDescriptor.getName();

    if (StringUtil.isEmpty(name) || name.length() < 2) {
      return false;
    }

    final PerlVariableType variableType = PerlVariableType.bySigil(name.charAt(0));
    if (variableType == null || variableType == PerlVariableType.CODE) {
      return false;
    }

    final String variableName = name.substring(1);

    final XSourcePosition sourcePosition = myStackFrame.getSourcePosition();
    if (sourcePosition == null) {
      return false;
    }

    final Project project = myStackFrame.getPerlExecutionStack().getSuspendContext().getDebugSession().getProject();
    final VirtualFile virtualFile = sourcePosition.getFile();
    PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);

    if (!(psiFile instanceof PerlFileImpl)) {
      return false;
    }

    PsiElement element = psiFile.findElementAt(sourcePosition.getOffset());

    if (element == null) {
      return false;
    }

    if (navigatable != null) {
      PerlVariableDeclarationSearcher variableProcessor = new PerlVariableDeclarationSearcher(variableName, variableType, element);
      PerlResolveUtil.treeWalkUp(element, variableProcessor);

      PerlVariableDeclarationElement result = variableProcessor.getResult();
      if (result == null) {
        return false;
      }

      navigatable.setSourcePosition(XSourcePositionImpl.createByElement(result));
    }
    else if (callback != null) {
      final Document document = psiFile.getViewProvider().getDocument();
      if (document == null) {
        return false;
      }

      final boolean[] found = new boolean[]{false};

      PerlVariableDeclarationSearcher variableProcessor = new PerlVariableDeclarationSearcher(variableName, variableType, element) {
        @Override
        public boolean execute(@NotNull PsiElement possibleElement, @NotNull ResolveState state) {
          boolean result = super.execute(possibleElement, state);

          if (!result) {
            registerElement(getResult());
          }
          else if (possibleElement instanceof PerlVariable &&
                   ((PerlVariable)possibleElement).getActualType() == variableType &&
                   StringUtil.equals(variableName, ((PerlVariable)possibleElement).getName())) {
            registerElement(possibleElement);
          }

          return result;
        }

        private void registerElement(@Nullable PsiElement targetElement) {
          if (targetElement == null) {
            return;
          }

          found[0] = true;

          try {
            if (mySourcePositionMethod != null) {
              mySourcePositionMethod.invoke(callback, XSourcePositionImpl.createByElement(targetElement));
            }
            else if (myLegacyMethod != null) {
              myLegacyMethod.invoke(callback, virtualFile, document, document.getLineNumber(targetElement.getTextOffset()));
            }
            else {
              found[0] = false;
            }
          }
          catch (InvocationTargetException e) {
            e.printStackTrace();
          }
          catch (IllegalAccessException e) {
            e.printStackTrace();
          }
        }
      };

      PerlResolveUtil.treeWalkUp(element, variableProcessor);
      return found[0];
    }
    return true;
  }


  @NotNull
  @Override
  public ThreeState computeInlineDebuggerData(@NotNull XInlineDebuggerDataCallback callback) {
    return computeMySourcePosition(null, callback) ? ThreeState.YES : ThreeState.NO;
  }
}
