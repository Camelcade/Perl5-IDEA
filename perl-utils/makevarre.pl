use strict;
use v5.10;

my %vars = ();

while( <> )
{
    chomp();
    s/^\s+|,\s*$//g;
    if( /^".(.+)"$/ )
    {
        $vars{$1} = 1;
    }
}

my @keys = keys %vars;

print join "|", map {"\"$_\""} sort {length $b <=> length $a || $a cmp $b} @keys;