package com.perl5.lang.perl.idea.livetemplates;

import com.intellij.codeInsight.template.impl.DefaultLiveTemplatesProvider;
import org.jetbrains.annotations.Nullable;

/**
 * Created by ELI-HOME on 01-Jun-15.
 * makes sure the file liveTemplates/perl.xml will be added to the live templates list
 */
public class PerlDefaultTemplatesProvider implements DefaultLiveTemplatesProvider {

    public static final String[] TEMPLATES = new String[]{"liveTemplates/Perl"};

    @Override
    public String[] getDefaultLiveTemplateFiles() {
        return TEMPLATES;
    }

    @Nullable
    @Override
    public String[] getHiddenLiveTemplateFiles() {
        return null;
    }

}
