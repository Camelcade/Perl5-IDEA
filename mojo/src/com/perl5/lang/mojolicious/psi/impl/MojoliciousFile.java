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

package com.perl5.lang.mojolicious.psi.impl;

import com.perl5.lang.perl.extensions.PerlImplicitVariablesProvider;
import com.perl5.lang.perl.psi.PerlFile;
import com.perl5.lang.perl.psi.PerlSelfHinter;

public interface MojoliciousFile extends PerlFile, PerlImplicitVariablesProvider, PerlSelfHinter {
  String MOJO_CONTROLLER_NS = "Mojolicious::Controller";
  String MOJO_SANDBOX_NS_PREFIX = "Mojo::Template::Sandbox";
  String MOJO_TAG_HELPERS_NS = "Mojolicious::Plugin::TagHelpers";
  String MOJO_DEFAULT_HELPERS_NS = "Mojolicious::Plugin::DefaultHelpers";
}
