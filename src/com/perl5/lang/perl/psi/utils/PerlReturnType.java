package com.perl5.lang.perl.psi.utils;

/**
 * Created by hurricup on 03.06.2015.
 */
public enum PerlReturnType
{
	VALUE,		// default
	REF,		// Package::Name	NYI
	ARRAY,		// @Package::Name	NYI
	HASH,		// %Package::Name	NYI
	ARRAY_REF,	// [Package::Name]
	HASH_REF,	// {Package::Name}
	CODE_REF	// &				NYI
}
