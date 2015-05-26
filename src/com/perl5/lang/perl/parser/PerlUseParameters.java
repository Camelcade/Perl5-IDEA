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

package com.perl5.lang.perl.parser;

import java.util.ArrayList;

/**
 * Created by hurricup on 04.05.2015.
 * class represents use/no state modifications
 */
public class PerlUseParameters
{
	private String perlVersion = null;
	private String packageName = null;
	private String packageVersion = null;
	private ArrayList<String> packageParams = null;
	private PerlPackageFile packageFile = null;

	public String getPerlVersion()
	{
		return perlVersion;
	}

	public void setPerlVersion(String perlVersion)
	{
		this.perlVersion = perlVersion;
	}

	public String getPackageName()
	{
		return packageName;
	}

	public void setPackageName(String packageName)
	{
		this.packageName = packageName;
	}

	public String getPackageVersion()
	{
		return packageVersion;
	}

	public void setPackageVersion(String packageVersion)
	{
		this.packageVersion = packageVersion;
	}

	public ArrayList<String> getPackageParams()
	{
		return packageParams;
	}

	public void setPackageParams(ArrayList<String> packageParams)
	{
		this.packageParams = new ArrayList<String>(packageParams);
	}

	public PerlPackageFile getPackageFile()
	{
		return packageFile;
	}

	public void setPackageFile(PerlPackageFile packageFile)
	{
		this.packageFile = packageFile;
	}
}
