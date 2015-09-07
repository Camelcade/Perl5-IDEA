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

package com.perl5.lang.perl.internals;

import java.util.regex.Matcher;

/**
 * Created by hurricup on 23.08.2015.
 * Represents perl version
 */
public class PerlVersion implements PerlVersionRegexps
{
	final protected boolean isAlpha;
	final protected boolean isStrict;
	final protected boolean isValid;
	protected int revision = 0;
	protected int major = 0;
	protected int minor = 0;

	public PerlVersion(double version)
	{
		long longVersion = (long) (version * 1000000);
		isStrict = true;
		isAlpha = false;
		isValid = version > 0;
		revision = (int) version;
		longVersion = (longVersion - revision * 1000000);
		major = (int) (longVersion / 1000);
		minor = (int) (longVersion % 1000);
	}

	public PerlVersion(String versionString)
	{
		Matcher matcher = null;
		if ((matcher = strictDecimalVersion.matcher(versionString)).matches())
		{
			isStrict = true;
			isAlpha = false;
		} else if ((matcher = strictDottedDecimalVersion.matcher(versionString)).matches())
		{
			isStrict = true;
			isAlpha = false;
		} else if ((matcher = laxDecimalVersion.matcher(versionString)).matches())
		{
			isStrict = false;
			isAlpha = false;

		} else if ((matcher = laxDottedDecimalVersion.matcher(versionString)).matches())
		{
			isStrict = true;
			isAlpha = false;
		} else
		{
			isAlpha = false;
			isStrict = false;
			isValid = false;
			return;
		}
		isValid = true;
	}

	public double getVersion()
	{
		return (double) revision + ((double) major / 1000) + ((double) minor / 1000000);
	}

	public String toString()
	{
		return Double.toString(getVersion());
	}

	public int getRevision()
	{
		return revision;
	}

	public void setRevision(int revision)
	{
		this.revision = revision;
	}

	public int getMajor()
	{
		return major;
	}

	public void setMajor(int major)
	{
		this.major = major;
	}

	public int getMinor()
	{
		return minor;
	}

	public void setMinor(int minor)
	{
		this.minor = minor;
	}

	public boolean isAlpha()
	{
		return isAlpha;
	}

	public boolean isStrict()
	{
		return isStrict;
	}

	public boolean isValid()
	{
		return isValid;
	}
}
