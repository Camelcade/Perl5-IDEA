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
        description => 'test one'
    },
    Seption4 => {
        fields => 'testfield',
    },
    Seption5 => {
        alias  => 'aliasfor5',
        fields => [ qw/test1 test2/ ]
    };
sub mydump;

mydump($_) for qw/Seption1 Seption2  Seption3 Seption4 Seption5 Foo::Bar/;

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

