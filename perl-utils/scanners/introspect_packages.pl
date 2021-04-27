use strict;
use warnings FATAL => 'all';
use v5.10;

my @packages = qw/
    Moose
    Moose::Role
    MooseX::MethodAttributes::Role
    MooseX::Role::WithOverloading
    MooseX::MethodAttributes
    Moose::Util::TypeConstraints
    MooseX::Types::CheckedUtilExports
    MooseX::ClassAttribute
    MooseX::Role::Parameterized
    Role::Tiny
    Moo
    Moo::Role
/;

say `cpanm @packages`;

while (my $package = shift @packages) {
    say `perl ./simple_introspector.pl $package`;
    say '---------------------------------------------------------' if @packages;
}

