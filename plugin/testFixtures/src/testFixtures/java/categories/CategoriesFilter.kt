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

package categories

import org.junit.experimental.categories.Categories
import org.junit.runner.Description

object CategoriesFilter {
  @JvmStatic
  fun shouldRun(klass: Class<*>): Boolean {
    val includeProp = System.getProperty("junit.include.categories")
    val excludeProp = System.getProperty("junit.exclude.categories")

    val include = parseCategoryList(includeProp)
    val exclude = parseCategoryList(excludeProp)

    val filter: Categories.CategoryFilter = Categories.CategoryFilter.categoryFilter(true, include, true, exclude)
    val desc: Description = Description.createSuiteDescription(klass)
    return filter.shouldRun(desc)
  }

  private fun parseCategoryList(prop: String?): Set<Class<*>> = prop?.trim()?.split(",")
    ?.map { className -> Class.forName(className.trim()) }
    ?.toSet() ?: emptySet()
}