package com.perl5.lang.perl.highlighter;

/**
 * Created by hurricup on 12.04.2015.
 */

import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PerlSyntaxHighlighterFactory extends SyntaxHighlighterFactory
{

	@Override
	@NotNull
	public SyntaxHighlighter getSyntaxHighlighter(@Nullable Project project, @Nullable VirtualFile virtualFile)
	{
		return new PerlSyntaxHighlighter();

	}
}
