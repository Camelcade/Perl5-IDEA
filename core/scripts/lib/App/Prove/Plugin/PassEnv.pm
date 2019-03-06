package App::Prove::Plugin::PassEnv;
{
    $App::Prove::Plugin::PassEnv::VERSION = '0.001';
}

# ABSTRACT: a prove plugin to pass environment variables

use strict;
use warnings;

sub load {
    my ($class, $p) = @_;
    foreach my $key (keys %ENV) {
        if ($key =~ /^PROVE_PASS_(.+)$/) {
            $ENV{$1} = $ENV{$key};

        }
    }
}

1;