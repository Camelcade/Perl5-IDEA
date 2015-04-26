package com.perl5.lang.perl.files;

import com.intellij.lang.Language;
import com.perl5.PerlIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by hurricup on 21.04.2015.
 */
public class PerlFileTypePackage extends PerlFileType
{
	public static final PerlFileTypePackage INSTANCE = new PerlFileTypePackage();
	public static final Language LANGUAGE = INSTANCE.getLanguage();

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
