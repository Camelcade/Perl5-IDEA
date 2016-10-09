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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 14.05.2016.
 */
public class PerlDebuggingEventLoadedFiles extends PerlDebuggingEventBase
{
	PerlLoadedFileDescriptor[] add;     // list of filenames
	PerlLoadedFileDescriptor[] remove; // list of filenames

	@Override
	public void run()
	{
		PerlScriptsPanel evalsListPanel = getDebugThread().getEvalsListPanel();
		PerlScriptsPanel scriptListPanel = getDebugThread().getScriptListPanel();
		List<PerlLoadedFileDescriptor> evalRemove = new ArrayList<PerlLoadedFileDescriptor>();
		List<PerlLoadedFileDescriptor> evalAdd = new ArrayList<PerlLoadedFileDescriptor>();
		List<PerlLoadedFileDescriptor> scriptRemove = new ArrayList<PerlLoadedFileDescriptor>();
		List<PerlLoadedFileDescriptor> scriptAdd = new ArrayList<PerlLoadedFileDescriptor>();

		for (PerlLoadedFileDescriptor fileDescriptor : add)
		{
			if (fileDescriptor != null)
			{
				if (fileDescriptor.isEval())
				{
					evalRemove.add(fileDescriptor);
					evalAdd.add(fileDescriptor);
				}
				else
				{
					scriptRemove.add(fileDescriptor);
					scriptAdd.add(fileDescriptor);
				}
			}
		}

		for (PerlLoadedFileDescriptor fileDescriptor : remove)
		{
			if (fileDescriptor != null)
			{
				if (fileDescriptor.isEval())
				{
					// fixme we should check if file source is loaded and mark it as unloaded or smth
					evalRemove.add(fileDescriptor);
				}
				else
				{
					scriptRemove.add(fileDescriptor);
				}
			}
		}

		evalsListPanel.bulkChange(evalAdd, evalRemove);
		scriptListPanel.bulkChange(scriptAdd, scriptRemove);
	}
}
