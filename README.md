# Perl5 plugins for IntelliJ IDEA

Blah blah blah

[![Project Validation](https://github.com/Camelcade/Perl5-IDEA/actions/workflows/push_and_pr.yml/badge.svg)](https://github.com/Camelcade/Perl5-IDEA/actions/workflows/push_and_pr.yml)
[![Coverage Status](https://coveralls.io/repos/github/Camelcade/Perl5-IDEA/badge.svg?branch=master&dummy)](https://coveralls.io/github/Camelcade/Perl5-IDEA?branch=master)

[![Downloads](https://img.shields.io/jetbrains/plugin/d/7796)](https://plugins.jetbrains.com/plugin/7796-perl)
[![Rating](https://img.shields.io/jetbrains/plugin/r/rating/7796)](https://plugins.jetbrains.com/plugin/7796-perl/reviews)
[![Version](https://img.shields.io/jetbrains/plugin/v/7796)](https://plugins.jetbrains.com/plugin/7796-perl/versions)

[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Camelcade_Perl5-IDEA&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=Camelcade_Perl5-IDEA)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=Camelcade_Perl5-IDEA&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=Camelcade_Perl5-IDEA)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=Camelcade_Perl5-IDEA&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=Camelcade_Perl5-IDEA)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=Camelcade_Perl5-IDEA&metric=bugs)](https://sonarcloud.io/summary/new_code?id=Camelcade_Perl5-IDEA)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=Camelcade_Perl5-IDEA&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=Camelcade_Perl5-IDEA)

This repo contains a bundle of Perl5-related plugins for [JetBrains IDEs](https://www.jetbrains.com/).

Current versions of plugins may be found in the JetBrains
repo: [Stable builds](https://plugins.jetbrains.com/plugin/7796-perl/versions/stable)
or [EAP Builds](https://plugins.jetbrains.com/plugin/7796-perl/versions/eap).

In order to get updates to EAP builds from the IDE, you need to add the EAP repository to the list of plugin
repositories: `https://plugins.jetbrains.com/plugins/eap/list`

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
