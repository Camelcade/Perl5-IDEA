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

package com.perl5.lang.perl.internals;

import java.util.regex.Pattern;

/**
 * regexps taken from {@code version/regex.pm} original names are kept, but in camelcase
 */
public final class PerlVersionRegexps {
  private PerlVersionRegexps() {
  }

  static final Pattern NUMERIC_VERSION = Pattern.compile(
    "(0|[1-9]\\d*)" +        // revision
    "(?:\\." +
    "([\\d_]+)" +            // major
    ")?"
  );

  static final Pattern DOTTED_VERSION = Pattern.compile(
    "v(?:0|[1-9]\\d*)" + // revision
    "(?:\\.\\d+)*+" +    // major, minor and others
    "(_\\d+)?"           // alpha
  );

  static final String FRACTION_PART = "(?:\\.(\\d++))";

  static final String STRICT_INTEGER_PART = "(0|[1-9]\\d*+)";
  static final String STRICT_DOTTED_DECIMAL_PART = "(?:\\.(\\d{1,3}+))";

  static final String LAX_INTEGER_PART = "(\\d++)";
  static final String LAX_DOTTED_DECIMAL_PART = "(?:\\.(\\d++))";
  static final String LAX_ALPHA_PART = "(?:_(\\d++))";

  // strict versions
  static final Pattern STRICT_DECIMAL_VERSION_PATTERN = Pattern.compile(
    STRICT_INTEGER_PART + FRACTION_PART + "?"
  );

  static final Pattern STRICT_DOTTED_DECIMAL_VERSION_PATTERN = Pattern.compile(
    "v" + STRICT_INTEGER_PART + STRICT_DOTTED_DECIMAL_PART + STRICT_DOTTED_DECIMAL_PART + "++"
  );

  static final Pattern STRICT_VERSION_PATTERN = Pattern.compile(
    STRICT_DECIMAL_VERSION_PATTERN + "|" + STRICT_DOTTED_DECIMAL_VERSION_PATTERN
  );

  // lax versions
  static final Pattern LAX_DECIMAL_VERSION_PATTERN = Pattern.compile(
    LAX_INTEGER_PART + "(?:\\.|" + FRACTION_PART + LAX_ALPHA_PART + "?)?"
    + "|"
    + FRACTION_PART + LAX_ALPHA_PART + "?"
  );

  static final Pattern LAX_DOTTED_DECIMAL_VERSION_PATTERN = Pattern.compile(
    "v" + LAX_INTEGER_PART + "(?:" + LAX_DOTTED_DECIMAL_PART + LAX_ALPHA_PART + "?)?"
    + "|"
    + LAX_INTEGER_PART + "?" + LAX_DOTTED_DECIMAL_PART + LAX_DOTTED_DECIMAL_PART + "++" + LAX_ALPHA_PART + "?"
  );

  static final Pattern LAX_VERSION_PATTERN = Pattern.compile(
    LAX_DECIMAL_VERSION_PATTERN + "|" + LAX_DOTTED_DECIMAL_VERSION_PATTERN
  );
}
