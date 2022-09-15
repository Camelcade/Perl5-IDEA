package Moose::Exception::MustSpecifyAtleastOneMethod;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Role';

sub _build_message {
    "Must specify at least one method";
}

__PACKAGE__->meta->make_immutable;
1;
