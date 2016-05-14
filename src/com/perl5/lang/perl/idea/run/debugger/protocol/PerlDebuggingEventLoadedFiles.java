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

package com.perl5.lang.perl.idea.run.debugger.protocol;

import com.perl5.lang.perl.idea.run.debugger.ui.PerlScriptsPanel;

/**
 * Created by hurricup on 14.05.2016.
 */
public class PerlDebuggingEventLoadedFiles extends PerlDebuggingEventBase
{
	String[] add;     // list of filenames
	String[] remove; // list of filenames

	@Override
	public void doWork()
	{
		PerlScriptsPanel evalsListPanel = getDebugThread().getEvalsListPanel();
		PerlScriptsPanel scriptListPanel = getDebugThread().getScriptListPanel();

		for (String file : add)
		{
			if (file != null)
			{
				if (file.startsWith(PerlStackFrameDescriptor.EVAL_PREFIX))
				{
					evalsListPanel.add(file);
				}
				else
				{
					scriptListPanel.add(file);
				}
			}
		}

		for (String file : remove)
		{
			if (file != null)
			{
				if (file.startsWith(PerlStackFrameDescriptor.EVAL_PREFIX))
				{
					// fixme we should check if file source is loaded and mark it as unloaded or smth
					evalsListPanel.remove(file);
				}
				else
				{
					scriptListPanel.remove(file);
				}
			}
		}

	}
}
