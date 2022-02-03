FROM perl:5.32.0
RUN cpan install App::cpanminus
RUN cpanm --notest Mojolicious Devel::Camelcadedb Devel::Cover JSON App::Prove::Plugin::PassEnv TAP::Formatter::Camelcade Devel::NYTProf Perl::Tidy Perl::Critic
