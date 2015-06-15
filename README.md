#Perl5 plugin for IntellijIDEA - Camelcade

This is my attempt to build a plugin for IDEA that will support Perl5 language.

This project is currently in development, and if you want to participate - you may fork or contact me via skype: hurricup

#Need testers
There is tremendous amount of work to do in development and project really need testers. Not only users-testers, but someone
to write tests (mostly for lexer and parser at the moment).

#Current status - development (0.28 beta)

* Implemented Perl SDK type, Perl5 module type.
* Implemented support for Perl5, POD and php-style perl <? ... ?>
* Implemented syntax highlighting, basic brace matcher, find usages, livetemplates. 
* Implemented basic code-folding for blocks, hashes, arrays, PODs, heredocs and sequential comments and imports.
* Implemented autocompletion for built-in functions, installed packages, namespaces and variables.
* Implemented autocompletion, navigation and refactoring for lexical variables.
* Implemented autocompletion, navigation and refactoring for global variables.
* Implemented basic project functions auto-completion, navigation and refactoring (works with obvious calls for now: package::method and package->method, auto-complete is pretty stupid)
* Implemented packages navigation and refactoring. For navigation to package files and refactoring, your lib root -I must be added to classroot.
* Implemented subs annotations: #@deprecated, #@returns, #@method and #@override. Annotations helps IDEA to understand your code properly and give you useful hints.
* Implemented basic code inspections. 
* Implemented IntelliLang integration for heredoc. Heredoc marker text now defines what language is inside the heredoc. In order to make this work, your IDEA should support necessary language directly or via plugin. Currently recognized markers:
  * JavaScript: JS, JS15, JS16, JS17, JS18, APPLEJS
  * Database: SQL, MYSQL, PGSQL, TSQL, OSQLP, DB2, SQL92, SQLITE, SYBASE, HSQLDB, GSQL, OSQL (Both Database navigator and JetBrains Database tools are supported)
  * Web-related: JSON, CSS, DTD, XHTML, XML, HTML
  * Misc: JAVA, YAML, MANIFEST, PHP, PYTHON
* Lexer and parser still have some bugs, see issue tracker and don't hesitate to report.
* Current, dev build you may find here: http://evstigneev.com/camelcade.jar

#See also

You may find Perl6 (HOLD) plugin in other repo: https://github.com/hurricup/Perl6-IDEA