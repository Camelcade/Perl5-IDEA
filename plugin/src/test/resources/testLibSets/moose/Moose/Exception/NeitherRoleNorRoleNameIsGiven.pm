package Moose::Exception::NeitherRoleNorRoleNameIsGiven;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';

sub _build_message {
    "You need to give role or role_name or both";
}

__PACKAGE__->meta->make_immutable;
1;
