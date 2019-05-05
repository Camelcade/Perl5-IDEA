/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public interface PerlDancerDSL {
  List<String> DSL_KEYWORDS = new ArrayList<>(Arrays.asList(
    "after",
    "any",
    "before",
    "before_template",
    "captures",
    "config",
    "content_type",
    "cookie",
    "cookies",
    "dance",
    "dancer_version",
    "debug",
    "del",
    "dirname",
    "engine",
    "error",
    "false",
    "forward",
    "from_dumper",
    "from_json",
    "from_xml",
    "from_yaml",
    "get",
    "halt",
    "header",
    "headers",
    "hook",
    "info",
    "layout",
    "load",
    "load_app",
    "logger",
    "mime",
    "options",
    "param",
    "param_array",
    "params",
    "pass",
    "patch",
    "path",
    "post",
    "prefix",
    "push_header",
    "put",
    "redirect",
    "render_with_layout",
    "request",
    "send_error",
    "send_file",
    "session",
    "set",
    "set_cookie",
    "setting",
    "splat",
    "start",
    "status",
    "template",
    "to_dumper",
    "to_json",
    "to_xml",
    "to_yaml",
    "true",
    "upload",
    "uri_for",
    "var",
    "vars",
    "warning"
  ));
}
