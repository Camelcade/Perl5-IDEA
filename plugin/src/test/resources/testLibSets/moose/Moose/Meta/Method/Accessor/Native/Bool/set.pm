package Moose::Meta::Method::Accessor::Native::Bool::set;
our $VERSION = '2.2101';

use strict;
use warnings;

use Moose::Role;

with 'Moose::Meta::Method::Accessor::Native::Writer';

sub _maximum_arguments { 0 }

sub _potential_value { 1 }

sub _inline_optimized_set_new_value {
    my $self = shift;
    my ($inv, $new, $slot_access) = @_;

    return $slot_access . ' = 1;';
}

no Moose::Role;

1;
