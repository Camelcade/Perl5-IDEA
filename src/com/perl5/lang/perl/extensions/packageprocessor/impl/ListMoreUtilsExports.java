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

package com.perl5.lang.perl.extensions.packageprocessor.impl;

import java.util.Arrays;
import java.util.List;

public interface ListMoreUtilsExports {
  List<String> EXPORT_OK = Arrays.asList(
    "after",
    "after_incl",
    "all",
    "all_u",
    "any",
    "any_u",
    "apply",
    "before",
    "before_incl",
    "bsearch",
    "bsearch_index",
    "bsearchidx",
    "distinct",
    "each_array",
    "each_arrayref",
    "false",
    "first_index",
    "first_result",
    "first_value",
    "firstidx",
    "firstres",
    "firstval",
    "indexes",
    "insert_after",
    "insert_after_string",
    "last_index",
    "last_result",
    "last_value",
    "lastidx",
    "lastres",
    "lastval",
    "mesh",
    "minmax",
    "natatime",
    "none",
    "none_u",
    "notall",
    "notall_u",
    "nsort_by",
    "one",
    "one_u",
    "only_index",
    "only_result",
    "only_value",
    "onlyidx",
    "onlyres",
    "onlyval",
    "pairwise",
    "part",
    "singleton",
    "sort_by",
    "true",
    "uniq",
    "zip"
  );
}
