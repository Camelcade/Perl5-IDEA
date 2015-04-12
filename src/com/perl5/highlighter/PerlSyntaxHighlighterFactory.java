package com.perl5.highlighter;

/**
 * Created by hurricup on 12.04.2015.
 */

import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public class PerlSyntaxHighlighterFactory extends SyntaxHighlighterFactory
{
	@NotNull
	@Override
	public SyntaxHighlighter getSyntaxHighlighter(Project project, VirtualFile virtualFile) {
		return new PerlSyntaxHighlighter();
	}
}
