/*
 * Copyright 2016 Alexandr Evstigneev
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

package parser;

/**
 * Created by hurricup on 28.02.2016.
 */
public class PerlParserTestPPIRegression extends PerlParserTestPPIBase
{
	private static final String myGroup = "08_regression";

	@Override
	protected String getTestsGroup()
	{
		return myGroup;
	}

	public void test01_rt_cpan_19629()
	{
		doTest("01_rt_cpan_19629");
	}

	public void test01_rt_cpan_19629b()
	{
		doTest("01_rt_cpan_19629b");
	}

	public void test02_rt_cpan_9582()
	{
		doTest("02_rt_cpan_9582");
	}

	public void test03_rt_cpan_9614()
	{
		doTest("03_rt_cpan_9614", false);
	}

	public void test04_tinderbox()
	{
		doTest("04_tinderbox");
	}

	public void test05_rt_cpan_13425()
	{
		doTest("05_rt_cpan_13425");
	}

	public void test06_partial_quote_double()
	{
		doTest("06_partial_quote_double", false);
	}

	public void test07_partial_quote_single()
	{
		doTest("07_partial_quote_single", false);
	}

	public void test08_partial_regex_substitution()
	{
		doTest("08_partial_regex_substitution", false);
	}

	public void test09_for_var()
	{
		doTest("09_for_var", false);
	}

	public void test10_leading_regexp()
	{
		doTest("10_leading_regexp");
	}

	public void test11_multiply_vs_glob_cast()
	{
		doTest("11_multiply_vs_glob_cast");
	}

	public void test12_pow()
	{
		doTest("12_pow");
	}

	public void test13_goto()
	{
		doTest("13_goto");
	}

	public void test14_minus()
	{
		doTest("14_minus");
	}

	public void test14b_minus()
	{
		doTest("14b_minus");
	}

	public void test15_dash_t()
	{
		doTest("15_dash_t");
	}

	public void test16_sub_declaration()
	{
		doTest("16_sub_declaration");
	}

	public void test17_scope()
	{
		doTest("17_scope");
	}

	public void test18_decimal_point()
	{
		doTest("18_decimal_point");
	}

	public void test19_long_operators()
	{
		doTest("19_long_operators");
	}

	public void test19_long_operators2()
	{
		doTest("19_long_operators2");
	}

	public void test20_hash_constructor()
	{
		doTest("20_hash_constructor");
	}

	public void test21_list_of_refs()
	{
		doTest("21_list_of_refs");
	}

	public void test22_hash_vs_brace()
	{
		doTest("22_hash_vs_brace");
	}

	public void test23_rt_cpan_8752()
	{
		doTest("23_rt_cpan_8752");
	}

	public void test24_compound()
	{
		doTest("24_compound");
	}

	public void test25_hash_block()
	{
		doTest("25_hash_block");
	}

	public void test26_rt_cpan_23253()
	{
		doTest("26_rt_cpan_23253");
	}

	public void test27_constant_hash()
	{
		doTest("27_constant_hash");
	}

	/*  See the https://github.com/hurricup/Perl5-IDEA/issues/864
		public void test28_backref_style_heredoc()
		{
			doTest("28_backref_style_heredoc");
		}
	*/
	public void test29_magic_carat()
	{
		doTest("29_magic_carat");
	}

	public void test30_hash_bang()
	{
		doTest("30_hash_bang");
	}

	public void test31_hash_carat_H()
	{
		doTest("31_hash_carat_H");
	}

	public void test32_readline()
	{
		doTest("32_readline");
	}

	public void test33_magic_carat_long()
	{
		doTest("33_magic_carat_long");
	}

	public void test34_attr_whitespace()
	{
		doTest("34_attr_whitespace");
	}

	public void test35_attr_perlsub()
	{
		doTest("35_attr_perlsub");
	}

	public void test36_begin_label()
	{
		doTest("36_begin_label");
	}

	public void test37_partial_prototype()
	{
		doTest("37_partial_prototype", false);
	}

	public void test38_multiply()
	{
		doTest("38_multiply");
	}

	public void test39_foreach_our()
	{
		doTest("39_foreach_our");
	}

	public void test40_foreach_eval()
	{
		doTest("40_foreach_eval");
	}

	public void test41_scalar_hash()
	{
		doTest("41_scalar_hash");
	}

	public void test42_numeric_package()
	{
		doTest("42_numeric_package");
	}

	public void test43_nonblock_map()
	{
		doTest("43_nonblock_map");
	}

/*
	public void test()
	{
		doTest("");
	}
*/

}
