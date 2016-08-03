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

package com.perl5.lang.ea.fileTypes;

import com.perl5.PerlBundle;
import com.perl5.PerlIcons;
import com.perl5.lang.ea.PerlExternalAnnotationsLanguage;
import com.perl5.lang.perl.fileTypes.PerlFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by hurricup on 03.08.2016.
 */
public class PerlExternalAnnotationsFileType extends PerlFileType
{
	public final static PerlExternalAnnotationsFileType INSTANCE = new PerlExternalAnnotationsFileType();

	private PerlExternalAnnotationsFileType()
	{
		super(PerlExternalAnnotationsLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public String getName()
	{
		return PerlBundle.message("pmea.filetype.name");
	}

	@NotNull
	@Override
	public String getDescription()
	{
		return PerlBundle.message("pmea.filetype.description");
	}

	@NotNull
	@Override
	public String getDefaultExtension()
	{
		return PerlBundle.message("pmea.filetype.extension");
	}

	@Nullable
	@Override
	public Icon getIcon()
	{
		return PerlIcons.PERL_EXTERNAL_ANNOTATIONS_LANGUAGE_ICON;
	}

	@Override
	public boolean checkStrictPragma()
	{
		return false;
	}

	@Override
	public boolean checkWarningsPragma()
	{
		return false;
	}
}
