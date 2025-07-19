/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.configuration.settings;

import java.util.Collections;
import java.util.List;

public final class PerlStrictWarningsDefaults {
  private PerlStrictWarningsDefaults() {
  }

  public static final List<String> DEFAULT_STRICT_AND_WARNINGS_PROVIDERS = List.of(
    "Dancer",
    "Dancer2",
    "Modern::Perl",
    "Mojo::Base",
    "Mojolicious::Lite",
    "Moo",
    "Moo::Role",
    "Moose",
    "Moose::Role",
    "Moose::Util::TypeConstraints",
    "MooseX::ClassAttribute",
    "MooseX::MethodAttributes",
    "MooseX::Types::CheckedUtilExports",
    "Role::Tiny",
    "Test2::Bundle::Extended",
    "Test2::Bundle::More",
    "Test2::Bundle::Simple",
    "Test2::V0",
    "strictures"
  );

  public static final List<String> DEFAULT_STRICT_ONLY_PROVIDERS = Collections.singletonList("strict");

  public static final List<String> DEFAULT_WARNINGS_ONLY_PROVIDERS = Collections.singletonList("warnings");
}
