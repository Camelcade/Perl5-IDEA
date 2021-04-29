package Moose::Exception::MustDefineAnAttributeName;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Class';

sub _build_message {
    "You must define an attribute name";
}

__PACKAGE__->meta->make_immutable;
1;
