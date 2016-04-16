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

package com.perl5.lang.mason2.psi.impl;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.perl5.lang.mason2.Mason2TemplatingLanguage;
import com.perl5.lang.mason2.filetypes.MasonTopLevelComponentFileType;
import com.perl5.lang.mason2.idea.generation.Mason2TemplatingCodeGeneratorImpl;
import com.perl5.lang.perl.extensions.PerlCodeGenerator;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 13.01.2016.
 */
public class MasonTemplatingFileImpl extends MasonFileImpl
{
	public MasonTemplatingFileImpl(@NotNull FileViewProvider viewProvider)
	{
		super(viewProvider, Mason2TemplatingLanguage.INSTANCE);
	}

	@Override
	public String toString()
	{
		return "Mason2 template file";
	}

	@Override
	protected FileType getDefaultFileType()
	{
		return MasonTopLevelComponentFileType.INSTANCE;
	}

	@Override
	public PerlCodeGenerator getCodeGenerator()
	{
		return Mason2TemplatingCodeGeneratorImpl.INSTANCE;
	}

	@Override
	public byte[] getPerlContentInBytes()
	{
		return null;
	}

}
