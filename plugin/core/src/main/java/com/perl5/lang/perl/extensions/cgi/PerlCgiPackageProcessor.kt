/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.perl.extensions.cgi

import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessorBase
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement
import com.perl5.lang.perl.util.PerlSubUtil

private val exportTags: Map<String, List<String>> = mapOf(
  ":html2" to listOf(
    "h1", "h2", "h3", "h4", "h5", "h6", "p", "br", "hr", "ol", "ul", "li", "dl", "dt", "dd", "menu", "code", "var", "strong", "em", "tt",
    "u", "i", "b", "blockquote", "pre", "img", "a", "address", "cite", "samp", "dfn", "html", "head", "base", "body", "Link", "nextid",
    "title", "meta", "kbd", "start_html", "end_html", "input", "Select", "option", "comment", "charset", "escapeHTML"
  ),
  ":html3" to listOf(
    "div", "table", "caption", "th", "td", "TR", "Tr", "sup", "Sub", "strike", "applet", "Param", "nobr", "embed", "basefont", "style",
    "span", "layer", "ilayer", "font", "frameset", "frame", "script", "small", "big", "Area", "Map"
  ),
  ":html4" to listOf(
    "abbr", "acronym", "bdo", "col", "colgroup", "del", "fieldset", "iframe", "ins", "label", "legend", "noframes", "noscript", "object",
    "optgroup", "Q", "thead", "tbody", "tfoot",
  ),
  ":form" to listOf(
    "textfield", "textarea", "filefield", "password_field", "hidden", "checkbox", "checkbox_group", "submit", "reset", "defaults",
    "radio_group", "popup_menu", "button", "autoEscape", "scrolling_list", "image_button", "start_form", "end_form",
    "start_multipart_form", "end_multipart_form", "isindex", "tmpFileName", "uploadInfo", "URL_ENCODED", "MULTIPART",
  ),
  ":cgi" to listOf(
    "param", "multi_param", "upload", "path_info", "path_translated", "request_uri", "url", "self_url", "script_name", "cookie", "Dump",
    "raw_cookie", "request_method", "query_string", "Accept", "user_agent", "remote_host", "content_type", "remote_addr", "referer",
    "server_name", "server_software", "server_port", "server_protocol", "virtual_port", "virtual_host", "remote_ident", "auth_type",
    "http", "append", "save_parameters", "restore_parameters", "param_fetch", "remote_user", "user_name", "header", "redirect",
    "import_names", "put", "Delete", "Delete_all", "url_param", "cgi_error", "env_query_string"
  ),
  ":netscape" to listOf("blink", "fontsize", "center"),
  ":ssl" to listOf("https"),
  ":cgi-lib" to listOf("ReadParse", "PrintHeader", "HtmlTop", "HtmlBot", "SplitParam", "Vars"),
  ":push" to listOf("multipart_init", "multipart_start", "multipart_end", "multipart_final"),
  ":html" to listOf(":html2", ":html3", ":html4", ":netscape"),
  ":standard" to listOf(":html2", ":html3", ":html4", ":form", ":cgi", ":ssl"),
  ":all" to listOf(":html2", ":html3", ":html4", ":netscape", ":form", ":cgi", ":ssl", ":push"),
)

class PerlCgiPackageProcessor : PerlPackageProcessorBase() {
  override fun addExports(useStatement: PerlUseStatementElement, export: MutableSet<in String>, exportOk: MutableSet<in String>) {
    exportOk.addAll(exportTags.keys)
    exportOk.addAll(PerlSubUtil.getSubDefinitionsInPackage(useStatement.project, "CGI").mapNotNull { it.subName })
  }

  override fun getImportParameters(useStatement: PerlUseStatementElement): MutableList<String>? {
    val realParameters = super.getImportParameters(useStatement)
    if( realParameters == null){
      return null
    }
    return expandTags(realParameters).toMutableList()
  }

  private fun expandTags(imports: List<String>): Set<String>{
    val result: MutableSet<String> = mutableSetOf()

    imports.forEach{
      if( it.startsWith(":")){
        result.addAll(expandTag(it))
      }
      else{
        result.add(it)
      }
    }

    return result
  }

  private fun expandTag(tag: String): Set<String>{
    return exportTags[tag]?.let { expandTags(it).toSet() } ?: emptySet()
  }
}