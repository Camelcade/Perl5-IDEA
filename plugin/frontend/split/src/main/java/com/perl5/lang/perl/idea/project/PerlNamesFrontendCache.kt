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

package com.perl5.lang.perl.idea.project

import java.util.concurrent.atomic.AtomicBoolean

class PerlNamesFrontendCache : PerlNamesCache {
  private val isDisposed: AtomicBoolean = AtomicBoolean(false)
  override fun getSubsNamesSet(): Set<String> = emptySet()
  override fun getNamespacesNamesSet(): Set<String> = emptySet()
  override fun dispose(): Unit = isDisposed.set(true)
  override fun forceCacheUpdate(): Unit = Unit
  override fun cleanCache(): Unit = Unit
  override fun isDisposed(): Boolean = isDisposed.get()
}