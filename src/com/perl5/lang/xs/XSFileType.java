package com.perl5.lang.xs;

import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.perl5.PerlIcons;
import com.perl5.lang.pod.PodLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by hurricup on 21.04.2015.
 */
public class XSFileType extends LanguageFileType
{
	public static final XSFileType INSTANCE = new XSFileType();
	public static final Language LANGUAGE = INSTANCE.getLanguage();

	private XSFileType() {
		super(XSLanguage.INSTANCE);
	}

	@NotNull
	@Override
	public String getName() {
		return "XS extension";
	}

	@NotNull
	@Override
	public String getDescription() {
		return "Perl5 extension in C";
	}

	@NotNull
	@Override
	public String getDefaultExtension() {
		return "xs";
	}

	@Nullable
	@Override
	public Icon getIcon() {
		return PerlIcons.XS_FILE;
	}
}
