package Moose::Exception::MetaclassNotLoaded;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Class';

sub _build_message {
    my $self = shift;
    "The Metaclass ".$self->class_name." must be loaded. (Perhaps you forgot to 'use ".$self->class_name."'?)";
}

__PACKAGE__->meta->make_immutable;
1;
