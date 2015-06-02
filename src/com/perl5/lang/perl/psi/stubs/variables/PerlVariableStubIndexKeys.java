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

package com.perl5.lang.perl.psi.stubs.variables;

import com.intellij.psi.stubs.StubIndexKey;
import com.perl5.lang.perl.psi.PerlVariable;

/**
 * Created by hurricup on 30.05.2015.
 */
public interface PerlVariableStubIndexKeys
{
	public static final StubIndexKey<String,PerlVariable> KEY_SCALAR = StubIndexKey.createIndexKey("perl.global.scalar");
	public static final StubIndexKey<String,PerlVariable> KEY_ARRAY = StubIndexKey.createIndexKey("perl.global.array");
	public static final StubIndexKey<String,PerlVariable> KEY_ARRAY_INDEX = StubIndexKey.createIndexKey("perl.global.array.index");
	public static final StubIndexKey<String,PerlVariable> KEY_HASH = StubIndexKey.createIndexKey("perl.global.hash");

}
