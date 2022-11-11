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

import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PerlLayerDescriptor {
  private static final Map<Integer, String> FLAGS_MAP = new LinkedHashMap<>();
  private static final int UTF_FLAG = 0x00008000;

  static {
    FLAGS_MAP.put(0x00000100, "EOF");
    FLAGS_MAP.put(0x00000200, "CANWRITE");
    FLAGS_MAP.put(0x00000400, "CANREAD");
    FLAGS_MAP.put(0x00000800, "ERROR");
    FLAGS_MAP.put(0x00001000, "TRUNCATE");
    FLAGS_MAP.put(0x00002000, "APPEND");
    FLAGS_MAP.put(0x00004000, "CRLF");
    FLAGS_MAP.put(UTF_FLAG, "UTF8");
    FLAGS_MAP.put(0x00010000, "UNBUF");
    FLAGS_MAP.put(0x00020000, "WRBUF");
    FLAGS_MAP.put(0x00040000, "RDBUF");
    FLAGS_MAP.put(0x00080000, "LINEBUF");
    FLAGS_MAP.put(0x00100000, "TEMP");
    FLAGS_MAP.put(0x00200000, "OPEN");
    FLAGS_MAP.put(0x00400000, "FASTGETS");
    FLAGS_MAP.put(0x00800000, "TTY");
    FLAGS_MAP.put(0x01000000, "NOTREG");
    FLAGS_MAP.put(0x02000000, "CLEARED");
  }


  @SuppressWarnings("unused") private @Nullable String name;
  @SuppressWarnings("unused") private @Nullable String param;
  @SuppressWarnings("unused") private @Nullable String flags;

  public @NotNull String getName() {
    return name == null ? "UNKNOWN" : name;
  }

  public @Nullable String getParam() {
    return param;
  }

  public @Nullable String getFlags() {
    return flags;
  }

  public @Nullable String getPresentableFlags() {
    if (flags == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    int flagValue = getIntFlag();
    sb.append("0x").append(Integer.toHexString(flagValue)).append(":");

    List<String> flagNames = new ArrayList<>();
    for (Map.Entry<Integer, String> entry : FLAGS_MAP.entrySet()) {
      if ((flagValue & entry.getKey()) != 0) {
        flagNames.add(entry.getValue());
      }
    }
    sb.append(StringUtil.join(flagNames, ","));

    return sb.toString();
  }

  private int getIntFlag() {
    if (flags == null) {
      return 0;
    }
    try {
      return Integer.parseInt(flags);
    }
    catch (NumberFormatException e) {
      return 0;
    }
  }

  public @NotNull String getPresentableName() {
    String name = ":" + getName();
    if (param != null) {
      name += "(" + param + ")";
    }
    if ((getIntFlag() & UTF_FLAG) != 0) {
      name += " :utf8";
    }
    return name;
  }

  @Override
  public String toString() {
    return "PerlLayerDescriptor{" +
           "name='" + name + '\'' +
           ", param='" + param + '\'' +
           ", flags='" + flags + '\'' +
           '}';
  }
}
