package Moose::Exception::MustSpecifyAtleastOneRole;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Role';

sub _build_message {
    "Must specify at least one role";
}

__PACKAGE__->meta->make_immutable;
1;
