package com.perl5.lang.pod;

import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.PerlLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by hurricup on 21.04.2015.
 */
public class PodFileType extends LanguageFileType
{
	public static final PodFileType INSTANCE = new PodFileType();
	public static final Language LANGUAGE = INSTANCE.getLanguage();

	private PodFileType() {
		super(PodLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public String getName() {
		return "Plain Old Documentation";
	}

	@NotNull
	@Override
	public String getDescription() {
		return "Perl5 Documentation File";
	}

	@NotNull
	@Override
	public String getDefaultExtension() {
		return "pod";
	}

	@Nullable
	@Override
	public Icon getIcon() {
		return PerlIcons.POD_FILE;
	}
}
