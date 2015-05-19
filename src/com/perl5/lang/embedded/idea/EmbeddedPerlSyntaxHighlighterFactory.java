package com.perl5.lang.embedded.idea;

import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighterFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 19.05.2015.
 */
public class EmbeddedPerlSyntaxHighlighterFactory extends PerlSyntaxHighlighterFactory
{
	@Override
	@NotNull
	public SyntaxHighlighter getSyntaxHighlighter(@Nullable Project project, @Nullable VirtualFile virtualFile)
	{
		return new EmbeddedPerlHighlighter(project,virtualFile);
	}

}
