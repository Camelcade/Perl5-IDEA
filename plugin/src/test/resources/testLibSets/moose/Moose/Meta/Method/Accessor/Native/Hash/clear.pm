package Moose::Meta::Method::Accessor::Native::Hash::clear;
our $VERSION = '2.2101';

use strict;
use warnings;

use Moose::Role;

with 'Moose::Meta::Method::Accessor::Native::Hash::Writer';

sub _maximum_arguments { 0 }

sub _adds_members { 0 }

# The inner () in this expression is for the benefit of inlining code that
# might end up looking like "values %{ {} }". This is a syntax error in perl
# but 'values %{ { () } }' is not.
sub _potential_value { '{ ( ) }' }

# There are no new members so we don't need to coerce new values (none exist)
# and we always want to check the new (empty) hash as a whole.
sub _inline_coerce_new_values { '' }

sub _check_new_members_only { 0 }

sub _inline_optimized_set_new_value {
    my $self = shift;
    my ($inv, $new, $slot_access) = @_;

    return $slot_access . ' = {};';
}

sub _return_value { '' }

no Moose::Role;

1;
