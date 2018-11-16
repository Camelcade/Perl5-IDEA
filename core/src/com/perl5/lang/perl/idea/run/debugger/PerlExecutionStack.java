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

import com.intellij.util.containers.ContainerUtil;
import com.intellij.xdebugger.frame.XExecutionStack;
import com.intellij.xdebugger.frame.XStackFrame;
import com.perl5.lang.perl.idea.run.debugger.protocol.PerlStackFrameDescriptor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 04.05.2016.
 */
public class PerlExecutionStack extends XExecutionStack {
  private final PerlSuspendContext mySuspendContext;
  List<PerlStackFrame> myPerlStackFrames = new ArrayList<>();

  public PerlExecutionStack(PerlStackFrameDescriptor[] frames, PerlSuspendContext suspendContext) {
    super("");
    mySuspendContext = suspendContext;
    for (PerlStackFrameDescriptor stackFrameDescriptor : frames) {
      myPerlStackFrames.add(new PerlStackFrame(stackFrameDescriptor, this));
    }
  }

  @Nullable
  @Override
  public XStackFrame getTopFrame() {
    return ContainerUtil.getFirstItem(myPerlStackFrames);
  }

  @Override
  public void computeStackFrames(int firstFrameIndex, XStackFrameContainer container) {
    container.addStackFrames(myPerlStackFrames, true);
  }

  public PerlSuspendContext getSuspendContext() {
    return mySuspendContext;
  }
}
