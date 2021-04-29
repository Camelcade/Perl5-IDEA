package Moose::Meta::Method::Accessor::Native::Hash::delete;
our $VERSION = '2.2101';

use strict;
use warnings;

use Moose::Role;

with 'Moose::Meta::Method::Accessor::Native::Hash::Writer';

sub _adds_members { 0 }

# There are no new members so we don't need to coerce new values (none exist)
# and we always want to check the new (empty) hash as a whole.
sub _inline_coerce_new_values { '' }

sub _check_new_members_only { 0 }

sub _potential_value {
    my $self = shift;
    my ($slot_access) = @_;

    return '(do { '
             . 'my %potential = %{ (' . $slot_access . ') }; '
             . '@return = delete @potential{@_}; '
             . '\%potential; '
         . '})';
}

sub _inline_optimized_set_new_value {
    my $self = shift;
    my ($inv, $new, $slot_access) = @_;

    return '@return = delete @{ (' . $slot_access . ') }{@_};';
}

sub _return_value {
    my $self = shift;
    my ($slot_access) = @_;

    return 'wantarray ? @return : $return[-1]';
}

no Moose::Role;

1;
