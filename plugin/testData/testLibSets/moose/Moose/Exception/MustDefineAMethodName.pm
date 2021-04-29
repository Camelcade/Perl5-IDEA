package Moose::Exception::MustDefineAMethodName;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';
with 'Moose::Exception::Role::Instance';

sub _build_message {
    "You must define a method name";
}

__PACKAGE__->meta->make_immutable;
1;
