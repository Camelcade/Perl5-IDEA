# Perl5 plugins for IntelliJ IDEA

[![Build Status](https://travis-ci.org/Camelcade/Perl5-IDEA.svg)](https://travis-ci.org/Camelcade/Perl5-IDEA) [![Coverage Status](https://coveralls.io/repos/github/Camelcade/Perl5-IDEA/badge.svg)](https://coveralls.io/github/Camelcade/Perl5-IDEA)

This repo contains a bundle of Perl5-related plugins for [JetBrains IDEs](https://www.jetbrains.com/). 

Current versions of plugins may be found tn [JetBrains repo](https://plugins.jetbrains.com/).

#### This repository contains following plugins:

- Perl plugin (`core`) - base plugin with perl & pod support and all basic features ([repo](https://plugins.jetbrains.com/plugin/7796-perl)).  
  - [Docker](https://www.docker.com/) Support (`transport.docker`) - allows to use interpreters from Docker containers ([repo](https://plugins.jetbrains.com/plugin/11328-perl-docker-support)).
  - [WSL](https://en.wikipedia.org/wiki/Windows_Subsystem_for_Linux) Support (`transport.wsl`) - allows to use interpreters from WSL (Windows Subsystem for Linux) ([repo](https://plugins.jetbrains.com/plugin/11329-perl-wsl-support)).
- [Template Toolkit](http://www.template-toolkit.org/) Support (`lang.tt2`) 
- [Mojolicious](https://mojolicious.org/) Support (`lang.mojo`) 
- Mason framework (`lang.mason.framework`) - core plugin of Mason plugins:
  - [HTML::Mason](https://metacpan.org/pod/HTML::Mason) Support (`lang.mason.htmlmason`) 
  - [Mason2](https://metacpan.org/pod/Mason) Support (`lang.mason.mason2`) 
- Embedded Perl Support (`lang.embedded`) - adds support for php-like perl syntax with `<?` and `?>` markers.

You can always build and install current development version of any plugin:

1. Clone this repo to your hard drive
2. Go to the repo directory
3. Run `./gradlew buildPlugin` (this builds all plugins from the repo)
4. In the plugins dialog of your IDE, use `Install plugin from disk...` option
5. Choose plugin file in the `build/distributions` directory of the respective plugin root  

If you want to participate in development, don't forget to read our [contribution policy](https://github.com/hurricup/Perl5-IDEA/wiki/Contribution-policy).

[![Donate to support Project](https://www.paypalobjects.com/en_US/GB/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=HJCUADZKY5G7E)

* [Camelcade twitter](https://twitter.com/CamelcadeIDE) - here you may find links to the most recent dev builds
* [Camelcade wiki](https://github.com/hurricup/Perl5-IDEA/wiki)
* [Gitter chat](https://gitter.im/IntelliJ-Plugin-Perl5/Lobby)
* [Google group](https://groups.google.com/forum/#!forum/camelcade)
* [Onion parsing comparision](http://evstigneev.com/onion_all.png)

YourKit supports open source projects with its full-featured <a href="https://www.yourkit.com/java/profiler/index.jsp">Java Profiler</a>.

[![Optimized using YourKit Java Profiler](https://www.yourkit.com/images/yklogo.png)](https://www.yourkit.com/java/profiler/index.jsp)

Wiki: https://github.com/hurricup/Perl5-IDEA/wiki
