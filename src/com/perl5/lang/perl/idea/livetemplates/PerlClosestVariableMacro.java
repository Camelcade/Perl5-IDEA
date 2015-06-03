package com.perl5.lang.perl.idea.livetemplates;

import com.intellij.codeInsight.template.Expression;
import com.intellij.codeInsight.template.ExpressionContext;
import com.intellij.codeInsight.template.Result;
import com.intellij.codeInsight.template.TextResult;
import com.intellij.codeInsight.template.macro.MacroBase;
import com.perl5.lang.perl.psi.PerlVariable;
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
        Collection<PerlVariable> perlVars = PerlUtil.findDeclaredLexicalVariables(context.getPsiElementAtStartOffset());
        int position = context.getStartOffset();
        int closestDistance = -1;
        String result = "";

        //find the closest variable and return it's text
        for (PerlVariable perlVar : perlVars) {
            int currentDistance = position - perlVar.getTextOffset();
            if (currentDistance < closestDistance || closestDistance == -1) {
                result = perlVar.getText();
                closestDistance = currentDistance;
            }
        }

        return new TextResult(result);
    }
}
