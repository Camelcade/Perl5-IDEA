/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.pod.parser.psi.stubs;

import com.intellij.psi.stubs.StubIndexKey;
import com.perl5.lang.perl.idea.stubs.PerlStubIndexBase;
import com.perl5.lang.pod.parser.psi.PodStubBasedSection;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 26.03.2016.
 */
public class PodStubIndex extends PerlStubIndexBase<PodStubBasedSection>
{
	public static final StubIndexKey<String, PodStubBasedSection> KEY = StubIndexKey.createIndexKey("pod.index");

	public static final int VERSION = 3;

	@Override
	public int getVersion()
	{
		return super.getVersion() + VERSION;
	}

	@NotNull
	@Override
	public StubIndexKey<String, PodStubBasedSection> getKey()
	{
		return KEY;
	}
}
