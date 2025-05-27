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

package com.perl5.lang.pod.parser.psi;

import java.util.List;


public class PodSyntaxElements {
  public static final String FORMAT_ROFF = "roff";
  public static final String FORMAT_MAN = "man";
  public static final String FORMAT_LATEX = "latex";
  public static final String FORMAT_TEX = "tex";
  public static final String FORMAT_TEXT = "text";
  public static final String FORMAT_HTML = "html";
  public static final List<String> KNOWN_FORMATTERS = List.of(FORMAT_HTML, FORMAT_MAN, FORMAT_LATEX, FORMAT_ROFF, FORMAT_TEX, FORMAT_TEXT);

  public static final String CUT_COMMAND = "=cut";
  public static final String CUT_COMMAND_WITH_LEADING_NEWLINE = "\n" + CUT_COMMAND;

  private PodSyntaxElements() {
  }
}
