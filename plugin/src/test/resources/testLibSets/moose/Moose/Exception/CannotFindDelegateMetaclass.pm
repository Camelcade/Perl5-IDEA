package Moose::Exception::CannotFindDelegateMetaclass;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Attribute';

sub _build_message {
    my $self = shift;
    "Cannot find delegate metaclass for attribute ".$self->attribute->name;
}

__PACKAGE__->meta->make_immutable;
1;
