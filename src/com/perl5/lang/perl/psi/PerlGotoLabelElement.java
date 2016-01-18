package com.perl5.lang.perl.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * Created by pverma on 12/30/15.
 */
public interface PerlGotoLabelElement extends PsiElement {
    @NotNull
    String getLabel();
}
