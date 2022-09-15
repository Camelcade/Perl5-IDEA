package Moose::Exception::CanExtendOnlyClasses;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Role';

sub _build_message {
    my $self      = shift;
    my $role_name = $self->role_name;
    return "You cannot inherit from a Moose Role ($role_name)";
}

__PACKAGE__->meta->make_immutable;
1;
