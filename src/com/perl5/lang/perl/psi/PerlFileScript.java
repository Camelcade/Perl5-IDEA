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

package com.perl5.lang.perl.psi;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.perl5.lang.perl.files.PerlFileTypePackage;
import com.perl5.lang.perl.files.PerlFileTypeScript;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by hurricup on 26.04.2015.
 */
public class PerlFileScript extends PerlFile
{
	public PerlFileScript(@NotNull FileViewProvider viewProvider) {
		super(viewProvider);
	}

	@NotNull
	@Override
	public FileType getFileType() {
		return PerlFileTypeScript.INSTANCE;
	}

	@Override
	public String toString() {
		return "Perl Script File";
	}

	@Override
	public Icon getIcon(int flags) {
		return super.getIcon(flags);
	}

}
