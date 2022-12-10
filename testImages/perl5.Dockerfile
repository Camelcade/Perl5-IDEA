ARG PERL_VERSION=5.8.0
FROM perl:$PERL_VERSION
RUN cpan install App::cpanminus
ARG PERL_PACKAGES="not set"
RUN cpanm --notest $PERL_PACKAGES
