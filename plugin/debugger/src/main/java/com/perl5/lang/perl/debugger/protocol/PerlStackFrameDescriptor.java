/*
 * Copyright 2015-2022 Alexandr Evstigneev
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


import java.util.Arrays;

public class PerlStackFrameDescriptor {
  public static final transient String EVAL_PREFIX = "(eval ";

  @SuppressWarnings("unused") PerlLoadedFileDescriptor file;
  @SuppressWarnings("unused") private int line;
  @SuppressWarnings("unused") private int main_size;
  @SuppressWarnings("unused") private PerlValueDescriptor[] lexicals;
  @SuppressWarnings("unused") private PerlValueDescriptor[] globals;
  @SuppressWarnings("unused") private PerlValueDescriptor[] args;

  public PerlLoadedFileDescriptor getFileDescriptor() {
    return file;
  }

  public int getLine() {
    return line;
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
