package MyPackage;
use strict;
use warnings FATAL => 'all';
use v5.10;

use Carp::Always;
use Test::More qw/ok like/;

use Sub::Identify qw//;

say 'List<String> EXPORT = Arrays.asList(';
foreach my $key (sort keys %::MyPackage::) {
    my $coderef = *{$::MyPackage::{$key}}{CODE};
    if ($coderef) {
        my $target_name = Sub::Identify::sub_fullname($coderef);
        if ($target_name =~ /^Test/) {
            say sprintf '"%s",', $key;
        }
    }
}
say ');'
