package Moose::Exception::AddRoleTakesAMooseMetaRoleInstance;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Class';

has 'role_to_be_added' => (
    is       => 'ro',
    isa      => 'Any',
    required => 1,
);

sub _build_message {
    "Roles must be instances of Moose::Meta::Role";
}

__PACKAGE__->meta->make_immutable;
1;
