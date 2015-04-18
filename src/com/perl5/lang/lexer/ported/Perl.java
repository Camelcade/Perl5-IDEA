package com.perl5.lang.lexer.ported;

/**
 * Created by hurricup on 18.04.2015.
 * Perl.h
 */
public interface Perl
{
	enum expectation {
		XOPERATOR,
		XTERM,
		XREF,
		XSTATE,
		XBLOCK,
		XATTRBLOCK,
		XATTRTERM,
		XTERMBLOCK,
		XBLOCKTERM,
		XPOSTDEREF,
		XTERMORDORDOR /* evil hack */
    /* update exp_name[] in toke.c if adding to this enum */
	};

	/* handy constants */
	static final String PL_warn_uninit = "Use of uninitialized value%s%s%s";
	static final String PL_warn_uninit_sv = "Use of uninitialized value%s%s%s";//" SVf "
	static final String PL_warn_nosemi = "Semicolon seems to be missing";
	static final String PL_warn_reserved = "Unquoted string \"%s\" may clash with future reserved word";
	static final String PL_warn_nl = "Unsuccessful %s on filename containing newline";
	static final String PL_no_wrongref = "Can't use %s ref as %s ref";
	/* The core no longer needs these here. If you require the string constant,
	please inline a copy into your own code.  */
	static final String PL_no_symref = "Can't use string (\"%.32s\") as %s ref while \"strict refs\" in use"; // __attribute__deprecated__

	static final String PL_no_symref_sv = "Can't use string (\"%s\") as %s ref while \"strict refs\" in use"; // __attribute__deprecated__ " SVf32
	static final String PL_no_usym = "Can't use an undefined value as %s reference";
	static final String PL_no_aelem = "Modification of non-creatable array value attempted, subscript %d";
	static final String PL_no_helem_sv = "Modification of non-creatable hash value attempted, subscript \"%s\""; // " SVf "
	static final String PL_no_modify = "Modification of a read-only value attempted";
	static final String PL_no_mem = "Out of memory!\n";
	static final String PL_no_security = "Insecure dependency in %s%s";
	static final String PL_no_sock_func = "Unsupported socket function \"%s\" called";
	static final String PL_no_dir_func = "Unsupported directory function \"%s\" called";
	static final String PL_no_func = "The %s function is unimplemented";
	static final String PL_no_myglob = "\"%s\" %se %s can't be in a package";
	static final String PL_no_localize_ref = "Can't localize through a reference";
	static final String PL_memory_wrap = "panic: memory wrap";

	static final String PL_Yes = "1";
	static final String PL_No = "";
	static final String PL_hexdigit = "0123456789abcdef0123456789ABCDEF";

}
