package Moose::Exception::InvalidTypeConstraint;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';

has 'registry_object' => (
    is       => 'ro',
    isa      => 'Moose::Meta::TypeConstraint::Registry',
    required => 1
);

has 'type' => (
    is       => 'ro',
    isa      => 'Any',
    required => 1
);

sub _build_message {
    return "No type supplied / type is not a valid type constraint";
}

__PACKAGE__->meta->make_immutable;
1;
