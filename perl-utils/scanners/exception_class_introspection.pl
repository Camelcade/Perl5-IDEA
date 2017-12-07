package Foo::Bar;
use strict;
use warnings;
use Data::Dumper;
use v5.10;

use Exception::Class
    'Seption1',
    Seption2 => {
        isa => 'Seption1',
    },
    Seption3 => {
        description => 'test one',
        isa         => 'Seption2'
    },
    Seption4 => {
        fields => 'testfield',
        isa    => 'Seption3'
    },
    Seption5 => {
        alias  => 'aliasfor5',
        ias    => 'Seption4',
        fields => [ qw/test1 test2/ ]
    },
    Seption6 => {
        qw/
            alias   aliasfor6
                isa     Seption5
                fields  somefield
                description some\description
            /
    }
;

sub mydump;

mydump($_) for qw/Seption1 Seption2  Seption3 Seption4 Seption5 Seption6 Foo::Bar/;

aliasfor5();
#Seption4->Fields();

sub mydump{
    my $ns = shift;

    say "Dumping $ns";

    no strict 'refs';
    for my $name (keys %{$ns."::"}) {
        say "  $name:";
        for my $slot (qw/ARRAY HASH CODE/) {
            if (defined *{"${ns}::${name}"}{$slot}) {
                say "    $slot";
            }
        }
    }
}

