/*
 * Copyright 2015 Alexandr Evstigneev
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

package com.perl5.lang.perl.parser;

import com.perl5.lang.perl.PerlTokenType;

/**
 * Created by hurricup on 25.11.2015.
 */
public interface PerlMooseParserExtension
{
	PerlTokenType RESERVED_INNER = new PerlTokenType("inner");
	PerlTokenType RESERVED_WITH = new PerlTokenType("with");
	PerlTokenType RESERVED_EXTENDS = new PerlTokenType("extends");
	PerlTokenType RESERVED_META = new PerlTokenType("meta");
	PerlTokenType RESERVED_OVERRIDE = new PerlTokenType("override");
	PerlTokenType RESERVED_AROUND = new PerlTokenType("around");
	PerlTokenType RESERVED_SUPER = new PerlTokenType("super");
	PerlTokenType RESERVED_AUGMENT = new PerlTokenType("augment");
	PerlTokenType RESERVED_AFTER = new PerlTokenType("after");
	PerlTokenType RESERVED_BEFORE = new PerlTokenType("before");
	PerlTokenType RESERVED_HAS = new PerlTokenType("has");

}
