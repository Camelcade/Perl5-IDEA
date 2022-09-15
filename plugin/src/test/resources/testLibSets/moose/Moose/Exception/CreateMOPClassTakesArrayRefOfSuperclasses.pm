package Moose::Exception::CreateMOPClassTakesArrayRefOfSuperclasses;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::RoleForCreateMOPClass';

sub _build_message {
    "You must pass an ARRAY ref of superclasses";
}

__PACKAGE__->meta->make_immutable;
1;
