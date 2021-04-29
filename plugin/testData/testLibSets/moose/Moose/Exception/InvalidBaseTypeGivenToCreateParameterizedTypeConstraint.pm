package Moose::Exception::InvalidBaseTypeGivenToCreateParameterizedTypeConstraint;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::TypeConstraint';

sub _build_message {
    my $self = shift;
    "Could not locate the base type (".$self->type_name.")";
}

__PACKAGE__->meta->make_immutable;
1;
