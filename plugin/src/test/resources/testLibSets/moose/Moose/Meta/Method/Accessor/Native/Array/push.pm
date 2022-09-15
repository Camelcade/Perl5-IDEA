package Moose::Meta::Method::Accessor::Native::Array::push;
our $VERSION = '2.2101';

use strict;
use warnings;

use Moose::Role;

with 'Moose::Meta::Method::Accessor::Native::Array::Writer';

sub _adds_members { 1 }

sub _potential_value {
    my $self = shift;
    my ($slot_access) = @_;

    return '[ @{ (' . $slot_access . ') }, @_ ]';
}

sub _inline_optimized_set_new_value {
    my $self = shift;
    my ($inv, $new, $slot_access) = @_;

    return 'push @{ (' . $slot_access . ') }, @_;';
}

sub _return_value {
    my $self = shift;
    my ($slot_access) = @_;

    return 'scalar @{ (' . $slot_access . ') }';
}

no Moose::Role;

1;
