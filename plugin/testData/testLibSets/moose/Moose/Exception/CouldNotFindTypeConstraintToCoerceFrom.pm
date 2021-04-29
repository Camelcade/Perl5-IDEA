package Moose::Exception::CouldNotFindTypeConstraintToCoerceFrom;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Instance';

has 'constraint_name' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

sub _build_message {
    my $self = shift;
    "Could not find the type constraint (".$self->constraint_name.") to coerce from";
}

__PACKAGE__->meta->make_immutable;
1;
