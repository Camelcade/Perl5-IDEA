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

package com.perl5.lang.perl.idea.stubs;

import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.io.StringRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hurricup on 16.07.2015.
 */
public class PerlStubSerializationUtil {
  @Nullable
  public static String readNullableString(@NotNull StubInputStream dataStream) throws IOException {
    StringRef stringRef = dataStream.readName();
    return stringRef == null ? null : stringRef.getString();
  }


  public static void writeStringsList(@NotNull StubOutputStream dataStream, List<String> stringList) throws IOException {
    if (stringList == null) {
      dataStream.writeInt(-1);
    }
    else {
      dataStream.writeInt(stringList.size());
      for (String stringItem : stringList) {
        dataStream.writeName(stringItem);
      }
    }
  }

  public static List<String> readStringsList(@NotNull StubInputStream dataStream) throws IOException {
    int listSize = dataStream.readInt();

    if (listSize == -1) {
      return null;
    }

    ArrayList<String> result = new ArrayList<String>(listSize);
    for (int i = 0; i < listSize; i++) {
      result.add(dataStream.readName().toString());
    }
    return result;
  }

  public static void writeStringListMap(@NotNull StubOutputStream dataStream, Map<String, List<String>> stringListMap) throws IOException {
    dataStream.writeInt(stringListMap.size());
    for (String key : stringListMap.keySet()) {
      dataStream.writeName(key);
      writeStringsList(dataStream, stringListMap.get(key));
    }
  }

  public static Map<String, List<String>> readStringListMap(@NotNull StubInputStream dataStream) throws IOException {
    int mapSize = dataStream.readInt();
    Map<String, List<String>> stringListMap = new HashMap<String, List<String>>(mapSize);
    for (int i = 0; i < mapSize; i++) {
      String key = dataStream.readName().toString();
      stringListMap.put(key, readStringsList(dataStream));
    }
    return stringListMap;
  }
}
