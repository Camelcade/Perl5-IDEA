/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.regexp;

import org.intellij.lang.regexp.DefaultRegExpPropertiesProvider;
import org.intellij.lang.regexp.RegExpLanguageHost;
import org.intellij.lang.regexp.psi.RegExpChar;
import org.intellij.lang.regexp.psi.RegExpGroup;
import org.intellij.lang.regexp.psi.RegExpNamedGroupRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 30.11.2016.
 */
public class Perl5RegexpHost implements RegExpLanguageHost {
  private static final DefaultRegExpPropertiesProvider PROPERTIES_PROVIDER = DefaultRegExpPropertiesProvider.getInstance();

  @Override
  public boolean characterNeedsEscaping(char c) {
    return false;
  }

  @Override
  public boolean supportsPerl5EmbeddedComments() {
    return false;
  }

  @Override
  public boolean supportsPossessiveQuantifiers() {
    return false;
  }

  @Override
  public boolean supportsPythonConditionalRefs() {
    return false;
  }

  @Override
  public boolean supportsNamedGroupSyntax(RegExpGroup group) {
    return false;
  }

  @Override
  public boolean supportsNamedGroupRefSyntax(RegExpNamedGroupRef ref) {
    return false;
  }

  @Override
  public boolean supportsExtendedHexCharacter(RegExpChar regExpChar) {
    return false;
  }

  @Override
  public boolean isValidCategory(@NotNull String category) {
    return PROPERTIES_PROVIDER.isValidCategory(category);
  }

  @NotNull
  @Override
  public String[][] getAllKnownProperties() {
    // todo: http://perldoc.perl.org/perluniprops.html and /Perl5/lib/unicore/
    return PROPERTIES_PROVIDER.getAllKnownProperties();
  }

  @Nullable
  @Override
  public String getPropertyDescription(@Nullable String name) {
    return PROPERTIES_PROVIDER.getPropertyDescription(name);
  }

  @NotNull
  @Override
  public String[][] getKnownCharacterClasses() {
    return PROPERTIES_PROVIDER.getKnownCharacterClasses();
  }
}
