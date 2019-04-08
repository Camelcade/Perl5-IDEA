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

package com.perl5.lang.perl.psi.stubs;

import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.io.StringRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;

/**
 * Created by hurricup on 16.07.2015.
 */
public class PerlStubSerializationUtil {
  @NotNull
  public static String readNotNullString(@NotNull StubInputStream dataStream) throws IOException {
    return Objects.requireNonNull(readString(dataStream));
  }

  @Nullable
  public static String readString(@NotNull StubInputStream dataStream) throws IOException {
    return StringRef.toString(dataStream.readName());
  }


  public static void writeStringsList(@NotNull StubOutputStream dataStream, List<String> stringList) throws IOException {
    if (stringList == null) {
      dataStream.writeVarInt(0);
    }
    else {
      dataStream.writeVarInt(stringList.size() + 1);
      for (String stringItem : stringList) {
        dataStream.writeName(stringItem);
      }
    }
  }

  @Nullable
  public static List<String> readStringsList(@NotNull StubInputStream dataStream) throws IOException {
    int listSize = dataStream.readVarInt();

    if (listSize == 0) {
      return null;
    }

    ArrayList<String> result = new ArrayList<>(listSize);
    for (int i = 0; i < listSize - 1; i++) {
      result.add(readString(dataStream));
    }
    return result;
  }

  public static void writeStringListMap(@NotNull StubOutputStream dataStream, Map<String, List<String>> stringListMap) throws IOException {
    dataStream.writeVarInt(stringListMap.size());
    for (String key : stringListMap.keySet()) {
      dataStream.writeName(key);
      writeStringsList(dataStream, stringListMap.get(key));
    }
  }

  public static Map<String, List<String>> readStringListMap(@NotNull StubInputStream dataStream) throws IOException {
    int mapSize = dataStream.readVarInt();
    Map<String, List<String>> stringListMap = new HashMap<>(mapSize);
    for (int i = 0; i < mapSize; i++) {
      stringListMap.put(readString(dataStream), readStringsList(dataStream));
    }
    return stringListMap;
  }
}
