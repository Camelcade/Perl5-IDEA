package com.perl5.lang.pod.parser.parsing;

import com.intellij.lang.PsiBuilder;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.parser.PerlElementTypes;
import com.perl5.lang.perl.parser.parsing.util.ParserUtils;
import com.perl5.lang.pod.parser.PodParser;

/**
 * Created by hurricup on 21.04.2015.
 */
public class CompilationUnit implements PerlElementTypes
{

	public static void parseFile(PsiBuilder builder, PodParser parser) {

		//    ParserUtils.getToken(builder, mSH_COMMENT);
		//    ParserUtils.getToken(builder, mNLS);

		//  if (!PackageDefinition.parse(builder, parser)) {
		//      parser.parseStatementWithImports(builder);
		//    }

		while (!builder.eof()) {
//			if (!Separators.parse(builder)) {
//				builder.error(PerlBundle.message("separator.expected"));
//			}
//			if (builder.eof()) break;
			if (!parser.parseStatement(builder, false)) {
//				if (!parser.parseStatementWithImports(builder)) {
				//ParserUtils.wrapError(builder, PerlBundle.message("unexpected.symbol"));
				throw new Error("unexpected symbol");
			}
		}
	}



}
