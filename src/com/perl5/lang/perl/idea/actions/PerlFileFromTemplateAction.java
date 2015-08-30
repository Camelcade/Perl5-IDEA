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

package com.perl5.lang.perl.idea.actions;

import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.DumbAware;

import javax.swing.*;

/**
 * Created by hurricup on 26.08.2015.
 */
public abstract class PerlFileFromTemplateAction extends CreateFileFromTemplateAction implements DumbAware
{
	public PerlFileFromTemplateAction(String text, String description, Icon icon)
	{
		super(text, description, icon);
	}

	@Override
	protected boolean isAvailable(DataContext dataContext)
	{
		// fixme conditions should be changed for micro-ides
		return super.isAvailable(dataContext);
	}
}
