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

package com.perl5.lang.perl.debugger.protocol;


import com.google.gson.annotations.Expose;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.Arrays;

public class PerlStackFrameDescriptor {
  public static final String EVAL_PREFIX = "(eval ";

  @Expose @NotNull PerlLoadedFileDescriptor file;
  @Expose private int line;
  @Expose private int main_size;
  @Expose private PerlValueDescriptor[] lexicals;
  @Expose private PerlValueDescriptor[] globals;
  @Expose private PerlValueDescriptor[] args;

  public PerlStackFrameDescriptor() {
  }

  @VisibleForTesting
  public PerlStackFrameDescriptor(@NotNull PerlLoadedFileDescriptor file, int line) {
    this.file = file;
    this.line = line;
  }

  public PerlLoadedFileDescriptor getFileDescriptor() {
    return file;
  }

  public int getZeroBasedLine() {
    return line;
  }

  public int getOneBasedLine() {
    return line + 1;
  }

  public PerlValueDescriptor[] getLexicals() {
    return lexicals;
  }

  public PerlValueDescriptor[] getGlobals() {
    return globals;
  }

  public int getMainSize() {
    return main_size;
  }

  public PerlValueDescriptor[] getArgs() {
    return args;
  }

  @Override
  public String toString() {
    return "PerlStackFrameDescriptor{" +
           "file=" + file +
           ", line=" + line +
           ", main_size=" + main_size +
           ", lexicals=" + Arrays.toString(lexicals) +
           ", globals=" + Arrays.toString(globals) +
           ", args=" + Arrays.toString(args) +
           '}';
  }
}
