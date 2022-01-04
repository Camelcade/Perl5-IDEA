# Perl5 plugins for IntelliJ IDEA

[![Java CI with Gradle](https://github.com/Camelcade/Perl5-IDEA/actions/workflows/gradle.yml/badge.svg?branch=master)](https://github.com/Camelcade/Perl5-IDEA/actions/workflows/gradle.yml?query=branch%3Amaster)
[![Coverage Status](https://coveralls.io/repos/github/Camelcade/Perl5-IDEA/badge.svg?branch=master)](https://coveralls.io/github/Camelcade/Perl5-IDEA?branch=master)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=Camelcade_Perl5-IDEA&metric=bugs)](https://sonarcloud.io/summary/new_code?id=Camelcade_Perl5-IDEA)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=Camelcade_Perl5-IDEA&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=Camelcade_Perl5-IDEA)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=Camelcade_Perl5-IDEA&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=Camelcade_Perl5-IDEA)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=Camelcade_Perl5-IDEA&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=Camelcade_Perl5-IDEA)

[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Camelcade_Perl5-IDEA&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=Camelcade_Perl5-IDEA)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=Camelcade_Perl5-IDEA&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=Camelcade_Perl5-IDEA)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=Camelcade_Perl5-IDEA&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=Camelcade_Perl5-IDEA)

[Support with PayPal](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=HJCUADZKY5G7E)
|
[Support with Yandex.Money](https://money.yandex.ru/to/41001227135087)

This repo contains a bundle of Perl5-related plugins for [JetBrains IDEs](https://www.jetbrains.com/).

Current versions of plugins may be found in [JetBrains repo](https://plugins.jetbrains.com/).

#### This repository contains following plugins:

- Perl plugin (`plugin`) - base plugin with perl & pod support and all basic
  features ([repo](https://plugins.jetbrains.com/plugin/7796-perl)).
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

* [Camelcade twitter](https://twitter.com/CamelcadeIDE) - here you may find links to the most recent dev builds
* [Camelcade wiki](https://github.com/hurricup/Perl5-IDEA/wiki)
* [Discussions](https://github.com/Camelcade/Perl5-IDEA/discussions)

YourKit supports open source projects with its full-featured <a href="https://www.yourkit.com/java/profiler/index.jsp">Java Profiler</a>.

[![Optimized using YourKit Java Profiler](https://www.yourkit.com/images/yklogo.png)](https://www.yourkit.com/java/profiler/index.jsp)

Wiki: https://github.com/hurricup/Perl5-IDEA/wiki
