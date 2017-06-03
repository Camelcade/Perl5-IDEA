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

package com.perl5.lang.perl.idea.codeInsight;

import com.intellij.lang.parameterInfo.ParameterInfoUIContext;
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by hurricup on 27.06.2016.
 */
public class PerlParameterInfo {
  private final PerlSubArgument myArgument;
  private boolean myIsSelected;

  public PerlParameterInfo(@NotNull PerlSubArgument argument) {
    this(argument, false);
  }

  public PerlParameterInfo(@NotNull PerlSubArgument argument, boolean isSelected) {
    myArgument = argument;
    myIsSelected = isSelected;
  }

  @NotNull
  public PerlSubArgument getArgument() {
    return myArgument;
  }

  public boolean isSelected() {
    return myIsSelected;
  }

  public void setSelected(boolean selected) {
    myIsSelected = selected;
  }

  @Override
  public String toString() {
    return myArgument.toStringLong();
  }

  public void setUpUIPresentation(@NotNull ParameterInfoUIContext context) {
    String text = toString();
    context.setupUIComponentPresentation(
      text,
      myIsSelected ? 0 : -1,
      myIsSelected ? text.length() : 0,
      false,
      false,
      false,
      context.getDefaultParameterColor()
    );
  }

  public static PerlParameterInfo[] wrapArguments(List<PerlSubArgument> arguments) {
    List<PerlParameterInfo> parameterInfos = ContainerUtil.map(arguments, new Function<PerlSubArgument, PerlParameterInfo>() {
      @Override
      public PerlParameterInfo fun(PerlSubArgument perlSubArgument) {
        return new PerlParameterInfo(perlSubArgument);
      }
    });
    return parameterInfos.toArray(new PerlParameterInfo[parameterInfos.size()]);
  }
}
