package com.perl5.lang.pod;

import com.intellij.psi.tree.IElementType;
import com.perl5.lang.pod.lexer.elements.*;

/**
 * Created by hurricup on 21.04.2015.
 */
// @todo POD_CODE should be multiline and parsed using perl
public interface PodTokenTypes
{
	IElementType POD_MARKER = new PodMarker();
	IElementType POD_TAG = new PodTag();
	IElementType POD_CODE = new PodCode();
	IElementType POD_TEXT = new PodText();
}
