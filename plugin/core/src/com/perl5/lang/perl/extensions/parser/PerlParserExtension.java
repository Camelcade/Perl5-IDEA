/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.perl.extensions.parser;

import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.parser.builder.PerlBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;


public abstract class PerlParserExtension {
  public static final ExtensionPointName<PerlParserExtension> EP_NAME = ExtensionPointName.create("com.perl5.parserExtension");

  /**
   * @return a set of keywords and element types to lex that can be anywhere
   */
  @NotNull
  public Map<String, IElementType> getCustomTokensMap() {
    return Collections.emptyMap();
  }

  /**
   * @return a map of keywords and token types for identifiers after dereference, e.g. {@code $self->mk_accessors}.
   */
  @NotNull
  public Map<String, IElementType> getCustomTokensAfterDereferenceMap() {
    return Collections.emptyMap();
  }

  /**
   * Returns list of extendable tokensets. Loader will attempt to add them into builder
   * Should return list of pairs: token to extend - TokenSet of extended tokens
   * Reqired to avoid extra TERM expressions in PSI tree
   *
   * @return list of pairs to extend
   */
  @Nullable
  public List<Pair<IElementType, TokenSet>> getExtensionSets() {
    return null;
  }

  /**
   * Returns tokenset, containing bare regex prefixesj like =~ or case
   *
   * @return list of pairs to extend
   */
  @Nullable
  public TokenSet getRegexPrefixTokenSet() {
    return null;
  }

  /**
   * Parse method. Attempt to parse term
   * You may re-use PerlParser static methods to implement native perl expressions
   *
   * @param b PerlBuilder
   * @param l parsing level
   * @return parsing result
   */
  public boolean parseTerm(PerlBuilder b, int l) {
    return false;
  }

  /**
   * Parses element in dereference sequence.
   *
   * @param b PerlBuilder
   * @param l parsing level
   * @return Parsing result
   */
  public boolean parseNestedElement(PerlBuilder b, int l) {
    return false;
  }

  /**
   * Callback to add highlighting tokens
   */
  public void addHighlighting() {
  }
}
