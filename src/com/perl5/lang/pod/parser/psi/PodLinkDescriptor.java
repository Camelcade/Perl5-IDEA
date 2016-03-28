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

package com.perl5.lang.pod.parser.psi;

import com.intellij.openapi.util.text.StringUtil;
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hurricup on 27.03.2016.
 * Builds pod link from the string
 */
public class PodLinkDescriptor
{
	private static final Pattern FILE_IDENTIFIER_PATTERN = Pattern.compile("([^\\s/\"'`]+)");
	private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("\"([^\"]*?)\"|(.*?)");
	private static final Pattern EXPLICIT_TITLE_PATTERN = Pattern.compile("(?:(?:" + IDENTIFIER_PATTERN + ")\\|)?");
	private static final Pattern URL_PATTERN = Pattern.compile(
			EXPLICIT_TITLE_PATTERN +
					"(\\A\\w+:[^:\\s]\\S*\\z)"
	);
	private static final Pattern NAMED_ELEMENT_PATTERN = Pattern.compile(
			EXPLICIT_TITLE_PATTERN +    // title
					"(?:(?<!/)" + FILE_IDENTIFIER_PATTERN + "(?=$|/))?" +        // file
					"(?:/?(?:" + IDENTIFIER_PATTERN + "))?"    // section
	);

	private final String myOriginal;
	private String myFileId;
	private String myEnforcedFileId;
	private String mySection;
	private String myTitle;
	private boolean myIsUrl;

	private PodLinkDescriptor(String link)
	{
		myOriginal = link;
	}

	@Nullable
	public static PodLinkDescriptor getDescriptor(String link)
	{
		PodLinkDescriptor descriptor = new PodLinkDescriptor(link);
		Matcher m;
		if ((m = URL_PATTERN.matcher(link)).matches())
		{
			if (m.group(1) != null)
				descriptor.setTitle(m.group(1));
			else if (m.group(2) != null)
				descriptor.setTitle(m.group(2));

			descriptor.setFileId(m.group(3));
			descriptor.setUrl(true);

			return descriptor;
		}
		else if ((m = NAMED_ELEMENT_PATTERN.matcher(link)).matches())
		{
			if (m.group(1) != null)
				descriptor.setTitle(m.group(1));
			else if (m.group(2) != null)
				descriptor.setTitle(m.group(2));

			if (m.group(3) != null)
				descriptor.setFileId(m.group(3));

			if (m.group(4) != null)
				descriptor.setSection(m.group(4));
			else if (m.group(5) != null)
				descriptor.setSection(m.group(5));

			return descriptor;
		}
		System.err.println("Unable to parse: " + link);
		return null;
	}

	@Override
	public String toString()
	{
		return super.toString() + myOriginal;
	}

	public String getFileId()
	{
		return myFileId;
	}

	public void setFileId(String myFileId)
	{
		this.myFileId = StringUtil.isEmpty(myFileId) ? null : myFileId;
	}

	public String getSection()
	{
		return mySection;
	}

	public void setSection(String mySection)
	{
		this.mySection = StringUtil.isEmpty(mySection) ? null : mySection;
	}

	public String getTitle()
	{
		return myTitle == null ? getInferredTitle() : myTitle;
	}

	public void setTitle(String myTitle)
	{
		this.myTitle = StringUtil.isEmpty(myTitle) ? null : myTitle;
	}

	protected String getInferredTitle()
	{
		if (getFileId() != null)
		{
			if (getSection() != null)
			{
				return getSection() + " in " + getFileId();
			}
			else
			{
				return getFileId();
			}
		}
		else if (getSection() != null)
		{
			return getSection();
		}
		return "INCORRECTLY PARSED LINK, REPORT TO DEVS";
	}

	public boolean isUrl()
	{
		return myIsUrl;
	}

	public void setUrl(boolean url)
	{
		myIsUrl = url;
	}

	public String getCanonicalUrl()
	{
		if (isUrl())
		{
			return getFileId();
		}

		StringBuilder url = new StringBuilder("");
		if (getFileId() != null)
		{
			url.append(getFileId());
		}
		else if (getEnforcedFileId() != null)
		{
			url.append(getEnforcedFileId());
		}

		if (getSection() != null)
		{
			url.append("/");
			url.append(getSection());
		}

		return PodRenderUtil.encodeLink(url.toString());
	}

	public String getEnforcedFileId()
	{
		return myEnforcedFileId;
	}

	public void setEnforcedFileId(String myEnforcedFileId)
	{
		this.myEnforcedFileId = myEnforcedFileId;
	}
}
