package Moose::Exception::DoesRequiresRoleName;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Class';

sub _build_message {
    "You must supply a role name to does()";
}

__PACKAGE__->meta->make_immutable;
1;
