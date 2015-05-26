#Perl5 plugin for IntellijIDEA - Camelcade

This is my attempt to build a plugin for IDEA that will support Perl5 language.

This project is currently in development, and if you want to participate - you may fork or contact me via skype: hurricup

#Current status - beta testing and development (0.14.1 beta)

* Implemented JFlex lexer and Grammar-kit for Perl and POD. They still have some bugs, see issue tracker.
* Implemented support for php-style perl <? ... ?>
* Implemented syntax highlighting for Perl and POD
* Implemented file types for Perl scripts and packages, POD files, XS files.
* Implemented autocompletion for built-in functions, packages and variables.
* Implemented project functions auto-completion 
* Implemented basic code-folding for blocks, hashes, arrays, PODs, heredocs and sequential comments and imports.
* Implemented crude defined subs, used packages and defined variables autocompletion.
* Implemented basic brace matcher
* Implemented jump to function declaration by click (works with obvious calls for now: package::method and package->method)
* Implemented methods re-factoring (same as above, use with CARE with VCS and extensive testing)
* Current, let's say, stable version, you may find in the camelcade.jar (ready for installation)

#See also

You may find Perl6 (HOLD) plugin in other repo: https://github.com/hurricup/Perl6-IDEA