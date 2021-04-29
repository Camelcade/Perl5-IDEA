package Moose::Exception::CannotCreateMethodAliasLocalMethodIsPresentInClass;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Role', 'Moose::Exception::Role::Method', 'Moose::Exception::Role::Class';

has 'aliased_method_name' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

sub _build_message {
    "Cannot create a method alias if a local method of the same name exists";
}

__PACKAGE__->meta->make_immutable;
1;
