package Moose::Exception::IsaLacksDoesMethod;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::InvalidAttributeOptions';

sub _build_message {
    my $self = shift;
    "Cannot have an isa option which cannot ->does() on attribute (".$self->attribute_name.")";
}

__PACKAGE__->meta->make_immutable;
1;
