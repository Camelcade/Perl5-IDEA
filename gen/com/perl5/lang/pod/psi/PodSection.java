// This is a generated file. Not intended for manual editing.
package com.perl5.lang.pod.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PodSection extends PsiElement
{

	@NotNull
	List<PodParagraph> getParagraphList();

	@NotNull
	PsiElement getPodTag();

}
