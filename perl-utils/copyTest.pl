#!/usr/bin/perl
use strict;
use warnings FATAL => 'all';
use v5.10;

for my $file (grep {/hash/i} <*.code>) {
    my $target = $file;
    $target =~ s/hash/array/;
    $target =~ s/Hash/Array/;
    say "$file => $target";
    say `cp $file $target`;
}
