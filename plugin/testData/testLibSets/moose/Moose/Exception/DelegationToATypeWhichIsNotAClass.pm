package Moose::Exception::DelegationToATypeWhichIsNotAClass;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Attribute';

sub _build_message {
    my $self = shift;
    "The ".$self->attribute->name." attribute is trying to delegate to a type (".$self->attribute->type_constraint->name.") that is not backed by a class";
}

__PACKAGE__->meta->make_immutable;
1;
