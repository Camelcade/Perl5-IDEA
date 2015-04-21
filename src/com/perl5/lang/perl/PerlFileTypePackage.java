package com.perl5.lang.perl;

import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.perl5.PerlIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by hurricup on 21.04.2015.
 */
public class PerlFileTypePackage extends LanguageFileType
{
	public static final PerlFileTypePackage INSTANCE = new PerlFileTypePackage();
	public static final Language LANGUAGE = INSTANCE.getLanguage();

	public PerlFileTypePackage(){super(PerlLanguage.INSTANCE);}

	@NotNull
	@Override
	public String getName() {
		return "Perl5 package";
	}

	@NotNull
	@Override
	public String getDescription() {
		return "Perl5 package";
	}

	@NotNull
	@Override
	public String getDefaultExtension() {
		return "pm";
	}

	@Nullable
	@Override
	public Icon getIcon() {
		return PerlIcons.PM_FILE;
	}
}
