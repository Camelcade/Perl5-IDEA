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

import com.intellij.execution.ExecutionException;

/**
 * Created by hurricup on 18.05.2016.
 */
public interface PerlDebugOptions {
  String ROLE_SERVER = "server";
  String ROLE_CLIENT = "client";

  String getDebugHost();

  int getDebugPort() throws ExecutionException;

  String getRemoteProjectRoot();

  String getStartMode();

  void setStartMode(String startMode);

  String getScriptCharset();

  void setScriptCharset(String charset);

  String getPerlRole();

  boolean isNonInteractiveModeEnabled();

  void setNonInteractiveModeEnabled(boolean isEnabled);

  boolean isCompileTimeBreakpointsEnabled();

  void setCompileTimeBreakpointsEnabled(boolean isEnabled);

  String getInitCode();

  void setInitCode(String code);
}
