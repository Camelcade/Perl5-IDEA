package Test::Namespace;
use strict;
use warnings FATAL => 'all';
use v5.10;
use Sub::Identify;

# put the module you are introspecting here:
use Moose;

say 'Parents:';
our @ISA;
say "\t$_" for @ISA;
say "";

say 'Subs:';
no strict 'refs';
for my $sub (sort keys %Test::Namespace::) {
    if (my $coderef = *{__PACKAGE__ . "::$sub"}{CODE}) {
        my $fqn = Sub::Identify::sub_fullname $coderef;
        my ($file, $line) = Sub::Identify::get_code_location $coderef;
        $file //= 'undefined';
        $line //= 'undefined';
        say "\t$sub\t=>\t$fqn ($file, $line)";
    }
}
