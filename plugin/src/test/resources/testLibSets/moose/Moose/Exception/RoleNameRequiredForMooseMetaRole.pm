package Moose::Exception::RoleNameRequiredForMooseMetaRole;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Role';

sub _build_message {
    "You must supply a role name to look for";
}

__PACKAGE__->meta->make_immutable;
1;
