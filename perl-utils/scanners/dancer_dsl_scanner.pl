#!/usr/bin/perl
use strict;
use warnings FATAL => 'all';
package MyPackage;
use v5.10;
use Dancer;
use Sub::Identify qw//;

foreach my $key (sort keys %::MyPackage::)
{
    my $coderef = *{$::MyPackage::{$key}}{CODE};
    if ($coderef) {
        my $target_name = Sub::Identify::sub_fullname( $coderef );
        if ($target_name =~ /^Dancer/)
        {
            say sprintf '"%s",', $key;
        }
    }
}