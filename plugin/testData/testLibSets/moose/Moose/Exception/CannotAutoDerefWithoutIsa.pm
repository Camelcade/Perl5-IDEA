package Moose::Exception::CannotAutoDerefWithoutIsa;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::InvalidAttributeOptions';

sub _build_message {
    my $self = shift;
    "You cannot auto-dereference without specifying a type constraint on attribute (".$self->attribute_name.")";
}

__PACKAGE__->meta->make_immutable;
1;
