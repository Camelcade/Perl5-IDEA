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
public class PerlParserLibsTest extends PerlParserSubtestBase
{
	public static final String DATA_PATH = "testData/parser";

	@Override
	protected String getTestDataPath()
	{
		return DATA_PATH;
	}

	@Override
	protected String getTestsGroup()
	{
		return "perl5libs";
	}

	public void testAlgorithm_C3_pm()
	{
		doTest("Algorithm_C3_pm");
	}

	public void testAlgorithm_DiffOld_pm()
	{
		doTest("Algorithm_DiffOld_pm");
	}

	public void testAlgorithm_Diff_XS_pm()
	{
		doTest("Algorithm_Diff_XS_pm");
	}

	public void testAlgorithm_Diff_pm()
	{
		doTest("Algorithm_Diff_pm");
	}

	public void testAlgorithm_cdiff_pl()
	{
		doTest("Algorithm_cdiff_pl");
	}

	public void testAlgorithm_diff_pl()
	{
		doTest("Algorithm_diff_pl");
	}

	public void testAlgorithm_diffnew_pl()
	{
		doTest("Algorithm_diffnew_pl");
	}

	public void testAlgorithm_htmldiff_pl()
	{
		doTest("Algorithm_htmldiff_pl");
	}

	public void testAlien_Tidyp_ConfigData_pm()
	{
		doTest("Alien_Tidyp_ConfigData_pm");
	}

	public void testAlien_Tidyp_pm()
	{
		doTest("Alien_Tidyp_pm");
	}

	public void testAlt_Crypt_RSA_BigInt_pm()
	{
		doTest("Alt_Crypt_RSA_BigInt_pm");
	}

	public void testAnyDBM_File_pm()
	{
		doTest("AnyDBM_File_pm");
	}

	public void testApache_SOAP_pm()
	{
		doTest("Apache_SOAP_pm");
	}

	public void testAppConfig_Args_pm()
	{
		doTest("AppConfig_Args_pm");
	}

	public void testAppConfig_CGI_pm()
	{
		doTest("AppConfig_CGI_pm");
	}

	public void testAppConfig_File_pm()
	{
		doTest("AppConfig_File_pm");
	}

	public void testAppConfig_Getopt_pm()
	{
		doTest("AppConfig_Getopt_pm");
	}

	public void testAppConfig_State_pm()
	{
		doTest("AppConfig_State_pm");
	}

	public void testAppConfig_Sys_pm()
	{
		doTest("AppConfig_Sys_pm");
	}

	public void testAppConfig_pm()
	{
		doTest("AppConfig_pm");
	}

	public void testApp_Cmd_ArgProcessor_pm()
	{
		doTest("App_Cmd_ArgProcessor_pm");
	}

	public void testApp_Cmd_Command_commands_pm()
	{
		doTest("App_Cmd_Command_commands_pm");
	}

	public void testApp_Cmd_Command_help_pm()
	{
		doTest("App_Cmd_Command_help_pm");
	}

	public void testApp_Cmd_Command_pm()
	{
		doTest("App_Cmd_Command_pm");
	}

	public void testApp_Cmd_Command_version_pm()
	{
		doTest("App_Cmd_Command_version_pm");
	}

	public void testApp_Cmd_Plugin_pm()
	{
		doTest("App_Cmd_Plugin_pm");
	}

	public void testApp_Cmd_Setup_pm()
	{
		doTest("App_Cmd_Setup_pm");
	}

	public void testApp_Cmd_Simple_pm()
	{
		doTest("App_Cmd_Simple_pm");
	}

	public void testApp_Cmd_Subdispatch_DashedStyle_pm()
	{
		doTest("App_Cmd_Subdispatch_DashedStyle_pm");
	}

	public void testApp_Cmd_Subdispatch_pm()
	{
		doTest("App_Cmd_Subdispatch_pm");
	}

	public void testApp_Cmd_Tester_CaptureExternal_pm()
	{
		doTest("App_Cmd_Tester_CaptureExternal_pm");
	}

	public void testApp_Cmd_Tester_pm()
	{
		doTest("App_Cmd_Tester_pm");
	}

	public void testApp_Cmd_pm()
	{
		doTest("App_Cmd_pm");
	}

	public void testApp_Cpan_pm()
	{
		doTest("App_Cpan_pm");
	}

	public void testApp_Prove_State_Result_Test_pm()
	{
		doTest("App_Prove_State_Result_Test_pm");
	}

	public void testApp_Prove_State_Result_pm()
	{
		doTest("App_Prove_State_Result_pm");
	}

	public void testApp_Prove_State_pm()
	{
		doTest("App_Prove_State_pm");
	}

	public void testApp_Prove_pm()
	{
		doTest("App_Prove_pm");
	}

	public void testApp_cpanminus_fatscript_pm()
	{
		doTest("App_cpanminus_fatscript_pm");
	}

	public void testApp_cpanminus_pm()
	{
		doTest("App_cpanminus_pm");
	}

	public void testApp_cpanoutdated_pm()
	{
		doTest("App_cpanoutdated_pm");
	}

	public void testApp_local_lib_Win32Helper_pm()
	{
		doTest("App_local_lib_Win32Helper_pm");
	}

	public void testApp_module_version_pm()
	{
		doTest("App_module_version_pm");
	}

	public void testApp_pmuninstall_pm()
	{
		doTest("App_pmuninstall_pm");
	}

	public void testArchive_Any_Lite_pm()
	{
		doTest("Archive_Any_Lite_pm");
	}

	public void testArchive_Extract_pm()
	{
		doTest("Archive_Extract_pm");
	}

	public void testArchive_Tar_Constant_pm()
	{
		doTest("Archive_Tar_Constant_pm");
	}

	public void testArchive_Tar_File_pm()
	{
		doTest("Archive_Tar_File_pm");
	}

	public void testArchive_Tar_pm()
	{
		doTest("Archive_Tar_pm");
	}

	public void testArchive_Zip_Archive_pm()
	{
		doTest("Archive_Zip_Archive_pm");
	}

	public void testArchive_Zip_BufferedFileHandle_pm()
	{
		doTest("Archive_Zip_BufferedFileHandle_pm");
	}

	public void testArchive_Zip_DirectoryMember_pm()
	{
		doTest("Archive_Zip_DirectoryMember_pm");
	}

	public void testArchive_Zip_FileMember_pm()
	{
		doTest("Archive_Zip_FileMember_pm");
	}

	public void testArchive_Zip_MemberRead_pm()
	{
		doTest("Archive_Zip_MemberRead_pm");
	}

	public void testArchive_Zip_Member_pm()
	{
		doTest("Archive_Zip_Member_pm");
	}

	public void testArchive_Zip_MockFileHandle_pm()
	{
		doTest("Archive_Zip_MockFileHandle_pm");
	}

	public void testArchive_Zip_NewFileMember_pm()
	{
		doTest("Archive_Zip_NewFileMember_pm");
	}

	public void testArchive_Zip_StringMember_pm()
	{
		doTest("Archive_Zip_StringMember_pm");
	}

	public void testArchive_Zip_Tree_pm()
	{
		doTest("Archive_Zip_Tree_pm");
	}

	public void testArchive_Zip_ZipFileMember_pm()
	{
		doTest("Archive_Zip_ZipFileMember_pm");
	}

	public void testArchive_Zip_pm()
	{
		doTest("Archive_Zip_pm");
	}

	public void testArray_Diff_pm()
	{
		doTest("Array_Diff_pm");
	}

	public void testAttribute_Handlers_pm()
	{
		doTest("Attribute_Handlers_pm");
	}

	public void testAttribute_Params_Validate_pm()
	{
		doTest("Attribute_Params_Validate_pm");
	}

	public void testAuthen_SASL_CRAM_MD5_pm()
	{
		doTest("Authen_SASL_CRAM_MD5_pm");
	}

	public void testAuthen_SASL_EXTERNAL_pm()
	{
		doTest("Authen_SASL_EXTERNAL_pm");
	}

	public void testAuthen_SASL_Perl_ANONYMOUS_pm()
	{
		doTest("Authen_SASL_Perl_ANONYMOUS_pm");
	}

	public void testAuthen_SASL_Perl_CRAM_MD5_pm()
	{
		doTest("Authen_SASL_Perl_CRAM_MD5_pm");
	}

	public void testAuthen_SASL_Perl_DIGEST_MD5_pm()
	{
		doTest("Authen_SASL_Perl_DIGEST_MD5_pm");
	}

	public void testAuthen_SASL_Perl_EXTERNAL_pm()
	{
		doTest("Authen_SASL_Perl_EXTERNAL_pm");
	}

	public void testAuthen_SASL_Perl_GSSAPI_pm()
	{
		doTest("Authen_SASL_Perl_GSSAPI_pm");
	}

	public void testAuthen_SASL_Perl_LOGIN_pm()
	{
		doTest("Authen_SASL_Perl_LOGIN_pm");
	}

	public void testAuthen_SASL_Perl_PLAIN_pm()
	{
		doTest("Authen_SASL_Perl_PLAIN_pm");
	}

	public void testAuthen_SASL_Perl_pm()
	{
		doTest("Authen_SASL_Perl_pm");
	}

	public void testAuthen_SASL_pm()
	{
		doTest("Authen_SASL_pm");
	}

	public void testAutoLoader_pm()
	{
		doTest("AutoLoader_pm");
	}

	public void testAutoSplit_pm()
	{
		doTest("AutoSplit_pm");
	}

	public void testB_Concise_pm()
	{
		doTest("B_Concise_pm");
	}

	public void testB_Debug_pm()
	{
		doTest("B_Debug_pm");
	}

	public void testB_Deparse_pm()
	{
		doTest("B_Deparse_pm");
	}

	public void testB_Hooks_EndOfScope_PP_FieldHash_pm()
	{
		doTest("B_Hooks_EndOfScope_PP_FieldHash_pm");
	}

	public void testB_Hooks_EndOfScope_PP_HintHash_pm()
	{
		doTest("B_Hooks_EndOfScope_PP_HintHash_pm");
	}

	public void testB_Hooks_EndOfScope_PP_pm()
	{
		doTest("B_Hooks_EndOfScope_PP_pm");
	}

	public void testB_Hooks_EndOfScope_XS_pm()
	{
		doTest("B_Hooks_EndOfScope_XS_pm");
	}

	public void testB_Hooks_EndOfScope_pm()
	{
		doTest("B_Hooks_EndOfScope_pm");
	}

	public void testB_Hooks_OP_Check_Install_Files_pm()
	{
		doTest("B_Hooks_OP_Check_Install_Files_pm");
	}

	public void testB_Hooks_OP_Check_pm()
	{
		doTest("B_Hooks_OP_Check_pm");
	}

	public void testB_Lint_Debug_pm()
	{
		doTest("B_Lint_Debug_pm");
	}

	public void testB_Lint_pm()
	{
		doTest("B_Lint_pm");
	}

	public void testB_Op_private_pm()
	{
		doTest("B_Op_private_pm");
	}

	public void testB_Showlex_pm()
	{
		doTest("B_Showlex_pm");
	}

	public void testB_Terse_pm()
	{
		doTest("B_Terse_pm");
	}

	public void testB_Utils_Install_Files_pm()
	{
		doTest("B_Utils_Install_Files_pm");
	}

	public void testB_Utils_OP_pm()
	{
		doTest("B_Utils_OP_pm");
	}

	public void testB_Utils_pm()
	{
		doTest("B_Utils_pm");
	}

	public void testB_Xref_pm()
	{
		doTest("B_Xref_pm");
	}

	public void testB_pm()
	{
		doTest("B_pm");
	}

	public void testBenchmark_pm()
	{
		doTest("Benchmark_pm");
	}

	public void testBerkeleyDB_Btree_pm()
	{
		doTest("BerkeleyDB_Btree_pm");
	}

	public void testBerkeleyDB_Hash_pm()
	{
		doTest("BerkeleyDB_Hash_pm");
	}

	public void testBerkeleyDB_pm()
	{
		doTest("BerkeleyDB_pm");
	}

	public void testBundle_DBD_CSV_pm()
	{
		doTest("Bundle_DBD_CSV_pm");
	}

	public void testBundle_DBD_Pg_pm()
	{
		doTest("Bundle_DBD_Pg_pm");
	}

	public void testBundle_DBD_mysql_pm()
	{
		doTest("Bundle_DBD_mysql_pm");
	}

	public void testBundle_DBI_pm()
	{
		doTest("Bundle_DBI_pm");
	}

	public void testBytes_Random_Secure_pm()
	{
		doTest("Bytes_Random_Secure_pm");
	}

	public void testCGI_Carp_pm()
	{
		doTest("CGI_Carp_pm");
	}

	public void testCGI_Cookie_pm()
	{
		doTest("CGI_Cookie_pm");
	}

	public void testCGI_File_Temp_pm()
	{
		doTest("CGI_File_Temp_pm");
	}

	public void testCGI_HTML_Functions_pm()
	{
		doTest("CGI_HTML_Functions_pm");
	}

	public void testCGI_Pretty_pm()
	{
		doTest("CGI_Pretty_pm");
	}

	public void testCGI_Push_pm()
	{
		doTest("CGI_Push_pm");
	}

	public void testCGI_Util_pm()
	{
		doTest("CGI_Util_pm");
	}

	public void testCGI_pm()
	{
		doTest("CGI_pm");
	}

	public void testCPANPLUS_Backend_RV_pm()
	{
		doTest("CPANPLUS_Backend_RV_pm");
	}

	public void testCPANPLUS_Backend_pm()
	{
		doTest("CPANPLUS_Backend_pm");
	}

	public void testCPANPLUS_Config_HomeEnv_pm()
	{
		doTest("CPANPLUS_Config_HomeEnv_pm");
	}

	public void testCPANPLUS_Config_pm()
	{
		doTest("CPANPLUS_Config_pm");
	}

	public void testCPANPLUS_Configure_Setup_pm()
	{
		doTest("CPANPLUS_Configure_Setup_pm");
	}

	public void testCPANPLUS_Configure_pm()
	{
		doTest("CPANPLUS_Configure_pm");
	}

	public void testCPANPLUS_Dist_Autobundle_pm()
	{
		doTest("CPANPLUS_Dist_Autobundle_pm");
	}

	public void testCPANPLUS_Dist_Base_pm()
	{
		doTest("CPANPLUS_Dist_Base_pm");
	}

	public void testCPANPLUS_Dist_Build_Constants_pm()
	{
		doTest("CPANPLUS_Dist_Build_Constants_pm");
	}

	public void testCPANPLUS_Dist_Build_pm()
	{
		doTest("CPANPLUS_Dist_Build_pm");
	}

	public void testCPANPLUS_Dist_MM_pm()
	{
		doTest("CPANPLUS_Dist_MM_pm");
	}

	public void testCPANPLUS_Dist_Sample_pm()
	{
		doTest("CPANPLUS_Dist_Sample_pm");
	}

	public void testCPANPLUS_Dist_pm()
	{
		doTest("CPANPLUS_Dist_pm");
	}

	public void testCPANPLUS_Error_pm()
	{
		doTest("CPANPLUS_Error_pm");
	}

	public void testCPANPLUS_Internals_Constants_Report_pm()
	{
		doTest("CPANPLUS_Internals_Constants_Report_pm");
	}

	public void testCPANPLUS_Internals_Constants_pm()
	{
		doTest("CPANPLUS_Internals_Constants_pm");
	}

	public void testCPANPLUS_Internals_Extract_pm()
	{
		doTest("CPANPLUS_Internals_Extract_pm");
	}

	public void testCPANPLUS_Internals_Fetch_pm()
	{
		doTest("CPANPLUS_Internals_Fetch_pm");
	}

	public void testCPANPLUS_Internals_Report_pm()
	{
		doTest("CPANPLUS_Internals_Report_pm");
	}

	public void testCPANPLUS_Internals_Search_pm()
	{
		doTest("CPANPLUS_Internals_Search_pm");
	}

	public void testCPANPLUS_Internals_Source_Memory_pm()
	{
		doTest("CPANPLUS_Internals_Source_Memory_pm");
	}

	public void testCPANPLUS_Internals_Source_SQLite_Tie_pm()
	{
		doTest("CPANPLUS_Internals_Source_SQLite_Tie_pm");
	}

	public void testCPANPLUS_Internals_Source_SQLite_pm()
	{
		doTest("CPANPLUS_Internals_Source_SQLite_pm");
	}

	public void testCPANPLUS_Internals_Source_pm()
	{
		doTest("CPANPLUS_Internals_Source_pm");
	}

	public void testCPANPLUS_Internals_Utils_Autoflush_pm()
	{
		doTest("CPANPLUS_Internals_Utils_Autoflush_pm");
	}

	public void testCPANPLUS_Internals_Utils_pm()
	{
		doTest("CPANPLUS_Internals_Utils_pm");
	}

	public void testCPANPLUS_Internals_pm()
	{
		doTest("CPANPLUS_Internals_pm");
	}

	public void testCPANPLUS_Module_Author_Fake_pm()
	{
		doTest("CPANPLUS_Module_Author_Fake_pm");
	}

	public void testCPANPLUS_Module_Author_pm()
	{
		doTest("CPANPLUS_Module_Author_pm");
	}

	public void testCPANPLUS_Module_Checksums_pm()
	{
		doTest("CPANPLUS_Module_Checksums_pm");
	}

	public void testCPANPLUS_Module_Fake_pm()
	{
		doTest("CPANPLUS_Module_Fake_pm");
	}

	public void testCPANPLUS_Module_Signature_pm()
	{
		doTest("CPANPLUS_Module_Signature_pm");
	}

	public void testCPANPLUS_Module_pm()
	{
		doTest("CPANPLUS_Module_pm");
	}

	public void testCPANPLUS_Selfupdate_pm()
	{
		doTest("CPANPLUS_Selfupdate_pm");
	}

	public void testCPANPLUS_Shell_Classic_pm()
	{
		doTest("CPANPLUS_Shell_Classic_pm");
	}

	public void testCPANPLUS_Shell_Default_Plugins_CustomSource_pm()
	{
		doTest("CPANPLUS_Shell_Default_Plugins_CustomSource_pm");
	}

	public void testCPANPLUS_Shell_Default_Plugins_Remote_pm()
	{
		doTest("CPANPLUS_Shell_Default_Plugins_Remote_pm");
	}

	public void testCPANPLUS_Shell_Default_Plugins_Source_pm()
	{
		doTest("CPANPLUS_Shell_Default_Plugins_Source_pm");
	}

	public void testCPANPLUS_Shell_Default_pm()
	{
		doTest("CPANPLUS_Shell_Default_pm");
	}

	public void testCPANPLUS_Shell_pm()
	{
		doTest("CPANPLUS_Shell_pm");
	}

	public void testCPANPLUS_pm()
	{
		doTest("CPANPLUS_pm");
	}

	public void testCPAN_Author_pm()
	{
		doTest("CPAN_Author_pm");
	}

	public void testCPAN_Bundle_pm()
	{
		doTest("CPAN_Bundle_pm");
	}

	public void testCPAN_CacheMgr_pm()
	{
		doTest("CPAN_CacheMgr_pm");
	}

	public void testCPAN_Complete_pm()
	{
		doTest("CPAN_Complete_pm");
	}

	public void testCPAN_Config_pm()
	{
		doTest("CPAN_Config_pm");
	}

	public void testCPAN_Debug_pm()
	{
		doTest("CPAN_Debug_pm");
	}

	public void testCPAN_DeferredCode_pm()
	{
		doTest("CPAN_DeferredCode_pm");
	}

	public void testCPAN_DistnameInfo_pm()
	{
		doTest("CPAN_DistnameInfo_pm");
	}

	public void testCPAN_Distribution_pm()
	{
		doTest("CPAN_Distribution_pm");
	}

	public void testCPAN_Distroprefs_pm()
	{
		doTest("CPAN_Distroprefs_pm");
	}

	public void testCPAN_Distrostatus_pm()
	{
		doTest("CPAN_Distrostatus_pm");
	}

	public void testCPAN_Exception_RecursiveDependency_pm()
	{
		doTest("CPAN_Exception_RecursiveDependency_pm");
	}

	public void testCPAN_Exception_blocked_urllist_pm()
	{
		doTest("CPAN_Exception_blocked_urllist_pm");
	}

	public void testCPAN_Exception_yaml_not_installed_pm()
	{
		doTest("CPAN_Exception_yaml_not_installed_pm");
	}

	public void testCPAN_Exception_yaml_process_error_pm()
	{
		doTest("CPAN_Exception_yaml_process_error_pm");
	}

	public void testCPAN_FTP_netrc_pm()
	{
		doTest("CPAN_FTP_netrc_pm");
	}

	public void testCPAN_FTP_pm()
	{
		doTest("CPAN_FTP_pm");
	}

	public void testCPAN_FirstTime_pm()
	{
		doTest("CPAN_FirstTime_pm");
	}

	public void testCPAN_HTTP_Client_pm()
	{
		doTest("CPAN_HTTP_Client_pm");
	}

	public void testCPAN_HTTP_Credentials_pm()
	{
		doTest("CPAN_HTTP_Credentials_pm");
	}

	public void testCPAN_HandleConfig_pm()
	{
		doTest("CPAN_HandleConfig_pm");
	}

	public void testCPAN_Index_pm()
	{
		doTest("CPAN_Index_pm");
	}

	public void testCPAN_InfoObj_pm()
	{
		doTest("CPAN_InfoObj_pm");
	}

	public void testCPAN_Kwalify_pm()
	{
		doTest("CPAN_Kwalify_pm");
	}

	public void testCPAN_LWP_UserAgent_pm()
	{
		doTest("CPAN_LWP_UserAgent_pm");
	}

	public void testCPAN_Meta_Check_pm()
	{
		doTest("CPAN_Meta_Check_pm");
	}

	public void testCPAN_Meta_Converter_pm()
	{
		doTest("CPAN_Meta_Converter_pm");
	}

	public void testCPAN_Meta_Feature_pm()
	{
		doTest("CPAN_Meta_Feature_pm");
	}

	public void testCPAN_Meta_History_pm()
	{
		doTest("CPAN_Meta_History_pm");
	}

	public void testCPAN_Meta_Merge_pm()
	{
		doTest("CPAN_Meta_Merge_pm");
	}

	public void testCPAN_Meta_Prereqs_pm()
	{
		doTest("CPAN_Meta_Prereqs_pm");
	}

	public void testCPAN_Meta_Requirements_pm()
	{
		doTest("CPAN_Meta_Requirements_pm");
	}

	public void testCPAN_Meta_Spec_pm()
	{
		doTest("CPAN_Meta_Spec_pm");
	}

	public void testCPAN_Meta_Validator_pm()
	{
		doTest("CPAN_Meta_Validator_pm");
	}

	public void testCPAN_Meta_YAML_pm()
	{
		doTest("CPAN_Meta_YAML_pm");
	}

	public void testCPAN_Meta_pm()
	{
		doTest("CPAN_Meta_pm");
	}

	public void testCPAN_Mirrors_pm()
	{
		doTest("CPAN_Mirrors_pm");
	}

	public void testCPAN_Module_pm()
	{
		doTest("CPAN_Module_pm");
	}

	public void testCPAN_Nox_pm()
	{
		doTest("CPAN_Nox_pm");
	}

	public void testCPAN_Plugin_Specfile_pm()
	{
		doTest("CPAN_Plugin_Specfile_pm");
	}

	public void testCPAN_Plugin_pm()
	{
		doTest("CPAN_Plugin_pm");
	}

	public void testCPAN_Prompt_pm()
	{
		doTest("CPAN_Prompt_pm");
	}

	public void testCPAN_Queue_pm()
	{
		doTest("CPAN_Queue_pm");
	}

	public void testCPAN_SQLite_DBI_Index_pm()
	{
		doTest("CPAN_SQLite_DBI_Index_pm");
	}

	public void testCPAN_SQLite_DBI_Search_pm()
	{
		doTest("CPAN_SQLite_DBI_Search_pm");
	}

	public void testCPAN_SQLite_DBI_pm()
	{
		doTest("CPAN_SQLite_DBI_pm");
	}

	public void testCPAN_SQLite_Index_pm()
	{
		doTest("CPAN_SQLite_Index_pm");
	}

	public void testCPAN_SQLite_Info_pm()
	{
		doTest("CPAN_SQLite_Info_pm");
	}

	public void testCPAN_SQLite_META_pm()
	{
		doTest("CPAN_SQLite_META_pm");
	}

	public void testCPAN_SQLite_Populate_pm()
	{
		doTest("CPAN_SQLite_Populate_pm");
	}

	public void testCPAN_SQLite_Search_pm()
	{
		doTest("CPAN_SQLite_Search_pm");
	}

	public void testCPAN_SQLite_State_pm()
	{
		doTest("CPAN_SQLite_State_pm");
	}

	public void testCPAN_SQLite_Util_pm()
	{
		doTest("CPAN_SQLite_Util_pm");
	}

	public void testCPAN_SQLite_pm()
	{
		doTest("CPAN_SQLite_pm");
	}

	public void testCPAN_Shell_pm()
	{
		doTest("CPAN_Shell_pm");
	}

	public void testCPAN_Tarzip_pm()
	{
		doTest("CPAN_Tarzip_pm");
	}

	public void testCPAN_URL_pm()
	{
		doTest("CPAN_URL_pm");
	}

	public void testCPAN_Uploader_pm()
	{
		doTest("CPAN_Uploader_pm");
	}

	public void testCPAN_Version_pm()
	{
		doTest("CPAN_Version_pm");
	}

	public void testCPAN_pm()
	{
		doTest("CPAN_pm");
	}

	public void testCache_BaseCacheTester_pm()
	{
		doTest("Cache_BaseCacheTester_pm");
	}

	public void testCache_BaseCache_pm()
	{
		doTest("Cache_BaseCache_pm");
	}

	public void testCache_CacheMetaData_pm()
	{
		doTest("Cache_CacheMetaData_pm");
	}

	public void testCache_CacheSizer_pm()
	{
		doTest("Cache_CacheSizer_pm");
	}

	public void testCache_CacheTester_pm()
	{
		doTest("Cache_CacheTester_pm");
	}

	public void testCache_CacheUtils_pm()
	{
		doTest("Cache_CacheUtils_pm");
	}

	public void testCache_Cache_pm()
	{
		doTest("Cache_Cache_pm");
	}

	public void testCache_FileBackend_pm()
	{
		doTest("Cache_FileBackend_pm");
	}

	public void testCache_FileCache_pm()
	{
		doTest("Cache_FileCache_pm");
	}

	public void testCache_MemoryBackend_pm()
	{
		doTest("Cache_MemoryBackend_pm");
	}

	public void testCache_MemoryCache_pm()
	{
		doTest("Cache_MemoryCache_pm");
	}

	public void testCache_NullCache_pm()
	{
		doTest("Cache_NullCache_pm");
	}

	public void testCache_Object_pm()
	{
		doTest("Cache_Object_pm");
	}

	public void testCache_SharedMemoryBackend_pm()
	{
		doTest("Cache_SharedMemoryBackend_pm");
	}

	public void testCache_SharedMemoryCache_pm()
	{
		doTest("Cache_SharedMemoryCache_pm");
	}

	public void testCache_SizeAwareCacheTester_pm()
	{
		doTest("Cache_SizeAwareCacheTester_pm");
	}

	public void testCache_SizeAwareCache_pm()
	{
		doTest("Cache_SizeAwareCache_pm");
	}

	public void testCache_SizeAwareFileCache_pm()
	{
		doTest("Cache_SizeAwareFileCache_pm");
	}

	public void testCache_SizeAwareMemoryCache_pm()
	{
		doTest("Cache_SizeAwareMemoryCache_pm");
	}

	public void testCache_SizeAwareSharedMemoryCache_pm()
	{
		doTest("Cache_SizeAwareSharedMemoryCache_pm");
	}

	public void testCapture_Tiny_pm()
	{
		doTest("Capture_Tiny_pm");
	}

	public void testCarp_Always_pm()
	{
		doTest("Carp_Always_pm");
	}

	public void testCarp_Clan_pm()
	{
		doTest("Carp_Clan_pm");
	}

	public void testCarp_Heavy_pm()
	{
		doTest("Carp_Heavy_pm");
	}

	public void testCarp_pm()
	{
		doTest("Carp_pm");
	}

	public void testClass_Accessor_Chained_Fast_pm()
	{
		doTest("Class_Accessor_Chained_Fast_pm");
	}

	public void testClass_Accessor_Chained_pm()
	{
		doTest("Class_Accessor_Chained_pm");
	}

	public void testClass_Accessor_Fast_pm()
	{
		doTest("Class_Accessor_Fast_pm");
	}

	public void testClass_Accessor_Faster_pm()
	{
		doTest("Class_Accessor_Faster_pm");
	}

	public void testClass_Accessor_Grouped_pm()
	{
		doTest("Class_Accessor_Grouped_pm");
	}

	public void testClass_Accessor_Lite_pm()
	{
		doTest("Class_Accessor_Lite_pm");
	}

	public void testClass_Accessor_pm()
	{
		doTest("Class_Accessor_pm");
	}

	public void testClass_C3_Componentised_ApplyHooks_pm()
	{
		doTest("Class_C3_Componentised_ApplyHooks_pm");
	}

	public void testClass_C3_Componentised_pm()
	{
		doTest("Class_C3_Componentised_pm");
	}

	public void testClass_C3_next_pm()
	{
		doTest("Class_C3_next_pm");
	}

	public void testClass_C3_pm()
	{
		doTest("Class_C3_pm");
	}

	public void testClass_Container_pm()
	{
		doTest("Class_Container_pm");
	}

	public void testClass_Data_Inheritable_pm()
	{
		doTest("Class_Data_Inheritable_pm");
	}

	public void testClass_ErrorHandler_pm()
	{
		doTest("Class_ErrorHandler_pm");
	}

	public void testClass_Inspector_Functions_pm()
	{
		doTest("Class_Inspector_Functions_pm");
	}

	public void testClass_Inspector_pm()
	{
		doTest("Class_Inspector_pm");
	}

	public void testClass_Load_PP_pm()
	{
		doTest("Class_Load_PP_pm");
	}

	public void testClass_Load_XS_pm()
	{
		doTest("Class_Load_XS_pm");
	}

	public void testClass_Load_pm()
	{
		doTest("Class_Load_pm");
	}

	public void testClass_LoaderTest_pm()
	{
		doTest("Class_LoaderTest_pm");
	}

	public void testClass_Loader_pm()
	{
		doTest("Class_Loader_pm");
	}

	public void testClass_MOP_Attribute_pm()
	{
		doTest("Class_MOP_Attribute_pm");
	}

	public void testClass_MOP_Class_Immutable_Trait_pm()
	{
		doTest("Class_MOP_Class_Immutable_Trait_pm");
	}

	public void testClass_MOP_Class_pm()
	{
		doTest("Class_MOP_Class_pm");
	}

	public void testClass_MOP_Deprecated_pm()
	{
		doTest("Class_MOP_Deprecated_pm");
	}

	public void testClass_MOP_Instance_pm()
	{
		doTest("Class_MOP_Instance_pm");
	}

	public void testClass_MOP_Method_Accessor_pm()
	{
		doTest("Class_MOP_Method_Accessor_pm");
	}

	public void testClass_MOP_Method_Constructor_pm()
	{
		doTest("Class_MOP_Method_Constructor_pm");
	}

	public void testClass_MOP_Method_Generated_pm()
	{
		doTest("Class_MOP_Method_Generated_pm");
	}

	public void testClass_MOP_Method_Inlined_pm()
	{
		doTest("Class_MOP_Method_Inlined_pm");
	}

	public void testClass_MOP_Method_Meta_pm()
	{
		doTest("Class_MOP_Method_Meta_pm");
	}

	public void testClass_MOP_Method_Wrapped_pm()
	{
		doTest("Class_MOP_Method_Wrapped_pm");
	}

	public void testClass_MOP_Method_pm()
	{
		doTest("Class_MOP_Method_pm");
	}

	public void testClass_MOP_MiniTrait_pm()
	{
		doTest("Class_MOP_MiniTrait_pm");
	}

	public void testClass_MOP_Mixin_AttributeCore_pm()
	{
		doTest("Class_MOP_Mixin_AttributeCore_pm");
	}

	public void testClass_MOP_Mixin_HasAttributes_pm()
	{
		doTest("Class_MOP_Mixin_HasAttributes_pm");
	}

	public void testClass_MOP_Mixin_HasMethods_pm()
	{
		doTest("Class_MOP_Mixin_HasMethods_pm");
	}

	public void testClass_MOP_Mixin_HasOverloads_pm()
	{
		doTest("Class_MOP_Mixin_HasOverloads_pm");
	}

	public void testClass_MOP_Mixin_pm()
	{
		doTest("Class_MOP_Mixin_pm");
	}

	public void testClass_MOP_Module_pm()
	{
		doTest("Class_MOP_Module_pm");
	}

	public void testClass_MOP_Object_pm()
	{
		doTest("Class_MOP_Object_pm");
	}

	public void testClass_MOP_Overload_pm()
	{
		doTest("Class_MOP_Overload_pm");
	}

	public void testClass_MOP_Package_pm()
	{
		doTest("Class_MOP_Package_pm");
	}

	public void testClass_MOP_pm()
	{
		doTest("Class_MOP_pm");
	}

	public void testClass_Method_Modifiers_pm()
	{
		doTest("Class_Method_Modifiers_pm");
	}

	public void testClass_Singleton_pm()
	{
		doTest("Class_Singleton_pm");
	}

	public void testClass_Struct_pm()
	{
		doTest("Class_Struct_pm");
	}

	public void testClass_Tiny_pm()
	{
		doTest("Class_Tiny_pm");
	}

	public void testClass_Unload_pm()
	{
		doTest("Class_Unload_pm");
	}

	public void testClass_XSAccessor_Array_pm()
	{
		doTest("Class_XSAccessor_Array_pm");
	}

	public void testClass_XSAccessor_Heavy_pm()
	{
		doTest("Class_XSAccessor_Heavy_pm");
	}

	public void testClass_XSAccessor_pm()
	{
		doTest("Class_XSAccessor_pm");
	}

	public void testClone_PP_pm()
	{
		doTest("Clone_PP_pm");
	}

	public void testClone_pm()
	{
		doTest("Clone_pm");
	}

	public void testCompress_Raw_Bzip2_pm()
	{
		doTest("Compress_Raw_Bzip2_pm");
	}

	public void testCompress_Raw_Lzma_pm()
	{
		doTest("Compress_Raw_Lzma_pm");
	}

	public void testCompress_Raw_Zlib_pm()
	{
		doTest("Compress_Raw_Zlib_pm");
	}

	public void testCompress_Zlib_pm()
	{
		doTest("Compress_Zlib_pm");
	}

	public void testCompress_unLZMA_pm()
	{
		doTest("Compress_unLZMA_pm");
	}

	public void testConfig_Any_Base_pm()
	{
		doTest("Config_Any_Base_pm");
	}

	public void testConfig_Any_General_pm()
	{
		doTest("Config_Any_General_pm");
	}

	public void testConfig_Any_INI_pm()
	{
		doTest("Config_Any_INI_pm");
	}

	public void testConfig_Any_JSON_pm()
	{
		doTest("Config_Any_JSON_pm");
	}

	public void testConfig_Any_Perl_pm()
	{
		doTest("Config_Any_Perl_pm");
	}

	public void testConfig_Any_XML_pm()
	{
		doTest("Config_Any_XML_pm");
	}

	public void testConfig_Any_YAML_pm()
	{
		doTest("Config_Any_YAML_pm");
	}

	public void testConfig_Any_pm()
	{
		doTest("Config_Any_pm");
	}

	public void testConfig_Extensions_pm()
	{
		doTest("Config_Extensions_pm");
	}

	public void testConfig_INI_Reader_pm()
	{
		doTest("Config_INI_Reader_pm");
	}

	public void testConfig_INI_Writer_pm()
	{
		doTest("Config_INI_Writer_pm");
	}

	public void testConfig_INI_pm()
	{
		doTest("Config_INI_pm");
	}

	public void testConfig_MVP_Assembler_WithBundles_pm()
	{
		doTest("Config_MVP_Assembler_WithBundles_pm");
	}

	public void testConfig_MVP_Assembler_pm()
	{
		doTest("Config_MVP_Assembler_pm");
	}

	public void testConfig_MVP_Error_pm()
	{
		doTest("Config_MVP_Error_pm");
	}

	public void testConfig_MVP_Reader_Findable_ByExtension_pm()
	{
		doTest("Config_MVP_Reader_Findable_ByExtension_pm");
	}

	public void testConfig_MVP_Reader_Findable_pm()
	{
		doTest("Config_MVP_Reader_Findable_pm");
	}

	public void testConfig_MVP_Reader_Finder_pm()
	{
		doTest("Config_MVP_Reader_Finder_pm");
	}

	public void testConfig_MVP_Reader_Hash_pm()
	{
		doTest("Config_MVP_Reader_Hash_pm");
	}

	public void testConfig_MVP_Reader_INI_pm()
	{
		doTest("Config_MVP_Reader_INI_pm");
	}

	public void testConfig_MVP_Reader_pm()
	{
		doTest("Config_MVP_Reader_pm");
	}

	public void testConfig_MVP_Section_pm()
	{
		doTest("Config_MVP_Section_pm");
	}

	public void testConfig_MVP_Sequence_pm()
	{
		doTest("Config_MVP_Sequence_pm");
	}

	public void testConfig_MVP_pm()
	{
		doTest("Config_MVP_pm");
	}

	public void testConfig_Perl_V_pm()
	{
		doTest("Config_Perl_V_pm");
	}

	public void testConfig_git_pl()
	{
		doTest("Config_git_pl");
	}

	public void testConfig_heavy_pl()
	{
		doTest("Config_heavy_pl");
	}

	public void testConfig_pm()
	{
		doTest("Config_pm");
	}

	public void testContext_Preserve_pm()
	{
		doTest("Context_Preserve_pm");
	}

	public void testConvert_ASCII_Armor_pm()
	{
		doTest("Convert_ASCII_Armor_pm");
	}

	public void testConvert_ASCII_Armour_pm()
	{
		doTest("Convert_ASCII_Armour_pm");
	}

	public void testConvert_ASN1_Debug_pm()
	{
		doTest("Convert_ASN1_Debug_pm");
	}

	public void testConvert_ASN1_IO_pm()
	{
		doTest("Convert_ASN1_IO_pm");
	}

	public void testConvert_ASN1__decode_pm()
	{
		doTest("Convert_ASN1__decode_pm");
	}

	public void testConvert_ASN1__encode_pm()
	{
		doTest("Convert_ASN1__encode_pm");
	}

	public void testConvert_ASN1_parser_pm()
	{
		doTest("Convert_ASN1_parser_pm");
	}

	public void testConvert_ASN1_pm()
	{
		doTest("Convert_ASN1_pm");
	}

	public void testConvert_PEM_CBC_pm()
	{
		doTest("Convert_PEM_CBC_pm");
	}

	public void testConvert_PEM_pm()
	{
		doTest("Convert_PEM_pm");
	}

	public void testCpanel_JSON_XS_Boolean_pm()
	{
		doTest("Cpanel_JSON_XS_Boolean_pm");
	}

	public void testCpanel_JSON_XS_pm()
	{
		doTest("Cpanel_JSON_XS_pm");
	}

	public void testCrypt_Blowfish_pm()
	{
		doTest("Crypt_Blowfish_pm");
	}

	public void testCrypt_CAST5_PP_Tables_pm()
	{
		doTest("Crypt_CAST5_PP_Tables_pm");
	}

	public void testCrypt_CAST5_PP_pm()
	{
		doTest("Crypt_CAST5_PP_pm");
	}

	public void testCrypt_CBC_pm()
	{
		doTest("Crypt_CBC_pm");
	}

	public void testCrypt_DES_EDE3_pm()
	{
		doTest("Crypt_DES_EDE3_pm");
	}

	public void testCrypt_DES_pm()
	{
		doTest("Crypt_DES_pm");
	}

	public void testCrypt_DSA_GMP_KeyChain_pm()
	{
		doTest("Crypt_DSA_GMP_KeyChain_pm");
	}

	public void testCrypt_DSA_GMP_Key_PEM_pm()
	{
		doTest("Crypt_DSA_GMP_Key_PEM_pm");
	}

	public void testCrypt_DSA_GMP_Key_SSH2_pm()
	{
		doTest("Crypt_DSA_GMP_Key_SSH2_pm");
	}

	public void testCrypt_DSA_GMP_Key_pm()
	{
		doTest("Crypt_DSA_GMP_Key_pm");
	}

	public void testCrypt_DSA_GMP_Signature_pm()
	{
		doTest("Crypt_DSA_GMP_Signature_pm");
	}

	public void testCrypt_DSA_GMP_Util_pm()
	{
		doTest("Crypt_DSA_GMP_Util_pm");
	}

	public void testCrypt_DSA_GMP_pm()
	{
		doTest("Crypt_DSA_GMP_pm");
	}

	public void testCrypt_DSA_KeyChain_pm()
	{
		doTest("Crypt_DSA_KeyChain_pm");
	}

	public void testCrypt_DSA_Key_PEM_pm()
	{
		doTest("Crypt_DSA_Key_PEM_pm");
	}

	public void testCrypt_DSA_Key_SSH2_pm()
	{
		doTest("Crypt_DSA_Key_SSH2_pm");
	}

	public void testCrypt_DSA_Key_pm()
	{
		doTest("Crypt_DSA_Key_pm");
	}

	public void testCrypt_DSA_Signature_pm()
	{
		doTest("Crypt_DSA_Signature_pm");
	}

	public void testCrypt_DSA_Util_pm()
	{
		doTest("Crypt_DSA_Util_pm");
	}

	public void testCrypt_DSA_pm()
	{
		doTest("Crypt_DSA_pm");
	}

	public void testCrypt_IDEA_pm()
	{
		doTest("Crypt_IDEA_pm");
	}

	public void testCrypt_OpenSSL_AES_pm()
	{
		doTest("Crypt_OpenSSL_AES_pm");
	}

	public void testCrypt_OpenSSL_Bignum_CTX_pm()
	{
		doTest("Crypt_OpenSSL_Bignum_CTX_pm");
	}

	public void testCrypt_OpenSSL_Bignum_pm()
	{
		doTest("Crypt_OpenSSL_Bignum_pm");
	}

	public void testCrypt_OpenSSL_DSA_pm()
	{
		doTest("Crypt_OpenSSL_DSA_pm");
	}

	public void testCrypt_OpenSSL_RSA_pm()
	{
		doTest("Crypt_OpenSSL_RSA_pm");
	}

	public void testCrypt_OpenSSL_Random_pm()
	{
		doTest("Crypt_OpenSSL_Random_pm");
	}

	public void testCrypt_OpenSSL_X509_pm()
	{
		doTest("Crypt_OpenSSL_X509_pm");
	}

	public void testCrypt_RC4_pm()
	{
		doTest("Crypt_RC4_pm");
	}

	public void testCrypt_RC6_pm()
	{
		doTest("Crypt_RC6_pm");
	}

	public void testCrypt_RIPEMD160_MAC_pm()
	{
		doTest("Crypt_RIPEMD160_MAC_pm");
	}

	public void testCrypt_RIPEMD160_pm()
	{
		doTest("Crypt_RIPEMD160_pm");
	}

	public void testCrypt_RSA_DataFormat_pm()
	{
		doTest("Crypt_RSA_DataFormat_pm");
	}

	public void testCrypt_RSA_Debug_pm()
	{
		doTest("Crypt_RSA_Debug_pm");
	}

	public void testCrypt_RSA_ES_OAEP_pm()
	{
		doTest("Crypt_RSA_ES_OAEP_pm");
	}

	public void testCrypt_RSA_ES_PKCS1v15_pm()
	{
		doTest("Crypt_RSA_ES_PKCS1v15_pm");
	}

	public void testCrypt_RSA_Errorhandler_pm()
	{
		doTest("Crypt_RSA_Errorhandler_pm");
	}

	public void testCrypt_RSA_Key_Private_SSH_pm()
	{
		doTest("Crypt_RSA_Key_Private_SSH_pm");
	}

	public void testCrypt_RSA_Key_Private_pm()
	{
		doTest("Crypt_RSA_Key_Private_pm");
	}

	public void testCrypt_RSA_Key_Public_SSH_pm()
	{
		doTest("Crypt_RSA_Key_Public_SSH_pm");
	}

	public void testCrypt_RSA_Key_Public_pm()
	{
		doTest("Crypt_RSA_Key_Public_pm");
	}

	public void testCrypt_RSA_Key_pm()
	{
		doTest("Crypt_RSA_Key_pm");
	}

	public void testCrypt_RSA_Primitives_pm()
	{
		doTest("Crypt_RSA_Primitives_pm");
	}

	public void testCrypt_RSA_SS_PKCS1v15_pm()
	{
		doTest("Crypt_RSA_SS_PKCS1v15_pm");
	}

	public void testCrypt_RSA_SS_PSS_pm()
	{
		doTest("Crypt_RSA_SS_PSS_pm");
	}

	public void testCrypt_RSA_pm()
	{
		doTest("Crypt_RSA_pm");
	}

	public void testCrypt_Random_Seed_pm()
	{
		doTest("Crypt_Random_Seed_pm");
	}

	public void testCrypt_Random_TESHA2_Config_pm()
	{
		doTest("Crypt_Random_TESHA2_Config_pm");
	}

	public void testCrypt_Random_TESHA2_pm()
	{
		doTest("Crypt_Random_TESHA2_pm");
	}

	public void testCrypt_Rijndael_pm()
	{
		doTest("Crypt_Rijndael_pm");
	}

	public void testCrypt_SSLeay_CTX_pm()
	{
		doTest("Crypt_SSLeay_CTX_pm");
	}

	public void testCrypt_SSLeay_Conn_pm()
	{
		doTest("Crypt_SSLeay_Conn_pm");
	}

	public void testCrypt_SSLeay_Err_pm()
	{
		doTest("Crypt_SSLeay_Err_pm");
	}

	public void testCrypt_SSLeay_MainContext_pm()
	{
		doTest("Crypt_SSLeay_MainContext_pm");
	}

	public void testCrypt_SSLeay_Version_pm()
	{
		doTest("Crypt_SSLeay_Version_pm");
	}

	public void testCrypt_SSLeay_X509_pm()
	{
		doTest("Crypt_SSLeay_X509_pm");
	}

	public void testCrypt_SSLeay_pm()
	{
		doTest("Crypt_SSLeay_pm");
	}

	public void testCrypt_Serpent_pm()
	{
		doTest("Crypt_Serpent_pm");
	}

	public void testCrypt_Twofish_pm()
	{
		doTest("Crypt_Twofish_pm");
	}

	public void testCwd_pm()
	{
		doTest("Cwd_pm");
	}

	public void testDBD_ADO_Const_pm()
	{
		doTest("DBD_ADO_Const_pm");
	}

	public void testDBD_ADO_GetInfo_pm()
	{
		doTest("DBD_ADO_GetInfo_pm");
	}

	public void testDBD_ADO_TypeInfo_pm()
	{
		doTest("DBD_ADO_TypeInfo_pm");
	}

	public void testDBD_ADO_pm()
	{
		doTest("DBD_ADO_pm");
	}

	public void testDBD_CSV_GetInfo_pm()
	{
		doTest("DBD_CSV_GetInfo_pm");
	}

	public void testDBD_CSV_TypeInfo_pm()
	{
		doTest("DBD_CSV_TypeInfo_pm");
	}

	public void testDBD_CSV_pm()
	{
		doTest("DBD_CSV_pm");
	}

	public void testDBD_DBM_pm()
	{
		doTest("DBD_DBM_pm");
	}

	public void testDBD_ExampleP_pm()
	{
		doTest("DBD_ExampleP_pm");
	}

	public void testDBD_File_pm()
	{
		doTest("DBD_File_pm");
	}

	public void testDBD_Gofer_Policy_Base_pm()
	{
		doTest("DBD_Gofer_Policy_Base_pm");
	}

	public void testDBD_Gofer_Policy_classic_pm()
	{
		doTest("DBD_Gofer_Policy_classic_pm");
	}

	public void testDBD_Gofer_Policy_pedantic_pm()
	{
		doTest("DBD_Gofer_Policy_pedantic_pm");
	}

	public void testDBD_Gofer_Policy_rush_pm()
	{
		doTest("DBD_Gofer_Policy_rush_pm");
	}

	public void testDBD_Gofer_Transport_Base_pm()
	{
		doTest("DBD_Gofer_Transport_Base_pm");
	}

	public void testDBD_Gofer_Transport_corostream_pm()
	{
		doTest("DBD_Gofer_Transport_corostream_pm");
	}

	public void testDBD_Gofer_Transport_null_pm()
	{
		doTest("DBD_Gofer_Transport_null_pm");
	}

	public void testDBD_Gofer_Transport_pipeone_pm()
	{
		doTest("DBD_Gofer_Transport_pipeone_pm");
	}

	public void testDBD_Gofer_Transport_stream_pm()
	{
		doTest("DBD_Gofer_Transport_stream_pm");
	}

	public void testDBD_Gofer_pm()
	{
		doTest("DBD_Gofer_pm");
	}

	public void testDBD_NullP_pm()
	{
		doTest("DBD_NullP_pm");
	}

	public void testDBD_ODBC_Changes_pm()
	{
		doTest("DBD_ODBC_Changes_pm");
	}

	public void testDBD_ODBC_FAQ_pm()
	{
		doTest("DBD_ODBC_FAQ_pm");
	}

	public void testDBD_ODBC_TO_DO_pm()
	{
		doTest("DBD_ODBC_TO_DO_pm");
	}

	public void testDBD_ODBC_pm()
	{
		doTest("DBD_ODBC_pm");
	}

	public void testDBD_Oracle_GetInfo_pm()
	{
		doTest("DBD_Oracle_GetInfo_pm");
	}

	public void testDBD_Oracle_Object_pm()
	{
		doTest("DBD_Oracle_Object_pm");
	}

	public void testDBD_Oracle_pm()
	{
		doTest("DBD_Oracle_pm");
	}

	public void testDBD_Pg_pm()
	{
		doTest("DBD_Pg_pm");
	}

	public void testDBD_Proxy_pm()
	{
		doTest("DBD_Proxy_pm");
	}

	public void testDBD_SQLite_VirtualTable_FileContent_pm()
	{
		doTest("DBD_SQLite_VirtualTable_FileContent_pm");
	}

	public void testDBD_SQLite_VirtualTable_PerlData_pm()
	{
		doTest("DBD_SQLite_VirtualTable_PerlData_pm");
	}

	public void testDBD_SQLite_VirtualTable_pm()
	{
		doTest("DBD_SQLite_VirtualTable_pm");
	}

	public void testDBD_SQLite_pm()
	{
		doTest("DBD_SQLite_pm");
	}

	public void testDBD_Sponge_pm()
	{
		doTest("DBD_Sponge_pm");
	}

	public void testDBD_mysql_GetInfo_pm()
	{
		doTest("DBD_mysql_GetInfo_pm");
	}

	public void testDBD_mysql_pm()
	{
		doTest("DBD_mysql_pm");
	}

	public void testDBI_Changes_pm()
	{
		doTest("DBI_Changes_pm");
	}

	public void testDBI_Const_GetInfoReturn_pm()
	{
		doTest("DBI_Const_GetInfoReturn_pm");
	}

	public void testDBI_Const_GetInfoType_pm()
	{
		doTest("DBI_Const_GetInfoType_pm");
	}

	public void testDBI_Const_GetInfo_ANSI_pm()
	{
		doTest("DBI_Const_GetInfo_ANSI_pm");
	}

	public void testDBI_Const_GetInfo_ODBC_pm()
	{
		doTest("DBI_Const_GetInfo_ODBC_pm");
	}

	public void testDBI_DBD_Metadata_pm()
	{
		doTest("DBI_DBD_Metadata_pm");
	}

	public void testDBI_DBD_SqlEngine_pm()
	{
		doTest("DBI_DBD_SqlEngine_pm");
	}

	public void testDBI_DBD_pm()
	{
		doTest("DBI_DBD_pm");
	}

	public void testDBI_FAQ_pm()
	{
		doTest("DBI_FAQ_pm");
	}

	public void testDBI_Gofer_Execute_pm()
	{
		doTest("DBI_Gofer_Execute_pm");
	}

	public void testDBI_Gofer_Request_pm()
	{
		doTest("DBI_Gofer_Request_pm");
	}

	public void testDBI_Gofer_Response_pm()
	{
		doTest("DBI_Gofer_Response_pm");
	}

	public void testDBI_Gofer_Serializer_Base_pm()
	{
		doTest("DBI_Gofer_Serializer_Base_pm");
	}

	public void testDBI_Gofer_Serializer_DataDumper_pm()
	{
		doTest("DBI_Gofer_Serializer_DataDumper_pm");
	}

	public void testDBI_Gofer_Serializer_Storable_pm()
	{
		doTest("DBI_Gofer_Serializer_Storable_pm");
	}

	public void testDBI_Gofer_Transport_Base_pm()
	{
		doTest("DBI_Gofer_Transport_Base_pm");
	}

	public void testDBI_Gofer_Transport_pipeone_pm()
	{
		doTest("DBI_Gofer_Transport_pipeone_pm");
	}

	public void testDBI_Gofer_Transport_stream_pm()
	{
		doTest("DBI_Gofer_Transport_stream_pm");
	}

	public void testDBI_ProfileData_pm()
	{
		doTest("DBI_ProfileData_pm");
	}

	public void testDBI_ProfileDumper_Apache_pm()
	{
		doTest("DBI_ProfileDumper_Apache_pm");
	}

	public void testDBI_ProfileDumper_pm()
	{
		doTest("DBI_ProfileDumper_pm");
	}

	public void testDBI_ProfileSubs_pm()
	{
		doTest("DBI_ProfileSubs_pm");
	}

	public void testDBI_Profile_pm()
	{
		doTest("DBI_Profile_pm");
	}

	public void testDBI_ProxyServer_pm()
	{
		doTest("DBI_ProxyServer_pm");
	}

	public void testDBI_PurePerl_pm()
	{
		doTest("DBI_PurePerl_pm");
	}

	public void testDBI_SQL_Nano_pm()
	{
		doTest("DBI_SQL_Nano_pm");
	}

	public void testDBI_Test_Case_DBD_CSV_t10_base_pm()
	{
		doTest("DBI_Test_Case_DBD_CSV_t10_base_pm");
	}

	public void testDBI_Test_Case_DBD_CSV_t11_dsnlist_pm()
	{
		doTest("DBI_Test_Case_DBD_CSV_t11_dsnlist_pm");
	}

	public void testDBI_Test_Case_DBD_CSV_t20_createdrop_pm()
	{
		doTest("DBI_Test_Case_DBD_CSV_t20_createdrop_pm");
	}

	public void testDBI_Test_Case_DBD_CSV_t85_error_pm()
	{
		doTest("DBI_Test_Case_DBD_CSV_t85_error_pm");
	}

	public void testDBI_Test_DBD_CSV_Conf_pm()
	{
		doTest("DBI_Test_DBD_CSV_Conf_pm");
	}

	public void testDBI_Test_DBD_CSV_List_pm()
	{
		doTest("DBI_Test_DBD_CSV_List_pm");
	}

	public void testDBI_Util_CacheMemory_pm()
	{
		doTest("DBI_Util_CacheMemory_pm");
	}

	public void testDBI_Util__accessor_pm()
	{
		doTest("DBI_Util__accessor_pm");
	}

	public void testDBI_W32ODBC_pm()
	{
		doTest("DBI_W32ODBC_pm");
	}

	public void testDBI_pm()
	{
		doTest("DBI_pm");
	}

	public void testDBIx_Class_AccessorGroup_pm()
	{
		doTest("DBIx_Class_AccessorGroup_pm");
	}

	public void testDBIx_Class_Admin_Descriptive_pm()
	{
		doTest("DBIx_Class_Admin_Descriptive_pm");
	}

	public void testDBIx_Class_Admin_Types_pm()
	{
		doTest("DBIx_Class_Admin_Types_pm");
	}

	public void testDBIx_Class_Admin_Usage_pm()
	{
		doTest("DBIx_Class_Admin_Usage_pm");
	}

	public void testDBIx_Class_Admin_pm()
	{
		doTest("DBIx_Class_Admin_pm");
	}

	public void testDBIx_Class_CDBICompat_AbstractSearch_pm()
	{
		doTest("DBIx_Class_CDBICompat_AbstractSearch_pm");
	}

	public void testDBIx_Class_CDBICompat_AccessorMapping_pm()
	{
		doTest("DBIx_Class_CDBICompat_AccessorMapping_pm");
	}

	public void testDBIx_Class_CDBICompat_AttributeAPI_pm()
	{
		doTest("DBIx_Class_CDBICompat_AttributeAPI_pm");
	}

	public void testDBIx_Class_CDBICompat_AutoUpdate_pm()
	{
		doTest("DBIx_Class_CDBICompat_AutoUpdate_pm");
	}

	public void testDBIx_Class_CDBICompat_ColumnCase_pm()
	{
		doTest("DBIx_Class_CDBICompat_ColumnCase_pm");
	}

	public void testDBIx_Class_CDBICompat_ColumnGroups_pm()
	{
		doTest("DBIx_Class_CDBICompat_ColumnGroups_pm");
	}

	public void testDBIx_Class_CDBICompat_ColumnsAsHash_pm()
	{
		doTest("DBIx_Class_CDBICompat_ColumnsAsHash_pm");
	}

	public void testDBIx_Class_CDBICompat_Constraints_pm()
	{
		doTest("DBIx_Class_CDBICompat_Constraints_pm");
	}

	public void testDBIx_Class_CDBICompat_Constructor_pm()
	{
		doTest("DBIx_Class_CDBICompat_Constructor_pm");
	}

	public void testDBIx_Class_CDBICompat_Copy_pm()
	{
		doTest("DBIx_Class_CDBICompat_Copy_pm");
	}

	public void testDBIx_Class_CDBICompat_DestroyWarning_pm()
	{
		doTest("DBIx_Class_CDBICompat_DestroyWarning_pm");
	}

	public void testDBIx_Class_CDBICompat_GetSet_pm()
	{
		doTest("DBIx_Class_CDBICompat_GetSet_pm");
	}

	public void testDBIx_Class_CDBICompat_ImaDBI_pm()
	{
		doTest("DBIx_Class_CDBICompat_ImaDBI_pm");
	}

	public void testDBIx_Class_CDBICompat_Iterator_pm()
	{
		doTest("DBIx_Class_CDBICompat_Iterator_pm");
	}

	public void testDBIx_Class_CDBICompat_LazyLoading_pm()
	{
		doTest("DBIx_Class_CDBICompat_LazyLoading_pm");
	}

	public void testDBIx_Class_CDBICompat_LiveObjectIndex_pm()
	{
		doTest("DBIx_Class_CDBICompat_LiveObjectIndex_pm");
	}

	public void testDBIx_Class_CDBICompat_NoObjectIndex_pm()
	{
		doTest("DBIx_Class_CDBICompat_NoObjectIndex_pm");
	}

	public void testDBIx_Class_CDBICompat_Pager_pm()
	{
		doTest("DBIx_Class_CDBICompat_Pager_pm");
	}

	public void testDBIx_Class_CDBICompat_ReadOnly_pm()
	{
		doTest("DBIx_Class_CDBICompat_ReadOnly_pm");
	}

	public void testDBIx_Class_CDBICompat_Relationship_pm()
	{
		doTest("DBIx_Class_CDBICompat_Relationship_pm");
	}

	public void testDBIx_Class_CDBICompat_Relationships_pm()
	{
		doTest("DBIx_Class_CDBICompat_Relationships_pm");
	}

	public void testDBIx_Class_CDBICompat_Retrieve_pm()
	{
		doTest("DBIx_Class_CDBICompat_Retrieve_pm");
	}

	public void testDBIx_Class_CDBICompat_SQLTransformer_pm()
	{
		doTest("DBIx_Class_CDBICompat_SQLTransformer_pm");
	}

	public void testDBIx_Class_CDBICompat_Stringify_pm()
	{
		doTest("DBIx_Class_CDBICompat_Stringify_pm");
	}

	public void testDBIx_Class_CDBICompat_TempColumns_pm()
	{
		doTest("DBIx_Class_CDBICompat_TempColumns_pm");
	}

	public void testDBIx_Class_CDBICompat_Triggers_pm()
	{
		doTest("DBIx_Class_CDBICompat_Triggers_pm");
	}

	public void testDBIx_Class_CDBICompat_pm()
	{
		doTest("DBIx_Class_CDBICompat_pm");
	}

	public void testDBIx_Class_Carp_pm()
	{
		doTest("DBIx_Class_Carp_pm");
	}

	public void testDBIx_Class_ClassResolver_PassThrough_pm()
	{
		doTest("DBIx_Class_ClassResolver_PassThrough_pm");
	}

	public void testDBIx_Class_Componentised_pm()
	{
		doTest("DBIx_Class_Componentised_pm");
	}

	public void testDBIx_Class_Core_pm()
	{
		doTest("DBIx_Class_Core_pm");
	}

	public void testDBIx_Class_Cursor_pm()
	{
		doTest("DBIx_Class_Cursor_pm");
	}

	public void testDBIx_Class_DB_pm()
	{
		doTest("DBIx_Class_DB_pm");
	}

	public void testDBIx_Class_Exception_pm()
	{
		doTest("DBIx_Class_Exception_pm");
	}

	public void testDBIx_Class_FilterColumn_pm()
	{
		doTest("DBIx_Class_FilterColumn_pm");
	}

	public void testDBIx_Class_InflateColumn_DateTime_pm()
	{
		doTest("DBIx_Class_InflateColumn_DateTime_pm");
	}

	public void testDBIx_Class_InflateColumn_File_pm()
	{
		doTest("DBIx_Class_InflateColumn_File_pm");
	}

	public void testDBIx_Class_InflateColumn_pm()
	{
		doTest("DBIx_Class_InflateColumn_pm");
	}

	public void testDBIx_Class_Optional_Dependencies_pm()
	{
		doTest("DBIx_Class_Optional_Dependencies_pm");
	}

	public void testDBIx_Class_Ordered_pm()
	{
		doTest("DBIx_Class_Ordered_pm");
	}

	public void testDBIx_Class_PK_Auto_DB2_pm()
	{
		doTest("DBIx_Class_PK_Auto_DB2_pm");
	}

	public void testDBIx_Class_PK_Auto_MSSQL_pm()
	{
		doTest("DBIx_Class_PK_Auto_MSSQL_pm");
	}

	public void testDBIx_Class_PK_Auto_MySQL_pm()
	{
		doTest("DBIx_Class_PK_Auto_MySQL_pm");
	}

	public void testDBIx_Class_PK_Auto_Oracle_pm()
	{
		doTest("DBIx_Class_PK_Auto_Oracle_pm");
	}

	public void testDBIx_Class_PK_Auto_Pg_pm()
	{
		doTest("DBIx_Class_PK_Auto_Pg_pm");
	}

	public void testDBIx_Class_PK_Auto_SQLite_pm()
	{
		doTest("DBIx_Class_PK_Auto_SQLite_pm");
	}

	public void testDBIx_Class_PK_Auto_pm()
	{
		doTest("DBIx_Class_PK_Auto_pm");
	}

	public void testDBIx_Class_PK_pm()
	{
		doTest("DBIx_Class_PK_pm");
	}

	public void testDBIx_Class_Relationship_Accessor_pm()
	{
		doTest("DBIx_Class_Relationship_Accessor_pm");
	}

	public void testDBIx_Class_Relationship_Base_pm()
	{
		doTest("DBIx_Class_Relationship_Base_pm");
	}

	public void testDBIx_Class_Relationship_BelongsTo_pm()
	{
		doTest("DBIx_Class_Relationship_BelongsTo_pm");
	}

	public void testDBIx_Class_Relationship_CascadeActions_pm()
	{
		doTest("DBIx_Class_Relationship_CascadeActions_pm");
	}

	public void testDBIx_Class_Relationship_HasMany_pm()
	{
		doTest("DBIx_Class_Relationship_HasMany_pm");
	}

	public void testDBIx_Class_Relationship_HasOne_pm()
	{
		doTest("DBIx_Class_Relationship_HasOne_pm");
	}

	public void testDBIx_Class_Relationship_Helpers_pm()
	{
		doTest("DBIx_Class_Relationship_Helpers_pm");
	}

	public void testDBIx_Class_Relationship_ManyToMany_pm()
	{
		doTest("DBIx_Class_Relationship_ManyToMany_pm");
	}

	public void testDBIx_Class_Relationship_ProxyMethods_pm()
	{
		doTest("DBIx_Class_Relationship_ProxyMethods_pm");
	}

	public void testDBIx_Class_Relationship_pm()
	{
		doTest("DBIx_Class_Relationship_pm");
	}

	public void testDBIx_Class_ResultClass_HashRefInflator_pm()
	{
		doTest("DBIx_Class_ResultClass_HashRefInflator_pm");
	}

	public void testDBIx_Class_ResultSetColumn_pm()
	{
		doTest("DBIx_Class_ResultSetColumn_pm");
	}

	public void testDBIx_Class_ResultSetManager_pm()
	{
		doTest("DBIx_Class_ResultSetManager_pm");
	}

	public void testDBIx_Class_ResultSetProxy_pm()
	{
		doTest("DBIx_Class_ResultSetProxy_pm");
	}

	public void testDBIx_Class_ResultSet_Pager_pm()
	{
		doTest("DBIx_Class_ResultSet_Pager_pm");
	}

	public void testDBIx_Class_ResultSet_pm()
	{
		doTest("DBIx_Class_ResultSet_pm");
	}

	public void testDBIx_Class_ResultSourceHandle_pm()
	{
		doTest("DBIx_Class_ResultSourceHandle_pm");
	}

	public void testDBIx_Class_ResultSourceProxy_Table_pm()
	{
		doTest("DBIx_Class_ResultSourceProxy_Table_pm");
	}

	public void testDBIx_Class_ResultSourceProxy_pm()
	{
		doTest("DBIx_Class_ResultSourceProxy_pm");
	}

	public void testDBIx_Class_ResultSource_RowParser_Util_pm()
	{
		doTest("DBIx_Class_ResultSource_RowParser_Util_pm");
	}

	public void testDBIx_Class_ResultSource_RowParser_pm()
	{
		doTest("DBIx_Class_ResultSource_RowParser_pm");
	}

	public void testDBIx_Class_ResultSource_Table_pm()
	{
		doTest("DBIx_Class_ResultSource_Table_pm");
	}

	public void testDBIx_Class_ResultSource_View_pm()
	{
		doTest("DBIx_Class_ResultSource_View_pm");
	}

	public void testDBIx_Class_ResultSource_pm()
	{
		doTest("DBIx_Class_ResultSource_pm");
	}

	public void testDBIx_Class_Row_pm()
	{
		doTest("DBIx_Class_Row_pm");
	}

	public void testDBIx_Class_SQLAHacks_MSSQL_pm()
	{
		doTest("DBIx_Class_SQLAHacks_MSSQL_pm");
	}

	public void testDBIx_Class_SQLAHacks_MySQL_pm()
	{
		doTest("DBIx_Class_SQLAHacks_MySQL_pm");
	}

	public void testDBIx_Class_SQLAHacks_OracleJoins_pm()
	{
		doTest("DBIx_Class_SQLAHacks_OracleJoins_pm");
	}

	public void testDBIx_Class_SQLAHacks_Oracle_pm()
	{
		doTest("DBIx_Class_SQLAHacks_Oracle_pm");
	}

	public void testDBIx_Class_SQLAHacks_SQLite_pm()
	{
		doTest("DBIx_Class_SQLAHacks_SQLite_pm");
	}

	public void testDBIx_Class_SQLAHacks_pm()
	{
		doTest("DBIx_Class_SQLAHacks_pm");
	}

	public void testDBIx_Class_SQLMaker_ACCESS_pm()
	{
		doTest("DBIx_Class_SQLMaker_ACCESS_pm");
	}

	public void testDBIx_Class_SQLMaker_LimitDialects_pm()
	{
		doTest("DBIx_Class_SQLMaker_LimitDialects_pm");
	}

	public void testDBIx_Class_SQLMaker_MSSQL_pm()
	{
		doTest("DBIx_Class_SQLMaker_MSSQL_pm");
	}

	public void testDBIx_Class_SQLMaker_MySQL_pm()
	{
		doTest("DBIx_Class_SQLMaker_MySQL_pm");
	}

	public void testDBIx_Class_SQLMaker_OracleJoins_pm()
	{
		doTest("DBIx_Class_SQLMaker_OracleJoins_pm");
	}

	public void testDBIx_Class_SQLMaker_Oracle_pm()
	{
		doTest("DBIx_Class_SQLMaker_Oracle_pm");
	}

	public void testDBIx_Class_SQLMaker_SQLite_pm()
	{
		doTest("DBIx_Class_SQLMaker_SQLite_pm");
	}

	public void testDBIx_Class_SQLMaker_pm()
	{
		doTest("DBIx_Class_SQLMaker_pm");
	}

	public void testDBIx_Class_Schema_Versioned_pm()
	{
		doTest("DBIx_Class_Schema_Versioned_pm");
	}

	public void testDBIx_Class_Schema_pm()
	{
		doTest("DBIx_Class_Schema_pm");
	}

	public void testDBIx_Class_Serialize_Storable_pm()
	{
		doTest("DBIx_Class_Serialize_Storable_pm");
	}

	public void testDBIx_Class_StartupCheck_pm()
	{
		doTest("DBIx_Class_StartupCheck_pm");
	}

	public void testDBIx_Class_Storage_BlockRunner_pm()
	{
		doTest("DBIx_Class_Storage_BlockRunner_pm");
	}

	public void testDBIx_Class_Storage_DBIHacks_pm()
	{
		doTest("DBIx_Class_Storage_DBIHacks_pm");
	}

	public void testDBIx_Class_Storage_DBI_ACCESS_pm()
	{
		doTest("DBIx_Class_Storage_DBI_ACCESS_pm");
	}

	public void testDBIx_Class_Storage_DBI_ADO_CursorUtils_pm()
	{
		doTest("DBIx_Class_Storage_DBI_ADO_CursorUtils_pm");
	}

	public void testDBIx_Class_Storage_DBI_ADO_MS_Jet_Cursor_pm()
	{
		doTest("DBIx_Class_Storage_DBI_ADO_MS_Jet_Cursor_pm");
	}

	public void testDBIx_Class_Storage_DBI_ADO_MS_Jet_pm()
	{
		doTest("DBIx_Class_Storage_DBI_ADO_MS_Jet_pm");
	}

	public void testDBIx_Class_Storage_DBI_ADO_Microsoft_SQL_Server_Cursor_pm()
	{
		doTest("DBIx_Class_Storage_DBI_ADO_Microsoft_SQL_Server_Cursor_pm");
	}

	public void testDBIx_Class_Storage_DBI_ADO_Microsoft_SQL_Server_pm()
	{
		doTest("DBIx_Class_Storage_DBI_ADO_Microsoft_SQL_Server_pm");
	}

	public void testDBIx_Class_Storage_DBI_ADO_pm()
	{
		doTest("DBIx_Class_Storage_DBI_ADO_pm");
	}

	public void testDBIx_Class_Storage_DBI_AutoCast_pm()
	{
		doTest("DBIx_Class_Storage_DBI_AutoCast_pm");
	}

	public void testDBIx_Class_Storage_DBI_Cursor_pm()
	{
		doTest("DBIx_Class_Storage_DBI_Cursor_pm");
	}

	public void testDBIx_Class_Storage_DBI_DB2_pm()
	{
		doTest("DBIx_Class_Storage_DBI_DB2_pm");
	}

	public void testDBIx_Class_Storage_DBI_Firebird_Common_pm()
	{
		doTest("DBIx_Class_Storage_DBI_Firebird_Common_pm");
	}

	public void testDBIx_Class_Storage_DBI_Firebird_pm()
	{
		doTest("DBIx_Class_Storage_DBI_Firebird_pm");
	}

	public void testDBIx_Class_Storage_DBI_IdentityInsert_pm()
	{
		doTest("DBIx_Class_Storage_DBI_IdentityInsert_pm");
	}

	public void testDBIx_Class_Storage_DBI_Informix_pm()
	{
		doTest("DBIx_Class_Storage_DBI_Informix_pm");
	}

	public void testDBIx_Class_Storage_DBI_InterBase_pm()
	{
		doTest("DBIx_Class_Storage_DBI_InterBase_pm");
	}

	public void testDBIx_Class_Storage_DBI_MSSQL_pm()
	{
		doTest("DBIx_Class_Storage_DBI_MSSQL_pm");
	}

	public void testDBIx_Class_Storage_DBI_NoBindVars_pm()
	{
		doTest("DBIx_Class_Storage_DBI_NoBindVars_pm");
	}

	public void testDBIx_Class_Storage_DBI_ODBC_ACCESS_pm()
	{
		doTest("DBIx_Class_Storage_DBI_ODBC_ACCESS_pm");
	}

	public void testDBIx_Class_Storage_DBI_ODBC_DB2_400_SQL_pm()
	{
		doTest("DBIx_Class_Storage_DBI_ODBC_DB2_400_SQL_pm");
	}

	public void testDBIx_Class_Storage_DBI_ODBC_Firebird_pm()
	{
		doTest("DBIx_Class_Storage_DBI_ODBC_Firebird_pm");
	}

	public void testDBIx_Class_Storage_DBI_ODBC_Microsoft_SQL_Server_pm()
	{
		doTest("DBIx_Class_Storage_DBI_ODBC_Microsoft_SQL_Server_pm");
	}

	public void testDBIx_Class_Storage_DBI_ODBC_SQL_Anywhere_pm()
	{
		doTest("DBIx_Class_Storage_DBI_ODBC_SQL_Anywhere_pm");
	}

	public void testDBIx_Class_Storage_DBI_ODBC_pm()
	{
		doTest("DBIx_Class_Storage_DBI_ODBC_pm");
	}

	public void testDBIx_Class_Storage_DBI_Oracle_Generic_pm()
	{
		doTest("DBIx_Class_Storage_DBI_Oracle_Generic_pm");
	}

	public void testDBIx_Class_Storage_DBI_Oracle_WhereJoins_pm()
	{
		doTest("DBIx_Class_Storage_DBI_Oracle_WhereJoins_pm");
	}

	public void testDBIx_Class_Storage_DBI_Oracle_pm()
	{
		doTest("DBIx_Class_Storage_DBI_Oracle_pm");
	}

	public void testDBIx_Class_Storage_DBI_Pg_pm()
	{
		doTest("DBIx_Class_Storage_DBI_Pg_pm");
	}

	public void testDBIx_Class_Storage_DBI_Replicated_Balancer_First_pm()
	{
		doTest("DBIx_Class_Storage_DBI_Replicated_Balancer_First_pm");
	}

	public void testDBIx_Class_Storage_DBI_Replicated_Balancer_Random_pm()
	{
		doTest("DBIx_Class_Storage_DBI_Replicated_Balancer_Random_pm");
	}

	public void testDBIx_Class_Storage_DBI_Replicated_Balancer_pm()
	{
		doTest("DBIx_Class_Storage_DBI_Replicated_Balancer_pm");
	}

	public void testDBIx_Class_Storage_DBI_Replicated_Pool_pm()
	{
		doTest("DBIx_Class_Storage_DBI_Replicated_Pool_pm");
	}

	public void testDBIx_Class_Storage_DBI_Replicated_Replicant_pm()
	{
		doTest("DBIx_Class_Storage_DBI_Replicated_Replicant_pm");
	}

	public void testDBIx_Class_Storage_DBI_Replicated_Types_pm()
	{
		doTest("DBIx_Class_Storage_DBI_Replicated_Types_pm");
	}

	public void testDBIx_Class_Storage_DBI_Replicated_WithDSN_pm()
	{
		doTest("DBIx_Class_Storage_DBI_Replicated_WithDSN_pm");
	}

	public void testDBIx_Class_Storage_DBI_Replicated_pm()
	{
		doTest("DBIx_Class_Storage_DBI_Replicated_pm");
	}

	public void testDBIx_Class_Storage_DBI_SQLAnywhere_Cursor_pm()
	{
		doTest("DBIx_Class_Storage_DBI_SQLAnywhere_Cursor_pm");
	}

	public void testDBIx_Class_Storage_DBI_SQLAnywhere_pm()
	{
		doTest("DBIx_Class_Storage_DBI_SQLAnywhere_pm");
	}

	public void testDBIx_Class_Storage_DBI_SQLite_pm()
	{
		doTest("DBIx_Class_Storage_DBI_SQLite_pm");
	}

	public void testDBIx_Class_Storage_DBI_Sybase_ASE_NoBindVars_pm()
	{
		doTest("DBIx_Class_Storage_DBI_Sybase_ASE_NoBindVars_pm");
	}

	public void testDBIx_Class_Storage_DBI_Sybase_ASE_pm()
	{
		doTest("DBIx_Class_Storage_DBI_Sybase_ASE_pm");
	}

	public void testDBIx_Class_Storage_DBI_Sybase_FreeTDS_pm()
	{
		doTest("DBIx_Class_Storage_DBI_Sybase_FreeTDS_pm");
	}

	public void testDBIx_Class_Storage_DBI_Sybase_MSSQL_pm()
	{
		doTest("DBIx_Class_Storage_DBI_Sybase_MSSQL_pm");
	}

	public void testDBIx_Class_Storage_DBI_Sybase_Microsoft_SQL_Server_NoBindVars_pm()
	{
		doTest("DBIx_Class_Storage_DBI_Sybase_Microsoft_SQL_Server_NoBindVars_pm");
	}

	public void testDBIx_Class_Storage_DBI_Sybase_Microsoft_SQL_Server_pm()
	{
		doTest("DBIx_Class_Storage_DBI_Sybase_Microsoft_SQL_Server_pm");
	}

	public void testDBIx_Class_Storage_DBI_Sybase_pm()
	{
		doTest("DBIx_Class_Storage_DBI_Sybase_pm");
	}

	public void testDBIx_Class_Storage_DBI_UniqueIdentifier_pm()
	{
		doTest("DBIx_Class_Storage_DBI_UniqueIdentifier_pm");
	}

	public void testDBIx_Class_Storage_DBI_mysql_pm()
	{
		doTest("DBIx_Class_Storage_DBI_mysql_pm");
	}

	public void testDBIx_Class_Storage_DBI_pm()
	{
		doTest("DBIx_Class_Storage_DBI_pm");
	}

	public void testDBIx_Class_Storage_Debug_PrettyPrint_pm()
	{
		doTest("DBIx_Class_Storage_Debug_PrettyPrint_pm");
	}

	public void testDBIx_Class_Storage_Statistics_pm()
	{
		doTest("DBIx_Class_Storage_Statistics_pm");
	}

	public void testDBIx_Class_Storage_TxnScopeGuard_pm()
	{
		doTest("DBIx_Class_Storage_TxnScopeGuard_pm");
	}

	public void testDBIx_Class_Storage_pm()
	{
		doTest("DBIx_Class_Storage_pm");
	}

	public void testDBIx_Class_UTF8Columns_pm()
	{
		doTest("DBIx_Class_UTF8Columns_pm");
	}

	public void testDBIx_Class__Util_pm()
	{
		doTest("DBIx_Class__Util_pm");
	}

	public void testDBIx_Class_pm()
	{
		doTest("DBIx_Class_pm");
	}

	public void testDBIx_Simple_Result_RowObject_pm()
	{
		doTest("DBIx_Simple_Result_RowObject_pm");
	}

	public void testDBIx_Simple_pm()
	{
		doTest("DBIx_Simple_pm");
	}

	public void testDBM_Deep_Array_pm()
	{
		doTest("DBM_Deep_Array_pm");
	}

	public void testDBM_Deep_ConfigData_pm()
	{
		doTest("DBM_Deep_ConfigData_pm");
	}

	public void testDBM_Deep_Engine_DBI_pm()
	{
		doTest("DBM_Deep_Engine_DBI_pm");
	}

	public void testDBM_Deep_Engine_File_pm()
	{
		doTest("DBM_Deep_Engine_File_pm");
	}

	public void testDBM_Deep_Engine_pm()
	{
		doTest("DBM_Deep_Engine_pm");
	}

	public void testDBM_Deep_Hash_pm()
	{
		doTest("DBM_Deep_Hash_pm");
	}

	public void testDBM_Deep_Iterator_DBI_pm()
	{
		doTest("DBM_Deep_Iterator_DBI_pm");
	}

	public void testDBM_Deep_Iterator_File_BucketList_pm()
	{
		doTest("DBM_Deep_Iterator_File_BucketList_pm");
	}

	public void testDBM_Deep_Iterator_File_Index_pm()
	{
		doTest("DBM_Deep_Iterator_File_Index_pm");
	}

	public void testDBM_Deep_Iterator_File_pm()
	{
		doTest("DBM_Deep_Iterator_File_pm");
	}

	public void testDBM_Deep_Iterator_pm()
	{
		doTest("DBM_Deep_Iterator_pm");
	}

	public void testDBM_Deep_Null_pm()
	{
		doTest("DBM_Deep_Null_pm");
	}

	public void testDBM_Deep_Sector_DBI_Reference_pm()
	{
		doTest("DBM_Deep_Sector_DBI_Reference_pm");
	}

	public void testDBM_Deep_Sector_DBI_Scalar_pm()
	{
		doTest("DBM_Deep_Sector_DBI_Scalar_pm");
	}

	public void testDBM_Deep_Sector_DBI_pm()
	{
		doTest("DBM_Deep_Sector_DBI_pm");
	}

	public void testDBM_Deep_Sector_File_BucketList_pm()
	{
		doTest("DBM_Deep_Sector_File_BucketList_pm");
	}

	public void testDBM_Deep_Sector_File_Data_pm()
	{
		doTest("DBM_Deep_Sector_File_Data_pm");
	}

	public void testDBM_Deep_Sector_File_Index_pm()
	{
		doTest("DBM_Deep_Sector_File_Index_pm");
	}

	public void testDBM_Deep_Sector_File_Null_pm()
	{
		doTest("DBM_Deep_Sector_File_Null_pm");
	}

	public void testDBM_Deep_Sector_File_Reference_pm()
	{
		doTest("DBM_Deep_Sector_File_Reference_pm");
	}

	public void testDBM_Deep_Sector_File_Scalar_pm()
	{
		doTest("DBM_Deep_Sector_File_Scalar_pm");
	}

	public void testDBM_Deep_Sector_File_pm()
	{
		doTest("DBM_Deep_Sector_File_pm");
	}

	public void testDBM_Deep_Sector_pm()
	{
		doTest("DBM_Deep_Sector_pm");
	}

	public void testDBM_Deep_Storage_DBI_pm()
	{
		doTest("DBM_Deep_Storage_DBI_pm");
	}

	public void testDBM_Deep_Storage_File_pm()
	{
		doTest("DBM_Deep_Storage_File_pm");
	}

	public void testDBM_Deep_Storage_pm()
	{
		doTest("DBM_Deep_Storage_pm");
	}

	public void testDBM_Deep_pm()
	{
		doTest("DBM_Deep_pm");
	}

	public void testDBM_Filter_compress_pm()
	{
		doTest("DBM_Filter_compress_pm");
	}

	public void testDBM_Filter_encode_pm()
	{
		doTest("DBM_Filter_encode_pm");
	}

	public void testDBM_Filter_int32_pm()
	{
		doTest("DBM_Filter_int32_pm");
	}

	public void testDBM_Filter_null_pm()
	{
		doTest("DBM_Filter_null_pm");
	}

	public void testDBM_Filter_pm()
	{
		doTest("DBM_Filter_pm");
	}

	public void testDBM_Filter_utf8_pm()
	{
		doTest("DBM_Filter_utf8_pm");
	}

	public void testDB_File_pm()
	{
		doTest("DB_File_pm");
	}

	public void testDB_pm()
	{
		doTest("DB_pm");
	}

	public void testDDP_pm()
	{
		doTest("DDP_pm");
	}

	public void testDTL_Fast_Cache_Compressed_pm()
	{
		doTest("DTL_Fast_Cache_Compressed_pm");
	}

	public void testDTL_Fast_Cache_File_pm()
	{
		doTest("DTL_Fast_Cache_File_pm");
	}

	public void testDTL_Fast_Cache_Memcached_pm()
	{
		doTest("DTL_Fast_Cache_Memcached_pm");
	}

	public void testDTL_Fast_Cache_Runtime_pm()
	{
		doTest("DTL_Fast_Cache_Runtime_pm");
	}

	public void testDTL_Fast_Cache_Serialized_pm()
	{
		doTest("DTL_Fast_Cache_Serialized_pm");
	}

	public void testDTL_Fast_Cache_pm()
	{
		doTest("DTL_Fast_Cache_pm");
	}

	public void testDTL_Fast_Context_pm()
	{
		doTest("DTL_Fast_Context_pm");
	}

	public void testDTL_Fast_Entity_pm()
	{
		doTest("DTL_Fast_Entity_pm");
	}

	public void testDTL_Fast_Expression_Operator_Binary_And_pm()
	{
		doTest("DTL_Fast_Expression_Operator_Binary_And_pm");
	}

	public void testDTL_Fast_Expression_Operator_Binary_Div_pm()
	{
		doTest("DTL_Fast_Expression_Operator_Binary_Div_pm");
	}

	public void testDTL_Fast_Expression_Operator_Binary_Eq_pm()
	{
		doTest("DTL_Fast_Expression_Operator_Binary_Eq_pm");
	}

	public void testDTL_Fast_Expression_Operator_Binary_Ge_pm()
	{
		doTest("DTL_Fast_Expression_Operator_Binary_Ge_pm");
	}

	public void testDTL_Fast_Expression_Operator_Binary_Gt_pm()
	{
		doTest("DTL_Fast_Expression_Operator_Binary_Gt_pm");
	}

	public void testDTL_Fast_Expression_Operator_Binary_In_pm()
	{
		doTest("DTL_Fast_Expression_Operator_Binary_In_pm");
	}

	public void testDTL_Fast_Expression_Operator_Binary_Le_pm()
	{
		doTest("DTL_Fast_Expression_Operator_Binary_Le_pm");
	}

	public void testDTL_Fast_Expression_Operator_Binary_Logical_pm()
	{
		doTest("DTL_Fast_Expression_Operator_Binary_Logical_pm");
	}

	public void testDTL_Fast_Expression_Operator_Binary_Lt_pm()
	{
		doTest("DTL_Fast_Expression_Operator_Binary_Lt_pm");
	}

	public void testDTL_Fast_Expression_Operator_Binary_Minus_pm()
	{
		doTest("DTL_Fast_Expression_Operator_Binary_Minus_pm");
	}

	public void testDTL_Fast_Expression_Operator_Binary_Mod_pm()
	{
		doTest("DTL_Fast_Expression_Operator_Binary_Mod_pm");
	}

	public void testDTL_Fast_Expression_Operator_Binary_Mul_pm()
	{
		doTest("DTL_Fast_Expression_Operator_Binary_Mul_pm");
	}

	public void testDTL_Fast_Expression_Operator_Binary_Ne_pm()
	{
		doTest("DTL_Fast_Expression_Operator_Binary_Ne_pm");
	}

	public void testDTL_Fast_Expression_Operator_Binary_NotIn_pm()
	{
		doTest("DTL_Fast_Expression_Operator_Binary_NotIn_pm");
	}

	public void testDTL_Fast_Expression_Operator_Binary_Or_pm()
	{
		doTest("DTL_Fast_Expression_Operator_Binary_Or_pm");
	}

	public void testDTL_Fast_Expression_Operator_Binary_Plus_pm()
	{
		doTest("DTL_Fast_Expression_Operator_Binary_Plus_pm");
	}

	public void testDTL_Fast_Expression_Operator_Binary_Pow_pm()
	{
		doTest("DTL_Fast_Expression_Operator_Binary_Pow_pm");
	}

	public void testDTL_Fast_Expression_Operator_Binary_pm()
	{
		doTest("DTL_Fast_Expression_Operator_Binary_pm");
	}

	public void testDTL_Fast_Expression_Operator_Unary_Defined_pm()
	{
		doTest("DTL_Fast_Expression_Operator_Unary_Defined_pm");
	}

	public void testDTL_Fast_Expression_Operator_Unary_Logical_pm()
	{
		doTest("DTL_Fast_Expression_Operator_Unary_Logical_pm");
	}

	public void testDTL_Fast_Expression_Operator_Unary_Not_pm()
	{
		doTest("DTL_Fast_Expression_Operator_Unary_Not_pm");
	}

	public void testDTL_Fast_Expression_Operator_Unary_pm()
	{
		doTest("DTL_Fast_Expression_Operator_Unary_pm");
	}

	public void testDTL_Fast_Expression_Operator_pm()
	{
		doTest("DTL_Fast_Expression_Operator_pm");
	}

	public void testDTL_Fast_Expression_pm()
	{
		doTest("DTL_Fast_Expression_pm");
	}

	public void testDTL_Fast_FilterManager_pm()
	{
		doTest("DTL_Fast_FilterManager_pm");
	}

	public void testDTL_Fast_Filter_Add_pm()
	{
		doTest("DTL_Fast_Filter_Add_pm");
	}

	public void testDTL_Fast_Filter_Addslashes_pm()
	{
		doTest("DTL_Fast_Filter_Addslashes_pm");
	}

	public void testDTL_Fast_Filter_Capfirst_pm()
	{
		doTest("DTL_Fast_Filter_Capfirst_pm");
	}

	public void testDTL_Fast_Filter_Center_pm()
	{
		doTest("DTL_Fast_Filter_Center_pm");
	}

	public void testDTL_Fast_Filter_Cut_pm()
	{
		doTest("DTL_Fast_Filter_Cut_pm");
	}

	public void testDTL_Fast_Filter_Date_pm()
	{
		doTest("DTL_Fast_Filter_Date_pm");
	}

	public void testDTL_Fast_Filter_DefaultIfNone_pm()
	{
		doTest("DTL_Fast_Filter_DefaultIfNone_pm");
	}

	public void testDTL_Fast_Filter_Default_pm()
	{
		doTest("DTL_Fast_Filter_Default_pm");
	}

	public void testDTL_Fast_Filter_Dictsort_pm()
	{
		doTest("DTL_Fast_Filter_Dictsort_pm");
	}

	public void testDTL_Fast_Filter_Dictsortreversed_pm()
	{
		doTest("DTL_Fast_Filter_Dictsortreversed_pm");
	}

	public void testDTL_Fast_Filter_Divisibleby_pm()
	{
		doTest("DTL_Fast_Filter_Divisibleby_pm");
	}

	public void testDTL_Fast_Filter_Escape_pm()
	{
		doTest("DTL_Fast_Filter_Escape_pm");
	}

	public void testDTL_Fast_Filter_Escapejs_pm()
	{
		doTest("DTL_Fast_Filter_Escapejs_pm");
	}

	public void testDTL_Fast_Filter_Filesizeformat_pm()
	{
		doTest("DTL_Fast_Filter_Filesizeformat_pm");
	}

	public void testDTL_Fast_Filter_First_pm()
	{
		doTest("DTL_Fast_Filter_First_pm");
	}

	public void testDTL_Fast_Filter_Floatformat_pm()
	{
		doTest("DTL_Fast_Filter_Floatformat_pm");
	}

	public void testDTL_Fast_Filter_Getdigit_pm()
	{
		doTest("DTL_Fast_Filter_Getdigit_pm");
	}

	public void testDTL_Fast_Filter_Iriencode_pm()
	{
		doTest("DTL_Fast_Filter_Iriencode_pm");
	}

	public void testDTL_Fast_Filter_Join_pm()
	{
		doTest("DTL_Fast_Filter_Join_pm");
	}

	public void testDTL_Fast_Filter_Last_pm()
	{
		doTest("DTL_Fast_Filter_Last_pm");
	}

	public void testDTL_Fast_Filter_Length_pm()
	{
		doTest("DTL_Fast_Filter_Length_pm");
	}

	public void testDTL_Fast_Filter_Lengthis_pm()
	{
		doTest("DTL_Fast_Filter_Lengthis_pm");
	}

	public void testDTL_Fast_Filter_Linebreaks_pm()
	{
		doTest("DTL_Fast_Filter_Linebreaks_pm");
	}

	public void testDTL_Fast_Filter_Linebreaksbr_pm()
	{
		doTest("DTL_Fast_Filter_Linebreaksbr_pm");
	}

	public void testDTL_Fast_Filter_Linenumbers_pm()
	{
		doTest("DTL_Fast_Filter_Linenumbers_pm");
	}

	public void testDTL_Fast_Filter_Ljust_pm()
	{
		doTest("DTL_Fast_Filter_Ljust_pm");
	}

	public void testDTL_Fast_Filter_Lower_pm()
	{
		doTest("DTL_Fast_Filter_Lower_pm");
	}

	public void testDTL_Fast_Filter_MakeList_pm()
	{
		doTest("DTL_Fast_Filter_MakeList_pm");
	}

	public void testDTL_Fast_Filter_Numberformat_pm()
	{
		doTest("DTL_Fast_Filter_Numberformat_pm");
	}

	public void testDTL_Fast_Filter_PhoneToNumeric_pm()
	{
		doTest("DTL_Fast_Filter_PhoneToNumeric_pm");
	}

	public void testDTL_Fast_Filter_Pluralize_pm()
	{
		doTest("DTL_Fast_Filter_Pluralize_pm");
	}

	public void testDTL_Fast_Filter_Random_pm()
	{
		doTest("DTL_Fast_Filter_Random_pm");
	}

	public void testDTL_Fast_Filter_Removetags_pm()
	{
		doTest("DTL_Fast_Filter_Removetags_pm");
	}

	public void testDTL_Fast_Filter_Reverse_pm()
	{
		doTest("DTL_Fast_Filter_Reverse_pm");
	}

	public void testDTL_Fast_Filter_Rjust_pm()
	{
		doTest("DTL_Fast_Filter_Rjust_pm");
	}

	public void testDTL_Fast_Filter_Ru_Pluralize_pm()
	{
		doTest("DTL_Fast_Filter_Ru_Pluralize_pm");
	}

	public void testDTL_Fast_Filter_SafeSeq_pm()
	{
		doTest("DTL_Fast_Filter_SafeSeq_pm");
	}

	public void testDTL_Fast_Filter_Safe_pm()
	{
		doTest("DTL_Fast_Filter_Safe_pm");
	}

	public void testDTL_Fast_Filter_Slice_pm()
	{
		doTest("DTL_Fast_Filter_Slice_pm");
	}

	public void testDTL_Fast_Filter_Slugify_pm()
	{
		doTest("DTL_Fast_Filter_Slugify_pm");
	}

	public void testDTL_Fast_Filter_Split_pm()
	{
		doTest("DTL_Fast_Filter_Split_pm");
	}

	public void testDTL_Fast_Filter_Strftime_pm()
	{
		doTest("DTL_Fast_Filter_Strftime_pm");
	}

	public void testDTL_Fast_Filter_Stringformat_pm()
	{
		doTest("DTL_Fast_Filter_Stringformat_pm");
	}

	public void testDTL_Fast_Filter_Striptags_pm()
	{
		doTest("DTL_Fast_Filter_Striptags_pm");
	}

	public void testDTL_Fast_Filter_Time_pm()
	{
		doTest("DTL_Fast_Filter_Time_pm");
	}

	public void testDTL_Fast_Filter_Timesince_pm()
	{
		doTest("DTL_Fast_Filter_Timesince_pm");
	}

	public void testDTL_Fast_Filter_Timeuntil_pm()
	{
		doTest("DTL_Fast_Filter_Timeuntil_pm");
	}

	public void testDTL_Fast_Filter_Title_pm()
	{
		doTest("DTL_Fast_Filter_Title_pm");
	}

	public void testDTL_Fast_Filter_Truncatechars_pm()
	{
		doTest("DTL_Fast_Filter_Truncatechars_pm");
	}

	public void testDTL_Fast_Filter_Truncatecharshtml_pm()
	{
		doTest("DTL_Fast_Filter_Truncatecharshtml_pm");
	}

	public void testDTL_Fast_Filter_Truncatewords_pm()
	{
		doTest("DTL_Fast_Filter_Truncatewords_pm");
	}

	public void testDTL_Fast_Filter_Truncatewordshtml_pm()
	{
		doTest("DTL_Fast_Filter_Truncatewordshtml_pm");
	}

	public void testDTL_Fast_Filter_Unorderedlist_pm()
	{
		doTest("DTL_Fast_Filter_Unorderedlist_pm");
	}

	public void testDTL_Fast_Filter_Upper_pm()
	{
		doTest("DTL_Fast_Filter_Upper_pm");
	}

	public void testDTL_Fast_Filter_Urlencode_pm()
	{
		doTest("DTL_Fast_Filter_Urlencode_pm");
	}

	public void testDTL_Fast_Filter_Urlize_pm()
	{
		doTest("DTL_Fast_Filter_Urlize_pm");
	}

	public void testDTL_Fast_Filter_Urlizetrunc_pm()
	{
		doTest("DTL_Fast_Filter_Urlizetrunc_pm");
	}

	public void testDTL_Fast_Filter_Wordcount_pm()
	{
		doTest("DTL_Fast_Filter_Wordcount_pm");
	}

	public void testDTL_Fast_Filter_Wordwrap_pm()
	{
		doTest("DTL_Fast_Filter_Wordwrap_pm");
	}

	public void testDTL_Fast_Filter_Yesno_pm()
	{
		doTest("DTL_Fast_Filter_Yesno_pm");
	}

	public void testDTL_Fast_Filter_pm()
	{
		doTest("DTL_Fast_Filter_pm");
	}

	public void testDTL_Fast_Filters_pm()
	{
		doTest("DTL_Fast_Filters_pm");
	}

	public void testDTL_Fast_Parser_pm()
	{
		doTest("DTL_Fast_Parser_pm");
	}

	public void testDTL_Fast_Renderer_pm()
	{
		doTest("DTL_Fast_Renderer_pm");
	}

	public void testDTL_Fast_Replacer_Replacement_pm()
	{
		doTest("DTL_Fast_Replacer_Replacement_pm");
	}

	public void testDTL_Fast_Replacer_pm()
	{
		doTest("DTL_Fast_Replacer_pm");
	}

	public void testDTL_Fast_Tag_Autoescape_pm()
	{
		doTest("DTL_Fast_Tag_Autoescape_pm");
	}

	public void testDTL_Fast_Tag_BlockSuper_pm()
	{
		doTest("DTL_Fast_Tag_BlockSuper_pm");
	}

	public void testDTL_Fast_Tag_Block_pm()
	{
		doTest("DTL_Fast_Tag_Block_pm");
	}

	public void testDTL_Fast_Tag_Comment_pm()
	{
		doTest("DTL_Fast_Tag_Comment_pm");
	}

	public void testDTL_Fast_Tag_Cycle_pm()
	{
		doTest("DTL_Fast_Tag_Cycle_pm");
	}

	public void testDTL_Fast_Tag_Debug_pm()
	{
		doTest("DTL_Fast_Tag_Debug_pm");
	}

	public void testDTL_Fast_Tag_DumpHTML_pm()
	{
		doTest("DTL_Fast_Tag_DumpHTML_pm");
	}

	public void testDTL_Fast_Tag_DumpWarn_pm()
	{
		doTest("DTL_Fast_Tag_DumpWarn_pm");
	}

	public void testDTL_Fast_Tag_Dump_pm()
	{
		doTest("DTL_Fast_Tag_Dump_pm");
	}

	public void testDTL_Fast_Tag_Extends_pm()
	{
		doTest("DTL_Fast_Tag_Extends_pm");
	}

	public void testDTL_Fast_Tag_Filter_pm()
	{
		doTest("DTL_Fast_Tag_Filter_pm");
	}

	public void testDTL_Fast_Tag_Firstof_pm()
	{
		doTest("DTL_Fast_Tag_Firstof_pm");
	}

	public void testDTL_Fast_Tag_Firstofdefined_pm()
	{
		doTest("DTL_Fast_Tag_Firstofdefined_pm");
	}

	public void testDTL_Fast_Tag_For_pm()
	{
		doTest("DTL_Fast_Tag_For_pm");
	}

	public void testDTL_Fast_Tag_If_Condition_pm()
	{
		doTest("DTL_Fast_Tag_If_Condition_pm");
	}

	public void testDTL_Fast_Tag_If_pm()
	{
		doTest("DTL_Fast_Tag_If_pm");
	}

	public void testDTL_Fast_Tag_Ifchanged_pm()
	{
		doTest("DTL_Fast_Tag_Ifchanged_pm");
	}

	public void testDTL_Fast_Tag_Ifequal_pm()
	{
		doTest("DTL_Fast_Tag_Ifequal_pm");
	}

	public void testDTL_Fast_Tag_Ifnotequal_pm()
	{
		doTest("DTL_Fast_Tag_Ifnotequal_pm");
	}

	public void testDTL_Fast_Tag_Include_pm()
	{
		doTest("DTL_Fast_Tag_Include_pm");
	}

	public void testDTL_Fast_Tag_Load_pm()
	{
		doTest("DTL_Fast_Tag_Load_pm");
	}

	public void testDTL_Fast_Tag_Now_pm()
	{
		doTest("DTL_Fast_Tag_Now_pm");
	}

	public void testDTL_Fast_Tag_Regroup_pm()
	{
		doTest("DTL_Fast_Tag_Regroup_pm");
	}

	public void testDTL_Fast_Tag_Simple_pm()
	{
		doTest("DTL_Fast_Tag_Simple_pm");
	}

	public void testDTL_Fast_Tag_Spaceless_pm()
	{
		doTest("DTL_Fast_Tag_Spaceless_pm");
	}

	public void testDTL_Fast_Tag_Sprintf_pm()
	{
		doTest("DTL_Fast_Tag_Sprintf_pm");
	}

	public void testDTL_Fast_Tag_Ssi_pm()
	{
		doTest("DTL_Fast_Tag_Ssi_pm");
	}

	public void testDTL_Fast_Tag_Templatetag_pm()
	{
		doTest("DTL_Fast_Tag_Templatetag_pm");
	}

	public void testDTL_Fast_Tag_Url_pm()
	{
		doTest("DTL_Fast_Tag_Url_pm");
	}

	public void testDTL_Fast_Tag_Verbatim_pm()
	{
		doTest("DTL_Fast_Tag_Verbatim_pm");
	}

	public void testDTL_Fast_Tag_Widthratio_pm()
	{
		doTest("DTL_Fast_Tag_Widthratio_pm");
	}

	public void testDTL_Fast_Tag_With_pm()
	{
		doTest("DTL_Fast_Tag_With_pm");
	}

	public void testDTL_Fast_Tag_pm()
	{
		doTest("DTL_Fast_Tag_pm");
	}

	public void testDTL_Fast_Tags_pm()
	{
		doTest("DTL_Fast_Tags_pm");
	}

	public void testDTL_Fast_Template_pm()
	{
		doTest("DTL_Fast_Template_pm");
	}

	public void testDTL_Fast_Text_pm()
	{
		doTest("DTL_Fast_Text_pm");
	}

	public void testDTL_Fast_Utils_pm()
	{
		doTest("DTL_Fast_Utils_pm");
	}

	public void testDTL_Fast_Variable_pm()
	{
		doTest("DTL_Fast_Variable_pm");
	}

	public void testDTL_Fast_pm()
	{
		doTest("DTL_Fast_pm");
	}

	public void testData_Buffer_pm()
	{
		doTest("Data_Buffer_pm");
	}

	public void testData_Dump_FilterContext_pm()
	{
		doTest("Data_Dump_FilterContext_pm");
	}

	public void testData_Dump_Filtered_pm()
	{
		doTest("Data_Dump_Filtered_pm");
	}

	public void testData_Dump_Streamer___Printers_pm()
	{
		doTest("Data_Dump_Streamer___Printers_pm");
	}

	public void testData_Dump_Streamer_pm()
	{
		doTest("Data_Dump_Streamer_pm");
	}

	public void testData_Dump_Trace_pm()
	{
		doTest("Data_Dump_Trace_pm");
	}

	public void testData_Dump_pm()
	{
		doTest("Data_Dump_pm");
	}

	public void testData_Dumper_Concise_Sugar_pm()
	{
		doTest("Data_Dumper_Concise_Sugar_pm");
	}

	public void testData_Dumper_Concise_pm()
	{
		doTest("Data_Dumper_Concise_pm");
	}

	public void testData_Dumper_pm()
	{
		doTest("Data_Dumper_pm");
	}

	public void testData_OptList_pm()
	{
		doTest("Data_OptList_pm");
	}

	public void testData_Page_pm()
	{
		doTest("Data_Page_pm");
	}

	public void testData_Printer_Filter_DB_pm()
	{
		doTest("Data_Printer_Filter_DB_pm");
	}

	public void testData_Printer_Filter_DateTime_pm()
	{
		doTest("Data_Printer_Filter_DateTime_pm");
	}

	public void testData_Printer_Filter_Digest_pm()
	{
		doTest("Data_Printer_Filter_Digest_pm");
	}

	public void testData_Printer_Filter_pm()
	{
		doTest("Data_Printer_Filter_pm");
	}

	public void testData_Printer_pm()
	{
		doTest("Data_Printer_pm");
	}

	public void testData_Random_WordList_pm()
	{
		doTest("Data_Random_WordList_pm");
	}

	public void testData_Random_pm()
	{
		doTest("Data_Random_pm");
	}

	public void testData_Section_pm()
	{
		doTest("Data_Section_pm");
	}

	public void testDateTime_Duration_pm()
	{
		doTest("DateTime_Duration_pm");
	}

	public void testDateTime_Format_DateParse_pm()
	{
		doTest("DateTime_Format_DateParse_pm");
	}

	public void testDateTime_Helpers_pm()
	{
		doTest("DateTime_Helpers_pm");
	}

	public void testDateTime_Infinite_pm()
	{
		doTest("DateTime_Infinite_pm");
	}

	public void testDateTime_LeapSecond_pm()
	{
		doTest("DateTime_LeapSecond_pm");
	}

	public void testDateTime_PPExtra_pm()
	{
		doTest("DateTime_PPExtra_pm");
	}

	public void testDateTime_PP_pm()
	{
		doTest("DateTime_PP_pm");
	}

	public void testDateTime_TimeZone_Africa_Abidjan_pm()
	{
		doTest("DateTime_TimeZone_Africa_Abidjan_pm");
	}

	public void testDateTime_TimeZone_Africa_Accra_pm()
	{
		doTest("DateTime_TimeZone_Africa_Accra_pm");
	}

	public void testDateTime_TimeZone_Africa_Algiers_pm()
	{
		doTest("DateTime_TimeZone_Africa_Algiers_pm");
	}

	public void testDateTime_TimeZone_Africa_Bissau_pm()
	{
		doTest("DateTime_TimeZone_Africa_Bissau_pm");
	}

	public void testDateTime_TimeZone_Africa_Cairo_pm()
	{
		doTest("DateTime_TimeZone_Africa_Cairo_pm");
	}

	public void testDateTime_TimeZone_Africa_Casablanca_pm()
	{
		doTest("DateTime_TimeZone_Africa_Casablanca_pm");
	}

	public void testDateTime_TimeZone_Africa_Ceuta_pm()
	{
		doTest("DateTime_TimeZone_Africa_Ceuta_pm");
	}

	public void testDateTime_TimeZone_Africa_El_Aaiun_pm()
	{
		doTest("DateTime_TimeZone_Africa_El_Aaiun_pm");
	}

	public void testDateTime_TimeZone_Africa_Johannesburg_pm()
	{
		doTest("DateTime_TimeZone_Africa_Johannesburg_pm");
	}

	public void testDateTime_TimeZone_Africa_Khartoum_pm()
	{
		doTest("DateTime_TimeZone_Africa_Khartoum_pm");
	}

	public void testDateTime_TimeZone_Africa_Lagos_pm()
	{
		doTest("DateTime_TimeZone_Africa_Lagos_pm");
	}

	public void testDateTime_TimeZone_Africa_Maputo_pm()
	{
		doTest("DateTime_TimeZone_Africa_Maputo_pm");
	}

	public void testDateTime_TimeZone_Africa_Monrovia_pm()
	{
		doTest("DateTime_TimeZone_Africa_Monrovia_pm");
	}

	public void testDateTime_TimeZone_Africa_Nairobi_pm()
	{
		doTest("DateTime_TimeZone_Africa_Nairobi_pm");
	}

	public void testDateTime_TimeZone_Africa_Ndjamena_pm()
	{
		doTest("DateTime_TimeZone_Africa_Ndjamena_pm");
	}

	public void testDateTime_TimeZone_Africa_Tripoli_pm()
	{
		doTest("DateTime_TimeZone_Africa_Tripoli_pm");
	}

	public void testDateTime_TimeZone_Africa_Tunis_pm()
	{
		doTest("DateTime_TimeZone_Africa_Tunis_pm");
	}

	public void testDateTime_TimeZone_Africa_Windhoek_pm()
	{
		doTest("DateTime_TimeZone_Africa_Windhoek_pm");
	}

	public void testDateTime_TimeZone_America_Adak_pm()
	{
		doTest("DateTime_TimeZone_America_Adak_pm");
	}

	public void testDateTime_TimeZone_America_Anchorage_pm()
	{
		doTest("DateTime_TimeZone_America_Anchorage_pm");
	}

	public void testDateTime_TimeZone_America_Araguaina_pm()
	{
		doTest("DateTime_TimeZone_America_Araguaina_pm");
	}

	public void testDateTime_TimeZone_America_Argentina_Buenos_Aires_pm()
	{
		doTest("DateTime_TimeZone_America_Argentina_Buenos_Aires_pm");
	}

	public void testDateTime_TimeZone_America_Argentina_Catamarca_pm()
	{
		doTest("DateTime_TimeZone_America_Argentina_Catamarca_pm");
	}

	public void testDateTime_TimeZone_America_Argentina_Cordoba_pm()
	{
		doTest("DateTime_TimeZone_America_Argentina_Cordoba_pm");
	}

	public void testDateTime_TimeZone_America_Argentina_Jujuy_pm()
	{
		doTest("DateTime_TimeZone_America_Argentina_Jujuy_pm");
	}

	public void testDateTime_TimeZone_America_Argentina_La_Rioja_pm()
	{
		doTest("DateTime_TimeZone_America_Argentina_La_Rioja_pm");
	}

	public void testDateTime_TimeZone_America_Argentina_Mendoza_pm()
	{
		doTest("DateTime_TimeZone_America_Argentina_Mendoza_pm");
	}

	public void testDateTime_TimeZone_America_Argentina_Rio_Gallegos_pm()
	{
		doTest("DateTime_TimeZone_America_Argentina_Rio_Gallegos_pm");
	}

	public void testDateTime_TimeZone_America_Argentina_Salta_pm()
	{
		doTest("DateTime_TimeZone_America_Argentina_Salta_pm");
	}

	public void testDateTime_TimeZone_America_Argentina_San_Juan_pm()
	{
		doTest("DateTime_TimeZone_America_Argentina_San_Juan_pm");
	}

	public void testDateTime_TimeZone_America_Argentina_San_Luis_pm()
	{
		doTest("DateTime_TimeZone_America_Argentina_San_Luis_pm");
	}

	public void testDateTime_TimeZone_America_Argentina_Tucuman_pm()
	{
		doTest("DateTime_TimeZone_America_Argentina_Tucuman_pm");
	}

	public void testDateTime_TimeZone_America_Argentina_Ushuaia_pm()
	{
		doTest("DateTime_TimeZone_America_Argentina_Ushuaia_pm");
	}

	public void testDateTime_TimeZone_America_Asuncion_pm()
	{
		doTest("DateTime_TimeZone_America_Asuncion_pm");
	}

	public void testDateTime_TimeZone_America_Atikokan_pm()
	{
		doTest("DateTime_TimeZone_America_Atikokan_pm");
	}

	public void testDateTime_TimeZone_America_Bahia_Banderas_pm()
	{
		doTest("DateTime_TimeZone_America_Bahia_Banderas_pm");
	}

	public void testDateTime_TimeZone_America_Bahia_pm()
	{
		doTest("DateTime_TimeZone_America_Bahia_pm");
	}

	public void testDateTime_TimeZone_America_Barbados_pm()
	{
		doTest("DateTime_TimeZone_America_Barbados_pm");
	}

	public void testDateTime_TimeZone_America_Belem_pm()
	{
		doTest("DateTime_TimeZone_America_Belem_pm");
	}

	public void testDateTime_TimeZone_America_Belize_pm()
	{
		doTest("DateTime_TimeZone_America_Belize_pm");
	}

	public void testDateTime_TimeZone_America_Blanc_Sablon_pm()
	{
		doTest("DateTime_TimeZone_America_Blanc_Sablon_pm");
	}

	public void testDateTime_TimeZone_America_Boa_Vista_pm()
	{
		doTest("DateTime_TimeZone_America_Boa_Vista_pm");
	}

	public void testDateTime_TimeZone_America_Bogota_pm()
	{
		doTest("DateTime_TimeZone_America_Bogota_pm");
	}

	public void testDateTime_TimeZone_America_Boise_pm()
	{
		doTest("DateTime_TimeZone_America_Boise_pm");
	}

	public void testDateTime_TimeZone_America_Cambridge_Bay_pm()
	{
		doTest("DateTime_TimeZone_America_Cambridge_Bay_pm");
	}

	public void testDateTime_TimeZone_America_Campo_Grande_pm()
	{
		doTest("DateTime_TimeZone_America_Campo_Grande_pm");
	}

	public void testDateTime_TimeZone_America_Cancun_pm()
	{
		doTest("DateTime_TimeZone_America_Cancun_pm");
	}

	public void testDateTime_TimeZone_America_Caracas_pm()
	{
		doTest("DateTime_TimeZone_America_Caracas_pm");
	}

	public void testDateTime_TimeZone_America_Cayenne_pm()
	{
		doTest("DateTime_TimeZone_America_Cayenne_pm");
	}

	public void testDateTime_TimeZone_America_Chicago_pm()
	{
		doTest("DateTime_TimeZone_America_Chicago_pm");
	}

	public void testDateTime_TimeZone_America_Chihuahua_pm()
	{
		doTest("DateTime_TimeZone_America_Chihuahua_pm");
	}

	public void testDateTime_TimeZone_America_Costa_Rica_pm()
	{
		doTest("DateTime_TimeZone_America_Costa_Rica_pm");
	}

	public void testDateTime_TimeZone_America_Creston_pm()
	{
		doTest("DateTime_TimeZone_America_Creston_pm");
	}

	public void testDateTime_TimeZone_America_Cuiaba_pm()
	{
		doTest("DateTime_TimeZone_America_Cuiaba_pm");
	}

	public void testDateTime_TimeZone_America_Curacao_pm()
	{
		doTest("DateTime_TimeZone_America_Curacao_pm");
	}

	public void testDateTime_TimeZone_America_Danmarkshavn_pm()
	{
		doTest("DateTime_TimeZone_America_Danmarkshavn_pm");
	}

	public void testDateTime_TimeZone_America_Dawson_Creek_pm()
	{
		doTest("DateTime_TimeZone_America_Dawson_Creek_pm");
	}

	public void testDateTime_TimeZone_America_Dawson_pm()
	{
		doTest("DateTime_TimeZone_America_Dawson_pm");
	}

	public void testDateTime_TimeZone_America_Denver_pm()
	{
		doTest("DateTime_TimeZone_America_Denver_pm");
	}

	public void testDateTime_TimeZone_America_Detroit_pm()
	{
		doTest("DateTime_TimeZone_America_Detroit_pm");
	}

	public void testDateTime_TimeZone_America_Edmonton_pm()
	{
		doTest("DateTime_TimeZone_America_Edmonton_pm");
	}

	public void testDateTime_TimeZone_America_Eirunepe_pm()
	{
		doTest("DateTime_TimeZone_America_Eirunepe_pm");
	}

	public void testDateTime_TimeZone_America_El_Salvador_pm()
	{
		doTest("DateTime_TimeZone_America_El_Salvador_pm");
	}

	public void testDateTime_TimeZone_America_Fortaleza_pm()
	{
		doTest("DateTime_TimeZone_America_Fortaleza_pm");
	}

	public void testDateTime_TimeZone_America_Glace_Bay_pm()
	{
		doTest("DateTime_TimeZone_America_Glace_Bay_pm");
	}

	public void testDateTime_TimeZone_America_Godthab_pm()
	{
		doTest("DateTime_TimeZone_America_Godthab_pm");
	}

	public void testDateTime_TimeZone_America_Goose_Bay_pm()
	{
		doTest("DateTime_TimeZone_America_Goose_Bay_pm");
	}

	public void testDateTime_TimeZone_America_Grand_Turk_pm()
	{
		doTest("DateTime_TimeZone_America_Grand_Turk_pm");
	}

	public void testDateTime_TimeZone_America_Guatemala_pm()
	{
		doTest("DateTime_TimeZone_America_Guatemala_pm");
	}

	public void testDateTime_TimeZone_America_Guayaquil_pm()
	{
		doTest("DateTime_TimeZone_America_Guayaquil_pm");
	}

	public void testDateTime_TimeZone_America_Guyana_pm()
	{
		doTest("DateTime_TimeZone_America_Guyana_pm");
	}

	public void testDateTime_TimeZone_America_Halifax_pm()
	{
		doTest("DateTime_TimeZone_America_Halifax_pm");
	}

	public void testDateTime_TimeZone_America_Havana_pm()
	{
		doTest("DateTime_TimeZone_America_Havana_pm");
	}

	public void testDateTime_TimeZone_America_Hermosillo_pm()
	{
		doTest("DateTime_TimeZone_America_Hermosillo_pm");
	}

	public void testDateTime_TimeZone_America_Indiana_Indianapolis_pm()
	{
		doTest("DateTime_TimeZone_America_Indiana_Indianapolis_pm");
	}

	public void testDateTime_TimeZone_America_Indiana_Knox_pm()
	{
		doTest("DateTime_TimeZone_America_Indiana_Knox_pm");
	}

	public void testDateTime_TimeZone_America_Indiana_Marengo_pm()
	{
		doTest("DateTime_TimeZone_America_Indiana_Marengo_pm");
	}

	public void testDateTime_TimeZone_America_Indiana_Petersburg_pm()
	{
		doTest("DateTime_TimeZone_America_Indiana_Petersburg_pm");
	}

	public void testDateTime_TimeZone_America_Indiana_Tell_City_pm()
	{
		doTest("DateTime_TimeZone_America_Indiana_Tell_City_pm");
	}

	public void testDateTime_TimeZone_America_Indiana_Vevay_pm()
	{
		doTest("DateTime_TimeZone_America_Indiana_Vevay_pm");
	}

	public void testDateTime_TimeZone_America_Indiana_Vincennes_pm()
	{
		doTest("DateTime_TimeZone_America_Indiana_Vincennes_pm");
	}

	public void testDateTime_TimeZone_America_Indiana_Winamac_pm()
	{
		doTest("DateTime_TimeZone_America_Indiana_Winamac_pm");
	}

	public void testDateTime_TimeZone_America_Inuvik_pm()
	{
		doTest("DateTime_TimeZone_America_Inuvik_pm");
	}

	public void testDateTime_TimeZone_America_Iqaluit_pm()
	{
		doTest("DateTime_TimeZone_America_Iqaluit_pm");
	}

	public void testDateTime_TimeZone_America_Jamaica_pm()
	{
		doTest("DateTime_TimeZone_America_Jamaica_pm");
	}

	public void testDateTime_TimeZone_America_Juneau_pm()
	{
		doTest("DateTime_TimeZone_America_Juneau_pm");
	}

	public void testDateTime_TimeZone_America_Kentucky_Louisville_pm()
	{
		doTest("DateTime_TimeZone_America_Kentucky_Louisville_pm");
	}

	public void testDateTime_TimeZone_America_Kentucky_Monticello_pm()
	{
		doTest("DateTime_TimeZone_America_Kentucky_Monticello_pm");
	}

	public void testDateTime_TimeZone_America_La_Paz_pm()
	{
		doTest("DateTime_TimeZone_America_La_Paz_pm");
	}

	public void testDateTime_TimeZone_America_Lima_pm()
	{
		doTest("DateTime_TimeZone_America_Lima_pm");
	}

	public void testDateTime_TimeZone_America_Los_Angeles_pm()
	{
		doTest("DateTime_TimeZone_America_Los_Angeles_pm");
	}

	public void testDateTime_TimeZone_America_Maceio_pm()
	{
		doTest("DateTime_TimeZone_America_Maceio_pm");
	}

	public void testDateTime_TimeZone_America_Managua_pm()
	{
		doTest("DateTime_TimeZone_America_Managua_pm");
	}

	public void testDateTime_TimeZone_America_Manaus_pm()
	{
		doTest("DateTime_TimeZone_America_Manaus_pm");
	}

	public void testDateTime_TimeZone_America_Martinique_pm()
	{
		doTest("DateTime_TimeZone_America_Martinique_pm");
	}

	public void testDateTime_TimeZone_America_Matamoros_pm()
	{
		doTest("DateTime_TimeZone_America_Matamoros_pm");
	}

	public void testDateTime_TimeZone_America_Mazatlan_pm()
	{
		doTest("DateTime_TimeZone_America_Mazatlan_pm");
	}

	public void testDateTime_TimeZone_America_Menominee_pm()
	{
		doTest("DateTime_TimeZone_America_Menominee_pm");
	}

	public void testDateTime_TimeZone_America_Merida_pm()
	{
		doTest("DateTime_TimeZone_America_Merida_pm");
	}

	public void testDateTime_TimeZone_America_Metlakatla_pm()
	{
		doTest("DateTime_TimeZone_America_Metlakatla_pm");
	}

	public void testDateTime_TimeZone_America_Mexico_City_pm()
	{
		doTest("DateTime_TimeZone_America_Mexico_City_pm");
	}

	public void testDateTime_TimeZone_America_Miquelon_pm()
	{
		doTest("DateTime_TimeZone_America_Miquelon_pm");
	}

	public void testDateTime_TimeZone_America_Moncton_pm()
	{
		doTest("DateTime_TimeZone_America_Moncton_pm");
	}

	public void testDateTime_TimeZone_America_Monterrey_pm()
	{
		doTest("DateTime_TimeZone_America_Monterrey_pm");
	}

	public void testDateTime_TimeZone_America_Montevideo_pm()
	{
		doTest("DateTime_TimeZone_America_Montevideo_pm");
	}

	public void testDateTime_TimeZone_America_Nassau_pm()
	{
		doTest("DateTime_TimeZone_America_Nassau_pm");
	}

	public void testDateTime_TimeZone_America_New_York_pm()
	{
		doTest("DateTime_TimeZone_America_New_York_pm");
	}

	public void testDateTime_TimeZone_America_Nipigon_pm()
	{
		doTest("DateTime_TimeZone_America_Nipigon_pm");
	}

	public void testDateTime_TimeZone_America_Nome_pm()
	{
		doTest("DateTime_TimeZone_America_Nome_pm");
	}

	public void testDateTime_TimeZone_America_Noronha_pm()
	{
		doTest("DateTime_TimeZone_America_Noronha_pm");
	}

	public void testDateTime_TimeZone_America_North_Dakota_Beulah_pm()
	{
		doTest("DateTime_TimeZone_America_North_Dakota_Beulah_pm");
	}

	public void testDateTime_TimeZone_America_North_Dakota_Center_pm()
	{
		doTest("DateTime_TimeZone_America_North_Dakota_Center_pm");
	}

	public void testDateTime_TimeZone_America_North_Dakota_New_Salem_pm()
	{
		doTest("DateTime_TimeZone_America_North_Dakota_New_Salem_pm");
	}

	public void testDateTime_TimeZone_America_Ojinaga_pm()
	{
		doTest("DateTime_TimeZone_America_Ojinaga_pm");
	}

	public void testDateTime_TimeZone_America_Panama_pm()
	{
		doTest("DateTime_TimeZone_America_Panama_pm");
	}

	public void testDateTime_TimeZone_America_Pangnirtung_pm()
	{
		doTest("DateTime_TimeZone_America_Pangnirtung_pm");
	}

	public void testDateTime_TimeZone_America_Paramaribo_pm()
	{
		doTest("DateTime_TimeZone_America_Paramaribo_pm");
	}

	public void testDateTime_TimeZone_America_Phoenix_pm()
	{
		doTest("DateTime_TimeZone_America_Phoenix_pm");
	}

	public void testDateTime_TimeZone_America_Port_au_Prince_pm()
	{
		doTest("DateTime_TimeZone_America_Port_au_Prince_pm");
	}

	public void testDateTime_TimeZone_America_Port_of_Spain_pm()
	{
		doTest("DateTime_TimeZone_America_Port_of_Spain_pm");
	}

	public void testDateTime_TimeZone_America_Porto_Velho_pm()
	{
		doTest("DateTime_TimeZone_America_Porto_Velho_pm");
	}

	public void testDateTime_TimeZone_America_Puerto_Rico_pm()
	{
		doTest("DateTime_TimeZone_America_Puerto_Rico_pm");
	}

	public void testDateTime_TimeZone_America_Rainy_River_pm()
	{
		doTest("DateTime_TimeZone_America_Rainy_River_pm");
	}

	public void testDateTime_TimeZone_America_Rankin_Inlet_pm()
	{
		doTest("DateTime_TimeZone_America_Rankin_Inlet_pm");
	}

	public void testDateTime_TimeZone_America_Recife_pm()
	{
		doTest("DateTime_TimeZone_America_Recife_pm");
	}

	public void testDateTime_TimeZone_America_Regina_pm()
	{
		doTest("DateTime_TimeZone_America_Regina_pm");
	}

	public void testDateTime_TimeZone_America_Resolute_pm()
	{
		doTest("DateTime_TimeZone_America_Resolute_pm");
	}

	public void testDateTime_TimeZone_America_Rio_Branco_pm()
	{
		doTest("DateTime_TimeZone_America_Rio_Branco_pm");
	}

	public void testDateTime_TimeZone_America_Santa_Isabel_pm()
	{
		doTest("DateTime_TimeZone_America_Santa_Isabel_pm");
	}

	public void testDateTime_TimeZone_America_Santarem_pm()
	{
		doTest("DateTime_TimeZone_America_Santarem_pm");
	}

	public void testDateTime_TimeZone_America_Santiago_pm()
	{
		doTest("DateTime_TimeZone_America_Santiago_pm");
	}

	public void testDateTime_TimeZone_America_Santo_Domingo_pm()
	{
		doTest("DateTime_TimeZone_America_Santo_Domingo_pm");
	}

	public void testDateTime_TimeZone_America_Sao_Paulo_pm()
	{
		doTest("DateTime_TimeZone_America_Sao_Paulo_pm");
	}

	public void testDateTime_TimeZone_America_Scoresbysund_pm()
	{
		doTest("DateTime_TimeZone_America_Scoresbysund_pm");
	}

	public void testDateTime_TimeZone_America_Sitka_pm()
	{
		doTest("DateTime_TimeZone_America_Sitka_pm");
	}

	public void testDateTime_TimeZone_America_St_Johns_pm()
	{
		doTest("DateTime_TimeZone_America_St_Johns_pm");
	}

	public void testDateTime_TimeZone_America_Swift_Current_pm()
	{
		doTest("DateTime_TimeZone_America_Swift_Current_pm");
	}

	public void testDateTime_TimeZone_America_Tegucigalpa_pm()
	{
		doTest("DateTime_TimeZone_America_Tegucigalpa_pm");
	}

	public void testDateTime_TimeZone_America_Thule_pm()
	{
		doTest("DateTime_TimeZone_America_Thule_pm");
	}

	public void testDateTime_TimeZone_America_Thunder_Bay_pm()
	{
		doTest("DateTime_TimeZone_America_Thunder_Bay_pm");
	}

	public void testDateTime_TimeZone_America_Tijuana_pm()
	{
		doTest("DateTime_TimeZone_America_Tijuana_pm");
	}

	public void testDateTime_TimeZone_America_Toronto_pm()
	{
		doTest("DateTime_TimeZone_America_Toronto_pm");
	}

	public void testDateTime_TimeZone_America_Vancouver_pm()
	{
		doTest("DateTime_TimeZone_America_Vancouver_pm");
	}

	public void testDateTime_TimeZone_America_Whitehorse_pm()
	{
		doTest("DateTime_TimeZone_America_Whitehorse_pm");
	}

	public void testDateTime_TimeZone_America_Winnipeg_pm()
	{
		doTest("DateTime_TimeZone_America_Winnipeg_pm");
	}

	public void testDateTime_TimeZone_America_Yakutat_pm()
	{
		doTest("DateTime_TimeZone_America_Yakutat_pm");
	}

	public void testDateTime_TimeZone_America_Yellowknife_pm()
	{
		doTest("DateTime_TimeZone_America_Yellowknife_pm");
	}

	public void testDateTime_TimeZone_Antarctica_Casey_pm()
	{
		doTest("DateTime_TimeZone_Antarctica_Casey_pm");
	}

	public void testDateTime_TimeZone_Antarctica_Davis_pm()
	{
		doTest("DateTime_TimeZone_Antarctica_Davis_pm");
	}

	public void testDateTime_TimeZone_Antarctica_DumontDUrville_pm()
	{
		doTest("DateTime_TimeZone_Antarctica_DumontDUrville_pm");
	}

	public void testDateTime_TimeZone_Antarctica_Macquarie_pm()
	{
		doTest("DateTime_TimeZone_Antarctica_Macquarie_pm");
	}

	public void testDateTime_TimeZone_Antarctica_Mawson_pm()
	{
		doTest("DateTime_TimeZone_Antarctica_Mawson_pm");
	}

	public void testDateTime_TimeZone_Antarctica_Palmer_pm()
	{
		doTest("DateTime_TimeZone_Antarctica_Palmer_pm");
	}

	public void testDateTime_TimeZone_Antarctica_Rothera_pm()
	{
		doTest("DateTime_TimeZone_Antarctica_Rothera_pm");
	}

	public void testDateTime_TimeZone_Antarctica_Syowa_pm()
	{
		doTest("DateTime_TimeZone_Antarctica_Syowa_pm");
	}

	public void testDateTime_TimeZone_Antarctica_Troll_pm()
	{
		doTest("DateTime_TimeZone_Antarctica_Troll_pm");
	}

	public void testDateTime_TimeZone_Antarctica_Vostok_pm()
	{
		doTest("DateTime_TimeZone_Antarctica_Vostok_pm");
	}

	public void testDateTime_TimeZone_Asia_Almaty_pm()
	{
		doTest("DateTime_TimeZone_Asia_Almaty_pm");
	}

	public void testDateTime_TimeZone_Asia_Amman_pm()
	{
		doTest("DateTime_TimeZone_Asia_Amman_pm");
	}

	public void testDateTime_TimeZone_Asia_Anadyr_pm()
	{
		doTest("DateTime_TimeZone_Asia_Anadyr_pm");
	}

	public void testDateTime_TimeZone_Asia_Aqtau_pm()
	{
		doTest("DateTime_TimeZone_Asia_Aqtau_pm");
	}

	public void testDateTime_TimeZone_Asia_Aqtobe_pm()
	{
		doTest("DateTime_TimeZone_Asia_Aqtobe_pm");
	}

	public void testDateTime_TimeZone_Asia_Ashgabat_pm()
	{
		doTest("DateTime_TimeZone_Asia_Ashgabat_pm");
	}

	public void testDateTime_TimeZone_Asia_Baghdad_pm()
	{
		doTest("DateTime_TimeZone_Asia_Baghdad_pm");
	}

	public void testDateTime_TimeZone_Asia_Baku_pm()
	{
		doTest("DateTime_TimeZone_Asia_Baku_pm");
	}

	public void testDateTime_TimeZone_Asia_Bangkok_pm()
	{
		doTest("DateTime_TimeZone_Asia_Bangkok_pm");
	}

	public void testDateTime_TimeZone_Asia_Beirut_pm()
	{
		doTest("DateTime_TimeZone_Asia_Beirut_pm");
	}

	public void testDateTime_TimeZone_Asia_Bishkek_pm()
	{
		doTest("DateTime_TimeZone_Asia_Bishkek_pm");
	}

	public void testDateTime_TimeZone_Asia_Brunei_pm()
	{
		doTest("DateTime_TimeZone_Asia_Brunei_pm");
	}

	public void testDateTime_TimeZone_Asia_Chita_pm()
	{
		doTest("DateTime_TimeZone_Asia_Chita_pm");
	}

	public void testDateTime_TimeZone_Asia_Choibalsan_pm()
	{
		doTest("DateTime_TimeZone_Asia_Choibalsan_pm");
	}

	public void testDateTime_TimeZone_Asia_Colombo_pm()
	{
		doTest("DateTime_TimeZone_Asia_Colombo_pm");
	}

	public void testDateTime_TimeZone_Asia_Damascus_pm()
	{
		doTest("DateTime_TimeZone_Asia_Damascus_pm");
	}

	public void testDateTime_TimeZone_Asia_Dhaka_pm()
	{
		doTest("DateTime_TimeZone_Asia_Dhaka_pm");
	}

	public void testDateTime_TimeZone_Asia_Dili_pm()
	{
		doTest("DateTime_TimeZone_Asia_Dili_pm");
	}

	public void testDateTime_TimeZone_Asia_Dubai_pm()
	{
		doTest("DateTime_TimeZone_Asia_Dubai_pm");
	}

	public void testDateTime_TimeZone_Asia_Dushanbe_pm()
	{
		doTest("DateTime_TimeZone_Asia_Dushanbe_pm");
	}

	public void testDateTime_TimeZone_Asia_Gaza_pm()
	{
		doTest("DateTime_TimeZone_Asia_Gaza_pm");
	}

	public void testDateTime_TimeZone_Asia_Hebron_pm()
	{
		doTest("DateTime_TimeZone_Asia_Hebron_pm");
	}

	public void testDateTime_TimeZone_Asia_Ho_Chi_Minh_pm()
	{
		doTest("DateTime_TimeZone_Asia_Ho_Chi_Minh_pm");
	}

	public void testDateTime_TimeZone_Asia_Hong_Kong_pm()
	{
		doTest("DateTime_TimeZone_Asia_Hong_Kong_pm");
	}

	public void testDateTime_TimeZone_Asia_Hovd_pm()
	{
		doTest("DateTime_TimeZone_Asia_Hovd_pm");
	}

	public void testDateTime_TimeZone_Asia_Irkutsk_pm()
	{
		doTest("DateTime_TimeZone_Asia_Irkutsk_pm");
	}

	public void testDateTime_TimeZone_Asia_Jakarta_pm()
	{
		doTest("DateTime_TimeZone_Asia_Jakarta_pm");
	}

	public void testDateTime_TimeZone_Asia_Jayapura_pm()
	{
		doTest("DateTime_TimeZone_Asia_Jayapura_pm");
	}

	public void testDateTime_TimeZone_Asia_Jerusalem_pm()
	{
		doTest("DateTime_TimeZone_Asia_Jerusalem_pm");
	}

	public void testDateTime_TimeZone_Asia_Kabul_pm()
	{
		doTest("DateTime_TimeZone_Asia_Kabul_pm");
	}

	public void testDateTime_TimeZone_Asia_Kamchatka_pm()
	{
		doTest("DateTime_TimeZone_Asia_Kamchatka_pm");
	}

	public void testDateTime_TimeZone_Asia_Karachi_pm()
	{
		doTest("DateTime_TimeZone_Asia_Karachi_pm");
	}

	public void testDateTime_TimeZone_Asia_Kathmandu_pm()
	{
		doTest("DateTime_TimeZone_Asia_Kathmandu_pm");
	}

	public void testDateTime_TimeZone_Asia_Khandyga_pm()
	{
		doTest("DateTime_TimeZone_Asia_Khandyga_pm");
	}

	public void testDateTime_TimeZone_Asia_Kolkata_pm()
	{
		doTest("DateTime_TimeZone_Asia_Kolkata_pm");
	}

	public void testDateTime_TimeZone_Asia_Krasnoyarsk_pm()
	{
		doTest("DateTime_TimeZone_Asia_Krasnoyarsk_pm");
	}

	public void testDateTime_TimeZone_Asia_Kuala_Lumpur_pm()
	{
		doTest("DateTime_TimeZone_Asia_Kuala_Lumpur_pm");
	}

	public void testDateTime_TimeZone_Asia_Kuching_pm()
	{
		doTest("DateTime_TimeZone_Asia_Kuching_pm");
	}

	public void testDateTime_TimeZone_Asia_Macau_pm()
	{
		doTest("DateTime_TimeZone_Asia_Macau_pm");
	}

	public void testDateTime_TimeZone_Asia_Magadan_pm()
	{
		doTest("DateTime_TimeZone_Asia_Magadan_pm");
	}

	public void testDateTime_TimeZone_Asia_Makassar_pm()
	{
		doTest("DateTime_TimeZone_Asia_Makassar_pm");
	}

	public void testDateTime_TimeZone_Asia_Manila_pm()
	{
		doTest("DateTime_TimeZone_Asia_Manila_pm");
	}

	public void testDateTime_TimeZone_Asia_Nicosia_pm()
	{
		doTest("DateTime_TimeZone_Asia_Nicosia_pm");
	}

	public void testDateTime_TimeZone_Asia_Novokuznetsk_pm()
	{
		doTest("DateTime_TimeZone_Asia_Novokuznetsk_pm");
	}

	public void testDateTime_TimeZone_Asia_Novosibirsk_pm()
	{
		doTest("DateTime_TimeZone_Asia_Novosibirsk_pm");
	}

	public void testDateTime_TimeZone_Asia_Omsk_pm()
	{
		doTest("DateTime_TimeZone_Asia_Omsk_pm");
	}

	public void testDateTime_TimeZone_Asia_Oral_pm()
	{
		doTest("DateTime_TimeZone_Asia_Oral_pm");
	}

	public void testDateTime_TimeZone_Asia_Pontianak_pm()
	{
		doTest("DateTime_TimeZone_Asia_Pontianak_pm");
	}

	public void testDateTime_TimeZone_Asia_Pyongyang_pm()
	{
		doTest("DateTime_TimeZone_Asia_Pyongyang_pm");
	}

	public void testDateTime_TimeZone_Asia_Qatar_pm()
	{
		doTest("DateTime_TimeZone_Asia_Qatar_pm");
	}

	public void testDateTime_TimeZone_Asia_Qyzylorda_pm()
	{
		doTest("DateTime_TimeZone_Asia_Qyzylorda_pm");
	}

	public void testDateTime_TimeZone_Asia_Rangoon_pm()
	{
		doTest("DateTime_TimeZone_Asia_Rangoon_pm");
	}

	public void testDateTime_TimeZone_Asia_Riyadh_pm()
	{
		doTest("DateTime_TimeZone_Asia_Riyadh_pm");
	}

	public void testDateTime_TimeZone_Asia_Sakhalin_pm()
	{
		doTest("DateTime_TimeZone_Asia_Sakhalin_pm");
	}

	public void testDateTime_TimeZone_Asia_Samarkand_pm()
	{
		doTest("DateTime_TimeZone_Asia_Samarkand_pm");
	}

	public void testDateTime_TimeZone_Asia_Seoul_pm()
	{
		doTest("DateTime_TimeZone_Asia_Seoul_pm");
	}

	public void testDateTime_TimeZone_Asia_Shanghai_pm()
	{
		doTest("DateTime_TimeZone_Asia_Shanghai_pm");
	}

	public void testDateTime_TimeZone_Asia_Singapore_pm()
	{
		doTest("DateTime_TimeZone_Asia_Singapore_pm");
	}

	public void testDateTime_TimeZone_Asia_Srednekolymsk_pm()
	{
		doTest("DateTime_TimeZone_Asia_Srednekolymsk_pm");
	}

	public void testDateTime_TimeZone_Asia_Taipei_pm()
	{
		doTest("DateTime_TimeZone_Asia_Taipei_pm");
	}

	public void testDateTime_TimeZone_Asia_Tashkent_pm()
	{
		doTest("DateTime_TimeZone_Asia_Tashkent_pm");
	}

	public void testDateTime_TimeZone_Asia_Tbilisi_pm()
	{
		doTest("DateTime_TimeZone_Asia_Tbilisi_pm");
	}

	public void testDateTime_TimeZone_Asia_Tehran_pm()
	{
		doTest("DateTime_TimeZone_Asia_Tehran_pm");
	}

	public void testDateTime_TimeZone_Asia_Thimphu_pm()
	{
		doTest("DateTime_TimeZone_Asia_Thimphu_pm");
	}

	public void testDateTime_TimeZone_Asia_Tokyo_pm()
	{
		doTest("DateTime_TimeZone_Asia_Tokyo_pm");
	}

	public void testDateTime_TimeZone_Asia_Ulaanbaatar_pm()
	{
		doTest("DateTime_TimeZone_Asia_Ulaanbaatar_pm");
	}

	public void testDateTime_TimeZone_Asia_Urumqi_pm()
	{
		doTest("DateTime_TimeZone_Asia_Urumqi_pm");
	}

	public void testDateTime_TimeZone_Asia_Ust_Nera_pm()
	{
		doTest("DateTime_TimeZone_Asia_Ust_Nera_pm");
	}

	public void testDateTime_TimeZone_Asia_Vladivostok_pm()
	{
		doTest("DateTime_TimeZone_Asia_Vladivostok_pm");
	}

	public void testDateTime_TimeZone_Asia_Yakutsk_pm()
	{
		doTest("DateTime_TimeZone_Asia_Yakutsk_pm");
	}

	public void testDateTime_TimeZone_Asia_Yekaterinburg_pm()
	{
		doTest("DateTime_TimeZone_Asia_Yekaterinburg_pm");
	}

	public void testDateTime_TimeZone_Asia_Yerevan_pm()
	{
		doTest("DateTime_TimeZone_Asia_Yerevan_pm");
	}

	public void testDateTime_TimeZone_Atlantic_Azores_pm()
	{
		doTest("DateTime_TimeZone_Atlantic_Azores_pm");
	}

	public void testDateTime_TimeZone_Atlantic_Bermuda_pm()
	{
		doTest("DateTime_TimeZone_Atlantic_Bermuda_pm");
	}

	public void testDateTime_TimeZone_Atlantic_Canary_pm()
	{
		doTest("DateTime_TimeZone_Atlantic_Canary_pm");
	}

	public void testDateTime_TimeZone_Atlantic_Cape_Verde_pm()
	{
		doTest("DateTime_TimeZone_Atlantic_Cape_Verde_pm");
	}

	public void testDateTime_TimeZone_Atlantic_Faroe_pm()
	{
		doTest("DateTime_TimeZone_Atlantic_Faroe_pm");
	}

	public void testDateTime_TimeZone_Atlantic_Madeira_pm()
	{
		doTest("DateTime_TimeZone_Atlantic_Madeira_pm");
	}

	public void testDateTime_TimeZone_Atlantic_Reykjavik_pm()
	{
		doTest("DateTime_TimeZone_Atlantic_Reykjavik_pm");
	}

	public void testDateTime_TimeZone_Atlantic_South_Georgia_pm()
	{
		doTest("DateTime_TimeZone_Atlantic_South_Georgia_pm");
	}

	public void testDateTime_TimeZone_Atlantic_Stanley_pm()
	{
		doTest("DateTime_TimeZone_Atlantic_Stanley_pm");
	}

	public void testDateTime_TimeZone_Australia_Adelaide_pm()
	{
		doTest("DateTime_TimeZone_Australia_Adelaide_pm");
	}

	public void testDateTime_TimeZone_Australia_Brisbane_pm()
	{
		doTest("DateTime_TimeZone_Australia_Brisbane_pm");
	}

	public void testDateTime_TimeZone_Australia_Broken_Hill_pm()
	{
		doTest("DateTime_TimeZone_Australia_Broken_Hill_pm");
	}

	public void testDateTime_TimeZone_Australia_Currie_pm()
	{
		doTest("DateTime_TimeZone_Australia_Currie_pm");
	}

	public void testDateTime_TimeZone_Australia_Darwin_pm()
	{
		doTest("DateTime_TimeZone_Australia_Darwin_pm");
	}

	public void testDateTime_TimeZone_Australia_Eucla_pm()
	{
		doTest("DateTime_TimeZone_Australia_Eucla_pm");
	}

	public void testDateTime_TimeZone_Australia_Hobart_pm()
	{
		doTest("DateTime_TimeZone_Australia_Hobart_pm");
	}

	public void testDateTime_TimeZone_Australia_Lindeman_pm()
	{
		doTest("DateTime_TimeZone_Australia_Lindeman_pm");
	}

	public void testDateTime_TimeZone_Australia_Lord_Howe_pm()
	{
		doTest("DateTime_TimeZone_Australia_Lord_Howe_pm");
	}

	public void testDateTime_TimeZone_Australia_Melbourne_pm()
	{
		doTest("DateTime_TimeZone_Australia_Melbourne_pm");
	}

	public void testDateTime_TimeZone_Australia_Perth_pm()
	{
		doTest("DateTime_TimeZone_Australia_Perth_pm");
	}

	public void testDateTime_TimeZone_Australia_Sydney_pm()
	{
		doTest("DateTime_TimeZone_Australia_Sydney_pm");
	}

	public void testDateTime_TimeZone_CET_pm()
	{
		doTest("DateTime_TimeZone_CET_pm");
	}

	public void testDateTime_TimeZone_CST6CDT_pm()
	{
		doTest("DateTime_TimeZone_CST6CDT_pm");
	}

	public void testDateTime_TimeZone_Catalog_pm()
	{
		doTest("DateTime_TimeZone_Catalog_pm");
	}

	public void testDateTime_TimeZone_EET_pm()
	{
		doTest("DateTime_TimeZone_EET_pm");
	}

	public void testDateTime_TimeZone_EST5EDT_pm()
	{
		doTest("DateTime_TimeZone_EST5EDT_pm");
	}

	public void testDateTime_TimeZone_EST_pm()
	{
		doTest("DateTime_TimeZone_EST_pm");
	}

	public void testDateTime_TimeZone_Europe_Amsterdam_pm()
	{
		doTest("DateTime_TimeZone_Europe_Amsterdam_pm");
	}

	public void testDateTime_TimeZone_Europe_Andorra_pm()
	{
		doTest("DateTime_TimeZone_Europe_Andorra_pm");
	}

	public void testDateTime_TimeZone_Europe_Athens_pm()
	{
		doTest("DateTime_TimeZone_Europe_Athens_pm");
	}

	public void testDateTime_TimeZone_Europe_Belgrade_pm()
	{
		doTest("DateTime_TimeZone_Europe_Belgrade_pm");
	}

	public void testDateTime_TimeZone_Europe_Berlin_pm()
	{
		doTest("DateTime_TimeZone_Europe_Berlin_pm");
	}

	public void testDateTime_TimeZone_Europe_Brussels_pm()
	{
		doTest("DateTime_TimeZone_Europe_Brussels_pm");
	}

	public void testDateTime_TimeZone_Europe_Bucharest_pm()
	{
		doTest("DateTime_TimeZone_Europe_Bucharest_pm");
	}

	public void testDateTime_TimeZone_Europe_Budapest_pm()
	{
		doTest("DateTime_TimeZone_Europe_Budapest_pm");
	}

	public void testDateTime_TimeZone_Europe_Chisinau_pm()
	{
		doTest("DateTime_TimeZone_Europe_Chisinau_pm");
	}

	public void testDateTime_TimeZone_Europe_Copenhagen_pm()
	{
		doTest("DateTime_TimeZone_Europe_Copenhagen_pm");
	}

	public void testDateTime_TimeZone_Europe_Dublin_pm()
	{
		doTest("DateTime_TimeZone_Europe_Dublin_pm");
	}

	public void testDateTime_TimeZone_Europe_Gibraltar_pm()
	{
		doTest("DateTime_TimeZone_Europe_Gibraltar_pm");
	}

	public void testDateTime_TimeZone_Europe_Helsinki_pm()
	{
		doTest("DateTime_TimeZone_Europe_Helsinki_pm");
	}

	public void testDateTime_TimeZone_Europe_Istanbul_pm()
	{
		doTest("DateTime_TimeZone_Europe_Istanbul_pm");
	}

	public void testDateTime_TimeZone_Europe_Kaliningrad_pm()
	{
		doTest("DateTime_TimeZone_Europe_Kaliningrad_pm");
	}

	public void testDateTime_TimeZone_Europe_Kiev_pm()
	{
		doTest("DateTime_TimeZone_Europe_Kiev_pm");
	}

	public void testDateTime_TimeZone_Europe_Lisbon_pm()
	{
		doTest("DateTime_TimeZone_Europe_Lisbon_pm");
	}

	public void testDateTime_TimeZone_Europe_London_pm()
	{
		doTest("DateTime_TimeZone_Europe_London_pm");
	}

	public void testDateTime_TimeZone_Europe_Luxembourg_pm()
	{
		doTest("DateTime_TimeZone_Europe_Luxembourg_pm");
	}

	public void testDateTime_TimeZone_Europe_Madrid_pm()
	{
		doTest("DateTime_TimeZone_Europe_Madrid_pm");
	}

	public void testDateTime_TimeZone_Europe_Malta_pm()
	{
		doTest("DateTime_TimeZone_Europe_Malta_pm");
	}

	public void testDateTime_TimeZone_Europe_Minsk_pm()
	{
		doTest("DateTime_TimeZone_Europe_Minsk_pm");
	}

	public void testDateTime_TimeZone_Europe_Monaco_pm()
	{
		doTest("DateTime_TimeZone_Europe_Monaco_pm");
	}

	public void testDateTime_TimeZone_Europe_Moscow_pm()
	{
		doTest("DateTime_TimeZone_Europe_Moscow_pm");
	}

	public void testDateTime_TimeZone_Europe_Oslo_pm()
	{
		doTest("DateTime_TimeZone_Europe_Oslo_pm");
	}

	public void testDateTime_TimeZone_Europe_Paris_pm()
	{
		doTest("DateTime_TimeZone_Europe_Paris_pm");
	}

	public void testDateTime_TimeZone_Europe_Prague_pm()
	{
		doTest("DateTime_TimeZone_Europe_Prague_pm");
	}

	public void testDateTime_TimeZone_Europe_Riga_pm()
	{
		doTest("DateTime_TimeZone_Europe_Riga_pm");
	}

	public void testDateTime_TimeZone_Europe_Rome_pm()
	{
		doTest("DateTime_TimeZone_Europe_Rome_pm");
	}

	public void testDateTime_TimeZone_Europe_Samara_pm()
	{
		doTest("DateTime_TimeZone_Europe_Samara_pm");
	}

	public void testDateTime_TimeZone_Europe_Simferopol_pm()
	{
		doTest("DateTime_TimeZone_Europe_Simferopol_pm");
	}

	public void testDateTime_TimeZone_Europe_Sofia_pm()
	{
		doTest("DateTime_TimeZone_Europe_Sofia_pm");
	}

	public void testDateTime_TimeZone_Europe_Stockholm_pm()
	{
		doTest("DateTime_TimeZone_Europe_Stockholm_pm");
	}

	public void testDateTime_TimeZone_Europe_Tallinn_pm()
	{
		doTest("DateTime_TimeZone_Europe_Tallinn_pm");
	}

	public void testDateTime_TimeZone_Europe_Tirane_pm()
	{
		doTest("DateTime_TimeZone_Europe_Tirane_pm");
	}

	public void testDateTime_TimeZone_Europe_Uzhgorod_pm()
	{
		doTest("DateTime_TimeZone_Europe_Uzhgorod_pm");
	}

	public void testDateTime_TimeZone_Europe_Vienna_pm()
	{
		doTest("DateTime_TimeZone_Europe_Vienna_pm");
	}

	public void testDateTime_TimeZone_Europe_Vilnius_pm()
	{
		doTest("DateTime_TimeZone_Europe_Vilnius_pm");
	}

	public void testDateTime_TimeZone_Europe_Volgograd_pm()
	{
		doTest("DateTime_TimeZone_Europe_Volgograd_pm");
	}

	public void testDateTime_TimeZone_Europe_Warsaw_pm()
	{
		doTest("DateTime_TimeZone_Europe_Warsaw_pm");
	}

	public void testDateTime_TimeZone_Europe_Zaporozhye_pm()
	{
		doTest("DateTime_TimeZone_Europe_Zaporozhye_pm");
	}

	public void testDateTime_TimeZone_Europe_Zurich_pm()
	{
		doTest("DateTime_TimeZone_Europe_Zurich_pm");
	}

	public void testDateTime_TimeZone_Floating_pm()
	{
		doTest("DateTime_TimeZone_Floating_pm");
	}

	public void testDateTime_TimeZone_HST_pm()
	{
		doTest("DateTime_TimeZone_HST_pm");
	}

	public void testDateTime_TimeZone_Indian_Chagos_pm()
	{
		doTest("DateTime_TimeZone_Indian_Chagos_pm");
	}

	public void testDateTime_TimeZone_Indian_Christmas_pm()
	{
		doTest("DateTime_TimeZone_Indian_Christmas_pm");
	}

	public void testDateTime_TimeZone_Indian_Cocos_pm()
	{
		doTest("DateTime_TimeZone_Indian_Cocos_pm");
	}

	public void testDateTime_TimeZone_Indian_Kerguelen_pm()
	{
		doTest("DateTime_TimeZone_Indian_Kerguelen_pm");
	}

	public void testDateTime_TimeZone_Indian_Mahe_pm()
	{
		doTest("DateTime_TimeZone_Indian_Mahe_pm");
	}

	public void testDateTime_TimeZone_Indian_Maldives_pm()
	{
		doTest("DateTime_TimeZone_Indian_Maldives_pm");
	}

	public void testDateTime_TimeZone_Indian_Mauritius_pm()
	{
		doTest("DateTime_TimeZone_Indian_Mauritius_pm");
	}

	public void testDateTime_TimeZone_Indian_Reunion_pm()
	{
		doTest("DateTime_TimeZone_Indian_Reunion_pm");
	}

	public void testDateTime_TimeZone_Local_Android_pm()
	{
		doTest("DateTime_TimeZone_Local_Android_pm");
	}

	public void testDateTime_TimeZone_Local_Unix_pm()
	{
		doTest("DateTime_TimeZone_Local_Unix_pm");
	}

	public void testDateTime_TimeZone_Local_VMS_pm()
	{
		doTest("DateTime_TimeZone_Local_VMS_pm");
	}

	public void testDateTime_TimeZone_Local_Win32_pm()
	{
		doTest("DateTime_TimeZone_Local_Win32_pm");
	}

	public void testDateTime_TimeZone_Local_pm()
	{
		doTest("DateTime_TimeZone_Local_pm");
	}

	public void testDateTime_TimeZone_MET_pm()
	{
		doTest("DateTime_TimeZone_MET_pm");
	}

	public void testDateTime_TimeZone_MST7MDT_pm()
	{
		doTest("DateTime_TimeZone_MST7MDT_pm");
	}

	public void testDateTime_TimeZone_MST_pm()
	{
		doTest("DateTime_TimeZone_MST_pm");
	}

	public void testDateTime_TimeZone_OffsetOnly_pm()
	{
		doTest("DateTime_TimeZone_OffsetOnly_pm");
	}

	public void testDateTime_TimeZone_OlsonDB_Change_pm()
	{
		doTest("DateTime_TimeZone_OlsonDB_Change_pm");
	}

	public void testDateTime_TimeZone_OlsonDB_Observance_pm()
	{
		doTest("DateTime_TimeZone_OlsonDB_Observance_pm");
	}

	public void testDateTime_TimeZone_OlsonDB_Rule_pm()
	{
		doTest("DateTime_TimeZone_OlsonDB_Rule_pm");
	}

	public void testDateTime_TimeZone_OlsonDB_Zone_pm()
	{
		doTest("DateTime_TimeZone_OlsonDB_Zone_pm");
	}

	public void testDateTime_TimeZone_OlsonDB_pm()
	{
		doTest("DateTime_TimeZone_OlsonDB_pm");
	}

	public void testDateTime_TimeZone_PST8PDT_pm()
	{
		doTest("DateTime_TimeZone_PST8PDT_pm");
	}

	public void testDateTime_TimeZone_Pacific_Apia_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Apia_pm");
	}

	public void testDateTime_TimeZone_Pacific_Auckland_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Auckland_pm");
	}

	public void testDateTime_TimeZone_Pacific_Bougainville_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Bougainville_pm");
	}

	public void testDateTime_TimeZone_Pacific_Chatham_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Chatham_pm");
	}

	public void testDateTime_TimeZone_Pacific_Chuuk_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Chuuk_pm");
	}

	public void testDateTime_TimeZone_Pacific_Easter_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Easter_pm");
	}

	public void testDateTime_TimeZone_Pacific_Efate_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Efate_pm");
	}

	public void testDateTime_TimeZone_Pacific_Enderbury_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Enderbury_pm");
	}

	public void testDateTime_TimeZone_Pacific_Fakaofo_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Fakaofo_pm");
	}

	public void testDateTime_TimeZone_Pacific_Fiji_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Fiji_pm");
	}

	public void testDateTime_TimeZone_Pacific_Funafuti_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Funafuti_pm");
	}

	public void testDateTime_TimeZone_Pacific_Galapagos_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Galapagos_pm");
	}

	public void testDateTime_TimeZone_Pacific_Gambier_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Gambier_pm");
	}

	public void testDateTime_TimeZone_Pacific_Guadalcanal_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Guadalcanal_pm");
	}

	public void testDateTime_TimeZone_Pacific_Guam_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Guam_pm");
	}

	public void testDateTime_TimeZone_Pacific_Honolulu_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Honolulu_pm");
	}

	public void testDateTime_TimeZone_Pacific_Kiritimati_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Kiritimati_pm");
	}

	public void testDateTime_TimeZone_Pacific_Kosrae_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Kosrae_pm");
	}

	public void testDateTime_TimeZone_Pacific_Kwajalein_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Kwajalein_pm");
	}

	public void testDateTime_TimeZone_Pacific_Majuro_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Majuro_pm");
	}

	public void testDateTime_TimeZone_Pacific_Marquesas_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Marquesas_pm");
	}

	public void testDateTime_TimeZone_Pacific_Nauru_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Nauru_pm");
	}

	public void testDateTime_TimeZone_Pacific_Niue_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Niue_pm");
	}

	public void testDateTime_TimeZone_Pacific_Norfolk_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Norfolk_pm");
	}

	public void testDateTime_TimeZone_Pacific_Noumea_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Noumea_pm");
	}

	public void testDateTime_TimeZone_Pacific_Pago_Pago_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Pago_Pago_pm");
	}

	public void testDateTime_TimeZone_Pacific_Palau_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Palau_pm");
	}

	public void testDateTime_TimeZone_Pacific_Pitcairn_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Pitcairn_pm");
	}

	public void testDateTime_TimeZone_Pacific_Pohnpei_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Pohnpei_pm");
	}

	public void testDateTime_TimeZone_Pacific_Port_Moresby_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Port_Moresby_pm");
	}

	public void testDateTime_TimeZone_Pacific_Rarotonga_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Rarotonga_pm");
	}

	public void testDateTime_TimeZone_Pacific_Tahiti_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Tahiti_pm");
	}

	public void testDateTime_TimeZone_Pacific_Tarawa_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Tarawa_pm");
	}

	public void testDateTime_TimeZone_Pacific_Tongatapu_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Tongatapu_pm");
	}

	public void testDateTime_TimeZone_Pacific_Wake_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Wake_pm");
	}

	public void testDateTime_TimeZone_Pacific_Wallis_pm()
	{
		doTest("DateTime_TimeZone_Pacific_Wallis_pm");
	}

	public void testDateTime_TimeZone_UTC_pm()
	{
		doTest("DateTime_TimeZone_UTC_pm");
	}

	public void testDateTime_TimeZone_WET_pm()
	{
		doTest("DateTime_TimeZone_WET_pm");
	}

	public void testDateTime_TimeZone_pm()
	{
		doTest("DateTime_TimeZone_pm");
	}

	public void testDateTime_pm()
	{
		doTest("DateTime_pm");
	}

	public void testDate_Format_pm()
	{
		doTest("Date_Format_pm");
	}

	public void testDate_Language_Afar_pm()
	{
		doTest("Date_Language_Afar_pm");
	}

	public void testDate_Language_Amharic_pm()
	{
		doTest("Date_Language_Amharic_pm");
	}

	public void testDate_Language_Austrian_pm()
	{
		doTest("Date_Language_Austrian_pm");
	}

	public void testDate_Language_Brazilian_pm()
	{
		doTest("Date_Language_Brazilian_pm");
	}

	public void testDate_Language_Bulgarian_pm()
	{
		doTest("Date_Language_Bulgarian_pm");
	}

	public void testDate_Language_Chinese_GB_pm()
	{
		doTest("Date_Language_Chinese_GB_pm");
	}

	public void testDate_Language_Chinese_pm()
	{
		doTest("Date_Language_Chinese_pm");
	}

	public void testDate_Language_Czech_pm()
	{
		doTest("Date_Language_Czech_pm");
	}

	public void testDate_Language_Danish_pm()
	{
		doTest("Date_Language_Danish_pm");
	}

	public void testDate_Language_Dutch_pm()
	{
		doTest("Date_Language_Dutch_pm");
	}

	public void testDate_Language_English_pm()
	{
		doTest("Date_Language_English_pm");
	}

	public void testDate_Language_Finnish_pm()
	{
		doTest("Date_Language_Finnish_pm");
	}

	public void testDate_Language_French_pm()
	{
		doTest("Date_Language_French_pm");
	}

	public void testDate_Language_Gedeo_pm()
	{
		doTest("Date_Language_Gedeo_pm");
	}

	public void testDate_Language_German_pm()
	{
		doTest("Date_Language_German_pm");
	}

	public void testDate_Language_Greek_pm()
	{
		doTest("Date_Language_Greek_pm");
	}

	public void testDate_Language_Hungarian_pm()
	{
		doTest("Date_Language_Hungarian_pm");
	}

	public void testDate_Language_Icelandic_pm()
	{
		doTest("Date_Language_Icelandic_pm");
	}

	public void testDate_Language_Italian_pm()
	{
		doTest("Date_Language_Italian_pm");
	}

	public void testDate_Language_Norwegian_pm()
	{
		doTest("Date_Language_Norwegian_pm");
	}

	public void testDate_Language_Oromo_pm()
	{
		doTest("Date_Language_Oromo_pm");
	}

	public void testDate_Language_Romanian_pm()
	{
		doTest("Date_Language_Romanian_pm");
	}

	public void testDate_Language_Russian_cp1251_pm()
	{
		doTest("Date_Language_Russian_cp1251_pm");
	}

	public void testDate_Language_Russian_koi8r_pm()
	{
		doTest("Date_Language_Russian_koi8r_pm");
	}

	public void testDate_Language_Russian_pm()
	{
		doTest("Date_Language_Russian_pm");
	}

	public void testDate_Language_Sidama_pm()
	{
		doTest("Date_Language_Sidama_pm");
	}

	public void testDate_Language_Somali_pm()
	{
		doTest("Date_Language_Somali_pm");
	}

	public void testDate_Language_Spanish_pm()
	{
		doTest("Date_Language_Spanish_pm");
	}

	public void testDate_Language_Swedish_pm()
	{
		doTest("Date_Language_Swedish_pm");
	}

	public void testDate_Language_TigrinyaEritrean_pm()
	{
		doTest("Date_Language_TigrinyaEritrean_pm");
	}

	public void testDate_Language_TigrinyaEthiopian_pm()
	{
		doTest("Date_Language_TigrinyaEthiopian_pm");
	}

	public void testDate_Language_Tigrinya_pm()
	{
		doTest("Date_Language_Tigrinya_pm");
	}

	public void testDate_Language_Turkish_pm()
	{
		doTest("Date_Language_Turkish_pm");
	}

	public void testDate_Language_pm()
	{
		doTest("Date_Language_pm");
	}

	public void testDate_Parse_pm()
	{
		doTest("Date_Parse_pm");
	}

	public void testDevel_CheckLib_pm()
	{
		doTest("Devel_CheckLib_pm");
	}

	public void testDevel_Declare_Context_Simple_pm()
	{
		doTest("Devel_Declare_Context_Simple_pm");
	}

	public void testDevel_Declare_MethodInstaller_Simple_pm()
	{
		doTest("Devel_Declare_MethodInstaller_Simple_pm");
	}

	public void testDevel_Declare_pm()
	{
		doTest("Devel_Declare_pm");
	}

	public void testDevel_Dwarn_pm()
	{
		doTest("Devel_Dwarn_pm");
	}

	public void testDevel_FindPerl_pm()
	{
		doTest("Devel_FindPerl_pm");
	}

	public void testDevel_GlobalDestruction_pm()
	{
		doTest("Devel_GlobalDestruction_pm");
	}

	public void testDevel_InnerPackage_pm()
	{
		doTest("Devel_InnerPackage_pm");
	}

	public void testDevel_LeakGuard_Object_State_pm()
	{
		doTest("Devel_LeakGuard_Object_State_pm");
	}

	public void testDevel_LeakGuard_Object_pm()
	{
		doTest("Devel_LeakGuard_Object_pm");
	}

	public void testDevel_OverloadInfo_pm()
	{
		doTest("Devel_OverloadInfo_pm");
	}

	public void testDevel_PPPort_pm()
	{
		doTest("Devel_PPPort_pm");
	}

	public void testDevel_PartialDump_pm()
	{
		doTest("Devel_PartialDump_pm");
	}

	public void testDevel_Peek_pm()
	{
		doTest("Devel_Peek_pm");
	}

	public void testDevel_SelfStubber_pm()
	{
		doTest("Devel_SelfStubber_pm");
	}

	public void testDevel_StackTrace_Frame_pm()
	{
		doTest("Devel_StackTrace_Frame_pm");
	}

	public void testDevel_StackTrace_pm()
	{
		doTest("Devel_StackTrace_pm");
	}

	public void testDigest_CMAC_pm()
	{
		doTest("Digest_CMAC_pm");
	}

	public void testDigest_HMAC_MD5_pm()
	{
		doTest("Digest_HMAC_MD5_pm");
	}

	public void testDigest_HMAC_SHA1_pm()
	{
		doTest("Digest_HMAC_SHA1_pm");
	}

	public void testDigest_HMAC_pm()
	{
		doTest("Digest_HMAC_pm");
	}

	public void testDigest_MD2_pm()
	{
		doTest("Digest_MD2_pm");
	}

	public void testDigest_MD5_pm()
	{
		doTest("Digest_MD5_pm");
	}

	public void testDigest_OMAC1_pm()
	{
		doTest("Digest_OMAC1_pm");
	}

	public void testDigest_OMAC2_pm()
	{
		doTest("Digest_OMAC2_pm");
	}

	public void testDigest_OMAC_Base_pm()
	{
		doTest("Digest_OMAC_Base_pm");
	}

	public void testDigest_Perl_MD5_pm()
	{
		doTest("Digest_Perl_MD5_pm");
	}

	public void testDigest_SHA1_pm()
	{
		doTest("Digest_SHA1_pm");
	}

	public void testDigest_SHA_pm()
	{
		doTest("Digest_SHA_pm");
	}

	public void testDigest_Whirlpool_pm()
	{
		doTest("Digest_Whirlpool_pm");
	}

	public void testDigest_base_pm()
	{
		doTest("Digest_base_pm");
	}

	public void testDigest_file_pm()
	{
		doTest("Digest_file_pm");
	}

	public void testDigest_pm()
	{
		doTest("Digest_pm");
	}

	public void testDirHandle_pm()
	{
		doTest("DirHandle_pm");
	}

	public void testDist_CheckConflicts_pm()
	{
		doTest("Dist_CheckConflicts_pm");
	}

	public void testDist_Zilla_App_Command_add_pm()
	{
		doTest("Dist_Zilla_App_Command_add_pm");
	}

	public void testDist_Zilla_App_Command_authordeps_pm()
	{
		doTest("Dist_Zilla_App_Command_authordeps_pm");
	}

	public void testDist_Zilla_App_Command_build_pm()
	{
		doTest("Dist_Zilla_App_Command_build_pm");
	}

	public void testDist_Zilla_App_Command_clean_pm()
	{
		doTest("Dist_Zilla_App_Command_clean_pm");
	}

	public void testDist_Zilla_App_Command_install_pm()
	{
		doTest("Dist_Zilla_App_Command_install_pm");
	}

	public void testDist_Zilla_App_Command_listdeps_pm()
	{
		doTest("Dist_Zilla_App_Command_listdeps_pm");
	}

	public void testDist_Zilla_App_Command_new_pm()
	{
		doTest("Dist_Zilla_App_Command_new_pm");
	}

	public void testDist_Zilla_App_Command_nop_pm()
	{
		doTest("Dist_Zilla_App_Command_nop_pm");
	}

	public void testDist_Zilla_App_Command_pm()
	{
		doTest("Dist_Zilla_App_Command_pm");
	}

	public void testDist_Zilla_App_Command_release_pm()
	{
		doTest("Dist_Zilla_App_Command_release_pm");
	}

	public void testDist_Zilla_App_Command_run_pm()
	{
		doTest("Dist_Zilla_App_Command_run_pm");
	}

	public void testDist_Zilla_App_Command_setup_pm()
	{
		doTest("Dist_Zilla_App_Command_setup_pm");
	}

	public void testDist_Zilla_App_Command_smoke_pm()
	{
		doTest("Dist_Zilla_App_Command_smoke_pm");
	}

	public void testDist_Zilla_App_Command_test_pm()
	{
		doTest("Dist_Zilla_App_Command_test_pm");
	}

	public void testDist_Zilla_App_Command_version_pm()
	{
		doTest("Dist_Zilla_App_Command_version_pm");
	}

	public void testDist_Zilla_App_Tester_pm()
	{
		doTest("Dist_Zilla_App_Tester_pm");
	}

	public void testDist_Zilla_App_pm()
	{
		doTest("Dist_Zilla_App_pm");
	}

	public void testDist_Zilla_Chrome_Term_pm()
	{
		doTest("Dist_Zilla_Chrome_Term_pm");
	}

	public void testDist_Zilla_Chrome_Test_pm()
	{
		doTest("Dist_Zilla_Chrome_Test_pm");
	}

	public void testDist_Zilla_Dist_Builder_pm()
	{
		doTest("Dist_Zilla_Dist_Builder_pm");
	}

	public void testDist_Zilla_Dist_Minter_pm()
	{
		doTest("Dist_Zilla_Dist_Minter_pm");
	}

	public void testDist_Zilla_File_FromCode_pm()
	{
		doTest("Dist_Zilla_File_FromCode_pm");
	}

	public void testDist_Zilla_File_InMemory_pm()
	{
		doTest("Dist_Zilla_File_InMemory_pm");
	}

	public void testDist_Zilla_File_OnDisk_pm()
	{
		doTest("Dist_Zilla_File_OnDisk_pm");
	}

	public void testDist_Zilla_MVP_Assembler_GlobalConfig_pm()
	{
		doTest("Dist_Zilla_MVP_Assembler_GlobalConfig_pm");
	}

	public void testDist_Zilla_MVP_Assembler_Zilla_pm()
	{
		doTest("Dist_Zilla_MVP_Assembler_Zilla_pm");
	}

	public void testDist_Zilla_MVP_Assembler_pm()
	{
		doTest("Dist_Zilla_MVP_Assembler_pm");
	}

	public void testDist_Zilla_MVP_Reader_Finder_pm()
	{
		doTest("Dist_Zilla_MVP_Reader_Finder_pm");
	}

	public void testDist_Zilla_MVP_Reader_Perl_pm()
	{
		doTest("Dist_Zilla_MVP_Reader_Perl_pm");
	}

	public void testDist_Zilla_MVP_RootSection_pm()
	{
		doTest("Dist_Zilla_MVP_RootSection_pm");
	}

	public void testDist_Zilla_MVP_Section_pm()
	{
		doTest("Dist_Zilla_MVP_Section_pm");
	}

	public void testDist_Zilla_MintingProfile_Default_pm()
	{
		doTest("Dist_Zilla_MintingProfile_Default_pm");
	}

	public void testDist_Zilla_PluginBundle_Basic_pm()
	{
		doTest("Dist_Zilla_PluginBundle_Basic_pm");
	}

	public void testDist_Zilla_PluginBundle_Classic_pm()
	{
		doTest("Dist_Zilla_PluginBundle_Classic_pm");
	}

	public void testDist_Zilla_PluginBundle_FakeClassic_pm()
	{
		doTest("Dist_Zilla_PluginBundle_FakeClassic_pm");
	}

	public void testDist_Zilla_PluginBundle_Filter_pm()
	{
		doTest("Dist_Zilla_PluginBundle_Filter_pm");
	}

	public void testDist_Zilla_Plugin_AutoPrereq_pm()
	{
		doTest("Dist_Zilla_Plugin_AutoPrereq_pm");
	}

	public void testDist_Zilla_Plugin_AutoPrereqs_pm()
	{
		doTest("Dist_Zilla_Plugin_AutoPrereqs_pm");
	}

	public void testDist_Zilla_Plugin_AutoVersion_pm()
	{
		doTest("Dist_Zilla_Plugin_AutoVersion_pm");
	}

	public void testDist_Zilla_Plugin_BumpVersion_pm()
	{
		doTest("Dist_Zilla_Plugin_BumpVersion_pm");
	}

	public void testDist_Zilla_Plugin_CPANFile_pm()
	{
		doTest("Dist_Zilla_Plugin_CPANFile_pm");
	}

	public void testDist_Zilla_Plugin_ConfirmRelease_pm()
	{
		doTest("Dist_Zilla_Plugin_ConfirmRelease_pm");
	}

	public void testDist_Zilla_Plugin_DistINI_pm()
	{
		doTest("Dist_Zilla_Plugin_DistINI_pm");
	}

	public void testDist_Zilla_Plugin_Encoding_pm()
	{
		doTest("Dist_Zilla_Plugin_Encoding_pm");
	}

	public void testDist_Zilla_Plugin_ExecDir_pm()
	{
		doTest("Dist_Zilla_Plugin_ExecDir_pm");
	}

	public void testDist_Zilla_Plugin_ExtraTests_pm()
	{
		doTest("Dist_Zilla_Plugin_ExtraTests_pm");
	}

	public void testDist_Zilla_Plugin_FakeRelease_pm()
	{
		doTest("Dist_Zilla_Plugin_FakeRelease_pm");
	}

	public void testDist_Zilla_Plugin_FileFinder_ByName_pm()
	{
		doTest("Dist_Zilla_Plugin_FileFinder_ByName_pm");
	}

	public void testDist_Zilla_Plugin_FileFinder_Filter_pm()
	{
		doTest("Dist_Zilla_Plugin_FileFinder_Filter_pm");
	}

	public void testDist_Zilla_Plugin_FinderCode_pm()
	{
		doTest("Dist_Zilla_Plugin_FinderCode_pm");
	}

	public void testDist_Zilla_Plugin_GatherDir_Template_pm()
	{
		doTest("Dist_Zilla_Plugin_GatherDir_Template_pm");
	}

	public void testDist_Zilla_Plugin_GatherDir_pm()
	{
		doTest("Dist_Zilla_Plugin_GatherDir_pm");
	}

	public void testDist_Zilla_Plugin_GenerateFile_pm()
	{
		doTest("Dist_Zilla_Plugin_GenerateFile_pm");
	}

	public void testDist_Zilla_Plugin_InlineFiles_pm()
	{
		doTest("Dist_Zilla_Plugin_InlineFiles_pm");
	}

	public void testDist_Zilla_Plugin_InstallGuide_pm()
	{
		doTest("Dist_Zilla_Plugin_InstallGuide_pm");
	}

	public void testDist_Zilla_Plugin_License_pm()
	{
		doTest("Dist_Zilla_Plugin_License_pm");
	}

	public void testDist_Zilla_Plugin_MakeMaker_Runner_pm()
	{
		doTest("Dist_Zilla_Plugin_MakeMaker_Runner_pm");
	}

	public void testDist_Zilla_Plugin_MakeMaker_pm()
	{
		doTest("Dist_Zilla_Plugin_MakeMaker_pm");
	}

	public void testDist_Zilla_Plugin_ManifestSkip_pm()
	{
		doTest("Dist_Zilla_Plugin_ManifestSkip_pm");
	}

	public void testDist_Zilla_Plugin_Manifest_pm()
	{
		doTest("Dist_Zilla_Plugin_Manifest_pm");
	}

	public void testDist_Zilla_Plugin_MetaConfig_pm()
	{
		doTest("Dist_Zilla_Plugin_MetaConfig_pm");
	}

	public void testDist_Zilla_Plugin_MetaJSON_pm()
	{
		doTest("Dist_Zilla_Plugin_MetaJSON_pm");
	}

	public void testDist_Zilla_Plugin_MetaNoIndex_pm()
	{
		doTest("Dist_Zilla_Plugin_MetaNoIndex_pm");
	}

	public void testDist_Zilla_Plugin_MetaResources_pm()
	{
		doTest("Dist_Zilla_Plugin_MetaResources_pm");
	}

	public void testDist_Zilla_Plugin_MetaTests_pm()
	{
		doTest("Dist_Zilla_Plugin_MetaTests_pm");
	}

	public void testDist_Zilla_Plugin_MetaYAML_pm()
	{
		doTest("Dist_Zilla_Plugin_MetaYAML_pm");
	}

	public void testDist_Zilla_Plugin_ModuleBuild_pm()
	{
		doTest("Dist_Zilla_Plugin_ModuleBuild_pm");
	}

	public void testDist_Zilla_Plugin_ModuleShareDirs_pm()
	{
		doTest("Dist_Zilla_Plugin_ModuleShareDirs_pm");
	}

	public void testDist_Zilla_Plugin_NextRelease_pm()
	{
		doTest("Dist_Zilla_Plugin_NextRelease_pm");
	}

	public void testDist_Zilla_Plugin_PkgDist_pm()
	{
		doTest("Dist_Zilla_Plugin_PkgDist_pm");
	}

	public void testDist_Zilla_Plugin_PkgVersion_pm()
	{
		doTest("Dist_Zilla_Plugin_PkgVersion_pm");
	}

	public void testDist_Zilla_Plugin_PodCoverageTests_pm()
	{
		doTest("Dist_Zilla_Plugin_PodCoverageTests_pm");
	}

	public void testDist_Zilla_Plugin_PodSyntaxTests_pm()
	{
		doTest("Dist_Zilla_Plugin_PodSyntaxTests_pm");
	}

	public void testDist_Zilla_Plugin_PodVersion_pm()
	{
		doTest("Dist_Zilla_Plugin_PodVersion_pm");
	}

	public void testDist_Zilla_Plugin_PodWeaverIfPod_pm()
	{
		doTest("Dist_Zilla_Plugin_PodWeaverIfPod_pm");
	}

	public void testDist_Zilla_Plugin_PodWeaver_pm()
	{
		doTest("Dist_Zilla_Plugin_PodWeaver_pm");
	}

	public void testDist_Zilla_Plugin_Prereq_pm()
	{
		doTest("Dist_Zilla_Plugin_Prereq_pm");
	}

	public void testDist_Zilla_Plugin_Prereqs_pm()
	{
		doTest("Dist_Zilla_Plugin_Prereqs_pm");
	}

	public void testDist_Zilla_Plugin_PruneCruft_pm()
	{
		doTest("Dist_Zilla_Plugin_PruneCruft_pm");
	}

	public void testDist_Zilla_Plugin_PruneFiles_pm()
	{
		doTest("Dist_Zilla_Plugin_PruneFiles_pm");
	}

	public void testDist_Zilla_Plugin_Readme_pm()
	{
		doTest("Dist_Zilla_Plugin_Readme_pm");
	}

	public void testDist_Zilla_Plugin_RemovePrereqs_pm()
	{
		doTest("Dist_Zilla_Plugin_RemovePrereqs_pm");
	}

	public void testDist_Zilla_Plugin_ShareDir_pm()
	{
		doTest("Dist_Zilla_Plugin_ShareDir_pm");
	}

	public void testDist_Zilla_Plugin_TemplateModule_pm()
	{
		doTest("Dist_Zilla_Plugin_TemplateModule_pm");
	}

	public void testDist_Zilla_Plugin_TestRelease_pm()
	{
		doTest("Dist_Zilla_Plugin_TestRelease_pm");
	}

	public void testDist_Zilla_Plugin_UploadToCPAN_pm()
	{
		doTest("Dist_Zilla_Plugin_UploadToCPAN_pm");
	}

	public void testDist_Zilla_Prereqs_pm()
	{
		doTest("Dist_Zilla_Prereqs_pm");
	}

	public void testDist_Zilla_Role_AfterBuild_pm()
	{
		doTest("Dist_Zilla_Role_AfterBuild_pm");
	}

	public void testDist_Zilla_Role_AfterMint_pm()
	{
		doTest("Dist_Zilla_Role_AfterMint_pm");
	}

	public void testDist_Zilla_Role_AfterRelease_pm()
	{
		doTest("Dist_Zilla_Role_AfterRelease_pm");
	}

	public void testDist_Zilla_Role_BeforeArchive_pm()
	{
		doTest("Dist_Zilla_Role_BeforeArchive_pm");
	}

	public void testDist_Zilla_Role_BeforeBuild_pm()
	{
		doTest("Dist_Zilla_Role_BeforeBuild_pm");
	}

	public void testDist_Zilla_Role_BeforeMint_pm()
	{
		doTest("Dist_Zilla_Role_BeforeMint_pm");
	}

	public void testDist_Zilla_Role_BeforeRelease_pm()
	{
		doTest("Dist_Zilla_Role_BeforeRelease_pm");
	}

	public void testDist_Zilla_Role_BuildPL_pm()
	{
		doTest("Dist_Zilla_Role_BuildPL_pm");
	}

	public void testDist_Zilla_Role_BuildRunner_pm()
	{
		doTest("Dist_Zilla_Role_BuildRunner_pm");
	}

	public void testDist_Zilla_Role_Chrome_pm()
	{
		doTest("Dist_Zilla_Role_Chrome_pm");
	}

	public void testDist_Zilla_Role_ConfigDumper_pm()
	{
		doTest("Dist_Zilla_Role_ConfigDumper_pm");
	}

	public void testDist_Zilla_Role_EncodingProvider_pm()
	{
		doTest("Dist_Zilla_Role_EncodingProvider_pm");
	}

	public void testDist_Zilla_Role_ExecFiles_pm()
	{
		doTest("Dist_Zilla_Role_ExecFiles_pm");
	}

	public void testDist_Zilla_Role_FileFinderUser_pm()
	{
		doTest("Dist_Zilla_Role_FileFinderUser_pm");
	}

	public void testDist_Zilla_Role_FileFinder_pm()
	{
		doTest("Dist_Zilla_Role_FileFinder_pm");
	}

	public void testDist_Zilla_Role_FileGatherer_pm()
	{
		doTest("Dist_Zilla_Role_FileGatherer_pm");
	}

	public void testDist_Zilla_Role_FileInjector_pm()
	{
		doTest("Dist_Zilla_Role_FileInjector_pm");
	}

	public void testDist_Zilla_Role_FileMunger_pm()
	{
		doTest("Dist_Zilla_Role_FileMunger_pm");
	}

	public void testDist_Zilla_Role_FilePruner_pm()
	{
		doTest("Dist_Zilla_Role_FilePruner_pm");
	}

	public void testDist_Zilla_Role_File_pm()
	{
		doTest("Dist_Zilla_Role_File_pm");
	}

	public void testDist_Zilla_Role_InstallTool_pm()
	{
		doTest("Dist_Zilla_Role_InstallTool_pm");
	}

	public void testDist_Zilla_Role_LicenseProvider_pm()
	{
		doTest("Dist_Zilla_Role_LicenseProvider_pm");
	}

	public void testDist_Zilla_Role_MetaProvider_pm()
	{
		doTest("Dist_Zilla_Role_MetaProvider_pm");
	}

	public void testDist_Zilla_Role_MintingProfile_ShareDir_pm()
	{
		doTest("Dist_Zilla_Role_MintingProfile_ShareDir_pm");
	}

	public void testDist_Zilla_Role_MintingProfile_pm()
	{
		doTest("Dist_Zilla_Role_MintingProfile_pm");
	}

	public void testDist_Zilla_Role_ModuleMaker_pm()
	{
		doTest("Dist_Zilla_Role_ModuleMaker_pm");
	}

	public void testDist_Zilla_Role_MutableFile_pm()
	{
		doTest("Dist_Zilla_Role_MutableFile_pm");
	}

	public void testDist_Zilla_Role_NameProvider_pm()
	{
		doTest("Dist_Zilla_Role_NameProvider_pm");
	}

	public void testDist_Zilla_Role_PPI_pm()
	{
		doTest("Dist_Zilla_Role_PPI_pm");
	}

	public void testDist_Zilla_Role_PluginBundle_Easy_pm()
	{
		doTest("Dist_Zilla_Role_PluginBundle_Easy_pm");
	}

	public void testDist_Zilla_Role_PluginBundle_pm()
	{
		doTest("Dist_Zilla_Role_PluginBundle_pm");
	}

	public void testDist_Zilla_Role_Plugin_pm()
	{
		doTest("Dist_Zilla_Role_Plugin_pm");
	}

	public void testDist_Zilla_Role_PrereqSource_pm()
	{
		doTest("Dist_Zilla_Role_PrereqSource_pm");
	}

	public void testDist_Zilla_Role_ReleaseStatusProvider_pm()
	{
		doTest("Dist_Zilla_Role_ReleaseStatusProvider_pm");
	}

	public void testDist_Zilla_Role_Releaser_pm()
	{
		doTest("Dist_Zilla_Role_Releaser_pm");
	}

	public void testDist_Zilla_Role_ShareDir_pm()
	{
		doTest("Dist_Zilla_Role_ShareDir_pm");
	}

	public void testDist_Zilla_Role_Stash_Authors_pm()
	{
		doTest("Dist_Zilla_Role_Stash_Authors_pm");
	}

	public void testDist_Zilla_Role_Stash_Login_pm()
	{
		doTest("Dist_Zilla_Role_Stash_Login_pm");
	}

	public void testDist_Zilla_Role_Stash_pm()
	{
		doTest("Dist_Zilla_Role_Stash_pm");
	}

	public void testDist_Zilla_Role_StubBuild_pm()
	{
		doTest("Dist_Zilla_Role_StubBuild_pm");
	}

	public void testDist_Zilla_Role_TestRunner_pm()
	{
		doTest("Dist_Zilla_Role_TestRunner_pm");
	}

	public void testDist_Zilla_Role_TextTemplate_pm()
	{
		doTest("Dist_Zilla_Role_TextTemplate_pm");
	}

	public void testDist_Zilla_Role_VersionProvider_pm()
	{
		doTest("Dist_Zilla_Role_VersionProvider_pm");
	}

	public void testDist_Zilla_Stash_Mint_pm()
	{
		doTest("Dist_Zilla_Stash_Mint_pm");
	}

	public void testDist_Zilla_Stash_PAUSE_pm()
	{
		doTest("Dist_Zilla_Stash_PAUSE_pm");
	}

	public void testDist_Zilla_Stash_Rights_pm()
	{
		doTest("Dist_Zilla_Stash_Rights_pm");
	}

	public void testDist_Zilla_Stash_User_pm()
	{
		doTest("Dist_Zilla_Stash_User_pm");
	}

	public void testDist_Zilla_Tester_pm()
	{
		doTest("Dist_Zilla_Tester_pm");
	}

	public void testDist_Zilla_Tutorial_pm()
	{
		doTest("Dist_Zilla_Tutorial_pm");
	}

	public void testDist_Zilla_Types_pm()
	{
		doTest("Dist_Zilla_Types_pm");
	}

	public void testDist_Zilla_Util_AuthorDeps_pm()
	{
		doTest("Dist_Zilla_Util_AuthorDeps_pm");
	}

	public void testDist_Zilla_Util_pm()
	{
		doTest("Dist_Zilla_Util_pm");
	}

	public void testDist_Zilla_pm()
	{
		doTest("Dist_Zilla_pm");
	}

	public void testDumpvalue_pm()
	{
		doTest("Dumpvalue_pm");
	}

	public void testDynaLoader_pm()
	{
		doTest("DynaLoader_pm");
	}

	public void testEmail_Abstract_EmailMIME_pm()
	{
		doTest("Email_Abstract_EmailMIME_pm");
	}

	public void testEmail_Abstract_EmailSimple_pm()
	{
		doTest("Email_Abstract_EmailSimple_pm");
	}

	public void testEmail_Abstract_MIMEEntity_pm()
	{
		doTest("Email_Abstract_MIMEEntity_pm");
	}

	public void testEmail_Abstract_MailInternet_pm()
	{
		doTest("Email_Abstract_MailInternet_pm");
	}

	public void testEmail_Abstract_MailMessage_pm()
	{
		doTest("Email_Abstract_MailMessage_pm");
	}

	public void testEmail_Abstract_Plugin_pm()
	{
		doTest("Email_Abstract_Plugin_pm");
	}

	public void testEmail_Abstract_pm()
	{
		doTest("Email_Abstract_pm");
	}

	public void testEmail_Address_pm()
	{
		doTest("Email_Address_pm");
	}

	public void testEmail_Date_Format_pm()
	{
		doTest("Email_Date_Format_pm");
	}

	public void testEmail_MIME_ContentType_pm()
	{
		doTest("Email_MIME_ContentType_pm");
	}

	public void testEmail_MIME_Creator_pm()
	{
		doTest("Email_MIME_Creator_pm");
	}

	public void testEmail_MIME_Encode_pm()
	{
		doTest("Email_MIME_Encode_pm");
	}

	public void testEmail_MIME_Encodings_pm()
	{
		doTest("Email_MIME_Encodings_pm");
	}

	public void testEmail_MIME_Header_pm()
	{
		doTest("Email_MIME_Header_pm");
	}

	public void testEmail_MIME_Kit_Assembler_Standard_pm()
	{
		doTest("Email_MIME_Kit_Assembler_Standard_pm");
	}

	public void testEmail_MIME_Kit_KitReader_Dir_pm()
	{
		doTest("Email_MIME_Kit_KitReader_Dir_pm");
	}

	public void testEmail_MIME_Kit_ManifestReader_JSON_pm()
	{
		doTest("Email_MIME_Kit_ManifestReader_JSON_pm");
	}

	public void testEmail_MIME_Kit_ManifestReader_YAML_pm()
	{
		doTest("Email_MIME_Kit_ManifestReader_YAML_pm");
	}

	public void testEmail_MIME_Kit_Renderer_TestRenderer_pm()
	{
		doTest("Email_MIME_Kit_Renderer_TestRenderer_pm");
	}

	public void testEmail_MIME_Kit_Role_Assembler_pm()
	{
		doTest("Email_MIME_Kit_Role_Assembler_pm");
	}

	public void testEmail_MIME_Kit_Role_Component_pm()
	{
		doTest("Email_MIME_Kit_Role_Component_pm");
	}

	public void testEmail_MIME_Kit_Role_KitReader_pm()
	{
		doTest("Email_MIME_Kit_Role_KitReader_pm");
	}

	public void testEmail_MIME_Kit_Role_ManifestDesugarer_pm()
	{
		doTest("Email_MIME_Kit_Role_ManifestDesugarer_pm");
	}

	public void testEmail_MIME_Kit_Role_ManifestReader_pm()
	{
		doTest("Email_MIME_Kit_Role_ManifestReader_pm");
	}

	public void testEmail_MIME_Kit_Role_Renderer_pm()
	{
		doTest("Email_MIME_Kit_Role_Renderer_pm");
	}

	public void testEmail_MIME_Kit_Role_Validator_pm()
	{
		doTest("Email_MIME_Kit_Role_Validator_pm");
	}

	public void testEmail_MIME_Kit_pm()
	{
		doTest("Email_MIME_Kit_pm");
	}

	public void testEmail_MIME_Modifier_pm()
	{
		doTest("Email_MIME_Modifier_pm");
	}

	public void testEmail_MIME_pm()
	{
		doTest("Email_MIME_pm");
	}

	public void testEmail_MessageID_pm()
	{
		doTest("Email_MessageID_pm");
	}

	public void testEmail_Sender_Failure_Multi_pm()
	{
		doTest("Email_Sender_Failure_Multi_pm");
	}

	public void testEmail_Sender_Failure_Permanent_pm()
	{
		doTest("Email_Sender_Failure_Permanent_pm");
	}

	public void testEmail_Sender_Failure_Temporary_pm()
	{
		doTest("Email_Sender_Failure_Temporary_pm");
	}

	public void testEmail_Sender_Failure_pm()
	{
		doTest("Email_Sender_Failure_pm");
	}

	public void testEmail_Sender_Manual_QuickStart_pm()
	{
		doTest("Email_Sender_Manual_QuickStart_pm");
	}

	public void testEmail_Sender_Manual_pm()
	{
		doTest("Email_Sender_Manual_pm");
	}

	public void testEmail_Sender_Role_CommonSending_pm()
	{
		doTest("Email_Sender_Role_CommonSending_pm");
	}

	public void testEmail_Sender_Role_HasMessage_pm()
	{
		doTest("Email_Sender_Role_HasMessage_pm");
	}

	public void testEmail_Sender_Simple_pm()
	{
		doTest("Email_Sender_Simple_pm");
	}

	public void testEmail_Sender_Success_Partial_pm()
	{
		doTest("Email_Sender_Success_Partial_pm");
	}

	public void testEmail_Sender_Success_pm()
	{
		doTest("Email_Sender_Success_pm");
	}

	public void testEmail_Sender_Transport_DevNull_pm()
	{
		doTest("Email_Sender_Transport_DevNull_pm");
	}

	public void testEmail_Sender_Transport_Failable_pm()
	{
		doTest("Email_Sender_Transport_Failable_pm");
	}

	public void testEmail_Sender_Transport_Maildir_pm()
	{
		doTest("Email_Sender_Transport_Maildir_pm");
	}

	public void testEmail_Sender_Transport_Mbox_pm()
	{
		doTest("Email_Sender_Transport_Mbox_pm");
	}

	public void testEmail_Sender_Transport_Print_pm()
	{
		doTest("Email_Sender_Transport_Print_pm");
	}

	public void testEmail_Sender_Transport_SMTP_Persistent_pm()
	{
		doTest("Email_Sender_Transport_SMTP_Persistent_pm");
	}

	public void testEmail_Sender_Transport_SMTP_pm()
	{
		doTest("Email_Sender_Transport_SMTP_pm");
	}

	public void testEmail_Sender_Transport_Sendmail_pm()
	{
		doTest("Email_Sender_Transport_Sendmail_pm");
	}

	public void testEmail_Sender_Transport_Test_pm()
	{
		doTest("Email_Sender_Transport_Test_pm");
	}

	public void testEmail_Sender_Transport_Wrapper_pm()
	{
		doTest("Email_Sender_Transport_Wrapper_pm");
	}

	public void testEmail_Sender_Transport_pm()
	{
		doTest("Email_Sender_Transport_pm");
	}

	public void testEmail_Sender_Util_pm()
	{
		doTest("Email_Sender_Util_pm");
	}

	public void testEmail_Sender_pm()
	{
		doTest("Email_Sender_pm");
	}

	public void testEmail_Simple_Creator_pm()
	{
		doTest("Email_Simple_Creator_pm");
	}

	public void testEmail_Simple_Header_pm()
	{
		doTest("Email_Simple_Header_pm");
	}

	public void testEmail_Simple_pm()
	{
		doTest("Email_Simple_pm");
	}

	public void testEmail_Stuffer_pm()
	{
		doTest("Email_Stuffer_pm");
	}

	public void testEmail_Valid_pm()
	{
		doTest("Email_Valid_pm");
	}

	public void testEncode_Alias_pm()
	{
		doTest("Encode_Alias_pm");
	}

	public void testEncode_Byte_pm()
	{
		doTest("Encode_Byte_pm");
	}

	public void testEncode_CJKConstants_pm()
	{
		doTest("Encode_CJKConstants_pm");
	}

	public void testEncode_CN_HZ_pm()
	{
		doTest("Encode_CN_HZ_pm");
	}

	public void testEncode_CN_pm()
	{
		doTest("Encode_CN_pm");
	}

	public void testEncode_Config_pm()
	{
		doTest("Encode_Config_pm");
	}

	public void testEncode_EBCDIC_pm()
	{
		doTest("Encode_EBCDIC_pm");
	}

	public void testEncode_Encoder_pm()
	{
		doTest("Encode_Encoder_pm");
	}

	public void testEncode_Encoding_pm()
	{
		doTest("Encode_Encoding_pm");
	}

	public void testEncode_GSM0338_pm()
	{
		doTest("Encode_GSM0338_pm");
	}

	public void testEncode_Guess_pm()
	{
		doTest("Encode_Guess_pm");
	}

	public void testEncode_JP_H2Z_pm()
	{
		doTest("Encode_JP_H2Z_pm");
	}

	public void testEncode_JP_JIS7_pm()
	{
		doTest("Encode_JP_JIS7_pm");
	}

	public void testEncode_JP_pm()
	{
		doTest("Encode_JP_pm");
	}

	public void testEncode_KR_2022_KR_pm()
	{
		doTest("Encode_KR_2022_KR_pm");
	}

	public void testEncode_KR_pm()
	{
		doTest("Encode_KR_pm");
	}

	public void testEncode_Locale_pm()
	{
		doTest("Encode_Locale_pm");
	}

	public void testEncode_MIME_Header_ISO_2022_JP_pm()
	{
		doTest("Encode_MIME_Header_ISO_2022_JP_pm");
	}

	public void testEncode_MIME_Header_pm()
	{
		doTest("Encode_MIME_Header_pm");
	}

	public void testEncode_MIME_Name_pm()
	{
		doTest("Encode_MIME_Name_pm");
	}

	public void testEncode_Symbol_pm()
	{
		doTest("Encode_Symbol_pm");
	}

	public void testEncode_TW_pm()
	{
		doTest("Encode_TW_pm");
	}

	public void testEncode_Unicode_UTF7_pm()
	{
		doTest("Encode_Unicode_UTF7_pm");
	}

	public void testEncode_Unicode_pm()
	{
		doTest("Encode_Unicode_pm");
	}

	public void testEncode_compat_5006001_pm()
	{
		doTest("Encode_compat_5006001_pm");
	}

	public void testEncode_compat_Alias_pm()
	{
		doTest("Encode_compat_Alias_pm");
	}

	public void testEncode_compat_common_pm()
	{
		doTest("Encode_compat_common_pm");
	}

	public void testEncode_compat_pm()
	{
		doTest("Encode_compat_pm");
	}

	public void testEncode_pm()
	{
		doTest("Encode_pm");
	}

	public void testEnglish_pm()
	{
		doTest("English_pm");
	}

	public void testEnv_pm()
	{
		doTest("Env_pm");
	}

	public void testErrno_pm()
	{
		doTest("Errno_pm");
	}

	public void testError_Simple_pm()
	{
		doTest("Error_Simple_pm");
	}

	public void testError_pm()
	{
		doTest("Error_pm");
	}

	public void testEval_Closure_pm()
	{
		doTest("Eval_Closure_pm");
	}

	public void testExcel_Writer_XLSX_Chart_Area_pm()
	{
		doTest("Excel_Writer_XLSX_Chart_Area_pm");
	}

	public void testExcel_Writer_XLSX_Chart_Bar_pm()
	{
		doTest("Excel_Writer_XLSX_Chart_Bar_pm");
	}

	public void testExcel_Writer_XLSX_Chart_Column_pm()
	{
		doTest("Excel_Writer_XLSX_Chart_Column_pm");
	}

	public void testExcel_Writer_XLSX_Chart_Doughnut_pm()
	{
		doTest("Excel_Writer_XLSX_Chart_Doughnut_pm");
	}

	public void testExcel_Writer_XLSX_Chart_Line_pm()
	{
		doTest("Excel_Writer_XLSX_Chart_Line_pm");
	}

	public void testExcel_Writer_XLSX_Chart_Pie_pm()
	{
		doTest("Excel_Writer_XLSX_Chart_Pie_pm");
	}

	public void testExcel_Writer_XLSX_Chart_Radar_pm()
	{
		doTest("Excel_Writer_XLSX_Chart_Radar_pm");
	}

	public void testExcel_Writer_XLSX_Chart_Scatter_pm()
	{
		doTest("Excel_Writer_XLSX_Chart_Scatter_pm");
	}

	public void testExcel_Writer_XLSX_Chart_Stock_pm()
	{
		doTest("Excel_Writer_XLSX_Chart_Stock_pm");
	}

	public void testExcel_Writer_XLSX_Chart_pm()
	{
		doTest("Excel_Writer_XLSX_Chart_pm");
	}

	public void testExcel_Writer_XLSX_Chartsheet_pm()
	{
		doTest("Excel_Writer_XLSX_Chartsheet_pm");
	}

	public void testExcel_Writer_XLSX_Drawing_pm()
	{
		doTest("Excel_Writer_XLSX_Drawing_pm");
	}

	public void testExcel_Writer_XLSX_Examples_pm()
	{
		doTest("Excel_Writer_XLSX_Examples_pm");
	}

	public void testExcel_Writer_XLSX_Format_pm()
	{
		doTest("Excel_Writer_XLSX_Format_pm");
	}

	public void testExcel_Writer_XLSX_Package_App_pm()
	{
		doTest("Excel_Writer_XLSX_Package_App_pm");
	}

	public void testExcel_Writer_XLSX_Package_Comments_pm()
	{
		doTest("Excel_Writer_XLSX_Package_Comments_pm");
	}

	public void testExcel_Writer_XLSX_Package_ContentTypes_pm()
	{
		doTest("Excel_Writer_XLSX_Package_ContentTypes_pm");
	}

	public void testExcel_Writer_XLSX_Package_Core_pm()
	{
		doTest("Excel_Writer_XLSX_Package_Core_pm");
	}

	public void testExcel_Writer_XLSX_Package_Packager_pm()
	{
		doTest("Excel_Writer_XLSX_Package_Packager_pm");
	}

	public void testExcel_Writer_XLSX_Package_Relationships_pm()
	{
		doTest("Excel_Writer_XLSX_Package_Relationships_pm");
	}

	public void testExcel_Writer_XLSX_Package_SharedStrings_pm()
	{
		doTest("Excel_Writer_XLSX_Package_SharedStrings_pm");
	}

	public void testExcel_Writer_XLSX_Package_Styles_pm()
	{
		doTest("Excel_Writer_XLSX_Package_Styles_pm");
	}

	public void testExcel_Writer_XLSX_Package_Table_pm()
	{
		doTest("Excel_Writer_XLSX_Package_Table_pm");
	}

	public void testExcel_Writer_XLSX_Package_Theme_pm()
	{
		doTest("Excel_Writer_XLSX_Package_Theme_pm");
	}

	public void testExcel_Writer_XLSX_Package_VML_pm()
	{
		doTest("Excel_Writer_XLSX_Package_VML_pm");
	}

	public void testExcel_Writer_XLSX_Package_XMLwriter_pm()
	{
		doTest("Excel_Writer_XLSX_Package_XMLwriter_pm");
	}

	public void testExcel_Writer_XLSX_Shape_pm()
	{
		doTest("Excel_Writer_XLSX_Shape_pm");
	}

	public void testExcel_Writer_XLSX_Utility_pm()
	{
		doTest("Excel_Writer_XLSX_Utility_pm");
	}

	public void testExcel_Writer_XLSX_Workbook_pm()
	{
		doTest("Excel_Writer_XLSX_Workbook_pm");
	}

	public void testExcel_Writer_XLSX_Worksheet_pm()
	{
		doTest("Excel_Writer_XLSX_Worksheet_pm");
	}

	public void testExcel_Writer_XLSX_pm()
	{
		doTest("Excel_Writer_XLSX_pm");
	}

	public void testException_Class_Base_pm()
	{
		doTest("Exception_Class_Base_pm");
	}

	public void testException_Class_pm()
	{
		doTest("Exception_Class_pm");
	}

	public void testExporter_Heavy_pm()
	{
		doTest("Exporter_Heavy_pm");
	}

	public void testExporter_Shiny_pm()
	{
		doTest("Exporter_Shiny_pm");
	}

	public void testExporter_Tiny_pm()
	{
		doTest("Exporter_Tiny_pm");
	}

	public void testExporter_pm()
	{
		doTest("Exporter_pm");
	}

	public void testExtUtils_CBuilder_Base_pm()
	{
		doTest("ExtUtils_CBuilder_Base_pm");
	}

	public void testExtUtils_CBuilder_Platform_Unix_pm()
	{
		doTest("ExtUtils_CBuilder_Platform_Unix_pm");
	}

	public void testExtUtils_CBuilder_Platform_VMS_pm()
	{
		doTest("ExtUtils_CBuilder_Platform_VMS_pm");
	}

	public void testExtUtils_CBuilder_Platform_Windows_BCC_pm()
	{
		doTest("ExtUtils_CBuilder_Platform_Windows_BCC_pm");
	}

	public void testExtUtils_CBuilder_Platform_Windows_GCC_pm()
	{
		doTest("ExtUtils_CBuilder_Platform_Windows_GCC_pm");
	}

	public void testExtUtils_CBuilder_Platform_Windows_MSVC_pm()
	{
		doTest("ExtUtils_CBuilder_Platform_Windows_MSVC_pm");
	}

	public void testExtUtils_CBuilder_Platform_Windows_pm()
	{
		doTest("ExtUtils_CBuilder_Platform_Windows_pm");
	}

	public void testExtUtils_CBuilder_Platform_aix_pm()
	{
		doTest("ExtUtils_CBuilder_Platform_aix_pm");
	}

	public void testExtUtils_CBuilder_Platform_android_pm()
	{
		doTest("ExtUtils_CBuilder_Platform_android_pm");
	}

	public void testExtUtils_CBuilder_Platform_cygwin_pm()
	{
		doTest("ExtUtils_CBuilder_Platform_cygwin_pm");
	}

	public void testExtUtils_CBuilder_Platform_darwin_pm()
	{
		doTest("ExtUtils_CBuilder_Platform_darwin_pm");
	}

	public void testExtUtils_CBuilder_Platform_dec_osf_pm()
	{
		doTest("ExtUtils_CBuilder_Platform_dec_osf_pm");
	}

	public void testExtUtils_CBuilder_Platform_os2_pm()
	{
		doTest("ExtUtils_CBuilder_Platform_os2_pm");
	}

	public void testExtUtils_CBuilder_pm()
	{
		doTest("ExtUtils_CBuilder_pm");
	}

	public void testExtUtils_Command_MM_pm()
	{
		doTest("ExtUtils_Command_MM_pm");
	}

	public void testExtUtils_Command_pm()
	{
		doTest("ExtUtils_Command_pm");
	}

	public void testExtUtils_Config_pm()
	{
		doTest("ExtUtils_Config_pm");
	}

	public void testExtUtils_Constant_Base_pm()
	{
		doTest("ExtUtils_Constant_Base_pm");
	}

	public void testExtUtils_Constant_ProxySubs_pm()
	{
		doTest("ExtUtils_Constant_ProxySubs_pm");
	}

	public void testExtUtils_Constant_Utils_pm()
	{
		doTest("ExtUtils_Constant_Utils_pm");
	}

	public void testExtUtils_Constant_XS_pm()
	{
		doTest("ExtUtils_Constant_XS_pm");
	}

	public void testExtUtils_Constant_pm()
	{
		doTest("ExtUtils_Constant_pm");
	}

	public void testExtUtils_Depends_pm()
	{
		doTest("ExtUtils_Depends_pm");
	}

	public void testExtUtils_Embed_pm()
	{
		doTest("ExtUtils_Embed_pm");
	}

	public void testExtUtils_F77_pm()
	{
		doTest("ExtUtils_F77_pm");
	}

	public void testExtUtils_Helpers_Unix_pm()
	{
		doTest("ExtUtils_Helpers_Unix_pm");
	}

	public void testExtUtils_Helpers_VMS_pm()
	{
		doTest("ExtUtils_Helpers_VMS_pm");
	}

	public void testExtUtils_Helpers_Windows_pm()
	{
		doTest("ExtUtils_Helpers_Windows_pm");
	}

	public void testExtUtils_Helpers_pm()
	{
		doTest("ExtUtils_Helpers_pm");
	}

	public void testExtUtils_InstallPaths_pm()
	{
		doTest("ExtUtils_InstallPaths_pm");
	}

	public void testExtUtils_Install_pm()
	{
		doTest("ExtUtils_Install_pm");
	}

	public void testExtUtils_Installed_pm()
	{
		doTest("ExtUtils_Installed_pm");
	}

	public void testExtUtils_Liblist_Kid_pm()
	{
		doTest("ExtUtils_Liblist_Kid_pm");
	}

	public void testExtUtils_Liblist_pm()
	{
		doTest("ExtUtils_Liblist_pm");
	}

	public void testExtUtils_MM_AIX_pm()
	{
		doTest("ExtUtils_MM_AIX_pm");
	}

	public void testExtUtils_MM_Any_pm()
	{
		doTest("ExtUtils_MM_Any_pm");
	}

	public void testExtUtils_MM_BeOS_pm()
	{
		doTest("ExtUtils_MM_BeOS_pm");
	}

	public void testExtUtils_MM_Cygwin_pm()
	{
		doTest("ExtUtils_MM_Cygwin_pm");
	}

	public void testExtUtils_MM_DOS_pm()
	{
		doTest("ExtUtils_MM_DOS_pm");
	}

	public void testExtUtils_MM_Darwin_pm()
	{
		doTest("ExtUtils_MM_Darwin_pm");
	}

	public void testExtUtils_MM_MacOS_pm()
	{
		doTest("ExtUtils_MM_MacOS_pm");
	}

	public void testExtUtils_MM_NW5_pm()
	{
		doTest("ExtUtils_MM_NW5_pm");
	}

	public void testExtUtils_MM_OS2_pm()
	{
		doTest("ExtUtils_MM_OS2_pm");
	}

	public void testExtUtils_MM_QNX_pm()
	{
		doTest("ExtUtils_MM_QNX_pm");
	}

	public void testExtUtils_MM_UWIN_pm()
	{
		doTest("ExtUtils_MM_UWIN_pm");
	}

	public void testExtUtils_MM_Unix_pm()
	{
		doTest("ExtUtils_MM_Unix_pm");
	}

	public void testExtUtils_MM_VMS_pm()
	{
		doTest("ExtUtils_MM_VMS_pm");
	}

	public void testExtUtils_MM_VOS_pm()
	{
		doTest("ExtUtils_MM_VOS_pm");
	}

	public void testExtUtils_MM_Win32_pm()
	{
		doTest("ExtUtils_MM_Win32_pm");
	}

	public void testExtUtils_MM_Win95_pm()
	{
		doTest("ExtUtils_MM_Win95_pm");
	}

	public void testExtUtils_MM_pm()
	{
		doTest("ExtUtils_MM_pm");
	}

	public void testExtUtils_MY_pm()
	{
		doTest("ExtUtils_MY_pm");
	}

	public void testExtUtils_MakeMaker_CPANfile_pm()
	{
		doTest("ExtUtils_MakeMaker_CPANfile_pm");
	}

	public void testExtUtils_MakeMaker_Config_pm()
	{
		doTest("ExtUtils_MakeMaker_Config_pm");
	}

	public void testExtUtils_MakeMaker_Locale_pm()
	{
		doTest("ExtUtils_MakeMaker_Locale_pm");
	}

	public void testExtUtils_MakeMaker_pm()
	{
		doTest("ExtUtils_MakeMaker_pm");
	}

	public void testExtUtils_MakeMaker_version_pm()
	{
		doTest("ExtUtils_MakeMaker_version_pm");
	}

	public void testExtUtils_MakeMaker_version_regex_pm()
	{
		doTest("ExtUtils_MakeMaker_version_regex_pm");
	}

	public void testExtUtils_MakeMaker_version_vpp_pm()
	{
		doTest("ExtUtils_MakeMaker_version_vpp_pm");
	}

	public void testExtUtils_Manifest_pm()
	{
		doTest("ExtUtils_Manifest_pm");
	}

	public void testExtUtils_Miniperl_pm()
	{
		doTest("ExtUtils_Miniperl_pm");
	}

	public void testExtUtils_Mkbootstrap_pm()
	{
		doTest("ExtUtils_Mkbootstrap_pm");
	}

	public void testExtUtils_Mksymlists_pm()
	{
		doTest("ExtUtils_Mksymlists_pm");
	}

	public void testExtUtils_Packlist_pm()
	{
		doTest("ExtUtils_Packlist_pm");
	}

	public void testExtUtils_ParseXS_Constants_pm()
	{
		doTest("ExtUtils_ParseXS_Constants_pm");
	}

	public void testExtUtils_ParseXS_CountLines_pm()
	{
		doTest("ExtUtils_ParseXS_CountLines_pm");
	}

	public void testExtUtils_ParseXS_Eval_pm()
	{
		doTest("ExtUtils_ParseXS_Eval_pm");
	}

	public void testExtUtils_ParseXS_Utilities_pm()
	{
		doTest("ExtUtils_ParseXS_Utilities_pm");
	}

	public void testExtUtils_ParseXS_pm()
	{
		doTest("ExtUtils_ParseXS_pm");
	}

	public void testExtUtils_PkgConfig_pm()
	{
		doTest("ExtUtils_PkgConfig_pm");
	}

	public void testExtUtils_Typemaps_Cmd_pm()
	{
		doTest("ExtUtils_Typemaps_Cmd_pm");
	}

	public void testExtUtils_Typemaps_InputMap_pm()
	{
		doTest("ExtUtils_Typemaps_InputMap_pm");
	}

	public void testExtUtils_Typemaps_OutputMap_pm()
	{
		doTest("ExtUtils_Typemaps_OutputMap_pm");
	}

	public void testExtUtils_Typemaps_Type_pm()
	{
		doTest("ExtUtils_Typemaps_Type_pm");
	}

	public void testExtUtils_Typemaps_pm()
	{
		doTest("ExtUtils_Typemaps_pm");
	}

	public void testExtUtils_testlib_pm()
	{
		doTest("ExtUtils_testlib_pm");
	}

	public void testFCGI_pm()
	{
		doTest("FCGI_pm");
	}

	public void testFFI_Raw_Callback_pm()
	{
		doTest("FFI_Raw_Callback_pm");
	}

	public void testFFI_Raw_MemPtr_pm()
	{
		doTest("FFI_Raw_MemPtr_pm");
	}

	public void testFFI_Raw_Ptr_pm()
	{
		doTest("FFI_Raw_Ptr_pm");
	}

	public void testFFI_Raw_pm()
	{
		doTest("FFI_Raw_pm");
	}

	public void testFatal_pm()
	{
		doTest("Fatal_pm");
	}

	public void testFcntl_pm()
	{
		doTest("Fcntl_pm");
	}

	public void testFh_pm()
	{
		doTest("Fh_pm");
	}

	public void testFileCache_pm()
	{
		doTest("FileCache_pm");
	}

	public void testFileHandle_pm()
	{
		doTest("FileHandle_pm");
	}

	public void testFile_Basename_pm()
	{
		doTest("File_Basename_pm");
	}

	public void testFile_CheckTree_pm()
	{
		doTest("File_CheckTree_pm");
	}

	public void testFile_Compare_pm()
	{
		doTest("File_Compare_pm");
	}

	public void testFile_Copy_Recursive_pm()
	{
		doTest("File_Copy_Recursive_pm");
	}

	public void testFile_Copy_pm()
	{
		doTest("File_Copy_pm");
	}

	public void testFile_DosGlob_pm()
	{
		doTest("File_DosGlob_pm");
	}

	public void testFile_Fetch_pm()
	{
		doTest("File_Fetch_pm");
	}

	public void testFile_Find_Object_Base_pm()
	{
		doTest("File_Find_Object_Base_pm");
	}

	public void testFile_Find_Object_PathComp_pm()
	{
		doTest("File_Find_Object_PathComp_pm");
	}

	public void testFile_Find_Object_Result_pm()
	{
		doTest("File_Find_Object_Result_pm");
	}

	public void testFile_Find_Object_pm()
	{
		doTest("File_Find_Object_pm");
	}

	public void testFile_Find_Rule_Perl_pm()
	{
		doTest("File_Find_Rule_Perl_pm");
	}

	public void testFile_Find_Rule_pm()
	{
		doTest("File_Find_Rule_pm");
	}

	public void testFile_Find_pm()
	{
		doTest("File_Find_pm");
	}

	public void testFile_GlobMapper_pm()
	{
		doTest("File_GlobMapper_pm");
	}

	public void testFile_Glob_pm()
	{
		doTest("File_Glob_pm");
	}

	public void testFile_HomeDir_Darwin_Carbon_pm()
	{
		doTest("File_HomeDir_Darwin_Carbon_pm");
	}

	public void testFile_HomeDir_Darwin_Cocoa_pm()
	{
		doTest("File_HomeDir_Darwin_Cocoa_pm");
	}

	public void testFile_HomeDir_Darwin_pm()
	{
		doTest("File_HomeDir_Darwin_pm");
	}

	public void testFile_HomeDir_Driver_pm()
	{
		doTest("File_HomeDir_Driver_pm");
	}

	public void testFile_HomeDir_FreeDesktop_pm()
	{
		doTest("File_HomeDir_FreeDesktop_pm");
	}

	public void testFile_HomeDir_MacOS9_pm()
	{
		doTest("File_HomeDir_MacOS9_pm");
	}

	public void testFile_HomeDir_Test_pm()
	{
		doTest("File_HomeDir_Test_pm");
	}

	public void testFile_HomeDir_Unix_pm()
	{
		doTest("File_HomeDir_Unix_pm");
	}

	public void testFile_HomeDir_Windows_pm()
	{
		doTest("File_HomeDir_Windows_pm");
	}

	public void testFile_HomeDir_pm()
	{
		doTest("File_HomeDir_pm");
	}

	public void testFile_Listing_pm()
	{
		doTest("File_Listing_pm");
	}

	public void testFile_Map_pm()
	{
		doTest("File_Map_pm");
	}

	public void testFile_Path_pm()
	{
		doTest("File_Path_pm");
	}

	public void testFile_Remove_pm()
	{
		doTest("File_Remove_pm");
	}

	public void testFile_ShareDir_Install_pm()
	{
		doTest("File_ShareDir_Install_pm");
	}

	public void testFile_ShareDir_pm()
	{
		doTest("File_ShareDir_pm");
	}

	public void testFile_Slurp_Tiny_pm()
	{
		doTest("File_Slurp_Tiny_pm");
	}

	public void testFile_Slurp_benchmark_pl()
	{
		doTest("File_Slurp_benchmark_pl");
	}

	public void testFile_Slurp_pm()
	{
		doTest("File_Slurp_pm");
	}

	public void testFile_Spec_Cygwin_pm()
	{
		doTest("File_Spec_Cygwin_pm");
	}

	public void testFile_Spec_Epoc_pm()
	{
		doTest("File_Spec_Epoc_pm");
	}

	public void testFile_Spec_Functions_pm()
	{
		doTest("File_Spec_Functions_pm");
	}

	public void testFile_Spec_Mac_pm()
	{
		doTest("File_Spec_Mac_pm");
	}

	public void testFile_Spec_OS2_pm()
	{
		doTest("File_Spec_OS2_pm");
	}

	public void testFile_Spec_Unix_pm()
	{
		doTest("File_Spec_Unix_pm");
	}

	public void testFile_Spec_VMS_pm()
	{
		doTest("File_Spec_VMS_pm");
	}

	public void testFile_Spec_Win32_pm()
	{
		doTest("File_Spec_Win32_pm");
	}

	public void testFile_Spec_pm()
	{
		doTest("File_Spec_pm");
	}

	public void testFile_Temp_pm()
	{
		doTest("File_Temp_pm");
	}

	public void testFile_Which_pm()
	{
		doTest("File_Which_pm");
	}

	public void testFile_pushd_pm()
	{
		doTest("File_pushd_pm");
	}

	public void testFile_stat_pm()
	{
		doTest("File_stat_pm");
	}

	public void testFilter_Simple_pm()
	{
		doTest("Filter_Simple_pm");
	}

	public void testFilter_Util_Call_pm()
	{
		doTest("Filter_Util_Call_pm");
	}

	public void testFindBin_pm()
	{
		doTest("FindBin_pm");
	}

	public void testGDBM_File_pm()
	{
		doTest("GDBM_File_pm");
	}

	public void testGD_Group_pm()
	{
		doTest("GD_Group_pm");
	}

	public void testGD_Image_pm()
	{
		doTest("GD_Image_pm");
	}

	public void testGD_Polygon_pm()
	{
		doTest("GD_Polygon_pm");
	}

	public void testGD_Polyline_pm()
	{
		doTest("GD_Polyline_pm");
	}

	public void testGD_Simple_pm()
	{
		doTest("GD_Simple_pm");
	}

	public void testGD_pm()
	{
		doTest("GD_pm");
	}

	public void testGetopt_Long_Descriptive_Opts_pm()
	{
		doTest("Getopt_Long_Descriptive_Opts_pm");
	}

	public void testGetopt_Long_Descriptive_Usage_pm()
	{
		doTest("Getopt_Long_Descriptive_Usage_pm");
	}

	public void testGetopt_Long_Descriptive_pm()
	{
		doTest("Getopt_Long_Descriptive_pm");
	}

	public void testGetopt_Long_pm()
	{
		doTest("Getopt_Long_pm");
	}

	public void testGetopt_Std_pm()
	{
		doTest("Getopt_Std_pm");
	}

	public void testGraphics_ColorUtils_pm()
	{
		doTest("Graphics_ColorUtils_pm");
	}

	public void testGuard_pm()
	{
		doTest("Guard_pm");
	}

	public void testHTML_AsSubs_pm()
	{
		doTest("HTML_AsSubs_pm");
	}

	public void testHTML_Element_pm()
	{
		doTest("HTML_Element_pm");
	}

	public void testHTML_Element_traverse_pm()
	{
		doTest("HTML_Element_traverse_pm");
	}

	public void testHTML_Entities_pm()
	{
		doTest("HTML_Entities_pm");
	}

	public void testHTML_Filter_pm()
	{
		doTest("HTML_Filter_pm");
	}

	public void testHTML_Form_pm()
	{
		doTest("HTML_Form_pm");
	}

	public void testHTML_HeadParser_pm()
	{
		doTest("HTML_HeadParser_pm");
	}

	public void testHTML_LinkExtor_pm()
	{
		doTest("HTML_LinkExtor_pm");
	}

	public void testHTML_Mason_ApacheHandler_pm()
	{
		doTest("HTML_Mason_ApacheHandler_pm");
	}

	public void testHTML_Mason_Apache_Request_pm()
	{
		doTest("HTML_Mason_Apache_Request_pm");
	}

	public void testHTML_Mason_CGIHandler_pm()
	{
		doTest("HTML_Mason_CGIHandler_pm");
	}

	public void testHTML_Mason_Cache_BaseCache_pm()
	{
		doTest("HTML_Mason_Cache_BaseCache_pm");
	}

	public void testHTML_Mason_Compiler_ToObject_pm()
	{
		doTest("HTML_Mason_Compiler_ToObject_pm");
	}

	public void testHTML_Mason_Compiler_pm()
	{
		doTest("HTML_Mason_Compiler_pm");
	}

	public void testHTML_Mason_ComponentSource_pm()
	{
		doTest("HTML_Mason_ComponentSource_pm");
	}

	public void testHTML_Mason_Component_FileBased_pm()
	{
		doTest("HTML_Mason_Component_FileBased_pm");
	}

	public void testHTML_Mason_Component_Subcomponent_pm()
	{
		doTest("HTML_Mason_Component_Subcomponent_pm");
	}

	public void testHTML_Mason_Component_pm()
	{
		doTest("HTML_Mason_Component_pm");
	}

	public void testHTML_Mason_Escapes_pm()
	{
		doTest("HTML_Mason_Escapes_pm");
	}

	public void testHTML_Mason_Exceptions_pm()
	{
		doTest("HTML_Mason_Exceptions_pm");
	}

	public void testHTML_Mason_FakeApache_pm()
	{
		doTest("HTML_Mason_FakeApache_pm");
	}

	public void testHTML_Mason_Handler_pm()
	{
		doTest("HTML_Mason_Handler_pm");
	}

	public void testHTML_Mason_Interp_pm()
	{
		doTest("HTML_Mason_Interp_pm");
	}

	public void testHTML_Mason_Lexer_pm()
	{
		doTest("HTML_Mason_Lexer_pm");
	}

	public void testHTML_Mason_MethodMaker_pm()
	{
		doTest("HTML_Mason_MethodMaker_pm");
	}

	public void testHTML_Mason_Parser_pm()
	{
		doTest("HTML_Mason_Parser_pm");
	}

	public void testHTML_Mason_Plugin_Context_pm()
	{
		doTest("HTML_Mason_Plugin_Context_pm");
	}

	public void testHTML_Mason_Plugin_pm()
	{
		doTest("HTML_Mason_Plugin_pm");
	}

	public void testHTML_Mason_Request_pm()
	{
		doTest("HTML_Mason_Request_pm");
	}

	public void testHTML_Mason_Resolver_File_pm()
	{
		doTest("HTML_Mason_Resolver_File_pm");
	}

	public void testHTML_Mason_Resolver_Null_pm()
	{
		doTest("HTML_Mason_Resolver_Null_pm");
	}

	public void testHTML_Mason_Resolver_pm()
	{
		doTest("HTML_Mason_Resolver_pm");
	}

	public void testHTML_Mason_Tests_pm()
	{
		doTest("HTML_Mason_Tests_pm");
	}

	public void testHTML_Mason_Tools_pm()
	{
		doTest("HTML_Mason_Tools_pm");
	}

	public void testHTML_Mason_Utils_pm()
	{
		doTest("HTML_Mason_Utils_pm");
	}

	public void testHTML_Mason_pm()
	{
		doTest("HTML_Mason_pm");
	}

	public void testHTML_Parse_pm()
	{
		doTest("HTML_Parse_pm");
	}

	public void testHTML_Parser_pm()
	{
		doTest("HTML_Parser_pm");
	}

	public void testHTML_PullParser_pm()
	{
		doTest("HTML_PullParser_pm");
	}

	public void testHTML_Tagset_pm()
	{
		doTest("HTML_Tagset_pm");
	}

	public void testHTML_TokeParser_pm()
	{
		doTest("HTML_TokeParser_pm");
	}

	public void testHTML_TreeBuilder_pm()
	{
		doTest("HTML_TreeBuilder_pm");
	}

	public void testHTML_Tree_pm()
	{
		doTest("HTML_Tree_pm");
	}

	public void testHTTP_Config_pm()
	{
		doTest("HTTP_Config_pm");
	}

	public void testHTTP_Cookies_Microsoft_pm()
	{
		doTest("HTTP_Cookies_Microsoft_pm");
	}

	public void testHTTP_Cookies_Netscape_pm()
	{
		doTest("HTTP_Cookies_Netscape_pm");
	}

	public void testHTTP_Cookies_pm()
	{
		doTest("HTTP_Cookies_pm");
	}

	public void testHTTP_Daemon_pm()
	{
		doTest("HTTP_Daemon_pm");
	}

	public void testHTTP_Date_pm()
	{
		doTest("HTTP_Date_pm");
	}

	public void testHTTP_Headers_Auth_pm()
	{
		doTest("HTTP_Headers_Auth_pm");
	}

	public void testHTTP_Headers_ETag_pm()
	{
		doTest("HTTP_Headers_ETag_pm");
	}

	public void testHTTP_Headers_Util_pm()
	{
		doTest("HTTP_Headers_Util_pm");
	}

	public void testHTTP_Headers_pm()
	{
		doTest("HTTP_Headers_pm");
	}

	public void testHTTP_Message_pm()
	{
		doTest("HTTP_Message_pm");
	}

	public void testHTTP_Negotiate_pm()
	{
		doTest("HTTP_Negotiate_pm");
	}

	public void testHTTP_Request_Common_pm()
	{
		doTest("HTTP_Request_Common_pm");
	}

	public void testHTTP_Request_pm()
	{
		doTest("HTTP_Request_pm");
	}

	public void testHTTP_Response_pm()
	{
		doTest("HTTP_Response_pm");
	}

	public void testHTTP_Server_Simple_CGI_Environment_pm()
	{
		doTest("HTTP_Server_Simple_CGI_Environment_pm");
	}

	public void testHTTP_Server_Simple_CGI_pm()
	{
		doTest("HTTP_Server_Simple_CGI_pm");
	}

	public void testHTTP_Server_Simple_pm()
	{
		doTest("HTTP_Server_Simple_pm");
	}

	public void testHTTP_Status_pm()
	{
		doTest("HTTP_Status_pm");
	}

	public void testHTTP_Tiny_pm()
	{
		doTest("HTTP_Tiny_pm");
	}

	public void testHash_Merge_pm()
	{
		doTest("Hash_Merge_pm");
	}

	public void testHash_Util_FieldHash_pm()
	{
		doTest("Hash_Util_FieldHash_pm");
	}

	public void testHash_Util_pm()
	{
		doTest("Hash_Util_pm");
	}

	public void testHook_LexWrap_pm()
	{
		doTest("Hook_LexWrap_pm");
	}

	public void testI18N_Collate_pm()
	{
		doTest("I18N_Collate_pm");
	}

	public void testI18N_LangTags_Detect_pm()
	{
		doTest("I18N_LangTags_Detect_pm");
	}

	public void testI18N_LangTags_List_pm()
	{
		doTest("I18N_LangTags_List_pm");
	}

	public void testI18N_LangTags_pm()
	{
		doTest("I18N_LangTags_pm");
	}

	public void testIO_All_Base_pm()
	{
		doTest("IO_All_Base_pm");
	}

	public void testIO_All_DBM_pm()
	{
		doTest("IO_All_DBM_pm");
	}

	public void testIO_All_Dir_pm()
	{
		doTest("IO_All_Dir_pm");
	}

	public void testIO_All_File_pm()
	{
		doTest("IO_All_File_pm");
	}

	public void testIO_All_Filesys_pm()
	{
		doTest("IO_All_Filesys_pm");
	}

	public void testIO_All_Link_pm()
	{
		doTest("IO_All_Link_pm");
	}

	public void testIO_All_MLDBM_pm()
	{
		doTest("IO_All_MLDBM_pm");
	}

	public void testIO_All_Pipe_pm()
	{
		doTest("IO_All_Pipe_pm");
	}

	public void testIO_All_STDIO_pm()
	{
		doTest("IO_All_STDIO_pm");
	}

	public void testIO_All_Socket_pm()
	{
		doTest("IO_All_Socket_pm");
	}

	public void testIO_All_String_pm()
	{
		doTest("IO_All_String_pm");
	}

	public void testIO_All_Temp_pm()
	{
		doTest("IO_All_Temp_pm");
	}

	public void testIO_All_pm()
	{
		doTest("IO_All_pm");
	}

	public void testIO_AtomicFile_pm()
	{
		doTest("IO_AtomicFile_pm");
	}

	public void testIO_CaptureOutput_pm()
	{
		doTest("IO_CaptureOutput_pm");
	}

	public void testIO_Capture_Stderr_pm()
	{
		doTest("IO_Capture_Stderr_pm");
	}

	public void testIO_Capture_Stdout_pm()
	{
		doTest("IO_Capture_Stdout_pm");
	}

	public void testIO_Capture_Tie_STDx_pm()
	{
		doTest("IO_Capture_Tie_STDx_pm");
	}

	public void testIO_Capture_pm()
	{
		doTest("IO_Capture_pm");
	}

	public void testIO_Compress_Adapter_Bzip2_pm()
	{
		doTest("IO_Compress_Adapter_Bzip2_pm");
	}

	public void testIO_Compress_Adapter_Deflate_pm()
	{
		doTest("IO_Compress_Adapter_Deflate_pm");
	}

	public void testIO_Compress_Adapter_Identity_pm()
	{
		doTest("IO_Compress_Adapter_Identity_pm");
	}

	public void testIO_Compress_Adapter_Lzma_pm()
	{
		doTest("IO_Compress_Adapter_Lzma_pm");
	}

	public void testIO_Compress_Adapter_Xz_pm()
	{
		doTest("IO_Compress_Adapter_Xz_pm");
	}

	public void testIO_Compress_Base_Common_pm()
	{
		doTest("IO_Compress_Base_Common_pm");
	}

	public void testIO_Compress_Base_pm()
	{
		doTest("IO_Compress_Base_pm");
	}

	public void testIO_Compress_Bzip2_pm()
	{
		doTest("IO_Compress_Bzip2_pm");
	}

	public void testIO_Compress_Deflate_pm()
	{
		doTest("IO_Compress_Deflate_pm");
	}

	public void testIO_Compress_Gzip_Constants_pm()
	{
		doTest("IO_Compress_Gzip_Constants_pm");
	}

	public void testIO_Compress_Gzip_pm()
	{
		doTest("IO_Compress_Gzip_pm");
	}

	public void testIO_Compress_Lzma_pm()
	{
		doTest("IO_Compress_Lzma_pm");
	}

	public void testIO_Compress_RawDeflate_pm()
	{
		doTest("IO_Compress_RawDeflate_pm");
	}

	public void testIO_Compress_Xz_pm()
	{
		doTest("IO_Compress_Xz_pm");
	}

	public void testIO_Compress_Zip_Constants_pm()
	{
		doTest("IO_Compress_Zip_Constants_pm");
	}

	public void testIO_Compress_Zip_pm()
	{
		doTest("IO_Compress_Zip_pm");
	}

	public void testIO_Compress_Zlib_Constants_pm()
	{
		doTest("IO_Compress_Zlib_Constants_pm");
	}

	public void testIO_Compress_Zlib_Extra_pm()
	{
		doTest("IO_Compress_Zlib_Extra_pm");
	}

	public void testIO_Dir_pm()
	{
		doTest("IO_Dir_pm");
	}

	public void testIO_File_pm()
	{
		doTest("IO_File_pm");
	}

	public void testIO_HTML_pm()
	{
		doTest("IO_HTML_pm");
	}

	public void testIO_Handle_pm()
	{
		doTest("IO_Handle_pm");
	}

	public void testIO_InnerFile_pm()
	{
		doTest("IO_InnerFile_pm");
	}

	public void testIO_Interactive_pm()
	{
		doTest("IO_Interactive_pm");
	}

	public void testIO_Lines_pm()
	{
		doTest("IO_Lines_pm");
	}

	public void testIO_Pipe_pm()
	{
		doTest("IO_Pipe_pm");
	}

	public void testIO_Poll_pm()
	{
		doTest("IO_Poll_pm");
	}

	public void testIO_ScalarArray_pm()
	{
		doTest("IO_ScalarArray_pm");
	}

	public void testIO_Scalar_pm()
	{
		doTest("IO_Scalar_pm");
	}

	public void testIO_Seekable_pm()
	{
		doTest("IO_Seekable_pm");
	}

	public void testIO_Select_pm()
	{
		doTest("IO_Select_pm");
	}

	public void testIO_SessionData_pm()
	{
		doTest("IO_SessionData_pm");
	}

	public void testIO_SessionSet_pm()
	{
		doTest("IO_SessionSet_pm");
	}

	public void testIO_Socket_INET6_pm()
	{
		doTest("IO_Socket_INET6_pm");
	}

	public void testIO_Socket_INET_pm()
	{
		doTest("IO_Socket_INET_pm");
	}

	public void testIO_Socket_IP_pm()
	{
		doTest("IO_Socket_IP_pm");
	}

	public void testIO_Socket_SSL_Intercept_pm()
	{
		doTest("IO_Socket_SSL_Intercept_pm");
	}

	public void testIO_Socket_SSL_PublicSuffix_pm()
	{
		doTest("IO_Socket_SSL_PublicSuffix_pm");
	}

	public void testIO_Socket_SSL_Utils_pm()
	{
		doTest("IO_Socket_SSL_Utils_pm");
	}

	public void testIO_Socket_SSL_pm()
	{
		doTest("IO_Socket_SSL_pm");
	}

	public void testIO_Socket_Socks_pm()
	{
		doTest("IO_Socket_Socks_pm");
	}

	public void testIO_Socket_UNIX_pm()
	{
		doTest("IO_Socket_UNIX_pm");
	}

	public void testIO_Socket_pm()
	{
		doTest("IO_Socket_pm");
	}

	public void testIO_String_pm()
	{
		doTest("IO_String_pm");
	}

	public void testIO_Stringy_pm()
	{
		doTest("IO_Stringy_pm");
	}

	public void testIO_TieCombine_Handle_pm()
	{
		doTest("IO_TieCombine_Handle_pm");
	}

	public void testIO_TieCombine_Scalar_pm()
	{
		doTest("IO_TieCombine_Scalar_pm");
	}

	public void testIO_TieCombine_pm()
	{
		doTest("IO_TieCombine_pm");
	}

	public void testIO_Uncompress_Adapter_Bunzip2_pm()
	{
		doTest("IO_Uncompress_Adapter_Bunzip2_pm");
	}

	public void testIO_Uncompress_Adapter_Identity_pm()
	{
		doTest("IO_Uncompress_Adapter_Identity_pm");
	}

	public void testIO_Uncompress_Adapter_Inflate_pm()
	{
		doTest("IO_Uncompress_Adapter_Inflate_pm");
	}

	public void testIO_Uncompress_Adapter_UnLzma_pm()
	{
		doTest("IO_Uncompress_Adapter_UnLzma_pm");
	}

	public void testIO_Uncompress_Adapter_UnXz_pm()
	{
		doTest("IO_Uncompress_Adapter_UnXz_pm");
	}

	public void testIO_Uncompress_AnyInflate_pm()
	{
		doTest("IO_Uncompress_AnyInflate_pm");
	}

	public void testIO_Uncompress_AnyUncompress_pm()
	{
		doTest("IO_Uncompress_AnyUncompress_pm");
	}

	public void testIO_Uncompress_Base_pm()
	{
		doTest("IO_Uncompress_Base_pm");
	}

	public void testIO_Uncompress_Bunzip2_pm()
	{
		doTest("IO_Uncompress_Bunzip2_pm");
	}

	public void testIO_Uncompress_Gunzip_pm()
	{
		doTest("IO_Uncompress_Gunzip_pm");
	}

	public void testIO_Uncompress_Inflate_pm()
	{
		doTest("IO_Uncompress_Inflate_pm");
	}

	public void testIO_Uncompress_RawInflate_pm()
	{
		doTest("IO_Uncompress_RawInflate_pm");
	}

	public void testIO_Uncompress_UnLzma_pm()
	{
		doTest("IO_Uncompress_UnLzma_pm");
	}

	public void testIO_Uncompress_UnXz_pm()
	{
		doTest("IO_Uncompress_UnXz_pm");
	}

	public void testIO_Uncompress_Unzip_pm()
	{
		doTest("IO_Uncompress_Unzip_pm");
	}

	public void testIO_WrapTie_pm()
	{
		doTest("IO_WrapTie_pm");
	}

	public void testIO_Wrap_pm()
	{
		doTest("IO_Wrap_pm");
	}

	public void testIO_Zlib_pm()
	{
		doTest("IO_Zlib_pm");
	}

	public void testIO_pm()
	{
		doTest("IO_pm");
	}

	public void testIPC_Cmd_pm()
	{
		doTest("IPC_Cmd_pm");
	}

	public void testIPC_Open2_pm()
	{
		doTest("IPC_Open2_pm");
	}

	public void testIPC_Open3_pm()
	{
		doTest("IPC_Open3_pm");
	}

	public void testIPC_Run3_ProfArrayBuffer_pm()
	{
		doTest("IPC_Run3_ProfArrayBuffer_pm");
	}

	public void testIPC_Run3_ProfLogReader_pm()
	{
		doTest("IPC_Run3_ProfLogReader_pm");
	}

	public void testIPC_Run3_ProfLogger_pm()
	{
		doTest("IPC_Run3_ProfLogger_pm");
	}

	public void testIPC_Run3_ProfPP_pm()
	{
		doTest("IPC_Run3_ProfPP_pm");
	}

	public void testIPC_Run3_ProfReporter_pm()
	{
		doTest("IPC_Run3_ProfReporter_pm");
	}

	public void testIPC_Run3_pm()
	{
		doTest("IPC_Run3_pm");
	}

	public void testIPC_Run_Debug_pm()
	{
		doTest("IPC_Run_Debug_pm");
	}

	public void testIPC_Run_IO_pm()
	{
		doTest("IPC_Run_IO_pm");
	}

	public void testIPC_Run_Timer_pm()
	{
		doTest("IPC_Run_Timer_pm");
	}

	public void testIPC_Run_Win32Helper_pm()
	{
		doTest("IPC_Run_Win32Helper_pm");
	}

	public void testIPC_Run_Win32IO_pm()
	{
		doTest("IPC_Run_Win32IO_pm");
	}

	public void testIPC_Run_Win32Pump_pm()
	{
		doTest("IPC_Run_Win32Pump_pm");
	}

	public void testIPC_Run_pm()
	{
		doTest("IPC_Run_pm");
	}

	public void testIPC_System_Simple_pm()
	{
		doTest("IPC_System_Simple_pm");
	}

	public void testImager_Color_Float_pm()
	{
		doTest("Imager_Color_Float_pm");
	}

	public void testImager_Color_Table_pm()
	{
		doTest("Imager_Color_Table_pm");
	}

	public void testImager_Color_pm()
	{
		doTest("Imager_Color_pm");
	}

	public void testImager_CountColor_pm()
	{
		doTest("Imager_CountColor_pm");
	}

	public void testImager_Expr_Assem_pm()
	{
		doTest("Imager_Expr_Assem_pm");
	}

	public void testImager_Expr_pm()
	{
		doTest("Imager_Expr_pm");
	}

	public void testImager_ExtUtils_pm()
	{
		doTest("Imager_ExtUtils_pm");
	}

	public void testImager_File_CUR_pm()
	{
		doTest("Imager_File_CUR_pm");
	}

	public void testImager_File_GIF_pm()
	{
		doTest("Imager_File_GIF_pm");
	}

	public void testImager_File_ICO_pm()
	{
		doTest("Imager_File_ICO_pm");
	}

	public void testImager_File_JPEG_pm()
	{
		doTest("Imager_File_JPEG_pm");
	}

	public void testImager_File_PNG_pm()
	{
		doTest("Imager_File_PNG_pm");
	}

	public void testImager_File_SGI_pm()
	{
		doTest("Imager_File_SGI_pm");
	}

	public void testImager_File_TIFF_pm()
	{
		doTest("Imager_File_TIFF_pm");
	}

	public void testImager_Fill_pm()
	{
		doTest("Imager_Fill_pm");
	}

	public void testImager_Filter_DynTest_pm()
	{
		doTest("Imager_Filter_DynTest_pm");
	}

	public void testImager_Filter_Flines_pm()
	{
		doTest("Imager_Filter_Flines_pm");
	}

	public void testImager_Filter_Mandelbrot_pm()
	{
		doTest("Imager_Filter_Mandelbrot_pm");
	}

	public void testImager_Font_BBox_pm()
	{
		doTest("Imager_Font_BBox_pm");
	}

	public void testImager_Font_FT2_pm()
	{
		doTest("Imager_Font_FT2_pm");
	}

	public void testImager_Font_FreeType2_pm()
	{
		doTest("Imager_Font_FreeType2_pm");
	}

	public void testImager_Font_Image_pm()
	{
		doTest("Imager_Font_Image_pm");
	}

	public void testImager_Font_T1_pm()
	{
		doTest("Imager_Font_T1_pm");
	}

	public void testImager_Font_Test_pm()
	{
		doTest("Imager_Font_Test_pm");
	}

	public void testImager_Font_Truetype_pm()
	{
		doTest("Imager_Font_Truetype_pm");
	}

	public void testImager_Font_Type1_pm()
	{
		doTest("Imager_Font_Type1_pm");
	}

	public void testImager_Font_W32_pm()
	{
		doTest("Imager_Font_W32_pm");
	}

	public void testImager_Font_Win32_pm()
	{
		doTest("Imager_Font_Win32_pm");
	}

	public void testImager_Font_Wrap_pm()
	{
		doTest("Imager_Font_Wrap_pm");
	}

	public void testImager_Font_pm()
	{
		doTest("Imager_Font_pm");
	}

	public void testImager_Fountain_pm()
	{
		doTest("Imager_Fountain_pm");
	}

	public void testImager_Matrix2d_pm()
	{
		doTest("Imager_Matrix2d_pm");
	}

	public void testImager_Preprocess_pm()
	{
		doTest("Imager_Preprocess_pm");
	}

	public void testImager_Probe_pm()
	{
		doTest("Imager_Probe_pm");
	}

	public void testImager_Regops_pm()
	{
		doTest("Imager_Regops_pm");
	}

	public void testImager_Test_pm()
	{
		doTest("Imager_Test_pm");
	}

	public void testImager_Transform_pm()
	{
		doTest("Imager_Transform_pm");
	}

	public void testImager_pm()
	{
		doTest("Imager_pm");
	}

	public void testJSON_MaybeXS_pm()
	{
		doTest("JSON_MaybeXS_pm");
	}

	public void testJSON_PP_Boolean_pm()
	{
		doTest("JSON_PP_Boolean_pm");
	}

	public void testJSON_PP_pm()
	{
		doTest("JSON_PP_pm");
	}

	public void testJSON_XS_Boolean_pm()
	{
		doTest("JSON_XS_Boolean_pm");
	}

	public void testJSON_XS_pm()
	{
		doTest("JSON_XS_pm");
	}

	public void testJSON_backportPP_Boolean_pm()
	{
		doTest("JSON_backportPP_Boolean_pm");
	}

	public void testJSON_backportPP_Compat5005_pm()
	{
		doTest("JSON_backportPP_Compat5005_pm");
	}

	public void testJSON_backportPP_Compat5006_pm()
	{
		doTest("JSON_backportPP_Compat5006_pm");
	}

	public void testJSON_backportPP_pm()
	{
		doTest("JSON_backportPP_pm");
	}

	public void testJSON_pm()
	{
		doTest("JSON_pm");
	}

	public void testLWP_Authen_Basic_pm()
	{
		doTest("LWP_Authen_Basic_pm");
	}

	public void testLWP_Authen_Digest_pm()
	{
		doTest("LWP_Authen_Digest_pm");
	}

	public void testLWP_Authen_Ntlm_pm()
	{
		doTest("LWP_Authen_Ntlm_pm");
	}

	public void testLWP_ConnCache_pm()
	{
		doTest("LWP_ConnCache_pm");
	}

	public void testLWP_DebugFile_pm()
	{
		doTest("LWP_DebugFile_pm");
	}

	public void testLWP_Debug_pm()
	{
		doTest("LWP_Debug_pm");
	}

	public void testLWP_MediaTypes_pm()
	{
		doTest("LWP_MediaTypes_pm");
	}

	public void testLWP_MemberMixin_pm()
	{
		doTest("LWP_MemberMixin_pm");
	}

	public void testLWP_Protocol_GHTTP_pm()
	{
		doTest("LWP_Protocol_GHTTP_pm");
	}

	public void testLWP_Protocol_cpan_pm()
	{
		doTest("LWP_Protocol_cpan_pm");
	}

	public void testLWP_Protocol_data_pm()
	{
		doTest("LWP_Protocol_data_pm");
	}

	public void testLWP_Protocol_file_pm()
	{
		doTest("LWP_Protocol_file_pm");
	}

	public void testLWP_Protocol_ftp_pm()
	{
		doTest("LWP_Protocol_ftp_pm");
	}

	public void testLWP_Protocol_gopher_pm()
	{
		doTest("LWP_Protocol_gopher_pm");
	}

	public void testLWP_Protocol_http_pm()
	{
		doTest("LWP_Protocol_http_pm");
	}

	public void testLWP_Protocol_https_pm()
	{
		doTest("LWP_Protocol_https_pm");
	}

	public void testLWP_Protocol_loopback_pm()
	{
		doTest("LWP_Protocol_loopback_pm");
	}

	public void testLWP_Protocol_mailto_pm()
	{
		doTest("LWP_Protocol_mailto_pm");
	}

	public void testLWP_Protocol_nntp_pm()
	{
		doTest("LWP_Protocol_nntp_pm");
	}

	public void testLWP_Protocol_nogo_pm()
	{
		doTest("LWP_Protocol_nogo_pm");
	}

	public void testLWP_Protocol_pm()
	{
		doTest("LWP_Protocol_pm");
	}

	public void testLWP_RobotUA_pm()
	{
		doTest("LWP_RobotUA_pm");
	}

	public void testLWP_Simple_pm()
	{
		doTest("LWP_Simple_pm");
	}

	public void testLWP_UserAgent_pm()
	{
		doTest("LWP_UserAgent_pm");
	}

	public void testLWP_pm()
	{
		doTest("LWP_pm");
	}

	public void testLexical_SealRequireHints_pm()
	{
		doTest("Lexical_SealRequireHints_pm");
	}

	public void testList_AllUtils_pm()
	{
		doTest("List_AllUtils_pm");
	}

	public void testList_MoreUtils_PP_pm()
	{
		doTest("List_MoreUtils_PP_pm");
	}

	public void testList_MoreUtils_XS_pm()
	{
		doTest("List_MoreUtils_XS_pm");
	}

	public void testList_MoreUtils_pm()
	{
		doTest("List_MoreUtils_pm");
	}

	public void testList_Util_XS_pm()
	{
		doTest("List_Util_XS_pm");
	}

	public void testList_Util_pm()
	{
		doTest("List_Util_pm");
	}

	public void testLocale_Codes_Constants_pm()
	{
		doTest("Locale_Codes_Constants_pm");
	}

	public void testLocale_Codes_Country_Codes_pm()
	{
		doTest("Locale_Codes_Country_Codes_pm");
	}

	public void testLocale_Codes_Country_Retired_pm()
	{
		doTest("Locale_Codes_Country_Retired_pm");
	}

	public void testLocale_Codes_Country_pm()
	{
		doTest("Locale_Codes_Country_pm");
	}

	public void testLocale_Codes_Currency_Codes_pm()
	{
		doTest("Locale_Codes_Currency_Codes_pm");
	}

	public void testLocale_Codes_Currency_Retired_pm()
	{
		doTest("Locale_Codes_Currency_Retired_pm");
	}

	public void testLocale_Codes_Currency_pm()
	{
		doTest("Locale_Codes_Currency_pm");
	}

	public void testLocale_Codes_LangExt_Codes_pm()
	{
		doTest("Locale_Codes_LangExt_Codes_pm");
	}

	public void testLocale_Codes_LangExt_Retired_pm()
	{
		doTest("Locale_Codes_LangExt_Retired_pm");
	}

	public void testLocale_Codes_LangExt_pm()
	{
		doTest("Locale_Codes_LangExt_pm");
	}

	public void testLocale_Codes_LangFam_Codes_pm()
	{
		doTest("Locale_Codes_LangFam_Codes_pm");
	}

	public void testLocale_Codes_LangFam_Retired_pm()
	{
		doTest("Locale_Codes_LangFam_Retired_pm");
	}

	public void testLocale_Codes_LangFam_pm()
	{
		doTest("Locale_Codes_LangFam_pm");
	}

	public void testLocale_Codes_LangVar_Codes_pm()
	{
		doTest("Locale_Codes_LangVar_Codes_pm");
	}

	public void testLocale_Codes_LangVar_Retired_pm()
	{
		doTest("Locale_Codes_LangVar_Retired_pm");
	}

	public void testLocale_Codes_LangVar_pm()
	{
		doTest("Locale_Codes_LangVar_pm");
	}

	public void testLocale_Codes_Language_Codes_pm()
	{
		doTest("Locale_Codes_Language_Codes_pm");
	}

	public void testLocale_Codes_Language_Retired_pm()
	{
		doTest("Locale_Codes_Language_Retired_pm");
	}

	public void testLocale_Codes_Language_pm()
	{
		doTest("Locale_Codes_Language_pm");
	}

	public void testLocale_Codes_Script_Codes_pm()
	{
		doTest("Locale_Codes_Script_Codes_pm");
	}

	public void testLocale_Codes_Script_Retired_pm()
	{
		doTest("Locale_Codes_Script_Retired_pm");
	}

	public void testLocale_Codes_Script_pm()
	{
		doTest("Locale_Codes_Script_pm");
	}

	public void testLocale_Codes_pm()
	{
		doTest("Locale_Codes_pm");
	}

	public void testLocale_Country_pm()
	{
		doTest("Locale_Country_pm");
	}

	public void testLocale_Currency_pm()
	{
		doTest("Locale_Currency_pm");
	}

	public void testLocale_Language_pm()
	{
		doTest("Locale_Language_pm");
	}

	public void testLocale_Maketext_GutsLoader_pm()
	{
		doTest("Locale_Maketext_GutsLoader_pm");
	}

	public void testLocale_Maketext_Guts_pm()
	{
		doTest("Locale_Maketext_Guts_pm");
	}

	public void testLocale_Maketext_Simple_pm()
	{
		doTest("Locale_Maketext_Simple_pm");
	}

	public void testLocale_Maketext_pm()
	{
		doTest("Locale_Maketext_pm");
	}

	public void testLocale_Script_pm()
	{
		doTest("Locale_Script_pm");
	}

	public void testLog_Any_Adapter_Base_pm()
	{
		doTest("Log_Any_Adapter_Base_pm");
	}

	public void testLog_Any_Adapter_File_pm()
	{
		doTest("Log_Any_Adapter_File_pm");
	}

	public void testLog_Any_Adapter_Null_pm()
	{
		doTest("Log_Any_Adapter_Null_pm");
	}

	public void testLog_Any_Adapter_Stderr_pm()
	{
		doTest("Log_Any_Adapter_Stderr_pm");
	}

	public void testLog_Any_Adapter_Stdout_pm()
	{
		doTest("Log_Any_Adapter_Stdout_pm");
	}

	public void testLog_Any_Adapter_Test_pm()
	{
		doTest("Log_Any_Adapter_Test_pm");
	}

	public void testLog_Any_Adapter_Util_pm()
	{
		doTest("Log_Any_Adapter_Util_pm");
	}

	public void testLog_Any_Adapter_pm()
	{
		doTest("Log_Any_Adapter_pm");
	}

	public void testLog_Any_Manager_pm()
	{
		doTest("Log_Any_Manager_pm");
	}

	public void testLog_Any_Proxy_Test_pm()
	{
		doTest("Log_Any_Proxy_Test_pm");
	}

	public void testLog_Any_Proxy_pm()
	{
		doTest("Log_Any_Proxy_pm");
	}

	public void testLog_Any_Test_pm()
	{
		doTest("Log_Any_Test_pm");
	}

	public void testLog_Any_pm()
	{
		doTest("Log_Any_pm");
	}

	public void testLog_Dispatch_ApacheLog_pm()
	{
		doTest("Log_Dispatch_ApacheLog_pm");
	}

	public void testLog_Dispatch_Array_pm()
	{
		doTest("Log_Dispatch_Array_pm");
	}

	public void testLog_Dispatch_Base_pm()
	{
		doTest("Log_Dispatch_Base_pm");
	}

	public void testLog_Dispatch_Code_pm()
	{
		doTest("Log_Dispatch_Code_pm");
	}

	public void testLog_Dispatch_Conflicts_pm()
	{
		doTest("Log_Dispatch_Conflicts_pm");
	}

	public void testLog_Dispatch_Email_MIMELite_pm()
	{
		doTest("Log_Dispatch_Email_MIMELite_pm");
	}

	public void testLog_Dispatch_Email_MailSend_pm()
	{
		doTest("Log_Dispatch_Email_MailSend_pm");
	}

	public void testLog_Dispatch_Email_MailSender_pm()
	{
		doTest("Log_Dispatch_Email_MailSender_pm");
	}

	public void testLog_Dispatch_Email_MailSendmail_pm()
	{
		doTest("Log_Dispatch_Email_MailSendmail_pm");
	}

	public void testLog_Dispatch_Email_pm()
	{
		doTest("Log_Dispatch_Email_pm");
	}

	public void testLog_Dispatch_File_Locked_pm()
	{
		doTest("Log_Dispatch_File_Locked_pm");
	}

	public void testLog_Dispatch_File_pm()
	{
		doTest("Log_Dispatch_File_pm");
	}

	public void testLog_Dispatch_Handle_pm()
	{
		doTest("Log_Dispatch_Handle_pm");
	}

	public void testLog_Dispatch_Null_pm()
	{
		doTest("Log_Dispatch_Null_pm");
	}

	public void testLog_Dispatch_Output_pm()
	{
		doTest("Log_Dispatch_Output_pm");
	}

	public void testLog_Dispatch_Screen_pm()
	{
		doTest("Log_Dispatch_Screen_pm");
	}

	public void testLog_Dispatch_Syslog_pm()
	{
		doTest("Log_Dispatch_Syslog_pm");
	}

	public void testLog_Dispatch_pm()
	{
		doTest("Log_Dispatch_pm");
	}

	public void testLog_Dispatchouli_Global_pm()
	{
		doTest("Log_Dispatchouli_Global_pm");
	}

	public void testLog_Dispatchouli_Proxy_pm()
	{
		doTest("Log_Dispatchouli_Proxy_pm");
	}

	public void testLog_Dispatchouli_pm()
	{
		doTest("Log_Dispatchouli_pm");
	}

	public void testLog_Log4perl_Appender_Buffer_pm()
	{
		doTest("Log_Log4perl_Appender_Buffer_pm");
	}

	public void testLog_Log4perl_Appender_DBI_pm()
	{
		doTest("Log_Log4perl_Appender_DBI_pm");
	}

	public void testLog_Log4perl_Appender_File_pm()
	{
		doTest("Log_Log4perl_Appender_File_pm");
	}

	public void testLog_Log4perl_Appender_Limit_pm()
	{
		doTest("Log_Log4perl_Appender_Limit_pm");
	}

	public void testLog_Log4perl_Appender_RRDs_pm()
	{
		doTest("Log_Log4perl_Appender_RRDs_pm");
	}

	public void testLog_Log4perl_Appender_ScreenColoredLevels_pm()
	{
		doTest("Log_Log4perl_Appender_ScreenColoredLevels_pm");
	}

	public void testLog_Log4perl_Appender_Screen_pm()
	{
		doTest("Log_Log4perl_Appender_Screen_pm");
	}

	public void testLog_Log4perl_Appender_Socket_pm()
	{
		doTest("Log_Log4perl_Appender_Socket_pm");
	}

	public void testLog_Log4perl_Appender_String_pm()
	{
		doTest("Log_Log4perl_Appender_String_pm");
	}

	public void testLog_Log4perl_Appender_Synchronized_pm()
	{
		doTest("Log_Log4perl_Appender_Synchronized_pm");
	}

	public void testLog_Log4perl_Appender_TestArrayBuffer_pm()
	{
		doTest("Log_Log4perl_Appender_TestArrayBuffer_pm");
	}

	public void testLog_Log4perl_Appender_TestBuffer_pm()
	{
		doTest("Log_Log4perl_Appender_TestBuffer_pm");
	}

	public void testLog_Log4perl_Appender_TestFileCreeper_pm()
	{
		doTest("Log_Log4perl_Appender_TestFileCreeper_pm");
	}

	public void testLog_Log4perl_Appender_pm()
	{
		doTest("Log_Log4perl_Appender_pm");
	}

	public void testLog_Log4perl_Catalyst_pm()
	{
		doTest("Log_Log4perl_Catalyst_pm");
	}

	public void testLog_Log4perl_Config_BaseConfigurator_pm()
	{
		doTest("Log_Log4perl_Config_BaseConfigurator_pm");
	}

	public void testLog_Log4perl_Config_DOMConfigurator_pm()
	{
		doTest("Log_Log4perl_Config_DOMConfigurator_pm");
	}

	public void testLog_Log4perl_Config_PropertyConfigurator_pm()
	{
		doTest("Log_Log4perl_Config_PropertyConfigurator_pm");
	}

	public void testLog_Log4perl_Config_Watch_pm()
	{
		doTest("Log_Log4perl_Config_Watch_pm");
	}

	public void testLog_Log4perl_Config_pm()
	{
		doTest("Log_Log4perl_Config_pm");
	}

	public void testLog_Log4perl_DateFormat_pm()
	{
		doTest("Log_Log4perl_DateFormat_pm");
	}

	public void testLog_Log4perl_FAQ_pm()
	{
		doTest("Log_Log4perl_FAQ_pm");
	}

	public void testLog_Log4perl_Filter_Boolean_pm()
	{
		doTest("Log_Log4perl_Filter_Boolean_pm");
	}

	public void testLog_Log4perl_Filter_LevelMatch_pm()
	{
		doTest("Log_Log4perl_Filter_LevelMatch_pm");
	}

	public void testLog_Log4perl_Filter_LevelRange_pm()
	{
		doTest("Log_Log4perl_Filter_LevelRange_pm");
	}

	public void testLog_Log4perl_Filter_MDC_pm()
	{
		doTest("Log_Log4perl_Filter_MDC_pm");
	}

	public void testLog_Log4perl_Filter_StringMatch_pm()
	{
		doTest("Log_Log4perl_Filter_StringMatch_pm");
	}

	public void testLog_Log4perl_Filter_pm()
	{
		doTest("Log_Log4perl_Filter_pm");
	}

	public void testLog_Log4perl_InternalDebug_pm()
	{
		doTest("Log_Log4perl_InternalDebug_pm");
	}

	public void testLog_Log4perl_JavaMap_ConsoleAppender_pm()
	{
		doTest("Log_Log4perl_JavaMap_ConsoleAppender_pm");
	}

	public void testLog_Log4perl_JavaMap_FileAppender_pm()
	{
		doTest("Log_Log4perl_JavaMap_FileAppender_pm");
	}

	public void testLog_Log4perl_JavaMap_JDBCAppender_pm()
	{
		doTest("Log_Log4perl_JavaMap_JDBCAppender_pm");
	}

	public void testLog_Log4perl_JavaMap_NTEventLogAppender_pm()
	{
		doTest("Log_Log4perl_JavaMap_NTEventLogAppender_pm");
	}

	public void testLog_Log4perl_JavaMap_RollingFileAppender_pm()
	{
		doTest("Log_Log4perl_JavaMap_RollingFileAppender_pm");
	}

	public void testLog_Log4perl_JavaMap_SyslogAppender_pm()
	{
		doTest("Log_Log4perl_JavaMap_SyslogAppender_pm");
	}

	public void testLog_Log4perl_JavaMap_TestBuffer_pm()
	{
		doTest("Log_Log4perl_JavaMap_TestBuffer_pm");
	}

	public void testLog_Log4perl_JavaMap_pm()
	{
		doTest("Log_Log4perl_JavaMap_pm");
	}

	public void testLog_Log4perl_Layout_NoopLayout_pm()
	{
		doTest("Log_Log4perl_Layout_NoopLayout_pm");
	}

	public void testLog_Log4perl_Layout_PatternLayout_Multiline_pm()
	{
		doTest("Log_Log4perl_Layout_PatternLayout_Multiline_pm");
	}

	public void testLog_Log4perl_Layout_PatternLayout_pm()
	{
		doTest("Log_Log4perl_Layout_PatternLayout_pm");
	}

	public void testLog_Log4perl_Layout_SimpleLayout_pm()
	{
		doTest("Log_Log4perl_Layout_SimpleLayout_pm");
	}

	public void testLog_Log4perl_Layout_pm()
	{
		doTest("Log_Log4perl_Layout_pm");
	}

	public void testLog_Log4perl_Level_pm()
	{
		doTest("Log_Log4perl_Level_pm");
	}

	public void testLog_Log4perl_Logger_pm()
	{
		doTest("Log_Log4perl_Logger_pm");
	}

	public void testLog_Log4perl_MDC_pm()
	{
		doTest("Log_Log4perl_MDC_pm");
	}

	public void testLog_Log4perl_NDC_pm()
	{
		doTest("Log_Log4perl_NDC_pm");
	}

	public void testLog_Log4perl_Resurrector_pm()
	{
		doTest("Log_Log4perl_Resurrector_pm");
	}

	public void testLog_Log4perl_Util_Semaphore_pm()
	{
		doTest("Log_Log4perl_Util_Semaphore_pm");
	}

	public void testLog_Log4perl_Util_TimeTracker_pm()
	{
		doTest("Log_Log4perl_Util_TimeTracker_pm");
	}

	public void testLog_Log4perl_Util_pm()
	{
		doTest("Log_Log4perl_Util_pm");
	}

	public void testLog_Log4perl_pm()
	{
		doTest("Log_Log4perl_pm");
	}

	public void testLog_Message_Config_pm()
	{
		doTest("Log_Message_Config_pm");
	}

	public void testLog_Message_Handlers_pm()
	{
		doTest("Log_Message_Handlers_pm");
	}

	public void testLog_Message_Item_pm()
	{
		doTest("Log_Message_Item_pm");
	}

	public void testLog_Message_Simple_pm()
	{
		doTest("Log_Message_Simple_pm");
	}

	public void testLog_Message_pm()
	{
		doTest("Log_Message_pm");
	}

	public void testLog_Report_Die_pm()
	{
		doTest("Log_Report_Die_pm");
	}

	public void testLog_Report_Dispatcher_Callback_pm()
	{
		doTest("Log_Report_Dispatcher_Callback_pm");
	}

	public void testLog_Report_Dispatcher_File_pm()
	{
		doTest("Log_Report_Dispatcher_File_pm");
	}

	public void testLog_Report_Dispatcher_Log4perl_pm()
	{
		doTest("Log_Report_Dispatcher_Log4perl_pm");
	}

	public void testLog_Report_Dispatcher_LogDispatch_pm()
	{
		doTest("Log_Report_Dispatcher_LogDispatch_pm");
	}

	public void testLog_Report_Dispatcher_Perl_pm()
	{
		doTest("Log_Report_Dispatcher_Perl_pm");
	}

	public void testLog_Report_Dispatcher_Syslog_pm()
	{
		doTest("Log_Report_Dispatcher_Syslog_pm");
	}

	public void testLog_Report_Dispatcher_Try_pm()
	{
		doTest("Log_Report_Dispatcher_Try_pm");
	}

	public void testLog_Report_Dispatcher_pm()
	{
		doTest("Log_Report_Dispatcher_pm");
	}

	public void testLog_Report_Domain_pm()
	{
		doTest("Log_Report_Domain_pm");
	}

	public void testLog_Report_Exception_pm()
	{
		doTest("Log_Report_Exception_pm");
	}

	public void testLog_Report_Message_pm()
	{
		doTest("Log_Report_Message_pm");
	}

	public void testLog_Report_Minimal_Domain_pm()
	{
		doTest("Log_Report_Minimal_Domain_pm");
	}

	public void testLog_Report_Minimal_pm()
	{
		doTest("Log_Report_Minimal_pm");
	}

	public void testLog_Report_Optional_pm()
	{
		doTest("Log_Report_Optional_pm");
	}

	public void testLog_Report_Translator_pm()
	{
		doTest("Log_Report_Translator_pm");
	}

	public void testLog_Report_Util_pm()
	{
		doTest("Log_Report_Util_pm");
	}

	public void testLog_Report_pm()
	{
		doTest("Log_Report_pm");
	}

	public void testMIME_Base64_pm()
	{
		doTest("MIME_Base64_pm");
	}

	public void testMIME_Charset_UTF_pm()
	{
		doTest("MIME_Charset_UTF_pm");
	}

	public void testMIME_Charset__Compat_pm()
	{
		doTest("MIME_Charset__Compat_pm");
	}

	public void testMIME_Charset_pm()
	{
		doTest("MIME_Charset_pm");
	}

	public void testMIME_QuotedPrint_pm()
	{
		doTest("MIME_QuotedPrint_pm");
	}

	public void testMIME_Type_pm()
	{
		doTest("MIME_Type_pm");
	}

	public void testMIME_Types_pm()
	{
		doTest("MIME_Types_pm");
	}

	public void testMRO_Compat_pm()
	{
		doTest("MRO_Compat_pm");
	}

	public void testMail_Address_pm()
	{
		doTest("Mail_Address_pm");
	}

	public void testMail_Cap_pm()
	{
		doTest("Mail_Cap_pm");
	}

	public void testMail_Field_AddrList_pm()
	{
		doTest("Mail_Field_AddrList_pm");
	}

	public void testMail_Field_Date_pm()
	{
		doTest("Mail_Field_Date_pm");
	}

	public void testMail_Field_Generic_pm()
	{
		doTest("Mail_Field_Generic_pm");
	}

	public void testMail_Field_pm()
	{
		doTest("Mail_Field_pm");
	}

	public void testMail_Filter_pm()
	{
		doTest("Mail_Filter_pm");
	}

	public void testMail_Header_pm()
	{
		doTest("Mail_Header_pm");
	}

	public void testMail_Internet_pm()
	{
		doTest("Mail_Internet_pm");
	}

	public void testMail_Mailer_pm()
	{
		doTest("Mail_Mailer_pm");
	}

	public void testMail_Mailer_qmail_pm()
	{
		doTest("Mail_Mailer_qmail_pm");
	}

	public void testMail_Mailer_rfc822_pm()
	{
		doTest("Mail_Mailer_rfc822_pm");
	}

	public void testMail_Mailer_sendmail_pm()
	{
		doTest("Mail_Mailer_sendmail_pm");
	}

	public void testMail_Mailer_smtp_pm()
	{
		doTest("Mail_Mailer_smtp_pm");
	}

	public void testMail_Mailer_smtps_pm()
	{
		doTest("Mail_Mailer_smtps_pm");
	}

	public void testMail_Mailer_testfile_pm()
	{
		doTest("Mail_Mailer_testfile_pm");
	}

	public void testMail_Send_pm()
	{
		doTest("Mail_Send_pm");
	}

	public void testMail_Util_pm()
	{
		doTest("Mail_Util_pm");
	}

	public void testMason_App_pm()
	{
		doTest("Mason_App_pm");
	}

	public void testMason_CodeCache_pm()
	{
		doTest("Mason_CodeCache_pm");
	}

	public void testMason_Compilation_pm()
	{
		doTest("Mason_Compilation_pm");
	}

	public void testMason_Component_ClassMeta_pm()
	{
		doTest("Mason_Component_ClassMeta_pm");
	}

	public void testMason_Component_Import_pm()
	{
		doTest("Mason_Component_Import_pm");
	}

	public void testMason_Component_Moose_pm()
	{
		doTest("Mason_Component_Moose_pm");
	}

	public void testMason_Component_pm()
	{
		doTest("Mason_Component_pm");
	}

	public void testMason_DynamicFilter_pm()
	{
		doTest("Mason_DynamicFilter_pm");
	}

	public void testMason_Exceptions_pm()
	{
		doTest("Mason_Exceptions_pm");
	}

	public void testMason_Filters_Standard_pm()
	{
		doTest("Mason_Filters_Standard_pm");
	}

	public void testMason_Interp_pm()
	{
		doTest("Mason_Interp_pm");
	}

	public void testMason_Moose_Role_pm()
	{
		doTest("Mason_Moose_Role_pm");
	}

	public void testMason_Moose_pm()
	{
		doTest("Mason_Moose_pm");
	}

	public void testMason_PluginBundle_Default_pm()
	{
		doTest("Mason_PluginBundle_Default_pm");
	}

	public void testMason_PluginBundle_pm()
	{
		doTest("Mason_PluginBundle_pm");
	}

	public void testMason_PluginManager_pm()
	{
		doTest("Mason_PluginManager_pm");
	}

	public void testMason_PluginRole_pm()
	{
		doTest("Mason_PluginRole_pm");
	}

	public void testMason_Plugin_Defer_Filters_pm()
	{
		doTest("Mason_Plugin_Defer_Filters_pm");
	}

	public void testMason_Plugin_Defer_Request_pm()
	{
		doTest("Mason_Plugin_Defer_Request_pm");
	}

	public void testMason_Plugin_Defer_pm()
	{
		doTest("Mason_Plugin_Defer_pm");
	}

	public void testMason_Plugin_DollarDot_Compilation_pm()
	{
		doTest("Mason_Plugin_DollarDot_Compilation_pm");
	}

	public void testMason_Plugin_DollarDot_pm()
	{
		doTest("Mason_Plugin_DollarDot_pm");
	}

	public void testMason_Plugin_LvalueAttributes_Interp_pm()
	{
		doTest("Mason_Plugin_LvalueAttributes_Interp_pm");
	}

	public void testMason_Plugin_LvalueAttributes_pm()
	{
		doTest("Mason_Plugin_LvalueAttributes_pm");
	}

	public void testMason_Plugin_TidyObjectFiles_Interp_pm()
	{
		doTest("Mason_Plugin_TidyObjectFiles_Interp_pm");
	}

	public void testMason_Plugin_TidyObjectFiles_pm()
	{
		doTest("Mason_Plugin_TidyObjectFiles_pm");
	}

	public void testMason_Plugin_pm()
	{
		doTest("Mason_Plugin_pm");
	}

	public void testMason_Request_pm()
	{
		doTest("Mason_Request_pm");
	}

	public void testMason_Result_pm()
	{
		doTest("Mason_Result_pm");
	}

	public void testMason_Test_Class_pm()
	{
		doTest("Mason_Test_Class_pm");
	}

	public void testMason_Test_Overrides_Component_StrictMoose_pm()
	{
		doTest("Mason_Test_Overrides_Component_StrictMoose_pm");
	}

	public void testMason_Test_Plugins_Notify_Compilation_pm()
	{
		doTest("Mason_Test_Plugins_Notify_Compilation_pm");
	}

	public void testMason_Test_Plugins_Notify_Component_pm()
	{
		doTest("Mason_Test_Plugins_Notify_Component_pm");
	}

	public void testMason_Test_Plugins_Notify_Interp_pm()
	{
		doTest("Mason_Test_Plugins_Notify_Interp_pm");
	}

	public void testMason_Test_Plugins_Notify_Request_pm()
	{
		doTest("Mason_Test_Plugins_Notify_Request_pm");
	}

	public void testMason_Test_Plugins_Notify_pm()
	{
		doTest("Mason_Test_Plugins_Notify_pm");
	}

	public void testMason_Test_RootClass_Compilation_pm()
	{
		doTest("Mason_Test_RootClass_Compilation_pm");
	}

	public void testMason_Test_RootClass_Component_pm()
	{
		doTest("Mason_Test_RootClass_Component_pm");
	}

	public void testMason_Test_RootClass_Interp_pm()
	{
		doTest("Mason_Test_RootClass_Interp_pm");
	}

	public void testMason_Test_RootClass_Request_pm()
	{
		doTest("Mason_Test_RootClass_Request_pm");
	}

	public void testMason_Test_RootClass_pm()
	{
		doTest("Mason_Test_RootClass_pm");
	}

	public void testMason_TieHandle_pm()
	{
		doTest("Mason_TieHandle_pm");
	}

	public void testMason_Types_pm()
	{
		doTest("Mason_Types_pm");
	}

	public void testMason_Util_pm()
	{
		doTest("Mason_Util_pm");
	}

	public void testMason_pm()
	{
		doTest("Mason_pm");
	}

	public void testMason_t_Autobase_pm()
	{
		doTest("Mason_t_Autobase_pm");
	}

	public void testMason_t_Cache_pm()
	{
		doTest("Mason_t_Cache_pm");
	}

	public void testMason_t_CompCalls_pm()
	{
		doTest("Mason_t_CompCalls_pm");
	}

	public void testMason_t_Compilation_pm()
	{
		doTest("Mason_t_Compilation_pm");
	}

	public void testMason_t_ComponentMeta_pm()
	{
		doTest("Mason_t_ComponentMeta_pm");
	}

	public void testMason_t_Defer_pm()
	{
		doTest("Mason_t_Defer_pm");
	}

	public void testMason_t_DollarDot_pm()
	{
		doTest("Mason_t_DollarDot_pm");
	}

	public void testMason_t_Errors_pm()
	{
		doTest("Mason_t_Errors_pm");
	}

	public void testMason_t_Filters_pm()
	{
		doTest("Mason_t_Filters_pm");
	}

	public void testMason_t_Globals_pm()
	{
		doTest("Mason_t_Globals_pm");
	}

	public void testMason_t_HTMLFilters_pm()
	{
		doTest("Mason_t_HTMLFilters_pm");
	}

	public void testMason_t_Interp_pm()
	{
		doTest("Mason_t_Interp_pm");
	}

	public void testMason_t_LvalueAttributes_pm()
	{
		doTest("Mason_t_LvalueAttributes_pm");
	}

	public void testMason_t_Plugins_pm()
	{
		doTest("Mason_t_Plugins_pm");
	}

	public void testMason_t_Reload_pm()
	{
		doTest("Mason_t_Reload_pm");
	}

	public void testMason_t_Request_pm()
	{
		doTest("Mason_t_Request_pm");
	}

	public void testMason_t_ResolveURI_pm()
	{
		doTest("Mason_t_ResolveURI_pm");
	}

	public void testMason_t_Sanity_pm()
	{
		doTest("Mason_t_Sanity_pm");
	}

	public void testMason_t_Sections_pm()
	{
		doTest("Mason_t_Sections_pm");
	}

	public void testMason_t_Skel_pm()
	{
		doTest("Mason_t_Skel_pm");
	}

	public void testMason_t_StaticSource_pm()
	{
		doTest("Mason_t_StaticSource_pm");
	}

	public void testMason_t_Syntax_pm()
	{
		doTest("Mason_t_Syntax_pm");
	}

	public void testMason_t_Util_pm()
	{
		doTest("Mason_t_Util_pm");
	}

	public void testMath_Base_Convert_Bases_pm()
	{
		doTest("Math_Base_Convert_Bases_pm");
	}

	public void testMath_Base_Convert_Bitmaps_pm()
	{
		doTest("Math_Base_Convert_Bitmaps_pm");
	}

	public void testMath_Base_Convert_CalcPP_pm()
	{
		doTest("Math_Base_Convert_CalcPP_pm");
	}

	public void testMath_Base_Convert_Shortcuts_pm()
	{
		doTest("Math_Base_Convert_Shortcuts_pm");
	}

	public void testMath_Base_Convert_pm()
	{
		doTest("Math_Base_Convert_pm");
	}

	public void testMath_BigFloat_Trace_pm()
	{
		doTest("Math_BigFloat_Trace_pm");
	}

	public void testMath_BigFloat_pm()
	{
		doTest("Math_BigFloat_pm");
	}

	public void testMath_BigInt_CalcEmu_pm()
	{
		doTest("Math_BigInt_CalcEmu_pm");
	}

	public void testMath_BigInt_Calc_pm()
	{
		doTest("Math_BigInt_Calc_pm");
	}

	public void testMath_BigInt_FastCalc_pm()
	{
		doTest("Math_BigInt_FastCalc_pm");
	}

	public void testMath_BigInt_GMP_pm()
	{
		doTest("Math_BigInt_GMP_pm");
	}

	public void testMath_BigInt_Trace_pm()
	{
		doTest("Math_BigInt_Trace_pm");
	}

	public void testMath_BigInt_pm()
	{
		doTest("Math_BigInt_pm");
	}

	public void testMath_BigRat_pm()
	{
		doTest("Math_BigRat_pm");
	}

	public void testMath_Complex_pm()
	{
		doTest("Math_Complex_pm");
	}

	public void testMath_GMP_pm()
	{
		doTest("Math_GMP_pm");
	}

	public void testMath_Int64_die_on_overflow_pm()
	{
		doTest("Math_Int64_die_on_overflow_pm");
	}

	public void testMath_Int64_native_if_available_pm()
	{
		doTest("Math_Int64_native_if_available_pm");
	}

	public void testMath_Int64_pm()
	{
		doTest("Math_Int64_pm");
	}

	public void testMath_MPC_pm()
	{
		doTest("Math_MPC_pm");
	}

	public void testMath_MPFR_Prec_pm()
	{
		doTest("Math_MPFR_Prec_pm");
	}

	public void testMath_MPFR_Random_pm()
	{
		doTest("Math_MPFR_Random_pm");
	}

	public void testMath_MPFR_V_pm()
	{
		doTest("Math_MPFR_V_pm");
	}

	public void testMath_MPFR_pm()
	{
		doTest("Math_MPFR_pm");
	}

	public void testMath_Prime_Util_ECAffinePoint_pm()
	{
		doTest("Math_Prime_Util_ECAffinePoint_pm");
	}

	public void testMath_Prime_Util_ECProjectivePoint_pm()
	{
		doTest("Math_Prime_Util_ECProjectivePoint_pm");
	}

	public void testMath_Prime_Util_GMP_pm()
	{
		doTest("Math_Prime_Util_GMP_pm");
	}

	public void testMath_Prime_Util_MemFree_pm()
	{
		doTest("Math_Prime_Util_MemFree_pm");
	}

	public void testMath_Prime_Util_PPFE_pm()
	{
		doTest("Math_Prime_Util_PPFE_pm");
	}

	public void testMath_Prime_Util_PP_pm()
	{
		doTest("Math_Prime_Util_PP_pm");
	}

	public void testMath_Prime_Util_PrimalityProving_pm()
	{
		doTest("Math_Prime_Util_PrimalityProving_pm");
	}

	public void testMath_Prime_Util_PrimeArray_pm()
	{
		doTest("Math_Prime_Util_PrimeArray_pm");
	}

	public void testMath_Prime_Util_PrimeIterator_pm()
	{
		doTest("Math_Prime_Util_PrimeIterator_pm");
	}

	public void testMath_Prime_Util_RandomPrimes_pm()
	{
		doTest("Math_Prime_Util_RandomPrimes_pm");
	}

	public void testMath_Prime_Util_ZetaBigFloat_pm()
	{
		doTest("Math_Prime_Util_ZetaBigFloat_pm");
	}

	public void testMath_Prime_Util_pm()
	{
		doTest("Math_Prime_Util_pm");
	}

	public void testMath_Random_ISAAC_PP_pm()
	{
		doTest("Math_Random_ISAAC_PP_pm");
	}

	public void testMath_Random_ISAAC_pm()
	{
		doTest("Math_Random_ISAAC_pm");
	}

	public void testMath_Round_pm()
	{
		doTest("Math_Round_pm");
	}

	public void testMath_Trig_pm()
	{
		doTest("Math_Trig_pm");
	}

	public void testMath_UInt64_pm()
	{
		doTest("Math_UInt64_pm");
	}

	public void testMemoize_AnyDBM_File_pm()
	{
		doTest("Memoize_AnyDBM_File_pm");
	}

	public void testMemoize_ExpireFile_pm()
	{
		doTest("Memoize_ExpireFile_pm");
	}

	public void testMemoize_ExpireTest_pm()
	{
		doTest("Memoize_ExpireTest_pm");
	}

	public void testMemoize_Expire_pm()
	{
		doTest("Memoize_Expire_pm");
	}

	public void testMemoize_NDBM_File_pm()
	{
		doTest("Memoize_NDBM_File_pm");
	}

	public void testMemoize_SDBM_File_pm()
	{
		doTest("Memoize_SDBM_File_pm");
	}

	public void testMemoize_Storable_pm()
	{
		doTest("Memoize_Storable_pm");
	}

	public void testMemoize_pm()
	{
		doTest("Memoize_pm");
	}

	public void testMethod_Generate_Accessor_pm()
	{
		doTest("Method_Generate_Accessor_pm");
	}

	public void testMethod_Generate_BuildAll_pm()
	{
		doTest("Method_Generate_BuildAll_pm");
	}

	public void testMethod_Generate_Constructor_pm()
	{
		doTest("Method_Generate_Constructor_pm");
	}

	public void testMethod_Generate_DemolishAll_pm()
	{
		doTest("Method_Generate_DemolishAll_pm");
	}

	public void testMethod_Inliner_pm()
	{
		doTest("Method_Inliner_pm");
	}

	public void testMethod_Signatures_Simple_pm()
	{
		doTest("Method_Signatures_Simple_pm");
	}

	public void testMixin_Linewise_Readers_pm()
	{
		doTest("Mixin_Linewise_Readers_pm");
	}

	public void testMixin_Linewise_Writers_pm()
	{
		doTest("Mixin_Linewise_Writers_pm");
	}

	public void testMixin_Linewise_pm()
	{
		doTest("Mixin_Linewise_pm");
	}

	public void testModern_Perl_pm()
	{
		doTest("Modern_Perl_pm");
	}

	public void testModule_Build_Base_pm()
	{
		doTest("Module_Build_Base_pm");
	}

	public void testModule_Build_Compat_pm()
	{
		doTest("Module_Build_Compat_pm");
	}

	public void testModule_Build_ConfigData_pm()
	{
		doTest("Module_Build_ConfigData_pm");
	}

	public void testModule_Build_Config_pm()
	{
		doTest("Module_Build_Config_pm");
	}

	public void testModule_Build_Cookbook_pm()
	{
		doTest("Module_Build_Cookbook_pm");
	}

	public void testModule_Build_Dumper_pm()
	{
		doTest("Module_Build_Dumper_pm");
	}

	public void testModule_Build_Notes_pm()
	{
		doTest("Module_Build_Notes_pm");
	}

	public void testModule_Build_PPMMaker_pm()
	{
		doTest("Module_Build_PPMMaker_pm");
	}

	public void testModule_Build_Platform_Default_pm()
	{
		doTest("Module_Build_Platform_Default_pm");
	}

	public void testModule_Build_Platform_MacOS_pm()
	{
		doTest("Module_Build_Platform_MacOS_pm");
	}

	public void testModule_Build_Platform_Unix_pm()
	{
		doTest("Module_Build_Platform_Unix_pm");
	}

	public void testModule_Build_Platform_VMS_pm()
	{
		doTest("Module_Build_Platform_VMS_pm");
	}

	public void testModule_Build_Platform_VOS_pm()
	{
		doTest("Module_Build_Platform_VOS_pm");
	}

	public void testModule_Build_Platform_Windows_pm()
	{
		doTest("Module_Build_Platform_Windows_pm");
	}

	public void testModule_Build_Platform_aix_pm()
	{
		doTest("Module_Build_Platform_aix_pm");
	}

	public void testModule_Build_Platform_cygwin_pm()
	{
		doTest("Module_Build_Platform_cygwin_pm");
	}

	public void testModule_Build_Platform_darwin_pm()
	{
		doTest("Module_Build_Platform_darwin_pm");
	}

	public void testModule_Build_Platform_os2_pm()
	{
		doTest("Module_Build_Platform_os2_pm");
	}

	public void testModule_Build_PodParser_pm()
	{
		doTest("Module_Build_PodParser_pm");
	}

	public void testModule_Build_Tiny_pm()
	{
		doTest("Module_Build_Tiny_pm");
	}

	public void testModule_Build_pm()
	{
		doTest("Module_Build_pm");
	}

	public void testModule_CPANTS_Analyse_pm()
	{
		doTest("Module_CPANTS_Analyse_pm");
	}

	public void testModule_CPANTS_Kwalitee_BrokenInstaller_pm()
	{
		doTest("Module_CPANTS_Kwalitee_BrokenInstaller_pm");
	}

	public void testModule_CPANTS_Kwalitee_CpantsErrors_pm()
	{
		doTest("Module_CPANTS_Kwalitee_CpantsErrors_pm");
	}

	public void testModule_CPANTS_Kwalitee_Distname_pm()
	{
		doTest("Module_CPANTS_Kwalitee_Distname_pm");
	}

	public void testModule_CPANTS_Kwalitee_Distros_pm()
	{
		doTest("Module_CPANTS_Kwalitee_Distros_pm");
	}

	public void testModule_CPANTS_Kwalitee_Files_pm()
	{
		doTest("Module_CPANTS_Kwalitee_Files_pm");
	}

	public void testModule_CPANTS_Kwalitee_FindModules_pm()
	{
		doTest("Module_CPANTS_Kwalitee_FindModules_pm");
	}

	public void testModule_CPANTS_Kwalitee_License_pm()
	{
		doTest("Module_CPANTS_Kwalitee_License_pm");
	}

	public void testModule_CPANTS_Kwalitee_Manifest_pm()
	{
		doTest("Module_CPANTS_Kwalitee_Manifest_pm");
	}

	public void testModule_CPANTS_Kwalitee_MetaYML_pm()
	{
		doTest("Module_CPANTS_Kwalitee_MetaYML_pm");
	}

	public void testModule_CPANTS_Kwalitee_NeedsCompiler_pm()
	{
		doTest("Module_CPANTS_Kwalitee_NeedsCompiler_pm");
	}

	public void testModule_CPANTS_Kwalitee_Pod_pm()
	{
		doTest("Module_CPANTS_Kwalitee_Pod_pm");
	}

	public void testModule_CPANTS_Kwalitee_Prereq_pm()
	{
		doTest("Module_CPANTS_Kwalitee_Prereq_pm");
	}

	public void testModule_CPANTS_Kwalitee_Repackageable_pm()
	{
		doTest("Module_CPANTS_Kwalitee_Repackageable_pm");
	}

	public void testModule_CPANTS_Kwalitee_Signature_pm()
	{
		doTest("Module_CPANTS_Kwalitee_Signature_pm");
	}

	public void testModule_CPANTS_Kwalitee_Uses_pm()
	{
		doTest("Module_CPANTS_Kwalitee_Uses_pm");
	}

	public void testModule_CPANTS_Kwalitee_Version_pm()
	{
		doTest("Module_CPANTS_Kwalitee_Version_pm");
	}

	public void testModule_CPANTS_Kwalitee_pm()
	{
		doTest("Module_CPANTS_Kwalitee_pm");
	}

	public void testModule_CPANfile_Environment_pm()
	{
		doTest("Module_CPANfile_Environment_pm");
	}

	public void testModule_CPANfile_Prereq_pm()
	{
		doTest("Module_CPANfile_Prereq_pm");
	}

	public void testModule_CPANfile_Prereqs_pm()
	{
		doTest("Module_CPANfile_Prereqs_pm");
	}

	public void testModule_CPANfile_Requirement_pm()
	{
		doTest("Module_CPANfile_Requirement_pm");
	}

	public void testModule_CPANfile_pm()
	{
		doTest("Module_CPANfile_pm");
	}

	public void testModule_CoreList_TieHashDelta_pm()
	{
		doTest("Module_CoreList_TieHashDelta_pm");
	}

	public void testModule_CoreList_Utils_pm()
	{
		doTest("Module_CoreList_Utils_pm");
	}

	public void testModule_CoreList_pm()
	{
		doTest("Module_CoreList_pm");
	}

	public void testModule_ExtractUse_Grammar_pm()
	{
		doTest("Module_ExtractUse_Grammar_pm");
	}

	public void testModule_ExtractUse_pm()
	{
		doTest("Module_ExtractUse_pm");
	}

	public void testModule_Find_pm()
	{
		doTest("Module_Find_pm");
	}

	public void testModule_Implementation_pm()
	{
		doTest("Module_Implementation_pm");
	}

	public void testModule_Load_Conditional_pm()
	{
		doTest("Module_Load_Conditional_pm");
	}

	public void testModule_Load_pm()
	{
		doTest("Module_Load_pm");
	}

	public void testModule_Loaded_pm()
	{
		doTest("Module_Loaded_pm");
	}

	public void testModule_Metadata_pm()
	{
		doTest("Module_Metadata_pm");
	}

	public void testModule_Path_pm()
	{
		doTest("Module_Path_pm");
	}

	public void testModule_Pluggable_Object_pm()
	{
		doTest("Module_Pluggable_Object_pm");
	}

	public void testModule_Pluggable_pm()
	{
		doTest("Module_Pluggable_pm");
	}

	public void testModule_Runtime_Conflicts_pm()
	{
		doTest("Module_Runtime_Conflicts_pm");
	}

	public void testModule_Runtime_pm()
	{
		doTest("Module_Runtime_pm");
	}

	public void testMojoX_Log_Report_pm()
	{
		doTest("MojoX_Log_Report_pm");
	}

	public void testMojoX_MIME_Types_pm()
	{
		doTest("MojoX_MIME_Types_pm");
	}

	public void testMojo_Asset_File_pm()
	{
		doTest("Mojo_Asset_File_pm");
	}

	public void testMojo_Asset_Memory_pm()
	{
		doTest("Mojo_Asset_Memory_pm");
	}

	public void testMojo_Asset_pm()
	{
		doTest("Mojo_Asset_pm");
	}

	public void testMojo_Base_pm()
	{
		doTest("Mojo_Base_pm");
	}

	public void testMojo_ByteStream_pm()
	{
		doTest("Mojo_ByteStream_pm");
	}

	public void testMojo_Cache_pm()
	{
		doTest("Mojo_Cache_pm");
	}

	public void testMojo_Collection_pm()
	{
		doTest("Mojo_Collection_pm");
	}

	public void testMojo_Content_MultiPart_pm()
	{
		doTest("Mojo_Content_MultiPart_pm");
	}

	public void testMojo_Content_Single_pm()
	{
		doTest("Mojo_Content_Single_pm");
	}

	public void testMojo_Content_pm()
	{
		doTest("Mojo_Content_pm");
	}

	public void testMojo_Cookie_Request_pm()
	{
		doTest("Mojo_Cookie_Request_pm");
	}

	public void testMojo_Cookie_Response_pm()
	{
		doTest("Mojo_Cookie_Response_pm");
	}

	public void testMojo_Cookie_pm()
	{
		doTest("Mojo_Cookie_pm");
	}

	public void testMojo_DOM_CSS_pm()
	{
		doTest("Mojo_DOM_CSS_pm");
	}

	public void testMojo_DOM_HTML_pm()
	{
		doTest("Mojo_DOM_HTML_pm");
	}

	public void testMojo_DOM_pm()
	{
		doTest("Mojo_DOM_pm");
	}

	public void testMojo_Date_pm()
	{
		doTest("Mojo_Date_pm");
	}

	public void testMojo_EventEmitter_pm()
	{
		doTest("Mojo_EventEmitter_pm");
	}

	public void testMojo_Exception_pm()
	{
		doTest("Mojo_Exception_pm");
	}

	public void testMojo_Headers_pm()
	{
		doTest("Mojo_Headers_pm");
	}

	public void testMojo_HelloWorld_pm()
	{
		doTest("Mojo_HelloWorld_pm");
	}

	public void testMojo_Home_pm()
	{
		doTest("Mojo_Home_pm");
	}

	public void testMojo_IOLoop_Client_pm()
	{
		doTest("Mojo_IOLoop_Client_pm");
	}

	public void testMojo_IOLoop_Delay_pm()
	{
		doTest("Mojo_IOLoop_Delay_pm");
	}

	public void testMojo_IOLoop_Server_pm()
	{
		doTest("Mojo_IOLoop_Server_pm");
	}

	public void testMojo_IOLoop_Stream_pm()
	{
		doTest("Mojo_IOLoop_Stream_pm");
	}

	public void testMojo_IOLoop_pm()
	{
		doTest("Mojo_IOLoop_pm");
	}

	public void testMojo_JSON_Pointer_pm()
	{
		doTest("Mojo_JSON_Pointer_pm");
	}

	public void testMojo_JSON_pm()
	{
		doTest("Mojo_JSON_pm");
	}

	public void testMojo_Loader_pm()
	{
		doTest("Mojo_Loader_pm");
	}

	public void testMojo_Log_pm()
	{
		doTest("Mojo_Log_pm");
	}

	public void testMojo_Message_Request_pm()
	{
		doTest("Mojo_Message_Request_pm");
	}

	public void testMojo_Message_Response_pm()
	{
		doTest("Mojo_Message_Response_pm");
	}

	public void testMojo_Message_pm()
	{
		doTest("Mojo_Message_pm");
	}

	public void testMojo_Parameters_pm()
	{
		doTest("Mojo_Parameters_pm");
	}

	public void testMojo_Path_pm()
	{
		doTest("Mojo_Path_pm");
	}

	public void testMojo_Reactor_EV_pm()
	{
		doTest("Mojo_Reactor_EV_pm");
	}

	public void testMojo_Reactor_Poll_pm()
	{
		doTest("Mojo_Reactor_Poll_pm");
	}

	public void testMojo_Reactor_pm()
	{
		doTest("Mojo_Reactor_pm");
	}

	public void testMojo_Server_CGI_pm()
	{
		doTest("Mojo_Server_CGI_pm");
	}

	public void testMojo_Server_Daemon_pm()
	{
		doTest("Mojo_Server_Daemon_pm");
	}

	public void testMojo_Server_Hypnotoad_pm()
	{
		doTest("Mojo_Server_Hypnotoad_pm");
	}

	public void testMojo_Server_Morbo_pm()
	{
		doTest("Mojo_Server_Morbo_pm");
	}

	public void testMojo_Server_PSGI_pm()
	{
		doTest("Mojo_Server_PSGI_pm");
	}

	public void testMojo_Server_Prefork_pm()
	{
		doTest("Mojo_Server_Prefork_pm");
	}

	public void testMojo_Server_pm()
	{
		doTest("Mojo_Server_pm");
	}

	public void testMojo_Template_pm()
	{
		doTest("Mojo_Template_pm");
	}

	public void testMojo_Transaction_HTTP_pm()
	{
		doTest("Mojo_Transaction_HTTP_pm");
	}

	public void testMojo_Transaction_WebSocket_pm()
	{
		doTest("Mojo_Transaction_WebSocket_pm");
	}

	public void testMojo_Transaction_pm()
	{
		doTest("Mojo_Transaction_pm");
	}

	public void testMojo_URL_pm()
	{
		doTest("Mojo_URL_pm");
	}

	public void testMojo_Upload_pm()
	{
		doTest("Mojo_Upload_pm");
	}

	public void testMojo_UserAgent_CookieJar_pm()
	{
		doTest("Mojo_UserAgent_CookieJar_pm");
	}

	public void testMojo_UserAgent_Proxy_pm()
	{
		doTest("Mojo_UserAgent_Proxy_pm");
	}

	public void testMojo_UserAgent_Server_pm()
	{
		doTest("Mojo_UserAgent_Server_pm");
	}

	public void testMojo_UserAgent_Transactor_pm()
	{
		doTest("Mojo_UserAgent_Transactor_pm");
	}

	public void testMojo_UserAgent_pm()
	{
		doTest("Mojo_UserAgent_pm");
	}

	public void testMojo_Util_pm()
	{
		doTest("Mojo_Util_pm");
	}

	public void testMojo_pm()
	{
		doTest("Mojo_pm");
	}

	public void testMojolicious_Command_cgi_pm()
	{
		doTest("Mojolicious_Command_cgi_pm");
	}

	public void testMojolicious_Command_cpanify_pm()
	{
		doTest("Mojolicious_Command_cpanify_pm");
	}

	public void testMojolicious_Command_daemon_pm()
	{
		doTest("Mojolicious_Command_daemon_pm");
	}

	public void testMojolicious_Command_eval_pm()
	{
		doTest("Mojolicious_Command_eval_pm");
	}

	public void testMojolicious_Command_generate_app_pm()
	{
		doTest("Mojolicious_Command_generate_app_pm");
	}

	public void testMojolicious_Command_generate_lite_app_pm()
	{
		doTest("Mojolicious_Command_generate_lite_app_pm");
	}

	public void testMojolicious_Command_generate_makefile_pm()
	{
		doTest("Mojolicious_Command_generate_makefile_pm");
	}

	public void testMojolicious_Command_generate_plugin_pm()
	{
		doTest("Mojolicious_Command_generate_plugin_pm");
	}

	public void testMojolicious_Command_generate_pm()
	{
		doTest("Mojolicious_Command_generate_pm");
	}

	public void testMojolicious_Command_get_pm()
	{
		doTest("Mojolicious_Command_get_pm");
	}

	public void testMojolicious_Command_inflate_pm()
	{
		doTest("Mojolicious_Command_inflate_pm");
	}

	public void testMojolicious_Command_pm()
	{
		doTest("Mojolicious_Command_pm");
	}

	public void testMojolicious_Command_prefork_pm()
	{
		doTest("Mojolicious_Command_prefork_pm");
	}

	public void testMojolicious_Command_psgi_pm()
	{
		doTest("Mojolicious_Command_psgi_pm");
	}

	public void testMojolicious_Command_routes_pm()
	{
		doTest("Mojolicious_Command_routes_pm");
	}

	public void testMojolicious_Command_test_pm()
	{
		doTest("Mojolicious_Command_test_pm");
	}

	public void testMojolicious_Command_version_pm()
	{
		doTest("Mojolicious_Command_version_pm");
	}

	public void testMojolicious_Commands_pm()
	{
		doTest("Mojolicious_Commands_pm");
	}

	public void testMojolicious_Controller_pm()
	{
		doTest("Mojolicious_Controller_pm");
	}

	public void testMojolicious_Lite_pm()
	{
		doTest("Mojolicious_Lite_pm");
	}

	public void testMojolicious_Plugin_Charset_pm()
	{
		doTest("Mojolicious_Plugin_Charset_pm");
	}

	public void testMojolicious_Plugin_Config_pm()
	{
		doTest("Mojolicious_Plugin_Config_pm");
	}

	public void testMojolicious_Plugin_DefaultHelpers_pm()
	{
		doTest("Mojolicious_Plugin_DefaultHelpers_pm");
	}

	public void testMojolicious_Plugin_EPLRenderer_pm()
	{
		doTest("Mojolicious_Plugin_EPLRenderer_pm");
	}

	public void testMojolicious_Plugin_EPRenderer_pm()
	{
		doTest("Mojolicious_Plugin_EPRenderer_pm");
	}

	public void testMojolicious_Plugin_HeaderCondition_pm()
	{
		doTest("Mojolicious_Plugin_HeaderCondition_pm");
	}

	public void testMojolicious_Plugin_JSONConfig_pm()
	{
		doTest("Mojolicious_Plugin_JSONConfig_pm");
	}

	public void testMojolicious_Plugin_Mount_pm()
	{
		doTest("Mojolicious_Plugin_Mount_pm");
	}

	public void testMojolicious_Plugin_PODRenderer_pm()
	{
		doTest("Mojolicious_Plugin_PODRenderer_pm");
	}

	public void testMojolicious_Plugin_TagHelpers_pm()
	{
		doTest("Mojolicious_Plugin_TagHelpers_pm");
	}

	public void testMojolicious_Plugin_pm()
	{
		doTest("Mojolicious_Plugin_pm");
	}

	public void testMojolicious_Plugins_pm()
	{
		doTest("Mojolicious_Plugins_pm");
	}

	public void testMojolicious_Renderer_pm()
	{
		doTest("Mojolicious_Renderer_pm");
	}

	public void testMojolicious_Routes_Match_pm()
	{
		doTest("Mojolicious_Routes_Match_pm");
	}

	public void testMojolicious_Routes_Pattern_pm()
	{
		doTest("Mojolicious_Routes_Pattern_pm");
	}

	public void testMojolicious_Routes_Route_pm()
	{
		doTest("Mojolicious_Routes_Route_pm");
	}

	public void testMojolicious_Routes_pm()
	{
		doTest("Mojolicious_Routes_pm");
	}

	public void testMojolicious_Sessions_pm()
	{
		doTest("Mojolicious_Sessions_pm");
	}

	public void testMojolicious_Static_pm()
	{
		doTest("Mojolicious_Static_pm");
	}

	public void testMojolicious_Types_pm()
	{
		doTest("Mojolicious_Types_pm");
	}

	public void testMojolicious_Validator_Validation_pm()
	{
		doTest("Mojolicious_Validator_Validation_pm");
	}

	public void testMojolicious_Validator_pm()
	{
		doTest("Mojolicious_Validator_pm");
	}

	public void testMojolicious_pm()
	{
		doTest("Mojolicious_pm");
	}

	public void testMooX_Types_MooseLike_Base_pm()
	{
		doTest("MooX_Types_MooseLike_Base_pm");
	}

	public void testMooX_Types_MooseLike_pm()
	{
		doTest("MooX_Types_MooseLike_pm");
	}

	public void testMoo_HandleMoose_FakeMetaClass_pm()
	{
		doTest("Moo_HandleMoose_FakeMetaClass_pm");
	}

	public void testMoo_HandleMoose__TypeMap_pm()
	{
		doTest("Moo_HandleMoose__TypeMap_pm");
	}

	public void testMoo_HandleMoose_pm()
	{
		doTest("Moo_HandleMoose_pm");
	}

	public void testMoo_Object_pm()
	{
		doTest("Moo_Object_pm");
	}

	public void testMoo_Role_pm()
	{
		doTest("Moo_Role_pm");
	}

	public void testMoo__Utils_pm()
	{
		doTest("Moo__Utils_pm");
	}

	public void testMoo__mro_pm()
	{
		doTest("Moo__mro_pm");
	}

	public void testMoo__strictures_pm()
	{
		doTest("Moo__strictures_pm");
	}

	public void testMoo_pm()
	{
		doTest("Moo_pm");
	}

	public void testMoo_sification_pm()
	{
		doTest("Moo_sification_pm");
	}

	public void testMooseX_ClassAttribute_Meta_Role_Attribute_pm()
	{
		doTest("MooseX_ClassAttribute_Meta_Role_Attribute_pm");
	}

	public void testMooseX_ClassAttribute_Trait_Application_ToClass_pm()
	{
		doTest("MooseX_ClassAttribute_Trait_Application_ToClass_pm");
	}

	public void testMooseX_ClassAttribute_Trait_Application_ToRole_pm()
	{
		doTest("MooseX_ClassAttribute_Trait_Application_ToRole_pm");
	}

	public void testMooseX_ClassAttribute_Trait_Application_pm()
	{
		doTest("MooseX_ClassAttribute_Trait_Application_pm");
	}

	public void testMooseX_ClassAttribute_Trait_Attribute_pm()
	{
		doTest("MooseX_ClassAttribute_Trait_Attribute_pm");
	}

	public void testMooseX_ClassAttribute_Trait_Class_pm()
	{
		doTest("MooseX_ClassAttribute_Trait_Class_pm");
	}

	public void testMooseX_ClassAttribute_Trait_Mixin_HasClassAttributes_pm()
	{
		doTest("MooseX_ClassAttribute_Trait_Mixin_HasClassAttributes_pm");
	}

	public void testMooseX_ClassAttribute_Trait_Role_Composite_pm()
	{
		doTest("MooseX_ClassAttribute_Trait_Role_Composite_pm");
	}

	public void testMooseX_ClassAttribute_Trait_Role_pm()
	{
		doTest("MooseX_ClassAttribute_Trait_Role_pm");
	}

	public void testMooseX_ClassAttribute_pm()
	{
		doTest("MooseX_ClassAttribute_pm");
	}

	public void testMooseX_ConfigFromFile_pm()
	{
		doTest("MooseX_ConfigFromFile_pm");
	}

	public void testMooseX_Declare_Context_Namespaced_pm()
	{
		doTest("MooseX_Declare_Context_Namespaced_pm");
	}

	public void testMooseX_Declare_Context_Parameterized_pm()
	{
		doTest("MooseX_Declare_Context_Parameterized_pm");
	}

	public void testMooseX_Declare_Context_WithOptions_pm()
	{
		doTest("MooseX_Declare_Context_WithOptions_pm");
	}

	public void testMooseX_Declare_Context_pm()
	{
		doTest("MooseX_Declare_Context_pm");
	}

	public void testMooseX_Declare_StackItem_pm()
	{
		doTest("MooseX_Declare_StackItem_pm");
	}

	public void testMooseX_Declare_Syntax_EmptyBlockIfMissing_pm()
	{
		doTest("MooseX_Declare_Syntax_EmptyBlockIfMissing_pm");
	}

	public void testMooseX_Declare_Syntax_Extending_pm()
	{
		doTest("MooseX_Declare_Syntax_Extending_pm");
	}

	public void testMooseX_Declare_Syntax_InnerSyntaxHandling_pm()
	{
		doTest("MooseX_Declare_Syntax_InnerSyntaxHandling_pm");
	}

	public void testMooseX_Declare_Syntax_KeywordHandling_pm()
	{
		doTest("MooseX_Declare_Syntax_KeywordHandling_pm");
	}

	public void testMooseX_Declare_Syntax_Keyword_Class_pm()
	{
		doTest("MooseX_Declare_Syntax_Keyword_Class_pm");
	}

	public void testMooseX_Declare_Syntax_Keyword_Clean_pm()
	{
		doTest("MooseX_Declare_Syntax_Keyword_Clean_pm");
	}

	public void testMooseX_Declare_Syntax_Keyword_MethodModifier_pm()
	{
		doTest("MooseX_Declare_Syntax_Keyword_MethodModifier_pm");
	}

	public void testMooseX_Declare_Syntax_Keyword_Method_pm()
	{
		doTest("MooseX_Declare_Syntax_Keyword_Method_pm");
	}

	public void testMooseX_Declare_Syntax_Keyword_Namespace_pm()
	{
		doTest("MooseX_Declare_Syntax_Keyword_Namespace_pm");
	}

	public void testMooseX_Declare_Syntax_Keyword_Role_pm()
	{
		doTest("MooseX_Declare_Syntax_Keyword_Role_pm");
	}

	public void testMooseX_Declare_Syntax_Keyword_With_pm()
	{
		doTest("MooseX_Declare_Syntax_Keyword_With_pm");
	}

	public void testMooseX_Declare_Syntax_MethodDeclaration_Parameterized_pm()
	{
		doTest("MooseX_Declare_Syntax_MethodDeclaration_Parameterized_pm");
	}

	public void testMooseX_Declare_Syntax_MethodDeclaration_pm()
	{
		doTest("MooseX_Declare_Syntax_MethodDeclaration_pm");
	}

	public void testMooseX_Declare_Syntax_MooseSetup_pm()
	{
		doTest("MooseX_Declare_Syntax_MooseSetup_pm");
	}

	public void testMooseX_Declare_Syntax_NamespaceHandling_pm()
	{
		doTest("MooseX_Declare_Syntax_NamespaceHandling_pm");
	}

	public void testMooseX_Declare_Syntax_OptionHandling_pm()
	{
		doTest("MooseX_Declare_Syntax_OptionHandling_pm");
	}

	public void testMooseX_Declare_Syntax_RoleApplication_pm()
	{
		doTest("MooseX_Declare_Syntax_RoleApplication_pm");
	}

	public void testMooseX_Declare_Util_pm()
	{
		doTest("MooseX_Declare_Util_pm");
	}

	public void testMooseX_Declare_pm()
	{
		doTest("MooseX_Declare_pm");
	}

	public void testMooseX_Getopt_Basic_pm()
	{
		doTest("MooseX_Getopt_Basic_pm");
	}

	public void testMooseX_Getopt_Dashes_pm()
	{
		doTest("MooseX_Getopt_Dashes_pm");
	}

	public void testMooseX_Getopt_GLD_pm()
	{
		doTest("MooseX_Getopt_GLD_pm");
	}

	public void testMooseX_Getopt_Meta_Attribute_NoGetopt_pm()
	{
		doTest("MooseX_Getopt_Meta_Attribute_NoGetopt_pm");
	}

	public void testMooseX_Getopt_Meta_Attribute_Trait_NoGetopt_pm()
	{
		doTest("MooseX_Getopt_Meta_Attribute_Trait_NoGetopt_pm");
	}

	public void testMooseX_Getopt_Meta_Attribute_Trait_pm()
	{
		doTest("MooseX_Getopt_Meta_Attribute_Trait_pm");
	}

	public void testMooseX_Getopt_Meta_Attribute_pm()
	{
		doTest("MooseX_Getopt_Meta_Attribute_pm");
	}

	public void testMooseX_Getopt_OptionTypeMap_pm()
	{
		doTest("MooseX_Getopt_OptionTypeMap_pm");
	}

	public void testMooseX_Getopt_ProcessedArgv_pm()
	{
		doTest("MooseX_Getopt_ProcessedArgv_pm");
	}

	public void testMooseX_Getopt_Strict_pm()
	{
		doTest("MooseX_Getopt_Strict_pm");
	}

	public void testMooseX_Getopt_pm()
	{
		doTest("MooseX_Getopt_pm");
	}

	public void testMooseX_HasDefaults_Meta_IsRO_pm()
	{
		doTest("MooseX_HasDefaults_Meta_IsRO_pm");
	}

	public void testMooseX_HasDefaults_Meta_IsRW_pm()
	{
		doTest("MooseX_HasDefaults_Meta_IsRW_pm");
	}

	public void testMooseX_HasDefaults_RO_pm()
	{
		doTest("MooseX_HasDefaults_RO_pm");
	}

	public void testMooseX_HasDefaults_RW_pm()
	{
		doTest("MooseX_HasDefaults_RW_pm");
	}

	public void testMooseX_HasDefaults_pm()
	{
		doTest("MooseX_HasDefaults_pm");
	}

	public void testMooseX_LazyRequire_Meta_Attribute_Trait_LazyRequire_pm()
	{
		doTest("MooseX_LazyRequire_Meta_Attribute_Trait_LazyRequire_pm");
	}

	public void testMooseX_LazyRequire_pm()
	{
		doTest("MooseX_LazyRequire_pm");
	}

	public void testMooseX_Meta_TypeCoercion_Structured_Optional_pm()
	{
		doTest("MooseX_Meta_TypeCoercion_Structured_Optional_pm");
	}

	public void testMooseX_Meta_TypeCoercion_Structured_pm()
	{
		doTest("MooseX_Meta_TypeCoercion_Structured_pm");
	}

	public void testMooseX_Meta_TypeConstraint_ForceCoercion_pm()
	{
		doTest("MooseX_Meta_TypeConstraint_ForceCoercion_pm");
	}

	public void testMooseX_Meta_TypeConstraint_Structured_Optional_pm()
	{
		doTest("MooseX_Meta_TypeConstraint_Structured_Optional_pm");
	}

	public void testMooseX_Meta_TypeConstraint_Structured_pm()
	{
		doTest("MooseX_Meta_TypeConstraint_Structured_pm");
	}

	public void testMooseX_Method_Signatures_Meta_Method_pm()
	{
		doTest("MooseX_Method_Signatures_Meta_Method_pm");
	}

	public void testMooseX_Method_Signatures_Types_pm()
	{
		doTest("MooseX_Method_Signatures_Types_pm");
	}

	public void testMooseX_Method_Signatures_pm()
	{
		doTest("MooseX_Method_Signatures_pm");
	}

	public void testMooseX_NonMoose_InsideOut_pm()
	{
		doTest("MooseX_NonMoose_InsideOut_pm");
	}

	public void testMooseX_NonMoose_Meta_Role_Class_pm()
	{
		doTest("MooseX_NonMoose_Meta_Role_Class_pm");
	}

	public void testMooseX_NonMoose_Meta_Role_Constructor_pm()
	{
		doTest("MooseX_NonMoose_Meta_Role_Constructor_pm");
	}

	public void testMooseX_NonMoose_pm()
	{
		doTest("MooseX_NonMoose_pm");
	}

	public void testMooseX_OneArgNew_pm()
	{
		doTest("MooseX_OneArgNew_pm");
	}

	public void testMooseX_Role_Parameterized_Meta_Role_Parameterized_pm()
	{
		doTest("MooseX_Role_Parameterized_Meta_Role_Parameterized_pm");
	}

	public void testMooseX_Role_Parameterized_Meta_Trait_Parameterizable_pm()
	{
		doTest("MooseX_Role_Parameterized_Meta_Trait_Parameterizable_pm");
	}

	public void testMooseX_Role_Parameterized_Meta_Trait_Parameterized_pm()
	{
		doTest("MooseX_Role_Parameterized_Meta_Trait_Parameterized_pm");
	}

	public void testMooseX_Role_Parameterized_Parameters_pm()
	{
		doTest("MooseX_Role_Parameterized_Parameters_pm");
	}

	public void testMooseX_Role_Parameterized_pm()
	{
		doTest("MooseX_Role_Parameterized_pm");
	}

	public void testMooseX_Role_WithOverloading_Meta_Role_Application_Composite_ToClass_pm()
	{
		doTest("MooseX_Role_WithOverloading_Meta_Role_Application_Composite_ToClass_pm");
	}

	public void testMooseX_Role_WithOverloading_Meta_Role_Application_Composite_ToInstance_pm()
	{
		doTest("MooseX_Role_WithOverloading_Meta_Role_Application_Composite_ToInstance_pm");
	}

	public void testMooseX_Role_WithOverloading_Meta_Role_Application_Composite_ToRole_pm()
	{
		doTest("MooseX_Role_WithOverloading_Meta_Role_Application_Composite_ToRole_pm");
	}

	public void testMooseX_Role_WithOverloading_Meta_Role_Application_Composite_pm()
	{
		doTest("MooseX_Role_WithOverloading_Meta_Role_Application_Composite_pm");
	}

	public void testMooseX_Role_WithOverloading_Meta_Role_Application_FixOverloadedRefs_pm()
	{
		doTest("MooseX_Role_WithOverloading_Meta_Role_Application_FixOverloadedRefs_pm");
	}

	public void testMooseX_Role_WithOverloading_Meta_Role_Application_ToClass_pm()
	{
		doTest("MooseX_Role_WithOverloading_Meta_Role_Application_ToClass_pm");
	}

	public void testMooseX_Role_WithOverloading_Meta_Role_Application_ToInstance_pm()
	{
		doTest("MooseX_Role_WithOverloading_Meta_Role_Application_ToInstance_pm");
	}

	public void testMooseX_Role_WithOverloading_Meta_Role_Application_ToRole_pm()
	{
		doTest("MooseX_Role_WithOverloading_Meta_Role_Application_ToRole_pm");
	}

	public void testMooseX_Role_WithOverloading_Meta_Role_Application_pm()
	{
		doTest("MooseX_Role_WithOverloading_Meta_Role_Application_pm");
	}

	public void testMooseX_Role_WithOverloading_Meta_Role_Composite_pm()
	{
		doTest("MooseX_Role_WithOverloading_Meta_Role_Composite_pm");
	}

	public void testMooseX_Role_WithOverloading_Meta_Role_pm()
	{
		doTest("MooseX_Role_WithOverloading_Meta_Role_pm");
	}

	public void testMooseX_Role_WithOverloading_pm()
	{
		doTest("MooseX_Role_WithOverloading_pm");
	}

	public void testMooseX_SetOnce_pm()
	{
		doTest("MooseX_SetOnce_pm");
	}

	public void testMooseX_SimpleConfig_pm()
	{
		doTest("MooseX_SimpleConfig_pm");
	}

	public void testMooseX_StrictConstructor_Trait_Class_pm()
	{
		doTest("MooseX_StrictConstructor_Trait_Class_pm");
	}

	public void testMooseX_StrictConstructor_Trait_Method_Constructor_pm()
	{
		doTest("MooseX_StrictConstructor_Trait_Method_Constructor_pm");
	}

	public void testMooseX_StrictConstructor_pm()
	{
		doTest("MooseX_StrictConstructor_pm");
	}

	public void testMooseX_Traits_Util_pm()
	{
		doTest("MooseX_Traits_Util_pm");
	}

	public void testMooseX_Traits_pm()
	{
		doTest("MooseX_Traits_pm");
	}

	public void testMooseX_Types_Base_pm()
	{
		doTest("MooseX_Types_Base_pm");
	}

	public void testMooseX_Types_CheckedUtilExports_pm()
	{
		doTest("MooseX_Types_CheckedUtilExports_pm");
	}

	public void testMooseX_Types_Combine_pm()
	{
		doTest("MooseX_Types_Combine_pm");
	}

	public void testMooseX_Types_DateTime_pm()
	{
		doTest("MooseX_Types_DateTime_pm");
	}

	public void testMooseX_Types_Moose_pm()
	{
		doTest("MooseX_Types_Moose_pm");
	}

	public void testMooseX_Types_Path_Class_pm()
	{
		doTest("MooseX_Types_Path_Class_pm");
	}

	public void testMooseX_Types_Path_Tiny_pm()
	{
		doTest("MooseX_Types_Path_Tiny_pm");
	}

	public void testMooseX_Types_Perl_pm()
	{
		doTest("MooseX_Types_Perl_pm");
	}

	public void testMooseX_Types_Stringlike_pm()
	{
		doTest("MooseX_Types_Stringlike_pm");
	}

	public void testMooseX_Types_Structured_MessageStack_pm()
	{
		doTest("MooseX_Types_Structured_MessageStack_pm");
	}

	public void testMooseX_Types_Structured_OverflowHandler_pm()
	{
		doTest("MooseX_Types_Structured_OverflowHandler_pm");
	}

	public void testMooseX_Types_Structured_pm()
	{
		doTest("MooseX_Types_Structured_pm");
	}

	public void testMooseX_Types_TypeDecorator_pm()
	{
		doTest("MooseX_Types_TypeDecorator_pm");
	}

	public void testMooseX_Types_UndefinedType_pm()
	{
		doTest("MooseX_Types_UndefinedType_pm");
	}

	public void testMooseX_Types_Util_pm()
	{
		doTest("MooseX_Types_Util_pm");
	}

	public void testMooseX_Types_Wrapper_pm()
	{
		doTest("MooseX_Types_Wrapper_pm");
	}

	public void testMooseX_Types_pm()
	{
		doTest("MooseX_Types_pm");
	}

	public void testMoose_Autobox_Array_pm()
	{
		doTest("Moose_Autobox_Array_pm");
	}

	public void testMoose_Autobox_Code_pm()
	{
		doTest("Moose_Autobox_Code_pm");
	}

	public void testMoose_Autobox_Defined_pm()
	{
		doTest("Moose_Autobox_Defined_pm");
	}

	public void testMoose_Autobox_Hash_pm()
	{
		doTest("Moose_Autobox_Hash_pm");
	}

	public void testMoose_Autobox_Indexed_pm()
	{
		doTest("Moose_Autobox_Indexed_pm");
	}

	public void testMoose_Autobox_Item_pm()
	{
		doTest("Moose_Autobox_Item_pm");
	}

	public void testMoose_Autobox_List_pm()
	{
		doTest("Moose_Autobox_List_pm");
	}

	public void testMoose_Autobox_Number_pm()
	{
		doTest("Moose_Autobox_Number_pm");
	}

	public void testMoose_Autobox_Ref_pm()
	{
		doTest("Moose_Autobox_Ref_pm");
	}

	public void testMoose_Autobox_Scalar_pm()
	{
		doTest("Moose_Autobox_Scalar_pm");
	}

	public void testMoose_Autobox_String_pm()
	{
		doTest("Moose_Autobox_String_pm");
	}

	public void testMoose_Autobox_Undef_pm()
	{
		doTest("Moose_Autobox_Undef_pm");
	}

	public void testMoose_Autobox_Value_pm()
	{
		doTest("Moose_Autobox_Value_pm");
	}

	public void testMoose_Autobox_pm()
	{
		doTest("Moose_Autobox_pm");
	}

	public void testMoose_Conflicts_pm()
	{
		doTest("Moose_Conflicts_pm");
	}

	public void testMoose_Deprecated_pm()
	{
		doTest("Moose_Deprecated_pm");
	}

	public void testMoose_Exception_AccessorMustReadWrite_pm()
	{
		doTest("Moose_Exception_AccessorMustReadWrite_pm");
	}

	public void testMoose_Exception_AddParameterizableTypeTakesParameterizableType_pm()
	{
		doTest("Moose_Exception_AddParameterizableTypeTakesParameterizableType_pm");
	}

	public void testMoose_Exception_AddRoleTakesAMooseMetaRoleInstance_pm()
	{
		doTest("Moose_Exception_AddRoleTakesAMooseMetaRoleInstance_pm");
	}

	public void testMoose_Exception_AddRoleToARoleTakesAMooseMetaRole_pm()
	{
		doTest("Moose_Exception_AddRoleToARoleTakesAMooseMetaRole_pm");
	}

	public void testMoose_Exception_ApplyTakesABlessedInstance_pm()
	{
		doTest("Moose_Exception_ApplyTakesABlessedInstance_pm");
	}

	public void testMoose_Exception_AttachToClassNeedsAClassMOPClassInstanceOrASubclass_pm()
	{
		doTest("Moose_Exception_AttachToClassNeedsAClassMOPClassInstanceOrASubclass_pm");
	}

	public void testMoose_Exception_AttributeConflictInRoles_pm()
	{
		doTest("Moose_Exception_AttributeConflictInRoles_pm");
	}

	public void testMoose_Exception_AttributeConflictInSummation_pm()
	{
		doTest("Moose_Exception_AttributeConflictInSummation_pm");
	}

	public void testMoose_Exception_AttributeExtensionIsNotSupportedInRoles_pm()
	{
		doTest("Moose_Exception_AttributeExtensionIsNotSupportedInRoles_pm");
	}

	public void testMoose_Exception_AttributeIsRequired_pm()
	{
		doTest("Moose_Exception_AttributeIsRequired_pm");
	}

	public void testMoose_Exception_AttributeMustBeAnClassMOPMixinAttributeCoreOrSubclass_pm()
	{
		doTest("Moose_Exception_AttributeMustBeAnClassMOPMixinAttributeCoreOrSubclass_pm");
	}

	public void testMoose_Exception_AttributeNamesDoNotMatch_pm()
	{
		doTest("Moose_Exception_AttributeNamesDoNotMatch_pm");
	}

	public void testMoose_Exception_AttributeValueIsNotAnObject_pm()
	{
		doTest("Moose_Exception_AttributeValueIsNotAnObject_pm");
	}

	public void testMoose_Exception_AttributeValueIsNotDefined_pm()
	{
		doTest("Moose_Exception_AttributeValueIsNotDefined_pm");
	}

	public void testMoose_Exception_AutoDeRefNeedsArrayRefOrHashRef_pm()
	{
		doTest("Moose_Exception_AutoDeRefNeedsArrayRefOrHashRef_pm");
	}

	public void testMoose_Exception_BadOptionFormat_pm()
	{
		doTest("Moose_Exception_BadOptionFormat_pm");
	}

	public void testMoose_Exception_BothBuilderAndDefaultAreNotAllowed_pm()
	{
		doTest("Moose_Exception_BothBuilderAndDefaultAreNotAllowed_pm");
	}

	public void testMoose_Exception_BuilderDoesNotExist_pm()
	{
		doTest("Moose_Exception_BuilderDoesNotExist_pm");
	}

	public void testMoose_Exception_BuilderMethodNotSupportedForAttribute_pm()
	{
		doTest("Moose_Exception_BuilderMethodNotSupportedForAttribute_pm");
	}

	public void testMoose_Exception_BuilderMethodNotSupportedForInlineAttribute_pm()
	{
		doTest("Moose_Exception_BuilderMethodNotSupportedForInlineAttribute_pm");
	}

	public void testMoose_Exception_BuilderMustBeAMethodName_pm()
	{
		doTest("Moose_Exception_BuilderMustBeAMethodName_pm");
	}

	public void testMoose_Exception_CallingMethodOnAnImmutableInstance_pm()
	{
		doTest("Moose_Exception_CallingMethodOnAnImmutableInstance_pm");
	}

	public void testMoose_Exception_CallingReadOnlyMethodOnAnImmutableInstance_pm()
	{
		doTest("Moose_Exception_CallingReadOnlyMethodOnAnImmutableInstance_pm");
	}

	public void testMoose_Exception_CanExtendOnlyClasses_pm()
	{
		doTest("Moose_Exception_CanExtendOnlyClasses_pm");
	}

	public void testMoose_Exception_CanOnlyConsumeRole_pm()
	{
		doTest("Moose_Exception_CanOnlyConsumeRole_pm");
	}

	public void testMoose_Exception_CanOnlyWrapBlessedCode_pm()
	{
		doTest("Moose_Exception_CanOnlyWrapBlessedCode_pm");
	}

	public void testMoose_Exception_CanReblessOnlyIntoASubclass_pm()
	{
		doTest("Moose_Exception_CanReblessOnlyIntoASubclass_pm");
	}

	public void testMoose_Exception_CanReblessOnlyIntoASuperclass_pm()
	{
		doTest("Moose_Exception_CanReblessOnlyIntoASuperclass_pm");
	}

	public void testMoose_Exception_CannotAddAdditionalTypeCoercionsToUnion_pm()
	{
		doTest("Moose_Exception_CannotAddAdditionalTypeCoercionsToUnion_pm");
	}

	public void testMoose_Exception_CannotAddAsAnAttributeToARole_pm()
	{
		doTest("Moose_Exception_CannotAddAsAnAttributeToARole_pm");
	}

	public void testMoose_Exception_CannotApplyBaseClassRolesToRole_pm()
	{
		doTest("Moose_Exception_CannotApplyBaseClassRolesToRole_pm");
	}

	public void testMoose_Exception_CannotAssignValueToReadOnlyAccessor_pm()
	{
		doTest("Moose_Exception_CannotAssignValueToReadOnlyAccessor_pm");
	}

	public void testMoose_Exception_CannotAugmentIfLocalMethodPresent_pm()
	{
		doTest("Moose_Exception_CannotAugmentIfLocalMethodPresent_pm");
	}

	public void testMoose_Exception_CannotAugmentNoSuperMethod_pm()
	{
		doTest("Moose_Exception_CannotAugmentNoSuperMethod_pm");
	}

	public void testMoose_Exception_CannotAutoDerefWithoutIsa_pm()
	{
		doTest("Moose_Exception_CannotAutoDerefWithoutIsa_pm");
	}

	public void testMoose_Exception_CannotAutoDereferenceTypeConstraint_pm()
	{
		doTest("Moose_Exception_CannotAutoDereferenceTypeConstraint_pm");
	}

	public void testMoose_Exception_CannotCalculateNativeType_pm()
	{
		doTest("Moose_Exception_CannotCalculateNativeType_pm");
	}

	public void testMoose_Exception_CannotCallAnAbstractBaseMethod_pm()
	{
		doTest("Moose_Exception_CannotCallAnAbstractBaseMethod_pm");
	}

	public void testMoose_Exception_CannotCallAnAbstractMethod_pm()
	{
		doTest("Moose_Exception_CannotCallAnAbstractMethod_pm");
	}

	public void testMoose_Exception_CannotCoerceAWeakRef_pm()
	{
		doTest("Moose_Exception_CannotCoerceAWeakRef_pm");
	}

	public void testMoose_Exception_CannotCoerceAttributeWhichHasNoCoercion_pm()
	{
		doTest("Moose_Exception_CannotCoerceAttributeWhichHasNoCoercion_pm");
	}

	public void testMoose_Exception_CannotCreateHigherOrderTypeWithoutATypeParameter_pm()
	{
		doTest("Moose_Exception_CannotCreateHigherOrderTypeWithoutATypeParameter_pm");
	}

	public void testMoose_Exception_CannotCreateMethodAliasLocalMethodIsPresentInClass_pm()
	{
		doTest("Moose_Exception_CannotCreateMethodAliasLocalMethodIsPresentInClass_pm");
	}

	public void testMoose_Exception_CannotCreateMethodAliasLocalMethodIsPresent_pm()
	{
		doTest("Moose_Exception_CannotCreateMethodAliasLocalMethodIsPresent_pm");
	}

	public void testMoose_Exception_CannotDelegateLocalMethodIsPresent_pm()
	{
		doTest("Moose_Exception_CannotDelegateLocalMethodIsPresent_pm");
	}

	public void testMoose_Exception_CannotDelegateWithoutIsa_pm()
	{
		doTest("Moose_Exception_CannotDelegateWithoutIsa_pm");
	}

	public void testMoose_Exception_CannotFindDelegateMetaclass_pm()
	{
		doTest("Moose_Exception_CannotFindDelegateMetaclass_pm");
	}

	public void testMoose_Exception_CannotFindTypeGivenToMatchOnType_pm()
	{
		doTest("Moose_Exception_CannotFindTypeGivenToMatchOnType_pm");
	}

	public void testMoose_Exception_CannotFindType_pm()
	{
		doTest("Moose_Exception_CannotFindType_pm");
	}

	public void testMoose_Exception_CannotFixMetaclassCompatibility_pm()
	{
		doTest("Moose_Exception_CannotFixMetaclassCompatibility_pm");
	}

	public void testMoose_Exception_CannotGenerateInlineConstraint_pm()
	{
		doTest("Moose_Exception_CannotGenerateInlineConstraint_pm");
	}

	public void testMoose_Exception_CannotInitializeMooseMetaRoleComposite_pm()
	{
		doTest("Moose_Exception_CannotInitializeMooseMetaRoleComposite_pm");
	}

	public void testMoose_Exception_CannotInlineTypeConstraintCheck_pm()
	{
		doTest("Moose_Exception_CannotInlineTypeConstraintCheck_pm");
	}

	public void testMoose_Exception_CannotLocatePackageInINC_pm()
	{
		doTest("Moose_Exception_CannotLocatePackageInINC_pm");
	}

	public void testMoose_Exception_CannotMakeMetaclassCompatible_pm()
	{
		doTest("Moose_Exception_CannotMakeMetaclassCompatible_pm");
	}

	public void testMoose_Exception_CannotOverrideALocalMethod_pm()
	{
		doTest("Moose_Exception_CannotOverrideALocalMethod_pm");
	}

	public void testMoose_Exception_CannotOverrideBodyOfMetaMethods_pm()
	{
		doTest("Moose_Exception_CannotOverrideBodyOfMetaMethods_pm");
	}

	public void testMoose_Exception_CannotOverrideLocalMethodIsPresent_pm()
	{
		doTest("Moose_Exception_CannotOverrideLocalMethodIsPresent_pm");
	}

	public void testMoose_Exception_CannotOverrideNoSuperMethod_pm()
	{
		doTest("Moose_Exception_CannotOverrideNoSuperMethod_pm");
	}

	public void testMoose_Exception_CannotRegisterUnnamedTypeConstraint_pm()
	{
		doTest("Moose_Exception_CannotRegisterUnnamedTypeConstraint_pm");
	}

	public void testMoose_Exception_CannotUseLazyBuildAndDefaultSimultaneously_pm()
	{
		doTest("Moose_Exception_CannotUseLazyBuildAndDefaultSimultaneously_pm");
	}

	public void testMoose_Exception_CircularReferenceInAlso_pm()
	{
		doTest("Moose_Exception_CircularReferenceInAlso_pm");
	}

	public void testMoose_Exception_ClassDoesNotHaveInitMeta_pm()
	{
		doTest("Moose_Exception_ClassDoesNotHaveInitMeta_pm");
	}

	public void testMoose_Exception_ClassDoesTheExcludedRole_pm()
	{
		doTest("Moose_Exception_ClassDoesTheExcludedRole_pm");
	}

	public void testMoose_Exception_ClassNamesDoNotMatch_pm()
	{
		doTest("Moose_Exception_ClassNamesDoNotMatch_pm");
	}

	public void testMoose_Exception_CloneObjectExpectsAnInstanceOfMetaclass_pm()
	{
		doTest("Moose_Exception_CloneObjectExpectsAnInstanceOfMetaclass_pm");
	}

	public void testMoose_Exception_CodeBlockMustBeACodeRef_pm()
	{
		doTest("Moose_Exception_CodeBlockMustBeACodeRef_pm");
	}

	public void testMoose_Exception_CoercingWithoutCoercions_pm()
	{
		doTest("Moose_Exception_CoercingWithoutCoercions_pm");
	}

	public void testMoose_Exception_CoercionAlreadyExists_pm()
	{
		doTest("Moose_Exception_CoercionAlreadyExists_pm");
	}

	public void testMoose_Exception_CoercionNeedsTypeConstraint_pm()
	{
		doTest("Moose_Exception_CoercionNeedsTypeConstraint_pm");
	}

	public void testMoose_Exception_ConflictDetectedInCheckRoleExclusionsInToClass_pm()
	{
		doTest("Moose_Exception_ConflictDetectedInCheckRoleExclusionsInToClass_pm");
	}

	public void testMoose_Exception_ConflictDetectedInCheckRoleExclusions_pm()
	{
		doTest("Moose_Exception_ConflictDetectedInCheckRoleExclusions_pm");
	}

	public void testMoose_Exception_ConstructClassInstanceTakesPackageName_pm()
	{
		doTest("Moose_Exception_ConstructClassInstanceTakesPackageName_pm");
	}

	public void testMoose_Exception_CouldNotCreateMethod_pm()
	{
		doTest("Moose_Exception_CouldNotCreateMethod_pm");
	}

	public void testMoose_Exception_CouldNotCreateWriter_pm()
	{
		doTest("Moose_Exception_CouldNotCreateWriter_pm");
	}

	public void testMoose_Exception_CouldNotEvalConstructor_pm()
	{
		doTest("Moose_Exception_CouldNotEvalConstructor_pm");
	}

	public void testMoose_Exception_CouldNotEvalDestructor_pm()
	{
		doTest("Moose_Exception_CouldNotEvalDestructor_pm");
	}

	public void testMoose_Exception_CouldNotFindTypeConstraintToCoerceFrom_pm()
	{
		doTest("Moose_Exception_CouldNotFindTypeConstraintToCoerceFrom_pm");
	}

	public void testMoose_Exception_CouldNotGenerateInlineAttributeMethod_pm()
	{
		doTest("Moose_Exception_CouldNotGenerateInlineAttributeMethod_pm");
	}

	public void testMoose_Exception_CouldNotLocateTypeConstraintForUnion_pm()
	{
		doTest("Moose_Exception_CouldNotLocateTypeConstraintForUnion_pm");
	}

	public void testMoose_Exception_CouldNotParseType_pm()
	{
		doTest("Moose_Exception_CouldNotParseType_pm");
	}

	public void testMoose_Exception_CreateMOPClassTakesArrayRefOfAttributes_pm()
	{
		doTest("Moose_Exception_CreateMOPClassTakesArrayRefOfAttributes_pm");
	}

	public void testMoose_Exception_CreateMOPClassTakesArrayRefOfSuperclasses_pm()
	{
		doTest("Moose_Exception_CreateMOPClassTakesArrayRefOfSuperclasses_pm");
	}

	public void testMoose_Exception_CreateMOPClassTakesHashRefOfMethods_pm()
	{
		doTest("Moose_Exception_CreateMOPClassTakesHashRefOfMethods_pm");
	}

	public void testMoose_Exception_CreateTakesArrayRefOfRoles_pm()
	{
		doTest("Moose_Exception_CreateTakesArrayRefOfRoles_pm");
	}

	public void testMoose_Exception_CreateTakesHashRefOfAttributes_pm()
	{
		doTest("Moose_Exception_CreateTakesHashRefOfAttributes_pm");
	}

	public void testMoose_Exception_CreateTakesHashRefOfMethods_pm()
	{
		doTest("Moose_Exception_CreateTakesHashRefOfMethods_pm");
	}

	public void testMoose_Exception_DefaultToMatchOnTypeMustBeCodeRef_pm()
	{
		doTest("Moose_Exception_DefaultToMatchOnTypeMustBeCodeRef_pm");
	}

	public void testMoose_Exception_DelegationToAClassWhichIsNotLoaded_pm()
	{
		doTest("Moose_Exception_DelegationToAClassWhichIsNotLoaded_pm");
	}

	public void testMoose_Exception_DelegationToARoleWhichIsNotLoaded_pm()
	{
		doTest("Moose_Exception_DelegationToARoleWhichIsNotLoaded_pm");
	}

	public void testMoose_Exception_DelegationToATypeWhichIsNotAClass_pm()
	{
		doTest("Moose_Exception_DelegationToATypeWhichIsNotAClass_pm");
	}

	public void testMoose_Exception_DoesRequiresRoleName_pm()
	{
		doTest("Moose_Exception_DoesRequiresRoleName_pm");
	}

	public void testMoose_Exception_EnumCalledWithAnArrayRefAndAdditionalArgs_pm()
	{
		doTest("Moose_Exception_EnumCalledWithAnArrayRefAndAdditionalArgs_pm");
	}

	public void testMoose_Exception_EnumValuesMustBeString_pm()
	{
		doTest("Moose_Exception_EnumValuesMustBeString_pm");
	}

	public void testMoose_Exception_ExtendsMissingArgs_pm()
	{
		doTest("Moose_Exception_ExtendsMissingArgs_pm");
	}

	public void testMoose_Exception_HandlesMustBeAHashRef_pm()
	{
		doTest("Moose_Exception_HandlesMustBeAHashRef_pm");
	}

	public void testMoose_Exception_IllegalInheritedOptions_pm()
	{
		doTest("Moose_Exception_IllegalInheritedOptions_pm");
	}

	public void testMoose_Exception_IllegalMethodTypeToAddMethodModifier_pm()
	{
		doTest("Moose_Exception_IllegalMethodTypeToAddMethodModifier_pm");
	}

	public void testMoose_Exception_IncompatibleMetaclassOfSuperclass_pm()
	{
		doTest("Moose_Exception_IncompatibleMetaclassOfSuperclass_pm");
	}

	public void testMoose_Exception_InitMetaRequiresClass_pm()
	{
		doTest("Moose_Exception_InitMetaRequiresClass_pm");
	}

	public void testMoose_Exception_InitializeTakesUnBlessedPackageName_pm()
	{
		doTest("Moose_Exception_InitializeTakesUnBlessedPackageName_pm");
	}

	public void testMoose_Exception_InstanceBlessedIntoWrongClass_pm()
	{
		doTest("Moose_Exception_InstanceBlessedIntoWrongClass_pm");
	}

	public void testMoose_Exception_InstanceMustBeABlessedReference_pm()
	{
		doTest("Moose_Exception_InstanceMustBeABlessedReference_pm");
	}

	public void testMoose_Exception_InvalidArgPassedToMooseUtilMetaRole_pm()
	{
		doTest("Moose_Exception_InvalidArgPassedToMooseUtilMetaRole_pm");
	}

	public void testMoose_Exception_InvalidArgumentToMethod_pm()
	{
		doTest("Moose_Exception_InvalidArgumentToMethod_pm");
	}

	public void testMoose_Exception_InvalidArgumentsToTraitAliases_pm()
	{
		doTest("Moose_Exception_InvalidArgumentsToTraitAliases_pm");
	}

	public void testMoose_Exception_InvalidBaseTypeGivenToCreateParameterizedTypeConstraint_pm()
	{
		doTest("Moose_Exception_InvalidBaseTypeGivenToCreateParameterizedTypeConstraint_pm");
	}

	public void testMoose_Exception_InvalidHandleValue_pm()
	{
		doTest("Moose_Exception_InvalidHandleValue_pm");
	}

	public void testMoose_Exception_InvalidHasProvidedInARole_pm()
	{
		doTest("Moose_Exception_InvalidHasProvidedInARole_pm");
	}

	public void testMoose_Exception_InvalidNameForType_pm()
	{
		doTest("Moose_Exception_InvalidNameForType_pm");
	}

	public void testMoose_Exception_InvalidOverloadOperator_pm()
	{
		doTest("Moose_Exception_InvalidOverloadOperator_pm");
	}

	public void testMoose_Exception_InvalidRoleApplication_pm()
	{
		doTest("Moose_Exception_InvalidRoleApplication_pm");
	}

	public void testMoose_Exception_InvalidTypeConstraint_pm()
	{
		doTest("Moose_Exception_InvalidTypeConstraint_pm");
	}

	public void testMoose_Exception_InvalidTypeGivenToCreateParameterizedTypeConstraint_pm()
	{
		doTest("Moose_Exception_InvalidTypeGivenToCreateParameterizedTypeConstraint_pm");
	}

	public void testMoose_Exception_InvalidValueForIs_pm()
	{
		doTest("Moose_Exception_InvalidValueForIs_pm");
	}

	public void testMoose_Exception_IsaDoesNotDoTheRole_pm()
	{
		doTest("Moose_Exception_IsaDoesNotDoTheRole_pm");
	}

	public void testMoose_Exception_IsaLacksDoesMethod_pm()
	{
		doTest("Moose_Exception_IsaLacksDoesMethod_pm");
	}

	public void testMoose_Exception_LazyAttributeNeedsADefault_pm()
	{
		doTest("Moose_Exception_LazyAttributeNeedsADefault_pm");
	}

	public void testMoose_Exception_Legacy_pm()
	{
		doTest("Moose_Exception_Legacy_pm");
	}

	public void testMoose_Exception_MOPAttributeNewNeedsAttributeName_pm()
	{
		doTest("Moose_Exception_MOPAttributeNewNeedsAttributeName_pm");
	}

	public void testMoose_Exception_MatchActionMustBeACodeRef_pm()
	{
		doTest("Moose_Exception_MatchActionMustBeACodeRef_pm");
	}

	public void testMoose_Exception_MessageParameterMustBeCodeRef_pm()
	{
		doTest("Moose_Exception_MessageParameterMustBeCodeRef_pm");
	}

	public void testMoose_Exception_MetaclassIsAClassNotASubclassOfGivenMetaclass_pm()
	{
		doTest("Moose_Exception_MetaclassIsAClassNotASubclassOfGivenMetaclass_pm");
	}

	public void testMoose_Exception_MetaclassIsARoleNotASubclassOfGivenMetaclass_pm()
	{
		doTest("Moose_Exception_MetaclassIsARoleNotASubclassOfGivenMetaclass_pm");
	}

	public void testMoose_Exception_MetaclassIsNotASubclassOfGivenMetaclass_pm()
	{
		doTest("Moose_Exception_MetaclassIsNotASubclassOfGivenMetaclass_pm");
	}

	public void testMoose_Exception_MetaclassMustBeASubclassOfMooseMetaClass_pm()
	{
		doTest("Moose_Exception_MetaclassMustBeASubclassOfMooseMetaClass_pm");
	}

	public void testMoose_Exception_MetaclassMustBeASubclassOfMooseMetaRole_pm()
	{
		doTest("Moose_Exception_MetaclassMustBeASubclassOfMooseMetaRole_pm");
	}

	public void testMoose_Exception_MetaclassMustBeDerivedFromClassMOPClass_pm()
	{
		doTest("Moose_Exception_MetaclassMustBeDerivedFromClassMOPClass_pm");
	}

	public void testMoose_Exception_MetaclassNotLoaded_pm()
	{
		doTest("Moose_Exception_MetaclassNotLoaded_pm");
	}

	public void testMoose_Exception_MetaclassTypeIncompatible_pm()
	{
		doTest("Moose_Exception_MetaclassTypeIncompatible_pm");
	}

	public void testMoose_Exception_MethodExpectedAMetaclassObject_pm()
	{
		doTest("Moose_Exception_MethodExpectedAMetaclassObject_pm");
	}

	public void testMoose_Exception_MethodExpectsFewerArgs_pm()
	{
		doTest("Moose_Exception_MethodExpectsFewerArgs_pm");
	}

	public void testMoose_Exception_MethodExpectsMoreArgs_pm()
	{
		doTest("Moose_Exception_MethodExpectsMoreArgs_pm");
	}

	public void testMoose_Exception_MethodModifierNeedsMethodName_pm()
	{
		doTest("Moose_Exception_MethodModifierNeedsMethodName_pm");
	}

	public void testMoose_Exception_MethodNameConflictInRoles_pm()
	{
		doTest("Moose_Exception_MethodNameConflictInRoles_pm");
	}

	public void testMoose_Exception_MethodNameNotFoundInInheritanceHierarchy_pm()
	{
		doTest("Moose_Exception_MethodNameNotFoundInInheritanceHierarchy_pm");
	}

	public void testMoose_Exception_MethodNameNotGiven_pm()
	{
		doTest("Moose_Exception_MethodNameNotGiven_pm");
	}

	public void testMoose_Exception_MustDefineAMethodName_pm()
	{
		doTest("Moose_Exception_MustDefineAMethodName_pm");
	}

	public void testMoose_Exception_MustDefineAnAttributeName_pm()
	{
		doTest("Moose_Exception_MustDefineAnAttributeName_pm");
	}

	public void testMoose_Exception_MustDefineAnOverloadOperator_pm()
	{
		doTest("Moose_Exception_MustDefineAnOverloadOperator_pm");
	}

	public void testMoose_Exception_MustHaveAtLeastOneValueToEnumerate_pm()
	{
		doTest("Moose_Exception_MustHaveAtLeastOneValueToEnumerate_pm");
	}

	public void testMoose_Exception_MustPassAHashOfOptions_pm()
	{
		doTest("Moose_Exception_MustPassAHashOfOptions_pm");
	}

	public void testMoose_Exception_MustPassAMooseMetaRoleInstanceOrSubclass_pm()
	{
		doTest("Moose_Exception_MustPassAMooseMetaRoleInstanceOrSubclass_pm");
	}

	public void testMoose_Exception_MustPassAPackageNameOrAnExistingClassMOPPackageInstance_pm()
	{
		doTest("Moose_Exception_MustPassAPackageNameOrAnExistingClassMOPPackageInstance_pm");
	}

	public void testMoose_Exception_MustPassEvenNumberOfArguments_pm()
	{
		doTest("Moose_Exception_MustPassEvenNumberOfArguments_pm");
	}

	public void testMoose_Exception_MustPassEvenNumberOfAttributeOptions_pm()
	{
		doTest("Moose_Exception_MustPassEvenNumberOfAttributeOptions_pm");
	}

	public void testMoose_Exception_MustProvideANameForTheAttribute_pm()
	{
		doTest("Moose_Exception_MustProvideANameForTheAttribute_pm");
	}

	public void testMoose_Exception_MustSpecifyAtleastOneMethod_pm()
	{
		doTest("Moose_Exception_MustSpecifyAtleastOneMethod_pm");
	}

	public void testMoose_Exception_MustSpecifyAtleastOneRoleToApplicant_pm()
	{
		doTest("Moose_Exception_MustSpecifyAtleastOneRoleToApplicant_pm");
	}

	public void testMoose_Exception_MustSpecifyAtleastOneRole_pm()
	{
		doTest("Moose_Exception_MustSpecifyAtleastOneRole_pm");
	}

	public void testMoose_Exception_MustSupplyAClassMOPAttributeInstance_pm()
	{
		doTest("Moose_Exception_MustSupplyAClassMOPAttributeInstance_pm");
	}

	public void testMoose_Exception_MustSupplyADelegateToMethod_pm()
	{
		doTest("Moose_Exception_MustSupplyADelegateToMethod_pm");
	}

	public void testMoose_Exception_MustSupplyAMetaclass_pm()
	{
		doTest("Moose_Exception_MustSupplyAMetaclass_pm");
	}

	public void testMoose_Exception_MustSupplyAMooseMetaAttributeInstance_pm()
	{
		doTest("Moose_Exception_MustSupplyAMooseMetaAttributeInstance_pm");
	}

	public void testMoose_Exception_MustSupplyAnAccessorTypeToConstructWith_pm()
	{
		doTest("Moose_Exception_MustSupplyAnAccessorTypeToConstructWith_pm");
	}

	public void testMoose_Exception_MustSupplyAnAttributeToConstructWith_pm()
	{
		doTest("Moose_Exception_MustSupplyAnAttributeToConstructWith_pm");
	}

	public void testMoose_Exception_MustSupplyArrayRefAsCurriedArguments_pm()
	{
		doTest("Moose_Exception_MustSupplyArrayRefAsCurriedArguments_pm");
	}

	public void testMoose_Exception_MustSupplyPackageNameAndName_pm()
	{
		doTest("Moose_Exception_MustSupplyPackageNameAndName_pm");
	}

	public void testMoose_Exception_NeedsTypeConstraintUnionForTypeCoercionUnion_pm()
	{
		doTest("Moose_Exception_NeedsTypeConstraintUnionForTypeCoercionUnion_pm");
	}

	public void testMoose_Exception_NeitherAttributeNorAttributeNameIsGiven_pm()
	{
		doTest("Moose_Exception_NeitherAttributeNorAttributeNameIsGiven_pm");
	}

	public void testMoose_Exception_NeitherClassNorClassNameIsGiven_pm()
	{
		doTest("Moose_Exception_NeitherClassNorClassNameIsGiven_pm");
	}

	public void testMoose_Exception_NeitherRoleNorRoleNameIsGiven_pm()
	{
		doTest("Moose_Exception_NeitherRoleNorRoleNameIsGiven_pm");
	}

	public void testMoose_Exception_NeitherTypeNorTypeNameIsGiven_pm()
	{
		doTest("Moose_Exception_NeitherTypeNorTypeNameIsGiven_pm");
	}

	public void testMoose_Exception_NoAttributeFoundInSuperClass_pm()
	{
		doTest("Moose_Exception_NoAttributeFoundInSuperClass_pm");
	}

	public void testMoose_Exception_NoBodyToInitializeInAnAbstractBaseClass_pm()
	{
		doTest("Moose_Exception_NoBodyToInitializeInAnAbstractBaseClass_pm");
	}

	public void testMoose_Exception_NoCasesMatched_pm()
	{
		doTest("Moose_Exception_NoCasesMatched_pm");
	}

	public void testMoose_Exception_NoConstraintCheckForTypeConstraint_pm()
	{
		doTest("Moose_Exception_NoConstraintCheckForTypeConstraint_pm");
	}

	public void testMoose_Exception_NoDestructorClassSpecified_pm()
	{
		doTest("Moose_Exception_NoDestructorClassSpecified_pm");
	}

	public void testMoose_Exception_NoImmutableTraitSpecifiedForClass_pm()
	{
		doTest("Moose_Exception_NoImmutableTraitSpecifiedForClass_pm");
	}

	public void testMoose_Exception_NoParentGivenToSubtype_pm()
	{
		doTest("Moose_Exception_NoParentGivenToSubtype_pm");
	}

	public void testMoose_Exception_OnlyInstancesCanBeCloned_pm()
	{
		doTest("Moose_Exception_OnlyInstancesCanBeCloned_pm");
	}

	public void testMoose_Exception_OperatorIsRequired_pm()
	{
		doTest("Moose_Exception_OperatorIsRequired_pm");
	}

	public void testMoose_Exception_OverloadConflictInSummation_pm()
	{
		doTest("Moose_Exception_OverloadConflictInSummation_pm");
	}

	public void testMoose_Exception_OverloadRequiresAMetaClass_pm()
	{
		doTest("Moose_Exception_OverloadRequiresAMetaClass_pm");
	}

	public void testMoose_Exception_OverloadRequiresAMetaMethod_pm()
	{
		doTest("Moose_Exception_OverloadRequiresAMetaMethod_pm");
	}

	public void testMoose_Exception_OverloadRequiresAMetaOverload_pm()
	{
		doTest("Moose_Exception_OverloadRequiresAMetaOverload_pm");
	}

	public void testMoose_Exception_OverloadRequiresAMethodNameOrCoderef_pm()
	{
		doTest("Moose_Exception_OverloadRequiresAMethodNameOrCoderef_pm");
	}

	public void testMoose_Exception_OverloadRequiresAnOperator_pm()
	{
		doTest("Moose_Exception_OverloadRequiresAnOperator_pm");
	}

	public void testMoose_Exception_OverloadRequiresNamesForCoderef_pm()
	{
		doTest("Moose_Exception_OverloadRequiresNamesForCoderef_pm");
	}

	public void testMoose_Exception_OverrideConflictInComposition_pm()
	{
		doTest("Moose_Exception_OverrideConflictInComposition_pm");
	}

	public void testMoose_Exception_OverrideConflictInSummation_pm()
	{
		doTest("Moose_Exception_OverrideConflictInSummation_pm");
	}

	public void testMoose_Exception_PackageDoesNotUseMooseExporter_pm()
	{
		doTest("Moose_Exception_PackageDoesNotUseMooseExporter_pm");
	}

	public void testMoose_Exception_PackageNameAndNameParamsNotGivenToWrap_pm()
	{
		doTest("Moose_Exception_PackageNameAndNameParamsNotGivenToWrap_pm");
	}

	public void testMoose_Exception_PackagesAndModulesAreNotCachable_pm()
	{
		doTest("Moose_Exception_PackagesAndModulesAreNotCachable_pm");
	}

	public void testMoose_Exception_ParameterIsNotSubtypeOfParent_pm()
	{
		doTest("Moose_Exception_ParameterIsNotSubtypeOfParent_pm");
	}

	public void testMoose_Exception_ReferencesAreNotAllowedAsDefault_pm()
	{
		doTest("Moose_Exception_ReferencesAreNotAllowedAsDefault_pm");
	}

	public void testMoose_Exception_RequiredAttributeLacksInitialization_pm()
	{
		doTest("Moose_Exception_RequiredAttributeLacksInitialization_pm");
	}

	public void testMoose_Exception_RequiredAttributeNeedsADefault_pm()
	{
		doTest("Moose_Exception_RequiredAttributeNeedsADefault_pm");
	}

	public void testMoose_Exception_RequiredMethodsImportedByClass_pm()
	{
		doTest("Moose_Exception_RequiredMethodsImportedByClass_pm");
	}

	public void testMoose_Exception_RequiredMethodsNotImplementedByClass_pm()
	{
		doTest("Moose_Exception_RequiredMethodsNotImplementedByClass_pm");
	}

	public void testMoose_Exception_RoleDoesTheExcludedRole_pm()
	{
		doTest("Moose_Exception_RoleDoesTheExcludedRole_pm");
	}

	public void testMoose_Exception_RoleExclusionConflict_pm()
	{
		doTest("Moose_Exception_RoleExclusionConflict_pm");
	}

	public void testMoose_Exception_RoleNameRequiredForMooseMetaRole_pm()
	{
		doTest("Moose_Exception_RoleNameRequiredForMooseMetaRole_pm");
	}

	public void testMoose_Exception_RoleNameRequired_pm()
	{
		doTest("Moose_Exception_RoleNameRequired_pm");
	}

	public void testMoose_Exception_Role_AttributeName_pm()
	{
		doTest("Moose_Exception_Role_AttributeName_pm");
	}

	public void testMoose_Exception_Role_Attribute_pm()
	{
		doTest("Moose_Exception_Role_Attribute_pm");
	}

	public void testMoose_Exception_Role_Class_pm()
	{
		doTest("Moose_Exception_Role_Class_pm");
	}

	public void testMoose_Exception_Role_EitherAttributeOrAttributeName_pm()
	{
		doTest("Moose_Exception_Role_EitherAttributeOrAttributeName_pm");
	}

	public void testMoose_Exception_Role_InstanceClass_pm()
	{
		doTest("Moose_Exception_Role_InstanceClass_pm");
	}

	public void testMoose_Exception_Role_Instance_pm()
	{
		doTest("Moose_Exception_Role_Instance_pm");
	}

	public void testMoose_Exception_Role_InvalidAttributeOptions_pm()
	{
		doTest("Moose_Exception_Role_InvalidAttributeOptions_pm");
	}

	public void testMoose_Exception_Role_Method_pm()
	{
		doTest("Moose_Exception_Role_Method_pm");
	}

	public void testMoose_Exception_Role_ParamsHash_pm()
	{
		doTest("Moose_Exception_Role_ParamsHash_pm");
	}

	public void testMoose_Exception_Role_RoleForCreateMOPClass_pm()
	{
		doTest("Moose_Exception_Role_RoleForCreateMOPClass_pm");
	}

	public void testMoose_Exception_Role_RoleForCreate_pm()
	{
		doTest("Moose_Exception_Role_RoleForCreate_pm");
	}

	public void testMoose_Exception_Role_Role_pm()
	{
		doTest("Moose_Exception_Role_Role_pm");
	}

	public void testMoose_Exception_Role_TypeConstraint_pm()
	{
		doTest("Moose_Exception_Role_TypeConstraint_pm");
	}

	public void testMoose_Exception_RolesDoNotSupportAugment_pm()
	{
		doTest("Moose_Exception_RolesDoNotSupportAugment_pm");
	}

	public void testMoose_Exception_RolesDoNotSupportExtends_pm()
	{
		doTest("Moose_Exception_RolesDoNotSupportExtends_pm");
	}

	public void testMoose_Exception_RolesDoNotSupportInner_pm()
	{
		doTest("Moose_Exception_RolesDoNotSupportInner_pm");
	}

	public void testMoose_Exception_RolesDoNotSupportRegexReferencesForMethodModifiers_pm()
	{
		doTest("Moose_Exception_RolesDoNotSupportRegexReferencesForMethodModifiers_pm");
	}

	public void testMoose_Exception_RolesInCreateTakesAnArrayRef_pm()
	{
		doTest("Moose_Exception_RolesInCreateTakesAnArrayRef_pm");
	}

	public void testMoose_Exception_RolesListMustBeInstancesOfMooseMetaRole_pm()
	{
		doTest("Moose_Exception_RolesListMustBeInstancesOfMooseMetaRole_pm");
	}

	public void testMoose_Exception_SingleParamsToNewMustBeHashRef_pm()
	{
		doTest("Moose_Exception_SingleParamsToNewMustBeHashRef_pm");
	}

	public void testMoose_Exception_TriggerMustBeACodeRef_pm()
	{
		doTest("Moose_Exception_TriggerMustBeACodeRef_pm");
	}

	public void testMoose_Exception_TypeConstraintCannotBeUsedForAParameterizableType_pm()
	{
		doTest("Moose_Exception_TypeConstraintCannotBeUsedForAParameterizableType_pm");
	}

	public void testMoose_Exception_TypeConstraintIsAlreadyCreated_pm()
	{
		doTest("Moose_Exception_TypeConstraintIsAlreadyCreated_pm");
	}

	public void testMoose_Exception_TypeParameterMustBeMooseMetaType_pm()
	{
		doTest("Moose_Exception_TypeParameterMustBeMooseMetaType_pm");
	}

	public void testMoose_Exception_UnableToCanonicalizeHandles_pm()
	{
		doTest("Moose_Exception_UnableToCanonicalizeHandles_pm");
	}

	public void testMoose_Exception_UnableToCanonicalizeNonRolePackage_pm()
	{
		doTest("Moose_Exception_UnableToCanonicalizeNonRolePackage_pm");
	}

	public void testMoose_Exception_UnableToRecognizeDelegateMetaclass_pm()
	{
		doTest("Moose_Exception_UnableToRecognizeDelegateMetaclass_pm");
	}

	public void testMoose_Exception_UndefinedHashKeysPassedToMethod_pm()
	{
		doTest("Moose_Exception_UndefinedHashKeysPassedToMethod_pm");
	}

	public void testMoose_Exception_UnionCalledWithAnArrayRefAndAdditionalArgs_pm()
	{
		doTest("Moose_Exception_UnionCalledWithAnArrayRefAndAdditionalArgs_pm");
	}

	public void testMoose_Exception_UnionTakesAtleastTwoTypeNames_pm()
	{
		doTest("Moose_Exception_UnionTakesAtleastTwoTypeNames_pm");
	}

	public void testMoose_Exception_ValidationFailedForInlineTypeConstraint_pm()
	{
		doTest("Moose_Exception_ValidationFailedForInlineTypeConstraint_pm");
	}

	public void testMoose_Exception_ValidationFailedForTypeConstraint_pm()
	{
		doTest("Moose_Exception_ValidationFailedForTypeConstraint_pm");
	}

	public void testMoose_Exception_WrapTakesACodeRefToBless_pm()
	{
		doTest("Moose_Exception_WrapTakesACodeRefToBless_pm");
	}

	public void testMoose_Exception_WrongTypeConstraintGiven_pm()
	{
		doTest("Moose_Exception_WrongTypeConstraintGiven_pm");
	}

	public void testMoose_Exception_pm()
	{
		doTest("Moose_Exception_pm");
	}

	public void testMoose_Exporter_pm()
	{
		doTest("Moose_Exporter_pm");
	}

	public void testMoose_Meta_Attribute_Native_Trait_Array_pm()
	{
		doTest("Moose_Meta_Attribute_Native_Trait_Array_pm");
	}

	public void testMoose_Meta_Attribute_Native_Trait_Bool_pm()
	{
		doTest("Moose_Meta_Attribute_Native_Trait_Bool_pm");
	}

	public void testMoose_Meta_Attribute_Native_Trait_Code_pm()
	{
		doTest("Moose_Meta_Attribute_Native_Trait_Code_pm");
	}

	public void testMoose_Meta_Attribute_Native_Trait_Counter_pm()
	{
		doTest("Moose_Meta_Attribute_Native_Trait_Counter_pm");
	}

	public void testMoose_Meta_Attribute_Native_Trait_Hash_pm()
	{
		doTest("Moose_Meta_Attribute_Native_Trait_Hash_pm");
	}

	public void testMoose_Meta_Attribute_Native_Trait_Number_pm()
	{
		doTest("Moose_Meta_Attribute_Native_Trait_Number_pm");
	}

	public void testMoose_Meta_Attribute_Native_Trait_String_pm()
	{
		doTest("Moose_Meta_Attribute_Native_Trait_String_pm");
	}

	public void testMoose_Meta_Attribute_Native_Trait_pm()
	{
		doTest("Moose_Meta_Attribute_Native_Trait_pm");
	}

	public void testMoose_Meta_Attribute_Native_pm()
	{
		doTest("Moose_Meta_Attribute_Native_pm");
	}

	public void testMoose_Meta_Attribute_pm()
	{
		doTest("Moose_Meta_Attribute_pm");
	}

	public void testMoose_Meta_Class_Immutable_Trait_pm()
	{
		doTest("Moose_Meta_Class_Immutable_Trait_pm");
	}

	public void testMoose_Meta_Class_pm()
	{
		doTest("Moose_Meta_Class_pm");
	}

	public void testMoose_Meta_Instance_pm()
	{
		doTest("Moose_Meta_Instance_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Array_Writer_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Array_Writer_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Array_accessor_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Array_accessor_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Array_clear_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Array_clear_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Array_count_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Array_count_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Array_delete_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Array_delete_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Array_elements_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Array_elements_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Array_first_index_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Array_first_index_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Array_first_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Array_first_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Array_get_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Array_get_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Array_grep_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Array_grep_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Array_insert_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Array_insert_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Array_is_empty_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Array_is_empty_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Array_join_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Array_join_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Array_map_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Array_map_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Array_natatime_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Array_natatime_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Array_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Array_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Array_pop_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Array_pop_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Array_push_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Array_push_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Array_reduce_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Array_reduce_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Array_set_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Array_set_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Array_shallow_clone_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Array_shallow_clone_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Array_shift_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Array_shift_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Array_shuffle_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Array_shuffle_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Array_sort_in_place_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Array_sort_in_place_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Array_sort_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Array_sort_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Array_splice_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Array_splice_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Array_uniq_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Array_uniq_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Array_unshift_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Array_unshift_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Bool_not_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Bool_not_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Bool_set_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Bool_set_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Bool_toggle_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Bool_toggle_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Bool_unset_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Bool_unset_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Code_execute_method_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Code_execute_method_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Code_execute_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Code_execute_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Collection_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Collection_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Counter_Writer_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Counter_Writer_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Counter_dec_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Counter_dec_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Counter_inc_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Counter_inc_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Counter_reset_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Counter_reset_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Counter_set_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Counter_set_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Hash_Writer_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Hash_Writer_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Hash_accessor_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Hash_accessor_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Hash_clear_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Hash_clear_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Hash_count_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Hash_count_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Hash_defined_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Hash_defined_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Hash_delete_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Hash_delete_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Hash_elements_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Hash_elements_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Hash_exists_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Hash_exists_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Hash_get_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Hash_get_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Hash_is_empty_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Hash_is_empty_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Hash_keys_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Hash_keys_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Hash_kv_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Hash_kv_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Hash_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Hash_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Hash_set_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Hash_set_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Hash_shallow_clone_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Hash_shallow_clone_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Hash_values_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Hash_values_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Number_abs_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Number_abs_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Number_add_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Number_add_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Number_div_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Number_div_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Number_mod_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Number_mod_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Number_mul_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Number_mul_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Number_set_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Number_set_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Number_sub_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Number_sub_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Reader_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Reader_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_String_append_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_String_append_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_String_chomp_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_String_chomp_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_String_chop_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_String_chop_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_String_clear_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_String_clear_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_String_inc_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_String_inc_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_String_length_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_String_length_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_String_match_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_String_match_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_String_prepend_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_String_prepend_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_String_replace_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_String_replace_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_String_substr_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_String_substr_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_Writer_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_Writer_pm");
	}

	public void testMoose_Meta_Method_Accessor_Native_pm()
	{
		doTest("Moose_Meta_Method_Accessor_Native_pm");
	}

	public void testMoose_Meta_Method_Accessor_pm()
	{
		doTest("Moose_Meta_Method_Accessor_pm");
	}

	public void testMoose_Meta_Method_Augmented_pm()
	{
		doTest("Moose_Meta_Method_Augmented_pm");
	}

	public void testMoose_Meta_Method_Constructor_pm()
	{
		doTest("Moose_Meta_Method_Constructor_pm");
	}

	public void testMoose_Meta_Method_Delegation_pm()
	{
		doTest("Moose_Meta_Method_Delegation_pm");
	}

	public void testMoose_Meta_Method_Destructor_pm()
	{
		doTest("Moose_Meta_Method_Destructor_pm");
	}

	public void testMoose_Meta_Method_Meta_pm()
	{
		doTest("Moose_Meta_Method_Meta_pm");
	}

	public void testMoose_Meta_Method_Overridden_pm()
	{
		doTest("Moose_Meta_Method_Overridden_pm");
	}

	public void testMoose_Meta_Method_pm()
	{
		doTest("Moose_Meta_Method_pm");
	}

	public void testMoose_Meta_Mixin_AttributeCore_pm()
	{
		doTest("Moose_Meta_Mixin_AttributeCore_pm");
	}

	public void testMoose_Meta_Object_Trait_pm()
	{
		doTest("Moose_Meta_Object_Trait_pm");
	}

	public void testMoose_Meta_Role_Application_RoleSummation_pm()
	{
		doTest("Moose_Meta_Role_Application_RoleSummation_pm");
	}

	public void testMoose_Meta_Role_Application_ToClass_pm()
	{
		doTest("Moose_Meta_Role_Application_ToClass_pm");
	}

	public void testMoose_Meta_Role_Application_ToInstance_pm()
	{
		doTest("Moose_Meta_Role_Application_ToInstance_pm");
	}

	public void testMoose_Meta_Role_Application_ToRole_pm()
	{
		doTest("Moose_Meta_Role_Application_ToRole_pm");
	}

	public void testMoose_Meta_Role_Application_pm()
	{
		doTest("Moose_Meta_Role_Application_pm");
	}

	public void testMoose_Meta_Role_Attribute_pm()
	{
		doTest("Moose_Meta_Role_Attribute_pm");
	}

	public void testMoose_Meta_Role_Composite_pm()
	{
		doTest("Moose_Meta_Role_Composite_pm");
	}

	public void testMoose_Meta_Role_Method_Conflicting_pm()
	{
		doTest("Moose_Meta_Role_Method_Conflicting_pm");
	}

	public void testMoose_Meta_Role_Method_Required_pm()
	{
		doTest("Moose_Meta_Role_Method_Required_pm");
	}

	public void testMoose_Meta_Role_Method_pm()
	{
		doTest("Moose_Meta_Role_Method_pm");
	}

	public void testMoose_Meta_Role_pm()
	{
		doTest("Moose_Meta_Role_pm");
	}

	public void testMoose_Meta_TypeCoercion_Union_pm()
	{
		doTest("Moose_Meta_TypeCoercion_Union_pm");
	}

	public void testMoose_Meta_TypeCoercion_pm()
	{
		doTest("Moose_Meta_TypeCoercion_pm");
	}

	public void testMoose_Meta_TypeConstraint_Class_pm()
	{
		doTest("Moose_Meta_TypeConstraint_Class_pm");
	}

	public void testMoose_Meta_TypeConstraint_DuckType_pm()
	{
		doTest("Moose_Meta_TypeConstraint_DuckType_pm");
	}

	public void testMoose_Meta_TypeConstraint_Enum_pm()
	{
		doTest("Moose_Meta_TypeConstraint_Enum_pm");
	}

	public void testMoose_Meta_TypeConstraint_Parameterizable_pm()
	{
		doTest("Moose_Meta_TypeConstraint_Parameterizable_pm");
	}

	public void testMoose_Meta_TypeConstraint_Parameterized_pm()
	{
		doTest("Moose_Meta_TypeConstraint_Parameterized_pm");
	}

	public void testMoose_Meta_TypeConstraint_Registry_pm()
	{
		doTest("Moose_Meta_TypeConstraint_Registry_pm");
	}

	public void testMoose_Meta_TypeConstraint_Role_pm()
	{
		doTest("Moose_Meta_TypeConstraint_Role_pm");
	}

	public void testMoose_Meta_TypeConstraint_Union_pm()
	{
		doTest("Moose_Meta_TypeConstraint_Union_pm");
	}

	public void testMoose_Meta_TypeConstraint_pm()
	{
		doTest("Moose_Meta_TypeConstraint_pm");
	}

	public void testMoose_Object_pm()
	{
		doTest("Moose_Object_pm");
	}

	public void testMoose_Role_pm()
	{
		doTest("Moose_Role_pm");
	}

	public void testMoose_Util_MetaRole_pm()
	{
		doTest("Moose_Util_MetaRole_pm");
	}

	public void testMoose_Util_TypeConstraints_Builtins_pm()
	{
		doTest("Moose_Util_TypeConstraints_Builtins_pm");
	}

	public void testMoose_Util_TypeConstraints_pm()
	{
		doTest("Moose_Util_TypeConstraints_pm");
	}

	public void testMoose_Util_pm()
	{
		doTest("Moose_Util_pm");
	}

	public void testMoose_pm()
	{
		doTest("Moose_pm");
	}

	public void testMozilla_CA_pm()
	{
		doTest("Mozilla_CA_pm");
	}

	public void testMozilla_mk_ca_bundle_pl()
	{
		doTest("Mozilla_mk_ca_bundle_pl");
	}

	public void testNDBM_File_pm()
	{
		doTest("NDBM_File_pm");
	}

	public void testNEXT_pm()
	{
		doTest("NEXT_pm");
	}

	public void testNet_Cmd_pm()
	{
		doTest("Net_Cmd_pm");
	}

	public void testNet_Config_pm()
	{
		doTest("Net_Config_pm");
	}

	public void testNet_DNS_DomainName_pm()
	{
		doTest("Net_DNS_DomainName_pm");
	}

	public void testNet_DNS_Domain_pm()
	{
		doTest("Net_DNS_Domain_pm");
	}

	public void testNet_DNS_Header_pm()
	{
		doTest("Net_DNS_Header_pm");
	}

	public void testNet_DNS_Mailbox_pm()
	{
		doTest("Net_DNS_Mailbox_pm");
	}

	public void testNet_DNS_Nameserver_pm()
	{
		doTest("Net_DNS_Nameserver_pm");
	}

	public void testNet_DNS_Packet_pm()
	{
		doTest("Net_DNS_Packet_pm");
	}

	public void testNet_DNS_Parameters_pm()
	{
		doTest("Net_DNS_Parameters_pm");
	}

	public void testNet_DNS_Question_pm()
	{
		doTest("Net_DNS_Question_pm");
	}

	public void testNet_DNS_RR_AAAA_pm()
	{
		doTest("Net_DNS_RR_AAAA_pm");
	}

	public void testNet_DNS_RR_AFSDB_pm()
	{
		doTest("Net_DNS_RR_AFSDB_pm");
	}

	public void testNet_DNS_RR_APL_pm()
	{
		doTest("Net_DNS_RR_APL_pm");
	}

	public void testNet_DNS_RR_A_pm()
	{
		doTest("Net_DNS_RR_A_pm");
	}

	public void testNet_DNS_RR_CAA_pm()
	{
		doTest("Net_DNS_RR_CAA_pm");
	}

	public void testNet_DNS_RR_CERT_pm()
	{
		doTest("Net_DNS_RR_CERT_pm");
	}

	public void testNet_DNS_RR_CNAME_pm()
	{
		doTest("Net_DNS_RR_CNAME_pm");
	}

	public void testNet_DNS_RR_CSYNC_pm()
	{
		doTest("Net_DNS_RR_CSYNC_pm");
	}

	public void testNet_DNS_RR_DHCID_pm()
	{
		doTest("Net_DNS_RR_DHCID_pm");
	}

	public void testNet_DNS_RR_DNAME_pm()
	{
		doTest("Net_DNS_RR_DNAME_pm");
	}

	public void testNet_DNS_RR_EUI48_pm()
	{
		doTest("Net_DNS_RR_EUI48_pm");
	}

	public void testNet_DNS_RR_EUI64_pm()
	{
		doTest("Net_DNS_RR_EUI64_pm");
	}

	public void testNet_DNS_RR_GPOS_pm()
	{
		doTest("Net_DNS_RR_GPOS_pm");
	}

	public void testNet_DNS_RR_HINFO_pm()
	{
		doTest("Net_DNS_RR_HINFO_pm");
	}

	public void testNet_DNS_RR_HIP_pm()
	{
		doTest("Net_DNS_RR_HIP_pm");
	}

	public void testNet_DNS_RR_IPSECKEY_pm()
	{
		doTest("Net_DNS_RR_IPSECKEY_pm");
	}

	public void testNet_DNS_RR_ISDN_pm()
	{
		doTest("Net_DNS_RR_ISDN_pm");
	}

	public void testNet_DNS_RR_KX_pm()
	{
		doTest("Net_DNS_RR_KX_pm");
	}

	public void testNet_DNS_RR_L32_pm()
	{
		doTest("Net_DNS_RR_L32_pm");
	}

	public void testNet_DNS_RR_L64_pm()
	{
		doTest("Net_DNS_RR_L64_pm");
	}

	public void testNet_DNS_RR_LOC_pm()
	{
		doTest("Net_DNS_RR_LOC_pm");
	}

	public void testNet_DNS_RR_LP_pm()
	{
		doTest("Net_DNS_RR_LP_pm");
	}

	public void testNet_DNS_RR_MB_pm()
	{
		doTest("Net_DNS_RR_MB_pm");
	}

	public void testNet_DNS_RR_MG_pm()
	{
		doTest("Net_DNS_RR_MG_pm");
	}

	public void testNet_DNS_RR_MINFO_pm()
	{
		doTest("Net_DNS_RR_MINFO_pm");
	}

	public void testNet_DNS_RR_MR_pm()
	{
		doTest("Net_DNS_RR_MR_pm");
	}

	public void testNet_DNS_RR_MX_pm()
	{
		doTest("Net_DNS_RR_MX_pm");
	}

	public void testNet_DNS_RR_NAPTR_pm()
	{
		doTest("Net_DNS_RR_NAPTR_pm");
	}

	public void testNet_DNS_RR_NID_pm()
	{
		doTest("Net_DNS_RR_NID_pm");
	}

	public void testNet_DNS_RR_NS_pm()
	{
		doTest("Net_DNS_RR_NS_pm");
	}

	public void testNet_DNS_RR_NULL_pm()
	{
		doTest("Net_DNS_RR_NULL_pm");
	}

	public void testNet_DNS_RR_OPENPGPKEY_pm()
	{
		doTest("Net_DNS_RR_OPENPGPKEY_pm");
	}

	public void testNet_DNS_RR_OPT_pm()
	{
		doTest("Net_DNS_RR_OPT_pm");
	}

	public void testNet_DNS_RR_PTR_pm()
	{
		doTest("Net_DNS_RR_PTR_pm");
	}

	public void testNet_DNS_RR_PX_pm()
	{
		doTest("Net_DNS_RR_PX_pm");
	}

	public void testNet_DNS_RR_RP_pm()
	{
		doTest("Net_DNS_RR_RP_pm");
	}

	public void testNet_DNS_RR_RT_pm()
	{
		doTest("Net_DNS_RR_RT_pm");
	}

	public void testNet_DNS_RR_SOA_pm()
	{
		doTest("Net_DNS_RR_SOA_pm");
	}

	public void testNet_DNS_RR_SPF_pm()
	{
		doTest("Net_DNS_RR_SPF_pm");
	}

	public void testNet_DNS_RR_SRV_pm()
	{
		doTest("Net_DNS_RR_SRV_pm");
	}

	public void testNet_DNS_RR_SSHFP_pm()
	{
		doTest("Net_DNS_RR_SSHFP_pm");
	}

	public void testNet_DNS_RR_TKEY_pm()
	{
		doTest("Net_DNS_RR_TKEY_pm");
	}

	public void testNet_DNS_RR_TLSA_pm()
	{
		doTest("Net_DNS_RR_TLSA_pm");
	}

	public void testNet_DNS_RR_TSIG_pm()
	{
		doTest("Net_DNS_RR_TSIG_pm");
	}

	public void testNet_DNS_RR_TXT_pm()
	{
		doTest("Net_DNS_RR_TXT_pm");
	}

	public void testNet_DNS_RR_X25_pm()
	{
		doTest("Net_DNS_RR_X25_pm");
	}

	public void testNet_DNS_RR_pm()
	{
		doTest("Net_DNS_RR_pm");
	}

	public void testNet_DNS_Resolver_Base_pm()
	{
		doTest("Net_DNS_Resolver_Base_pm");
	}

	public void testNet_DNS_Resolver_MSWin32_pm()
	{
		doTest("Net_DNS_Resolver_MSWin32_pm");
	}

	public void testNet_DNS_Resolver_Recurse_pm()
	{
		doTest("Net_DNS_Resolver_Recurse_pm");
	}

	public void testNet_DNS_Resolver_UNIX_pm()
	{
		doTest("Net_DNS_Resolver_UNIX_pm");
	}

	public void testNet_DNS_Resolver_android_pm()
	{
		doTest("Net_DNS_Resolver_android_pm");
	}

	public void testNet_DNS_Resolver_cygwin_pm()
	{
		doTest("Net_DNS_Resolver_cygwin_pm");
	}

	public void testNet_DNS_Resolver_os2_pm()
	{
		doTest("Net_DNS_Resolver_os2_pm");
	}

	public void testNet_DNS_Resolver_pm()
	{
		doTest("Net_DNS_Resolver_pm");
	}

	public void testNet_DNS_Text_pm()
	{
		doTest("Net_DNS_Text_pm");
	}

	public void testNet_DNS_Update_pm()
	{
		doTest("Net_DNS_Update_pm");
	}

	public void testNet_DNS_ZoneFile_pm()
	{
		doTest("Net_DNS_ZoneFile_pm");
	}

	public void testNet_DNS_pm()
	{
		doTest("Net_DNS_pm");
	}

	public void testNet_Domain_pm()
	{
		doTest("Net_Domain_pm");
	}

	public void testNet_FTP_A_pm()
	{
		doTest("Net_FTP_A_pm");
	}

	public void testNet_FTP_E_pm()
	{
		doTest("Net_FTP_E_pm");
	}

	public void testNet_FTP_I_pm()
	{
		doTest("Net_FTP_I_pm");
	}

	public void testNet_FTP_L_pm()
	{
		doTest("Net_FTP_L_pm");
	}

	public void testNet_FTP_dataconn_pm()
	{
		doTest("Net_FTP_dataconn_pm");
	}

	public void testNet_FTP_pm()
	{
		doTest("Net_FTP_pm");
	}

	public void testNet_HTTPS_pm()
	{
		doTest("Net_HTTPS_pm");
	}

	public void testNet_HTTP_Methods_pm()
	{
		doTest("Net_HTTP_Methods_pm");
	}

	public void testNet_HTTP_NB_pm()
	{
		doTest("Net_HTTP_NB_pm");
	}

	public void testNet_HTTP_pm()
	{
		doTest("Net_HTTP_pm");
	}

	public void testNet_IMAP_Client_MsgAddress_pm()
	{
		doTest("Net_IMAP_Client_MsgAddress_pm");
	}

	public void testNet_IMAP_Client_MsgSummary_pm()
	{
		doTest("Net_IMAP_Client_MsgSummary_pm");
	}

	public void testNet_IMAP_Client_pm()
	{
		doTest("Net_IMAP_Client_pm");
	}

	public void testNet_NNTP_pm()
	{
		doTest("Net_NNTP_pm");
	}

	public void testNet_Netrc_pm()
	{
		doTest("Net_Netrc_pm");
	}

	public void testNet_POP3_pm()
	{
		doTest("Net_POP3_pm");
	}

	public void testNet_Ping_pm()
	{
		doTest("Net_Ping_pm");
	}

	public void testNet_SMTPS_pm()
	{
		doTest("Net_SMTPS_pm");
	}

	public void testNet_SMTP_pm()
	{
		doTest("Net_SMTP_pm");
	}

	public void testNet_SSH2_Channel_pm()
	{
		doTest("Net_SSH2_Channel_pm");
	}

	public void testNet_SSH2_Dir_pm()
	{
		doTest("Net_SSH2_Dir_pm");
	}

	public void testNet_SSH2_File_pm()
	{
		doTest("Net_SSH2_File_pm");
	}

	public void testNet_SSH2_Listener_pm()
	{
		doTest("Net_SSH2_Listener_pm");
	}

	public void testNet_SSH2_PublicKey_pm()
	{
		doTest("Net_SSH2_PublicKey_pm");
	}

	public void testNet_SSH2_SFTP_pm()
	{
		doTest("Net_SSH2_SFTP_pm");
	}

	public void testNet_SSH2_pm()
	{
		doTest("Net_SSH2_pm");
	}

	public void testNet_SSL_pm()
	{
		doTest("Net_SSL_pm");
	}

	public void testNet_SSLeay_Handle_pm()
	{
		doTest("Net_SSLeay_Handle_pm");
	}

	public void testNet_SSLeay_pm()
	{
		doTest("Net_SSLeay_pm");
	}

	public void testNet_Telnet_pm()
	{
		doTest("Net_Telnet_pm");
	}

	public void testNet_Time_pm()
	{
		doTest("Net_Time_pm");
	}

	public void testNet_hostent_pm()
	{
		doTest("Net_hostent_pm");
	}

	public void testNet_netent_pm()
	{
		doTest("Net_netent_pm");
	}

	public void testNet_protoent_pm()
	{
		doTest("Net_protoent_pm");
	}

	public void testNet_servent_pm()
	{
		doTest("Net_servent_pm");
	}

	public void testNumber_Compare_pm()
	{
		doTest("Number_Compare_pm");
	}

	public void testODBM_File_pm()
	{
		doTest("ODBM_File_pm");
	}

	public void testOLE_Storage_Lite_pm()
	{
		doTest("OLE_Storage_Lite_pm");
	}

	public void testOLE_pm()
	{
		doTest("OLE_pm");
	}

	public void testO_pm()
	{
		doTest("O_pm");
	}

	public void testObject_Accessor_pm()
	{
		doTest("Object_Accessor_pm");
	}

	public void testObject_Tiny_pm()
	{
		doTest("Object_Tiny_pm");
	}

	public void testOpcode_pm()
	{
		doTest("Opcode_pm");
	}

	public void testOpenGL_Config_pm()
	{
		doTest("OpenGL_Config_pm");
	}

	public void testOpenGL_pm()
	{
		doTest("OpenGL_pm");
	}

	public void testPAR_Dist_FromPPD_pm()
	{
		doTest("PAR_Dist_FromPPD_pm");
	}

	public void testPAR_Dist_InstallPPD_pm()
	{
		doTest("PAR_Dist_InstallPPD_pm");
	}

	public void testPAR_Dist_pm()
	{
		doTest("PAR_Dist_pm");
	}

	public void testPAR_Heavy_pm()
	{
		doTest("PAR_Heavy_pm");
	}

	public void testPAR_Repository_Client_DBM_pm()
	{
		doTest("PAR_Repository_Client_DBM_pm");
	}

	public void testPAR_Repository_Client_HTTP_pm()
	{
		doTest("PAR_Repository_Client_HTTP_pm");
	}

	public void testPAR_Repository_Client_Local_pm()
	{
		doTest("PAR_Repository_Client_Local_pm");
	}

	public void testPAR_Repository_Client_Util_pm()
	{
		doTest("PAR_Repository_Client_Util_pm");
	}

	public void testPAR_Repository_Client_pm()
	{
		doTest("PAR_Repository_Client_pm");
	}

	public void testPAR_Repository_Query_pm()
	{
		doTest("PAR_Repository_Query_pm");
	}

	public void testPAR_SetupProgname_pm()
	{
		doTest("PAR_SetupProgname_pm");
	}

	public void testPAR_SetupTemp_pm()
	{
		doTest("PAR_SetupTemp_pm");
	}

	public void testPAR_pm()
	{
		doTest("PAR_pm");
	}

	public void testPOSIX_pm()
	{
		doTest("POSIX_pm");
	}

	public void testPPI_Cache_pm()
	{
		doTest("PPI_Cache_pm");
	}

	public void testPPI_Document_File_pm()
	{
		doTest("PPI_Document_File_pm");
	}

	public void testPPI_Document_Fragment_pm()
	{
		doTest("PPI_Document_Fragment_pm");
	}

	public void testPPI_Document_Normalized_pm()
	{
		doTest("PPI_Document_Normalized_pm");
	}

	public void testPPI_Document_pm()
	{
		doTest("PPI_Document_pm");
	}

	public void testPPI_Dumper_pm()
	{
		doTest("PPI_Dumper_pm");
	}

	public void testPPI_Element_pm()
	{
		doTest("PPI_Element_pm");
	}

	public void testPPI_Exception_ParserRejection_pm()
	{
		doTest("PPI_Exception_ParserRejection_pm");
	}

	public void testPPI_Exception_ParserTimeout_pm()
	{
		doTest("PPI_Exception_ParserTimeout_pm");
	}

	public void testPPI_Exception_pm()
	{
		doTest("PPI_Exception_pm");
	}

	public void testPPI_Find_pm()
	{
		doTest("PPI_Find_pm");
	}

	public void testPPI_Lexer_pm()
	{
		doTest("PPI_Lexer_pm");
	}

	public void testPPI_Node_pm()
	{
		doTest("PPI_Node_pm");
	}

	public void testPPI_Normal_Standard_pm()
	{
		doTest("PPI_Normal_Standard_pm");
	}

	public void testPPI_Normal_pm()
	{
		doTest("PPI_Normal_pm");
	}

	public void testPPI_Statement_Break_pm()
	{
		doTest("PPI_Statement_Break_pm");
	}

	public void testPPI_Statement_Compound_pm()
	{
		doTest("PPI_Statement_Compound_pm");
	}

	public void testPPI_Statement_Data_pm()
	{
		doTest("PPI_Statement_Data_pm");
	}

	public void testPPI_Statement_End_pm()
	{
		doTest("PPI_Statement_End_pm");
	}

	public void testPPI_Statement_Expression_pm()
	{
		doTest("PPI_Statement_Expression_pm");
	}

	public void testPPI_Statement_Given_pm()
	{
		doTest("PPI_Statement_Given_pm");
	}

	public void testPPI_Statement_Include_Perl6_pm()
	{
		doTest("PPI_Statement_Include_Perl6_pm");
	}

	public void testPPI_Statement_Include_pm()
	{
		doTest("PPI_Statement_Include_pm");
	}

	public void testPPI_Statement_Null_pm()
	{
		doTest("PPI_Statement_Null_pm");
	}

	public void testPPI_Statement_Package_pm()
	{
		doTest("PPI_Statement_Package_pm");
	}

	public void testPPI_Statement_Scheduled_pm()
	{
		doTest("PPI_Statement_Scheduled_pm");
	}

	public void testPPI_Statement_Sub_pm()
	{
		doTest("PPI_Statement_Sub_pm");
	}

	public void testPPI_Statement_Unknown_pm()
	{
		doTest("PPI_Statement_Unknown_pm");
	}

	public void testPPI_Statement_UnmatchedBrace_pm()
	{
		doTest("PPI_Statement_UnmatchedBrace_pm");
	}

	public void testPPI_Statement_Variable_pm()
	{
		doTest("PPI_Statement_Variable_pm");
	}

	public void testPPI_Statement_When_pm()
	{
		doTest("PPI_Statement_When_pm");
	}

	public void testPPI_Statement_pm()
	{
		doTest("PPI_Statement_pm");
	}

	public void testPPI_Structure_Block_pm()
	{
		doTest("PPI_Structure_Block_pm");
	}

	public void testPPI_Structure_Condition_pm()
	{
		doTest("PPI_Structure_Condition_pm");
	}

	public void testPPI_Structure_Constructor_pm()
	{
		doTest("PPI_Structure_Constructor_pm");
	}

	public void testPPI_Structure_For_pm()
	{
		doTest("PPI_Structure_For_pm");
	}

	public void testPPI_Structure_Given_pm()
	{
		doTest("PPI_Structure_Given_pm");
	}

	public void testPPI_Structure_List_pm()
	{
		doTest("PPI_Structure_List_pm");
	}

	public void testPPI_Structure_Subscript_pm()
	{
		doTest("PPI_Structure_Subscript_pm");
	}

	public void testPPI_Structure_Unknown_pm()
	{
		doTest("PPI_Structure_Unknown_pm");
	}

	public void testPPI_Structure_When_pm()
	{
		doTest("PPI_Structure_When_pm");
	}

	public void testPPI_Structure_pm()
	{
		doTest("PPI_Structure_pm");
	}

	public void testPPI_Token_ArrayIndex_pm()
	{
		doTest("PPI_Token_ArrayIndex_pm");
	}

	public void testPPI_Token_Attribute_pm()
	{
		doTest("PPI_Token_Attribute_pm");
	}

	public void testPPI_Token_BOM_pm()
	{
		doTest("PPI_Token_BOM_pm");
	}

	public void testPPI_Token_Cast_pm()
	{
		doTest("PPI_Token_Cast_pm");
	}

	public void testPPI_Token_Comment_pm()
	{
		doTest("PPI_Token_Comment_pm");
	}

	public void testPPI_Token_DashedWord_pm()
	{
		doTest("PPI_Token_DashedWord_pm");
	}

	public void testPPI_Token_Data_pm()
	{
		doTest("PPI_Token_Data_pm");
	}

	public void testPPI_Token_End_pm()
	{
		doTest("PPI_Token_End_pm");
	}

	public void testPPI_Token_HereDoc_pm()
	{
		doTest("PPI_Token_HereDoc_pm");
	}

	public void testPPI_Token_Label_pm()
	{
		doTest("PPI_Token_Label_pm");
	}

	public void testPPI_Token_Magic_pm()
	{
		doTest("PPI_Token_Magic_pm");
	}

	public void testPPI_Token_Number_Binary_pm()
	{
		doTest("PPI_Token_Number_Binary_pm");
	}

	public void testPPI_Token_Number_Exp_pm()
	{
		doTest("PPI_Token_Number_Exp_pm");
	}

	public void testPPI_Token_Number_Float_pm()
	{
		doTest("PPI_Token_Number_Float_pm");
	}

	public void testPPI_Token_Number_Hex_pm()
	{
		doTest("PPI_Token_Number_Hex_pm");
	}

	public void testPPI_Token_Number_Octal_pm()
	{
		doTest("PPI_Token_Number_Octal_pm");
	}

	public void testPPI_Token_Number_Version_pm()
	{
		doTest("PPI_Token_Number_Version_pm");
	}

	public void testPPI_Token_Number_pm()
	{
		doTest("PPI_Token_Number_pm");
	}

	public void testPPI_Token_Operator_pm()
	{
		doTest("PPI_Token_Operator_pm");
	}

	public void testPPI_Token_Pod_pm()
	{
		doTest("PPI_Token_Pod_pm");
	}

	public void testPPI_Token_Prototype_pm()
	{
		doTest("PPI_Token_Prototype_pm");
	}

	public void testPPI_Token_QuoteLike_Backtick_pm()
	{
		doTest("PPI_Token_QuoteLike_Backtick_pm");
	}

	public void testPPI_Token_QuoteLike_Command_pm()
	{
		doTest("PPI_Token_QuoteLike_Command_pm");
	}

	public void testPPI_Token_QuoteLike_Readline_pm()
	{
		doTest("PPI_Token_QuoteLike_Readline_pm");
	}

	public void testPPI_Token_QuoteLike_Regexp_pm()
	{
		doTest("PPI_Token_QuoteLike_Regexp_pm");
	}

	public void testPPI_Token_QuoteLike_Words_pm()
	{
		doTest("PPI_Token_QuoteLike_Words_pm");
	}

	public void testPPI_Token_QuoteLike_pm()
	{
		doTest("PPI_Token_QuoteLike_pm");
	}

	public void testPPI_Token_Quote_Double_pm()
	{
		doTest("PPI_Token_Quote_Double_pm");
	}

	public void testPPI_Token_Quote_Interpolate_pm()
	{
		doTest("PPI_Token_Quote_Interpolate_pm");
	}

	public void testPPI_Token_Quote_Literal_pm()
	{
		doTest("PPI_Token_Quote_Literal_pm");
	}

	public void testPPI_Token_Quote_Single_pm()
	{
		doTest("PPI_Token_Quote_Single_pm");
	}

	public void testPPI_Token_Quote_pm()
	{
		doTest("PPI_Token_Quote_pm");
	}

	public void testPPI_Token_Regexp_Match_pm()
	{
		doTest("PPI_Token_Regexp_Match_pm");
	}

	public void testPPI_Token_Regexp_Substitute_pm()
	{
		doTest("PPI_Token_Regexp_Substitute_pm");
	}

	public void testPPI_Token_Regexp_Transliterate_pm()
	{
		doTest("PPI_Token_Regexp_Transliterate_pm");
	}

	public void testPPI_Token_Regexp_pm()
	{
		doTest("PPI_Token_Regexp_pm");
	}

	public void testPPI_Token_Separator_pm()
	{
		doTest("PPI_Token_Separator_pm");
	}

	public void testPPI_Token_Structure_pm()
	{
		doTest("PPI_Token_Structure_pm");
	}

	public void testPPI_Token_Symbol_pm()
	{
		doTest("PPI_Token_Symbol_pm");
	}

	public void testPPI_Token_Unknown_pm()
	{
		doTest("PPI_Token_Unknown_pm");
	}

	public void testPPI_Token_Whitespace_pm()
	{
		doTest("PPI_Token_Whitespace_pm");
	}

	public void testPPI_Token_Word_pm()
	{
		doTest("PPI_Token_Word_pm");
	}

	public void testPPI_Token__QuoteEngine_Full_pm()
	{
		doTest("PPI_Token__QuoteEngine_Full_pm");
	}

	public void testPPI_Token__QuoteEngine_Simple_pm()
	{
		doTest("PPI_Token__QuoteEngine_Simple_pm");
	}

	public void testPPI_Token__QuoteEngine_pm()
	{
		doTest("PPI_Token__QuoteEngine_pm");
	}

	public void testPPI_Token_pm()
	{
		doTest("PPI_Token_pm");
	}

	public void testPPI_Tokenizer_pm()
	{
		doTest("PPI_Tokenizer_pm");
	}

	public void testPPI_Transform_UpdateCopyright_pm()
	{
		doTest("PPI_Transform_UpdateCopyright_pm");
	}

	public void testPPI_Transform_pm()
	{
		doTest("PPI_Transform_pm");
	}

	public void testPPI_Util_pm()
	{
		doTest("PPI_Util_pm");
	}

	public void testPPI_XSAccessor_pm()
	{
		doTest("PPI_XSAccessor_pm");
	}

	public void testPPI_pm()
	{
		doTest("PPI_pm");
	}

	public void testPPM_RelocPerl_pm()
	{
		doTest("PPM_RelocPerl_pm");
	}

	public void testPPM_XML_Element_pm()
	{
		doTest("PPM_XML_Element_pm");
	}

	public void testPPM_XML_PPD_pm()
	{
		doTest("PPM_XML_PPD_pm");
	}

	public void testPPM_XML_PPMConfig_pm()
	{
		doTest("PPM_XML_PPMConfig_pm");
	}

	public void testPPM_XML_RepositorySummary_pm()
	{
		doTest("PPM_XML_RepositorySummary_pm");
	}

	public void testPPM_XML_ValidatingElement_pm()
	{
		doTest("PPM_XML_ValidatingElement_pm");
	}

	public void testPPM_pm()
	{
		doTest("PPM_pm");
	}

	public void testPackage_Constants_pm()
	{
		doTest("Package_Constants_pm");
	}

	public void testPackage_DeprecationManager_pm()
	{
		doTest("Package_DeprecationManager_pm");
	}

	public void testPackage_Stash_Conflicts_pm()
	{
		doTest("Package_Stash_Conflicts_pm");
	}

	public void testPackage_Stash_PP_pm()
	{
		doTest("Package_Stash_PP_pm");
	}

	public void testPackage_Stash_XS_pm()
	{
		doTest("Package_Stash_XS_pm");
	}

	public void testPackage_Stash_pm()
	{
		doTest("Package_Stash_pm");
	}

	public void testParams_Check_pm()
	{
		doTest("Params_Check_pm");
	}

	public void testParams_Util_pm()
	{
		doTest("Params_Util_pm");
	}

	public void testParams_ValidatePP_pm()
	{
		doTest("Params_ValidatePP_pm");
	}

	public void testParams_ValidateXS_pm()
	{
		doTest("Params_ValidateXS_pm");
	}

	public void testParams_Validate_Constants_pm()
	{
		doTest("Params_Validate_Constants_pm");
	}

	public void testParams_Validate_PP_pm()
	{
		doTest("Params_Validate_PP_pm");
	}

	public void testParams_Validate_XS_pm()
	{
		doTest("Params_Validate_XS_pm");
	}

	public void testParams_Validate_pm()
	{
		doTest("Params_Validate_pm");
	}

	public void testParse_CPAN_Meta_pm()
	{
		doTest("Parse_CPAN_Meta_pm");
	}

	public void testParse_Method_Signatures_ParamCollection_pm()
	{
		doTest("Parse_Method_Signatures_ParamCollection_pm");
	}

	public void testParse_Method_Signatures_Param_Bindable_pm()
	{
		doTest("Parse_Method_Signatures_Param_Bindable_pm");
	}

	public void testParse_Method_Signatures_Param_Named_pm()
	{
		doTest("Parse_Method_Signatures_Param_Named_pm");
	}

	public void testParse_Method_Signatures_Param_Placeholder_pm()
	{
		doTest("Parse_Method_Signatures_Param_Placeholder_pm");
	}

	public void testParse_Method_Signatures_Param_Positional_pm()
	{
		doTest("Parse_Method_Signatures_Param_Positional_pm");
	}

	public void testParse_Method_Signatures_Param_Unpacked_Array_pm()
	{
		doTest("Parse_Method_Signatures_Param_Unpacked_Array_pm");
	}

	public void testParse_Method_Signatures_Param_Unpacked_Hash_pm()
	{
		doTest("Parse_Method_Signatures_Param_Unpacked_Hash_pm");
	}

	public void testParse_Method_Signatures_Param_Unpacked_pm()
	{
		doTest("Parse_Method_Signatures_Param_Unpacked_pm");
	}

	public void testParse_Method_Signatures_Param_pm()
	{
		doTest("Parse_Method_Signatures_Param_pm");
	}

	public void testParse_Method_Signatures_Sig_pm()
	{
		doTest("Parse_Method_Signatures_Sig_pm");
	}

	public void testParse_Method_Signatures_TypeConstraint_pm()
	{
		doTest("Parse_Method_Signatures_TypeConstraint_pm");
	}

	public void testParse_Method_Signatures_Types_pm()
	{
		doTest("Parse_Method_Signatures_Types_pm");
	}

	public void testParse_Method_Signatures_pm()
	{
		doTest("Parse_Method_Signatures_pm");
	}

	public void testParse_RecDescent_pm()
	{
		doTest("Parse_RecDescent_pm");
	}

	public void testPath_Class_Dir_pm()
	{
		doTest("Path_Class_Dir_pm");
	}

	public void testPath_Class_Entity_pm()
	{
		doTest("Path_Class_Entity_pm");
	}

	public void testPath_Class_File_pm()
	{
		doTest("Path_Class_File_pm");
	}

	public void testPath_Class_pm()
	{
		doTest("Path_Class_pm");
	}

	public void testPath_Tiny_pm()
	{
		doTest("Path_Tiny_pm");
	}

	public void testPerlIO_Layers_pm()
	{
		doTest("PerlIO_Layers_pm");
	}

	public void testPerlIO_encoding_pm()
	{
		doTest("PerlIO_encoding_pm");
	}

	public void testPerlIO_mmap_pm()
	{
		doTest("PerlIO_mmap_pm");
	}

	public void testPerlIO_pm()
	{
		doTest("PerlIO_pm");
	}

	public void testPerlIO_scalar_pm()
	{
		doTest("PerlIO_scalar_pm");
	}

	public void testPerlIO_utf8_strict_pm()
	{
		doTest("PerlIO_utf8_strict_pm");
	}

	public void testPerlIO_via_QuotedPrint_pm()
	{
		doTest("PerlIO_via_QuotedPrint_pm");
	}

	public void testPerlIO_via_pm()
	{
		doTest("PerlIO_via_pm");
	}

	public void testPerl_OSType_pm()
	{
		doTest("Perl_OSType_pm");
	}

	public void testPerl_PrereqScanner_Scanner_Aliased_pm()
	{
		doTest("Perl_PrereqScanner_Scanner_Aliased_pm");
	}

	public void testPerl_PrereqScanner_Scanner_Moose_pm()
	{
		doTest("Perl_PrereqScanner_Scanner_Moose_pm");
	}

	public void testPerl_PrereqScanner_Scanner_POE_pm()
	{
		doTest("Perl_PrereqScanner_Scanner_POE_pm");
	}

	public void testPerl_PrereqScanner_Scanner_Perl5_pm()
	{
		doTest("Perl_PrereqScanner_Scanner_Perl5_pm");
	}

	public void testPerl_PrereqScanner_Scanner_Superclass_pm()
	{
		doTest("Perl_PrereqScanner_Scanner_Superclass_pm");
	}

	public void testPerl_PrereqScanner_Scanner_TestMore_pm()
	{
		doTest("Perl_PrereqScanner_Scanner_TestMore_pm");
	}

	public void testPerl_PrereqScanner_Scanner_pm()
	{
		doTest("Perl_PrereqScanner_Scanner_pm");
	}

	public void testPerl_PrereqScanner_pm()
	{
		doTest("Perl_PrereqScanner_pm");
	}

	public void testPerl_Tidy_pm()
	{
		doTest("Perl_Tidy_pm");
	}

	public void testPerl_Version_pm()
	{
		doTest("Perl_Version_pm");
	}

	public void testPkgConfig_pm()
	{
		doTest("PkgConfig_pm");
	}

	public void testPod_Checker_pm()
	{
		doTest("Pod_Checker_pm");
	}

	public void testPod_Elemental_Autoblank_pm()
	{
		doTest("Pod_Elemental_Autoblank_pm");
	}

	public void testPod_Elemental_Autochomp_pm()
	{
		doTest("Pod_Elemental_Autochomp_pm");
	}

	public void testPod_Elemental_Command_pm()
	{
		doTest("Pod_Elemental_Command_pm");
	}

	public void testPod_Elemental_Document_pm()
	{
		doTest("Pod_Elemental_Document_pm");
	}

	public void testPod_Elemental_Element_Generic_Blank_pm()
	{
		doTest("Pod_Elemental_Element_Generic_Blank_pm");
	}

	public void testPod_Elemental_Element_Generic_Command_pm()
	{
		doTest("Pod_Elemental_Element_Generic_Command_pm");
	}

	public void testPod_Elemental_Element_Generic_Nonpod_pm()
	{
		doTest("Pod_Elemental_Element_Generic_Nonpod_pm");
	}

	public void testPod_Elemental_Element_Generic_Text_pm()
	{
		doTest("Pod_Elemental_Element_Generic_Text_pm");
	}

	public void testPod_Elemental_Element_Nested_pm()
	{
		doTest("Pod_Elemental_Element_Nested_pm");
	}

	public void testPod_Elemental_Element_Pod5_Command_pm()
	{
		doTest("Pod_Elemental_Element_Pod5_Command_pm");
	}

	public void testPod_Elemental_Element_Pod5_Data_pm()
	{
		doTest("Pod_Elemental_Element_Pod5_Data_pm");
	}

	public void testPod_Elemental_Element_Pod5_Nonpod_pm()
	{
		doTest("Pod_Elemental_Element_Pod5_Nonpod_pm");
	}

	public void testPod_Elemental_Element_Pod5_Ordinary_pm()
	{
		doTest("Pod_Elemental_Element_Pod5_Ordinary_pm");
	}

	public void testPod_Elemental_Element_Pod5_Region_pm()
	{
		doTest("Pod_Elemental_Element_Pod5_Region_pm");
	}

	public void testPod_Elemental_Element_Pod5_Verbatim_pm()
	{
		doTest("Pod_Elemental_Element_Pod5_Verbatim_pm");
	}

	public void testPod_Elemental_Flat_pm()
	{
		doTest("Pod_Elemental_Flat_pm");
	}

	public void testPod_Elemental_Node_pm()
	{
		doTest("Pod_Elemental_Node_pm");
	}

	public void testPod_Elemental_Objectifier_pm()
	{
		doTest("Pod_Elemental_Objectifier_pm");
	}

	public void testPod_Elemental_Paragraph_pm()
	{
		doTest("Pod_Elemental_Paragraph_pm");
	}

	public void testPod_Elemental_PerlMunger_pm()
	{
		doTest("Pod_Elemental_PerlMunger_pm");
	}

	public void testPod_Elemental_Selectors_pm()
	{
		doTest("Pod_Elemental_Selectors_pm");
	}

	public void testPod_Elemental_Transformer_Gatherer_pm()
	{
		doTest("Pod_Elemental_Transformer_Gatherer_pm");
	}

	public void testPod_Elemental_Transformer_Nester_pm()
	{
		doTest("Pod_Elemental_Transformer_Nester_pm");
	}

	public void testPod_Elemental_Transformer_Pod5_pm()
	{
		doTest("Pod_Elemental_Transformer_Pod5_pm");
	}

	public void testPod_Elemental_Transformer_pm()
	{
		doTest("Pod_Elemental_Transformer_pm");
	}

	public void testPod_Elemental_Types_pm()
	{
		doTest("Pod_Elemental_Types_pm");
	}

	public void testPod_Elemental_pm()
	{
		doTest("Pod_Elemental_pm");
	}

	public void testPod_Escapes_pm()
	{
		doTest("Pod_Escapes_pm");
	}

	public void testPod_Eventual_Simple_pm()
	{
		doTest("Pod_Eventual_Simple_pm");
	}

	public void testPod_Eventual_pm()
	{
		doTest("Pod_Eventual_pm");
	}

	public void testPod_Find_pm()
	{
		doTest("Pod_Find_pm");
	}

	public void testPod_Functions_pm()
	{
		doTest("Pod_Functions_pm");
	}

	public void testPod_Html_pm()
	{
		doTest("Pod_Html_pm");
	}

	public void testPod_InputObjects_pm()
	{
		doTest("Pod_InputObjects_pm");
	}

	public void testPod_LaTeX_pm()
	{
		doTest("Pod_LaTeX_pm");
	}

	public void testPod_Man_pm()
	{
		doTest("Pod_Man_pm");
	}

	public void testPod_ParseLink_pm()
	{
		doTest("Pod_ParseLink_pm");
	}

	public void testPod_ParseUtils_pm()
	{
		doTest("Pod_ParseUtils_pm");
	}

	public void testPod_Parser_pm()
	{
		doTest("Pod_Parser_pm");
	}

	public void testPod_Perldoc_BaseTo_pm()
	{
		doTest("Pod_Perldoc_BaseTo_pm");
	}

	public void testPod_Perldoc_GetOptsOO_pm()
	{
		doTest("Pod_Perldoc_GetOptsOO_pm");
	}

	public void testPod_Perldoc_ToANSI_pm()
	{
		doTest("Pod_Perldoc_ToANSI_pm");
	}

	public void testPod_Perldoc_ToChecker_pm()
	{
		doTest("Pod_Perldoc_ToChecker_pm");
	}

	public void testPod_Perldoc_ToMan_pm()
	{
		doTest("Pod_Perldoc_ToMan_pm");
	}

	public void testPod_Perldoc_ToNroff_pm()
	{
		doTest("Pod_Perldoc_ToNroff_pm");
	}

	public void testPod_Perldoc_ToPod_pm()
	{
		doTest("Pod_Perldoc_ToPod_pm");
	}

	public void testPod_Perldoc_ToRtf_pm()
	{
		doTest("Pod_Perldoc_ToRtf_pm");
	}

	public void testPod_Perldoc_ToTerm_pm()
	{
		doTest("Pod_Perldoc_ToTerm_pm");
	}

	public void testPod_Perldoc_ToText_pm()
	{
		doTest("Pod_Perldoc_ToText_pm");
	}

	public void testPod_Perldoc_ToTk_pm()
	{
		doTest("Pod_Perldoc_ToTk_pm");
	}

	public void testPod_Perldoc_ToXml_pm()
	{
		doTest("Pod_Perldoc_ToXml_pm");
	}

	public void testPod_Perldoc_pm()
	{
		doTest("Pod_Perldoc_pm");
	}

	public void testPod_PlainText_pm()
	{
		doTest("Pod_PlainText_pm");
	}

	public void testPod_Select_pm()
	{
		doTest("Pod_Select_pm");
	}

	public void testPod_Simple_BlackBox_pm()
	{
		doTest("Pod_Simple_BlackBox_pm");
	}

	public void testPod_Simple_Checker_pm()
	{
		doTest("Pod_Simple_Checker_pm");
	}

	public void testPod_Simple_Debug_pm()
	{
		doTest("Pod_Simple_Debug_pm");
	}

	public void testPod_Simple_DumpAsText_pm()
	{
		doTest("Pod_Simple_DumpAsText_pm");
	}

	public void testPod_Simple_DumpAsXML_pm()
	{
		doTest("Pod_Simple_DumpAsXML_pm");
	}

	public void testPod_Simple_HTMLBatch_pm()
	{
		doTest("Pod_Simple_HTMLBatch_pm");
	}

	public void testPod_Simple_HTMLLegacy_pm()
	{
		doTest("Pod_Simple_HTMLLegacy_pm");
	}

	public void testPod_Simple_HTML_pm()
	{
		doTest("Pod_Simple_HTML_pm");
	}

	public void testPod_Simple_LinkSection_pm()
	{
		doTest("Pod_Simple_LinkSection_pm");
	}

	public void testPod_Simple_Methody_pm()
	{
		doTest("Pod_Simple_Methody_pm");
	}

	public void testPod_Simple_Progress_pm()
	{
		doTest("Pod_Simple_Progress_pm");
	}

	public void testPod_Simple_PullParserEndToken_pm()
	{
		doTest("Pod_Simple_PullParserEndToken_pm");
	}

	public void testPod_Simple_PullParserStartToken_pm()
	{
		doTest("Pod_Simple_PullParserStartToken_pm");
	}

	public void testPod_Simple_PullParserTextToken_pm()
	{
		doTest("Pod_Simple_PullParserTextToken_pm");
	}

	public void testPod_Simple_PullParserToken_pm()
	{
		doTest("Pod_Simple_PullParserToken_pm");
	}

	public void testPod_Simple_PullParser_pm()
	{
		doTest("Pod_Simple_PullParser_pm");
	}

	public void testPod_Simple_RTF_pm()
	{
		doTest("Pod_Simple_RTF_pm");
	}

	public void testPod_Simple_Search_pm()
	{
		doTest("Pod_Simple_Search_pm");
	}

	public void testPod_Simple_SimpleTree_pm()
	{
		doTest("Pod_Simple_SimpleTree_pm");
	}

	public void testPod_Simple_TextContent_pm()
	{
		doTest("Pod_Simple_TextContent_pm");
	}

	public void testPod_Simple_Text_pm()
	{
		doTest("Pod_Simple_Text_pm");
	}

	public void testPod_Simple_TiedOutFH_pm()
	{
		doTest("Pod_Simple_TiedOutFH_pm");
	}

	public void testPod_Simple_TranscodeDumb_pm()
	{
		doTest("Pod_Simple_TranscodeDumb_pm");
	}

	public void testPod_Simple_TranscodeSmart_pm()
	{
		doTest("Pod_Simple_TranscodeSmart_pm");
	}

	public void testPod_Simple_Transcode_pm()
	{
		doTest("Pod_Simple_Transcode_pm");
	}

	public void testPod_Simple_XHTML_pm()
	{
		doTest("Pod_Simple_XHTML_pm");
	}

	public void testPod_Simple_XMLOutStream_pm()
	{
		doTest("Pod_Simple_XMLOutStream_pm");
	}

	public void testPod_Simple_pm()
	{
		doTest("Pod_Simple_pm");
	}

	public void testPod_Strip_pm()
	{
		doTest("Pod_Strip_pm");
	}

	public void testPod_Text_Color_pm()
	{
		doTest("Pod_Text_Color_pm");
	}

	public void testPod_Text_Overstrike_pm()
	{
		doTest("Pod_Text_Overstrike_pm");
	}

	public void testPod_Text_Termcap_pm()
	{
		doTest("Pod_Text_Termcap_pm");
	}

	public void testPod_Text_pm()
	{
		doTest("Pod_Text_pm");
	}

	public void testPod_Usage_pm()
	{
		doTest("Pod_Usage_pm");
	}

	public void testPod_Weaver_Config_Assembler_pm()
	{
		doTest("Pod_Weaver_Config_Assembler_pm");
	}

	public void testPod_Weaver_Config_Finder_pm()
	{
		doTest("Pod_Weaver_Config_Finder_pm");
	}

	public void testPod_Weaver_Config_pm()
	{
		doTest("Pod_Weaver_Config_pm");
	}

	public void testPod_Weaver_PluginBundle_CorePrep_pm()
	{
		doTest("Pod_Weaver_PluginBundle_CorePrep_pm");
	}

	public void testPod_Weaver_PluginBundle_Default_pm()
	{
		doTest("Pod_Weaver_PluginBundle_Default_pm");
	}

	public void testPod_Weaver_Plugin_EnsurePod5_pm()
	{
		doTest("Pod_Weaver_Plugin_EnsurePod5_pm");
	}

	public void testPod_Weaver_Plugin_H1Nester_pm()
	{
		doTest("Pod_Weaver_Plugin_H1Nester_pm");
	}

	public void testPod_Weaver_Plugin_SingleEncoding_pm()
	{
		doTest("Pod_Weaver_Plugin_SingleEncoding_pm");
	}

	public void testPod_Weaver_Plugin_Transformer_pm()
	{
		doTest("Pod_Weaver_Plugin_Transformer_pm");
	}

	public void testPod_Weaver_Role_Dialect_pm()
	{
		doTest("Pod_Weaver_Role_Dialect_pm");
	}

	public void testPod_Weaver_Role_Finalizer_pm()
	{
		doTest("Pod_Weaver_Role_Finalizer_pm");
	}

	public void testPod_Weaver_Role_Plugin_pm()
	{
		doTest("Pod_Weaver_Role_Plugin_pm");
	}

	public void testPod_Weaver_Role_Preparer_pm()
	{
		doTest("Pod_Weaver_Role_Preparer_pm");
	}

	public void testPod_Weaver_Role_Section_pm()
	{
		doTest("Pod_Weaver_Role_Section_pm");
	}

	public void testPod_Weaver_Role_StringFromComment_pm()
	{
		doTest("Pod_Weaver_Role_StringFromComment_pm");
	}

	public void testPod_Weaver_Role_Transformer_pm()
	{
		doTest("Pod_Weaver_Role_Transformer_pm");
	}

	public void testPod_Weaver_Section_Authors_pm()
	{
		doTest("Pod_Weaver_Section_Authors_pm");
	}

	public void testPod_Weaver_Section_Bugs_pm()
	{
		doTest("Pod_Weaver_Section_Bugs_pm");
	}

	public void testPod_Weaver_Section_Collect_pm()
	{
		doTest("Pod_Weaver_Section_Collect_pm");
	}

	public void testPod_Weaver_Section_Generic_pm()
	{
		doTest("Pod_Weaver_Section_Generic_pm");
	}

	public void testPod_Weaver_Section_Leftovers_pm()
	{
		doTest("Pod_Weaver_Section_Leftovers_pm");
	}

	public void testPod_Weaver_Section_Legal_pm()
	{
		doTest("Pod_Weaver_Section_Legal_pm");
	}

	public void testPod_Weaver_Section_Name_pm()
	{
		doTest("Pod_Weaver_Section_Name_pm");
	}

	public void testPod_Weaver_Section_Region_pm()
	{
		doTest("Pod_Weaver_Section_Region_pm");
	}

	public void testPod_Weaver_Section_SeeAlsoMason_pm()
	{
		doTest("Pod_Weaver_Section_SeeAlsoMason_pm");
	}

	public void testPod_Weaver_Section_Version_pm()
	{
		doTest("Pod_Weaver_Section_Version_pm");
	}

	public void testPod_Weaver_pm()
	{
		doTest("Pod_Weaver_pm");
	}

	public void testProbe_Perl_pm()
	{
		doTest("Probe_Perl_pm");
	}

	public void testRole_HasMessage_Errf_pm()
	{
		doTest("Role_HasMessage_Errf_pm");
	}

	public void testRole_HasMessage_pm()
	{
		doTest("Role_HasMessage_pm");
	}

	public void testRole_Identifiable_HasIdent_pm()
	{
		doTest("Role_Identifiable_HasIdent_pm");
	}

	public void testRole_Identifiable_HasTags_pm()
	{
		doTest("Role_Identifiable_HasTags_pm");
	}

	public void testRole_Tiny_With_pm()
	{
		doTest("Role_Tiny_With_pm");
	}

	public void testRole_Tiny_pm()
	{
		doTest("Role_Tiny_pm");
	}

	public void testSDBM_File_pm()
	{
		doTest("SDBM_File_pm");
	}

	public void testSOAP_Constants_pm()
	{
		doTest("SOAP_Constants_pm");
	}

	public void testSOAP_Lite_Deserializer_XMLSchema1999_pm()
	{
		doTest("SOAP_Lite_Deserializer_XMLSchema1999_pm");
	}

	public void testSOAP_Lite_Deserializer_XMLSchema2001_pm()
	{
		doTest("SOAP_Lite_Deserializer_XMLSchema2001_pm");
	}

	public void testSOAP_Lite_Deserializer_XMLSchemaSOAP1_1_pm()
	{
		doTest("SOAP_Lite_Deserializer_XMLSchemaSOAP1_1_pm");
	}

	public void testSOAP_Lite_Deserializer_XMLSchemaSOAP1_2_pm()
	{
		doTest("SOAP_Lite_Deserializer_XMLSchemaSOAP1_2_pm");
	}

	public void testSOAP_Lite_Packager_pm()
	{
		doTest("SOAP_Lite_Packager_pm");
	}

	public void testSOAP_Lite_Utils_pm()
	{
		doTest("SOAP_Lite_Utils_pm");
	}

	public void testSOAP_Lite_pm()
	{
		doTest("SOAP_Lite_pm");
	}

	public void testSOAP_Packager_pm()
	{
		doTest("SOAP_Packager_pm");
	}

	public void testSOAP_Test_pm()
	{
		doTest("SOAP_Test_pm");
	}

	public void testSOAP_Transport_HTTP_pm()
	{
		doTest("SOAP_Transport_HTTP_pm");
	}

	public void testSOAP_Transport_IO_pm()
	{
		doTest("SOAP_Transport_IO_pm");
	}

	public void testSOAP_Transport_LOCAL_pm()
	{
		doTest("SOAP_Transport_LOCAL_pm");
	}

	public void testSOAP_Transport_LOOPBACK_pm()
	{
		doTest("SOAP_Transport_LOOPBACK_pm");
	}

	public void testSOAP_Transport_MAILTO_pm()
	{
		doTest("SOAP_Transport_MAILTO_pm");
	}

	public void testSOAP_Transport_POP3_pm()
	{
		doTest("SOAP_Transport_POP3_pm");
	}

	public void testSOAP_Transport_TCP_pm()
	{
		doTest("SOAP_Transport_TCP_pm");
	}

	public void testSQL_Abstract_Test_pm()
	{
		doTest("SQL_Abstract_Test_pm");
	}

	public void testSQL_Abstract_Tree_pm()
	{
		doTest("SQL_Abstract_Tree_pm");
	}

	public void testSQL_Abstract_pm()
	{
		doTest("SQL_Abstract_pm");
	}

	public void testSQL_Dialects_ANSI_pm()
	{
		doTest("SQL_Dialects_ANSI_pm");
	}

	public void testSQL_Dialects_AnyData_pm()
	{
		doTest("SQL_Dialects_AnyData_pm");
	}

	public void testSQL_Dialects_CSV_pm()
	{
		doTest("SQL_Dialects_CSV_pm");
	}

	public void testSQL_Dialects_Role_pm()
	{
		doTest("SQL_Dialects_Role_pm");
	}

	public void testSQL_Eval_pm()
	{
		doTest("SQL_Eval_pm");
	}

	public void testSQL_Parser_pm()
	{
		doTest("SQL_Parser_pm");
	}

	public void testSQL_Statement_Function_pm()
	{
		doTest("SQL_Statement_Function_pm");
	}

	public void testSQL_Statement_Functions_pm()
	{
		doTest("SQL_Statement_Functions_pm");
	}

	public void testSQL_Statement_GetInfo_pm()
	{
		doTest("SQL_Statement_GetInfo_pm");
	}

	public void testSQL_Statement_Operation_pm()
	{
		doTest("SQL_Statement_Operation_pm");
	}

	public void testSQL_Statement_Placeholder_pm()
	{
		doTest("SQL_Statement_Placeholder_pm");
	}

	public void testSQL_Statement_RAM_pm()
	{
		doTest("SQL_Statement_RAM_pm");
	}

	public void testSQL_Statement_TermFactory_pm()
	{
		doTest("SQL_Statement_TermFactory_pm");
	}

	public void testSQL_Statement_Term_pm()
	{
		doTest("SQL_Statement_Term_pm");
	}

	public void testSQL_Statement_Util_pm()
	{
		doTest("SQL_Statement_Util_pm");
	}

	public void testSQL_Statement_pm()
	{
		doTest("SQL_Statement_pm");
	}

	public void testSQL_Translator_Parser_DBIx_Class_pm()
	{
		doTest("SQL_Translator_Parser_DBIx_Class_pm");
	}

	public void testSQL_Translator_Producer_DBIx_Class_File_pm()
	{
		doTest("SQL_Translator_Producer_DBIx_Class_File_pm");
	}

	public void testSafe_pm()
	{
		doTest("Safe_pm");
	}

	public void testScalar_Util_pm()
	{
		doTest("Scalar_Util_pm");
	}

	public void testScope_Guard_pm()
	{
		doTest("Scope_Guard_pm");
	}

	public void testSearch_Dict_pm()
	{
		doTest("Search_Dict_pm");
	}

	public void testSelectSaver_pm()
	{
		doTest("SelectSaver_pm");
	}

	public void testSelfLoader_pm()
	{
		doTest("SelfLoader_pm");
	}

	public void testSet_Scalar_Base_pm()
	{
		doTest("Set_Scalar_Base_pm");
	}

	public void testSet_Scalar_Null_pm()
	{
		doTest("Set_Scalar_Null_pm");
	}

	public void testSet_Scalar_Real_pm()
	{
		doTest("Set_Scalar_Real_pm");
	}

	public void testSet_Scalar_Universe_pm()
	{
		doTest("Set_Scalar_Universe_pm");
	}

	public void testSet_Scalar_ValuedUniverse_pm()
	{
		doTest("Set_Scalar_ValuedUniverse_pm");
	}

	public void testSet_Scalar_Valued_pm()
	{
		doTest("Set_Scalar_Valued_pm");
	}

	public void testSet_Scalar_Virtual_pm()
	{
		doTest("Set_Scalar_Virtual_pm");
	}

	public void testSet_Scalar_pm()
	{
		doTest("Set_Scalar_pm");
	}

	public void testSocket6_pm()
	{
		doTest("Socket6_pm");
	}

	public void testSocket_pm()
	{
		doTest("Socket_pm");
	}

	public void testSoftware_LicenseUtils_pm()
	{
		doTest("Software_LicenseUtils_pm");
	}

	public void testSoftware_License_AGPL_3_pm()
	{
		doTest("Software_License_AGPL_3_pm");
	}

	public void testSoftware_License_Apache_1_1_pm()
	{
		doTest("Software_License_Apache_1_1_pm");
	}

	public void testSoftware_License_Apache_2_0_pm()
	{
		doTest("Software_License_Apache_2_0_pm");
	}

	public void testSoftware_License_Artistic_1_0_pm()
	{
		doTest("Software_License_Artistic_1_0_pm");
	}

	public void testSoftware_License_Artistic_2_0_pm()
	{
		doTest("Software_License_Artistic_2_0_pm");
	}

	public void testSoftware_License_BSD_pm()
	{
		doTest("Software_License_BSD_pm");
	}

	public void testSoftware_License_CC0_1_0_pm()
	{
		doTest("Software_License_CC0_1_0_pm");
	}

	public void testSoftware_License_CC_BY_1_0_pm()
	{
		doTest("Software_License_CC_BY_1_0_pm");
	}

	public void testSoftware_License_CC_BY_2_0_pm()
	{
		doTest("Software_License_CC_BY_2_0_pm");
	}

	public void testSoftware_License_CC_BY_3_0_pm()
	{
		doTest("Software_License_CC_BY_3_0_pm");
	}

	public void testSoftware_License_CC_BY_4_0_pm()
	{
		doTest("Software_License_CC_BY_4_0_pm");
	}

	public void testSoftware_License_CC_BY_NC_1_0_pm()
	{
		doTest("Software_License_CC_BY_NC_1_0_pm");
	}

	public void testSoftware_License_CC_BY_NC_2_0_pm()
	{
		doTest("Software_License_CC_BY_NC_2_0_pm");
	}

	public void testSoftware_License_CC_BY_NC_3_0_pm()
	{
		doTest("Software_License_CC_BY_NC_3_0_pm");
	}

	public void testSoftware_License_CC_BY_NC_4_0_pm()
	{
		doTest("Software_License_CC_BY_NC_4_0_pm");
	}

	public void testSoftware_License_CC_BY_NC_ND_2_0_pm()
	{
		doTest("Software_License_CC_BY_NC_ND_2_0_pm");
	}

	public void testSoftware_License_CC_BY_NC_ND_3_0_pm()
	{
		doTest("Software_License_CC_BY_NC_ND_3_0_pm");
	}

	public void testSoftware_License_CC_BY_NC_ND_4_0_pm()
	{
		doTest("Software_License_CC_BY_NC_ND_4_0_pm");
	}

	public void testSoftware_License_CC_BY_NC_SA_1_0_pm()
	{
		doTest("Software_License_CC_BY_NC_SA_1_0_pm");
	}

	public void testSoftware_License_CC_BY_NC_SA_2_0_pm()
	{
		doTest("Software_License_CC_BY_NC_SA_2_0_pm");
	}

	public void testSoftware_License_CC_BY_NC_SA_3_0_pm()
	{
		doTest("Software_License_CC_BY_NC_SA_3_0_pm");
	}

	public void testSoftware_License_CC_BY_NC_SA_4_0_pm()
	{
		doTest("Software_License_CC_BY_NC_SA_4_0_pm");
	}

	public void testSoftware_License_CC_BY_ND_1_0_pm()
	{
		doTest("Software_License_CC_BY_ND_1_0_pm");
	}

	public void testSoftware_License_CC_BY_ND_2_0_pm()
	{
		doTest("Software_License_CC_BY_ND_2_0_pm");
	}

	public void testSoftware_License_CC_BY_ND_3_0_pm()
	{
		doTest("Software_License_CC_BY_ND_3_0_pm");
	}

	public void testSoftware_License_CC_BY_ND_4_0_pm()
	{
		doTest("Software_License_CC_BY_ND_4_0_pm");
	}

	public void testSoftware_License_CC_BY_ND_NC_1_0_pm()
	{
		doTest("Software_License_CC_BY_ND_NC_1_0_pm");
	}

	public void testSoftware_License_CC_BY_SA_1_0_pm()
	{
		doTest("Software_License_CC_BY_SA_1_0_pm");
	}

	public void testSoftware_License_CC_BY_SA_2_0_pm()
	{
		doTest("Software_License_CC_BY_SA_2_0_pm");
	}

	public void testSoftware_License_CC_BY_SA_3_0_pm()
	{
		doTest("Software_License_CC_BY_SA_3_0_pm");
	}

	public void testSoftware_License_CC_BY_SA_4_0_pm()
	{
		doTest("Software_License_CC_BY_SA_4_0_pm");
	}

	public void testSoftware_License_CC_PDM_1_0_pm()
	{
		doTest("Software_License_CC_PDM_1_0_pm");
	}

	public void testSoftware_License_CCpack_pm()
	{
		doTest("Software_License_CCpack_pm");
	}

	public void testSoftware_License_Custom_pm()
	{
		doTest("Software_License_Custom_pm");
	}

	public void testSoftware_License_FreeBSD_pm()
	{
		doTest("Software_License_FreeBSD_pm");
	}

	public void testSoftware_License_GFDL_1_2_pm()
	{
		doTest("Software_License_GFDL_1_2_pm");
	}

	public void testSoftware_License_GFDL_1_3_pm()
	{
		doTest("Software_License_GFDL_1_3_pm");
	}

	public void testSoftware_License_GPL_1_pm()
	{
		doTest("Software_License_GPL_1_pm");
	}

	public void testSoftware_License_GPL_2_pm()
	{
		doTest("Software_License_GPL_2_pm");
	}

	public void testSoftware_License_GPL_3_pm()
	{
		doTest("Software_License_GPL_3_pm");
	}

	public void testSoftware_License_LGPL_2_1_pm()
	{
		doTest("Software_License_LGPL_2_1_pm");
	}

	public void testSoftware_License_LGPL_3_0_pm()
	{
		doTest("Software_License_LGPL_3_0_pm");
	}

	public void testSoftware_License_MIT_pm()
	{
		doTest("Software_License_MIT_pm");
	}

	public void testSoftware_License_Mozilla_1_0_pm()
	{
		doTest("Software_License_Mozilla_1_0_pm");
	}

	public void testSoftware_License_Mozilla_1_1_pm()
	{
		doTest("Software_License_Mozilla_1_1_pm");
	}

	public void testSoftware_License_Mozilla_2_0_pm()
	{
		doTest("Software_License_Mozilla_2_0_pm");
	}

	public void testSoftware_License_None_pm()
	{
		doTest("Software_License_None_pm");
	}

	public void testSoftware_License_OpenSSL_pm()
	{
		doTest("Software_License_OpenSSL_pm");
	}

	public void testSoftware_License_Perl_5_pm()
	{
		doTest("Software_License_Perl_5_pm");
	}

	public void testSoftware_License_PostgreSQL_pm()
	{
		doTest("Software_License_PostgreSQL_pm");
	}

	public void testSoftware_License_QPL_1_0_pm()
	{
		doTest("Software_License_QPL_1_0_pm");
	}

	public void testSoftware_License_SSLeay_pm()
	{
		doTest("Software_License_SSLeay_pm");
	}

	public void testSoftware_License_Sun_pm()
	{
		doTest("Software_License_Sun_pm");
	}

	public void testSoftware_License_Zlib_pm()
	{
		doTest("Software_License_Zlib_pm");
	}

	public void testSoftware_License_pm()
	{
		doTest("Software_License_pm");
	}

	public void testSort_Naturally_pm()
	{
		doTest("Sort_Naturally_pm");
	}

	public void testSort_Versions_pm()
	{
		doTest("Sort_Versions_pm");
	}

	public void testSpiffy_mixin_pm()
	{
		doTest("Spiffy_mixin_pm");
	}

	public void testSpiffy_pm()
	{
		doTest("Spiffy_pm");
	}

	public void testSpreadsheet_ParseExcel_Cell_pm()
	{
		doTest("Spreadsheet_ParseExcel_Cell_pm");
	}

	public void testSpreadsheet_ParseExcel_Dump_pm()
	{
		doTest("Spreadsheet_ParseExcel_Dump_pm");
	}

	public void testSpreadsheet_ParseExcel_FmtDefault_pm()
	{
		doTest("Spreadsheet_ParseExcel_FmtDefault_pm");
	}

	public void testSpreadsheet_ParseExcel_FmtJapan2_pm()
	{
		doTest("Spreadsheet_ParseExcel_FmtJapan2_pm");
	}

	public void testSpreadsheet_ParseExcel_FmtJapan_pm()
	{
		doTest("Spreadsheet_ParseExcel_FmtJapan_pm");
	}

	public void testSpreadsheet_ParseExcel_FmtUnicode_pm()
	{
		doTest("Spreadsheet_ParseExcel_FmtUnicode_pm");
	}

	public void testSpreadsheet_ParseExcel_Font_pm()
	{
		doTest("Spreadsheet_ParseExcel_Font_pm");
	}

	public void testSpreadsheet_ParseExcel_Format_pm()
	{
		doTest("Spreadsheet_ParseExcel_Format_pm");
	}

	public void testSpreadsheet_ParseExcel_SaveParser_Workbook_pm()
	{
		doTest("Spreadsheet_ParseExcel_SaveParser_Workbook_pm");
	}

	public void testSpreadsheet_ParseExcel_SaveParser_Worksheet_pm()
	{
		doTest("Spreadsheet_ParseExcel_SaveParser_Worksheet_pm");
	}

	public void testSpreadsheet_ParseExcel_SaveParser_pm()
	{
		doTest("Spreadsheet_ParseExcel_SaveParser_pm");
	}

	public void testSpreadsheet_ParseExcel_Utility_pm()
	{
		doTest("Spreadsheet_ParseExcel_Utility_pm");
	}

	public void testSpreadsheet_ParseExcel_Workbook_pm()
	{
		doTest("Spreadsheet_ParseExcel_Workbook_pm");
	}

	public void testSpreadsheet_ParseExcel_Worksheet_pm()
	{
		doTest("Spreadsheet_ParseExcel_Worksheet_pm");
	}

	public void testSpreadsheet_ParseExcel_pm()
	{
		doTest("Spreadsheet_ParseExcel_pm");
	}

	public void testSpreadsheet_ParseXLSX_pm()
	{
		doTest("Spreadsheet_ParseXLSX_pm");
	}

	public void testSpreadsheet_WriteExcel_BIFFwriter_pm()
	{
		doTest("Spreadsheet_WriteExcel_BIFFwriter_pm");
	}

	public void testSpreadsheet_WriteExcel_Big_pm()
	{
		doTest("Spreadsheet_WriteExcel_Big_pm");
	}

	public void testSpreadsheet_WriteExcel_Chart_Area_pm()
	{
		doTest("Spreadsheet_WriteExcel_Chart_Area_pm");
	}

	public void testSpreadsheet_WriteExcel_Chart_Bar_pm()
	{
		doTest("Spreadsheet_WriteExcel_Chart_Bar_pm");
	}

	public void testSpreadsheet_WriteExcel_Chart_Column_pm()
	{
		doTest("Spreadsheet_WriteExcel_Chart_Column_pm");
	}

	public void testSpreadsheet_WriteExcel_Chart_External_pm()
	{
		doTest("Spreadsheet_WriteExcel_Chart_External_pm");
	}

	public void testSpreadsheet_WriteExcel_Chart_Line_pm()
	{
		doTest("Spreadsheet_WriteExcel_Chart_Line_pm");
	}

	public void testSpreadsheet_WriteExcel_Chart_Pie_pm()
	{
		doTest("Spreadsheet_WriteExcel_Chart_Pie_pm");
	}

	public void testSpreadsheet_WriteExcel_Chart_Scatter_pm()
	{
		doTest("Spreadsheet_WriteExcel_Chart_Scatter_pm");
	}

	public void testSpreadsheet_WriteExcel_Chart_Stock_pm()
	{
		doTest("Spreadsheet_WriteExcel_Chart_Stock_pm");
	}

	public void testSpreadsheet_WriteExcel_Chart_pm()
	{
		doTest("Spreadsheet_WriteExcel_Chart_pm");
	}

	public void testSpreadsheet_WriteExcel_Examples_pm()
	{
		doTest("Spreadsheet_WriteExcel_Examples_pm");
	}

	public void testSpreadsheet_WriteExcel_Format_pm()
	{
		doTest("Spreadsheet_WriteExcel_Format_pm");
	}

	public void testSpreadsheet_WriteExcel_Formula_pm()
	{
		doTest("Spreadsheet_WriteExcel_Formula_pm");
	}

	public void testSpreadsheet_WriteExcel_OLEwriter_pm()
	{
		doTest("Spreadsheet_WriteExcel_OLEwriter_pm");
	}

	public void testSpreadsheet_WriteExcel_Properties_pm()
	{
		doTest("Spreadsheet_WriteExcel_Properties_pm");
	}

	public void testSpreadsheet_WriteExcel_Utility_pm()
	{
		doTest("Spreadsheet_WriteExcel_Utility_pm");
	}

	public void testSpreadsheet_WriteExcel_Workbook_pm()
	{
		doTest("Spreadsheet_WriteExcel_Workbook_pm");
	}

	public void testSpreadsheet_WriteExcel_Worksheet_pm()
	{
		doTest("Spreadsheet_WriteExcel_Worksheet_pm");
	}

	public void testSpreadsheet_WriteExcel_pm()
	{
		doTest("Spreadsheet_WriteExcel_pm");
	}

	public void testStackTrace_Auto_pm()
	{
		doTest("StackTrace_Auto_pm");
	}

	public void testStorable_pm()
	{
		doTest("Storable_pm");
	}

	public void testString_Errf_pm()
	{
		doTest("String_Errf_pm");
	}

	public void testString_Flogger_pm()
	{
		doTest("String_Flogger_pm");
	}

	public void testString_Formatter_Cookbook_pm()
	{
		doTest("String_Formatter_Cookbook_pm");
	}

	public void testString_Formatter_pm()
	{
		doTest("String_Formatter_pm");
	}

	public void testString_Print_pm()
	{
		doTest("String_Print_pm");
	}

	public void testString_RewritePrefix_pm()
	{
		doTest("String_RewritePrefix_pm");
	}

	public void testString_Truncate_pm()
	{
		doTest("String_Truncate_pm");
	}

	public void testString_bench_pl()
	{
		doTest("String_bench_pl");
	}

	public void testSub_Defer_pm()
	{
		doTest("Sub_Defer_pm");
	}

	public void testSub_Exporter_ForMethods_pm()
	{
		doTest("Sub_Exporter_ForMethods_pm");
	}

	public void testSub_Exporter_GlobExporter_pm()
	{
		doTest("Sub_Exporter_GlobExporter_pm");
	}

	public void testSub_Exporter_Progressive_pm()
	{
		doTest("Sub_Exporter_Progressive_pm");
	}

	public void testSub_Exporter_Util_pm()
	{
		doTest("Sub_Exporter_Util_pm");
	}

	public void testSub_Exporter_pm()
	{
		doTest("Sub_Exporter_pm");
	}

	public void testSub_Identify_pm()
	{
		doTest("Sub_Identify_pm");
	}

	public void testSub_Install_pm()
	{
		doTest("Sub_Install_pm");
	}

	public void testSub_Name_pm()
	{
		doTest("Sub_Name_pm");
	}

	public void testSub_Quote_pm()
	{
		doTest("Sub_Quote_pm");
	}

	public void testSub_Uplevel_pm()
	{
		doTest("Sub_Uplevel_pm");
	}

	public void testSub_Util_pm()
	{
		doTest("Sub_Util_pm");
	}

	public void testSymbol_pm()
	{
		doTest("Symbol_pm");
	}

	public void testSyntax_Feature_Junction_pm()
	{
		doTest("Syntax_Feature_Junction_pm");
	}

	public void testSyntax_Keyword_Junction_All_pm()
	{
		doTest("Syntax_Keyword_Junction_All_pm");
	}

	public void testSyntax_Keyword_Junction_Any_pm()
	{
		doTest("Syntax_Keyword_Junction_Any_pm");
	}

	public void testSyntax_Keyword_Junction_Base_pm()
	{
		doTest("Syntax_Keyword_Junction_Base_pm");
	}

	public void testSyntax_Keyword_Junction_None_pm()
	{
		doTest("Syntax_Keyword_Junction_None_pm");
	}

	public void testSyntax_Keyword_Junction_One_pm()
	{
		doTest("Syntax_Keyword_Junction_One_pm");
	}

	public void testSyntax_Keyword_Junction_pm()
	{
		doTest("Syntax_Keyword_Junction_pm");
	}

	public void testSys_Hostname_pm()
	{
		doTest("Sys_Hostname_pm");
	}

	public void testSys_Syslog_Win32_pm()
	{
		doTest("Sys_Syslog_Win32_pm");
	}

	public void testSys_Syslog_pm()
	{
		doTest("Sys_Syslog_pm");
	}

	public void testTAP_Base_pm()
	{
		doTest("TAP_Base_pm");
	}

	public void testTAP_Formatter_Base_pm()
	{
		doTest("TAP_Formatter_Base_pm");
	}

	public void testTAP_Formatter_Color_pm()
	{
		doTest("TAP_Formatter_Color_pm");
	}

	public void testTAP_Formatter_Console_ParallelSession_pm()
	{
		doTest("TAP_Formatter_Console_ParallelSession_pm");
	}

	public void testTAP_Formatter_Console_Session_pm()
	{
		doTest("TAP_Formatter_Console_Session_pm");
	}

	public void testTAP_Formatter_Console_pm()
	{
		doTest("TAP_Formatter_Console_pm");
	}

	public void testTAP_Formatter_File_Session_pm()
	{
		doTest("TAP_Formatter_File_Session_pm");
	}

	public void testTAP_Formatter_File_pm()
	{
		doTest("TAP_Formatter_File_pm");
	}

	public void testTAP_Formatter_Session_pm()
	{
		doTest("TAP_Formatter_Session_pm");
	}

	public void testTAP_Harness_Env_pm()
	{
		doTest("TAP_Harness_Env_pm");
	}

	public void testTAP_Harness_pm()
	{
		doTest("TAP_Harness_pm");
	}

	public void testTAP_Object_pm()
	{
		doTest("TAP_Object_pm");
	}

	public void testTAP_Parser_Aggregator_pm()
	{
		doTest("TAP_Parser_Aggregator_pm");
	}

	public void testTAP_Parser_Grammar_pm()
	{
		doTest("TAP_Parser_Grammar_pm");
	}

	public void testTAP_Parser_IteratorFactory_pm()
	{
		doTest("TAP_Parser_IteratorFactory_pm");
	}

	public void testTAP_Parser_Iterator_Array_pm()
	{
		doTest("TAP_Parser_Iterator_Array_pm");
	}

	public void testTAP_Parser_Iterator_Process_pm()
	{
		doTest("TAP_Parser_Iterator_Process_pm");
	}

	public void testTAP_Parser_Iterator_Stream_pm()
	{
		doTest("TAP_Parser_Iterator_Stream_pm");
	}

	public void testTAP_Parser_Iterator_pm()
	{
		doTest("TAP_Parser_Iterator_pm");
	}

	public void testTAP_Parser_Multiplexer_pm()
	{
		doTest("TAP_Parser_Multiplexer_pm");
	}

	public void testTAP_Parser_ResultFactory_pm()
	{
		doTest("TAP_Parser_ResultFactory_pm");
	}

	public void testTAP_Parser_Result_Bailout_pm()
	{
		doTest("TAP_Parser_Result_Bailout_pm");
	}

	public void testTAP_Parser_Result_Comment_pm()
	{
		doTest("TAP_Parser_Result_Comment_pm");
	}

	public void testTAP_Parser_Result_Plan_pm()
	{
		doTest("TAP_Parser_Result_Plan_pm");
	}

	public void testTAP_Parser_Result_Pragma_pm()
	{
		doTest("TAP_Parser_Result_Pragma_pm");
	}

	public void testTAP_Parser_Result_Test_pm()
	{
		doTest("TAP_Parser_Result_Test_pm");
	}

	public void testTAP_Parser_Result_Unknown_pm()
	{
		doTest("TAP_Parser_Result_Unknown_pm");
	}

	public void testTAP_Parser_Result_Version_pm()
	{
		doTest("TAP_Parser_Result_Version_pm");
	}

	public void testTAP_Parser_Result_YAML_pm()
	{
		doTest("TAP_Parser_Result_YAML_pm");
	}

	public void testTAP_Parser_Result_pm()
	{
		doTest("TAP_Parser_Result_pm");
	}

	public void testTAP_Parser_Scheduler_Job_pm()
	{
		doTest("TAP_Parser_Scheduler_Job_pm");
	}

	public void testTAP_Parser_Scheduler_Spinner_pm()
	{
		doTest("TAP_Parser_Scheduler_Spinner_pm");
	}

	public void testTAP_Parser_Scheduler_pm()
	{
		doTest("TAP_Parser_Scheduler_pm");
	}

	public void testTAP_Parser_SourceHandler_Executable_pm()
	{
		doTest("TAP_Parser_SourceHandler_Executable_pm");
	}

	public void testTAP_Parser_SourceHandler_File_pm()
	{
		doTest("TAP_Parser_SourceHandler_File_pm");
	}

	public void testTAP_Parser_SourceHandler_Handle_pm()
	{
		doTest("TAP_Parser_SourceHandler_Handle_pm");
	}

	public void testTAP_Parser_SourceHandler_Perl_pm()
	{
		doTest("TAP_Parser_SourceHandler_Perl_pm");
	}

	public void testTAP_Parser_SourceHandler_RawTAP_pm()
	{
		doTest("TAP_Parser_SourceHandler_RawTAP_pm");
	}

	public void testTAP_Parser_SourceHandler_pm()
	{
		doTest("TAP_Parser_SourceHandler_pm");
	}

	public void testTAP_Parser_Source_pm()
	{
		doTest("TAP_Parser_Source_pm");
	}

	public void testTAP_Parser_YAMLish_Reader_pm()
	{
		doTest("TAP_Parser_YAMLish_Reader_pm");
	}

	public void testTAP_Parser_YAMLish_Writer_pm()
	{
		doTest("TAP_Parser_YAMLish_Writer_pm");
	}

	public void testTAP_Parser_pm()
	{
		doTest("TAP_Parser_pm");
	}

	public void testTask_Weaken_pm()
	{
		doTest("Task_Weaken_pm");
	}

	public void testTemplate_Base_pm()
	{
		doTest("Template_Base_pm");
	}

	public void testTemplate_Config_pm()
	{
		doTest("Template_Config_pm");
	}

	public void testTemplate_Constants_pm()
	{
		doTest("Template_Constants_pm");
	}

	public void testTemplate_Context_pm()
	{
		doTest("Template_Context_pm");
	}

	public void testTemplate_Directive_pm()
	{
		doTest("Template_Directive_pm");
	}

	public void testTemplate_Document_pm()
	{
		doTest("Template_Document_pm");
	}

	public void testTemplate_Exception_pm()
	{
		doTest("Template_Exception_pm");
	}

	public void testTemplate_Filters_pm()
	{
		doTest("Template_Filters_pm");
	}

	public void testTemplate_Grammar_pm()
	{
		doTest("Template_Grammar_pm");
	}

	public void testTemplate_Iterator_pm()
	{
		doTest("Template_Iterator_pm");
	}

	public void testTemplate_Namespace_Constants_pm()
	{
		doTest("Template_Namespace_Constants_pm");
	}

	public void testTemplate_Parser_pm()
	{
		doTest("Template_Parser_pm");
	}

	public void testTemplate_Plugin_Assert_pm()
	{
		doTest("Template_Plugin_Assert_pm");
	}

	public void testTemplate_Plugin_CGI_pm()
	{
		doTest("Template_Plugin_CGI_pm");
	}

	public void testTemplate_Plugin_Datafile_pm()
	{
		doTest("Template_Plugin_Datafile_pm");
	}

	public void testTemplate_Plugin_Date_pm()
	{
		doTest("Template_Plugin_Date_pm");
	}

	public void testTemplate_Plugin_Directory_pm()
	{
		doTest("Template_Plugin_Directory_pm");
	}

	public void testTemplate_Plugin_Dumper_pm()
	{
		doTest("Template_Plugin_Dumper_pm");
	}

	public void testTemplate_Plugin_File_pm()
	{
		doTest("Template_Plugin_File_pm");
	}

	public void testTemplate_Plugin_Filter_pm()
	{
		doTest("Template_Plugin_Filter_pm");
	}

	public void testTemplate_Plugin_Format_pm()
	{
		doTest("Template_Plugin_Format_pm");
	}

	public void testTemplate_Plugin_HTML_pm()
	{
		doTest("Template_Plugin_HTML_pm");
	}

	public void testTemplate_Plugin_Image_pm()
	{
		doTest("Template_Plugin_Image_pm");
	}

	public void testTemplate_Plugin_Iterator_pm()
	{
		doTest("Template_Plugin_Iterator_pm");
	}

	public void testTemplate_Plugin_Math_pm()
	{
		doTest("Template_Plugin_Math_pm");
	}

	public void testTemplate_Plugin_Pod_pm()
	{
		doTest("Template_Plugin_Pod_pm");
	}

	public void testTemplate_Plugin_Procedural_pm()
	{
		doTest("Template_Plugin_Procedural_pm");
	}

	public void testTemplate_Plugin_Scalar_pm()
	{
		doTest("Template_Plugin_Scalar_pm");
	}

	public void testTemplate_Plugin_String_pm()
	{
		doTest("Template_Plugin_String_pm");
	}

	public void testTemplate_Plugin_Table_pm()
	{
		doTest("Template_Plugin_Table_pm");
	}

	public void testTemplate_Plugin_URL_pm()
	{
		doTest("Template_Plugin_URL_pm");
	}

	public void testTemplate_Plugin_View_pm()
	{
		doTest("Template_Plugin_View_pm");
	}

	public void testTemplate_Plugin_Wrap_pm()
	{
		doTest("Template_Plugin_Wrap_pm");
	}

	public void testTemplate_Plugin_pm()
	{
		doTest("Template_Plugin_pm");
	}

	public void testTemplate_Plugins_pm()
	{
		doTest("Template_Plugins_pm");
	}

	public void testTemplate_Provider_pm()
	{
		doTest("Template_Provider_pm");
	}

	public void testTemplate_Service_pm()
	{
		doTest("Template_Service_pm");
	}

	public void testTemplate_Stash_Context_pm()
	{
		doTest("Template_Stash_Context_pm");
	}

	public void testTemplate_Stash_XS_pm()
	{
		doTest("Template_Stash_XS_pm");
	}

	public void testTemplate_Stash_pm()
	{
		doTest("Template_Stash_pm");
	}

	public void testTemplate_Test_pm()
	{
		doTest("Template_Test_pm");
	}

	public void testTemplate_Tiny_pm()
	{
		doTest("Template_Tiny_pm");
	}

	public void testTemplate_VMethods_pm()
	{
		doTest("Template_VMethods_pm");
	}

	public void testTemplate_View_pm()
	{
		doTest("Template_View_pm");
	}

	public void testTemplate_pm()
	{
		doTest("Template_pm");
	}

	public void testTerm_ANSIColor_pm()
	{
		doTest("Term_ANSIColor_pm");
	}

	public void testTerm_Cap_pm()
	{
		doTest("Term_Cap_pm");
	}

	public void testTerm_Complete_pm()
	{
		doTest("Term_Complete_pm");
	}

	public void testTerm_Encoding_pm()
	{
		doTest("Term_Encoding_pm");
	}

	public void testTerm_ReadKey_pm()
	{
		doTest("Term_ReadKey_pm");
	}

	public void testTerm_ReadLine_Perl_pm()
	{
		doTest("Term_ReadLine_Perl_pm");
	}

	public void testTerm_ReadLine_pm()
	{
		doTest("Term_ReadLine_pm");
	}

	public void testTerm_ReadLine_readline_pm()
	{
		doTest("Term_ReadLine_readline_pm");
	}

	public void testTerm_UI_History_pm()
	{
		doTest("Term_UI_History_pm");
	}

	public void testTerm_UI_pm()
	{
		doTest("Term_UI_pm");
	}

	public void testTest_Base_Filter_pm()
	{
		doTest("Test_Base_Filter_pm");
	}

	public void testTest_Base_pm()
	{
		doTest("Test_Base_pm");
	}

	public void testTest_Builder_IO_Scalar_pm()
	{
		doTest("Test_Builder_IO_Scalar_pm");
	}

	public void testTest_Builder_Module_pm()
	{
		doTest("Test_Builder_Module_pm");
	}

	public void testTest_Builder_Tester_Color_pm()
	{
		doTest("Test_Builder_Tester_Color_pm");
	}

	public void testTest_Builder_Tester_pm()
	{
		doTest("Test_Builder_Tester_pm");
	}

	public void testTest_Builder_pm()
	{
		doTest("Test_Builder_pm");
	}

	public void testTest_CheckDeps_pm()
	{
		doTest("Test_CheckDeps_pm");
	}

	public void testTest_Class_Load_pm()
	{
		doTest("Test_Class_Load_pm");
	}

	public void testTest_Class_MethodInfo_pm()
	{
		doTest("Test_Class_MethodInfo_pm");
	}

	public void testTest_Class_Most_pm()
	{
		doTest("Test_Class_Most_pm");
	}

	public void testTest_Class_pm()
	{
		doTest("Test_Class_pm");
	}

	public void testTest_CleanNamespaces_pm()
	{
		doTest("Test_CleanNamespaces_pm");
	}

	public void testTest_DZil_pm()
	{
		doTest("Test_DZil_pm");
	}

	public void testTest_Deep_All_pm()
	{
		doTest("Test_Deep_All_pm");
	}

	public void testTest_Deep_Any_pm()
	{
		doTest("Test_Deep_Any_pm");
	}

	public void testTest_Deep_ArrayEach_pm()
	{
		doTest("Test_Deep_ArrayEach_pm");
	}

	public void testTest_Deep_ArrayElementsOnly_pm()
	{
		doTest("Test_Deep_ArrayElementsOnly_pm");
	}

	public void testTest_Deep_ArrayLengthOnly_pm()
	{
		doTest("Test_Deep_ArrayLengthOnly_pm");
	}

	public void testTest_Deep_ArrayLength_pm()
	{
		doTest("Test_Deep_ArrayLength_pm");
	}

	public void testTest_Deep_Array_pm()
	{
		doTest("Test_Deep_Array_pm");
	}

	public void testTest_Deep_Blessed_pm()
	{
		doTest("Test_Deep_Blessed_pm");
	}

	public void testTest_Deep_Boolean_pm()
	{
		doTest("Test_Deep_Boolean_pm");
	}

	public void testTest_Deep_Cache_Simple_pm()
	{
		doTest("Test_Deep_Cache_Simple_pm");
	}

	public void testTest_Deep_Cache_pm()
	{
		doTest("Test_Deep_Cache_pm");
	}

	public void testTest_Deep_Class_pm()
	{
		doTest("Test_Deep_Class_pm");
	}

	public void testTest_Deep_Cmp_pm()
	{
		doTest("Test_Deep_Cmp_pm");
	}

	public void testTest_Deep_Code_pm()
	{
		doTest("Test_Deep_Code_pm");
	}

	public void testTest_Deep_HashEach_pm()
	{
		doTest("Test_Deep_HashEach_pm");
	}

	public void testTest_Deep_HashElements_pm()
	{
		doTest("Test_Deep_HashElements_pm");
	}

	public void testTest_Deep_HashKeysOnly_pm()
	{
		doTest("Test_Deep_HashKeysOnly_pm");
	}

	public void testTest_Deep_HashKeys_pm()
	{
		doTest("Test_Deep_HashKeys_pm");
	}

	public void testTest_Deep_Hash_pm()
	{
		doTest("Test_Deep_Hash_pm");
	}

	public void testTest_Deep_Ignore_pm()
	{
		doTest("Test_Deep_Ignore_pm");
	}

	public void testTest_Deep_Isa_pm()
	{
		doTest("Test_Deep_Isa_pm");
	}

	public void testTest_Deep_ListMethods_pm()
	{
		doTest("Test_Deep_ListMethods_pm");
	}

	public void testTest_Deep_MM_pm()
	{
		doTest("Test_Deep_MM_pm");
	}

	public void testTest_Deep_Methods_pm()
	{
		doTest("Test_Deep_Methods_pm");
	}

	public void testTest_Deep_NoTest_pm()
	{
		doTest("Test_Deep_NoTest_pm");
	}

	public void testTest_Deep_Number_pm()
	{
		doTest("Test_Deep_Number_pm");
	}

	public void testTest_Deep_Obj_pm()
	{
		doTest("Test_Deep_Obj_pm");
	}

	public void testTest_Deep_RefType_pm()
	{
		doTest("Test_Deep_RefType_pm");
	}

	public void testTest_Deep_Ref_pm()
	{
		doTest("Test_Deep_Ref_pm");
	}

	public void testTest_Deep_RegexpMatches_pm()
	{
		doTest("Test_Deep_RegexpMatches_pm");
	}

	public void testTest_Deep_RegexpOnly_pm()
	{
		doTest("Test_Deep_RegexpOnly_pm");
	}

	public void testTest_Deep_RegexpRefOnly_pm()
	{
		doTest("Test_Deep_RegexpRefOnly_pm");
	}

	public void testTest_Deep_RegexpRef_pm()
	{
		doTest("Test_Deep_RegexpRef_pm");
	}

	public void testTest_Deep_RegexpVersion_pm()
	{
		doTest("Test_Deep_RegexpVersion_pm");
	}

	public void testTest_Deep_Regexp_pm()
	{
		doTest("Test_Deep_Regexp_pm");
	}

	public void testTest_Deep_ScalarRefOnly_pm()
	{
		doTest("Test_Deep_ScalarRefOnly_pm");
	}

	public void testTest_Deep_ScalarRef_pm()
	{
		doTest("Test_Deep_ScalarRef_pm");
	}

	public void testTest_Deep_Set_pm()
	{
		doTest("Test_Deep_Set_pm");
	}

	public void testTest_Deep_Shallow_pm()
	{
		doTest("Test_Deep_Shallow_pm");
	}

	public void testTest_Deep_Stack_pm()
	{
		doTest("Test_Deep_Stack_pm");
	}

	public void testTest_Deep_String_pm()
	{
		doTest("Test_Deep_String_pm");
	}

	public void testTest_Deep_pm()
	{
		doTest("Test_Deep_pm");
	}

	public void testTest_Differences_pm()
	{
		doTest("Test_Differences_pm");
	}

	public void testTest_Exception_pm()
	{
		doTest("Test_Exception_pm");
	}

	public void testTest_FailWarnings_pm()
	{
		doTest("Test_FailWarnings_pm");
	}

	public void testTest_Fatal_pm()
	{
		doTest("Test_Fatal_pm");
	}

	public void testTest_File_ShareDir_Dist_pm()
	{
		doTest("Test_File_ShareDir_Dist_pm");
	}

	public void testTest_File_ShareDir_Module_pm()
	{
		doTest("Test_File_ShareDir_Module_pm");
	}

	public void testTest_File_ShareDir_Object_Dist_pm()
	{
		doTest("Test_File_ShareDir_Object_Dist_pm");
	}

	public void testTest_File_ShareDir_Object_Inc_pm()
	{
		doTest("Test_File_ShareDir_Object_Inc_pm");
	}

	public void testTest_File_ShareDir_Object_Module_pm()
	{
		doTest("Test_File_ShareDir_Object_Module_pm");
	}

	public void testTest_File_ShareDir_TempDirObject_pm()
	{
		doTest("Test_File_ShareDir_TempDirObject_pm");
	}

	public void testTest_File_ShareDir_Utils_pm()
	{
		doTest("Test_File_ShareDir_Utils_pm");
	}

	public void testTest_File_ShareDir_pm()
	{
		doTest("Test_File_ShareDir_pm");
	}

	public void testTest_Harness_pm()
	{
		doTest("Test_Harness_pm");
	}

	public void testTest_Kwalitee_pm()
	{
		doTest("Test_Kwalitee_pm");
	}

	public void testTest_LeakTrace_Script_pm()
	{
		doTest("Test_LeakTrace_Script_pm");
	}

	public void testTest_LeakTrace_pm()
	{
		doTest("Test_LeakTrace_pm");
	}

	public void testTest_LongString_pm()
	{
		doTest("Test_LongString_pm");
	}

	public void testTest_Mojo_pm()
	{
		doTest("Test_Mojo_pm");
	}

	public void testTest_Moose_pm()
	{
		doTest("Test_Moose_pm");
	}

	public void testTest_More_pm()
	{
		doTest("Test_More_pm");
	}

	public void testTest_Most_Exception_pm()
	{
		doTest("Test_Most_Exception_pm");
	}

	public void testTest_Most_pm()
	{
		doTest("Test_Most_pm");
	}

	public void testTest_NoWarnings_Warning_pm()
	{
		doTest("Test_NoWarnings_Warning_pm");
	}

	public void testTest_NoWarnings_pm()
	{
		doTest("Test_NoWarnings_pm");
	}

	public void testTest_Object_Test_pm()
	{
		doTest("Test_Object_Test_pm");
	}

	public void testTest_Object_pm()
	{
		doTest("Test_Object_pm");
	}

	public void testTest_Pod_pm()
	{
		doTest("Test_Pod_pm");
	}

	public void testTest_Requires_pm()
	{
		doTest("Test_Requires_pm");
	}

	public void testTest_Script_pm()
	{
		doTest("Test_Script_pm");
	}

	public void testTest_Simple_pm()
	{
		doTest("Test_Simple_pm");
	}

	public void testTest_SubCalls_pm()
	{
		doTest("Test_SubCalls_pm");
	}

	public void testTest_Tester_CaptureRunner_pm()
	{
		doTest("Test_Tester_CaptureRunner_pm");
	}

	public void testTest_Tester_Capture_pm()
	{
		doTest("Test_Tester_Capture_pm");
	}

	public void testTest_Tester_Delegate_pm()
	{
		doTest("Test_Tester_Delegate_pm");
	}

	public void testTest_Tester_pm()
	{
		doTest("Test_Tester_pm");
	}

	public void testTest_Trap_Builder_PerlIO_pm()
	{
		doTest("Test_Trap_Builder_PerlIO_pm");
	}

	public void testTest_Trap_Builder_SystemSafe_pm()
	{
		doTest("Test_Trap_Builder_SystemSafe_pm");
	}

	public void testTest_Trap_Builder_TempFile_pm()
	{
		doTest("Test_Trap_Builder_TempFile_pm");
	}

	public void testTest_Trap_Builder_pm()
	{
		doTest("Test_Trap_Builder_pm");
	}

	public void testTest_Trap_pm()
	{
		doTest("Test_Trap_pm");
	}

	public void testTest_UseAllModules_pm()
	{
		doTest("Test_UseAllModules_pm");
	}

	public void testTest_Warn_pm()
	{
		doTest("Test_Warn_pm");
	}

	public void testTest_Warnings_pm()
	{
		doTest("Test_Warnings_pm");
	}

	public void testTest_Without_Module_pm()
	{
		doTest("Test_Without_Module_pm");
	}

	public void testTest_YAML_pm()
	{
		doTest("Test_YAML_pm");
	}

	public void testTest_pm()
	{
		doTest("Test_pm");
	}

	public void testTest_use_ok_pm()
	{
		doTest("Test_use_ok_pm");
	}

	public void testText_Abbrev_pm()
	{
		doTest("Text_Abbrev_pm");
	}

	public void testText_Balanced_pm()
	{
		doTest("Text_Balanced_pm");
	}

	public void testText_CSV_PP_pm()
	{
		doTest("Text_CSV_PP_pm");
	}

	public void testText_CSV_XS_pm()
	{
		doTest("Text_CSV_XS_pm");
	}

	public void testText_CSV_pm()
	{
		doTest("Text_CSV_pm");
	}

	public void testText_Diff_Config_pm()
	{
		doTest("Text_Diff_Config_pm");
	}

	public void testText_Diff_Table_pm()
	{
		doTest("Text_Diff_Table_pm");
	}

	public void testText_Diff_pm()
	{
		doTest("Text_Diff_pm");
	}

	public void testText_Glob_pm()
	{
		doTest("Text_Glob_pm");
	}

	public void testText_LineFold_pm()
	{
		doTest("Text_LineFold_pm");
	}

	public void testText_ParseWords_pm()
	{
		doTest("Text_ParseWords_pm");
	}

	public void testText_Patch_pm()
	{
		doTest("Text_Patch_pm");
	}

	public void testText_Soundex_pm()
	{
		doTest("Text_Soundex_pm");
	}

	public void testText_Tabs_pm()
	{
		doTest("Text_Tabs_pm");
	}

	public void testText_Template_Preprocess_pm()
	{
		doTest("Text_Template_Preprocess_pm");
	}

	public void testText_Template_pm()
	{
		doTest("Text_Template_pm");
	}

	public void testText_Wrap_pm()
	{
		doTest("Text_Wrap_pm");
	}

	public void testThread_Queue_pm()
	{
		doTest("Thread_Queue_pm");
	}

	public void testThread_Semaphore_pm()
	{
		doTest("Thread_Semaphore_pm");
	}

	public void testThread_pm()
	{
		doTest("Thread_pm");
	}

	public void testThrowable_Error_pm()
	{
		doTest("Throwable_Error_pm");
	}

	public void testThrowable_pm()
	{
		doTest("Throwable_pm");
	}

	public void testTie_Array_CSV_HoldRow_pm()
	{
		doTest("Tie_Array_CSV_HoldRow_pm");
	}

	public void testTie_Array_CSV_pm()
	{
		doTest("Tie_Array_CSV_pm");
	}

	public void testTie_Array_pm()
	{
		doTest("Tie_Array_pm");
	}

	public void testTie_EncryptedHash_pm()
	{
		doTest("Tie_EncryptedHash_pm");
	}

	public void testTie_File_pm()
	{
		doTest("Tie_File_pm");
	}

	public void testTie_Handle_pm()
	{
		doTest("Tie_Handle_pm");
	}

	public void testTie_Hash_NamedCapture_pm()
	{
		doTest("Tie_Hash_NamedCapture_pm");
	}

	public void testTie_Hash_pm()
	{
		doTest("Tie_Hash_pm");
	}

	public void testTie_IxHash_pm()
	{
		doTest("Tie_IxHash_pm");
	}

	public void testTie_Memoize_pm()
	{
		doTest("Tie_Memoize_pm");
	}

	public void testTie_RefHash_pm()
	{
		doTest("Tie_RefHash_pm");
	}

	public void testTie_Registry_pm()
	{
		doTest("Tie_Registry_pm");
	}

	public void testTie_Scalar_pm()
	{
		doTest("Tie_Scalar_pm");
	}

	public void testTie_StdHandle_pm()
	{
		doTest("Tie_StdHandle_pm");
	}

	public void testTie_SubstrHash_pm()
	{
		doTest("Tie_SubstrHash_pm");
	}

	public void testTime_HiRes_pm()
	{
		doTest("Time_HiRes_pm");
	}

	public void testTime_Local_pm()
	{
		doTest("Time_Local_pm");
	}

	public void testTime_Piece_pm()
	{
		doTest("Time_Piece_pm");
	}

	public void testTime_Seconds_pm()
	{
		doTest("Time_Seconds_pm");
	}

	public void testTime_Zone_pm()
	{
		doTest("Time_Zone_pm");
	}

	public void testTime_gmtime_pm()
	{
		doTest("Time_gmtime_pm");
	}

	public void testTime_localtime_pm()
	{
		doTest("Time_localtime_pm");
	}

	public void testTime_tm_pm()
	{
		doTest("Time_tm_pm");
	}

	public void testTree_DAG_Node_pm()
	{
		doTest("Tree_DAG_Node_pm");
	}

	public void testTry_Tiny_pm()
	{
		doTest("Try_Tiny_pm");
	}

	public void testTypes_Serialiser_Error_pm()
	{
		doTest("Types_Serialiser_Error_pm");
	}

	public void testTypes_Serialiser_pm()
	{
		doTest("Types_Serialiser_pm");
	}

	public void testUNIVERSAL_pm()
	{
		doTest("UNIVERSAL_pm");
	}

	public void testURI_Escape_XS_pm()
	{
		doTest("URI_Escape_XS_pm");
	}

	public void testURI_Escape_pm()
	{
		doTest("URI_Escape_pm");
	}

	public void testURI_Heuristic_pm()
	{
		doTest("URI_Heuristic_pm");
	}

	public void testURI_IRI_pm()
	{
		doTest("URI_IRI_pm");
	}

	public void testURI_QueryParam_pm()
	{
		doTest("URI_QueryParam_pm");
	}

	public void testURI_Split_pm()
	{
		doTest("URI_Split_pm");
	}

	public void testURI_URL_pm()
	{
		doTest("URI_URL_pm");
	}

	public void testURI_WithBase_pm()
	{
		doTest("URI_WithBase_pm");
	}

	public void testURI__foreign_pm()
	{
		doTest("URI__foreign_pm");
	}

	public void testURI__generic_pm()
	{
		doTest("URI__generic_pm");
	}

	public void testURI__idna_pm()
	{
		doTest("URI__idna_pm");
	}

	public void testURI__ldap_pm()
	{
		doTest("URI__ldap_pm");
	}

	public void testURI__login_pm()
	{
		doTest("URI__login_pm");
	}

	public void testURI__punycode_pm()
	{
		doTest("URI__punycode_pm");
	}

	public void testURI__query_pm()
	{
		doTest("URI__query_pm");
	}

	public void testURI__segment_pm()
	{
		doTest("URI__segment_pm");
	}

	public void testURI__server_pm()
	{
		doTest("URI__server_pm");
	}

	public void testURI__userpass_pm()
	{
		doTest("URI__userpass_pm");
	}

	public void testURI_data_pm()
	{
		doTest("URI_data_pm");
	}

	public void testURI_file_Base_pm()
	{
		doTest("URI_file_Base_pm");
	}

	public void testURI_file_FAT_pm()
	{
		doTest("URI_file_FAT_pm");
	}

	public void testURI_file_Mac_pm()
	{
		doTest("URI_file_Mac_pm");
	}

	public void testURI_file_OS2_pm()
	{
		doTest("URI_file_OS2_pm");
	}

	public void testURI_file_QNX_pm()
	{
		doTest("URI_file_QNX_pm");
	}

	public void testURI_file_Unix_pm()
	{
		doTest("URI_file_Unix_pm");
	}

	public void testURI_file_Win32_pm()
	{
		doTest("URI_file_Win32_pm");
	}

	public void testURI_file_pm()
	{
		doTest("URI_file_pm");
	}

	public void testURI_ftp_pm()
	{
		doTest("URI_ftp_pm");
	}

	public void testURI_gopher_pm()
	{
		doTest("URI_gopher_pm");
	}

	public void testURI_http_pm()
	{
		doTest("URI_http_pm");
	}

	public void testURI_https_pm()
	{
		doTest("URI_https_pm");
	}

	public void testURI_ldap_pm()
	{
		doTest("URI_ldap_pm");
	}

	public void testURI_ldapi_pm()
	{
		doTest("URI_ldapi_pm");
	}

	public void testURI_ldaps_pm()
	{
		doTest("URI_ldaps_pm");
	}

	public void testURI_mailto_pm()
	{
		doTest("URI_mailto_pm");
	}

	public void testURI_mms_pm()
	{
		doTest("URI_mms_pm");
	}

	public void testURI_news_pm()
	{
		doTest("URI_news_pm");
	}

	public void testURI_nntp_pm()
	{
		doTest("URI_nntp_pm");
	}

	public void testURI_pm()
	{
		doTest("URI_pm");
	}

	public void testURI_pop_pm()
	{
		doTest("URI_pop_pm");
	}

	public void testURI_rlogin_pm()
	{
		doTest("URI_rlogin_pm");
	}

	public void testURI_rsync_pm()
	{
		doTest("URI_rsync_pm");
	}

	public void testURI_rtsp_pm()
	{
		doTest("URI_rtsp_pm");
	}

	public void testURI_rtspu_pm()
	{
		doTest("URI_rtspu_pm");
	}

	public void testURI_sip_pm()
	{
		doTest("URI_sip_pm");
	}

	public void testURI_sips_pm()
	{
		doTest("URI_sips_pm");
	}

	public void testURI_snews_pm()
	{
		doTest("URI_snews_pm");
	}

	public void testURI_ssh_pm()
	{
		doTest("URI_ssh_pm");
	}

	public void testURI_telnet_pm()
	{
		doTest("URI_telnet_pm");
	}

	public void testURI_tn3270_pm()
	{
		doTest("URI_tn3270_pm");
	}

	public void testURI_urn_isbn_pm()
	{
		doTest("URI_urn_isbn_pm");
	}

	public void testURI_urn_oid_pm()
	{
		doTest("URI_urn_oid_pm");
	}

	public void testURI_urn_pm()
	{
		doTest("URI_urn_pm");
	}

	public void testUnicode_Collate_CJK_Big5_pm()
	{
		doTest("Unicode_Collate_CJK_Big5_pm");
	}

	public void testUnicode_Collate_CJK_GB2312_pm()
	{
		doTest("Unicode_Collate_CJK_GB2312_pm");
	}

	public void testUnicode_Collate_CJK_JISX0208_pm()
	{
		doTest("Unicode_Collate_CJK_JISX0208_pm");
	}

	public void testUnicode_Collate_CJK_Korean_pm()
	{
		doTest("Unicode_Collate_CJK_Korean_pm");
	}

	public void testUnicode_Collate_CJK_Pinyin_pm()
	{
		doTest("Unicode_Collate_CJK_Pinyin_pm");
	}

	public void testUnicode_Collate_CJK_Stroke_pm()
	{
		doTest("Unicode_Collate_CJK_Stroke_pm");
	}

	public void testUnicode_Collate_CJK_Zhuyin_pm()
	{
		doTest("Unicode_Collate_CJK_Zhuyin_pm");
	}

	public void testUnicode_Collate_Locale_af_pl()
	{
		doTest("Unicode_Collate_Locale_af_pl");
	}

	public void testUnicode_Collate_Locale_ar_pl()
	{
		doTest("Unicode_Collate_Locale_ar_pl");
	}

	public void testUnicode_Collate_Locale_as_pl()
	{
		doTest("Unicode_Collate_Locale_as_pl");
	}

	public void testUnicode_Collate_Locale_az_pl()
	{
		doTest("Unicode_Collate_Locale_az_pl");
	}

	public void testUnicode_Collate_Locale_be_pl()
	{
		doTest("Unicode_Collate_Locale_be_pl");
	}

	public void testUnicode_Collate_Locale_bg_pl()
	{
		doTest("Unicode_Collate_Locale_bg_pl");
	}

	public void testUnicode_Collate_Locale_bn_pl()
	{
		doTest("Unicode_Collate_Locale_bn_pl");
	}

	public void testUnicode_Collate_Locale_ca_pl()
	{
		doTest("Unicode_Collate_Locale_ca_pl");
	}

	public void testUnicode_Collate_Locale_cs_pl()
	{
		doTest("Unicode_Collate_Locale_cs_pl");
	}

	public void testUnicode_Collate_Locale_cy_pl()
	{
		doTest("Unicode_Collate_Locale_cy_pl");
	}

	public void testUnicode_Collate_Locale_da_pl()
	{
		doTest("Unicode_Collate_Locale_da_pl");
	}

	public void testUnicode_Collate_Locale_de_phone_pl()
	{
		doTest("Unicode_Collate_Locale_de_phone_pl");
	}

	public void testUnicode_Collate_Locale_ee_pl()
	{
		doTest("Unicode_Collate_Locale_ee_pl");
	}

	public void testUnicode_Collate_Locale_eo_pl()
	{
		doTest("Unicode_Collate_Locale_eo_pl");
	}

	public void testUnicode_Collate_Locale_es_pl()
	{
		doTest("Unicode_Collate_Locale_es_pl");
	}

	public void testUnicode_Collate_Locale_es_trad_pl()
	{
		doTest("Unicode_Collate_Locale_es_trad_pl");
	}

	public void testUnicode_Collate_Locale_et_pl()
	{
		doTest("Unicode_Collate_Locale_et_pl");
	}

	public void testUnicode_Collate_Locale_fa_pl()
	{
		doTest("Unicode_Collate_Locale_fa_pl");
	}

	public void testUnicode_Collate_Locale_fi_phone_pl()
	{
		doTest("Unicode_Collate_Locale_fi_phone_pl");
	}

	public void testUnicode_Collate_Locale_fi_pl()
	{
		doTest("Unicode_Collate_Locale_fi_pl");
	}

	public void testUnicode_Collate_Locale_fil_pl()
	{
		doTest("Unicode_Collate_Locale_fil_pl");
	}

	public void testUnicode_Collate_Locale_fo_pl()
	{
		doTest("Unicode_Collate_Locale_fo_pl");
	}

	public void testUnicode_Collate_Locale_fr_pl()
	{
		doTest("Unicode_Collate_Locale_fr_pl");
	}

	public void testUnicode_Collate_Locale_gu_pl()
	{
		doTest("Unicode_Collate_Locale_gu_pl");
	}

	public void testUnicode_Collate_Locale_ha_pl()
	{
		doTest("Unicode_Collate_Locale_ha_pl");
	}

	public void testUnicode_Collate_Locale_haw_pl()
	{
		doTest("Unicode_Collate_Locale_haw_pl");
	}

	public void testUnicode_Collate_Locale_hi_pl()
	{
		doTest("Unicode_Collate_Locale_hi_pl");
	}

	public void testUnicode_Collate_Locale_hr_pl()
	{
		doTest("Unicode_Collate_Locale_hr_pl");
	}

	public void testUnicode_Collate_Locale_hu_pl()
	{
		doTest("Unicode_Collate_Locale_hu_pl");
	}

	public void testUnicode_Collate_Locale_hy_pl()
	{
		doTest("Unicode_Collate_Locale_hy_pl");
	}

	public void testUnicode_Collate_Locale_ig_pl()
	{
		doTest("Unicode_Collate_Locale_ig_pl");
	}

	public void testUnicode_Collate_Locale_is_pl()
	{
		doTest("Unicode_Collate_Locale_is_pl");
	}

	public void testUnicode_Collate_Locale_ja_pl()
	{
		doTest("Unicode_Collate_Locale_ja_pl");
	}

	public void testUnicode_Collate_Locale_kk_pl()
	{
		doTest("Unicode_Collate_Locale_kk_pl");
	}

	public void testUnicode_Collate_Locale_kl_pl()
	{
		doTest("Unicode_Collate_Locale_kl_pl");
	}

	public void testUnicode_Collate_Locale_kn_pl()
	{
		doTest("Unicode_Collate_Locale_kn_pl");
	}

	public void testUnicode_Collate_Locale_ko_pl()
	{
		doTest("Unicode_Collate_Locale_ko_pl");
	}

	public void testUnicode_Collate_Locale_kok_pl()
	{
		doTest("Unicode_Collate_Locale_kok_pl");
	}

	public void testUnicode_Collate_Locale_ln_pl()
	{
		doTest("Unicode_Collate_Locale_ln_pl");
	}

	public void testUnicode_Collate_Locale_lt_pl()
	{
		doTest("Unicode_Collate_Locale_lt_pl");
	}

	public void testUnicode_Collate_Locale_lv_pl()
	{
		doTest("Unicode_Collate_Locale_lv_pl");
	}

	public void testUnicode_Collate_Locale_mk_pl()
	{
		doTest("Unicode_Collate_Locale_mk_pl");
	}

	public void testUnicode_Collate_Locale_ml_pl()
	{
		doTest("Unicode_Collate_Locale_ml_pl");
	}

	public void testUnicode_Collate_Locale_mr_pl()
	{
		doTest("Unicode_Collate_Locale_mr_pl");
	}

	public void testUnicode_Collate_Locale_mt_pl()
	{
		doTest("Unicode_Collate_Locale_mt_pl");
	}

	public void testUnicode_Collate_Locale_nb_pl()
	{
		doTest("Unicode_Collate_Locale_nb_pl");
	}

	public void testUnicode_Collate_Locale_nn_pl()
	{
		doTest("Unicode_Collate_Locale_nn_pl");
	}

	public void testUnicode_Collate_Locale_nso_pl()
	{
		doTest("Unicode_Collate_Locale_nso_pl");
	}

	public void testUnicode_Collate_Locale_om_pl()
	{
		doTest("Unicode_Collate_Locale_om_pl");
	}

	public void testUnicode_Collate_Locale_or_pl()
	{
		doTest("Unicode_Collate_Locale_or_pl");
	}

	public void testUnicode_Collate_Locale_pa_pl()
	{
		doTest("Unicode_Collate_Locale_pa_pl");
	}

	public void testUnicode_Collate_Locale_pl_pl()
	{
		doTest("Unicode_Collate_Locale_pl_pl");
	}

	public void testUnicode_Collate_Locale_pm()
	{
		doTest("Unicode_Collate_Locale_pm");
	}

	public void testUnicode_Collate_Locale_ro_pl()
	{
		doTest("Unicode_Collate_Locale_ro_pl");
	}

	public void testUnicode_Collate_Locale_ru_pl()
	{
		doTest("Unicode_Collate_Locale_ru_pl");
	}

	public void testUnicode_Collate_Locale_sa_pl()
	{
		doTest("Unicode_Collate_Locale_sa_pl");
	}

	public void testUnicode_Collate_Locale_se_pl()
	{
		doTest("Unicode_Collate_Locale_se_pl");
	}

	public void testUnicode_Collate_Locale_si_dict_pl()
	{
		doTest("Unicode_Collate_Locale_si_dict_pl");
	}

	public void testUnicode_Collate_Locale_si_pl()
	{
		doTest("Unicode_Collate_Locale_si_pl");
	}

	public void testUnicode_Collate_Locale_sk_pl()
	{
		doTest("Unicode_Collate_Locale_sk_pl");
	}

	public void testUnicode_Collate_Locale_sl_pl()
	{
		doTest("Unicode_Collate_Locale_sl_pl");
	}

	public void testUnicode_Collate_Locale_sq_pl()
	{
		doTest("Unicode_Collate_Locale_sq_pl");
	}

	public void testUnicode_Collate_Locale_sr_pl()
	{
		doTest("Unicode_Collate_Locale_sr_pl");
	}

	public void testUnicode_Collate_Locale_sv_pl()
	{
		doTest("Unicode_Collate_Locale_sv_pl");
	}

	public void testUnicode_Collate_Locale_sv_refo_pl()
	{
		doTest("Unicode_Collate_Locale_sv_refo_pl");
	}

	public void testUnicode_Collate_Locale_ta_pl()
	{
		doTest("Unicode_Collate_Locale_ta_pl");
	}

	public void testUnicode_Collate_Locale_te_pl()
	{
		doTest("Unicode_Collate_Locale_te_pl");
	}

	public void testUnicode_Collate_Locale_th_pl()
	{
		doTest("Unicode_Collate_Locale_th_pl");
	}

	public void testUnicode_Collate_Locale_tn_pl()
	{
		doTest("Unicode_Collate_Locale_tn_pl");
	}

	public void testUnicode_Collate_Locale_to_pl()
	{
		doTest("Unicode_Collate_Locale_to_pl");
	}

	public void testUnicode_Collate_Locale_tr_pl()
	{
		doTest("Unicode_Collate_Locale_tr_pl");
	}

	public void testUnicode_Collate_Locale_uk_pl()
	{
		doTest("Unicode_Collate_Locale_uk_pl");
	}

	public void testUnicode_Collate_Locale_ur_pl()
	{
		doTest("Unicode_Collate_Locale_ur_pl");
	}

	public void testUnicode_Collate_Locale_vi_pl()
	{
		doTest("Unicode_Collate_Locale_vi_pl");
	}

	public void testUnicode_Collate_Locale_wae_pl()
	{
		doTest("Unicode_Collate_Locale_wae_pl");
	}

	public void testUnicode_Collate_Locale_wo_pl()
	{
		doTest("Unicode_Collate_Locale_wo_pl");
	}

	public void testUnicode_Collate_Locale_yo_pl()
	{
		doTest("Unicode_Collate_Locale_yo_pl");
	}

	public void testUnicode_Collate_Locale_zh_big5_pl()
	{
		doTest("Unicode_Collate_Locale_zh_big5_pl");
	}

	public void testUnicode_Collate_Locale_zh_gb_pl()
	{
		doTest("Unicode_Collate_Locale_zh_gb_pl");
	}

	public void testUnicode_Collate_Locale_zh_pin_pl()
	{
		doTest("Unicode_Collate_Locale_zh_pin_pl");
	}

	public void testUnicode_Collate_Locale_zh_pl()
	{
		doTest("Unicode_Collate_Locale_zh_pl");
	}

	public void testUnicode_Collate_Locale_zh_strk_pl()
	{
		doTest("Unicode_Collate_Locale_zh_strk_pl");
	}

	public void testUnicode_Collate_Locale_zh_zhu_pl()
	{
		doTest("Unicode_Collate_Locale_zh_zhu_pl");
	}

	public void testUnicode_Collate_pm()
	{
		doTest("Unicode_Collate_pm");
	}

	public void testUnicode_GCString_pm()
	{
		doTest("Unicode_GCString_pm");
	}

	public void testUnicode_LineBreak_Constants_pm()
	{
		doTest("Unicode_LineBreak_Constants_pm");
	}

	public void testUnicode_LineBreak_pm()
	{
		doTest("Unicode_LineBreak_pm");
	}

	public void testUnicode_Normalize_pm()
	{
		doTest("Unicode_Normalize_pm");
	}

	public void testUnicode_UCD_pm()
	{
		doTest("Unicode_UCD_pm");
	}

	public void testUnicode_UTF8_pm()
	{
		doTest("Unicode_UTF8_pm");
	}

	public void testUser_grent_pm()
	{
		doTest("User_grent_pm");
	}

	public void testUser_pwent_pm()
	{
		doTest("User_pwent_pm");
	}

	public void testV_pm()
	{
		doTest("V_pm");
	}

	public void testVariable_Magic_pm()
	{
		doTest("Variable_Magic_pm");
	}

	public void testWWW_Mechanize_Image_pm()
	{
		doTest("WWW_Mechanize_Image_pm");
	}

	public void testWWW_Mechanize_Link_pm()
	{
		doTest("WWW_Mechanize_Link_pm");
	}

	public void testWWW_Mechanize_pm()
	{
		doTest("WWW_Mechanize_pm");
	}

	public void testWWW_RobotRules_AnyDBM_File_pm()
	{
		doTest("WWW_RobotRules_AnyDBM_File_pm");
	}

	public void testWWW_RobotRules_pm()
	{
		doTest("WWW_RobotRules_pm");
	}

	public void testWin32API_File_pm()
	{
		doTest("Win32API_File_pm");
	}

	public void testWin32API_Registry_pm()
	{
		doTest("Win32API_Registry_pm");
	}

	public void testWin32CORE_pm()
	{
		doTest("Win32CORE_pm");
	}

	public void testWin32_API_Callback_pm()
	{
		doTest("Win32_API_Callback_pm");
	}

	public void testWin32_API_Struct_pm()
	{
		doTest("Win32_API_Struct_pm");
	}

	public void testWin32_API_Test_pm()
	{
		doTest("Win32_API_Test_pm");
	}

	public void testWin32_API_Type_pm()
	{
		doTest("Win32_API_Type_pm");
	}

	public void testWin32_API_pm()
	{
		doTest("Win32_API_pm");
	}

	public void testWin32_Console_ANSI_pm()
	{
		doTest("Win32_Console_ANSI_pm");
	}

	public void testWin32_Console_pm()
	{
		doTest("Win32_Console_pm");
	}

	public void testWin32_DBIODBC_pm()
	{
		doTest("Win32_DBIODBC_pm");
	}

	public void testWin32_Daemon_pm()
	{
		doTest("Win32_Daemon_pm");
	}

	public void testWin32_EventLog_pm()
	{
		doTest("Win32_EventLog_pm");
	}

	public void testWin32_File_Object_pm()
	{
		doTest("Win32_File_Object_pm");
	}

	public void testWin32_File_pm()
	{
		doTest("Win32_File_pm");
	}

	public void testWin32_GuiTest_Cmd_pm()
	{
		doTest("Win32_GuiTest_Cmd_pm");
	}

	public void testWin32_GuiTest_Examples_pm()
	{
		doTest("Win32_GuiTest_Examples_pm");
	}

	public void testWin32_GuiTest_pm()
	{
		doTest("Win32_GuiTest_pm");
	}

	public void testWin32_IPHelper_pm()
	{
		doTest("Win32_IPHelper_pm");
	}

	public void testWin32_Job_pm()
	{
		doTest("Win32_Job_pm");
	}

	public void testWin32_OLE_Const_pm()
	{
		doTest("Win32_OLE_Const_pm");
	}

	public void testWin32_OLE_Enum_pm()
	{
		doTest("Win32_OLE_Enum_pm");
	}

	public void testWin32_OLE_Lite_pm()
	{
		doTest("Win32_OLE_Lite_pm");
	}

	public void testWin32_OLE_NLS_pm()
	{
		doTest("Win32_OLE_NLS_pm");
	}

	public void testWin32_OLE_TypeInfo_pm()
	{
		doTest("Win32_OLE_TypeInfo_pm");
	}

	public void testWin32_OLE_Variant_pm()
	{
		doTest("Win32_OLE_Variant_pm");
	}

	public void testWin32_OLE_pm()
	{
		doTest("Win32_OLE_pm");
	}

	public void testWin32_Pipe_pm()
	{
		doTest("Win32_Pipe_pm");
	}

	public void testWin32_Process_pm()
	{
		doTest("Win32_Process_pm");
	}

	public void testWin32_ServiceManager_pm()
	{
		doTest("Win32_ServiceManager_pm");
	}

	public void testWin32_Service_pm()
	{
		doTest("Win32_Service_pm");
	}

	public void testWin32_ShellQuote_pm()
	{
		doTest("Win32_ShellQuote_pm");
	}

	public void testWin32_TieRegistry_pm()
	{
		doTest("Win32_TieRegistry_pm");
	}

	public void testWin32_UTCFileTime_pm()
	{
		doTest("Win32_UTCFileTime_pm");
	}

	public void testWin32_WinError_pm()
	{
		doTest("Win32_WinError_pm");
	}

	public void testWin32_pm()
	{
		doTest("Win32_pm");
	}

	public void testXML_LibXML_AttributeHash_pm()
	{
		doTest("XML_LibXML_AttributeHash_pm");
	}

	public void testXML_LibXML_Boolean_pm()
	{
		doTest("XML_LibXML_Boolean_pm");
	}

	public void testXML_LibXML_Common_pm()
	{
		doTest("XML_LibXML_Common_pm");
	}

	public void testXML_LibXML_Devel_pm()
	{
		doTest("XML_LibXML_Devel_pm");
	}

	public void testXML_LibXML_ErrNo_pm()
	{
		doTest("XML_LibXML_ErrNo_pm");
	}

	public void testXML_LibXML_Error_pm()
	{
		doTest("XML_LibXML_Error_pm");
	}

	public void testXML_LibXML_Literal_pm()
	{
		doTest("XML_LibXML_Literal_pm");
	}

	public void testXML_LibXML_NodeList_pm()
	{
		doTest("XML_LibXML_NodeList_pm");
	}

	public void testXML_LibXML_Number_pm()
	{
		doTest("XML_LibXML_Number_pm");
	}

	public void testXML_LibXML_Reader_pm()
	{
		doTest("XML_LibXML_Reader_pm");
	}

	public void testXML_LibXML_SAX_Builder_pm()
	{
		doTest("XML_LibXML_SAX_Builder_pm");
	}

	public void testXML_LibXML_SAX_Generator_pm()
	{
		doTest("XML_LibXML_SAX_Generator_pm");
	}

	public void testXML_LibXML_SAX_Parser_pm()
	{
		doTest("XML_LibXML_SAX_Parser_pm");
	}

	public void testXML_LibXML_SAX_pm()
	{
		doTest("XML_LibXML_SAX_pm");
	}

	public void testXML_LibXML_XPathContext_pm()
	{
		doTest("XML_LibXML_XPathContext_pm");
	}

	public void testXML_LibXML_pm()
	{
		doTest("XML_LibXML_pm");
	}

	public void testXML_LibXSLT_pm()
	{
		doTest("XML_LibXSLT_pm");
	}

	public void testXML_NamespaceSupport_pm()
	{
		doTest("XML_NamespaceSupport_pm");
	}

	public void testXML_Parser_Expat_pm()
	{
		doTest("XML_Parser_Expat_pm");
	}

	public void testXML_Parser_LWPExternEnt_pl()
	{
		doTest("XML_Parser_LWPExternEnt_pl");
	}

	public void testXML_Parser_Lite_pm()
	{
		doTest("XML_Parser_Lite_pm");
	}

	public void testXML_Parser_Style_Debug_pm()
	{
		doTest("XML_Parser_Style_Debug_pm");
	}

	public void testXML_Parser_Style_Objects_pm()
	{
		doTest("XML_Parser_Style_Objects_pm");
	}

	public void testXML_Parser_Style_Stream_pm()
	{
		doTest("XML_Parser_Style_Stream_pm");
	}

	public void testXML_Parser_Style_Subs_pm()
	{
		doTest("XML_Parser_Style_Subs_pm");
	}

	public void testXML_Parser_Style_Tree_pm()
	{
		doTest("XML_Parser_Style_Tree_pm");
	}

	public void testXML_Parser_pm()
	{
		doTest("XML_Parser_pm");
	}

	public void testXML_SAX_Base_pm()
	{
		doTest("XML_SAX_Base_pm");
	}

	public void testXML_SAX_BuildSAXBase_pl()
	{
		doTest("XML_SAX_BuildSAXBase_pl");
	}

	public void testXML_SAX_DocumentLocator_pm()
	{
		doTest("XML_SAX_DocumentLocator_pm");
	}

	public void testXML_SAX_Exception_pm()
	{
		doTest("XML_SAX_Exception_pm");
	}

	public void testXML_SAX_Expat_pm()
	{
		doTest("XML_SAX_Expat_pm");
	}

	public void testXML_SAX_ParserFactory_pm()
	{
		doTest("XML_SAX_ParserFactory_pm");
	}

	public void testXML_SAX_PurePerl_DTDDecls_pm()
	{
		doTest("XML_SAX_PurePerl_DTDDecls_pm");
	}

	public void testXML_SAX_PurePerl_DebugHandler_pm()
	{
		doTest("XML_SAX_PurePerl_DebugHandler_pm");
	}

	public void testXML_SAX_PurePerl_DocType_pm()
	{
		doTest("XML_SAX_PurePerl_DocType_pm");
	}

	public void testXML_SAX_PurePerl_EncodingDetect_pm()
	{
		doTest("XML_SAX_PurePerl_EncodingDetect_pm");
	}

	public void testXML_SAX_PurePerl_Exception_pm()
	{
		doTest("XML_SAX_PurePerl_Exception_pm");
	}

	public void testXML_SAX_PurePerl_NoUnicodeExt_pm()
	{
		doTest("XML_SAX_PurePerl_NoUnicodeExt_pm");
	}

	public void testXML_SAX_PurePerl_Productions_pm()
	{
		doTest("XML_SAX_PurePerl_Productions_pm");
	}

	public void testXML_SAX_PurePerl_Reader_NoUnicodeExt_pm()
	{
		doTest("XML_SAX_PurePerl_Reader_NoUnicodeExt_pm");
	}

	public void testXML_SAX_PurePerl_Reader_Stream_pm()
	{
		doTest("XML_SAX_PurePerl_Reader_Stream_pm");
	}

	public void testXML_SAX_PurePerl_Reader_String_pm()
	{
		doTest("XML_SAX_PurePerl_Reader_String_pm");
	}

	public void testXML_SAX_PurePerl_Reader_URI_pm()
	{
		doTest("XML_SAX_PurePerl_Reader_URI_pm");
	}

	public void testXML_SAX_PurePerl_Reader_UnicodeExt_pm()
	{
		doTest("XML_SAX_PurePerl_Reader_UnicodeExt_pm");
	}

	public void testXML_SAX_PurePerl_Reader_pm()
	{
		doTest("XML_SAX_PurePerl_Reader_pm");
	}

	public void testXML_SAX_PurePerl_UnicodeExt_pm()
	{
		doTest("XML_SAX_PurePerl_UnicodeExt_pm");
	}

	public void testXML_SAX_PurePerl_XMLDecl_pm()
	{
		doTest("XML_SAX_PurePerl_XMLDecl_pm");
	}

	public void testXML_SAX_PurePerl_pm()
	{
		doTest("XML_SAX_PurePerl_pm");
	}

	public void testXML_SAX_pm()
	{
		doTest("XML_SAX_pm");
	}

	public void testXML_Simple_pm()
	{
		doTest("XML_Simple_pm");
	}

	public void testXML_Twig_XPath_pm()
	{
		doTest("XML_Twig_XPath_pm");
	}

	public void testXML_Twig_pm()
	{
		doTest("XML_Twig_pm");
	}

	public void testXSLoader_pm()
	{
		doTest("XSLoader_pm");
	}

	public void testYAML_Any_pm()
	{
		doTest("YAML_Any_pm");
	}

	public void testYAML_Dumper_Base_pm()
	{
		doTest("YAML_Dumper_Base_pm");
	}

	public void testYAML_Dumper_pm()
	{
		doTest("YAML_Dumper_pm");
	}

	public void testYAML_Error_pm()
	{
		doTest("YAML_Error_pm");
	}

	public void testYAML_LibYAML_pm()
	{
		doTest("YAML_LibYAML_pm");
	}

	public void testYAML_Loader_Base_pm()
	{
		doTest("YAML_Loader_Base_pm");
	}

	public void testYAML_Loader_pm()
	{
		doTest("YAML_Loader_pm");
	}

	public void testYAML_Marshall_pm()
	{
		doTest("YAML_Marshall_pm");
	}

	public void testYAML_Mo_pm()
	{
		doTest("YAML_Mo_pm");
	}

	public void testYAML_Node_pm()
	{
		doTest("YAML_Node_pm");
	}

	public void testYAML_Tag_pm()
	{
		doTest("YAML_Tag_pm");
	}

	public void testYAML_Tiny_pm()
	{
		doTest("YAML_Tiny_pm");
	}

	public void testYAML_Types_pm()
	{
		doTest("YAML_Types_pm");
	}

	public void testYAML_XS_LibYAML_pm()
	{
		doTest("YAML_XS_LibYAML_pm");
	}

	public void testYAML_XS_pm()
	{
		doTest("YAML_XS_pm");
	}

	public void testYAML_pm()
	{
		doTest("YAML_pm");
	}

	public void test_charnames_pm()
	{
		doTest("_charnames_pm");
	}

	public void testaliased_pm()
	{
		doTest("aliased_pm");
	}

	public void testarybase_pm()
	{
		doTest("arybase_pm");
	}

	public void testattributes_pm()
	{
		doTest("attributes_pm");
	}

	public void testauto_DBD_Oracle_mk_pm()
	{
		doTest("auto_DBD_Oracle_mk_pm");
	}

	public void testautobox_pm()
	{
		doTest("autobox_pm");
	}

	public void testautobox_universal_pm()
	{
		doTest("autobox_universal_pm");
	}

	public void testautodie_ScopeUtil_pm()
	{
		doTest("autodie_ScopeUtil_pm");
	}

	public void testautodie_Scope_GuardStack_pm()
	{
		doTest("autodie_Scope_GuardStack_pm");
	}

	public void testautodie_Scope_Guard_pm()
	{
		doTest("autodie_Scope_Guard_pm");
	}

	public void testautodie_exception_pm()
	{
		doTest("autodie_exception_pm");
	}

	public void testautodie_exception_system_pm()
	{
		doTest("autodie_exception_system_pm");
	}

	public void testautodie_hints_pm()
	{
		doTest("autodie_hints_pm");
	}

	public void testautodie_pm()
	{
		doTest("autodie_pm");
	}

	public void testautodie_skip_pm()
	{
		doTest("autodie_skip_pm");
	}

	public void testautouse_pm()
	{
		doTest("autouse_pm");
	}

	public void testbareword_filehandles_pm()
	{
		doTest("bareword_filehandles_pm");
	}

	public void testbase_pm()
	{
		doTest("base_pm");
	}

	public void testbigint_pm()
	{
		doTest("bigint_pm");
	}

	public void testbignum_pm()
	{
		doTest("bignum_pm");
	}

	public void testbigrat_pm()
	{
		doTest("bigrat_pm");
	}

	public void testblib_pm()
	{
		doTest("blib_pm");
	}

	public void testbytes_heavy_pl()
	{
		doTest("bytes_heavy_pl");
	}

	public void testbytes_pm()
	{
		doTest("bytes_pm");
	}

	public void testcharnames_pm()
	{
		doTest("charnames_pm");
	}

	public void testcommon_sense_pm()
	{
		doTest("common_sense_pm");
	}

	public void testconstant_pm()
	{
		doTest("constant_pm");
	}

	public void testdbixs_rev_pl()
	{
		doTest("dbixs_rev_pl");
	}

	public void testdeprecate_pm()
	{
		doTest("deprecate_pm");
	}

	public void testdiagnostics_pm()
	{
		doTest("diagnostics_pm");
	}

	public void testdumpvar_pl()
	{
		doTest("dumpvar_pl");
	}

	public void testencoding_pm()
	{
		doTest("encoding_pm");
	}

	public void testencoding_warnings_pm()
	{
		doTest("encoding_warnings_pm");
	}

	public void testenum_pm()
	{
		doTest("enum_pm");
	}

	public void testexperimental_pm()
	{
		doTest("experimental_pm");
	}

	public void testfeature_pm()
	{
		doTest("feature_pm");
	}

	public void testfields_pm()
	{
		doTest("fields_pm");
	}

	public void testfiletest_pm()
	{
		doTest("filetest_pm");
	}

	public void testfoundation_pm()
	{
		doTest("foundation_pm");
	}

	public void testif_pm()
	{
		doTest("if_pm");
	}

	public void testindirect_pm()
	{
		doTest("indirect_pm");
	}

	public void testinteger_pm()
	{
		doTest("integer_pm");
	}

	public void testlatest_pm()
	{
		doTest("latest_pm");
	}

	public void testless_pm()
	{
		doTest("less_pm");
	}

	public void testlib_core_only_pm()
	{
		doTest("lib_core_only_pm");
	}

	public void testlib_pm()
	{
		doTest("lib_pm");
	}

	public void testlocal_lib_pm()
	{
		doTest("local_lib_pm");
	}

	public void testlocale_pm()
	{
		doTest("locale_pm");
	}

	public void testmeta_notation_pm()
	{
		doTest("meta_notation_pm");
	}

	public void testmetaclass_pm()
	{
		doTest("metaclass_pm");
	}

	public void testmkconsts_pl()
	{
		doTest("mkconsts_pl");
	}

	public void testmro_pm()
	{
		doTest("mro_pm");
	}

	public void testmultidimensional_pm()
	{
		doTest("multidimensional_pm");
	}

	public void testnamespace_autoclean_pm()
	{
		doTest("namespace_autoclean_pm");
	}

	public void testnamespace_clean_pm()
	{
		doTest("namespace_clean_pm");
	}

	public void testntheory_pm()
	{
		doTest("ntheory_pm");
	}

	public void testojo_pm()
	{
		doTest("ojo_pm");
	}

	public void testok_pm()
	{
		doTest("ok_pm");
	}

	public void testoo_pm()
	{
		doTest("oo_pm");
	}

	public void testoose_pm()
	{
		doTest("oose_pm");
	}

	public void testopen_pm()
	{
		doTest("open_pm");
	}

	public void testops_pm()
	{
		doTest("ops_pm");
	}

	public void testoverload_numbers_pm()
	{
		doTest("overload_numbers_pm");
	}

	public void testoverload_pm()
	{
		doTest("overload_pm");
	}

	public void testoverloading_pm()
	{
		doTest("overloading_pm");
	}

	public void testparent_pm()
	{
		doTest("parent_pm");
	}

	public void testperl5db_pl()
	{
		doTest("perl5db_pl");
	}

	public void testperlfaq_pm()
	{
		doTest("perlfaq_pm");
	}

	public void testpler_pm()
	{
		doTest("pler_pm");
	}

	public void testre_pm()
	{
		doTest("re_pm");
	}

	public void testscan_pl()
	{
		doTest("scan_pl");
	}

	public void testsigtrap_pm()
	{
		doTest("sigtrap_pm");
	}

	public void testsort_pm()
	{
		doTest("sort_pm");
	}

	public void teststrict_pm()
	{
		doTest("strict_pm");
	}

	public void teststrictures_extra_pm()
	{
		doTest("strictures_extra_pm");
	}

	public void teststrictures_pm()
	{
		doTest("strictures_pm");
	}

	public void testsubs_pm()
	{
		doTest("subs_pm");
	}

	public void testsyntax_pm()
	{
		doTest("syntax_pm");
	}

	public void testthreads_pm()
	{
		doTest("threads_pm");
	}

	public void testthreads_shared_pm()
	{
		doTest("threads_shared_pm");
	}

	public void testunicore_CombiningClass_pl()
	{
		doTest("unicore_CombiningClass_pl");
	}

	public void testunicore_Decomposition_pl()
	{
		doTest("unicore_Decomposition_pl");
	}

	public void testunicore_Heavy_pl()
	{
		doTest("unicore_Heavy_pl");
	}

	public void testunicore_Name_pl()
	{
		doTest("unicore_Name_pl");
	}

	public void testunicore_Name_pm()
	{
		doTest("unicore_Name_pm");
	}

	public void testunicore_To_Age_pl()
	{
		doTest("unicore_To_Age_pl");
	}

	public void testunicore_To_Bc_pl()
	{
		doTest("unicore_To_Bc_pl");
	}

	public void testunicore_To_Bmg_pl()
	{
		doTest("unicore_To_Bmg_pl");
	}

	public void testunicore_To_Bpb_pl()
	{
		doTest("unicore_To_Bpb_pl");
	}

	public void testunicore_To_Bpt_pl()
	{
		doTest("unicore_To_Bpt_pl");
	}

	public void testunicore_To_Cf_pl()
	{
		doTest("unicore_To_Cf_pl");
	}

	public void testunicore_To_Digit_pl()
	{
		doTest("unicore_To_Digit_pl");
	}

	public void testunicore_To_Ea_pl()
	{
		doTest("unicore_To_Ea_pl");
	}

	public void testunicore_To_Fold_pl()
	{
		doTest("unicore_To_Fold_pl");
	}

	public void testunicore_To_GCB_pl()
	{
		doTest("unicore_To_GCB_pl");
	}

	public void testunicore_To_Gc_pl()
	{
		doTest("unicore_To_Gc_pl");
	}

	public void testunicore_To_Hst_pl()
	{
		doTest("unicore_To_Hst_pl");
	}

	public void testunicore_To_Isc_pl()
	{
		doTest("unicore_To_Isc_pl");
	}

	public void testunicore_To_Jg_pl()
	{
		doTest("unicore_To_Jg_pl");
	}

	public void testunicore_To_Jt_pl()
	{
		doTest("unicore_To_Jt_pl");
	}

	public void testunicore_To_Lb_pl()
	{
		doTest("unicore_To_Lb_pl");
	}

	public void testunicore_To_Lc_pl()
	{
		doTest("unicore_To_Lc_pl");
	}

	public void testunicore_To_Lower_pl()
	{
		doTest("unicore_To_Lower_pl");
	}

	public void testunicore_To_NFCQC_pl()
	{
		doTest("unicore_To_NFCQC_pl");
	}

	public void testunicore_To_NFDQC_pl()
	{
		doTest("unicore_To_NFDQC_pl");
	}

	public void testunicore_To_NFKCCF_pl()
	{
		doTest("unicore_To_NFKCCF_pl");
	}

	public void testunicore_To_NFKCQC_pl()
	{
		doTest("unicore_To_NFKCQC_pl");
	}

	public void testunicore_To_NFKDQC_pl()
	{
		doTest("unicore_To_NFKDQC_pl");
	}

	public void testunicore_To_Na1_pl()
	{
		doTest("unicore_To_Na1_pl");
	}

	public void testunicore_To_NameAlia_pl()
	{
		doTest("unicore_To_NameAlia_pl");
	}

	public void testunicore_To_Nt_pl()
	{
		doTest("unicore_To_Nt_pl");
	}

	public void testunicore_To_Nv_pl()
	{
		doTest("unicore_To_Nv_pl");
	}

	public void testunicore_To_PerlDeci_pl()
	{
		doTest("unicore_To_PerlDeci_pl");
	}

	public void testunicore_To_SB_pl()
	{
		doTest("unicore_To_SB_pl");
	}

	public void testunicore_To_Sc_pl()
	{
		doTest("unicore_To_Sc_pl");
	}

	public void testunicore_To_Scx_pl()
	{
		doTest("unicore_To_Scx_pl");
	}

	public void testunicore_To_Tc_pl()
	{
		doTest("unicore_To_Tc_pl");
	}

	public void testunicore_To_Title_pl()
	{
		doTest("unicore_To_Title_pl");
	}

	public void testunicore_To_Uc_pl()
	{
		doTest("unicore_To_Uc_pl");
	}

	public void testunicore_To_Upper_pl()
	{
		doTest("unicore_To_Upper_pl");
	}

	public void testunicore_To_WB_pl()
	{
		doTest("unicore_To_WB_pl");
	}

	public void testunicore_UCD_pl()
	{
		doTest("unicore_UCD_pl");
	}

	public void testunicore_lib_Age_NA_pl()
	{
		doTest("unicore_lib_Age_NA_pl");
	}

	public void testunicore_lib_Age_V11_pl()
	{
		doTest("unicore_lib_Age_V11_pl");
	}

	public void testunicore_lib_Age_V20_pl()
	{
		doTest("unicore_lib_Age_V20_pl");
	}

	public void testunicore_lib_Age_V30_pl()
	{
		doTest("unicore_lib_Age_V30_pl");
	}

	public void testunicore_lib_Age_V31_pl()
	{
		doTest("unicore_lib_Age_V31_pl");
	}

	public void testunicore_lib_Age_V32_pl()
	{
		doTest("unicore_lib_Age_V32_pl");
	}

	public void testunicore_lib_Age_V40_pl()
	{
		doTest("unicore_lib_Age_V40_pl");
	}

	public void testunicore_lib_Age_V41_pl()
	{
		doTest("unicore_lib_Age_V41_pl");
	}

	public void testunicore_lib_Age_V50_pl()
	{
		doTest("unicore_lib_Age_V50_pl");
	}

	public void testunicore_lib_Age_V51_pl()
	{
		doTest("unicore_lib_Age_V51_pl");
	}

	public void testunicore_lib_Age_V52_pl()
	{
		doTest("unicore_lib_Age_V52_pl");
	}

	public void testunicore_lib_Age_V60_pl()
	{
		doTest("unicore_lib_Age_V60_pl");
	}

	public void testunicore_lib_Age_V61_pl()
	{
		doTest("unicore_lib_Age_V61_pl");
	}

	public void testunicore_lib_Age_V70_pl()
	{
		doTest("unicore_lib_Age_V70_pl");
	}

	public void testunicore_lib_Alpha_Y_pl()
	{
		doTest("unicore_lib_Alpha_Y_pl");
	}

	public void testunicore_lib_Bc_AL_pl()
	{
		doTest("unicore_lib_Bc_AL_pl");
	}

	public void testunicore_lib_Bc_AN_pl()
	{
		doTest("unicore_lib_Bc_AN_pl");
	}

	public void testunicore_lib_Bc_BN_pl()
	{
		doTest("unicore_lib_Bc_BN_pl");
	}

	public void testunicore_lib_Bc_B_pl()
	{
		doTest("unicore_lib_Bc_B_pl");
	}

	public void testunicore_lib_Bc_CS_pl()
	{
		doTest("unicore_lib_Bc_CS_pl");
	}

	public void testunicore_lib_Bc_EN_pl()
	{
		doTest("unicore_lib_Bc_EN_pl");
	}

	public void testunicore_lib_Bc_ES_pl()
	{
		doTest("unicore_lib_Bc_ES_pl");
	}

	public void testunicore_lib_Bc_ET_pl()
	{
		doTest("unicore_lib_Bc_ET_pl");
	}

	public void testunicore_lib_Bc_L_pl()
	{
		doTest("unicore_lib_Bc_L_pl");
	}

	public void testunicore_lib_Bc_NSM_pl()
	{
		doTest("unicore_lib_Bc_NSM_pl");
	}

	public void testunicore_lib_Bc_ON_pl()
	{
		doTest("unicore_lib_Bc_ON_pl");
	}

	public void testunicore_lib_Bc_R_pl()
	{
		doTest("unicore_lib_Bc_R_pl");
	}

	public void testunicore_lib_Bc_WS_pl()
	{
		doTest("unicore_lib_Bc_WS_pl");
	}

	public void testunicore_lib_BidiC_Y_pl()
	{
		doTest("unicore_lib_BidiC_Y_pl");
	}

	public void testunicore_lib_BidiM_Y_pl()
	{
		doTest("unicore_lib_BidiM_Y_pl");
	}

	public void testunicore_lib_Blk_NB_pl()
	{
		doTest("unicore_lib_Blk_NB_pl");
	}

	public void testunicore_lib_Bpt_C_pl()
	{
		doTest("unicore_lib_Bpt_C_pl");
	}

	public void testunicore_lib_Bpt_N_pl()
	{
		doTest("unicore_lib_Bpt_N_pl");
	}

	public void testunicore_lib_Bpt_O_pl()
	{
		doTest("unicore_lib_Bpt_O_pl");
	}

	public void testunicore_lib_CE_Y_pl()
	{
		doTest("unicore_lib_CE_Y_pl");
	}

	public void testunicore_lib_CI_Y_pl()
	{
		doTest("unicore_lib_CI_Y_pl");
	}

	public void testunicore_lib_CWCF_Y_pl()
	{
		doTest("unicore_lib_CWCF_Y_pl");
	}

	public void testunicore_lib_CWCM_Y_pl()
	{
		doTest("unicore_lib_CWCM_Y_pl");
	}

	public void testunicore_lib_CWKCF_Y_pl()
	{
		doTest("unicore_lib_CWKCF_Y_pl");
	}

	public void testunicore_lib_CWL_Y_pl()
	{
		doTest("unicore_lib_CWL_Y_pl");
	}

	public void testunicore_lib_CWT_Y_pl()
	{
		doTest("unicore_lib_CWT_Y_pl");
	}

	public void testunicore_lib_CWU_Y_pl()
	{
		doTest("unicore_lib_CWU_Y_pl");
	}

	public void testunicore_lib_Cased_Y_pl()
	{
		doTest("unicore_lib_Cased_Y_pl");
	}

	public void testunicore_lib_Ccc_AR_pl()
	{
		doTest("unicore_lib_Ccc_AR_pl");
	}

	public void testunicore_lib_Ccc_ATAR_pl()
	{
		doTest("unicore_lib_Ccc_ATAR_pl");
	}

	public void testunicore_lib_Ccc_A_pl()
	{
		doTest("unicore_lib_Ccc_A_pl");
	}

	public void testunicore_lib_Ccc_BR_pl()
	{
		doTest("unicore_lib_Ccc_BR_pl");
	}

	public void testunicore_lib_Ccc_B_pl()
	{
		doTest("unicore_lib_Ccc_B_pl");
	}

	public void testunicore_lib_Ccc_DB_pl()
	{
		doTest("unicore_lib_Ccc_DB_pl");
	}

	public void testunicore_lib_Ccc_NK_pl()
	{
		doTest("unicore_lib_Ccc_NK_pl");
	}

	public void testunicore_lib_Ccc_NR_pl()
	{
		doTest("unicore_lib_Ccc_NR_pl");
	}

	public void testunicore_lib_Ccc_OV_pl()
	{
		doTest("unicore_lib_Ccc_OV_pl");
	}

	public void testunicore_lib_Ccc_VR_pl()
	{
		doTest("unicore_lib_Ccc_VR_pl");
	}

	public void testunicore_lib_CompEx_Y_pl()
	{
		doTest("unicore_lib_CompEx_Y_pl");
	}

	public void testunicore_lib_DI_Y_pl()
	{
		doTest("unicore_lib_DI_Y_pl");
	}

	public void testunicore_lib_Dash_Y_pl()
	{
		doTest("unicore_lib_Dash_Y_pl");
	}

	public void testunicore_lib_Dep_Y_pl()
	{
		doTest("unicore_lib_Dep_Y_pl");
	}

	public void testunicore_lib_Dia_Y_pl()
	{
		doTest("unicore_lib_Dia_Y_pl");
	}

	public void testunicore_lib_Dt_Com_pl()
	{
		doTest("unicore_lib_Dt_Com_pl");
	}

	public void testunicore_lib_Dt_Enc_pl()
	{
		doTest("unicore_lib_Dt_Enc_pl");
	}

	public void testunicore_lib_Dt_Fin_pl()
	{
		doTest("unicore_lib_Dt_Fin_pl");
	}

	public void testunicore_lib_Dt_Font_pl()
	{
		doTest("unicore_lib_Dt_Font_pl");
	}

	public void testunicore_lib_Dt_Init_pl()
	{
		doTest("unicore_lib_Dt_Init_pl");
	}

	public void testunicore_lib_Dt_Iso_pl()
	{
		doTest("unicore_lib_Dt_Iso_pl");
	}

	public void testunicore_lib_Dt_Med_pl()
	{
		doTest("unicore_lib_Dt_Med_pl");
	}

	public void testunicore_lib_Dt_Nar_pl()
	{
		doTest("unicore_lib_Dt_Nar_pl");
	}

	public void testunicore_lib_Dt_Nb_pl()
	{
		doTest("unicore_lib_Dt_Nb_pl");
	}

	public void testunicore_lib_Dt_NonCanon_pl()
	{
		doTest("unicore_lib_Dt_NonCanon_pl");
	}

	public void testunicore_lib_Dt_Sqr_pl()
	{
		doTest("unicore_lib_Dt_Sqr_pl");
	}

	public void testunicore_lib_Dt_Sub_pl()
	{
		doTest("unicore_lib_Dt_Sub_pl");
	}

	public void testunicore_lib_Dt_Sup_pl()
	{
		doTest("unicore_lib_Dt_Sup_pl");
	}

	public void testunicore_lib_Dt_Vert_pl()
	{
		doTest("unicore_lib_Dt_Vert_pl");
	}

	public void testunicore_lib_Ea_A_pl()
	{
		doTest("unicore_lib_Ea_A_pl");
	}

	public void testunicore_lib_Ea_H_pl()
	{
		doTest("unicore_lib_Ea_H_pl");
	}

	public void testunicore_lib_Ea_N_pl()
	{
		doTest("unicore_lib_Ea_N_pl");
	}

	public void testunicore_lib_Ea_Na_pl()
	{
		doTest("unicore_lib_Ea_Na_pl");
	}

	public void testunicore_lib_Ea_W_pl()
	{
		doTest("unicore_lib_Ea_W_pl");
	}

	public void testunicore_lib_Ext_Y_pl()
	{
		doTest("unicore_lib_Ext_Y_pl");
	}

	public void testunicore_lib_GCB_CN_pl()
	{
		doTest("unicore_lib_GCB_CN_pl");
	}

	public void testunicore_lib_GCB_EX_pl()
	{
		doTest("unicore_lib_GCB_EX_pl");
	}

	public void testunicore_lib_GCB_LVT_pl()
	{
		doTest("unicore_lib_GCB_LVT_pl");
	}

	public void testunicore_lib_GCB_LV_pl()
	{
		doTest("unicore_lib_GCB_LV_pl");
	}

	public void testunicore_lib_GCB_SM_pl()
	{
		doTest("unicore_lib_GCB_SM_pl");
	}

	public void testunicore_lib_GCB_XX_pl()
	{
		doTest("unicore_lib_GCB_XX_pl");
	}

	public void testunicore_lib_Gc_C_pl()
	{
		doTest("unicore_lib_Gc_C_pl");
	}

	public void testunicore_lib_Gc_Cf_pl()
	{
		doTest("unicore_lib_Gc_Cf_pl");
	}

	public void testunicore_lib_Gc_Cn_pl()
	{
		doTest("unicore_lib_Gc_Cn_pl");
	}

	public void testunicore_lib_Gc_LC_pl()
	{
		doTest("unicore_lib_Gc_LC_pl");
	}

	public void testunicore_lib_Gc_L_pl()
	{
		doTest("unicore_lib_Gc_L_pl");
	}

	public void testunicore_lib_Gc_Ll_pl()
	{
		doTest("unicore_lib_Gc_Ll_pl");
	}

	public void testunicore_lib_Gc_Lm_pl()
	{
		doTest("unicore_lib_Gc_Lm_pl");
	}

	public void testunicore_lib_Gc_Lo_pl()
	{
		doTest("unicore_lib_Gc_Lo_pl");
	}

	public void testunicore_lib_Gc_Lu_pl()
	{
		doTest("unicore_lib_Gc_Lu_pl");
	}

	public void testunicore_lib_Gc_M_pl()
	{
		doTest("unicore_lib_Gc_M_pl");
	}

	public void testunicore_lib_Gc_Mc_pl()
	{
		doTest("unicore_lib_Gc_Mc_pl");
	}

	public void testunicore_lib_Gc_Me_pl()
	{
		doTest("unicore_lib_Gc_Me_pl");
	}

	public void testunicore_lib_Gc_Mn_pl()
	{
		doTest("unicore_lib_Gc_Mn_pl");
	}

	public void testunicore_lib_Gc_N_pl()
	{
		doTest("unicore_lib_Gc_N_pl");
	}

	public void testunicore_lib_Gc_Nd_pl()
	{
		doTest("unicore_lib_Gc_Nd_pl");
	}

	public void testunicore_lib_Gc_Nl_pl()
	{
		doTest("unicore_lib_Gc_Nl_pl");
	}

	public void testunicore_lib_Gc_No_pl()
	{
		doTest("unicore_lib_Gc_No_pl");
	}

	public void testunicore_lib_Gc_P_pl()
	{
		doTest("unicore_lib_Gc_P_pl");
	}

	public void testunicore_lib_Gc_Pd_pl()
	{
		doTest("unicore_lib_Gc_Pd_pl");
	}

	public void testunicore_lib_Gc_Pe_pl()
	{
		doTest("unicore_lib_Gc_Pe_pl");
	}

	public void testunicore_lib_Gc_Pf_pl()
	{
		doTest("unicore_lib_Gc_Pf_pl");
	}

	public void testunicore_lib_Gc_Pi_pl()
	{
		doTest("unicore_lib_Gc_Pi_pl");
	}

	public void testunicore_lib_Gc_Po_pl()
	{
		doTest("unicore_lib_Gc_Po_pl");
	}

	public void testunicore_lib_Gc_Ps_pl()
	{
		doTest("unicore_lib_Gc_Ps_pl");
	}

	public void testunicore_lib_Gc_S_pl()
	{
		doTest("unicore_lib_Gc_S_pl");
	}

	public void testunicore_lib_Gc_Sc_pl()
	{
		doTest("unicore_lib_Gc_Sc_pl");
	}

	public void testunicore_lib_Gc_Sk_pl()
	{
		doTest("unicore_lib_Gc_Sk_pl");
	}

	public void testunicore_lib_Gc_Sm_pl()
	{
		doTest("unicore_lib_Gc_Sm_pl");
	}

	public void testunicore_lib_Gc_So_pl()
	{
		doTest("unicore_lib_Gc_So_pl");
	}

	public void testunicore_lib_Gc_Z_pl()
	{
		doTest("unicore_lib_Gc_Z_pl");
	}

	public void testunicore_lib_Gc_Zs_pl()
	{
		doTest("unicore_lib_Gc_Zs_pl");
	}

	public void testunicore_lib_GrBase_Y_pl()
	{
		doTest("unicore_lib_GrBase_Y_pl");
	}

	public void testunicore_lib_Hex_Y_pl()
	{
		doTest("unicore_lib_Hex_Y_pl");
	}

	public void testunicore_lib_Hst_NA_pl()
	{
		doTest("unicore_lib_Hst_NA_pl");
	}

	public void testunicore_lib_Hyphen_Y_pl()
	{
		doTest("unicore_lib_Hyphen_Y_pl");
	}

	public void testunicore_lib_IDC_Y_pl()
	{
		doTest("unicore_lib_IDC_Y_pl");
	}

	public void testunicore_lib_IDS_Y_pl()
	{
		doTest("unicore_lib_IDS_Y_pl");
	}

	public void testunicore_lib_Ideo_Y_pl()
	{
		doTest("unicore_lib_Ideo_Y_pl");
	}

	public void testunicore_lib_In_2_0_pl()
	{
		doTest("unicore_lib_In_2_0_pl");
	}

	public void testunicore_lib_In_2_1_pl()
	{
		doTest("unicore_lib_In_2_1_pl");
	}

	public void testunicore_lib_In_3_0_pl()
	{
		doTest("unicore_lib_In_3_0_pl");
	}

	public void testunicore_lib_In_3_1_pl()
	{
		doTest("unicore_lib_In_3_1_pl");
	}

	public void testunicore_lib_In_3_2_pl()
	{
		doTest("unicore_lib_In_3_2_pl");
	}

	public void testunicore_lib_In_4_0_pl()
	{
		doTest("unicore_lib_In_4_0_pl");
	}

	public void testunicore_lib_In_4_1_pl()
	{
		doTest("unicore_lib_In_4_1_pl");
	}

	public void testunicore_lib_In_5_0_pl()
	{
		doTest("unicore_lib_In_5_0_pl");
	}

	public void testunicore_lib_In_5_1_pl()
	{
		doTest("unicore_lib_In_5_1_pl");
	}

	public void testunicore_lib_In_5_2_pl()
	{
		doTest("unicore_lib_In_5_2_pl");
	}

	public void testunicore_lib_In_6_0_pl()
	{
		doTest("unicore_lib_In_6_0_pl");
	}

	public void testunicore_lib_In_6_1_pl()
	{
		doTest("unicore_lib_In_6_1_pl");
	}

	public void testunicore_lib_In_6_2_pl()
	{
		doTest("unicore_lib_In_6_2_pl");
	}

	public void testunicore_lib_In_6_3_pl()
	{
		doTest("unicore_lib_In_6_3_pl");
	}

	public void testunicore_lib_In_7_0_pl()
	{
		doTest("unicore_lib_In_7_0_pl");
	}

	public void testunicore_lib_Jg_Ain_pl()
	{
		doTest("unicore_lib_Jg_Ain_pl");
	}

	public void testunicore_lib_Jg_Alef_pl()
	{
		doTest("unicore_lib_Jg_Alef_pl");
	}

	public void testunicore_lib_Jg_Beh_pl()
	{
		doTest("unicore_lib_Jg_Beh_pl");
	}

	public void testunicore_lib_Jg_Dal_pl()
	{
		doTest("unicore_lib_Jg_Dal_pl");
	}

	public void testunicore_lib_Jg_FarsiYeh_pl()
	{
		doTest("unicore_lib_Jg_FarsiYeh_pl");
	}

	public void testunicore_lib_Jg_Feh_pl()
	{
		doTest("unicore_lib_Jg_Feh_pl");
	}

	public void testunicore_lib_Jg_Gaf_pl()
	{
		doTest("unicore_lib_Jg_Gaf_pl");
	}

	public void testunicore_lib_Jg_Hah_pl()
	{
		doTest("unicore_lib_Jg_Hah_pl");
	}

	public void testunicore_lib_Jg_Lam_pl()
	{
		doTest("unicore_lib_Jg_Lam_pl");
	}

	public void testunicore_lib_Jg_NoJoinin_pl()
	{
		doTest("unicore_lib_Jg_NoJoinin_pl");
	}

	public void testunicore_lib_Jg_Qaf_pl()
	{
		doTest("unicore_lib_Jg_Qaf_pl");
	}

	public void testunicore_lib_Jg_Reh_pl()
	{
		doTest("unicore_lib_Jg_Reh_pl");
	}

	public void testunicore_lib_Jg_Sad_pl()
	{
		doTest("unicore_lib_Jg_Sad_pl");
	}

	public void testunicore_lib_Jg_Seen_pl()
	{
		doTest("unicore_lib_Jg_Seen_pl");
	}

	public void testunicore_lib_Jg_Waw_pl()
	{
		doTest("unicore_lib_Jg_Waw_pl");
	}

	public void testunicore_lib_Jg_Yeh_pl()
	{
		doTest("unicore_lib_Jg_Yeh_pl");
	}

	public void testunicore_lib_Jt_C_pl()
	{
		doTest("unicore_lib_Jt_C_pl");
	}

	public void testunicore_lib_Jt_D_pl()
	{
		doTest("unicore_lib_Jt_D_pl");
	}

	public void testunicore_lib_Jt_R_pl()
	{
		doTest("unicore_lib_Jt_R_pl");
	}

	public void testunicore_lib_Jt_T_pl()
	{
		doTest("unicore_lib_Jt_T_pl");
	}

	public void testunicore_lib_Jt_U_pl()
	{
		doTest("unicore_lib_Jt_U_pl");
	}

	public void testunicore_lib_LOE_Y_pl()
	{
		doTest("unicore_lib_LOE_Y_pl");
	}

	public void testunicore_lib_Lb_AI_pl()
	{
		doTest("unicore_lib_Lb_AI_pl");
	}

	public void testunicore_lib_Lb_AL_pl()
	{
		doTest("unicore_lib_Lb_AL_pl");
	}

	public void testunicore_lib_Lb_BA_pl()
	{
		doTest("unicore_lib_Lb_BA_pl");
	}

	public void testunicore_lib_Lb_BB_pl()
	{
		doTest("unicore_lib_Lb_BB_pl");
	}

	public void testunicore_lib_Lb_CJ_pl()
	{
		doTest("unicore_lib_Lb_CJ_pl");
	}

	public void testunicore_lib_Lb_CL_pl()
	{
		doTest("unicore_lib_Lb_CL_pl");
	}

	public void testunicore_lib_Lb_CM_pl()
	{
		doTest("unicore_lib_Lb_CM_pl");
	}

	public void testunicore_lib_Lb_EX_pl()
	{
		doTest("unicore_lib_Lb_EX_pl");
	}

	public void testunicore_lib_Lb_GL_pl()
	{
		doTest("unicore_lib_Lb_GL_pl");
	}

	public void testunicore_lib_Lb_ID_pl()
	{
		doTest("unicore_lib_Lb_ID_pl");
	}

	public void testunicore_lib_Lb_IS_pl()
	{
		doTest("unicore_lib_Lb_IS_pl");
	}

	public void testunicore_lib_Lb_NS_pl()
	{
		doTest("unicore_lib_Lb_NS_pl");
	}

	public void testunicore_lib_Lb_OP_pl()
	{
		doTest("unicore_lib_Lb_OP_pl");
	}

	public void testunicore_lib_Lb_PO_pl()
	{
		doTest("unicore_lib_Lb_PO_pl");
	}

	public void testunicore_lib_Lb_PR_pl()
	{
		doTest("unicore_lib_Lb_PR_pl");
	}

	public void testunicore_lib_Lb_QU_pl()
	{
		doTest("unicore_lib_Lb_QU_pl");
	}

	public void testunicore_lib_Lb_SA_pl()
	{
		doTest("unicore_lib_Lb_SA_pl");
	}

	public void testunicore_lib_Lb_XX_pl()
	{
		doTest("unicore_lib_Lb_XX_pl");
	}

	public void testunicore_lib_Lower_Y_pl()
	{
		doTest("unicore_lib_Lower_Y_pl");
	}

	public void testunicore_lib_Math_Y_pl()
	{
		doTest("unicore_lib_Math_Y_pl");
	}

	public void testunicore_lib_NChar_Y_pl()
	{
		doTest("unicore_lib_NChar_Y_pl");
	}

	public void testunicore_lib_NFCQC_M_pl()
	{
		doTest("unicore_lib_NFCQC_M_pl");
	}

	public void testunicore_lib_NFCQC_Y_pl()
	{
		doTest("unicore_lib_NFCQC_Y_pl");
	}

	public void testunicore_lib_NFDQC_N_pl()
	{
		doTest("unicore_lib_NFDQC_N_pl");
	}

	public void testunicore_lib_NFDQC_Y_pl()
	{
		doTest("unicore_lib_NFDQC_Y_pl");
	}

	public void testunicore_lib_NFKCQC_N_pl()
	{
		doTest("unicore_lib_NFKCQC_N_pl");
	}

	public void testunicore_lib_NFKCQC_Y_pl()
	{
		doTest("unicore_lib_NFKCQC_Y_pl");
	}

	public void testunicore_lib_NFKDQC_N_pl()
	{
		doTest("unicore_lib_NFKDQC_N_pl");
	}

	public void testunicore_lib_NFKDQC_Y_pl()
	{
		doTest("unicore_lib_NFKDQC_Y_pl");
	}

	public void testunicore_lib_Nt_Di_pl()
	{
		doTest("unicore_lib_Nt_Di_pl");
	}

	public void testunicore_lib_Nt_None_pl()
	{
		doTest("unicore_lib_Nt_None_pl");
	}

	public void testunicore_lib_Nt_Nu_pl()
	{
		doTest("unicore_lib_Nt_Nu_pl");
	}

	public void testunicore_lib_Nv_0_pl()
	{
		doTest("unicore_lib_Nv_0_pl");
	}

	public void testunicore_lib_Nv_10000_pl()
	{
		doTest("unicore_lib_Nv_10000_pl");
	}

	public void testunicore_lib_Nv_1000_pl()
	{
		doTest("unicore_lib_Nv_1000_pl");
	}

	public void testunicore_lib_Nv_100_pl()
	{
		doTest("unicore_lib_Nv_100_pl");
	}

	public void testunicore_lib_Nv_10_pl()
	{
		doTest("unicore_lib_Nv_10_pl");
	}

	public void testunicore_lib_Nv_11_pl()
	{
		doTest("unicore_lib_Nv_11_pl");
	}

	public void testunicore_lib_Nv_12_pl()
	{
		doTest("unicore_lib_Nv_12_pl");
	}

	public void testunicore_lib_Nv_13_pl()
	{
		doTest("unicore_lib_Nv_13_pl");
	}

	public void testunicore_lib_Nv_14_pl()
	{
		doTest("unicore_lib_Nv_14_pl");
	}

	public void testunicore_lib_Nv_15_pl()
	{
		doTest("unicore_lib_Nv_15_pl");
	}

	public void testunicore_lib_Nv_16_pl()
	{
		doTest("unicore_lib_Nv_16_pl");
	}

	public void testunicore_lib_Nv_17_pl()
	{
		doTest("unicore_lib_Nv_17_pl");
	}

	public void testunicore_lib_Nv_18_pl()
	{
		doTest("unicore_lib_Nv_18_pl");
	}

	public void testunicore_lib_Nv_19_pl()
	{
		doTest("unicore_lib_Nv_19_pl");
	}

	public void testunicore_lib_Nv_1_2_pl()
	{
		doTest("unicore_lib_Nv_1_2_pl");
	}

	public void testunicore_lib_Nv_1_3_pl()
	{
		doTest("unicore_lib_Nv_1_3_pl");
	}

	public void testunicore_lib_Nv_1_4_pl()
	{
		doTest("unicore_lib_Nv_1_4_pl");
	}

	public void testunicore_lib_Nv_1_8_pl()
	{
		doTest("unicore_lib_Nv_1_8_pl");
	}

	public void testunicore_lib_Nv_1_pl()
	{
		doTest("unicore_lib_Nv_1_pl");
	}

	public void testunicore_lib_Nv_20_pl()
	{
		doTest("unicore_lib_Nv_20_pl");
	}

	public void testunicore_lib_Nv_2_3_pl()
	{
		doTest("unicore_lib_Nv_2_3_pl");
	}

	public void testunicore_lib_Nv_2_pl()
	{
		doTest("unicore_lib_Nv_2_pl");
	}

	public void testunicore_lib_Nv_300_pl()
	{
		doTest("unicore_lib_Nv_300_pl");
	}

	public void testunicore_lib_Nv_30_pl()
	{
		doTest("unicore_lib_Nv_30_pl");
	}

	public void testunicore_lib_Nv_3_4_pl()
	{
		doTest("unicore_lib_Nv_3_4_pl");
	}

	public void testunicore_lib_Nv_3_pl()
	{
		doTest("unicore_lib_Nv_3_pl");
	}

	public void testunicore_lib_Nv_40_pl()
	{
		doTest("unicore_lib_Nv_40_pl");
	}

	public void testunicore_lib_Nv_4_pl()
	{
		doTest("unicore_lib_Nv_4_pl");
	}

	public void testunicore_lib_Nv_50000_pl()
	{
		doTest("unicore_lib_Nv_50000_pl");
	}

	public void testunicore_lib_Nv_5000_pl()
	{
		doTest("unicore_lib_Nv_5000_pl");
	}

	public void testunicore_lib_Nv_500_pl()
	{
		doTest("unicore_lib_Nv_500_pl");
	}

	public void testunicore_lib_Nv_50_pl()
	{
		doTest("unicore_lib_Nv_50_pl");
	}

	public void testunicore_lib_Nv_5_pl()
	{
		doTest("unicore_lib_Nv_5_pl");
	}

	public void testunicore_lib_Nv_60_pl()
	{
		doTest("unicore_lib_Nv_60_pl");
	}

	public void testunicore_lib_Nv_6_pl()
	{
		doTest("unicore_lib_Nv_6_pl");
	}

	public void testunicore_lib_Nv_70_pl()
	{
		doTest("unicore_lib_Nv_70_pl");
	}

	public void testunicore_lib_Nv_7_pl()
	{
		doTest("unicore_lib_Nv_7_pl");
	}

	public void testunicore_lib_Nv_80_pl()
	{
		doTest("unicore_lib_Nv_80_pl");
	}

	public void testunicore_lib_Nv_8_pl()
	{
		doTest("unicore_lib_Nv_8_pl");
	}

	public void testunicore_lib_Nv_900_pl()
	{
		doTest("unicore_lib_Nv_900_pl");
	}

	public void testunicore_lib_Nv_90_pl()
	{
		doTest("unicore_lib_Nv_90_pl");
	}

	public void testunicore_lib_Nv_9_pl()
	{
		doTest("unicore_lib_Nv_9_pl");
	}

	public void testunicore_lib_PatSyn_Y_pl()
	{
		doTest("unicore_lib_PatSyn_Y_pl");
	}

	public void testunicore_lib_PatWS_Y_pl()
	{
		doTest("unicore_lib_PatWS_Y_pl");
	}

	public void testunicore_lib_Perl_Alnum_pl()
	{
		doTest("unicore_lib_Perl_Alnum_pl");
	}

	public void testunicore_lib_Perl_Assigned_pl()
	{
		doTest("unicore_lib_Perl_Assigned_pl");
	}

	public void testunicore_lib_Perl_Blank_pl()
	{
		doTest("unicore_lib_Perl_Blank_pl");
	}

	public void testunicore_lib_Perl_Graph_pl()
	{
		doTest("unicore_lib_Perl_Graph_pl");
	}

	public void testunicore_lib_Perl_PerlWord_pl()
	{
		doTest("unicore_lib_Perl_PerlWord_pl");
	}

	public void testunicore_lib_Perl_PosixPun_pl()
	{
		doTest("unicore_lib_Perl_PosixPun_pl");
	}

	public void testunicore_lib_Perl_Print_pl()
	{
		doTest("unicore_lib_Perl_Print_pl");
	}

	public void testunicore_lib_Perl_SpacePer_pl()
	{
		doTest("unicore_lib_Perl_SpacePer_pl");
	}

	public void testunicore_lib_Perl_Title_pl()
	{
		doTest("unicore_lib_Perl_Title_pl");
	}

	public void testunicore_lib_Perl_Word_pl()
	{
		doTest("unicore_lib_Perl_Word_pl");
	}

	public void testunicore_lib_Perl_XPosixPu_pl()
	{
		doTest("unicore_lib_Perl_XPosixPu_pl");
	}

	public void testunicore_lib_Perl__PerlAny_pl()
	{
		doTest("unicore_lib_Perl__PerlAny_pl");
	}

	public void testunicore_lib_Perl__PerlCh2_pl()
	{
		doTest("unicore_lib_Perl__PerlCh2_pl");
	}

	public void testunicore_lib_Perl__PerlCha_pl()
	{
		doTest("unicore_lib_Perl__PerlCha_pl");
	}

	public void testunicore_lib_Perl__PerlFol_pl()
	{
		doTest("unicore_lib_Perl__PerlFol_pl");
	}

	public void testunicore_lib_Perl__PerlIDC_pl()
	{
		doTest("unicore_lib_Perl__PerlIDC_pl");
	}

	public void testunicore_lib_Perl__PerlIDS_pl()
	{
		doTest("unicore_lib_Perl__PerlIDS_pl");
	}

	public void testunicore_lib_Perl__PerlPr2_pl()
	{
		doTest("unicore_lib_Perl__PerlPr2_pl");
	}

	public void testunicore_lib_Perl__PerlPro_pl()
	{
		doTest("unicore_lib_Perl__PerlPro_pl");
	}

	public void testunicore_lib_Perl__PerlQuo_pl()
	{
		doTest("unicore_lib_Perl__PerlQuo_pl");
	}

	public void testunicore_lib_QMark_Y_pl()
	{
		doTest("unicore_lib_QMark_Y_pl");
	}

	public void testunicore_lib_SB_AT_pl()
	{
		doTest("unicore_lib_SB_AT_pl");
	}

	public void testunicore_lib_SB_CL_pl()
	{
		doTest("unicore_lib_SB_CL_pl");
	}

	public void testunicore_lib_SB_EX_pl()
	{
		doTest("unicore_lib_SB_EX_pl");
	}

	public void testunicore_lib_SB_FO_pl()
	{
		doTest("unicore_lib_SB_FO_pl");
	}

	public void testunicore_lib_SB_LE_pl()
	{
		doTest("unicore_lib_SB_LE_pl");
	}

	public void testunicore_lib_SB_LO_pl()
	{
		doTest("unicore_lib_SB_LO_pl");
	}

	public void testunicore_lib_SB_NU_pl()
	{
		doTest("unicore_lib_SB_NU_pl");
	}

	public void testunicore_lib_SB_SC_pl()
	{
		doTest("unicore_lib_SB_SC_pl");
	}

	public void testunicore_lib_SB_ST_pl()
	{
		doTest("unicore_lib_SB_ST_pl");
	}

	public void testunicore_lib_SB_Sp_pl()
	{
		doTest("unicore_lib_SB_Sp_pl");
	}

	public void testunicore_lib_SB_UP_pl()
	{
		doTest("unicore_lib_SB_UP_pl");
	}

	public void testunicore_lib_SB_XX_pl()
	{
		doTest("unicore_lib_SB_XX_pl");
	}

	public void testunicore_lib_SD_Y_pl()
	{
		doTest("unicore_lib_SD_Y_pl");
	}

	public void testunicore_lib_STerm_Y_pl()
	{
		doTest("unicore_lib_STerm_Y_pl");
	}

	public void testunicore_lib_Sc_Arab_pl()
	{
		doTest("unicore_lib_Sc_Arab_pl");
	}

	public void testunicore_lib_Sc_Armn_pl()
	{
		doTest("unicore_lib_Sc_Armn_pl");
	}

	public void testunicore_lib_Sc_Beng_pl()
	{
		doTest("unicore_lib_Sc_Beng_pl");
	}

	public void testunicore_lib_Sc_Cham_pl()
	{
		doTest("unicore_lib_Sc_Cham_pl");
	}

	public void testunicore_lib_Sc_Cprt_pl()
	{
		doTest("unicore_lib_Sc_Cprt_pl");
	}

	public void testunicore_lib_Sc_Cyrl_pl()
	{
		doTest("unicore_lib_Sc_Cyrl_pl");
	}

	public void testunicore_lib_Sc_Deva_pl()
	{
		doTest("unicore_lib_Sc_Deva_pl");
	}

	public void testunicore_lib_Sc_Dupl_pl()
	{
		doTest("unicore_lib_Sc_Dupl_pl");
	}

	public void testunicore_lib_Sc_Ethi_pl()
	{
		doTest("unicore_lib_Sc_Ethi_pl");
	}

	public void testunicore_lib_Sc_Geor_pl()
	{
		doTest("unicore_lib_Sc_Geor_pl");
	}

	public void testunicore_lib_Sc_Gran_pl()
	{
		doTest("unicore_lib_Sc_Gran_pl");
	}

	public void testunicore_lib_Sc_Grek_pl()
	{
		doTest("unicore_lib_Sc_Grek_pl");
	}

	public void testunicore_lib_Sc_Gujr_pl()
	{
		doTest("unicore_lib_Sc_Gujr_pl");
	}

	public void testunicore_lib_Sc_Guru_pl()
	{
		doTest("unicore_lib_Sc_Guru_pl");
	}

	public void testunicore_lib_Sc_Han_pl()
	{
		doTest("unicore_lib_Sc_Han_pl");
	}

	public void testunicore_lib_Sc_Hang_pl()
	{
		doTest("unicore_lib_Sc_Hang_pl");
	}

	public void testunicore_lib_Sc_Hebr_pl()
	{
		doTest("unicore_lib_Sc_Hebr_pl");
	}

	public void testunicore_lib_Sc_Hira_pl()
	{
		doTest("unicore_lib_Sc_Hira_pl");
	}

	public void testunicore_lib_Sc_Hmng_pl()
	{
		doTest("unicore_lib_Sc_Hmng_pl");
	}

	public void testunicore_lib_Sc_Kana_pl()
	{
		doTest("unicore_lib_Sc_Kana_pl");
	}

	public void testunicore_lib_Sc_Khar_pl()
	{
		doTest("unicore_lib_Sc_Khar_pl");
	}

	public void testunicore_lib_Sc_Khmr_pl()
	{
		doTest("unicore_lib_Sc_Khmr_pl");
	}

	public void testunicore_lib_Sc_Knda_pl()
	{
		doTest("unicore_lib_Sc_Knda_pl");
	}

	public void testunicore_lib_Sc_Lana_pl()
	{
		doTest("unicore_lib_Sc_Lana_pl");
	}

	public void testunicore_lib_Sc_Lao_pl()
	{
		doTest("unicore_lib_Sc_Lao_pl");
	}

	public void testunicore_lib_Sc_Latn_pl()
	{
		doTest("unicore_lib_Sc_Latn_pl");
	}

	public void testunicore_lib_Sc_Limb_pl()
	{
		doTest("unicore_lib_Sc_Limb_pl");
	}

	public void testunicore_lib_Sc_Linb_pl()
	{
		doTest("unicore_lib_Sc_Linb_pl");
	}

	public void testunicore_lib_Sc_Mlym_pl()
	{
		doTest("unicore_lib_Sc_Mlym_pl");
	}

	public void testunicore_lib_Sc_Mong_pl()
	{
		doTest("unicore_lib_Sc_Mong_pl");
	}

	public void testunicore_lib_Sc_Orya_pl()
	{
		doTest("unicore_lib_Sc_Orya_pl");
	}

	public void testunicore_lib_Sc_Sinh_pl()
	{
		doTest("unicore_lib_Sc_Sinh_pl");
	}

	public void testunicore_lib_Sc_Talu_pl()
	{
		doTest("unicore_lib_Sc_Talu_pl");
	}

	public void testunicore_lib_Sc_Taml_pl()
	{
		doTest("unicore_lib_Sc_Taml_pl");
	}

	public void testunicore_lib_Sc_Telu_pl()
	{
		doTest("unicore_lib_Sc_Telu_pl");
	}

	public void testunicore_lib_Sc_Tibt_pl()
	{
		doTest("unicore_lib_Sc_Tibt_pl");
	}

	public void testunicore_lib_Sc_Zinh_pl()
	{
		doTest("unicore_lib_Sc_Zinh_pl");
	}

	public void testunicore_lib_Sc_Zyyy_pl()
	{
		doTest("unicore_lib_Sc_Zyyy_pl");
	}

	public void testunicore_lib_Sc_Zzzz_pl()
	{
		doTest("unicore_lib_Sc_Zzzz_pl");
	}

	public void testunicore_lib_Scx_Arab_pl()
	{
		doTest("unicore_lib_Scx_Arab_pl");
	}

	public void testunicore_lib_Scx_Armn_pl()
	{
		doTest("unicore_lib_Scx_Armn_pl");
	}

	public void testunicore_lib_Scx_Beng_pl()
	{
		doTest("unicore_lib_Scx_Beng_pl");
	}

	public void testunicore_lib_Scx_Bopo_pl()
	{
		doTest("unicore_lib_Scx_Bopo_pl");
	}

	public void testunicore_lib_Scx_Cakm_pl()
	{
		doTest("unicore_lib_Scx_Cakm_pl");
	}

	public void testunicore_lib_Scx_Copt_pl()
	{
		doTest("unicore_lib_Scx_Copt_pl");
	}

	public void testunicore_lib_Scx_Cprt_pl()
	{
		doTest("unicore_lib_Scx_Cprt_pl");
	}

	public void testunicore_lib_Scx_Cyrl_pl()
	{
		doTest("unicore_lib_Scx_Cyrl_pl");
	}

	public void testunicore_lib_Scx_Deva_pl()
	{
		doTest("unicore_lib_Scx_Deva_pl");
	}

	public void testunicore_lib_Scx_Dupl_pl()
	{
		doTest("unicore_lib_Scx_Dupl_pl");
	}

	public void testunicore_lib_Scx_Geor_pl()
	{
		doTest("unicore_lib_Scx_Geor_pl");
	}

	public void testunicore_lib_Scx_Gran_pl()
	{
		doTest("unicore_lib_Scx_Gran_pl");
	}

	public void testunicore_lib_Scx_Grek_pl()
	{
		doTest("unicore_lib_Scx_Grek_pl");
	}

	public void testunicore_lib_Scx_Gujr_pl()
	{
		doTest("unicore_lib_Scx_Gujr_pl");
	}

	public void testunicore_lib_Scx_Guru_pl()
	{
		doTest("unicore_lib_Scx_Guru_pl");
	}

	public void testunicore_lib_Scx_Han_pl()
	{
		doTest("unicore_lib_Scx_Han_pl");
	}

	public void testunicore_lib_Scx_Hang_pl()
	{
		doTest("unicore_lib_Scx_Hang_pl");
	}

	public void testunicore_lib_Scx_Hira_pl()
	{
		doTest("unicore_lib_Scx_Hira_pl");
	}

	public void testunicore_lib_Scx_Kana_pl()
	{
		doTest("unicore_lib_Scx_Kana_pl");
	}

	public void testunicore_lib_Scx_Knda_pl()
	{
		doTest("unicore_lib_Scx_Knda_pl");
	}

	public void testunicore_lib_Scx_Latn_pl()
	{
		doTest("unicore_lib_Scx_Latn_pl");
	}

	public void testunicore_lib_Scx_Limb_pl()
	{
		doTest("unicore_lib_Scx_Limb_pl");
	}

	public void testunicore_lib_Scx_Linb_pl()
	{
		doTest("unicore_lib_Scx_Linb_pl");
	}

	public void testunicore_lib_Scx_Mlym_pl()
	{
		doTest("unicore_lib_Scx_Mlym_pl");
	}

	public void testunicore_lib_Scx_Mong_pl()
	{
		doTest("unicore_lib_Scx_Mong_pl");
	}

	public void testunicore_lib_Scx_Mymr_pl()
	{
		doTest("unicore_lib_Scx_Mymr_pl");
	}

	public void testunicore_lib_Scx_Orya_pl()
	{
		doTest("unicore_lib_Scx_Orya_pl");
	}

	public void testunicore_lib_Scx_Phlp_pl()
	{
		doTest("unicore_lib_Scx_Phlp_pl");
	}

	public void testunicore_lib_Scx_Sind_pl()
	{
		doTest("unicore_lib_Scx_Sind_pl");
	}

	public void testunicore_lib_Scx_Sinh_pl()
	{
		doTest("unicore_lib_Scx_Sinh_pl");
	}

	public void testunicore_lib_Scx_Syrc_pl()
	{
		doTest("unicore_lib_Scx_Syrc_pl");
	}

	public void testunicore_lib_Scx_Tagb_pl()
	{
		doTest("unicore_lib_Scx_Tagb_pl");
	}

	public void testunicore_lib_Scx_Takr_pl()
	{
		doTest("unicore_lib_Scx_Takr_pl");
	}

	public void testunicore_lib_Scx_Taml_pl()
	{
		doTest("unicore_lib_Scx_Taml_pl");
	}

	public void testunicore_lib_Scx_Telu_pl()
	{
		doTest("unicore_lib_Scx_Telu_pl");
	}

	public void testunicore_lib_Scx_Thaa_pl()
	{
		doTest("unicore_lib_Scx_Thaa_pl");
	}

	public void testunicore_lib_Scx_Tirh_pl()
	{
		doTest("unicore_lib_Scx_Tirh_pl");
	}

	public void testunicore_lib_Scx_Yi_pl()
	{
		doTest("unicore_lib_Scx_Yi_pl");
	}

	public void testunicore_lib_Scx_Zinh_pl()
	{
		doTest("unicore_lib_Scx_Zinh_pl");
	}

	public void testunicore_lib_Scx_Zyyy_pl()
	{
		doTest("unicore_lib_Scx_Zyyy_pl");
	}

	public void testunicore_lib_Term_Y_pl()
	{
		doTest("unicore_lib_Term_Y_pl");
	}

	public void testunicore_lib_UIdeo_Y_pl()
	{
		doTest("unicore_lib_UIdeo_Y_pl");
	}

	public void testunicore_lib_Upper_Y_pl()
	{
		doTest("unicore_lib_Upper_Y_pl");
	}

	public void testunicore_lib_WB_EX_pl()
	{
		doTest("unicore_lib_WB_EX_pl");
	}

	public void testunicore_lib_WB_FO_pl()
	{
		doTest("unicore_lib_WB_FO_pl");
	}

	public void testunicore_lib_WB_HL_pl()
	{
		doTest("unicore_lib_WB_HL_pl");
	}

	public void testunicore_lib_WB_KA_pl()
	{
		doTest("unicore_lib_WB_KA_pl");
	}

	public void testunicore_lib_WB_LE_pl()
	{
		doTest("unicore_lib_WB_LE_pl");
	}

	public void testunicore_lib_WB_MB_pl()
	{
		doTest("unicore_lib_WB_MB_pl");
	}

	public void testunicore_lib_WB_ML_pl()
	{
		doTest("unicore_lib_WB_ML_pl");
	}

	public void testunicore_lib_WB_MN_pl()
	{
		doTest("unicore_lib_WB_MN_pl");
	}

	public void testunicore_lib_WB_NU_pl()
	{
		doTest("unicore_lib_WB_NU_pl");
	}

	public void testunicore_lib_WB_XX_pl()
	{
		doTest("unicore_lib_WB_XX_pl");
	}

	public void testunicore_lib_XIDC_Y_pl()
	{
		doTest("unicore_lib_XIDC_Y_pl");
	}

	public void testunicore_lib_XIDS_Y_pl()
	{
		doTest("unicore_lib_XIDS_Y_pl");
	}

	public void testutf8_heavy_pl()
	{
		doTest("utf8_heavy_pl");
	}

	public void testutf8_pm()
	{
		doTest("utf8_pm");
	}

	public void testvars_pm()
	{
		doTest("vars_pm");
	}

	public void testversion_pm()
	{
		doTest("version_pm");
	}

	public void testversion_regex_pm()
	{
		doTest("version_regex_pm");
	}

	public void testversion_vpp_pm()
	{
		doTest("version_vpp_pm");
	}

	public void testversion_vxs_pm()
	{
		doTest("version_vxs_pm");
	}

	public void testvmsish_pm()
	{
		doTest("vmsish_pm");
	}

	public void testwarnings_pm()
	{
		doTest("warnings_pm");
	}

	public void testwarnings_register_pm()
	{
		doTest("warnings_register_pm");
	}


}
