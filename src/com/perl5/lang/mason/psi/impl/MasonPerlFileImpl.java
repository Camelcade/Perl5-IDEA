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

package com.perl5.lang.mason.psi.impl;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.perl5.lang.mason.filetypes.MasonTopLevelComponentFileType;
import com.perl5.lang.mason.MasonPerlLanguage;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 20.12.2015.
 */
public class MasonPerlFileImpl extends PerlFileImpl
{
	public MasonPerlFileImpl(@NotNull FileViewProvider viewProvider)
	{
		super(viewProvider, MasonPerlLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public FileType getFileType()
	{
		return MasonTopLevelComponentFileType.INSTANCE;
	}

	@Override
	public String toString()
	{
		return "Mojolicious Perl5 Template";
	}
}
