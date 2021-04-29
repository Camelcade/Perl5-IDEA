package Moose::Exception::CannotRegisterUnnamedTypeConstraint;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';

sub _build_message {
    "can't register an unnamed type constraint";
}

__PACKAGE__->meta->make_immutable;
1;
