/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.sdk.host.os;

import com.perl5.PerlIcons;

public interface PerlOsHandlers {
  PerlOsHandler WINDOWS10 = new PerlWindows10Handler("Windows 10", PerlIcons.WINDOWS10_ICON);
  PerlOsHandler WINDOWS = new PerlWindowsHandler("Windows", PerlIcons.WINDOWS_ICON);
  PerlOsHandler MACOS = new PerlBsdHandler("MacOs", PerlIcons.MAC_ICON);
  PerlOsHandler FREEBSD = new PerlBsdHandler("FreeBsd", PerlIcons.FREEBSD_ICON);
  PerlOsHandler LINUX = new PerlLinuxHandler("Linux", PerlIcons.LINUX_ICON);
  PerlOsHandler SOLARIS = new PerlUnixHandler("Solaris", PerlIcons.SOLARIS_ICON);
  PerlOsHandler UNIX = new PerlUnixHandler("UNIX", PerlIcons.UNIX_ICON);
}
