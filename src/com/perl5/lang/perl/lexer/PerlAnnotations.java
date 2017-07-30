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

package com.perl5.lang.perl.lexer;

import com.intellij.psi.tree.IElementType;
import gnu.trove.THashMap;

import java.util.Map;

/**
 * Created by hurricup on 03.06.2015.
 */
public class PerlAnnotations implements PerlElementTypes {
  public static final Map<String, IElementType> TOKENS_MAP = new THashMap<>();

  static {
    TOKENS_MAP.put("deprecated", ANNOTATION_DEPRECATED_KEY);
    TOKENS_MAP.put("returns", ANNOTATION_RETURNS_KEY);
    TOKENS_MAP.put("type", ANNOTATION_TYPE_KEY);
    TOKENS_MAP.put("method", ANNOTATION_METHOD_KEY);
    TOKENS_MAP.put("inject", ANNOTATION_INJECT_KEY);

    // these are parsed but not used
    TOKENS_MAP.put("override", ANNOTATION_OVERRIDE_KEY);
    TOKENS_MAP.put("abstract", ANNOTATION_ABSTRACT_KEY);
    TOKENS_MAP.put("noinspection", ANNOTATION_NOINSPECTION_KEY);
  }
}
