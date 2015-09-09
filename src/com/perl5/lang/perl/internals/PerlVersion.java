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

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by hurricup on 23.08.2015.
 * Represents perl version
 */
public class PerlVersion implements PerlVersionRegexps, Comparable<PerlVersion>
{
	public static final PerlVersion V5_12 = new PerlVersion(5.012);

	protected boolean isAlpha;
	protected boolean isStrict;
	protected boolean isValid;
	protected int revision;
	protected int major;
	protected int minor;
	protected List<Integer> extraChunks = Collections.emptyList();

	public PerlVersion(double version)
	{
		try
		{
			parseDoubleVersion(version);
		} catch (Exception e)
		{
		}
	}

	public PerlVersion(String versionString)
	{

		try
		{
			Matcher matcher;

			if (numericVersion.matcher(versionString).matches())
			{
				parseDoubleVersion(Double.parseDouble(versionString.replace("_", "")));
				isAlpha = versionString.contains("_"); // fixme not sure about this at all
			} else if ((matcher = dottedVersion.matcher(versionString)).matches())
			{
				List<String> versionChunks = new ArrayList<String>(Arrays.asList(versionString.replace("v", "").replace('_', '.').split("\\.")));
				isAlpha = matcher.group(1) != null;
				revision = Integer.parseInt(versionChunks.remove(0));

				if (versionChunks.size() > 0)
				{
					if (versionChunks.get(0).length() > 3)
						throw new Exception();

					major = Integer.parseInt(versionChunks.remove(0));

					if (versionChunks.size() > 0)
					{
						if (versionChunks.get(0).length() > 3)
							throw new Exception();

						minor = Integer.parseInt(versionChunks.remove(0));

						if (versionChunks.size() > 0)
						{
							extraChunks = new ArrayList<Integer>();
							for (String chunk : versionChunks)
								if (chunk.length() > 3)
									throw new Exception();
								else
									extraChunks.add(Integer.parseInt(chunk));
						}
					}
				}

			} else
			{
				isValid = false;
				return;
			}
			isStrict = strict.matcher(versionString).matches();
			isValid = true;
		} catch (Exception e) // catching numberformat exception
		{
			isValid = isStrict = isAlpha = false;
			revision = major = minor = 0;
		}
	}

	public void parseDoubleVersion(double version) throws Exception
	{
		if (version <= Integer.MAX_VALUE)
		{
			long longVersion = (long) (version * 1000000);
			isStrict = true;
			isAlpha = false;
			isValid = version > 0;
			if (isValid)
			{
				revision = (int) version;
				longVersion = (longVersion - revision * 1000000);
				major = (int) (longVersion / 1000);
				minor = (int) (longVersion % 1000);
			}
		} else
			throw new Exception("Version is too big");
	}

	public double getDoubleVersion()
	{
		double version = (double) revision + ((double) major / 1000) + ((double) minor / 1000000);

		if (!extraChunks.isEmpty())
		{
			long divider = 1000000000;
			for (Integer chunk : extraChunks)
			{
				version += ((double) chunk) / divider;
				divider *= 1000;
			}
		}

		return version;
	}

	public String getStrictDottedVersion()
	{
		List<String> result = new ArrayList<String>(Arrays.asList(Integer.toString(revision)));

		if (major > 0 || minor > 0 || extraChunks.size() > 0)
			result.add(Integer.toString(major));

		if (minor > 0 || extraChunks.size() > 0)
			result.add(Integer.toString(minor));

		for (Integer chunk : extraChunks)
			result.add(Integer.toString(chunk));

		return "v" + StringUtils.join(result, ".");
	}

	public String getStrictNumericVersion()
	{
		return Double.toString(getDoubleVersion());
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

	@Override
	public int compareTo(PerlVersion o)
	{
		return Double.compare(getDoubleVersion(), o.getDoubleVersion());
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof PerlVersion && (this == o || this.getDoubleVersion() == ((PerlVersion) o).getDoubleVersion());
	}

	public boolean lesserThan(PerlVersion o)
	{
		return compareTo(o) == -1;
	}

	public boolean greaterThan(PerlVersion o)
	{
		return compareTo(o) == 1;
	}
}
