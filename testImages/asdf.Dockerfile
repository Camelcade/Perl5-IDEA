ARG PERL_VERSION=5.8.0
FROM perl:$PERL_VERSION
RUN git clone --depth 1 https://github.com/asdf-vm/asdf.git ~/.asdf --branch v0.11.2
RUN ~/.asdf/bin/asdf plugin add perl
ARG PERL_VERSION
RUN bash -c '~/.asdf/bin/asdf install perl $PERL_VERSION --noman -j 8'
ENV ASDF_PERL_VERSION=$PERL_VERSION
RUN bash -c ' ~/.asdf/bin/asdf exec cpan install App::cpanminus'
ARG PERL_PACKAGES
RUN bash -c '~/.asdf/bin/asdf exec cpanm --notest $PERL_PACKAGES'
RUN bash -c '~/.asdf/bin/asdf reshim perl
