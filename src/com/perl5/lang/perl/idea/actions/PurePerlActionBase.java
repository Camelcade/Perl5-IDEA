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

package com.perl5.lang.perl.idea.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.PsiFile;
import com.perl5.lang.perl.fileTypes.PurePerlFileType;
import com.perl5.lang.perl.util.PerlActionUtil;

/**
 * Created by hurricup on 13.08.2016.
 * Actions applicable to files with pure perl syntax
 */
public abstract class PurePerlActionBase extends PerlActionBase
{
	@Override
	protected boolean isEnabled(AnActionEvent event)
	{
		return isMyFile(PerlActionUtil.getPsiFileFromEvent(event));
	}

	protected boolean isMyFile(PsiFile file)
	{
		if (file == null || !file.isPhysical())
		{
			return false;
		}
		FileType fileType = file.getFileType();
		return fileType instanceof PurePerlFileType;
	}
}
