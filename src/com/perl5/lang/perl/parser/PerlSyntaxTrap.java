package com.perl5.lang.perl.parser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hurricup on 04.05.2015.
 */
public class PerlSyntaxTrap
{
	protected boolean capture = false;
	protected final ArrayList<String> captures = new ArrayList<String>();

	public void start()
	{
		capture = true;
		captures.clear();
	}

	public void stop()
	{
		capture = false;
	}

	public ArrayList<String> getCaptures()
	{
		return captures;
	}

	public String getFistCapture()
	{
		assert captures.size() > 0;
		return captures.get(0);
	}


	public void capture(String string)
	{
		if( capture)
			captures.add(string);
	}
}
