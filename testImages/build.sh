#!/bin/bash -ex
PERL_VERSION="5.42.0"

for tag in perl5 perlbrew plenv asdf
do
  docker-buildx build \
    --progress plain \
    --platform linux/amd64 \
    --tag hurricup/camelcade-test:$tag-$PERL_VERSION \
    --output type=image \
    --output type=docker \
    --cache-to type=inline \
    --cache-from type=registry,ref=hurricup/camelcade-test:$tag-$PERL_VERSION \
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
  docker push hurricup/camelcade-test:$tag-$PERL_VERSION
done
