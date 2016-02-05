// This is a generated file. Not intended for manual editing.
package com.perl5.lang.pod.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LightPsiParser;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;

import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import static com.perl5.lang.pod.lexer.PodElementTypes.*;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class PodParser implements PsiParser, LightPsiParser
{

	/* ********************************************************** */
	// [POD_TEXT] POD_NEWLINE
	public static boolean paragraph(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "paragraph")) return false;
		if (!nextTokenIs(b, "<paragraph>", POD_NEWLINE, POD_TEXT)) return false;
		boolean r;
		Marker m = enter_section_(b, l, _NONE_, "<paragraph>");
		r = paragraph_0(b, l + 1);
		r = r && consumeToken(b, POD_NEWLINE);
		exit_section_(b, l, m, PARAGRAPH, r, false, null);
		return r;
	}

	// [POD_TEXT]
	private static boolean paragraph_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "paragraph_0")) return false;
		consumeToken(b, POD_TEXT);
		return true;
	}

	/* ********************************************************** */
	// paragraph * section*
	static boolean pod_file(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "pod_file")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = pod_file_0(b, l + 1);
		r = r && pod_file_1(b, l + 1);
		exit_section_(b, m, null, r);
		return r;
	}

	// paragraph *
	private static boolean pod_file_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "pod_file_0")) return false;
		int c = current_position_(b);
		while (true)
		{
			if (!paragraph(b, l + 1)) break;
			if (!empty_element_parsed_guard_(b, "pod_file_0", c)) break;
			c = current_position_(b);
		}
		return true;
	}

	// section*
	private static boolean pod_file_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "pod_file_1")) return false;
		int c = current_position_(b);
		while (true)
		{
			if (!section(b, l + 1)) break;
			if (!empty_element_parsed_guard_(b, "pod_file_1", c)) break;
			c = current_position_(b);
		}
		return true;
	}

	/* ********************************************************** */
	// POD_TAG (paragraph | POD_CODE ) +
	public static boolean section(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "section")) return false;
		if (!nextTokenIs(b, POD_TAG)) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = consumeToken(b, POD_TAG);
		r = r && section_1(b, l + 1);
		exit_section_(b, m, SECTION, r);
		return r;
	}

	// (paragraph | POD_CODE ) +
	private static boolean section_1(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "section_1")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = section_1_0(b, l + 1);
		int c = current_position_(b);
		while (r)
		{
			if (!section_1_0(b, l + 1)) break;
			if (!empty_element_parsed_guard_(b, "section_1", c)) break;
			c = current_position_(b);
		}
		exit_section_(b, m, null, r);
		return r;
	}

	// paragraph | POD_CODE
	private static boolean section_1_0(PsiBuilder b, int l)
	{
		if (!recursion_guard_(b, l, "section_1_0")) return false;
		boolean r;
		Marker m = enter_section_(b);
		r = paragraph(b, l + 1);
		if (!r) r = consumeToken(b, POD_CODE);
		exit_section_(b, m, null, r);
		return r;
	}

	public ASTNode parse(IElementType t, PsiBuilder b)
	{
		parseLight(t, b);
		return b.getTreeBuilt();
	}

	public void parseLight(IElementType t, PsiBuilder b)
	{
		boolean r;
		b = adapt_builder_(t, b, this, null);
		Marker m = enter_section_(b, 0, _COLLAPSE_, null);
		if (t == PARAGRAPH)
		{
			r = paragraph(b, 0);
		}
		else if (t == SECTION)
		{
			r = section(b, 0);
		}
		else
		{
			r = parse_root_(t, b, 0);
		}
		exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
	}

	protected boolean parse_root_(IElementType t, PsiBuilder b, int l)
	{
		return pod_file(b, l + 1);
	}

}
