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
import com.perl5.lang.perl.extensions.packageprocessor.IPerlWarningsProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPragmaProcessorBase;
import com.perl5.lang.perl.internals.PerlWarningsMask;
import com.perl5.lang.perl.psi.PerlUseStatement;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * Created by hurricup on 18.08.2015.
 */
public class WarningsProcessor extends PerlPragmaProcessorBase implements IPerlPackageOptionsProvider, IPerlWarningsProvider
{
	protected static final HashMap<String, String> OPTIONS = new HashMap<String, String>();

	static
	{
		OPTIONS.put("FATAL", "v5.8");

		OPTIONS.put("all", "v5.8");
		OPTIONS.put("closure", "v5.8");
		OPTIONS.put("deprecated", "v5.8");
		OPTIONS.put("exiting", "v5.8");
		OPTIONS.put("glob", "v5.8");
		OPTIONS.put("io", "v5.8");
		OPTIONS.put("closed", "v5.8");
		OPTIONS.put("exec", "v5.8");
		OPTIONS.put("layer", "v5.8");
		OPTIONS.put("newline", "v5.8");
		OPTIONS.put("pipe", "v5.8");
		OPTIONS.put("unopened", "v5.8");
		OPTIONS.put("misc", "v5.8");
		OPTIONS.put("numeric", "v5.8");
		OPTIONS.put("once", "v5.8");
		OPTIONS.put("overflow", "v5.8");
		OPTIONS.put("pack", "v5.8");
		OPTIONS.put("portable", "v5.8");
		OPTIONS.put("recursion", "v5.8");
		OPTIONS.put("redefine", "v5.8");
		OPTIONS.put("regexp", "v5.8");
		OPTIONS.put("severe", "v5.8");
		OPTIONS.put("debugging", "v5.8");
		OPTIONS.put("inplace", "v5.8");
		OPTIONS.put("internal", "v5.8");
		OPTIONS.put("malloc", "v5.8");
		OPTIONS.put("signal", "v5.8");
		OPTIONS.put("substr", "v5.8");
		OPTIONS.put("syntax", "v5.8");
		OPTIONS.put("ambiguous", "v5.8");
		OPTIONS.put("bareword", "v5.8");
		OPTIONS.put("digit", "v5.8");
		OPTIONS.put("parenthesis", "v5.8");
		OPTIONS.put("precedence", "v5.8");
		OPTIONS.put("printf", "v5.8");
		OPTIONS.put("prototype", "v5.8");
		OPTIONS.put("qw", "v5.8");
		OPTIONS.put("reserved", "v5.8");
		OPTIONS.put("semicolon", "v5.8");
		OPTIONS.put("taint", "v5.8");
		OPTIONS.put("threads", "v5.8");
		OPTIONS.put("uninitialized", "v5.8");
		OPTIONS.put("unpack", "v5.8");
		OPTIONS.put("untie", "v5.8");
		OPTIONS.put("utf8", "v5.8");
		OPTIONS.put("void", "v5.8");

		OPTIONS.put("imprecision", "v5.11");
		OPTIONS.put("illegalproto", "v5.11");

		OPTIONS.put("non_unicode", "v5.13");
		OPTIONS.put("nonchar", "v5.13");
		OPTIONS.put("surrogate", "v5.13");

		OPTIONS.put("experimental", "v5.17");
		OPTIONS.put("experimental::lexical_subs", "v5.17");
		OPTIONS.put("experimental::lexical_topic", "v5.17");
		OPTIONS.put("experimental::regex_sets", "v5.17");
		OPTIONS.put("experimental::smartmatch", "v5.17");

		OPTIONS.put("experimental::autoderef", "v5.19");
		OPTIONS.put("experimental::postderef", "v5.19");
		OPTIONS.put("experimental::signatures", "v5.19");
		OPTIONS.put("syscalls", "v5.19");

		OPTIONS.put("experimental::bitwise", "v5.21");
		OPTIONS.put("experimental::const_attr", "v5.21");
		OPTIONS.put("experimental::re_strict", "v5.21");
		OPTIONS.put("experimental::refaliasing", "v5.21");
		OPTIONS.put("experimental::win32_perlio", "v5.21");
		OPTIONS.put("locale", "v5.21");
		OPTIONS.put("missing", "v5.21");
		OPTIONS.put("redundant", "v5.21");
	}

	@NotNull
	@Override
	public HashMap<String, String> getOptions()
	{
		return OPTIONS;
	}

	@Override
	public PerlWarningsMask getWarningMask(PerlUseStatement useStatement, PerlWarningsMask currentMask)
	{
		// fixme implement modification
		return currentMask == null ? new PerlWarningsMask() : currentMask.clone();
	}
}
