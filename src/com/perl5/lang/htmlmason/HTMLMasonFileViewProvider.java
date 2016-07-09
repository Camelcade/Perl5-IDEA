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

package com.perl5.lang.htmlmason;

import com.intellij.lang.Language;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes;
import com.perl5.lang.perl.psi.PerlMultiplePsiFilesPerDocumentFileViewProvider;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 05.03.2016.
 */
public class HTMLMasonFileViewProvider extends PerlMultiplePsiFilesPerDocumentFileViewProvider implements HTMLMasonElementTypes
{
	public HTMLMasonFileViewProvider(final PsiManager manager, final VirtualFile virtualFile, final boolean physical)
	{
		super(manager, virtualFile, physical);
	}

	@Override
	@NotNull
	public Language getBaseLanguage()
	{
		return HTMLMasonLanguage.INSTANCE;
	}

	@NotNull
	@Override
	protected IElementType getTemplateContentElementType()
	{
		return HTML_MASON_HTML_TEMPLATE_DATA;
	}

	@NotNull
	@Override
	protected IElementType getPODContentElementType()
	{
		return HTML_MASON_POD_TEMPLATE_DATA;
	}

	@Override
	protected HTMLMasonFileViewProvider cloneInner(final VirtualFile copy)
	{
		return new HTMLMasonFileViewProvider(getManager(), copy, false);
	}
}
