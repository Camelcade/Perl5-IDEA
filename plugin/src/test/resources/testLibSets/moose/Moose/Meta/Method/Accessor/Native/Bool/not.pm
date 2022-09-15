package Moose::Meta::Method::Accessor::Native::Bool::not;
our $VERSION = '2.2101';

use strict;
use warnings;

use Moose::Role;

with 'Moose::Meta::Method::Accessor::Native::Reader';

sub _maximum_arguments { 0 }

sub _return_value {
    my $self = shift;
    my ($slot_access) = @_;

    return '!' . $slot_access;
}

1;
