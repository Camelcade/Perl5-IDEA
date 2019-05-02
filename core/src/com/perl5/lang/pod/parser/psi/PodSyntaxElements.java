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

package com.perl5.lang.pod.parser.psi;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by hurricup on 26.03.2016.
 */
public class PodSyntaxElements {
  public static final String FORMAT_ROFF = "roff";
  public static final String FORMAT_MAN = "man";
  public static final String FORMAT_LATEX = "latex";
  public static final String FORMAT_TEX = "tex";
  public static final String FORMAT_TEXT = "text";
  public static final String FORMAT_HTML = "html";
  public static final List<String> KNOWN_FORMATTERS = Collections.unmodifiableList(
    Arrays.asList(FORMAT_HTML, FORMAT_MAN, FORMAT_LATEX, FORMAT_ROFF, FORMAT_TEX, FORMAT_TEXT));

  public static final String CUT_COMMAND = "=cut";

  private PodSyntaxElements() {
  }
}
