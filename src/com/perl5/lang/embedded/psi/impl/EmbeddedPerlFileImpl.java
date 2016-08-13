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

package com.perl5.lang.embedded.psi.impl;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.perl5.lang.embedded.EmbeddedPerlLanguage;
import com.perl5.lang.embedded.filetypes.EmbeddedPerlFileType;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 18.05.2015.
 */
public class EmbeddedPerlFileImpl extends PerlFileImpl
{
	public EmbeddedPerlFileImpl(@NotNull FileViewProvider viewProvider)
	{
		super(viewProvider, EmbeddedPerlLanguage.INSTANCE);
	}

	@Override
	public String toString()
	{
		return "Embedded Perl file";
	}


	@Override
	protected FileType getDefaultFileType()
	{
		return EmbeddedPerlFileType.INSTANCE;
	}

	@Override
	public byte[] getPerlContentInBytes()
	{
		return null;
	}

}
