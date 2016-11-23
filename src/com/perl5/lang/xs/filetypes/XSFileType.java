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

package com.perl5.lang.xs.filetypes;

import com.intellij.lang.Language;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.fileTypes.PerlPluginBaseFileType;
import com.perl5.lang.xs.XSLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by hurricup on 21.04.2015.
 */
public class XSFileType extends PerlPluginBaseFileType
{
	public static final XSFileType INSTANCE = new XSFileType();
	public static final Language LANGUAGE = INSTANCE.getLanguage();

	private XSFileType()
	{
		super(XSLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public String getName()
	{
		return "XS extension";
	}

	@NotNull
	@Override
	public String getDescription()
	{
		return "Perl5 extension in C";
	}

	@NotNull
	@Override
	public String getDefaultExtension()
	{
		return "xs";
	}

	@Nullable
	@Override
	public Icon getIcon()
	{
		return PerlIcons.XS_FILE;
	}
}
