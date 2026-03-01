/*
 * Copyright 2015-2026 Alexandr Evstigneev
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
package com.perl5.lang.perl.idea.regexp

import org.intellij.lang.regexp.DefaultRegExpPropertiesProvider.getInstance
import org.intellij.lang.regexp.RegExpLanguageHost
import org.intellij.lang.regexp.psi.RegExpChar
import org.intellij.lang.regexp.psi.RegExpElement
import org.intellij.lang.regexp.psi.RegExpGroup
import org.intellij.lang.regexp.psi.RegExpNamedGroupRef

class Perl5RegexpHost : RegExpLanguageHost {
  override fun characterNeedsEscaping(c: Char, isInClass: Boolean): Boolean = false

  override fun supportsPerl5EmbeddedComments(): Boolean = false

  override fun supportsPossessiveQuantifiers(context: RegExpElement?): Boolean = false

  override fun supportsPythonConditionalRefs(): Boolean = false

  override fun supportsNamedGroupSyntax(group: RegExpGroup?): Boolean = false

  override fun supportsNamedGroupRefSyntax(ref: RegExpNamedGroupRef?): Boolean = false

  override fun supportsExtendedHexCharacter(regExpChar: RegExpChar?): Boolean = false

  override fun isValidCategory(category: String): Boolean = getInstance().isValidCategory(category)

  // todo: http://perldoc.perl.org/perluniprops.html and /Perl5/lib/unicore/
  override fun getAllKnownProperties(): Array<Array<String>> = getInstance().allKnownProperties

  override fun getPropertyDescription(name: String?): String? = getInstance().getPropertyDescription(name)

  override fun getKnownCharacterClasses(): Array<Array<String>> = getInstance().knownCharacterClasses
}
