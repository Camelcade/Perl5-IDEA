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

package com.perl5.lang.tt2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hurricup on 13.06.2016.
 */
public interface TemplateToolkitFilterNames {
  List<String> FILTER_NAMES = new ArrayList<String>(Arrays.asList(
    "format(format)",
    "upper",
    "lower",
    "ucfirst",
    "lcfirst",
    "trim",
    "collapse",
    "html",
    "html_entity",
    "xml",
    "html_para",
    "html_break",
    "html_para_break",
    "html_line_break",
    "uri",
    "url",
    "indent(pad)",
    "truncate(length,dots)",
    "repeat(iterations)",
    "remove(string)",
    "replace(search, replace)",
    "redirect(file, options)",
    "eval",
    "evaltt",
    "perl",
    "evalperl",
    "stdout(options)",
    "stderr",
    "null"
  ));
}
