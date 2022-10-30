ARG PERL_VERSION=5.8.0
FROM perl:$PERL_VERSION
RUN cpan install App::cpanminus
RUN cpanm --notest Mojolicious Devel::Camelcadedb Devel::Cover JSON App::Prove::Plugin::PassEnv TAP::Formatter::Camelcade Devel::NYTProf Perl::Tidy Perl::Critic
