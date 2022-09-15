package Moose::Exception::MethodNameNotFoundInInheritanceHierarchy;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Class';

has 'method_name' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

sub _build_message {
    my $self = shift;
    "The method '".$self->method_name."' was not found in the inheritance hierarchy for ".$self->class_name;
}

__PACKAGE__->meta->make_immutable;
1;
