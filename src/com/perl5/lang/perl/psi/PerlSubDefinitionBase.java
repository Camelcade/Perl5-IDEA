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

package com.perl5.lang.perl.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.StubElement;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by hurricup on 11.11.2015.
 */
public interface PerlSubDefinitionBase<Stub extends StubElement>
  extends PerlSubBase<Stub>, PerlLexicalScope, PerlElementPatterns, PerlElementTypes {

  /**
   * Returns sub block, even if it's in the lazy_parsable_block
   *
   * @return sub block element; optional in constant, different extensions
   */
  PsiPerlBlock getBlockSmart();

  /**
   * Returns list of accepted arguments
   *
   * @return list of accepted arguments
   */
  @NotNull
  List<PerlSubArgument> getSubArgumentsList();

  /**
   * Returns compiled and parenthesised arguments list
   *
   * @return String with parenthesised arguments
   */
  String getSubArgumentsListAsString();

  /**
   * Returns list of arguments defined in signature
   *
   * @return list of arguments or null if there is no signature
   */
  @Nullable
  List<PerlSubArgument> getPerlSubArgumentsFromSignature();

  /**
   * Returns signature container
   *
   * @return PsiElement or null
   */
  @Nullable
  PsiElement getSignatureContainer();
}
