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

package com.perl5.lang.pod.parser.psi.impl;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.filetypes.PodFileType;
import com.perl5.lang.pod.parser.psi.PodFile;
import com.perl5.lang.pod.parser.psi.PodRenderingContext;
import com.perl5.lang.pod.parser.psi.PodTitledSection;
import com.perl5.lang.pod.parser.psi.util.PodFileUtil;
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by hurricup on 21.04.2015.
 */
public class PodFileImpl extends PsiFileBase implements PodFile
{
	public PodFileImpl(@NotNull FileViewProvider viewProvider)
	{
		super(viewProvider, PodLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public FileType getFileType()
	{
		return PodFileType.INSTANCE;
	}

	@Override
	public String toString()
	{
		return "POD file";
	}

	@Override
	public Icon getIcon(int flags)
	{
		PsiFile baseFile = getViewProvider().getAllFiles().get(0);
		return baseFile == this ? super.getIcon(flags) : baseFile.getIcon(flags);
	}

	// fixme this is debugging method
	public String getAsHTML()
	{
		StringBuilder builder = new StringBuilder();
		renderElementAsHTML(builder, new PodRenderingContext());
		return builder.toString();
	}

	// fixme this is debugging method
	public String getAsText()
	{
		StringBuilder builder = new StringBuilder();
		renderElementAsText(builder, new PodRenderingContext());
		return builder.toString();
	}

	@Override
	public void renderElementAsHTML(StringBuilder builder, PodRenderingContext context)
	{
		PodRenderUtil.renderPsiRangeAsHTML(getFirstNamedBlock(), null, builder, context);
	}

	@Nullable
	public PsiElement getFirstNamedBlock()
	{
		return findChildByClass(PodTitledSection.class);
	}

	@Override
	public void renderElementAsText(StringBuilder builder, PodRenderingContext context)
	{
		PodRenderUtil.renderPsiRangeAsText(getFirstNamedBlock(), null, builder, context);
	}

	@Override
	public boolean isIndexed()
	{
		return false;
	}

	@Override
	public int getListLevel()
	{
		return 0;
	}

	@Override
	public boolean isHeading()
	{
		return false;
	}

	@Override
	public ItemPresentation getPresentation()
	{
		return this;
	}

	@Nullable
	@Override
	public String getPresentableText()
	{
		String packageName = PodFileUtil.getPackageName(this);
		if (StringUtil.isEmpty(packageName))
		{
			return getName();
		}
		if (StringUtil.startsWith(packageName, "pods::"))
		{
			return packageName.substring(6);
		}
		return packageName;
	}

	@Nullable
	@Override
	public String getLocationString()
	{
		final PsiDirectory psiDirectory = getParent();
		if (psiDirectory != null)
		{
			return psiDirectory.getVirtualFile().getPresentableUrl();
		}
		return null;
	}

	@Nullable
	@Override
	public Icon getIcon(boolean unused)
	{
		return getFileType().getIcon();
	}

	@Nullable
	@Override
	public String getPodLink()
	{
		return getPresentableText();
	}

	@Nullable
	@Override
	public String getPodLinkText()
	{
		return getPodLink();
	}

	@Override
	public int getHeadingLevel()
	{
		return 0;
	}


	@Nullable
	@Override
	public String getUsageViewTypeLocation()
	{
		return "POD file";
	}

	@Nullable
	@Override
	public String getUsageViewLongNameLocation()
	{
		return "NYI Long name location string for " + this;
	}

	@Nullable
	@Override
	public String getUsageViewShortNameLocation()
	{
		return "NYI Short name location string for " + this;
	}

}