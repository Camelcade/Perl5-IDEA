package Moose::Exception::CannotAddAsAnAttributeToARole;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Role';

has 'attribute_class' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1,
);

sub _build_message {
    my $self = shift;
    "Cannot add a ".$self->attribute_class." as an attribute to a role";
}

__PACKAGE__->meta->make_immutable;
1;
