package Moose::Exception::CannotCreateMethodAliasLocalMethodIsPresent;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Role', 'Moose::Exception::Role::Method';

has 'role_being_applied_name' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

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
