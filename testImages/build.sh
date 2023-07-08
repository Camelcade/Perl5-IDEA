#!/bin/bash -ex
PERL_VERSION="5.38.0"

for tag in perl5 perlbrew plenv asdf
do
  docker build --progress plain --tag registry-1.docker.io/hurricup/camelcade-test:$tag-$PERL_VERSION \
    --build-arg PERL_VERSION=$PERL_VERSION \
    --build-arg PERL_PACKAGES='
  App::Prove::Plugin::PassEnv
  B::Debug
  Devel::Camelcadedb
  Devel::Cover
  Devel::NYTProf
  JSON
  Mojolicious
  Perl::Critic
  Perl::Tidy
  TAP::Formatter::Camelcade
  Types::Serialiser
' \
    --file $tag.Dockerfile .
  docker push registry-1.docker.io/hurricup/camelcade-test:$tag-$PERL_VERSION
done
