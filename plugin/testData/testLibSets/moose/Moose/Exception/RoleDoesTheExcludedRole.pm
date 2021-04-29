package Moose::Exception::RoleDoesTheExcludedRole;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Role';

has 'excluded_role_name' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

has 'second_role_name' => (
    is       => 'ro',
    isa      => 'Str',
    required => 1
);

sub _build_message {
    my $self               = shift;
    my $role_name          = $self->role_name;
    my $excluded_role_name = $self->excluded_role_name;
    return "The role $role_name does the excluded role '$excluded_role_name'";
}

__PACKAGE__->meta->make_immutable;
1;
