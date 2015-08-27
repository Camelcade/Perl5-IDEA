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

package com.perl5.lang.perl.idea.stubs.imports;

import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import com.perl5.lang.perl.psi.PerlUseStatement;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 20.08.2015.
 */
public class PerlUseStatementStubIndex extends StringStubIndexExtension<PerlUseStatement>
{
	public static final int VERSION = 3;
	public static final StubIndexKey<String, PerlUseStatement> KEY = StubIndexKey.createIndexKey("perl.imports");

	@Override
	public int getVersion()
	{
		return super.getVersion() + VERSION;
	}

	@NotNull
	@Override
	public StubIndexKey<String, PerlUseStatement> getKey()
	{
		return KEY;
	}
}
