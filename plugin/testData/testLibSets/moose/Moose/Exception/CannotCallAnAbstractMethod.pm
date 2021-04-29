package Moose::Exception::CannotCallAnAbstractMethod;
our $VERSION = '2.2101';

use Moose;
extends 'Moose::Exception';

sub _build_message {
    "Abstract method";
}

__PACKAGE__->meta->make_immutable;
1;
