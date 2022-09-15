package Moose::Exception::NoAttributeFoundInSuperClass;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Class', 'Moose::Exception::Role::InvalidAttributeOptions';

sub _build_message {
    my $self = shift;
    "Could not find an attribute by the name of '".$self->attribute_name."' to inherit from in ".$self->class_name;
}

__PACKAGE__->meta->make_immutable;
1;
