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

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.util.text.StringUtil;
import com.perl5.PerlBundle;

/**
 * Created by hurricup on 08.05.2016.
 */
public class PerlDebuggingEventReady extends PerlDebuggingEventBase
{
	private static final String MODULE_VERSION_PREFIX = PerlBundle.message("perl.debugger.version.prefix");
	public String version;

	@Override
	public void run()
	{
	}

	public boolean isValid()
	{
		if (StringUtil.isNotEmpty(version) && StringUtil.startsWith(version, MODULE_VERSION_PREFIX))
		{
			return true;
		}

		Notifications.Bus.notify(new Notification(
				"PERL_DEBUGGER",
				PerlBundle.message("perl.debugger.incorrect.version.title"),
				PerlBundle.message("perl.debugger.incorrect.version.message", MODULE_VERSION_PREFIX, version),
				NotificationType.ERROR
		));
		return false;
	}


}
