/*
 * Copyright 2015 Alexandr Evstigneev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.perl5.lang.perl.idea.livetemplates;

import com.intellij.codeInsight.template.TemplateContextType;
import com.intellij.psi.PsiFile;
import com.perl5.lang.perl.PerlFileType;

/**
 * Created by ELI-HOME on 01-Jun-15.
 */
public class PerlTemplateContext extends TemplateContextType {

    protected PerlTemplateContext() {
        super("Perl5", "Perl 5");
    }

    @Override
    public boolean isInContext(PsiFile psiFile, int i) {
        if((psiFile.getFileType() instanceof PerlFileType)){
            return true;
        }
        return false;
    }
}
