package com.perl5.lang.perl.files;

import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.perl5.lang.perl.PerlLanguage;

/**
 * Created by hurricup on 26.04.2015.
 */
public abstract class PerlFileType extends LanguageFileType
{

	public PerlFileType(){super(PerlLanguage.INSTANCE);}

}
