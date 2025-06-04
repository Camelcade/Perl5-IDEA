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
package com.perl5.lang.perl.profiler

import com.intellij.DynamicBundle
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.PropertyKey

const val PATH_TO_BUNDLE: String = "messages.PerlProfilerBundle"

object PerlProfilerBundle : DynamicBundle(PATH_TO_BUNDLE) {
  @JvmStatic
  fun message(key: @PropertyKey(resourceBundle = PATH_TO_BUNDLE) String, vararg params: Any): @Nls String = getMessage(key, *params)
}
