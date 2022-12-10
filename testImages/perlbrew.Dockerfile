ARG PERL_VERSION=5.8.0
FROM perl:$PERL_VERSION
RUN \curl -L https://install.perlbrew.pl | bash
ARG PERL_VERSION
RUN bash -c 'source ~/perl5/perlbrew/etc/bashrc && \
    perlbrew install-cpanm && \
    perlbrew install --verbose perl-$PERL_VERSION --notest --noman && \
    perlbrew clean'
ARG PERL_PACKAGES
RUN bash -c 'source ~/perl5/perlbrew/etc/bashrc && \
    perlbrew exec -q --with perl-$PERL_VERSION cpan App::cpanminus && \
    perlbrew lib create perl-$PERL_VERSION@plugin_test || true  && \
    perlbrew exec -q --with perl-$PERL_VERSION@plugin_test cpanm --notest $PERL_PACKAGES'