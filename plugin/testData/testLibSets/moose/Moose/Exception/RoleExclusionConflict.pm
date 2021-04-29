package Moose::Exception::RoleExclusionConflict;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Role';

has 'roles' => (
    is         => 'ro',
    isa        => 'ArrayRef',
    required   => 1,
);

sub _build_message {
    my $self = shift;

    my @roles_array = @{$self->roles};
    my $role_noun = "Role".( @roles_array == 1 ? '' : 's');
    my $all_roles = join(', ', @roles_array);
    my $verb = "exclude".( @roles_array == 1 ? 's' : '' );
    my $role_name = $self->role_name;

    return "Conflict detected: $role_noun $all_roles $verb role '$role_name'";
}

__PACKAGE__->meta->make_immutable;
1;
