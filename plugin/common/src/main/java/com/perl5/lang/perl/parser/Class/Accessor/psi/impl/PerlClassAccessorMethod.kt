/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.perl.parser.Class.Accessor.psi.impl

import com.intellij.openapi.util.NotNullFactory
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.util.Function
import com.perl5.lang.perl.extensions.PerlRenameUsagesHelper
import com.perl5.lang.perl.psi.impl.PerlSubCallElement
import com.perl5.lang.perl.psi.light.PerlLightMethodDefinitionElement
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations
import com.perl5.lang.perl.psi.utils.PerlSubArgument
import org.jetbrains.annotations.NonNls

private const val GETTER_PREFIX: @NonNls String = "get_"
private const val SETTER_PREFIX: @NonNls String = "set_"

class PerlClassAccessorMethod : PerlLightMethodDefinitionElement<PerlSubCallElement?>, PerlRenameUsagesHelper {
  constructor(
    delegate: PerlSubCallElement,
    baseName: String,
    nameComputation: Function<String, String>,
    elementType: IElementType,
    nameIdentifier: PsiElement,
    packageName: String?,
    annotations: PerlSubAnnotations?
  ) : super(
    delegate,
    nameComputation.`fun`(baseName),
    elementType,
    nameIdentifier,
    packageName,
    emptyList(),
    annotations
  ) {
    setNameComputation(nameComputation)
    setSubArgumentsProvider(NotNullFactory {
      if (this.isFollowBestPractice) {
        if (hasSetterName()) {
          return@NotNullFactory listOf<PerlSubArgument>(
            PerlSubArgument.self(),
            PerlSubArgument.mandatoryScalar(PerlSubArgument.NEW_VALUE_VALUE)
          )
        }
      }
      else {
        return@NotNullFactory listOf<PerlSubArgument?>(
          PerlSubArgument.self(),
          PerlSubArgument.optionalScalar(PerlSubArgument.NEW_VALUE_VALUE)
        )
      }
      emptyList()
    })
  }

  constructor(
    delegate: PerlSubCallElement,
    stub: PerlSubDefinitionStub
  ) : super(delegate, stub) {
    nameComputation = when {
      hasGetterName() -> GETTER_COMPUTATION
      hasSetterName() -> SETTER_COMPUTATION
      else -> SIMPLE_COMPUTATION
    }
  }

  private val isFollowBestPractice: Boolean
    get() = PerlClassAccessorHandler.isFollowBestPractice(delegate)

  private fun hasGetterName(): Boolean = this.isFollowBestPractice && subName.startsWith(GETTER_PREFIX)

  private fun hasSetterName(): Boolean = this.isFollowBestPractice && subName.startsWith(SETTER_PREFIX)

  override fun getName(): String = this.baseName

  private val baseName: String
    get() {
      val name = subName
      return if (this.isFollowBestPractice) if (hasGetterName()) name.substring(GETTER_PREFIX.length)
      else name.substring(
        SETTER_PREFIX.length
      )
      else name
    }

  private val getterName: String?
    get() = if (this.isFollowBestPractice && hasSetterName()) GETTER_PREFIX + this.baseName else subName

  private val setterName: String?
    get() = if (this.isFollowBestPractice && hasGetterName()) SETTER_PREFIX + this.baseName else subName

  val pairedMethod: PerlClassAccessorMethod?
    get() {
      if (!this.isFollowBestPractice) {
        return null
      }
      val baseName = this.baseName
      for (element in delegate.lightElements) {
        if (element is PerlClassAccessorMethod &&
          baseName == element.baseName && (element != this)
        ) {
          return element
        }
      }
      return null
    }

  override fun getSubstitutedUsageName(newName: String, element: PsiElement): String {
    if (this.isFollowBestPractice) {
      val currentValue = element.text
      if (this.getterName == currentValue) {
        return GETTER_PREFIX + newName
      }
      else if (this.setterName == currentValue) {
        return SETTER_PREFIX + newName
      }
    }
    return newName
  }

  override fun isInplaceRefactoringAllowed(): Boolean = !this.isFollowBestPractice

  companion object {
    @JvmField
    val SIMPLE_COMPUTATION: Function<String, String> = Function { name: String -> name }

    @JvmField
    val GETTER_COMPUTATION: Function<String, String> = Function { name: String -> GETTER_PREFIX + name }

    @JvmField
    val SETTER_COMPUTATION: Function<String, String> = Function { name: String -> SETTER_PREFIX + name }
  }
}