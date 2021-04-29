package Moose::Exception::CannotAutoDereferenceTypeConstraint;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Attribute', 'Moose::Exception::Role::Instance', 'Moose::Exception::Role::TypeConstraint';

sub _build_message {
    my $self = shift;
    "Can not auto de-reference the type constraint '" . $self->type_name . "'";
}

__PACKAGE__->meta->make_immutable;
1;
