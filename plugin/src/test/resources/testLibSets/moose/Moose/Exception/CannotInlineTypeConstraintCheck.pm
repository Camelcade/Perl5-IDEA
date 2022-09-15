package Moose::Exception::CannotInlineTypeConstraintCheck;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::TypeConstraint';

sub _build_message {
    my $self = shift;
    'Cannot inline a type constraint check for ' . $self->type_name;
}

__PACKAGE__->meta->make_immutable;
1;
