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

package com.perl5.lang.perl.idea.execution;

import org.jetbrains.annotations.NotNull;

public class PortMapping {
  private final int remote;
  private final int local;

  private PortMapping(int remote, int local) {
    this.remote = remote;
    this.local = local;
  }

  public int getRemote() {
    return remote;
  }

  public int getLocal() {
    return local;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PortMapping)) {
      return false;
    }

    PortMapping mapping = (PortMapping)o;

    if (remote != mapping.remote) {
      return false;
    }
    return local == mapping.local;
  }

  @Override
  public int hashCode() {
    int result = remote;
    result = 31 * result + local;
    return result;
  }

  @NotNull
  public static PortMapping create(int port) {
    return new PortMapping(port, port);
  }
}
