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
 * Created by hurricup on 08.05.2015.
 * Class represents defined or declared sub
 */
public class PerlSub
{
	private String name;
	private PerlSubPrototype prototype;
	private PerlSubAttributes attributes;
	private PerlSubSignature signature;

	public PerlSub(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public PerlSubPrototype getPrototype()
	{
		return prototype;
	}

	public void setPrototype(PerlSubPrototype prototype)
	{
		this.prototype = prototype;
	}

	public PerlSubAttributes getAttributes()
	{
		return attributes;
	}

	public void setAttributes(PerlSubAttributes attributes)
	{
		this.attributes = attributes;
	}

	public PerlSubSignature getSignature()
	{
		return signature;
	}

	public void setSignature(PerlSubSignature signature)
	{
		this.signature = signature;
	}

	public boolean lookAlike(PerlSub otherSub)
	{
		return this == otherSub
			|| name.equals(otherSub.getName())
				&& (prototype == null && otherSub.getPrototype() == null || prototype != null && prototype.equals(otherSub.getPrototype()))
				&& (attributes == null && otherSub.getAttributes() == null || attributes!= null && attributes.equals(otherSub.getAttributes()))
		;
	}

}
