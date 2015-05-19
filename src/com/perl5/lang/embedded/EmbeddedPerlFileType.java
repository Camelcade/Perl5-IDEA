package com.perl5.lang.embedded;

import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.perl5.PerlIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by hurricup on 18.05.2015.
 */
public class EmbeddedPerlFileType extends LanguageFileType
{
	public static final EmbeddedPerlFileType INSTANCE = new EmbeddedPerlFileType();
	public static final Language LANGUAGE = INSTANCE.getLanguage();

	public EmbeddedPerlFileType(){
		super(EmbeddedPerlLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public String getName() {
		return "Embedded perl";
	}

	@NotNull
	@Override
	public String getDescription() {
		return "Embedded perl file";
	}

	@NotNull
	@Override
	public String getDefaultExtension() {
		return "thtml";
	}

	@Nullable
	@Override
	public Icon getIcon() {
		return PerlIcons.EMBEDDED_PERL_FILE;
	}

}
