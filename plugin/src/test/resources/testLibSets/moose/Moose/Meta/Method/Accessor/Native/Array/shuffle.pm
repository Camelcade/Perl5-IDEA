package Moose::Meta::Method::Accessor::Native::Array::shuffle;
our $VERSION = '2.2101';

use strict;
use warnings;

use List::Util ();

use Moose::Role;

with 'Moose::Meta::Method::Accessor::Native::Reader';

sub _maximum_arguments { 0 }

sub _return_value {
    my $self = shift;
    my ($slot_access) = @_;

    return 'List::Util::shuffle @{ (' . $slot_access . ') }';
}

no Moose::Role;

1;
