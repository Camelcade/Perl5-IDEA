/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.run.debugger.protocol;

/**
 * Created by hurricup on 05.05.2016.
 */
public class PerlStackFrameDescriptor {
  public transient static final String EVAL_PREFIX = "(eval ";

  PerlLoadedFileDescriptor file;
  private int line;
  private int main_size;
  private PerlValueDescriptor[] lexicals;
  private PerlValueDescriptor[] globals;
  private PerlValueDescriptor[] args;

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
}
