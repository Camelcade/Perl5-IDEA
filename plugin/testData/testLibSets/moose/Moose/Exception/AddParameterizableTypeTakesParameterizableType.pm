package Moose::Exception::AddParameterizableTypeTakesParameterizableType;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';

has 'type_name' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

sub _build_message {
    my $self = shift;
    "Type must be a Moose::Meta::TypeConstraint::Parameterizable not ".$self->type_name;
}

__PACKAGE__->meta->make_immutable;
1;
