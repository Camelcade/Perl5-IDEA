package com.perl5.lang.perl.idea.livetemplates;

import com.intellij.codeInsight.template.TemplateContextType;
import com.intellij.psi.PsiFile;
import com.perl5.lang.perl.PerlFileType;

/**
 * Created by ELI-HOME on 01-Jun-15.
 */
public class PerlTemplateContext extends TemplateContextType {

    protected PerlTemplateContext() {
        super("Perl", "Perl");
    }

    @Override
    public boolean isInContext(PsiFile psiFile, int i) {
        if((psiFile.getFileType() instanceof PerlFileType)){
            return true;
        }
        return false;
    }
}
