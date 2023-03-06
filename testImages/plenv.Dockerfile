ARG PERL_VERSION=5.8.0
FROM perl:$PERL_VERSION
RUN \curl -L https://install.perlbrew.pl | bash
ARG PERL_VERSION
RUN bash -c 'git clone --depth 1 https://github.com/tokuhirom/plenv.git ~/.plenv || git -C $(realpath ~/.plenv) pull'
RUN bash -c 'git clone --depth 1 https://github.com/tokuhirom/Perl-Build.git ~/.plenv/plugins/perl-build/ || git -C $(realpath ~/.plenv/plugins/perl-build) pull'
RUN bash -c '~/.plenv/bin/plenv install $PERL_VERSION --noman -j 8' && rm -Rf ~/.plenv/cache ~./.plenv/build
ENV PLENV_VERSION=$PERL_VERSION
RUN bash -c ' ~/.plenv/bin/plenv install-cpanm'
ARG PERL_PACKAGES
RUN bash -c '~/.plenv/bin/plenv exec cpanm --notest $PERL_PACKAGES'