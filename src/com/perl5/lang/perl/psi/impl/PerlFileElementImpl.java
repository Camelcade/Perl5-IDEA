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

package com.perl5.lang.perl.psi.impl;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileListener;
import com.intellij.psi.FileViewProvider;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.PerlFileType;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 26.04.2015.
 */
public class PerlFileElementImpl extends PsiFileBase implements PerlLexicalScope
{
	VirtualFileListener myChangeListener;

	public PerlFileElementImpl(@NotNull FileViewProvider viewProvider) {
		super(viewProvider, PerlLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public FileType getFileType()
	{
		return PerlFileType.INSTANCE;
	}

	@Override
	public PerlLexicalScope getLexicalScope()
	{
		return null;
	}

	/**
	 * Returns package name for this psi file. Name built from filename and innermost root.
	 * @return canonical package name or null if it's not pm file or it's not in source root
	 */
	public String getFilePackageName()
	{
		VirtualFile containingFile = getVirtualFile();

		if( "pm".equals(containingFile.getExtension()))
		{
			VirtualFile innermostSourceRoot = PerlUtil.findInnermostSourceRoot(getProject(), containingFile);
			String relativePath = VfsUtil.getRelativePath(containingFile, innermostSourceRoot);
			return PerlPackageUtil.getPackageNameByPath(relativePath);
		}
		return null;
	}

}
