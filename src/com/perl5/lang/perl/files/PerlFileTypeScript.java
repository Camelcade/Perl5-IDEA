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

package com.perl5.lang.perl.files;

import com.perl5.PerlIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import com.intellij.lang.Language;

public class PerlFileTypeScript extends PerlFileType
{
	public static final PerlFileTypeScript INSTANCE = new PerlFileTypeScript();
	public static final Language LANGUAGE = INSTANCE.getLanguage();

	@NotNull
	@Override
	public String getName() {
		return "Perl5 script";
	}

	@NotNull
	@Override
	public String getDescription() {
		return "Perl5 script file";
	}

	@NotNull
	@Override
	public String getDefaultExtension() {
		return "pl";
	}

	@Nullable
	@Override
	public Icon getIcon() {
		return PerlIcons.SCRIPT_FILE;
	}
}
