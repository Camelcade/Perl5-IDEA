#Perl5 plugin for IntellijIDEA - Camelcade

This is my attempt to build a plugin for IDEA that will support Perl5 language.

This project is currently in development, and if you want to participate - you may fork or contact me via skype: hurricup

#Need testers
There is tremendous amount of work to do in development and project really need testers. Not only users-testers, but someone
to write tests (mostly for lexer and parser at the moment).

#Current status - development (0.19 beta)

* Implemented JFlex lexer and Grammar-kit for Perl and POD. They still have some bugs, see issue tracker.
* Implemented support for php-style perl <? ... ?>
* Implemented syntax highlighting for Perl and POD
* Implemented autocompletion for built-in functions, packages and variables.
* Implemented autocompletion, navigation and refactoring for lexical variables.
* Implemented project functions auto-completion (pretty dumb for now). 
* Implemented basic project functions navigation and refactoring (works with obvious calls for now: package::method and package->method)
* Implemented lexical variables navigation and refactoring.
* Implemented packages navigation and refactoring. For navigation to package files and refactoring, your lib root -I must be explicitly marked as one of the source roots.
* Implemented basic code-folding for blocks, hashes, arrays, PODs, heredocs and sequential comments and imports.
* Implemented basic brace matcher.
* Implemented find usage. 
* Current, dev build you may find here: http://evstigneev.com/camelcade.jar

#See also

You may find Perl6 (HOLD) plugin in other repo: https://github.com/hurricup/Perl6-IDEA