package com.perl5.lang.perl.psi;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.containers.Stack;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.exceptions.PerlParsingException;
import com.perl5.lang.perl.exceptions.SubDeclaredException;
import com.perl5.lang.perl.exceptions.SubDefinedException;
import com.perl5.lang.perl.exceptions.SubDefinitionDiffersDeclarationException;
import com.perl5.lang.perl.parser.PerlCodeBlockState;
import com.perl5.lang.perl.parser.PerlPackage;
import com.perl5.lang.perl.parser.PerlSub;
import com.perl5.lang.perl.parser.PerlTokenData;
import com.perl5.lang.perl.util.PerlFunctionUtil;

import java.util.HashMap;

/**
 * Created by hurricup on 04.05.2015.
 * This wrapper created to be able to store per-parsing data like pragmas, warnings and variables ?
 */
public class PerlBuilder extends GeneratedParserUtilBase.Builder
{
	protected final Stack<PerlCodeBlockState> blockState = new Stack<PerlCodeBlockState>();
	protected final HashMap<String,PerlPackage> nameSpaces = new HashMap<String, PerlPackage>();

	protected String lastParsedPackage;
	protected String lastParsedVersion;

	public PerlBuilder(PsiBuilder builder, GeneratedParserUtilBase.ErrorState state, PsiParser parser) {
		super(builder, state, parser);
	}

	public PerlPackage getNamespace(String name)
	{
		assert name != null;
		assert !name.equals("");

		PerlPackage nameSpace = nameSpaces.get(name);
		if( nameSpace == null )
		{
			nameSpaces.put(name, new PerlPackage(name));
			return getNamespace(name);
		}
		return nameSpace;
	}


	/**
	 * Stores sub definition name for current parsing level
	 * @param subName	sub name
	 */
	public void beginSubDefinition(String subName)
	{
//		System.err.printf("Starting %s definition\n", subName);
		getCurrentBlockState().setSubDefinition(new PerlSub(subName));
	}

	/**
	 * Commiting sub definition, checking if not already defined, if consistent with earlier declaration
	 */
	public void commitSubDefinition() throws PerlParsingException
	{
		PerlCodeBlockState state = getCurrentBlockState();
		assert state != null;

		PerlSub sub = state.getSubDefinition();
		assert sub != null;

		PerlPackage namespace = state.getNamespace();
		assert namespace != null;

		state.setSubDefinition(null);
		namespace.defineSub(sub);
	}

	/**
	 * Stores sub declaration name for current parsing level
	 * @param subName    sub name
	 */
	public void beginSubDeclaration(String subName)
	{
		getCurrentBlockState().setSubDeclaration(new PerlSub(subName));
	}

	/**
	 * Commiting sub declaration
	 * @throws Exception
	 */
	public void commitSubDeclaration() throws PerlParsingException
	{
		PerlCodeBlockState state = getCurrentBlockState();
		assert state != null;

		PerlSub sub = state.getSubDeclaration();
		assert sub != null;

		PerlPackage namespace = state.getNamespace();
		assert namespace != null;

		state.setSubDeclaration(null);
		namespace.declareSub(sub);
	}

	/**
	 * Initialising stack of code block states, push initial state
	 */
	public void initCodeBlockStateStack()
	{
		assert blockState.size() == 0;
		blockState.clear();
		blockState.push(new PerlCodeBlockState());
	}

	/**
	 * Clones current level state to the new one. We've entered inner block
	 * @param debugText	debugging text @todo remove this
	 */
	public void pushCodeBlockState(String debugText)
	{
		blockState.push(new PerlCodeBlockState(getCurrentBlockState()));
//		System.out.println("Pushed codeblock state on: " + debugText);
	}

	/**
	 * Removes top code block state from the stack. We've left the inner block
	 * @param debugText debugging text @todo remove this
	 */
	public void popCodeBlockState(String debugText)
	{
		assert blockState.size() > 0;
		blockState.pop();
//		System.out.println("Popped codeblock state" + debugText);
	}

	/**
	 * Returns CodeBlockState of current block
	 * @return 	code block state
	 */
	public PerlCodeBlockState getCurrentBlockState()
	{
		int stackSize = blockState.size();
		assert stackSize > 0;
		return blockState.get(stackSize - 1);
	}

	/**
	 * Return token ahead of current, skips spaces and comments
	 * @param steps	positive or negative steps number to get token
	 * @return	token data: type and text
	 */
	public PerlTokenData getAheadToken(int steps)
	{
		assert steps != 0;
		int rawStep = 0;
		int step = steps / Math.abs(steps);

		IElementType rawTokenType = null;

		while( steps != 0 )
		{
			rawStep += step;
			rawTokenType = rawLookup(rawStep);

			// reached end
			if( rawTokenType == null )
				return null;

			if( !PerlParserDefinition.WHITE_SPACE_AND_COMMENTS.contains(rawTokenType))
			{
				steps-=step;
			}
		}

		return new PerlTokenData(rawTokenType, getOriginalText().subSequence(rawTokenTypeStart(rawStep), rawTokenTypeStart(rawStep+1)).toString());
	}

	public String getLastParsedPackage()
	{
		return lastParsedPackage;
	}

	public void setLastParsedPackage(String lastParsedPackage)
	{
		this.lastParsedPackage = lastParsedPackage;
	}

	public String getLastParsedVersion()
	{
		return lastParsedVersion;
	}

	public void setLastParsedVersion(String lastParsedVersion)
	{
		this.lastParsedVersion = lastParsedVersion;
	}
}
