package com.perl5;

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import com.intellij.lang.Language;

public class PerlFileType extends LanguageFileType
{
	public static final PerlFileType PERL_FILE_TYPE = new PerlFileType();
	public static final Language PERL_LANGUAGE = PERL_FILE_TYPE.getLanguage();

	private PerlFileType() {
		super(PerlLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public String getName() {
		return "Perl5 package";
	}

	@NotNull
	@Override
	public String getDescription() {
		return "Perl5 package file";
	}

	@NotNull
	@Override
	public String getDefaultExtension() {
		return "pm";
	}

	@Nullable
	@Override
	public Icon getIcon() {
		return PerlIcons.FILE;
	}
}
