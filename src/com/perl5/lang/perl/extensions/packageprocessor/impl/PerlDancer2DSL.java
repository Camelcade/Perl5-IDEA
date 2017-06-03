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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hurricup on 02.06.2016.
 */
public interface PerlDancer2DSL {
  List<String> DSL_KEYWORDS = new ArrayList<String>(Arrays.asList(
    "any",
    "app",
    "body_parameters",
    "captures",
    "config",
    "content",
    "content_type",
    "context",
    "cookie",
    "cookies",
    "dance",
    "dancer_app",
    "dancer_major_version",
    "dancer_version",
    "debug",
    "del",
    "delayed",
    "dirname",
    "done",
    "dsl",
    "engine",
    "error",
    "false",
    "flush",
    "forward",
    "from_dumper",
    "from_json",
    "from_yaml",
    "get",
    "halt",
    "header",
    "headers",
    "hook",
    "import",
    "info",
    "log",
    "mime",
    "options",
    "param",
    "params",
    "pass",
    "patch",
    "path",
    "post",
    "prefix",
    "psgi_app",
    "push_header",
    "push_response_header",
    "put",
    "query_parameters",
    "redirect",
    "request",
    "request_header",
    "response",
    "response_header",
    "response_headers",
    "route_parameters",
    "runner",
    "send_as",
    "send_error",
    "send_file",
    "session",
    "set",
    "setting",
    "splat",
    "start",
    "status",
    "template",
    "to_app",
    "to_dumper",
    "to_json",
    "to_yaml",
    "true",
    "upload",
    "uri_for",
    "var",
    "vars",
    "warning"
  ));
}
