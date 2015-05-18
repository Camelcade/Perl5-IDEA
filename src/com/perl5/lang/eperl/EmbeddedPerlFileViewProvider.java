package com.perl5.lang.eperl;

import com.intellij.lang.Language;
import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.lang.StdLanguages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.MultiplePsiFilesPerDocumentFileViewProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.source.PsiFileImpl;
import com.intellij.psi.templateLanguages.TemplateLanguageFileViewProvider;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Set;

/**
 * Created by hurricup on 18.05.2015.
 */
public class EmbeddedPerlFileViewProvider extends MultiplePsiFilesPerDocumentFileViewProvider implements TemplateLanguageFileViewProvider
{

	private static final THashSet<Language> ourRelevantLanguages =
			new THashSet<Language>(Arrays.asList(StdLanguages.HTML, PerlLanguage.INSTANCE, EmbeddedPerlLanguage.INSTANCE));


	public EmbeddedPerlFileViewProvider(final PsiManager manager, final VirtualFile virtualFile, final boolean physical) {
		super(manager, virtualFile, physical);
	}

	@Override
	@NotNull
	public Language getBaseLanguage() {
		return EmbeddedPerlLanguage.INSTANCE;
	}

	@Override
	@NotNull
	public Set<Language> getLanguages() {
		return ourRelevantLanguages;
	}

	@Override
	@Nullable
	protected PsiFile createFile(@NotNull final Language lang) {
		if (lang == getTemplateDataLanguage()) {
			// final PsiFileImpl file = (PsiFileImpl)LanguageParserDefinitions.INSTANCE.forLanguage(lang).createFile(this);

			final PsiFileImpl file = (PsiFileImpl) LanguageParserDefinitions.INSTANCE.forLanguage(StdLanguages.HTML).createFile(this);
			file.setContentElementType(PerlElementTypes.PERL_TEMPLATE_DATA);
			return file;
		}

		if (lang == getBaseLanguage()) {
			return LanguageParserDefinitions.INSTANCE.forLanguage(lang).createFile(this);
		}
		return null;
	}

	@Override
	protected EmbeddedPerlFileViewProvider cloneInner(final VirtualFile copy) {
		return new EmbeddedPerlFileViewProvider(getManager(), copy, false);
	}

	@Override
	@NotNull
	public Language getTemplateDataLanguage() {
		return StdLanguages.HTML;
	}

}
