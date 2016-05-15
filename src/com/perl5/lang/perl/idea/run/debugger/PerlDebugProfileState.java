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

package com.perl5.lang.perl.idea.run.debugger;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.util.net.NetUtils;
import com.perl5.lang.perl.idea.run.PerlConfiguration;
import com.perl5.lang.perl.idea.run.PerlRunProfileState;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hurricup on 04.05.2016.
 */
public class PerlDebugProfileState extends PerlRunProfileState
{
	private static final String[] DEBUG_ARGUMENTS = new String[]{"-d:Camelcadedb", "-IC:\\Repository\\IDEA-Perl5-Debugger\\lib\\"};
	private static final String debugHost = "localhost";
	private static final boolean isPerlServer = true;
	private Integer debugPort;

	public PerlDebugProfileState(ExecutionEnvironment environment)
	{
		super(environment);
	}

	@NotNull
	@Override
	protected String[] getPerlArguments()
	{
		return DEBUG_ARGUMENTS;
	}

	@Override
	protected Map<String, String> calcEnv(PerlConfiguration runProfile) throws ExecutionException
	{
		Map<String, String> stringStringMap = new HashMap<String, String>(super.calcEnv(runProfile));
		stringStringMap.put("PERL5_DEBUG_ROLE", isPerlServer() ? "server" : "client");
		stringStringMap.put("PERL5_DEBUG_HOST", getDebugHost());
		stringStringMap.put("PERL5_DEBUG_PORT", String.valueOf(getDebugPort()));
		return stringStringMap;
	}

	public String getDebugHost()
	{
		return debugHost;
	}

	public int getDebugPort() throws ExecutionException
	{
		if (debugPort == null)
		{
			debugPort = NetUtils.tryToFindAvailableSocketPort();
			if (debugPort == -1)
			{
				throw new ExecutionException("No free port to work on");
			}
		}

		return debugPort;
	}

	public boolean isPerlServer()
	{
		return isPerlServer;
	}
}
