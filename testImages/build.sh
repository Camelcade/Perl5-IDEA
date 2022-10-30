#!/bin/bash -e

PERL_VERSION="5.32.0"
docker build --tag hurricup/camelcade-test:perl5-$PERL_VERSION --build-arg PERL_VERSION=$PERL_VERSION --file perl5.Dockerfile .
docker push hurricup/camelcade-test:perl5-$PERL_VERSION