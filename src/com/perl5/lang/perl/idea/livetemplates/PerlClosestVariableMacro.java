package com.perl5.lang.perl.idea.livetemplates;

import com.intellij.codeInsight.template.Expression;
import com.intellij.codeInsight.template.ExpressionContext;
import com.intellij.codeInsight.template.Result;
import com.intellij.codeInsight.template.TextResult;
import com.intellij.codeInsight.template.macro.MacroBase;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.impl.PerlFileElementImpl;
import com.perl5.lang.perl.util.PerlUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * This macro searches for the closest variable in the scope
 * and returns it's text as a result for the live template using it
 */
public class PerlClosestVariableMacro extends MacroBase {

    public PerlClosestVariableMacro() {
        super("closestVariable", "closestVariable()");
    }

    protected Result calculateResult(@NotNull Expression[] params, ExpressionContext context, boolean quick) {

        //find all vars in scope
        PsiElement currentElement =  context.getPsiElementAtStartOffset();
        String result = "";

        if(currentElement != null )
        {
            PsiFile currentFile = currentElement.getContainingFile();

            if( currentFile instanceof PerlFileElementImpl)
            {
                Collection<PerlVariable> variablesCollection = ((PerlFileElementImpl) currentFile).getVisibleLexicalVariables(currentElement);

                int position = context.getStartOffset();
                int closestDistance = -1;

                //find the closest variable and return it's text
                for (PerlVariable perlVariable : variablesCollection)
                {
                    int currentDistance = position - perlVariable.getTextOffset();
                    if (currentDistance < closestDistance || closestDistance == -1)
                    {
                        result = perlVariable.getText();
                        closestDistance = currentDistance;
                    }
                }
            }
        }
        return new TextResult(result);
    }
}
