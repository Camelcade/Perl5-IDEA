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

package com.perl5.lang.perl.psi.utils;

import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 01.06.2015.
 */
public enum PerlVariableType {
  SCALAR,
  ARRAY,
  HASH,
  GLOB,
  CODE;

  public char getSigil() {
    if (this == SCALAR) {
      return '$';
    }
    else if (this == ARRAY) {
      return '@';
    }
    else if (this == HASH) {
      return '%';
    }
    else if (this == GLOB) {
      return '*';
    }
    else if (this == CODE) {
      return '&';
    }
    return ' ';
  }

  @Nullable
  public static PerlVariableType bySigil(char sigil) {
    if (sigil == '$') {
      return SCALAR;
    }
    else if (sigil == '@') {
      return ARRAY;
    }
    else if (sigil == '%') {
      return HASH;
    }
    else if (sigil == '*') {
      return GLOB;
    }
    else if (sigil == '&') {
      return CODE;
    }

    return null;
  }

};
