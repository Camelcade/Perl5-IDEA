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

package com.perl5.lang.perl.psi.utils;

import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.io.StringRef;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by hurricup on 03.06.2015.
 */
public class PerlSubAnnotations
{
	private static final byte IS_METHOD = 0x01;
	private static final byte IS_DEPRECATED = 0x02;
	private static final byte IS_ABSTRACT = 0x04;
	private static final byte IS_OVERRIDE = 0x08;

	private byte myFlags = 0;
	private PerlReturnType myReturnType = PerlReturnType.VALUE;
	private String myReturns = null;

	public PerlSubAnnotations()
	{
	}

	public PerlSubAnnotations(byte flags, String returns, PerlReturnType returnType)
	{
		myFlags = flags;
		myReturnType = returnType;
		myReturns = returns;
	}

	public static PerlSubAnnotations deserialize(@NotNull StubInputStream dataStream) throws IOException
	{
		byte flags = dataStream.readByte();
		StringRef returnsRef = dataStream.readName();
		return new PerlSubAnnotations(
				flags,
				returnsRef == null ? null : returnsRef.getString(),
				PerlReturnType.valueOf(dataStream.readName().toString())
		);
	}

	public void serialize(@NotNull StubOutputStream dataStream) throws IOException
	{
		dataStream.writeByte(myFlags);
		dataStream.writeName(myReturns);
		dataStream.writeName(myReturnType.toString());
	}

	public boolean isMethod()
	{
		return (myFlags & IS_METHOD) == IS_METHOD;
	}

	public void setIsMethod()
	{
		myFlags |= IS_METHOD;
	}

	public boolean isDeprecated()
	{
		return (myFlags & IS_DEPRECATED) == IS_DEPRECATED;
	}

	public void setIsDeprecated()
	{
		myFlags |= IS_DEPRECATED;
	}

	public boolean isAbstract()
	{
		return (myFlags & IS_ABSTRACT) == IS_ABSTRACT;
	}

	public void setIsAbstract()
	{
		myFlags |= IS_ABSTRACT;
	}

	public boolean isOverride()
	{
		return (myFlags & IS_OVERRIDE) == IS_OVERRIDE;
	}

	public void setIsOverride()
	{
		myFlags |= IS_OVERRIDE;
	}

	public String getReturns()
	{
		return myReturns;
	}

	public void setReturns(String returns)
	{
		this.myReturns = returns;
	}

	public PerlReturnType getReturnType()
	{
		return myReturnType;
	}

	public void setReturnType(PerlReturnType returnType)
	{
		this.myReturnType = returnType;
	}
}
