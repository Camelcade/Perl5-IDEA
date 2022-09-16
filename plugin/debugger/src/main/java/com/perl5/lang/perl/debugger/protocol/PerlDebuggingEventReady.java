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

package com.perl5.lang.perl.debugger.protocol;

import com.intellij.openapi.util.text.StringUtil;
import com.perl5.PerlBundle;


public class PerlDebuggingEventReady extends PerlDebuggingEventBase {
  public static final String MODULE_VERSION_PREFIX = PerlBundle.message("perl.debugger.version.prefix");
  private static final String MODULE_VERSION_PREFIX_V = "v" + MODULE_VERSION_PREFIX;
  public String version;

  @Override
  public void run() {
  }

  public boolean isValid() {
    return StringUtil.isNotEmpty(version) &&
           (StringUtil.startsWith(version, MODULE_VERSION_PREFIX_V) || StringUtil.startsWith(version, MODULE_VERSION_PREFIX));
  }
}
