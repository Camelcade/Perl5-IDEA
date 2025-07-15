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

package com.intellij.lang.impl

import com.intellij.lang.PsiBuilder
import com.intellij.lang.SyntaxTreeBuilder
import com.intellij.lang.WhitespacesAndCommentsBinder
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.util.NlsContexts
import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.Nls

private val LOG = logger<PerlStartMarker>()

internal class PerlStartMarker(delegate: PsiBuilder.Marker, perlDebugBuilder: PerlDebugBuilder) :
  PsiBuilderImpl.StartMarker((delegate as PsiBuilderImpl.ProductionMarker).markerId, perlDebugBuilder), PsiBuilder.Marker {

  private val myDelegate: PsiBuilderImpl.StartMarker = delegate as PsiBuilderImpl.StartMarker
  private val myStartTokenIndex: Int = (delegate as PsiBuilderImpl.ProductionMarker).startIndex
  private val myPerlDebugBuilder = myDelegate.myBuilder as PerlDebugBuilder

  init {
    LOG.assertTrue(delegate is PsiBuilderImpl.StartMarker, "Expected start marker, got " + delegate.javaClass)
  }

  override fun clean() {
    myDelegate.clean()
  }

  override fun tokenTextMatches(chars: CharSequence): Boolean {
    return myDelegate.tokenTextMatches(chars)
  }

  override fun getEndOffset(): Int {
    return myDelegate.endOffset
  }

  override fun getEndIndex(): Int {
    return myDelegate.endIndex
  }

  override fun setLexemeIndex(lexemeIndex: Int, done: Boolean) {
    myDelegate.setLexemeIndex(lexemeIndex, done)
  }

  override fun getLexemeIndex(done: Boolean): Int {
    return myDelegate.getLexemeIndex(done)
  }

  override fun addChild(node: PsiBuilderImpl.ProductionMarker) {
    myDelegate.addChild(node)
  }

  override fun precede(): PsiBuilder.Marker {
    return myPerlDebugBuilder.createPerlMarker(myDelegate.precede())
  }

  override fun drop() {
    myDelegate.drop()
  }

  override fun rollbackTo() {
    myPerlDebugBuilder.registerRollback(rawTokenIndex() - myStartTokenIndex)
    myDelegate.rollbackTo()
  }


  private fun rawTokenIndex() = ((myDelegate as PsiBuilderImpl.ProductionMarker).myBuilder as PsiBuilderImpl).rawTokenIndex()

  override fun done(type: IElementType) {
    myDelegate.done(type)
  }

  override fun collapse(type: IElementType) {
    myDelegate.collapse(type)
  }

  override fun doneBefore(type: IElementType, before: PsiBuilder.Marker) {
    myDelegate.doneBefore(type, before)
  }

  override fun doneBefore(type: IElementType, before: PsiBuilder.Marker, errorMessage: @Nls String) {
    myDelegate.doneBefore(type, before, errorMessage)
  }

  override fun error(message: @Nls String) {
    myDelegate.error(message)
  }

  override fun errorBefore(message: @Nls String, before: PsiBuilder.Marker) {
    myDelegate.errorBefore(message, before)
  }

  override fun getTokenType(): IElementType {
    return myDelegate.tokenType
  }

  override fun remapTokenType(type: IElementType) {
    myDelegate.remapTokenType(type)
  }

  override fun setCustomEdgeTokenBinders(left: WhitespacesAndCommentsBinder?, right: WhitespacesAndCommentsBinder?) {
    myDelegate.setCustomEdgeTokenBinders(left, right)
  }

  override fun toString(): String {
    return myDelegate.toString()
  }

  override fun getStartOffset(): Int {
    return myDelegate.startOffset
  }

  override fun getStartIndex(): Int {
    return myDelegate.startIndex
  }

  override fun getTextLength(): Int {
    return myDelegate.textLength
  }

  override fun doneBefore(type: IElementType, before: SyntaxTreeBuilder.Marker) {
    myDelegate.doneBefore(type, before)
  }

  override fun doneBefore(type: IElementType,
                          before: SyntaxTreeBuilder.Marker,
                          errorMessage: @NlsContexts.ParsingError String) {
    myDelegate.doneBefore(type, before, errorMessage)
  }

  override fun errorBefore(message: @NlsContexts.ParsingError String, before: SyntaxTreeBuilder.Marker) {
    myDelegate.errorBefore(message, before)
  }
}