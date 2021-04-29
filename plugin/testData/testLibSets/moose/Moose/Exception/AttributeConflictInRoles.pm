package Moose::Exception::AttributeConflictInRoles;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Role';

has 'second_role_name' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

has 'attribute_name' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

sub _build_message {
    my $self             = shift;
    my $role_name        = $self->role_name;
    my $second_role_name = $self->second_role_name;
    my $attribute_name   = $self->attribute_name;
    "Role '$role_name' has encountered an attribute conflict"
    . " while being composed into '$second_role_name'."
    . " This is a fatal error and cannot be disambiguated."
    . " The conflicting attribute is named '$attribute_name'.";
}

__PACKAGE__->meta->make_immutable;
1;
