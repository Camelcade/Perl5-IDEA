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

package com.perl5.lang.perl.extensions.packageprocessor.impl;

import com.perl5.lang.perl.extensions.packageprocessor.IPerlPackageOptionsProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPragmaProcessorBase;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * Created by evstigneev on 19.08.2015.
 */
public class FeatureProcessor extends PerlPragmaProcessorBase implements IPerlPackageOptionsProvider {
    protected static final HashMap<String, String> OPTIONS = new HashMap<String, String>();

    static {
        OPTIONS.put("array_base", "v5.8, supports the legacy $[ variable. It is on by default but disabled under use v5.16");

        OPTIONS.put("say", "v5.10, enables the Perl 6 style say function");
        OPTIONS.put("state", "v5.10, enables state variables");
        OPTIONS.put("switch", "v5.10, enables the Perl 6 given/when construct. Requires no warnings \"experimental::smartmatch\"");

        OPTIONS.put("fc", "v5.16, enables the fc function, which implements Unicode casefolding");
        OPTIONS.put("evalbytes", "v5.16, force eval to treat string argument as a string of bytes");
        OPTIONS.put("unicode_eval", "v5.16, force eval to treat string argument as a string of characters ignoring any use utf8 declarations");
        OPTIONS.put("unicode_strings", "v5.16, use Unicode rules in all string operations executed within its scope (unless they are also within the scope of either use locale or use bytes)");
        OPTIONS.put("current_sub", "v5.16, provides the __SUB__ token that returns a reference to the current subroutine or undef outside of a subroutine");

        OPTIONS.put("lexical_subs", "v5.18, enables declaration of subroutines via my sub foo, state sub foo and our sub foo syntax. Requires no warnings \"experimental::lexical_subs\"");

        OPTIONS.put("signatures", "v5.20, enables unpacking of subroutine arguments into lexical variables. Requires  no warnings \"experimental::signatures\"");

        OPTIONS.put("postderef_qq", "v5.20, allows the use of postfix dereference syntax in within interpolated strings. Requires no warnings \"experimental::postderef\"");
        OPTIONS.put("postderef", "v5.20, allows the use of postfix dereference syntax. Requires no warnings \"experimental::postderef\"");

        OPTIONS.put("bitwise", "v5.22, makes the four standard bitwise operators treat their operands consistently as numbers, and introduces four new dotted operators that treat their operands consistently as strings. The same applies to the assignment variants. Requires no warnings \"experimental::bitwise\"");
        OPTIONS.put("refaliasing", "v5.22, enables aliasing via assignment to references. Requires  no warnings \"experimental::refaliasing\"");

        OPTIONS.put(":all", "array_base current_sub evalbytes fc lexical_subs postderef postderef_qq say signatures state switch unicode_eval unicode_strings");
        OPTIONS.put(":default", "array_base");

        OPTIONS.put(":5.9.5", "array_base say state switch");
        OPTIONS.put(":5.10", "array_base say state switch");

        OPTIONS.put(":5.11", "array_base say state switch unicode_strings");
        OPTIONS.put(":5.12", "array_base say state switch unicode_strings");
        OPTIONS.put(":5.13", "array_base say state switch unicode_strings");
        OPTIONS.put(":5.14", "array_base say state switch unicode_strings");

        OPTIONS.put(":5.15", "current_sub evalbytes fc say state switch unicode_eval unicode_strings");
        OPTIONS.put(":5.16", "current_sub evalbytes fc say state switch unicode_eval unicode_strings");
        OPTIONS.put(":5.17", "current_sub evalbytes fc say state switch unicode_eval unicode_strings");
        OPTIONS.put(":5.18", "current_sub evalbytes fc say state switch unicode_eval unicode_strings");
        OPTIONS.put(":5.19", "current_sub evalbytes fc say state switch unicode_eval unicode_strings");
        OPTIONS.put(":5.20", "current_sub evalbytes fc say state switch unicode_eval unicode_strings");
        OPTIONS.put(":5.22", "current_sub evalbytes fc say state switch unicode_eval unicode_strings");
    }

    @NotNull
    @Override
    public HashMap<String, String> getOptions() {
        return OPTIONS;
    }
}
