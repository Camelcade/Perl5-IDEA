/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

package com.perl5.lang.pod.parser.psi.mixin;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.lexer.PerlLexer;
import com.perl5.lang.pod.parser.psi.PodFormatterE;
import com.perl5.lang.pod.parser.psi.PodRenderingContext;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hurricup on 26.03.2016.
 */
public class PodFormatterEMixin extends PodSectionMixin implements PodFormatterE {
  public static final Map<String, String> HTML_NAMES_MAP = new THashMap<>();
  public static final Pattern OCTAL_NUMBER_PATTERN = Pattern.compile("^(0[0-7]*)$");
  public static final Pattern HEX_NUMBER_PATTERN = Pattern.compile("^0?x([0-9a-fA-F]+)$");
  public static final Pattern NUMBER_PATTERN = Pattern.compile("^\\d+$");

  static {
    HTML_NAMES_MAP.put("lt", "60");
    HTML_NAMES_MAP.put("gt", "62");
    HTML_NAMES_MAP.put("quot", "34");
    HTML_NAMES_MAP.put("amp", "38");
    HTML_NAMES_MAP.put("apos", "39");

    HTML_NAMES_MAP.put("sol", "47");
    HTML_NAMES_MAP.put("verbar", "124");

    HTML_NAMES_MAP.put("lchevron", "171");
    HTML_NAMES_MAP.put("rchevron", "187");

    HTML_NAMES_MAP.put("nbsp", "160");
    HTML_NAMES_MAP.put("iexcl", "161");
    HTML_NAMES_MAP.put("cent", "162");
    HTML_NAMES_MAP.put("pound", "163");
    HTML_NAMES_MAP.put("curren", "164");
    HTML_NAMES_MAP.put("yen", "165");
    HTML_NAMES_MAP.put("brvbar", "166");
    HTML_NAMES_MAP.put("sect", "167");
    HTML_NAMES_MAP.put("uml", "168");
    HTML_NAMES_MAP.put("copy", "169");
    HTML_NAMES_MAP.put("ordf", "170");
    HTML_NAMES_MAP.put("laquo", "171");
    HTML_NAMES_MAP.put("not", "172");
    HTML_NAMES_MAP.put("shy", "173");
    HTML_NAMES_MAP.put("reg", "174");
    HTML_NAMES_MAP.put("macr", "175");
    HTML_NAMES_MAP.put("deg", "176");
    HTML_NAMES_MAP.put("plusmn", "177");
    HTML_NAMES_MAP.put("sup2", "178");
    HTML_NAMES_MAP.put("sup3", "179");
    HTML_NAMES_MAP.put("acute", "180");
    HTML_NAMES_MAP.put("micro", "181");
    HTML_NAMES_MAP.put("para", "182");
    HTML_NAMES_MAP.put("middot", "183");
    HTML_NAMES_MAP.put("cedil", "184");
    HTML_NAMES_MAP.put("sup1", "185");
    HTML_NAMES_MAP.put("ordm", "186");
    HTML_NAMES_MAP.put("raquo", "187");
    HTML_NAMES_MAP.put("frac14", "188");
    HTML_NAMES_MAP.put("frac12", "189");
    HTML_NAMES_MAP.put("frac34", "190");
    HTML_NAMES_MAP.put("iquest", "191");
    HTML_NAMES_MAP.put("Agrave", "192");
    HTML_NAMES_MAP.put("Aacute", "193");
    HTML_NAMES_MAP.put("Acirc", "194");
    HTML_NAMES_MAP.put("Atilde", "195");
    HTML_NAMES_MAP.put("Auml", "196");
    HTML_NAMES_MAP.put("Aring", "197");
    HTML_NAMES_MAP.put("AElig", "198");
    HTML_NAMES_MAP.put("Ccedil", "199");
    HTML_NAMES_MAP.put("Egrave", "200");
    HTML_NAMES_MAP.put("Eacute", "201");
    HTML_NAMES_MAP.put("Ecirc", "202");
    HTML_NAMES_MAP.put("Euml", "203");
    HTML_NAMES_MAP.put("Igrave", "204");
    HTML_NAMES_MAP.put("Iacute", "205");
    HTML_NAMES_MAP.put("Icirc", "206");
    HTML_NAMES_MAP.put("Iuml", "207");
    HTML_NAMES_MAP.put("ETH", "208");
    HTML_NAMES_MAP.put("Ntilde", "209");
    HTML_NAMES_MAP.put("Ograve", "210");
    HTML_NAMES_MAP.put("Oacute", "211");
    HTML_NAMES_MAP.put("Ocirc", "212");
    HTML_NAMES_MAP.put("Otilde", "213");
    HTML_NAMES_MAP.put("Ouml", "214");
    HTML_NAMES_MAP.put("times", "215");
    HTML_NAMES_MAP.put("Oslash", "216");
    HTML_NAMES_MAP.put("Ugrave", "217");
    HTML_NAMES_MAP.put("Uacute", "218");
    HTML_NAMES_MAP.put("Ucirc", "219");
    HTML_NAMES_MAP.put("Uuml", "220");
    HTML_NAMES_MAP.put("Yacute", "221");
    HTML_NAMES_MAP.put("THORN", "222");
    HTML_NAMES_MAP.put("szlig", "223");
    HTML_NAMES_MAP.put("agrave", "224");
    HTML_NAMES_MAP.put("aacute", "225");
    HTML_NAMES_MAP.put("acirc", "226");
    HTML_NAMES_MAP.put("atilde", "227");
    HTML_NAMES_MAP.put("auml", "228");
    HTML_NAMES_MAP.put("aring", "229");
    HTML_NAMES_MAP.put("aelig", "230");
    HTML_NAMES_MAP.put("ccedil", "231");
    HTML_NAMES_MAP.put("egrave", "232");
    HTML_NAMES_MAP.put("eacute", "233");
    HTML_NAMES_MAP.put("ecirc", "234");
    HTML_NAMES_MAP.put("euml", "235");
    HTML_NAMES_MAP.put("igrave", "236");
    HTML_NAMES_MAP.put("iacute", "237");
    HTML_NAMES_MAP.put("icirc", "238");
    HTML_NAMES_MAP.put("iuml", "239");
    HTML_NAMES_MAP.put("eth", "240");
    HTML_NAMES_MAP.put("ntilde", "241");
    HTML_NAMES_MAP.put("ograve", "242");
    HTML_NAMES_MAP.put("oacute", "243");
    HTML_NAMES_MAP.put("ocirc", "244");
    HTML_NAMES_MAP.put("otilde", "245");
    HTML_NAMES_MAP.put("ouml", "246");
    HTML_NAMES_MAP.put("divide", "247");
    HTML_NAMES_MAP.put("oslash", "248");
    HTML_NAMES_MAP.put("ugrave", "249");
    HTML_NAMES_MAP.put("uacute", "250");
    HTML_NAMES_MAP.put("ucirc", "251");
    HTML_NAMES_MAP.put("uuml", "252");
    HTML_NAMES_MAP.put("yacute", "253");
    HTML_NAMES_MAP.put("thorn", "254");
    HTML_NAMES_MAP.put("yuml", "255");

    HTML_NAMES_MAP.put("fnof", "402");
    HTML_NAMES_MAP.put("Alpha", "913");
    HTML_NAMES_MAP.put("Beta", "914");
    HTML_NAMES_MAP.put("Gamma", "915");
    HTML_NAMES_MAP.put("Delta", "916");
    HTML_NAMES_MAP.put("Epsilon", "917");
    HTML_NAMES_MAP.put("Zeta", "918");
    HTML_NAMES_MAP.put("Eta", "919");
    HTML_NAMES_MAP.put("Theta", "920");
    HTML_NAMES_MAP.put("Iota", "921");
    HTML_NAMES_MAP.put("Kappa", "922");
    HTML_NAMES_MAP.put("Lambda", "923");
    HTML_NAMES_MAP.put("Mu", "924");
    HTML_NAMES_MAP.put("Nu", "925");
    HTML_NAMES_MAP.put("Xi", "926");
    HTML_NAMES_MAP.put("Omicron", "927");
    HTML_NAMES_MAP.put("Pi", "928");
    HTML_NAMES_MAP.put("Rho", "929");
    HTML_NAMES_MAP.put("Sigma", "931");
    HTML_NAMES_MAP.put("Tau", "932");
    HTML_NAMES_MAP.put("Upsilon", "933");
    HTML_NAMES_MAP.put("Phi", "934");
    HTML_NAMES_MAP.put("Chi", "935");
    HTML_NAMES_MAP.put("Psi", "936");
    HTML_NAMES_MAP.put("Omega", "937");
    HTML_NAMES_MAP.put("alpha", "945");
    HTML_NAMES_MAP.put("beta", "946");
    HTML_NAMES_MAP.put("gamma", "947");
    HTML_NAMES_MAP.put("delta", "948");
    HTML_NAMES_MAP.put("epsilon", "949");
    HTML_NAMES_MAP.put("zeta", "950");
    HTML_NAMES_MAP.put("eta", "951");
    HTML_NAMES_MAP.put("theta", "952");
    HTML_NAMES_MAP.put("iota", "953");
    HTML_NAMES_MAP.put("kappa", "954");
    HTML_NAMES_MAP.put("lambda", "955");
    HTML_NAMES_MAP.put("mu", "956");
    HTML_NAMES_MAP.put("nu", "957");
    HTML_NAMES_MAP.put("xi", "958");
    HTML_NAMES_MAP.put("omicron", "959");
    HTML_NAMES_MAP.put("pi", "960");
    HTML_NAMES_MAP.put("rho", "961");
    HTML_NAMES_MAP.put("sigmaf", "962");
    HTML_NAMES_MAP.put("sigma", "963");
    HTML_NAMES_MAP.put("tau", "964");
    HTML_NAMES_MAP.put("upsilon", "965");
    HTML_NAMES_MAP.put("phi", "966");
    HTML_NAMES_MAP.put("chi", "967");
    HTML_NAMES_MAP.put("psi", "968");
    HTML_NAMES_MAP.put("omega", "969");
    HTML_NAMES_MAP.put("thetasym", "977");
    HTML_NAMES_MAP.put("upsih", "978");
    HTML_NAMES_MAP.put("piv", "982");
    HTML_NAMES_MAP.put("bull", "8226");
    HTML_NAMES_MAP.put("hellip", "8230");
    HTML_NAMES_MAP.put("prime", "8242");
    HTML_NAMES_MAP.put("Prime", "8243");
    HTML_NAMES_MAP.put("oline", "8254");
    HTML_NAMES_MAP.put("frasl", "8260");
    HTML_NAMES_MAP.put("weierp", "8472");
    HTML_NAMES_MAP.put("image", "8465");
    HTML_NAMES_MAP.put("real", "8476");
    HTML_NAMES_MAP.put("trade", "8482");
    HTML_NAMES_MAP.put("alefsym", "8501");
    HTML_NAMES_MAP.put("larr", "8592");
    HTML_NAMES_MAP.put("uarr", "8593");
    HTML_NAMES_MAP.put("rarr", "8594");
    HTML_NAMES_MAP.put("darr", "8595");
    HTML_NAMES_MAP.put("harr", "8596");
    HTML_NAMES_MAP.put("crarr", "8629");
    HTML_NAMES_MAP.put("lArr", "8656");
    HTML_NAMES_MAP.put("uArr", "8657");
    HTML_NAMES_MAP.put("rArr", "8658");
    HTML_NAMES_MAP.put("dArr", "8659");
    HTML_NAMES_MAP.put("hArr", "8660");
    HTML_NAMES_MAP.put("forall", "8704");
    HTML_NAMES_MAP.put("part", "8706");
    HTML_NAMES_MAP.put("exist", "8707");
    HTML_NAMES_MAP.put("empty", "8709");
    HTML_NAMES_MAP.put("nabla", "8711");
    HTML_NAMES_MAP.put("isin", "8712");
    HTML_NAMES_MAP.put("notin", "8713");
    HTML_NAMES_MAP.put("ni", "8715");
    HTML_NAMES_MAP.put("prod", "8719");
    HTML_NAMES_MAP.put("sum", "8721");
    HTML_NAMES_MAP.put("minus", "8722");
    HTML_NAMES_MAP.put("lowast", "8727");
    HTML_NAMES_MAP.put("radic", "8730");
    HTML_NAMES_MAP.put("prop", "8733");
    HTML_NAMES_MAP.put("infin", "8734");
    HTML_NAMES_MAP.put("ang", "8736");
    HTML_NAMES_MAP.put("and", "8743");
    HTML_NAMES_MAP.put("or", "8744");
    HTML_NAMES_MAP.put("cap", "8745");
    HTML_NAMES_MAP.put("cup", "8746");
    HTML_NAMES_MAP.put("int", "8747");
    HTML_NAMES_MAP.put("there4", "8756");
    HTML_NAMES_MAP.put("sim", "8764");
    HTML_NAMES_MAP.put("cong", "8773");
    HTML_NAMES_MAP.put("asymp", "8776");
    HTML_NAMES_MAP.put("ne", "8800");
    HTML_NAMES_MAP.put("equiv", "8801");
    HTML_NAMES_MAP.put("le", "8804");
    HTML_NAMES_MAP.put("ge", "8805");
    HTML_NAMES_MAP.put("sub", "8834");
    HTML_NAMES_MAP.put("sup", "8835");
    HTML_NAMES_MAP.put("nsub", "8836");
    HTML_NAMES_MAP.put("sube", "8838");
    HTML_NAMES_MAP.put("supe", "8839");
    HTML_NAMES_MAP.put("oplus", "8853");
    HTML_NAMES_MAP.put("otimes", "8855");
    HTML_NAMES_MAP.put("perp", "8869");
    HTML_NAMES_MAP.put("sdot", "8901");
    HTML_NAMES_MAP.put("lceil", "8968");
    HTML_NAMES_MAP.put("rceil", "8969");
    HTML_NAMES_MAP.put("lfloor", "8970");
    HTML_NAMES_MAP.put("rfloor", "8971");
    HTML_NAMES_MAP.put("lang", "9001");
    HTML_NAMES_MAP.put("rang", "9002");
    HTML_NAMES_MAP.put("loz", "9674");
    HTML_NAMES_MAP.put("spades", "9824");
    HTML_NAMES_MAP.put("clubs", "9827");
    HTML_NAMES_MAP.put("hearts", "9829");
    HTML_NAMES_MAP.put("diams", "9830");
    HTML_NAMES_MAP.put("OElig", "338");
    HTML_NAMES_MAP.put("oelig", "339");
    HTML_NAMES_MAP.put("Scaron", "352");
    HTML_NAMES_MAP.put("scaron", "353");
    HTML_NAMES_MAP.put("Yuml", "376");
    HTML_NAMES_MAP.put("circ", "710");
    HTML_NAMES_MAP.put("tilde", "732");
    HTML_NAMES_MAP.put("ensp", "8194");
    HTML_NAMES_MAP.put("emsp", "8195");
    HTML_NAMES_MAP.put("thinsp", "8201");
    HTML_NAMES_MAP.put("zwnj", "8204");
    HTML_NAMES_MAP.put("zwj", "8205");
    HTML_NAMES_MAP.put("lrm", "8206");
    HTML_NAMES_MAP.put("rlm", "8207");
    HTML_NAMES_MAP.put("ndash", "8211");
    HTML_NAMES_MAP.put("mdash", "8212");
    HTML_NAMES_MAP.put("lsquo", "8216");
    HTML_NAMES_MAP.put("rsquo", "8217");
    HTML_NAMES_MAP.put("sbquo", "8218");
    HTML_NAMES_MAP.put("ldquo", "8220");
    HTML_NAMES_MAP.put("rdquo", "8221");
    HTML_NAMES_MAP.put("bdquo", "8222");
    HTML_NAMES_MAP.put("dagger", "8224");
    HTML_NAMES_MAP.put("Dagger", "8225");
    HTML_NAMES_MAP.put("permil", "8240");
    HTML_NAMES_MAP.put("lsaquo", "8249");
    HTML_NAMES_MAP.put("rsaquo", "8250");
    HTML_NAMES_MAP.put("euro", "8364");
  }

  public PodFormatterEMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void renderElementContentAsHTML(StringBuilder builder, PodRenderingContext context) {
    PsiElement content = getContentBlock();

    if (content != null) {
      String myText = content.getText();

      Matcher m;

      if (myText == null) {
        myText = PerlLexer.STRING_UNDEF;
      }
      else if ((m = OCTAL_NUMBER_PATTERN.matcher(myText)).matches()) {
        myText = "&#" + Integer.parseInt(m.group(1), 8) + ";";
      }
      else if ((m = HEX_NUMBER_PATTERN.matcher(myText)).matches()) {
        myText = "&#" + Integer.parseInt(m.group(1), 16) + ";";
      }
      else if (NUMBER_PATTERN.matcher(myText).matches()) {
        myText = "&#" + myText + ";";
      }
      else {
        myText = "&" + myText + ';';
      }
      builder.append(myText);
    }
  }

  @Override
  public void renderElementContentAsText(StringBuilder builder, PodRenderingContext context) {
    PsiElement content = getContentBlock();

    if (content != null) {
      String myText = content.getText();

      Matcher m;
      int codePoint = -1;

      if (myText == null) {
        myText = "undef";
      }
      else if (HTML_NAMES_MAP.containsKey(myText)) {
        codePoint = Integer.parseInt(HTML_NAMES_MAP.get(myText));
      }
      else if ((m = OCTAL_NUMBER_PATTERN.matcher(myText)).matches()) {
        codePoint = Integer.parseInt(m.group(1), 8);
      }
      else if ((m = HEX_NUMBER_PATTERN.matcher(myText)).matches()) {
        codePoint = Integer.parseInt(m.group(1), 16);
      }
      else if (NUMBER_PATTERN.matcher(myText).matches()) {
        codePoint = Integer.parseInt(myText);
      }

      if (codePoint != -1) {
        try {
          myText = "" + (Character.toChars(codePoint))[0];
        }
        catch (IllegalArgumentException e) {
          myText = "Incorrect codePoint " + myText;
        }
      }

      builder.append(myText);
    }
  }
}
