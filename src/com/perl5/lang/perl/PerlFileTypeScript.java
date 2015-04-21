package com.perl5.lang.perl;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.perl5.PerlIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import com.intellij.lang.Language;

public class PerlFileTypeScript extends LanguageFileType
{
	public static final PerlFileTypeScript INSTANCE = new PerlFileTypeScript();
	public static final Language LANGUAGE = INSTANCE.getLanguage();

	private PerlFileTypeScript() {
		super(PerlLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public String getName() {
		return "Perl5 script";
	}

	@NotNull
	@Override
	public String getDescription() {
		return "Perl5 script file";
	}

	@NotNull
	@Override
	public String getDefaultExtension() {
		return "pl";
	}

	@Nullable
	@Override
	public Icon getIcon() {
		return PerlIcons.SCRIPT_FILE;
	}
}
