package Moose::Exception::MetaclassIsAClassNotASubclassOfGivenMetaclass;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Class';

use Moose::Util 'find_meta';

has 'metaclass' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

sub _build_message {
    my $self = shift;
    my $class = find_meta( $self->class_name );
    $self->class_name." already has a metaclass, but it does not inherit ".$self->metaclass.
        " ($class). You cannot make the same thing a role and a class. Remove either Moose or Moose::Role.";
}

__PACKAGE__->meta->make_immutable;
1;
