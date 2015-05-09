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
