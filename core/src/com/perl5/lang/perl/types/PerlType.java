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

package com.perl5.lang.perl.types;

import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.Nullable;

public abstract class PerlType {
  public static final String ARRAY_REF = "ArrayRef";
  public static final String HASH_REF = "HashRef";

  public static final String ARRAY = "Array";
  public static final String HASH = "Hash";

  @Nullable
  public abstract String getNamespaceName();

  @Nullable
  public static PerlType fromTypeString(@Nullable String typeString) {
    if (StringUtil.isEmpty(typeString)) {
      return null;
    }
    // fixme dirty implementation
    String arrayRefStart = ARRAY_REF + "[";
    String arrayRefEnd = "]";
    if( typeString.startsWith(arrayRefStart) && typeString.endsWith(arrayRefEnd) ){
      // fixme handle deep nested inner type
      String innerType = typeString
        .substring(arrayRefStart.length(), typeString.length() - arrayRefEnd.length());
      return PerlTypeArrayRef.fromInnerType(PerlTypeNamespace.fromNamespace(innerType));
    }else{
      return PerlTypeNamespace.fromNamespace(typeString);
    }
  }
}
