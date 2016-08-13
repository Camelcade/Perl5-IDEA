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

package com.perl5.lang.ea.idea.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.psi.PsiFile;
import com.perl5.lang.perl.fileTypes.PurePerlFileType;
import com.perl5.lang.perl.idea.actions.PurePerlActionBase;
import com.perl5.lang.perl.util.PerlActionUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 13.08.2016.
 */
public class GenerateExternalAnnotationsAction extends PurePerlActionBase
{
	@Override
	protected boolean isFileTypeOk(@NotNull PurePerlFileType fileType)
	{
		return fileType.isExternalAnnotationsSource();
	}

	@Override
	public void actionPerformed(AnActionEvent e)
	{
		PsiFile file = PerlActionUtil.getPsiFileFromEvent(e);
		assert file != null;

	}
}
